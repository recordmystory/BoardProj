package com.jw.board.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jw.board.model.service.BoardService;
import com.jw.common.util.UrlMappingUtil;

/**
 * .bo로 끝나는 모든 요청을 받는 Controller
 */
@WebServlet("*.bo")
//@WebInI

public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BoardController.class);
	
	private BoardService bService = new BoardService();
    private List<String> urlList = new ArrayList<>();
   
    
    private void initUrlList() {
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
    }
    
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        logger.info("요청 url : " + action);
        
        //List에 url 담아 두고 리스트에 action이 있는지 검사 
        if (urlList.contains(action)) {
        	String methodName = UrlMappingUtil.getMethodNameFromAction(action);
            try {
                Method method = BoardService.class.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
                method.invoke(bService, request, response);
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
        } 
    }
	
}
