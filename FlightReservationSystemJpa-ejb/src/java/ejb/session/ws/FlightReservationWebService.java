/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import pairHelper.MyPair;
import entity.FlightScheduleEntity;
import entity.FareEntity;
import entity.SeatInventoryEntity;
import entity.ReservationEntity;
import entity.PassengerEntity;
import entity.ItineraryEntity;
import enumeration.CabinClassTypeEnum;
import ejb.session.stateless.FlightScheduleSessionBeanLocal;
import ejb.session.stateless.ItinerarySessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.SeatsInventorySessionBeanLocal;
import exceptions.CabinClassNotFoundException;
import exceptions.FlightNotFoundException;
import exceptions.FlightScheduleNotFoundException;
import exceptions.InputDataValidationException;
import exceptions.InvalidLoginCredentialException;
import exceptions.ItineraryDoNotExistException;
import exceptions.ItineraryExistException;
import exceptions.PartnerNotFoundException;
import exceptions.ReservationExistException;
import exceptions.SeatInventoryNotFoundException;
import exceptions.UnknownPersistenceException;
import exceptions.SeatAlreadyBookedException;
import exceptions.SeatSlotNotFoundException;
import exceptions.UserNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;


@WebService(serviceName = "FlightReservationWebService")
@Stateless()
public class FlightReservationWebService {

    @EJB
    private ItinerarySessionBeanLocal itinerarySessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private SeatsInventorySessionBeanLocal seatsInventorySessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;
   
    
    @WebMethod(operationName = "doLogin")
    public long doLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException, PartnerNotFoundException {
        return partnerSessionBean.doLogin(username, password);
    }

    @WebMethod(operationName = "getFlightSchedules")
    public List<FlightScheduleEntity> getFlightSchedules(@WebParam(name = "origin") String origin, @WebParam(name = "destination") String destination, @WebParam(name = "date") String date,
            @WebParam(name = "cabinclasstype") CabinClassTypeEnum cabinclasstype) throws FlightNotFoundException, ParseException {

        Date departureDate = parseDate(date);
        
        List<FlightScheduleEntity> res = flightScheduleSessionBean.getFlightSchedules(origin, destination, departureDate, cabinclasstype);
        
        for (FlightScheduleEntity fse: res) {
            fse.getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
            fse.getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
            fse.getFlightSchedulePlan().getFlight().setAircraftConfig(null);
            fse.getFlightSchedulePlan().getFlight().getFlightRoute().setComplementaryRoute(null);
            fse.getFlightSchedulePlan().getFlight().getFlightRoute().setOriginRoute(null);
            fse.getFlightSchedulePlan().getFlight().setOriginFlight(null);
            fse.getFlightSchedulePlan().getFlight().setReturnFlight(null);
            fse.getFlightSchedulePlan().setComplementary(null);
            fse.getFlightSchedulePlan().setOrigin(null);
            fse.getFlightSchedulePlan().setFlightSchedule(null);
            fse.setReservations(null);
            
            for (FareEntity fare: fse.getFlightSchedulePlan().getFares()) {
                fare.setFlightSchedulePlan(null);
            }
            for (SeatInventoryEntity seats: fse.getSeatInventory()) {
                if (seats.getCabin()!= null) {
                    seats.getCabin().setAircraftConfig(null);
                }
                seats.setFlightSchedule(null);
            }
        }
        return res;
    }
    
    @WebMethod(operationName = "getBiggestFare")
    public FareEntity getBiggestFare(@WebParam(name = "flightscheduleentity") FlightScheduleEntity flightscheduleentity,
            @WebParam(name = "cabinclasstype") CabinClassTypeEnum cabinclastype) throws 
            FlightScheduleNotFoundException, 
            CabinClassNotFoundException {
        FareEntity fare = flightScheduleSessionBean.getHighestUnmanagedFare(flightscheduleentity, cabinclastype);
        fare.setFlightSchedulePlan(null);
        return fare;
    }
    
    @WebMethod(operationName = "getIndirectFlightSchedules")
    public List<MyPair> getIndirectFlightSchedules(@WebParam(name = "origin") String origin, @WebParam(name = "destination") String destination, @WebParam(name = "date") String date,
            @WebParam(name = "cabinclasstype") CabinClassTypeEnum cabinclasstype) throws FlightNotFoundException, ParseException {
        Date departureDate = parseDate(date);
        List<Pair<FlightScheduleEntity, FlightScheduleEntity>> list = flightScheduleSessionBean.getIndirectUnManagedFlightSchedules(origin, destination, departureDate, cabinclasstype);
        List<MyPair> newList = new ArrayList<>();
        for (Pair<FlightScheduleEntity, FlightScheduleEntity> pairs : list) {
            MyPair newPair = new MyPair(pairs.getKey(), pairs.getValue());
            newList.add(newPair);
        }
        for (MyPair res: newList) {
            FlightScheduleEntity fs = res.getFs1();
  
            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setComplementaryRoute(null);
            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setOriginRoute(null);
            fs.getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
            fs.getFlightSchedulePlan().getFlight().setOriginFlight(null);
            fs.getFlightSchedulePlan().getFlight().setReturnFlight(null);
            fs.getFlightSchedulePlan().getFlight().setAircraftConfig(null);
            for (FareEntity fare: fs.getFlightSchedulePlan().getFares()) {
                fare.setFlightSchedulePlan(null);
            }
            fs.getFlightSchedulePlan().setComplementary(null);
            fs.getFlightSchedulePlan().setOrigin(null);
            fs.getFlightSchedulePlan().setFlightSchedule(null);
            fs.setReservations(null);
            for (SeatInventoryEntity seats: fs.getSeatInventory()) {
                if (seats.getCabin()!= null) {seats.getCabin().setAircraftConfig(null);}
                seats.setFlightSchedule(null);
            }
            
            fs = res.getFs2();
            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setComplementaryRoute(null);
            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setOriginRoute(null);
            fs.getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
            fs.getFlightSchedulePlan().getFlight().setOriginFlight(null);
            fs.getFlightSchedulePlan().getFlight().setReturnFlight(null);
            fs.getFlightSchedulePlan().getFlight().setAircraftConfig(null);
            for (FareEntity fare: fs.getFlightSchedulePlan().getFares()) {
                fare.setFlightSchedulePlan(null);
            }
            fs.getFlightSchedulePlan().setComplementary(null);
            fs.getFlightSchedulePlan().setOrigin(null);
            fs.getFlightSchedulePlan().setFlightSchedule(null);
            fs.setReservations(null);
            for (SeatInventoryEntity seats: fs.getSeatInventory()) {
                if (seats.getCabin()!= null) {seats.getCabin().setAircraftConfig(null);}
                seats.setFlightSchedule(null);
            }

        }
        
        return newList;
    }
    
    @WebMethod(operationName = "retrieveFlightScheduleById")
    public FlightScheduleEntity retrieveFlightScheduleById(@WebParam(name = "flightscheduleid") long flightscheduleid) throws FlightScheduleNotFoundException {
        FlightScheduleEntity fs = flightScheduleSessionBean.retrieveUnmanagedFlightScheduleById(flightscheduleid);
        fs.getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
        fs.getFlightSchedulePlan().getFlight().getFlightRoute().setComplementaryRoute(null);
        fs.getFlightSchedulePlan().getFlight().getFlightRoute().setOriginRoute(null);
        fs.getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
        fs.getFlightSchedulePlan().getFlight().setOriginFlight(null);
        fs.getFlightSchedulePlan().getFlight().setReturnFlight(null);
        fs.getFlightSchedulePlan().getFlight().setAircraftConfig(null);
        for (FareEntity fare: fs.getFlightSchedulePlan().getFares()) {
            fare.setFlightSchedulePlan(null);
        }
        fs.getFlightSchedulePlan().setComplementary(null);
        fs.getFlightSchedulePlan().setOrigin(null);
        fs.getFlightSchedulePlan().setFlightSchedule(null);
        fs.setReservations(null);
        for (SeatInventoryEntity seats: fs.getSeatInventory()) {
            if (seats.getCabin()!= null) {seats.getCabin().setAircraftConfig(null);}
            seats.setFlightSchedule(null);
        }
        return fs;
            
    }
    
    @WebMethod(operationName = "getCorrectSeatInventory")
    public SeatInventoryEntity getCorrectSeatInventory(@WebParam(name = "flightscheduleentity") FlightScheduleEntity flightscheduleentity,
            @WebParam(name = "cabinclasstype") CabinClassTypeEnum cabinclasstype) throws 
            FlightScheduleNotFoundException, 
            SeatInventoryNotFoundException {
        SeatInventoryEntity seats = flightScheduleSessionBean.getCorrectSeatInventoryUnmanaged(flightscheduleentity, cabinclasstype);
        if (seats.getCabin()!= null) {seats.getCabin().setAircraftConfig(null);}
        seats.setFlightSchedule(null);
        return seats;
    }
    
    @WebMethod(operationName = "checkIfBooked")
    public boolean checkIfBooked(@WebParam(name = "seatinventoryentity") SeatInventoryEntity seatinventoryentity,
            @WebParam(name = "seatnumber") String seatnumber) throws SeatSlotNotFoundException {
        return seatsInventorySessionBean.checkIfBooked(seatinventoryentity, seatnumber);
    }
    
    @WebMethod(operationName = "createNewReservation")
    public long createNewReservation(@WebParam(name = "reservationentity") ReservationEntity reservationentity,
            @WebParam(name = "passengers") List<PassengerEntity> passengers,
            @WebParam(name = "flightscheduleid") long flightscheduleid,
            @WebParam(name = "itineraryid") long itineraryid) throws 
            InputDataValidationException, 
            ReservationExistException,
            UnknownPersistenceException,
            FlightScheduleNotFoundException,
            SeatInventoryNotFoundException,
            SeatAlreadyBookedException,
            ItineraryExistException,
            ItineraryDoNotExistException,
            SeatSlotNotFoundException {
        return reservationSessionBean.createNewReservation(reservationentity, passengers, flightscheduleid, itineraryid);
    }
    
    @WebMethod(operationName = "createNewItinerary")
    public long createNewItinerary(@WebParam(name = "creditcardnumber") String creditcardnumber,
            @WebParam(name = "cvv") String cvv,
            @WebParam(name = "userid") long userid) throws 
            UserNotFoundException, 
            InputDataValidationException,
            UnknownPersistenceException,
            ItineraryExistException {
        return itinerarySessionBean.createNewItinerary(new ItineraryEntity(creditcardnumber, cvv), userid).getIternaryId();
    }
    
    @WebMethod(operationName = "retrieveItinerariesByUserId")
    public List<ItineraryEntity> retrieveItinerariesByUserId(@WebParam(name = "userid") long userid) {
        List<ItineraryEntity> list = itinerarySessionBean.retrieveItinerariesByCustomerIdRemoved(userid);
        for (ItineraryEntity itinerary: list) {
            itinerary.getUser().setItineraries(null);
            for (ReservationEntity res: itinerary.getReservations()) {
                res.setItinerary(null);
                res.getFlightSchedule().setReservations(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().setComplementaryRoute(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().setOriginRoute(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().setOriginFlight(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().setReturnFlight(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().setAircraftConfig(null);
                for (FareEntity fare: res.getFlightSchedule().getFlightSchedulePlan().getFares()) {
                    fare.setFlightSchedulePlan(null);
                }
                res.getFlightSchedule().getFlightSchedulePlan().setComplementary(null);
                res.getFlightSchedule().getFlightSchedulePlan().setOrigin(null);
                res.getFlightSchedule().getFlightSchedulePlan().setFlightSchedule(null);
                res.getFlightSchedule().setReservations(null);
                for (SeatInventoryEntity seats: res.getFlightSchedule().getSeatInventory()) {
                    if (seats.getCabin()!= null) {seats.getCabin().setAircraftConfig(null);}
                    seats.setFlightSchedule(null);
                }
            }
        }
        return list;
    }
    
    @WebMethod(operationName = "retreiveItineraryById")
    public ItineraryEntity retrieveItineraryById(@WebParam(name = "itineraryid") long itineraryid) throws ItineraryDoNotExistException {
       ItineraryEntity itinerary = itinerarySessionBean.retrieveItineraryByIDRemoved(itineraryid);
       itinerary.getUser().setItineraries(null);
        for (ReservationEntity res: itinerary.getReservations()) {
            res.setItinerary(null);
            res.getFlightSchedule().setReservations(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().setComplementaryRoute(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().setOriginRoute(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().setFlightSchedulePlan(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().setOriginFlight(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().setReturnFlight(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().setAircraftConfig(null);
            for (FareEntity fare: res.getFlightSchedule().getFlightSchedulePlan().getFares()) {
                fare.setFlightSchedulePlan(null);
            }
            res.getFlightSchedule().getFlightSchedulePlan().setComplementary(null);
            res.getFlightSchedule().getFlightSchedulePlan().setOrigin(null);
            res.getFlightSchedule().getFlightSchedulePlan().setFlightSchedule(null);
            res.getFlightSchedule().setReservations(null);
            for (SeatInventoryEntity seats: res.getFlightSchedule().getSeatInventory()) {
                if (seats.getCabin()!= null) {seats.getCabin().setAircraftConfig(null);}
                seats.setFlightSchedule(null);
            }
        }
        return itinerary;
    }
    
    private Date parseDate(String inputDateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        return dateFormat.parse(inputDateString);
    }
}