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

import java.text.DecimalFormat;

/**
 * TODO Comment of DealInfo
 * @author yao.ma
 *
 */
public class DealInfo implements Comparable<DealInfo>{
	
	private int dealgroupid;
	private String dealTitle;
	private double price;
	private double marketPrice;
	private String picURL;
	private int quantity;
	
	public static final String PIC_PRE = "http://t1.dpfile.com";
	
	/**
	 * @param dealgroupid
	 * @param dealTitle
	 * @param price
	 * @param marketPrice
	 * @param picURL
	 * @param quantity
	 */
	public DealInfo(int dealgroupid, String dealTitle, double price,
			double marketPrice, String picURL, int quantity) {
		super();
		this.dealgroupid = dealgroupid;
		this.dealTitle = dealTitle;
		this.price = price;
		this.marketPrice = marketPrice;
		this.picURL = PIC_PRE + picURL;
		this.quantity = quantity;
	}

	public String getDiscount(){
		double discount = (price / marketPrice)* 10;
		DecimalFormat df = new DecimalFormat("#.#");
		return df.format(discount);
	}
	
	public int getDealgroupid() {
		return dealgroupid;
	}
	public void setDealgroupid(int dealgroupid) {
		this.dealgroupid = dealgroupid;
	}
	public String getDealTitle() {
		return dealTitle;
	}
	public void setDealTitle(String dealTitle) {
		this.dealTitle = dealTitle;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getPicURL() {
		return picURL;
	}
	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int compareTo(DealInfo o) {
		if(this.quantity > o.getQuantity())
			return -1;
		else if(this.quantity < o.getQuantity())
			return 1;
		return 0;
	}
	public double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

}
