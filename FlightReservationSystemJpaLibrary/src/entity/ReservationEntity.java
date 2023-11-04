/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enumeration.CabinClassTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author foozh
 */
@Entity
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    
    @Column(nullable = false, length = 7)
    @NotNull
    @Size(min = 2, max = 10)
    private String fareClassCode;

    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal fareAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private CabinClassTypeEnum cabinClass;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private ItineraryEntity itinerary;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<PassengerEntity> passenger;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private FlightScheduleEntity flightSchedule;
    
    public ReservationEntity() {
        passenger = new ArrayList<>();
    }

    public ReservationEntity(String fareBasisCode, BigDecimal fareAmount, CabinClassTypeEnum cabinClassType) {
        this();
        this.fareClassCode = fareBasisCode;
        this.fareAmount = fareAmount;
        this.cabinClass = cabinClassType;
    }

    public ReservationEntity(Long id, String fareBasisCode, BigDecimal fareAmount, CabinClassTypeEnum cabinClassType, ItineraryEntity itinerary,
            List<PassengerEntity> passenger, FlightScheduleEntity flightSchedule) {
        this.reservationId = id;
        this.fareClassCode = fareBasisCode;
        this.fareAmount = fareAmount;
        this.cabinClass = cabinClassType;
        this.itinerary = itinerary;
        this.passenger = passenger;
        this.flightSchedule = flightSchedule;
    }
    
    public String getFareClassCode() {
        return fareClassCode;
    }

    public void setFareClassCode(String fareClassCode) {
        this.fareClassCode = fareClassCode;
    }

    public BigDecimal getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(BigDecimal fareAmount) {
        this.fareAmount = fareAmount;
    }

    public CabinClassTypeEnum getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(CabinClassTypeEnum cabinClass) {
        this.cabinClass = cabinClass;
    }

    public ItineraryEntity getItinerary() {
        return itinerary;
    }

    public void setItinerary(ItineraryEntity itinerary) {
        this.itinerary = itinerary;
    }

    public List<PassengerEntity> getPassenger() {
        return passenger;
    }

    public void setPassenger(List<PassengerEntity> passenger) {
        this.passenger = passenger;
    }

    public FlightScheduleEntity getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightScheduleEntity flightSchedule) {
        this.flightSchedule = flightSchedule;
    }


    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationEntity[ id=" + reservationId + " ]";
    }
    
}
