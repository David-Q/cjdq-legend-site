package com.play.util;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.bean.Action;
import com.play.bean.RecmdResult;
import com.play.bean.ShopInfo;
import com.play.consts.ActionTypeConst;
import com.play.consts.Consts;
import com.play.project.ProjectPart;

public class JsonUtils
{
    public static JSONObject shopInfoTojsonObject(ShopInfo shop){
        JSONObject object = new JSONObject();
        object.put(Consts.SHOPID, shop.getShopid());
        object.put(Consts.SHOPNAME, shop.getShopName());
        object.put(Consts.DISTANCE_X, shop.getDistance());
        object.put(Consts.AVG_PRICE, shop.getPrice());
        object.put(Consts.STARUI, shop.getShopStar());
        object.put(Consts.PIC_URL, shop.getPicURL());
        object.put(Consts.CITY_ID, shop.getCityId());
        return object;
    }
    
    public static String toJsonString(List<ProjectPart> parts)
    {
        JSONObject finalResult = new JSONObject();
        
        JSONArray partArray = new JSONArray();
        for(ProjectPart part:parts)
        {
            JSONObject partObject = new JSONObject();
            ShopInfo shopInfo = part.getShopInfo();
            JSONObject shopObject = shopInfoTojsonObject(shopInfo);
            partObject.put("shop", shopObject);
            partObject.put("type", part.getType());
            partObject.put("transit", part.getTransit());
            partObject.put("walking", part.getWalking());
            partObject.put("driving", part.getDriving());
            partObject.put("route_to_next", part.getRouteToNext());
            partArray.add(partObject);
        }
        
        finalResult.put("parts", partArray);
        return finalResult.toJSONString();
    }
    
    public static String toJsonString(RecmdResult result)
    {
        List<Action> actionList = result.getActionList();
        JSONObject finalResult = new JSONObject();
        
        JSONArray actionArray= new JSONArray();
        for(Action action:actionList)
        {
            JSONObject actionObject = new JSONObject();
            actionObject.put(Consts.ACTION_TYPE, action.getActionType());
            actionObject.put(Consts.ACTION_HOUR_SPAN, action.getTimeSeg().getHourSpan());
            actionObject.put(Consts.ACTION_DAY, action.getTimeSeg().getDay());
            actionObject.put(Consts.ACTION_NAME, ActionTypeConst.ACTION_NAME_MAP.get(action.getActionType()));
            JSONArray jsonArray = new JSONArray();
            List<ShopInfo> shopList =action.getShopList();
            for(ShopInfo shop:shopList)
            {
                JSONObject shopJson = shopInfoTojsonObject(shop);
                jsonArray.add(shopJson);
            }
            actionObject.put(Consts.SHOPS, jsonArray);
            actionArray.add(actionObject);
        }
        
        finalResult.put(Consts.ACTIONS, actionArray);
        finalResult.put(Consts.REC_TITLE, result.getResultTitle());
        finalResult.put(Consts.SCORE1, result.getScore1());
        finalResult.put(Consts.SCORE2, result.getScore2());
        finalResult.put(Consts.SCORE3, result.getScore3());
        finalResult.put(Consts.TOTAL_PRICE, result.getTotalPrice());
        
        return finalResult.toJSONString();
    }
    
    
}
