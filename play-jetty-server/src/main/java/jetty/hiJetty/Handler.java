

package jetty.hiJetty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.dp.arts.client.response.Record;
import com.dp.arts.client.util.StringUtils;
import com.play.bean.Action;
import com.play.bean.RecmdResult;
import com.play.bean.ShopInfo;
import com.play.bean.ShopInfoListBean;
import com.play.bean.TimeSeg;
import com.play.consts.Consts;
import com.play.engine.RecommendEngine;
import com.play.project.ProjectMaker;
import com.play.project.ProjectPart;
import com.play.search.Searcher;
import com.play.search.ShopSearchClient;
import com.play.util.JsonUtils;
import com.play.util.TimeUtil;

public class Handler extends AbstractHandler
{
    Searcher searcher = new Searcher();
    ShopSearchClient client = new ShopSearchClient();

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        if (request.getPathInfo().startsWith("/favicon.ico"))
        {
            return;
        }
        final StringBuilder content = new StringBuilder();
        
        baseRequest.setHandled(true);
        
       
        if (request.getPathInfo().startsWith("/mysql"))
        {
            List<Record> records = searcher.searchShop(Arrays.asList("10"), 121.471352, 31.228731 , 1000);
            List<ShopInfo> shopInfos = new ArrayList<ShopInfo>(records.size());
            for (Record record : records) 
            {
                int shopid = Integer.valueOf(record.get("shopid"));
                int distance = Double.valueOf(record.get("_distance_")).intValue();
                int shoppower = Integer.valueOf(record.get("shoppower"));
                String shopName = record.get("shopname");
                int price  = Integer.valueOf(record.get("avgprice"));
                String picURL = record.get("defaultpic");
                String shoptype = record.get("shoptype");
                
                ShopInfo shopInfo = new ShopInfo(shopid, distance, shopName, shoppower, price, picURL, shoptype);

                shopInfos.add(shopInfo);
            }
            
            RecmdResult result = new RecmdResult();

            Action action = new Action(shopInfos, Consts.ACTION_BREAKFAST, new TimeSeg(1, 1));
//            action.setActionType(Consts.ACTION_BREAKFAST);
//            action.setShopList(shopInfos);

            result.setActionList(Arrays.asList(action, action));
            result.setResultTitle("旅游计划");
            result.setScore1(12);
            result.setScore2(10);
            result.setScore3(15);
            
            content.append(JsonUtils.toJsonString(result));
        }
        else if(request.getPathInfo().startsWith("/make"))
        {
            String checked = request.getParameter("checked");
            String types = request.getParameter("types");
            String shops = request.getParameter("shops");
            
            List<ProjectPart> parts = ProjectMaker.createProject(checked, shops, types);
            content.append(JsonUtils.toJsonString(parts));
           
        }
        else if(request.getPathInfo().startsWith("/recmd"))
        {
            
            double glng = 121.417072;
            double glat = 31.219188;
            Map<String, String> paraMap = request.getParameterMap();
            if(paraMap.containsKey("glng"))
                glng= Double.valueOf(request.getParameter("glng"));
            if(paraMap.containsKey("glat"))
                glat= Double.valueOf(request.getParameter("glat"));
            
            
            int afford = 2;
            if(paraMap.containsKey("afford"))
            	afford = Integer.valueOf(request.getParameter("afford"));
            long beginTime = System.currentTimeMillis();
            long endTime = beginTime + 15 * TimeUtil.ONE_HOUR;
            
            if(paraMap.containsKey("starttime")){
            	String startTimeString = request.getParameter("starttime");
            	beginTime = TimeUtil.parseTimeString(startTimeString);
            }
           
            if(paraMap.containsKey("endtime")){
            	String endTimeString = request.getParameter("endtime");
            	endTime = TimeUtil.parseTimeString(endTimeString);
            }
            
            String eatkeyword = null;
            if(paraMap.containsKey("eatkeyword"))
            	eatkeyword = request.getParameter("eatkeyword");
            String playkeyword = null;
            if(paraMap.containsKey("playkeyword"))
            	playkeyword = request.getParameter("playkeyword");
            
            
            int resultCnt = 500;
            int searchResultCnt = 5;
            List<ShopInfo> keyWordFoodShops = null;
            if(eatkeyword != null & !eatkeyword.equals(""))
            {
            	keyWordFoodShops = client.getRecFoodShopByKeyWord(glng, glat, searchResultCnt, afford, eatkeyword);
            }
            List<ShopInfo> foodShops = client.getRecFoodShop(glng, glat, resultCnt, afford);

            
            System.out.println();
            System.out.println();
            List<ShopInfo> keyWordPlayShops = null;
            if(playkeyword != null & !playkeyword.equals(""))
            {
            	keyWordPlayShops = client.getRecAmuseShopByKeyWord(glng, glat, searchResultCnt, afford, playkeyword);
            }
            List<ShopInfo> amuseShops = client.getRecAmuseShop(glng, glat, resultCnt, afford);
            
            System.out.println();
            System.out.println();
            
            List<ShopInfo> hotelShops = client.getRecHotelShop(glng, glat, resultCnt, afford);
            
            /*long beginTime = System.currentTimeMillis() - 10 * TimeUtil.ONE_HOUR;
            long endTime = beginTime + 15 * TimeUtil.ONE_HOUR + new Random().nextInt(500000);
            */
            ShopInfoListBean bean = new ShopInfoListBean(beginTime, endTime, hotelShops, foodShops, amuseShops, keyWordFoodShops, keyWordPlayShops); 
            
            RecommendEngine engine = new RecommendEngine();
            RecmdResult result = engine.getRecmdResult(bean);
            String resultJson = JsonUtils.toJsonString(result);
            content.append(resultJson);
            System.out.println("Final Result:\n" + result);
     		
        }

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(content.toString());
    }
}
