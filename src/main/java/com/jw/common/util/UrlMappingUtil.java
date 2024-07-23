package com.jw.common.util;

import java.util.Map;

/**
 * URL 매핑 관련 Util
 */
public class UrlMappingUtil {
	
	private static Map<String, Map<String, String>> urlMappings;

    static {
    	// JSON 파일 로드 및 URL 매핑 정보 설정
        ConfigUtil configUtil = ConfigUtil.getInstance();
        configUtil.loadJsonFile();
        urlMappings = configUtil.getUrlMappings();
    }
	
	/** url을 서비스 메소드명으로 변환
	 * @param action
	 * @return function + capitalize(module) : 메소드명
	 */
	public static String getMethodNameFromAction(String action) {
		// /board/list.bo -> listBoard
		String[] parts = action.split("/"); // '/' 기준으로 분리
		if (parts.length < 4) { // 배열의 길이가 4보다 작으면 null 반환
			return null;
		}

		String category = parts[2]; // 'board' || 'reply'
		String extractStr = parts[3].split("\\.")[0]; // .bo 제거 및 list, detail과 같은 문자열 추출

		return extractStr + convertUrl(category); // ex) listBoard, detailBoard return
	}
	
	/** 첫글자 대문자로 변환
	 * 
	 * @param str 변환할 문자열
	 * @return str.substring(0, 1).toUpperCase() + str.substring(1) : 대문자로 변환한 문자열
	 */
	public static String convertUrl(String str) {
		if (str == null || str.isEmpty()) { // 문자열이 null 혹은 비어있을 경우 원래 문자열 반환
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1); // 첫번째 글자 대문자로 변환 후 나머지 문자열과 더해 return
	}
	
	/** URL 타입 반환
	 * 
	 * @param url
	 * @return URL 타입 ( forward || redirect || ajax )
	 */
	public static String getUrlType(String url) {
        Map<String, String> mappingInfo = urlMappings.get(url);
        return mappingInfo != null ? mappingInfo.get("type") : null;
    }

    /** 뷰명 반환
     * 
     * @param url
     * @return 뷰 이름 ( /board/list, etc, ... )
     */
    public static String getViewName(String url) {
        Map<String, String> mappingInfo = urlMappings.get(url);
        return mappingInfo != null ? mappingInfo.get("viewName") : null;
    }

	/** url을 서비스 클래스명으로 변환
	 * 
	 * @param action
	 * @return
	 */
	public static String getServiceClassNameFromAction(String action) {
		String[] parts = action.split("/");
		if(parts.length < 3) {
			return null;
		}
		
//		String convertServiceName = convertUrl(parts[2]);
		return "com.jw." + parts[2] + ".model.service." + convertUrl(parts[2]) + "Service";
	}
	
	
}
