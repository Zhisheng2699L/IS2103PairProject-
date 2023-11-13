/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CabinClassSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import entity.AircraftConfigurationEntity;
import entity.AircraftTypeEntity;
import entity.AirportEntity;
import entity.CabinClassEntity;
import entity.EmployeeEntity;
import entity.FlightRouteEntity;
import enumeration.CabinClassTypeEnum;
import enumeration.EmployeeAccessRightEnum;
import exceptions.AircraftConfigNotFoundException;
import exceptions.AircraftTypeDoNotExistException;
import exceptions.AirportDoNotExistException;
import exceptions.CreateNewAircraftConfigErrorException;
import exceptions.ExistingAircraftConfigException;
import exceptions.FlightRouteDoNotExistException;
import exceptions.FlightRouteExistException;
import exceptions.UnknownPersistenceException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author kahjy
 */
public class FlightRoutePlanningModule {
    
    //requires employee, airport, aircraftconfig, aircrafttype, cabinclass, flightroutesesion
    private EmployeeEntity employee;
    private AirportSessionBeanRemote airportSessionBean;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBean;
    private CabinClassSessionBeanRemote cabinClassSessionBean;
    private FlightRouteSessionBeanRemote flightRouteSessionBean;
    
    public FlightRoutePlanningModule(EmployeeEntity employee, AirportSessionBeanRemote airportSessionBean, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean, 
            AircraftTypeSessionBeanRemote aircraftTypeSessionBean, CabinClassSessionBeanRemote cabinClassSessionBean, FlightRouteSessionBeanRemote flightRouteSessionBean) {
        this.employee = employee;
        this.airportSessionBean = airportSessionBean;
        this.aircraftConfigurationSessionBean = aircraftConfigurationSessionBean;
        this.aircraftTypeSessionBean = aircraftTypeSessionBean;
        this.cabinClassSessionBean = cabinClassSessionBean;
        this.flightRouteSessionBean = flightRouteSessionBean;
    }
    
     public void mainMenu() throws UnknownPersistenceException, ExistingAircraftConfigException, CreateNewAircraftConfigErrorException, AirportDoNotExistException {
        Scanner sc = new Scanner(System.in);
        int response = 0;
        
        while(true) {
            System.out.println("=======Flight Route Planning Module=====\n");
            System.out.println("Choose a system to enter:");
            System.out.println("1: Aircraft Configuration System");
            System.out.println("2: Flight Route System");
            System.out.println("3: Exit\n");
            
            response = 0;
            while(response < 1 || response > 3) {
                System.out.print("> ");
                response = sc.nextInt();
                
                if(response == 1) {
                    if(employee.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || employee.getAccessRight().equals(EmployeeAccessRightEnum.FLEETMANAGER)) {
                    aircraftConfigDetails();
                    } else {
                        System.out.println("Invalid EmployeeAccessRight, make sure you're an Administrator or Fleet Manager!");
                    }
                } else if (response == 2) {
                    if(employee.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || employee.getAccessRight().equals(EmployeeAccessRightEnum.ROUTEPLANNER)) {
                        flightRouteDetails();
                    } else {
                        System.out.println("Invalid EmployeeAccessRight, make sure you're an Administrator or RoutePlanner!");
                    }
                } else if(response == 3) {
                    break;
                } else {
                    System.out.println("Invalid input, try again with 1/2");
                }   
            }
            
            if (response == 3) {
                break;
            }
        }
    }
    
    private void aircraftConfigDetails() throws CreateNewAircraftConfigErrorException, UnknownPersistenceException, ExistingAircraftConfigException {
        Scanner sc = new Scanner(System.in);
        int response = 0;
        
        while(true) {
            System.out.println("Flight Route Planning Module : Aircraft Configuration \n");
            System.out.println("1: Create Aircraft Configuration");
            System.out.println("2: View All Aircraft Configuration");
            System.out.println("3: View Aircraft Cofiguration Details");
            System.out.println("4: Exit\n");
            
            response = 0;
            while(response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();
                
                if(response == 1) {
                    createAircraftConfig();
                } else if(response == 2) {
                    viewAllAircraftConfig();
                } else if(response == 3) {
                    viewAircraftConfigDetails();
                } else if(response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 4) {
                break;
            }
        }  
    }

    
    private void createAircraftConfig() throws CreateNewAircraftConfigErrorException, UnknownPersistenceException, ExistingAircraftConfigException {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("=====Create aircraft configuration=====");
            
            //Display available aircraft types
            List<AircraftTypeEntity> aircraftTypes = aircraftTypeSessionBean.retrieveAllAircraftType();    
            System.out.printf("%20s%25s%20s\n", "Aircraft Type ID", "Name", "Maximum Capacity");
            
            
            for(AircraftTypeEntity type : aircraftTypes) {
                System.out.printf("%20s%25s%20s\n", type.getAircraftID().toString(), type.getAircraftTypeName(), type.getMaxCapacity());
            }
            
            System.out.print("Enter aircraft type, INT ID: "); 
            Long typeId = getUserLongInput(sc);
            AircraftTypeEntity selectedAircraftType = aircraftTypeSessionBean.retrieveAircraftTypeById(typeId);
            
            System.out.print("Enter name> ");
            String name = sc.nextLine().trim();
            
            System.out.print("Enter cabin class number (1-4 only)> ");
            int cabinNum = getUserIntInput(sc, 1, 4);
            System.out.println();
            
            //Create the Aircraft Configuration here
            AircraftConfigurationEntity aircraftConfig = new AircraftConfigurationEntity(name, cabinNum);
                       
            List<CabinClassEntity> cabinClasses = new ArrayList<>();
            for(int i = 0; i < cabinNum; i++) {
                cabinClasses.add(createCabinClass());
            }
            
            aircraftConfig = aircraftConfigurationSessionBean.createNewAircraftConfig(aircraftConfig, selectedAircraftType.getAircraftID(), cabinClasses);
            System.out.println("Aircraft Configuration created for a " + aircraftConfig.getAircraftType().getAircraftTypeName() + " Type plane\n");
        } catch (AircraftTypeDoNotExistException | CreateNewAircraftConfigErrorException | UnknownPersistenceException | ExistingAircraftConfigException ex) {
            System.out.println("Error occured in creating Aircraft Configuration: " + ex.getMessage());   
            System.out.println("Please try again!"); 
        } 
    }
    
    private Long getUserLongInput(Scanner sc) {
        while (true) {
            try {
                return sc.nextLong();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine(); 
            }
        }
    }
    
    private int getUserIntInput(Scanner sc, int min, int max) {
        while (true) {
            try {
                int input = sc.nextInt();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine(); // Clear the invalid input from the scanner
            }
        }
    }

    private CabinClassEntity createCabinClass() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=====Create a new cabin class=====");

        CabinClassTypeEnum type = getCabinClassType(sc);
        int aisles = getIntegerInput(sc, "Enter number of aisles (0 to 2 only)> ", 0, 2);
        int rows = getIntegerInput(sc, "Enter number of rows> ", 1, Integer.MAX_VALUE);
        int seatsAbreast = getIntegerInput(sc, "Enter number of seats abreast> ", 1, Integer.MAX_VALUE);
        String config = getSeatingConfiguration(sc, aisles, seatsAbreast);

        return new CabinClassEntity(type, aisles, rows, seatsAbreast, config, rows * seatsAbreast);
    }

    private CabinClassTypeEnum getCabinClassType(Scanner sc) {
        while (true) {
            try {
                System.out.print("Enter class: First Class(F)/Business Class(J)/Premium Economy Class(W)/Economy Class(Y)> ");
                String input = sc.nextLine().trim();
                return CabinClassTypeEnum.valueOf(input);
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid cabin class type. Please enter a valid type.");
            }
        }
    }

    private int getIntegerInput(Scanner sc, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = Integer.parseInt(sc.nextLine().trim());
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Please enter a value within the specified range.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String getSeatingConfiguration(Scanner sc, int aisles, int seatsAbreast) {
        while (true) {
            String format = (aisles == 1) ? "Seating configuration (x-y format)> "
                                          : "Seating configuration (x-y-z format)> ";
            System.out.print(format);
            String config = sc.nextLine().trim();

            String[] parts = config.split("-");
            if (parts.length == aisles + 1) {
                int totalSeats = 0;
                try {
                    for (String part : parts) {
                        int seatsInColumn = Integer.parseInt(part);
                        totalSeats += seatsInColumn;
                    }
                    if (totalSeats == seatsAbreast) {
                        return config;
                    } else {
                        System.out.println("Total seats must match the seats abreast declared.");
                    }
                } catch (NumberFormatException ex) {
                    // Invalid number format, continue to the next iteration
                    System.out.println("Please enter valid numbers for seating configuration.");
                }
            }
            System.out.println("Invalid seating configuration format. Use the specified format.");
        }
    }   

    
    private void viewAllAircraftConfig() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=====View all aircraft configuration=====");
        List<AircraftConfigurationEntity> list;
        try {
            list = aircraftConfigurationSessionBean.retrieveAllConfiguration();
        } catch (AircraftConfigNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\n Try again!!\n");
            return;
        }
        System.out.printf("%30s%40s%25s%20s\n", "Config ID", "Name", "Cabin Classes", "Aircraft Type");
        
        for(AircraftConfigurationEntity config : list) {
            System.out.printf("%30s%40s%25s%20s\n", config.getAircraftConfigId().toString(), config.getName(), config.getNumberOfCabinClasses(), config.getAircraftType().getAircraftTypeName());
        }
        System.out.print("Press any key to continue ");
        sc.nextLine();
    }
    
    private void viewAircraftConfigDetails() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("===== View aircraft configuration details =====");
            System.out.print("Enter configuration ID> ");
            Long id = sc.nextLong();
            sc.nextLine();

            AircraftConfigurationEntity config = aircraftConfigurationSessionBean.retriveAircraftConfigByID(id);
            int totalSeats = 0;
            
            for (CabinClassEntity cabin: config.getCabin()) {
                totalSeats += cabin.getMaxSeatCapacity();
            }
            
            System.out.println("\nConfiguration Name: " + config.getName());
            System.out.println("Number of Cabin Classes: " + config.getCabin().size());
            System.out.println();
            
            for (CabinClassEntity cabin : config.getCabin()) {
                totalSeats += cabin.getMaxSeatCapacity();
                System.out.println("  - Cabin Class: " + cabin.getCabinClassType() +
                                   ", Aisles: " + cabin.getNumOfAisles() +
                                   ", Rows: " + cabin.getNumRows() +
                                   ", Seats Abreast: " + cabin.getNumSeatsAbreast() +
                                   ", Seating Configuration: " + cabin.getSeatingConfigPerColumn() +
                                   ", Total Seats: " + cabin.getMaxSeatCapacity());
            }

            System.out.println("\nTotal Seats in Configuration: " + totalSeats);
        
            System.out.println();
            System.out.print("Press any key to continue...> ");
            sc.nextLine();

        } catch (AircraftConfigNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void flightRouteDetails() throws AirportDoNotExistException {
        Scanner sc = new Scanner(System.in);
        int response;

        while (true) {
            System.out.println("=== Flight Planning Module :: Flight Route ===\n");
            System.out.println("1: Create Flight Route");
            System.out.println("2: View All Flight Routes");
            System.out.println("3: Delete Flight Route");
            System.out.println("4: Exit\n");

            response = getUserChoice(sc, 1, 4);

            switch (response) {
                case 1:
                    createFlightRoute();
                    break;
                case 2:
                    doViewAllFlightRoute();
                    break;
                case 3:
                    deleteFlightRoute();
                    break;
                case 4:
                    System.out.println("Exiting Flight Route Menu...");
                    return;
            }
        }  
    }
    
    private int getUserChoice(Scanner sc, int min, int max) {
        int choice;
        while (true) {
            System.out.print("> ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline
            if (choice >= min && choice <= max) {
                return choice;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
    }

    /*private void createFlightRoute() throws AirportDoNotExistException, FlightRouteDoNotExistException {
        AirportEntity originAirport, destinationAirport;
        Scanner sc = new Scanner(System.in);
        System.out.println("===== Create a new Flight Route =====");                
        System.out.print("Enter IATA code of origin airport: ");                         
        String origin = sc.nextLine().trim();
        System.out.print("Enter IATA code of destination airport: ");
        String destination = sc.nextLine().trim();
        FlightRouteEntity flightRoute = new FlightRouteEntity(); 
        
        try {
            // Retrieve origin and destination airports
            originAirport = airportSessionBean.retrieveAirportByIATA(origin);
            destinationAirport = airportSessionBean.retrieveAirportByIATA(destination);
            
            // Create a new flight route
            flightRoute = flightRouteSessionBean.createNewFlightRoute(flightRoute, originAirport.getAirportID(), destinationAirport.getAirportID());
            
            System.out.println("Flight Route successfully created!");
            
        } catch (UnknownPersistenceException | AirportDoNotExistException ex) {          
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
            return;
        } catch (FlightRouteExistException ex) {
            try {                         
                originAirport = airportSessionBean.retrieveAirportByIATA(origin);            
                destinationAirport = airportSessionBean.retrieveAirportByIATA(destination);
                flightRoute = flightRouteSessionBean.enableFlightRoute(originAirport.getAirportID(), destinationAirport.getAirportID());
                System.out.println("Previous disabled flight route entry found!\nRe-enabling flight route...");   // when flight route exists but disabled    
            } catch (FlightRouteDoNotExistException ex1) {
                System.out.println("Error: Flight route already exists in system!\n"); // when flight route already exists and is enabled
                return;
            } catch (AirportDoNotExistException ex1) {
               return; // will never hit this
            }
        } 
        System.out.print("Flight Route successfully created!\nWould you like to create its complementary return route? (Y or N)> ");
        String reply = sc.nextLine().trim();
        
        if((reply.equals("Y") || reply.equals("y"))) {
            try {     
                FlightRouteEntity returnFlightRoute = flightRouteSessionBean.createNewFlightRoute(new FlightRouteEntity(), destinationAirport.getAirportID(),originAirport.getAirportID());    
                flightRouteSessionBean.setComplementaryFlightRoute(returnFlightRoute.getFlightRouteId());
                System.out.println("Complementary return route created!\n"); // when no return flight route exists
            } catch (FlightRouteExistException ex) {             
                
                try {                            
                    flightRoute = flightRouteSessionBean.enableFlightRoute(destinationAirport.getAirportID(), originAirport.getAirportID());             
                    flightRouteSessionBean.setComplementaryFlightRoute(flightRoute.getFlightRouteId());                           
                    System.out.println("Previous disabled flight route entry found!\nRe-enabling flight route and pairing complementary flight routes...\n");   // when return flight route exists but disabled    
                } catch (FlightRouteDoNotExistException ex1) { // will be thrown by the code 3 lines above (not 2)
                    System.out.println("Complementary return route already exists!\nPairing complementary flight routes..\n"); // when return flight route already exists and is enabled
                    try {
                        flightRouteSessionBean.setComplementaryFlightRoute(flightRoute.getFlightRouteId());
                    } catch (FlightRouteDoNotExistException ex2) {
                        System.out.println("Error:" + ex2.getMessage() + "\n"); //will never hit this 
                    }
                } 
              
            } catch (UnknownPersistenceException | AirportDoNotExistException | FlightRouteDoNotExistException ex) {
               System.out.println("Error:" + ex.getMessage() + "\n"); // will never hit this
            } 
                     
        } 
    }*/
    
    private void createFlightRoute() throws AirportDoNotExistException {
    Scanner sc = new Scanner(System.in);
    System.out.println("*** Create a New Flight Route ***");

    // Input origin and destination airports
    System.out.print("Enter IATA code of origin airport: ");
    String origin = sc.nextLine().trim();
    System.out.print("Enter IATA code of destination airport: ");
    String destination = sc.nextLine().trim();

    FlightRouteEntity flightRoute = new FlightRouteEntity();

    try {
        // Retrieve origin and destination airports
        AirportEntity originAirport = airportSessionBean.retrieveAirportByIATA(origin);
        AirportEntity destinationAirport = airportSessionBean.retrieveAirportByIATA(destination);

        // Create a new flight route
        flightRoute = flightRouteSessionBean.createNewFlightRoute(new FlightRouteEntity(),
                originAirport.getAirportID(), destinationAirport.getAirportID());

        System.out.println("Flight Route successfully created!");

        // Check if the user wants to create a complementary return route
        System.out.print("Would you like to create its complementary return route? (Y or N): ");
        String reply = sc.nextLine().trim();

        if (reply.equalsIgnoreCase("Y")) {
            try {
                // Create the complementary return route
                FlightRouteEntity returnFlightRoute = flightRouteSessionBean.createNewFlightRoute(
                        new FlightRouteEntity(), destinationAirport.getAirportID(), originAirport.getAirportID());

                // Pair the complementary routes
                flightRouteSessionBean.setComplementaryFlightRoute(returnFlightRoute.getFlightRouteId());

                System.out.println("Complementary return route created and paired!");
            } catch (FlightRouteExistException ex) {
                System.out.println("Complementary return route already exists and is paired.");
            }
        }
        } catch (UnknownPersistenceException | AirportDoNotExistException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!");
        } catch (FlightRouteExistException ex) {
            System.out.println("Error: Flight route already exists!\nPlease try again!");
        } catch (FlightRouteDoNotExistException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!");
        }
    }
  
    private void doViewAllFlightRoute() {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== View all flight routes =====");
        List<FlightRouteEntity> list;
        
        try {
            list = flightRouteSessionBean.retrieveAllFlightRouteInOrder();
        } catch (FlightRouteDoNotExistException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
            return;
        }
        
        System.out.println("Flight Routes: ");
        System.out.printf("%20s%40s%20s%40s%25s\n", "Flight Route ID", "Origin Airport Name", "Origin Airport IATA", "Destination Airport Name", "Destination Airport IATA");

        for (FlightRouteEntity route : list) {
            System.out.printf("%20s%40s%20s%40s%25s\n", route.getFlightRouteId().toString(), route.getOrigin().getAirportName() ,route.getOrigin().getIATACode(), route.getDestination().getAirportName() ,route.getDestination().getIATACode());
        }
        
        System.out.print("Press Enter to continue... ");
        sc.nextLine();
    }

    private void deleteFlightRoute() {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== Delete flight route =====");
        List<FlightRouteEntity> list;
        
        try {
            list = flightRouteSessionBean.retrieveAllFlightRouteInOrder();
            
            if (list.isEmpty()) {
                System.out.println("No flight routes found for deletion.");
                return;
            }
        } catch (FlightRouteDoNotExistException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
            return;
        }
        System.out.printf("%20s%35s%20s%35s%25s\n", "Flight Route ID", "Origin Airport Name", "Origin Airport IATA", "Destination Airport Name", "Destination Airport IATA");
        
        for(FlightRouteEntity route : list) {
            System.out.printf("%20s%35s%20s%35s%25s\n", route.getFlightRouteId().toString(), route.getOrigin().getAirportName() ,route.getOrigin().getIATACode(), route.getDestination().getAirportName() ,route.getDestination().getIATACode());
        }
        
        System.out.print("Select flight route to delete (By ID)> ");
        long id = sc.nextLong();
        
        try {
            flightRouteSessionBean.removeFlightRoute(id);
            System.out.println("Flight Route successfully removed!\n");
        } catch (FlightRouteDoNotExistException ex) {
            System.out.println("Error: " + ex.getMessage() + "Flight route not found. \nPlease try again!\n");
        }
        
        
    }
    
    
}
