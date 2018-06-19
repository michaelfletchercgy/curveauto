package curveauto.model;

import java.util.List;

public interface DataAccess {
    void commit();

    void rollback();

    void close();

    List<Car> getAllCars();

    Car save(Car car);

    Car getCar(long id);

    Object deleteCar(long id);

    Object deleteMaintenanceType(long id);

    MaintenanceType saveMaintenanceType(MaintenanceType maintenanceType);

    MaintenanceType getMaintenanceType(long id);

    List<MaintenanceType> getAllMaintenanceTypes();

    List<Car> getAllCarsTypes();

    CarType getCarType(long id);
}
