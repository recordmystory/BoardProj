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
		String[] parts = action.split("/");
		if (parts.length < 3) {
			return null;
		}

		String module = parts[1]; // "board" or "reply"
		String function = parts[2].split("\\.")[0]; // "list", "insert", etc.

		return function + capitalize(module); // "listBoard", "insertReply", etc.
	}
	
	/** 첫글자 대문자로 변환
	 * 
	 * @param str 변환할 문자열
	 * @return str.substring(0, 1).toUpperCase() + str.substring(1) : 대문자로 변환한 문자열
	 */
	public static String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	
}
