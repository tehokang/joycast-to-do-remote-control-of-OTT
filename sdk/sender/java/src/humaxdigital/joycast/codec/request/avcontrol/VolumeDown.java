package humaxdigital.joycast.codec.request.avcontrol;

import java.util.ArrayList;
import java.util.List;

import humaxdigital.joycast.codec.request.Request;
import humaxdigital.joycast.codec.request.Request.Body;
import humaxdigital.joycast.codec.response.Param;

/**
 * VolumeDown of Request messages is to turn down the volume of video playing 
 * @see Request AvController
 */
public class VolumeDown extends Request
{

    public VolumeDown(String controller_id, String controller_type) 
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
    
    public static final String ACTION_NAME = "volumedown";
    public static final String ACTION_TYPE = "request";

}
