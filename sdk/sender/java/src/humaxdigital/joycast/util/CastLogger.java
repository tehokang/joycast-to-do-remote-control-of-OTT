package humaxdigital.joycast.util;

/**
 * Utility for logging of JoyCast SDK
 */
public class CastLogger 
{
    /**
     * Method to print debug format with tag
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) 
    {
        if (false == tag.isEmpty() && DEBUG ) 
        {
            System.out.println(m_tag + "[" + tag + "]" + "[D] " +  msg);
        }
    }
    
    /**
     * Method to print debug format
     * @param msg
     */
    public static void d(String msg) 
    {
        /* Pure Java */
        if ( DEBUG ) System.out.println(m_tag + "[D] " + msg);
        /* Android */
        //if(DEBUG) Log.d(tag, "[D]" + msg);
    }
    
    /**
     * Method to print information format with tag
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) 
    {
        if (false == tag.isEmpty() && INFO ) 
        {
            System.out.println(m_tag + "[" + tag + "]" + "[I] " +  msg);
        }
    }
    
    /**
     * Method to print information format
     * @param msg
     */
    public static void i(String msg) 
    {
        /* Pure Java */
        if ( INFO ) System.out.println(m_tag + "[I] " +  msg);
        /* Android */
        //if(INFO) Log.i(tag, "[I]" + msg);
    }    
    
    /**
     * Method to print warning format with tag
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) 
    {
        if (false == tag.isEmpty() && WARN ) 
        {
            System.out.println(m_tag + "[" + tag + "]" + "[W] " +  msg);
        }
    }
    
    /**
     * Method to print warning format
     * @param msg
     */
    public static void w(String msg) 
    {
        /* Pure Java */
        if ( WARN ) System.out.println(m_tag + "[W] " +  msg);
        /* Android */
        //if(WARN) Log.w(tag, "[W]" + msg);
    }
    
    /**
     * Method to print error format with tag
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) 
    {
        if (false == tag.isEmpty() && ERROR) 
        {
            System.out.println(m_tag + "[" + tag + "]" + "[E] " +  msg);
        }
    }
    
    /**
     * Method to print error format
     * @param msg
     */
    public static void e(String msg) 
    {
        /* Pure Java */
        if ( ERROR ) System.out.println(m_tag + "[E] " +  msg);
        /* Android */
        //if(ERROR) Log.e(tag, "[E]" + msg);
    }    

    public static boolean DEBUG = true;
    public static boolean INFO = true;
    public static boolean WARN = true;
    public static boolean ERROR = true;
    public static String m_tag = "HCS_SDK";
}
