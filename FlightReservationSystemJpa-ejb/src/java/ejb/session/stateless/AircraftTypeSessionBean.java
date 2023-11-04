/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftTypeEntity;
import exceptions.AircraftTypeDoNotExistException;
import exceptions.AircraftTypeExistException;
import exceptions.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author kahjy
 */
@Stateless
public class AircraftTypeSessionBean implements AircraftTypeSessionBeanRemote, AircraftTypeSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;

    public AircraftTypeSessionBean() {
    }
    
    @Override
    public AircraftTypeEntity createNewAircraftType(AircraftTypeEntity aircraftType) throws AircraftTypeExistException, UnknownPersistenceException {
        try {
            em.persist(aircraftType);
            em.flush();
            return aircraftType;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new AircraftTypeExistException("AircraftType name exists, please choose another name");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    public AircraftTypeEntity retrieveAircraftTypeById (Long id) throws AircraftTypeDoNotExistException {
    AircraftTypeEntity aircraftType = em.find(AircraftTypeEntity.class, id);

        if (aircraftType != null) {
            return aircraftType;
        } else {
            throw new AircraftTypeDoNotExistException("AircraftType id: " + id.toString() + " does not exist!");
        }
    }
    
    public List<AircraftTypeEntity> retrieveAllAircraftType() {
        Query query = em.createQuery("SELECT a FROM AircraftTypeEntity a ORDER BY a.aircraftID ASC");
        return query.getResultList();
    }
}
