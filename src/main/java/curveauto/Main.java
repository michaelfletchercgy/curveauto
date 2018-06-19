package curveauto;

public class Main {
    public static void main(String[] args) throws Exception {
        HibernateDataAccessFactory dataAccessFactory = new HibernateDataAccessFactory();
        API api = new API();
        new HttpServer(dataAccessFactory, api, 5050);
    }
}
