package humaxdigital.joycast.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

public class WebSocketClient 
{
    public interface Listener 
    {
        public void onConnected(String server_uri);
        public void onReceived(String message);
        public void onError(int error);
        public void onDisconnected();
    }

    public WebSocketClient(String ip, int port, String protocol, WebSocketClient.Listener listener) throws IOException, WebSocketException 
    {
        m_server_uri = "ws://" + ip + ":" + String.valueOf(port) + "/" + protocol;
        m_listener = listener;
        
        m_websocket = new WebSocketFactory().createSocket(m_server_uri);
        m_websocket.getSocket().setKeepAlive(true);
        
        m_websocket.addListener(new WebSocketListener() 
        {
            @Override
            public void onBinaryMessage(WebSocket arg0, byte[] arg1) 
            {
                CastLogger.d(TAG, "onBinaryMessage");
            }

            @Override
            public void onCloseFrame(WebSocket arg0, WebSocketFrame arg1) 
            {
                CastLogger.d(TAG, "onCloseFrame");
            }

            @Override
            public void onConnected(WebSocket arg0, Map<String, List<String>> arg1) 
            {
                CastLogger.d(TAG, "onConnected");
                CastLogger.d(TAG, "WebSocket : " + arg0.getURI().toString());
                
                Iterator<String> keys = arg1.keySet().iterator();
                while( keys.hasNext() )
                {
                    String key = keys.next();
                    List<String> values = arg1.get(key);
                    
                    CastLogger.d( String.format("key : %s ", key));
                    for ( int i=0; i<values.size();i ++) 
                    {
                        CastLogger.d( String.format("value : %s",  values.get(i)));
                    }
                }
                m_listener.onConnected(arg0.getURI().toString());
            }

            @Override
            public void onContinuationFrame(WebSocket arg0, WebSocketFrame arg1) 
            {
                CastLogger.d(TAG, "onContinuationFrame");
            }

            @Override
            public void onDisconnected(WebSocket arg0, WebSocketFrame arg1, WebSocketFrame arg2, boolean arg3) 
            {
                CastLogger.d(TAG, "onDisconnected");
                m_listener.onDisconnected();
            }

            @Override
            public void onError(WebSocket arg0, WebSocketException arg1) 
            {
                CastLogger.e(TAG, "onError");
                CastLogger.e(TAG, "Error code : " + arg1.getError().ordinal());
                CastLogger.e(TAG, "Error msg  : " + arg1.getMessage());
                m_listener.onError(arg1.getError().ordinal());
            }

            @Override
            public void onFrame(WebSocket arg0, WebSocketFrame arg1) 
            {
                CastLogger.d(TAG, "onFrame");
            }

            @Override
            public void onFrameError(WebSocket arg0, WebSocketException arg1, WebSocketFrame arg2) 
            {
                CastLogger.d(TAG, "onFrameError");
            }

            @Override
            public void onFrameSent(WebSocket arg0, WebSocketFrame arg1) 
            {
                CastLogger.d(TAG, "onFrameSent");
                CastLogger.d("-----------------");
                CastLogger.d("Sent Json : ");
                CastLogger.d(arg1.getPayloadText());
                CastLogger.d("-----------------");
                
            }

            @Override
            public void onFrameUnsent(WebSocket arg0, WebSocketFrame arg1) 
            {
                CastLogger.d(TAG, "onFrameUnsent");
            }

            @Override
            public void onMessageError(WebSocket arg0, WebSocketException arg1, List<WebSocketFrame> arg2) 
            {
                CastLogger.d("onMessageError");
            }

            @Override
            public void onPingFrame(WebSocket arg0, WebSocketFrame arg1) 
            {
                CastLogger.d(TAG, "onPingFrame");
            }

            @Override
            public void onPongFrame(WebSocket arg0, WebSocketFrame arg1) 
            {
                CastLogger.d(TAG, "onPongFrame");
            }

            @Override
            public void onSendError(WebSocket arg0, WebSocketException arg1, WebSocketFrame arg2) 
            {
                CastLogger.d(TAG, "onSendError");
            }

            @Override
            public void onStateChanged(WebSocket arg0, WebSocketState arg1) 
            {
                CastLogger.d(TAG, "onStateChanged");
            }

            @Override
            public void onTextFrame(WebSocket arg0, WebSocketFrame arg1) 
            {
                CastLogger.d(TAG, "onTextFrame");
            }

            @Override
            public void onTextMessage(WebSocket arg0, String arg1) 
            {
                CastLogger.d(TAG, "onTextMessage");
                try
                {
                    CastLogger.d("-----------------");
                    CastLogger.d("Received Json : ");
                    CastLogger.d(arg1);
                    CastLogger.d("-----------------");
                    m_listener.onReceived(arg1);
                }
                catch(Exception e)
                {
                    /**
                     * @note Nothing to do
                     */
//                    e.printStackTrace();
                }
            }

            @Override
            public void onTextMessageError(WebSocket arg0, WebSocketException arg1, byte[] arg2) 
            {
                CastLogger.d(TAG, "onTextMessageError");
            }

            @Override
            public void onUnexpectedError(WebSocket arg0, WebSocketException arg1) 
            {
                CastLogger.d(TAG, "onUnexpectedError");
            }

            @Override
            public void onBinaryFrame(WebSocket arg0, WebSocketFrame arg1) 
            {
                CastLogger.d(TAG, "onBinaryFrame");
            }
        });
    }
    
    public void connect() throws WebSocketException 
    {
        m_websocket.connect();
    }
    
    public void disconnect() 
    {
        m_websocket.disconnect();
    }
    
    public boolean send(String message) 
    {
        try 
        {
            m_websocket.sendText(message);
        }
        catch(Exception e) 
        {
            return false;
        }
        return true;
    }
    
    private final String TAG = "WebSocketClient";
    private String m_server_uri;
    private WebSocketClient.Listener m_listener = null;
    private WebSocket m_websocket;
}
