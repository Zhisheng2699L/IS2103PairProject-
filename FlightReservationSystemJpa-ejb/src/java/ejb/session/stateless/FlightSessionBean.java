/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import exceptions.AircraftConfigNotFoundException;
import exceptions.ExistingFlightException;
import exceptions.FlightNotFoundException;
import exceptions.FlightRouteDoNotExistException;
import exceptions.UnknownPersistenceException;
import exceptions.ViolationConstraintsException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;

/**
 *
 * @author foozh
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;
    
    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBean;

    @EJB
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBean;

    private final ValidatorFactory customValidatorFactory;
    private final Validator customValidator;
    
    public FlightSessionBean() {
        customValidatorFactory = Validation.buildDefaultValidatorFactory();
        customValidator = customValidatorFactory.getValidator();       
    }
    
    @Override
    public FlightEntity createNewFlight(FlightEntity flight, Long chosenRoute, Long chosenConfig) throws ExistingFlightException,
            UnknownPersistenceException, FlightRouteDoNotExistException, AircraftConfigNotFoundException,ViolationConstraintsException {
        if (flight != null) {
            Set<ConstraintViolation<FlightEntity>> constraintViolations = customValidator.validate(flight);
            if (!constraintViolations.isEmpty()) {
                throw new ViolationConstraintsException("Constraints Violated");
            }
            try {
                FlightRouteEntity flightRoute = flightRouteSessionBean.retrieveFlightRouteById(chosenRoute);
                AircraftConfigurationEntity aircraftConfig = aircraftConfigurationSessionBean.retriveAircraftConfigByID(chosenConfig);
                flight.setAircraftConfig(aircraftConfig);
                flight.setFlightRoute(flightRoute);
                em.persist(flight);
                em.flush();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ExistingFlightException("Flight with " + flight.getFlightNum() + " already exist");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        return flight;

    }
    
    @Override
    public FlightEntity enableFlight(String flightNumber, long routeID, long configID) throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.flightNum = :num AND f.disabled=true "
                + "AND f.flightRoute.flightRouteID = :route AND f.aircraftConfig.aircraftConfigID = :config");
        query.setParameter("num", flightNumber);
        query.setParameter("route", routeID);
        query.setParameter("config", configID);
        try {
            FlightEntity flight = (FlightEntity) query.getSingleResult();
            flight.setDisabled(false);
            em.flush();
            return flight;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightNotFoundException("Disabled flight deos not exist in system");
        }
    }
    
    @Override
    public FlightEntity retreiveFlightById(Long id) throws FlightNotFoundException {
        FlightEntity flight = em.find(FlightEntity.class, id);
        if (flight != null && flight.isDisabled() == false) {
            return flight;
        } else {
            throw new FlightNotFoundException("Flight " + id + " does not exist in the system");
        }
    }
    
    @Override
    public FlightEntity retrieveFlightByFlightNumber(String flightNum) throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.flightNum = :num AND f.disabled <> true");
        query.setParameter("num", flightNum);
        try {
            return (FlightEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightNotFoundException("Flight does not exist in system");
        }
    }
    
    @Override
    public void associateExistingFlightWithReturnFlight(Long sourceFlightID, Long returnFlightID) throws FlightNotFoundException {
        FlightEntity sourceFlight = retreiveFlightById(sourceFlightID);
        FlightEntity returnFlight = retreiveFlightById(returnFlightID);

        sourceFlight.setReturnFlight(returnFlight);
        returnFlight.setOriginFlight(sourceFlight);
    }
    
    @Override
    public List<FlightEntity> retrieveAllFlight() throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.disabled=false ORDER BY SUBSTRING(f.flightNum, 3) ASC");
        List<FlightEntity> result = query.getResultList();

        if (result.isEmpty()) {
            throw new FlightNotFoundException("No flights found in the system");
        }

        Map<Long, FlightEntity> flightIdToFlightMap = result.stream()
                .collect(Collectors.toMap(FlightEntity::getFlightId, flight -> flight));

        for (int i = 0; i < result.size(); i++) {
            FlightEntity flight = result.get(i);
            FlightEntity returnFlight = flight.getReturnFlight();
            if (returnFlight != null) {
                FlightEntity originalFlight = flightIdToFlightMap.get(returnFlight.getFlightId());
                if (originalFlight != null) {
                    result.remove(i);
                    result.add(i, originalFlight);
                }
            }
        }
        return result;
    }
    
    @Override
    public List<FlightEntity> retrieveAllFlightByFlightRoute(String originIATACode, String destinationIATACode) throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.disabled=false AND f.flightRoute.origin.IATACode = :origin "
                + "AND f.flightRoute.destination.IATACode = :dest ORDER BY SUBSTRING(f.flightNum, 3) ASC");
        query.setParameter("origin", originIATACode);
        query.setParameter("dest", destinationIATACode);
        List<FlightEntity> result = query.getResultList();
        if (result.isEmpty()) {
            throw new FlightNotFoundException("Flights with flight route from " + originIATACode + " to " + destinationIATACode + " does not exist in system");
        }
        return result;
    }
    
    @Override
    public List<FlightEntity[]> retrieveAllIndirectFlightByFlightRoute(String originIATACode, String destinationIATACode) throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f1, f2 FROM FlightEntity f1, FlightEntity f2 WHERE f1.disabled=false "
                + "AND f2.disabled=false AND f1.flightRoute.origin.IATACode = :origin AND "
                + "f2.flightRoute.destination.IATACode = :dest AND "
                + "f1.flightRoute.destination.IATACode=f2.flightRoute.origin.IATACode");
        query.setParameter("origin", originIATACode);
        query.setParameter("dest", destinationIATACode);
        List<FlightEntity[]> result = query.getResultList();
        if (result.isEmpty()) {
            throw new FlightNotFoundException("Indirect flights with flight route from " + originIATACode + " to " + destinationIATACode + " does not exist in system");
        }
        return result;
    }
    
    @Override
    public void updateFlight(FlightEntity oldFlight) throws FlightNotFoundException
            , FlightRouteDoNotExistException, UnknownPersistenceException,
            AircraftConfigNotFoundException,ExistingFlightException, ViolationConstraintsException {

        Set<ConstraintViolation<FlightEntity>> constraintViolations = customValidator.validate(oldFlight);
        if (constraintViolations.isEmpty()) {
            try {
                FlightEntity updateFlightEntity = retreiveFlightById(oldFlight.getFlightId());

                if (!updateFlightEntity.getFlightNum().equals(oldFlight.getFlightNum())) {
                    throw new FlightNotFoundException("Flight not found");
                }
                
                // Aircraft Config
                Long newAircraftConfigId = updateFlightEntity.getAircraftConfig().getAircraftConfigId();
                Long oldAircraftConfigId = oldFlight.getAircraftConfig().getAircraftConfigId();

                if (!Objects.equals(newAircraftConfigId, oldAircraftConfigId)) {
                    AircraftConfigurationEntity newAircraftConfig = aircraftConfigurationSessionBean.retriveAircraftConfigByID(oldAircraftConfigId);
                    updateFlightEntity.setAircraftConfig(newAircraftConfig);
                }

                // Flight Route
                Long newFlightRouteID = updateFlightEntity.getFlightRoute().getFlightRouteId();
                Long oldFlightRouteID = oldFlight.getFlightRoute().getFlightRouteId();

                if (!Objects.equals(newFlightRouteID, oldFlightRouteID)) {
                    FlightRouteEntity newFlightRoute = flightRouteSessionBean.retrieveFlightRouteById(newFlightRouteID);
                    FlightRouteEntity oldFlightRoute = flightRouteSessionBean.retrieveFlightRouteById(oldFlightRouteID);

                    oldFlightRoute.getFlights().remove(updateFlightEntity);
                    newFlightRoute.getFlights().add(updateFlightEntity);
                    updateFlightEntity.setFlightRoute(oldFlightRoute);
                }

                // Source Flight
                FlightEntity newOriginFlight = updateFlightEntity.getOriginFlight();
                FlightEntity oldOriginFlight = oldFlight.getOriginFlight();

                if (newOriginFlight != null) {
                    if (oldOriginFlight != null && !Objects.equals(newOriginFlight.getFlightId(), oldOriginFlight.getFlightId())) {
                        // Dissociation of the old origin flight
                        oldOriginFlight.setReturnFlight(null);
                        updateFlightEntity.setOriginFlight(null);

                        // Association of the new orign flight
                        FlightEntity newSource = this.retreiveFlightById(oldOriginFlight.getFlightId());
                        newSource.setReturnFlight(updateFlightEntity);
                        updateFlightEntity.setOriginFlight(newSource);
                    } else if (oldOriginFlight == null) {
                        // Dissociation of the old origin flight
                        updateFlightEntity.getOriginFlight().setOriginFlight(null);
                        updateFlightEntity.setOriginFlight(null);
                    }
                } else {
                    if (oldOriginFlight != null) {
                        // Association of the new origin flight
                        FlightEntity newSource = this.retreiveFlightById(oldOriginFlight.getFlightId());
                        newSource.setReturnFlight(updateFlightEntity);
                        updateFlightEntity.setOriginFlight(newSource);
                    }
                }


                FlightEntity newReturnFlight = updateFlightEntity.getReturnFlight();
                FlightEntity oldReturnFlight = oldFlight.getReturnFlight();

                if (newReturnFlight != null) {
                    if (oldReturnFlight != null && !Objects.equals(newReturnFlight.getFlightId(), oldReturnFlight.getFlightId())) {
                        // Dissociation of old return flight
                        oldReturnFlight.setOriginFlight(null);
                        updateFlightEntity.setReturnFlight(null);

                        // Association of new return flight
                        FlightEntity newReturning = this.retreiveFlightById(oldReturnFlight.getFlightId());
                        newReturning.setOriginFlight(updateFlightEntity);
                        updateFlightEntity.setReturnFlight(newReturning);
                    } else if (oldReturnFlight == null) {
                        // Dissociation of old return flight
                        updateFlightEntity.getReturnFlight().setOriginFlight(null);
                        updateFlightEntity.setReturnFlight(null);
                    }
                } else if (oldReturnFlight != null) {
                    // Association of new return flight
                    FlightEntity newReturning = this.retreiveFlightById(oldReturnFlight.getFlightId());
                    newReturning.setReturnFlight(updateFlightEntity);
                    updateFlightEntity.setReturnFlight(newReturning);
                }


                em.flush();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ExistingFlightException("Flight with " + oldFlight.getFlightNum() + " already exist");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new ViolationConstraintsException("Constraints Violated");
        }
    }

    @Override
    public void deleteFlight(Long flightID) throws FlightNotFoundException {
        FlightEntity flight = retreiveFlightById(flightID);
        if (flight.getFlightSchedulePlan().isEmpty()) {
          
            flight.getFlightRoute().getFlights().remove(flight);
            if (flight.getReturnFlight() != null) {
                flight.getReturnFlight().setOriginFlight(null);
            }
            flight.setReturnFlight(null);

            if (flight.getOriginFlight() != null) {
                flight.getOriginFlight().setReturnFlight(null);
            }
            flight.setOriginFlight(null);

            em.remove(flight);
        } else {
            flight.setDisabled(true);
        }
    } 
}
