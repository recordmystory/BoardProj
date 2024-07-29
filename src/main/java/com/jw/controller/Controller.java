package com.jw.controller;

import static com.jw.common.template.JDBCTemplate.close;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.jw.common.handler.ExceptionHandler;
import com.jw.common.util.StringUtil;

/**
 * .bo로 끝나는 모든 요청을 받는 Controller
 */
@WebServlet("*.bo")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextPath = request.getContextPath();
		String action = request.getRequestURI().substring(contextPath.length());

		String urlType = StringUtil.getUrlType(action); // (forward, redirect, ajax)
		String viewName = StringUtil.getViewName(action); // viewName

		String methodName = StringUtil.getMethodNameFromAction(action);

		try {
			String serviceClassName = StringUtil.getServiceName(action);
			Class<?> serviceClass = Class.forName(serviceClassName);
			Object serviceInstance = serviceClass.getDeclaredConstructor().newInstance();
			Method method = serviceClass.getMethod(methodName, Map.class);

			// 파라미터 맵 생성
			Map<String, String> paramMap = new HashMap<>();
			Enumeration<String> paramsNames = request.getParameterNames();

			while (paramsNames.hasMoreElements()) {
				String name = paramsNames.nextElement();
				String value = request.getParameter(name);

				// 문자 유효성 검사
				if (value == null || value.trim().isEmpty())
					throw new IllegalArgumentException("유효하지 않은 파라미터: " + name);

				paramMap.put(name, value);
			}

			// 서비스 메소드 호출
			Map<String, Object> result = (Map<String, Object>) method.invoke(serviceInstance, paramMap);

			if ("forward".equals(urlType)) {
				for (Map.Entry<String, Object> entry : result.entrySet()) {
					request.setAttribute(entry.getKey().toString(), entry.getValue());
				}
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
