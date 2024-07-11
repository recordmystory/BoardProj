package com.jw.board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
    public BoardListController() {
		this.pagingUtil = new PagingUtil();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		
			int listCount; // 현재 게시글의 총 개수
			int currentPage; // 현재 페이지 
			int pageLimit; // 페이징바의 페이지 최대 개수
			int boardLimit; // 한 페이지에 보여질 게시글 최대 개수 
			int maxPage; // 가장 마지막 페이지 (총 페이지수) : listCount와 boardLimit으로부터 영향을 받음
			int startPage; // 페이징바의 시작수
			int endPage; // 페이징바의 끝수
			
			listCount = new BoardService().selectBoardCount();
			
			currentPage = Integer.parseInt(request.getParameter("page"));
			
			pageLimit = 5;
			
			boardLimit = 10;
			
			maxPage = (int) Math.ceil((double) listCount / boardLimit);
			
			startPage = (currentPage - 1) / pageLimit * pageLimit + 1;
			
			endPage = startPage + pageLimit - 1;
			
			if(endPage > maxPage) {
				endPage = maxPage;
			}
			
			
			PageInfo page = new PageInfo(listCount, currentPage, pageLimit, boardLimit, maxPage, startPage, endPage);
		
		*/
		
		int listCount = new BoardService().selectBoardCount();
		int currentPage = Integer.parseInt(request.getParameter("page"));
		PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
		
		List<Board> list = new BoardService().selectBoard(page);
		
		request.setAttribute("page", page);
		request.setAttribute("list", list);
		request.getRequestDispatcher("/views/board/list.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
