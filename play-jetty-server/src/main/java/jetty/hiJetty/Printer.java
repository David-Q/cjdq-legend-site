package jetty.hiJetty;

public class Printer
{
    String k = "WRONG";
    
    private final static Printer printer = new Printer();
    
    public static Printer getInstance()
    {
        return printer;
    }

    public String getK()
    {
        return k;
    }

    public void setK(String k)
    {
        this.k = k;
    }
}
