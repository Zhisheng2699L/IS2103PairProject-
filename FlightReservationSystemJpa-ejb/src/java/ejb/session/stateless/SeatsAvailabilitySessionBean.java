/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClassEntity;
import entity.FlightScheduleEntity;
import entity.SeatInventoryEntity;
import exceptions.SeatAlreadyBookedException;
import exceptions.SeatSlotNotFoundException;
import exceptions.ViolationConstraintsException;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author foozh
 */
@Stateless
public class SeatsAvailabilitySessionBean implements SeatsAvailabilitySessionBeanRemote, SeatsAvailabilitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystemJpa-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory customValidatorFactory;
    private final Validator customValidator;

    public SeatsAvailabilitySessionBean() {
        customValidatorFactory = Validation.buildDefaultValidatorFactory();
        customValidator = customValidatorFactory.getValidator();
    }
    
    public SeatInventoryEntity createSeatInventory(SeatInventoryEntity seatInventory,
            FlightScheduleEntity flightSchedule, CabinClassEntity cabinClass) throws ViolationConstraintsException {

        int noOfRows = cabinClass.getNumRows();
        int noOfSeatsAbreast = cabinClass.getNumSeatsAbreast();
        char[][] seats = new char[noOfRows][noOfSeatsAbreast];

        for (int i = 0; i < noOfRows; i++) {
            for (int j = 0; j < noOfSeatsAbreast; j++) {
                seats[i][j] = '-';
            }
        }
        seatInventory.setSeats(seats);

        Set<ConstraintViolation<SeatInventoryEntity>> constraintViolations = customValidator.validate(seatInventory);
        if (constraintViolations.isEmpty()) {
           
            seatInventory.setCabin(cabinClass);
            seatInventory.setFlightSchedule(flightSchedule);
            flightSchedule.getSeatInventory().add(seatInventory);
            em.persist(seatInventory);
            
            return seatInventory;
        } else {
            throw new ViolationConstraintsException("Constraints Violated");
        }
    }
    
    public SeatInventoryEntity retrieveSeatsById(Long seatInventoryID) throws SeatSlotNotFoundException {
        SeatInventoryEntity seat = em.find(SeatInventoryEntity.class, seatInventoryID);
        if (seat != null) {
            return seat;
        } else {
            throw new SeatSlotNotFoundException("Seat Inventory does not exist!");
        }
    }
    
    public void deleteSeatInventory(List<SeatInventoryEntity> seats) {
        for (SeatInventoryEntity seat : seats) {
            em.remove(seat);
        }
    }
    
    public void bookSeat(long seatInventoryId, String seatNumber) throws SeatAlreadyBookedException, SeatSlotNotFoundException {
        SeatInventoryEntity seatInventory = retrieveSeatsById(seatInventoryId);

        int[] indices = convertSeatNumberToIndices(seatNumber);
        int row = indices[0];
        int col = indices[1];

        char[][] seats = seatInventory.getSeats();

        if (seats[row][col] == '-') {
            seats[row][col] = 'X';
            seatInventory.setSeats(seats);
        } else {
            throw new SeatAlreadyBookedException("Seat already booked");
        }

        seatInventory.setReservedSeats(seatInventory.getReservedSeats() + 1);
        seatInventory.setBalanceSeats(seatInventory.getBalanceSeats() - 1);
    }

    private int[] convertSeatNumberToIndices(String seatNumber) {
        int col = seatNumber.charAt(0) - 'A';
        int row = Integer.parseInt(seatNumber.substring(1)) - 1;
        return new int[]{row, col};
    }
    
    public boolean checkIfBooked(SeatInventoryEntity seatInventory, String seatNumber) throws SeatSlotNotFoundException {
       
        char[][] mtx = seatInventory.getSeats();
        int col = seatNumber.charAt(0) - 'A';
        int row = Integer.parseInt(seatNumber.substring(1)) - 1;
            
        if (!isValidSeatIndex(row, col, mtx)) {
            throw new SeatSlotNotFoundException("Seat Inventory does not exist!");
        }
        return mtx[row][col] == 'X';
    }
    private boolean isValidSeatIndex(int row, int col, char[][] mtx) {
        return row >= 0 && col >= 0 && row < mtx.length && col < mtx[0].length;
    }

}

