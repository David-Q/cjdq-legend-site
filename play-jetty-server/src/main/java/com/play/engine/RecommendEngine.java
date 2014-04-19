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
package com.play.engine;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.play.bean.Action;
import com.play.bean.RecmdResult;
import com.play.bean.ShopInfo;
import com.play.bean.ShopInfoListBean;
import com.play.bean.TimeSeg;
import com.play.consts.ActionTypeConst;
import com.play.search.ShopSearchClient;
import com.play.util.ScoreUtils;
import com.play.util.TimeUtil;

/**
 * TODO Comment of RecommendUtils
 * @author yao.ma
 *
 */
public class RecommendEngine {
	
	private static final double DISTANCE_WEIGHT_DEFAULT = 0.6;
	private static final double DISTANCE_WEIGHT_HOTEL = 0.5;
	private static final int DEFAULT_REC_CNT = 10;
	private static final double MAX_BREAKFAST_PRICE = 20;
	
	public static final int DEFAULT_KEYWORD_RESULT_SIZE = 5;
	
	private Set<String> MORNING_AMUSE_SET = Sets.newHashSet("面包甜点", "面包", "小吃快餐","零食","西式简餐", "西式甜点", "更多咖啡厅");
	private Set<String> AFTERNOON_AMUSE_SET = Sets.newHashSet("桌面游戏", "密室", "公园","下午茶","游乐游艺");
	private Set<String> EVENING_AMUSE_SET = Sets.newHashSet("电影院", "洗浴", "KTV","演出场馆");
	private Set<String> NIGHT_AMUSE_SET = Sets.newHashSet("足疗按摩",  "洗浴", "KTV","演出场馆");
	//已经推荐过的shopid
	private Set<Integer> recmdShopIDs = Sets.newHashSet();
	
	public RecmdResult getRecmdResult(ShopInfoListBean bean){
		
		if(bean == null)
		{
			System.out.println("Bean null!");
			return null;
		}
		RecmdResult recmdResult = new RecmdResult();
		long beginTime = bean.getBeginTime();
		long endTime = bean.getEndTime();
		
		List<ShopInfo> foodInfos = bean.getFoodShopList();
		List<ShopInfo> amuseInfos = bean.getAmuseShopList();
		List<ShopInfo> hotelInfos = bean.getHotelShopList();
		
		if(CollectionUtils.isEmpty(foodInfos) || CollectionUtils.isEmpty(amuseInfos) || CollectionUtils.isEmpty(hotelInfos)){
			return null;
		}
		
		//计算每种商户的得分，并排序
		ScoreUtils.calScoreWithDisWeight(foodInfos, DISTANCE_WEIGHT_DEFAULT);
		ScoreUtils.calScoreWithDisWeight(amuseInfos, DISTANCE_WEIGHT_DEFAULT);
		ScoreUtils.calScoreWithDisWeight(hotelInfos, DISTANCE_WEIGHT_HOTEL);
		Collections.sort(foodInfos, new ScoreUtils.ShopScoreComparator());
		Collections.sort(amuseInfos,new ScoreUtils.ShopScoreComparator());
		Collections.sort(hotelInfos, new ScoreUtils.ShopScoreComparator());
		
		/*关键词结果插在最前*/
//		if(bean.getKeywordFoodShopInfos() != null)
//		{
//			foodInfos.addAll(0, bean.getKeywordFoodShopInfos());
//		}

//		if(bean.getKeywordAmuseShopInfos() != null){
//			amuseInfos.addAll(0, bean.getKeywordAmuseShopInfos());
//		}
		insertKeyWordResult(bean.getKeywordFoodShopInfos(), foodInfos);
		insertKeyWordResult(bean.getKeywordAmuseShopInfos(), amuseInfos);
		
		List<TimeSeg> timeSegs = TimeUtil.getTimeSegment(beginTime, endTime);
		
		String actionType = null;
		List<ShopInfo> recShopInfos = null;
		Action action = null;
		for(int k =0; k< timeSegs.size(); k++){
		    TimeSeg timeSeg = timeSegs.get(k);
			if(timeSeg.getHourSeg() == TimeUtil.TIME_MORING){
				//推荐早餐
				actionType = ActionTypeConst.MORNING_BREAKFAST;
				//recShopInfos = recmdShopsByPrice(foodInfos, MAX_BREAKFAST_PRICE, DEFAULT_REC_CNT);
				recShopInfos = recmdShops(foodInfos, MORNING_AMUSE_SET);
				action = new Action(recShopInfos, actionType, timeSeg);
				recmdResult.addAction(action);
				
				//推荐早上娱乐活动
				actionType = ActionTypeConst.MORNING_AMUSE;
				recShopInfos = recmdShops(amuseInfos);
				action = new Action(recShopInfos, actionType, timeSeg);
				recmdResult.addAction(action);
			}
			
			//推荐午餐
			else if(timeSeg.getHourSeg() == TimeUtil.TIME_NOON){
				actionType = ActionTypeConst.NOON_LUNCH;
				recShopInfos = recmdShops(foodInfos);
				action = new Action(recShopInfos, actionType, timeSeg);
				recmdResult.addAction(action);
			}
			
			//推荐下午活动
			else if(timeSeg.getHourSeg() == TimeUtil.TIME_AFTERNOON){
				actionType = ActionTypeConst.AFTER_NOON_AMUSE;
				recShopInfos = recmdShops(amuseInfos, AFTERNOON_AMUSE_SET);
				action = new Action(recShopInfos, actionType, timeSeg);
				recmdResult.addAction(action);
			}
			
			//
			else if(timeSeg.getHourSeg() == TimeUtil.TIME_EVENING){
				//推荐晚餐
				actionType = ActionTypeConst.EVENING_MEAL;
				recShopInfos = recmdShops(foodInfos);
				action = new Action(recShopInfos, actionType, timeSeg);
				recmdResult.addAction(action);
				
				//推荐晚上娱乐活动
				actionType = ActionTypeConst.EVENING_AMUSE;
				recShopInfos = recmdShops(amuseInfos, EVENING_AMUSE_SET);
				action = new Action(recShopInfos, actionType, timeSeg);
				recmdResult.addAction(action);
			}
			
			else {
				//推荐晚上娱乐活动
				actionType = ActionTypeConst.NIGHT_AMUSE;
				recShopInfos = recmdShops(amuseInfos, NIGHT_AMUSE_SET);
				action = new Action(recShopInfos, actionType, timeSeg);
				recmdResult.addAction(action);
				
				//推荐酒店
				if(k+1 < timeSegs.size())
				{
				    actionType = ActionTypeConst.NIGHT_HOTEL;
				    recShopInfos = recmdShops(hotelInfos);
				    action = new Action(recShopInfos, actionType, timeSeg);
				    recmdResult.addAction(action);
				}
			}
		}
		return recmdResult;
	}

	/***
	 * 添加keyword result 到最终结果里
	 * @param keyWordResult
	 * @param oriResult
	 */
	private void insertKeyWordResult(List<ShopInfo> keyWordResult, List<ShopInfo> oriResult){
		if(keyWordResult == null || oriResult == null){
			return;
		}
		
		int keyWordSize = keyWordResult.size();
		int oriSize = oriResult.size();
		if(keyWordSize >= oriSize){
			oriResult.addAll(0, keyWordResult.subList(0, 1));
		}
		int keyWordSegment = 1;//关键词结果步长
		int oriSegment = 10;//原始结果步长
		int keyWordIndex =0;//关键词结果下标
		int oriIndex = 0;//原始结果下标
		while(keyWordIndex < keyWordSize && oriIndex < oriSize){
			List<ShopInfo> keyWordSubResult = keyWordResult.subList(keyWordIndex, keyWordIndex + keyWordSegment);
			oriResult.addAll(oriIndex, keyWordSubResult);
			keyWordIndex += keyWordSegment;
			oriIndex += oriSegment;
		}
		
	}
	/***
	 * 根据价格过滤
	 * @param shopInfos
	 * @param maxPrice
	 * @param recCnt
	 * @return
	 */
	private List<ShopInfo> recmdShopsByPrice(List<ShopInfo> shopInfos, double maxPrice, int recCnt){
		List<ShopInfo> recShops = Lists.newArrayList();
		
		for(ShopInfo shopInfo : shopInfos){
			int shopid = shopInfo.getShopid();
			if(recmdShopIDs.contains(shopid))
				continue;
			double price = shopInfo.getPrice();
			if(price >= maxPrice)
				continue;
			recmdShopIDs.add(shopid);
			recShops.add(shopInfo);
			if(recShops.size() >= recCnt)
				break;
		}
		return recShops;
	}
	/***
	 * 根据类别过滤
	 * @param shopInfos
	 * @param maxPrice
	 * @param recCnt
	 * @return
	 */
	private List<ShopInfo> recmdShopsByCategory(List<ShopInfo> shopInfos, Set<String> categorySet,  int recCnt){
		List<ShopInfo> recShops = Lists.newArrayList();
		
		for(ShopInfo shopInfo : shopInfos){
			int shopid = shopInfo.getShopid();
			String shopCat = shopInfo.getShopCategory();
			if(recmdShopIDs.contains(shopid))
				continue;
			//不属于该时段应该推荐的分类
			if(!categorySet.contains(shopCat))
				continue;
			recmdShopIDs.add(shopid);
			double price = shopInfo.getPrice();
			
			
			recShops.add(shopInfo);
			if(recShops.size() >= recCnt)
				break;
		}
		return recShops;
	}
	private List<ShopInfo> recmdShops(List<ShopInfo> shopInfos){
		return recmdShopsByPrice(shopInfos, Integer.MAX_VALUE, DEFAULT_REC_CNT);
	}
	
	private List<ShopInfo> recmdShops(List<ShopInfo> shopInfos, Set<String> categorySet){
		return recmdShopsByCategory(shopInfos, categorySet, DEFAULT_REC_CNT);
	}
	
	public static void main(String[] args) {
		ShopSearchClient client = new ShopSearchClient();
 		double lng = 116.487823;
 		double lat = 39.88445;
 		int resultCnt = 1000;
 		int afford = 1;
 		List<ShopInfo> foodShops = client.getRecFoodShop(lng, lat, resultCnt, afford);
 		
 		System.out.println();
 		System.out.println();
 		
 		List<ShopInfo> amuseShops = client.getRecAmuseShop(lng, lat, resultCnt, afford);
 		
 		System.out.println();
 		System.out.println();
 		
 		List<ShopInfo> hotelShops = client.getRecHotelShop(lng, lat, resultCnt, afford);
 		
 		long beginTime = System.currentTimeMillis() - 10 * TimeUtil.ONE_HOUR;
 		long endTime = beginTime + 25 * TimeUtil.ONE_HOUR + new Random().nextInt(500000);
		
 		ShopInfoListBean bean = new ShopInfoListBean(beginTime, endTime, hotelShops, foodShops, amuseShops); 
		
 		RecommendEngine engine = new RecommendEngine();
 		RecmdResult result = engine.getRecmdResult(bean);
 		System.out.println("Final Result:\n" + result);
	}

}
