package com.jw.board.model.dao;

import static com.jw.common.template.JDBCTemplate.close;
import static com.jw.common.template.JDBCTemplate.commit;
import static com.jw.common.template.JDBCTemplate.getConnection;
import static com.jw.common.template.JDBCTemplate.rollback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.PageInfo;
import com.jw.board.model.vo.Reply;
import com.jw.common.util.ConfigUtil;

public class BoardDao {
	private Properties prop;
	private static final Logger logger = Logger.getLogger(BoardDao.class);

	public BoardDao() { this.prop = ConfigUtil.getInstance().getProperties(); }
	
	/*	static { // 클래스 최초 로드 시만 getProperties() 메서드 호출
			prop = BoardController.getProperties();
		}*/
	
	/*	public static Properties getProperties() {
	    return prop;
	}*/
	
	
	/** SQL 실행 (INSERT, UPDATE, DELETE문)
	 * 
	 * @param sql : 실행할 쿼리 key
	 * @param params : 쿼리 파라미터
	 * @return result : 업데이트된 행 개수
	 */
	public int executeUpdate(String sql, Object... params) {
		
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		int result = 0;
		try {

	        pstmt = conn.prepareStatement(sql);
	        
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}

			result = pstmt.executeUpdate();

			if (result == 0) {
				logger.debug("result : 0 (insert || update || delete 된 행의 개수 0)");
			} else if (result == 1) {
				logger.debug("result : 1 (insert || update || delete 성공 ==> 글 등록)");
			}

			commit(conn);
		} catch (SQLException | IllegalArgumentException e) {
			logger.error(e.getClass().getName() + "발생 : " + e.getMessage());
			rollback(conn);
		} finally {
			close(pstmt);
			close(conn);
		}

		return result;
	}

	/** 게시글 목록 조회 및 페이징 
	 * 
	 * @param page
	 * @return list
	 */
	public List<Board> listBoard(Object... params) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Board> list = new ArrayList<>();
		
		String sql = prop.getProperty("listBoard");

		try {
			pstmt = conn.prepareStatement(sql);

//			int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
//			int endRow = startRow + page.getBoardLimit() - 1;
//			pstmt.setInt(1, startRow);
//			pstmt.setInt(2, endRow);
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			rset = pstmt.executeQuery();

			while (rset.next()) {
				
				Board b = new Board();
				
				b.setNo(rset.getInt("NO"));
				b.setTitle(rset.getString("TITLE"));
				b.setContent(rset.getString("CONTENT"));
				b.setHit(rset.getInt("HIT"));
				b.setRegId(rset.getString("REGID"));
				b.setRegDate(rset.getDate("REGDATE"));
				list.add(b);
			}
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " + e.getMessage());
		} finally {
			close(rset);
			close(pstmt);
			close(conn);
		}

		return list;
	}
	
	/** 게시글 목록 조회 및 페이징 
	 * 
	 * @return listCount
	 */
	public int selectBoardCount() {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		int listCount = 0;

		String sql = prop.getProperty("selectBoardCount");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();

			if (rset.next()) {
				listCount = rset.getInt("COUNT");
			}
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " + e.getMessage());
		} finally {
			close(rset);
			close(pstmt);
			close(conn);
		}
		
		return listCount;
	}
	
	/** 게시글 상세 조회
	 * 
	 * @param conn
	 * @param boardNo
	 * @return b
	 */
	public Board detailBoard(int boardNo) {
		Connection conn = getConnection(true);
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Board b = new Board();
		String sql = prop.getProperty("detailBoard");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);

			rset = pstmt.executeQuery();

			if (rset.next()) {
//				b = Board.builder()
//						 .no(rset.getInt("B_NO"))
//						 .title(rset.getString("B_TITLE"))
//						 .content(rset.getString("B_CONTENT"))
//						 .hit(rset.getInt("B_HIT"))
//						 .regDate(rset.getDate("REG_DATE"))
//						 .modDate(rset.getDate("MOD_DATE"))
//						 .build();
				
				b.setNo(rset.getInt("NO"));
				b.setTitle(rset.getString("TITLE"));
				b.setContent(rset.getString("CONTENT"));
				b.setHit(rset.getInt("HIT"));
				b.setRegDate(rset.getDate("REGDATE"));
				b.setModDate(rset.getDate("MODDATE"));
			}

		} catch (SQLException e) {
			logger.error("SQLException 발생 : " + e.getMessage());
		} finally {
			close(pstmt);
			close(conn);
		}

		return b;
	}

	/** 게시글 검색
	 * 
	 * @param conn
	 * @param keyword
	 * @return list
	 */
	public List<Board> listSearchBoard(PageInfo page, String keyword) {
		Connection conn = getConnection(true);
		PreparedStatement pstmt = null;
		Board b = new Board();
		ResultSet rset = null;
		List<Board> list = new ArrayList<>();
		
		String sql = prop.getProperty("listSearchBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
			int endRow = startRow + page.getBoardLimit() - 1;
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			
				rset = pstmt.executeQuery();
				while(rset.next()) {
//					Board b = Board.builder()
//								   .no(rset.getInt("B_NO"))
//								   .title(rset.getString("B_TITLE"))
//								   .content(rset.getString("B_CONTENT"))
//								   .hit(rset.getInt("B_"))
//								   .regId(rset.getString("REG_ID"))
//								   .regDate(rset.getDate("REG_DATE"))
//								   .build();
					
					b.setNo(rset.getInt("NO"));
					b.setTitle(rset.getString("TITLE"));
					b.setContent(rset.getString("CONTENT"));
					b.setHit(rset.getInt("HIT"));
					b.setRegId(rset.getString("REGID"));
					b.setRegDate(rset.getDate("REGDATE"));
					
					list.add(b);
			}
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " +  e.getMessage());
		} finally {
			close(pstmt);
			close(conn);
		}
		
		return list;
		
	}
	
	/** 검색된 게시물 count
	 * 
	 * @param keyword
	 * @return listCount
	 */
	public int selectSearchCount(String keyword) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int listCount = 0;

		String sql = prop.getProperty("selectSearchCount");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);

			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				listCount = rset.getInt("COUNT");
			}
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " + e.getMessage());
		} finally {
			close(rset);
			close(pstmt);
			close(conn);
		}
		
		return listCount;
	}

	/** 댓글 조회
	 * 
	 * @param boardNo
	 * @return list
	 */
	public List<Reply> listReply(int boardNo) {
		Connection conn = getConnection(true);
		Reply r = new Reply();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Reply> list = new ArrayList<>();

		String sql = prop.getProperty("listReply");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);

			rset = pstmt.executeQuery();

			while (rset.next()) {
//				Reply r = Reply.builder()
//							   .rNo(rset.getInt("REPLY_NO"))
//							   .content(rset.getString("REPLY_CONTENT"))
//							   .regId(rset.getString("REG_ID"))
//							   .regDate(rset.getString("REG_DATE"))
//							   .delYn(rset.getString("DEL_YN"))
//							   .bNo(rset.getInt("B_NO"))
//							   .build();
				
				r.setRNo(rset.getInt("RNO"));
				r.setContent(rset.getString("CONTENT"));
				r.setRegId(rset.getString("REGID"));
				r.setRegDate(rset.getString("REGDATE"));
				r.setDelYn(rset.getString("DELYN"));
				r.setBNo(rset.getInt("BNO"));

				list.add(r);
			}
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " + e.getMessage());
		} finally {
			close(rset);
			close(pstmt);
			close(conn);
			
		}
		
		return list;
	}
	
}
