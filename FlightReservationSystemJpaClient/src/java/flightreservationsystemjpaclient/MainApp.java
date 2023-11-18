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
import entity.FlightScheduleEntity;
import entity.ItineraryEntity;
import entity.PassengerEntity;
import entity.ReservationEntity;
import entity.SeatInventoryEntity;
import enumeration.CabinClassTypeEnum;
import exceptions.CabinClassNotFoundException;
import exceptions.CustomerExistException;
import exceptions.FlightNotFoundException;
import exceptions.FlightScheduleNotFoundException;
import exceptions.InvalidInputGeneralException;
import exceptions.InvalidLoginCredentialException;
import exceptions.ItineraryDoNotExistException;
import exceptions.SeatSlotNotFoundException;
import exceptions.UnknownPersistenceException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


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

    public MainApp() {
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
      
}