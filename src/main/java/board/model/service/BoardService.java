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
	 * @return resultMap : 결과 map, list와 page를 담음
	 * @throws NullPointerException 파라미터가 담긴 map에 필요한 key가 없거나 값이 null인 경우 발생
	 * @throws SQLException SQL문 관련한 예외 발생
	 * @throws IllegalArgumentException 잘못된 파라미터가 메소드에 전달될 때 발생
	 * @throws ReflectiveOperationException 리플렉션 동작 중 예외 발생
	 */
	public Map<String, Object> listBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException, ReflectiveOperationException {
		/*		
		 		String nowPage = paramMap.get("page");
				if (nowPage == null || nowPage.trim().isEmpty()) nowPage = "1";
				int currentPage = Integer.parseInt(nowPage);
		*/
		int listCount = bDao.selectBoardCount();

		// getOrDefault() : 찾는 key값이 있으면 그 key값의 value 반환, null이면 defaultValue 반환
		PageInfoVO page = PagingUtil.getPageInfo(listCount, Integer.parseInt(paramMap.getOrDefault("page", "1")), 10, 10); 
		int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
		int endRow = startRow + page.getBoardLimit() - 1;
		List<BoardVO> list = bDao.listBoard(startRow, endRow);
		
		return MapUtil.createResultMap("list", list, "page", page);

	}

	/** 게시글 등록
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map, result에 success | fail이 담기고 해당값으로 등록여부 판별
	 * @throws NullPointerException 파라미터가 담긴 map에 필요한 key가 없거나 값이 null인 경우 발생
	 * @throws SQLException SQL문 관련한 예외 발생
	 * @throws IllegalArgumentException 잘못된 파라미터가 메소드에 전달될 때 발생
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

		int result = bDao.updateExecute("insertBoard", title, content);
		
		return MapUtil.createResultMap("result", result > 0 ? "success" : "fail");
	}

	/** 글 작성 페이지 포워딩
	 * 
	 * @param paramMap : 파라미터가 담긴 map (사용되지 않음)
	 * @return new HashMap : 비어있는 map
	 * @throws Exception 예외 발생 시 발생한 예외를 Controller에서 처리
	 */
	public Map<String, Object> registBoard(Map<String, String> paramMap) throws Exception {
		return new HashMap<>();
	}

	/** 게시글 상세 조회
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map, BoardVO 객체를 담음
	 * @throws NullPointerException 파라미터가 담긴 map에 필요한 key가 없거나 값이 null인 경우 발생
	 * @throws SQLException SQL문 관련한 예외 발생
	 * @throws IllegalArgumentException 잘못된 파라미터가 메소드에 전달될 때 발생
	 * @throws ReflectiveOperationException 리플렉션 동작 중 예외 발생
	 */
	public Map<String, Object> selectDetailBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException, ReflectiveOperationException {
		BoardVO b = bDao.selectDetailBoard(Integer.parseInt(paramMap.get("no")));
//		bDao.updateExecute("updateHit", boardNo);
		
		return MapUtil.createResultMap("b", b);
	}

	/** 게시글 수정 폼 조회
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map, BoardVO 객체를 담음
	 * @throws NullPointerException 파라미터가 담긴 map에 필요한 key가 없거나 값이 null인 경우 발생
	 * @throws SQLException SQL문 관련한 예외 발생
	 * @throws IllegalArgumentException 잘못된 파라미터가 메소드에 전달될 때 발생
	 * @throws ReflectiveOperationException 리플렉션 동작 중 예외 발생
	 */
	public Map<String, Object> updateFormBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException, ReflectiveOperationException {
		BoardVO b = bDao.selectDetailBoard(Integer.parseInt(paramMap.get("no")));
		
		return MapUtil.createResultMap("b", b);
	}

	/** 게시글 수정
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map, result에 success | fail이 담기고 해당 값으로 수정여부 판별
	 * @throws NullPointerException 파라미터가 담긴 map에 필요한 key가 없거나 값이 null인 경우 발생
	 * @throws SQLException SQL문 관련한 예외 발생
	 * @throws IllegalArgumentException 잘못된 파라미터가 메소드에 전달될 때 발생
	 */
	public Map<String, Object> updateBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException {
		int result = bDao.updateExecute("updateBoard"
										, paramMap.get("title")
										, paramMap.get("content")
										, Integer.parseInt(paramMap.get("no")) );
		
		return MapUtil.createResultMap("result", result > 0 ? "success" : "fail");
	}
	
	/** 게시글 삭제 (삭제여부 컬럼 N에서 Y으로 update)
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map, result에 success | fail이 담기고 해당 값으로 삭제여부 판별
	 * @throws NullPointerException 파라미터가 담긴 map에 필요한 key가 없거나 값이 null인 경우 발생
	 * @throws SQLException SQL문 관련한 예외 발생
	 * @throws IllegalArgumentException 잘못된 파라미터가 메소드에 전달될 때 발생
	 */
	public Map<String, Object> deleteBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException {
		int result = bDao.updateExecute("deleteBoard", Integer.parseInt(paramMap.get("no")));
		
		return MapUtil.createResultMap("result", result > 0 ? "success" : "fail");
	}
	
	/** 검색 (AJAX)
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map, list, page, keyword(검색어)를 담음
	 * @throws NullPointerException 파라미터가 담긴 map에 필요한 key가 없거나 값이 null인 경우 발생
	 * @throws SQLException SQL문 관련한 예외 발생
	 * @throws IllegalArgumentException 잘못된 파라미터가 메소드에 전달될 때 발생
	 * @throws ReflectiveOperationException 리플렉션 동작 중 예외 발생
	 */
	public Map<String, Object> listSearchBoard(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException, ReflectiveOperationException {
		String keyword = paramMap.get("keyword");
		int listCount = bDao.selectSearchCount(keyword);

		PageInfoVO page = PagingUtil.getPageInfo(listCount, Integer.parseInt(paramMap.getOrDefault("page", "1")), 10, 10);
		int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
		int endRow = startRow + page.getBoardLimit() - 1;
		List<BoardVO> list = bDao.listSearchBoard(keyword, startRow, endRow);
		
		return MapUtil.createResultMap("list", list, "page", page, "keyword", keyword);
	}
	
}
