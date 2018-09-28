package sender.joycast.codec.request.photocontrol;

import java.util.ArrayList;
import java.util.List;

import sender.joycast.codec.request.Request;
import sender.joycast.codec.request.Request.Body;
import sender.joycast.codec.response.Param;

public class Load extends Request 
{
    public Load(String controller_id, String controller_type, String url) 
    {
        super(controller_id, controller_type);
        
        Body body = new Body();
        
        List<Param> params = new ArrayList<Param>();
        Param p1 = new Param("string",  "url", url);
        params.add(p1);
        
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        body.setParamList(params);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "load";
    public static final String ACTION_TYPE = "request";
}
