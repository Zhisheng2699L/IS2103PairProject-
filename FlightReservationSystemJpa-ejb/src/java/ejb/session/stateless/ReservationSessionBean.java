/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightScheduleEntity;
import entity.ItineraryEntity;
import entity.PassengerEntity;
import entity.ReservationEntity;
import entity.SeatInventoryEntity;
import enumeration.CabinClassTypeEnum;
import exceptions.FlightScheduleNotFoundException;
import exceptions.InputDataValidationException;
import exceptions.ItineraryDoNotExistException;
import exceptions.ReservationDoNotExistException;
import exceptions.ReservationExistException;
import exceptions.SeatAlreadyBookedException;
import exceptions.SeatSlotNotFoundException;
import exceptions.UnknownPersistenceException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author kahjy
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;
    @EJB
    private ItinerarySessionBeanLocal itinerarySessionBean;
    @EJB
    private CabinClassSessionBeanLocal cabinClassSessionBean;
    @EJB
    private SeatsInventorySessionBean seatsInventorySessionBean;
    @EJB
    private FareSessionBeanLocal fareSessionBean;
    @EJB
    private UserSessionBeanLocal userSessionBean; 

    
    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;
    
    //validation needed
    private final ValidatorFactory validatorFactory;
    private final Validator validator;  

    public ReservationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
@Override
public long createNewReservation(ReservationEntity reservation, List<PassengerEntity> passengers, long flightScheduleId, long itineraryId)
        throws ReservationExistException, UnknownPersistenceException, FlightScheduleNotFoundException, SeatSlotNotFoundException, SeatAlreadyBookedException, ItineraryDoNotExistException, InputDataValidationException {

    validateReservation(reservation);

    try {
        FlightScheduleEntity flightSchedule = retrieveAndValidateFlightSchedule(flightScheduleId);
        ItineraryEntity itinerary = retrieveItinerary(itineraryId);
        SeatInventoryEntity seat = findSeatForCabinClass(reservation.getCabinClass(), flightSchedule);

        createReservationEntities(reservation, passengers, seat, flightSchedule, itinerary);

        return reservation.getReservationId();
    } catch (PersistenceException ex) {
        handleReservationPersistenceException(ex);
    }

    return 0;
}
    
    public void validateReservation(ReservationEntity reservation) throws InputDataValidationException {
        Set<ConstraintViolation<ReservationEntity>> constraintViolations = validator.validate(reservation);
        if (!constraintViolations.isEmpty()) {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    public FlightScheduleEntity retrieveAndValidateFlightSchedule(long flightScheduleId) throws FlightScheduleNotFoundException {
        return flightScheduleSessionBean.retrieveFlightScheduleById(flightScheduleId);
    }
    
    public ItineraryEntity retrieveItinerary(long itineraryId) throws ItineraryDoNotExistException {
        return itinerarySessionBean.retrieveItineraryByID(itineraryId);
    }
    
    public SeatInventoryEntity findSeatForCabinClass(CabinClassTypeEnum cabinClassType, FlightScheduleEntity flightSchedule)
        throws SeatSlotNotFoundException {
    return flightSchedule.getSeatInventory().stream()
            .filter(seat -> seat.getCabin().getCabinClassType() == cabinClassType)
            .findFirst()
            .orElseThrow(() -> new SeatSlotNotFoundException("Seat for specified class not found"));
    }
    
    public void createReservationEntities(ReservationEntity reservation, List<PassengerEntity> passengers, SeatInventoryEntity seat,
                                           FlightScheduleEntity flightSchedule, ItineraryEntity itinerary) {
        em.persist(reservation);
        passengers.forEach(passenger -> {
            em.persist(passenger);
            reservation.getPassenger().add(passenger);
            try {
                seatsInventorySessionBean.bookSeat(seat.getSeatInventoryId(), passenger.getSeatNumber());
            } catch (SeatAlreadyBookedException ex) {
                Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SeatSlotNotFoundException ex) {
                Logger.getLogger(ReservationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        flightSchedule.getReservations().add(reservation);
        reservation.setFlightSchedule(flightSchedule);

        reservation.setItinerary(itinerary);
        itinerary.getReservations().add(reservation);

        em.flush();
    }
    
    public void handleReservationPersistenceException(PersistenceException ex) throws UnknownPersistenceException, ReservationExistException {
        if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
            throw new ReservationExistException("Reservation already exist");
        } else {
            throw new UnknownPersistenceException(ex.getMessage());
    }
}
    
    //need to provide error message if data input is incorrect.
    public String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ReservationEntity>> constraintViolations) {
        StringBuilder errorMessage = new StringBuilder("Input data validation error!");
        for (ConstraintViolation constraintViolation : constraintViolations) {
            errorMessage.append("\n\t").append(constraintViolation.getPropertyPath())
                    .append(" - ").append(constraintViolation.getInvalidValue())
                    .append("; ").append(constraintViolation.getMessage());
        }
        return errorMessage.toString();
    }
    
    @Override
    public ReservationEntity retrieveReservationById(long id) throws ReservationDoNotExistException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
