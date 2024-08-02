package com.jw.common.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Parameter 유효성 검사 및 URL 관련 유틸리티
 */
public class StringUtil {
	private static Map<String, Map<String, String>> urlMappings;

	static { // JSON 파일 로드 및 URL 매핑 정보 설정
		Configuration configUtil = Configuration.getInstance();
		configUtil.loadJsonFile();
		urlMappings = configUtil.getUrlMappings();
	}

	/**
	 * URL을 서비스 메소드명으로 변환
	 *
	 * @param url URL 액션 문자열
	 * @return 메소드명
	 */
	public static String getMethodNameFromAction(String url) {
		String[] parts = url.split("/");
		if (parts.length < 3) return "";

		String category = parts[1];
		String pageName = parts[2].split("\\.")[0];

		return pageName + convertUrl(category);
	}

	/**
	 * 문자열의 첫 글자를 대문자로 변환
	 *
	 * @param str 변환할 문자열
	 * @return 변환된 문자열
	 */
	public static String convertUrl(String str) {
		if (str == null || str.isEmpty()) return str;
		
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * URL 타입 반환
	 *
	 * @param url URL 문자열
	 * @return URL 타입 (forward, redirect, ajax)
	 */
	public static String getUrlType(String url) {
		Map<String, String> type = urlMappings.get(url);
		return type != null ? type.get("type") : "";
	}

	/**
	 * 뷰명 반환
	 *
	 * @param url URL 문자열
	 * @return 뷰명
	 */
	public static String getViewName(String url) {
		Map<String, String> view = urlMappings.get(url);
		return view != null ? view.get("viewName") : "";
	}
	
	/** 서비스 클래스명 반환
	 * 
	 * @param url URL 문자열
	 * @return 서비스명
	 */
	public static String getServiceName(String url) {
		Map<String, String> service = urlMappings.get(url);
		return service != null ? service.get("serviceName") : "";
	}
	
	/** 파라미터명과 파라미터 값을 유효성 검사 진행 후 map에 담아줌
	 * <p>파라미터가 숫자 및 문자가 아닐 경우엔 Excetion 발생 및 에러 페이지로 포워딩됨</p>
	 * 
	 * @param request HTTP 요청
	 * @return paramMap 파라미터가 담긴 map
	 */
	public static Map<String, String> setMapParameter(HttpServletRequest request){
		Map<String, String> paramMap = new HashMap<>();
		Enumeration<String> paramsNames = request.getParameterNames();

		while (paramsNames.hasMoreElements()) {
			String name = paramsNames.nextElement();
			String value = request.getParameter(name);

//			if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("유효하지 않은 문자 : " + name); // 문자 유효성 검사
//			if (!value.matches("\\d+")) throw new IllegalArgumentException("유효하지 않은 숫자 : " + name); // 숫자 유효성 검사
			
			paramMap.put(name, value); // map에는 파라미터 name과 그에 해당하는 값이 담겨있음
		}
		
		return paramMap;
	}
	
}
