package sender.joycast.codec.response;

import sender.joycast.codec.request.Request;

public class Event extends Request
{
    public Event() 
    {
        super();
    }
    
    public static final String EVENT = "event";
    
    public static final String AV_ON_POSITIONCHANGE = "on_position_change";
    public static final String AV_ON_DURATIONCHANGE = "on_duration_change";
    public static final String AV_ON_LOAD = "on_load";
    public static final String AV_ON_PLAY = "on_play";
    public static final String AV_ON_PLAYRATE_CHANGE = "on_playrate_change";
    public static final String AV_ON_PLAYING = "on_playing";
    public static final String AV_ON_PAUSE = "on_pause";
    public static final String AV_ON_SEEKING = "on_seeking";
    public static final String AV_ON_SEEKED = "on_seeked";
    public static final String AV_ON_VOLUME_CHANGE = "on_volume_change";
    public static final String AV_ON_MUTE = "on_mute";
    public static final String AV_ON_UNMUTE = "on_unmute";
    public static final String AV_ON_ENDED = "on_ended";
    public static final String AV_ON_ERROR = "on_error";
    public static final String AV_ON_ABORT = "on_abort";
    public static final String AV_ON_CUSTOM_MESSAGE = "on_custom_message";
    public static final String AV_ON_WAITING = "on_waiting";
    
    public static final String PT_ON_LOAD = "on_load";
    public static final String PT_ON_UNLOAD = "on_unload";
}
