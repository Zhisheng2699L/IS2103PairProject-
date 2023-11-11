package exceptions;

public class SeatAlreadyBookedException extends Exception
{
    public SeatAlreadyBookedException()
    {
    }
    
    
    
    public SeatAlreadyBookedException(String msg)
    {
        super(msg);
    }
}