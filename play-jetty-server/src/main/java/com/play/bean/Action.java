package com.play.bean;

import java.util.ArrayList;
import java.util.List;

public class Action
{
	/**
	 * 活动下的商户列表
	 */
    private List<ShopInfo> shopList = new ArrayList<ShopInfo>();
    
    /***
     * 活动类型
     */
    private String actionType;
    
    /***
     * 活动时第几天，哪个时段
     */
    private TimeSeg timeSeg;

    /**
	 * @param shopList
	 * @param actionType
	 */
	public Action(List<ShopInfo> shopList, String actionType, TimeSeg timeSeg) {
		super();
		this.shopList = shopList;
		this.actionType = actionType;
		this.timeSeg = timeSeg;
	}

	public List<ShopInfo> getShopList()
    {
        return shopList;
    }

    public void setShopList(List<ShopInfo> shopList)
    {
        this.shopList = shopList;
    }

    public String getActionType()
    {
        return actionType;
    }

    public void setActionType(String actionType)
    {
        this.actionType = actionType;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Action [actionType=" + actionType + "\t time= " + timeSeg.toString() + "]"); 
		sb.append("\n");
		for(ShopInfo shopInfo : shopList){
			sb.append(shopInfo.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public TimeSeg getTimeSeg() {
		return timeSeg;
	}

	public void setTimeSeg(TimeSeg timeSeg) {
		this.timeSeg = timeSeg;
	}
    
    
}
