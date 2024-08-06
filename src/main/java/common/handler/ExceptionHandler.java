package common.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 예외처리 Util
 */
public class ExceptionHandler {
	private static Logger logger = Logger.getLogger(ExceptionHandler.class);
	
    /** 에러 메시지 생성 및 로그 기록
     * 
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param e Exception 유형
     */
    public static void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
    	logger.error(getErrorMessage(e), e);
    	processException(request, response, "처리에 실패했습니다.");
        
    }
    
    /** 예외 유형에 따른 에러 메시지 반환
     * 
     * <p>
     * ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException | NullPointerException | SQLException 외에는 default 메시지 반환 
     * </p>
     * 
     * @param e Exception 유형
     * @return 에러 메시지
     */
    private static String getErrorMessage(Exception e) {
    	 if (e instanceof ClassNotFoundException) {
             return "서비스 클래스를 찾을 수 없음: " + e.getMessage();
         } else if (e instanceof NoSuchMethodException) {
             return "메서드를 찾을 수 없음: " + e.getMessage();
         } else if (e instanceof InstantiationException) {
             return "인스턴스화 실패: " + e.getMessage();
         } else if (e instanceof IllegalAccessException) {
             return "메서드 접근 실패: " + e.getMessage();
         } else if (e instanceof InvocationTargetException) {
             return "메서드 호출 실패: " + e.getMessage();
         } else if (e instanceof IllegalArgumentException) {
             return "잘못된 파라미터: " + e.getMessage();
         } else if (e instanceof NullPointerException) {
             return "파라미터 누락: " + e.getMessage();
         } else if (e instanceof NumberFormatException) {
             return "숫자 형식 오류 발생 : " + e.getMessage();
         } else if(e instanceof NoSuchFieldException) {
        	 return "필드를 찾을 수 없음 : " + e.getMessage();
         } else if (e instanceof SQLException) {
             return "데이터베이스 오류 발생: " + e.getMessage();
         } else {
             return "기타 오류로 인한 처리 실패: " + e.getMessage();
         }
    }
    
    /** 예외 발생 시 error 페이지 이동 
     * 
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param errorMessage 에러 메시지
     */
    private static void processException(HttpServletRequest request, HttpServletResponse response, String errorMessage) {
		try {
			request.getSession().setAttribute("alertMsg", errorMessage);
			request.getRequestDispatcher("/views/errorPage.jsp").forward(request, response);
		} catch (IOException | ServletException ex) {
			logger.error("오류 페이지 이동 실패: " + ex.getMessage(), ex);
		}
	}
}
