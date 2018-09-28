package sender.joycast.session;

import java.util.ArrayList;
import java.util.List;

/**
 * CastSession의 부모 클래스로서, WebSocket Relay와 (물리적) 세션 관계를 추상화한다. 
 */
public abstract class Session 
{
    public interface Listener 
    {
        /**
         * WebSocket Relay Server와 연결을 시작할 때 통지된다.   
         * @param session
         */
        public void onSessionStarting(Session session); 
        
        /**
         * WebSocket Relay Server 연결 시도가 실패할 때 통지된다.  
         * @param session
         * @param error
         */
        public void onSessionStartFailed(Session session, int error);
        
        /**
         * WebSocket Relay Server와 연결 완료 되었을 때 통지된다.  
         * @param session
         */
        public void onSessionStarted(Session session); 

        /**
         * WebSocket Relay Server와 연결 종료 되었을 때 통지된다.  
         * between relay server and CastSession
         * @param session
         * @param reason
         */
        public void onSessionEnded(Session session, int reason); 
        
        public void onSessionResumeFailed(Session session, int error); 
        public void onSessionResuming(Session session); 
        public void onSessionResumed(Session session); 
    }
    
    public enum SessionState 
    {
        CONNECTED, 
        CONNECTING,
        DISCONNECTED,
        NO_DEVICES_AVAILABLE
    }

    /**
     * WebSocket Relay Server와 연결여부를 확인한다. 
     * @return return true if it already connected.
     */
    public boolean isConnected() 
    {
        if ( m_session_state == SessionState.CONNECTED )
            return true;
        return false;
    }
    
    /**
     * WebSocket Relay Server와 연결중인지 확인한다. 
     * @return return true if it's trying to connect
     */
    public boolean isConnecting() 
    {
        if ( m_session_state == SessionState.CONNECTING ) 
            return true;
        return false;
    }
    
    /**
     * WebSocket Relay Server와 연결해제되었는지 확인한다. 
     * @return return true if it's already disconnected.
     */
    public boolean isDisconnected() 
    {
        if ( m_session_state == SessionState.DISCONNECTED )
            return true;
        return false;
    }
    
    /**
     * Session 상태를 확인하기 위해 이벤트 핸들러를 등록한다. 
     * @param listener
     */
    public void addEventListener(Session.Listener listener) 
    {
        m_session_listeners.add(listener);
    }
    
    /**
     * 등록된 이벤트 핸들러를 제거한다. 
     * @param listener
     */
    public void removeEventListener(Session.Listener listener) 
    {
        for ( int i=0; i<m_session_listeners.size(); i++ ) 
        {
            if ( m_session_listeners.get(i) == listener ) 
            {
                m_session_listeners.remove(i);
                break;
            }
        }
    }
    
    /**
     * 연결된 WebSocket Relay Server의 URI를 획득한다. 
     * @return ws:// 프로토콜로 시작하는 URI 형식을 리턴한다. 
     */
    public String getConnectedServerUri() {
        return m_connected_server_uri;
    }
    
    private final String TAG = "Session";
    protected String m_connected_server_uri = "";
    protected SessionState m_session_state = SessionState.DISCONNECTED;
    protected List<Session.Listener> m_session_listeners = new ArrayList<Session.Listener>();
    
}
