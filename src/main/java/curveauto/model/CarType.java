package curveauto.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="CAR_TYPE")
public class CarType {
    private long id;
    private String name;
    private Set<CarTypeMaintenance> carTypeMaintenances;

    public CarType() {
        this.carTypeMaintenances = new HashSet<>();
    }

    @Id
    @GeneratedValue()
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable=false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    public Set<CarTypeMaintenance> getCarTypeMaintenances() {
        return carTypeMaintenances;
    }

    public void setCarTypeMaintenances(Set<CarTypeMaintenance> carTypeMaintenances) {
        this.carTypeMaintenances = carTypeMaintenances;
    }
}
