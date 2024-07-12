package com.jw.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.jw.board.interfaces.ControllerV2;
import com.jw.board.model.service.BoardService;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.MyView;

public class BoardDetailControllerV2 implements ControllerV2 {
	private static final Logger logger = Logger.getLogger(BoardDetailControllerV2.class);
	@Override
	public MyView process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

	    try {
	        int boardNo = Integer.parseInt(request.getParameter("no"));

	        BoardService bService = new BoardService();

	        int result = bService.updateHit(boardNo);

	        if (result > 0) {
	            Board b = bService.selectBoardDetail(boardNo);

	            if (b != null) {
	                request.setAttribute("b", b);
	                return new MyView("/WEB-INF/views/board/detail.jsp");
	            } else {
	                session.setAttribute("alertMsg", "해당 게시글을 찾을 수 없습니다.");
	                response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
	                return null;
	            }
	        } else {
	            session.setAttribute("alertMsg", "게시글 조회에 실패했습니다.");
	            response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
	            return null;
	        }

	    } catch (NumberFormatException e) {
	        session.setAttribute("alertMsg", "올바른 게시글 번호가 아닙니다.");
	        response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
	        throw e; 
	    } catch (Exception e) {
	        logger.error("기타 오류 발생 : " + e.getMessage());
	        session.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
	        response.sendRedirect(request.getContextPath() + "/list.bo?page=1");
	        throw e;
		}
	}
}
