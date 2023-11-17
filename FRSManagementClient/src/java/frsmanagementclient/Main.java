/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CabinClassSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.SeatsInventorySessionBeanRemote;
import exceptions.AircraftConfigNotFoundException;
import exceptions.AirportDoNotExistException;
import exceptions.CreateNewAircraftConfigErrorException;
import exceptions.ExistingAircraftConfigException;
import exceptions.ExistingFlightException;
import exceptions.FlightExistException;
import exceptions.FlightNotFoundException;
import exceptions.FlightRouteDoNotExistException;
import exceptions.InputDataValidationException;
import exceptions.InvalidCostException;
import exceptions.InvalidLoginDetailsException;
import exceptions.UnknownPersistenceException;
import exceptions.ViolationConstraintsException;
import java.text.ParseException;
import javax.ejb.EJB;

/**
 *
 * @author foozh
 */
public class Main {

    @EJB
    private static  SeatsInventorySessionBeanRemote seatsInventorySessionBeanRemote;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;

    @EJB
    private static FlightSessionBeanRemote flightSessionBeanRemote;

    @EJB
    private static FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;

    @EJB
    private static FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;

    @EJB
    private static FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;

    @EJB
    private static FareSessionBeanRemote fareSessionBeanRemote;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;

    @EJB
    private static CabinClassSessionBeanRemote cabinClassSessionBeanRemote;

    @EJB
    private static AirportSessionBeanRemote airportSessionBeanRemote;

    @EJB
    private static AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;

    @EJB
    private static AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;

    


    public static void main(String[] args) throws UnknownPersistenceException, FlightExistException, AirportDoNotExistException, ExistingAircraftConfigException, CreateNewAircraftConfigErrorException, ExistingFlightException, ViolationConstraintsException, FlightNotFoundException, ParseException, InvalidCostException, AircraftConfigNotFoundException, FlightExistException, FlightRouteDoNotExistException, InputDataValidationException, InvalidLoginDetailsException {
        MainApp mainApp = new MainApp(flightScheduleSessionBeanRemote, airportSessionBeanRemote, aircraftTypeSessionBeanRemote, cabinClassSessionBeanRemote,
                reservationSessionBeanRemote,seatsInventorySessionBeanRemote, flightSchedulePlanSessionBeanRemote, flightSessionBeanRemote, 
                flightRouteSessionBeanRemote, aircraftConfigurationSessionBeanRemote, employeeSessionBeanRemote, fareSessionBeanRemote);
        mainApp.runApp();
    }
    /*FlightScheduleSessionBeanRemote flightScheduleSessionBean, AirportSessionBeanRemote airportSessionBean, 
    AircraftTypeSessionBeanRemote aircraftTypeSessionBean, CabinClassSessionBeanRemote cabinClassSessionBean, 
ReservationSessionBeanRemote reservationSessionBean, SeatsAvailabilitySessionBeanRemote seatsAvailabilitySessionBean, 
FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean, FlightSessionBeanRemote flightSessionBean, 
FlightRouteSessionBeanRemote flightRouteSessionBean, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean, 
EmployeeSessionBeanRemote employeeSessionBean, FareSessionBeanRemote fareSessionBean*/
    
}
