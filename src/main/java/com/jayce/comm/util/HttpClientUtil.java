package com.jayce.comm.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


/**
 * 
 * @author mywill
 *
 */
public class HttpClientUtil {

	private static final String DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * 鍙戦�乸ost璇锋眰锛岄粯璁ゅ彂閫佸瓧绗︾紪鐮佸拰鎺ュ彈瀛楃缂栫爜UTF-8,
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
//	public static String sendPost(String url,Map<String,String> map) throws ClientProtocolException, IOException{
//		return sendPost(url,map,DEFAULT_ENCODING,DEFAULT_ENCODING);
//	}
	
	/**
	 * 鍙戦�乸ost璇锋眰锛岄粯璁ゅ彂閫佸瓧绗︾紪鐮乁TF-8
	 * 
	 * @param url
	 * @param map
	 * @param encoding
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
//	public static String sendPost(String url,Map<String,String> map,String readEncoding) throws ClientProtocolException, IOException{
//		return sendPost(url,map,readEncoding,DEFAULT_ENCODING);
//	}
	
	/**
	 * 鍙戦�乸ost璇锋眰
	 * 
	 * @param url
	 * @param map
	 * @param sendEncoding
	 * @param readEncoding
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPost(String url,Map<String,String> map,String cookies,String sendEncoding,String readEncoding) throws ClientProtocolException, IOException{
		
		HttpClient httpClient = HttpClients.createDefault();
		
		HttpPost post = null;
		
		String result = null;
		
		try{
			post = new HttpPost(url);
			
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			
			if(map != null)
			for(String key : map.keySet()){
				String value = map.get(key);
				formParams.add(new BasicNameValuePair(key, value));
			}
			
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParams,sendEncoding);
			post.setEntity(formEntity);
			
			
			
			post.setHeader("Referer","http://oms.hqygou.com/order/temp/new");
			post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			post.setHeader("X-Requested-With","XMLHttpRequest");
			post.setHeader("Cookies",cookies);
			
			
			HttpResponse response = httpClient.execute(post);
			
			InputStream in = response.getEntity().getContent();
			
			result = IoUtil.getStringFromInputSream(in, readEncoding);
			
			in.close();
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if(post!=null && !post.isAborted()){
				post.abort();
			}
		}
		
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String upload(String url,Map<String,Object> map) throws ClientProtocolException, IOException{
		return upload(url,map,DEFAULT_ENCODING);
	}
	
	/**
	 * 
	 * @param url
	 * @param map
	 * @param readEncoding
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String upload(String url,Map<String,Object> map,String readEncoding) throws ClientProtocolException, IOException{
		
		return upload(url,map,readEncoding,DEFAULT_ENCODING);
	}
	
	/**
	 * 
	 * @param url
	 * @param map
	 * @param sendEncoding
	 * @param readEncoding
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String upload(String url,Map<String,Object> map,String sendEncoding,String readEncoding) throws ClientProtocolException, IOException{
		
		HttpClient httpClient = HttpClients.createDefault();
		
		HttpPost post = null;
		
		try{
			MultipartEntityBuilder reqEntityBuilder = MultipartEntityBuilder.create();  
			
			String params = "";
			
			if(map != null)
			for(String key : map.keySet()){
				Object value = map.get(key);
				if(value == null) continue;
				
				if(value instanceof File){
					reqEntityBuilder.addPart(key,getContentBody(value,sendEncoding));
				}else{
					params = params.length()>0?params+"&":params;
					params = params + key + "="+value.toString();
				}
			}
			
			url = url.contains("?")?(url + "&" + params): (url + "?" + params);
			
			post = new HttpPost(url);
			
			post.setEntity(reqEntityBuilder.build());
			
			HttpResponse response = httpClient.execute(post);
			
			InputStream in = response.getEntity().getContent();
			
			String result = IoUtil.getStringFromInputSream(in, readEncoding);
			
			in.close();
			
			return result;
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if(post!=null && !post.isAborted()){
				post.abort();
			}
		}
		
		
	}
	
	
	
	/**
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendGet(String url,Map<String,String> map) throws ClientProtocolException, IOException{
		return sendGet(url,map,DEFAULT_ENCODING,DEFAULT_ENCODING,"");
	}
	
	/**
	 * 鍙戦�乸ost璇锋眰锛岄粯璁ゅ彂閫佸瓧绗︾紪鐮乁TF-8
	 * 
	 * @param url
	 * @param map
	 * @param encoding
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendGet(String url,Map<String,String> map,String readEncoding) throws ClientProtocolException, IOException{
		return sendGet(url,map,readEncoding,DEFAULT_ENCODING,"");
	}
	
	/**
	 * 鍙戦�乸ost璇锋眰
	 * 
	 * @param url
	 * @param map
	 * @param sendEncoding
	 * @param readEncoding
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendGet(String url,Map<String,String> map,String cookies,String sendEncoding,String readEncoding) throws ClientProtocolException, IOException{
		
		HttpClient httpClient = HttpClients.createDefault();
		
		HttpGet get = null;
		
		try{
			
			StringBuffer buff = new StringBuffer();
			
			if(map != null)
			for(String key : map.keySet()){
				String value = map.get(key);
				buff.append("&").append(key).append("=").append(URLEncoder.encode(value, sendEncoding));
			}
			if(buff.length()!=0) buff.deleteCharAt(0);
			
			
			String params = buff.toString();
			
			url = url.contains("?") ? (url + params) : (url + "?" + params);
			
			get = new HttpGet(url);
			if(!"".equals(cookies)) {
				get.addHeader("Cookie", cookies);
			}
			HttpResponse response = httpClient.execute(get);
			
			InputStream in = response.getEntity().getContent();
			
			String result = IoUtil.getStringFromInputSream(in, readEncoding);
			
			in.close();
			
			return result;
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if(get!=null && !get.isAborted()){
				get.abort();
			}
		}
		
	}
	
	
	@SuppressWarnings("deprecation")
	private static ContentBody getContentBody(Object object,String charset) throws UnsupportedEncodingException{
		if( object instanceof File){
			return new FileBody((File)object);
		}else{
			return new StringBody(object.toString());
		}
	}
	
	/**
	 * 涓嬭浇鏂囦欢
	 * 
	 * @param url
	 * @param map
	 * @param targetDir
	 * @param sendEncoding
	 * @param readEncoding
	 * @return
	 * 
	 * @throws 濡傛灉response鏄痟tml/text绫诲瀷锛屾姏鍑哄紓甯� HttpClientException锛屼娇鐢╡.getMessage()鍙互鑾峰彇鍝嶅簲鍐呭 
	 */
	public static File downloadFile(String url,Map<String,String> map,File targetDir,String sendEncoding,String readEncoding){
		
		HttpClient httpClient = HttpClients.createDefault();
		
		HttpGet get = null;
		
		try{
			
			FileUtil.createDir(targetDir);
			
			StringBuffer buff = new StringBuffer();
			
			if(map != null)
			for(String key : map.keySet()){
				String value = map.get(key);
				buff.append("&").append(key).append("=").append(URLEncoder.encode(value, sendEncoding));
			}
			if(buff.length()!=0) buff.deleteCharAt(0);
			
			
			String params = buff.toString();
			
			url = url.contains("?") ? (url + params) : (url + "?" + params);
			
			get = new HttpGet(url);
			
			HttpResponse response = httpClient.execute(get);
			
			//println(response.getAllHeaders());
			
			Header[] contentTypes = response.getHeaders("Content-Type");
			
			if(contentTypes!=null&&contentTypes.length>0){
				String value = contentTypes[0].getValue();
				if(value.contains("text/html")){
					InputStream in = response.getEntity().getContent();
					String result = IoUtil.getStringFromInputSream(in, readEncoding);
					throw new Exception(result);
				}
			}
			
			Header[] headers = response.getHeaders("content-disposition");
			
			String fileName = null;
			
			if(headers!=null&&headers.length>0){
				fileName = headers[0].getValue();
				if(fileName.contains("filename=")){
					fileName = fileName.substring(fileName.indexOf("filename=")+9);
				}
			}else{
				fileName = String.valueOf(System.currentTimeMillis());
			}
			
			InputStream in = response.getEntity().getContent();
			
			File outFile = new File(targetDir,fileName);
			
			OutputStream out = new FileOutputStream(outFile);
			
			byte[] b = new byte[1024];//1024瀛楄妭
			
			int len = 0;
			
			while((len=in.read(b))!=-1){
				out.write(b, 0, len);
			}
			
			in.close();
			out.close();
			
			return outFile;
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if(get!=null && !get.isAborted()){
				get.abort();
			}
		}
		
	}
	
	public static void println(Header[] allHeaders) {
		if(allHeaders != null){
			for(Header header: allHeaders){
				System.out.println(header.getName()+":"+header.getValue());
			}
		}
	}

	/**
	 * 
	 * 
	 * @param url
	 * @param map
	 * @param targetDir
	 * @param sendEncoding
	 * @param readEncoding
	 * @return
	 * 
	 * @throws 濡傛灉response鏄痟tml/text绫诲瀷锛屾姏鍑哄紓甯� HttpClientException锛屼娇鐢╡.getMessage()鍙互鑾峰彇鍝嶅簲鍐呭 
	 */
	public static File downloadFile(String url,Map<String,String> map,String targetDir,String sendEncoding,String readEncoding){
		return downloadFile(url,map,new File(targetDir),DEFAULT_ENCODING,DEFAULT_ENCODING);
	}
	
	
	/**
	 * 涓嬭浇鏂囦欢
	 * 
	 * @param url
	 * @param map
	 * @param targetDir
	 * @return
	 * 
	 * @throws 濡傛灉response鏄痟tml/text绫诲瀷锛屾姏鍑哄紓甯� HttpClientException锛屼娇鐢╡.getMessage()鍙互鑾峰彇鍝嶅簲鍐呭 
	 */
	public static File downloadFile(String url,Map<String,String> map,File targetDir){
		return downloadFile(url,map,targetDir,DEFAULT_ENCODING,DEFAULT_ENCODING);
	}
	
	/**
	 * 涓嬭浇鏂囦欢
	 * 
	 * @param url
	 * @param map
	 * @param targetDir
	 * @return
	 * 
	 * @throws 濡傛灉response鏄痟tml/text绫诲瀷锛屾姏鍑哄紓甯� HttpClientException锛屼娇鐢╡.getMessage()鍙互鑾峰彇鍝嶅簲鍐呭 
	 */
	public static File downloadFile(String url,Map<String,String> map,String targetDir){
		return downloadFile(url,map,new File(targetDir));
	}
	
	/**
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public static String sendPostData(String url,String data){
		return sendPostData(url,data,DEFAULT_ENCODING);
	}
	
	/**
	 * 
	 * @param url
	 * @param data
	 * @param charset
	 * @return
	 */
	public static String sendPostData(String url,String data,String charset){
		
		String result = null;
		
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(HTTP.CONTENT_TYPE, "text/html;charset="+charset);
		
		try{
			httpPost.setEntity(new StringEntity(data, charset));
			HttpResponse response = httpClient.execute(httpPost);
			InputStream in = response.getEntity().getContent();
			result = IoUtil.getStringFromInputSream(in, charset);
			
			in.close();
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if(httpPost!=null && !httpPost.isAborted()){
				httpPost.abort();
			}
		}
		return result;
	}
	
}
