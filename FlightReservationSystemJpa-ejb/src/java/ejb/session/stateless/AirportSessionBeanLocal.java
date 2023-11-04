/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AirportEntity;
import exceptions.AirportDoNotExistException;
import exceptions.AirportExistException;
import exceptions.UnknownPersistenceException;
import javax.ejb.Local;

/**
 *
 * @author kahjy
 */
@Local
public interface AirportSessionBeanLocal {
    public AirportEntity createNewAirport(AirportEntity airport) throws AirportExistException, UnknownPersistenceException;
    
    public AirportEntity retrieveAirportById (Long id) throws AirportDoNotExistException;
    
    public AirportEntity retrieveAirportByIATA (String iata) throws AirportDoNotExistException;
}
