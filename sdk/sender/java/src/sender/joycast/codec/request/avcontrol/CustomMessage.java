package sender.joycast.codec.request.avcontrol;

import java.util.ArrayList;
import java.util.List;

import sender.joycast.codec.request.Request;
import sender.joycast.codec.request.Request.Body;
import sender.joycast.codec.response.Param;

/**
 * CustomMessage of Request messages is to send message customized by User
 * The message will consist of name and value
 * @see Request
 */
public class CustomMessage extends Request
{
    public CustomMessage(String controller_id, String controller_type, String name, String value) 
    {
        super(controller_id, controller_type);
        
        List<Param> params = new ArrayList<Param>();
        Param p1 = new Param("string", name, value);
        params.add(p1);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        body.setParamList(params);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "custom";
    public static final String ACTION_TYPE = "request";
}
