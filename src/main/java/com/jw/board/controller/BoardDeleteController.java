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

/**
 * Servlet implementation class BoardDeleteController
 */
@WebServlet("/delete.bo")
public class BoardDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BoardDeleteController.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
