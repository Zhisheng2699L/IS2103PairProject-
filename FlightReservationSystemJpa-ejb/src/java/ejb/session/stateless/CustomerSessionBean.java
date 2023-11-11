/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import exceptions.CustomerDoNotExistException;
import exceptions.CustomerExistException;
import exceptions.InvalidLoginCredentialException;
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
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;

    public CustomerSessionBean() {
    }
    
    @Override
    public CustomerEntity createNewCustomerEntity(CustomerEntity customer) throws UnknownPersistenceException, CustomerExistException {
        try {
            em.persist(customer);
            em.flush();
            return customer;
        } catch (PersistenceException ex) {
              if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CustomerExistException("Same Username/Contact/Id number exist!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerDoNotExistException{
        Query query = em.createQuery("SELECT e FROM CustomerEntity e WHERE e.username = :user");
        query.setParameter("user", username);
        
        try{
            return (CustomerEntity)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerDoNotExistException("Customer does not exist!");
        }
    }
    
    @Override
    public CustomerEntity doLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            CustomerEntity customer = retrieveCustomerByUsername(username);
            if(customer.getPassword().equals(password)) {
                return customer;
            } else {
                throw new InvalidLoginCredentialException("Wrong password input!");
            }
        } catch (CustomerDoNotExistException ex) {
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
    }

   
}
