/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.CabinClassEntity;
import exceptions.AircraftConfigNotFoundException;
import exceptions.AircraftTypeDoNotExistException;
import exceptions.CreateNewAircraftConfigErrorException;
import exceptions.ExistingAircraftConfigException;
import exceptions.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author foozh
 */
@Remote
public interface AircraftConfigurationSessionBeanRemote {
    
    public AircraftConfigurationEntity createNewAircraftConfig(AircraftConfigurationEntity aircraftConfig, long aircraftTypeID, List<CabinClassEntity> cabinClasses) throws
            CreateNewAircraftConfigErrorException, AircraftTypeDoNotExistException, UnknownPersistenceException, ExistingAircraftConfigException;
    
    public List<AircraftConfigurationEntity> retrieveAllConfiguration() throws AircraftConfigNotFoundException;
    
    public AircraftConfigurationEntity retrieveAircraftConfigByName(String name) throws AircraftConfigNotFoundException;
    
    public AircraftConfigurationEntity retriveAircraftConfigByID(Long aircraftConfigID) throws AircraftConfigNotFoundException;
    
    
    
}
