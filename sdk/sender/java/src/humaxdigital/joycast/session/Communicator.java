package humaxdigital.joycast.session;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.neovisionaries.ws.client.WebSocketException;

import humaxdigital.joycast.constant.Configuration;
import humaxdigital.joycast.util.WebSocketClient;

/**
 * Communicator is to manage websocket client functionalities
 */
public class Communicator implements WebSocketClient.Listener, Runnable 
{
    public interface Listener 
    {
        public void onConnected(String server_uri);
        public void onReceived(String event);
        public void onError(int error);
        public void onDisconnected();
    }

    public Communicator() 
    {
        m_send_queue = new ArrayBlockingQueue<String>(Configuration.MESSAGE_QUEUE_MAX);
        m_thread = new Thread(this);
        m_thread.start();
    }
    
    public void setEventListener(Communicator.Listener listener) 
    {
        m_listener = listener;
    }
    
    public void destroy() 
    {
        if ( m_websocket_client != null )
        {
            m_websocket_client.disconnect();
        }
        
        this.m_is_run = false;
        m_thread.interrupt();
        m_thread = null;
        m_send_queue.clear();
        m_send_queue = null;
    }
    
    public void connect(String ip, int port, String protocol) throws IOException, WebSocketException 
    {
        m_websocket_client = new WebSocketClient(ip, port, protocol, this);
        m_websocket_client.connect();
        
    }
    
    public void disconnect()
    {
        if ( m_websocket_client != null ) 
        {
            m_websocket_client.disconnect();
        }
    }
    
    @Override
    public void onConnected(String server_uri) 
    {
        m_listener.onConnected(server_uri);
    }

    @Override
    public void onReceived(String event) 
    {
        m_listener.onReceived(event);
    }

    @Override
    public void onError(int error) 
    {
        m_listener.onError(error);
    }

    @Override
    public void onDisconnected() 
    {
        m_listener.onDisconnected();
    }
    
    public synchronized boolean send(String message) 
    {
        
        try 
        {
            if ( m_send_queue != null && message != null) 
            {
                m_send_queue.put(message);
            }
        }
        catch (InterruptedException e) 
        {
            e.printStackTrace();
            return false;
        }
        return true;
        
        /**
         * @note Or send directly by using m_websocket_client
         */
//        return m_websocket_client.send(message);
    }
    
    /**
     * @note Please use queue if this needs message queuing to send \n
     * Fix up send(String message) to use below
     * @code m_websocket_client_wrapper.send(message) to m_queue.put(message) 
     */
    @Override
    public void run() 
    {
        while ( m_is_run ) 
        {
            try 
            {
                Thread.sleep(100);
                if ( null != m_send_queue ) 
                {
                    String message = (String)m_send_queue.take();
                    m_websocket_client.send(message);
                }
            } 
            catch (InterruptedException e) 
            {
            }
        }
    }
    
    private final String TAG = "Communicator";
    private Communicator.Listener m_listener;
    private BlockingQueue<String> m_send_queue;
    private boolean m_is_run = true;
    private Thread m_thread;
    private WebSocketClient m_websocket_client;
}
