package com.jw.board.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.jw.board.model.dao.BoardDao;
import com.jw.board.model.service.BoardService;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.Reply;

/**
 * .bo로 끝나는 모든 요청을 받는 Controller
 */
@WebServlet("*.bo")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BoardController.class);
	private static Properties prop = new Properties();
	private static BoardService bService = new BoardService();

	/**
	 * mapper 파일 로드
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
//			logger.debug("init 메소드 실행");
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

        switch (action) {
            case "/list.bo":
            	listBoard(request, response);
                break;
            case "/detail.bo":
            	selectBoardDtl(request, response);
                break; 
            case "/insert.bo":
            	insertBoard(request, response);
                break;
            case "/regist.bo":
        		request.getRequestDispatcher("views/board/registForm.jsp").forward(request, response);
                break;
            case "/updateForm.bo":
        		showUpdateForm(request, response);
                break;
            case "/update.bo":
            	updateBoard(request, response);
                break;
            case "/delete.bo":
            	updateDelYn(request, response);
                break;
            case "/search.bo":
            	listSearch(request, response);
                break;
            case "/replyinsert.bo":
            	insertReply(request, response);
                break;   
            case "/replylist.bo":
            	listReply(request, response);
                break;
            default:
            	request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
//                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
	}

	/** 게시글 상세 조회
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void selectBoardDtl(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));

			int result = bService.updateHit(boardNo);

			if (result > 0) {
				Board b = bService.selectBoardDtl(boardNo);

				if (b != null) { // Board 객체가 null이 아닐 때 request 영역에 담은 후 detail 페이지로 forward
					request.setAttribute("b", b);
					request.getRequestDispatcher("/views/board/detail.jsp").forward(request, response);
				} else {
					request.setAttribute("alertMsg", "해당 게시글을 찾을 수 없습니다.");
					response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
				}
			} else {
				request.setAttribute("alertMsg", "게시글 조회에 실패했습니다.");
				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
			}

		} catch (NumberFormatException e) {
			logger.error("NumberFormatException 발생 : " + e.getMessage());
			request.setAttribute("alertMsg", "올바른 게시글 번호가 아닙니다.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			request.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		}
	}

	/** 댓글 조회 (ajax)
	 * 
	 * @param request
	 * @param response
	 * @throws JsonIOException
	 * @throws IOException
	 */
	private void listReply(HttpServletRequest request, HttpServletResponse response) throws JsonIOException, IOException {
		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));
			
			List<Reply> list = bService.listReply(boardNo);

			response.setContentType("application/json; charset=utf-8");
			new Gson().toJson(list, response.getWriter());
		} catch (NumberFormatException e) {
			logger.error("NumberFormatException 발생 : " + e.getMessage());
		}

		
	}

	/** 댓글 등록 (ajax)
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void insertReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));
			String replyContent = request.getParameter("content");
	
			Reply r = new Reply();
	
			r.setBNo(boardNo);
			r.setContent(replyContent);
	
			int result = bService.insertReply(r);
	
			// ajax 요청이 온곳 : success function으로 데이터 전달
			response.getWriter().print(result);
		} catch(NumberFormatException e) {
			logger.error("NumberFormatException 발생 : " + e.getMessage());
		}
	}

	/** 게시물 검색 (ajax)
	 * 
	 * @param request
	 * @param response
	 * @throws JsonIOException
	 * @throws IOException
	 */
	private void listSearch(HttpServletRequest request, HttpServletResponse response) throws JsonIOException, IOException {
		String keyword = request.getParameter("keyword");

		try {
			int currentPage;

			try {
				currentPage = Integer.parseInt(request.getParameter("page"));
			} catch (NumberFormatException e) {
				currentPage = 1;
			}

			Map<String, Object> resultMap = bService.listSearch(currentPage, keyword);
			
			response.setContentType("application/json; charset=utf-8");
			new Gson().toJson(resultMap, response.getWriter());
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			request.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
		}
	
	}

	/** 게시물 삭제
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void updateDelYn(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));

			int result = bService.updateDelYn(boardNo);

			if (result > 0) {
				request.setAttribute("alertMsg", "성공적으로 삭제되었습니다.");
				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
			} else {
				request.setAttribute("alertMsg", "삭제에 실패했습니다.");
				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
			}

		} catch (NumberFormatException e) {
			logger.error("NumberFormatException 발생 : " + e.getMessage());
			request.setAttribute("alertMsg", "게시글 번호가 올바르지 않습니다.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			request.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		}
	}

	/** 게시글 목록 조회
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listBoard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int currentPage;
			try {
				currentPage = Integer.parseInt(request.getParameter("page"));
			} catch (NumberFormatException e) {
				currentPage = 1;
			}
			
			Map<String, Object> resultMap = bService.listBoard(currentPage);

			request.setAttribute("list", resultMap.get("list"));
			request.setAttribute("page", resultMap.get("page"));
			request.getRequestDispatcher("/views/board/list.jsp").forward(request, response);
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			request.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
		}
	}
	
	
	/** 게시글 등록
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void insertBoard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			// title 및 content값 비어있는지 체크
			if (title.trim().isEmpty()) {
				request.setAttribute("alertMsg", "제목을 입력해주세요.");
				response.sendRedirect(request.getContextPath() + "/views/board/registForm.jsp");
				return;
			} 
			
			if (content.trim().isEmpty()) {
				request.setAttribute("alertMsg", "내용을 입력해주세요.");
				response.sendRedirect(request.getContextPath() + "/views/board/registForm.jsp");
				return;
			} 
			
			Board b = new Board();
			b.setTitle(title);
			b.setContent(content);

			int result = bService.insertBoard(b);
			
			if (result > 0) {
				request.setAttribute("alertMsg", "게시글 등록이 완료되었습니다.");
				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
			} else {
				request.getSession().setAttribute("enteredTitle", title);
				request.getSession().setAttribute("enteredContent", content);
				
				request.setAttribute("alertMsg", "게시글 등록에 실패했습니다.");
				request.getRequestDispatcher("/views/board/registForm.jsp").forward(request, response);
			}
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			request.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		}

	}
	
	/** 게시글 수정 시 게시글 제목 및 내용 조회 
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void showUpdateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int boardNo = Integer.parseInt(request.getParameter("no"));
		
		Board b = bService.selectBoardDtl(boardNo);
		
		request.setAttribute("b", b);
		
		request.getRequestDispatcher("/views/board/updateForm.jsp").forward(request, response);
	}
	
	
	/** 게시글 수정
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void updateBoard(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			Board b = new Board();
			b.setNo(boardNo);
			b.setTitle(title);
			b.setContent(content);

			int result = bService.updateBoard(b);

			if (result > 0) {
				request.setAttribute("alertMsg", "성공적으로 수정되었습니다.");
				response.sendRedirect(request.getContextPath() + "/detail.bo?no=" + boardNo);
			} else {
				request.setAttribute("alertMsg", "수정에 실패했습니다.");
				response.sendRedirect(request.getContextPath() + "/detail.bo?no=" + boardNo);
			}

		} catch (NumberFormatException e) {
			logger.error("NumberFormatException 발생 : " + e.getMessage());
			request.setAttribute("alertMsg", "게시글 번호가 올바르지 않습니다.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			request.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		}

	}
	

}
