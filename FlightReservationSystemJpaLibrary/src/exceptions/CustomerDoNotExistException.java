package exceptions;



public class CustomerDoNotExistException extends Exception
{
    public CustomerDoNotExistException()
    {
    }
    
    
    
    public CustomerDoNotExistException(String msg)
    {
        super(msg);
    }
}