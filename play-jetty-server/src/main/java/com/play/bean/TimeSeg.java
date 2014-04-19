/**
 * Project: play-jetty-server
 * 
 * File Created at 2014-4-15
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

import com.play.util.TimeUtil;

/**
 * TODO Comment of TimeSeg
 * @author yao.ma
 *
 */
public class TimeSeg {
	/*
	 * 哪一天，1~n
	 */
	private int day;
	
	/*
	 * 一天中的哪个时段
	 */
	private int hourSeg;
	
	/**时段的字符串表示*/
	private String hourSpan;

	
	/**
	 * @param day
	 * @param hourSeg
	 */
	public TimeSeg(int day, int hourSeg) {
		super();
		this.day = day;
		this.hourSeg = hourSeg;
		iniTimeSpan();
	}

	public String getHourSpan(){
		return hourSpan;
	}
	
	private void iniTimeSpan(){
		this.hourSpan = TimeUtil.getTimeSpanStr(hourSeg);
	}
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHourSeg() {
		return hourSeg;
	}

	public void setHourSeg(int hourSeg) {
		this.hourSeg = hourSeg;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TimeSeg [day=");
		builder.append(day);
		builder.append(", hourSpan=");
		builder.append(hourSpan);
		builder.append("]");
		return builder.toString();
	}
	
	

}
