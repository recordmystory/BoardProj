package com.jw.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * JSP에서 받은 Parameter 값 검증 Util
 */
public class ParameterUtil {
	
	/** 파라미터가 null이거나 빈 문자열인지 검증
	 * 
	 * @param request
	 * @param paramName
	 * @return 파라미터가 null이거나 빈 문자열이면 true, 그렇지 않으면 false
	 */
	public static boolean isNullOrEmpty(HttpServletRequest request, String paramName) {
		String paramValue = request.getParameter(paramName);
		return paramName == null || paramValue.trim().isEmpty();
	}
	
	/** 파라미터 숫자인지 검증
	 * 
	 * @param request
	 * @param paramName 파라미터명
	 * @return 파라미터가 숫자 형식이면 true, 그렇지 않으면 false
	 */
	public static boolean isNumber(HttpServletRequest request, String paramName) {
        String paramValue = request.getParameter(paramName);
        
        // null이고 숫자가 아니라면 false return
        return paramValue != null && paramValue.matches("-?\\d+(\\.\\d+)?");
	}

}
