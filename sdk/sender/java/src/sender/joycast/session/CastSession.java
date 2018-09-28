package sender.joycast.session;

import sender.joycast.controller.AVController;
import sender.joycast.controller.Controller;
import sender.joycast.controller.PhotoController;
import sender.joycast.dial.DialServer;

/**
 * SessionManager를 통하여 생성된 CastSession는 JoyCast Stick websocket Relay와 직접 비동기 통신을 수행한다.
 * @see Session Controller.Listener
 */
public abstract class CastSession extends Session
{
    public interface Listener extends Session.Listener
    {
        /**
         * JoyCast Receiver가 AVController를 지원할 경우 Sender사용자에게 controller를 알려준다. 
         * @param session 연결된 CastSession 객체 
         * @param controller Receiver가 지원가능한 Controller 객체  
         */
        void onAddedSupportingAvController(CastSession session, Controller controller);
        void onAddedSupportingPhotoController(CastSession session, Controller controller);
        void onHummingAlived();
        void onHummingNotAlived();
    }
    
    /**
     * CastSession에 이벤트 핸들러를 등록한다.  
     * especially to know for Controller added by Server
     */
    public abstract void addEventListener(CastSession.Listener listener);
    
    /**
     * 등록된 이벤트 핸들러를 제거한다. 
     */
    public abstract void removeEventListener(CastSession.Listener listener);
    
    /**
     * 목표 JoyCast WebSocket Relay Server와 연결한다.  
     * @param server
     * @return return true if it's succeed else return false
     */
    public abstract boolean start(DialServer server, String protocol);
    
    /**
     * 목표 JoyCast WebSocket Relay Server와 연결한다. 
     * @param ip 
     * @param port 
     * @return return true if it's succeed else return false
     */
    public abstract boolean start(String ip, int port, String protocol);
    
    /**
     * 목표 JoyCast WebSocket Relay Server와 연결 해제한다. 
     */
    public abstract void stop();
    
    /**
     * JoyCast Receiver가 지원가능한 AVController를 획득한다. 
     * Receiver가 지원하지 않을 경우, null을 획득한다.
     * @return AVController
     */
    public abstract AVController getAvController();
    
    /**
     * JoyCast Receiver가 지원가능한 PhotoController를 획득한다.
     * Receiver가 지원하지 않을 경우, null을 획득한다.
     * @return PhotoController
     */
    public abstract PhotoController getPhotoController();
    
    /**
     * JoyCast Receiver가 동작 중인지 확인한다.동작 여부는 onHummingAlived리스너를 
     * 통하여 확인된다. timeout_msec내에 동작 여부가 확인되지 않을 경우, onHummingNotAlived
     * 리스너가 호출된다. 
     * @param timeout_msec Receiver 헬스 체크 타임 아웃 밀리초 
     * @return healthcheck용 패킷이 정상 송신된 경우 true를 반환한다 최종 확인은 리스너를 통해 확인해야 한다.  
     */
    public abstract boolean healthCheck(final long timeout_msec);
}
