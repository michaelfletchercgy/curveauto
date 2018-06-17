package curveauto;

import curveauto.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ratpack.handling.Context;
import ratpack.server.RatpackServer;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static ratpack.jackson.Jackson.fromJson;
import static ratpack.jackson.Jackson.json;

public class Main {
    public static void main(String[] args) throws Exception {
        new Main().startup();
    }



    private void startup() throws Exception {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(CarMaintenance.class);
        configuration.addAnnotatedClass(CarType.class);
        configuration.addAnnotatedClass(CarTypeMaintenance.class);
        configuration.addAnnotatedClass(MaintenanceType.class);

        // Postgres Basic Configuration
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgresPlusDialect");

        // For quick development purposes.  IRL these would not be on.
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.pool_size", "1");

        // Configuration
        String url = System.getenv("DATABASE_URL");
        String username = System.getenv("DATABASE_USERNAME");
        String password = System.getenv("DATABASE_PASSWORD");

        if (url == null || username == null || password == null) {
            System.err.println("You must set the environment variables DATABASE_URL, DATABASE_USERNAME and DATABASE_PASSWORD.");
            System.exit(1);
        }

        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);

        SessionFactory sessionFactory = configuration.buildSessionFactory();


        HibernateDataAccess testData = new HibernateDataAccess(sessionFactory.openSession());
        testData.createTestData();
        testData.commit();

        Path web = new File("web/").getAbsoluteFile().toPath();

        RatpackServer.start(server -> server
                .serverConfig(serverConfig -> serverConfig.baseDir(web))
                .handlers(chain -> chain
                        .path("api/cars", ctx -> ctx.byMethod(m -> m
                                .get(() -> genericList(ctx, sessionFactory, this::carsList))
                                .put(() -> genericPut(ctx, sessionFactory, Car.class, this::carsPut)))
                        )
                        .path("api/cars/:id", ctx -> ctx.byMethod(m -> m
                            .get(() -> genericScopedGet(ctx, "id", sessionFactory, this::carsGet))
                            .post(() -> genericScopedPost(ctx, "id", Car.class, sessionFactory, this::carsPost))
                            .delete(() -> genericScopedDelete(ctx, "id", sessionFactory, this::carsDelete)))
                        )
                        .files(f -> f.indexFiles("index.html"))
                    )
                );
    }

    private <T> void genericList(Context httpRequestCtx, SessionFactory sessionFactory, Function<DataAccess, List<T>> businessHandler) {
        DataAccess da = new HibernateDataAccess(sessionFactory.openSession());
        List<T> list = businessHandler.apply(da);
        httpRequestCtx.render(json(list));
        da.commit();
    }

    private <T> void genericPut(Context httpRequestCtx, SessionFactory sessionFactory, Class<T> clazz, BiFunction<T, DataAccess, T> businessHandler) {
        httpRequestCtx.parse(fromJson(clazz))
                .then(putValue -> {
                    DataAccess da = new HibernateDataAccess(sessionFactory.openSession());
                    T newValue = businessHandler.apply(putValue, da);
                    httpRequestCtx.render(json(newValue));
                    da.commit();
                });
    }

    private <T> void genericScopedGet(Context httpRequestCtx, String scopeParameterName, SessionFactory sessionFactory, BiFunction<String, DataAccess, T> businessHandler) {
        String scopeParameterValue = httpRequestCtx.getPathTokens().get(scopeParameterName);
        DataAccess da = new HibernateDataAccess(sessionFactory.openSession());
        T result = businessHandler.apply(scopeParameterValue, da);
        httpRequestCtx.render(json(result));
        da.commit();
    }

    private <T> void genericScopedDelete(Context httpRequestCtx, String scopeParameterName, SessionFactory sessionFactory, BiConsumer<String, DataAccess> businessHandler) {
        String scopeParameterValue = httpRequestCtx.getPathTokens().get(scopeParameterName);
        DataAccess da = new HibernateDataAccess(sessionFactory.openSession());
        businessHandler.accept(scopeParameterValue, da);
        httpRequestCtx.render("ok");
        da.commit();
    }

    private <T> void genericScopedPost(Context httpRequestCtx, String scopeParameterName, Class<T> clazz, SessionFactory sessionFactory, TriFunction<String, T, DataAccess, T> businessHandler) {
        String scopeParameterValue = httpRequestCtx.getPathTokens().get(scopeParameterName);

        httpRequestCtx
                .parse(fromJson(clazz))
                .then(putValue -> {
                    DataAccess da = new HibernateDataAccess(sessionFactory.openSession());
                    T newValue = businessHandler.apply(scopeParameterValue, putValue, da);

                    httpRequestCtx.render(json(newValue));
                    da.commit();
                });
    }

    private List<Car> carsList(DataAccess da) {
        return da.getAllCars();
    }

    private Car carsPut(Car car, DataAccess da) {
        if (car.getId() != 0) {
            throw new RuntimeException();
        }

        return da.save(car);
    }

    private Car carsGet(String id, DataAccess da) {
        return da.getCar(id);
    }

    private Car carsPost(String id, Car car, DataAccess da) {
        car.setId(Long.valueOf(id));

        return da.save(car);
    }

    private Object carsDelete(String id, DataAccess da) {
        return da.deleteCar(Long.valueOf(id));
    }
}
