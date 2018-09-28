package sender.joycast.controller;

import java.util.ArrayList;
import java.util.List;

import sender.joycast.codec.CodecFactory;
import sender.joycast.codec.request.avcontrol.CustomMessage;
import sender.joycast.codec.request.avcontrol.FullScreen;
import sender.joycast.codec.request.avcontrol.JumpBackward;
import sender.joycast.codec.request.avcontrol.JumpForward;
import sender.joycast.codec.request.avcontrol.MoveScreen;
import sender.joycast.codec.request.avcontrol.Mute;
import sender.joycast.codec.request.avcontrol.Pause;
import sender.joycast.codec.request.avcontrol.Play;
import sender.joycast.codec.request.avcontrol.PlayFaster;
import sender.joycast.codec.request.avcontrol.PlaySlower;
import sender.joycast.codec.request.avcontrol.PlaybackRate;
import sender.joycast.codec.request.avcontrol.Seek;
import sender.joycast.codec.request.avcontrol.Stop;
import sender.joycast.codec.request.avcontrol.Unmute;
import sender.joycast.codec.request.avcontrol.VolumeDown;
import sender.joycast.codec.request.avcontrol.VolumeUp;
import sender.joycast.codec.response.Event;
import sender.joycast.codec.response.Param;
import sender.joycast.codec.response.Response;
import sender.joycast.util.CastLogger;

/**
 * AVController is to control video which is placed in stick
 */
public class AVControllerImpl extends Controller implements AVController
{
    /**
     * This constructor should not be called from User application
     */
    public AVControllerImpl(Controller.Listener listener) 
    {
        super(listener);
        
        m_controller_type = CodecFactory.CONTROLLER_AV;
    }
    
    /**
     * Function to play video in stick with title and url of video
     * @param title video title
     * @param url video URL
     * @return return true if it's succeed to send message else return false
     */
    public boolean play(String title, String url)
    {
        Play play = new Play(m_controller_id, m_controller_type, title, url);
        return super.m_listener.onSendRequest(CodecFactory.encode(play));   
    }
    
    /**
     * Function to play video in stick with url of video
     * @param url video URL
     * @return return true if it's succeed to send message else return false
     */
    public boolean play(String url) 
    {
        Play play = new Play(m_controller_id, m_controller_type, url);
        return super.m_listener.onSendRequest(CodecFactory.encode(play));
    }
    
    /**
     * Function to play video, video will play if video paused.
     * @return return true if it's succeed to send message else return false
     */
    public boolean play()
    {
        Play play = new Play(m_controller_id, m_controller_type);
        return super.m_listener.onSendRequest(CodecFactory.encode(play));
    }
    
    /**
     * playbackRate doesn't work well on HTML5, fastforward only working
     * @param rate play rate. It refers to the attribute of video HTML5 element.
     * @return return true if it's succeed to send message else return false
     */
    public boolean playbackRate(double rate) 
    {
        PlaybackRate playbackrate = 
                new PlaybackRate(m_controller_id, m_controller_type, String.valueOf(rate));
        return super.m_listener.onSendRequest(CodecFactory.encode(playbackrate));
    }
    
    public boolean playFaster()
    {
        PlayFaster playfaster = new PlayFaster(m_controller_id, m_controller_type);
        return super.m_listener.onSendRequest(CodecFactory.encode(playfaster));
    }
    
    public boolean playSlower()
    {
       PlaySlower playslower = new PlaySlower(m_controller_id, m_controller_type);
       return super.m_listener.onSendRequest(CodecFactory.encode(playslower));
    }
    
    /**
     * Function to pause video
     * @return return true if it's succeed to send message else return false
     */
    public boolean pause() 
    {
        Pause pause = new Pause(m_controller_id, m_controller_type);
        return super.m_listener.onSendRequest(CodecFactory.encode(pause));
    }
    
    /**
     * Function to stop playing video
     * @return return true if it's succeed to send message else return false
     */
    public boolean stop() 
    {
        Stop stop = new Stop(m_controller_id, m_controller_type);
        return super.m_listener.onSendRequest(CodecFactory.encode(stop));
    }
    
    /**
     * Function to seek position of video
     * @param position to seek
     * @return return true if it's succeed to send message else return false
     */
    public boolean seek(double position) 
    {
        Seek seek = new Seek(m_controller_id, m_controller_type, String.valueOf(position));
        return super.m_listener.onSendRequest(CodecFactory.encode(seek));
    }
    
    /**
     * Function to turn up the volume of video
     * @return return true if it's succeed to send message else return false
     */
    public boolean volumeUp() 
    {
        VolumeUp vol = new VolumeUp(m_controller_id, m_controller_type);
        return super.m_listener.onSendRequest(CodecFactory.encode(vol));
    }
    
    /**
     * Function to turn down the volume of video
     * @return return true if it's succeed to send message else return false
     */
    public boolean volumeDown()
    {
        VolumeDown vol = new VolumeDown(m_controller_id, m_controller_type);
        return super.m_listener.onSendRequest(CodecFactory.encode(vol));
    }
    
    /**
     * Function to mute video
     * @return return true if it's succeed to send message else return false
     */
    public boolean mute()
    {
        Mute mute = new Mute(m_controller_id, m_controller_type);
        return super.m_listener.onSendRequest(CodecFactory.encode(mute));
    }
    
    /**
     * Function to not mute video
     * @return return true if it's succeed to send message else return false
     */
    public boolean unmute()
    {
        Unmute unmute = new Unmute(m_controller_id, m_controller_type);
        return super.m_listener.onSendRequest(CodecFactory.encode(unmute));
    }
    
    /**
     * Function to send message customized by User application
     * @param name name of custom message to send
     * @param value value of name of custom message to send
     * @return return true if it's succeed to send message else return false
     */
    public boolean sendCustomMessage(String name, String value)
    {
        CustomMessage custom_message = new CustomMessage(m_controller_id, m_controller_type, name, value);
        return super.m_listener.onSendRequest(CodecFactory.encode(custom_message));
    }
    
    /**
     * Function to seek forward with interval
     * @param interval to jump forward as seconds
     * @return return true if it's succeed to send message else return false
     */
    public boolean jumpForward(double interval)
    {
        JumpForward jump_forward = new JumpForward(m_controller_id, m_controller_type, String.valueOf(interval));
        return super.m_listener.onSendRequest(CodecFactory.encode(jump_forward));
    }
    
    /**
     * Function to seek backward with interval
     * @param interval to jump backward as seconds
     * @return return true if it's succeed to send message else return false
     */
    public boolean jumpBackward(double interval)
    {
        JumpBackward jump_backward = new JumpBackward(m_controller_id, m_controller_type, String.valueOf(interval));
        return super.m_listener.onSendRequest(CodecFactory.encode(jump_backward));
    }
    
    /**
     * Function to move position of video on screen
     * @param x x coordinate of video
     * @param y y coordinate of video
     * @param w width of video
     * @param h height of video
     * @return return true if it's succeed to send message else return false
     */
    public boolean moveScreen(int x, int y, int w, int h)
    {
        MoveScreen move_screen = new MoveScreen(m_controller_id, m_controller_type, 
                String.valueOf(x), String.valueOf(y), String.valueOf(w), String.valueOf(h));
        return super.m_listener.onSendRequest(CodecFactory.encode(move_screen));
    }
    
    /**
     * Function to change video as full screen
     * @return return true if it's succeed to send message else return false
     */
    public boolean fullScreen()
    {
        FullScreen full_screen = new FullScreen(m_controller_id, m_controller_type);
        return super.m_listener.onSendRequest(CodecFactory.encode(full_screen));
    }
    
    /**
     * Function to register listener to listen status of AVController
     * @param listener AVController.Listener
     */
    public void addEventListener(AVController.Listener listener) 
    {
        m_listeners.add(listener);
    }
    
    /**
     * Function to release listener
     * @param listener AVController.Listener registered before
     */
    public void removeEventListener(AVController.Listener listener) 
    {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            if ( listener == m_listeners.get(i) ) {
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
            case Event.AV_ON_ABORT:
                CastLogger.d(TAG, "AV_ON_ABORT");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onStopped();
                }
                break;
            case Event.AV_ON_ENDED:
                CastLogger.d(TAG, "AV_ON_ENDED");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onEnded();
                }
                break;
            case Event.AV_ON_ERROR:
                CastLogger.d(TAG, "AV_ON_ERROR");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onError();
                }
                break;
            case Event.AV_ON_LOAD:
                CastLogger.d(TAG, "AV_ON_LOAD");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onLoad(Double.valueOf(params.get(0).getValue()));
                }
                break;
            case Event.AV_ON_PAUSE:
                CastLogger.d(TAG, "AV_ON_PAUSE");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onPaused();
                }
                break;
            case Event.AV_ON_PLAY:
                CastLogger.d(TAG, "AV_ON_PLAY");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onPlayed();
                }
                break;
            case Event.AV_ON_PLAYRATE_CHANGE:
                CastLogger.d(TAG, "AV_ON_PLAYRATE_CHANGE");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onPlayRateChanged(Double.valueOf(params.get(0).getValue()));
                }
                break;
            case Event.AV_ON_PLAYING:
                CastLogger.d(TAG, "AV_ON_PLAYING");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onPlaying();
                }
                break;
            case Event.AV_ON_DURATIONCHANGE:
                CastLogger.d(TAG, "AV_ON_DURATIONCHANGE [" + params.get(0).getValue() + "]");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onDurationChanged(Double.valueOf(params.get(0).getValue()));
                }
                break;
            case Event.AV_ON_POSITIONCHANGE:
                CastLogger.d(TAG, "AV_ON_POSITIONCHANGE [" + params.get(1).getValue() + " / " + params.get(0).getValue() + "]");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    CastLogger.d(TAG, "called onPositionChanged");
                    m_listeners.get(i).onPositionChanged(
                            Double.valueOf(params.get(0).getValue()),
                            Double.valueOf(params.get(1).getValue()));
                }
                break;
            case Event.AV_ON_SEEKED:
                CastLogger.d(TAG, "AV_ON_SEEKED");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onSeeked();
                }
                break;
            case Event.AV_ON_SEEKING:
                CastLogger.d(TAG, "AV_ON_SEEKING");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onSeeking();
                }
                break;
            case Event.AV_ON_VOLUME_CHANGE:
                CastLogger.d(TAG, "AV_ON_VOLUME_CHANGE");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onVolumeChanged(Double.valueOf(params.get(0).getValue()));
                }
                break;
            case Event.AV_ON_MUTE:
                CastLogger.d(TAG, "AV_ON_MUTE");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onMute();
                }                
                break;
            case Event.AV_ON_UNMUTE:
                CastLogger.d(TAG, "AV_ON_UNMUTE");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onUnmute();
                }                
                break;
            case Event.AV_ON_WAITING:
                CastLogger.d(TAG, "AV_ON_WAITING");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onWaiting();
                }   
                break;
            case Event.AV_ON_CUSTOM_MESSAGE:
                CastLogger.d(TAG, "AV_ON_CUSTOM_MESSAGE");
                for ( int i=0; i<m_listeners.size(); i++ ) 
                {
                    m_listeners.get(i).onReceivedCustomMessage(
                            String.valueOf(params.get(0).getValue()),
                            String.valueOf(params.get(1).getValue()));
                }
                break;
            default:
                CastLogger.e(TAG, "Unknown event");
                break;
        }
    }
    
    private final String TAG = "AVController";
    private List<AVController.Listener> m_listeners = new ArrayList<AVController.Listener>();
}
