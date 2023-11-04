package exceptions;



public class AirportDoNotExistException extends Exception
{
    public AirportDoNotExistException()
    {
    }
    
    
    
    public AirportDoNotExistException(String msg)
    {
        super(msg);
    }
}