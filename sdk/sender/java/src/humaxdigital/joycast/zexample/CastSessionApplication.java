package humaxdigital.joycast.zexample;

import humaxdigital.joycast.DiscoveryManager;
import humaxdigital.joycast.SessionManager;
import humaxdigital.joycast.constant.Configuration;
import humaxdigital.joycast.dial.DialServer;
import humaxdigital.joycast.session.CastSession;
import humaxdigital.joycast.util.CastLogger;

public class CastSessionApplication 
{
    private static final String TAG = "CastSessionApplication";
    
    public static void main(String [] args)
    {
        final DiscoveryManager m_discovery_manager = DiscoveryManager.getInstance();
        
        if ( m_discovery_manager.discover() ) 
        {
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
                    CastLogger.d(TAG, "");
                    
                    m_discovery_manager.stopDiscovering();
                    
                    if ( true == m_discovery_manager.launch(server, Configuration.JOYCAST) ) 
                    {
                        CastLogger.d(TAG, "Launched " + Configuration.JOYCAST + " on target device");
                        /**
                         * @note create session to cast media(av, photo)
                         */
                        CastSession cast_session = SessionManager.createSession();
                        cast_session.start(server.getIpAddress().getHostAddress(), Configuration.JOYCAST_PORT, "");
                        
                        /**
                         * @note Do something to control Controller which CastSession is having
                         */
                        
                        /**
                         * @note destroy session after using
                         */
                        SessionManager.destroySession(cast_session);
                        
                    }
                }

            });
        }
    }
}
