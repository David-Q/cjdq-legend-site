package com.play.bean;

import java.util.ArrayList;
import java.util.List;

public class ShopInfoListBean
{
	private long beginTime;
	private long endTime;
    private List<ShopInfo> hotelShopList = new ArrayList<ShopInfo>();
    private List<ShopInfo> foodShopList = new ArrayList<ShopInfo>();
    private List<ShopInfo> amuseShopList = new ArrayList<ShopInfo>();
    
    private List<ShopInfo> keywordFoodShopInfos = new ArrayList<ShopInfo>();
    private List<ShopInfo> keywordAmuseShopInfos = new ArrayList<ShopInfo>();
    /**
	 * @param beginTime
	 * @param endTime
	 * @param hotelShopList
	 * @param foodShopList
	 * @param amuseShopList
	 */
	public ShopInfoListBean(long beginTime, long endTime,
			List<ShopInfo> hotelShopList, List<ShopInfo> foodShopList,
			List<ShopInfo> amuseShopList) {
		super();
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.hotelShopList = hotelShopList;
		this.foodShopList = foodShopList;
		this.amuseShopList = amuseShopList;
	}
	/**
	 * @param beginTime2
	 * @param endTime2
	 * @param hotelShops
	 * @param foodShops
	 * @param amuseShops
	 * @param keyWordFoodShops
	 * @param keyWordPlayShops
	 */
	public ShopInfoListBean(long beginTime, long endTime,
			List<ShopInfo> hotelShopList, List<ShopInfo> foodShopList,
			List<ShopInfo> amuseShopList, List<ShopInfo> keyWordFoodShops,
			List<ShopInfo> keyWordPlayShops) {
		super();
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.hotelShopList = hotelShopList;
		this.foodShopList = foodShopList;
		this.amuseShopList = amuseShopList;
		this.keywordFoodShopInfos = keyWordFoodShops;
		this.keywordAmuseShopInfos = keyWordPlayShops;
	}
	public List<ShopInfo> getHotelShopList()
    {
        return hotelShopList;
    }
    public void setHotelShopList(List<ShopInfo> hotelShopList)
    {
        this.hotelShopList = hotelShopList;
    }
    public List<ShopInfo> getFoodShopList()
    {
        return foodShopList;
    }
    public void setFoodShopList(List<ShopInfo> foodShopList)
    {
        this.foodShopList = foodShopList;
    }
    public List<ShopInfo> getAmuseShopList()
    {
        return amuseShopList;
    }
    public void setAmuseShopList(List<ShopInfo> amuseShopList)
    {
        this.amuseShopList = amuseShopList;
    }
	/**
	 * @return the beginTime
	 */
	public long getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public List<ShopInfo> getKeywordFoodShopInfos() {
		return keywordFoodShopInfos;
	}
	public void setKeywordFoodShopInfos(List<ShopInfo> keywordFoodShopInfos) {
		this.keywordFoodShopInfos = keywordFoodShopInfos;
	}
	public List<ShopInfo> getKeywordAmuseShopInfos() {
		return keywordAmuseShopInfos;
	}
	public void setKeywordAmuseShopInfos(List<ShopInfo> keywordAmuseShopInfos) {
		this.keywordAmuseShopInfos = keywordAmuseShopInfos;
	}
}
