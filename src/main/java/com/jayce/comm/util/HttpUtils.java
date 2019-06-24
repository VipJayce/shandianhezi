package com.jayce.comm.util;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

/**
 * 
 * @author mywill
 *
 */
public class HttpUtils {

	private static final String DEFAULT_ENCODING = "UTF-8";
	public static String USER_AGENT = "User-Agent";
	public static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.108 Safari/537.36";

	/**
	 * 发送get请求
	 * @param url 地址
	 * @param userKey 用户信息
	 * @return
	 */
	public static String sendGet(String url, String cookie) {
		return sendGet(url, null, cookie, "");
	}
	
	/**
	 * 发送get请求
	 * @param url 地址
	 * @param userKey 用户信息
	 * @param CSRFToken 是否需要CSRFToken
	 * @return
	 */
	public static String sendGet(String url, String cookie,String csrfToken) {
		return sendGet(url, null, cookie, csrfToken);
	}
	
	/**
	 * 发送get请求
	 * @param url 地址
	 * @param map 传递参数
	 * @param userKey 用户信息
	 * @return
	 */
	public static String sendGet(String url, Map<String, String> map, String cookie) {
		return sendGet(url, map, cookie, "");
	}
	
	/**
	 * 发送get请求
	 * @param url 地址
	 * @param map 传递参数
	 * @param userKey 用户信息
	 * @param CSRFToken 是否需要CSRFToken
	 * @return
	 */
	public static String sendGet(String url, Map<String, String> map, String cookie,String csrfToken) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet get = null;
		try {
			if (!EmptyUtil.isEmpty(map)) {
				StringBuffer buff = new StringBuffer();
				for (String key : map.keySet()) {
					String value = map.get(key);
					buff.append("&").append(key).append("=").append(URLEncoder.encode(value, "UTF-8"));
				}
				if (buff.length() != 0) {
					buff.deleteCharAt(0);
				}
				String params = buff.toString();
				url = url.contains("?") ? (url + params) : (url + "?" + params);
			}
			get = new HttpGet(url);
			get.setHeader(USER_AGENT, USER_AGENT_VALUE);
			get.setHeader("Cookie", cookie);
			if(!EmptyUtil.isEmpty(csrfToken)) {
				get.setHeader("X-CSRFToken", csrfToken);
			}
			HttpResponse response = httpClient.execute(get);
			InputStream in = response.getEntity().getContent();
			String result = IoUtil.getStringFromInputSream(in, "UTF-8");
			in.close();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (get != null && !get.isAborted()) {
				get.abort();
			}
		}
	}
	
	/**
	 * 发送post请求
	 * @param url 地址
	 * @param userKey 用户信息
	 * @return
	 */
	public static String sendPost(String url, String cookie) throws Exception {
		return sendPost(url, new HashMap<String, String>(), cookie);
	}
	
	/**
	 * 发送post请求
	 * @param url 地址
	 * @param map 传递参数
	 * @param userKey 用户信息
	 * @return
	 */
	public static String sendPost(String url, Map<String, String> map, String cookie) throws Exception {
		return sendPost(url, map, cookie, "");
	}
	
	/**
	 * 发送post请求
	 * @param url 地址
	 * @param userKey 用户信息
	 * @param CSRFToken 是否需要CSRFToken
	 * @return
	 */
	public static String sendPost(String url, String cookie, String csrfToken) throws Exception {
		return sendPost(url, null, cookie, csrfToken);
	}

	/**
	 * 发送post请求
	 * @param url 地址
	 * @param map 传递参数
	 * @param userKey 用户信息
	 * @param CSRFToken 是否需要CSRFToken
	 * @return
	 */
	public static String sendPost(String url, Map<String, String> map, String cookie,String csrfToken)
			throws Exception {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = null;
		String result = null;
		try {
			post = new HttpPost(url);
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			if (!EmptyUtil.isEmpty(map)) {
				for (String key : map.keySet()) {
					String value = map.get(key);
					formParams.add(new BasicNameValuePair(key, value));
				}
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParams, DEFAULT_ENCODING);
			post.setEntity(formEntity);
			post.setHeader("Referer", "http://oms.hqygou.com/order/temp/new");
			post.setHeader(USER_AGENT, USER_AGENT_VALUE);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			post.setHeader("X-Requested-With", "XMLHttpRequest");
			post.setHeader("Cookie", cookie);
			if (!EmptyUtil.isEmpty(csrfToken)) {
				post.setHeader("X-CSRFToken", csrfToken);
			}
			HttpResponse response = httpClient.execute(post);
			InputStream in = response.getEntity().getContent();
			result = IoUtil.getStringFromInputSream(in, "UTF-8");
			in.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (post != null && !post.isAborted()) {
				post.abort();
			}
		}
		return result;
	}
	
	/**
	 * JsoupPOST请求
	 * @param url 地址
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public static Response JsoupPOST(String url,Map<String,String> map,Map<String,String> cookies) throws Exception {
		Connection con = Jsoup.connect(url); // 获取connection
		con.header(HttpUtils.USER_AGENT, HttpUtils.USER_AGENT_VALUE); // 配置模拟浏览器
		con.ignoreContentType(true).followRedirects(true).method(Method.POST); // 获取响应
		if(!EmptyUtil.isEmpty(cookies)) {
			con.cookies(cookies);
		}
		if(!EmptyUtil.isEmpty(map)) {
			con.data(map);
		}
		Response rs = con.execute();
		return rs;
	}
	
	/**
	 * JsoupGET请求
	 * @param url 地址
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public static Response JsoupGET(String url,Map<String,String> map) throws Exception {
		Connection con = Jsoup.connect(url); // 获取connection
		con.header(HttpUtils.USER_AGENT, HttpUtils.USER_AGENT_VALUE); // 配置模拟浏览器
		con.ignoreContentType(true).followRedirects(true).method(Method.GET); // 获取响应
		if(!EmptyUtil.isEmpty(map)) {
			con.data(map);
		}
		Response rs = con.execute();
		return rs;
	}
}
