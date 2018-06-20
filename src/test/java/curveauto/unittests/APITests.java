package curveauto.unittests;

import curveauto.API;
import curveauto.da.DataAccess;
import curveauto.da.DataAccessFactory;
import curveauto.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * These test the key API functions that are more complicated that simple CRUD.
 */
public class APITests {
    @Test()
    public void testDeleteCarWithMaintenance() {
        MaintenanceType mt = new MaintenanceType();
        mt.setName("foo");

        Car car = new Car();
        car.setId(1);

        CarMaintenance cm = new CarMaintenance();
        cm.setCar(car);
        cm.setMaintenanceType(mt);

        DataAccess da = ((DataAccessFactory) () -> new DefaultDataAccess() {
            @Override
            public Car getCar(long id) {
                return car;
            }

            @Override
            public List<CarMaintenance> getCarMaintenance(long carId) {
                return Arrays.asList(cm);
            }
        }).create();

        API api = new API();

        try {
            api.carsDelete("1", da);
            Assert.fail();
        } catch(Exception e) {
            Assert.assertEquals(RuntimeException.class, e.getClass());
            Assert.assertEquals("Car has outstanding maintenance.  It cannot be deleted.", e.getMessage());
        }
    }

    @Test()
    public void testSaveCarWithBadWithMaintenance() {
        CarType electric = new CarType();
        electric.setName("Electric");

        MaintenanceType oil = new MaintenanceType();
        oil.setName("Oil Change");

        Car myTesla = new Car();
        myTesla.setCarType(electric);

        DataAccess da = ((DataAccessFactory) () -> new DefaultDataAccess() {
            @Override
            public Car getCar(long id) {

                return myTesla;
            }

            @Override
            public MaintenanceType getMaintenanceType(long id) {
                return oil;
            }

            @Override
            public CarType getCarType(long id) {
                return electric;
            }
        }).create();

        API api = new API();



        CarMaintenance myTeslaOilChange = new CarMaintenance();
        myTeslaOilChange.setMaintenanceType(oil);
        myTeslaOilChange.setCar(myTesla);

        try {
            api.addCarMaintenance(myTeslaOilChange, da);
            Assert.fail();
        } catch(Exception e) {
            Assert.assertEquals(RuntimeException.class, e.getClass());
            Assert.assertEquals("A Electric may not have a Oil Change maintenance type.", e.getMessage());
        }
    }

    public static class DefaultDataAccess implements DataAccess {
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
        public Car saveCar(Car car) {
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

        @Override
        public List<CarMaintenance> getCarMaintenance(long carId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public CarMaintenance saveCarMaintenance(CarMaintenance cm) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteCarMaintenance(long carId) {
            throw new UnsupportedOperationException();
        }
    }
}
