package humaxdigital.joycast.codec.request;

/**
 * QueryCapability message of Request is to be sent to check if the stick has controller
 * to play media 
 */
public class QueryCapability extends Request 
{
    public QueryCapability(String controller_id, String controller_type) 
    {
        super(controller_id, controller_type);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION);
        body.setActionType(ACTION_TYPE);
        
        super.setBody(body);
    }
    
    public static final String ACTION = "query_capability";
    public static final String ACTION_TYPE = "request";
}
