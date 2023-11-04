/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfigurationEntity;
import entity.CabinClassEntity;
import enumeration.CabinClassTypeEnum;
import exceptions.CabinClassNotFoundException;
import exceptions.CabinClassTypeEnumNotFoundException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author foozh
 */
@Stateless
public class CabinClassSessionBean implements CabinClassSessionBeanRemote, CabinClassSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;

    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBean;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public CabinClassEntity createNewCabinClass(CabinClassEntity cabin, AircraftConfigurationEntity aircraft) {
        em.persist(cabin);    
        cabin.setAircraftConfig(aircraft); 
        if (!aircraft.getCabin().contains(cabin)) {
            aircraft.getCabin().add(cabin);
        }
        
        return cabin; 
    }
    
    @Override
    public CabinClassEntity retrieveCabinByID(Long cabinClassId) throws CabinClassNotFoundException {
        CabinClassEntity cabin = em.find(CabinClassEntity.class, cabinClassId);
        if (cabin != null) {
            return cabin;
        } else {
            throw new CabinClassNotFoundException("Cabin class with " + cabinClassId+ " not found!\n");
        }
    }
    
    @Override
    public CabinClassTypeEnum findEnumType(String input) throws CabinClassTypeEnumNotFoundException {
        switch (input.toUpperCase()) {
            case "F":
                return CabinClassTypeEnum.F;
            case "J":
                return CabinClassTypeEnum.J;
            case "W":
                return CabinClassTypeEnum.W;
            case "Y":
                return CabinClassTypeEnum.Y;
            default:
                throw new CabinClassTypeEnumNotFoundException("Invalid input of cabin class type!\n");
        }
    }

    
    
  
}
