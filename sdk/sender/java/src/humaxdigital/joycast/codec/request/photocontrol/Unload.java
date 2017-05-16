package humaxdigital.joycast.codec.request.photocontrol;

import humaxdigital.joycast.codec.request.Request;

public class Unload extends Request 
{
    public Unload(String controller_id, String controller_type) 
    {
        super(controller_id, controller_type);
        
        Body body = new Body();
        
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "unload";
    public static final String ACTION_TYPE = "request";
}
