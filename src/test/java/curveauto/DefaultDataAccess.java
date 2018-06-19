package curveauto;

import curveauto.model.Car;
import curveauto.model.CarType;
import curveauto.model.DataAccess;
import curveauto.model.MaintenanceType;

import java.util.List;


public class DefaultDataAccess implements DataAccess {
    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void close() {

    }

    @Override
    public List<Car> getAllCars() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Car save(Car car) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Car getCar(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object deleteCar(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object deleteMaintenanceType(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MaintenanceType saveMaintenanceType(MaintenanceType maintenanceType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MaintenanceType getMaintenanceType(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<MaintenanceType> getAllMaintenanceTypes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Car> getAllCarsTypes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CarType getCarType(long id) {
        throw new UnsupportedOperationException();
    }
}
