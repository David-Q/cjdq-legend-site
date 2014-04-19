/**
 * Project: userprofile
 * 
 * File Created at 2014-3-5
 * $Id$
 * 
 * Copyright 2010 dianping.com.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Dianping Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with dianping.com.
 */
package com.play.bean;

import com.play.consts.Consts;
import com.play.route.Position;

/**
 * TODO Comment of ShopInfo
 * @author yao.ma
 *
 */
public class ShopInfo {
	
	protected int shopid;
	protected int distance;
	protected String shopName;
	protected int shoppower;
	protected String picURL;
	protected double price;
	protected String shopType;
	protected String shopCategory;
	protected Position position;
	protected String cityId;
	
	protected double distanceScore;
	protected double shopQualityScore;
	protected double finalScore;
	
	public ShopInfo(int shopid, int distance, String shopName, int shoppower, double price, String picURL, String shopType){
		
		this(shopid, distance, shopName, shoppower, price, picURL, shopType, null);
	}
	
	public ShopInfo(int shopid, int distance, String shopName, int shoppower, double price, String picURL, String shopType, String shopCategory){
		super();
		this.shopid = shopid;
		this.distance = distance;
		this.shopName = shopName;
		this.shoppower = shoppower;
		this.price = price;
		this.picURL = picURL;
		this.shopType = shopType;
		this.shopCategory = shopCategory;
	}
	public String getShopStar(){
		float starFloat = (float)shoppower / 10;
		int starcnt = Math.round(starFloat);
		String starString = "";
		for(int i = 0 ; i < starcnt; i++){
			starString += Consts.STAR;
		}
		for(int i = 0 ; i < 5- starcnt; i++){
			starString += Consts.NO_STAR;
		}
		return starString;
	}
	
	public int getShopid() {
		return shopid;
	}
	public void setShopid(int shopid) {
		this.shopid = shopid;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getDistanceScore() {
		return distanceScore;
	}

	public void setDistanceScore(double distanceScore) {
		this.distanceScore = distanceScore;
	}

	public double getShopQualityScore() {
		return shopQualityScore;
	}

	public void setShopQualityScore(double shopQualityScore) {
		this.shopQualityScore = shopQualityScore;
	}

	public double getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(double finalScore) {
		this.finalScore = finalScore;
	}

	public int getShoppower() {
		return shoppower;
	}

	public void setShoppower(int shoppower) {
		this.shoppower = shoppower;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}


    public String getPicURL()
    {
        return picURL;
    }


    public void setPicURL(String picURL)
    {
        this.picURL = picURL;
    }


    public String getShopType()
    {
        return shopType;
    }


    public void setShopType(String shopType)
    {
        this.shopType = shopType;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShopInfo [shopid=");
		builder.append(shopid);
		builder.append(", distance=");
		builder.append(distance);
		builder.append(", shopName=");
		builder.append(shopName);
		builder.append(", shoppower=");
		builder.append(shoppower);
		builder.append(", picURL=");
		builder.append(picURL);
		builder.append(", price=");
		builder.append(price);
		builder.append(", shopType=");
		builder.append(shopType);
		builder.append(", shopCategory=");
		builder.append(shopCategory);
		builder.append(", distanceScore=");
		builder.append(distanceScore);
		builder.append(", shopQualityScore=");
		builder.append(shopQualityScore);
		builder.append(", finalScore=");
		builder.append(finalScore);
		builder.append("]");
		return builder.toString();
	}

	public String getShopCategory() {
		return shopCategory;
	}

	public void setShopCategory(String shopCategory) {
		this.shopCategory = shopCategory;
	}

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    public String getCityId()
    {
        return cityId;
    }

    public void setCityId(String cityId)
    {
        this.cityId = cityId;
    }

    
}
