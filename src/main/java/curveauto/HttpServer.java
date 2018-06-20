package curveauto;

import curveauto.da.DataAccessFactory;
import curveauto.model.Car;
import curveauto.da.DataAccess;
import curveauto.model.CarMaintenance;
import curveauto.model.MaintenanceType;
import ratpack.handling.Context;
import ratpack.server.RatpackServer;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static ratpack.jackson.Jackson.fromJson;
import static ratpack.jackson.Jackson.json;

public class HttpServer {
    private DataAccessFactory dataAccessFactory;
    private RatpackServer server;

    public HttpServer(DataAccessFactory dataAccessFactory, API api, int port) throws Exception {
        this.dataAccessFactory = dataAccessFactory;

        Path web = new File("web/").getAbsoluteFile().toPath();

        server = RatpackServer.of(server -> server
                .serverConfig(serverConfig -> serverConfig.baseDir(web).port(port))
                .handlers(chain -> chain
                        .path("api/cars", ctx -> ctx.byMethod(m -> m
                                .get(() -> genericList(ctx, api::carsList))
                                .put(() -> genericPut(ctx, Car.class, api::carsPut)))
                        )
                        .path("api/cars/:id", ctx -> ctx.byMethod(m -> m
                                .get(() -> genericScopedGet(ctx, ctx.getPathTokens().get("id"), api::carsGet))
                                .post(() -> genericScopedPost(ctx, ctx.getPathTokens().get("id"), Car.class, api::carsPost))
                                .delete(() -> genericScopedDelete(ctx, ctx.getPathTokens().get("id"), api::carsDelete)))
                        )
                        .path("api/cars/:id/maintenance", ctx -> ctx.byMethod(m ->
                                m.get( () -> genericScopedList(ctx, ctx.getPathTokens().get("id"), api::carMaintenanceList)))
                        )
                        .path("api/carMaintenance/:id", ctx -> ctx.byMethod(m ->
                                m.delete(() -> genericScopedDelete(ctx, ctx.getPathTokens().get("id"), api::carMaintenanceDelete)))
                        )
                        .path("api/carMaintenance", ctx -> ctx.byMethod(m ->
                                m.put(() -> genericPut(ctx, CarMaintenance.class, api::addCarMaintenance)))
                        )
                        .path("api/carTypes", ctx -> ctx.byMethod(m -> m
                                .get(() -> genericList(ctx, api::carsTypeList)))
                        )
                        .path("api/maintenanceTypes", ctx -> ctx.byMethod(m -> m
                                .get(() -> genericList(ctx, api::maintenanceTypesList))
                                .put(() -> genericPut(ctx, MaintenanceType.class, api::maintenanceTypesPut)))
                        )
                        .path("api/maintenanceTypes/:id", ctx -> ctx.byMethod(m -> m
                                .get(() -> genericScopedGet(ctx, ctx.getPathTokens().get("id"), api::maintenanceTypesGet))
                                .post(() -> genericScopedPost(ctx, ctx.getPathTokens().get("id"), MaintenanceType.class, api::maintenanceTypesPost))
                                .delete(() -> genericScopedDelete(ctx, ctx.getPathTokens().get("id"), api::maintenanceTypesDelete)))
                        )
                        .files(f -> f.indexFiles("index.html"))
                )
        );
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    private <T> T withDataAccess(Function<DataAccess, T> fn) {
        DataAccess da = dataAccessFactory.create();
        try {
            T result = fn.apply(da);

            da.commit();

            return result;
        } catch(Exception e) {
            da.rollback();

            throw e;
        } finally {
            da.close();
        }
    }

    private <T> void genericList(Context ctx, Function<DataAccess, List<T>> apiFn) {
        List<T> result = withDataAccess(da -> apiFn.apply(da));

        ctx.render(json(result));
    }

    private <T> void genericPut(Context ctx, Class<T> clazz, BiFunction<T, DataAccess, T> apiFn) {
        ctx.parse(fromJson(clazz))
                .then(putValue -> {
                    try {
                        T result = withDataAccess(da -> apiFn.apply(putValue, da));
                        ctx.render(json(result));
                    } catch (RuntimeException e) {
                        ctx.render(json(ErrorJson.fromException(e)));
                    }

                });
    }

    private <T> void genericScopedList(Context ctx, String pathParameter, BiFunction<String, DataAccess, List<T>> apiFn) {
        List<T> result = withDataAccess(da -> apiFn.apply(pathParameter, da));

        if (result == null) {
            ctx.getResponse().status(404).send("not found");
        } else {
            ctx.render(json(result));
        }
    }

    private <T> void genericScopedGet(Context ctx, String pathParameter, BiFunction<String, DataAccess, T> apiFn) {
        T result = withDataAccess(da -> apiFn.apply(pathParameter, da));
        if (result == null) {
            ctx.getResponse().status(404).send("not found");
        } else {
            ctx.render(json(result));
        }
    }

    private <T> void genericScopedDelete(Context ctx, String pathParameter, BiConsumer<String, DataAccess> apiFn) {
        String result = withDataAccess(da -> {
            apiFn.accept(pathParameter, da);
            return "ok";
        });

        ctx.render(result);

    }

    private <T> void genericScopedPost(Context ctx, String pathParameter, Class<T> clazz, TriFunction<String, T, DataAccess, T> apiFn) {
        ctx.parse(fromJson(clazz))
                .then(putValue -> {
                    T newValue = withDataAccess(da -> apiFn.apply(pathParameter, putValue, da));
                    ctx.render(json(newValue));
                });
    }

    /**
     * This is for testing only.
     * @return The RatpackServer
     */
    public RatpackServer getServer() {
        return server;
    }

    @FunctionalInterface
    interface TriFunction<A,B,C,R> {

        R apply(A a, B b, C c);
    }

    private static class ErrorJson {
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public static ErrorJson fromException(Exception e) {
            ErrorJson err = new ErrorJson();
            err.setError(e.getMessage());
            return err;
        }
    }
}
