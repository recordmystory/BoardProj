package com.jw.controller;

import static com.jw.common.template.JDBCTemplate.close;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.jw.common.handler.ExceptionHandler;
import com.jw.common.util.StringUtil;

/**
 * @Description .bo로 끝나는 모든 요청을 받는 Controller
 */
@WebServlet("*.bo")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 서블릿 요청이 있을 때 호출
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextPath = request.getContextPath();
		String action = request.getRequestURI().substring(contextPath.length());
		
//		String[] actionArr = action.split("/");
//		String convertActionArr = Character.toUpperCase(actionArr[actionArr.length - 2].charAt(0)) + actionArr[actionArr.length - 2].substring(1) + "Service";
//		System.out.println("action split" + convertActionArr);
		
		// URL 타입과 뷰 이름 가져오기
		String urlType = StringUtil.getUrlType(action); // (forward, redirect, ajax)
		String viewName = StringUtil.getViewName(action); // viewName
		
		// 요청 URL값을 통해 서비스 메소드 이름 추출
		String methodName = StringUtil.getMethodNameFromAction(action);
		
		// methodName null 혹은 비어있는지 확인
		if (methodName == null || methodName.isEmpty())  {
			ExceptionHandler.handleException(request, response, new NullPointerException("methodName이 null이거나 empty"));
			return;
		}
				
		try {
			String serviceClassName = StringUtil.getServiceName(action);
			Class<?> serviceClass = Class.forName(serviceClassName); // 서비스 클래스 로드
			Object serviceInstance = serviceClass.getDeclaredConstructor().newInstance();
			Method method = serviceClass.getMethod(methodName, Map.class); // 서비스 클래스에서 호출할 메서드를 가져옴
			
			Map<String, String> paramMap = StringUtil.setMapParameter(request); // 파라미터 맵 생성
			
			Map<String, Object> result = (Map<String, Object>) method.invoke(serviceInstance, paramMap); // 서비스 메소드 호출
			
			// URL에 따라 요청 처리
			if ("forward".equals(urlType)) {
				result.entrySet().forEach( entry -> { request.setAttribute(entry.getKey().toString(), entry.getValue()); } );
				request.getRequestDispatcher("/views" + viewName).forward(request, response);
			} else if ("redirect".equals(urlType)) {
				response.sendRedirect(contextPath + viewName);
			} else if ("ajax".equals(urlType)) {
				response.setContentType("application/json; charset=UTF-8");
				response.getWriter().write(new Gson().toJson(result));
				close(response.getWriter()); // writer close
			} else {
				request.getRequestDispatcher("/views/errorPage.jsp").forward(request, response);
			}

		} catch (Exception e) {
			ExceptionHandler.handleException(request, response, e); // 예외처리 util 호출
		}
	}
}
