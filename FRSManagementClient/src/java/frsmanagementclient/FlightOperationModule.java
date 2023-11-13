/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfigurationEntity;
import entity.EmployeeEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import enumeration.EmployeeAccessRightEnum;
import exceptions.AircraftConfigNotFoundException;
import exceptions.ExistingFlightException;
import exceptions.FlightExistException;
import exceptions.FlightNotFoundException;
import exceptions.FlightRouteDoNotExistException;
import exceptions.InputDataValidationException;
import exceptions.UnknownPersistenceException;
import exceptions.ViolationConstraintsException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
/**
 *
 * @author kahjy
 */
public class FlightOperationModule {
    
    private EmployeeEntity employee;
    private FlightSessionBeanRemote flightSessionBean;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;
    private FlightRouteSessionBeanRemote flightRouteSessionBean;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean;
    private FareSessionBeanRemote fareSessionBean;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBean;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public FlightOperationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public FlightOperationModule(FlightScheduleSessionBeanRemote flightScheduleSessionBean, EmployeeEntity employee, FlightSessionBeanRemote flightSessionBean, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean, FlightRouteSessionBeanRemote flightRouteSessionBean, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean, FareSessionBeanRemote fareSessionBean) {
        this.flightScheduleSessionBean = flightScheduleSessionBean;
        this.employee = employee;
        this.flightSessionBean = flightSessionBean;
        this.flightSchedulePlanSessionBean = flightSchedulePlanSessionBean;
        this.flightRouteSessionBean = flightRouteSessionBean;
        this.aircraftConfigurationSessionBean = aircraftConfigurationSessionBean;
        this.fareSessionBean = fareSessionBean;
    }
    
    public void mainMenu() {
        Scanner sc = new Scanner(System.in);
        int response = 0;
        
        while(true) {
            System.out.println("===== Flight Operating Module =====\n");
            System.out.println("Choose a system to choose: ");
            System.out.println("1: Flight system");
            System.out.println("2: Flight schedule plan system");
            System.out.println("3: Exit\n");
            
            response = 0;
            while(response < 1 || response > 3) {
                System.out.print("> ");
                response = sc.nextInt();
                
                if (response == 1) {
                    if (employee.getAccessRight().equals(EmployeeAccessRightEnum.SCHEDULEMANAGER) || employee.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR)) {
                        flightDetails();
                    } else {
                        System.out.println("You are not an Admin or a Schedule Manager!");
                    }
                } else if (response == 2) {
                    if (employee.getAccessRight().equals(EmployeeAccessRightEnum.SCHEDULEMANAGER) || employee.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR)) {
                        flightSchedulePlanDetails();
                    } else {
                        System.out.println("You are not an Admin or a Schedule Manager!");
                    }
                } else if (response == 3) {
                    if (employee.getAccessRight().equals(EmployeeAccessRightEnum.SCHEDULEMANAGER) || employee.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR)) {
                        break();
                } else {
                        System.out.println("Invalid input. Please enter a valid number.");
                }
            }  
        }
        if (response == 3) {
            break;
        }
        
        System.out.println("End of Details");
    }
        
    private void flightDetails() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        
        while(true) {
            System.out.println("===== Flight Operations Menu =====\n");
            System.out.println("1: Create a New Flight");
            System.out.println("2: View All Flights");
            System.out.println("3: View Flight Details");
            System.out.println("4: Exit\n");
            
            choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
                case 1:
                    createNewflight();
                    break;
                case 2: 
                    viewAllFlights();
                    break;
                case 3:
                    viewFlightDetails();
                    break;
                case 4:
                    System.out.println("Existing flight Operations Menu..");
                    return;
                default:
                    System.out.println("Invalid choice!!");
            } 
        }
    }
    
    /*--------------------------------------------------- DO CREATE FLIGHT () ----------------------------------------------------------------------------*/

    private void doCreateFlight() throws ExistingFlightException, FlightRouteDoNotExistException, ViolationConstraintsException, InputDataValidationException, UnknownPersistenceException, AircraftConfigNotFoundException, FlightExistException {
        FlightEntity flight;
        long chosenRoute, chosenConfig;
        String flightNum;
        Scanner sc = new Scanner(System.in);
        System.out.println("===== Create a new flight =====");
        System.out.print("Enter Flight Number (integers only)> ");
        flightNum = "ML" + sc.nextLine().trim(); // not sure if must enforce MLxxx
        List<FlightRouteEntity> routes = flightRouteSessionBean.retrieveAllFlightRouteInOrder();

        System.out.printf("%20s%40s%20s%40s%25s\n", "Flight Route ID", "Origin Airport Name", "Origin Airport IATA", "Destination Airport Name", "Destination Airport IATA");
        for (FlightRouteEntity rte : routes) {
            System.out.printf("%20s%40s%20s%40s%25s\n", rte.getFlightRouteId().toString(), rte.getOrigin().getAirportName(), rte.getOrigin().getIATACode(), rte.getDestination().getAirportName(), rte.getDestination().getIATACode());
        }
        System.out.print("Enter Flight Route (By Id)>  ");
        chosenRoute = sc.nextLong();
        sc.nextLine();

        List<AircraftConfigurationEntity> aircraftConfig = aircraftConfigurationSessionBean.retrieveAllConfiguration();

        System.out.printf("%30s%40s%25s%20s\n", "Aircraft Configuration ID", "Name", "Number of Cabin Class", "Aircraft Type");
        for (AircraftConfigurationEntity config : aircraftConfig) {
            System.out.printf("%30s%40s%25s%20s\n", config.getAircraftConfigId().toString(), config.getName(), config.getNumberOfCabinClasses(), config.getAircraftType().getTypeName());
        }
        System.out.print("Enter Aircraft Configuration (By Id)>  ");
        chosenConfig = sc.nextLong();
        sc.nextLine();

        flight = new FlightEntity(flightNum);
        Set<ConstraintViolation<FlightEntity>> constraintViolations = validator.validate(flight);
        if (constraintViolations.isEmpty()) {
            flight = flightSessionBean.createNewFlight(flight, chosenRoute, chosenConfig);
        } else {
            prepareInputDataValidationErrorsMessage(constraintViolations);
            return;
        }

        try {
            flight = flightSessionBean.createNewFlight(flight, chosenRoute, chosenConfig);
            System.out.println("Flight created successfully!\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
            return;
        } catch (FlightExistException ex) {
            try {
                flight = flightSessionBean.enableFlight(flightNum, chosenRoute, chosenConfig);
                System.out.println("Previous disabled flight found!\nRe-enabling flight...");
            } catch (FlightNotFoundException ex1) {
                System.out.println("Error: Flight " + flightNum + " already exists\nPlease try again!\n");
                return;
            }
        }

        FlightRouteEntity flightRoute;
        try {
            flightRoute = flightRouteSessionBean.retrieveFlightRouteById(chosenRoute);
        } catch (FlightRouteDoNotExistException ex) {
            return; // will never hit this
        }

        if (flightRoute.getComplementaryRoute() != null) {
            System.out.print("Complementary route found!\nWould you like to create a complementary return flight? (Y/N)> ");
            if (sc.nextLine().trim().equalsIgnoreCase("Y")) {
                while (true) {
                    String returnFlightNum;
                    System.out.print("Enter return flight number > ");
                    returnFlightNum = "ML" + sc.nextLine().trim();
                    FlightRouteEntity returnFlightRoute = flightRoute.getComplementaryRoute();
                    try {
                        FlightEntity returnFlight = new FlightEntity(returnFlightNum);
                        returnFlight = flightSessionBean.createNewFlight(returnFlight, returnFlightRoute.getFlightRouteId(), chosenConfig);
                        flightSessionBean.associateExistingFlightWithReturnFlight(flight.getFlightId(), returnFlight.getFlightId());
                        System.out.println("Return flight created!\n");
                        break;
                    } catch (UnknownPersistenceException | FlightNotFoundException | FlightRouteDoNotExistException | AircraftConfigNotFoundException  ex) {
                        System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
                    } catch (FlightExistException ex) {
                        try {
                            FlightEntity returnFlight = flightSessionBean.enableFlight(returnFlightNum, returnFlightRoute.getFlightRouteId(), chosenConfig);
                            flightSessionBean.associateExistingFlightWithReturnFlight(flight.getFlightId(), returnFlight.getFlightId());
                            System.out.println("Previous disabled return flight found!\nRe-enabling flight...\n");
                            break;
                        } catch (FlightNotFoundException ex1) {
                            System.out.println("Error: Flight " + returnFlightNum + " already exists\nPlease try again!\n");
                        }
                    }
                }
            }
        }
    }

    private void prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightEntity>> constraintViolations) {
       System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    

}