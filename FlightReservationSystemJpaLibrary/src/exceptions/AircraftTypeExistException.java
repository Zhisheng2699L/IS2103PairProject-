package exceptions;



public class AircraftTypeExistException extends Exception
{
    public AircraftTypeExistException()
    {
    }
    
    
    
    public AircraftTypeExistException(String msg)
    {
        super(msg);
    }
}