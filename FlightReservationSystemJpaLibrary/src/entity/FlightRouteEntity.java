/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author foozh
 */
@Entity
@Cacheable(false)
@Table(uniqueConstraints=
       @UniqueConstraint(columnNames = {"origin", "destination"}))
public class FlightRouteEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightRouteId;
   
    @Column(nullable = false)
    @NotNull
    private boolean disabled;

    @OneToMany(mappedBy = "flightRoute", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<FlightEntity> flights;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "origin")
    private AirportEntity origin;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "destination")
    private AirportEntity destination;
    
    @OneToOne
    private FlightRouteEntity complementaryRoute;
    @OneToOne(mappedBy = "complementaryRoute")
    private FlightRouteEntity originRoute;
    
    public FlightRouteEntity() {
        flights = new ArrayList<>();  
        disabled = false;
    }

    public FlightRouteEntity getComplementaryRoute() {
        return complementaryRoute;
    }

    public void setComplementaryRoute(FlightRouteEntity complementaryRoute) {
        this.complementaryRoute = complementaryRoute;
    }

    public FlightRouteEntity getOriginRoute() {
        return originRoute;
    }

    public void setOriginRoute(FlightRouteEntity originRoute) {
        this.originRoute = originRoute;
    }

    
    public FlightRouteEntity(Long flightrouteId, boolean disabled, List<FlightEntity> flights, AirportEntity origin, AirportEntity destination) {
        this.flightRouteId = flightrouteId;
        this.disabled = disabled;
        this.flights = flights;
        this.origin = origin;
        this.destination = destination;
    }
    
    public Long getFlightRouteId() {
        return flightRouteId;
    }

    public void setFlightRouteId(Long flightRouteId) {
        this.flightRouteId = flightRouteId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public List<FlightEntity> getFlights() {
        return flights;
    }

    public AirportEntity getOrigin() {
        return origin;
    }

    public AirportEntity getDestination() {
        return destination;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    public void setOrigin(AirportEntity origin) {
        this.origin = origin;
    }

    public void setDestination(AirportEntity destination) {
        this.destination = destination;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightRouteId != null ? flightRouteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the FlightrouteId fields are not set
        if (!(object instanceof FlightRouteEntity)) {
            return false;
        }
        FlightRouteEntity other = (FlightRouteEntity) object;
        if ((this.flightRouteId == null && other.flightRouteId != null) || (this.flightRouteId != null && !this.flightRouteId.equals(other.flightRouteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightRouteEntity[ id=" + flightRouteId + " ]";
    }
    
}
