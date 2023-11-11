/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.UserEntity;
import exceptions.UserNotFoundException;
import javax.ejb.Remote;

/**
 *
 * @author kahjy
 */
@Remote
public interface UserSessionBeanRemote {

    public UserEntity retrieveUserById(Long userID) throws UserNotFoundException;
    
}
