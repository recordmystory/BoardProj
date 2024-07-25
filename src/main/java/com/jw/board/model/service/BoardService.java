package com.jw.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jw.board.model.dao.BoardDao;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.PageInfo;
import com.jw.common.util.PagingUtil;

public class BoardService {
//	private static final Logger logger = Logger.getLogger(BoardService.class);

	private static BoardDao bDao = new BoardDao();

	/** 게시글 목록 조회 및 페이징
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> listBoard(Map<String, String> paramMap) throws Exception {
		String nowPage = paramMap.get("page");
		if (nowPage == null || nowPage.trim().isEmpty()) nowPage = "1";
		int currentPage = Integer.parseInt(nowPage);
		int listCount = bDao.selectBoardCount();

		PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
		int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
		int endRow = startRow + page.getBoardLimit() - 1;
		List<Board> list = bDao.listBoard(startRow, endRow);
//		logger.info("list : " + list);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("page", page);
		
		return resultMap;
//		request.getRequestDispatcher("/views/board/list.jsp").forward(request, response);
	}

	/** 게시글 등록
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> insertBoard(Map<String, String> paramMap) throws Exception {
		String title = paramMap.get("title");
		String content = paramMap.get("content");
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("제목을 입력해주세요.");
		}

		if (content == null || content.trim().isEmpty()) {
			throw new IllegalArgumentException("내용을 입력해주세요.");
		}

		Board b = new Board();
		b.setTitle(title);
		b.setContent(content);

		int result = bDao.dmlQuery("insertBoard", title, content);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", result > 0 ? "success" : "fail");
		resultMap.put("msg", result > 0 ? "success" : "fail");
		resultMap.put("data", result);
		
		return resultMap;
//		return "/mav/board/list.bo?page=1";
		
		/*
		 * request.setAttribute("alertMsg", "등록 되었습니다.");
		 * response.sendRedirect("/board/list.bo?page=1");
		 */
	}

	/** 글 작성 페이지 포워딩
	 * @param paramMap
	 * @return 
	 * @throws Exception
	 */
	public Map<String, Object> registBoard(Map<String, String> paramMap) throws Exception {
		return new HashMap<>();
//		request.getRequestDispatcher("/views/board/registForm.jsp").forward(request, response);
	}

	/** 게시글 상세 조회
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> detailBoard(Map<String, String> paramMap) throws Exception {
		int boardNo = Integer.parseInt(paramMap.get("no"));
//		logger.info("boardNo : " + boardNo);
		Board b = bDao.detailBoard(boardNo);
		bDao.dmlQuery("updateHit", boardNo);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("b", b);
		
		
		
		return resultMap;
		/*request.setAttribute("b", b);
		request.getRequestDispatcher("/views/board/detail.jsp").forward(request, response);*/
	}

	/** 게시글 수정
	 * 
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> updateFormBoard(Map<String, String> paramMap) throws Exception {
		Board b = bDao.detailBoard(Integer.parseInt(paramMap.get("no")));
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("b", b);
		
		return resultMap;
//		request.getRequestDispatcher("/views/board/updateForm.jsp").forward(request, response);
	}

	/** 게시글 수정
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> updateBoard(Map<String, String> paramMap) throws Exception {
		int boardNo = Integer.parseInt(paramMap.get("no"));
		String title = paramMap.get("title");
		String content = paramMap.get("content");
		
		/*		Board b = new Board();
				b.setNo(boardNo);
				b.setTitle(request.getParameter("title"));
				b.setContent(request.getParameter("content"));*/

		int result = bDao.dmlQuery("updateBoard", title, content, boardNo);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", result > 0 ? "success" : "fail");
		
		return resultMap;
		/*request.setAttribute("alertMsg", "수정되었습니다.");
		response.sendRedirect(request.getContextPath() + "/board/detail.bo?no=" + boardNo);*/
	}
	
	/** 게시글 삭제 (delete문이 아닌 del_yn 컬럼 update)
	 * 
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> deleteBoard(Map<String, String> paramMap) throws Exception {
		int result = bDao.dmlQuery("deleteBoard", Integer.parseInt(paramMap.get("no")));
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", result > 0 ? "success" : "fail");
		
		return resultMap;
		
//		return "/mav/board/list.bo?page=1";
//		request.setAttribute("alertMsg", "삭제되었습니다.");
//		response.sendRedirect(request.getContextPath() + "/board/list.bo?page=1");
	}

	/** 검색 (ajax)
	 * 
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> listSearchBoard(Map<String, String> paramMap) throws Exception {
		String nowPage = paramMap.get("page");
		String keyword = paramMap.get("keyword");
		if (nowPage == null || nowPage.trim().isEmpty())
			nowPage = "1";
		int currentPage = Integer.parseInt(nowPage);
		int listCount = bDao.selectSearchCount(keyword);

		PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
		int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
		int endRow = startRow + page.getBoardLimit() - 1;
		List<Board> list = bDao.listSearchBoard(keyword, startRow, endRow);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("page", page);
//		resultMap.put("ajaxData", new Gson().toJson(resultMap));
		
		return resultMap;
//		response.setContentType("application/json; charset=utf-8");
//		return new Gson().toJson(resultMap);
	}

	/** 댓글 조회 (ajax)
	 * 
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	/*public Map<String, Object> listReply(Map<String, String> paramMap) throws Exception {
		int boardNo = Integer.parseInt(paramMap.get("no"));
		List<Reply> list = bDao.listReply(boardNo);
	
		Map<String, Object> resultMap = new HashMap<>();
	    resultMap.put("list", list);
	//		resultMap.put("ajaxData", new Gson().toJson(list));
		
		return resultMap;
	//		response.setContentType("application/json; charset=utf-8");
	//		return new Gson().toJson(list);
	}*/

	/** 댓글 등록 (ajax)
	 * 
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	/*public Map<String, Object> insertReply(Map<String, String> paramMap) throws Exception {
		int result = bDao.executeUpdate("insertReply", paramMap.get("content"), Integer.parseInt(paramMap.get("no")));
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("flag", result > 0 ? "success" : "fail");
	    return resultMap;
	}*/
}
