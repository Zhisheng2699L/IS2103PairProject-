/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AirportEntity;
import entity.FlightRouteEntity;
import exceptions.AirportDoNotExistException;
import exceptions.FlightRouteDoNotExistException;
import exceptions.FlightRouteExistException;
import exceptions.UnknownPersistenceException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author kahjy
 */
@Stateless
public class FlightRouteSessionBean implements FlightRouteSessionBeanRemote, FlightRouteSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;

    @EJB
    private AirportSessionBeanLocal airportSessionBean;

    @Override
    public FlightRouteEntity createNewFlightRoute(FlightRouteEntity flightRoute, long originAirportId, long destinationAirportId) throws FlightRouteExistException, UnknownPersistenceException, AirportDoNotExistException {
         try {
             AirportEntity originAirport = airportSessionBean.retrieveAirportById(originAirportId);
             AirportEntity destinationAirport = airportSessionBean.retrieveAirportById(destinationAirportId);
             
             em.persist(flightRoute);
             
             flightRoute.setOrigin(originAirport);
             flightRoute.setDestination(destinationAirport);
             
             em.flush();
             
             return flightRoute;
         } catch (AirportDoNotExistException ex) {
             throw new AirportDoNotExistException("Airport do not exist in System!");
         } catch (PersistenceException ex) {
             if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new FlightRouteExistException("Flight route already exists");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
         }
    }

    @Override
    public long setComplementaryFlightRoute(long routeID) throws FlightRouteDoNotExistException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FlightRouteEntity searchForFlightRouteByOriginAndDestination(String originAirportIATA, String destinationAirportIATA) throws FlightRouteDoNotExistException {
        Query query = em.createQuery("SELECT f FROM FlightRouteEntity f WHERE f.origin.IATACode = :codeOne AND f.destination.IATACode = :codeTwo AND f.disabled=false");
        query.setParameter("codeOrigin", originAirportIATA);
        query.setParameter("codeDestination", destinationAirportIATA);
        try{
            return (FlightRouteEntity)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightRouteDoNotExistException("Flight Route does not exist!");
        }
    }

    @Override
    public List<FlightRouteEntity> retrieveAllFlightRouteInOrder() throws FlightRouteDoNotExistException {
        Query query = em.createQuery("SELECT DISTINCT f FROM FlightRouteEntity f WHERE f.disabled=false ORDER BY f.origin.airportName ASC");
            List<FlightRouteEntity> result = query.getResultList();
            if (result.isEmpty()) {
                throw new FlightRouteDoNotExistException("No flight routes in system!");
            }
            int x = result.size()-1;
            while (x >= 0) {
                FlightRouteEntity flightroute = result.get(x);
                boolean replaced = false;
                for (int y = x - 2; y >= 0; y--) {
                    FlightRouteEntity otherflightroute = result.get(y);
                    if (otherflightroute.getComplementaryRoute()  != null
                            && otherflightroute.getComplementaryRoute().getFlightRouteId() == flightroute.getFlightRouteId()) {
                        result.remove(x);
                        result.add(y + 1, flightroute);
                        replaced = true;
                        break;
                        
                    }
                }
                if (replaced) {continue;}
                x--;
            }
            return result;
        }

    @Override
    public FlightRouteEntity enableFlightRoute(long originAirportId, long destinationAirportId) throws FlightRouteDoNotExistException {
        Query query = em.createQuery("SELECT f FROM FlightRouteEntity f WHERE f.origin.airportID = :codeOne AND f.destination.airportID = :codeTwo AND f.disabled=true");
        query.setParameter("codeOrigin", originAirportId);
        query.setParameter("codeDestination", destinationAirportId);
        try{
            FlightRouteEntity flight = (FlightRouteEntity) query.getSingleResult();
            flight.setDisabled(false);
            em.flush();
            return flight;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightRouteDoNotExistException("Disabled Flight Route does not exist in system");
        }
    }
    

    @Override
    public void removeFlightRoute(long flightRouteId) throws FlightRouteDoNotExistException {
        FlightRouteEntity route = retrieveFlightRouteById(flightRouteId);
        if (route.getFlights().isEmpty()) {
            
            // Disassociation of FK before removal
            if (route.getOriginRoute() != null) {
                route.getOriginRoute().setComplementaryRoute(null);
                route.getOriginRoute().setOriginRoute(null);
            }
            route.setComplementaryRoute(null);
            route.setOriginRoute(null);
            
            em.remove(route);
        } else {
            route.setDisabled(true);
        }
    }
    
    
    @Override
    public FlightRouteEntity retrieveFlightRouteById(Long id) throws FlightRouteDoNotExistException {
        FlightRouteEntity route = em.find(FlightRouteEntity.class, id);
        if (route == null || route.isDisabled() == true) {
            throw new FlightRouteDoNotExistException("Flight Route does not exist!");
        }
        return route;
    }

}
