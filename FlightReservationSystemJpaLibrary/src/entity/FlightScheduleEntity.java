/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * @author foozh
 */
@Entity
public class FlightScheduleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightScheduleId;
   
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date departureDateTime;

    @Column(nullable = false)
    @Min(0)
    @Max(24)
    private double duration;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private FlightSchedulePlanEntity flightSchedulePlan;

    @OneToMany(mappedBy = "flightSchedule", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<SeatInventoryEntity> seatInventory;

    @OneToMany(mappedBy = "flightSchedule", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<ReservationEntity> reservations;

    public FlightScheduleEntity() {
        reservations = new ArrayList<>();
        seatInventory = new ArrayList<>();
    }

    public FlightScheduleEntity(Date departureDateTime, double duration) {
        this.departureDateTime = departureDateTime;
        this.duration = duration;
    }

    public FlightScheduleEntity(Long flightScheduleId, Date departureDateTime, double duration, FlightSchedulePlanEntity flightSchedulePlan, List<SeatInventoryEntity> seatInventory, List<ReservationEntity> reservations) {
        this.flightScheduleId = flightScheduleId;
        this.departureDateTime = departureDateTime;
        this.duration = duration;
        this.flightSchedulePlan = flightSchedulePlan;
        this.seatInventory = seatInventory;
        this.reservations = reservations;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getDepartureDateTime() {
        return departureDateTime;
    }

    public double getDuration() {
        return duration;
    }

    public FlightSchedulePlanEntity getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public List<SeatInventoryEntity> getSeatInventory() {
        return seatInventory;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setDepartureDateTime(Date departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setFlightSchedulePlan(FlightSchedulePlanEntity flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    public void setSeatInventory(List<SeatInventoryEntity> seatInventory) {
        this.seatInventory = seatInventory;
    }

    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }
    
    
    public Long getFlightScheduleId() {
        return flightScheduleId;
    }
    
    public void setFlightScheduleId(Long flightScheduleId) {
        this.flightScheduleId = flightScheduleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightScheduleId != null ? flightScheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightScheduleId fields are not set
        if (!(object instanceof FlightScheduleEntity)) {
            return false;
        }
        FlightScheduleEntity other = (FlightScheduleEntity) object;
        if ((this.flightScheduleId == null && other.flightScheduleId != null) || (this.flightScheduleId != null && !this.flightScheduleId.equals(other.flightScheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightScheduleEntity[ id=" + flightScheduleId + " ]";
    }
    
}
