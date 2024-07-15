package com.jw.board.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.jw.board.model.dao.BoardDao;
import com.jw.board.model.service.BoardService;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.PageInfo;
import com.jw.board.model.vo.Reply;
import com.jw.common.util.PagingUtil;

/**
 * Servlet implementation class BoardController
 */
@WebServlet("*.bo")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BoardController.class);
	private static Properties prop = new Properties();

	/**
	 * mapper 파일 로드
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			logger.debug("init 메소드 실행");
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
                list(request, response);
                break;
            case "/detail.bo":
                detail(request, response);
                break; 
            case "/insert.bo":
                insert(request, response);
                break;
            case "/regist.bo":
        		request.getRequestDispatcher("views/board/registForm.jsp").forward(request, response);
                break;
            case "/updateForm.bo":
        		showUpdateForm(request, response);
                break;
            case "/update.bo":
        		update(request, response);
                break;
            case "/delete.bo":
        		delete(request, response);
                break;
            case "/search.bo":
        		ajaxSearch(request, response);
                break;
            case "/replyinsert.bo":
            	ajaxReplyinsert(request, response);
                break;   
            case "/replylist.bo":
            	ajaxReplylist(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
	}

	private void detail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();

		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));

			BoardService bService = new BoardService();

			int result = bService.updateHit(boardNo);

			if (result > 0) {
				Board b = bService.selectBoardDetail(boardNo);

				if (b != null) {
					request.setAttribute("b", b);
					request.getRequestDispatcher("/views/board/detail.jsp").forward(request, response);
				} else {
					session.setAttribute("alertMsg", "해당 게시글을 찾을 수 없습니다.");
					response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
				}
			} else {
				session.setAttribute("alertMsg", "게시글 조회에 실패했습니다.");
				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
			}

		} catch (NumberFormatException e) {
			logger.error("NumberFormatException 발생 : " + e.getMessage());
			session.setAttribute("alertMsg", "올바른 게시글 번호가 아닙니다.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			session.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		}
	}

	private void ajaxReplylist(HttpServletRequest request, HttpServletResponse response) throws JsonIOException, IOException {
		int boardNo = Integer.parseInt(request.getParameter("no"));

		List<Reply> list = new BoardService().selectReply(boardNo);

		response.setContentType("application/json; charset=utf-8");
		new Gson().toJson(list, response.getWriter());
	}

	private void ajaxReplyinsert(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int boardNo = Integer.parseInt(request.getParameter("no"));
		String replyContent = request.getParameter("content");

		Reply r = new Reply();

		r.setbNo(boardNo);
		r.setContent(replyContent);

		int result = new BoardService().insertReply(r);

		// ajax 요청이 온곳 : success function으로 데이터 전달
		response.getWriter().print(result);
	}

	private void ajaxSearch(HttpServletRequest request, HttpServletResponse response) throws JsonIOException, IOException {
		String keyword = request.getParameter("keyword");

		try {
			int listCount = new BoardService().selectSearchCount(keyword);
			int currentPage;

			try {
				currentPage = Integer.parseInt(request.getParameter("page"));
			} catch (NumberFormatException e) {
				currentPage = 1;
			}

			PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
			List<Board> list = new BoardService().selectSearch(page, keyword);

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("list", list);
			resultMap.put("page", page);

			response.setContentType("application/json; charset=utf-8");
			new Gson().toJson(resultMap, response.getWriter());
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			request.getSession().setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
		}
		
	
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();

		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));

			int result = new BoardService().updateDelYn(boardNo);

			if (result > 0) {
				session.setAttribute("alertMsg", "성공적으로 삭제되었습니다.");
				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
			} else {
				session.setAttribute("alertMsg", "삭제에 실패했습니다.");
				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
			}

		} catch (NumberFormatException e) {
			logger.error("NumberFormatException 발생 : " + e.getMessage());
			session.setAttribute("alertMsg", "게시글 번호가 올바르지 않습니다.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			session.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		}
	}

	private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int listCount = new BoardService().selectBoardCount();
			int currentPage;

			try {
				currentPage = Integer.parseInt(request.getParameter("page"));
			} catch (NumberFormatException e) {
				currentPage = 1;
			}

			PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);

			List<Board> list = new BoardService().selectBoard(page);

			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.getRequestDispatcher("/views/board/list.jsp").forward(request, response);
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			request.getSession().setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
		}
	}
	
	
	private void insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		try {
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
				session.setAttribute("alertMsg", "제목과 내용을 모두 입력해주세요.");
				response.sendRedirect(request.getContextPath() + "/views/board/write.jsp");
				return;
			}

			Board b = new Board();
			b.setTitle(title);
			b.setContent(content);

			int result = new BoardService().insertBoard(b);

			if (result > 0) {
				session.setAttribute("alertMsg", "게시글 등록이 완료되었습니다.");
				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
			} else {
				session.setAttribute("alertMsg", "게시글 등록에 실패했습니다.");
				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
			}
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			session.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		}

	}
	
	private void showUpdateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int boardNo = Integer.parseInt(request.getParameter("no"));
		
		Board b = new BoardService().selectBoardDetail(boardNo);
		
		request.setAttribute("b", b);
		
		request.getRequestDispatcher("/views/board/updateForm.jsp").forward(request, response);
	}
	
	
	private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			Board b = new Board();
			b.setNo(boardNo);
			b.setTitle(title);
			b.setContent(content);

			int result = new BoardService().updateBoard(b);

			if (result > 0) {
				request.getSession().setAttribute("alertMsg", "성공적으로 수정되었습니다.");
				response.sendRedirect(request.getContextPath() + "/detail.bo?no=" + boardNo);
			} else {
				request.getSession().setAttribute("alertMsg", "수정에 실패했습니다.");
				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
			}

		} catch (NumberFormatException e) {
			logger.error("NumberFormatException 발생 : " + e.getMessage());
			request.getSession().setAttribute("alertMsg", "게시글 번호가 올바르지 않습니다.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			request.getSession().setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		}

	}
	

}
