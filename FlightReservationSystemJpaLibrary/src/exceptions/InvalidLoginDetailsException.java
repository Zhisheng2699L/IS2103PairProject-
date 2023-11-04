package exceptions;



public class InvalidLoginDetailsException extends Exception
{
    public InvalidLoginDetailsException()
    {
    }
    
    
    
    public InvalidLoginDetailsException(String msg)
    {
        super(msg);
    }
}