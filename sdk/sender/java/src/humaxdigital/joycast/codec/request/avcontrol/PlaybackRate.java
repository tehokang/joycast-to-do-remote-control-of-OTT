package humaxdigital.joycast.codec.request.avcontrol;

import java.util.ArrayList;
import java.util.List;

import humaxdigital.joycast.codec.request.Request;
import humaxdigital.joycast.codec.request.Request.Body;
import humaxdigital.joycast.codec.response.Param;

/**
 * PlaybackRate of Request messages is to change rate of stick video playing
 * @see Request AvController
 */
public class PlaybackRate extends Request
{
    public PlaybackRate(String controller_id, String controller_type, String rate) 
    {
        super(controller_id, controller_type);
        
        List<Param> params = new ArrayList<Param>();
        Param p1 = new Param("float",  "playbackrate", rate);
        params.add(p1);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        body.setParamList(params);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "playbackrate";
    public static final String ACTION_TYPE = "request";
}
