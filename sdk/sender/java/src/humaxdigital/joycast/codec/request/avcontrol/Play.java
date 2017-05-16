package humaxdigital.joycast.codec.request.avcontrol;

import java.util.ArrayList;
import java.util.List;

import humaxdigital.joycast.codec.request.Request;
import humaxdigital.joycast.codec.request.Request.Body;
import humaxdigital.joycast.codec.response.Param;

/**
 * Play of Request messages is to play video of stick pausing/playing
 * @see Request AvController
 */
public class Play extends Request 
{
    public Play(String controller_id, String controller_type, String title, String url)
    {
        super(controller_id, controller_type);
        
        List<Param> params = new ArrayList<Param>();
        Param p1 = new Param("string",  "title", title);
        Param p2 = new Param("string",  "url", url);
        params.add(p1);
        params.add(p2);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        body.setParamList(params);
        
        super.setBody(body);     
    }
    
    public Play(String controller_id, String controller_type, String url) 
    {
        super(controller_id, controller_type);
        
        List<Param> params = new ArrayList<Param>();
        Param p1 = new Param("string",  "url", url);
        params.add(p1);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        body.setParamList(params);
        
        super.setBody(body);
    }
    
    public Play(String controller_id, String controller_type) 
    {
        super(controller_id, controller_type);
        
        List<Param> params = new ArrayList<Param>();
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        body.setParamList(params);
        
        super.setBody(body);
    }
    
    public static final String ACTION_NAME = "play";
    public static final String ACTION_TYPE = "request";
}
