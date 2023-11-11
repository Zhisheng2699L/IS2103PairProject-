package exceptions;

public class InvalidCostException extends Exception
{
    public InvalidCostException()
    {
    }
    
    
    
    public InvalidCostException(String msg)
    {
        super(msg);
    }
}