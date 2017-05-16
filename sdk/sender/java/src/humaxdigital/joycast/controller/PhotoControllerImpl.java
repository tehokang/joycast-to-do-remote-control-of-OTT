package humaxdigital.joycast.controller;

import java.util.ArrayList;
import java.util.List;

import humaxdigital.joycast.codec.CodecFactory;
import humaxdigital.joycast.codec.request.photocontrol.Load;
import humaxdigital.joycast.codec.request.photocontrol.Unload;
import humaxdigital.joycast.codec.response.Event;
import humaxdigital.joycast.codec.response.Param;
import humaxdigital.joycast.codec.response.Response;
import humaxdigital.joycast.util.CastLogger;

public class PhotoControllerImpl extends Controller implements PhotoController
{
    public PhotoControllerImpl(Controller.Listener listener) 
    {
        super(listener);
        
        m_controller_type = CodecFactory.CONTROLLER_PHOTO;
    }
    
    public boolean load(String url) 
    {
        Load load = new Load(m_controller_id, m_controller_type, url);
        return super.m_listener.onSendRequest(CodecFactory.encode(load));        
    }
    
    public boolean unload()
    {
        Unload unload = new Unload(m_controller_id, m_controller_type);
        return super.m_listener.onSendRequest(CodecFactory.encode(unload));
    }
        
    public void addEventListener(PhotoController.Listener listener) 
    {
        m_listeners.add(listener);
    }
    
    public void removeEventListener(PhotoController.Listener listener) 
    {
        for ( int i=0; i<m_listeners.size(); i++ ) 
        {
            if ( listener == m_listeners.get(i) ) 
            {
                m_listeners.remove(i);
                break;
            }
        }
    }
    
    @Override
    public void destroy() 
    {
        
    }
    
    @Override
    public void receivedResponse(Response response)
    {
        
    }
    
    @Override
    public void receivedEvent(Event event) 
    {
        if ( false == m_controller_type.equalsIgnoreCase(event.getHeader().getControllerType() ) )
        {
            return;
        }
        
        List<Param> params = event.getBody().getParamList();
        
        switch ( event.getBody().getActionName() )
        {
            case Event.PT_ON_LOAD:
                CastLogger.e("PT_ON_LOAD");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onLoaded(String.valueOf(params.get(0).getValue()));
                }
                break;
            case Event.PT_ON_UNLOAD:
                CastLogger.e("PT_ON_UNLOAD");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onUnloaded();
                }
                break;
            default:
                CastLogger.e(TAG, "Unknown event");
                break;
        }
    }
    
    private final String TAG = "PhotoController";
    private List<PhotoController.Listener> m_listeners = new ArrayList<PhotoController.Listener>();

}
