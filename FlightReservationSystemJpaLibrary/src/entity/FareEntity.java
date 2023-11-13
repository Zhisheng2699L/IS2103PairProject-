/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enumeration.CabinClassTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;

/**
 *
 * @author kahjy
 */
@Entity
public class FareEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fareId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private CabinClassTypeEnum  cabinClassType;
    
    @Column(nullable = false)
    @NotNull
    private String fareBasisCode;
    
    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal fareAmount;
    
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false)
    private FlightSchedulePlanEntity flightSchedulePlan;

    public FareEntity() {
    }

    public FareEntity(String fareBasisCode, BigDecimal fareAmount, CabinClassTypeEnum cabinClassType) {
        this();
        this.fareBasisCode = fareBasisCode;
        this.fareAmount = fareAmount;
        this.cabinClassType = cabinClassType;
    }




    public Long getFareId() {
        return fareId;
    }

    public void setFareId(Long fareId) {
        this.fareId = fareId;
    }

    public CabinClassTypeEnum getCabinClassType() {
        return cabinClassType;
    }

    public void setCabinClassType(CabinClassTypeEnum cabinClassType) {
        this.cabinClassType = cabinClassType;
    }

    public String getFareBasisCode() {
        return fareBasisCode;
    }

    public void setFareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
    }

    public BigDecimal getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(BigDecimal fareAmount) {
        this.fareAmount = fareAmount;
    }

    public FlightSchedulePlanEntity getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlanEntity flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }
    
    public Long getId() {
        return fareId;
    }

    public void setId(Long id) {
        this.fareId = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fareId != null ? fareId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FareEntity)) {
            return false;
        }
        FareEntity other = (FareEntity) object;
        if ((this.fareId == null && other.fareId != null) || (this.fareId != null && !this.fareId.equals(other.fareId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FareEntity[ id=" + fareId + " ]";
    }
    
}
