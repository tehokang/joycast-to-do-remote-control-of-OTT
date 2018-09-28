package sender.joycast.util;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class NetworkInformation 
{
    public static List<String> getBroadcast() 
    {
        List<String> broadcast_ips = new ArrayList<String>();
        Enumeration<NetworkInterface> en;
        try 
        {
            en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) 
            {
                NetworkInterface ni = en.nextElement();
                CastLogger.i(TAG, " Interface name = " + ni.getDisplayName());
                if ( ni.isLoopback() == false && 
                        ni.isUp() == true &&
                        ni.isVirtual() == false ) 
                { 
                    List<InterfaceAddress> list = ni.getInterfaceAddresses();
                    Iterator<InterfaceAddress> it = list.iterator();
    
                    while (it.hasNext()) 
                    {
                        InterfaceAddress ia = it.next();
                        if ( ia.getBroadcast() != null ) 
                        {
                            broadcast_ips.add(ia.getBroadcast().getHostAddress());
                        }
                    }
                }
            }
        } 
        catch (SocketException e) 
        {
            e.printStackTrace();
        }
        return broadcast_ips;
    }
    
    private static final String TAG = "NetworkInformation";
}
