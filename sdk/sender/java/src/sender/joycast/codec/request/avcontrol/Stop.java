package sender.joycast.codec.request.avcontrol;

import sender.joycast.codec.request.Request;

/**
 * Stop of Request messages is to stop playing video 
 * @see Request AvController
 */
public class Stop extends Request 
{
    public Stop(String controller_id, String controller_type) 
    {
        super(controller_id, controller_type);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "stop";
    public static final String ACTION_TYPE = "request";
}
