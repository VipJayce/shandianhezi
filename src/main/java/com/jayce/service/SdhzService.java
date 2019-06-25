package com.jayce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayce.comm.util.EmptyUtil;
import com.jayce.comm.util.FileUtil;
import com.jayce.comm.util.HttpUtils;
import com.jayce.service.ad.CaptureService;
import com.jayce.util.ReadFileUtil;

public class SdhzService {

//	static {
//		Map<String, String> user1Cookie = new HashMap<String, String>();
//		user1Cookie.put("Cookie",
//				"csrftoken=3AvKfk8Ez67yhLjRQzZv0GhfkZ55cIgiHK1Mwdhs3JjIgXvRvnKa2NOTKaNm0Eiz;sessionid=n8d1zkgbd0keu7cnbvwn9l9dk9wqw4yh");
//		user1Cookie.put("X-CSRFToken", "3AvKfk8Ez67yhLjRQzZv0GhfkZ55cIgiHK1Mwdhs3JjIgXvRvnKa2NOTKaNm0Eiz");
//		CommConstants.USER_COOKIE_CACHE.put("网服Q193&wh1234", user1Cookie);
//		Map<String, String> user2Cookie = new HashMap<String, String>();
//		user2Cookie.put("Cookie",
//				"csrftoken=RQUZOKpk6Qzr869e1NZsLBsZgsanjF3tivgOvMVPj14EQh52N0bWI1u8t9yU0WWD;sessionid=xxmedxktqxjkt0f94fesk6gfhtbab4tv");
//		user2Cookie.put("X-CSRFToken", "RQUZOKpk6Qzr869e1NZsLBsZgsanjF3tivgOvMVPj14EQh52N0bWI1u8t9yU0WWD");
//		CommConstants.USER_COOKIE_CACHE.put("兼职A2&wh1234", user2Cookie);
//	}

	public static void main(String[] args) throws Exception {
		CaptureService service = new CaptureService();
		//1、获取用户id
		String cookie = "csrftoken=Cyj0vM0LFsQZZYdHwaAmjqVw3xSv0bMD4cS18nI9z4hhB7v1TlF9cDdV9podNPMA; sessionid=g7uywe4i6nz0l36uyxufn2zlkgl56hfc";
		String csrftoken = "Cyj0vM0LFsQZZYdHwaAmjqVw3xSv0bMD4cS18nI9z4hhB7v1TlF9cDdV9podNPMA";
		String url = "http://ad.shandianhezi.com/account_amount/?day_number=0";
		String str = HttpUtils.sendGet(url, cookie);
		JSONArray arr1 = JSONArray.parseArray(str);
		System.out.println("广告id\t曝光量\t点击数\t消耗金额");
		for(int i=0;i<arr1.size();i++) {
			JSONArray arr2 = JSONArray.parseArray(JSONObject.toJSONString(arr1.get(i)));
			for(int j=0;j<arr2.size();j++) {
				JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(arr2.get(j)));
				System.out.println(json.get("campaign_id")+"\t"+json.getInteger("impression")+"\t"+json.getInteger("click")+"\t"+json.getDouble("cost"));
			}
		}
//		System.out.println(str);
//		String updatePriceUrl = "http://ad.shandianhezi.com/ad_campaing/593025/";
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("advertiser", trivalId);
//		map.put("name", trivalId);
//		map.put("start_time", trivalId);
//		map.put("end_time", trivalId);
//		map.put("budget", trivalId);
//		map.put("start_hour_min_second", trivalId);
//		map.put("end_hour_min_second", trivalId);
//		map.put("unit_price", trivalId);
//		map.put("campaing_style", trivalId);
//		map.put("purpose", trivalId);
//		Connection con = Jsoup.connect(updatePriceUrl); // 获取connection
//		con.header(HttpUtils.USER_AGENT, HttpUtils.USER_AGENT_VALUE); // 配置模拟浏览器
//		con.header("Cookie", cookie); // 配置模拟浏览器
//		con.header("X-CSRFToken", csrftoken); // 配置模拟浏览器
//		con.ignoreContentType(true).followRedirects(true).method(Method.PUT); // 获取响应
//		con.data(map);
//		Response rs = con.execute();
//		System.out.println(rs);
		
		
		
		
		
//		CaptureService service = new CaptureService();
//		String username = "兼职A2";
//		String username = "网服Q193";
//		String pwd = "wh1234";
//		String userKey = username + "&" + pwd;
		// 1、用户登录
//		List<String> list = new ArrayList<String>();
//		list.add("兼职A2");
//		list.add("网服Q193");
//		list.add("网服Q131");
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("兼职A2", "wh1234");
//		map.put("网服Q193", "wh1234");
//		map.put("网服Q131", "wh1234");
//		for (Entry<String,String> entry : map.entrySet()) {
//			service.simulateLogin(entry.getKey(), entry.getValue());
//		}
//		simulateLogin(username, pwd);
//		simulateLogin("网服Q193", pwd);
//		simulateLogin("网服Q131", pwd);
		// 2、获取每日消耗
//		System.out.println(getRecharge(userKey, "2019-06-13", "2019-06-13"));;
		// 3、获取所有广告列表
//		List<JSONObject> allList = getAllAdNumbers(userKey);
//		for (JSONObject json : allList) {
//			System.out.println(json.get("id") + "\t" + json.get("status"));
//		}
		// 4、根据状态获取所有广告列表
//		List<JSONObject> allList = queryAdNumbersByStatus(userKey, AdStatusEnum.PAUSE.getKey());
//		for (JSONObject json : allList) {
//			System.out.println(json.get("id") + "\t" + json.get("status"));
//		}
		// 5、改变广告投放状态
//		String reponse = changeAdStatus(userKey,"556252",AdStatusEnum.PAUSE.getKey());
//		System.out.println(reponse);
//		queryCurrentCost(userKey);
	}

}
