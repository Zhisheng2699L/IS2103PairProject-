/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/*package holidayreservationsystemclient;

import entity.SeatInventoryEntity;
import enumeration.CabinClassTypeEnum;
import exceptions.FlightNotFoundException;
import ejb.session.ws.MyPair;
import ejb.session.ws.FlightScheduleEntity;
import entity.FlightEntity;
import entity.FlightScheduleEntity;
import exceptions.FlightExistException;
import exceptions.InvalidLoginCredentialException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author kahjy
 */
/*public class HolidayReservationSystemClientApp {

    boolean loggedIn;
    Long currentPartner;

    public HolidayReservationSystemClientApp() {
    }

    public void runApp() {
        while (true) {
            if (!loggedIn) {
                Scanner sc = new Scanner(System.in);

                System.out.println("===== Welcome to Holiday Reservation System, Enjoy your stay here ^_^ =====\n");
                System.out.println("=== Login Portal ===");
                System.out.print("Enter username> ");
                String username = sc.nextLine().trim();
                System.out.print("Enter password> ");
                String password = sc.nextLine().trim();

                if (username.length() > 0 && password.length() > 0) { 
                    try {
                        currentPartner = doLogin(username, password);
                        loggedIn = true;
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } else {
                mainMenu();
            }
        }
    }

    private void mainMenu() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (loggedIn) {
            System.out.println("===== Welcome to Holiday Reservation System =====\n");
            System.out.println("What would you like to do today? \n");
            System.out.println("1: Search a Flight");
            System.out.println("2: Reserve a Flight");
            System.out.println("3: View My Flight Reservations");
            System.out.println("4: View My Flight Reservation Details");
            System.out.println("5: Log Out\n");

            response = 0;
            while (response < 1 || response > 5) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    doSearchFlight();
                } else if (response == 2) {
                    doReserveFlight();
                } else if (response == 3) {
                    doViewFlightReservation();
                } else if (response == 4) {
                    doViewFlightReservationDetails();
                } else if (response == 5) {
                    doLogout();
                    System.out.println("Log out successful. Do come again! \n");
                    break;
                } else {
                    System.out.println("Invalid Option, please try again!");
                }
            }
            if (response == 5) {
                break;
            }
        }
    }

    private void doLogout() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Are you sure you want to log out? (Y or N): ");
        String reply = sc.nextLine().trim();

        if ((reply.equals("Y") || reply.equals("y")) && loggedIn) {
            currentPartner = null;
            loggedIn = false;
        }
    }
     /* ------------------------ START OF doSearchFlight() ------------------------------ */   
    /*private void doSearchFlight() {
        Scanner sc = new Scanner (System.in);
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/M/yyyy");
        
        int tripType = selectOption("Enter Trip Type>('1' for One-Way and '2' for Round-Trip/Return): ", 1, 2);
       
        
        System.out.print("Please enter your departure airport (IATA code): ");
        String departureCode = sc.nextLine().trim();
        
        System.out.print("Please enter your destination airport (IATA code): ");
        String destinationCode = sc.nextLine().trim();

        Date departureDate;
        String date;
        while(true) {
            try {
                System.out.print("Enter departure date  (dd/mm/yyyy): ");
                date = sc.nextLine().trim();
                departureDate = inputFormat.parse(date);
                break;
            } catch (java.text.ParseException ex) {
                System.out.println("ERROR! Invalid date has been captured. PLEASE try again!");
            }
        }
        
        System.out.print("Enter number of passengers: ");
        int passengers = sc.nextInt();
        
        int passengerFlightPref = selectOption("Choose your preference> ('0' for No Preference, '1' for Direct Flight, '2' for Connecting Flights): ", 0,2);

        CabinClassTypeEnum cabin = selectCabinClass();
        
        if (passengerFlightPref  == 0) {
            boolean noDirectFlights = false;
            boolean noIndirectFlights = false;
            
            try {
                List<FlightScheduleEntity> outboundFlightSchedules = getFlightSchedules(departureCode, destinationCode, departureDate.toString(), cabin);
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(departureDate);
                
                List<FlightScheduleEntity> flightSchedulesPlusOne = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
                List<FlightScheduleEntity> flightSchedulesPlusTwo = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
                List<FlightScheduleEntity> flightSchedulesPlusThree = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);

                calendar.setTime(departureDate);
                List<FlightScheduleEntity> flightSchedulesMinusOne = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
                List<FlightScheduleEntity> flightSchedulesMinusTwo = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
                List<FlightScheduleEntity> flightSchedulesMinusThree = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
                
                displayFlightSchedules("Direct Outbound Flights", outboundFlightSchedules, flightSchedulesMinusOne, flightSchedulesMinusTwo, flightSchedulesMinusThree, flightSchedulesPlusOne, flightSchedulesPlusTwo, flightSchedulesPlusThree, cabin, passengers);
            
            } catch (FlightNotFoundException ex) {
                System.out.println("No direct flights available for your chosen route.");
                noDirectFlights = true;    
            }
            
            try {
                List<MyPair> outboundFlightSchedules = getIndirectFlightSchedules(departureCode, destinationCode, departureDate.toString(), cabin);
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(departureDate);
                
                List<MyPair> flightSchedulesPlusOne = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
                List<MyPair> flightSchedulesPlusTwo = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
                List<MyPair> flightSchedulesPlusThree = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);

                calendar.setTime(departureDate);
                List<MyPair> flightSchedulesMinusOne = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
                List<MyPair> flightSchedulesMinusTwo = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
                List<MyPair> flightSchedulesMinusThree = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
                
                displayFlightSchedules("Direct Outbound Flights", outboundFlightSchedules, flightSchedulesMinusOne, flightSchedulesMinusTwo, flightSchedulesMinusThree, flightSchedulesPlusOne, flightSchedulesPlusTwo, flightSchedulesPlusThree, cabin, passengers);
            
            } catch (FlightNotFoundException ex) {
                System.out.println("No indirect flights for your specified route\n");
                noIndirectFlights = true;
            }     
            if (noDirectFlights && noIndirectFlights) {
                return;
            } 
        }
        
        if (passengerFlightPref  == 1) {
            try {
                List<FlightScheduleEntity> outboundFlightSchedules = getFlightSchedules(departureCode, destinationCode, departureDate.toString(), cabin);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(departureDate);

                List<FlightScheduleEntity> flightSchedulesPlusOne = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
                List<FlightScheduleEntity> flightSchedulesPlusTwo = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
                List<FlightScheduleEntity> flightSchedulesPlusThree = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);

                calendar.setTime(departureDate);
                List<FlightScheduleEntity> flightSchedulesMinusOne = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
                List<FlightScheduleEntity> flightSchedulesMinusTwo = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
                List<FlightScheduleEntity> flightSchedulesMinusThree = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);

                displayFlightSchedules("Direct Outbound Flights", outboundFlightSchedules, flightSchedulesMinusOne, flightSchedulesMinusTwo, flightSchedulesMinusThree, flightSchedulesPlusOne, flightSchedulesPlusTwo, flightSchedulesPlusThree, cabin, passengers);

            } catch (FlightNotFoundException ex) {
                System.out.println("No direct flights available for your chosen route.");
                return;
            }
        }
        
        if (passengerFlightPref == 2) {
            try {
                List<MyPair> outboundFlightSchedules = getIndirectFlightSchedules(departureCode, destinationCode, departureDate.toString(), cabin);
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(departureDate);
                
                List<MyPair> flightSchedulesPlusOne = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
                List<MyPair> flightSchedulesPlusTwo = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
                List<MyPair> flightSchedulesPlusThree = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);

                calendar.setTime(departureDate);
                List<MyPair> flightSchedulesMinusOne = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
                List<MyPair> flightSchedulesMinusTwo = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
                List<MyPair> flightSchedulesMinusThree = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
                
                displayFlightSchedules("Direct Outbound Flights", outboundFlightSchedules, flightSchedulesMinusOne, flightSchedulesMinusTwo, flightSchedulesMinusThree, flightSchedulesPlusOne, flightSchedulesPlusTwo, flightSchedulesPlusThree, cabin, passengers);
            
            } catch (FlightNotFoundException ex) {
                System.out.println("No indirect flights for your specified route\n");
                return;
            }  
        }
        System.out.println("\n");
        
         if (tripType == 2 && passengerFlightPref == 0) {
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
            List<FlightScheduleEntity> dateActualFlightScheduleInBound = getFlightSchedules(destinationCode, departureCode, returnDate.toString(), cabin);

            Calendar calendar = Calendar.getInstance();
            
            calendar.setTime(returnDate);
            List<MyPair> flightSchedulesPlusOne = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
            List<MyPair> flightSchedulesPlusTwo = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
            List<MyPair> flightSchedulesPlusThree = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);

            calendar.setTime(departureDate);
            List<MyPair> flightSchedulesMinusOne = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
            List<MyPair> flightSchedulesMinusTwo = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
            List<MyPair> flightSchedulesMinusThree = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
            
            
            } catch (FlightNotFoundException ex) {
                System.out.print("No indirect flights for your specified route\n");
                noDirectFlights = true;
            }
            if (noDirectFlights && noIndirectFlights) {
                return;
            }    
        }
        
         
        if (tripType == 2 && passengerFlightPref == 1) {
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
            List<FlightScheduleEntity> dateActualFlightScheduleInBound = getFlightSchedules(destinationCode, departureCode, returnDate.toString(), cabin);

            Calendar calendar = Calendar.getInstance();
            
            calendar.setTime(returnDate);
            List<FlightScheduleEntity> flightSchedulesPlusOne = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
            List<FlightScheduleEntity> flightSchedulesPlusTwo = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
            List<FlightScheduleEntity> flightSchedulesPlusThree = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);

            calendar.setTime(departureDate);
            List<FlightScheduleEntity> flightSchedulesMinusOne = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
            List<FlightScheduleEntity> flightSchedulesMinusTwo = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
            List<FlightScheduleEntity> flightSchedulesMinusThree = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
            
            
            } catch (FlightNotFoundException ex) {
                System.out.print("Sorry there are no return flights for this flight route within this period");
                return;
            }  
        }
        
        if (tripType == 2 && passengerFlightPref == 2) {
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
            List<MyPair> dateActualFlightScheduleInBound = getFlightSchedules(destinationCode, departureCode, returnDate.toString(), cabin);

            Calendar calendar = Calendar.getInstance();
            
            calendar.setTime(returnDate);
            List<MyPair> flightSchedulesPlusOne = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
            List<MyPair> flightSchedulesPlusTwo = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);
            List<MyPair> flightSchedulesPlusThree = getFlightSchedulesForAdjustedDate(calendar, 1, departureCode, destinationCode, cabin);

            calendar.setTime(departureDate);
            List<MyPair> flightSchedulesMinusOne = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
            List<MyPair> flightSchedulesMinusTwo = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
            List<MyPair> flightSchedulesMinusThree = getFlightSchedulesForAdjustedDate(calendar, -1, departureCode, destinationCode, cabin);
            
            
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
        if (tripType == 1 && passengerFlightPref == 1) {
            outbound2 = null;
            inbound2 = null;
            inbound1 = null;
            System.out.print("Enter flight you would like to reserve (Flight Id): ");
            outbound1 = sc.nextLong();
            sc.nextLine();
        } else if (tripType == 1 && passengerFlightPref == 2) {
            inbound1 = null;
            inbound2 = null;
            System.out.print("Enter the first outbound flight you would like to reserve (Flight Id): ");
            outbound1 = sc.nextLong();
            System.out.print("Enter the connecting outbound flight you would like to reserve (Flight Id): ");
            outbound2 = sc.nextLong();   
            sc.nextLine();
        } else if (tripType == 2 && passengerFlightPref == 1) {
            outbound2 = null;
            inbound2 = null;
            System.out.print("Enter the outbound flight you would like to reserve (Flight Id): ");
            outbound1 = sc.nextLong();
            System.out.print("Enter the inbound flight you would like to reserve (Flight Id): ");
            inbound1 = sc.nextLong();
            sc.nextLine();    
        } else if (tripType == 2 && passengerFlightPref == 2) {
            System.out.print("Enter the first outbound flight you would like to reserve (Flight Id): ");
            outbound1 = sc.nextLong();
            System.out.print("Enter the connecting outbound flight you would like to reserve (Flight Id): ");
            outbound2 = sc.nextLong();
            System.out.print("Enter the first inbound flight you would like to reserve (Flight Id): ");
            inbound1 = sc.nextLong();
            System.out.print("Enter the connecting inbound flight you would like to reserve (Flight Id): ");
            inbound2 = sc.nextLong();
        } else if (passengerFlightPref == 0) {
            System.out.print("Select type of flight you would like to reserve ('1' for Direct Flight, '2' for Connecting Flight): ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (tripType == 1 && choice == 1) {
                outbound2 = null;
                inbound2 = null;
                inbound1 = null;
                System.out.print("Enter flight you would like to reserve (Flight Id): ");
                outbound1 = sc.nextLong();
                sc.nextLine();
            } else if (tripType == 1 && choice == 2) {
                inbound1 = null;
                inbound2 = null;
                System.out.print("Enter the first outbound flight you would like to reserve (Flight Id): ");
                outbound1 = sc.nextLong();
                System.out.print("Enter the connecting outbound flight you would like to reserve (Flight Id): ");
                outbound2 = sc.nextLong(); 
            } else if (tripType == 2 && choice == 1) {
               outbound2 = null;
                inbound2 = null;
                System.out.print("Enter the outbound flight you would like to reserve (Flight Id): ");
                outbound1 = sc.nextLong();
                System.out.print("Enter the inbound flight you would like to reserve (Flight Id): ");
                inbound1 = sc.nextLong();
                sc.nextLine();   
            } else if (tripType == 2 && choice == 2) {
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
        doReserveFlight(outbound1, outbound2, inbound1, inbound2, cabin, passengers);
    }

    
    
    
    
    private int selectOption(String prompt, int min, int max) {
        Scanner sc = new Scanner(System.in);
        int selection;
        while (true) {
            System.out.print(prompt);
            selection = sc.nextInt();
            sc.nextLine(); // consume newline
            if (selection >= min && selection <= max) {
                return selection;
            }
            System.out.println("ERROR! please try again with the correct number ('1' or '2')");
        }
    }
    
    private CabinClassTypeEnum selectCabinClass() {
        int cabinPref = selectOption("Choose your preference for cabin class ('0' for No Preference, '1' for First Class, '2' for Business Class, '3' for Premium Economy Class, '4' Economy Class): ", 0, 4);
        switch (cabinPref) {
            case 1: return CabinClassTypeEnum.F;
            case 2: return CabinClassTypeEnum.J;
            case 3: return CabinClassTypeEnum.W;
            case 4: return CabinClassTypeEnum.Y;
            default: return null;
        }
    }
    
    private List<FlightScheduleEntity> getFlightSchedulesForAdjustedDate(Calendar calendar, int daysToAdd, String departureCode, String destinationCode, CabinClassTypeEnum cabin) {
        calendar.add(Calendar.DATE, daysToAdd);
        return getFlightSchedules(departureCode, destinationCode, calendar.getTime().toString(), cabin);
    }
    
    private void displayFlightSchedules(String title, List<FlightScheduleEntity> onDate, List<FlightScheduleEntity> minusOne, List<FlightScheduleEntity> minusTwo, List<FlightScheduleEntity> minusThree, List<FlightScheduleEntity> plusOne, List<FlightScheduleEntity> plusTwo, List<FlightScheduleEntity> plusThree, CabinClassTypeEnum cabin, int passengers) {
        System.out.println("===== " + title + " =====");
        System.out.println("Flights On Your Desired Date:");
        printSingleFlightSchedule(onDate, cabin, passengers);
        // ... similar for other dates
    }
    
    private void printSingleFlightSchedule(List<FlightScheduleEntity> flightSchedules, CabinClassTypeEnum cabinClassPreference, int passengers) throws CabinClassNotFoundException_Exception, FlightScheduleNotFoundException_Exception {
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
            c2.setTime(flightScheduleEntity.getDepartureDateTime().toGregorianCalendar().getTime());
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
                        getBiggestFare(flightScheduleEntity, seats.getCabin().getCabinClassType()).getFareAmount(),
                        getBiggestFare(flightScheduleEntity, seats.getCabin().getCabinClassType()).getFareAmount().multiply(BigDecimal.valueOf(passengers))
                );

            }
        }
    }
    */
    /* ------------------------ END OF doSearchFlight() ------------------------------ */
   /* 
    private static Long doLogin(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException {
        ejb.session.ws.FlightReservationWebService_Service service = new ejb.session.ws.FlightReservationWebService_Service();
        ejb.session.ws.FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.doLogin(username, password);
    }
    
    private static java.util.List<ejb.session.ws.FlightScheduleEntity> getFlightSchedules(java.lang.String origin, java.lang.String destination, java.lang.String date, ejb.session.ws.CabinClassTypeEnum cabinclasstype) throws FlightNotFoundException_Exception, ParseException_Exception {
        ejb.session.ws.FlightReservationWebService_Service service = new ejb.session.ws.FlightReservationWebService_Service();
        ejb.session.ws.FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.getFlightSchedules(origin, destination, date, cabinclasstype);
    }
    
    private static java.util.List<ejb.session.ws.MyPair> getIndirectFlightSchedules(java.lang.String origin, java.lang.String destination, java.lang.String date, ejb.session.ws.CabinClassTypeEnum cabinclasstype) throws FlightNotFoundException_Exception, ParseException_Exception {
        ejb.session.ws.FlightReservationWebService_Service service = new ejb.session.ws.FlightReservationWebService_Service();
        ejb.session.ws.FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.getIndirectFlightSchedules(origin, destination, date, cabinclasstype);
    }
}*/
