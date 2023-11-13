/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import ejb.session.stateless.SeatsAvailabilitySessionBeanRemote;
import entity.EmployeeEntity;

/**
 *
 * @author kahjy
 */
public class MainApp {
   
    private AirportSessionBeanRemote airportSessionBean;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBean;
    private CabinClassSessionBeanRemote cabinClassSessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private SeatsAvailabilitySessionBeanRemote seatsAvailabilitySessionBean;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;
    private FlightSessionBeanRemote flightSessionBean;
    private FlightRouteSessionBeanRemote flightRouteSessionBean;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private FareSessionBeanRemote fareSessionBean;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBean;    
    
    private FlightRoutePlanningModule flightRoutePlanningModule;
    private FlightOperationModule flightOperationModule;
    private SalesAdminModule salesAdminModule;
    
    private boolean login = false;
    private EmployeeEntity currentEmployee;
    
    public MainApp(FlightScheduleSessionBeanRemote flightScheduleSessionBean, AirportSessionBeanRemote airportSessionBean, AircraftTypeSessionBeanRemote aircraftTypeSessionBean, CabinClassSessionBeanRemote cabinClassSessionBean, ReservationSessionBeanRemote reservationSessionBean, SeatsAvailabilitySessionBeanRemote seatsAvailabilitySessionBean, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean, FlightSessionBeanRemote flightSessionBean, FlightRouteSessionBeanRemote flightRouteSessionBean, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean, EmployeeSessionBeanRemote employeeSessionBean, FareSessionBeanRemote fareSessionBean) {
        this.airportSessionBean = airportSessionBean;
        this.aircraftTypeSessionBean = aircraftTypeSessionBean;
        this.cabinClassSessionBean = cabinClassSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.seatsAvailabilitySessionBean = seatsAvailabilitySessionBean;
        this.flightSchedulePlanSessionBean = flightSchedulePlanSessionBean;
        this.flightSessionBean = flightSessionBean;
        this.flightRouteSessionBean = flightRouteSessionBean;
        this.aircraftConfigurationSessionBean = aircraftConfigurationSessionBean;
        this.employeeSessionBean = employeeSessionBean;
        this.fareSessionBean = fareSessionBean;
        this.flightScheduleSessionBean = flightScheduleSessionBean;
    }
    
    
}
