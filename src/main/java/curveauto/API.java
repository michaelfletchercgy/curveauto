package curveauto;

import curveauto.model.Car;
import curveauto.model.CarMaintenance;
import curveauto.model.DataAccess;
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

        validateCar(car);

        return da.save(car);
    }

    public Car carsGet(String id, DataAccess da) {
        return da.getCar(Long.valueOf(id));
    }

    public Car carsPost(String id, Car car, DataAccess da) {
        car.setId(Long.valueOf(id));

        car.setCarType(da.getCarType(car.getCarType().getId()));

        validateCar(car);

        return da.save(car);
    }

    private void validateCar(Car car) {
        Set<MaintenanceType> permittedMaintenanceTypes = car.getCarType().getCarTypeMaintenances().stream()
                .map(ctm -> ctm.getMaintenanceType())
                .collect(Collectors.toSet());

        for (CarMaintenance mt : car.getMaintenance()) {
            if (!permittedMaintenanceTypes.contains(mt.getMaintenanceType())) {
                throw new RuntimeException("A " + car.getCarType().getName() + " may not have a " + mt.getMaintenanceType().getName() +
                        " maintenance type.");
            }
        }
    }

    public Object carsDelete(String id, DataAccess da) {
        long carId = Long.valueOf(id);

        Car car = da.getCar(carId);
        if (!car.getMaintenance().isEmpty()) {
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
}
