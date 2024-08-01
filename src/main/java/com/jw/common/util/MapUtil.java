package com.jw.common.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
	
	/** resultMap 생성 
	 * 
	 * @param obj key-value 형태의 객체
	 * @return resultMap 처리 결과가 담긴 map 
	 */
	public static Map<String, Object> createResultMap(Object... obj) {
		for(Object object : obj) {
			System.out.println(object);
		}
		Map<String, Object> resultMap = new HashMap<>();
		for (int i = 0; i < obj.length; i += 2) {
			resultMap.put((String) obj[i], obj[i + 1]);
		}
		
		return resultMap;
	}
}
