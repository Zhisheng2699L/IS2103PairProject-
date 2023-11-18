/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.AircraftTypeEntity;
import entity.AirportEntity;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import enumeration.EmployeeAccessRightEnum;
import exceptions.AircraftTypeExistException;
import exceptions.AirportExistException;
import exceptions.EmployeeDoNotExistException;
import exceptions.EmployeeUsernameExistException;
import exceptions.PartnerUsernameExistException;
import exceptions.UnknownPersistenceException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author foozh
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean implements DataInitSessionBeanRemote, DataInitSessionBeanLocal {

    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBean;

    @EJB
    private AirportSessionBeanLocal airportSessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    
    

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {

        try {
            employeeSessionBean.retrieveEmployeeByUsername("Employee 1");
        } catch (EmployeeDoNotExistException ex) {
            doDataInitialisation();
        }
    }

    private void doDataInitialisation() {
        try {
            
            employeeSessionBean.createNewEmployee(new EmployeeEntity("Foo", "Zhi Sheng", "Employee 1", "password", EmployeeAccessRightEnum.ADMINISTRATOR));
            employeeSessionBean.createNewEmployee(new EmployeeEntity("Pung", "Kahjyun", "Employee 2", "password", EmployeeAccessRightEnum.ADMINISTRATOR));
            employeeSessionBean.createNewEmployee(new EmployeeEntity("Joe", "Mama", "Employee 3", "password", EmployeeAccessRightEnum.ROUTEPLANNER));
            employeeSessionBean.createNewEmployee(new EmployeeEntity("Harry", "Lee", "Employee 4", "password", EmployeeAccessRightEnum.FLEETMANAGER));
            employeeSessionBean.createNewEmployee(new EmployeeEntity("Torrey", "Dejong", "Employee 5", "password", EmployeeAccessRightEnum.EMPLOYEE));
            employeeSessionBean.createNewEmployee(new EmployeeEntity("Ryan", "New", "Employee 6", "password", EmployeeAccessRightEnum.EMPLOYEE));
            employeeSessionBean.createNewEmployee(new EmployeeEntity("Gangman", "ShiBasui", "Employee 7", "password", EmployeeAccessRightEnum.EMPLOYEE));
            employeeSessionBean.createNewEmployee(new EmployeeEntity("Davidson", "Nga", "Employee 8", "password", EmployeeAccessRightEnum.SALESMANAGER));
            employeeSessionBean.createNewEmployee(new EmployeeEntity("David", "Tan", "Employee 9", "password", EmployeeAccessRightEnum.SCHEDULEMANAGER));


            partnerSessionBean.createNewPartner(new PartnerEntity("Holiday.com", "Partner 1", "password"));
            
            aircraftTypeSessionBean.createNewAircraftType(new AircraftTypeEntity("Boeing 737", 215));
            aircraftTypeSessionBean.createNewAircraftType(new AircraftTypeEntity("Boeing 747", 467));

            airportSessionBean.createNewAirport(new AirportEntity("Changi Airport", "SIN", "Singapore", "Singapore", "Singapore", 8));
            airportSessionBean.createNewAirport(new AirportEntity("Taoyuan International Airport", "TPE", "Taoyuan", "Taipei", "Taiwan", 8));
            airportSessionBean.createNewAirport(new AirportEntity("Incheon International Airport", "ICN", "Seoul", "Seoul", "South Korea", 9));
            airportSessionBean.createNewAirport(new AirportEntity("Sydney Airport", "SYD", "Sydney", "New South Wales", "Australia", 11));
            airportSessionBean.createNewAirport(new AirportEntity("Sendai Airport", "SDJ", "Sendai", "Miyagi", "Japan", 9));
            airportSessionBean.createNewAirport(new AirportEntity("Narita International Airport", "NRT", "Narita", "Chiba", "Japan", 9));


        } catch (EmployeeUsernameExistException | UnknownPersistenceException | PartnerUsernameExistException | AirportExistException | AircraftTypeExistException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
