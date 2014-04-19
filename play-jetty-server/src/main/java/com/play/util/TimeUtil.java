/**
 * Project: play-jetty-server
 * 
 * File Created at 2014-4-12
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
package com.play.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.play.bean.TimeSeg;


/**
 * TODO Comment of TimeUtil
 * @author yao.ma
 *
 */
public class TimeUtil {
    private static DateFormat formatter = new SimpleDateFormat("yyyy/MM/DD|HH:mm");
    
	public static final long ONE_HOUR = 60 * 60 * 1000;
	public static final long ONE_DAY = 24 * ONE_HOUR;
	
	public static final int TIME_MORING  = 0;
	public static final int TIME_NOON = 1;
	public static final int TIME_AFTERNOON = 2;
	public static final int TIME_EVENING = 3;
	public static final int TIME_NIGHT = 4;
	
	public static final String [] TIME_SEG_ARRAY = {"MORING", "NOON", "AFTERNOON", "EVENING", "NIGHT"};
	
	public static final int MORNING_MIN = 0;
	public static final int MORNING_MAX = 10;
	public static final int NOON_MIN = 11;
	public static final int NOON_MAX = 12;
	public static final int AFTERNOON_MIN = 13;
	public static final int AFTERNOON_MAX = 16;
	public static final int EVENING_MIN = 17;
	public static final int EVENING_MAX = 19;
	public static final int NIGHT_MIN = 20;
	public static final int NIGHT_MAX = 23;
	
	public static final int [] TIME_SPAN_ARRAY = {MORNING_MIN, MORNING_MAX,  NOON_MIN, NOON_MAX,
		AFTERNOON_MIN, AFTERNOON_MAX, EVENING_MIN, EVENING_MAX, NIGHT_MIN, NIGHT_MAX};
	
	/***
	 * 获取某个时段的起始与结束时间
	 * @param timeSeg
	 * @return
	 */
	public static final String getTimeSpanStr(int timeSeg){
	    if(timeSeg == MORNING_MIN)
	    {
	        return "7 : 00 ~ "+ TIME_SPAN_ARRAY[2 * timeSeg + 1] + " : 59";
	    }
		return TIME_SPAN_ARRAY[2 * timeSeg] + " : 00 ~ " + TIME_SPAN_ARRAY[2 * timeSeg + 1] + " : 59";
	}

	/***
	 * 获取开始时间和结束时间对应的时段序列
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static List<TimeSeg> getTimeSegment(long beginTime, long endTime){
		if(beginTime > endTime){
			System.err.println("time span error: begin time later than end time!");
			return null;
		}
		int beginDay = getDay(beginTime);
		int endDay = getDay(endTime);
		
		int beginHour = getHour(beginTime);
		int endHour = getHour(endTime);

		List<TimeSeg> timeSegs = getTimeSegment(beginHour, endHour, beginDay, endDay);
//		System.out.println("Time span : ");
//		for(int i = 0; i < timeSegs.size(); i++){
//			System.out.println(TIME_SEG_ARRAY[timeSegs.get(i)]);
//		}
		return timeSegs;
//		long timeSpan = endTime - beginTime;
//		if(timeSpan > ONE_DAY){
//			System.err.println("time span error: exceed one day!");
//			return null;
//		}
		
	}
	
	/***
	 * 获取开始时间和结束时间对应的时段序列
	 * @param beginHour
	 * @param endHour
	 * @return
	 */
	public static List<TimeSeg> getTimeSegment(int beginHour, int endHour, int beginDay, int endDay){
		List<Integer> timeSegmentList = Lists.newArrayList();
		List<TimeSeg> timeSegs = Lists.newArrayList();
		TimeSeg timeSegTmp = null;
		//如果开始时间和结束时间是同一天
		if(beginDay == endDay){
			if(beginHour == endHour)
			{
				int seg = getTimeSegment(beginHour);
				timeSegTmp = new TimeSeg(1, seg);
				timeSegmentList.add(seg);
				return timeSegs;
			}

			int minSeg = getTimeSegment(beginHour);
			int maxSeg = getTimeSegment(endHour);
			
			for(int seg = minSeg; seg <= maxSeg; seg++){
				timeSegTmp = new TimeSeg(1, seg);
				timeSegs.add(timeSegTmp);
				timeSegmentList.add(seg);
			}
			return timeSegs;
		}
		//如果开始时间和结束时间不在一天
		int days = endDay - beginDay + 1;
		//第一天的时段
		int firstMinSeg = getTimeSegment(beginHour);
		for(int i = firstMinSeg; i < TIME_SEG_ARRAY.length; i++){
			timeSegTmp = new TimeSeg(1, i);
			timeSegs.add(timeSegTmp);
			timeSegmentList.add(i);
		}
		//中间n天的时段
		for(int day = 1; day < days - 1; day++){
			for(int seg = 0; seg < TIME_SEG_ARRAY.length; seg++){
				timeSegTmp = new TimeSeg(day + 1, seg);
				timeSegs.add(timeSegTmp);
				timeSegmentList.add(seg);
			}
		}
		//最后一天的时段
		int lastMaxSeg = getTimeSegment(endHour);
		int lastday = endDay - beginDay + 1;
		for(int i = 0; i <= lastMaxSeg; i++){
			timeSegTmp = new TimeSeg(lastday, i);
			timeSegs.add(timeSegTmp);
			timeSegmentList.add(i);
		}
		return timeSegs;
	}
	
	public static List<Integer> getTimeSegment(int beginHour, int endHour){
		List<Integer> timeSegmentList = Lists.newArrayList();
		
		if(beginHour == endHour)
		{
			int seg = getTimeSegment(beginHour);
			timeSegmentList.add(seg);
			return timeSegmentList;
		}
		//超过一天
		if(endHour < beginHour){
			endHour = 23;
		}
		int minSeg = getTimeSegment(beginHour);
		int maxSeg = getTimeSegment(endHour);
		
		for(int seg = minSeg; seg <= maxSeg; seg++){
			timeSegmentList.add(seg);
		}
		return timeSegmentList;
	}
	
	/***
	 * 返回当前小时所在时段
	 * @param hour
	 * @return
	 */
	public static int getTimeSegment(int hour){
		if(hour >= MORNING_MIN && hour <= MORNING_MAX){
			return TIME_MORING;
		}
		else if(hour >= NOON_MIN && hour <= NOON_MAX){
			return TIME_NOON;
		}
		else if(hour >= AFTERNOON_MIN && hour <= AFTERNOON_MAX){
			return TIME_AFTERNOON;
		}
		else if(hour >= EVENING_MIN && hour <= EVENING_MAX){
			return TIME_EVENING;
		}
		else {
			return TIME_NIGHT;
		}
	}
	/** 
	 * 获取指定时间对应的小时
	 * @param time
	 * @return
	 */
	public static int getHour(long time){
		Date date = new Date(time);
		int hour = date.getHours();
		System.out.println("time: " + time + "\thour: " + hour);
		return hour;
	}
	
	public static int getDay(long time){
		Date date = new Date(time);
		int day = date.getDate();
		System.out.println("time: " + time + "\tDay: " + day);
		return day;
	}
	
	public static long parseTimeString(String time)
	{
	    Date date;
        try
        {
            date = formatter.parse(time);
            return date.getTime();
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
	}
	
	public static void main(String [] args){
		long beginTime = System.currentTimeMillis();
		long endTime = beginTime + 37 * ONE_HOUR + new Random().nextInt(500000);
		
		List<TimeSeg> timeSegs = getTimeSegment(beginTime, endTime);
		
		for(TimeSeg seg : timeSegs){
			System.out.println("Day " + seg.getDay() + "\t" + TIME_SEG_ARRAY[seg.getHourSeg() % TIME_SEG_ARRAY.length]);
		}
	}

}
