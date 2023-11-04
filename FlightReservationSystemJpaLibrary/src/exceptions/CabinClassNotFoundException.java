package exceptions;



public class CabinClassNotFoundException extends Exception
{
    public CabinClassNotFoundException()
    {
    }
    
    
    
    public CabinClassNotFoundException(String msg)
    {
        super(msg);
    }
}