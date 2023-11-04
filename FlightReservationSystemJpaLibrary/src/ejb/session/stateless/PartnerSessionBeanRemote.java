/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import exceptions.InvalidLoginCredentialException;
import exceptions.PartnerNotFoundException;
import exceptions.PartnerUsernameExistException;
import exceptions.UnknownPersistenceException;
import javax.ejb.Remote;

/**
 *
 * @author foozh
 */
@Remote
public interface PartnerSessionBeanRemote {
    
    public PartnerEntity createNewPartner(PartnerEntity partner) throws PartnerUsernameExistException, UnknownPersistenceException;
    
    public PartnerEntity retrievePartnerById(Long id) throws PartnerNotFoundException;
    
    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException;
    
    public long doLogin(String username, String password) throws InvalidLoginCredentialException, PartnerNotFoundException;
    
    
    
}
