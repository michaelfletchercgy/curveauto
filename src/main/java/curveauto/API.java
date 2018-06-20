package curveauto;

import curveauto.model.Car;
import curveauto.model.CarMaintenance;
import curveauto.da.DataAccess;
import curveauto.model.MaintenanceType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class API {
    public API() {

    }

    public List<Car> carsTypeList(DataAccess da) {
        return da.getAllCarsTypes();
    }

    public List<Car> carsList(DataAccess da) {
        return da.getAllCars();
    }

    public Car carsPut(Car car, DataAccess da) {
        if (car.getId() != 0) {
            throw new RuntimeException();
        }

        return da.saveCar(car);
    }

    public Car carsGet(String id, DataAccess da) {
        return da.getCar(Long.valueOf(id));
    }

    public Car carsPost(String id, Car car, DataAccess da) {
        car.setId(Long.valueOf(id));

        car.setCarType(da.getCarType(car.getCarType().getId()));

        return da.saveCar(car);
    }

    public Object carsDelete(String id, DataAccess da) {
        long carId = Long.valueOf(id);

        if (!da.getCarMaintenance(carId).isEmpty()) {
            throw new RuntimeException("Car has outstanding maintenance.  It cannot be deleted.");
        }

        return da.deleteCar(carId);
    }

    public List<MaintenanceType> maintenanceTypesList(DataAccess da) {
        return da.getAllMaintenanceTypes();
    }

    public MaintenanceType maintenanceTypesPut(MaintenanceType car, DataAccess da) {
        if (car.getId() != 0) {
            throw new RuntimeException();
        }

        return da.saveMaintenanceType(car);
    }

    public MaintenanceType maintenanceTypesGet(String id, DataAccess da) {
        return da.getMaintenanceType(Long.valueOf(id));
    }

    public MaintenanceType maintenanceTypesPost(String id, MaintenanceType maintenanceType, DataAccess da) {
        maintenanceType.setId(Long.valueOf(id));

        return da.saveMaintenanceType(maintenanceType);
    }

    public Object maintenanceTypesDelete(String id, DataAccess da) {
        return da.deleteMaintenanceType(Long.valueOf(id));
    }

    public CarMaintenance addCarMaintenance(CarMaintenance cm, DataAccess da) {
        cm.setMaintenanceType(da.getMaintenanceType(cm.getMaintenanceType().getId()));
        cm.setCar(da.getCar(cm.getCar().getId()));

        Set<MaintenanceType> permittedMaintenanceTypes = cm.getCar().getCarType().getCarTypeMaintenances().stream()
                .map(ctm -> ctm.getMaintenanceType())
                .collect(Collectors.toSet());

        if (!permittedMaintenanceTypes.contains(cm.getMaintenanceType())) {
            throw new RuntimeException("A " + cm.getCar().getCarType().getName() + " may not have a " + cm.getMaintenanceType().getName() +
                    " maintenance type.");
        }


        return da.saveCarMaintenance(cm);
    }

    public List<CarMaintenance> carMaintenanceList(String id, DataAccess da) {
        long carId = Long.valueOf(id);
        return da.getCarMaintenance(carId);
    }

    public void carMaintenanceDelete(String id, DataAccess da) {
        long carId = Long.valueOf(id);
        da.deleteCarMaintenance(carId);
    }
}
