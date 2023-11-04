/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import exceptions.EmployeeDoNotExistException;
import exceptions.EmployeeUsernameExistException;
import exceptions.InvalidLoginDetailsException;
import exceptions.UnknownPersistenceException;
import javax.ejb.Local;

/**
 *
 * @author kahjy
 */
@Local
public interface EmployeeSessionBeanLocal {
    
    public EmployeeEntity createNewEmployee(EmployeeEntity newEmployee) throws UnknownPersistenceException, EmployeeUsernameExistException;
    
    public EmployeeEntity retrieveEmployeeByUsername (String username) throws EmployeeDoNotExistException;
    
    public EmployeeEntity retrieveEmployeeById (Long id) throws EmployeeDoNotExistException;
    
    public EmployeeEntity tryLogin(String username, String password) throws InvalidLoginDetailsException;
}
