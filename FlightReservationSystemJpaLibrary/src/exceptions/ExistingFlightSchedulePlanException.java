package exceptions;



public class ExistingFlightSchedulePlanException extends Exception
{
    public ExistingFlightSchedulePlanException()
    {
    }
    
    public ExistingFlightSchedulePlanException(String msg)
    {
        super(msg);
    }
}