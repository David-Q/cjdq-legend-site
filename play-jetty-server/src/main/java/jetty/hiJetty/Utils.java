package jetty.hiJetty;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Utils
{
    private static Printer printer;
    private static Utils instance;
    private static ApplicationContext context;
    
    /*static
    {
        context = new FileSystemXmlApplicationContext("src/main/resources/conf/server-bean.xml");
        printer = (Printer)context.getBean("printer");
    }*/
    
    public Utils getInstance()
    {
        return instance;
    }
    
    public void init()
    {
        System.out.println("C");
        instance = this;
        System.out.println("A");
    }
    
    public static String print()
    {
        return printer.k;
    }

    public Printer getPrinter()
    {
        return printer;
    }

    public void setPrinter(Printer printer)
    {
        this.printer = printer;
    }
}
