package com.jayce.service.ad;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayce.comm.constants.AdStatusEnum;
import com.jayce.comm.constants.CommConstants;
import com.jayce.comm.util.EmptyUtil;
import com.jayce.comm.util.HttpUtils;

@Service
public class CaptureService {
	private Logger logger = LoggerFactory.getLogger(CaptureService.class);
	/**
	 * 用户登录
	 * 
	 * @param userName 用户名
	 * @param pwd      密码
	 * @throws Exception
	 */
	public Map<String,String> simulateLogin(String userName, String pwd) {
		Map<String, String> userCookie = new HashMap<String, String>();
		// 第一次请求 grab login form page first 获取登陆提交的表单信息，及修改其提交data数据（login，password）
		try {
			Response rs = HttpUtils.JsoupGET(CommConstants.LOGIN_URL, null);
			Document d1 = Jsoup.parse(rs.body()); // 转换为Dom树
			List<Element> eleList = d1.select("form"); // 获取提交form表单，可以通过查看页面源码代码得知
			Map<String, String> datas = new HashMap<>();
			for (int i = 0; i < eleList.size(); i++) {
				for (Element e : eleList.get(i).getAllElements()) {
					// 设置用户名
					if (e.attr("name").equals("username")) {
						e.attr("value", userName);
					}
					// 设置用户密码
					if (e.attr("name").equals("password")) {
						e.attr("value", pwd);
					}
					// 排除空值表单属性
					if (e.attr("name").length() > 0) {
						datas.put(e.attr("name"), e.attr("value"));
					}
				}
			}
			// 第二次请求，以post方式提交表单数据以及cookie信息
			Response login = HttpUtils.JsoupPOST(CommConstants.LOGIN_URL, datas, rs.cookies());
			Map<String, String> cookieMap = login.cookies();
			String cookie = "";
			for (String key : cookieMap.keySet()) {
				cookie += key + "=" + cookieMap.get(key) + ";";
			}//
			cookie = cookie.substring(0, cookie.length() - 1);
			if(cookie.contains("sessionid")) {//取到sessionId代表登录成功
				userCookie.put("Cookie", cookie);
				userCookie.put("X-CSRFToken", cookieMap.get("csrftoken"));
				logger.info("用户"+userName+"登录成功:"+cookie);
			}
			
		} catch (Exception e1) {
			logger.error("用户"+userName+"登录失败",e1);
		}
		return userCookie;
	}
	
	/**
	 * 前面登录获取到的cookie信息 获取每日消耗金额
	 * 
	 * @param cookie   	cookie
	 * @param startTime 开始日
	 * @param endTime   结束日 [{"date": "2019-06-13", "advertiser": 4868,
	 *                  "total_cost": 12328.5, "total_recharge": 8000.07}, {"date":
	 *                  "2019-06-13", "total_recharge": 8000.07, "advertiser":
	 *                  4868}, {"date": "2019-06-12", "advertiser": 4868,
	 *                  "total_cost": 23190.6, "total_recharge": 8000.07}, {"date":
	 *                  "2019-06-12", "total_recharge": 8000.07, "advertiser":
	 *                  4868}]
	 * @return
	 * @throws IOException
	 */
	public BigDecimal getRecharge(String cookie, String startTime, String endTime) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("start_time", startTime);// "2019-06-05"
		map.put("end_time", endTime);
		BigDecimal totalCost = BigDecimal.ZERO;
		try {
			String str = HttpUtils.sendGet(CommConstants.RECHARGE_URL, map, cookie);
//			System.out.println(str);
			if (!EmptyUtil.isEmpty(str)) {
				JSONArray array = JSONArray.parseArray(str);
				for (Object object : array) {
					JSONObject obj = (JSONObject) object;
					if (!EmptyUtil.isEmpty(obj.get("total_cost"))) {
						totalCost = (BigDecimal) obj.get("total_cost");
					}
				}
			}
		} catch (Exception e) {
			logger.error("用户("+cookie+")获取每日消耗失败",e);
		}
		return totalCost;
	}

	/***
	 * 查询账户的余额
	 * <span id="sur_money">5578.16元</span>
	 * @param cookie
	 * @return
	 * @throws Exception
	 */
	public BigDecimal queryCurrentCost(String cookie) {
		try {
			// 获得2个金额，一个为现金余额 ，一个为>赠送金额余额，取第一个
			String str = HttpUtils.sendGet(CommConstants.QUERY_INDEX_URL, cookie);
			Document d1 = Jsoup.parse(str); // 转换为Dom树
			List<Element> eleList = d1.select("#sur_money"); // 获取提交form表单，可以通过查看页面源码代码得知
			String ss = eleList.get(0).text();
			ss = ss.substring(0, ss.length() - 1);
			return new BigDecimal(ss);
		} catch (Exception e) {
			logger.error("用户("+cookie+")查询账户余额失败",e);
		}
		return BigDecimal.ZERO;
	}
	
	/***
	 * 查询用户登录id
	 * <span id="base_username_id" style="margin:0 10px;font-size:25px;line-height:35px;color:#fff; display: none">4386</span>
	 * @param cookie
	 * @return
	 * @throws Exception
	 */
	public String queryUserNameId(String cookie) {
		String trivalId = "";
		try {
			String str = HttpUtils.sendGet(CommConstants.QUERY_INDEX_URL, cookie);
			Document d1 = Jsoup.parse(str); // 转换为Dom树
			List<Element> eleList = d1.select("#base_username_id"); // 获取提交form表单，可以通过查看页面源码代码得知
			trivalId = eleList.get(0).text();//用户id
		} catch (Exception e) {
			logger.error("用户("+cookie+")查询用户id失败",e);
		}
		return trivalId;
	}
	
	/***
	 * 将账户多条广告的状态转换为账户的某个状态
	 * 
	 * @param userKey
	 * @return
	 */
	public String getStatusByUserKey(String cookie,String csrfToken) {
		try {
			//1、查询是否有投放中的广告
			String queryUrl = CommConstants.QUERY_ALL_ADS_BY_STATUS_URL + "?search="+AdStatusEnum.START.getKey()+"&_=" + new Date().getTime();
			String returnStr = HttpUtils.sendGet(queryUrl, cookie, csrfToken);
			JSONObject json = JSONObject.parseObject(returnStr);
			int tfzCount = Integer.parseInt(String.valueOf(json.get("count")));
			//2、查询是否有暂停中的广告
			queryUrl = CommConstants.QUERY_ALL_ADS_BY_STATUS_URL + "?search="+AdStatusEnum.PAUSE.getKey()+"&_=" + new Date().getTime();
			returnStr = HttpUtils.sendGet(queryUrl, cookie, csrfToken);
			json = JSONObject.parseObject(returnStr);
			int	tzCount = Integer.parseInt(String.valueOf(json.get("count")));
			if(tfzCount == 0 && tzCount == 0) {
				return "其他状态";
			}
			if(tfzCount != 0 && tzCount != 0) {
				return "部分暂停部分投放中";
			}
			//3、查询总广告个数
			returnStr = HttpUtils.sendGet(CommConstants.QUERY_ALL_ADS_URL, cookie, csrfToken);
			json = JSONObject.parseObject(returnStr);
			int allCount = Integer.parseInt(String.valueOf(json.get("count")));
			if(tfzCount == 0 && tzCount!=0) {
				if(tzCount == allCount) {
					return "全部暂停";
				}else {
					return "部分暂停且无投放中";
				}
			}
			if(tfzCount != 0 && tzCount==0) {
				if(tfzCount == allCount) {
					return "全部投放中";
				}else {
					return "部分投放中且无暂停";
				}
			}
		} catch (Exception e) {
			logger.error("用户("+cookie+")获取广告状态失败",e);
		}
		return "状态有误";
	}
	
	/**
	 * 根据广告状态获取所有广告信息列表
	 * 
	 * @param cookie 用户信息
	 * @param status  状态
	 *                http://ad.shandianhezi.com/search_ad_campaing/?search=yebz&_=1560421289383
	 * @return
	 */
	public List<JSONObject> queryAdNumbersByStatus(String cookie,String csrfToken, String status) {
		List<JSONObject> allList = new ArrayList<JSONObject>();
		String url = CommConstants.QUERY_ALL_ADS_BY_STATUS_URL + "?search=" + status + "&_=" + new Date().getTime();
		travelseAds(allList, cookie, url, csrfToken);
		return allList;
	}

	/**
	 * 获取所有广告信息列表
	 * 
	 * @param cookie 用户信息
	 * @return
	 */
	public List<JSONObject> getAllAdNumbers(String cookie) {
		List<JSONObject> allList = new ArrayList<JSONObject>();
		travelseAds(allList, cookie, CommConstants.QUERY_ALL_ADS_URL, "");
		return allList;
	}

	/**
	 * 遍历广告列表，直至下一页无数据
	 * 
	 * @param allList 广告总列表
	 * @param userKey 用户信息
	 * @param url     地址
	 */
	public void travelseAds(List<JSONObject> allList, String cookie, String url, String csrfToken) {
		String returnStr = HttpUtils.sendGet(url, cookie, csrfToken);
//		System.out.println(returnStr);
		JSONObject json;
		try {
			json = JSONObject.parseObject(returnStr);
			String nextPage = !EmptyUtil.isEmpty(json.get("next")) ? json.getString("next") : "";
			JSONArray array = json.containsKey("results") ? json.getJSONArray("results") : new JSONArray();
			for (Object object : array) {
				JSONObject obj = (JSONObject) object;
				allList.add(obj);
			}
			if (!EmptyUtil.isEmpty(nextPage)) {
				travelseAds(allList, cookie, nextPage, "");
//			} else {
//				System.out.println("total:" + json.get("count"));
			}
		} catch (Exception e) {
			logger.error("用户("+cookie+")获取广告列表失败",e);
		}
	}
	
	/**
	 * 改变广告投放状态
	 * 
	 * @param key      用户信息
	 * @param adNumber 广告id
	 * @param status   状态
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String changeAdStatus(String cookie,String csrfToken, String adNumber, String status) {
		// 设置cookie和post上面的map数据
		Response login;
		try {
			Map<String, String> data = new HashMap<String, String>();
			data.put("number_td", adNumber);
			data.put("status", status);
			Connection con2 = Jsoup.connect(CommConstants.CHANGE_AD_STATUS_URL);
			con2.header(HttpUtils.USER_AGENT, HttpUtils.USER_AGENT_VALUE);
			con2.header("Cookie", cookie);
			con2.header("X-CSRFToken", csrfToken);
			login = con2.ignoreContentType(true).followRedirects(true).method(Method.POST).data(data).execute();
			return login.body();
		} catch (IOException e) {
			logger.error("用户("+cookie+")改变广告"+ adNumber +"状态("+ status +")失败",e);
		}
		return "";
	}
	/**
	 * 改变广告价格
	 * 
	 * @param key      用户信息
	 * @param adNumber 广告id
	 * @param status   状态
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String changeAdPrice(String cookie,String csrfToken, String adNumber, Map<String,String> data) {
		// 设置cookie和post上面的map数据
		try {
			String updatePriceUrl = CommConstants.CHANGE_AD_PRICE_URL + adNumber +"/";
			Connection con = Jsoup.connect(updatePriceUrl); // 获取connection
			con.header(HttpUtils.USER_AGENT, HttpUtils.USER_AGENT_VALUE); // 配置模拟浏览器
			con.header("Cookie", cookie); // 配置模拟浏览器
			con.header("X-CSRFToken", csrfToken); // 配置模拟浏览器
			con.ignoreContentType(true).followRedirects(true).method(Method.PUT); // 获取响应
			con.data(data);
			Response rs = con.execute();
			return rs.body();
		} catch (IOException e) {
			logger.error("用户("+cookie+")改变广告"+ adNumber +"价格("+ data +")失败",e);
		}
		return "";
	}
}
