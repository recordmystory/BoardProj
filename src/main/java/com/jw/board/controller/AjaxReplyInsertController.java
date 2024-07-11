package com.jw.board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jw.board.model.service.BoardService;
import com.jw.board.model.vo.Reply;

/**
 * Servlet implementation class AjaxReplyInsertController
 */
@WebServlet("/replyinsert.bo")
public class AjaxReplyInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxReplyInsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 댓글 작성할 글 번호와 댓글 내용을 reply 객체에 담아서 service로 이동
		int boardNo = Integer.parseInt(request.getParameter("no"));
		String replyContent = request.getParameter("content");
		
		Reply r = new Reply();
		
		r.setbNo(boardNo);
		r.setContent(replyContent);
		
		int result = new BoardService().insertReply(r);
		
		// ajax 요청이 온곳 : success function으로 데이터 전달
		response.getWriter().print(result);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
