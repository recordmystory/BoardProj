package com.jw.common.util;

/**
 * 
 */
public class UrlMappingUtil {
	
	/** url을 서비스 메소드명으로 변환
	 * @param action
	 * @return function + capitalize(module) : 메소드명
	 */
	public static String getMethodNameFromAction(String action) {
		// /board/list.bo -> listBoard
		String[] parts = action.split("/"); // '/' 기준으로 분리
		if (parts.length < 3) { // 배열의 길이가 3보다 작으면 null 반환
			return null;
		}

		String module = parts[1]; // 'board' || 'reply'
		String function = parts[2].split("\\.")[0]; // .bo 제거 및 list, detail과 같은 문자열 추출

		return function + convertUrl(module); // ex) listBoard, detailBoard return
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
	
	
}
