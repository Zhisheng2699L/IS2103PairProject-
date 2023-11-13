/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.AirportEntity;
import exceptions.AirportDoNotExistException;
import javax.ejb.Remote;

/**
 *
 * @author kahjy
 */
@Remote
public interface AirportSessionBeanRemote {
    public AirportEntity retrieveAirportById(Long id) throws AirportDoNotExistException;

    public AirportEntity retrieveAirportByIATA(String iata) throws AirportDoNotExistException;
}
