/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kahjy
 */
@Entity
public class AircraftConfigurationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftConfigId;
    
    @Column(nullable = false, unique = true, length = 64)
    @Size(min = 1, max = 64)
    @NotNull
    private String name;
    
    @Column(nullable = false, unique = true, length = 64)
    @Min(1)
    @Max(4)
    @NotNull
    private int numberOfCabinClasses;
    
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private AircraftTypeEntity aircraftType;
    
    @OneToMany(mappedBy = "aircraftConfig", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private List<CabinClassEntity> cabin; 

    public Long getAircraftConfigId() {
        return aircraftConfigId;
    }

    public void setAircraftConfigId(Long aircraftConfigId) {
        this.aircraftConfigId = aircraftConfigId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfCabinClasses() {
        return numberOfCabinClasses;
    }

    public void setNumberOfCabinClasses(int numberOfCabinClasses) {
        this.numberOfCabinClasses = numberOfCabinClasses;
    }

    public AircraftTypeEntity getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftTypeEntity aircraftType) {
        this.aircraftType = aircraftType;
    }

    public List<CabinClassEntity> getCabin() {
        return cabin;
    }

    public void setCabin(List<CabinClassEntity> cabin) {
        this.cabin = cabin;
    }
    
    public Long getId() {
        return aircraftConfigId;
    }

    public void setId(Long aircraftConfigId) {
        this.aircraftConfigId = aircraftConfigId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftConfigId != null ? aircraftConfigId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AircraftConfigurationEntity)) {
            return false;
        }
        AircraftConfigurationEntity other = (AircraftConfigurationEntity) object;
        if ((this.aircraftConfigId == null && other.aircraftConfigId != null) || (this.aircraftConfigId != null && !this.aircraftConfigId.equals(other.aircraftConfigId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftConfigurationEntity[ id=" + aircraftConfigId + " ]";
    }
    
}
