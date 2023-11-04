package exceptions;



public class AircraftConfigNotFoundException extends Exception
{
    public AircraftConfigNotFoundException()
    {
    }
    
    
    
    public AircraftConfigNotFoundException(String msg)
    {
        super(msg);
    }
}