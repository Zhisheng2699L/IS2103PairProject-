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
import ejb.session.stateless.SeatsAvailabilitySessionBeanRemote;
import entity.CustomerEntity;
import entity.FlightScheduleEntity;
import entity.SeatInventoryEntity;
import enumeration.CabinClassTypeEnum;
import exceptions.CabinClassNotFoundException;
import exceptions.CustomerExistException;
import exceptions.FlightNotFoundException;
import exceptions.FlightScheduleNotFoundException;
import exceptions.InvalidInputGeneralException;
import exceptions.InvalidLoginCredentialException;
import exceptions.UnknownPersistenceException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.ejb.EJB;


/**
 *
 * @author foozh
 */
public class MainApp {
    
    @EJB
    private EmployeeSessionBeanRemote employeeSessionBean;

    @EJB
    private PartnerSessionBeanRemote partnerSessionBean;

    @EJB
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBean;

    @EJB
    private AirportSessionBeanRemote airportSessionBean;

    @EJB
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean;

    @EJB
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;

    @EJB
    private FlightSessionBeanRemote flightSessionBean;

    @EJB
    private FlightRouteSessionBeanRemote flightRouteSessionBean;

    @EJB
    private FareSessionBeanRemote fareSessionBean;

    @EJB
    private SeatsAvailabilitySessionBeanRemote seatsInventorySessionBean;

    @EJB
    private FlightScheduleSessionBeanRemote flightScheduleSessionBean;

    @EJB
    private CustomerSessionBeanRemote customerSessionBean;

    @EJB
    private ItinerarySessionBeanRemote itinerarySessionBean;

    @EJB
    private ReservationSessionBeanRemote reservationSessionBean;

    @EJB
    private PassengerSessionBeanRemote passengerSessionBean;
    
    private CustomerEntity currentCustomer;
    private boolean login;

    public MainApp() {
    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBean, PartnerSessionBeanRemote partnerSessionBean, AircraftTypeSessionBeanRemote aircraftTypeSessionBean, AirportSessionBeanRemote airportSessionBean, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean, FlightSessionBeanRemote flightSessionBean,
            FlightRouteSessionBeanRemote flightRouteSessionBean, FareSessionBeanRemote fareSessionBean,
            SeatsAvailabilitySessionBeanRemote seatsInventorySessionBean, FlightScheduleSessionBeanRemote flightScheduleSessionBean,
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
    }
    
    public void runApp() throws UnknownPersistenceException, CustomerExistException, InvalidLoginCredentialException {
        
        while(true) {
            
            if(!login) {
                Scanner sc = new Scanner(System.in);
                Integer response = 0;
                
                System.out.println("=== Welcome to Merlion Airlines Flight Reservation System===\n");
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
            System.out.println("*** Welcome to Merlion Airlines Reservation Client ***\n");
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
                }
            
                   
            }
            
            
            
        }
    }
  
    private void searchForFlight() throws InvalidInputGeneralException, FlightNotFoundException {
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
        
        if (flightPreference == 0) {
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
      
}
