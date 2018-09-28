package sender.joycast.codec.request.avcontrol;

import sender.joycast.codec.request.Request;

public class PlayFaster extends Request
{
    public PlayFaster(String controller_id, String controller_type) 
    {
        super(controller_id, controller_type);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "play_faster";
    public static final String ACTION_TYPE = "request";
}
