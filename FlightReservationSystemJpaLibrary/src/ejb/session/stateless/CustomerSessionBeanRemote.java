/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import exceptions.CustomerDoNotExistException;
import exceptions.CustomerExistException;
import exceptions.InvalidLoginCredentialException;
import exceptions.UnknownPersistenceException;
import javax.ejb.Remote;

/**
 *
 * @author kahjy
 */
@Remote
public interface CustomerSessionBeanRemote {
    public CustomerEntity createNewCustomerEntity(CustomerEntity customer) throws UnknownPersistenceException, CustomerExistException;
    
    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerDoNotExistException;

    public CustomerEntity doLogin(String username, String password) throws InvalidLoginCredentialException;
}
