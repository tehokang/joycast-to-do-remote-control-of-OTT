package humaxdigital.joycast.codec.response;

public class Param
{
    public Param() 
    {
        this.setType("");
        this.setName("");
        this.setValue("");
    }
    
    public Param(String type, String name, String value) 
    {
        this.setType(type);
        this.setName(name);
        this.setValue(value);
    }
    
    public String getType() 
    {
        return m_type;
    }
    
    public void setType(String type) 
    {
        m_type = type;
    }
    
    public String getName() 
    {
        return m_name;
    }
    
    public void setName(String name) 
    {
        m_name = name;
    }
    
    public String getValue() 
    {
        return m_value;
    }
    
    public void setValue(String value) 
    {
        m_value = value;
    }
    protected String m_type;
    protected String m_name;
    protected String m_value;
}