package curveauto.unittests;

import curveauto.API;
import curveauto.da.DataAccess;
import curveauto.da.DataAccessFactory;
import curveauto.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class APITests {
    @Test()
    public void testDeleteCarWithMaintenance() {
        DataAccess da = ((DataAccessFactory) () -> new DefaultDataAccess() {
            @Override
            public Car getCar(long id) {
                MaintenanceType mt = new MaintenanceType();
                mt.setName("foo");

                Car car = new Car();
                car.setId(1);

                CarMaintenance cm = new CarMaintenance();
                cm.setMaintenanceType(mt);
                car.getMaintenance().add(cm);

                return car;
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

        DataAccess da = ((DataAccessFactory) () -> new DefaultDataAccess() {
            @Override
            public Car getCar(long id) {
                MaintenanceType mt = new MaintenanceType();
                mt.setName("foo");

                Car car = new Car();
                car.setId(1);

                CarMaintenance cm = new CarMaintenance();
                cm.setMaintenanceType(mt);
                car.getMaintenance().add(cm);

                return super.getCar(id);
            }

            @Override
            public CarType getCarType(long id) {
                return electric;
            }
        }).create();

        API api = new API();

        MaintenanceType oil = new MaintenanceType();
        oil.setName("Oil Change");

        MaintenanceType tires = new MaintenanceType();
        oil.setName("Tire Change");



        CarTypeMaintenance ctm = new CarTypeMaintenance();
        ctm.setMaintenanceType(tires);
        electric.getCarTypeMaintenances().add(ctm);




        Car myTesla = new Car();
        myTesla.setCarType(electric);

        CarMaintenance myTeslaOilChange = new CarMaintenance();
        myTeslaOilChange.setMaintenanceType(oil);
        myTesla.getMaintenance().add(myTeslaOilChange);

        CarMaintenance myTeslaTireChange = new CarMaintenance();
        myTeslaTireChange.setMaintenanceType(tires);
        myTesla.getMaintenance().add(myTeslaTireChange);

        try {
            api.carsPost("1", myTesla, da);
            Assert.fail();
        } catch(Exception e) {
            Assert.assertEquals(RuntimeException.class, e.getClass());
            Assert.assertEquals("A Electric may not have a Tire Change maintenance type.", e.getMessage());
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
}
