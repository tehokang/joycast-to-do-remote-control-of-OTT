package humaxdigital.joycast.codec.response;

public class Response extends Event 
{
    public Response() 
    {
        super();
    }
    
    public Result getResult() 
    {
        return m_result;
    }
    
    public void setResult(Result result) 
    {
        m_result = result;
    }
    
    protected Result m_result;
    public static final String RESPONSE = "response";
}
