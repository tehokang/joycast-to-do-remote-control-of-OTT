/**
 *
 */
package sender.joycast;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import sender.joycast.constant.Configuration;
import sender.joycast.dial.DialClient;
import sender.joycast.dial.DialServer;
import sender.joycast.util.CastLogger;
import sender.joycast.util.NetworkInformation;

/**
 * Class to search dial servers and launch application on specific dial server
 */
public class DiscoveryManagerImpl extends DiscoveryManager 
{
    public interface Listener
    {
        /**
         * Interface will be called when DicoveryManager find a new DialServer
         * in your network area
         * @param server dial server which is searched after discover()
         */
        public void onSearched(DialServer server);
    }
    
    /**
     * DicoveryManager will be managed by singleton
     * @throws UnknownHostException 
     */
    public DiscoveryManagerImpl() throws UnknownHostException
    {
        m_dial_clients = new ArrayList<DialClient>();
        m_listeners = new ArrayList<DiscoveryManager.Listener>();
        
        List<String> broadcast_ip_addresses = NetworkInformation.getBroadcast();
        for ( int i=0; i<broadcast_ip_addresses.size(); i++ )
        {
            initialize(broadcast_ip_addresses.get(i), Configuration.DIAL_MULTICAST_PORT);
        }
        initialize(Configuration.DIAL_MULTICAST_IP, Configuration.DIAL_MULTICAST_PORT);
    }
    
    /**
     * Function to discover DialServer on your networks by using
     * all of network interface you can broadcast
     * @return return true if it's succeed else return false
     */
    public boolean discover()
    {
        if ( m_dial_clients.size() == 0 )
        {
            CastLogger.e("No network interfaces to discover");
            return false;
        }
        
        stopDiscovering();
        
        for ( int i=0; i<m_dial_clients.size(); i++ )
        {
            m_dial_clients.get(i).start();
        }
        return true;
    }

    /**
     * Function to stop discovering DialServer on your networks
     */
    public void stopDiscovering()
    {
        for ( int i=0; i<m_dial_clients.size(); i++ )
        {
            m_dial_clients.get(i).stop();
        }
    }

    /**
     * Function to get list of DialServer you found
     * @return list of DialServer you should check the length of list before you access specific element
     */
    public List<DialServer> getDiscoveredDialServerList()
    {
        List<DialServer> all_of_servers = new ArrayList<DialServer>();

        for ( int i=0; i<m_dial_clients.size(); i++ )
        {
            DialClient dial_client = m_dial_clients.get(i);
            List<DialServer> servers = dial_client.getDiscoveredDialServerList();
            
            for ( int j=0; j<servers.size(); j++ )
            {
                boolean is_already_added = false;
                for ( int k=0; k<all_of_servers.size(); k++) 
                {
                    if ( all_of_servers.get(k).getUuid().equals(servers.get(j).getUuid()) == true )
                    {
                        is_already_added = true;
                    }
                }
                if ( is_already_added == false ) all_of_servers.add(servers.get(j));
            }
        }
        return all_of_servers;
    }

    /**
     * Function to run specific application on DialServer
     * @param server DialServer instance you get received from DiscoveryManager's onSearched
     * @param target_app String of target_app e.g. YouTube, NetFlix
     * @return return true if it's succeed else return false
     * @see DiscoveryManager.Listener
     */
    public boolean launch(DialServer server, String target_app)
    {
        for ( int i=0; i<m_dial_clients.size(); i++ )
        {
            DialClient dial_client = m_dial_clients.get(i);
            if ( dial_client.findDialServer(server) == true )
            {
                return dial_client.launch(server, target_app);
            }
        }
        return false;
    }

    public boolean stopApplication(DialServer server, String target_app)
    {
        for ( int i=0; i<m_dial_clients.size(); i++ )
        {
            DialClient dial_client = m_dial_clients.get(i);
            if ( dial_client.findDialServer(server) == true )
            {
                return dial_client.stopApplication(server, target_app);
            }
        }
        return false;
    }
    
    /**
     * Function to register listener to listen to notification of DiscoveryManager
     * @param listener to register
     */
    public void addEventListener(DiscoveryManager.Listener listener)
    {
        m_listeners.add(listener);
    }

    /**
     * Function to release listener to not listen any more from DiscoveryManager
     * @param listener to release
     */
    public void removeEventListener(DiscoveryManager.Listener listener)
    {
        for(int i=0; i<m_listeners.size(); i++ )
        {
            if ( listener == m_listeners.get(i) )
            {
                m_listeners.remove(i);
                break;
            }
        }
    }

    /**
     * Function to release resources of DiscoveryManager
     */
    public void destroy()
    {
        stopDiscovering();
        
        m_dial_clients.clear();
        m_dial_clients = null;
        m_listeners.clear();
        m_listeners = null;
    }

    private DialClient initialize(String ip, int port) throws UnknownHostException
    {
        DialClient dial_client;
        dial_client = new DialClient(ip, port);
        dial_client.addEventListener(new DialClient.Listener()
        {
            @Override
            public void onSearched(DialServer server)
            {
                for(int i=0; i<m_listeners.size(); i++ )
                {
                    m_listeners.get(i).onSearched(server);
                }
            }
        });
        m_dial_clients.add(dial_client);
        return dial_client;
    }
    
    private final String TAG = "DiscoveryManager";
    private List<DialClient> m_dial_clients = null;
    private List<DiscoveryManager.Listener> m_listeners = null;
}
