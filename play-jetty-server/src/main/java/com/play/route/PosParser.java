package com.play.route;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.consts.Consts;

public class PosParser
{
    public static HttpClient client = new HttpClient();
    public static final String baiduak = "WbE9F149lzDGd8zrPyOAd4Lv";
    private static final int maxRouteCount = 2;
    
    public static void addBaiduPos(Position pos)
    {
        GetMethod method = new GetMethod("http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x="+ pos.getGlng() +"&y="+ pos.getGlat());
        System.out.println("http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x="+ pos.getGlng() +"&y="+ pos.getGlat());
        
        try
        {
            client.executeMethod(method);
            String baiduResponse = method.getResponseBodyAsString();
            
            JSONObject jsonObject = JSON.parseObject(baiduResponse);
            double blng = BASE64.decodeBaiduPosition(jsonObject.getString(Consts.BAIDU_RESULT_X));
            double blat = BASE64.decodeBaiduPosition(jsonObject.getString(Consts.BAIDU_RESULT_Y));
            pos.setBlng(blng);
            pos.setBlat(blat);
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static String getWalkingInfo(Position origin, Position destination)
    {
        GetMethod method = new GetMethod("http://api.map.baidu.com/direction/v1?mode=walking&origin="+ origin.getBlat() +","+ origin.getBlng() +"&destination="+destination.getBlat()+","+ destination.getBlng() +"&region="+destination.getCityName()+"&output=json&ak="+baiduak);
        System.out.println("http://api.map.baidu.com/direction/v1?mode=walking&origin="+ origin.getBlat() +","+ origin.getBlng() +"&destination="+destination.getBlat()+","+ destination.getBlng() +"&region="+destination.getCityName()+"&output=json&ak="+baiduak);
        
        StringBuilder sb = new StringBuilder();
        try
        {
            sb.append("<br/>步行路线如下：<br/>");
            client.executeMethod(method);
            String baiduResponse = method.getResponseBodyAsString();
            
            JSONObject jsonObject = JSON.parseObject(baiduResponse);
            JSONObject resultTotal = jsonObject.getJSONObject("result");
            JSONArray routes = resultTotal.getJSONArray("routes");
            for(int k = 0 ; k<(routes.size()<maxRouteCount?routes.size():maxRouteCount); k++)
            {
                JSONObject route = routes.getJSONObject(k);
                sb.append("<br/>线路 "+(k+1)+ "-总行程"+ route.getString("distance") +"米"+ ",耗时"+ route.getString("duration") +"秒" +":<br/>");
                JSONArray steps = route.getJSONArray("steps");
                for(int b = 0; b<steps.size() ; b++)
                {
                    sb.append("("+(b+1)+")"+steps.getJSONObject(b).getString("instructions")).append("<br/>");
                }
            }
            
            return sb.toString();
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "";
        
    }
    
    public static String getTransitInfo(Position origin, Position destination)
    {
        GetMethod method = new GetMethod("http://api.map.baidu.com/direction/v1?mode=transit&origin="+ origin.getBlat() +","+ origin.getBlng() +"&destination="+destination.getBlat()+","+ destination.getBlng() +"&region="+destination.getCityName()+"&output=json&ak="+baiduak);
        System.out.println("http://api.map.baidu.com/direction/v1?mode=transit&origin="+ origin.getBlat() +","+ origin.getBlng() +"&destination="+destination.getBlat()+","+ destination.getBlng() +"&region="+destination.getCityName()+"&output=json&ak="+baiduak);
        StringBuilder sb = new StringBuilder();
        try
        {
            sb.append("<br/>交通路线如下：<br/>");
            client.executeMethod(method);
            String baiduResponse = method.getResponseBodyAsString();
            
            JSONObject jsonObject = JSON.parseObject(baiduResponse);
            JSONObject resultTotal = jsonObject.getJSONObject("result");
            JSONArray routes = resultTotal.getJSONArray("routes");
            if(routes == null)
            {
                JSONObject taxi = resultTotal.getJSONObject("taxi");
                
                sb.append("<br/>线路 1-总行程"+ taxi.getString("distance") +"米"+ ",耗时"+ taxi.getString("duration") +"秒" +":<br/>");
                sb.append("没有公共交通，可以打taxi，"+taxi.getString("remark")).append("<br/>");
            }
            else
            {
                for(int k = 0 ; k< (routes.size()<maxRouteCount?routes.size():maxRouteCount) ; k++)
                {
                    JSONObject route = routes.getJSONObject(k);
                    JSONArray schemes = route.getJSONArray("scheme");
                    for(int s = 0; s<schemes.size(); s++)
                    {
                        JSONObject scheme = schemes.getJSONObject(s);
                        sb.append("<br/>线路 "+(k+1)+ "-总行程"+ scheme.getString("distance") +"米"+ ",耗时"+ scheme.getString("duration") +"秒" +":<br/>");
                        JSONArray steps = scheme.getJSONArray("steps");
                        for(int b = 0; b<steps.size() ; b++)
                        {
                            sb.append("("+(b+1)+")"+steps.getJSONArray(b).getJSONObject(0).getString("stepInstruction")).append("<br/>");
                        }
                    }
                    
                }
            }
            
            return sb.toString();
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "";
    }
    
    public static String getDrivingInfo(Position origin, Position destination)
    {
        GetMethod method = new GetMethod("http://api.map.baidu.com/direction/v1?mode=driving&origin="+ origin.getBlat() +","+ origin.getBlng() +"&destination="+destination.getBlat()+","+ destination.getBlng() +"&region="+destination.getCityName()+"&output=json&ak="+baiduak);
        StringBuilder sb = new StringBuilder();
        try
        {
            sb.append("<br/>自驾路线如下：<br/>");
            client.executeMethod(method);
            String baiduResponse = method.getResponseBodyAsString();
            
            JSONObject jsonObject = JSON.parseObject(baiduResponse);
            JSONObject resultTotal = jsonObject.getJSONObject("result");
            JSONArray routes = resultTotal.getJSONArray("routes");
            for(int k = 0 ; k< (routes.size()<maxRouteCount?routes.size():maxRouteCount) ; k++)
            {
                JSONObject route = routes.getJSONObject(k);
                sb.append("<br/>线路 "+(k+1)+ "-总行程"+ route.getString("distance") +"米"+ ",耗时"+ route.getString("duration") +"秒" +":<br/>");
                JSONArray steps = route.getJSONArray("steps");
                for(int b = 0; b<steps.size() ; b++)
                {
                    sb.append("("+(b+1)+")"+steps.getJSONObject(b).getString("instructions")).append("<br/>");
                }
            }
            
            return sb.toString();
        }
        catch (HttpException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "";
        
    }
    
    public static void main(String[] args)
    {
        Position origin = new Position();
        origin.setGlng(121.41570568084717);
        origin.setGlat(31.217866375826173);
        origin.setCityName("shanghai");
        
        Position destination = new Position();
        destination.setGlng(121.43759250640869);
        destination.setGlat(31.240141450441158);
        destination.setCityName("shanghai");
        
        addBaiduPos(origin);addBaiduPos(destination);
        System.out.println(getWalkingInfo(origin,destination));
        System.out.println(getTransitInfo(origin,destination));
        System.out.println(getDrivingInfo(origin,destination));
    }
}
