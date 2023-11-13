/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FareEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.SeatInventoryEntity;
import enumeration.CabinClassTypeEnum;
import exceptions.CabinClassNotFoundException;
import exceptions.FlightNotFoundException;
import exceptions.FlightScheduleNotFoundException;
import exceptions.SeatInventoryNotFoundException;
import exceptions.UpdateFlightScheduleException;
import exceptions.ViolationConstraintsException;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Local;

/**
 *
 * @author foozh
 */
@Local
public interface FlightScheduleSessionBeanLocal {
    
    public FlightScheduleEntity createNewSchedule(FlightSchedulePlanEntity flightSchedulePlan, FlightScheduleEntity schedule) throws ViolationConstraintsException;
    
    public FlightScheduleEntity retrieveFlightScheduleById(Long flightScheduleID) throws FlightScheduleNotFoundException;
    
    public FlightScheduleEntity retrieveUnmanagedFlightScheduleById(Long flightScheduleID) throws FlightScheduleNotFoundException;
    
    public void deleteSchedule(List<FlightScheduleEntity> flightSchedule);
    
    public List<FlightScheduleEntity> getFlightSchedules(String departure, String destination, Date date, CabinClassTypeEnum cabin) throws FlightNotFoundException;
    
    public List<FlightScheduleEntity> getUnManagedFlightSchedules(String departure, String destination, Date date, CabinClassTypeEnum cabin) throws FlightNotFoundException;
    
    public List<Pair<FlightScheduleEntity, FlightScheduleEntity>> getIndirectUnManagedFlightSchedules(
            String departure, String destination, Date date, CabinClassTypeEnum cabin) throws FlightNotFoundException;
    
    public FareEntity retrieveLowestFare(FlightScheduleEntity flightSchedule, CabinClassTypeEnum cabinClassType)
            throws FlightScheduleNotFoundException, CabinClassNotFoundException;
    
    public FareEntity getHighestUnmanagedFare(FlightScheduleEntity flightScheduleEntity, CabinClassTypeEnum type) throws FlightScheduleNotFoundException,
            CabinClassNotFoundException;
    
    public SeatInventoryEntity getCorrectSeatInventory(FlightScheduleEntity flightSchedule, CabinClassTypeEnum cabinClassType) throws FlightScheduleNotFoundException,
            SeatInventoryNotFoundException;
    
    public SeatInventoryEntity getCorrectSeatInventoryUnmanaged(FlightScheduleEntity flightSchedule, CabinClassTypeEnum cabinClassType) throws FlightScheduleNotFoundException,
            SeatInventoryNotFoundException;
     
    public FlightScheduleEntity updateFlightSchedule(long flightScheduleId, Date newDepartureDateTime, double newFlightDuration) throws FlightScheduleNotFoundException, UpdateFlightScheduleException;
    
    public void deleteFlightSchedule(long flightScheduleId) throws FlightScheduleNotFoundException, UpdateFlightScheduleException;
    
}
