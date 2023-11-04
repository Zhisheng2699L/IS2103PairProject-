/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import exceptions.EmployeeUsernameExistException;
import exceptions.EmployeeDoNotExistException;
import exceptions.InvalidLoginDetailsException;
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
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionFlightReservationSystem-ejbPU")
    private EntityManager em;

    public EmployeeSessionBean() {
    }
    
    public EmployeeEntity createNewEmployee(EmployeeEntity newEmployee) throws UnknownPersistenceException, EmployeeUsernameExistException {
        try {
            em.persist(newEmployee);
            em.flush();
            return newEmployee;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new EmployeeUsernameExistException("Employee with the same username exists, please choose another username");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    public EmployeeEntity retrieveEmployeeByUsername (String username) throws EmployeeDoNotExistException {
        Query query = em.createQuery("SELECT e FROM EmployeeEntity e WHERE e.userName = :user");
        query.setParameter("user", username);
        
        try {
            return(EmployeeEntity)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeDoNotExistException("Employee does not exist!");
        }
    }
    
    public EmployeeEntity retrieveEmployeeById (Long id) throws EmployeeDoNotExistException {
        EmployeeEntity employee = em.find(EmployeeEntity.class, id);
        
        if (employee != null) {
            return employee;
        } else {
            throw new EmployeeDoNotExistException("Employee id: " + id.toString() + " does not exist!");
        }
    }
    
    public EmployeeEntity tryLogin(String username, String password) throws InvalidLoginDetailsException {
        try {
            EmployeeEntity employee = retrieveEmployeeByUsername(username);
            if (employee.getPassword().equals(password)) {
                return employee;
            } else {
                throw new InvalidLoginDetailsException("Incorrect Password, Try again.");
            }
        } catch (EmployeeDoNotExistException ex) {
            throw new InvalidLoginDetailsException("Username does not exist!");
        }
    }

    
}
