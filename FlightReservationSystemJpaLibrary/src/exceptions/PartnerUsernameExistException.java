package exceptions;



public class PartnerUsernameExistException extends Exception
{
    public PartnerUsernameExistException()
    {
    }
    
    
    
    public PartnerUsernameExistException(String msg)
    {
        super(msg);
    }
}