package sender.joycast.codec.request.avcontrol;

import java.util.ArrayList;
import java.util.List;

import sender.joycast.codec.request.Request;
import sender.joycast.codec.request.Request.Body;
import sender.joycast.codec.response.Param;

/**
 * MoveScreen of Request messages is to change the position of video on the screen
 * @see Request AvController
 */
public class MoveScreen extends Request
{
    public MoveScreen(String controller_id, String controller_type, String x, String y, String w, String h)
    {
        super(controller_id, controller_type);
        
        List<Param> params = new ArrayList<Param>();
        Param p1 = new Param("int", "x", x);
        Param p2 = new Param("int", "y", y);
        Param p3 = new Param("int", "w", w);
        Param p4 = new Param("int", "h", h);
        params.add(p1);
        params.add(p2);
        params.add(p3);
        params.add(p4);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION_NAME);
        body.setActionType(ACTION_TYPE);
        body.setParamList(params);
        
        super.setBody(body);     
    }
    public static final String ACTION_NAME = "move_screen";
    public static final String ACTION_TYPE = "request";
}
