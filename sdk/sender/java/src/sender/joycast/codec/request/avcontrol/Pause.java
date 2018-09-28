package sender.joycast.codec.request.avcontrol;

import sender.joycast.codec.request.Request;

/**
 * Pause of Request messages is to pause video of stick playing
 * @see Request AvController
 */
public class Pause extends Request 
{
    public Pause(String controller_id, String controller_type) 
    {
        super(controller_id, controller_type);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "pause";
    public static final String ACTION_TYPE = "request";
}
