/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.PassengerEntity;
import entity.ReservationEntity;
import exceptions.FlightScheduleNotFoundException;
import exceptions.InputDataValidationException;
import exceptions.ItineraryDoNotExistException;
import exceptions.ReservationDoNotExistException;
import exceptions.ReservationExistException;
import exceptions.SeatAlreadyBookedException;
import exceptions.SeatSlotNotFoundException;
import exceptions.UnknownPersistenceException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author kahjy
 */
@Remote
public interface ReservationSessionBeanRemote {
    public long createNewReservation(ReservationEntity reservation, List<PassengerEntity> passengers, long flightScheduleId, long itineraryId) throws ReservationExistException, UnknownPersistenceException, FlightScheduleNotFoundException, SeatSlotNotFoundException, SeatAlreadyBookedException, ItineraryDoNotExistException, InputDataValidationException;

    public ReservationEntity retrieveReservationById(long id) throws ReservationDoNotExistException;
}
