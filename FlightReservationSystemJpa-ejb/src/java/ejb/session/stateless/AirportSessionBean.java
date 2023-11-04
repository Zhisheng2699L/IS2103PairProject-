/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AirportEntity;
import exceptions.AirportDoNotExistException;
import exceptions.AirportExistException;
import exceptions.UnknownPersistenceException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author kahjy
 */
@Stateless
public class AirportSessionBean implements AirportSessionBeanRemote, AirportSessionBeanLocal {

    @PersistenceContext(unitName = "AirportSessionBean-ejbPU")
    private EntityManager em;

    public AirportSessionBean() {
    }
    
    public AirportEntity createNewAirport(AirportEntity airport) throws AirportExistException, UnknownPersistenceException {
        try {
            em.persist(airport);
            em.flush();
            return airport;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new AirportExistException("Airport IATA code exists, please choose another IATA");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    public AirportEntity retrieveAirportById (Long id) throws AirportDoNotExistException {
    AirportEntity airport = em.find(AirportEntity.class, id);

        if (airport != null) {
            return airport;
        } else {
            throw new AirportDoNotExistException("Airport id: " + id.toString() + " does not exist!");
        }
    }
    
    public AirportEntity retrieveAirportByIATA (String iata) throws AirportDoNotExistException {
        Query query = em.createQuery("SELECT a FROM AirportEntity a WHERE a.IATACode = :code");
        query.setParameter("code", iata);
        
        try {
            return(AirportEntity)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AirportDoNotExistException("Airport does not exist!");
        }
    }
}
