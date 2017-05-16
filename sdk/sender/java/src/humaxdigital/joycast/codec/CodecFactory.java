package humaxdigital.joycast.codec;

import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import humaxdigital.joycast.codec.request.Request;
import humaxdigital.joycast.codec.response.Response;
import humaxdigital.joycast.codec.response.Result;

/**
 * Class to encode/decode procotols based JSON between Server and Client 
 */
public class CodecFactory 
{
    public static final String CONTROLLER_AV = "av_controller";
    public static final String CONTROLLER_PHOTO = "photo_controller";
    
    /**
     * Function to encode as string from Request object
     * @see Request
     * @param request to encode as string
     * @return return true if there is no any error during encoding else return false
     */
    public static String encode(Request request) 
    {
        m_object_mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        StringWriter requestString = new StringWriter();
        try 
        {
            m_object_mapper.writeValue(requestString,  request);
        } 
        catch(Exception e) 
        {
            return "";
        }
        return requestString.toString();
    }
    
    /**
     * Function to decode as Event/Response object
     * @see Response
     * @param event to decode into Event/Response
     * @return Response object if there is no error to decode JSON string
     */
    public static Response decode(String event) 
    {
        Response response = null;
        try 
        {
            response = m_object_mapper.readValue(event, Response.class);
        } 
        catch (Exception e) 
        {
            /**
             * @warning sometime the message have wrong format without end of string "}"
             */
//            e.printStackTrace();
        } 
        return response;
    }
    
    /**
     * Function to check the result of Response is fine or not
     * @param result of Response
     * @return return true if this result has success else return false
     */
    public static boolean isSuccess(Result result) 
    {
        if ( result.getCode().compareToIgnoreCase(Result.CODE_OK) == 0 ) 
        { 
            return true;
        } 
        return false;
    }
    
    private static ObjectMapper m_object_mapper = new ObjectMapper();
}
