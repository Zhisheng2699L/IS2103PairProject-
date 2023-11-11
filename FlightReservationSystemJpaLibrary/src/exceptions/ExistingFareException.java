package exceptions;



public class ExistingFareException extends Exception
{
    public ExistingFareException()
    {
    }
    
    
    
    public ExistingFareException(String msg)
    {
        super(msg);
    }
}