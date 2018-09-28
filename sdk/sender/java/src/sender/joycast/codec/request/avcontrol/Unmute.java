package sender.joycast.codec.request.avcontrol;

import sender.joycast.codec.request.Request;

/**
 * Unmute of Request messages is to unmute video playing 
 * @see Request AvController
 */
public class Unmute extends Request
{
    public Unmute(String controller_id, String controller_type) 
    {
        super(controller_id, controller_type);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "unmute";
    public static final String ACTION_TYPE = "request";
}
