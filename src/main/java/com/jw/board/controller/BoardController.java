package com.jw.board.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jw.board.model.dao.BoardDao;
import com.jw.board.model.service.BoardService;

/**
 * .bo로 끝나는 모든 요청을 받는 Controller
 */
@WebServlet("*.bo")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BoardController.class);
	private static Properties prop = new Properties();
	private static BoardService bService = new BoardService();
    private static Map<String, String> urlMap = new HashMap<>();
    
    static {
        urlMap.put("/list.bo", "listBoard");
        urlMap.put("/detail.bo", "selectBoardDtl");
        urlMap.put("/insert.bo", "insertBoard");
        urlMap.put("/regist.bo", "registForm");
        urlMap.put("/updateForm.bo", "updateForm");
        urlMap.put("/update.bo", "updateBoard");
        urlMap.put("/delete.bo", "updateDelYn");
        urlMap.put("/search.bo", "listSearch");
        urlMap.put("/replyinsert.bo", "insertReply");
        urlMap.put("/replylist.bo", "listReply");
    }
	/**
	 * mapper 파일 로드
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			prop.loadFromXML(new FileInputStream(BoardDao.class.getResource("/db/mappers/board-mapper.xml").getPath()));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Properties getProperties() {
		return prop;
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());

        if (urlMap.containsKey(action)) {
            String methodName = urlMap.get(action);
            try {
                Method method = BoardService.class.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
                method.invoke(bService, request, response);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error("리플렉션을 통한 메서드 호출 실패: " + e.getMessage());
                request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
            }
        } else {
            request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
        }
    }

}
