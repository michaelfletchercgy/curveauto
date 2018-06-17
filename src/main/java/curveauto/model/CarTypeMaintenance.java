package curveauto.model;

import javax.persistence.*;

@Entity
@Table(name="CAR_TYPE_MAINTENANCE")
public class CarTypeMaintenance {
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

    @ManyToOne(fetch=FetchType.EAGER)
    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(MaintenanceType maintenanceType) {
        this.maintenanceType = maintenanceType;
    }
}
