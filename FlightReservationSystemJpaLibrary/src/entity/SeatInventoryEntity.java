/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author kahjy
 */
@Entity
public class SeatInventoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatInventoryId;

    @Column(nullable = false)
    @Min(0)
    @NotNull
    private int availableSeats;
    
    @Column(nullable = false)
    @Min(0)
    @NotNull
    private int reservedSeats;
    
    @Column(nullable = false)
    @Min(0)
    @NotNull
    private int balanceSeats;
    
    @Column(nullable = false)
    @NotNull
    private char[][] seats;
    
    @ManyToOne(optional = false, cascade = CascadeType.DETACH) 
    @JoinColumn(nullable = false)
    private CabinClassEntity cabin;
    
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false)
    private FlightScheduleEntity flightSchedule;

    public SeatInventoryEntity() {
    }

    public SeatInventoryEntity(Long seatInventoryId, int availableSeats, int reservedSeats, int balanceSeats, char[][] seats, CabinClassEntity cabin, FlightScheduleEntity flightSchedule) {
        this.seatInventoryId = seatInventoryId;
        this.availableSeats = availableSeats;
        this.reservedSeats = reservedSeats;
        this.balanceSeats = balanceSeats;
        this.seats = seats;
        this.cabin = cabin;
        this.flightSchedule = flightSchedule;
    }
    
    public SeatInventoryEntity(int availableSeats, int reservedSeats, int balanceSeats) {
        this();
        this.availableSeats = availableSeats;
        this.reservedSeats = reservedSeats;
        this.balanceSeats = balanceSeats;
    }

    public Long getSeatInventoryId() {
        return seatInventoryId;
    }

    public void setSeatInventoryId(Long seatInventoryId) {
        this.seatInventoryId = seatInventoryId;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(int reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public int getBalanceSeats() {
        return balanceSeats;
    }

    public void setBalanceSeats(int balanceSeats) {
        this.balanceSeats = balanceSeats;
    }

    public char[][] getSeats() {
        return seats;
    }

    public void setSeats(char[][] seats) {
        this.seats = seats;
    }

    public CabinClassEntity getCabin() {
        return cabin;
    }

    public void setCabin(CabinClassEntity cabin) {
        this.cabin = cabin;
    }

    public FlightScheduleEntity getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightScheduleEntity flightSchedule) {
        this.flightSchedule = flightSchedule;
    }
  
    public Long getId() {
        return seatInventoryId;
    }

    public void setId(Long seatInventoryId) {
        this.seatInventoryId = seatInventoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seatInventoryId != null ? seatInventoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SeatInventoryEntity)) {
            return false;
        }
        SeatInventoryEntity other = (SeatInventoryEntity) object;
        if ((this.seatInventoryId == null && other.seatInventoryId != null) || (this.seatInventoryId != null && !this.seatInventoryId.equals(other.seatInventoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SeatInventoryEntity[ id=" + seatInventoryId + " ]";
    }
    
}
