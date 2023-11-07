package exceptions;



public class SeatSlotNotFoundException extends Exception
{
    public SeatSlotNotFoundException()
    {
    }
    
    public SeatSlotNotFoundException(String msg)
    {
        super(msg);
    }
}