package com.play.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dp.arts.client.response.Record;
import com.play.bean.ShopInfo;
import com.play.route.PosParser;
import com.play.route.Position;
import com.play.search.Searcher;

public class ProjectMaker
{
    private static Map<Integer,String> cityMap = new HashMap<Integer,String>();
    static{
        cityMap.put(1, "shanghai");
        cityMap.put(2, "beijing");
        cityMap.put(3, "hangzhou");
        cityMap.put(4, "guangzhou");
        cityMap.put(5, "nanjing");
        cityMap.put(6, "suzhou");
        cityMap.put(10, "tianjin");
        cityMap.put(7, "shenzhen");
    }
    
    public static Searcher searcher = new Searcher();
    
    public static List<ProjectPart> createProject(String checked, String shops, String types)
    {
        List<ProjectPart> projectParts = new ArrayList<ProjectPart>();
        
        List<ShopInfo> shopinfos = new ArrayList<ShopInfo>();
        String[] checkArray = checked.split(",");
        String[] shopArray = shops.split(",");
        String[] typeArray = types.split(",");
        
        for(int k=0; k< checkArray.length; k++)
        {
            if(Integer.parseInt(checkArray[k]) == 1)
            {
                Record record= searcher.searchShopById(shopArray[k]);
                int shopid = Integer.valueOf(record.get("shopid"));
                int cityid = Integer.valueOf(record.get("cityid"));
                int shoppower = Integer.valueOf(record.get("shoppower"));
                String shopName = record.get("shopname");
                int price  = Integer.valueOf(record.get("avgprice"));
                String picURL = record.get("defaultpic");
                String shoptype = record.get("shoptype");
                double glng = Double.valueOf(record.get("gpoi").split(":")[0]);
                double glat = Double.valueOf(record.get("gpoi").split(":")[1]);
                
                
                ShopInfo shopInfo = new ShopInfo(shopid, 0, shopName, shoppower, price, picURL, shoptype);
                Position position = new Position();
                position.setGlat(glat);
                position.setGlng(glng);
                position.setCityName(cityMap.get(cityid));
                PosParser.addBaiduPos(position);
                shopInfo.setPosition(position);
                shopinfos.add(shopInfo);
                
                
                ProjectPart part = new ProjectPart();
                part.setShopInfo(shopInfo);
                part.setType(typeArray[k]);
                projectParts.add(part);
            }
        }
        
        for(int k=0;k<projectParts.size()-1; k++)
        {
            ProjectPart part = projectParts.get(k);
            StringBuilder sb = new StringBuilder();
            
            part.setWalking(PosParser.getWalkingInfo(shopinfos.get(k).getPosition(), shopinfos.get(k+1).getPosition()));
            part.setTransit(PosParser.getTransitInfo(shopinfos.get(k).getPosition(), shopinfos.get(k+1).getPosition()));
            part.setDriving(PosParser.getDrivingInfo(shopinfos.get(k).getPosition(), shopinfos.get(k+1).getPosition()));
            
            
            sb.append(PosParser.getTransitInfo(shopinfos.get(k).getPosition(), shopinfos.get(k+1).getPosition()));
            sb.append("<br/>");
            sb.append(PosParser.getWalkingInfo(shopinfos.get(k).getPosition(), shopinfos.get(k+1).getPosition()));
            sb.append("<br/>");
            sb.append(PosParser.getDrivingInfo(shopinfos.get(k).getPosition(), shopinfos.get(k+1).getPosition()));
            sb.append("<br/>");
            part.setRouteToNext(sb.toString());
        }
        
        return projectParts;
    }
}
