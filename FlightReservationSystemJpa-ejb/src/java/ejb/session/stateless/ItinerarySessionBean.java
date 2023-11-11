/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ItineraryEntity;
import entity.UserEntity;
import exceptions.InputDataValidationException;
import exceptions.ItineraryDoNotExistException;
import exceptions.ItineraryExistException;
import exceptions.UnknownPersistenceException;
import exceptions.UserNotFoundException;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author kahjy
 */
@Stateless
public class ItinerarySessionBean implements ItinerarySessionBeanRemote, ItinerarySessionBeanLocal {

    @EJB(name = "UserSessionBeanLocal")
    private UserSessionBeanLocal userSessionBeanLocal;

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;
   
    private final Validator validator;  
    private final ValidatorFactory validatorFactory;
    
    public ItinerarySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    

    @Override
    public ItineraryEntity createNewItinerary(ItineraryEntity itinerary, long userId) throws UnknownPersistenceException, InputDataValidationException, UserNotFoundException, ItineraryExistException {
        Set<ConstraintViolation<ItineraryEntity>> constraintViolations = validator.validate(itinerary);
        UserEntity user = userSessionBeanLocal.retrieveUserById(userId);
        
         if (constraintViolations.isEmpty()) {
            try {
                em.persist(itinerary);

                itinerary.setUser(user);
                user.getItineraries().add(itinerary);

                em.flush();
                return itinerary;
            } catch (PersistenceException ex) { 
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ItineraryExistException("Itinerary already exists!");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public ItineraryEntity retrieveItineraryByID(long itineraryId) throws ItineraryDoNotExistException {
        ItineraryEntity itinerary = em.find(ItineraryEntity.class, itineraryId);
        if (itinerary == null) {
            throw new ItineraryDoNotExistException("Itinerary do not exist!");
        }
        return itinerary;
    }
    
    @Override
    public ItineraryEntity retrieveItineraryByIDRemoved(long itineraryId) throws ItineraryDoNotExistException {
        ItineraryEntity itinerary = em.find(ItineraryEntity.class, itineraryId);
        if (itinerary == null) {
            throw new ItineraryDoNotExistException("Itinerary not found");
        } else {
            em.detach(itinerary);
            return itinerary;
        }
    }

    @Override
    public List<ItineraryEntity> retrieveItinerariesByCustomerId(Long userID) {
        Query query = em.createQuery("SELECT r FROM ItineraryEntity r WHERE r.user.UserID = :id");
        query.setParameter("id", userID);
        
        return query.getResultList();
    }
    
    @Override
    public List<ItineraryEntity> retrieveItinerariesByCustomerIdRemoved(Long userID) {
        Query query = em.createQuery("SELECT r FROM ItineraryEntity r WHERE r.user.UserID = :id");
        query.setParameter("id", userID);
        
        List<ItineraryEntity> list =  query.getResultList();
        for (ItineraryEntity it: list) {
            em.detach(it);
        }
        return list;
    }
    
    //need to provide error message if data input is incorrect.
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ItineraryEntity>> constraintViolations) {
        StringBuilder errorMessage = new StringBuilder("Input data validation error!");
        for (ConstraintViolation constraintViolation : constraintViolations) {
            errorMessage.append("\n\t").append(constraintViolation.getPropertyPath())
                    .append(" - ").append(constraintViolation.getInvalidValue())
                    .append("; ").append(constraintViolation.getMessage());
        }
        return errorMessage.toString();
    }
}
