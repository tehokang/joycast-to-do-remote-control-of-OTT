/**
 *
 */
package sender.joycast;

import java.net.UnknownHostException;
import java.util.List;

import sender.joycast.dial.DialServer;

/**
 * DIAL Client 기능 클래스로서,
 * JoyCast Stick을 DDSP통하여 발견하고 어플리케이션을 실행시킨다. 
 */
public abstract class DiscoveryManager
{
    public interface Listener
    {
        /**
         * discover() 메소드 실행의 결과 이벤트
         * 매개변수로 전달받은 DialServer 속성을 읽은 후 원하는 JoyCast Stick에 
         * 어플리케이션을 실행할 수 있다. 
         * @param DIAL서버 
         */
        public void onSearched(DialServer server);
    }

    /**
     * DiscoveryManager는 싱글톤으로 구현되어 있다. 
     * 사용을 원하면 getInstance를 통해 인스턴스를 획득할 수 있다. 
     * @return DiscoveryManager 인스턴스
     */
    public static DiscoveryManager getInstance()
    {
        try
        {
            if ( m_instance == null )
            {
                m_instance = new DiscoveryManagerImpl();
            }
        }
        catch (UnknownHostException e)
        {
            return null;
        }
        return m_instance;
    }

    /**
     * getInstance를 통해 얻어진 인스턴스는 반드시 destoryInstance를 통하여 소멸시켜야 한다. 
     */
    public static void destroyInstance()
    {
        if ( m_instance == null ) {
            ((DiscoveryManagerImpl)m_instance).destroy();
            m_instance = null;
        }
    }

    /**
     * Infra Network에서 동작중인 DialServer를 검색한다. 
     * @return return true if it's succeed else return false
     */
    public abstract boolean discover();

    /**
     * discover() 메소드 실행을 중지시킨다.
     */
    public abstract void stopDiscovering();

    /**
     * discover() 실행 후 발견된 DialServer 리스트를 획득한다. 
     * onSearched() 이벤트가 발생하기 전까지 리스트는 비어있는 상태이다. 
     * @return list of DialServer you should check the length of list before you access specific element
     */
    public abstract List<DialServer> getDiscoveredDialServerList();

    /**
     * 발견된 DialServer에 DIAL Application을 실행시킨다.
     * @param onSearched 또는 getDiscoveredDialServerList로 획득한 DialServer 
     * @param DialServer에 실행될 목표 어플리케이션 이름 (e.g. JoyCast, YouTube, ...)
     * @return return true if it's succeed else return false
     * @see DiscoveryManager.Listener
     */
    public abstract boolean launch(DialServer server, String target_app);
    
    /**
     * DialServer에서 동작중인 Application을 종료시킨다.
     * @param server 종료시킬 목표 DialServer
     * @param target_app 종료시킬 목표 Application(e.g. JoyCast, YouTube, ...)
     * @return
     */
    public abstract boolean stopApplication(DialServer server, String target_app);
    
    /**
     * DiscoveryManager 동작을 듣기 위해 이벤트 핸들러를 등록한다.  
     * @param listener to register
     */
    public abstract void addEventListener(DiscoveryManager.Listener listener);

    /**
     * addEventListener로 등록된 이벤트 핸들로를 제거한다. 
     * @param listener to release
     */
    public abstract void removeEventListener(DiscoveryManager.Listener listener);

    private static DiscoveryManager m_instance = null;
}
