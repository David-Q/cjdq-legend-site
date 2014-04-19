package jetty.hiJetty;



import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetWorkUtil
{
     private static final String localHostName = initLocalHostName();
     private static final String localIP = initLocalIp();

    /**
     * 功能描述：获取eth0网卡下的本机ipv4地址
    */
    public static String getLocalIp()
    {
        return localIP;
    }

    /**
     * 功能描述：获取eth0网卡下的本机ipv4地址
     * <p>
     * 前置条件：
     * <p>
     * 方法影响：
     * <p>
     * Author xinli, 2013-3-18
     * 
     * @since arts-common 2.0
     * @return
     */
    private static String initLocalIp()
    {
        String localIp = "127.0.0.1";

        try
        {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements())
            {
                NetworkInterface current = interfaces.nextElement();
                if (!current.isUp() || current.isLoopback() || current.isVirtual())
                {
                    continue;
                }
                Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements())
                {
                    InetAddress addr = addresses.nextElement();
                    if (addr.isLoopbackAddress())
                    {
                        continue;
                    }
                    if (addr instanceof Inet4Address)
                    {
                        return addr.getHostAddress();
                    }
                }
            }

        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }

        return localIp;
    }

    public static String getLocalHostName()
    {
        return localHostName;
    }

    /**
     * 这个函数比较耗时,不能频繁调用
     * @return
     */
    private static String initLocalHostName()
    {
        String host = "unknown";
        try
        {
                InetAddress ia = InetAddress.getLocalHost();
                host = ia.getHostName();//获取计算机主机名
//              ip = ia.getHostAddress();//获取计算机IP
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
        }

        return host;
    }
}
