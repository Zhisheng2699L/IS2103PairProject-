package exceptions;



public class FlightScheduleNotFoundException extends Exception
{
    public FlightScheduleNotFoundException()
    {
    }
    
    
    
    public FlightScheduleNotFoundException(String msg)
    {
        super(msg);
    }
}