package exceptions;



public class FlightSchedulePlanDoNotExistException extends Exception
{
    public  FlightSchedulePlanDoNotExistException()
    {
    }
    
    
    
    public  FlightSchedulePlanDoNotExistException(String msg)
    {
        super(msg);
    }
}