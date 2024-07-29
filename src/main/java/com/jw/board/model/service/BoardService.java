package com.jw.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jw.board.model.dao.BoardDao;
import com.jw.board.model.vo.BoardVO;
import com.jw.board.model.vo.PageInfoVO;
import com.jw.common.util.PagingUtil;

public class BoardService {
	private static final BoardDao bDao = new BoardDao();

	/** 게시글 목록 조회 및 페이징
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception
	 */
	public Map<String, Object> listBoard(Map<String, String> paramMap) throws Exception {
		String nowPage = paramMap.get("page");
		if (nowPage == null || nowPage.trim().isEmpty()) nowPage = "1";
		int currentPage = Integer.parseInt(nowPage);
		int listCount = bDao.selectBoardCount();

		PageInfoVO page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
		int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
		int endRow = startRow + page.getBoardLimit() - 1;
		List<BoardVO> list = bDao.listBoard(startRow, endRow);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("page", page);
		
		return resultMap;
	}

	/** 게시글 등록
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
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

		BoardVO b = new BoardVO();
		b.setTitle(title);
		b.setContent(content);

		int result = bDao.updateExecute("insertBoard", title, content);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", result > 0 ? "success" : "fail");
		resultMap.put("msg", result > 0 ? "success" : "fail");
		resultMap.put("data", result);
		
		return resultMap;
	}

	/** 글 작성 페이지 포워딩
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> registBoard(Map<String, String> paramMap) throws Exception {
		return new HashMap<>();
	}

	/** 게시글 상세 조회
	 * 
	 *@param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception
	 */
	public Map<String, Object> detailBoard(Map<String, String> paramMap) throws Exception {
		int boardNo = Integer.parseInt(paramMap.get("no"));
		BoardVO b = bDao.detailBoard(boardNo);
		bDao.updateExecute("updateHit", boardNo);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("b", b);
		
		
		
		return resultMap;
	}

	/** 게시글 수정
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception
	 */
	public Map<String, Object> updateFormBoard(Map<String, String> paramMap) throws Exception {
		BoardVO b = bDao.detailBoard(Integer.parseInt(paramMap.get("no")));
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("b", b);
		
		return resultMap;
	}

	/** 게시글 수정
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception
	 */
	public Map<String, Object> updateBoard(Map<String, String> paramMap) throws Exception {
		int result = bDao.updateExecute("updateBoard"
									, paramMap.get("title")
									, paramMap.get("content")
									, Integer.parseInt(paramMap.get("no")) );
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", result > 0 ? "success" : "fail");
		
		return resultMap;
	}
	
	/** 게시글 삭제 (delete문이 아닌 del_yn 컬럼 update)
	 * 
	 *@param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception
	 */
	public Map<String, Object> deleteBoard(Map<String, String> paramMap) throws Exception {
		int result = bDao.updateExecute("deleteBoard", Integer.parseInt(paramMap.get("no")));
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", result > 0 ? "success" : "fail");
		
		return resultMap;
		
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

		PageInfoVO page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
		int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
		int endRow = startRow + page.getBoardLimit() - 1;
		List<BoardVO> list = bDao.listSearchBoard(keyword, startRow, endRow);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("page", page);
		return resultMap;
		
	}
}
