/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftTypeEntity;
import exceptions.AircraftTypeDoNotExistException;
import exceptions.AircraftTypeExistException;
import exceptions.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author kahjy
 */
@Local
public interface AircraftTypeSessionBeanLocal {
    
    public AircraftTypeEntity createNewAircraftType(AircraftTypeEntity aircraftType) throws AircraftTypeExistException, UnknownPersistenceException;
    
    public AircraftTypeEntity retrieveAircraftTypeById(Long id) throws AircraftTypeDoNotExistException;

    public List<AircraftTypeEntity> retrieveAllAircraftType();
}
