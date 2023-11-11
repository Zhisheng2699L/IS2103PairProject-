/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.FareEntity;
import entity.FlightSchedulePlanEntity;
import exceptions.ExistingFareException;
import exceptions.FareDoNotExistException;
import exceptions.InvalidCostException;
import exceptions.UnknownPersistenceException;
import exceptions.ViolationConstraintsException;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author foozh
 */
@Remote
public interface FareSessionBeanRemote {
    
    public FareEntity createFareEntity(FareEntity fare, FlightSchedulePlanEntity flightSchedulePlan) throws ExistingFareException,
            UnknownPersistenceException, ViolationConstraintsException;
    
    public FareEntity retrieveFareById(Long fareID) throws FareDoNotExistException;
    
    public FareEntity updateFare(long fareID, BigDecimal newCost) throws FareDoNotExistException, InvalidCostException;
    
    public void deleteFares(List<FareEntity> fares);
    
}
