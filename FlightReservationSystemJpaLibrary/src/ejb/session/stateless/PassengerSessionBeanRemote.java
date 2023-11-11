/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.PassengerEntity;
import exceptions.PassengerExistException;
import exceptions.UnknownPersistenceException;
import javax.ejb.Remote;

/**
 *
 * @author kahjy
 */
@Remote
public interface PassengerSessionBeanRemote {
    public PassengerEntity createNewPassenger(PassengerEntity passenger) throws PassengerExistException, UnknownPersistenceException;
    
    public PassengerEntity retrievePassengerByPassport(String passport);
}
