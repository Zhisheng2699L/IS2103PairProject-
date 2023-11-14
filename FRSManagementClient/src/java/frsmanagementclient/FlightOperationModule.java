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
import entity.CabinClassEntity;
import entity.EmployeeEntity;
import entity.FareEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import enumeration.EmployeeAccessRightEnum;
import enumeration.ScheduleTypeEnum;
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
import exceptions.UpdateFlightException;
import exceptions.UpdateFlightScheduleException;
import exceptions.ViolationConstraintsException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javafx.util.Pair;
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
    
    private final Validator validator;
    private final ValidatorFactory validatorFactory;
    
    public FlightOperationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public FlightOperationModule(FlightScheduleSessionBeanRemote flightScheduleSessionBean, EmployeeEntity employee, FlightSessionBeanRemote flightSessionBean, 
            FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean, FlightRouteSessionBeanRemote flightRouteSessionBean, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean, 
            FareSessionBeanRemote fareSessionBean) {
        this.flightScheduleSessionBean = flightScheduleSessionBean;
        this.employee = employee;
        this.flightSessionBean = flightSessionBean;
        this.flightSchedulePlanSessionBean = flightSchedulePlanSessionBean;
        this.flightRouteSessionBean = flightRouteSessionBean;
        this.aircraftConfigurationSessionBean = aircraftConfigurationSessionBean;
        this.fareSessionBean = fareSessionBean;
        
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public void mainMenu() throws ExistingFlightException, FlightRouteDoNotExistException, ViolationConstraintsException, InputDataValidationException, FlightExistException, UnknownPersistenceException, AircraftConfigNotFoundException, InvalidCostException, FlightNotFoundException, ParseException {
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
                        flightList();
                    } else {
                        System.out.println("You are not an Admin or a Schedule Manager!");
                    }
                } else if (response == 2) {
                    if (employee.getAccessRight().equals(EmployeeAccessRightEnum.SCHEDULEMANAGER) || employee.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR)) {
                        flightSchedulePlanList();
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
    public void flightList() throws ExistingFlightException, FlightRouteDoNotExistException, ViolationConstraintsException, InputDataValidationException, FlightExistException, UnknownPersistenceException, AircraftConfigNotFoundException, FlightNotFoundException {
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

    private void createNewflight() throws ExistingFlightException, FlightRouteDoNotExistException, ViolationConstraintsException, InputDataValidationException, UnknownPersistenceException, AircraftConfigNotFoundException, FlightExistException, FlightNotFoundException {
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
            System.out.printf("%30s%40s%25s%20s\n", config.getAircraftConfigId().toString(), config.getName(), config.getNumberOfCabinClasses(), config.getAircraftType().getAircraftTypeName());
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

        
        FlightEntity existingFlight = flightSessionBean.retrieveFlightByFlightNumber(flight.getFlightNum());
            if (existingFlight != null) {
                throw new FlightExistException("Flight with the same number already exists.");
            }
        
        try {
            flight = flightSessionBean.createNewFlight(flight, chosenRoute, chosenConfig);
            System.out.println("Flight created successfully!\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
            return;
        } 
        
        try {
            flight = flightSessionBean.enableFlight(flightNum, chosenRoute, chosenConfig);
            System.out.println("Previous disabled flight found!\nRe-enabling flight...");
        } catch (FlightNotFoundException ex1) {
            System.out.println("Error: Flight " + flightNum + " already exists\nPlease try again!\n");
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
                    } 
                    existingFlight = flightSessionBean.retrieveFlightByFlightNumber(flight.getFlightNum());
                    if (existingFlight != null) {
                        throw new FlightExistException("Flight with the same number already exists.");
                    }
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
    

/*---------------------------------- START OF VIEW ALL FLIGHTS in flightList ----------------------------------*/    
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
/*---------------------------------- END OF viewAllFlight in flightList ----------------------------------*/    

/*----------------------------------- START OF viewFlightDetails (updateFlight/deleteFlight) in flightList ------------------------------*/
    private void viewFlightDetails() throws ExistingFlightException, ViolationConstraintsException {
        try {
            Scanner sc = new Scanner(System.in);
            int response = 0;
            System.out.println("*** View Flight details ***");
            System.out.print("Enter Flight ID> ");
            Long id = sc.nextLong();
            
            FlightEntity flight = flightSessionBean.retreiveFlightById(id);
            FlightRouteEntity route = flight.getFlightRoute();
            AircraftConfigurationEntity config = flight.getAircraftConfig();

            System.out.printf("%10s%20s%20s%35s%20s%35s%25s%30s%40s%25s%20s%20s%30s\n", "Flight ID", "Flight Number", "Flight Route ID", "Origin Airport Name", "Origin Airport IATA", "Destination Airport Name", "Destination Airport IATA", "Aircraft Configuration ID", "Name", "Cabin Class ID", "Max Seats Capacity", "Aircraft Type", "Returning Flight Number");
            for(int i = 0; i< config.getNumberOfCabinClasses(); i++) {
            System.out.printf("%10s%20s%20s%35s%20s%35s%25s%30s%40s%25s%20s%20s%30s\n", flight.getFlightId(), flight.getFlightNum(), route.getFlightRouteId().toString(), route.getOrigin().getAirportName() ,route.getOrigin().getIATACode(), route.getDestination().getAirportName() ,route.getDestination().getIATACode(), config.getAircraftConfigId().toString(), config.getName(), config.getCabin().get(i).getCabinClassId(), config.getCabin().get(i).getMaxSeatCapacity(), config.getAircraftType().getAircraftTypeName(), flight.getReturnFlight()!= null ? flight.getReturnFlight().getFlightNum(): "None");
            }
            System.out.println("--------------------------");
            System.out.println("1: Update Flight");
            System.out.println("2: Delete Flight");
            System.out.println("3: Back\n");
            
            System.out.print("> ");
            response = sc.nextInt();
            
            if(response == 1) {
                updateFlight(flight);
            }
            else if(response == 2) {
                deleteFlight(flight);
            }
        } catch (FlightNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
        }
    }
    
    private void updateFlight(FlightEntity flight) throws ExistingFlightException, ViolationConstraintsException {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Update Flight ***");

        try {
            List<FlightRouteEntity> routes = flightRouteSessionBean.retrieveAllFlightRouteInOrder();
            List<AircraftConfigurationEntity> aircraftConfig = aircraftConfigurationSessionBean.retrieveAllConfiguration();

            System.out.printf("%20s%40s%20s%40s%25s\n", "Flight Route ID", "Origin Airport Name", "Origin Airport IATA", "Destination Airport Name", "Destination Airport IATA");
            for (FlightRouteEntity rte : routes) {
                System.out.printf("%20s%40s%20s%40s%25s\n", rte.getFlightRouteId().toString(), rte.getOrigin().getAirportName(), rte.getOrigin().getIATACode(), rte.getDestination().getAirportName(), rte.getDestination().getIATACode());
            }

            System.out.print("Enter Flight Route (BY ID)(negative number if no change)>  ");
            Long chosenRoute = sc.nextLong();
            sc.nextLine();

            System.out.printf("%30s%40s%25s%30s\n", "Aircraft Configuration ID", "Name", "Number of Cabin Class", "Aircraft Type");
            for (AircraftConfigurationEntity config : aircraftConfig) {
                System.out.printf("%30s%40s%25s%30s\n", config.getAircraftConfigId().toString(), config.getName(), config.getNumberOfCabinClasses(), config.getAircraftType().getAircraftTypeName());
            }

            System.out.print("Enter Aircraft Configuration (BY ID)(negative number if no change)>  ");
            Long chosenConfig = sc.nextLong();
            sc.nextLine();

            List<FlightEntity> list = flightSessionBean.retrieveAllFlightByFlightRoute(flight.getFlightRoute().getDestination().getIATACode(), flight.getFlightRoute().getOrigin().getIATACode());

            boolean display = list.stream().anyMatch(returnFlight -> returnFlight.getOriginFlight()== null && !returnFlight.getFlightId().equals(flight.getFlightId()));

            if (display) {
                System.out.printf("%10s%20s%20s%40s\n", "Flight ID", "Flight Number", "Flight Route", "Aircraft Configuration");
                list.stream()
                    .filter(returnFlight -> returnFlight.getOriginFlight()== null && !returnFlight.getFlightId().equals(flight.getFlightId()))
                    .forEach(returnFlight -> {
                        System.out.printf("%10s%20s%20s%40s\n", returnFlight.getFlightId(), returnFlight.getFlightNum(), returnFlight.getFlightRoute().getOrigin().getIATACode() + " -> " + returnFlight.getFlightRoute().getDestination().getIATACode(), returnFlight.getAircraftConfig().getName());
                    });

                System.out.print("Enter return flight to associate (BY ID)(negative number if no change or none)>  ");
                Long chosenReturnFlight = sc.nextLong();
                sc.nextLine();

                if (chosenReturnFlight > 0) {
                    FlightEntity fe = flightSessionBean.retreiveFlightById(chosenReturnFlight);
                    flight.setReturnFlight(fe);
                }
            }

            // Modify flight details based on user input
            if (chosenRoute > 0) {
                FlightRouteEntity flightRoute = flightRouteSessionBean.retrieveFlightRouteById(chosenRoute);
                flight.setFlightRoute(flightRoute);

                if (flight.getReturnFlight()!= null) {
                    flight.getReturnFlight().setOriginFlight(null);
                }
                flight.setReturnFlight(null);

                if (flight.getOriginFlight()!= null) {
                    flight.getOriginFlight().setReturnFlight(null);
                }
                flight.setOriginFlight(null);
            }

            if (chosenConfig > 0) {
                AircraftConfigurationEntity config = aircraftConfigurationSessionBean.retriveAircraftConfigByID(chosenConfig);
                flight.setAircraftConfig(config);
            }

            Set<ConstraintViolation<FlightEntity>> constraintViolations = validator.validate(flight);
            if (constraintViolations.isEmpty()) {
                flightSessionBean.updateFlight(flight);
                System.out.println("Flight Updated Successfully");
            } else {
                prepareInputDataValidationErrorsMessage(constraintViolations);
            }
        } catch (FlightRouteDoNotExistException | AircraftConfigNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease create the required items first!\n");
        } catch (FlightNotFoundException ex) {
            System.out.println("Error: Flight not found - " + ex.getMessage() + "\nPlease try again!\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println("Error: Unknown Persistence Exception - " + ex.getMessage() + "\nPlease try again!\n");
        }
    }
    
    private void deleteFlight(FlightEntity flight) {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== Delete Flight =====");
        System.out.printf("Confirm delete flight %s (Flight ID: %d) (Enter Y/N)> ", flight.getFlightNum(), flight.getFlightId());
        String reply = sc.nextLine().trim();
        
        if(reply.equalsIgnoreCase("Y")) {
            try {
                flightSessionBean.deleteFlight(flight.getFlightId());
                System.out.println("Flight Deleted Succesfully!\n");
            } catch (FlightNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
/* ---------------------------------- END OF viewFlightDetails (updateFlight/deleteFlight) in flightList -------------------------------- */
    
/* ---------------------------------- START OF flightScheudlePlanList(createFlightSchedulePlan/viewAllFlightSchedulePlan/viewFlightSchedulePlanDetails) ------------------------*/
    private void flightSchedulePlanList() throws InvalidCostException, ParseException {
        Scanner sc = new Scanner(System.in);
        int response = 0;

        while (true) {
            System.out.println("===== Flight Operation Module : Flight Schedule Plan =====\n");
            System.out.println("1: Create Flight Schedule Plan");
            System.out.println("2: View All Flight Schedule Plan");
            System.out.println("3: View Flight Schedule Plan Details");
            System.out.println("4: Exit\n");

            response = 0;
            
            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    createFlightSchedulePlan();
                } else if (response == 2) {
                    viewAllFlightSchedulePlan();
                } else if (response == 3) {
                    viewFlightSchedulePlanDetails();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }
    
    private void createFlightSchedulePlan() {
        try {
            Scanner sc = new Scanner(System.in);
            FlightSchedulePlanEntity plan = new FlightSchedulePlanEntity();
            SimpleDateFormat recurrenceDateFormat = new SimpleDateFormat("dd/M/yyyy");

            //have a list of pair of date and double to store details (after consulting Prof)
            List<Pair<Date, Double>> scheduleInfoList = new ArrayList<>();
            Pair<Date, Double> pair = null;
            int recurrentDayOfWeek = 0;
            
            System.out.println("===== Create Flight Schedule Plan =====");
            
            List<FlightEntity> list = flightSessionBean.retrieveAllFlight();
            
            displayAvailableFlights(list);
            
            System.out.print("Enter flight to create a schedule plan for (By Id): ");
            FlightEntity flight = flightSessionBean.retreiveFlightById(sc.nextLong());
            sc.nextLine();
            
            plan.setFlight(flight);
            plan.setFlightNum(flight.getFlightNum());
            
            System.out.println("Enter Scheudle Type> (1: Single schedule, 2: Multiple schedule, 3: Recurrent schedules every n day, 4: Recurrent schedules every week): ");
            int typeInput = sc.nextInt();
            sc.nextLine();
            
            System.out.println("Create new flight schedule for flight number: " + flight.getFlightNum());
            switch (typeInput) {
                case 1:
                    plan.setTypeExistingInPlan(ScheduleTypeEnum.SINGLE);
                    pair = getFlightScheduleInfo();
                    break;
                case 2:
                    plan.setTypeExistingInPlan(ScheduleTypeEnum.MULTIPLE);
                    int num = getNumberOfSchedulesToCreate(sc);
                    for (int i = 0; i < num; i++) {
                        Pair pair1 = getFlightScheduleInfo();
                        scheduleInfoList.add(pair1);
                    }
                    break;
                case 3:
                    plan.setTypeExistingInPlan(ScheduleTypeEnum.RECURRENTDAY);
                    pair = getFlightScheduleInfo();
                    Date dailyEnd = getRecurrentEndDate(sc, recurrenceDateFormat);
                    plan.setRecurringEndDate(dailyEnd);
                    break;
                case 4:
                    plan.setTypeExistingInPlan(ScheduleTypeEnum.RECURRENTWEEK);
                    pair = getFlightScheduleInfo();
                    recurrentDayOfWeek = getRecurrentDay(sc);
                    Date weekEnd = getRecurrentEndDate(sc, recurrenceDateFormat );
                    plan.setRecurringEndDate(weekEnd);
                    break;   
            }
            
            List<FareEntity> fares = createFaresForCabinClasses(sc, flight, plan);

            Set<ConstraintViolation<FlightSchedulePlanEntity>> constraintViolations = validator.validate(plan);
             if (constraintViolations.isEmpty()) {
                 if (plan.getTypeExistingInPlan().equals(ScheduleTypeEnum.MULTIPLE)) {
                     plan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanMultiple(plan, scheduleInfoList, fares, flight.getFlightId());
                 } else if (plan.getTypeExistingInPlan().equals(ScheduleTypeEnum.RECURRENTDAY)) {
                     System.out.print("Enter interval of recurrence (1-6)> ");
                     int days = sc.nextInt();
                     sc.nextLine();
                     plan = flightSchedulePlanSessionBean.createNewFlightSchedulePlan(plan, fares, flight.getFlightId(), pair, days);
                 } else if (plan.getTypeExistingInPlan().equals(ScheduleTypeEnum.RECURRENTWEEK)) {
                     plan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanWeekly(plan, fares, flight.getFlightId(), pair, recurrentDayOfWeek);
                 } else {
                     plan = flightSchedulePlanSessionBean.createNewFlightSchedulePlan(plan, fares, flight.getFlightId(), pair, 0);
                 }
                 System.out.println("New Flight Schedule Plan for Flight " + plan.getFlightNum() + " created successfully!\n");
             } else {
                 prepareInputDataValidationErrorsMessage1(constraintViolations);
             }
         } catch (Exception ex) {
             ex.printStackTrace();
         }
     }
      
    private void displayAvailableFlights(List<FlightEntity> list) {
        System.out.printf("%10s%20s%20s%25s\n", "Flight ID", "Flight Number", "Flight Route", "Aircraft Configuration");
        for (FlightEntity flight : list) {
            System.out.printf("%10s%20s%20s%25s\n", flight.getFlightId(), flight.getFlightNum(), flight.getFlightRoute().getOrigin().getIATACode() + " -> " + flight.getFlightRoute().getDestination().getIATACode(), flight.getAircraftConfig().getName());
        }
    }
    
    private Pair<Date, Double> getFlightScheduleInfo() throws ParseException {
        Date departure;
        double duration;
        
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss a");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss a");
        
        System.out.print("Enter departure Date and Time (dd/mm/yyyy hh:mm:ss AM/PM): ");
        String input = sc.nextLine().trim();
        departure = formatter.parse(input);
        System.out.print("Enter estimated flight duration (HOURS): ");
        duration = sc.nextDouble();
        return new Pair<>(departure, duration);
    }
    
    private int getNumberOfSchedulesToCreate(Scanner sc) {
        System.out.print("Enter number of schedule to be created> ");
        return sc.nextInt();
    }
    
    private Date getRecurrentEndDate(Scanner sc, SimpleDateFormat dateFormat) throws ParseException {
        System.out.print("Enter recurrent end date (dd/mm/yyyy)> ");
        String date = sc.nextLine().trim();
        return dateFormat.parse(date);
    }

    private int getRecurrentDay(Scanner sc) {
        System.out.print("Enter recurrent day (1 -> Sunday, 7 -> Saturday)> ");
        return sc.nextInt();
    }
     
    private List<FareEntity> createFaresForCabinClasses(Scanner sc, FlightEntity flight, FlightSchedulePlanEntity plan) {
        List<CabinClassEntity> cabinClass = flight.getAircraftConfig().getCabin();
        System.out.println("Aircraft Configuration for flight " + flight.getFlightNum() + " contains " + cabinClass.size() + " cabins");
        System.out.println("Please enter fares for each cabin class!\n");

        List<FareEntity> fares = new ArrayList<>();
        for (CabinClassEntity cc : cabinClass) {
            String type = getCabinClassType(cc);
            System.out.println("** Creating fare for " + type + " **");
            while (true) {
                fares.add(createFareEntity(cc));
                System.out.print("Would you like to add more fares to this cabin class? (Y/N)> ");
                String reply = sc.nextLine().trim();
                if (!reply.equalsIgnoreCase("Y")) {
                    break;
                }
            }
        }
        plan.setFares(fares);
        System.out.println("Fares successfully created!\n");
        
        return fares;
    }
    
    private String getCabinClassType(CabinClassEntity cc) {
        String type = "";
        if (cc.getCabinClassType() != null) {
            switch (cc.getCabinClassType()) {
                case F:
                    type = "First Class";
                    break;
                case J:
                    type = "Business Class";
                    break;
                case W:
                    type = "Premium Economy Class";
                    break;
                case Y:
                    type = "Economy Class";
                    break;
            }
        }
        return type;
    }
    
    private FareEntity createFareEntity(CabinClassEntity cabinclass) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter fare basis code (2 to 6 numbers)> ");
        String code = cabinclass.getCabinClassType() + sc.next().trim();
        System.out.print("Enter fare amount> ");
        BigDecimal cost = sc.nextBigDecimal();
        FareEntity fare = new FareEntity(code, cost, cabinclass.getCabinClassType());
        return fare;
    }

    /*---------------------------------*/
    private void viewAllFlightSchedulePlan() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** View All Flight Schedule Plans ***");
  
        List<FlightSchedulePlanEntity> list;
        try {
            list = flightSchedulePlanSessionBean.retrieveAllFlightSchedulePlan();
        } catch (FlightSchedulePlanDoNotExistException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n");
            return;
        }
        System.out.printf("%10s%15s%20s%40s%30s\n", "Plan ID", "Flight Number", "Type Plan", "Recurrent End Date", "Number of Flight Schedule");
        for (FlightSchedulePlanEntity plan : list) {
            System.out.printf("%10s%15s%20s%40s%30s\n", plan.getFlightSchedulePlanId(), plan.getFlightNum(), plan.getTypeExistingInPlan(), plan.getRecurringEndDate(), plan.getFlightSchedule().size());
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();     
    }
    
    
    
    /*----------------------------------*/
    private void viewFlightSchedulePlanDetails() throws InvalidCostException, java.text.ParseException {
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
        
    private void updateFlightSchedulePlan(FlightSchedulePlanEntity plan) throws InvalidCostException, java.text.ParseException {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== Update Flight Schedule Plan =====");

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
/* ---------------------------------- START OF flightScheudlePlanList(createFlightSchedulePlan/viewAllFlightSchedulePlan/viewFlightSchedulePlanDetails) ------------------------*/
    
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
    private void updateFlightSchedule(FlightSchedulePlanEntity plan) throws java.text.ParseException {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss a");

        System.out.printf("%30s%30s%20s\n", "Flight Schedule ID", "Departure Date Time", "Duration");

        for (FlightScheduleEntity flightSchedule: plan.getFlightSchedule()) {
            System.out.printf("%30s%30s%20s\n", 
                flightSchedule.getFlightScheduleId(),
                flightSchedule.getDepartureDateTime().toString().substring(0, 19),
                flightSchedule.getDuration()
            );
        }

        System.out.print("Which flight schedule would you like to update (ID)> ");
        int flightScheduleId = sc.nextInt();
        sc.nextLine();
        System.out.println("\n1: Update information");
        System.out.println("2: Delete flight schedule");
        System.out.println("3: Cancel\n");
        System.out.print("> ");
        int response = sc.nextInt();

        if (response == 1) {
            try {
                System.out.print("Enter new departure Date and Time (dd/mm/yyyy hh:mm:ss AM/PM)> ");
                String input = sc.nextLine().trim();
                Date departure = formatter.parse(input);

                System.out.print("Enter new estimated flight duration (HRS)> ");
                double duration = sc.nextDouble();

                flightScheduleSessionBean.updateFlightSchedule(flightScheduleId, departure, duration);
                System.out.println("Flight Schedule " + flightScheduleId + " successfully updated!\n");
            } catch (ParseException ex) {
                System.out.println("Error: Invalid date and time format. Please try again.\n");
            } catch (FlightScheduleNotFoundException | UpdateFlightScheduleException ex) {
                System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
            }
        } else if (response == 2) {
            try {
                flightScheduleSessionBean.deleteFlightSchedule(flightScheduleId);
                System.out.println("Flight Schedule " + flightScheduleId + " successfully removed!\n");
            } catch (FlightScheduleNotFoundException | UpdateFlightScheduleException ex) {
                System.out.println("Error: " + ex.getMessage() + "\n");
            }
        }
    }
    
    private void prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightEntity>> constraintViolations) {
        System.out.println("\nInput data validation error!:");
        for(ConstraintViolation constraintViolation:constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }
        System.out.println("\nPlease try again......\n");
    }
    
    private void prepareInputDataValidationErrorsMessage1(Set<ConstraintViolation<FlightSchedulePlanEntity>> constraintViolations) {
        System.out.println("\nInput data validation error!:");
        for(ConstraintViolation constraintViolation:constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }
        System.out.println("\nPlease try again......\n");
    }
    

}