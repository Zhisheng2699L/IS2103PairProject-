package exceptions;



public class AircraftConfigExistException extends Exception
{
    public AircraftConfigExistException()
    {
    }
    
    
    
    public AircraftConfigExistException(String msg)
    {
        super(msg);
    }
}