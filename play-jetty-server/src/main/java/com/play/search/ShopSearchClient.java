/**
 * Project: play-jetty-server
 * 
 * File Created at 2014-4-11
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
package com.play.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.dp.arts.client.SearchClient;
import com.dp.arts.client.function.DistanceFunction;
import com.dp.arts.client.request.GeoQuery;
import com.dp.arts.client.request.KeywordQuery;
import com.dp.arts.client.request.LeafNode;
import com.dp.arts.client.request.Poi;
import com.dp.arts.client.request.Query;
import com.dp.arts.client.request.RangeQuery;
import com.dp.arts.client.request.Request;
import com.dp.arts.client.request.Request.Platform;
import com.dp.arts.client.request.Request.SortOrder;
import com.dp.arts.client.request.SortItem;
import com.dp.arts.client.request.StatItem;
import com.dp.arts.client.request.TermQuery;
import com.dp.arts.client.response.Record;
import com.dp.arts.client.response.Response;
import com.dp.arts.client.response.StatResult;
import com.play.bean.RecmdResult;
import com.play.bean.ShopInfo;
import com.play.bean.ShopInfoListBean;
import com.play.consts.Consts;
import com.play.engine.RecommendEngine;
import com.play.util.TimeUtil;

/**
 * TODO Comment of ShopSearchClient
 * @author yao.ma
 *
 */
public class ShopSearchClient {
	
	private static Logger LOGGER = Logger.getLogger(ShopSearchClient.class);
	public static final String DEFAULT_SEARCH_SERVIE = "http://192.168.5.149:4053/search/shop";
	public static final int DEFAULT_DISTANCE = 5000;
	public static final String DEFAULT_CITY = "1";
	
	public static final String CATEGORY_FOOD = "10";
	public static final String CATEGORY_AMUSE = "30";
	public static final String CATEGORY_HOTEL = "60";
	
	public static final String SHOP_TYPE_FOOD = "type_food";
	public static final String SHOP_TYPE_AMUSE = "type_amuse";
	public static final String SHOP_TYPE_HOTEL = "type_hotel";
	
	public static final String CONFIG_PATH = "configuration.xml";
	
	private static SearchClient searchClient  = new SearchClient(DEFAULT_SEARCH_SERVIE);
	static
	{
	 // 设置搜索服务的url或者LionKey，这里使用beta环境
        // searchclient参数设置和初始化：
	    searchClient.setLogExceptionToCat(false); //是否需要将客户端异常记录到Cat
	    searchClient.setMaxConnections(128);  //客户端连接server端的最大连接数
	    searchClient.setMaxRetryTimes(3);  //设置server端连接不上时最多的重试次数
	    searchClient.setSearchTimeout(5000);  //设置接受server端响应的最大时间
	    searchClient.init();
	}
	
	public SearchClient getSearchClient() {
		return searchClient;
	}

	public Request getSearchRequest() {
		// 新建搜索请求对象，设置请求的平台，以及请求的应用名，比如手机商户搜索，设置如下：
		Request request = new Request(Platform.MAPI, "shop");

		return request;
	}

	/**
	 * 设置类别
	 * 
	 * @param request
	 * @param category
	 */
	public void setShopType(Request request, String type) {

		Query query= null;
		// 创建精确匹配查询，
		//第一个参数为匹配查询的字段名，该字段属性为index
		//第二个参数为查询内容
		//对商户频道为美食（美食的频道id=10）的查询如下：
		query = new TermQuery("shoptype", type);
		request.addQuery(query);
	}

	/***
	 * 设置城市
	 * 
	 * @param request
	 * @param city
	 */
	public void setCity(Request request, String city) {
		TermQuery cityQuery = null;
		if (city == null)
			cityQuery = new TermQuery("cityid", DEFAULT_CITY);
		else {
			cityQuery = new TermQuery("cityid", city);
		}
		request.addQuery(cityQuery);
	}

	/**
	 * 设置搜索返回的结果字段
	 * 
	 * @param request
	 */
	public void setOutputField(Request request) {
		
		// 返回字段，这些字段属性为store
		request.addOutputField("cityid");
		request.addOutputField("shopname");
		request.addOutputField("shopid");
		request.addOutputField("avgprice");
		request.addOutputField("poi");
		request.addOutputField("shoppower");
		request.addOutputField("defaultpic");
		request.addOutputField("businesshours");
		request.addOutputField("category2");
		request.addOutputField("shoptags");
		request.addOutputField("shoptagssearch");
		request.addOutputField("maincategoryname");
		// 返回商户距离，和附近查询方式类似，在dist中的第一个参数为商户的经纬度字段名，第二个参数为坐标，结果会在_distance_字段中返回，单位为米
		//request.addOutputField("dist(poi,121.471352:31.228731)");
	}

	/***
	 * 获取原始的搜索结果
	 * 
	 * @param searchClient
	 * @param request
	 * @return
	 */
	public List<Record> getSearchResult(SearchClient searchClient,
			Request request) {
		Response response;
		try {
			LOGGER.info("Request: " + request.toString());
			response = searchClient.search(request);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if (response.getStatus() != Response.OK) {
			LOGGER.error("Response error : " + response.getErrorMessage());

			return null;
		}
		int totalHits = response.getTotalHits();
		LOGGER.info("total hits:" + totalHits);
		List<Record> records = response.getRecordList();
		return records;
	}
	
	/**转换搜索结果类型*/
	public List<ShopInfo> transformResult(List<Record> oriResult, String shopType){
		if(oriResult == null){
			LOGGER.error("original search result null!");
		}
		List<ShopInfo> shopInfos = new ArrayList<ShopInfo>(oriResult.size());
		for (Record record : oriResult) {
			int shopid = Integer.valueOf(record.get("shopid"));
			int distance = Double.valueOf(record.get("_distance_")).intValue();
			int shoppower = Integer.valueOf(record.get("shoppower"));
			String shopName = record.get("shopname");
			int price  = Integer.valueOf(record.get("avgprice"));
			String picURL = record.get("defaultpic");
			String cityid = record.get("cityid");
			
			String cat = record.get("maincategoryname");
			ShopInfo shopInfo = new ShopInfo(shopid, distance, shopName, shoppower, price, picURL, shopType, cat);
			shopInfo.setCityId(cityid);
			shopInfos.add(shopInfo);
			//System.out.println("Record: " + record);
		}
		return shopInfos;
	}
	/**
	 * 设置用户位置
	 * @param request
	 * @param lat
	 * @param lng
	 */
	public void setPos(Request request, double lat, double lng, int distance){
		// 创建附近查询，
		//第一个参数为地理位置的字段名，该字段属性为index，docvalues
		//第二个参数为查询的坐标，用冒号分割，前经度（lng）后纬度（lat）
		//第三个参数为距离，单位为米
		//对商户的poi字段 做 lng=121.471352，lat=31.228731 周围 2000米的查询如下：
		Query query = new GeoQuery("poi", lng, lat, distance);
		request.addQuery(query);
 	}
	
	public String getPosStr(double lng, double lat){
		return "dist(poi," + lng + ":" + lat + ")";
	}
	
	/***
	 * 限定商户星级在三星以上
	 * @param request
	 */
	public void setStarRange(Request request){

		// 创建范围查询，
		//第一个参数为范围查询的字段名，该字段属性为docvalues
		//第二个参数为查询的下界（包含下界点）
		//第三个参数为查询的上界（不包含上界点）
		//对商户人均 80>价格>=50 的查询如下：
		Query query = new RangeQuery("shoppower", "20", "100");
		request.addQuery(query);
	}
	
	/***
	 * 限定价格大于0
	 * @param request
	 */
	public void setPriceRange(Request request, int priceLow, int priceHigh){

		// 创建范围查询，
		//第一个参数为范围查询的字段名，该字段属性为docvalues
		//第二个参数为查询的下界（包含下界点）
		//第三个参数为查询的上界（不包含上界点）
		//对商户人均 80>价格>=50 的查询如下：
		Query query = new RangeQuery("avgprice", priceLow + "", priceHigh + "");
		request.addQuery(query);
	}
	/***
	 * 发送一次请求获取用户城市
	 * @return
	 */
	public String getCityid(double lng, double lat){
		SearchClient searchClient = this.getSearchClient();
		Request request = this.getSearchRequest();
		this.setShopType(request, CATEGORY_FOOD);
		this.setPos(request, lat, lng, DEFAULT_DISTANCE);
//		double lng = 121.415768;
//		double lat = 31.21827;
		request.addStatItem(new StatItem("cityid"));
		Response response  =null;
		try {
			response = searchClient.search(request);
		} catch (IOException e) {
			LOGGER.error("response error ", e);
		}
		if(response == null){
			return DEFAULT_CITY;
		}
		StatResult statResult = response.getStatResult("cityid");
		if(statResult == null || statResult.getStMap() == null){
			return DEFAULT_CITY;
		}
		String city =  DEFAULT_CITY;
		int maxCont = 0;
		for(Map.Entry<String, Integer> entry : statResult.getStMap().entrySet()){
			String cityid = entry.getKey();
			int count = entry.getValue();
			if(count > maxCont){
				maxCont = count;
				city = cityid;
			}
		}
		return city;
		
	}
	/** 模拟搜索 */
	public void simSearch() {
		SearchClient searchClient = this.getSearchClient();
		Request request = this.getSearchRequest();
		this.setCity(request, DEFAULT_CITY);
		this.setShopType(request, CATEGORY_FOOD);
		double lng = 116.341095;
		double lat = 39.724089;
		//String city = getCityid(lng, lat);
		//LOGGER.info("lng: "+ lng + "\tlat:"+ lat + "\tcity: "+ city);
		
		// 排序可以做多维排序，会按照加入的先后做维度排序。
		//排序方法也支持dist，如果需要按商户距离排序，可以这样写：
		request.addSortItem(new SortItem(getPosStr(lng, lat), SortOrder.ASC));
		this.setOutputField(request);
		request.addOutputField(getPosStr(lng, lat));

		//request.addInfo("lng", "" + lng); // 经纬度，手机段必须要填写
		//request.addInfo("lat", "" + lat);
		request.addInfo("geotype", "shopgooglepos"); // 可以是默认经纬度shoppos,google经纬度shopgooglepos

		request.setLimit(0, 100);
		List<Record> result = this.getSearchResult(searchClient, request);
		if (result == null) {
			LOGGER.error("Result null!");
			return;
		}
		List<ShopInfo> shopInfos = transformResult(result, CATEGORY_FOOD);
		for(ShopInfo shopInfo : shopInfos){
			System.out.println(shopInfo);
		}

	}

	/***
	 * 获取某个类别的商户结果,无关键词
	 * @param lng
	 * @param lat
	 * @param shopType
	 * @param resultCnt
	 * @return
	 */
	public List<ShopInfo> getShopResult(double lng, double lat, String shopType, int resultCnt, int afford){
		return getShopResult(lng, lat, shopType, resultCnt, afford, null);
	}
	
	/***
	 * 根据关键词获取某个类别的商户结果
	 * @param lng
	 * @param lat
	 * @param shopType
	 * @param resultCnt
	 * @return
	 */
	public List<ShopInfo> getShopResult(double lng, double lat, String shopType, int resultCnt, int afford, String keyWord){
		SearchClient searchClient = this.getSearchClient();
		Request request = this.getSearchRequest();
		String city = getCityid(lng, lat);
		if(city != null){
			this.setCity(request, city);
		}
		else {
			this.setCity(request, DEFAULT_CITY);
		}
		
		int priceLow = getPriceLow(shopType, afford);
        int priceHigh = getPriceHigh(shopType, afford);
        
		if(keyWord != null)
		{
		    this.setKeyWord(request, keyWord);
		    priceLow = 1;
		    priceHigh = 9999;
		}
		
		this.setShopType(request, shopType);
		this.setStarRange(request);
		
		this.setPriceRange(request, priceLow, priceHigh);
		System.out.println("lng: "+ lng + "\tlat:"+ lat + "\tType: "+ shopType + "\tprice low: " + 
		priceLow + "\tprice high: " + priceHigh);
		
		// 排序可以做多维排序，会按照加入的先后做维度排序。
		//排序方法也支持dist，如果需要按商户距离排序，可以这样写：
		request.addSortItem(new SortItem(getPosStr(lng, lat), SortOrder.ASC));
		this.setOutputField(request);
		request.addOutputField(getPosStr(lng, lat));

		request.addInfo("geotype", "shopgooglepos"); // 可以是默认经纬度shoppos,google经纬度shopgooglepos

		request.setLimit(0, resultCnt);
		List<Record> result = this.getSearchResult(searchClient, request);
		if (result == null) {
			LOGGER.error("Result null!");
			return null;
		}
		List<ShopInfo> shopInfos = transformResult(result, shopType);
		LOGGER.info("Result of type: " + shopType);
		for(ShopInfo shopInfo : shopInfos){
			System.out.println(shopInfo);
		}
		
		return shopInfos;

	}
	
	public void setKeyWord(Request request, String keyWord){
		if(keyWord != null){
			Query query = new KeywordQuery("searchkeyword", keyWord);
			request.addQuery(query);
		}
	}
	/**
	 * @param shopType
	 * @param afford
	 * @return
	 */
	private int getPriceHigh(String shopType, int afford) {
		if(shopType.equals(CATEGORY_FOOD)){
			if(afford == Consts.AFFORD_LOW){
				return 20;
			}
			else if(afford == Consts.AFFORD_NORMAL){
				return 100;
			}
			else {
				return 2000;
			}
		}
		else if(shopType.equals(CATEGORY_AMUSE)){
			if(afford == Consts.AFFORD_LOW){
				return 30;
			}
			else if(afford == Consts.AFFORD_NORMAL){
				return 100;
			}
			else {
				return 2000;
			}
		}
		else if(shopType.equals(CATEGORY_HOTEL)){
			if(afford == Consts.AFFORD_LOW){
				return 100;
			}
			else if(afford == Consts.AFFORD_NORMAL){
				return 250;
			}
			else {
				return 2000;
			}
		}
		return 0;
	}

	/**
	 * @param shopType
	 * @param afford
	 * @return
	 */
	private int getPriceLow(String shopType, int afford) {
		if(shopType.equals(CATEGORY_FOOD)){
			if(afford == Consts.AFFORD_LOW){
				return 1;
			}
			else if(afford == Consts.AFFORD_NORMAL){
				return 30;
			}
			else {
				return 80;
			}
		}
		else if(shopType.equals(CATEGORY_AMUSE)){
			if(afford == Consts.AFFORD_LOW){
				return 1;
			}
			else if(afford == Consts.AFFORD_NORMAL){
				return 30;
			}
			else {
				return 80;
			}
		}
		else if(shopType.equals(CATEGORY_HOTEL)){
			if(afford == Consts.AFFORD_LOW){
				return 20;
			}
			else if(afford == Consts.AFFORD_NORMAL){
				return 100;
			}
			else {
				return 250;
			}
		}
		return 0;
	}

	/***
	 * 获取美食类商户结果
	 * @param lng
	 * @param lat
	 * @param resultCnt
	 * @return
	 */
	public List<ShopInfo> getRecFoodShop(double lng, double lat, int resultCnt, int afford){
		return getShopResult(lng, lat, CATEGORY_FOOD , resultCnt, afford);
	}
	
	/**
	 * 根据关键词获取美食类结果
	 * @param lng
	 * @param lat
	 * @param resultCnt
	 * @param afford
	 * @param keyword
	 * @return
	 */
	public List<ShopInfo> getRecFoodShopByKeyWord(double lng, double lat, int resultCnt, int afford, String keyword){
		return getShopResult(lng, lat, CATEGORY_FOOD , resultCnt, afford, keyword);
	}
	
	
	/**
	 * @param glng
	 * @param glat
	 * @param searchResultCnt
	 * @param afford
	 * @param playkeyword
	 * @return
	 */
	public List<ShopInfo> getRecAmuseShopByKeyWord(double lng, double lat, int resultCnt, int afford, String keyword) {
		return getShopResult(lng, lat, CATEGORY_AMUSE, resultCnt, afford, keyword);
	}
	/***
	 * 获取娱乐类商户结果
	 * @param lng
	 * @param lat
	 * @param resultCnt
	 * @return
	 */
	public List<ShopInfo> getRecAmuseShop(double lng, double lat, int resultCnt, int afford){
		return getShopResult(lng, lat, CATEGORY_AMUSE , resultCnt, afford );
	}
	
	/***
	 * 获取酒店类商户结果
	 * @param lng
	 * @param lat
	 * @param resultCnt
	 * @return
	 */
	public List<ShopInfo> getRecHotelShop(double lng, double lat, int resultCnt, int afford){
		return getShopResult(lng, lat, CATEGORY_HOTEL , resultCnt, afford );
	}
	public static void main(String[] args) {
		ShopSearchClient client = new ShopSearchClient();
 		double lng = 121.417072;
 		double lat = 31.219188;
 		int resultCnt = 100;
 		int afford = 1;
 		List<ShopInfo> foodShops = client.getRecFoodShop(lng, lat, resultCnt, afford);
 		
 		System.out.println();
 		System.out.println();
 		
 		List<ShopInfo> amuseShops = client.getRecAmuseShop(lng, lat, resultCnt, afford);
 		
 		System.out.println();
 		System.out.println();
 		
 		List<ShopInfo> hotelShops = client.getRecHotelShop(lng, lat, resultCnt, afford);
 		
 		long beginTime = System.currentTimeMillis() - 10 * TimeUtil.ONE_HOUR;
		long endTime = beginTime + 15 * TimeUtil.ONE_HOUR + new Random().nextInt(500000);
		
 		ShopInfoListBean bean = new ShopInfoListBean(beginTime, endTime, hotelShops, foodShops, amuseShops); 
		
 		RecommendEngine engine = new RecommendEngine();
 		RecmdResult result = engine.getRecmdResult(bean);
 		System.out.println("Final Result:\n" + result);
	}

	

}
