/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClassEntity;
import entity.FlightScheduleEntity;
import entity.SeatInventoryEntity;
import exceptions.SeatAlreadyBookedException;
import exceptions.SeatSlotNotFoundException;
import exceptions.ViolationConstraintsException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author foozh
 */
@Local
public interface SeatsInventorySessionBeanLocal {
    
    public SeatInventoryEntity createSeatInventory(SeatInventoryEntity seatInventory,
FlightScheduleEntity flightSchedule, CabinClassEntity cabinClass) throws ViolationConstraintsException;

    public SeatInventoryEntity retrieveSeatsById(Long seatInventoryID) throws SeatSlotNotFoundException;

    public void deleteSeatInventory(List<SeatInventoryEntity> seats);

    public void bookSeat(long seatInventoryId, String seatNumber) throws SeatAlreadyBookedException, SeatSlotNotFoundException;

    public boolean checkIfBooked(SeatInventoryEntity seatInventory, String seatNumber) throws SeatSlotNotFoundException;
    
}
