package com.jw.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jw.board.interfaces.ControllerV2;
import com.jw.board.model.service.BoardService;
import com.jw.board.model.vo.MyView;

/**
 * Servlet implementation class BoardController
 */
@WebServlet(name="frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(FrontControllerServletV2.class);
	private Map<String, ControllerV2> controllerMap = new HashMap<>();
	
	/**
	 * 
	 * @see HttpServlet#HttpServlet()
	 */
	 public FrontControllerServletV2() {
	        controllerMap.put("/front-controller/v2/board/list.bo", new BoardListControllerV2());
	        controllerMap.put("/front-controller/v2/board/detail.bo", new BoardDetailControllerV2());
	 }
	

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String path = requestURI.substring(contextPath.length());

		logger.info("requestURI: " + requestURI + ", contextPath: " + contextPath + ", path: " + path);

		ControllerV2 controller = controllerMap.get(path);

		if (controller == null) {
			logger.info("요청된 경로가 아님");
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		try {
			MyView view = controller.process(request, response);
			view.render(request, response);
		} catch (Exception e) {
			logger.error("서비스 처리 중 오류 발생: " + e.getMessage());
			throw new ServletException(e);
		}
	}


}
