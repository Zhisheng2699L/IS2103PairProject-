/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FareEntity;
import entity.FlightEntity;
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
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author foozh
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {
    
    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;
    
    
    @EJB
    private SeatsAvailabilitySessionBeanLocal seatsAvailabilitySessionBean;
    
    @EJB
    private FlightSessionBeanLocal flightSessionBean; 

    private final ValidatorFactory customValidatorFactory;
    private final Validator customValidator;
    
    
    public FlightScheduleSessionBean() {
        customValidatorFactory = Validation.buildDefaultValidatorFactory();
        customValidator = customValidatorFactory.getValidator();
    }
    
    @Override
    public FlightScheduleEntity createNewSchedule(FlightSchedulePlanEntity flightSchedulePlan, FlightScheduleEntity schedule) throws ViolationConstraintsException {
        Set<ConstraintViolation<FlightScheduleEntity>> constraintViolations = customValidator.validate(schedule);
        if (constraintViolations.isEmpty()) {
            em.persist(schedule);

            schedule.setFlightSchedulePlan(flightSchedulePlan);
            if (!flightSchedulePlan.getFlightSchedule().contains(schedule)) {
                flightSchedulePlan.getFlightSchedule().add(schedule);
            }

            return schedule;

        } else {
            throw new ViolationConstraintsException("Constraints violated");
        }
    }
    
    @Override
    public FlightScheduleEntity retrieveFlightScheduleById(Long flightScheduleID) throws FlightScheduleNotFoundException {
        FlightScheduleEntity schedule = em.find(FlightScheduleEntity.class, flightScheduleID);
        
        if(schedule != null) {
            return schedule;
        } else {
            throw new FlightScheduleNotFoundException("Flight Schedule " + flightScheduleID + " not found!");      
        }
        
    }
    
    @Override
    public FlightScheduleEntity retrieveUnmanagedFlightScheduleById(Long flightScheduleID) throws FlightScheduleNotFoundException {
        FlightScheduleEntity schedule = em.find(FlightScheduleEntity.class, flightScheduleID);

        if (schedule != null) {
            em.detach(schedule);
            return schedule;
        } else {
            throw new FlightScheduleNotFoundException("Flight Schedule " + flightScheduleID + " not found!");
        }

    }
    
    @Override
    public void deleteSchedule(List<FlightScheduleEntity> flightSchedule) {

        for (FlightScheduleEntity s : flightSchedule) {
            seatsAvailabilitySessionBean.deleteSeatInventory(s.getSeatInventory());
            em.remove(s);
        }
    }
    
    @Override
    public List<FlightScheduleEntity> getFlightSchedules(String departure, String destination, Date date, CabinClassTypeEnum cabin) throws FlightNotFoundException {
        List<FlightScheduleEntity> schedule = new ArrayList<>();
        List<FlightEntity> flights = flightSessionBean.retrieveAllFlightByFlightRoute(departure, destination);

        for (FlightEntity f : flights) {
            for (FlightSchedulePlanEntity plan : f.getFlightSchedulePlan()) {
                if (plan.isDisabled()) {
                    continue;
                }
                for (FlightScheduleEntity scheduleEntity : plan.getFlightSchedule()) {
                    if (isMatchingSchedule(scheduleEntity, date, cabin)) {
                        schedule.add(scheduleEntity);
                    }
                }
            }
        }

        Collections.sort(schedule, (FlightScheduleEntity schedule1, FlightScheduleEntity schedule2)
                -> schedule1.getDepartureDateTime().compareTo(schedule2.getDepartureDateTime()));

        return schedule;
    }

    private boolean isMatchingSchedule(FlightScheduleEntity schedule, Date date, CabinClassTypeEnum cabin) {
        if (cabin == null || hasMatchingCabin(schedule, cabin)) {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(schedule.getDepartureDateTime());
            c2.setTime(date);
            return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
                    && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
        }
        return false;
    }

    private boolean hasMatchingCabin(FlightScheduleEntity schedule, CabinClassTypeEnum cabin) {
        for (SeatInventoryEntity s : schedule.getSeatInventory()) {
            if (s.getCabin().getCabinClassType().equals(cabin)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public List<FlightScheduleEntity> getUnManagedFlightSchedules(String departure, String destination, Date date, CabinClassTypeEnum cabin) throws FlightNotFoundException {
        List<FlightScheduleEntity> schedule = new ArrayList<>();
        List<FlightEntity> flights = flightSessionBean.retrieveAllFlightByFlightRoute(departure, destination);

        for (FlightEntity f : flights) {
            schedule.addAll(filterUnmanagedFlightSchedules(f, date, cabin));
        }

        Collections.sort(schedule, (FlightScheduleEntity schedule1, FlightScheduleEntity schedule2) ->
                schedule1.getDepartureDateTime().compareTo(schedule2.getDepartureDateTime()));

        return schedule;
    }

    private List<FlightScheduleEntity> filterUnmanagedFlightSchedules(FlightEntity flight, Date date, CabinClassTypeEnum cabin) {
        List<FlightScheduleEntity> filteredSchedules = new ArrayList();

        for (FlightSchedulePlanEntity p : flight.getFlightSchedulePlan()) {
            if (!p.isDisabled()) {
                for (FlightScheduleEntity schedule : p.getFlightSchedule()) {
                    if (isMatchingSchedule(schedule, date, cabin)) {
                        em.detach(schedule);
                        filteredSchedules.add(schedule);
                    }
                }
            }
        }

        return filteredSchedules;
    }
   
    public List<Pair<FlightScheduleEntity, FlightScheduleEntity>> getIndirectUnManagedFlightSchedules(
            String departure, String destination, Date date, CabinClassTypeEnum cabin) throws FlightNotFoundException {
        List<Pair<FlightScheduleEntity, FlightScheduleEntity>> schedule = new ArrayList<>();
        List<FlightEntity[]> flights = flightSessionBean.retrieveAllIndirectFlightByFlightRoute(departure, destination);

        for (Object[] pair : flights) {
            FlightEntity firstFlight = (FlightEntity) pair[0];
            FlightEntity secondFlight = (FlightEntity) pair[1];

            for (FlightScheduleEntity flightSchedule : getValidFlightSchedules(firstFlight, date, cabin)) {
                for (FlightScheduleEntity f2 : getValidFlightSchedules(secondFlight, date, cabin)) {
                    if (isTimeGapAcceptable(flightSchedule, f2)) {
                        schedule.add(new Pair<>(flightSchedule, f2));
                    }
                }
            }
        }

        sortFlightSchedulesByDeparture(schedule);
        return schedule;
    }

    private List<FlightScheduleEntity> getValidFlightSchedules(FlightEntity flight, Date date, CabinClassTypeEnum cabin) {
        List<FlightScheduleEntity> validSchedules = new ArrayList<>();

        for (FlightSchedulePlanEntity plan : flight.getFlightSchedulePlan()) {
            if (!plan.isDisabled()) {
                for (FlightScheduleEntity flightSchedule : plan.getFlightSchedule()) {
                    if (hasMatchingCabin(flightSchedule, cabin)) {
                        validSchedules.add(flightSchedule);
                    }
                }
            }
        }

        return validSchedules;
    }

    private boolean isTimeGapAcceptable(FlightScheduleEntity flightSchedule1, FlightScheduleEntity flightSchedule2) {
        Calendar arrivalTime1 = calculateArrivalTime(flightSchedule1);
        Calendar departureTime2 = Calendar.getInstance();
        departureTime2.setTime(flightSchedule2.getDepartureDateTime());
        long gap = Duration.between(arrivalTime1.toInstant(), departureTime2.toInstant()).toHours();
        return gap >= 2L && gap <= 12L;
    }

    private Calendar calculateArrivalTime(FlightScheduleEntity flightSchedule) {
        Calendar departureTime = Calendar.getInstance();
        departureTime.setTime(flightSchedule.getDepartureDateTime());
        double duration = flightSchedule.getDuration();
        int hours = (int) duration;
        int minutes = (int) ((duration % 1) * 60);
        departureTime.add(Calendar.HOUR_OF_DAY, hours);
        departureTime.add(Calendar.MINUTE, minutes);
        int timeDifference = flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getGmt()
                - flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getGmt();
        departureTime.add(Calendar.HOUR_OF_DAY, timeDifference);
        return departureTime;
    }

    private void sortFlightSchedulesByDeparture(List<Pair<FlightScheduleEntity, FlightScheduleEntity>> schedule) {
        schedule.sort(Comparator.comparing(pair -> pair.getKey().getDepartureDateTime()));
    }
    
    public FareEntity retrieveLowestFare(FlightScheduleEntity flightSchedule, CabinClassTypeEnum cabinClassType)
            throws FlightScheduleNotFoundException, CabinClassNotFoundException {
        FlightScheduleEntity flightScheduleEntity = retrieveFlightScheduleById(flightSchedule.getFlightScheduleId());
        
        List<FareEntity> fares = flightScheduleEntity.getFlightSchedulePlan().getFares();
        List<FareEntity> ccfares = filterFaresByCabinClassType(fares, cabinClassType);

        if (ccfares.isEmpty()) {
            throw new CabinClassNotFoundException("Cabin class not found");
        }

        return findLowestFare(ccfares);
    }

    private List<FareEntity> filterFaresByCabinClassType(List<FareEntity> fares, CabinClassTypeEnum cabinClassType) {
        List<FareEntity> ccfares = new ArrayList<>();
        for (FareEntity fare : fares) {
            if (fare.getCabinClassType().equals(cabinClassType)) {
                ccfares.add(fare);
            }
        }
        return ccfares;
    }

    private FareEntity findLowestFare(List<FareEntity> fares) {
        FareEntity lowestFare = fares.get(0);
        for (FareEntity fare : fares) {
            if (fare.getFareAmount().compareTo(lowestFare.getFareAmount()) < 0) {
                lowestFare = fare;
            }
        }
        return lowestFare;
    }
    
    public FareEntity getHighestUnmanagedFare(FlightScheduleEntity flightScheduleEntity, CabinClassTypeEnum type) throws FlightScheduleNotFoundException,
            CabinClassNotFoundException {
        FlightScheduleEntity flightSchedule = retrieveFlightScheduleById(flightScheduleEntity.getFlightScheduleId());
        List<FareEntity> fares = flightSchedule.getFlightSchedulePlan().getFares();
        List<FareEntity> ccfares = new ArrayList<>();
        for (FareEntity fare : fares) {
            if (fare.getCabinClassType().equals(type)) {
                ccfares.add(fare);
            }
        }
        if (ccfares.isEmpty()) {
            throw new CabinClassNotFoundException("Cabin Class not found");
        }

        FareEntity biggest = ccfares.get(0);
        for (FareEntity fare : ccfares) {
            if (fare.getFareAmount().compareTo(biggest.getFareAmount()) > 0) {
                biggest = fare;
            }
        }
        em.detach(biggest);
        return biggest;
    }

    public SeatInventoryEntity getCorrectSeatInventory(FlightScheduleEntity flightSchedule, CabinClassTypeEnum cabinClassType) throws FlightScheduleNotFoundException,
            SeatInventoryNotFoundException {
        FlightScheduleEntity flightScheduleEntity = retrieveFlightScheduleById(flightSchedule.getFlightScheduleId());
        for (SeatInventoryEntity seat : flightScheduleEntity.getSeatInventory()) {
            if (seat.getCabin().getCabinClassType() == cabinClassType) {
                return seat;
            }
        }
        throw new SeatInventoryNotFoundException("No such seat inventory");
    }

    public SeatInventoryEntity getCorrectSeatInventoryUnmanaged(FlightScheduleEntity flightSchedule, CabinClassTypeEnum cabinClassType) throws FlightScheduleNotFoundException,
            SeatInventoryNotFoundException {
        FlightScheduleEntity flightScheduleEntity = retrieveFlightScheduleById(flightSchedule.getFlightScheduleId());
        for (SeatInventoryEntity seat : flightScheduleEntity.getSeatInventory()) {
            if (seat.getCabin().getCabinClassType() == cabinClassType) {
                em.detach(seat);
                return seat;
            }
        }
        throw new SeatInventoryNotFoundException("No such seat inventory");
    }

    public FlightScheduleEntity updateFlightSchedule(long flightScheduleId, Date newDepartureDateTime, double newFlightDuration) throws FlightScheduleNotFoundException, UpdateFlightScheduleException {
        FlightScheduleEntity flightSchedule = retrieveFlightScheduleById(flightScheduleId);

        for (FlightSchedulePlanEntity schedulePlan : flightSchedule.getFlightSchedulePlan().getFlight().getFlightSchedulePlan()) {
            for (FlightScheduleEntity schedule : schedulePlan.getFlightSchedule()) {
                if (schedule.getFlightScheduleId() == flightScheduleId) {
                    continue;
                }
                Date start1 = schedule.getDepartureDateTime();
                Date end1 = calculateEndTime(start1, schedule.getDuration());
                Date end2 = calculateEndTime(newDepartureDateTime, newFlightDuration);

                if (start1.before(end2) && newDepartureDateTime.before(end1)) {
                    throw new UpdateFlightScheduleException("Conflicts with existing flight schedules");
                }
            }
        }

        flightSchedule.setDepartureDateTime(newDepartureDateTime);
        flightSchedule.setDuration(newFlightDuration);
        em.flush();
        return flightSchedule;
    }
    
    private Date calculateEndTime(Date departureTime, double duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(departureTime);
        int hours = (int) duration;
        int minutes = (int) (duration % 1 * 60);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    
    public void deleteFlightSchedule(long flightScheduleId) throws FlightScheduleNotFoundException, UpdateFlightScheduleException {
        FlightScheduleEntity flightSchedule = retrieveFlightScheduleById(flightScheduleId);
        if (!flightSchedule.getReservations().isEmpty()) {
            throw new UpdateFlightScheduleException("Ticket has already been issued, unable to delete");
        } else {
            flightSchedule.getFlightSchedulePlan().getFlightSchedule().remove(flightSchedule);
            for (SeatInventoryEntity seats : flightSchedule.getSeatInventory()) {
                em.remove(seats);
            }
            em.remove(flightSchedule);
        }
    }
}
