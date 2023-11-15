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
import entity.FareEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import enumeration.EmployeeAccessRightEnum;
import exceptions.AircraftConfigNotFoundException;
import exceptions.ExistingFlightException;
import exceptions.FareDoNotExistException;
import exceptions.FlightExistException;
import exceptions.FlightNotFoundException;
import exceptions.FlightRouteDoNotExistException;
import exceptions.FlightScheduleNotFoundException;
import exceptions.FlightSchedulePlanDoNotExistException;
import exceptions.InputDataValidationException;
import exceptions.InvalidCostException;
import exceptions.UnknownPersistenceException;
import exceptions.UpdateFlightScheduleException;
import exceptions.ViolationConstraintsException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import oracle.jrockit.jfr.parser.ParseException;

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
    
    public void mainMenu() throws ExistingFlightException, FlightRouteDoNotExistException, ViolationConstraintsException, InputDataValidationException, FlightExistException, UnknownPersistenceException, AircraftConfigNotFoundException {
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
                        break;
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
        
    /**
     *
     * @throws ExistingFlightException
     * @throws FlightRouteDoNotExistException
     * @throws ViolationConstraintsException
     * @throws InputDataValidationException
     * @throws FlightExistException
     * @throws UnknownPersistenceException
     * @throws AircraftConfigNotFoundException
     */
    public void flightDetails() throws ExistingFlightException, FlightRouteDoNotExistException, ViolationConstraintsException,
            InputDataValidationException, FlightExistException, UnknownPersistenceException, AircraftConfigNotFoundException {
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

    private void createNewflight() throws ExistingFlightException, FlightRouteDoNotExistException, ViolationConstraintsException, InputDataValidationException, UnknownPersistenceException, AircraftConfigNotFoundException, FlightExistException {
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
    

/*---------------------------------- VIEW ALL FLIGHTS ----------------------------------*/    
    private void viewAllFlights() {
        System.out.println("===== View All Flights Schedule ======");

        try {
            List<FlightSchedulePlanEntity> list = flightSchedulePlanSessionBean.retrieveAllFlightSchedulePlan();
            printFlightSchedulePlanList(list);
        } catch (FlightSchedulePlanDoNotExistException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
        }

        waitForEnterKeyPress();
    }

    private void printFlightSchedulePlanList(List<FlightSchedulePlanEntity> list) {
        System.out.printf("%10s%15s%20s%40s%30s\n", "Plan Id", "Flight Number", "Type Plan", "Recurrent End Date", "Number of Flight Schedule");
        for (FlightSchedulePlanEntity plan : list) {
            System.out.printf("%10s%15s%20s%40s%30s\n", plan.getFlightSchedulePlanId(), plan.getFlightNum(), plan.getTypeExistingInPlan(), plan.getRecurringEndDate(), plan.getFlightSchedule().size());
        }
    }

    private void waitForEnterKeyPress() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Press Enter key to continue...> ");
        sc.nextLine();
    }

/*---------------------------------- END OF VIEW ALL FLIGHTS ----------------------------------*/    
    
    
    private void doViewFlightSchedulePlanDetails() {
        try {
            Scanner sc = new Scanner(System.in);
            int response = 0;
            System.out.println("===== View Flight Schedule Plan details =====");
            System.out.print("Enter Flight Schedule Plan ID> ");
            Long id = sc.nextLong();
            
            FlightSchedulePlanEntity plan = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanEntityById(id);
            FlightEntity flight = plan.getFlight();
            FlightRouteEntity route = flight.getFlightRoute();
            List<FlightScheduleEntity> schedule = plan.getFlightSchedule();
            List<FareEntity> fare = plan.getFares();          
            
            System.out.printf("%10s%15s%20s%25s%30s%25s%40s%40s%20s%30s\n", "Plan ID", "Flight Number", "Type Plan", "Flight Schedule ID", "Departure Date", "Duration", "Origin", "Destination", "Cabin Class Type", "Fare");
            
            for (FlightScheduleEntity list : schedule) { 
                for (FareEntity fares : fare) {
                    System.out.printf("%10s%15s%20s%25s%30s%25s%40s%40s%20s%30s\n", plan.getFlightSchedulePlanId(), plan.getFlightNum(), plan.getTypeExistingInPlan(), list.getFlightScheduleId(), list.getDepartureDateTime().toString().substring(0, 19), list.getDuration(), route.getOrigin().getAirportName(), route.getDestination().getAirportName(), fares.getCabinClassType(), fares.getFareAmount());
                }
            }
            System.out.println("--------------------------");
            System.out.println("1: Update Flight Schedule Plan");
            System.out.println("2: Delete Flight Schedule Plan");
            System.out.println("3: Exit\n");
            
            System.out.print("> ");
            response = sc.nextInt();
            
            if(response == 1) {
                updateFlightSchedulePlan(plan);
            }
            else if(response == 2) {
                deleteFlightSchedulePlan(plan);
            } else {
                return ;
            }
    }   catch (FlightSchedulePlanDoNotExistException ex) {
            System.out.println(ex.getMessage());
        }
    }
        
    private void updateFlightSchedulePlan(FlightSchedulePlanEntity plan) throws InvalidCostException {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Update Flight Schedule Plan ***");

        System.out.println("1: Update Fares");
        System.out.println("2: Update Flight Schedules");
        System.out.println("3: Back\n");

        System.out.print("> ");
        int response = sc.nextInt();

        if(response == 1) {
            updateFares(plan);
        }
        else if(response == 2) {
            updateFlightSchedule(plan);
        } 
    
    }
    
    /*========================== START OF updateFares in updateFlightSchedulePlan ======================================== */
    private void updateFares(FlightSchedulePlanEntity plan) throws InvalidCostException {
        try {
            Scanner sc = new Scanner(System.in);
            displayAllFares(plan);
            
            System.out.print("Which fare do you want to update (integer number only)?: ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice < 1 || choice > plan.getFares().size()) {
                System.out.println("ERROR: Invalid option\n Please try again!\n");
                return;
            }
            FareEntity fare = plan.getFares().get(choice - 1);
            System.out.print("Enter new fare amount> ");
            BigDecimal newAmt = sc.nextBigDecimal();
            fareSessionBean.updateFare(fare.getFareId(), newAmt);
            System.out.println("Fare updated successfully!\n");
        } catch (FareDoNotExistException ex) {
            System.out.println("ERROR: " + ex.getMessage() + "\n");
            
        }
    }
    
    private void displayAllFares(FlightSchedulePlanEntity plan) {
        System.out.println(" = All Fares Shown Here =");
        int i = 1;
        for (FareEntity fare : plan.getFares()) {
            System.out.println(i + ") " + fare.getFareBasisCode() + ", $" + fare.getFareAmount());
            i++;
        }
    }
    
     /*========================== END OF updateFares in updateFlightSchedulePlan ======================================== */
    
    
    /*========================== START OF deleteFlightSchedulePlan in doViewFlightSchedulePlanDetails ==========================*/
    private void deleteFlightSchedulePlan(FlightSchedulePlanEntity plan) {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== Delete Flight Schedule Plan =====");
        System.out.print("Are you sure you want to delete (Y/N)> ");
        String response = sc.nextLine().trim();
        
        if(response.equalsIgnoreCase("Y")) {
            try {
                flightSchedulePlanSessionBean.deleteFlightSchedulePlan(plan.getFlightSchedulePlanId());
                System.out.println("Deletion successful!");
            } catch (FlightSchedulePlanDoNotExistException | FlightScheduleNotFoundException | FareDoNotExistException ex) {
                System.out.println("Error: " + ex.getMessage() + "\n");
            }
        }
    }    
     /*========================== END OF deleteFlightSchedulePlan in doViewFlightSchedulePlanDetails ======================================== */

    
    /*============================ START OF updateFlightSchedule in updateFlightSchedulePlan  ==================================*/
    private void updateFlightSchedule(FlightSchedulePlanEntity plan) {
        Scanner sc =  new Scanner(System.in);
        System.out.printf("%30s%30s%20s\n", "Flight Schedule ID", "Departure Date Time", "Duration");
        for (FlightScheduleEntity flightSchedule: plan.getFlightSchedule()) {
            System.out.printf("%30s%30s%20s\n", flightSchedule.getFlightScheduleId(), flightSchedule.getDepartureDateTime().toString().substring(0, 19), flightSchedule.getDuration());
        }
        System.out.print("Which flight schedule would you like to update (ID)> ");
        int flightScheduleId = sc.nextInt();
        sc.nextLine();
        System.out.println();
        System.out.println("1: Update information");
        System.out.println("2: Delete flight schedule");
        System.out.println("3: Cancel\n");

        System.out.print("> ");
        int response = sc.nextInt();

        if(response == 1) {
            try {
                Date departure;
                double duration;
                
                SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss a");
                
                try {
                    System.out.print("Enter new departure Date and Time (dd/mm/yyyy hh:mm:ss AM/PM)> ");
                    String input = sc.nextLine().trim();
                    departure = formatter.parse(input);
                } catch (ParseException ex) {
                    System.out.println("Error: Invalid date and time\nPlease try again\n");
                    return;
                }
                System.out.print("Enter new estimated flight duration (HRS)> ");
                duration = sc.nextDouble();
                flightScheduleSessionBean.updateFlightSchedule(flightScheduleId, departure, duration);
                System.out.println("Flight Schedule " + flightScheduleId + " successfully updated!\n");
            } catch (FlightScheduleNotFoundException | UpdateFlightScheduleException ex) {
                System.out.println("Error: " + ex.getMessage() +"\nPlease try again!\n");
            }
        }
        else if(response == 2) {
            try {
                flightScheduleSessionBean.deleteFlightSchedule(flightScheduleId);
                System.out.println("Flight Schedule " + flightScheduleId + " successfully removed!\n");
            } catch (FlightScheduleNotFoundException | UpdateFlightScheduleException ex) {
                System.out.println("Error: " + ex.getMessage() +"\n");
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