package curveauto.model;

import java.util.List;

public interface DataAccess {

    List<Car> getAllCars();

    Car save(Car car);

    Car getCar(String id);

    Object deleteCar(long id);

    void commit();
}
