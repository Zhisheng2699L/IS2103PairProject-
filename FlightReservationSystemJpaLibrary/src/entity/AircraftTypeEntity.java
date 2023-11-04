/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kahjy
 */
@Entity
public class AircraftTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftID;
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String aircraftTypeName;
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Min(0)
    private int maxCapacity;
    
    @OneToMany(mappedBy = "aircraftType", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private List<AircraftConfigurationEntity> aircraftConfig;

    public AircraftTypeEntity() {
        aircraftConfig = new ArrayList<>();
    }

    public AircraftTypeEntity(Long aircraftID, String aircraftTypeName, int maxCapacity, List<AircraftConfigurationEntity> aircraftConfig) {
        this.aircraftID = aircraftID;
        this.aircraftTypeName = aircraftTypeName;
        this.maxCapacity = maxCapacity;
        this.aircraftConfig = aircraftConfig;
    }

    public Long getAircraftID() {
        return aircraftID;
    }

    public void setAircraftID(Long aircraftID) {
        this.aircraftID = aircraftID;
    }

    public String getAircraftTypeName() {
        return aircraftTypeName;
    }

    public void setAircraftTypeName(String aircraftTypeName) {
        this.aircraftTypeName = aircraftTypeName;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(String maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public List<AircraftConfigurationEntity> getAircraftConfig() {
        return aircraftConfig;
    }

    public void setAircraftConfig(List<AircraftConfigurationEntity> aircraftConfig) {
        this.aircraftConfig = aircraftConfig;
    }

    
    
    public Long getId() {
        return aircraftID;
    }

    public void setId(Long aircraftID) {
        this.aircraftID = aircraftID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftID != null ? aircraftID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AircraftTypeEntity)) {
            return false;
        }
        AircraftTypeEntity other = (AircraftTypeEntity) object;
        if ((this.aircraftID == null && other.aircraftID != null) || (this.aircraftID != null && !this.aircraftID.equals(other.aircraftID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftTypeEntity[ id=" + aircraftID + " ]";
    }
    
}
