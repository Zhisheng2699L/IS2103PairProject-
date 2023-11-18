/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.SeatInventoryEntity;
import exceptions.SeatAlreadyBookedException;
import exceptions.SeatSlotNotFoundException;
import javax.ejb.Remote;

/**
 *
 * @author foozh
 */
@Remote
public interface SeatsInventorySessionBeanRemote {
    
    /*public SeatInventoryEntity createSeatInventory(SeatInventoryEntity seatInventory,
FlightScheduleEntity flightSchedule, CabinClassEntity cabinClass) throws ViolationConstraintsException;

    public SeatInventoryEntity retrieveSeatsById(Long seatInventoryID) throws SeatSlotNotFoundException;

    public void deleteSeatInventory(List<SeatInventoryEntity> seats);*/

    public void bookSeat(long seatInventoryId, String seatNumber) throws SeatAlreadyBookedException, SeatSlotNotFoundException;

    public boolean checkIfBooked(SeatInventoryEntity seatInventory, String seatNumber) throws SeatSlotNotFoundException;
}
