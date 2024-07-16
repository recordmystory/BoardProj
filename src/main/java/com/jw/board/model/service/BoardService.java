package com.jw.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jw.board.model.dao.BoardDao;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.PageInfo;
import com.jw.board.model.vo.Reply;
import com.jw.common.util.PagingUtil;


public class BoardService {

	private BoardDao bDao = new BoardDao();

	/**
	 * 게시글 목록 조회 및 페이징
	 * 
	 * @param page
	 * 
	 * @return resultMap
	 */

	public Map<String, Object> listBoard(int currentPage) {
		int listCount = selectBoardCount();
		PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
		
		List<Board> list = bDao.listBoard(page);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("page", page);

		return resultMap;
	}

	/**
	 * 게시글 목록 조회 및 페이징
	 * 
	 * @return listCount : 총 게시글 개수
	 */
	public int selectBoardCount() {
		int listCount = bDao.selectBoardCount();

		return listCount;
	}

	/**
	 * 게시글 등록
	 * 
	 * @param b
	 * @return result
	 */
	public int insertBoard(Board b) {
		int result = bDao.insertBoard(b);

		return result;
	}

	/**
	 * 조회수 증가
	 * 
	 * @param boardNo
	 * @return result
	 */

	public int updateHit(int boardNo) {
		int result = bDao.updateHit(boardNo);

		return result;
	}

	/**
	 * 게시글 상세 조회
	 * 
	 * @param boardNo
	 * @return b
	 */
	public Board selectBoardDtl(int boardNo) {
		Board b = bDao.selectBoardDtl(boardNo);

		return b;
	}

	/**
	 * 게시글 수정
	 * 
	 * @param b
	 * @return result
	 */
	public int updateBoard(Board b) {
		int result = bDao.updateBoard(b);

		return result;
	}

	/**
	 * 게시글 삭제 (delete문이 아닌 del_yn 컬럼 update)
	 * 
	 * @param boardNo
	 * 
	 * @return result
	 */
	public int updateDelYn(int boardNo) {
		int result = bDao.updateDelYn(boardNo);

		return result;
	}

	/**
	 * 검색 (ajax)
	 * 
	 * @param keyword
	 * 
	 * @return resultMap
	 */
	public Map<String, Object> listSearch(int currentPage, String keyword) {
		int listCount = selectSearchCount(keyword);
		PageInfo page = PagingUtil.getPageInfo(listCount, currentPage, 10, 10);
		
		List<Board> list = bDao.listSearch(page, keyword);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", list);
		resultMap.put("page", page);
		
		return resultMap;
	}
	
	public int selectSearchCount(String keyword) {
		int result = bDao.selectSearchCount(keyword);
		
		return result;
	}
	

	/**
	 * 댓글 조회 (ajax)
	 * 
	 * @param boardNo
	 * @return list
	 */
	public List<Reply> listReply(int boardNo) {
		List<Reply> list = bDao.listReply(boardNo);

		return list;
	}

	/**
	 * 댓글 등록 (ajax)
	 * 
	 * @param r
	 * @return result
	 */
	public int insertReply(Reply r) {
		int result = bDao.insertReply(r);

		return result;
	}



}
