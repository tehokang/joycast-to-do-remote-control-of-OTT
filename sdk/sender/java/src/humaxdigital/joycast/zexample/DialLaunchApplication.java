package humaxdigital.joycast.zexample;

import humaxdigital.joycast.DiscoveryManager;
import humaxdigital.joycast.constant.Configuration;
import humaxdigital.joycast.dial.DialServer;
import humaxdigital.joycast.util.CastLogger;

public class DialLaunchApplication 
{
    private static final String TAG = "DialLaunchApplication";
    
    public static void main(String [] args)
    {
        final DiscoveryManager m_discovery_manager = DiscoveryManager.getInstance();
        
        m_discovery_manager.addEventListener(new DiscoveryManager.Listener() 
        {
            @Override
            public void onSearched(DialServer server) 
            {
                CastLogger.d(TAG, "DialServer");
                CastLogger.d(TAG, "location : " + server.getLocation());
                CastLogger.d(TAG, "ip address : " + server.getIpAddress().getHostAddress());
                CastLogger.d(TAG, "port : " + server.getPort());
                CastLogger.d(TAG, "appsUrl : " + server.getAppsUrl());
                CastLogger.d(TAG, "friendlyName : " + server.getFriendlyName());
                CastLogger.d(TAG, "uuid : " + server.getUuid());
                CastLogger.d(TAG, "manufacturer : " + server.getManufacturer());
                CastLogger.d(TAG, "modelName : " + server.getModelName());
                CastLogger.d(TAG, "version_major : " + server.getVersionMajor());
                CastLogger.d(TAG, "version_minor : " + server.getVersionMinor());
                CastLogger.d(TAG, "manufacturerURL : " + server.getManufacturerURL());
                CastLogger.d(TAG, "modelDescription: : " + server.getModelDescription());
                CastLogger.d(TAG, "serialNumber : " + server.getSerialNumber());
                CastLogger.d(TAG, "");
                
                m_discovery_manager.stopDiscovering();
                
                if ( true == m_discovery_manager.launch(server, Configuration.JOYCAST) ) 
                {
                    CastLogger.d("Launched " + Configuration.JOYCAST + " on target device");
                }
            }
        });
        
        if ( m_discovery_manager.discover() ) 
        {
            
        }
    }
}
