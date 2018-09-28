package sender.joycast;

import java.util.ArrayList;
import java.util.List;

import sender.joycast.session.CastSession;
import sender.joycast.session.CastSessionImpl;

/**
 * SessionManager는 JoyCast Stick에 탑재된 WebSocket Relay 통신 세션을 생성/소멸을 관리한다 
 */
public class SessionManager 
{
    /**
     * SessionManager 인스턴스는 singleton으로 관리된다.
     * 사용자는 직접 SessionManager를 생성하지 않아야 한다.
     */
    private SessionManager() 
    {
        
    }
    
    /**
     * 세션 인스턴스를 생성한다. 만들어진 인스턴스의 메소드를 통하여 세션을 설정할 수 있다.  
     * @see CastSession SessionManager.destroySession
     * @return CastSession instance
     */
    public static CastSession createSession() 
    {
        CastSession session = null ;
        try 
        {
            session = new CastSessionImpl();
            m_sessions.add(session);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return session;
    }
    
    /**
     * 생성된 인스턴스는 사용 완료 후 반드시 destroySession을 통하여 소멸시켜야 한다. 
     * @param session to destroy
     * @return return false if there is no match with CastSession you want to destory else return true.
     */
    public static boolean destroySession(CastSession session) 
    {
        for ( int i=0; i<m_sessions.size(); i++ ) 
        {
            if ( session == m_sessions.get(i) ) 
            {
                m_sessions.get(i).stop();
                m_sessions.remove(i);
                return true;
            }
        }
        return false;
    }
    
    private final String TAG = "SessionManager";
    private static List<CastSession> m_sessions = new ArrayList<CastSession>();
}
