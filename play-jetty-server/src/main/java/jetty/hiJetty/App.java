package jetty.hiJetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws Exception
    {
        String serverBeanXml = "server-bean.xml";
        //serverBeanXml = "src/main/resources/conf/server-bean.xml";
        if(args.length>0)
        {
            serverBeanXml = args[0];
        }
        Server server = new Server();
        
        
        ApplicationContext context = new FileSystemXmlApplicationContext(serverBeanXml);
        Handler searchHandler = (Handler) context.getBean("searchHandler");
        Params params = (Params)context.getBean("params");
        int port = params.getPort();
        server.setHandler(searchHandler);
        
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        connector.setMaxIdleTime(5000);
        connector.setAcceptors(1);
        server.setConnectors(new Connector[]{connector});
        
        QueuedThreadPool threadPool = new QueuedThreadPool();
        // one thread for accept, another for data read/write
        threadPool.setMinThreads(1 + 1 * 2);
        threadPool.setMaxThreads(16 + 1 * 2);
        threadPool.setMaxQueued(1024);
        threadPool.setMaxIdleTimeMs(10000);
        server.setThreadPool(threadPool);
        
        server.start();
    }
}
