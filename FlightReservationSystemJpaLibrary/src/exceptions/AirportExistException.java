package exceptions;



public class AirportExistException extends Exception
{
    public AirportExistException()
    {
    }
    
    
    
    public AirportExistException(String msg)
    {
        super(msg);
    }
}