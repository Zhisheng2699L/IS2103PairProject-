/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.SeatsInventorySessionBeanRemote;
import entity.EmployeeEntity;
import entity.FlightEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.PassengerEntity;
import entity.ReservationEntity;
import entity.SeatInventoryEntity;
import enumeration.CabinClassTypeEnum;
import enumeration.EmployeeAccessRightEnum;
import exceptions.FlightNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;

/**
 *
 * @author kahjy
 */

//SALES ADMIN MODULE IS TO PROVIDE FUNCTIONALITY for viewing seats availability and flight reservations!!

public class SalesAdminModule {
    private EmployeeEntity currentEmployee;
    private SeatsInventorySessionBeanRemote seatsInventorySessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private FlightSessionBeanRemote flightSessionBean;
    
    public SalesAdminModule(EmployeeEntity currentEmployee, SeatsInventorySessionBeanRemote seatsAvailabilitySessionBean, ReservationSessionBeanRemote reservationSessionBean, FlightSessionBeanRemote flightSessionBean) {
        this.currentEmployee = currentEmployee;
        this.seatsInventorySessionBean = seatsAvailabilitySessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.flightSessionBean = flightSessionBean;
    }

    public void mainMenu() {
        Scanner sc = new Scanner(System.in);
        int response = 0;
        
        while(true) {
            System.out.println("===== Sales Management Module =====\n");
            System.out.println("1: View Seats Availability");
            System.out.println("2: View Flight Reservation");
            System.out.println("3: Exit\n");
            
            response = 0;
            while(response < 1 || response > 3) {
                System.out.print("> ");
                response = sc.nextInt();
                
                if (response == 1) {
                    if(currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.SALESMANAGER)) {
                        viewSeatsAvailability();
                    } else {
                        System.out.println("Invalid EmployeeAccessRight, make sure you're an Administrator or Sales Manager!");
                    }
                } else if (response == 2) {
                    if(currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.ADMINISTRATOR) || currentEmployee.getAccessRight().equals(EmployeeAccessRightEnum.SALESMANAGER)) {
                        viewFlightReservations();
                    } else {
                        System.out.println("Invalid EmployeeAccessRight, make sure you're an Administrator or Sales Manager!");
                    }
                } else if(response == 3) {
                    break;
                } else {
                    System.out.println("Invalid choice!!");
                }
                
            }
            if(response == 3) {
                break;
            }
        }
    }
    
    private void viewSeatsAvailability() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("====== viewSeatsAvailability ======");
            System.out.println("Enter your Flight Number please: "); //takes in both int and alphabets
            String flightNumber = sc.nextLine().trim();
            FlightEntity flight = flightSessionBean.retrieveFlightByFlightNumber(flightNumber);
            
            if (flight.getFlightSchedulePlan().isEmpty()) {
                System.out.println("THIS CHOSEN FLIGHT IS NOT AVAILABLE, TRY AGAIN");
                return;
            }
            
            System.out.println("View all flight schedule -> Flight Number: " + flightNumber + ", " + flight.getFlightRoute().getOrigin().getIATACode() + " travelling to " + flight.getFlightRoute().getDestination().getIATACode());
            
            System.out.printf("%25s%30s%20s\n", "Flight Schedule Id", "Departure Date Time", "Duration (HOURS)");
            
            for (FlightSchedulePlanEntity flightSchedulePlan : flight.getFlightSchedulePlan()) {
                for (FlightScheduleEntity flightSchedule : flightSchedulePlan.getFlightSchedule()) {
                    System.out.printf("%25s%30s%20s\n", flightSchedule.getFlightScheduleId().toString(), flightSchedule.getDepartureDateTime().toString().substring(0, 19), String.valueOf(flightSchedule.getDuration()));
                }
            }
            
            System.out.println("Please select your flight schedule (By Id): ");
            Long chosenFlightScheduleId = sc.nextLong();
            sc.nextLine();
            
            FlightScheduleEntity chosenFlightSchedule = null;
            
            for (FlightSchedulePlanEntity flightSchedulePlan: flight.getFlightSchedulePlan()) {
                for (FlightScheduleEntity flightSchedule: flightSchedulePlan.getFlightSchedule()) {
                    if (flightSchedule.getFlightScheduleId() == chosenFlightScheduleId) {
                                chosenFlightSchedule = flightSchedule;
                    }
                }
            }
            
            if (chosenFlightSchedule == null) {
                System.out.println("ERROR: Flight Schedule: " + chosenFlightScheduleId + " does not exist for flight " + flightNumber + "\n");
                return;
            }
            
            int totalAvailableSeats = 0;
            int totalReservedSeats = 0;
            int totalBalanceSeats = 0;
            
            for (SeatInventoryEntity seatInventory : chosenFlightSchedule.getSeatInventory()) {
                totalAvailableSeats += seatInventory.getAvailableSeats();
                totalReservedSeats += seatInventory.getReservedSeats();
                totalBalanceSeats += seatInventory.getBalanceSeats();
            
                char[][] totalSeats = seatInventory.getSeats();
                String cabinClassConfig = seatInventory.getCabin().getSeatingConfigPerColumn();
                String cabinClassName = "";
                if (null != seatInventory.getCabin().getCabinClassType()) {
                    switch (seatInventory.getCabin().getCabinClassType()) {
                        case F: 
                            cabinClassName = "First Class";
                            break;
                        case J:
                            cabinClassName = "Business Class";
                            break;
                        case W:
                            cabinClassName = "Premium Economy class";
                            break;
                        case Y:
                            cabinClassName = "Economy Class";
                            break;
                        default:
                            break;
                    }
                    
                    System.out.println("~~~ " + cabinClassName + "~~~ ");
                    System.out.print("Column - ");
                    int columnCount = 0;
                    int columnNumber = 0;
                    
                    for (int i = 0; i < cabinClassConfig.length(); i++) {
                        if (Character.isDigit(cabinClassConfig.charAt(i))) {
                            columnNumber += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
                            
                            while (columnCount < columnNumber) {
                                System.out.print((char)('A' + columnCount) + " ");
                                columnCount++;
                            }
                        } else {
                            System.out.print("na");
                        }
                    }
                    System.out.println();
                }
                
                for (int j = 0; j < totalSeats.length; j++) {
                    System.out.printf("%-8s", String.valueOf(j + 1));
                    int rowCount = 0;
                    int rowNumber = 0;
                    
                    for (int i = 0; i < cabinClassConfig.length(); i++) {
                        if (Character.isDigit(cabinClassConfig.charAt(i))) {
                            rowNumber += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));

                            while (rowCount < rowNumber) {
                                System.out.print(totalSeats[j][rowCount] + "  ");
                                rowCount++;
                            }
                        } else {
                            System.out.print("na");
                        }
                    }
                    
                    System.out.println();
                }
                
                System.out.println("\nNumber of available seats: " + seatInventory.getAvailableSeats());
                System.out.println("Number of reserved seats: " + seatInventory.getReservedSeats());
                System.out.println("Number of balance seats: " + seatInventory.getBalanceSeats() + "\n");
            }
            

            System.out.println("TOTAL Number of available seats: " + totalAvailableSeats);
            System.out.println("TOTAL Number of reserved seats: " + totalReservedSeats);
            System.out.println("TOTAL Number of balance seats: " + totalBalanceSeats);
            System.out.print("Press Enter key to continue. ");
            sc.nextLine();
        } catch (FlightNotFoundException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
    
    private void viewFlightReservations() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("====== View Flight Reservations ======");
            System.out.print("Enter/Input your flight number: ");
            String flightNumber = sc.nextLine().trim();
            FlightEntity flight = flightSessionBean.retrieveFlightByFlightNumber(flightNumber);
            
            if (flight.getFlightSchedulePlan().isEmpty()) {
                System.out.println("There is no flight schedule plans with this flight number.\n");
                return;
            }
            
            System.out.println("Displaying all flight schedules for Flight: " + flightNumber + ", " + flight.getFlightRoute().getOrigin().getIATACode() + " travelling to " + flight.getFlightRoute().getDestination().getIATACode());
            System.out.printf("%25s%30s%20s\n", "Flight Schedule ID", "Departure Date Time", "Duration (HRS)");
        
            System.out.print("Select flight schedule (By Id): ");
            Long chosenFlightScheduleId = sc.nextLong();
            sc.nextLine();
            
            FlightScheduleEntity chosenFlightSchedule = null;
            for (FlightSchedulePlanEntity fsp : flight.getFlightSchedulePlan()) {
                for (FlightScheduleEntity fs : fsp.getFlightSchedule()) {
                    if (fs.getFlightScheduleId().equals(chosenFlightScheduleId)) {
                        chosenFlightSchedule = fs;
                        break;
                    }
                }
                
                if (chosenFlightSchedule != null) {
                    break;
                }
            }
            
            if (chosenFlightSchedule == null) {
                System.out.println("Flight Schedule ID: " + chosenFlightScheduleId + " does not exist for flight for Flight Number:  " + flightNumber + "\n");
                return;
            }
            
            List<CabinClassTypeEnum> cabinTypes = new ArrayList<>();
            for (ReservationEntity reservation : chosenFlightSchedule.getReservations()) {
                if (!cabinTypes.contains(reservation.getCabinClass())) {
                    cabinTypes.add(reservation.getCabinClass());
                }
            }
        
            List<List<Pair<PassengerEntity, String>>> reservationsByCabin = new ArrayList<>();
            for (CabinClassTypeEnum cabinType : cabinTypes) {
                List<Pair<PassengerEntity, String>> cabinReservations = new ArrayList<>();

                for (ReservationEntity reservation : chosenFlightSchedule.getReservations()) {
                    if (reservation.getCabinClass() == cabinType) {
                        String fareBasisCode = reservation.getFareClassCode();
                        for (PassengerEntity passenger: reservation.getPassenger()) {
                            cabinReservations.add(new Pair<>(passenger, fareBasisCode));
                        }
                    }
                }
                reservationsByCabin.add(cabinReservations);
            }

        for (int i = 0; i < cabinTypes.size(); i++) {
            CabinClassTypeEnum cabinType = cabinTypes.get(i);
            String cabinClassName = cabinTypeToClassName(cabinType);

            System.out.println(" -- " + cabinClassName + " -- ");
            System.out.println();

            List<Pair<PassengerEntity, String>> cabinReservations = reservationsByCabin.get(i);
            Collections.sort(cabinReservations, (o1, o2) -> o1.getKey().getSeatNumber().compareTo(o2.getKey().getSeatNumber()));

            for (Pair<PassengerEntity, String> pair : cabinReservations) {
                PassengerEntity passenger = pair.getKey();
                String fareCode = pair.getValue();
                System.out.println(passenger.getFirstName() + " " + passenger.getLastName() + ", Seat " + passenger.getSeatNumber() + ", " + fareCode);
            }

            System.out.println();
        }

            System.out.print("Press Enter to continue: ");
            sc.nextLine();
        } catch (FlightNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
        }
    }
    
    private String cabinTypeToClassName(CabinClassTypeEnum cabinType) {
        switch (cabinType) {
            case F:
                return "First Class";
            case J:
                return "Business Class";
            case W:
                return "Premium Economy Class";
            case Y:
                return "Economy Class";
            default:
                return "Unknown Cabin Class";
        }
    }            
}
    
   
