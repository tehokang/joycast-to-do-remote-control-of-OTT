package humaxdigital.joycast.controller;

/**
 * JoyCast Receiver지원 가능한 컨트롤러중 Photo 기능을 담당하는 클래스이다. 
 * Receiver의 PhotoController과 1:1 추상화되어 사용자 명령을 Receiver의 PhotoController에 전달한다. 
 */
public interface PhotoController
{
    /**
     * PhotoController의 동작 상태에 대한 이벤트 핸들러 정의 
     */
    public interface Listener 
    {
        public void onLoaded(String url);
        public void onUnloaded();
    }
    
    /**
     * Function to show photo in stick 
     * @param url photo URL
     * @return return true if it's succeed to send message else return false
     */
    public boolean load(String url); 


    /**
     * Function to hide photo in stick 
     * @param url photo URL
     * @return return true if it's succeed to send message else return false
     */
    public boolean unload();
        
    /**
     * 이벤트 핸들러 등록 
     * @param listener
     */
    public void addEventListener(PhotoController.Listener listener);
    
    /**
     * 등록된 이벤트 핸들러 제거 
     * @param listener
     */
    public void removeEventListener(PhotoController.Listener listener);
}
