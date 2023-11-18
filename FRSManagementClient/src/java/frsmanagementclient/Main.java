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

    @EJB(name = "FlightSessionBeanRemote")
    private static FlightSessionBeanRemote flightSessionBean;
    
    @EJB(name = "FareSessionBeanRemote")
    private static FareSessionBeanRemote fareSessionBean;

    @EJB(name = "EmployeeSessionBeanRemote")
    private static EmployeeSessionBeanRemote employeeSessionBean;

    @EJB(name = "AircraftConfigurationSessionBeanRemote")
    private static AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean;

    @EJB(name = "FlightRouteSessionBeanRemote")
    private static FlightRouteSessionBeanRemote flightRouteSessionBean;

    @EJB(name = "FlightSchedulePlanSessionBeanRemote")
    private static FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;

    @EJB(name = "SeatsInventorySessionBeanRemote")
    private static SeatsInventorySessionBeanRemote seatsInventorySessionBean;

    @EJB(name = "ReservationSessionBeanRemote")
    private static ReservationSessionBeanRemote reservationSessionBean;

    @EJB(name = "CabinClassSessionBeanRemote")
    private static CabinClassSessionBeanRemote cabinClassSessionBean;

    @EJB(name = "AircraftTypeSessionBeanRemote")
    private static AircraftTypeSessionBeanRemote aircraftTypeSessionBean;

    @EJB(name = "AirportSessionBeanRemote")
    private static AirportSessionBeanRemote airportSessionBean;

    @EJB(name = "FlightScheduleSessionBeanRemote")
    private static FlightScheduleSessionBeanRemote flightScheduleSessionBean;   

    public static void main(String[] args) throws UnknownPersistenceException, AirportDoNotExistException, ExistingAircraftConfigException, CreateNewAircraftConfigErrorException, ExistingFlightException, FlightNotFoundException, ViolationConstraintsException, ParseException, InvalidCostException, AircraftConfigNotFoundException, FlightExistException, InputDataValidationException, FlightRouteDoNotExistException, InvalidLoginDetailsException {
        MainApp mainApp = new MainApp(flightScheduleSessionBean, airportSessionBean, aircraftTypeSessionBean, cabinClassSessionBean,reservationSessionBean,
                seatsInventorySessionBean, flightSchedulePlanSessionBean, flightSessionBean, flightRouteSessionBean, 
                aircraftConfigurationSessionBean, employeeSessionBean, fareSessionBean);
        mainApp.runApp();
    }
    /*FlightScheduleSessionBeanRemote flightScheduleSessionBean, AirportSessionBeanRemote airportSessionBean, 
    AircraftTypeSessionBeanRemote aircraftTypeSessionBean, CabinClassSessionBeanRemote cabinClassSessionBean, 
ReservationSessionBeanRemote reservationSessionBean, SeatsAvailabilitySessionBeanRemote seatsAvailabilitySessionBean, 
FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean, FlightSessionBeanRemote flightSessionBean, 
FlightRouteSessionBeanRemote flightRouteSessionBean, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean, 
EmployeeSessionBeanRemote employeeSessionBean, FareSessionBeanRemote fareSessionBean*/
    
}
