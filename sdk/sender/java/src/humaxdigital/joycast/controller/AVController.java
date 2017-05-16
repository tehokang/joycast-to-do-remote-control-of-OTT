package humaxdigital.joycast.controller;

/**
 * JoyCast Receiver지원 가능한 컨트롤러중 Video/Audio 기능을 담당하는 클래스이다. 
 * Receiver의 AVController과 1:1 추상화되어 사용자 명령을 Receiver의 AVController에 전달한다. 
 */
public interface AVController
{
    /**
     * AVController의 동작 상태에 대한 이벤트 핸들러 정의 
     */
    public interface Listener 
    {
        /**
         * JoyCast Receiver가 Video 재생을 위한 로드가 완료되었을 때 받아지는 이벤트 
         * @param duration total duration of video to play
         */
        public void onLoad(double duration);
        
        /**
         * JoyCast Receiver가 Video 재생을 시작하였을 때 받아지는 이벤트 
         */
        public void onPlayed();
        
        /**
         * JoyCast Receiver의 Video 재생 속도가 변경되었을 때 받아지는 이벤트 
         * @param playrate
         */
        public void onPlayRateChanged(double playrate);
        
        /**
         * JoyCast Receiver의 Video가 정상 재생중임을 랜덤하게 알려주는 이벤트 
         */
        public void onPlaying();
        
        /**
         * JoyCast Receiver의 Video가 일시정지되었을 때 받아지는 이벤트 
         */
        public void onPaused();
        
        /**
         * JoyCast Receiver의 Video가 종료되었을 때 받아지는 이벤트 
         */
        public void onStopped();
        
        /**
         * JoyCast Receiver의 Video가 seeking 중일때 받아지는 이벤트 
         */
        public void onSeeking();
        
        /**
         * JoyCast Receiver의 Video가 seek 완료 되었을 때 받아지는 이벤트 
         */
        public void onSeeked();
        
        /**
         * JoyCast Receiver의 Video볼륨이 변경되었을 때 받아지는 이벤트 
         * @param volume 
         */
        public void onVolumeChanged(double volume);
        
        /**
         * JoyCast Receiver의 Video 볼륨이 mute되었을 때 받아지는 이벤트 
         */
        public void onMute();
        
        /**
         * JoyCast Receiver의 Video 볼륨이 unmute되었을 때 받아지는 이벤트 
         */
        public void onUnmute();
        
        /**
         * JoyCast Receiver의 Video가 End of stream(file)에 도달하였을 때 받아지는 이벤트 
         */
        public void onEnded();
        
        /**
         * JoyCast Receiver의 Video에서 Error가 발생하였을 때 받아지는 이벤트  
         */
        public void onError();
        
        /**
         * JoyCast Receiver의 Video가 재생하기전 버퍼링할때 받아지는 이벤트 
         */
        public void onWaiting();
        
        /**
         * JoyCast Receiver Video의 재생시간이 변경되었을 때 받아지는 이벤트 
         * @param duration
         */
        public void onDurationChanged(double duration);
        
        /**
         * JoyCast Receiver Video의 현재 재생시간이 변경되었을 때 받아지는 이벤트 
         * @param position
         */
        public void onPositionChanged(double duration, double position);

        /**
         * JoyCast Receiver로부터 Custom message 를 받았을 때 이벤트 
         * @param name
         * @param value
         */
        public void onReceivedCustomMessage(String name, String value);
    }
    
    /**
     * JoyCast Receiver의 AVController에게 재생 요청
     * @param title video title
     * @param url video URL
     * @return return true if it's succeed to send message else return false
     */
    public boolean play(String title, String url);
    
    /**
     * JoyCast Receiver의 AVController에게 재생 요청 
     * @param url video URL
     * @return return true if it's succeed to send message else return false
     */
    public boolean play(String url);
    
    /**
     * JoyCast Receiver의 AVController에게 재생 요청 
     * @return return true if it's succeed to send message else return false
     */
    public boolean play();
    
    /**
     * JoyCast Receiver의 Video 재생 속도 조절 요청 
     * @param rate play rate. It refers to the attribute of video HTML5 element.
     * @return return true if it's succeed to send message else return false
     */
    public boolean playbackRate(double rate); 
    
    /**
     * JoyCast Receiver의 Video 재생속도 증가 요청(+0.1) 
     * @return return true if it's suucceed to send message else return false
     */
    public boolean playFaster();
    
    /**
     * JoyCast Receiver의 Video 재생속도 감소 요청(-0.1)
     * @return return true if it's suucceed to send message else return false
     */
    public boolean playSlower();
    
    /**
     * JoyCast Receiver의 Video 일시정지 요청 
     * @return return true if it's succeed to send message else return false
     */
    public boolean pause(); 
    
    /**
     * JoyCast Receiver의 Video 정지 요청 
     * @return return true if it's succeed to send message else return false
     */
    public boolean stop();
    
    /**
     * JoyCast Receiver의 Video재생위치 변경 요청 
     * @param position to seek
     * @return return true if it's succeed to send message else return false
     */
    public boolean seek(double position); 
    
    /**
     * JoyCast Receiver의 Video 볼륨 업 요청 (+0.1) (전체 볼륨 0.0~1.0)
     * @return return true if it's succeed to send message else return false
     */
    public boolean volumeUp(); 
    
    /**
     * JoyCast Receiver의 Video 볼륨 다운 요청(-0.1) (전체 볼륨 0.0~1.0)
     * @return return true if it's succeed to send message else return false
     */
    public boolean volumeDown();
    
    /**
     * JoyCast Receiver의 Video 볼륨 무음 요청 
     * @return return true if it's succeed to send message else return false
     */
    public boolean mute();
    
    /**
     * JoyCast Receiver의 Video 볼륨무음 해지 요청 
     * @return return true if it's succeed to send message else return false
     */
    public boolean unmute();
    
    /**
     * JoyCast Receiver에게 Custom message 전송 요청 
     * @param name name of custom message to send
     * @param value value of name of custom message to send
     * @return return true if it's succeed to send message else return false
     */
    public boolean sendCustomMessage(String name, String value);
    
    /**
     * Function to seek forward with interval
     * @param interval to jump forward as seconds
     * @return return true if it's succeed to send message else return false
     */
    public boolean jumpForward(double interval);
    
    /**
     * Function to seek backward with interval
     * @param interval to jump backward as seconds
     * @return return true if it's succeed to send message else return false
     */
    public boolean jumpBackward(double interval);
    
    /**
     * JoyCast Receiver의 Video 화면 위치 조정 요청 
     * @param x x coordinate of video
     * @param y y coordinate of video
     * @param w width of video
     * @param h height of video
     * @return return true if it's succeed to send message else return false
     */
    public boolean moveScreen(int x, int y, int w, int h);
    
    /**
     * JoyCast Receiver의 Video 전체 화면 요청 
     * @return return true if it's succeed to send message else return false
     */
    public boolean fullScreen();
    
    /**
     * Function to register listener to listen status of AVController
     * @param listener AVController.Listener
     */
    public void addEventListener(AVController.Listener listener); 
    
    /**
     * Function to release listener
     * @param listener AVController.Listener registered before
     */
    public void removeEventListener(AVController.Listener listener); 
}
