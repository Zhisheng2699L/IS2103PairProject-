/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FareEntity;
import entity.FlightSchedulePlanEntity;
import exceptions.ExistingFareException;
import exceptions.FareDoNotExistException;
import exceptions.InvalidCostException;
import exceptions.UnknownPersistenceException;
import exceptions.ViolationConstraintsException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
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
 * @author foozh
 */
@Stateless
public class FareSessionBean implements FareSessionBeanRemote, FareSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;

    private final ValidatorFactory customValidatorFactory;
    private final Validator customValidator;

    public FareSessionBean() {
        customValidatorFactory = Validation.buildDefaultValidatorFactory();
        customValidator = customValidatorFactory.getValidator();
    }
    
    @Override
    public FareEntity createFareEntity(FareEntity fare, FlightSchedulePlanEntity flightSchedulePlan) throws ExistingFareException,
            UnknownPersistenceException, ViolationConstraintsException {
        Set<ConstraintViolation<FareEntity>> constraintViolations = customValidator.validate(fare);

        if (constraintViolations.isEmpty()) {
            try {
                flightSchedulePlan.getFares().add(fare);
                fare.setFlightSchedulePlan(flightSchedulePlan);
                
                em.persist(fare);
                return fare;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ExistingFareException("Fare code exist");
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
    public FareEntity retrieveFareById(Long fareID) throws FareDoNotExistException {
        FareEntity fare = em.find(FareEntity.class, fareID);
        if (fare != null) {
            return fare;
        } else {
            throw new FareDoNotExistException("Fare " + fareID + " not found!");
        }
    }
    
    @Override
    public FareEntity updateFare(long fareID, BigDecimal newCost) throws FareDoNotExistException, InvalidCostException {
        if (newCost == null || newCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidCostException("Invalid new cost");
        }

        FareEntity fare = retrieveFareById(fareID);
        fare.setFareAmount(newCost);
        em.flush();
        return fare;    
    }
    
    @Override
    public void deleteFares(List<FareEntity> fares) {
        for (FareEntity fare : fares) {
            em.remove(fare);
        }
    }
}
