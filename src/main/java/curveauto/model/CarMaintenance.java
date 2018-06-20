package curveauto.model;

import javax.persistence.*;

@Entity
@Table(name="CAR_MAINTENANCE")
public class CarMaintenance {
    private long id;
    private Car car;
    private long odometer;
    private MaintenanceType maintenanceType;

    @Id
    @GeneratedValue()
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public long getOdometer() {
        return odometer;
    }

    public void setOdometer(long odometer) {
        this.odometer = odometer;
    }

    @ManyToOne
    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(MaintenanceType maintenanceType) {
        this.maintenanceType = maintenanceType;
    }
}
