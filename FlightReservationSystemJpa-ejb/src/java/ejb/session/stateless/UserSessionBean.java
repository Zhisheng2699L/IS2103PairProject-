/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.UserEntity;
import exceptions.UserNotFoundException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kahjy
 */
@Stateless
public class UserSessionBean implements UserSessionBeanRemote, UserSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;

    @Override
    public UserEntity retrieveUserById(Long userID) throws UserNotFoundException {
        UserEntity user = em.find(UserEntity.class, userID);
        
        if(user != null) {
            return user;
        } else {
            throw new UserNotFoundException("User " + userID + "does not exist");
        }
    }
    
    
}
