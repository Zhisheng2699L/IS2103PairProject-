/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftConfigurationSessionBeanLocal;
import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.FlightRouteSessionBeanLocal;
import ejb.session.stateless.FlightSchedulePlanSessionBeanLocal;
import ejb.session.stateless.FlightSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.AircraftConfigurationEntity;
import entity.AircraftTypeEntity;
import entity.AirportEntity;
import entity.CabinClassEntity;
import entity.EmployeeEntity;
import entity.FareEntity;
import entity.FlightEntity;
import entity.FlightRouteEntity;
import entity.FlightScheduleEntity;
import entity.FlightSchedulePlanEntity;
import entity.PartnerEntity;
import enumeration.CabinClassTypeEnum;
import enumeration.EmployeeAccessRightEnum;
import enumeration.ScheduleTypeEnum;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 *
 * @author foozh
 */
@LocalBean
@Startup
@Singleton
public class TestInitSessionBean implements TestInitSessionBeanRemote, TestInitSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;
    
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    
    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;
    
    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBean;
    
    @EJB
    private AirportSessionBeanLocal airportSessionBean;
    
    @EJB
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBean;
    
    @EJB
    private FlightSchedulePlanSessionBeanLocal flightSchedulePlanSessionBean;

    @EJB
    private FlightSessionBeanLocal flightSessionBean;

    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBean;
    
    @PostConstruct
    public void postConstruct() {

        if (em.find(EmployeeEntity.class, 1l) == null) {
            doDataInitialisation();
        }
    }
    
    private void doDataInitialisation() {
    
         try {
             
             //Employees
             employeeSessionBean.createNewEmployee(new EmployeeEntity("Fleet", "Manager", "fleetmanager", "password", EmployeeAccessRightEnum.FLEETMANAGER));
             employeeSessionBean.createNewEmployee(new EmployeeEntity("Route", "Planner", "routeplanner", "password", EmployeeAccessRightEnum.ROUTEPLANNER));
             employeeSessionBean.createNewEmployee(new EmployeeEntity("Schedule", "Manager", "schedulemanager", "password", EmployeeAccessRightEnum.SCHEDULEMANAGER));
             employeeSessionBean.createNewEmployee(new EmployeeEntity("Sales", "Manager", "salesmanager", "password", EmployeeAccessRightEnum.SALESMANAGER));
         
             //Partner
             partnerSessionBean.createNewPartner(new PartnerEntity("Holiday.com", "holidaydotcom", "password"));
             
             //Airport
             airportSessionBean.createNewAirport(new AirportEntity("Changi", "SIN", "Singapore", "Singapore", "Singapore", 8));
             airportSessionBean.createNewAirport(new AirportEntity("Hong Kong", "HKG", "Chek Lap Kok", "Hong Kong", "China", 8));
             airportSessionBean.createNewAirport(new AirportEntity("Taoyuan", "TPE", "Taoyuan", "Taipei", "Taiwan R.O.C.", 8));
             airportSessionBean.createNewAirport(new AirportEntity("Narita", "NRT", "Narita", "Chiba", "Japan", 9));
             airportSessionBean.createNewAirport(new AirportEntity("Sydney", "SYD", "Sydney", "New South Wales", "Australia", 11));
             
            //Aircraft Type
            aircraftTypeSessionBean.createNewAircraftType(new AircraftTypeEntity("Boeing 737", 200));
            aircraftTypeSessionBean.createNewAircraftType(new AircraftTypeEntity("Boeing 747", 400));
            
            //Aircraft Config
            List<CabinClassEntity> cabinClassList = new ArrayList<>();
            CabinClassEntity cabin = new CabinClassEntity(CabinClassTypeEnum.Y, 1, 30, 6, "3-3", 180);
            AircraftConfigurationEntity aircraftConfig = new AircraftConfigurationEntity("Boeing 737 All Economy", 1);
            cabinClassList.add(cabin);
            aircraftConfigurationSessionBean.createNewAircraftConfig(aircraftConfig, 1l, cabinClassList);

            cabinClassList.clear();
            cabin = new CabinClassEntity(CabinClassTypeEnum.F, 1, 5, 2, "1-1", 10);
            cabinClassList.add(cabin);
            cabin = new CabinClassEntity(CabinClassTypeEnum.J, 1, 5, 4, "2-2", 20);
            cabinClassList.add(cabin);
            cabin = new CabinClassEntity(CabinClassTypeEnum.Y, 1, 25, 6, "3-3", 150);
            cabinClassList.add(cabin);
            aircraftConfig = new AircraftConfigurationEntity("Boeing 737 Three Classes", 3);
            aircraftConfigurationSessionBean.createNewAircraftConfig(aircraftConfig, 1l, cabinClassList);

            cabinClassList.clear();
            cabin = new CabinClassEntity(CabinClassTypeEnum.Y, 2, 38, 10, "3-4-3", 380);
            cabinClassList.add(cabin);
            aircraftConfig = new AircraftConfigurationEntity("Boeing 747 All Economy", 1);
            aircraftConfigurationSessionBean.createNewAircraftConfig(aircraftConfig, 2l, cabinClassList);

            cabinClassList.clear();
            cabin = new CabinClassEntity(CabinClassTypeEnum.F, 1, 5, 2, "1-1", 10);
            cabinClassList.add(cabin);
            cabin = new CabinClassEntity(CabinClassTypeEnum.J, 2, 5, 6, "2-2-2", 30);
            cabinClassList.add(cabin);
            cabin = new CabinClassEntity(CabinClassTypeEnum.Y, 2, 32, 10, "3-4-3", 320);
            cabinClassList.add(cabin);
            aircraftConfig = new AircraftConfigurationEntity("Boeing 747 Three Classes", 3);
            aircraftConfigurationSessionBean.createNewAircraftConfig(aircraftConfig, 2l, cabinClassList);
            
            //Flight Route
            FlightRouteEntity source = new FlightRouteEntity(airportSessionBean.retrieveAirportByIATA("SIN"), airportSessionBean.retrieveAirportByIATA("HKG"));
            source = flightRouteSessionBean.createNewFlightRoute(source, airportSessionBean.retrieveAirportByIATA("SIN").getAirportID(), airportSessionBean.retrieveAirportByIATA("HKG").getAirportID());
            FlightRouteEntity complimentary = new FlightRouteEntity(airportSessionBean.retrieveAirportByIATA("HKG"), airportSessionBean.retrieveAirportByIATA("SIN"));
            flightRouteSessionBean.setComplementaryFlightRoute(source.getFlightRouteId());
            
            source =  flightRouteSessionBean.createNewFlightRoute(new FlightRouteEntity(), airportSessionBean.retrieveAirportByIATA("SIN").getAirportID(), airportSessionBean.retrieveAirportByIATA("TPE").getAirportID());
            complimentary = flightRouteSessionBean.createNewFlightRoute(new FlightRouteEntity(), airportSessionBean.retrieveAirportByIATA("TPE").getAirportID(), airportSessionBean.retrieveAirportByIATA("SIN").getAirportID());
            flightRouteSessionBean.setComplementaryFlightRoute(source.getFlightRouteId());
            
            source =  flightRouteSessionBean.createNewFlightRoute(new FlightRouteEntity(), airportSessionBean.retrieveAirportByIATA("SIN").getAirportID(), airportSessionBean.retrieveAirportByIATA("NRT").getAirportID());
            complimentary = flightRouteSessionBean.createNewFlightRoute(new FlightRouteEntity(), airportSessionBean.retrieveAirportByIATA("NRT").getAirportID(), airportSessionBean.retrieveAirportByIATA("SIN").getAirportID());
            flightRouteSessionBean.setComplementaryFlightRoute(source.getFlightRouteId());
            
            source =  flightRouteSessionBean.createNewFlightRoute(new FlightRouteEntity(), airportSessionBean.retrieveAirportByIATA("HKG").getAirportID(), airportSessionBean.retrieveAirportByIATA("NRT").getAirportID());
            complimentary = flightRouteSessionBean.createNewFlightRoute(new FlightRouteEntity(), airportSessionBean.retrieveAirportByIATA("NRT").getAirportID(), airportSessionBean.retrieveAirportByIATA("HKG").getAirportID());
            flightRouteSessionBean.setComplementaryFlightRoute(source.getFlightRouteId());
             
             //Flight
             FlightEntity flight = new FlightEntity("ML111");
             FlightRouteEntity route = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("SIN", "HKG");
             AircraftConfigurationEntity configs = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 Three Classes");
             FlightEntity flightReturn = new FlightEntity("ML112");
             FlightRouteEntity routeReturn = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("HKG", "SIN");
             AircraftConfigurationEntity configsReturn = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 Three Classes");
             flightSessionBean.createNewFlight(flight, route.getFlightRouteId(), configs.getAircraftConfigId());
             flightSessionBean.createNewFlight(flightReturn, routeReturn.getFlightRouteId(), configsReturn.getAircraftConfigId());
             flightSessionBean.associateExistingFlightWithReturnFlight(flight.getFlightId(), flightReturn.getFlightId());

             flight = new FlightEntity("ML211");
             route = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("SIN", "TPE");
             configs = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 Three Classes");
             flightReturn = new FlightEntity("ML212");
             routeReturn = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("TPE", "SIN");
             configsReturn = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 Three Classes");
             flightSessionBean.createNewFlight(flight, route.getFlightRouteId(), configs.getAircraftConfigId());
             flightSessionBean.createNewFlight(flightReturn, routeReturn.getFlightRouteId(), configsReturn.getAircraftConfigId());
             flightSessionBean.associateExistingFlightWithReturnFlight(flight.getFlightId(), flightReturn.getFlightId());

             flight = new FlightEntity("ML311");
             route = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("SIN", "NRT");
             configs = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 747 Three Classes");
             flightReturn = new FlightEntity("ML312");
             routeReturn = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("NRT", "SIN");
             configsReturn = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 747 Three Classes");
             flightSessionBean.createNewFlight(flight, route.getFlightRouteId(), configs.getAircraftConfigId());
             flightSessionBean.createNewFlight(flightReturn, routeReturn.getFlightRouteId(), configsReturn.getAircraftConfigId());
             flightSessionBean.associateExistingFlightWithReturnFlight(flight.getFlightId(), flightReturn.getFlightId());

             flight = new FlightEntity("ML411");
             route = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("HKG", "NRT");
             configs = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 Three Classes");
             flightReturn = new FlightEntity("ML412");
             routeReturn = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("NRT", "HKG");
             configsReturn = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 Three Classes");
             flightSessionBean.createNewFlight(flight, route.getFlightRouteId(), configs.getAircraftConfigId());
             flightSessionBean.createNewFlight(flightReturn, routeReturn.getFlightRouteId(), configsReturn.getAircraftConfigId());
             flightSessionBean.associateExistingFlightWithReturnFlight(flight.getFlightId(), flightReturn.getFlightId());

             flight = new FlightEntity("ML511");
             route = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("TPE", "NRT");
             configs = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 Three Classes");
             flightReturn = new FlightEntity("ML512");
             routeReturn = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("NRT", "TPE");
             configsReturn = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 Three Classes");
             flightSessionBean.createNewFlight(flight, route.getFlightRouteId(), configs.getAircraftConfigId());
             flightSessionBean.createNewFlight(flightReturn, routeReturn.getFlightRouteId(), configsReturn.getAircraftConfigId());
             flightSessionBean.associateExistingFlightWithReturnFlight(flight.getFlightId(), flightReturn.getFlightId());

             flight = new FlightEntity("ML611");
             route = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("SIN", "SYD");
             configs = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 Three Classes");
             flightReturn = new FlightEntity("ML612");
             routeReturn = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("SYD", "SIN");
             configsReturn = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 Three Classes");
             flightSessionBean.createNewFlight(flight, route.getFlightRouteId(), configs.getAircraftConfigId());
             flightSessionBean.createNewFlight(flightReturn, routeReturn.getFlightRouteId(), configsReturn.getAircraftConfigId());
             flightSessionBean.associateExistingFlightWithReturnFlight(flight.getFlightId(), flightReturn.getFlightId());

             flight = new FlightEntity("ML621");
             route = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("SIN", "SYD");
             configs = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 All Economy");
             flightReturn = new FlightEntity("ML622");
             routeReturn = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("SYD", "SIN");
             configsReturn = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 737 All Economy");
             flightSessionBean.createNewFlight(flight, route.getFlightRouteId(), configs.getAircraftConfigId());
             flightSessionBean.createNewFlight(flightReturn, routeReturn.getFlightRouteId(), configsReturn.getAircraftConfigId());
             flightSessionBean.associateExistingFlightWithReturnFlight(flight.getFlightId(), flightReturn.getFlightId());

             flight = new FlightEntity("ML711");
             route = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("SYD", "NRT");
             configs = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 747 Three Classes");
             flightReturn = new FlightEntity("ML712");
             routeReturn = flightRouteSessionBean.searchForFlightRouteByOriginAndDestination("NRT", "SYD");
             configsReturn = aircraftConfigurationSessionBean.retrieveAircraftConfigByName("Boeing 747 Three Classes");
             flightSessionBean.createNewFlight(flight, route.getFlightRouteId(), configs.getAircraftConfigId());
             flightSessionBean.createNewFlight(flightReturn, routeReturn.getFlightRouteId(), configsReturn.getAircraftConfigId());
             flightSessionBean.associateExistingFlightWithReturnFlight(flight.getFlightId(), flightReturn.getFlightId());
             
             //Flight Schedule Plan
             SimpleDateFormat recurrentFormat = new SimpleDateFormat("dd/M/yyyy");
             SimpleDateFormat scheduleFormatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss a");

             Date recurrentEnd = recurrentFormat.parse("31/12/2020");
             FlightEntity flight1 = flightSessionBean.retrieveFlightByFlightNumber("ML711");
             Date startDateTime = scheduleFormatter.parse("1/12/2020 9:00:00 AM");
             Pair<Date, Double> pair = new Pair<>(startDateTime, 14.0);
             FlightSchedulePlanEntity flightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, flight1);

             List<FareEntity> fares = new ArrayList<>();
             fares.add(new FareEntity("F001", BigDecimal.valueOf(6500), CabinClassTypeEnum.F));
             fares.add(new FareEntity("F002", BigDecimal.valueOf(6000), CabinClassTypeEnum.F));
             fares.add(new FareEntity("J001", BigDecimal.valueOf(3500), CabinClassTypeEnum.J));
             fares.add(new FareEntity("J002", BigDecimal.valueOf(3000), CabinClassTypeEnum.J));
             fares.add(new FareEntity("Y001", BigDecimal.valueOf(1500), CabinClassTypeEnum.Y));
             fares.add(new FareEntity("Y002", BigDecimal.valueOf(1000), CabinClassTypeEnum.Y));

             flightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanWeekly(flightSchedulePlan, fares, flight1.getFlightId(), pair, 2);
             
             List<Pair<Date, Double>> info = new ArrayList<>();
             FlightEntity flight2 = flightSessionBean.retrieveFlightByFlightNumber("ML712");
             FlightSchedulePlanEntity returnFlightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, flight2);
             int diff = flight1.getFlightRoute().getDestination().getGmt() - flight1.getFlightRoute().getOrigin().getGmt();
             for (FlightScheduleEntity fs : flightSchedulePlan.getFlightSchedule()) {
                 Calendar c = Calendar.getInstance();
                 c.setTime(fs.getDepartureDateTime());
                 double duration = fs.getDuration();
                 int hour = (int) duration;
                 int min = (int) (duration % 1 * 60);
                 c.add(Calendar.HOUR_OF_DAY, hour);
                 c.add(Calendar.MINUTE, min);
                 c.add(Calendar.HOUR_OF_DAY, 2 + diff);
                 Date date = c.getTime();
                 info.add(new Pair<>(date, fs.getDuration()));
             }

             List<FareEntity> fares2 = new ArrayList<>();
             fares2.add(new FareEntity("F001", BigDecimal.valueOf(6500), CabinClassTypeEnum.F));
             fares2.add(new FareEntity("F002", BigDecimal.valueOf(6000), CabinClassTypeEnum.F));
             fares2.add(new FareEntity("J001", BigDecimal.valueOf(3500), CabinClassTypeEnum.J));
             fares2.add(new FareEntity("J002", BigDecimal.valueOf(3000), CabinClassTypeEnum.J));
             fares2.add(new FareEntity("Y001", BigDecimal.valueOf(1500), CabinClassTypeEnum.Y));
             fares2.add(new FareEntity("Y002", BigDecimal.valueOf(1000), CabinClassTypeEnum.Y));

             returnFlightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanMultiple(returnFlightSchedulePlan, info, fares2, flight2.getFlightId());
             flightSchedulePlanSessionBean.associateExistingPlanToComplementaryPlan(flightSchedulePlan.getFlightSchedulePlanId(), returnFlightSchedulePlan.getFlightSchedulePlanId());
             
            recurrentEnd = recurrentFormat.parse("31/12/2020");
            flight1 = flightSessionBean.retrieveFlightByFlightNumber("ML611");
            startDateTime = scheduleFormatter.parse("1/12/2020 12:00:00 PM");
            pair = new Pair<>(startDateTime, 8.0);
            flightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, flight1);
            
            fares.clear();
            fares.add(new FareEntity("F001", BigDecimal.valueOf(3250), CabinClassTypeEnum.F));
            fares.add(new FareEntity("F002", BigDecimal.valueOf(3000), CabinClassTypeEnum.F));
            fares.add(new FareEntity("J001", BigDecimal.valueOf(1750), CabinClassTypeEnum.J));
            fares.add(new FareEntity("J002", BigDecimal.valueOf(1500), CabinClassTypeEnum.J));
            fares.add(new FareEntity("Y001", BigDecimal.valueOf(750), CabinClassTypeEnum.Y));
            fares.add(new FareEntity("Y002", BigDecimal.valueOf(500), CabinClassTypeEnum.Y));
            
            flightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanWeekly(flightSchedulePlan, fares, flight1.getFlightId(), pair, 1);
            flight2 = flightSessionBean.retrieveFlightByFlightNumber("ML612");
            returnFlightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, flight2);
            info = new ArrayList<>();
            diff = flight1.getFlightRoute().getDestination().getGmt() - flight1.getFlightRoute().getOrigin().getGmt();
            for(FlightScheduleEntity fs : flightSchedulePlan.getFlightSchedule()) {
                Calendar c = Calendar.getInstance();
                c.setTime(fs.getDepartureDateTime());
                double duration = fs.getDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                c.add(Calendar.HOUR_OF_DAY, hour);
                c.add(Calendar.MINUTE, min);
                c.add(Calendar.HOUR_OF_DAY, 2 + diff);
                Date date = c.getTime();
                info.add(new Pair<>(date, fs.getDuration()));
            }
            
            fares2.clear();
            fares2.add(new FareEntity("F001", BigDecimal.valueOf(3250), CabinClassTypeEnum.F));
            fares2.add(new FareEntity("F002", BigDecimal.valueOf(3000), CabinClassTypeEnum.F));
            fares2.add(new FareEntity("J001", BigDecimal.valueOf(1750), CabinClassTypeEnum.J));
            fares2.add(new FareEntity("J002", BigDecimal.valueOf(1500), CabinClassTypeEnum.J));
            fares2.add(new FareEntity("Y001", BigDecimal.valueOf(750), CabinClassTypeEnum.Y));
            fares2.add(new FareEntity("Y002", BigDecimal.valueOf(500), CabinClassTypeEnum.Y));
                     
            returnFlightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanMultiple(returnFlightSchedulePlan, info, fares2, flight2.getFlightId());
            flightSchedulePlanSessionBean.associateExistingPlanToComplementaryPlan(flightSchedulePlan.getFlightSchedulePlanId(), returnFlightSchedulePlan.getFlightSchedulePlanId());
            
             recurrentEnd = recurrentFormat.parse("31/12/2020");
             flight1 = flightSessionBean.retrieveFlightByFlightNumber("ML621");
             startDateTime = scheduleFormatter.parse("1/12/2020 10:00:00 AM");
             pair = new Pair<>(startDateTime, 8.0);
             flightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, flight1);

             fares.clear();
             fares.add(new FareEntity("Y001", BigDecimal.valueOf(700), CabinClassTypeEnum.Y));
             fares.add(new FareEntity("Y002", BigDecimal.valueOf(400), CabinClassTypeEnum.Y));

             flightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanWeekly(flightSchedulePlan, fares, flight1.getFlightId(), pair, 3);
             flight2 = flightSessionBean.retrieveFlightByFlightNumber("ML622");
             returnFlightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, flight2);
             info = new ArrayList<>();
             diff = flight1.getFlightRoute().getDestination().getGmt() - flight1.getFlightRoute().getOrigin().getGmt();
             for (FlightScheduleEntity fs : flightSchedulePlan.getFlightSchedule()) {
                 Calendar c = Calendar.getInstance();
                 c.setTime(fs.getDepartureDateTime());
                 double duration = fs.getDuration();
                 int hour = (int) duration;
                 int min = (int) (duration % 1 * 60);
                 c.add(Calendar.HOUR_OF_DAY, hour);
                 c.add(Calendar.MINUTE, min);
                 c.add(Calendar.HOUR_OF_DAY, 2 + diff);
                 Date date = c.getTime();
                 info.add(new Pair<>(date, fs.getDuration()));
             }

             fares2.clear();
             fares2.add(new FareEntity("Y001", BigDecimal.valueOf(700), CabinClassTypeEnum.Y));
             fares2.add(new FareEntity("Y002", BigDecimal.valueOf(400), CabinClassTypeEnum.Y));

             returnFlightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanMultiple(returnFlightSchedulePlan, info, fares2, flight2.getFlightId());
             flightSchedulePlanSessionBean.associateExistingPlanToComplementaryPlan(flightSchedulePlan.getFlightSchedulePlanId(), returnFlightSchedulePlan.getFlightSchedulePlanId());
            
             
             recurrentEnd = recurrentFormat.parse("31/12/2020");
             flight1 = flightSessionBean.retrieveFlightByFlightNumber("ML311");
             startDateTime = scheduleFormatter.parse("1/12/2020 10:00:00 AM");
             pair = new Pair<>(startDateTime, 6.5);
             flightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, flight1);

             fares.clear();
             fares.add(new FareEntity("F001", BigDecimal.valueOf(3350), CabinClassTypeEnum.F));
             fares.add(new FareEntity("F002", BigDecimal.valueOf(3100), CabinClassTypeEnum.F));
             fares.add(new FareEntity("J001", BigDecimal.valueOf(1850), CabinClassTypeEnum.J));
             fares.add(new FareEntity("J002", BigDecimal.valueOf(1600), CabinClassTypeEnum.J));
             fares.add(new FareEntity("Y001", BigDecimal.valueOf(850), CabinClassTypeEnum.Y));
             fares.add(new FareEntity("Y002", BigDecimal.valueOf(600), CabinClassTypeEnum.Y));

             flightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanWeekly(flightSchedulePlan, fares, flight1.getFlightId(), pair, 2);
             flight2 = flightSessionBean.retrieveFlightByFlightNumber("ML312");
             returnFlightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.RECURRENTWEEK, recurrentEnd, flight2);
             info = new ArrayList<>();
             diff = flight1.getFlightRoute().getDestination().getGmt() - flight1.getFlightRoute().getOrigin().getGmt();
             for (FlightScheduleEntity fs : flightSchedulePlan.getFlightSchedule()) {
                 Calendar c = Calendar.getInstance();
                 c.setTime(fs.getDepartureDateTime());
                 double duration = fs.getDuration();
                 int hour = (int) duration;
                 int min = (int) (duration % 1 * 60);
                 c.add(Calendar.HOUR_OF_DAY, hour);
                 c.add(Calendar.MINUTE, min);
                 c.add(Calendar.HOUR_OF_DAY, 3 + diff);
                 Date date = c.getTime();
                 info.add(new Pair<>(date, fs.getDuration()));
             }

             fares2.clear();
             fares2.add(new FareEntity("F001", BigDecimal.valueOf(3350), CabinClassTypeEnum.F));
             fares2.add(new FareEntity("F002", BigDecimal.valueOf(3100), CabinClassTypeEnum.F));
             fares2.add(new FareEntity("J001", BigDecimal.valueOf(1850), CabinClassTypeEnum.J));
             fares2.add(new FareEntity("J002", BigDecimal.valueOf(1600), CabinClassTypeEnum.J));
             fares2.add(new FareEntity("Y001", BigDecimal.valueOf(850), CabinClassTypeEnum.Y));
             fares2.add(new FareEntity("Y002", BigDecimal.valueOf(600), CabinClassTypeEnum.Y));

             returnFlightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanMultiple(returnFlightSchedulePlan, info , fares2, flight2.getFlightId());
             flightSchedulePlanSessionBean.associateExistingPlanToComplementaryPlan(flightSchedulePlan.getFlightSchedulePlanId(), returnFlightSchedulePlan.getFlightSchedulePlanId());
             
             
             recurrentEnd = recurrentFormat.parse("31/12/2020");
             flight1 = flightSessionBean.retrieveFlightByFlightNumber("ML411");
             startDateTime = scheduleFormatter.parse("1/12/2020 1:00:00 PM");
             pair = new Pair<>(startDateTime, 4.0);
             flightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.RECURRENTDAY, recurrentEnd, flight1);

             fares.clear();
             fares.add(new FareEntity("F001", BigDecimal.valueOf(3150), CabinClassTypeEnum.F));
             fares.add(new FareEntity("F002", BigDecimal.valueOf(2900), CabinClassTypeEnum.F));
             fares.add(new FareEntity("J001", BigDecimal.valueOf(1650), CabinClassTypeEnum.J));
             fares.add(new FareEntity("J002", BigDecimal.valueOf(1400), CabinClassTypeEnum.J));
             fares.add(new FareEntity("Y001", BigDecimal.valueOf(650), CabinClassTypeEnum.Y));
             fares.add(new FareEntity("Y002", BigDecimal.valueOf(400), CabinClassTypeEnum.Y));

             flightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlan(flightSchedulePlan, fares, flight1.getFlightId(), pair, 2);
             flight2 = flightSessionBean.retrieveFlightByFlightNumber("ML412");
             returnFlightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.RECURRENTDAY, recurrentEnd, flight2);
             info = new ArrayList<>();
             diff = flight1.getFlightRoute().getDestination().getGmt() - flight1.getFlightRoute().getOrigin().getGmt();
             for (FlightScheduleEntity fs : flightSchedulePlan.getFlightSchedule()) {
                 Calendar c = Calendar.getInstance();
                 c.setTime(fs.getDepartureDateTime());
                 double duration = fs.getDuration();
                 int hour = (int) duration;
                 int min = (int) (duration % 1 * 60);
                 c.add(Calendar.HOUR_OF_DAY, hour);
                 c.add(Calendar.MINUTE, min);
                 c.add(Calendar.HOUR_OF_DAY, 4 + diff);
                 Date date = c.getTime();
                 info.add(new Pair<>(date, fs.getDuration()));
             }

             fares2.clear();
             fares2.add(new FareEntity("F001", BigDecimal.valueOf(3150), CabinClassTypeEnum.F));
             fares2.add(new FareEntity("F002", BigDecimal.valueOf(2900), CabinClassTypeEnum.F));
             fares2.add(new FareEntity("J001", BigDecimal.valueOf(1650), CabinClassTypeEnum.J));
             fares2.add(new FareEntity("J002", BigDecimal.valueOf(1400), CabinClassTypeEnum.J));
             fares2.add(new FareEntity("Y001", BigDecimal.valueOf(650), CabinClassTypeEnum.Y));
             fares2.add(new FareEntity("Y002", BigDecimal.valueOf(400), CabinClassTypeEnum.Y));

             returnFlightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanMultiple(returnFlightSchedulePlan,info , fares2, flight2.getFlightId());
             flightSchedulePlanSessionBean.associateExistingPlanToComplementaryPlan(flightSchedulePlan.getFlightSchedulePlanId(), returnFlightSchedulePlan.getFlightSchedulePlanId());
           
             
             flight1 = flightSessionBean.retrieveFlightByFlightNumber("ML511");
             Date startDateTime1 = scheduleFormatter.parse("7/12/2020 5:00:00 PM");
             Date startDateTime2 = scheduleFormatter.parse("8/12/2020 5:00:00 PM");
             Date startDateTime3 = scheduleFormatter.parse("9/12/2020 5:00:00 PM");
             List<Pair<Date, Double>> list1 = new ArrayList<>();
             list1.add(new Pair<>(startDateTime1, 3.0));
             list1.add(new Pair<>(startDateTime2, 3.0));
             list1.add(new Pair<>(startDateTime3, 3.0));
             flightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.MULTIPLE, flight1);

             fares.clear();
             fares.add(new FareEntity("F001", BigDecimal.valueOf(3100), CabinClassTypeEnum.F));
             fares.add(new FareEntity("F002", BigDecimal.valueOf(2850), CabinClassTypeEnum.F));
             fares.add(new FareEntity("J001", BigDecimal.valueOf(1600), CabinClassTypeEnum.J));
             fares.add(new FareEntity("J002", BigDecimal.valueOf(1350), CabinClassTypeEnum.J));
             fares.add(new FareEntity("Y001", BigDecimal.valueOf(600), CabinClassTypeEnum.Y));
             fares.add(new FareEntity("Y002", BigDecimal.valueOf(350), CabinClassTypeEnum.Y));

             flightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanMultiple(flightSchedulePlan,info, fares, flight1.getFlightId());
             flight2 = flightSessionBean.retrieveFlightByFlightNumber("ML512");
             returnFlightSchedulePlan = new FlightSchedulePlanEntity(ScheduleTypeEnum.MULTIPLE, flight2);
             info = new ArrayList<>();
             diff = flight1.getFlightRoute().getDestination().getGmt() - flight1.getFlightRoute().getOrigin().getGmt();
             for (FlightScheduleEntity fs : flightSchedulePlan.getFlightSchedule()) {
                 Calendar c = Calendar.getInstance();
                 c.setTime(fs.getDepartureDateTime());
                 double duration = fs.getDuration();
                 int hour = (int) duration;
                 int min = (int) (duration % 1 * 60);
                 c.add(Calendar.HOUR_OF_DAY, hour);
                 c.add(Calendar.MINUTE, min);
                 c.add(Calendar.HOUR_OF_DAY, diff + 2);
                 Date date = c.getTime();
                 info.add(new Pair<>(date, fs.getDuration()));
             }

             fares2.clear();
             fares2.add(new FareEntity("F001", BigDecimal.valueOf(3100), CabinClassTypeEnum.F));
             fares2.add(new FareEntity("F002", BigDecimal.valueOf(2850), CabinClassTypeEnum.F));
             fares2.add(new FareEntity("J001", BigDecimal.valueOf(1600), CabinClassTypeEnum.J));
             fares2.add(new FareEntity("J002", BigDecimal.valueOf(1350), CabinClassTypeEnum.J));
             fares2.add(new FareEntity("Y001", BigDecimal.valueOf(600), CabinClassTypeEnum.Y));
             fares2.add(new FareEntity("Y002", BigDecimal.valueOf(350), CabinClassTypeEnum.Y));

             returnFlightSchedulePlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanMultiple(returnFlightSchedulePlan, info,fares2, flight2.getFlightId());
             flightSchedulePlanSessionBean.associateExistingPlanToComplementaryPlan(flightSchedulePlan.getFlightSchedulePlanId(), returnFlightSchedulePlan.getFlightSchedulePlanId());
               
         }
           
        catch(Exception e){
            e.printStackTrace();
        }
    
    
    }
    
    
    
    
    

    

  

   

    

    


    

    
}
