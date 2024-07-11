package com.jw.board.model.service;

import java.sql.Connection;
import java.util.List;

import com.jw.board.model.dao.BoardDao;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.PageInfo;
import com.jw.board.model.vo.Reply;
import com.jw.common.template.JDBCTemplate;

import static com.jw.common.template.JDBCTemplate.*;

public class BoardService {

	private BoardDao bDao = new BoardDao();

	/**
	 * 게시글 목록 조회 및 페이징
	 * 
	 * @param page
	 * 
	 * @return list
	 */

	public List<Board> selectBoard(PageInfo page) {
		Connection conn = getConnection(true);
		List<Board> list = bDao.selectBoard(conn, page);

		close(conn);

		return list;
	}

	/**
	 * 게시글 목록 조회 및 페이징
	 * 
	 * @return listCount : 총 게시글 개수
	 */
	public int selectBoardCount() {
		Connection conn = getConnection(true);

		int listCount = bDao.selectBoardCount(conn);
		close(conn);

		return listCount;
	}

	/**
	 * 게시글 등록
	 * 
	 * @param b
	 * @return result
	 */
	public int insertBoard(Board b) {
		Connection conn = getConnection();
		int result = bDao.insertBoard(conn, b);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);

		return result;
	}

	/**
	 * 조회수 증가
	 * 
	 * @param boardNo
	 * @return
	 */
	public int updateHit(int boardNo) {
		Connection conn = getConnection();
		int result = bDao.updateHit(conn, boardNo);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);

		return result;
	}

	/**
	 * 게시글 상세 조회
	 * 
	 * @param boardNo
	 * @return b
	 */
	public Board selectBoardDetail(int boardNo) {
		Connection conn = getConnection(true);
		Board b = bDao.selectBoardDetail(conn, boardNo);
		close(conn);
		return b;
	}

	/**
	 * 게시글 수정
	 * 
	 * @param b
	 * @return result
	 */
	public int updateBoard(Board b) {
		Connection conn = getConnection();
		int result = bDao.updateBoard(conn, b);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);

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
		Connection conn = getConnection();
		int result = bDao.updateDelYn(conn, boardNo);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);

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
		Connection conn = getConnection();
		List<Board> list = bDao.selectSearch(conn, keyword);
		close(conn);

		return list;
	}

	/**
	 * 댓글 조회 (ajax)
	 * 
	 * @param boardNo
	 * @return list
	 */
	public List<Reply> selectReply(int boardNo) {
		Connection conn = getConnection(true);
		List<Reply> list = bDao.selectReply(conn, boardNo);
		close(conn);

		return list;
	}

	/**
	 * 댓글 등록 (ajax)
	 * 
	 * @param r
	 * @return result
	 */
	public int insertReply(Reply r) {
		Connection conn = getConnection();

		int result = bDao.insertReply(conn, r);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		return result;
	}

}
