package com.jw.board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jw.board.model.service.BoardService;
import com.jw.board.model.vo.Board;

/**
 * Servlet implementation class BoardInsertController
 */
@WebServlet("/insert.bo")
public class BoardInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BoardInsertController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		HttpSession session = request.getSession();
		
		Board b = new Board();
		b.setTitle(title);
		b.setContent(content);
		
		int result = new BoardService().insertBoard(b);

		if (result > 0) {
			session.setAttribute("alertMsg", "게시글 등록이 완료되었습니다.");	
	            response.sendRedirect(request.getContextPath() + "/list.bo?page=1");			
		} else {
			session.setAttribute("alertMsg", "게시글 등록에 실패했습니다.");
			request.getRequestDispatcher("/views/board/list.jsp").forward(request, response);
		}
	}

}
