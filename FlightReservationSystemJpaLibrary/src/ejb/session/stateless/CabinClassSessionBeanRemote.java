/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.CabinClassEntity;
import enumeration.CabinClassTypeEnum;
import exceptions.CabinClassNotFoundException;
import exceptions.CabinClassTypeEnumNotFoundException;
import javax.ejb.Remote;

/**
 *
 * @author foozh
 */
@Remote
public interface CabinClassSessionBeanRemote {
    
    public CabinClassEntity createNewCabinClass(CabinClassEntity cabin, AircraftConfigurationEntity aircraft);
    
    public CabinClassEntity retrieveCabinByID(Long cabinClassId) throws CabinClassNotFoundException;
    
    public CabinClassTypeEnum findEnumType(String input) throws CabinClassTypeEnumNotFoundException;
       
}
