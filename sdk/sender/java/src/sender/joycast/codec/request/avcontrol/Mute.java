package sender.joycast.codec.request.avcontrol;

import sender.joycast.codec.request.Request;

/**
 * Mute of Request messages is to mute video of stick playing
 * @see Request AvController
 */
public class Mute extends Request
{
    public Mute(String controller_id, String controller_type) 
    {
        super(controller_id, controller_type);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "mute";
    public static final String ACTION_TYPE = "request";
}
