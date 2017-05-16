package humaxdigital.joycast.codec.request.avcontrol;

import java.util.ArrayList;
import java.util.List;

import humaxdigital.joycast.codec.request.Request;
import humaxdigital.joycast.codec.request.Request.Body;
import humaxdigital.joycast.codec.response.Param;

/**
 * FullScreen of Request messages is to make fullscreen of stick
 */
public class FullScreen extends Request
{
    public FullScreen(String controller_id, String controller_type) 
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
    
    public static final String ACTION_NAME = "full_screen";
    public static final String ACTION_TYPE = "request";
}
