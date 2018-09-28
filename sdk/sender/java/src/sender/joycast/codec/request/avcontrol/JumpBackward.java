package sender.joycast.codec.request.avcontrol;

import java.util.ArrayList;
import java.util.List;

import sender.joycast.codec.request.Request;
import sender.joycast.codec.request.Request.Body;
import sender.joycast.codec.response.Param;

/**
 * JumpBackward of Request messages is to seek backward with interval 
 * interval should be double type when User call
 */
public class JumpBackward extends Request
{
    public JumpBackward(String controller_id, String controller_type, String interval) 
    {
        super(controller_id, controller_type);
        
        List<Param> params = new ArrayList<Param>();
        Param p1 = new Param("double",  "interval", interval);
        params.add(p1);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        body.setParamList(params);
        
        super.setBody(body);
    }
    public static final String ACTION_NAME = "jump_backward";
    public static final String ACTION_TYPE = "request";
}
