/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightRouteEntity;
import exceptions.FlightRouteExistException;
import exceptions.UnknownPersistenceException;
import javax.ejb.Local;

/**
 *
 * @author kahjy
 */
@Local
public interface FlightRouteSessionBeanLocal {
    public FlightRouteEntity createNewFlightRoute(FlightRouteEntity flightRoute, long originAirportID, long destinationAirportID) throws FlightRouteExistException, UnknownPersistenceException, AirportDoNotExistException;

    public FlightRouteEntity viewFlightRouteByOriginAndDestination (String originAirportIATA, String destinationAirportIATA) throws FlightRouteDoNotExistException;

}
