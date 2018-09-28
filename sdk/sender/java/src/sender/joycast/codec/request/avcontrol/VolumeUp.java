package sender.joycast.codec.request.avcontrol;

import java.util.ArrayList;
import java.util.List;

import sender.joycast.codec.request.Request;
import sender.joycast.codec.request.Request.Body;
import sender.joycast.codec.response.Param;

/**
 * VolumeUp of Request messages is to turn up the volume of video playing 
 * @see Request AvController
 */
public class VolumeUp extends Request 
{
    public VolumeUp(String controller_id, String controller_type) 
    {
        super(controller_id, controller_type);
        
        List<Param> params = new ArrayList<Param>();
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        body.setParamList(params);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "volumeup";
    public static final String ACTION_TYPE = "request";
}
