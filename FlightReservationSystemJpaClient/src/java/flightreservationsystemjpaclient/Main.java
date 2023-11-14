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
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(itinerarySessionBean, reservationSessionBean, passengerSessionBean, fareSessionBean, seatsInventorySessionBean, flightRouteSessionBean, flightSessionBean,
                flightSchedulePlanSessionBean, flightScheduleSessionBean, airportSessionBean, customerSessionBean);
        mainApp.runApp();
    }
    
}
