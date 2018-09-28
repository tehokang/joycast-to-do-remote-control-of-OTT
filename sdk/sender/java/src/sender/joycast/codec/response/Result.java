package sender.joycast.codec.response;

import java.util.ArrayList;
import java.util.List;

public class Result 
{
    public String getCode() 
    {
        return m_code;
    }
    
    public void setCode(String code) 
    {
        m_code = code;
    }
    
    public String getDecription() 
    {
        return m_description;
    }
    
    public void setDescription(String description) 
    {
        m_description = description;
    }
    
    public List<Param> getParamList() 
    {
        return m_params;
    }
    
    public void setParamList(List<Param> params) 
    {
        m_params = params;
    }
    
    public static final String CODE_OK = "200";
    public static final String CODE_SESSION_FULL = "300";
    
    protected String m_code;
    protected String m_description;
    protected List<Param> m_params = new ArrayList<Param>();
}
