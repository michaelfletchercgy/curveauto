package curveauto.model;

import javax.persistence.*;

@Entity
@Table(name="MAINTENANCE_TYPE")
public class MaintenanceType {
    private long id;
    private String name;

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

    @Override
    public String toString() {
        return "MaintenanceType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

