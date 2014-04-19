/**
 * Project: userprofile
 * 
 * File Created at 2014-3-6
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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.play.bean.ShopInfo;

/**
 * TODO Comment of ScoreUtils
 * @author yao.ma
 *
 */
public class ScoreUtils {
	
	private static final Logger LOGGER = Logger.getLogger(ScoreUtils.class);
	
	public static final double DISTANCE_CURVE_HIGH = -0.9;
	public static final double DISTANCE_CURVE_LOW = -0.7;
	public static final int SHOP_DENSITY_DISTANTCE_MAX = 1000;
	
	public static final int DISTANCE_MIN = 100;
	
	public static final double DISTANCE_WEIGHT_08 = 0.8;
	public static final double DISTANCE_WEIGHT_07 = 0.7;
	public static final double DISTANCE_WEIGHT_06 = 0.6;
	public static final double DISTANCE_WEIGHT_04 = 0.4;
	public static final double DISTANCE_WEIGHT_02= 0.2;
	public static final double DISTANCE_WEIGHT_00= 0.0;
	
	public static final String SHOP_QUALITY_FILE = "tuangou_quanlity.txt";
	private static Map<Integer, Double> shopQualityScoreMap = Maps.newHashMap();
	
	static
	{
	    loadShopQualityMap();
	}
	
	/***
	 * 计算得分版本1：距离0.8，质量0.2
	 * @param shopInfos
	 */
	public static void calScore_dis08(List<ShopInfo> shopInfos){
		LOGGER.info("Calculate final score using smart sort version1");
		double finalScore = 0;
		calDistanceScore(shopInfos);
		calShopQualityScore(shopInfos);
		for(ShopInfo shopInfo : shopInfos){
			finalScore = finalScore(DISTANCE_WEIGHT_08, shopInfo.getDistanceScore(), shopInfo.getShopQualityScore());
			LOGGER.info("Distance:" + shopInfo.getDistance() + "\tDis Weight:" + DISTANCE_WEIGHT_08  + "\tdisScore: " + shopInfo.getDistanceScore()
					+ "\tQuality Score:" + shopInfo.getShopQualityScore() + "\tFinal Score:" + finalScore );
			shopInfo.setFinalScore(finalScore);
		}
	}
	
	/***
	 * 计算得分版本1：距离0.8，质量0.2, 距离最近3个距离分置为1
	 * @param shopInfos
	 */
	public static void calScore_dis08_top3(List<ShopInfo> shopInfos){
		LOGGER.info("Calculate final score using smart sort version1");
		double finalScore = 0;
		calDistanceScoreTop3(shopInfos);
		calShopQualityScore(shopInfos);
		for(ShopInfo shopInfo : shopInfos){
			finalScore = finalScore(DISTANCE_WEIGHT_08, shopInfo.getDistanceScore(), shopInfo.getShopQualityScore());
			LOGGER.info("Distance:" + shopInfo.getDistance() + "\tDis Weight:" + DISTANCE_WEIGHT_08  + "\tdisScore: " + shopInfo.getDistanceScore()
					+ "\tQuality Score:" + shopInfo.getShopQualityScore() + "\tFinal Score:" + finalScore );
			shopInfo.setFinalScore(finalScore);
		}
	}
	/***
	 * 计算得分版本1：距离0.7，质量0.3
	 * @param shopInfos
	 */
	public static void calScore_dis07(List<ShopInfo> shopInfos){
		LOGGER.info("Calculate final score using smart sort version2, dis 0.7");
		double finalScore = 0;
		calDistanceScore(shopInfos);
		calShopQualityScore(shopInfos);
		for(ShopInfo shopInfo : shopInfos){
			finalScore = finalScore(DISTANCE_WEIGHT_07, shopInfo.getDistanceScore(), shopInfo.getShopQualityScore());
			LOGGER.info("Distance:" + shopInfo.getDistance() + "\tDis Weight:" + DISTANCE_WEIGHT_07  + "\tdisScore: " + shopInfo.getDistanceScore()
					+ "\tQuality Score:" + shopInfo.getShopQualityScore() + "\tFinal Score:" + finalScore );
			shopInfo.setFinalScore(finalScore);
		}
	}
	
	/***
	 * 计算得分版本1：距离0.6，质量0.4
	 * @param shopInfos
	 */
	public static void calScore_dis06(List<ShopInfo> shopInfos){
		LOGGER.info("Calculate final score using smart sort version3, dis 0.6");
		double finalScore = 0;
		calDistanceScore(shopInfos);
		calShopQualityScore(shopInfos);
		for(ShopInfo shopInfo : shopInfos){
			finalScore = finalScore(DISTANCE_WEIGHT_06, shopInfo.getDistanceScore(), shopInfo.getShopQualityScore());
			LOGGER.info("Distance:" + shopInfo.getDistance() + "\tDis Weight:" + DISTANCE_WEIGHT_06  + "\tdisScore: " + shopInfo.getDistanceScore()
					+ "\tQuality Score:" + shopInfo.getShopQualityScore() + "\tFinal Score:" + finalScore );
			shopInfo.setFinalScore(finalScore);
		}
	}
	/**
	 * 根据距离权重计算距离分
	 * @param shopInfos
	 */
	public static void calScoreWithDisWeight(List<ShopInfo> shopInfos, double disweight){
		LOGGER.info("Calculate final score using smart sort version, distance score weight:"+ disweight);
		if(shopInfos == null)
			return;
		double finalScore = 0;
		calDistanceScore(shopInfos);
		calShopQualityScore(shopInfos);
		for(ShopInfo shopInfo : shopInfos){
			finalScore = finalScore(disweight, shopInfo.getDistanceScore(), shopInfo.getShopQualityScore());
			LOGGER.info("Distance:" + shopInfo.getDistance() + "\tDis Weight:" + disweight  + "\tdisScore: " + shopInfo.getDistanceScore()
					+ "\tQuality Score:" + shopInfo.getShopQualityScore() + "\tFinal Score:" + finalScore );
			shopInfo.setFinalScore(finalScore);
		}
	}
	/**
	 * 计算最终得分
	 * @param distanceWeight
	 * @param distanceScore
	 * @param shopScore
	 * @return
	 */
	public static double finalScore(double distanceWeight, double distanceScore, double shopScore){
		double finalScore = distanceWeight * distanceScore + 
				(1 - distanceWeight) * shopScore;
		return finalScore;
	}
	/***
	 * 商户密度是否高？
	 * 依据：1000米内的团购商户数大于20
	 * @param shopInfos
	 * @return
	 */
	public static boolean isShopDense(List<ShopInfo> shopInfos){
		if(shopInfos == null)
			return false;
		int cnt = 0;
		for(ShopInfo shopInfo : shopInfos){
			if(shopInfo.getDistance() < SHOP_DENSITY_DISTANTCE_MAX)
				cnt++;
		}
		if(cnt > 20)
			return true;
		return false;
	}
	
	
	/***
	 * 距离分计算
	 * @param shopInfos
	 */
	private static void calDistanceScore(List<ShopInfo> shopInfos){
		LOGGER.info("calculate distance score!");
		if(shopInfos == null)
		{
			LOGGER.info("shop result null!");
			return;
		}
		double disScore = 0;
		boolean isShopDense = isShopDense(shopInfos);
		
		for(ShopInfo shopInfo : shopInfos){
			int distance = shopInfo.getDistance();
			if(distance < DISTANCE_MIN){
				disScore = 1;
				shopInfo.setDistanceScore(disScore);
				continue;
			}
			int distanceSegment = distance / DISTANCE_MIN;
			if(isShopDense){
				disScore = Math.pow(distanceSegment, DISTANCE_CURVE_HIGH);
			}
			else {
				disScore = Math.pow(distanceSegment, DISTANCE_CURVE_LOW);
			}
			shopInfo.setDistanceScore(disScore);
		}
		
	}
	
	
	/***
	 * 距离分计算
	 * @param shopInfos
	 */
	private static void calDistanceScoreTop3(List<ShopInfo> shopInfos){
		LOGGER.info("calculate distance score! ");
		double disScore = 0;
		Collections.sort(shopInfos,  new DistanceComparator());
		boolean isShopDense = isShopDense(shopInfos);
		int i = 0;
		for(ShopInfo shopInfo : shopInfos){
			//最近3单得分置为1
			if(i++ < 3){
				disScore = 1;
				shopInfo.setDistanceScore(disScore);
				continue;
			}
			int distance = shopInfo.getDistance();
			if(distance < DISTANCE_MIN){
				disScore = 1;
				shopInfo.setDistanceScore(disScore);
				continue;
			}
			int distanceSegment = distance / DISTANCE_MIN;
			if(isShopDense){
				disScore = Math.pow(distanceSegment, DISTANCE_CURVE_HIGH);
			}
			else {
				disScore = Math.pow(distanceSegment, DISTANCE_CURVE_LOW);
			}
			shopInfo.setDistanceScore(disScore);
		}
		
	}
	
	/***
	 * 计算商户质量分
	 * @param shopInfos
	 */
	private static void calShopQualityScore(List<ShopInfo> shopInfos){
		LOGGER.info("calculate shop quality score!");
		if(shopInfos == null)
			return;
		double shopQualityScore = 0;
		if(shopQualityScoreMap == null || shopQualityScoreMap.size() < 1){
			loadShopQualityMap();
		}
		for(ShopInfo shopInfo : shopInfos){
			int shopid = shopInfo.getShopid();
			if(shopQualityScoreMap.containsKey(shopid)){
				shopQualityScore = shopQualityScoreMap.get(shopid);
			}
			shopInfo.setShopQualityScore(shopQualityScore);
		}
		
	}
	
	/***
	 * 读取商户质量分
	 */
	private static void loadShopQualityMap(){
		List<String> lines = null;;
		try {
			lines = Files.readLines(new File(SHOP_QUALITY_FILE), Charsets.UTF_8);
		} catch (IOException e) {
			LOGGER.error("Load file error!");
			e.printStackTrace();
			return;
		}
		for(String line : lines){
			String [] data = line.split("\t");
			if(data == null || data.length != 2)
				continue;
			int shopid = Integer.valueOf(data[0]);
			double score = Double.valueOf(data[1]);
			shopQualityScoreMap.put(shopid, score);
		}
		LOGGER.info("Shop Quality map read finished, size: " + shopQualityScoreMap.size());
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		loadShopQualityMap();
		System.out.println("shop score map size: " + shopQualityScoreMap.size());
		int index = 0;
		for(Map.Entry<Integer, Double> entry : shopQualityScoreMap.entrySet()){
			if(index++ > 100){
				break;
			}
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}

	}
	
	public static class ShopScoreComparator implements Comparator<ShopInfo> {


	    public int compare(ShopInfo o1, ShopInfo o2) {
	        if(o1.getFinalScore() > o2.getFinalScore()){
	            return -1;
	        }
	        else if(o1.getFinalScore() < o2.getFinalScore()){
	            return 1;           
	        }
	        return 0;
	    }

	}

    public static class DistanceComparator implements Comparator<ShopInfo> {

        public int compare(ShopInfo o1, ShopInfo o2) {
            if(o1.getDistance() < o2.getDistance()){
                return -1;
            }
            else if(o1.getDistance() > o2.getDistance()){
                return 1;
            }
            return 0;
        }

    }

}
