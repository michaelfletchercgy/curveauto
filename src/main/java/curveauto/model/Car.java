package curveauto.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="CAR")
public class Car {
    private long id;
    private CarType carType;
    private String vin;
    private String make;
    private String model;
    private int year;
    private int odometer;
    private Set<CarMaintenance> maintenance;

    public Car() {
        maintenance = new HashSet<>();
    }
    @Id
    @GeneratedValue()
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(fetch=FetchType.EAGER)
    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    @OneToMany(fetch=FetchType.EAGER)
    public Set<CarMaintenance> getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Set<CarMaintenance> maintenance) {
        this.maintenance = maintenance;
    }
}
