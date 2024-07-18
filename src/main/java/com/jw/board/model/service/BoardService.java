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

	/**
	 * 게시글 목록 조회 및 페이징
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listBoard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String nowPage = request.getParameter("page");
		if (nowPage == null || nowPage.trim().isEmpty())
			nowPage = "1";
		int currentPage = Integer.parseInt(nowPage);
		int listCount = bDao.selectBoardCount();

		PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
		int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
		int endRow = startRow + page.getBoardLimit() - 1;
		List<Board> list = bDao.listBoard(startRow, endRow);

		request.setAttribute("list", list);
		request.setAttribute("page", page);
		request.getRequestDispatcher("/views/board/list.jsp").forward(request, response);
	}

	/**
	 * 게시글 등록
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void insertBoard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("제목을 입력해주세요.");
		}

		if (content == null || content.trim().isEmpty()) {
			throw new IllegalArgumentException("내용을 입력해주세요.");
		}

		Board b = new Board();
		b.setTitle(title);
		b.setContent(content);

		int result = bDao.insertBoard(title, content);

		if (result <= 0) {
			throw new Exception("처리에 실패했습니다.");
		}
		
		request.setAttribute("alertMsg", "등록 되었습니다.");
		response.sendRedirect("/board/list.bo?page=1");
	}

	/**
	 * 글 작성 페이지 포워딩
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void registBoard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getRequestDispatcher("/views/board/registForm.jsp").forward(request, response);
	}

	/**
	 * 게시글 상세 조회
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void detailBoard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int boardNo = Integer.parseInt(request.getParameter("no"));
		Board b = bDao.detailBoard(boardNo);
		bDao.updateHit(boardNo);
		request.setAttribute("b", b);
		request.getRequestDispatcher("/views/board/detail.jsp").forward(request, response);
	}

	/**
	 * 게시글 수정
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateFormBoard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Board b = bDao.detailBoard(Integer.parseInt(request.getParameter("no")));
		request.setAttribute("b", b);
		request.getRequestDispatcher("/views/board/updateForm.jsp").forward(request, response);
	}

	/**
	 * 게시글 수정
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateBoard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int boardNo = Integer.parseInt(request.getParameter("no"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		/*		Board b = new Board();
				b.setNo(boardNo);
				b.setTitle(request.getParameter("title"));
				b.setContent(request.getParameter("content"));*/

		bDao.updateBoard(title, content, boardNo);
		request.setAttribute("alertMsg", "수정되었습니다.");
		response.sendRedirect(request.getContextPath() + "/board/detail.bo?no=" + boardNo);
	}
	

	/**
	 * 게시글 삭제 (delete문이 아닌 del_yn 컬럼 update)
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void deleteBoard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		bDao.deleteBoard(Integer.parseInt(request.getParameter("no")));
		request.setAttribute("alertMsg", "삭제되었습니다.");
		response.sendRedirect(request.getContextPath() + "/board/list.bo?page=1");
	}

	/**
	 * 검색 (ajax)
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listSearchBoard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String nowPage = request.getParameter("page");
		String keyword = request.getParameter("keyword");
		if (nowPage == null || nowPage.trim().isEmpty())
			nowPage = "1";
		int currentPage = Integer.parseInt(nowPage);
		int listCount = bDao.selectSearchCount(keyword);

		PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
		List<Board> list = bDao.listSearchBoard(page, keyword);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("page", page);

		response.setContentType("application/json; charset=utf-8");
		new Gson().toJson(resultMap, response.getWriter());
	}

	/**
	 * 댓글 조회 (ajax)
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listReply(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int boardNo = Integer.parseInt(request.getParameter("no"));
		List<Reply> list = bDao.listReply(boardNo);
		response.setContentType("application/json; charset=utf-8");
		new Gson().toJson(list, response.getWriter());

	}

	/**
	 * 댓글 등록 (ajax)
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void insertReply(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int result = bDao.insertReply(request.getParameter("content"), request.getParameter("no"));
		response.getWriter().print(result);
	}
}
