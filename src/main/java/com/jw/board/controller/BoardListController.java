package com.jw.board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jw.board.model.service.BoardService;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.PageInfo;
import com.jw.common.util.PagingUtil;

/**
 * Servlet implementation class BoardListController
 */
@WebServlet("/list.bo")
public class BoardListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final PagingUtil pagingUtil;
	private static final Logger logger = Logger.getLogger(BoardListController.class);
	
    public BoardListController() {
		this.pagingUtil = new PagingUtil();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
