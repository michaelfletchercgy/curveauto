package curveauto.da;

import curveauto.model.Car;
import curveauto.model.CarType;
import curveauto.model.MaintenanceType;

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
