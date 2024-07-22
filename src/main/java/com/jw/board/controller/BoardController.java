package com.jw.board.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.jw.board.model.service.BoardService;
import com.jw.common.util.ExceptionHandler;
import com.jw.common.util.ParameterUtil;
import com.jw.common.util.UrlMappingUtil;

/**
 * .bo로 끝나는 모든 요청을 받는 Controller
 */
@WebServlet("*.bo")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BoardController.class);
	
	private BoardService bService = new BoardService();
//	private static UrlMappingUtil urlMapping = new UrlMappingUtil();;
//    private List<String> urlList = new ArrayList<>();
   
    
	/*private void initUrlList() {
	    urlList.add("/board/list.bo");
	    urlList.add("/board/detail.bo");
	    urlList.add("/board/insert.bo");
	    urlList.add("/board/regist.bo");
	    urlList.add("/board/updateForm.bo");
	    urlList.add("/board/update.bo");
	    urlList.add("/board/delete.bo");
	    urlList.add("/board/listSearch.bo");
	    urlList.add("/reply/insert.bo");
	    urlList.add("/reply/list.bo");
	}*/
    
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
//		initUrlList();

	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String action = uri.substring(contextPath.length());

		String urlType = UrlMappingUtil.getUrlType(action);
	    String viewName = UrlMappingUtil.getViewName(action);

		if (urlType != null) {
			
			String methodName = UrlMappingUtil.getMethodNameFromAction(action);
			
			try {
				
				String serviceClassName = UrlMappingUtil.getServiceClassNameFromAction(action);
			    
			    Class<?> serviceClass = Class.forName(serviceClassName);
			    Object serviceInstance = serviceClass.getDeclaredConstructor().newInstance();
			    
//				 Class<?> serviceClass = Class.forName("com.jw.board.model.service.BoardService");

//				Class<?> serviceClass = Class.forName(serviceClassName);
				
//				Object serviceInstance = Class.forName("com.jw.board.model.service.BoardService").getDeclaredConstructor().newInstance();
				// 클래스안에 메서드-> 반환값 
//				Object serviceInstance = Class.forName(UrlMappingUtil.getServiceClassNameFromAction(action)).getDeclaredConstructor().newInstance();
//                Method method = Class.forName(UrlMappingUtil.getServiceClassNameFromAction(action)).getMethod(methodName, Map.class);
			    
				Method method = serviceClass.getMethod(methodName, Map.class);
				
				Map<String, String> paramMap = new HashMap<>();
				Enumeration<String> paramsNames = request.getParameterNames();
				while (paramsNames.hasMoreElements()) {
					String name = paramsNames.nextElement();
					String value = request.getParameter(name);
					
					// 파라미터 검증
					if (ParameterUtil.isNullOrEmpty(request, name)) {
	                    ExceptionHandler.processException(request, response, "파라미터가 유효하지 않습니다: " + name, new IllegalArgumentException("IllegalArgumentException 발생 : " + name));
	                    return;
	                }

	                if (isNumberParameter(name) && !ParameterUtil.isNumber(request, name)) {
	                    ExceptionHandler.processException(request, response, "숫자 파라미터가 유효하지 않습니다: " + name, new IllegalArgumentException("IllegalArgumentException 발생 : " + name));
	                    return;
	                }

					paramMap.put(name, value);
				}
				// 서비스 메서드 호출
				Map<String, Object> result = (Map<String, Object>) method.invoke(serviceInstance, paramMap);

//				logger.info("Service result: " + result);

				if ("forward".equals(urlType)) {
					/* if (result instanceof Map) {
					     Map<?, ?> resultMap = (Map<?, ?>) result;
					     for (Map.Entry<?, ?> entry : resultMap.entrySet()) {
					         request.setAttribute(entry.getKey().toString(), entry.getValue());
					     }
					 }*/
					for (Map.Entry<String, Object> entry : result.entrySet()) {
						request.setAttribute(entry.getKey().toString(), entry.getValue());
					}
					request.getRequestDispatcher("/views" + viewName).forward(request, response);
				} else if ("redirect".equals(urlType)) {
					response.sendRedirect(request.getContextPath() + viewName);
				} else if ("ajax".equals(urlType)) {
					response.setContentType("application/json; charset=UTF-8");
					response.getWriter().write(new Gson().toJson(result));
				}

			} catch (Exception e) {
				// 예외처리 util 호출
				ExceptionHandler.processException(request, response, "처리에 실패했습니다 : " + e.getMessage(), e);
			}
		} else {
			request.getRequestDispatcher("/views/board/errorPage.jsp").forward(request, response);
		}
		
			/*catch (NoSuchMethodException e) {
				handleException(request, response, "해당 메서드를 찾을 수 없습니다: " + e.getMessage(), e);
			} catch (IllegalAccessException e) {
				handleException(request, response, "메서드 접근 실패: " + e.getMessage(), e);
			} catch (InvocationTargetException e) {
				handleException(request, response, "메서드 호출 실패: " + e.getMessage(), e);
			} catch (IOException e) {
				handleException(request, response, "입출력 오류: " + e.getMessage(), e);
			} catch (ServletException e) {
				handleException(request, response, "서블릿 오류: " + e.getMessage(), e);*/
		
	}
	

	private boolean isNumberParameter(String paramName) {
		return "numberParam".equals(paramName);
	}
	
	/*@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String action = uri.substring(contextPath.length());
	
		logger.info("요청 url : " + action);
	
		String urlType = UrlMappingUtil.getUrlType(action);
	    String viewName = UrlMappingUtil.getViewName(action);
	
		if (urlType != null) {
			
			String methodName = UrlMappingUtil.getMethodNameFromAction(action);
			String serviceClassName = UrlMappingUtil.getServiceClassNameFromAction(action);
			
			try {
				
				String serviceClassName = UrlMappingUtil.getServiceClassNameFromAction(action);
			    
			    Class<?> serviceClass = Class.forName(serviceClassName);
			    Object serviceInstance = serviceClass.getDeclaredConstructor().newInstance();
			    
	//				 Class<?> serviceClass = Class.forName("com.jw.board.model.service.BoardService");
	
	//				Class<?> serviceClass = Class.forName(serviceClassName);
				
	//				Object serviceInstance = Class.forName("com.jw.board.model.service.BoardService").getDeclaredConstructor().newInstance();
				// 클래스안에 메서드-> 반환값 
	//				Object serviceInstance = Class.forName(UrlMappingUtil.getServiceClassNameFromAction(action)).getDeclaredConstructor().newInstance();
	//                Method method = Class.forName(UrlMappingUtil.getServiceClassNameFromAction(action)).getMethod(methodName, Map.class);
			    
				Method method = serviceClass.getMethod(methodName, Map.class);
	
				Map<String, String> paramMap = new HashMap<>();
				Enumeration<String> paramsNames = request.getParameterNames();
				while (paramsNames.hasMoreElements()) {
					String name = paramsNames.nextElement();
					String value = request.getParameter(name);
					paramMap.put(name, value);
				}
				// 서비스 메서드 호출
				Map<String, Object> result = (Map<String, Object>) method.invoke(serviceInstance, paramMap);
	
	//				logger.info("Service result: " + result);
	
				if ("forward".equals(urlType)) {
					 if (result instanceof Map) {
					     Map<?, ?> resultMap = (Map<?, ?>) result;
					     for (Map.Entry<?, ?> entry : resultMap.entrySet()) {
					         request.setAttribute(entry.getKey().toString(), entry.getValue());
					     }
					 }
					for (Map.Entry<String, Object> entry : result.entrySet()) {
						request.setAttribute(entry.getKey().toString(), entry.getValue());
					}
					request.getRequestDispatcher("/views" + viewName).forward(request, response);
				} else if ("redirect".equals(urlType)) {
					response.sendRedirect(request.getContextPath() + viewName);
				} else if ("ajax".equals(urlType)) {
					response.setContentType("application/json; charset=UTF-8");
					response.getWriter().write(new Gson().toJson(result));
				}
	
			} catch (NoSuchMethodException e) {
				handleException(request, response, "해당 메서드를 찾을 수 없습니다: " + e.getMessage(), e);
			} catch (IllegalAccessException e) {
				handleException(request, response, "메서드 접근 실패: " + e.getMessage(), e);
			} catch (InvocationTargetException e) {
				handleException(request, response, "메서드 호출 실패: " + e.getMessage(), e);
			} catch (IOException e) {
				handleException(request, response, "입출력 오류: " + e.getMessage(), e);
			} catch (ServletException e) {
				handleException(request, response, "서블릿 오류: " + e.getMessage(), e);
			 catch (Exception e) {
				 // 예외처리 util 호출
				 ExceptionHandler.processException(request, response, "처리에 실패했습니다 : " + e.getMessage(), e);
			}
		} else {
			request.getRequestDispatcher("/views/board/errorPage.jsp").forward(request, response);
		}
	}*/
	
	/*private void handleException(HttpServletRequest request, HttpServletResponse response, String errorMessage,
			Exception e) {
		logger.error(errorMessage, e);
		try {
			if (!response.isCommitted()) {
				request.setAttribute("alertMsg", errorMessage);
				request.getRequestDispatcher("/views/board/errorPage.jsp").forward(request, response);
			}
		} catch (IOException | ServletException ex) {
			logger.error("오류 페이지 이동 실패: " + ex.getMessage(), ex);
		}
	}*/
}
	
	
	
	
	
	
	
	/*  if (urlList.contains(action)) {
			String methodName = UrlMappingUtil.getMethodNameFromAction(action);
		    try {
		        Method method = BoardService.class.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
		        method.invoke(bService, request, response);
		        
		        // 모든 파라미터 쿼리 스트링으로 변환
		        String queryString = "";
		        Enumeration<String> paramsNames = request.getParameterNames();
		        while(paramsNames.hasMoreElements()) {
		        	String name = (String)paramsNames.nextElement();
		        	String value = request.getParameter(name);
		        	queryString += name + "=" + value + "&";
		        	
		        	if (!queryString.isEmpty()) {
		                queryString = queryString.substring(0, queryString.length() - 1);
		            }
		        	
		        logger.info("queryString : " + queryString);
		        }
		        
		        //리턴값도 받을수잇어 
		        
		        //여기서 리퀘스트로 설정 
		        // 해당 viewName 를 리스톤스 
		        // 여기서 리다이렉ㅌ인지 그냥일반포워드인지 json인지 구분해서 분
		    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
		        logger.error("리플렉션을 통한 메서드 호출 실패 ==> " + e.getMessage());
		        e.printStackTrace();
		        if (!response.isCommitted()) {
		        	logger.error(e.getMessage());
		        	request.setAttribute("alertMsg", e.getMessage());
		            request.getRequestDispatcher("/views/board/errorPage.jsp").forward(request, response);
		        }
		    }
		    
		    //switch문 mav, re, ajax
		} */
	

