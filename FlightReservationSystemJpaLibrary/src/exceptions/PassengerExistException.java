package exceptions;



public class PassengerExistException extends Exception
{
    public PassengerExistException()
    {
    }
    
    
    
    public PassengerExistException(String msg)
    {
        super(msg);
    }
}