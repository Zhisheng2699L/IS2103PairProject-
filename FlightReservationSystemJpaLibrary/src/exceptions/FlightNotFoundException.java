package exceptions;



public class FlightNotFoundException extends Exception
{
    public FlightNotFoundException()
    {
    }
    
    
    
    public FlightNotFoundException(String msg)
    {
        super(msg);
    }
}