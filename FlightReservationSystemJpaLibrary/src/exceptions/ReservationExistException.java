package exceptions;



public class ReservationExistException extends Exception
{
    public ReservationExistException()
    {
    }
    
    
    
    public ReservationExistException(String msg)
    {
        super(msg);
    }
}