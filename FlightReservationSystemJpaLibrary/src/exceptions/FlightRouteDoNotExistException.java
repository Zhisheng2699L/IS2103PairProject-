package exceptions;



public class FlightRouteDoNotExistException extends Exception
{
    public FlightRouteDoNotExistException()
    {
    }
    
    
    
    public FlightRouteDoNotExistException(String msg)
    {
        super(msg);
    }
}