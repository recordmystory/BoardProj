package com.jw.common.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 예외처리 Util
 */
public class ExceptionHandler {
	private static final Logger logger = Logger.getLogger(ExceptionHandler.class);
	
    /** 에러 메시지 생성 및 로그 기록
     * @param request
     * @param response
     * @param e
     */
    public static void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
    	logger.error(getErrorMessage(e), e);
    	processException(request, response, "처리에 실패했습니다.");
        
    }
    
    /** 예외 유형에 따른 메시지 반환
     * 
     * @param e
     * @return 에러 메시지
     */
    private static String getErrorMessage(Exception e) {
    	 if (e instanceof ClassNotFoundException) {
             return "서비스 클래스를 찾을 수 없습니다: " + e.getMessage();
         } else if (e instanceof NoSuchMethodException) {
             return "메서드를 찾을 수 없습니다: " + e.getMessage();
         } else if (e instanceof InstantiationException) {
             return "인스턴스화 실패: " + e.getMessage();
         } else if (e instanceof IllegalAccessException) {
             return "메서드 접근 실패: " + e.getMessage();
         } else if (e instanceof InvocationTargetException) {
             return "메서드 호출 실패: " + e.getMessage();
         } else if (e instanceof IllegalArgumentException) {
             return "잘못된 인수: " + e.getMessage();
         } else {
             return "처리 실패: " + e.getMessage();
         }
    }
    
    /** 예외 발생 시 error 페이지 이동 
     * 
     * @param request
     * @param response
     * @param errorMessage 
     */
    private static void processException(HttpServletRequest request, HttpServletResponse response, String errorMessage) {
        try {
            if (!response.isCommitted()) { // 응답이 커밋되지 않았을 때 오류페이지로 포워딩
                request.setAttribute("alertMsg", errorMessage);
                request.getRequestDispatcher("/views/board/errorPage.jsp").forward(request, response);
            }
        } catch (IOException | ServletException ex) {
            logger.error("오류 페이지 이동 실패: " + ex.getMessage(), ex);
        }
    }
}
