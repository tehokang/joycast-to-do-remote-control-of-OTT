package sender.joycast.codec.request.avcontrol;

import java.util.ArrayList;
import java.util.List;

import sender.joycast.codec.request.Request;
import sender.joycast.codec.request.Request.Body;
import sender.joycast.codec.response.Param;

/**
 * Seek of Request messages is to move current position of video playing 
 * @see Request AvController
 */
public class Seek extends Request 
{
    public Seek(String controller_id, String controller_type, String pos) 
    {
        super(controller_id, controller_type);
        
        Body body = new Body();
        
        List<Param> params = new ArrayList<Param>();
        Param p1 = new Param("double",  "position", pos);
        params.add(p1);
        
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        body.setParamList(params);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "seek";
    public static final String ACTION_TYPE = "request";
}
