package curveauto.model;

import javax.persistence.*;

@Entity
@Table(name="CAR_MAINTENANCE")
public class CarMaintenance {
    private long id;
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
    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(MaintenanceType maintenanceType) {
        this.maintenanceType = maintenanceType;
    }
}
