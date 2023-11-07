package exceptions;



public class FareDoNotExistException extends Exception
{
    public FareDoNotExistException()
    {
    }
    
    
    
    public FareDoNotExistException(String msg)
    {
        super(msg);
    }
}