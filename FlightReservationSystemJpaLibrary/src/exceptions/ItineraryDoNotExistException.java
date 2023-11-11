package exceptions;



public class ItineraryDoNotExistException extends Exception
{
    public ItineraryDoNotExistException()
    {
    }
    
    
    
    public ItineraryDoNotExistException(String msg)
    {
        super(msg);
    }
}