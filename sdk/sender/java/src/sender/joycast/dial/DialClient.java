package sender.joycast.dial;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import sender.joycast.util.CastLogger;
import sender.joycast.util.HttpRequestHelper;

/**
 * DialClient is to discover DialServer and launch specific application on DialServer 
 */
public class DialClient
{
    public interface Listener 
    {
        public void onSearched(DialServer server);
    }
    
    public void addEventListener(DialClient.Listener listener) 
    {
        m_listeners.add(listener);
    }
    
    public void removeEventListener(DialClient.Listener listener ) 
    {
        for (int i=0;i<m_listeners.size(); i++) 
        {
            if (listener == m_listeners.get(i)) 
            {
                m_listeners.remove(i);
                break;
            }
        }
    }
    
    public void removeAllEventListener() 
    {
        m_listeners.clear();
    }
    
    /**
     * Constructor
     * @param broadcastAddress
     *            destination address for probes
     * @param handler
     *            update Handler in main thread
     */
    public DialClient(String broadcast_ip, int broadcast_port ) throws UnknownHostException 
    {
        m_discovered_dial_servers = new ArrayList<DialServer>();
        m_listeners = new ArrayList<DialClient.Listener>();
        
        m_broadcast_ip = broadcast_ip;
        m_broadcast_port = broadcast_port;
        
        try 
        {
            mSocket = new DatagramSocket();
            mSocket.setBroadcast(true);
        }
        catch (SocketException e) 
        {
            CastLogger.e("Could not create broadcast client socket.");
            throw new RuntimeException();
        } 
        
        m_broadcast_interval = PROBE_INTERVAL_MS;
        CastLogger.i(TAG, "Starting client on address ");
    }

    public void start() 
    {
        final byte[] buffer = new byte[4096];
        m_is_receiving = true;
        m_is_broadcasting = true;
        m_discovered_dial_servers.clear();
        
        m_broadcast_thread = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                while (m_is_broadcasting) 
                {
                    try 
                    {
                        DialClient.this.sendSearchPacket();
                        try
                        {
                            Thread.sleep(m_broadcast_interval);
                            CastLogger.d("sent multicast packet to search dial server");
                        }
                        catch (InterruptedException e) 
                        {
                            
                        }
                        m_broadcast_interval = m_broadcast_interval * 2;
                        if (m_broadcast_interval > PROBE_INTERVAL_MS_MAX) 
                        {
                            m_broadcast_interval = PROBE_INTERVAL_MS_MAX;
                        }
                    } 
                    catch (Exception e) 
                    {
                        e.printStackTrace();
                    }
                }
                CastLogger.d(TAG, "Stopped thread broadcasting...");
            }
        });
        CastLogger.d(TAG, "Started thread broadcasting...");
        m_broadcast_thread.start();

        m_receiving_thread = new Thread(new Runnable()
        {
            @Override
            public void run() 
            {
                while (m_is_receiving) 
                {
                    try 
                    {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        mSocket.receive(packet);
                        
                        String dial_server_location = handleSearchResponsePacket(packet);
                        DialServer server = getDialServer(dial_server_location);
                        
                        if ( server != null ) 
                        {
                            boolean is_already_found = false;
                            for ( int i=0; i<m_discovered_dial_servers.size(); i++ ) 
                            {
                                if ( true == server.getUuid().equals(m_discovered_dial_servers.get(i).getUuid()) )
                                {
                                    CastLogger.i("Already found this dialserver [" + 
                                        server.getIpAddress().getHostAddress() + "]");
                                    is_already_found = true;
                                }
                            }
                            
                            if ( is_already_found == false )
                            {
                                m_discovered_dial_servers.add(server);
                            }
                            
                            for(int i=0;i<m_listeners.size();i++) 
                            {
                                m_listeners.get(i).onSearched(server);
                            }
                        }
                    } 
                    catch (InterruptedIOException e) 
                    {
                        /**
                         * @note timeout
                         */
                    } 
                    catch (IOException e) 
                    {
                        /**
                         * @note SocketException - stop() was called
                         */
                    } 
                    catch (IllegalArgumentException e) 
                    {
                    }
                }
                CastLogger.d(TAG, "Stopped thread receiving...");
            }
        });
        CastLogger.d(TAG, "Started thread receiving...");
        m_receiving_thread.start();
        
    }
    
    /**
     * Immediately stops the receiver thread, and cancels the probe timer.
     */
    public void stop() 
    {
        m_is_broadcasting = false;
        m_is_receiving = false;
        
        if ( m_broadcast_thread != null )
        {
            m_broadcast_thread.interrupt();
        }
        
        if ( m_receiving_thread != null )
        {
            m_receiving_thread.interrupt();
        }
    }
    
    public List<DialServer> getDiscoveredDialServerList() 
    {
        return m_discovered_dial_servers;
    }
    
    public boolean launch(DialServer server, String target_app) 
    {
        return new HttpRequestHelper().sendPost(server.getAppsUrl() + "/" + target_app, null);
    }
    
    public boolean stopApplication(DialServer server, String target_app)
    {
        return new HttpRequestHelper().sendDelete(server.getAppsUrl() + "/" + target_app + "/run", null);
    }
    
    public boolean findDialServer(DialServer server) 
    {
        for ( int i=0; i<m_discovered_dial_servers.size(); i++) 
        {
            if ( server == m_discovered_dial_servers.get(i) ) 
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @throws IOException 
     * Sends a single broadcast discovery request.
     */
    private void sendSearchPacket() throws Exception 
    {
        String message = "M-SEARCH * HTTP/1.1\r\n" + "HOST: " + 
                m_broadcast_ip + String.valueOf(m_broadcast_port) + "\r\n" + 
                "MAN: \"ssdp:discover\"\r\n" + 
                "MX: 10\r\n" + 
                "ST: " + 
                SEARCH_TARGET + "\r\n\r\n";
        
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(m_broadcast_ip), m_broadcast_port);
        if ( false == mSocket.isClosed() ) mSocket.send(packet);
    }

    /**
     * Parse a received packet, and notify the main thread if valid.
     * @param packet 
     * The locally-received DatagramPacket
     */
    private String handleSearchResponsePacket(DatagramPacket packet) 
    {
        String strPacket = new String(packet.getData(), 0, packet.getLength());
        String tokens[] = strPacket.trim().split("\\n");
        CastLogger.d(TAG, "response=" + strPacket);

        String location = null;
        for (int i = 0; i < tokens.length; i++) 
        {
            String token = tokens[i].trim();
            if (token.startsWith(HEADER_LOCATION)) 
            {
                location = token.substring(10).trim();
            }
            else if (token.startsWith(HEADER_ST)) 
            {
                String st = token.substring(4).trim();
                if (st.equals(SEARCH_TARGET)) 
                {
                    /**
                     * @note May add to check if needs to check search_target
                     */
                }
            }
        }
        return location;
    }

    private String getFieldValue(Document doc, String field) 
    {
        Element element = (Element)doc.getElementsByTagName(field).item(0);
        if ( element != null ) 
            return element.getTextContent();
        return "";
    }
    
    private DialServer getDialServer(String location) 
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            HttpResponse response = new HttpRequestHelper().sendHttpGet(location);
            if ( response != null ) 
            {
                Header header = response.getLastHeader(HEADER_APPLICATION_URL);
                Document doc = builder.parse(new InputSource(response.getEntity().getContent()));
                
                String appsUrl = header.getValue();
                String friendlyName = getFieldValue(doc, "friendlyName");
                String uuid = getFieldValue(doc, "UDN");
                String manufacturer = getFieldValue(doc, "manufacturer");
                String modelName = getFieldValue(doc, "modelName");
                String ver_major = getFieldValue(doc, "major");
                String ver_minor = getFieldValue(doc, "minor");
                String manufacturerURL = getFieldValue(doc, "manufacturerURL");
                String modelDescription = getFieldValue(doc, "modelDescription");
                String serialNumber = getFieldValue(doc, "serialNumber");
                
                URL url = new URL(location);
                return new DialServer(
                        location, InetAddress.getByName(url.getHost()), 
                        url.getPort(), appsUrl, friendlyName, 
                        uuid, manufacturer, modelName, 
                        ver_major, ver_minor, manufacturerURL, modelDescription, serialNumber);
            }
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        } 
        return null;
    }

    private static final String TAG = "DialClient";
    private static final int PROBE_INTERVAL_MS = 3000; //6000;
    private static final int PROBE_INTERVAL_MS_MAX = 5000; //12000;

    private static final String HEADER_ST = "ST";
    private static final String HEADER_LOCATION = "LOCATION";
    private static final String HEADER_APPLICATION_URL = "Application-URL";
    private static final String SEARCH_TARGET = "urn:dial-multiscreen-org:service:dial:1";
    
    private DatagramSocket mSocket;
    private int m_broadcast_port;
    private String m_broadcast_ip;
    private int m_broadcast_interval;
    private Thread m_receiving_thread;
    private boolean m_is_receiving = true;
    private Thread m_broadcast_thread;
    private boolean m_is_broadcasting = true;
    
    private List<DialServer> m_discovered_dial_servers = null;
    private List<DialClient.Listener> m_listeners = null;
}
