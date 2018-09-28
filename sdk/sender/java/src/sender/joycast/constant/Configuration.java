package sender.joycast.constant;

public class Configuration 
{
    /**
     * DIAL Application prefix to launch YouTube 
     */
    public static final String YOUTUBE = "YouTube";
    
    /**
     * DIAL Application prefix to launch NetFlix
     */
    public static final String NETFLIX = "Netflix";
    
    /**
     * DIAL Application prefix to launch JoyCast
     */
    public static final String JOYCAST = "JoyCast";
    
    /**
     * DIAL multicast target IP
     */
    public static final String DIAL_MULTICAST_IP = "239.255.255.250";
    
    /**
     * DIAL multicast target port
     */
    public static final int DIAL_MULTICAST_PORT = 1900;
    
    /**
     * JoyCast WebSocket Relay Server port
     */
    public static final int JOYCAST_PORT = 7755;
    
    /**
     * Maximum size of Message queue  
     */
    public static final int MESSAGE_QUEUE_MAX = 100;
    
    /**
     * Maximum size of Event queue 
     */
    public static final int EVENT_QUEUE_MAX = 100;
}
