package humaxdigital.joycast.controller;

import humaxdigital.joycast.codec.response.Event;
import humaxdigital.joycast.codec.response.Response;

/**
 * Abstraction class to describe common interface of Controller
 */
public abstract class Controller 
{
    public interface Listener 
    {
        boolean onSendRequest(String message);
    }
    
    public abstract void destroy();
    
    public abstract void receivedEvent(Event event);
    
    public abstract void receivedResponse(Response response);
    
    public Controller(Controller.Listener listener) 
    {
        m_listener = listener;
    }
    
    public void setControllerId(String controller_id) 
    {
        m_controller_id = controller_id;
    }
    
    public String getControllerId() 
    {
        return m_controller_id;
    }
    
    public String getControllerType() 
    {
        return m_controller_type;
    }
    
    protected boolean send(String message) 
    {
        return m_listener.onSendRequest(message);
    }
    
    private final String TAG = "Controller";
    protected String m_controller_id = "none";
    protected String m_controller_type = "none";
    protected Controller.Listener m_listener;
}
