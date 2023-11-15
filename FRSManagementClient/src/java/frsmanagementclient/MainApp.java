/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CabinClassSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.SeatsAvailabilitySessionBeanRemote;
import entity.EmployeeEntity;
import enumeration.EmployeeAccessRightEnum;
import exceptions.AircraftConfigNotFoundException;
import exceptions.AirportDoNotExistException;
import exceptions.CreateNewAircraftConfigErrorException;
import exceptions.ExistingAircraftConfigException;
import exceptions.ExistingFlightException;
import exceptions.FlightExistException;
import exceptions.FlightNotFoundException;
import exceptions.FlightRouteDoNotExistException;
import exceptions.InputDataValidationException;
import exceptions.InvalidCostException;
import exceptions.InvalidLoginCredentialException;
import exceptions.InvalidLoginDetailsException;
import exceptions.UnknownPersistenceException;
import exceptions.ViolationConstraintsException;
import java.text.ParseException;
import java.util.Scanner;

/**
 *
 * @author kahjy
 */
public class MainApp {
   
    private AirportSessionBeanRemote airportSessionBean;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBean;
    private CabinClassSessionBeanRemote cabinClassSessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private SeatsAvailabilitySessionBeanRemote seatsAvailabilitySessionBean;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;
    private FlightSessionBeanRemote flightSessionBean;
    private FlightRouteSessionBeanRemote flightRouteSessionBean;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private FareSessionBeanRemote fareSessionBean;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBean;    
    
    private FlightRoutePlanningModule flightRoutePlanningModule;
    private FlightOperationModule flightOperationModule;
    private SalesAdminModule salesAdminModule;
    
    private boolean login = false;
    private EmployeeEntity currentEmployee;
    
    public MainApp(FlightScheduleSessionBeanRemote flightScheduleSessionBean, AirportSessionBeanRemote airportSessionBean, AircraftTypeSessionBeanRemote aircraftTypeSessionBean, CabinClassSessionBeanRemote cabinClassSessionBean, ReservationSessionBeanRemote reservationSessionBean, SeatsAvailabilitySessionBeanRemote seatsAvailabilitySessionBean, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean, FlightSessionBeanRemote flightSessionBean, FlightRouteSessionBeanRemote flightRouteSessionBean, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean, EmployeeSessionBeanRemote employeeSessionBean, FareSessionBeanRemote fareSessionBean) {
        this.airportSessionBean = airportSessionBean;
        this.aircraftTypeSessionBean = aircraftTypeSessionBean;
        this.cabinClassSessionBean = cabinClassSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.seatsAvailabilitySessionBean = seatsAvailabilitySessionBean;
        this.flightSchedulePlanSessionBean = flightSchedulePlanSessionBean;
        this.flightSessionBean = flightSessionBean;
        this.flightRouteSessionBean = flightRouteSessionBean;
        this.aircraftConfigurationSessionBean = aircraftConfigurationSessionBean;
        this.employeeSessionBean = employeeSessionBean;
        this.fareSessionBean = fareSessionBean;
        this.flightScheduleSessionBean = flightScheduleSessionBean;
    }
    
    public void runApp() throws UnknownPersistenceException, AirportDoNotExistException, ExistingAircraftConfigException, CreateNewAircraftConfigErrorException, AirportDoNotExistException, ExistingFlightException, ViolationConstraintsException, FlightNotFoundException, ParseException, InvalidCostException, AircraftConfigNotFoundException, FlightExistException, InputDataValidationException, AirportDoNotExistException, AirportDoNotExistException, FlightRouteDoNotExistException, AirportDoNotExistException, InvalidLoginDetailsException, AirportDoNotExistException, AirportDoNotExistException, AirportDoNotExistException, AirportDoNotExistException, AirportDoNotExistException {
        while (true) {
            if(!login) {
                Scanner sc = new Scanner(System.in);
                Integer response = 0;
                
                System.out.println("=== Welcome to Merlion Flight Reservation System (Management)===\n");
                System.out.println("1: Login");
                System.out.println("2: Exit\n");
                
                response = 0;
                while(response < 1 || response > 2) {
                    System.out.print("> ");
                    response = sc.nextInt();
                    if(response == 1) {
                        try {
                            doLogin();
                            System.out.println("Login Successful!\n");
                            login = true;
                            flightOperationModule = new FlightOperationModule(flightScheduleSessionBean, currentEmployee, flightSessionBean, flightSchedulePlanSessionBean, flightRouteSessionBean, aircraftConfigurationSessionBean, fareSessionBean);
                            
                            flightRoutePlanningModule = new FlightRoutePlanningModule(currentEmployee, airportSessionBean, aircraftConfigurationSessionBean, aircraftTypeSessionBean, cabinClassSessionBean, flightRouteSessionBean);

                            salesAdminModule = new SalesAdminModule(currentEmployee, seatsAvailabilitySessionBean, reservationSessionBean, flightSessionBean);
                            mainMenu();
                        } catch (InvalidLoginCredentialException ex) {
                            System.out.println(ex.getMessage());
                            System.out.println();
                        }
                    } else if (response == 2) {
                        break;
                    } else {
                        System.out.println("Invalid input, please try again!\n");
                    }
                }
                if(response == 2) {
                    break;
                }
            } else {
                mainMenu();
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException, InvalidLoginDetailsException {
        Scanner sc = new Scanner(System.in) ;
        System.out.println("*** Merlion Flight Reservation System :: LOGIN ***\n");
        System.out.print("Enter username> ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        String password = sc.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeSessionBean.tryLogin(username, password);
            //login = true;
        } else {
            throw new InvalidLoginCredentialException("Missing Login Credentials");
        }
    }
    
    private void mainMenu() throws UnknownPersistenceException, ExistingAircraftConfigException, CreateNewAircraftConfigErrorException, AirportDoNotExistException, ExistingFlightException, FlightRouteDoNotExistException, FlightRouteDoNotExistException, FlightRouteDoNotExistException, FlightRouteDoNotExistException, FlightRouteDoNotExistException, FlightRouteDoNotExistException, FlightRouteDoNotExistException, FlightRouteDoNotExistException, FlightRouteDoNotExistException, FlightRouteDoNotExistException, FlightRouteDoNotExistException, ViolationConstraintsException, ViolationConstraintsException, InputDataValidationException, InputDataValidationException, FlightExistException, AircraftConfigNotFoundException, InvalidCostException, ParseException, FlightNotFoundException {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(login) {
            System.out.println("*** Merlion Flight Reservation System ***\n");
            System.out.println("You are currently logged in as " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + " with " + currentEmployee.getAccessRight().toString() + " rights!\n");
            System.out.println("*** Select Module To Access ***");
            System.out.println("1: Flight Operation Module");
            System.out.println("2: Flight Planning Module");
            System.out.println("3: Sales Management Module");
            System.out.println("4: Log Out\n");
            
            response = 0;
            while(response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();
                
                if(response == 1) {
                    if(currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.SCHEDULEMANAGER)) {
                        flightOperationModule.mainMenu();
                    } else {
                        System.out.println("You are not authorised to use this feature.");
                    }
                } else if(response == 2) {
                    if(currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.FLEETMANAGER) || currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.ROUTEPLANNER)) {
                        flightRoutePlanningModule.mainMenu();
                    }else {
                        System.out.println("You are not authorised to use this feature.");
                    }
                    
                } else if (response == 3) {
                    if(currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.SALESMANAGER)) {
                        salesAdminModule.mainMenu();
                    }else {
                        System.out.println("You are not authorised to use this feature.");
                    }
                } else if (response == 4) {
                    doLogOut();
                    System.out.println("Log out successful.\n");
                    break;
                } else {
                    System.out.println("Invalid Option, please try again!");
                }
            }
            
            if(response == 4) {
                break;
            }
        }
    }
    
    private void doLogOut() {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Are you sure you want to log out? (Y or N)> ");
        String reply = sc.nextLine().trim();
        
        if((reply.equals("Y") || reply.equals("y")) && login) {
            currentEmployee = null;
            login = false;
        }
    }
    
}
