/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.ItineraryEntity;
import exceptions.InputDataValidationException;
import exceptions.ItineraryDoNotExistException;
import exceptions.ItineraryExistException;
import exceptions.UnknownPersistenceException;
import exceptions.UserNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author kahjy
 */
@Local
public interface ItinerarySessionBeanLocal {
    public ItineraryEntity retrieveItineraryByID(long itineraryId) throws ItineraryDoNotExistException;
    
    public List<ItineraryEntity> retrieveItinerariesByCustomerId(Long userID);
    
    public ItineraryEntity createNewItinerary(ItineraryEntity itinerary, long userId) throws UnknownPersistenceException, InputDataValidationException, UserNotFoundException, ItineraryExistException; 

    public ItineraryEntity retrieveItineraryByIDRemoved(long itineraryId) throws ItineraryDoNotExistException;

    public List<ItineraryEntity> retrieveItinerariesByCustomerIdRemoved(Long userID);
}
