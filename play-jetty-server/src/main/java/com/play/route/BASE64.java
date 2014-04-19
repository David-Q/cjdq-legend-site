package com.play.route;

import java.io.IOException;

import org.apache.commons.codec.binary.StringUtils;

public class BASE64
{
    /** 
     * 编码 
     * @param bstr 
     * @return String 
     */  
    public static String encode(byte[] bstr){  
    return new sun.misc.BASE64Encoder().encode(bstr);  
    }  
  
    /** 
     * 解码 
     * @param str 
     * @return string 
     */  
    public static byte[] decode(String str){  
    byte[] bt = null;  
    try {  
        @SuppressWarnings("restriction")
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();  
        bt = decoder.decodeBuffer( str );  
    } catch (IOException e) {  
        e.printStackTrace();  
    }  
  
        return bt;  
    }
    
    public static String decodeToString(String encodedString, String charsetName)
    {
        return StringUtils.newString(decode(encodedString), charsetName);
    }
    
    public static double decodeBaiduPosition(String encodedString)
    {
        return Double.parseDouble(decodeToString(encodedString, "utf8"));
    }
    
    public static void main(String[] args)
    {
        System.out.println(decodeToString("MTIxLjQzMDE1NDAxNjgy","utf8"));
    }
}
