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
        
        // ----------------------------------------- 주석 -----------------------------------------
		/*switch (action) {
		case "/list.bo":
			try {
				Map<String, Object> resultMap = bService.listBoard(request.getParameter("page"));
				request.setAttribute("list", resultMap.get("list"));
				request.setAttribute("page", resultMap.get("page"));
				request.getRequestDispatcher("/views/board/list.jsp").forward(request, response);
			} catch (NullPointerException e) {
				logger.error("NullPointerException 발생 ==> " + e.getMessage());
				request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
			}
			break;
			
		case "/detail.bo":
			try {
				int boardNo = Integer.parseInt(request.getParameter("no"));
				Board b = bService.selectBoardDtl(boardNo);
				
				request.setAttribute("b", b);
				request.getRequestDispatcher("/views/board/detail.jsp").forward(request, response);
			} catch (Exception e) {
				logger.error("기타 오류 발생 : " + e.getMessage());
				request.setAttribute("alertMsg", "조회에 실패했습니다.");
				request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
			}
			break;
			
		case "/insert.bo":
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			try {
				bService.insertBoard(title, content);
				request.setAttribute("alertMsg", "게시글 등록이 완료되었습니다.");
				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
			} catch (IllegalArgumentException e) {
				logger.error("IllegalArgumentException 발생 : " + e.getMessage());
				request.setAttribute("alertMsg", e.getMessage());
				request.getSession().setAttribute("enteredTitle", title);
				request.getSession().setAttribute("enteredContent", content);
				request.getRequestDispatcher("views/board/registForm.jsp").forward(request, response);
			} catch (Exception e) {
				logger.error("기타 오류 발생 : " + e.getMessage());
				request.setAttribute("alertMsg", e.getMessage());
				request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
			}
			break;
			
		case "/regist.bo":
			request.getRequestDispatcher("views/board/registForm.jsp").forward(request, response);
			break;
			
		case "/updateForm.bo":
			try {
				int boardNo = Integer.parseInt(request.getParameter("no"));
				Board b = bService.selectBoardDtl(boardNo);
				request.setAttribute("b", b);
				request.getRequestDispatcher("/views/board/updateForm.jsp").forward(request, response);
			} catch (Exception e) {
				logger.error("기타 오류 발생 : " + e.getMessage());
				request.setAttribute("alertMsg", e.getMessage());
				request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
			}
			break;
			
		case "/update.bo":
			try {
				int boardNo = Integer.parseInt(request.getParameter("no"));
				String updateTitle = request.getParameter("title");
				String updateContent = request.getParameter("content");
				
				Board b = new Board();
				b.setNo(boardNo);
				b.setTitle(updateTitle);
				b.setContent(updateContent);
				
				int result = bService.updateBoard(b);
				request.setAttribute("alertMsg", result > 0 ? "처리되었습니다." : "처리에 실패했습니다.");
			} catch (NumberFormatException e) {
				logger.error("NumberFormatException 발생 : " + e.getMessage());
				request.setAttribute("alertMsg", "처리에 실패했습니다.");
				request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
			} catch (Exception e) {
				logger.error("기타 오류 발생 : " + e.getMessage());
				request.setAttribute("alertMsg", "처리에 실패했습니다.");
				request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
			}
			break;
			
		case "/delete.bo":
			try {
				int boardNo = Integer.parseInt(request.getParameter("no"));
				int result = bService.updateDelYn(boardNo);
				request.setAttribute("alertMsg", result > 0 ? "처리되었습니다." : "처리에 실패했습니다.");
				String redirectUrl = result > 0 ? "/list.bo?page=1" : "board/views/errorPage.jsp";
				response.sendRedirect(request.getContextPath() + redirectUrl);
			} catch (NumberFormatException e) {
				logger.error("NumberFormatException 발생 : " + e.getMessage());
				request.setAttribute("alertMsg", "처리에 실패했습니다.");
				request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
			} catch (Exception e) {
				logger.error("기타 오류 발생 : " + e.getMessage());
				request.setAttribute("alertMsg", "처리에 실패했습니다.");
				request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
			}
			break;
			
		case "/search.bo":
			try {
				Map<String, Object> resultMap = bService.listSearch(request.getParameter("page"), request.getParameter("keyword"));
				response.setContentType("application/json; charset=utf-8");
				new Gson().toJson(resultMap, response.getWriter());
			} catch (Exception e) {
				logger.error("기타 오류 발생 : " + e.getMessage());
				request.setAttribute("alertMsg", "처리에 실패했습니다.");
			}
			break;
			
		case "/replyinsert.bo":
			try {
				Reply r = new Reply();
				r.setBNo(Integer.parseInt(request.getParameter("no")));
				r.setContent(request.getParameter("content"));
				
				int result = bService.insertReply(r);
				response.getWriter().print(result);
			} catch (NumberFormatException e) {
				logger.error("NumberFormatException 발생 : " + e.getMessage());
			}
			break;
			
		case "/replylist.bo":
			try {
				List<Reply> list = bService.listReply(Integer.parseInt(request.getParameter("no")));
		
				response.setContentType("application/json; charset=utf-8");
				new Gson().toJson(list, response.getWriter());
			} catch (NumberFormatException e) {
				logger.error("NumberFormatException 발생 : " + e.getMessage());
			}
			break;
		default:
			request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
			break;
		}*/

	

        
	/*
	 * switch (action) { case "/list.bo": try { Map<String, Object> resultMap =
	 * bService.listBoard(request.getParameter("page"));
	 * request.setAttribute("list", resultMap.get("list"));
	 * request.setAttribute("page", resultMap.get("page"));
	 * request.getRequestDispatcher("/views/board/list.jsp").forward(request,
	 * response); } catch (NullPointerException e) {
	 * logger.error("NullPointerException 발생 ==> " + e.getMessage());
	 * request.getRequestDispatcher("views/board/errorPage.jsp").forward(request,
	 * response); } break; case "/detail.bo": try { int boardNo =
	 * Integer.parseInt(request.getParameter("no")); Board b =
	 * bService.selectBoardDtl(boardNo);
	 * 
	 * request.setAttribute("b", b);
	 * request.getRequestDispatcher("/views/board/detail.jsp").forward(request,
	 * response); } catch (Exception e) { logger.error("기타 오류 발생 : " +
	 * e.getMessage()); request.setAttribute("alertMsg", "조회에 실패했습니다.");
	 * request.getRequestDispatcher("views/board/errorPage.jsp").forward(request,
	 * response); } break; case "/insert.bo": String title =
	 * request.getParameter("title"); String content =
	 * request.getParameter("content"); try { bService.insertBoard(title, content);
	 * request.setAttribute("alertMsg", "게시글 등록이 완료되었습니다.");
	 * response.sendRedirect(request.getContextPath() + "/list.bo?page=1"); } catch
	 * (IllegalArgumentException e) { logger.error("IllegalArgumentException 발생 : "
	 * + e.getMessage()); request.setAttribute("alertMsg", e.getMessage());
	 * request.getSession().setAttribute("enteredTitle", title);
	 * request.getSession().setAttribute("enteredContent", content);
	 * request.getRequestDispatcher("views/board/registForm.jsp").forward(request,
	 * response); } catch (Exception e) { logger.error("기타 오류 발생 : " +
	 * e.getMessage()); request.setAttribute("alertMsg", e.getMessage());
	 * request.getRequestDispatcher("views/board/errorPage.jsp").forward(request,
	 * response); } break; case "/regist.bo":
	 * request.getRequestDispatcher("views/board/registForm.jsp").forward(request,
	 * response); break; case "/updateForm.bo": try{ int boardNo =
	 * Integer.parseInt(request.getParameter("no")); Board b =
	 * bService.selectBoardDtl(boardNo); request.setAttribute("b", b);
	 * request.getRequestDispatcher("/views/board/updateForm.jsp").forward(request,
	 * response); } catch (Exception e) { logger.error("기타 오류 발생 : " +
	 * e.getMessage()); request.setAttribute("alertMsg", e.getMessage());
	 * request.getRequestDispatcher("views/board/errorPage.jsp").forward(request,
	 * response); } break; case "/update.bo": try { int boardNo =
	 * Integer.parseInt(request.getParameter("no")); String updateTitle =
	 * request.getParameter("title"); String updateContent =
	 * request.getParameter("content");
	 * 
	 * Board b = new Board(); b.setNo(boardNo); b.setTitle(updateTitle);
	 * b.setContent(updateContent);
	 * 
	 * int result = bService.updateBoard(b); request.setAttribute("alertMsg", result
	 * > 0 ? "처리되었습니다." : "처리에 실패했습니다.");
	 * 
	 * } catch (NumberFormatException e) {
	 * logger.error("NumberFormatException 발생 : " + e.getMessage());
	 * request.setAttribute("alertMsg", "처리에 실패했습니다.");
	 * request.getRequestDispatcher("views/board/errorPage.jsp").forward(request,
	 * response); } catch (Exception e) { logger.error("기타 오류 발생 : " +
	 * e.getMessage()); request.setAttribute("alertMsg", "처리에 실패했습니다.");
	 * request.getRequestDispatcher("views/board/errorPage.jsp").forward(request,
	 * response); } break; case "/delete.bo": try { int boardNo =
	 * Integer.parseInt(request.getParameter("no"));
	 * 
	 * int result = bService.updateDelYn(boardNo);
	 * 
	 * request.setAttribute("alertMsg", result > 0 ? "처리되었습니다." : "처리에 실패했습니다.");
	 * String redirectUrl = result > 0 ? "/list.bo?page=1" :
	 * "board/views/errorPage.jsp"; response.sendRedirect(request.getContextPath() +
	 * redirectUrl);
	 * 
	 * } catch (NumberFormatException e) {
	 * logger.error("NumberFormatException 발생 : " + e.getMessage());
	 * request.setAttribute("alertMsg", "처리에 실패했습니다.");
	 * request.getRequestDispatcher("views/board/errorPage.jsp").forward(request,
	 * response); } catch (Exception e) { logger.error("기타 오류 발생 : " +
	 * e.getMessage()); request.setAttribute("alertMsg", "처리에 실패했습니다.");
	 * request.getRequestDispatcher("views/board/errorPage.jsp").forward(request,
	 * response); } break; case "/search.bo": try { Map<String, Object> resultMap =
	 * bService.listSearch(request.getParameter("page"),
	 * request.getParameter("keyword"));
	 * 
	 * response.setContentType("application/json; charset=utf-8"); new
	 * Gson().toJson(resultMap, response.getWriter()); } catch (Exception e) {
	 * logger.error("기타 오류 발생 : " + e.getMessage());
	 * request.setAttribute("alertMsg", "처리에 실패했습니다."); } break; case
	 * "/replyinsert.bo": try { int boardNo =
	 * Integer.parseInt(request.getParameter("no")); String replyContent =
	 * request.getParameter("content");
	 * 
	 * Reply r = new Reply();
	 * 
	 * r.setBNo(Integer.parseInt(request.getParameter("no")));
	 * 
	 * r.setContent(replyContent);
	 * 
	 * int result = bService.insertReply(r);
	 * 
	 * // ajax 요청이 온곳 : success function으로 데이터 전달
	 * response.getWriter().print(result); } catch (NumberFormatException e) {
	 * logger.error("NumberFormatException 발생 : " + e.getMessage()); } //
	 * insertReply(request, response); break; case "/replylist.bo":
	 * listReply(request, response); break; default:
	 * request.getRequestDispatcher("views/board/errorPage.jsp").forward(request,
	 * response); break;
	 * 
	 * } }
	 */
//	}

	/**
	 * 게시글 상세 조회
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
//	private void selectBoardDtl(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//		try {
//			int boardNo = Integer.parseInt(request.getParameter("no"));
//			
//			int result = bService.updateHit(boardNo);
//
//			if (result > 0) {
//				Board b = bService.selectBoardDtl(boardNo);
//
//				if (b != null) { // Board 객체가 null이 아닐 때 request 영역에 담은 후 detail 페이지로 forward
//					request.setAttribute("b", b);
//					request.getRequestDispatcher("/views/board/detail.jsp").forward(request, response);
//					return;
//				} 
//				
//				request.setAttribute("alertMsg", "조회에 실패했습니다.");
//				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
//			} 
//
//		} catch (NumberFormatException e) {
//			logger.error("NumberFormatException 발생 : " + e.getMessage());
//			request.setAttribute("alertMsg", "조회에 실패했습니다.");
//			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
//		} catch (Exception e) {
//			logger.error("기타 오류 발생 : " + e.getMessage());
//			request.setAttribute("alertMsg", "조회에 실패했습니다.");
//			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
//		}
//	}

	/**
	 * 댓글 조회 (ajax)
	 * 
	 * @param request
	 * @param response
	 * @throws JsonIOException
	 * @throws IOException
	 */
//	private void listReply(HttpServletRequest request, HttpServletResponse response)
//			throws JsonIOException, IOException {
//		try {
//			int boardNo = Integer.parseInt(request.getParameter("no"));
//
//			List<Reply> list = bService.listReply(boardNo);
//
//			response.setContentType("application/json; charset=utf-8");
//			new Gson().toJson(list, response.getWriter());
//		} catch (NumberFormatException e) {
//			logger.error("NumberFormatException 발생 : " + e.getMessage());
//		}
//
//	}

	/**
	 * 댓글 등록 (ajax)
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
//	private void insertReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		try {
//			int boardNo = Integer.parseInt(request.getParameter("no"));
//			String replyContent = request.getParameter("content");
//
//			Reply r = new Reply();
//
//			r.setBNo(boardNo);
//			r.setContent(replyContent);
//
//			int result = bService.insertReply(r);
//
//			// ajax 요청이 온곳 : success function으로 데이터 전달
//			response.getWriter().print(result);
//		} catch (NumberFormatException e) {
//			logger.error("NumberFormatException 발생 : " + e.getMessage());
//		}
//	}

	/**
	 * 게시물 검색 (ajax)
	 * 
	 * @param request
	 * @param response
	 * @throws JsonIOException
	 * @throws IOException
	 */
//	private void listSearch(HttpServletRequest request, HttpServletResponse response)
//			throws JsonIOException, IOException {
//		String keyword = request.getParameter("keyword");
//
//		try {
//			int currentPage;
//
//			try {
//				currentPage = Integer.parseInt(request.getParameter("page"));
//			} catch (NumberFormatException e) {
//				currentPage = 1;
//			}
//
//			Map<String, Object> resultMap = bService.listSearch(currentPage, keyword);
//
//			response.setContentType("application/json; charset=utf-8");
//			new Gson().toJson(resultMap, response.getWriter());
//		} catch (Exception e) {
//			logger.error("기타 오류 발생 : " + e.getMessage());
//			request.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
//		}
//
//	}

	/**
	 * 게시물 삭제
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
//	private void updateDelYn(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//		try {
//			int boardNo = Integer.parseInt(request.getParameter("no"));
//
//			int result = bService.updateDelYn(boardNo);
//			
//			if (result > 0) {
//				request.setAttribute("alertMsg", "성공적으로 삭제되었습니다.");
//				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
//			} else {
//				request.setAttribute("alertMsg", "삭제에 실패했습니다.");
//				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
//			}
//
//		} catch (NumberFormatException e) {
//			logger.error("NumberFormatException 발생 : " + e.getMessage());
//			request.setAttribute("alertMsg", "게시글 번호가 올바르지 않습니다.");
//			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
//		} catch (Exception e) {
//			logger.error("기타 오류 발생 : " + e.getMessage());
//			request.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
//			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
//		}
//	}

	/**
	 * 게시글 목록 조회
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	/*
	 * 
	 * private void listBoard(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException {
	 * 
	 * Map<String, Object> resultMap =
	 * bService.listBoard(request.getParameter("page"));
	 * request.setAttribute("list", resultMap.get("list"));
	 * request.setAttribute("page", resultMap.get("page"));
	 * 
	 * request.getRequestDispatcher("/views/board/list.jsp").forward(request,
	 * response); }
	 * 
	 */

	/**
	 * 게시글 등록
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
//	private void insertBoard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//		try {
//			String title = request.getParameter("title");
//			String content = request.getParameter("content");
//			
//			// title 및 content값 비어있는지 체크
//			if (title.trim().isEmpty()) {
//				request.setAttribute("alertMsg", "제목을 입력해주세요.");
//				response.sendRedirect(request.getContextPath() + "/views/board/registForm.jsp");
//				return;
//			} 
//			
//			if (content.trim().isEmpty()) {
//				request.setAttribute("alertMsg", "내용을 입력해주세요.");
//				response.sendRedirect(request.getContextPath() + "/views/board/registForm.jsp");
//				return;
//			} 
//			
//			Board b = new Board();
//			b.setTitle(title);
//			b.setContent(content);
//
//			int result = bService.insertBoard(b);
//			
//			if (result > 0) {
//				request.setAttribute("alertMsg", "게시글 등록이 완료되었습니다.");
//				response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
//			} else {
//				request.getSession().setAttribute("enteredTitle", title);
//				request.getSession().setAttribute("enteredContent", content);
//				
//				request.setAttribute("alertMsg", "게시글 등록에 실패했습니다.");
//				request.getRequestDispatcher("/views/board/registForm.jsp").forward(request, response);
//			}
//		} catch (Exception e) {
//			logger.error("기타 오류 발생 : " + e.getMessage());
//			request.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
//			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
//		}
//
//	}
//	
	/**
	 * 게시글 수정 시 게시글 제목 및 내용 조회
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
//	private void showUpdateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		int boardNo = Integer.parseInt(request.getParameter("no"));
//		
//		Board b = bService.selectBoardDtl(boardNo);
//		
//		request.setAttribute("b", b);
//		
//		request.getRequestDispatcher("/views/board/updateForm.jsp").forward(request, response);
//	}

	/**
	 * 게시글 수정
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
//	private void updateBoard(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		
//		try {
//			int boardNo = Integer.parseInt(request.getParameter("no"));
//			String title = request.getParameter("title");
//			String content = request.getParameter("content");
//
//			Board b = new Board();
//			b.setNo(boardNo);
//			b.setTitle(title);
//			b.setContent(content);
//
//			int result = bService.updateBoard(b);
//
//			if (result > 0) {
//				request.setAttribute("alertMsg", "성공적으로 수정되었습니다.");
//				response.sendRedirect(request.getContextPath() + "/detail.bo?no=" + boardNo);
//			} else {
//				request.setAttribute("alertMsg", "수정에 실패했습니다.");
//				response.sendRedirect(request.getContextPath() + "/detail.bo?no=" + boardNo);
//			}
//
//		} catch (NumberFormatException e) {
//			logger.error("NumberFormatException 발생 : " + e.getMessage());
//			request.setAttribute("alertMsg", "게시글 번호가 올바르지 않습니다.");
//			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
//		} catch (Exception e) {
//			logger.error("기타 오류 발생 : " + e.getMessage());
//			request.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
//			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
//		}
//
//	}

}
