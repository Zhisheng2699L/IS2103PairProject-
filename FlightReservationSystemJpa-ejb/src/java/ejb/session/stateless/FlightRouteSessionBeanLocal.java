/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightRouteEntity;
import exceptions.AirportDoNotExistException;
import exceptions.FlightRouteDoNotExistException;
import exceptions.FlightRouteExistException;
import exceptions.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author kahjy
 */
@Local
public interface FlightRouteSessionBeanLocal {
    public FlightRouteEntity createNewFlightRoute(FlightRouteEntity flightRoute, long originAirportID, long destinationAirportID) throws FlightRouteExistException, UnknownPersistenceException, AirportDoNotExistException;

    public long setComplementaryFlightRoute(long routeID) throws FlightRouteDoNotExistException;
    
    public FlightRouteEntity searchForFlightRouteByOriginAndDestination (String originAirportIATA, String destinationAirportIATA) throws FlightRouteDoNotExistException;

    public List<FlightRouteEntity> retrieveAllFlightRouteInOrder() throws FlightRouteDoNotExistException;
    
    public FlightRouteEntity enableFlightRoute(long originAirportId, long destinationAirportId) throws FlightRouteDoNotExistException;
            
    public FlightRouteEntity retrieveFlightRouteById(Long id) throws FlightRouteDoNotExistException;
    
    public void removeFlightRoute (long flightRouteId) throws FlightRouteDoNotExistException;
}
