package com.jw.common.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
	
	/** resultMap 생성
	 * 
	 * @param obj : key-value 
	 * @return resultMap
	 */
	public static Map<String, Object> createResultMap(Object... obj) {
		Map<String, Object> resultMap = new HashMap<>();
		for (int i = 0; i < obj.length; i += 2) {
			resultMap.put((String) obj[i], obj[i + 1]);
		}
		return resultMap;
	}
}
