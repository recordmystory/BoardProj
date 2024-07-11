package com.jw.board.model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import org.apache.log4j.Logger;

import com.jw.board.controller.BoardListController;
import com.jw.board.model.dao.BoardDao;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.PageInfo;
import com.jw.board.model.vo.Reply;
import com.jw.common.template.JDBCTemplate;

import static com.jw.common.template.JDBCTemplate.*;

public class BoardService {

	private BoardDao bDao = new BoardDao();
	Logger logger = Logger.getLogger(BoardService.class);
	
	/**
	 * 게시글 목록 조회 및 페이징
	 * 
	 * @param page
	 * 
	 * @return list
	 */

	public List<Board> selectBoard(PageInfo page) {
		List<Board> list = bDao.selectBoard(page);

		return list;
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
	 * @return
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
	public Board selectBoardDetail(int boardNo) {
		Board b = bDao.selectBoardDetail(boardNo);
		
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
	 * 게시글 삭제 (delete가 아닌 del_yn 컬럼 업데이트)
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
	 * @return list
	 */
	public List<Board> selectSearch(String keyword) {
		List<Board> list = bDao.selectSearch(keyword);

		return list;
	}

	/**
	 * 댓글 조회 (ajax)
	 * 
	 * @param boardNo
	 * @return list
	 */
	public List<Reply> selectReply(int boardNo) {
		List<Reply> list = bDao.selectReply(boardNo);

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
