/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
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
import exceptions.CustomerExistException;
import exceptions.InvalidLoginCredentialException;
import exceptions.UnknownPersistenceException;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author foozh
 */
public class Main {

     @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;
    
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBean;
    
    @EJB
    private static PartnerSessionBeanRemote partnerSessionBean;
    
    @EJB
    private static AircraftTypeSessionBeanRemote aircraftTypeSessionBean;
    
    @EJB
    private static AirportSessionBeanRemote airportSessionBean;
    
    @EJB
    private static AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean;
    
    @EJB
    private static FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;

    @EJB
    private static FlightSessionBeanRemote flightSessionBean;

    @EJB
    private static FlightRouteSessionBeanRemote flightRouteSessionBean;
    
    @EJB
    private static FareSessionBeanRemote fareSessionBean;

    @EJB
    private static SeatsAvailabilitySessionBeanRemote seatsInventorySessionBean;

    @EJB
    private static FlightScheduleSessionBeanRemote flightScheduleSessionBean;
    
    @EJB
    private static CustomerSessionBeanRemote customerSessionBean;
    
    @EJB
    private static ItinerarySessionBeanRemote itinerarySessionBean;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBean;
    
    @EJB
    private static PassengerSessionBeanRemote passengerSessionBean;
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownPersistenceException, CustomerExistException, InvalidLoginCredentialException {
        
        MainApp mainApp = new MainApp(employeeSessionBean, partnerSessionBean, aircraftTypeSessionBean, airportSessionBean, aircraftConfigurationSessionBean, flightSchedulePlanSessionBean, flightSessionBean,
             flightRouteSessionBean, fareSessionBean, seatsInventorySessionBean, flightScheduleSessionBean, customerSessionBean, itinerarySessionBean, reservationSessionBean, passengerSessionBean);
        mainApp.runApp();
    }
    
}
