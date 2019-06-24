package com.jayce.comm.util;

import java.util.Collection;
import java.util.Map;

public class EmptyUtil {

	/**
	 * 鍒ゆ柇瀛楃涓叉槸鍚︿负绌猴紝闀垮害涓�琚涓烘槸绌哄瓧绗︿覆.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 鍒ゆ柇鍒楄〃鏄惁涓虹┖锛屽垪琛ㄦ病鏈夊厓绱犱篃琚涓烘槸绌�
	 * 
	 * 
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.size() == 0;
	}

	/**
	 * 鍒ゆ柇Map鏄惁涓虹┖
	 * 
	 * @param     <V>
	 * @param     <K>
	 * 
	 * 
	 * @param map
	 * @return
	 */
	public static <K, V> boolean isEmpty(Map<K, V> map) {
		return map == null || map.size() == 0 || map.isEmpty();
	}

	/**
	 * 鍒ゆ柇鏁扮粍鏄惁涓虹┖
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 鍒ゆ柇瀵硅薄鏄惁涓虹┖
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else {
			return isEmpty(obj.toString());
		}
	}
}
