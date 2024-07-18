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
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BoardController.class);
	
//	private Properties prop = new Properties();
	private BoardService bService = new BoardService();
//    private Map<String, String> urlMap = new HashMap<>();
    private List<String> urlList = new ArrayList<>();
   
    
	/*    static {
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
	}   */
	/*
	private void addUrlMapping(String url) {
	    String methodName = getMethodNameFromAction(url);
	    if (methodName != null) {
	        urlMap.put(url, methodName);
	        logger.info("Mapped URL " + url + " to method " + methodName);
	    } else {
	        logger.warn("Failed to map URL " + url);
	    }
	
	    }*/
    
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
	/*
		private void initUrlMap() {
			addUrlMapping("/board/list.bo");
			addUrlMapping("/board/detail.bo");
			addUrlMapping("/board/insert.bo");
			addUrlMapping("/board/regist.bo");
			addUrlMapping("/board/updateForm.bo");
			addUrlMapping("/board/update.bo");
			addUrlMapping("/board/delete.bo");
			addUrlMapping("/board/listSearch.bo");
			addUrlMapping("/reply/insert.bo");
			addUrlMapping("/reply/list.bo");
		}
	*/
	/**
	 * mapper 파일 로드
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		initUrlList();
		//initUrlMap();
		
		/*try {
			String filePath = BoardDao.class.getResource("/db/mappers/board-mapper.xml").getPath();
			if (filePath == null) {
				throw new IOException("filePath == null");
			}
			prop.loadFromXML(new FileInputStream(filePath));
		} catch (IOException e) {
			logger.error("IOException 발생 ==> board-mapper.xml 파일 로드 실패" + e.getMessage());
		}*/
		

	}
	
	/*
		public Properties getProperties() {
			return prop;
		}
		
		*/

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String action = uri.substring(contextPath.length());
        
        logger.info("Received action: " + action);
        
        //List에 url 담아 두고 리스트에 action이 있는지 검사 
        if (urlList.contains(action)) {
        	String methodName = UrlMappingUtil.getMethodNameFromAction(action);
            try {
                Method method = BoardService.class.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
                method.invoke(bService, request, response);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error("리플렉션을 통한 메서드 호출 실패 ==> " + e.getMessage());
                e.printStackTrace();
                if (!response.isCommitted()) {
                	logger.error(e.getMessage());
                	request.setAttribute("alertMsg", e.getMessage());
                    request.getRequestDispatcher("/views/board/errorPage.jsp").forward(request, response);
                }
            }
        } 
    }
	
	

	
}
