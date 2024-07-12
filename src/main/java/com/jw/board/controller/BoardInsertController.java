package com.jw.board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.jw.board.model.service.BoardService;
import com.jw.board.model.vo.Board;

/**
 * Servlet implementation class BoardInsertController
 */
@WebServlet("/insert.bo")
public class BoardInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BoardInsertController.class);

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

}
