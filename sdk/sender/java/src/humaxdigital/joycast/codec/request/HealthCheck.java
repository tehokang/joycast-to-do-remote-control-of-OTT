package humaxdigital.joycast.codec.request;

public class HealthCheck extends Request
{
    public HealthCheck(String controller_id, String controller_type) 
    {
        super(controller_id, controller_type);
        
        Body body = new Body();
        body.setHash(String.valueOf(hashCode()));
        body.setActionName(ACTION);
        body.setActionType(ACTION_TYPE);
        
        super.setBody(body);
    }
    
    public static final String ACTION = "health_check";
    public static final String ACTION_TYPE = "request";
}
