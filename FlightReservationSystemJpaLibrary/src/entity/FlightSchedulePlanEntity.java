/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enumeration.ScheduleTypeEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 * @author foozh
 */
@Entity
public class FlightSchedulePlanEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightSchedulePlanId;

    @Column(nullable = false, length = 8)
    @NotNull
    @Size(min = 3, max = 8)
    private String flightNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ScheduleTypeEnum typeExistingInPlan;

    @Column(nullable = false)
    @NotNull
    private boolean disabled;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date recurringEndDate;

    @OneToMany(mappedBy = "flightSchedulePlan", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<FlightScheduleEntity> flightSchedule;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private FlightEntity flight;

    @OneToMany(mappedBy = "flightSchedulePlan", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<FareEntity> fares;

    @OneToOne(mappedBy = "complementary", cascade = CascadeType.PERSIST)
    private FlightSchedulePlanEntity origin;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    private FlightSchedulePlanEntity complementary;

    public Long getFlightSchedulePlanId() {
        return flightSchedulePlanId;
    }

    public void setFlightSchedulePlanId(Long flightSchedulePlanId) {
        this.flightSchedulePlanId = flightSchedulePlanId;
    }

    public FlightSchedulePlanEntity() {
        flightSchedule = new ArrayList<>();
        fares = new ArrayList<>();
    }
    
    //non-recurring
    public FlightSchedulePlanEntity(ScheduleTypeEnum typeExistingInPlan, FlightEntity flight) {
        this();
        this.typeExistingInPlan = typeExistingInPlan;
        this.flight = flight;
        this.flightNum = flight.getFlightNum();
        this.recurringEndDate = null;
    }
    
    //Recurring
    public FlightSchedulePlanEntity(ScheduleTypeEnum typeExistingInPlan, Date recurrentEndDate, FlightEntity flight) {
        this();
        this.typeExistingInPlan = typeExistingInPlan;
        this.recurringEndDate = recurringEndDate;
        this.flight = flight;
        this.flightNum = flight.getFlightNum();
    }

    public FlightSchedulePlanEntity(Long flightSchedulePlanId, String flightNum, ScheduleTypeEnum typeExistingInPlan, boolean disabled, Date recurringEndDate, List<FlightScheduleEntity> flightSchedule,
            FlightEntity flight, List<FareEntity> fares, FlightSchedulePlanEntity origin, FlightSchedulePlanEntity complementary) {
        this.flightSchedulePlanId = flightSchedulePlanId;
        this.flightNum = flightNum;
        this.typeExistingInPlan = typeExistingInPlan;
        this.disabled = disabled;
        this.recurringEndDate = recurringEndDate;
        this.flightSchedule = flightSchedule;
        this.flight = flight;
        this.fares = fares;
        this.origin = origin;
        this.complementary = complementary;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFlightNum() {
        return flightNum;
    }

    public ScheduleTypeEnum getTypeExistingInPlan() {
        return typeExistingInPlan;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public Date getRecurringEndDate() {
        return recurringEndDate;
    }

    public List<FlightScheduleEntity> getFlightSchedule() {
        return flightSchedule;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public List<FareEntity> getFares() {
        return fares;
    }

    public FlightSchedulePlanEntity getOrigin() {
        return origin;
    }

    public FlightSchedulePlanEntity getComplementary() {
        return complementary;
    }

    public void setFlightNum(String flightNum) {
        this.flightNum = flightNum;
    }

    public void setTypeExistingInPlan(ScheduleTypeEnum typeExistingInPlan) {
        this.typeExistingInPlan = typeExistingInPlan;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setRecurringEndDate(Date recurringEndDate) {
        this.recurringEndDate = recurringEndDate;
    }

    public void setFlightSchedule(List<FlightScheduleEntity> flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public void setFares(List<FareEntity> fares) {
        this.fares = fares;
    }

    public void setOrigin(FlightSchedulePlanEntity origin) {
        this.origin = origin;
    }

    public void setComplementary(FlightSchedulePlanEntity complementary) {
        this.complementary = complementary;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightSchedulePlanId != null ? flightSchedulePlanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightSchedulePlanId fields are not set
        if (!(object instanceof FlightSchedulePlanEntity)) {
            return false;
        }
        FlightSchedulePlanEntity other = (FlightSchedulePlanEntity) object;
        if ((this.flightSchedulePlanId == null && other.flightSchedulePlanId != null) || (this.flightSchedulePlanId != null && !this.flightSchedulePlanId.equals(other.flightSchedulePlanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedulePlanEntity[ id=" + flightSchedulePlanId + " ]";
    }
    
}
