package com.jw.board.model.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.jw.board.model.dao.BoardDao;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.PageInfo;
import com.jw.board.model.vo.Reply;
import com.jw.common.util.PagingUtil;


public class BoardService {

	private static BoardDao bDao = new BoardDao();

    /** 게시글 목록 조회 및 페이징
     * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listBoard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String nowPage = request.getParameter("page");
			if (nowPage == null || nowPage.trim().isEmpty()) nowPage = "1";
			int currentPage = Integer.parseInt(nowPage);
			int listCount = bDao.selectBoardCount();

			PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
			List<Board> list = bDao.listBoard(page);

			request.setAttribute("list", list);
			request.setAttribute("page", page);
			request.getRequestDispatcher("/views/board/list.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("alertMsg", "처리에 실패했습니다.");
			request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
		}
	}

	/** 게시글 등록
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void insertBoard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        try {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("제목을 입력해주세요.");
            } 
            
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("내용을 입력해주세요.");
            }
            
            Board b = new Board();
            b.setTitle(title);
            b.setContent(content);
            
            int result = bDao.insertBoard(b);
            
            if(result <= 0) {
                throw new Exception("처리에 실패했습니다.");
            }
            response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
        } catch (IllegalArgumentException e) {
            request.setAttribute("alertMsg", e.getMessage());
            request.getSession().setAttribute("enteredTitle", title);
            request.getSession().setAttribute("enteredContent", content);
            request.getRequestDispatcher("views/board/registForm.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("alertMsg", e.getMessage());
            request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
        }
    }
	
	/** 글 작성 페이지 포워딩
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void registForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("views/board/registForm.jsp").forward(request, response);
    }
	
	/** 게시글 상세 조회
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void selectBoardDtl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));
			Board b = bDao.selectBoardDtl(boardNo);
			bDao.updateHit(boardNo);
			request.setAttribute("b", b);
			request.getRequestDispatcher("/views/board/detail.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("alertMsg", "조회에 실패했습니다.");
			request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
		}
	}
	
	/**
	 * 게시글 수정
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Board b = bDao.selectBoardDtl(Integer.parseInt(request.getParameter("no")));
			request.setAttribute("b", b);
			request.getRequestDispatcher("/views/board/updateForm.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("alertMsg", e.getMessage());
			request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
		}
	}
	 
	 /** 게시글 수정
	  * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateBoard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));

			Board b = new Board();
			b.setNo(boardNo);
			b.setTitle(request.getParameter(request.getParameter("title")));
			b.setContent(request.getParameter(request.getParameter("content")));

			bDao.updateBoard(b);
			response.sendRedirect(request.getContextPath() + "/detail.bo?no=" + boardNo);
		} catch (Exception e) {
			request.setAttribute("alertMsg", "처리에 실패했습니다.");
			request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
		}
	}

	/** 게시글 삭제 (delete문이 아닌 del_yn 컬럼 update)
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateDelYn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			bDao.updateDelYn(Integer.parseInt(request.getParameter("no")));
			response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
		} catch (Exception e) {
			request.setAttribute("alertMsg", "처리에 실패했습니다.");
			request.getRequestDispatcher("views/board/errorPage.jsp").forward(request, response);
		}
	}
	
	 /** 검색 (ajax)
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String nowPage = request.getParameter("page");
			String keyword = request.getParameter("keyword");
			if (nowPage == null || nowPage.trim().isEmpty())
				nowPage = "1";
			int currentPage = Integer.parseInt(nowPage);
			int listCount = bDao.selectSearchCount(keyword);

			PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
			List<Board> list = bDao.listSearch(page, keyword);

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("list", list);
			resultMap.put("page", page);

			response.setContentType("application/json; charset=utf-8");
			new Gson().toJson(resultMap, response.getWriter());
		} catch (Exception e) {
			request.setAttribute("alertMsg", "처리에 실패했습니다.");
		}
	}
	
	/** 댓글 조회 (ajax)
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listReply(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));
			List<Reply> list = bDao.listReply(boardNo);
			response.setContentType("application/json; charset=utf-8");
			new Gson().toJson(list, response.getWriter());
		} catch (Exception e) {
			request.setAttribute("alertMsg", "처리에 실패했습니다.");
		}
	}
	
	/** 댓글 등록 (ajax)
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void insertReply(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Reply r = new Reply();
			r.setBNo(Integer.parseInt(request.getParameter("no")));
			r.setContent(request.getParameter("content"));

			int result = bDao.insertReply(r);
			response.getWriter().print(result);
		} catch (Exception e) {
			request.setAttribute("alertMsg", "처리에 실패했습니다.");
		}
	}
}
