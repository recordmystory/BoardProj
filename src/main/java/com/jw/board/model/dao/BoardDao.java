package com.jw.board.model.dao;

import static com.jw.common.template.JDBCTemplate.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.jw.board.model.vo.BoardVO;

/**
 * 게시판 DAO
 */
public class BoardDao extends BaseDao {
	
	/** 게시글 목록 조회 및 페이징 
	 * 
	 * @param params : 페이지 시작수, 페이지 끝수
	 * @return list : 조회 결과가 담긴 list
	 * @throws NullPointerException : 파라미터가 null일 경우, rset이 null일 경우, selectExecute 반환값이 null일 경우 발생 
	 * @throws IllegalArgumentException : 부적절한 파라미터가 전달된 경우 발생
	 * @throws SQLException : SQL문 실행 중 발생
	 */
	public List<BoardVO> listBoard(Object... params) throws NullPointerException, IllegalArgumentException, SQLException {
		return selectExecute("listBoard", rset -> {
			List<BoardVO> list = new ArrayList<>();
	        while (rset.next()) {
	            BoardVO b = new BoardVO();
	            b.setNo(rset.getInt("NO"));
	            b.setTitle(rset.getString("TITLE"));
	            b.setContent(rset.getString("CONTENT"));
	            b.setHit(rset.getInt("HIT"));
	            b.setRegId(rset.getString("REGID"));
	            b.setRegDate(rset.getDate("REGDATE"));
	            list.add(b);
	        }
	        return list;
		}, params);
	}
	
	
	/** 게시글 목록 조회 및 페이징 
	 * 
	 * @return listCount : 게시글의 총 개수
	 * @throws SQLException : SQL문 실행 중 발생 
	 * @throws IllegalArgumentException : 부적절한 파라미터가 전달된 경우 발생
	 * @throws NullPointerException : 파라미터가 null일 경우, rset이 null일 경우, selectExecute 반환값이 null일 경우 발생
	 */
	public int selectBoardCount() throws NullPointerException, IllegalArgumentException, SQLException {
		return selectExecute("selectBoardCount", rset -> {
			int listCount = 0;
			if(rset.next()) {
				listCount = rset.getInt("COUNT");
			}
			return listCount;
		});
	}
	
	/** 게시글 상세 조회
	 * 
	 * @param params : 글 번호
	 * @return board : 조회 결과가 담긴 BoardVO 객체
	 * @throws NullPointerException : 파라미터가 null일 경우, rset이 null일 경우, selectExecute 반환값이 null일 경우 발생
	 * @throws IllegalArgumentException : 부적절한 파라미터가 전달된 경우 발생
	 * @throws SQLException : SQL문 실행 중 발생 
	 */
	public BoardVO selectDetailBoard(Object... params) throws NullPointerException, IllegalArgumentException, SQLException {
		Connection conn = getConnection();
		
		BoardVO board = null;
		try {			
			board = selectExecute("detailBoard", rset -> {	
				BoardVO b = null;
				if (rset.next()) {
					b = new BoardVO();
					b.setNo(rset.getInt("NO"));
					b.setTitle(rset.getString("TITLE"));
					b.setContent(rset.getString("CONTENT"));
					b.setHit(rset.getInt("HIT"));
					b.setRegDate(rset.getDate("REGDATE"));
					b.setModDate(rset.getDate("MODDATE"));
				}
				return b;
			}, params);
			
			updateExecute("updateHit", params);
			commit(conn);
		} catch (SQLException e) {
			if (conn != null) rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return board;
		
	}
	
	/** 검색된 게시글 조회 (AJAX)
	 * 
	 * @param params : 검색어, 페이지 시작수, 페이지 끝수 
	 * @return list : 검색결과가 담긴 list
	 * @throws NullPointerException : 파라미터가 null일 경우, rset이 null일 경우, selectExecute 반환값이 null일 경우 발생
	 * @throws IllegalArgumentException : 부적절한 파라미터가 전달된 경우 발생
	 * @throws SQLException : SQL문 실행 중 발생 
	 */
	public List<BoardVO> listSearchBoard(Object... params) throws NullPointerException, IllegalArgumentException, SQLException {
		return selectExecute("listSearchBoard", rset -> {
			List<BoardVO> list = new ArrayList<>();
			while(rset.next()) {
				BoardVO b = new BoardVO();
				b.setNo(rset.getInt("NO"));
				b.setTitle(rset.getString("TITLE"));
				b.setContent(rset.getString("CONTENT"));
				b.setHit(rset.getInt("HIT"));
				b.setRegId(rset.getString("REGID"));
				b.setRegDate(rset.getDate("REGDATE"));
				
				list.add(b);
			}
			return list;
		}, params);
		
	}
	
	/** 검색된 게시물 개수 조회
	 * 
	 * @param params : 검색어
	 * @return listCount : 게시글 총 개수
	 * @throws NullPointerException : : 파라미터가 null일 경우, rset이 null일 경우, selectExecute 반환값이 null일 경우 발생
	 * @throws IllegalArgumentException : 부적절한 파라미터가 전달된 경우 발생
	 * @throws SQLException : SQL문 실행 중 발생 
	 */
	public int selectSearchCount(Object... params) throws NullPointerException, IllegalArgumentException, SQLException {
		return selectExecute("selectSearchCount", rset -> {
			int listCount = 0;
			if(rset.next()) {
				listCount = rset.getInt("COUNT");
			}
			return listCount;
		}, params);
	}
}
