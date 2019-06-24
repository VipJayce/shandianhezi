package com.jayce.comm.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class IoUtil {

	public static void close(Closeable closeable){
		if(closeable!=null){
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * 
	 * @param in
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String getStringFromInputSream(InputStream in,String encoding) throws IOException{
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,encoding));
		
		StringBuffer buff = new StringBuffer();
		
		String line = null;
		int i = 0;
		while((line = reader.readLine())!=null){
			if(i>0) buff.append("\n");
			buff.append(line);
			i++;
		}
		
		return buff.toString();
		
	}
	
	/**
	 * 
	 * @param path
	 * @param in
	 * @return
	 */
	public static File writeFileForInputStream(String path, InputStream in) {
		return writeFileForInputStream(path,in,false);
	}
	
	/**
	 * 
	 * @param path
	 * @param in
	 * @param append
	 * @return
	 */
	public static File writeFileForInputStream(String path, InputStream in,boolean append) {
		
		File file = null;
		
		try {
			
			file = new File(path);
			
			writeFileForInputStream(file,in,append);
		} catch (RuntimeException e) {
			delFile(path);
			throw e;
		}
		return file;
	}
	
	/**
	 * 
	 * @param file
	 * @param in
	 * @return
	 */
	public static File writeFileForInputStream(File file, InputStream in) {
		return writeFileForInputStream(file,in,false);
	}
	
	/**
	 * 
	 * @param file
	 * @param in
	 * @param append
	 * @return
	 */
	public static File writeFileForInputStream(File file, InputStream in,boolean append) {
		
		OutputStream out = null;
		try {
			
			if(!file.exists()){
				file.createNewFile();
			}
			
			out = new FileOutputStream(file);
			
			byte[] b = new byte[1024];//1024瀛楄妭
			
			int len = 0;
			
			while((len=in.read(b))!=-1){
				out.write(b, 0, len);
			}
			
		} catch (IOException e) {
			throw new RuntimeException("Write file failed.",e);
		} finally {
			closeOutputStream(out);
		}
		return file;
	}
	
	/**
	 * Close InputStream if it is not null.
	 * 
	 * @param in
	 */
	public static void closeInputStream(InputStream in){
		if(in != null){
			try {
				in.close();
			} catch (IOException e) {
				throw new RuntimeException("Close InputStream failed.",e);
			}
		}
	}
	
	/**
	 * Close OutputStream if it is not null.
	 * 
	 * @param in
	 */
	public static void closeOutputStream(OutputStream in){
		if(in != null){
			try {
				in.close();
			} catch (IOException e) {
				throw new RuntimeException("Close InputStream failed.",e);
			}
		}
	}

	/**
	 * 浠庢湇鍔″櫒涓� 鍒犻櫎鏂囦欢
	 * 
	 * @param fileName
	 *            鏂囦欢鍚�
	 * @return true: 浠庢湇鍔″櫒涓婂垹闄ゆ垚鍔� false:鍚﹀垯澶辫触
	 */
	public static boolean delFile(String fileName) {
		File file = new File(fileName);
		return deleteFile(file);
	}
	
	/**
	 * 鍒犻櫎鏂囦欢 <br>
	 * 
	 * Delete File if it exist. <br>
	 * 
	 * It will return true if it delete file successfully 
	 * and return false if it delete failed or the file is null or the file is not exist.
	 * 
	 * @param file
	 * @return 
	 */
	public static boolean deleteFile(File file) {
		
		if(file == null) return false;
		
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}
	
}
