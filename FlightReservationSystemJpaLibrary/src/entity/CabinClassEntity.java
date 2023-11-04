/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enumeration.CabinClassTypeEnum;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kahjy
 */
@Entity
public class CabinClassEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassId;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private CabinClassTypeEnum cabinClassType;
    
    @Column(nullable = false)
    @Min(0)
    @Max(2)
    @NotNull
    private int numOfAisles;
    
    @Column(nullable = false)
    @Min(1)
    @NotNull
    private int numRows;
    
    @Column(nullable = false)
    @Min(1)
    @NotNull
    private int numSeatsAbreast;
    
    @Column(nullable = false)
    @Size(min = 1, max = 5)
    @NotNull
    private int seatingConfigPerColumn;
    
    @Column(nullable = false)
    @Min(1)
    @NotNull
    private int maxSeatCapacity;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false)
    private AircraftConfigurationEntity aircraftConfig;

    public CabinClassEntity() {
    }

    public CabinClassEntity(Long cabinClassId, CabinClassTypeEnum cabinClassType, int numOfAisles, int numRows, int numSeatsAbreast, int seatingConfigPerColumn, int maxSeatCapacity, AircraftConfigurationEntity aircraftConfig) {
        this.cabinClassId = cabinClassId;
        this.cabinClassType = cabinClassType;
        this.numOfAisles = numOfAisles;
        this.numRows = numRows;
        this.numSeatsAbreast = numSeatsAbreast;
        this.seatingConfigPerColumn = seatingConfigPerColumn;
        this.maxSeatCapacity = maxSeatCapacity;
        this.aircraftConfig = aircraftConfig;
    }

    public CabinClassEntity(CabinClassTypeEnum cabinClassType, int numOfAisles, int numRows, int numSeatsAbreast, int seatingConfigPerColumn, int maxSeatCapacity) {
        this.cabinClassType = cabinClassType;
        this.numOfAisles = numOfAisles;
        this.numRows = numRows;
        this.numSeatsAbreast = numSeatsAbreast;
        this.seatingConfigPerColumn = seatingConfigPerColumn;
        this.maxSeatCapacity = maxSeatCapacity;
    }

    public Long getCabinClassId() {
        return cabinClassId;
    }

    public void setCabinClassId(Long cabinClassId) {
        this.cabinClassId = cabinClassId;
    }

    public CabinClassTypeEnum getCabinClassType() {
        return cabinClassType;
    }

    public void setCabinClassType(CabinClassTypeEnum cabinClassType) {
        this.cabinClassType = cabinClassType;
    }

    public int getNumOfAisles() {
        return numOfAisles;
    }

    public void setNumOfAisles(int numOfAisles) {
        this.numOfAisles = numOfAisles;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getNumSeatsAbreast() {
        return numSeatsAbreast;
    }

    public void setNumSeatsAbreast(int numSeatsAbreast) {
        this.numSeatsAbreast = numSeatsAbreast;
    }

    public int getSeatingConfigPerColumn() {
        return seatingConfigPerColumn;
    }

    public void setSeatingConfigPerColumn(int seatingConfigPerColumn) {
        this.seatingConfigPerColumn = seatingConfigPerColumn;
    }

    public int getMaxSeatCapacity() {
        return maxSeatCapacity;
    }

    public void setMaxSeatCapacity(int maxSeatCapacity) {
        this.maxSeatCapacity = maxSeatCapacity;
    }

    public AircraftConfigurationEntity getAircraftConfig() {
        return aircraftConfig;
    }

    public void setAircraftConfig(AircraftConfigurationEntity aircraftConfig) {
        this.aircraftConfig = aircraftConfig;
    }
    
    
    
    public Long getId() {
        return cabinClassId;
    }

    public void setId(Long cabinClassId) {
        this.cabinClassId = cabinClassId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cabinClassId != null ? cabinClassId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CabinClassEntity)) {
            return false;
        }
        CabinClassEntity other = (CabinClassEntity) object;
        if ((this.cabinClassId == null && other.cabinClassId != null) || (this.cabinClassId != null && !this.cabinClassId.equals(other.cabinClassId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CabinClass[ id=" + cabinClassId + " ]";
    }
    
}
