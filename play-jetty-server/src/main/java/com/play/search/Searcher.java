package com.play.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dp.arts.client.SearchClient;
import com.dp.arts.client.request.GeoQuery;
import com.dp.arts.client.request.Query;
import com.dp.arts.client.request.Request;
import com.dp.arts.client.request.Request.Platform;
import com.dp.arts.client.request.Request.SortOrder;
import com.dp.arts.client.request.SortItem;
import com.dp.arts.client.request.TermQuery;
import com.dp.arts.client.response.Record;
import com.dp.arts.client.response.Response;
import com.play.consts.Consts;

public class Searcher
{
    private static final int limit = 5;
    
    public static final String DEFAULT_SEARCH_SERVIE = "http://192.168.5.149:4053/search/shop";
    
    public SearchClient searchClient = new SearchClient(DEFAULT_SEARCH_SERVIE);
    
    public List<Record> searchShop(List<String> categoryids, double lng,double lat, int distance)
    {
        Request request = new Request(Platform.MAPI, "shop");
        
        Query query = new GeoQuery("gpoi", lng, lat, distance);
        request.addQuery(query);
        
        query = new TermQuery("categoryids",categoryids);
        request.addQuery(query);
        
        request.addOutputField("cityid");
        request.addOutputField("shopname");
        request.addOutputField("shopid");
        request.addOutputField("avgprice");
        request.addOutputField("poi");
        request.addOutputField("shoppower");
        request.addOutputField("defaultpic");
        request.addOutputField("categoryids");
        request.addOutputField("shoptype");
        // 返回商户距离，和附近查询方式类似，在dist中的第一个参数为商户的经纬度字段名，第二个参数为坐标，结果会在_distance_字段中返回，单位为米
        request.addOutputField("dist(poi,"+lng+":"+lat+")");
        
        request.addSortItem(new SortItem("shoppower", SortOrder.DESC));
        request.setLimit(0, limit);
        
        try
        {
            Response response = searchClient.search(request);
            return response.getRecordList();
        }
        catch (IOException e)
        {
            System.out.println(e);
            return new ArrayList<Record>();
                    
        }
    }
    
    public Record searchShopById(String shopid)
    {
        Request request = new Request(Platform.MAPI, "shop");
        Query query = new TermQuery("shopid",shopid);
        
        request.addQuery(query);
        
        request.addOutputField("cityid");
        request.addOutputField("shopname");
        request.addOutputField("shopid");
        request.addOutputField("avgprice");
        request.addOutputField("poi");
        request.addOutputField("shoppower");
        request.addOutputField("defaultpic");
        request.addOutputField("categoryids");
        request.addOutputField("shoptype");
        request.addOutputField("gpoi");
        
        try
        {
            Response response = searchClient.search(request);
            List<Record> list = response.getRecordList();
            if(list.size()>0)
            {
                return list.get(0);
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        return null;
    }

    public SearchClient getSearchClient()
    {
        return searchClient;
    }

    public void setSearchClient(SearchClient searchClient)
    {
        this.searchClient = searchClient;
    }
    
    public static void main(String[] args)
    {
        Searcher searcher = new Searcher();
        List<Record> records = searcher.searchShop(Arrays.asList("10"), 121.471352, 31.228731 , 1000);
        for(Record record: records)
        {
            System.out.println(record.get(Consts.SHOPNAME)+"-"+record.get(Consts.DISTANCE));
        }
    }
}
