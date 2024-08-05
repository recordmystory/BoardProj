package board.model.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.model.dao.BoardDao;
import board.model.vo.BoardVO;
import board.model.vo.PageInfoVO;
import common.util.MapUtil;
import common.util.PagingUtil;

/**
 * 게시판 Service
 */
public class BoardService {
	private static final BoardDao bDao = new BoardDao();

	/** 게시글 목록 조회 및 페이징
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception 예외 발생 시 발생한 예외를 Controller에서 처리
	 */
	public Map<String, Object> listBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException {
		String nowPage = paramMap.get("page");
		if (nowPage == null || nowPage.trim().isEmpty()) nowPage = "1";
		int currentPage = Integer.parseInt(nowPage);
		int listCount = bDao.selectBoardCount();

		PageInfoVO page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
		int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
		int endRow = startRow + page.getBoardLimit() - 1;
		List<BoardVO> list = bDao.listBoard(startRow, endRow);
		
		return MapUtil.createResultMap("list", list, "page", page);

	}

	/** 게시글 등록
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception 예외 발생 시 발생한 예외를 Controller에서 처리
	 */
	public Map<String, Object> insertBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException {
		String title = paramMap.get("title");
		String content = paramMap.get("content");
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("제목을 입력해주세요.");
		}

		if (content == null || content.trim().isEmpty()) {
			throw new IllegalArgumentException("내용을 입력해주세요.");
		}
		/*BoardVO b = new BoardVO();
		b.setTitle(title);
		b.setContent(content);*/

		int result = bDao.updateExecute("insertBoard", title, content);
		
		return MapUtil.createResultMap("result", result > 0 ? "success" : "fail");
	}

	/** 글 작성 페이지 포워딩
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return new HashMap
	 * @throws Exception 예외 발생 시 발생한 예외를 Controller에서 처리
	 */
	public Map<String, Object> registBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException {
		return new HashMap<>();
	}

	/** 게시글 상세 조회
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception 예외 발생 시 발생한 예외를 Controller에서 처리
	 */
	public Map<String, Object> selectDetailBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException {
		BoardVO b = bDao.selectDetailBoard(Integer.parseInt(paramMap.get("no")));
//		bDao.updateExecute("updateHit", boardNo);
		
		return MapUtil.createResultMap("b", b);
	}

	/** 게시글 수정
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception 예외 발생 시 발생한 예외를 Controller에서 처리
	 */
	public Map<String, Object> updateFormBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException {
		BoardVO b = bDao.selectDetailBoard(Integer.parseInt(paramMap.get("no")));
		
		return MapUtil.createResultMap("b", b);
	}

	/** 게시글 수정
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception 예외 발생 시 발생한 예외를 Controller에서 처리
	 */
	public Map<String, Object> updateBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException {
		int result = bDao.updateExecute("updateBoard"
									, paramMap.get("title")
									, paramMap.get("content")
									, Integer.parseInt(paramMap.get("no")) );
		
		return MapUtil.createResultMap("result", result > 0 ? "success" : "fail");
	}
	
	/** 게시글 삭제 (delete문이 아닌 del_yn 컬럼 update)
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception 예외 발생 시 발생한 예외를 Controller에서 처리
	 */
	public Map<String, Object> deleteBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException {
		int result = bDao.updateExecute("deleteBoard", Integer.parseInt(paramMap.get("no")));
		
		return MapUtil.createResultMap("result", result > 0 ? "success" : "fail");
	}
	
	/** 검색 (ajax)
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception 예외 발생 시 발생한 예외를 Controller에서 처리
	 */
	public Map<String, Object> listSearchBoard(Map<String, String> paramMap) throws NullPointerException, IllegalArgumentException, SQLException {
		String currentPage = paramMap.get("page");
		String keyword = paramMap.get("keyword");
		if (currentPage == null || currentPage.trim().isEmpty()) currentPage = "1";
		
		int listCount = bDao.selectSearchCount(keyword);

		PageInfoVO page = PagingUtil.getPageInfo(listCount, Integer.parseInt(currentPage), 10, 10);
		int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
		int endRow = startRow + page.getBoardLimit() - 1;
		List<BoardVO> list = bDao.listSearchBoard(keyword, startRow, endRow);
		
		return MapUtil.createResultMap("list", list, "page", page, "keyword", keyword);
	}
	
}
