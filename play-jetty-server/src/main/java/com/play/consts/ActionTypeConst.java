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
package com.play.consts;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * TODO Comment of ActionTypeConst
 * @author yao.ma
 *
 */
public class ActionTypeConst {
	
	public static final String MORNING_BREAKFAST = "morning_meal";
	public static final String MORNING_AMUSE = "morning_amuse";
	
	public static final String NOON_LUNCH = "lunch";
	
	public static final String AFTER_NOON_AMUSE = "afternoon_amuse";
	
	public static final String EVENING_MEAL = "evening_meal";
	public static final String EVENING_AMUSE = "evening_amuse";
	
	public static final String NIGHT_AMUSE = "night_amuse";
	public static final String NIGHT_HOTEL ="night_hotel";
	
	public static final Map<String, String> ACTION_NAME_MAP = Maps.newHashMap();
	
	static {
		ACTION_NAME_MAP.put(MORNING_BREAKFAST, "美味早餐");
		ACTION_NAME_MAP.put(MORNING_AMUSE, "上午活动");
		ACTION_NAME_MAP.put(NOON_LUNCH, "正餐驾到");
		ACTION_NAME_MAP.put(AFTER_NOON_AMUSE, "午后一刻");
		ACTION_NAME_MAP.put(EVENING_MEAL, "丰盛晚餐");
		ACTION_NAME_MAP.put(EVENING_AMUSE, "餐后娱乐");
		ACTION_NAME_MAP.put(NIGHT_AMUSE, "午夜生活");
		ACTION_NAME_MAP.put(NIGHT_HOTEL, "住哪儿");
	}

	
}
