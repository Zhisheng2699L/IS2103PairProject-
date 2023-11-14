/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.SeatsAvailabilitySessionBeanRemote;
import entity.EmployeeEntity;
import enumeration.EmployeeAccessRightEnum;
import java.util.Scanner;

/**
 *
 * @author kahjy
 */

//SALES ADMIN MODULE IS TO PROVIDE FUNCTIONALITY for viewing seats availability and flight reservations!!

public class SalesAdminModule {
    private EmployeeEntity currentEmployee;
    private SeatsAvailabilitySessionBeanRemote seatsAvailabilitySessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private FlightSessionBeanRemote flightSessionBean;
    
    public SalesAdminModule(EmployeeEntity currentEmployee, SeatsAvailabilitySessionBeanRemote seatsAvailabilitySessionBean, ReservationSessionBeanRemote reservationSessionBean, FlightSessionBeanRemote flightSessionBean) {
        this.currentEmployee = currentEmployee;
        this.seatsAvailabilitySessionBean = seatsAvailabilitySessionBean;
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
                    System.out.println("Invalid input, please try again!\n");
                }
                
            }
            if(response == 3) {
                break;
            }
        }
    }
    
    private void viewSeatsInventory() {
        
    }
}
