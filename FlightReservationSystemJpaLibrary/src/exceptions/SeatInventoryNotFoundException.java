package exceptions;



public class SeatInventoryNotFoundException extends Exception
{
    public SeatInventoryNotFoundException()
    {
    }
    
    
    
    public SeatInventoryNotFoundException(String msg)
    {
        super(msg);
    }
}