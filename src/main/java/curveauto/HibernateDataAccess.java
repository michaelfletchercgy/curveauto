package curveauto;

import curveauto.model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateDataAccess implements DataAccess {
    private Session session;
    private Transaction tx;

    public HibernateDataAccess(Session session) {
        this.session = session;
        this.tx = session.beginTransaction();
    }

    public void createTestData() {
        // Maintenance
        MaintenanceType oilChange = new MaintenanceType();
        session.save(oilChange);

        MaintenanceType tireRotation = new MaintenanceType();
        session.save(tireRotation);

        // Electric Car
        CarType electric = new CarType();
        electric.setName("Electric");
        session.save(electric);

        CarTypeMaintenance electricTireRotation = new CarTypeMaintenance();
        electricTireRotation.setMaintenanceType(tireRotation);
        electric.getCarTypeMaintenances().add(electricTireRotation);

        // Diesel Car
        CarType diesel = new CarType();
        diesel.setName("Diesel");
        session.save(diesel);

        CarTypeMaintenance dieselTireRotation = new CarTypeMaintenance();
        dieselTireRotation.setMaintenanceType(tireRotation);
        diesel.getCarTypeMaintenances().add(dieselTireRotation);

        CarTypeMaintenance dieselOilChange = new CarTypeMaintenance();
        dieselOilChange.setMaintenanceType(oilChange);
        diesel.getCarTypeMaintenances().add(dieselOilChange);

        // Gasoline Car
        CarType gasoline = new CarType();
        gasoline.setName("Gas");
        session.save(gasoline);

        CarTypeMaintenance gasolineTireRotation = new CarTypeMaintenance();
        gasolineTireRotation.setMaintenanceType(tireRotation);
        gasoline.getCarTypeMaintenances().add(gasolineTireRotation);

        CarTypeMaintenance gasolineOilChange = new CarTypeMaintenance();
        gasolineOilChange.setMaintenanceType(oilChange);
        gasoline.getCarTypeMaintenances().add(gasolineOilChange);

        Car myElectricCar = new Car();
        myElectricCar.setVin("1234567890");
        myElectricCar.setCarType(electric);
        session.save(myElectricCar);

        Car myTruck = new Car();
        myTruck.setVin("1234567890");
        myTruck.setCarType(diesel);
        session.save(myTruck);

    }

    @Override
    public List<Car> getAllCars() {
        return session.createQuery("from Car").list();
    }

    @Override
    public Car save(Car car) {
        session.save(car);
        return car;
    }

    @Override
    public Car getCar(String id) {
        return session.get(Car.class, Long.parseLong(id));
    }

    @Override
    public Object deleteCar(long id) {
        Car car = session.load(Car.class, Long.valueOf(id));
        session.delete(car);
        return new Object();
    }

    @Override
    public void commit() {
        tx.commit();

        // This is just to ensure that the session is not used again.  Any further use would fail.
        session = null;
        tx = null;
    }
}
