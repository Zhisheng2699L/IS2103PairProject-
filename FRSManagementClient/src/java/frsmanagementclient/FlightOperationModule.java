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
import entity.EmployeeEntity;
import entity.FlightRouteEntity;
import enumeration.EmployeeAccessRightEnum;
import exceptions.FlightRouteDoNotExistException;
import java.util.List;
import java.util.Scanner;
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
    
    private void doCreateFlight() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("===== Create a new flight =====");
        
        //Validate and input Flight Number
        String flightNum = validateFlightNumber(sc);
        if (flightNum == null) {
            return;
        }
        
        //Retrieve and display flight routes
        List<FlightRouteEntity> routes = retrieveAndDisplayFlightRoutes();
        if (routes == null) {
            return;
        } 
    }
    
    private String validateFlightNumber(Scanner sc) {
        System.out.print("Enter Flight Number (ML + numbers only)> ");
        String flightNum = sc.nextLine().trim();

        if (!flightNum.matches("ML\\d+")) {
            System.out.println("Invalid flight number format! Please enter a valid flight number.");
            return null;
        }

        return "ML" + flightNum;
    }
    
    private List<FlightRouteEntity> retrieveAndDisplayFlightRoutes() {
        try {
            List<FlightRouteEntity> routes = flightRouteSessionBean.retrieveAllFlightRouteInOrder();

            System.out.printf("%20s%40s%20s%40s%25s\n", "Flight Route ID", "Origin Airport Name", "Origin Airport IATA", "Destination Airport Name", "Destination Airport IATA");
            for (FlightRouteEntity rte : routes) {
                System.out.printf("%20s%40s%20s%40s%25s\n", rte.getFlightRouteId().toString(), rte.getOrigin().getAirportName(), rte.getOrigin().getIATACode(), rte.getDestination().getAirportName(), rte.getDestination().getIATACode());
            }

            return routes;
        } catch (FlightRouteDoNotExistException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease create flight route first!\n");
            return null;
        }
    }
    
    
    
    
    
}
