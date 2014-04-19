package com.play.project;

import com.play.bean.ShopInfo;

public class ProjectPart
{
    private ShopInfo shopInfo;
    private String type;
    private String routeToNext = "";
    private String transit = "";
    private String walking = "";
    private String driving = "";
    
    public ShopInfo getShopInfo()
    {
        return shopInfo;
    }
    public void setShopInfo(ShopInfo shopInfo)
    {
        this.shopInfo = shopInfo;
    }
    public String getRouteToNext()
    {
        return routeToNext;
    }
    public void setRouteToNext(String routeToNext)
    {
        this.routeToNext = routeToNext;
    }
    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public String getTransit()
    {
        return transit;
    }
    public void setTransit(String transit)
    {
        this.transit = transit;
    }
    public String getWalking()
    {
        return walking;
    }
    public void setWalking(String walking)
    {
        this.walking = walking;
    }
    public String getDriving()
    {
        return driving;
    }
    public void setDriving(String driving)
    {
        this.driving = driving;
    }
}
