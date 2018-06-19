package curveauto;

import curveauto.model.*;
import org.junit.Assert;
import org.junit.Test;

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
        }).create();

        API api = new API();

        MaintenanceType oil = new MaintenanceType();
        oil.setName("Oil Change");

        MaintenanceType tires = new MaintenanceType();
        oil.setName("Tire Change");

        CarType electric = new CarType();
        electric.setName("Electric");

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
            Assert.assertEquals("bad", e.getMessage());
        }
    }
}
