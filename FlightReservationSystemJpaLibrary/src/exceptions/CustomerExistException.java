package exceptions;



public class CustomerExistException extends Exception
{
    public CustomerExistException()
    {
    }
    
    
    
    public CustomerExistException(String msg)
    {
        super(msg);
    }
}