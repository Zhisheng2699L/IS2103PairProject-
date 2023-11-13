/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClassEntity;
import entity.FareEntity;
import entity.FlightEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.SeatInventoryEntity;
import exceptions.ExistingFareException;
import exceptions.ExistingFlightSchedulePlanException;
import exceptions.FlightNotFoundException;
import exceptions.FlightSchedulePlanDoNotExistException;
import exceptions.InputDataValidationException;
import exceptions.UnknownPersistenceException;
import exceptions.ViolationConstraintsException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javafx.util.Pair;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;

/**
 *
 * @author foozh
 */
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;
    
    @EJB
    private SeatsAvailabilitySessionBeanLocal seatsAvailabilitySessionBean;
    @EJB
    private FareSessionBeanLocal fareSessionBean;
    @EJB
    private FlightSessionBeanLocal flightSessionBean;
    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;
    
    @Resource
    private EJBContext eJBContext;
    
    private final ValidatorFactory customValidatorFactory;
    private final Validator customValidator;
    
    
    public FlightSchedulePlanSessionBean() {
        customValidatorFactory = Validation.buildDefaultValidatorFactory();
        customValidator = customValidatorFactory.getValidator();
    }
    
    @Override
    public FlightSchedulePlanEntity createNewFlightSchedulePlan(FlightSchedulePlanEntity plan, List<FareEntity> fares,
            long flightId, Pair<Date, Double> pair, int recurrent) throws ViolationConstraintsException,
            FlightNotFoundException, ExistingFlightSchedulePlanException, ExistingFareException,
            UnknownPersistenceException, InputDataValidationException {
         Set<ConstraintViolation<FlightSchedulePlanEntity>>constraintViolations = customValidator.validate(plan);
         if (constraintViolations.isEmpty()) {
             
            try {
                
              if (recurrent == 0) {
                    FlightScheduleEntity schedule = new FlightScheduleEntity(pair.getKey(), pair.getValue());
                    flightScheduleSessionBean.createNewSchedule(plan, schedule);
                } else {
                    
                    Date presentDate = pair.getKey();
                    Date endDate = plan.getRecurringEndDate();
                    
                    for (Calendar c = Calendar.getInstance(); endDate.compareTo(c.getTime()) > 0; c.add(Calendar.DAY_OF_MONTH, recurrent)) {
                        c.setTime(presentDate);
                        FlightScheduleEntity schedule = new FlightScheduleEntity(c.getTime(), pair.getValue());
                        flightScheduleSessionBean.createNewSchedule(plan, schedule);
                        presentDate = c.getTime();
                    }      
                }
                FlightToPlanAssociation(flightId, plan);
                for (FlightScheduleEntity fse : plan.getFlightSchedule()) {
                    for (CabinClassEntity cc : plan.getFlight().getAircraftConfig().getCabin()) {
                        SeatInventoryEntity seats = new SeatInventoryEntity(cc.getMaxSeatCapacity(), 0, cc.getMaxSeatCapacity());
                        seatsAvailabilitySessionBean.createSeatInventory(seats, fse, cc);
                    }
                }

                for (FareEntity fare : fares) {
                    fareSessionBean.createFareEntity(fare, plan);
                }
                
                em.persist(plan);
                em.flush();
                return plan;
            } catch (PersistenceException ex) {
                eJBContext.setRollbackOnly();
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ExistingFlightSchedulePlanException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            eJBContext.setRollbackOnly();
            throw new InputDataValidationException("Invalid");
        }
    }
    
    @Override
    public FlightSchedulePlanEntity createNewFlightSchedulePlanWeekly(FlightSchedulePlanEntity plan, List<FareEntity> fares, long flightID, Pair<Date, Double> pair, int recurrent) throws InputDataValidationException, ExistingFareException, UnknownPersistenceException,
            FlightNotFoundException, ExistingFlightSchedulePlanException, ViolationConstraintsException {
        Set<ConstraintViolation<FlightSchedulePlanEntity>> constraintViolations = customValidator.validate(plan);
        if (constraintViolations.isEmpty()) {
            try {
             
                Date presentDate = pair.getKey();
                Date endDate = plan.getRecurringEndDate();

                Calendar cal = Calendar.getInstance();
                cal.setTime(presentDate);
                FlightScheduleEntity schedule = new FlightScheduleEntity(cal.getTime(), pair.getValue());
                flightScheduleSessionBean.createNewSchedule(plan, schedule);

                boolean adjustToDayOfWeek = false;
                while (cal.get(Calendar.DAY_OF_WEEK) != recurrent) {
                    cal.add(Calendar.DATE, 1);
                    adjustToDayOfWeek = true;
                }

                if (adjustToDayOfWeek) {
                    schedule = new FlightScheduleEntity(cal.getTime(), pair.getValue());
                    flightScheduleSessionBean.createNewSchedule(plan, schedule);
                }

                cal.add(Calendar.DAY_OF_MONTH, 7);

                while (endDate.compareTo(cal.getTime()) > 0) {
                    schedule = new FlightScheduleEntity(cal.getTime(), pair.getValue());
                    flightScheduleSessionBean.createNewSchedule(plan, schedule);
                    cal.add(Calendar.DAY_OF_MONTH, 7);
                }

                FlightToPlanAssociation(flightID, plan);

                for (FlightScheduleEntity flightSchedule : plan.getFlightSchedule()) {
                    for (CabinClassEntity cc : plan.getFlight().getAircraftConfig().getCabin()) {
                        SeatInventoryEntity seats = new SeatInventoryEntity(cc.getMaxSeatCapacity(), 0, cc.getMaxSeatCapacity());
                        seatsAvailabilitySessionBean.createSeatInventory(seats, flightSchedule, cc);
                    }
                }

                for (FareEntity fare : fares) {
                    fareSessionBean.createFareEntity(fare, plan);
                }
                
                em.persist(plan);
                em.flush();
                return plan;
            } catch (PersistenceException ex) {
                eJBContext.setRollbackOnly();
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ExistingFlightSchedulePlanException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            eJBContext.setRollbackOnly();
            throw new InputDataValidationException("Invalid");
        }
    }


    private void FlightToPlanAssociation(Long flightID, FlightSchedulePlanEntity flightSchedulePlan) throws FlightNotFoundException,
            ExistingFlightSchedulePlanException {

        FlightEntity flight = flightSessionBean.retreiveFlightById(flightID);

        for (FlightSchedulePlanEntity fsp : flight.getFlightSchedulePlan()) {
            for (FlightScheduleEntity flightSchedule : fsp.getFlightSchedule()) {
                Date start1 = flightSchedule.getDepartureDateTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start1);
                double duration = flightSchedule.getDuration();
                Date end1 = calculateScheduleEndTime(start1,duration);
            
                for (FlightScheduleEntity flightSchedule2 : flightSchedulePlan.getFlightSchedule()) {
                    Date start2 = flightSchedule2.getDepartureDateTime();
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(start2);
                    double duration2 = flightSchedule2.getDuration();
                    Date end2 = calculateScheduleEndTime(start2,duration2);

                    if (isOverlapping(start1, end1, start2, end2)) {
           
                        throw new ExistingFlightSchedulePlanException("Overlaps with existing flight schedules");
                    }
                }
            }
        }

 
        List<FlightScheduleEntity> flightSchedule = flightSchedulePlan.getFlightSchedule();;
        for (int i = 0; i < flightSchedule.size(); i++) {
            Date start1 = flightSchedule.get(i).getDepartureDateTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start1);
            double duration = flightSchedule.get(i).getDuration();
            Date end1 = calculateScheduleEndTime(start1, duration);
            for (int j = i + 1; j < flightSchedule.size(); j++) {
                Date start2 = flightSchedule.get(j).getDepartureDateTime();
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(start2);
                double duration2 = flightSchedule.get(j).getDuration();
                Date end2 = calculateScheduleEndTime(start2, duration2);

                if (isOverlapping(start1, end1, start2, end2)) {
                    throw new ExistingFlightSchedulePlanException("Flight schedule overlaps");
                }
            }
        }

        flight.getFlightSchedulePlan().add(flightSchedulePlan);
        flightSchedulePlan.setFlight(flight);

    }
    
    private boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }
    
    private Date calculateScheduleEndTime(Date startTime, double duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        int hours = (int) duration;
        int minutes = (int) (duration % 1 * 60);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
    
    @Override
    public FlightSchedulePlanEntity createNewFlightSchedulePlanMultiple(FlightSchedulePlanEntity plan, List<Pair<Date, Double>> details ,
            List<FareEntity> fares, long flightID) throws InputDataValidationException, ExistingFareException,
            UnknownPersistenceException, FlightNotFoundException,   ExistingFlightSchedulePlanException, ViolationConstraintsException {
        Set<ConstraintViolation<FlightSchedulePlanEntity>> constraintViolations = customValidator.validate(plan);

        if (constraintViolations.isEmpty()) {
            try {
              
                int quantity = details.size();
                int i = 0;
                while (i < quantity) {
                    FlightScheduleEntity schedule = new FlightScheduleEntity(details.get(i).getKey(), details.get(i).getValue());
                    flightScheduleSessionBean.createNewSchedule(plan, schedule);

                    i++; 
                }
                FlightToPlanAssociation(flightID, plan);

                for (FlightScheduleEntity flightSchedule : plan.getFlightSchedule()) {
                    for (CabinClassEntity cabinClass : plan.getFlight().getAircraftConfig().getCabin()) {
                        SeatInventoryEntity seats = new SeatInventoryEntity(cabinClass.getMaxSeatCapacity(), 0, cabinClass.getMaxSeatCapacity());
                        seatsAvailabilitySessionBean.createSeatInventory(seats, flightSchedule, cabinClass);
                    }
                }

                for (FareEntity fare : fares) {
                    fareSessionBean.createFareEntity(fare, plan);
                }
                
                em.persist(plan);
                em.flush();
                return plan;
            } catch (PersistenceException ex) {
                eJBContext.setRollbackOnly();
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ExistingFlightSchedulePlanException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            eJBContext.setRollbackOnly();
            throw new InputDataValidationException("Invalid");
        }

    }
    
    @Override
    public List<FlightSchedulePlanEntity> retrieveAllFlightSchedulePlan() throws FlightSchedulePlanDoNotExistException {
        Query query = em.createQuery("SELECT DISTINCT p FROM FlightScheduleEntity f,"
                + " IN(f.flightSchedulePlan) p WHERE f.flightSchedulePlan.flightSchedulePlanId = p.flightSchedulePlanId "
                + "AND p.disabled = false ORDER BY p.flightNum ASC, f.departureDateTime DESC");
        List<FlightSchedulePlanEntity> result = query.getResultList();
        Collections.sort(result, new FlightSchedulePlanComparator());
        return result;
    }

    private class FlightSchedulePlanComparator implements Comparator<FlightSchedulePlanEntity> {
        @Override
        public int compare(FlightSchedulePlanEntity plan1, FlightSchedulePlanEntity plan2) {
           
            FlightSchedulePlanEntity complementaryOfPlan1 = plan1.getComplementary();
            FlightSchedulePlanEntity complementaryOfPlan2 = plan2.getComplementary();

            if (complementaryOfPlan1 != null && Objects.equals(complementaryOfPlan1.getFlightSchedulePlanId(), plan2.getFlightSchedulePlanId())) {
                return -1;
            } else if (complementaryOfPlan2 != null && Objects.equals(complementaryOfPlan2.getFlightSchedulePlanId(), plan1.getFlightSchedulePlanId())) {
                return 1; 
            }

            return 0;
        }
    }
    
    @Override
    public FlightSchedulePlanEntity retrieveFlightSchedulePlanEntityById(Long flightSchedulePlanID) throws FlightSchedulePlanDoNotExistException {
        FlightSchedulePlanEntity plan = em.find(FlightSchedulePlanEntity.class, flightSchedulePlanID);

        if (plan != null && plan.isDisabled() == false) {
            return plan;
        } else {
            throw new FlightSchedulePlanDoNotExistException("Flight Schedule Plan ID " + flightSchedulePlanID.toString() + " does not exist!");
        }
    }
    
    @Override
    public void associateExistingPlanToComplementaryPlan(Long sourcFlightSchedulePlanID, Long returnFlightSchedulePlanID) throws FlightSchedulePlanDoNotExistException {
        FlightSchedulePlanEntity source = retrieveFlightSchedulePlanEntityById(sourcFlightSchedulePlanID);
        FlightSchedulePlanEntity complementary = retrieveFlightSchedulePlanEntityById(returnFlightSchedulePlanID);

        source.setComplementary(complementary);
        complementary.setComplementary(source);

    }

    @Override
    public void processFlightSchedulePlanDeletion(FlightSchedulePlanEntity plan) {
        if (isDeletable(plan)) {
            deleteFlightSchedulePlan(plan);
        } else {
            disableFlightSchedulePlan(plan);
        }
    }

    private boolean isDeletable(FlightSchedulePlanEntity plan) {
        return plan.getFlightSchedule().stream().allMatch(sched -> sched.getReservations().isEmpty());
    }

    private void deleteFlightSchedulePlan(FlightSchedulePlanEntity plan) {
        flightScheduleSessionBean.deleteSchedule(plan.getFlightSchedule());
        removePlanFromFlight(plan);
        fareSessionBean.deleteFares(plan.getFares());
        dissociateComplementaryPlans(plan);
        em.remove(plan);
    }

    private void removePlanFromFlight(FlightSchedulePlanEntity plan) {
        plan.getFlight().getFlightSchedulePlan().remove(plan);
    }

    private void dissociateComplementaryPlans(FlightSchedulePlanEntity plan) {
        if (plan.getOrigin() != null) {
            plan.getOrigin().setComplementary(null);
            plan.setOrigin(null);
        }
        if(plan.getComplementary() != null) {
            plan.getComplementary().setOrigin(null);
            plan.setComplementary(null);
        }
    }

    private void disableFlightSchedulePlan(FlightSchedulePlanEntity plan) {
        plan.setDisabled(true);
    }
 }

                
                
                
                
                
                
        
  
    
    
    
    

