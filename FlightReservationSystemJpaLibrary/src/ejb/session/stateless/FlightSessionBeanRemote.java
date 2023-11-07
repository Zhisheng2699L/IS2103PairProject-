/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightEntity;
import exceptions.AircraftConfigNotFoundException;
import exceptions.ExistingFlightException;
import exceptions.FlightNotFoundException;
import exceptions.FlightRouteDoNotExistException;
import exceptions.UnknownPersistenceException;
import exceptions.ViolationConstraintsException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author foozh
 */
@Remote
public interface FlightSessionBeanRemote {
    
    public FlightEntity createNewFlight(FlightEntity flight, Long chosenRoute, Long chosenConfig) throws ExistingFlightException,
            UnknownPersistenceException, FlightRouteDoNotExistException, AircraftConfigNotFoundException, ViolationConstraintsException;
    
    public FlightEntity enableFlight(String flightNumber, long routeID, long configID) throws FlightNotFoundException;
    
    public FlightEntity retreiveFlightById(Long id) throws FlightNotFoundException;
    
    public FlightEntity retrieveFlightByFlightNumber(String flightNum) throws FlightNotFoundException;
    
    public void associateExistingFlightWithReturnFlight(Long sourceFlightID, Long returnFlightID) throws FlightNotFoundException;
    
    public List<FlightEntity> retrieveAllFlight() throws FlightNotFoundException;
    
    public List<FlightEntity> retrieveAllFlightByFlightRoute(String originIATACode, String destinationIATACode) throws FlightNotFoundException;
    
    public List<FlightEntity[]> retrieveAllIndirectFlightByFlightRoute(String originIATACode, String destinationIATACode) throws FlightNotFoundException;
    
    public void updateFlight(FlightEntity oldFlight) throws FlightNotFoundException
            , FlightRouteDoNotExistException, UnknownPersistenceException,
            AircraftConfigNotFoundException,ExistingFlightException, ViolationConstraintsException;
    
    public void deleteFlight(Long flightID) throws FlightNotFoundException;
    
}
