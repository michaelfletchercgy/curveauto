package curveauto;

import curveauto.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateDataAccessFactory implements DataAccessFactory {
    private SessionFactory sessionFactory;

    public HibernateDataAccessFactory() {
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
        configuration.setProperty("hibernate.connection.pool_size", "4");

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

        sessionFactory = configuration.buildSessionFactory();


        HibernateDataAccess testData = new HibernateDataAccess(sessionFactory.openSession());
        testData.createTestData();
        testData.commit();
    }
    @Override
    public DataAccess create() {
        return new HibernateDataAccess(sessionFactory.openSession());
    }
}
