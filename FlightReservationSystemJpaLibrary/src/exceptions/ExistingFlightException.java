package exceptions;



public class ExistingFlightException extends Exception
{
    public ExistingFlightException()
    {
    }
    
    
    
    public ExistingFlightException(String msg)
    {
        super(msg);
    }
}