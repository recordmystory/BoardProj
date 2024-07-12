package com.jw.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.jw.board.interfaces.ControllerV2;
import com.jw.board.model.service.BoardService;
import com.jw.board.model.vo.MyView;

public class BoardDeleteControllerV2 implements ControllerV2 {
	private static final Logger logger = Logger.getLogger(BoardDeleteControllerV2.class);
	private static final BoardService bService = new BoardService();
	
	@Override
	public MyView process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		try {
			int boardNo = Integer.parseInt(request.getParameter("no"));

			int result = new BoardService().updateDelYn(boardNo);
			
			if (result > 0) {
				session.setAttribute("alertMsg", "성공적으로 삭제되었습니다.");
			} else {
				session.setAttribute("alertMsg", "삭제에 실패했습니다.");
			}

		} catch (NumberFormatException e) {
			logger.error("NumberFormatException 발생 : " + e.getMessage());
			session.setAttribute("alertMsg", "게시글 번호가 올바르지 않습니다.");
		} catch (Exception e) {
			logger.error("기타 오류 발생 : " + e.getMessage());
			
			session.setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
		}
		
		if (!response.isCommitted()) {
	        response.sendRedirect(request.getContextPath() + "/list.do?page=1");
	    }

	    // 이후의 코드는 실행되지 않음
	    return null;
	}

}
