package exceptions;



public class ReservationDoNotExistException extends Exception
{
    public ReservationDoNotExistException()
    {
    }
    
    
    
    public ReservationDoNotExistException(String msg)
    {
        super(msg);
    }
}