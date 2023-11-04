package exceptions;



public class CabinClassTypeEnumNotFoundException extends Exception
{
    public CabinClassTypeEnumNotFoundException()
    {
    }
    
    public CabinClassTypeEnumNotFoundException(String msg)
    {
        super(msg);
    }
}