package com.jw.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jw.board.interfaces.ControllerV2;
import com.jw.board.model.service.BoardService;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.MyView;
import com.jw.board.model.vo.PageInfo;
import com.jw.common.util.PagingUtil;

public class BoardListControllerV2 implements ControllerV2 {
	private static final Logger logger = Logger.getLogger(BoardListControllerV2.class);
	private static final BoardService bService = new BoardService();

	@Override
	public MyView process(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		logger.info("BoardListControllerV2 process 메서드 실행");
        try {
            int listCount = bService.selectBoardCount();
            int currentPage;

            try {
                currentPage = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                currentPage = 1;
            }

            PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);

            List<Board> list = bService.selectBoard(page);

            request.setAttribute("page", page);
            request.setAttribute("list", list);

            return new MyView("/WEB-INF/views/board/list.jsp");
        } catch (Exception e) {
            logger.error("기타 오류 발생 : " + e.getMessage());
            request.getSession().setAttribute("alertMsg", "기타 오류가 발생했습니다. 잠시후 다시 시도해주세요.");
            throw e;
        }
	}
}
