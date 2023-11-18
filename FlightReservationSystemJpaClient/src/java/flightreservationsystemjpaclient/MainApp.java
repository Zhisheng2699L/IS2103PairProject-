/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightreservationsystemjpaclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.ItinerarySessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.PassengerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.SeatsInventorySessionBeanRemote;
import entity.CustomerEntity;
import entity.FareEntity;
import entity.FlightEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.ItineraryEntity;
import entity.PassengerEntity;
import entity.ReservationEntity;
import entity.SeatInventoryEntity;
import enumeration.CabinClassTypeEnum;
import exceptions.CabinClassNotFoundException;
import exceptions.CustomerExistException;
import exceptions.FlightNotFoundException;
import exceptions.FlightScheduleNotFoundException;
import exceptions.InputDataValidationException;
import exceptions.InvalidInputGeneralException;
import exceptions.InvalidLoginCredentialException;
import exceptions.ItineraryDoNotExistException;
import exceptions.ItineraryExistException;
import exceptions.ReservationExistException;
import exceptions.SeatAlreadyBookedException;
import exceptions.SeatInventoryNotFoundException;
import exceptions.SeatSlotNotFoundException;
import exceptions.UnknownPersistenceException;
import exceptions.UserNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import javafx.util.Pair;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


/**
 *
 * @author foozh
 */
public class MainApp {

    private EmployeeSessionBeanRemote employeeSessionBean;
    private PartnerSessionBeanRemote partnerSessionBean;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBean;
    private AirportSessionBeanRemote airportSessionBean;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;
    private FlightSessionBeanRemote flightSessionBean;
    private FlightRouteSessionBeanRemote flightRouteSessionBean;
    private FareSessionBeanRemote fareSessionBean;
    private SeatsInventorySessionBeanRemote seatsInventorySessionBean;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBean;
    private CustomerSessionBeanRemote customerSessionBean;
    private ItinerarySessionBeanRemote itinerarySessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private PassengerSessionBeanRemote passengerSessionBean;
    
    private CustomerEntity currentCustomer;
    private boolean login;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();    
    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBean, PartnerSessionBeanRemote partnerSessionBean, AircraftTypeSessionBeanRemote aircraftTypeSessionBean, AirportSessionBeanRemote airportSessionBean, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean, FlightSessionBeanRemote flightSessionBean,
            FlightRouteSessionBeanRemote flightRouteSessionBean, FareSessionBeanRemote fareSessionBean,
            SeatsInventorySessionBeanRemote seatsInventorySessionBean, FlightScheduleSessionBeanRemote flightScheduleSessionBean,
            CustomerSessionBeanRemote customerSessionBean, ItinerarySessionBeanRemote itinerarySessionBean,
            ReservationSessionBeanRemote reservationSessionBean, PassengerSessionBeanRemote passengerSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
        this.partnerSessionBean = partnerSessionBean;
        this.aircraftTypeSessionBean = aircraftTypeSessionBean;
        this.airportSessionBean = airportSessionBean;
        this.aircraftConfigurationSessionBean = aircraftConfigurationSessionBean;
        this.flightSchedulePlanSessionBean = flightSchedulePlanSessionBean;
        this.flightSessionBean = flightSessionBean;
        this.flightRouteSessionBean = flightRouteSessionBean;
        this.fareSessionBean = fareSessionBean;
        this.seatsInventorySessionBean = seatsInventorySessionBean;
        this.flightScheduleSessionBean = flightScheduleSessionBean;
        this.customerSessionBean = customerSessionBean;
        this.itinerarySessionBean = itinerarySessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.passengerSessionBean = passengerSessionBean;
    // Common initialization
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    public void runApp() throws UnknownPersistenceException, CustomerExistException, InvalidLoginCredentialException {
        
        while(true) {
            
            if(!login) {
                Scanner sc = new Scanner(System.in);
                Integer response = 0;
                
                System.out.println("=== Welcome to Flight Reservation System===\n");
                System.out.println("1: Customer Login");
                System.out.println("2: Register for new Customer Account");
                System.out.println("3: Search Flight");
                System.out.println("4: Exit\n");
                
                response = 0;
                while(response < 1 || response > 4) {
                    System.out.print("Choose your option : ");
                    if(response == 1) {      
                        try{    
                            doLogin();
                            System.out.println("You have successfully logged into the system");
                            login = true;   
                        } catch (InvalidLoginCredentialException ex) {      
                            ex.printStackTrace();
                        } 
                    } else if (response == 2) {
                        try {
                            doRegisterCustomer();
                        } catch (Exception ex) {
                           ex.printStackTrace();      
                        } 
                    } else if(response == 3) {
                            
                        try {
                            searchForFlight();
                        } catch (Exception ex) {
                           ex.printStackTrace(); 
                        }   
                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.println("Stop playing with the buttons. Try again");
                    } 
                    
                    if (response == 4) {
                        break;
                    } else {
                        customerMainMenu();    
                    }

                }
            
            }
        }
        
    }
            
    
    
    private void doLogin() throws InvalidLoginCredentialException {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** LOGIN TO RESERVATION SYSTEM ***\n");
        
        System.out.print("Please enter your username : ");
        String username = sc.nextLine().trim();
        
        System.out.print("Enter password : ");
        String password = sc.nextLine().trim();
        
        if(username != null && password != null && username.length() > 0 && password.length() > 0) {   
             currentCustomer = customerSessionBean.doLogin(username, password);
        } else {
        
            throw new InvalidLoginCredentialException("Login is invalid my friend");
        }             
    }
    
    private void doRegisterCustomer() throws UnknownPersistenceException, CustomerExistException, InvalidLoginCredentialException {
         Scanner sc = new Scanner(System.in) ;
         System.out.println("***REGISTER FOR NEW CUSTOMER ACCOUNT***\n");
         
        System.out.print("Enter first name> ");
        String firstName = sc.nextLine().trim();
        
        System.out.print("Enter last name> ");
        String lastName = sc.nextLine().trim();
        
        System.out.print("Enter identification number> ");
        String idNum = sc.nextLine().trim();
        
        System.out.print("Enter contact number> ");
        String contactNumber = sc.nextLine().trim();
        
        System.out.print("Enter address> ");
        String address = sc.nextLine().trim();
        
        System.out.print("Enter postal code> ");
        String postalCode = sc.nextLine().trim();
        
        System.out.print("Enter desired username> ");
        String username = sc.nextLine().trim();
        
        System.out.print("Enter desired password> ");
        String password = sc.nextLine().trim();
        
        if(firstName != null && lastName != null && idNum != null && contactNumber != null && address != null 
                && postalCode != null && username!=null && password != null) {
            
            CustomerEntity customer = new CustomerEntity(firstName, lastName, idNum, contactNumber, address, postalCode, username, password);
            currentCustomer = customerSessionBean.createNewCustomerEntity(customer);
            System.out.println("Account successfully created for ID " + currentCustomer.getIdentificationNumber());
            login = true;
            customerMainMenu();
        }else {
            throw new InvalidLoginCredentialException("Invalid Account Credentials my friend ");
        }    
    }
    
    private void customerMainMenu() {
        Scanner sc = new Scanner(System.in);
        int response = 0;
        while (login) {
            System.out.println("*** Welcome to Airlines Reservation Client ***\n");
            System.out.println("You are currently logged in as " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName() + "!\n");
            System.out.println();
            System.out.println("*** These are the functions you have access to ***");
            System.out.println("1 : Reserve Flight");
            System.out.println("2 : View My Flight Reservations");
            System.out.println("3 : View My Flight Reservation Details");
            System.out.println("4 : Log Out");
            
            response = 0;
            
            while (response < 1 || response > 4) {
                System.out.print(" : ");
                response = sc.nextInt();
            
                if(response == 1) {
                    try { 
                        searchForFlight();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (response == 2) {
                    try{
                        viewFlightReservations();
                    } catch (Exception ex) {
                        ex.printStackTrace();                       
                    }
                } else if (response == 3) {
                    try {   
                        viewMyFlightReservationDetails();         
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if(response == 4) {
                
                    try {   
                        doLogOut();         
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }    
                } else {
                    System.out.println("Stop playing with my buttons. Try again");
                }
                     
            }
            if (response == 4) {
                break;
            }       
        }
    }
  
    private void searchForFlight() throws InvalidInputGeneralException, FlightNotFoundException, CabinClassNotFoundException, FlightScheduleNotFoundException, ReservationExistException, UnknownPersistenceException, SeatSlotNotFoundException, SeatAlreadyBookedException, ItineraryDoNotExistException, InputDataValidationException, SeatInventoryNotFoundException, UserNotFoundException, ItineraryExistException {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/M/yyyy");
        Pattern pattern = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
        System.out.println("*** SEARCH FOR FLIGHT ***\n");
        
        int typeOfFlight;
        while (true) {
            System.out.println("Enter the Trip Type");
            System.out.println("1 : One Way Trip please");
            System.out.println("2 : Round-Trip/Return");
            System.out.print("Enter your choice : ");
            typeOfFlight = sc.nextInt();
            sc.nextLine();
            if (typeOfFlight != 1 && typeOfFlight != 2) {
                System.out.println("Invalid Trip Type! Please try again!");
            } else {
                break;
            }
        }
        
        System.out.print("Please enter the IATA Code of your departure airport : ");
        String departure = sc.nextLine().trim();

        System.out.print("Please enter the IATA Code of your  destination airport :");
        String destination = sc.nextLine().trim();
        
        Date departureDate;
        while (true) {
            try {
                System.out.print("Enter the departure date (dd/mm/yyyy) : ");
                String date = sc.nextLine().trim();
                //double verification just in case
                if(pattern.matcher(date).matches()) {
                    departureDate = inputFormat.parse(date);
                } else {
                    throw new InvalidInputGeneralException("This is not a date");
                }
                break;
            } catch (ParseException ex) {
                System.out.println("Date is not valid! Please try again!");
            }
        }
        System.out.print("Enter number of passengers desired :  ");
        int passengers = sc.nextInt();
        
        int flightPreference;
        while (true) {
            System.out.println("State you preference for your Flight : ");
            System.out.println("0 : No Preference");
            System.out.println("1 : Direct Flight");
            System.out.println("2 : Connecting Flight");
            System.out.print("Choose your option : ");
            flightPreference = sc.nextInt();
            if (flightPreference != 1 && flightPreference != 2 && flightPreference != 0) {
                System.out.println("Invalid option! Please try again!");
            } else {
                break;
            }
        }
        
        CabinClassTypeEnum cabinPreference;
        OUTER:
        while (true) {
            System.out.print("Now please enter your cabin class preference (0.No Preference 1. First Class 2. Business Class 3. Premium Economy Class 4.Economy Class)> ");
            System.out.println("0.No Preference");
            System.out.println("1. First Class");
            System.out.println("2. Business Class");
            System.out.println("3.Premium Economy Class");
            System.out.println("4.Economy Class");
            System.out.println("Choose your cabin!");
            int cabinPref = sc.nextInt();
            sc.nextLine();
            switch (cabinPref) {
                case 1:
                    cabinPreference = CabinClassTypeEnum.F;
                    break OUTER;
                case 2:
                    cabinPreference = CabinClassTypeEnum.J;
                    break OUTER;
                case 3:
                    cabinPreference = CabinClassTypeEnum.W;
                    break OUTER;
                case 4:
                    cabinPreference = CabinClassTypeEnum.Y;
                    break OUTER;
                case 0:
                    cabinPreference = null;
                    break OUTER;
                default:
                    System.out.println("There is no such cabin type! Please try again.");
                    break;
            }
        
        }
        
        
        if (flightPreference  == 0) {
            boolean noDirectFlights = false;
            boolean noIndirectFlights = false;
            
            try {
                List<FlightScheduleEntity> outboundFlightSchedules = getFlightSchedules(departure, destination, departureDate, cabinPreference);
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(departureDate);
                
                List<FlightScheduleEntity> flightSchedulesPlusOne = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
                List<FlightScheduleEntity> flightSchedulesPlusTwo = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
                List<FlightScheduleEntity> flightSchedulesPlusThree = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);

                calendar.setTime(departureDate);
                List<FlightScheduleEntity> flightSchedulesMinusOne = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
                List<FlightScheduleEntity> flightSchedulesMinusTwo = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
                List<FlightScheduleEntity> flightSchedulesMinusThree = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
                
                displayFlightSchedules("Direct Outbound Flights", outboundFlightSchedules, flightSchedulesMinusOne, flightSchedulesMinusTwo, flightSchedulesMinusThree, 
                        flightSchedulesPlusOne, flightSchedulesPlusTwo, flightSchedulesPlusThree, cabinPreference, passengers);
            
            } catch (FlightNotFoundException ex) {
                System.out.println("No direct flights available for your chosen route.");
                noDirectFlights = true;    
            }
            
            try {
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> outboundFlightSchedules = getIndirectUnManagedFlightSchedules(departure, destination, departureDate, cabinPreference);
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(departureDate);
                
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesPlusOne = getIndirectFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesPlusTwo = getIndirectFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesPlusThree = getIndirectFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);

                calendar.setTime(departureDate);
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesMinusOne = getIndirectFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesMinusTwo = getIndirectFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesMinusThree = getIndirectFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
                
                System.out.println("                      ============= Available Outbound Flights ============= ");

                System.out.println("                             ============ On Desired Date =========== ");
                printFlightScheduleWithConnecting(outboundFlightSchedules, cabinPreference, passengers);
                
                System.out.println("\n                  ============ Departing 1 day before Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesMinusOne, cabinPreference, passengers);
            
                System.out.println("\n                  ============ Departing 2 days before Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesMinusTwo, cabinPreference, passengers);
                                  
                System.out.println("\n                  ============ Departing 3 days before Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesMinusThree, cabinPreference, passengers);
              
                System.out.println("\n                  ============ Departing 1 day after Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesPlusOne, cabinPreference, passengers);
                         
                System.out.println("\n                  ============ Departing 2 days after Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesPlusTwo, cabinPreference, passengers);
                               
                System.out.println("\n                  ============ Departing 3 days after Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesPlusThree, cabinPreference, passengers);
                
            } catch (FlightNotFoundException ex) {
                System.out.println("No indirect flights for your specified route\n");
                noIndirectFlights = true;
            }     
            if (noDirectFlights && noIndirectFlights) {
                return;
            } 
        }
        
        if (flightPreference  == 1) {
            try {
                List<FlightScheduleEntity> outboundFlightSchedules = getFlightSchedules(departure, destination, departureDate, cabinPreference);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(departureDate);

                List<FlightScheduleEntity> flightSchedulesPlusOne = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
                List<FlightScheduleEntity> flightSchedulesPlusTwo = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
                List<FlightScheduleEntity> flightSchedulesPlusThree = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);

                calendar.setTime(departureDate);
                List<FlightScheduleEntity> flightSchedulesMinusOne = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
                List<FlightScheduleEntity> flightSchedulesMinusTwo = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
                List<FlightScheduleEntity> flightSchedulesMinusThree = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);

                displayFlightSchedules("Direct Outbound Flights", outboundFlightSchedules, flightSchedulesMinusOne, flightSchedulesMinusTwo, flightSchedulesMinusThree, flightSchedulesPlusOne, flightSchedulesPlusTwo, flightSchedulesPlusThree, cabinPreference, passengers);

            } catch (FlightNotFoundException ex) {
                System.out.println("No direct flights available for your chosen route.");
                return;
            }
        }
        
        if (flightPreference == 2) {
            try {
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> outboundFlightSchedules = getIndirectUnManagedFlightSchedules(departure, destination, departureDate, cabinPreference);
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(departureDate);
                
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesPlusOne = getIndirectFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesPlusTwo = getIndirectFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesPlusThree = getIndirectFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);

                calendar.setTime(departureDate);
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesMinusOne = getIndirectFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesMinusTwo = getIndirectFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
                List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulesMinusThree = getIndirectFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
                
                System.out.println("                      ============= Available Outbound Flights ============= ");

                System.out.println("                             ============ On Desired Date =========== ");
                printFlightScheduleWithConnecting(outboundFlightSchedules, cabinPreference, passengers);
                
                System.out.println("\n                  ============ Departing 1 day before Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesMinusOne, cabinPreference, passengers);
            
                System.out.println("\n                  ============ Departing 2 days before Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesMinusTwo, cabinPreference, passengers);
                                  
                System.out.println("\n                  ============ Departing 3 days before Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesMinusThree, cabinPreference, passengers);
              
                System.out.println("\n                  ============ Departing 1 day after Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesPlusOne, cabinPreference, passengers);
                         
                System.out.println("\n                  ============ Departing 2 days after Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesPlusTwo, cabinPreference, passengers);
                               
                System.out.println("\n                  ============ Departing 3 days after Desired Date ============ ");
                printFlightScheduleWithConnecting(flightSchedulesPlusThree, cabinPreference, passengers);
            
            } catch (FlightNotFoundException ex) {
                System.out.println("No indirect flights for your specified route\n");
                return;
            }  
        }
        System.out.println("\n");
        
         if (typeOfFlight == 2 && flightPreference == 0) {
            Date returnDate;
            while (true) {
                try { 
                    System.out.print("Enter return date (dd/mm/yyyy): ");
                    String date2 = sc.nextLine().trim();
                    returnDate = inputFormat.parse(date2);
                    break;
                } catch (ParseException ex) {
                    System.out.println("Error! Invalid date\n Please try again!");
                }      
            }
            boolean noDirectFlights = false;
            boolean noIndirectFlights = false;
        
        try {
            List<FlightScheduleEntity> dateActualFlightScheduleInBound = getFlightSchedules(destination, departure, returnDate, cabinPreference);

            Calendar calendar = Calendar.getInstance();
            
            calendar.setTime(returnDate);
            List<FlightScheduleEntity>  flightSchedulesPlusOne = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
            List<FlightScheduleEntity>  flightSchedulesPlusTwo = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
            List<FlightScheduleEntity>  flightSchedulesPlusThree = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);

            calendar.setTime(departureDate);
            List<FlightScheduleEntity>  flightSchedulesMinusOne = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
            List<FlightScheduleEntity>  flightSchedulesMinusTwo = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
            List<FlightScheduleEntity>  flightSchedulesMinusThree = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
            
            
            } catch (FlightNotFoundException ex) {
                System.out.print("No indirect flights for your specified route\n");
                noDirectFlights = true;
            }
            if (noDirectFlights && noIndirectFlights) {
                return;
            }    
        }
        
         
        if (typeOfFlight == 2 && flightPreference == 1) {
            Date returnDate;
            while (true) {
                try { 
                    System.out.print("Enter return date (dd/mm/yyyy): ");
                    String date2 = sc.nextLine().trim();
                    returnDate = inputFormat.parse(date2);
                    break;
                } catch (ParseException ex) {
                    System.out.println("Error! Invalid date\n Please try again!");
                }      
            }
            boolean noDirectFlights = false;
            boolean noIndirectFlights = false;
        
        try {
            List<FlightScheduleEntity> dateActualFlightScheduleInBound = getFlightSchedules(destination, departure, returnDate, cabinPreference);

            Calendar calendar = Calendar.getInstance();
            
            calendar.setTime(returnDate);
            List<FlightScheduleEntity> flightSchedulesPlusOne = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
            List<FlightScheduleEntity> flightSchedulesPlusTwo = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
            List<FlightScheduleEntity> flightSchedulesPlusThree = getFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);

            calendar.setTime(departureDate);
            List<FlightScheduleEntity> flightSchedulesMinusOne = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
            List<FlightScheduleEntity> flightSchedulesMinusTwo = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
            List<FlightScheduleEntity> flightSchedulesMinusThree = getFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
            
            
            } catch (FlightNotFoundException ex) {
                System.out.print("Sorry there are no return flights for this flight route within this period");
                return;
            }  
        }
        
        if (typeOfFlight == 2 && flightPreference == 2) {
            Date returnDate;
            while (true) {
                try { 
                    System.out.print("Enter return date (dd/mm/yyyy): ");
                    String date2 = sc.nextLine().trim();
                    returnDate = inputFormat.parse(date2);
                    break;
                } catch (ParseException ex) {
                    System.out.println("Error! Invalid date\n Please try again!");
                }      
            }
            boolean noDirectFlights = false;
            boolean noIndirectFlights = false;
        
        try {
            List<Pair<FlightScheduleEntity, FlightScheduleEntity>> dateActualFlightScheduleInBound = getIndirectUnManagedFlightSchedules(destination, departure, returnDate, cabinPreference);

            Calendar calendar = Calendar.getInstance();
            
            calendar.setTime(returnDate);
            List<Pair<FlightScheduleEntity, FlightScheduleEntity>>  flightSchedulesPlusOne = getIndirectFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
            List<Pair<FlightScheduleEntity, FlightScheduleEntity>>  flightSchedulesPlusTwo = getIndirectFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);
            List<Pair<FlightScheduleEntity, FlightScheduleEntity>>  flightSchedulesPlusThree = getIndirectFlightSchedulesForAdjustedDate(calendar, 1, departure, destination, cabinPreference);

            calendar.setTime(departureDate);
            List<Pair<FlightScheduleEntity, FlightScheduleEntity>>  flightSchedulesMinusOne = getIndirectFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
            List<Pair<FlightScheduleEntity, FlightScheduleEntity>>  flightSchedulesMinusTwo = getIndirectFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
            List<Pair<FlightScheduleEntity, FlightScheduleEntity>>  flightSchedulesMinusThree = getIndirectFlightSchedulesForAdjustedDate(calendar, -1, departure, destination, cabinPreference);
            
            
            } catch (FlightNotFoundException ex) {
                System.out.print("Sorry there are no return flights for this flight route within this period");
                return;
            }  
        }
        
        System.out.println("\n");
        
        System.out.print("Would you like to reserve a flight? (Y/N): ");
        String ans = sc.nextLine().trim();

        if (ans.equalsIgnoreCase("n")) {
            return;
        }

        Long outbound1, outbound2, inbound1, inbound2;
        if (typeOfFlight == 1 && flightPreference == 1) {
            outbound2 = null;
            inbound2 = null;
            inbound1 = null;
            System.out.print("Enter flight you would like to reserve (Flight Id): ");
            outbound1 = sc.nextLong();
            sc.nextLine();
        } else if (typeOfFlight == 1 && flightPreference == 2) {
            inbound1 = null;
            inbound2 = null;
            System.out.print("Enter the first outbound flight you would like to reserve (Flight Id): ");
            outbound1 = sc.nextLong();
            System.out.print("Enter the connecting outbound flight you would like to reserve (Flight Id): ");
            outbound2 = sc.nextLong();   
            sc.nextLine();
        } else if (typeOfFlight == 2 && flightPreference == 1) {
            outbound2 = null;
            inbound2 = null;
            System.out.print("Enter the outbound flight you would like to reserve (Flight Id): ");
            outbound1 = sc.nextLong();
            System.out.print("Enter the inbound flight you would like to reserve (Flight Id): ");
            inbound1 = sc.nextLong();
            sc.nextLine();    
        } else if (typeOfFlight == 2 && flightPreference == 2) {
            System.out.print("Enter the first outbound flight you would like to reserve (Flight Id): ");
            outbound1 = sc.nextLong();
            System.out.print("Enter the connecting outbound flight you would like to reserve (Flight Id): ");
            outbound2 = sc.nextLong();
            System.out.print("Enter the first inbound flight you would like to reserve (Flight Id): ");
            inbound1 = sc.nextLong();
            System.out.print("Enter the connecting inbound flight you would like to reserve (Flight Id): ");
            inbound2 = sc.nextLong();
        } else if (flightPreference == 0) {
            System.out.print("Select type of flight you would like to reserve ('1' for Direct Flight, '2' for Connecting Flight): ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (typeOfFlight == 1 && choice == 1) {
                outbound2 = null;
                inbound2 = null;
                inbound1 = null;
                System.out.print("Enter flight you would like to reserve (Flight Id): ");
                outbound1 = sc.nextLong();
                sc.nextLine();
            } else if (typeOfFlight == 1 && choice == 2) {
                inbound1 = null;
                inbound2 = null;
                System.out.print("Enter the first outbound flight you would like to reserve (Flight Id): ");
                outbound1 = sc.nextLong();
                System.out.print("Enter the connecting outbound flight you would like to reserve (Flight Id): ");
                outbound2 = sc.nextLong(); 
            } else if (typeOfFlight == 2 && choice == 1) {
               outbound2 = null;
                inbound2 = null;
                System.out.print("Enter the outbound flight you would like to reserve (Flight Id): ");
                outbound1 = sc.nextLong();
                System.out.print("Enter the inbound flight you would like to reserve (Flight Id): ");
                inbound1 = sc.nextLong();
                sc.nextLine();   
            } else if (typeOfFlight == 2 && choice == 2) {
                System.out.print("Enter the first outbound flight you would like to reserve (Flight Id): ");
                 outbound1 = sc.nextLong();
                 System.out.print("Enter the connecting outbound flight you would like to reserve (Flight Id): ");
                 outbound2 = sc.nextLong();
                 System.out.print("Enter the first inbound flight you would like to reserve (Flight Id): ");
                 inbound1 = sc.nextLong();
                 System.out.print("Enter the connecting inbound flight you would like to reserve (Flight Id): ");
                 inbound2 = sc.nextLong();       
            } else {
                System.out.println("Error! Invalid option\nPlease try again!\n");
                return;
            }
        } else {
            return;
        }
        reserveFlight(outbound1, outbound2, inbound1, inbound2, cabinPreference, passengers);
    }
    
    private void reserveFlight(Long outbound1, Long outbound2, Long inbound1, Long inbound2, CabinClassTypeEnum cabinClassType, int noOfPassengers) throws ReservationExistException, UnknownPersistenceException, FlightScheduleNotFoundException, SeatSlotNotFoundException, SeatAlreadyBookedException, ItineraryDoNotExistException, InputDataValidationException, SeatInventoryNotFoundException, UserNotFoundException, ItineraryExistException {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("=== Reserve Flight ===\n");
            
            FlightScheduleEntity outbound1FlightSchedule;
            List<String> outbound1SeatSelection;   
            FareEntity outbound1Fare;
            SeatInventoryEntity outbound1Seats;
            ReservationEntity outbound1Reservation;
            
            FlightScheduleEntity outbound2FlightSchedule;
            List<String> outbound2SeatSelection;
            FareEntity outbound2Fare;
            SeatInventoryEntity outbound2Seats;
            ReservationEntity outbound2Reservation;
            
            FlightScheduleEntity inbound1FlightSchedule;
            List<String> inbound1SeatSelection;
            FareEntity inbound1Fare; 
            SeatInventoryEntity inbound1Seats;
            ReservationEntity inbound1Reservation;
            
            FlightScheduleEntity inbound2FlightSchedule;
            List<String> inbound2SeatSelection;
            FareEntity inbound2Fare;
            SeatInventoryEntity inbound2Seats;  
            ReservationEntity inbound2Reservation;
            
            ItineraryEntity itinerary = new ItineraryEntity();
            
            BigDecimal pricePerPax;

            if (outbound2 == null && inbound1 == null && inbound2 == null) {
                outbound1FlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(outbound1);
                System.out.println("Seat Selection for outbound flight " + outbound1FlightSchedule.getFlightSchedulePlan().getFlightNum());
                if (cabinClassType == null) {
                    outbound1Seats = getDesiredSeatInventory(outbound1FlightSchedule);  
                } else {
                    outbound1Seats = flightScheduleSessionBean.getCorrectSeatInventory(outbound1FlightSchedule, cabinClassType);
                }
                outbound1Fare = flightScheduleSessionBean.retrieveLowestFare(outbound1FlightSchedule, outbound1Seats.getCabin().getCabinClassType());
                outbound1SeatSelection = getSeatBookings(outbound1Seats, noOfPassengers);
                            
                outbound1Reservation = new ReservationEntity(outbound1Fare.getFareBasisCode(), outbound1Fare.getFareAmount(), outbound1Seats.getCabin().getCabinClassType());
                
                pricePerPax = outbound1Fare.getFareAmount();
                System.out.println("Price per person : $" + pricePerPax.toString() + "\nTotal Amount : $" + pricePerPax.multiply(new BigDecimal(noOfPassengers)));
                
                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                itinerary.setCreditCardNumber(creditCardNum);
                itinerary.setCvv(cvv); 
             
                /*Set<ConstraintViolation<ItineraryEntity>> constraintViolations = validator.validate(itinerary);
                if (constraintViolations.isEmpty()) {
                    itinerary = itinerarySessionBean.createNewItinerary(itinerary, currentCustomer.getUserID());
                } else {
                    showInputDataValidationErrorsForItineraryEntity(constraintViolations);
                    return;
                }*/
                
                List<PassengerEntity> passengers = obtainPassengerDetails(noOfPassengers);
                
                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(outbound1SeatSelection.get(i));
                }
                reservationSessionBean.createNewReservation(outbound1Reservation, passengers, outbound1FlightSchedule.getFlightScheduleId(), itinerary.getIternaryId());                  
                
                System.out.println("Reservation Itinerary (Booking ID: " + itinerary.getIternaryId()+ ") created successfully for User " + currentCustomer.getUserId() + "!\n");
            } else if (outbound2 == null && inbound2 == null) {
                outbound1FlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(outbound1);
                System.out.println("Seat Selection for outbound flight " + outbound1FlightSchedule.getFlightSchedulePlan().getFlightNum());
                if (cabinClassType == null) {
                    outbound1Seats = getDesiredSeatInventory(outbound1FlightSchedule);  
                } else {
                    outbound1Seats = flightScheduleSessionBean.getCorrectSeatInventory(outbound1FlightSchedule, cabinClassType);
                }
                outbound1Fare = flightScheduleSessionBean.retrieveLowestFare(outbound1FlightSchedule, outbound1Seats.getCabin().getCabinClassType());
                outbound1SeatSelection = getSeatBookings(outbound1Seats, noOfPassengers);
                outbound1Reservation = new ReservationEntity(outbound1Fare.getFareBasisCode(), outbound1Fare.getFareAmount(), outbound1Seats.getCabin().getCabinClassType());
                
                inbound1FlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(inbound1);
                System.out.println("Seat Selection for inbound flight " + inbound1FlightSchedule.getFlightSchedulePlan().getFlightNum());
                if (cabinClassType == null) {
                    inbound1Seats = getDesiredSeatInventory(inbound1FlightSchedule);   
                } else {
                    inbound1Seats = flightScheduleSessionBean.getCorrectSeatInventory(inbound1FlightSchedule, cabinClassType);
                }
                inbound1Fare = flightScheduleSessionBean.retrieveLowestFare(inbound1FlightSchedule, inbound1Seats.getCabin().getCabinClassType());
                inbound1SeatSelection = getSeatBookings(inbound1Seats, noOfPassengers); 
                inbound1Reservation = new ReservationEntity(inbound1Fare.getFareBasisCode(), inbound1Fare.getFareAmount(), inbound1Seats.getCabin().getCabinClassType());
                
                pricePerPax = outbound1Fare.getFareAmount().add(inbound1Fare.getFareAmount());
                List<PassengerEntity> passengers = obtainPassengerDetails(noOfPassengers);
                
                System.out.println("Price per person : $" + pricePerPax.toString() + "\nTotal Amount : $" + pricePerPax.multiply(new BigDecimal(noOfPassengers)));
                
                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                itinerary.setCreditCardNumber(creditCardNum);
                itinerary.setCvv(cvv); 
             
                Set<ConstraintViolation<ItineraryEntity>> constraintViolations = validator.validate(itinerary);
                if (constraintViolations.isEmpty()) {
                    itinerary = itinerarySessionBean.createNewItinerary(itinerary, currentCustomer.getUserId());
                } else {
                    System.out.println("Constraint violations!");
                    return;
                }
                
                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(outbound1SeatSelection.get(i));
                }
                reservationSessionBean.createNewReservation(outbound1Reservation, passengers, outbound1FlightSchedule.getFlightScheduleId(), itinerary.getIternaryId());                  
                
                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(inbound1SeatSelection.get(i));
                }  
                reservationSessionBean.createNewReservation(inbound1Reservation, passengers, inbound1FlightSchedule.getFlightScheduleId(), itinerary.getIternaryId());  
                
                System.out.println("Reservation Itinerary (Booking ID: " + itinerary.getIternaryId() + ") created successfully for User " + currentCustomer.getUserId() + "!\n");
            } else if (inbound1 == null && inbound2 == null) {
                outbound1FlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(outbound1);
                System.out.println("Seat Selection for outbound flight " + outbound1FlightSchedule.getFlightSchedulePlan().getFlightNum());
                if (cabinClassType == null) {
                    outbound1Seats = getDesiredSeatInventory(outbound1FlightSchedule);  
                } else {
                    outbound1Seats = flightScheduleSessionBean.getCorrectSeatInventory(outbound1FlightSchedule, cabinClassType);
                }
                outbound1Fare = flightScheduleSessionBean.retrieveLowestFare(outbound1FlightSchedule, outbound1Seats.getCabin().getCabinClassType());
                outbound1SeatSelection = getSeatBookings(outbound1Seats, noOfPassengers);
                outbound1Reservation = new ReservationEntity(outbound1Fare.getFareBasisCode(), outbound1Fare.getFareAmount(), outbound1Seats.getCabin().getCabinClassType());
                
                outbound2FlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(outbound2);
                System.out.println("Seat Selection for outbound connecting flight " + outbound2FlightSchedule.getFlightSchedulePlan().getFlightNum());
                if (cabinClassType == null) {
                    outbound2Seats = getDesiredSeatInventory(outbound2FlightSchedule);  
                } else {
                   outbound2Seats = flightScheduleSessionBean.getCorrectSeatInventory(outbound2FlightSchedule, cabinClassType);
                }    
                outbound2Fare = flightScheduleSessionBean.retrieveLowestFare(outbound2FlightSchedule, outbound2Seats.getCabin().getCabinClassType());
                outbound2SeatSelection = getSeatBookings(outbound2Seats, noOfPassengers);
                outbound2Reservation = new ReservationEntity(outbound2Fare.getFareBasisCode(), outbound2Fare.getFareAmount(), outbound2Seats.getCabin().getCabinClassType());
                
                pricePerPax = outbound1Fare.getFareAmount().add(outbound2Fare.getFareAmount());
                List<PassengerEntity> passengers = obtainPassengerDetails(noOfPassengers);
                
                System.out.println("Price per person : $" + pricePerPax.toString() + "\nTotal Amount : $" + pricePerPax.multiply(new BigDecimal(noOfPassengers)));
                
                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                itinerary.setCreditCardNumber(creditCardNum);
                itinerary.setCvv(cvv); 
             
                Set<ConstraintViolation<ItineraryEntity>> constraintViolations = validator.validate(itinerary);
                if (constraintViolations.isEmpty()) {
                    itinerary = itinerarySessionBean.createNewItinerary(itinerary, currentCustomer.getUserId());
                } else {
                    System.out.println("Constraint violations!!");
                    return;
                }
                
                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(outbound1SeatSelection.get(i));
                }
                reservationSessionBean.createNewReservation(outbound1Reservation, passengers, outbound1FlightSchedule.getFlightScheduleId(), itinerary.getIternaryId());
            
                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(outbound2SeatSelection.get(i));
                }
                reservationSessionBean.createNewReservation(outbound2Reservation, passengers, outbound2FlightSchedule.getFlightScheduleId(), itinerary.getIternaryId());
                
                System.out.println("Reservation Itinerary (Booking ID: " + itinerary.getIternaryId()+ ") created successfully for User " + currentCustomer.getUserId() + "!\n");
            } else {
                outbound1FlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(outbound1);
                System.out.println("Seat Selection for outbound flight " + outbound1FlightSchedule.getFlightSchedulePlan().getFlightNum());
                if (cabinClassType == null) {
                    outbound1Seats = getDesiredSeatInventory(outbound1FlightSchedule);  
                } else {
                    outbound1Seats = flightScheduleSessionBean.getCorrectSeatInventory(outbound1FlightSchedule, cabinClassType);
                }
                outbound1Fare = flightScheduleSessionBean.retrieveLowestFare(outbound1FlightSchedule, outbound1Seats.getCabin().getCabinClassType());
                outbound1SeatSelection = getSeatBookings(outbound1Seats, noOfPassengers);
                outbound1Reservation = new ReservationEntity(outbound1Fare.getFareBasisCode(), outbound1Fare.getFareAmount(), outbound1Seats.getCabin().getCabinClassType());
                
                outbound2FlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(outbound2);
                System.out.println("Seat Selection for outbound connecting flight " + outbound2FlightSchedule.getFlightSchedulePlan().getFlightNum());
                if (cabinClassType == null) {
                    outbound2Seats = getDesiredSeatInventory(outbound2FlightSchedule);  
                } else {
                   outbound2Seats = flightScheduleSessionBean.getCorrectSeatInventory(outbound2FlightSchedule, cabinClassType);
                }
                outbound2Fare = flightScheduleSessionBean.retrieveLowestFare(outbound2FlightSchedule, outbound2Seats.getCabin().getCabinClassType());
                outbound2SeatSelection = getSeatBookings(outbound2Seats, noOfPassengers);
                outbound2Reservation = new ReservationEntity(outbound2Fare.getFareBasisCode(), outbound2Fare.getFareAmount(), outbound2Seats.getCabin().getCabinClassType());
                
                inbound1FlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(inbound1);
                System.out.println("Seat Selection for inbound flight " + inbound1FlightSchedule.getFlightSchedulePlan().getFlightNum());
                if (cabinClassType == null) {
                    inbound1Seats = getDesiredSeatInventory(inbound1FlightSchedule);   
                } else {
                    inbound1Seats = flightScheduleSessionBean.getCorrectSeatInventory(inbound1FlightSchedule, cabinClassType);
                }
                inbound1Fare = flightScheduleSessionBean.retrieveLowestFare(inbound1FlightSchedule, inbound1Seats.getCabin().getCabinClassType());
                inbound1SeatSelection = getSeatBookings(inbound1Seats, noOfPassengers);   
                inbound1Reservation = new ReservationEntity(inbound1Fare.getFareBasisCode(), inbound1Fare.getFareAmount(), inbound1Seats.getCabin().getCabinClassType());
                
                inbound2FlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(inbound2);
                System.out.println("Seat Selection for inbound connecting flight " + inbound2FlightSchedule.getFlightSchedulePlan().getFlightNum());
                if (cabinClassType == null) {
                    inbound2Seats = getDesiredSeatInventory(inbound2FlightSchedule);
                } else {
                    inbound2Seats = flightScheduleSessionBean.getCorrectSeatInventory(inbound2FlightSchedule, cabinClassType);
                }
                inbound2Fare = flightScheduleSessionBean.retrieveLowestFare(inbound2FlightSchedule, inbound2Seats.getCabin().getCabinClassType());
                inbound2SeatSelection = getSeatBookings(inbound2Seats, noOfPassengers);
                inbound2Reservation = new ReservationEntity(inbound2Fare.getFareBasisCode(), inbound2Fare.getFareAmount(), inbound2Seats.getCabin().getCabinClassType());
                
                pricePerPax = outbound1Fare.getFareAmount().add(outbound2Fare.getFareAmount()).add(inbound1Fare.getFareAmount()).add(inbound2Fare.getFareAmount());
                List<PassengerEntity> passengers = obtainPassengerDetails(noOfPassengers);
                
                System.out.println("Price per person : $" + pricePerPax.toString() + "\nTotal Amount : $" + pricePerPax.multiply(new BigDecimal(noOfPassengers)));
                
                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                itinerary.setCreditCardNumber(creditCardNum);
                itinerary.setCvv(cvv); 
             
                Set<ConstraintViolation<ItineraryEntity>> constraintViolations = validator.validate(itinerary);
                if (constraintViolations.isEmpty()) {
                    itinerary = itinerarySessionBean.createNewItinerary(itinerary, currentCustomer.getUserId());
                } else {
                    System.out.println("INPUT DATA VALIDATION ERRORS! CONSTRAINT VIOLATIONS!!");
                    return;
                }
                
                 for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(outbound1SeatSelection.get(i));
                }
                reservationSessionBean.createNewReservation(outbound1Reservation, passengers, outbound1FlightSchedule.getFlightScheduleId(), itinerary.getIternaryId());
            
                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(outbound2SeatSelection.get(i));
                }
                reservationSessionBean.createNewReservation(outbound2Reservation, passengers, outbound2FlightSchedule.getFlightScheduleId(), itinerary.getIternaryId());
            
                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(inbound1SeatSelection.get(i));
                }  
                reservationSessionBean.createNewReservation(inbound1Reservation, passengers, inbound1FlightSchedule.getFlightScheduleId(), itinerary.getIternaryId());
                
                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(inbound2SeatSelection.get(i));
                }  
                reservationSessionBean.createNewReservation(inbound2Reservation, passengers, inbound2FlightSchedule.getFlightScheduleId(), itinerary.getIternaryId());
                
                System.out.println("Reservation Itinerary (Booking ID: " + itinerary.getIternaryId()+ ") created successfully for User " + currentCustomer.getUserId() + "!\n");
            }           
        } catch (CabinClassNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
        }
    }

        
        
        
        /*if (flightPreference == 0) {
            boolean exit = false;
            boolean exit2 = false;
            try {
                Map<String, List<FlightScheduleEntity>> flightSchedulesMap = new HashMap<>();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(departureDate);
                
                for (int i = 0; i <= 3; i++) {
                    if (i != 0) {
                        calendar.add(Calendar.DATE, 1);
                    }
                    String key = "OutBoundFlightScheduleDateAdd" + i;
                    flightSchedulesMap.put(key, flightScheduleSessionBean.getFlightSchedules(departure,
                            destination, calendar.getTime(), cabinPreference));
                }

                calendar.setTime(departureDate); // Reset to the original departure date

                for (int i = -1; i >= -3; i--) {
                    calendar.add(Calendar.DATE, -1);
                    String key = "OutBoundFlightScheduleDateMinus" + Math.abs(i);
                    flightSchedulesMap.put(key, flightScheduleSessionBean.getFlightSchedules(departure,
                            destination, calendar.getTime(), cabinPreference));
                }

                System.out.println(" **** Available Direct Outbound Flights **** ");

                String[] descriptions = {
                    "On Desired Date",
                    "Departing 1 day before Desired Date",
                    "Departing 2 days before Desired Date",
                    "Departing 3 days before Desired Date",
                    "Departing 1 day after Desired Date",
                    "Departing 2 days after Desired Date",
                    "Departing 3 days after Desired Date"
                };

                for (String description : descriptions) {
                    String key = "OutBoundFlightScheduleDate" + description.replaceAll("[^A-Za-z0-]+", "");
                    System.out.printf("\n                  ============ %s ============\n", description);
                    printFlightScheduleSingle(flightSchedulesMap.get(key), cabinPreference, passengers);
                }
            } catch (FlightNotFoundException ex) {
                System.out.print("There is no flights with the given conditions");
                exit = true;
            } catch (FlightScheduleNotFoundException | CabinClassNotFoundException ex) {
                ex.printStackTrace();
            } 
        }*/
        

    
    private List<PassengerEntity> obtainPassengerDetails(int noOfPassengers) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Obtain Passenger Details ***\n");
        List<PassengerEntity> passengers = new ArrayList<>();
        for (int i = 1; i <= noOfPassengers; i++) {
            System.out.print("Enter passenger " + i + " first name :  ");
            String firstName = sc.nextLine().trim();
            System.out.print("Enter passenger " + i + " last name : ");
            String lastName = sc.nextLine().trim();
            System.out.print("Enter passenger " + i + " passport number : ");
            String passport = sc.nextLine().trim();
            passengers.add(new PassengerEntity(firstName, lastName, passport, null));
        }
        return passengers;
    } 
    
//    private List<String> getSeatBookings(SeatInventoryEntity seatInventory, int noOfPassengers) throws SeatSlotNotFoundException {
//        Scanner sc = new Scanner(System.in);
//        int totalAvailSeats = seatInventory.getAvailable();
//        int totalReservedSeats = seatInventory.getReserved();
//        int totalBalanceSeats = seatInventory.getBalance();
//
//        char[][] seats = seatInventory.getSeats();
//        String cabinClassConfig = seatInventory.getCabin().getSeatingConfigPerColumn();
//
//        //Display Seats
//        String type = "";
//        if (null !=  seatInventory.getCabin().getCabinClassType())
//            switch (seatInventory.getCabin().getCabinClassType()) {
//                case F:
//                    type = "First Class";
//                    break;
//                case J:
//                    type = "Business Class";
//                    break;
//                case W:
//                    type = "Premium Economy Class";
//                    break;
//                case Y:
//                    type = "Economy Class";
//                    break;
//                default:
//                    break;
//            }
//
//        System.out.println(" -- " + type + " -- ");
//        System.out.print("Row  ");
//        int count = 0;
//        int no = 0;
//        for (int i = 0; i < cabinClassConfig.length(); i++) {
//            if (Character.isDigit(cabinClassConfig.charAt(i))) {
//                no += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
//                while (count < no) {
//                    System.out.print((char)('A' + count) + "  ");
//                    count++;
//                }
//            } else {
//                System.out.print("   ");
//            }
//        }
//        System.out.println();
//
//        for (int j = 0; j < seats.length; j++) {
//            System.out.printf("%-5s", String.valueOf(j+1));
//            int count2 = 0;
//            int no2 = 0;
//            for (int i = 0; i < cabinClassConfig.length(); i++) {
//                if (Character.isDigit(cabinClassConfig.charAt(i))) {
//                    no2 += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
//                    while (count2 < no2) {
//                        System.out.print(seats[j][count2] + "  ");
//                        count2++;
//                    }
//                } else {
//                    System.out.print("   ");
//                }
//            }
//            System.out.println();
//        }
//        System.out.println(" --- Total --- ");
//        System.out.println("Number of available seats: " + totalAvailSeats);
//        System.out.println("Number of reserved seats: " + totalReservedSeats);
//        System.out.println("Number of balance seats: " + totalBalanceSeats);
//
//        List<String> seatSelection = new ArrayList<>();
//        while (true) {        
//            for (int i = 0; i < noOfPassengers; i++) {                   
//                String seatNumber;
//                while (true) {
//                    System.out.print("\nEnter seat to reserve for Passenger " +  (i + 1) + "(Eg. A5)> ");
//                    seatNumber = sc.nextLine().trim();               
//                    boolean booked = seatsInventorySessionBean.checkIfBooked(seatInventory, seatNumber);
//                    if (booked) {
//                        System.out.println("Seat already taken!\nPlease choose another seat");
//                    } else {
//                        break;
//                    }
//                }    
//                seatSelection.add(seatNumber);
//            }
//            boolean distinct = seatSelection.stream().distinct().count() ==  seatSelection.size();
//            if (distinct) {
//                return seatSelection;
//            } else {
//                System.out.println("Duplicate seats detected!\nPlease try again");
//            }
//        }
//    }
    private List<String> getSeatBookings(SeatInventoryEntity seatInventory, int noOfPassengers) throws SeatSlotNotFoundException {
        Scanner sc = new Scanner(System.in);
        int totalAvailSeats = seatInventory.getAvailableSeats();
        int totalReservedSeats = seatInventory.getReservedSeats();
        int totalBalanceSeats = seatInventory.getBalanceSeats();

        char[][] seats = seatInventory.getSeats();
        String cabinClassConfig = seatInventory.getCabin().getSeatingConfigPerColumn();

        //Display Seats
        String type = "";
        if (null != seatInventory.getCabin().getCabinClassType()) {
            switch (seatInventory.getCabin().getCabinClassType()) {
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
                default:
                    break;
            }
        }

        System.out.println(" -- " + type + " -- ");
        System.out.print("Row  ");
        int count = 0;
        int no = 0;
        for (int i = 0; i < cabinClassConfig.length(); i++) {
            if (Character.isDigit(cabinClassConfig.charAt(i))) {
                no += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
                while (count < no) {
                    System.out.print((char) ('A' + count) + "  ");
                    count++;
                }
            } else {
                System.out.print("   ");
            }
        }
        System.out.println();

        for (int j = 0; j < seats.length; j++) {
            System.out.printf("%-5s", String.valueOf(j + 1));
            int count2 = 0;
            int no2 = 0;
            for (int i = 0; i < cabinClassConfig.length(); i++) {
                if (Character.isDigit(cabinClassConfig.charAt(i))) {
                    no2 += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
                    while (count2 < no2) {
                        System.out.print(seats[j][count2] + "  ");
                        count2++;
                    }
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.println(" --- Total --- ");
        System.out.println("Number of available seats: " + totalAvailSeats);
        System.out.println("Number of reserved seats: " + totalReservedSeats);
        System.out.println("Number of balance seats: " + totalBalanceSeats);

        List<String> seatSelection = new ArrayList<>();
        while (true) {
            for (int i = 0; i < noOfPassengers; i++) {
                String seatNumber;
                while (true) {
                    System.out.print("\nEnter seat to reserve for Passenger " + (i + 1) + "(Eg. A5)> ");
                    seatNumber = sc.nextLine().trim();
                    boolean booked = seatsInventorySessionBean.checkIfBooked(seatInventory, seatNumber);
                    if (booked) {
                        System.out.println("Seat already taken!\nPlease choose another seat");
                    } else {
                        break;
                    }
                }
                seatSelection.add(seatNumber);
            }
            boolean distinct = seatSelection.stream().distinct().count() == seatSelection.size();
            if (distinct) {
                return seatSelection;
            } else {
                System.out.println("Duplicate seats detected!\nPlease try again");
            }
        }
    }
    
    private SeatInventoryEntity getDesiredSeatInventory(FlightScheduleEntity flightSchedule) {
        Scanner sc = new Scanner(System.in);
        int i = 1;
        System.out.println(" ** These are the available cabin classes ** ");
        for (SeatInventoryEntity seats : flightSchedule.getSeatInventory()) {
            String cabinClass;
            if (seats.getCabin().getCabinClassType() == null) {
                cabinClass = "Economy Class";
            } else switch (seats.getCabin().getCabinClassType()) {
                case F:
                    cabinClass = "First Class";
                    break;
                case J:
                    cabinClass = "Business Class";
                    break;
                case W:
                    cabinClass = "Premium Economy Class";
                    break;
                default:
                    cabinClass = "Economy Class";
                    break;
            }

            System.out.println(i + " " + cabinClass);
            i++;
        }
        while (true) {
            System.out.print("Select desired cabin class : ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice <= flightSchedule.getSeatInventory().size() && choice >= 1) {
                return flightSchedule.getSeatInventory().get(choice - 1);
            } else {
                System.out.println("Please enter a valid input.");
            }
        }

    }
    
    private void printFlightScheduleSingle(List<FlightScheduleEntity> flightSchedules, CabinClassTypeEnum cabinClassPreference, int passengers) throws CabinClassNotFoundException,
            FlightScheduleNotFoundException {
        System.out.printf("%15s%20s%30s%30s%40s%20s%20s%20s%30s%25s%25s\n", "Flight ID",
                "Flight Number",
                "Departure Airport",
                "Arrival Airport",
                "Departure Date & Time",
                "Duration (HRS)",
                "Arrival Date & Time",
                "Cabin Type",
                "Number of Seats Balanced",
                "Price per head",
                "Total Price");
        for (FlightScheduleEntity flightScheduleEntity : flightSchedules) {
            int timeZoneDifference = flightScheduleEntity.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getGmt()
                    - flightScheduleEntity.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getGmt();
    
            Date departureTime = flightScheduleEntity.getDepartureDateTime();
            double durationHours = flightScheduleEntity.getDuration();
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(departureTime);
            addDurationToCalendar(calendar, durationHours);
            calendar.add(Calendar.HOUR_OF_DAY, timeZoneDifference);
            Date arrival = calendar.getTime();
            
            for (SeatInventoryEntity seats : flightScheduleEntity.getSeatInventory()) {
                String cabinClassType;
                CabinClassTypeEnum cabinType = seats.getCabin().getCabinClassType();

                if (cabinClassPreference == null || cabinType == cabinClassPreference) {
                    switch (cabinType) {
                        case F:
                            cabinClassType = "First Class";
                            break;
                        case J:
                            cabinClassType = "Business Class";
                            break;
                        case W:
                            cabinClassType = "Premium Economy Class";
                            break;
                        case Y:
                            cabinClassType = "Economy Class";
                            break;
                        default:
                            continue;
                    }
                } else {
                    continue;
                }

                System.out.printf("%15s%20s%30s%30s%40s%20s%20s%20s%30s%25s%25s\n", flightScheduleEntity.getFlightScheduleId(),
                        flightScheduleEntity.getFlightSchedulePlan().getFlightNum(),
                        flightScheduleEntity.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getAirportName(),
                        flightScheduleEntity.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getAirportName(),
                        flightScheduleEntity.getDepartureDateTime().toString().substring(0, 19),
                        flightScheduleEntity.getDuration(),
                        arrival.toString().substring(0, 19),
                        cabinClassType,
                        seats.getBalanceSeats(),
                        flightScheduleSessionBean.retrieveLowestFare(flightScheduleEntity, seats.getCabin().getCabinClassType()).getFareAmount(),
                        flightScheduleSessionBean.retrieveLowestFare(flightScheduleEntity, seats.getCabin().getCabinClassType()).getFareAmount().multiply(BigDecimal.valueOf(passengers))
                );

            }
        }
    }
    
    private void addDurationToCalendar(Calendar calendar, double durationHours) {
        int hoursToAdd = (int) durationHours;
        int minutesToAdd = (int) ((durationHours % 1) * 60);
        calendar.add(Calendar.HOUR_OF_DAY, hoursToAdd);
        calendar.add(Calendar.MINUTE, minutesToAdd);
    }
    
    
    public void viewFlightReservations() {
    
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View Flight Reservations ***");
        List<ItineraryEntity> list = itinerarySessionBean.retrieveItinerariesByCustomerId(currentCustomer.getUserId());
        for (ItineraryEntity itinerary : list) {
            System.out.println("Itinerary Reservation ID: " + itinerary.getIternaryId());
            for (ReservationEntity reservation : itinerary.getReservations()) {
                String originCode = reservation.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIATACode();
                String destinationCode = reservation.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIATACode();
                String journey = originCode + " -> " + destinationCode;
                String departureDateTime = reservation.getFlightSchedule().getDepartureDateTime().toString().substring(0, 19);
                String duration = String.format("%.2f Hrs", reservation.getFlightSchedule().getDuration());
                String flightNum = reservation.getFlightSchedule().getFlightSchedulePlan().getFlightNum();

                System.out.println(String.format("\t%s, %s, %s, %s", flightNum, journey, departureDateTime, duration));
            }
            System.out.println("====================");
        }

        System.out.print("Press any key to continue ");
        sc.nextLine();
    }
    
    public void viewMyFlightReservationDetails() throws ItineraryDoNotExistException {
        
        try {
    
            Scanner sc = new Scanner(System.in);
            System.out.println("*** View Flight Reservation Details ***");

            System.out.print("Please enter the reservation Id : ");
            long reservationId = sc.nextLong();
            sc.nextLine();
            System.out.println();
            ItineraryEntity itinerary = itinerarySessionBean.retrieveItineraryByID(reservationId);

             BigDecimal totalAmountPaid = new BigDecimal(0);
                for (ReservationEntity reservation: itinerary.getReservations()) {

                    // Calculation of the total amount paid
                    BigDecimal fareAmount = reservation.getFareAmount();
                    int numberOfPassengers = reservation.getPassenger().size();
                    BigDecimal totalFareForReservation = fareAmount.multiply(new BigDecimal(numberOfPassengers));
                    totalAmountPaid = totalAmountPaid.add(totalFareForReservation);

                    // Building details for the journey
                    String originIATACode = reservation.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIATACode();
                    String destinationIATACode = reservation.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIATACode();
                    String journey = originIATACode + " -> " + destinationIATACode;

                    String departureDateTime = reservation.getFlightSchedule().getDepartureDateTime().toString().substring(0, 19);
                    double flightDuration = reservation.getFlightSchedule().getDuration();
                    String duration = String.format("%.2f Hrs", flightDuration);
                    String flightNum = reservation.getFlightSchedule().getFlightSchedulePlan().getFlightNum();

                    String cabinClass;
                    if (null == reservation.getCabinClass()) {
                        cabinClass = "Economy Class";
                    } else switch (reservation.getCabinClass()) {
                    case F:
                        cabinClass = "First Class";
                        break;
                    case J:
                        cabinClass = "Business Class";
                        break;
                    case W:
                        cabinClass = "Premium Economy Class";
                        break;
                    default:
                        cabinClass = "Economy Class";
                        break;
                    }

                    String output = String.format("Flight: %s, %s, %s, %s", flightNum, journey, departureDateTime, duration);
                    System.out.println(output);
   
                    System.out.println();
                    for (PassengerEntity passenger: reservation.getPassenger()) {                  
                        String name = passenger.getFirstName() + " " + passenger.getLastName();           
                        String seatNumber = passenger.getSeatNumber();
                        String formattedOutput = String.format("\t%s, %s, Seat %s", name, cabinClass, seatNumber);
                        System.out.println(formattedOutput);
                    }
                    System.out.println();
                }
                
                System.out.println("Total amount paid : $" + totalAmountPaid.toString());
                System.out.print("Press any key to continue : ");
                sc.nextLine();

            } catch (ItineraryDoNotExistException ex) {
                ex.printStackTrace();
            }
    }
    
    public void doLogOut() {
        
        
        Scanner sc = new Scanner(System.in);

        System.out.println("Log out of the System : ");
        System.out.println("Press 1 to log out : ");
        System.out.println("Press any other key to stay logged in :");
        String reply = sc.nextLine().trim();

        if (reply.equals("1") && login) {
            currentCustomer = null;
            login = false;
            System.out.println("You have been successfully logged out.");
        } else {
            System.out.println("Stayed logged in.");
        }
    }
    
    private void displayFlightSchedules(String title, List<FlightScheduleEntity> onDate, List<FlightScheduleEntity> minusOne, List<FlightScheduleEntity> minusTwo, List<FlightScheduleEntity> minusThree, List<FlightScheduleEntity> plusOne, List<FlightScheduleEntity> plusTwo, 
            List<FlightScheduleEntity> plusThree, CabinClassTypeEnum cabin, int passengers) throws CabinClassNotFoundException, FlightScheduleNotFoundException {
        System.out.println("===== " + title + " =====");
        System.out.println("Flights On Your Desired Date:");
        printSingleFlightSchedule(onDate, cabin, passengers);
        // ... similar for other dates
    }
    
    private void displayFlightSchedules1(String title, List<Pair<FlightScheduleEntity, FlightScheduleEntity>> onDate, List<FlightScheduleEntity> minusOne, List<FlightScheduleEntity> minusTwo, List<FlightScheduleEntity> minusThree, List<FlightScheduleEntity> plusOne, List<FlightScheduleEntity> plusTwo, List<FlightScheduleEntity> plusThree, CabinClassTypeEnum cabin, int passengers) throws CabinClassNotFoundException, FlightScheduleNotFoundException {
        System.out.println("===== " + title + " =====");
        System.out.println("Flights On Your Desired Date:");
        printFlightSchedulePair(onDate, cabin, passengers);
        // ... similar for other dates
    }
    
    private void printFlightSchedulePair(List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulePairs, CabinClassTypeEnum cabinClassPreference, int passengers) throws CabinClassNotFoundException, FlightScheduleNotFoundException {
        // Print headers as in printSingleFlightSchedule
        // ...

        for (Pair<FlightScheduleEntity, FlightScheduleEntity> pair : flightSchedulePairs) {
            FlightScheduleEntity firstSchedule = pair.getKey();
            FlightScheduleEntity secondSchedule = pair.getValue();

            // Print details of the first schedule
            printFlightScheduleDetails(firstSchedule, cabinClassPreference, passengers);

            // Print details of the second schedule
            printFlightScheduleDetails(secondSchedule, cabinClassPreference, passengers);

            // You can add additional formatting or separation between pairs if needed
        }
    }

    private void printFlightScheduleDetails(FlightScheduleEntity flightScheduleEntity, CabinClassTypeEnum cabinClassPreference, int passengers) {
        // Similar content as in your existing printSingleFlightSchedule method
        // ...
    }
    
    private void printSingleFlightSchedule(List<FlightScheduleEntity> flightSchedules, CabinClassTypeEnum cabinClassPreference, int passengers) throws CabinClassNotFoundException, FlightScheduleNotFoundException {
        System.out.printf("%15s%20s%30s%30s%40s%20s%20s%20s%30s%25s%25s\n", "Flight ID", 
                        "Flight Number", 
                        "Departure Airport", 
                        "Arrival Airport", 
                        "Departure Date & Time", 
                        "Duration (HRS)", 
                        "Arrival Date & Time", 
                        "Cabin Type", 
                        "Number of Seats Balanced", 
                        "Price per head", 
                        "Total Price");          
        for (FlightScheduleEntity flightScheduleEntity : flightSchedules) {
            int diff = flightScheduleEntity.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getGmt()
                    - flightScheduleEntity.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getGmt();
            Calendar c2 = Calendar.getInstance();
            c2.setTime(flightScheduleEntity.getDepartureDateTime());
            double duration = flightScheduleEntity.getDuration();
            int hour = (int) duration;
            int min = (int) (duration % 1 * 60);
            c2.add(Calendar.HOUR_OF_DAY, hour);
            c2.add(Calendar.MINUTE, min);
            c2.add(Calendar.HOUR_OF_DAY, diff);
            Date arrival = c2.getTime();
            for (SeatInventoryEntity seats : flightScheduleEntity.getSeatInventory()) {
                String cabinClassType;
                if (seats.getCabin().getCabinClassType() == CabinClassTypeEnum.F && (cabinClassPreference == CabinClassTypeEnum.F || cabinClassPreference == null)) {
                    cabinClassType = "First Class";
                } else if (seats.getCabin().getCabinClassType() == CabinClassTypeEnum.J && (cabinClassPreference == CabinClassTypeEnum.J || cabinClassPreference == null)) {
                    cabinClassType = "Business Class";
                } else if (seats.getCabin().getCabinClassType() == CabinClassTypeEnum.W && (cabinClassPreference == CabinClassTypeEnum.W || cabinClassPreference == null)) {
                    cabinClassType = "Premium Economy Class";
                } else if (seats.getCabin().getCabinClassType() == CabinClassTypeEnum.Y && (cabinClassPreference == CabinClassTypeEnum.Y || cabinClassPreference == null)) {
                    cabinClassType = "Economy Class";
                } else {
                    continue;
                }

                System.out.printf("%15s%20s%30s%30s%40s%20s%20s%20s%30s%25s%25s\n", flightScheduleEntity.getFlightScheduleId(),
                        flightScheduleEntity.getFlightSchedulePlan().getFlightNum(),
                        flightScheduleEntity.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getAirportName(),
                        flightScheduleEntity.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getAirportName(),
                        flightScheduleEntity.getDepartureDateTime().toString().substring(0, 19),
                        flightScheduleEntity.getDuration(),
                        arrival.toString().substring(0, 19),
                        cabinClassType,
                        seats.getBalanceSeats(),
                        flightScheduleSessionBean.retrieveLowestFare(flightScheduleEntity, seats.getCabin().getCabinClassType()).getFareAmount(),
                        flightScheduleSessionBean.retrieveLowestFare(flightScheduleEntity, seats.getCabin().getCabinClassType()).getFareAmount().multiply(BigDecimal.valueOf(passengers))
                );

            }
        }
    }
    
    public List<FlightScheduleEntity> getFlightSchedules(String departure, String destination, Date date, CabinClassTypeEnum cabin) throws FlightNotFoundException {
        List<FlightScheduleEntity> schedule = new ArrayList<>();
        List<FlightEntity> flights = flightSessionBean.retrieveAllFlightByFlightRoute(departure, destination);

        for (FlightEntity f : flights) {
            for (FlightSchedulePlanEntity plan : f.getFlightSchedulePlan()) {
                if (plan.isDisabled()) {
                    continue;
                }
                for (FlightScheduleEntity scheduleEntity : plan.getFlightSchedule()) {
                    if (isMatchingSchedule(scheduleEntity, date, cabin)) {
                        schedule.add(scheduleEntity);
                    }
                }
            }
        }

        Collections.sort(schedule, (FlightScheduleEntity schedule1, FlightScheduleEntity schedule2)
                -> schedule1.getDepartureDateTime().compareTo(schedule2.getDepartureDateTime()));

        return schedule;
    }
    
    private boolean isMatchingSchedule(FlightScheduleEntity schedule, Date date, CabinClassTypeEnum cabin) {
        if (cabin == null || hasMatchingCabin(schedule, cabin)) {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(schedule.getDepartureDateTime());
            c2.setTime(date);
            return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
                    && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
        }
        return false;
    }
    private boolean hasMatchingCabin(FlightScheduleEntity schedule, CabinClassTypeEnum cabin) {
        for (SeatInventoryEntity s : schedule.getSeatInventory()) {
            if (s.getCabin().getCabinClassType().equals(cabin)) {
                return true;
            }
        }
        return false;
    }
    
    private List<FlightScheduleEntity> getFlightSchedulesForAdjustedDate(Calendar calendar, int daysToAdd, String departureCode, String destinationCode, CabinClassTypeEnum cabin) throws FlightNotFoundException {
        calendar.add(Calendar.DATE, daysToAdd);
        return getFlightSchedules(departureCode, destinationCode, calendar.getTime(), cabin);
    }
    
    private List<Pair<FlightScheduleEntity, FlightScheduleEntity>> getIndirectFlightSchedulesForAdjustedDate(Calendar calendar, int daysToAdd, String departureCode, String destinationCode, CabinClassTypeEnum cabin) throws FlightNotFoundException {
        calendar.add(Calendar.DATE, daysToAdd);
        // Assuming you have a method like getIndirectFlightSchedules that returns List<Pair<FlightScheduleEntity, FlightScheduleEntity>>
        return getIndirectUnManagedFlightSchedules(departureCode, destinationCode, calendar.getTime(), cabin);
    }
    
    public List<Pair<FlightScheduleEntity, FlightScheduleEntity>> getIndirectUnManagedFlightSchedules(
            String departure, String destination, Date date, CabinClassTypeEnum cabin) throws FlightNotFoundException {
        List<Pair<FlightScheduleEntity, FlightScheduleEntity>> schedule = new ArrayList<>();
        List<FlightEntity[]> flights = flightSessionBean.retrieveAllIndirectFlightByFlightRoute(departure, destination);

        for (Object[] pair : flights) {
            FlightEntity firstFlight = (FlightEntity) pair[0];
            FlightEntity secondFlight = (FlightEntity) pair[1];

            for (FlightScheduleEntity flightSchedule : getValidFlightSchedules(firstFlight, date, cabin)) {
                for (FlightScheduleEntity f2 : getValidFlightSchedules(secondFlight, date, cabin)) {
                    if (isTimeGapAcceptable(flightSchedule, f2)) {
                        schedule.add(new Pair<>(flightSchedule, f2));
                    }
                }
            }
        }

        sortFlightSchedulesByDeparture(schedule);
        return schedule;
    }
    
    private void sortFlightSchedulesByDeparture(List<Pair<FlightScheduleEntity, FlightScheduleEntity>> schedule) {
        schedule.sort(Comparator.comparing(pair -> pair.getKey().getDepartureDateTime()));
    }
    
    private List<FlightScheduleEntity> getValidFlightSchedules(FlightEntity flight, Date date, CabinClassTypeEnum cabin) {
        List<FlightScheduleEntity> validSchedules = new ArrayList<>();

        for (FlightSchedulePlanEntity plan : flight.getFlightSchedulePlan()) {
            if (!plan.isDisabled()) {
                for (FlightScheduleEntity flightSchedule : plan.getFlightSchedule()) {
                    if (hasMatchingCabin(flightSchedule, cabin)) {
                        validSchedules.add(flightSchedule);
                    }
                }
            }
        }

        return validSchedules;
    }
    
    private boolean isTimeGapAcceptable(FlightScheduleEntity flightSchedule1, FlightScheduleEntity flightSchedule2) {
        Calendar arrivalTime1 = calculateArrivalTime(flightSchedule1);
        Calendar departureTime2 = Calendar.getInstance();
        departureTime2.setTime(flightSchedule2.getDepartureDateTime());
        long gap = Duration.between(arrivalTime1.toInstant(), departureTime2.toInstant()).toHours();
        return gap >= 2L && gap <= 12L;
    }
    
    private Calendar calculateArrivalTime(FlightScheduleEntity flightSchedule) {
        Calendar departureTime = Calendar.getInstance();
        departureTime.setTime(flightSchedule.getDepartureDateTime());
        double duration = flightSchedule.getDuration();
        int hours = (int) duration;
        int minutes = (int) ((duration % 1) * 60);
        departureTime.add(Calendar.HOUR_OF_DAY, hours);
        departureTime.add(Calendar.MINUTE, minutes);
        int timeDifference = flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getGmt()
                - flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getGmt();
        departureTime.add(Calendar.HOUR_OF_DAY, timeDifference);
        return departureTime;
    }
    
    private void printFlightScheduleWithConnecting(List<Pair<FlightScheduleEntity, FlightScheduleEntity>> flightSchedulePairs, CabinClassTypeEnum cabin, int passengers) throws FlightScheduleNotFoundException, CabinClassNotFoundException {
         System.out.printf("%15s%20s%40s%40s%30s%20s%30s%30s%30s%25s%25s%25s%30s%45s%45s%40s%20s%30s%30s%30s%25s%25s\n", "Flight ID", 
                        "Flight Number", 
                        "Departure Airport", 
                        "Arrival Airport", 
                        "Departure Date & Time", 
                        "Duration (HRS)", 
                        "Arrival Date & Time", 
                        "Cabin Type", 
                        "Number of Seats Balanced", 
                        "Price per head", 
                        "Total Price",
                        "Connecting Flight ID", 
                        "Connecting Flight Number", 
                        "Connecting Departure Airport", 
                        "Arrival Airport", 
                        "Departure Date & Time", 
                        "Duration (HRS)", 
                        "Arrival Date & Time", 
                        "Cabin Type", 
                        "Number of Seats Balanced", 
                        "Price per head", 
                        "Total Price");
        for (Pair<FlightScheduleEntity, FlightScheduleEntity> pair: flightSchedulePairs) {
            FlightScheduleEntity flight1 = pair.getKey();
            FlightScheduleEntity flight2 = pair.getValue();
            int diff1 = flight1.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getGmt() - 
                    flight1.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getGmt();
            Calendar c2 = Calendar.getInstance();
            c2.setTime(flight1.getDepartureDateTime());
            double duration = flight1.getDuration();
            int hour = (int) duration;
            int min = (int) (duration % 1 * 60);
            c2.add(Calendar.HOUR_OF_DAY, hour);
            c2.add(Calendar.MINUTE, min);
            c2.add(Calendar.HOUR_OF_DAY, diff1);
            Date arrival1 = c2.getTime();
            int diff2 = flight2.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getGmt() - 
                    flight2.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getGmt();
            Calendar c3 = Calendar.getInstance();
            c3.setTime(flight2.getDepartureDateTime());
            double duration2 = flight2.getDuration();
            int hour2 = (int) duration2;
            int min2 = (int) (duration2 % 1 * 60);
            c3.add(Calendar.HOUR_OF_DAY, hour2);
            c3.add(Calendar.MINUTE, min2);
            c3.add(Calendar.HOUR_OF_DAY, diff2);
            Date arrival2 = c3.getTime();
            for (SeatInventoryEntity seats1: flight1.getSeatInventory()) {
                for (SeatInventoryEntity seats2: flight2.getSeatInventory()) {
                    String cabinClassType1, cabinClassType2;
                    if (cabin == null) {
                        if (seats1.getCabin().getCabinClassType() == CabinClassTypeEnum.F) {
                            cabinClassType1 = "First Class"; 
                        } else if (seats1.getCabin().getCabinClassType() == CabinClassTypeEnum.J) {
                            cabinClassType1 = "Business Class";
                        } else if (seats1.getCabin().getCabinClassType() == CabinClassTypeEnum.W) {
                            cabinClassType1 = "Premium Economy Class";
                        } else {
                            cabinClassType1 = "Economy Class";  
                        }
                        if (seats2.getCabin().getCabinClassType() == CabinClassTypeEnum.F) {
                            cabinClassType2 = "First Class"; 
                        } else if (seats2.getCabin().getCabinClassType() == CabinClassTypeEnum.J) {
                            cabinClassType2 = "Business Class";
                        } else if (seats2.getCabin().getCabinClassType() == CabinClassTypeEnum.W) {
                            cabinClassType2 = "Premium Economy Class";
                        } else {
                            cabinClassType2 = "Economy Class";  
                        }
                    } else if (seats1.getCabin().getCabinClassType() == CabinClassTypeEnum.F && seats2.getCabin().getCabinClassType() == CabinClassTypeEnum.F && cabin == CabinClassTypeEnum.F) {
                        cabinClassType1 = "First Class"; 
                        cabinClassType2 = "First Class"; 
                    } else if (seats1.getCabin().getCabinClassType() == CabinClassTypeEnum.J && seats2.getCabin().getCabinClassType() == CabinClassTypeEnum.J && cabin == CabinClassTypeEnum.J) {
                        cabinClassType1 = "Business Class";
                        cabinClassType2 = "Business Class";
                    } else if (seats1.getCabin().getCabinClassType() == CabinClassTypeEnum.W && seats2.getCabin().getCabinClassType() == CabinClassTypeEnum.W && cabin == CabinClassTypeEnum.W) {
                        cabinClassType1 = "Premium Economy Class";
                        cabinClassType2 = "Premium Economy Class";
                    } else if (seats1.getCabin().getCabinClassType() == CabinClassTypeEnum.Y && seats2.getCabin().getCabinClassType() == CabinClassTypeEnum.Y && cabin == CabinClassTypeEnum.Y) {
                        cabinClassType1 = "Economy Class"; 
                        cabinClassType2 = "Economy Class"; 
                    } else {
                        continue;
                    }
                    System.out.printf("%15s%20s%40s%40s%30s%20s%30s%30s%30s%25s%25s%25s%30s%45s%45s%40s%20s%30s%30s%30s%25s%25s\n", flight1.getFlightScheduleId(), 
                        flight1.getFlightSchedulePlan().getFlightNum(), 
                        flight1.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getAirportName(), 
                        flight1.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getAirportName(), 
                        flight1.getDepartureDateTime().toString().substring(0, 19), 
                        flight1.getDuration(), 
                        arrival1.toString().substring(0, 19), 
                        cabinClassType1, 
                        seats1.getBalanceSeats(), 
                        flightScheduleSessionBean.retrieveLowestFare(flight1, seats1.getCabin().getCabinClassType()).getFareAmount(), 
                        flightScheduleSessionBean.retrieveLowestFare(flight1, seats1.getCabin().getCabinClassType()).getFareAmount().multiply(BigDecimal.valueOf(passengers)),
                        flight2.getFlightScheduleId(), 
                        flight2.getFlightSchedulePlan().getFlightNum(), 
                        flight2.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getAirportName(), 
                        flight2.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getAirportName(), 
                        flight2.getDepartureDateTime().toString().substring(0, 19), 
                        flight2.getDuration(), 
                        arrival2.toString().substring(0, 19), 
                        cabinClassType2, 
                        seats2.getBalanceSeats(), 
                        flightScheduleSessionBean.retrieveLowestFare(flight2, seats2.getCabin().getCabinClassType()).getFareAmount(), 
                        flightScheduleSessionBean.retrieveLowestFare(flight2, seats2.getCabin().getCabinClassType()).getFareAmount().multiply(BigDecimal.valueOf(passengers))
                    );
                }
            }
        }
    }
}