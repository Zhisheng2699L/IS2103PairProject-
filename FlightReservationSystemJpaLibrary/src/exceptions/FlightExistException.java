package exceptions;



public class FlightExistException extends Exception
{
    public FlightExistException()
    {
    }
    
    
    
    public FlightExistException(String msg)
    {
        super(msg);
    }
}