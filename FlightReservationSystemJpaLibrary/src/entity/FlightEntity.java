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
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

/**
 *
 * @author foozh
 */
@Entity
public class FlightEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;
    
    @Column(nullable = false, unique = true, length = 8)
    @Size(min = 3, max = 8)
    private String flightNum;
    
    @Column(nullable = false)
    private boolean disabled;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private FlightRouteEntity flightRoute;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private AircraftConfigurationEntity aircraftConfig;

    @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<FlightSchedulePlanEntity> flightSchedulePlan;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    private FlightEntity returnFlight;
    @OneToOne(mappedBy = "returnFlight", cascade = CascadeType.PERSIST)
    private FlightEntity originFlight;

    public FlightEntity() {
    }
    
    public FlightEntity(String flightNum) {
        this();
        this.flightNum = flightNum;
    }

    public FlightEntity(String flightNum) {
        this.flightNum = flightNum;
    }
    

    public FlightEntity(Long FlightId, String flightNum, boolean disabled, FlightRouteEntity flightRoute, AircraftConfigurationEntity aircraftConfig, List<FlightSchedulePlanEntity> flightSchedulePlan) {
        this.flightId = FlightId;
        this.flightNum = flightNum;
        this.disabled = disabled;
        this.flightRoute = flightRoute;
        this.aircraftConfig = aircraftConfig;
        this.flightSchedulePlan = flightSchedulePlan;
    }
    
    public Long getFlightId() {
        return flightId;
    }

    public FlightEntity getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(FlightEntity returnFlight) {
        this.returnFlight = returnFlight;
    }

    public FlightEntity getOriginFlight() {
        return originFlight;
    }

    public void setOriginFlight(FlightEntity originFlight) {
        this.originFlight = originFlight;
    }
    
    public String getFlightNum() {
        return flightNum;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }
    
    public void setFlightNum(String flightNum) {
        this.flightNum = flightNum;
    }
    
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
    public FlightRouteEntity getFlightRoute() {
        return flightRoute;
    }  

    public void setFlightRoute(FlightRouteEntity flightRoute) {
        this.flightRoute = flightRoute;
    }
    
    public AircraftConfigurationEntity getAircraftConfig() {
        return aircraftConfig;
    }

    public void setAircraftConfig(AircraftConfigurationEntity aircraftConfig) {
        this.aircraftConfig = aircraftConfig;
    }
    
    public List<FlightSchedulePlanEntity> getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(List<FlightSchedulePlanEntity> flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightId != null ? flightId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the FlightId fields are not set
        if (!(object instanceof FlightEntity)) {
            return false;
        }
        FlightEntity other = (FlightEntity) object;
        if ((this.flightId == null && other.flightId != null) || (this.flightId != null && !this.flightId.equals(other.flightId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Flight[ id=" + flightId + " ]";
    }
    
 
    
}
