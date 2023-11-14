/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.FareEntity;
import entity.FlightSchedulePlanEntity;
import exceptions.ExistingFareException;
import exceptions.ExistingFlightSchedulePlanException;
import exceptions.FareDoNotExistException;
import exceptions.FlightNotFoundException;
import exceptions.FlightScheduleNotFoundException;
import exceptions.FlightSchedulePlanDoNotExistException;
import exceptions.InputDataValidationException;
import exceptions.UnknownPersistenceException;
import exceptions.ViolationConstraintsException;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Remote;

/**
 *
 * @author foozh
 */
@Remote
public interface FlightSchedulePlanSessionBeanRemote {
    
    public FlightSchedulePlanEntity createNewFlightSchedulePlan(FlightSchedulePlanEntity plan, List<FareEntity> fares,
            long flightId, Pair<Date, Double> pair, int recurrent) throws ViolationConstraintsException,
            FlightNotFoundException, ExistingFlightSchedulePlanException, ExistingFareException,
            UnknownPersistenceException, InputDataValidationException;
    
    public FlightSchedulePlanEntity createNewFlightSchedulePlanWeekly(FlightSchedulePlanEntity plan, List<FareEntity> fares, long flightID, Pair<Date, Double> pair, int recurrent) throws InputDataValidationException, ExistingFareException, UnknownPersistenceException,
            FlightNotFoundException, ExistingFlightSchedulePlanException, ViolationConstraintsException;
    
    public FlightSchedulePlanEntity createNewFlightSchedulePlanMultiple(FlightSchedulePlanEntity plan, List<Pair<Date, Double>> details ,
            List<FareEntity> fares, long flightID) throws InputDataValidationException, ExistingFareException,
            UnknownPersistenceException, FlightNotFoundException,   ExistingFlightSchedulePlanException, ViolationConstraintsException;
    
    public List<FlightSchedulePlanEntity> retrieveAllFlightSchedulePlan() throws FlightSchedulePlanDoNotExistException;
    
    public FlightSchedulePlanEntity retrieveFlightSchedulePlanEntityById(Long flightSchedulePlanID) throws FlightSchedulePlanDoNotExistException;
    
    public void associateExistingPlanToComplementaryPlan(Long sourcFlightSchedulePlanID, Long returnFlightSchedulePlanID) throws FlightSchedulePlanDoNotExistException;
    
    public void deleteFlightSchedulePlan(Long flightSchedulePlanID) throws FlightSchedulePlanDoNotExistException, FlightScheduleNotFoundException, FareDoNotExistException;
    
    public void processFlightSchedulePlanDeletion(FlightSchedulePlanEntity plan);
    
    
    
}
