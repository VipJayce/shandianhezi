package com.jayce.comm.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class FileUtil {

	public static final String CLASSPATH = "classpath://";
	public static final int CLASSPATH_LENGTH = CLASSPATH.length();
	public static final String ROOT_PATH = "file://";
	public static final int ROOT_PATH_LENGTH = ROOT_PATH.length();

	/**
	 * <ul>
	 * <li>If fileName is null or fileName is empty String,return ${fileName}.</li>
	 * <li>If fileName like 'classpath://${fileName}', then get file from
	 * classpath , return file name '${classpath}/${fileName}'.</li>
	 * <li>If fileName like 'file://${fileName}' , then get file from root path
	 * , return file name '/${fileName}'.</li>
	 * <li>Else return ${fileName}.</li>
	 * </ul>
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileName(String fileName) {

		if (fileName == null)
			return fileName;

		if (fileName.startsWith(CLASSPATH)) {
			String name = fileName.substring(CLASSPATH_LENGTH);
			URL url = Thread.currentThread().getContextClassLoader()
					.getResource(name);
			return url.getFile();
		}

		if (fileName.startsWith(ROOT_PATH)) {
			String name = fileName.substring(ROOT_PATH_LENGTH - 1);
			return name;
		}

		return fileName;
	}

	/**
	 * <ul>
	 * <li>If fileName is null or fileName is empty String,return null.</li>
	 * <li>If fileName like 'classpath://${fileName}', then get stream from
	 * classpath , return '${classpath}/${fileName}'.</li>
	 * <li>If fileName like 'file://${fileName}' , then get stream from root
	 * path , return '/${fileName}'.</li>
	 * <li>Else return stream from file ${fileName}.</li>
	 * </ul>
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getResourceAsStream(String fileName)
			throws FileNotFoundException {

		if (fileName == null || fileName.length() == 0)
			return null;

		if (fileName.startsWith(CLASSPATH)) {
			String name = fileName.substring(CLASSPATH_LENGTH);
			return FileUtil.class.getClassLoader().getResourceAsStream(name);
		}

		if (fileName.startsWith(ROOT_PATH)) {
			String name = fileName.substring(ROOT_PATH_LENGTH - 1);
			File file = new File(name);
			return new FileInputStream(file);
		}

		File file = new File(fileName);
		return new FileInputStream(file);
	}

	/**
	 * 鎸夐『搴忓姞杞�
	 * 
	 * @Title: loadProperties
	 * @Description: 鍔犺浇properties鏂囦欢
	 * @param fileName
	 *            鏂囦欢鍚嶏紝鏀寔涓ょ鏂瑰紡锛屽垎鍒互file://鍜宑lasspath://寮�澶� <br/>
	 *            file://琛ㄧず缁濆璺緞锛屼緥濡俧ile://etc/config.propertites <br/>
	 *            classpath://琛ㄧず鐩稿浜巆lasspath涓嬬殑鏂囦欢锛屼緥濡俢lasspath://config.properties <br/>
	 * @param 璁惧畾鏂囦欢
	 * @return List<KeyValue> 杩斿洖绫诲瀷
	 * @throws IOException
	 */
	public static List<KeyValue> loadPropertiesToKeyValue(String fileName,
			String charset) throws IOException {

		List<KeyValue> list = new ArrayList<KeyValue>();

		InputStream in = getResourceAsStream(fileName);

		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				charset));

		String line = "";

		while ((line = reader.readLine()) != null) {
			if (!line.contains("="))
				continue;
			String key = line.substring(0, line.indexOf("="));
			String value = line.substring(line.indexOf("=") + 1);
			KeyValue keyValue = new KeyValue(key, value);
			list.add(keyValue);
		}

		return list;
	}

	/**
	 * 
	 * @Title: loadProperties
	 * @Description: 鍔犺浇properties鏂囦欢
	 * @param fileName
	 *            鏂囦欢鍚嶏紝鏀寔涓ょ鏂瑰紡锛屽垎鍒互file://鍜宑lasspath://寮�澶� <br/>
	 *            file://琛ㄧず缁濆璺緞锛屼緥濡俧ile://etc/config.propertites <br/>
	 *            classpath://琛ㄧず鐩稿浜巆lasspath涓嬬殑鏂囦欢锛屼緥濡俢lasspath://config.properties <br/>
	 * @param 璁惧畾鏂囦欢
	 * @return Properties 杩斿洖绫诲瀷
	 * @throws IOException
	 */
	public static Properties loadProperties(String fileName) throws IOException {

		Properties prop = new Properties();

		InputStream in = getResourceAsStream(fileName);

		prop.load(in);

		return prop;
	}

	/**
	 * 鍐欏叆鏂囦欢
	 * 
	 * @param path
	 * @param list
	 * @return
	 */
	public static boolean writeFile(String path, List<String> list) {
		boolean flag = false;
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new java.io.FileWriter(new File(path)));
			for (String s : list) {
				writer.write(s);
				writer.write("\r\n");
				// writer.newLine();
			}
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 璇诲彇鏂囦欢
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static List<String> reader(String path, String encoding)
			throws Exception {
		File file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			throw new FileNotFoundException();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), encoding));

		String temp = br.readLine();
		List<String> list = new ArrayList<String>();
		while (temp != null) {
			list.add(temp);
			temp = br.readLine();
		}
		if (br != null) {
			br.close();
		}
		return list;
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
	
	/**
	 * 
	 * 
	 * @param dirName
	 */
	public static void createDir(String dirName){
		
		File dir = new File(dirName);
		
		createDir(dir);
		
	}
	
	/**
	 * 
	 * 
	 * @param dir
	 */
	public static void createDir(File dir){
		
		File parentDir = dir.getParentFile();
		
		if(parentDir!=null){
			createDir(parentDir);
		}
		
		if(dir.exists()) return;
		
		dir.mkdir();
	}
	
}
