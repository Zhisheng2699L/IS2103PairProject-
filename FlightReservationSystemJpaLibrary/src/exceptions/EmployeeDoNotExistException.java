package exceptions;



public class EmployeeDoNotExistException extends Exception
{
    public EmployeeDoNotExistException()
    {
    }
    
    
    
    public EmployeeDoNotExistException(String msg)
    {
        super(msg);
    }
}