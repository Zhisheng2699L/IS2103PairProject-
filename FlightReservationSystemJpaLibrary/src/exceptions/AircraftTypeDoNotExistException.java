package exceptions;



public class AircraftTypeDoNotExistException extends Exception
{
    public AircraftTypeDoNotExistException()
    {
    }
    
    
    
    public AircraftTypeDoNotExistException(String msg)
    {
        super(msg);
    }
}