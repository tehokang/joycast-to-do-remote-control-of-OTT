package sender.joycast.session;

import java.util.ArrayList;
import java.util.List;

import sender.joycast.codec.CodecFactory;
import sender.joycast.codec.request.HealthCheck;
import sender.joycast.codec.request.QueryCapability;
import sender.joycast.codec.response.Event;
import sender.joycast.codec.response.Param;
import sender.joycast.codec.response.Response;
import sender.joycast.constant.Configuration;
import sender.joycast.controller.AVController;
import sender.joycast.controller.AVControllerImpl;
import sender.joycast.controller.Controller;
import sender.joycast.controller.PhotoController;
import sender.joycast.controller.PhotoControllerImpl;
import sender.joycast.dial.DialServer;
import sender.joycast.util.CastLogger;

/**
 * CastSession is almost synchronized with Session of stick to control websocket and 
 * AvController derived from Controller. CastSession will be used directly by User application
 * @see Session Controller.Listener
 */
public class CastSessionImpl extends CastSession implements Controller.Listener, Communicator.Listener
{
    /**
     * Function to register listener to listen to status of CastSession 
     * especially to know for Controller added by Server
     */
    public void addEventListener(CastSession.Listener listener)
    {
        m_listeners.add(listener);
        m_session_listeners.add(listener);
    }
    
    /**
     * Function to release listener attached
     */
    public void removeEventListener(CastSession.Listener listener)
    {
        for ( int i=0; i<m_listeners.size(); i++ ) 
        {
            if ( m_listeners.get(i) == listener ) 
            {
                m_listeners.remove(i);
                m_session_listeners.remove(i);
                break;
            }
        }
    }
    
    @Override
    public void onConnected(String server_uri) 
    {
        CastLogger.i(TAG, "connected to " + server_uri);
        m_session_state = SessionState.CONNECTING;
        m_connected_server_uri = server_uri;
        
        m_query_thread = new Thread(new Runnable()
        {
            @Override
            public void run() 
            {
                while( false == m_responsed_query ) 
                {
                    try 
                    {
                        if ( m_communicator != null )
                        {
                            m_communicator.send(
                                CodecFactory.encode(
                                        new QueryCapability("none", "none")));
                        }
                        Thread.sleep(5000);
                    } 
                    catch (InterruptedException e) 
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        m_query_thread.start();
        
        for ( int i=0; i<m_session_listeners.size(); i++ ) 
            m_session_listeners.get(i).onSessionStarted(CastSessionImpl.this);
    }

    @Override
    public void onReceived(String event) 
    {
        Response response = CodecFactory.decode(event);
        if ( response != null )
        {
            switch ( response.getBody().getActionType() ) 
            {
                case Response.RESPONSE:
                    __process_response__(response);
                    break;
                case Event.EVENT:
                    __process_event__(response);
                    break;
                default:
                    CastLogger.e("Unknown action type of response [" + event + "]");
                    break;
            }
        }
    }

    @Override
    public void onError(int error) 
    {
        for ( int i=0; i<m_session_listeners.size(); i++ ) 
        {
            
        }
    }

    @Override
    public void onDisconnected() 
    {
        m_session_state = SessionState.DISCONNECTED;
        for ( int i=0; i<m_session_listeners.size(); i++ ) 
        {
            m_session_listeners.get(i).onSessionEnded(CastSessionImpl.this, 0);
        }
    }
    /**
     * CastSession has to be created by SessionManager, User application can't call directly
     * @throws Exception
     */
    public CastSessionImpl() throws Exception 
    {
        m_controllers = new ArrayList<Controller>();
        m_listeners = new ArrayList<CastSession.Listener>();
        
        m_communicator = new Communicator();
        m_communicator.setEventListener(this);
    }
    
    /**
     * Start connecting and handshaking with Server by using JSON protocol
     * @param server
     * @return return true if it's succeed else return false
     */
    public boolean start(DialServer server, String protocol)
    {
        return start(
                server.getIpAddress().getHostAddress(), Configuration.JOYCAST_PORT, protocol);
    }
    
    /**
     * Start connecting and handshaking with Server by using JSON protocol
     * @param ip 
     * @param port 
     * @return return true if it's succeed else return false
     */
    public boolean start(String ip, int port, String protocol)
    {
        for ( int i=0; i<m_session_listeners.size(); i++ ) 
        {
            m_session_listeners.get(i).onSessionStarting(CastSessionImpl.this);
        }
        
        try 
        {
            m_communicator.connect(ip, port, protocol);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            for ( int i=0; i<m_session_listeners.size(); i++ ) 
            {
                m_session_listeners.get(i).onSessionStartFailed(CastSessionImpl.this, 0);
            }     
            return false;
        }
        
        return true;
    }
    
    /**
     * Stop connection between Server and CastSession
     */
    public void stop() 
    {
        m_communicator.destroy();
        m_communicator = null;

        if ( m_query_thread != null) 
        {
            m_query_thread.interrupt();
            m_query_thread = null;
        }
        
        for ( int i=0; i<m_controllers.size(); i++) 
        {
            m_controllers.get(i).destroy();
        }        
    }
    
    /**
     * Function to get AVController supporting by Server
     * @return AVController
     */
    public AVController getAvController() 
    {
        for ( int i=0; i<m_controllers.size(); i++) 
        {
            if ( m_controllers.get(i).getControllerType() == CodecFactory.CONTROLLER_AV )
                return (AVController) m_controllers.get(i);
        }
        return null;
    }
    
    /**
     * Function to get PhotoController supporting by Server (Not implemented yet)
     * @return PhotoController
     */
    public PhotoController getPhotoController() 
    {
        for ( int i=0; i<m_controllers.size(); i++) 
        {
            if ( m_controllers.get(i).getControllerType() == CodecFactory.CONTROLLER_PHOTO )
                return (PhotoController) m_controllers.get(i);
        }
        return null;
    }
    
    public boolean healthCheck(final long timeout_msec)
    {
        m_responsed_healthcheck = false;
        if ( m_healthcheck_thread != null ) 
        {
            m_healthcheck_thread.interrupt();
            m_healthcheck_thread = null;
        }
        
        m_healthcheck_thread = new Thread(new Runnable(){
            @Override
            public void run()
            {
                long startTime = System.currentTimeMillis();
                while ( m_responsed_healthcheck == false )
                {
                    try
                    {
                        if ( startTime + timeout_msec < System.currentTimeMillis() ) 
                        {
                            for ( int j=0; j<m_listeners.size(); j++ ) 
                                m_listeners.get(j).onHummingNotAlived();
                            break;
                        }
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e)
                    {
                        /**
                         * Nothing to inform, exception is normal happening
                         */
                    }
                }
            }
        });
        m_healthcheck_thread.start();
        
        return m_communicator.send(
                CodecFactory.encode(new HealthCheck("none", "none")));
    }
    
    @Override
    public boolean onSendRequest(String message)
    {
        return m_communicator.send(message);
    }
    
    private void __process_response__(Response response)
    {
        switch ( response.getBody().getActionName() )
        {
            case QueryCapability.ACTION:
                __process_response_of_query_capability__(response);
                break;
            case HealthCheck.ACTION:
                __process_response_of_health_check__(response);
                break;
            default:
                __process_response_default__(response);
                break;
        }
    }
    
    private void __process_response_default__(Response response)
    {
        for ( int i=0; i<m_controllers.size(); i++ ) 
        {
            Controller controller = m_controllers.get(i);
            switch ( controller.getControllerType() )
            {
                case CodecFactory.CONTROLLER_AV:
                case CodecFactory.CONTROLLER_PHOTO:
                    controller.receivedResponse(response);
                    break;
                default:
                    CastLogger.e("Unknown response of controller");
                    break;
            }
        }
    }
    
    private void __process_response_of_health_check__(Response response)
    {
        m_responsed_healthcheck = true;
        
        if ( m_healthcheck_thread != null ) 
        {
            m_healthcheck_thread.interrupt();
            m_healthcheck_thread = null;
        }
        
        for ( int j=0; j<m_listeners.size(); j++ ) 
            m_listeners.get(j).onHummingAlived();
    }
    
    private void __process_response_of_query_capability__(Response response)
    {
        m_responsed_query = true;
        m_session_state = SessionState.CONNECTED;
        
        List<Param> params = response.getResult().getParamList();
        for ( int i=0; i<params.size(); i++ ) 
        {
            Controller controller = null;
            switch( params.get(i).getName() )
            {
                case CodecFactory.CONTROLLER_AV:
                    controller = new AVControllerImpl(CastSessionImpl.this);
                    controller.setControllerId(params.get(i).getValue());
                    break;
                case CodecFactory.CONTROLLER_PHOTO:
                    controller = new PhotoControllerImpl(CastSessionImpl.this);
                    controller.setControllerId(params.get(i).getValue());
                    break;
                default:
                    CastLogger.e("Not support unknown controller [ " + 
                            params.get(i).getName() + "]");
                    continue;
            }
            m_controllers.add(controller);
        }
        
        for ( int i=0; i<m_controllers.size(); i++ ) 
        {
            Controller controller = m_controllers.get(i);
            switch ( controller.getControllerType() )
            {
                case CodecFactory.CONTROLLER_AV:
                    for ( int j=0; j<m_listeners.size(); j++ ) 
                        m_listeners.get(j).onAddedSupportingAvController(this, controller);
                    break;
                case CodecFactory.CONTROLLER_PHOTO:
                    for ( int j=0; j<m_listeners.size(); j++ ) 
                        m_listeners.get(j).onAddedSupportingPhotoController(this, controller);
                    break;
                default:
                    CastLogger.e("Unknown response of controller");
                    break;
            }
        }
    }
    
    private void __process_event__(Event event)
    {
        for ( int i=0; i<m_controllers.size(); i++ ) 
        {
            Controller controller = m_controllers.get(i);
            switch ( controller.getControllerType() )
            {
                case CodecFactory.CONTROLLER_AV:
                case CodecFactory.CONTROLLER_PHOTO:
                    controller.receivedEvent(event);
                    break;
                default:
                    CastLogger.e("Unknown event of controller");
                    break;
            }
        }
    }
    
    private boolean m_responsed_query = false;
    private boolean m_responsed_healthcheck = false;
    
    private final String TAG = "CastSession";
    private List<Controller> m_controllers = null;
    private Communicator m_communicator = null;
    private Thread m_query_thread = null;
    private Thread m_healthcheck_thread = null;
    private List<CastSession.Listener> m_listeners = null;
}
