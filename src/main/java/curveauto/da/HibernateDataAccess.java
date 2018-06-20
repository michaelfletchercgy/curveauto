package curveauto.da;

import curveauto.model.*;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class HibernateDataAccess implements DataAccess {
    private Session session;
    private Transaction tx;

    public HibernateDataAccess(Session session) {
        this.session = session;
        this.tx = session.beginTransaction();
    }

    @Override
    public void commit() {
        tx.commit();

        close();
    }

    @Override
    public void rollback() {
        tx.rollback();

        close();
    }

    @Override
    public void close() {
        if (session != null) {
            session.close();
        }

        // This is just to ensure that the session is not used again.  Any further use would fail.
        session = null;
        tx = null;
    }

    public void createTestData() {
        // Maintenance
        MaintenanceType oilChange = new MaintenanceType();
        oilChange.setName("Oil Change");
        session.save(oilChange);

        MaintenanceType tireRotation = new MaintenanceType();
        tireRotation.setName("Tire Rotation");
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
        myTruck.setMake("Dodge");
        myTruck.setModel("RAM 1500");
        myTruck.setOdometer(303400);
        myTruck.setYear(2004);
        session.save(myTruck);

        CarMaintenance myTruckOil = new CarMaintenance();
        myTruckOil.setMaintenanceType(oilChange);
        myTruckOil.setCar(myTruck);
        myTruckOil.setOdometer(100000);
        session.save(myTruckOil);

    }

    @Override
    public List<Car> getAllCars() {
        return session.createQuery("from Car car order by car.id").list();
    }

    @Override
    public Car saveCar(Car car) {
        session.saveOrUpdate(car);
        return car;
    }

    @Override
    public Car getCar(long id) {
        return session.get(Car.class, id);
    }

    @Override
    public Object deleteCar(long id) {
        Car car = session.load(Car.class, id);
        session.delete(car);
        return new Object();
    }

    @Override
    public Object deleteMaintenanceType(long id) {
        MaintenanceType mt = session.load(MaintenanceType.class, id);
        session.delete(mt);
        return new Object();
    }

    @Override
    public MaintenanceType saveMaintenanceType(MaintenanceType maintenanceType) {
        session.saveOrUpdate(maintenanceType);
        return maintenanceType;
    }

    @Override
    public MaintenanceType getMaintenanceType(long id) {
        return session.get(MaintenanceType.class, id);
    }

    @Override
    public List<MaintenanceType> getAllMaintenanceTypes() {
        return session.createQuery("from MaintenanceType").list();
    }

    @Override
    public List<Car> getAllCarsTypes() {
        return session.createQuery("from CarType").list();
    }

    @Override
    public CarType getCarType(long id) {
        return session.get(CarType.class, id);
    }

    @Override
    public List<CarMaintenance> getCarMaintenance(long carId) {
        Query q = session.createQuery("from CarMaintenance cm where cm.car.id = :carId");
        q.setParameter("carId", carId);

        return q.list();
    }

    @Override
    public CarMaintenance saveCarMaintenance(CarMaintenance cm) {
        session.saveOrUpdate(cm);

        return cm;
    }

    @Override
    public void deleteCarMaintenance(long carMaintenanceId) {
        CarMaintenance cm = session.load(CarMaintenance.class, carMaintenanceId);
        session.delete(cm);
    }
}
