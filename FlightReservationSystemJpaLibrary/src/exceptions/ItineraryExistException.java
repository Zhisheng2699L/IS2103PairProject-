package exceptions;



public class ItineraryExistException extends Exception
{
    public ItineraryExistException()
    {
    }
    
    
    
    public ItineraryExistException(String msg)
    {
        super(msg);
    }
}