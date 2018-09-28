package sender.joycast.dial;

import java.net.InetAddress;

/**
 * JoyCast에서 동작중인 DialServer를 추상화한다. 
 */
public class DialServer 
{
    public DialServer() 
    {

    }

    /**
     * 사용자는 DialServer를 직접 정의하지 않으며, DiscoverManager의 discovery를 통하여 DialServer를 정의된다.
     * @param location
     * @param ipAddress
     * @param port
     * @param appsUrl
     * @param friendlyName
     * @param uuid
     * @param manufacturer
     * @param modelName
     * @param version_major
     * @param version_minor
     * @param manufacturerURL
     * @param modelDescription
     * @param serialNumber
     */
    public DialServer(String location, InetAddress ipAddress, int port, 
            String appsUrl, String friendlyName, String uuid, String manufacturer, String modelName, 
            String version_major, String version_minor, String manufacturerURL, String modelDescription, 
            String serialNumber) 
    {
        this.m_location = location;
        this.m_ip_address = ipAddress;
        this.m_port = port;
        this.m_apps_url = appsUrl;
        this.m_friendly_name = friendlyName;
        this.m_uuid = uuid;
        this.m_manufacturer = manufacturer;
        this.m_model_name = modelName;
        this.m_version_major = version_major;
        this.m_version_minor = version_minor;
        this.m_manufacturer_url = manufacturerURL;
        this.m_model_description = modelDescription;
        this.m_serial_number = serialNumber;
        
    }

    /**
     * Function to get major version of dial server
     * @return major number of version
     */
    public String getVersionMajor() 
    {
        return m_version_major;
    }

    /**
     * Function to get minor version of dial server
     * @return minor number of version
     */
    public String getVersionMinor() 
    {
        return m_version_minor;
    }
    
    /**
     * Function to get manufacturer of device running dial server
     * @return manufacturer URL
     */
    public String getManufacturerURL() 
    {
        return m_manufacturer_url;
    }
    
    /**
     * Function to get model description of device running dial server
     * @return model description
     */
    public String getModelDescription() 
    {
        return m_model_description;
    }
    
    /**
     * Function to get serial information of device
     * @return serial number 
     */
    public String getSerialNumber() 
    {
        return m_serial_number;
    }
    
    /**
     * Function to get location information of device
     * @return location
     */
    public String getLocation() 
    {
        return m_location;
    }

    /**
     * Function to get IP address of stick 
     * @return InetAddress of stick dial
     */
    public InetAddress getIpAddress() 
    {
        return m_ip_address;
    }

    /**
     * Function to get port of stick running dial
     * @return port 
     */
    public int getPort() 
    {
        return m_port;
    }

    /**
     * Function to get apps url of dial supporting
     * @return appsurl
     */
    public String getAppsUrl() 
    {
        return m_apps_url;
    }

    /**
     * Function to get friendlyName of dial
     * @return friendlyName
     */
    public String getFriendlyName() 
    {
        return m_friendly_name;
    }

    /**
     * Function to get uuid of dial
     * @return uuid
     */
    public String getUuid() 
    {
        return m_uuid;
    }

    /**
     * Function to get manufacturer information
     * @return manufacturer
     */
    public String getManufacturer() 
    {
        return m_manufacturer;
    }

    /**
     * Function to get the name of model running dial
     * @return model name
     */
    public String getModelName() 
    {
        return m_model_name;
    }

    public DialServer clone() 
    {
        return new DialServer(m_location, m_ip_address, m_port, m_apps_url, 
                m_friendly_name, m_uuid, m_manufacturer, m_model_name, 
                m_version_major, m_version_minor, m_manufacturer_url, m_model_description, m_serial_number);
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) 
        {
            return true;
        }
        if (!(obj instanceof DialServer)) 
        {
            return false;
        }
        DialServer that = (DialServer) obj;
        return equal(this.m_ip_address, that.m_ip_address) && (this.m_port == that.m_port);
    }

    private static <T> boolean equal(T obj1, T obj2) 
    {
        if (obj1 == null) 
        {
            return obj2 == null;
        }
        return obj1.equals(obj2);
    }

    @Override
    public String toString() 
    {
        return String.format("%s [%s:%d]", m_friendly_name, m_ip_address.getHostAddress(), m_port);
    }

    private final String TAG = "DialServer";
    private String m_location;
    private InetAddress m_ip_address;
    private int m_port;
    private String m_apps_url;
    private String m_friendly_name;
    private String m_uuid;
    private String m_manufacturer;
    private String m_model_name;
    
    /**
     * @note added more by Humax
     */
    private String m_version_major;
    private String m_version_minor;
    private String m_manufacturer_url;
    private String m_model_description;
    private String m_serial_number;
}
