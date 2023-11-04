package exceptions;



public class ExistingAircraftConfigException extends Exception
{
    public ExistingAircraftConfigException()
    {
    }
    
    
    
    public ExistingAircraftConfigException(String msg)
    {
        super(msg);
    }
}