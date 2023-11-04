/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftTypeEntity;
import exceptions.AircraftTypeDoNotExistException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author kahjy
 */
@Remote
public interface AircraftTypeSessionBeanRemote {
    
    public AircraftTypeEntity retrieveAircraftTypeById(Long id) throws AircraftTypeDoNotExistException;
 
    public List<AircraftTypeEntity> retrieveAllAircraftType();
}
