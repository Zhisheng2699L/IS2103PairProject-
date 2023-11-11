/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.PassengerEntity;
import exceptions.PassengerExistException;
import exceptions.UnknownPersistenceException;
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
public class PassengerSessionBean implements PassengerSessionBeanRemote, PassengerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;

    public PassengerSessionBean() {
    }

    @Override
    public PassengerEntity createNewPassenger(PassengerEntity passenger) throws PassengerExistException, UnknownPersistenceException {
        try {
            em.persist(passenger);
            em.flush();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new PassengerExistException("Passenger " + passenger.getPassengerId() + " already exist");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
        return passenger;
    }

    @Override
    public PassengerEntity retrievePassengerByPassport(String passport) {
        Query query = em.createQuery("SELECT p FROM PassengerEntity p WHERE p.passportNumber = :passport");
        query.setParameter("passport", passport);
        
        return (PassengerEntity)query.getSingleResult();
    }

    
}
