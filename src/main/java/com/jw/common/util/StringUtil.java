package com.jw.common.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Parameter 유효성 검사 및 URL 관련 유틸리티
 */
public class StringUtil {
	 // URL 매핑 정보를 담은 맵
    private static Map<String, Map<String, String>> urlMappings;

    static {
        // JSON 파일 로드 및 URL 매핑 정보 설정
        ConfigUtil configUtil = ConfigUtil.getInstance();
        configUtil.loadJsonFile();
        urlMappings = configUtil.getUrlMappings();
    }

    /**
     * 파라미터가 null이거나 빈 문자열인지 검증
     *
     * @param request   HttpServletRequest 객체
     * @param paramName 파라미터명
     * @return 파라미터가 null이거나 빈 문자열이면 true, 그렇지 않으면 false
     */
    public static boolean isNullOrEmpty(HttpServletRequest request, String paramName) {
        String paramValue = request.getParameter(paramName);
        return paramName == null || paramValue.trim().isEmpty();
    }

    /**
     * 파라미터가 숫자인지 검증
     *
     * @param request   HttpServletRequest 객체
     * @param paramName 파라미터명
     * @return 파라미터가 숫자 형식이면 true, 그렇지 않으면 false
     */
    public static boolean isNumber(HttpServletRequest request, String paramName) {
        String paramValue = request.getParameter(paramName);
        return paramValue != null && paramValue.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * URL을 서비스 메소드명으로 변환
     *
     * @param action URL 액션 문자열
     * @return 메소드명
     */
    public static String getMethodNameFromAction(String action) {
        String[] parts = action.split("/");
        if (parts.length < 3) {
            return null;
        }

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
        if (str == null || str.isEmpty()) {
            return str;
        }
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
        return type != null ? type.get("type") : null;
    }

    /**
     * 뷰명 반환
     *
     * @param url URL 문자열
     * @return 뷰 이름
     */
    public static String getViewName(String url) {
        Map<String, String> view = urlMappings.get(url);
        return view != null ? view.get("viewName") : null;
    }

    /**
     * URL을 서비스 클래스명으로 변환
     *
     * @param action URL 액션 문자열
     * @return 서비스 클래스 경로
     */
    public static String getServiceClassNameFromAction(String action) {
        String[] parts = action.split("/");
        if (parts.length < 2) {
            return null;
        }
        return "com.jw." + parts[1] + ".model.service." + convertUrl(parts[1]) + "Service";
    }
}