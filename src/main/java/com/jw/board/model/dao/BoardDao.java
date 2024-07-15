package com.jw.board.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import static com.jw.common.template.JDBCTemplate.*;

import com.jw.board.controller.BoardController;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.PageInfo;
import com.jw.board.model.vo.Reply;

public class BoardDao {
	private Properties prop;
	private static final Logger logger = Logger.getLogger(BoardDao.class);

	public BoardDao() {
		this.prop = BoardController.getProperties();
	}
	
	public Properties getProperties() {
        return prop;
    }

	/** 게시글 목록 조회 및 페이징 
	 * 
	 * @param page
	 * @return list
	 */
	public List<Board> selectBoard(PageInfo page) {
		Connection conn = getConnection();
		List<Board> list = new ArrayList<>();
		String sql = prop.getProperty("selectBoard");

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
			int endRow = startRow + page.getBoardLimit() - 1;
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);

			logger.debug("selectBoard query : " + sql);

			try (ResultSet rset = pstmt.executeQuery()) {
				while (rset.next()) {
					Board b = new Board();

					b.setNo(rset.getInt("B_NO"));
					b.setTitle(rset.getString("B_TITLE"));
					b.setContent(rset.getString("B_CONTENT"));
					b.setHit(rset.getInt("B_HIT"));
					b.setRegId(rset.getString("REG_ID"));
					b.setRegDate(rset.getDate("REG_DATE"));

					list.add(b);
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " + e.getMessage());
		} finally {
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
		int listCount = 0;

		String sql = prop.getProperty("selectBoardCount");

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			logger.debug("selectBoardCount query : " + sql);
			
			try (ResultSet rset = pstmt.executeQuery()) {
				if (rset.next()) {
					listCount = rset.getInt("COUNT");
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " + e.getMessage());
		} finally {
			close(conn);
		}
		
		return listCount;
	}
	
	/** 게시글 등록
	 * 
	 * @param b
	 * @return result
	 */
	public int insertBoard(Board b) {
		Connection conn = getConnection();	
		int result = 0;
		String sql = prop.getProperty("insertBoard");

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, b.getTitle());
			pstmt.setString(2, b.getContent());
			
			logger.debug("insertBoard query : " + sql);
			
			result = pstmt.executeUpdate();
			commit(conn);
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " +  e.getMessage());
			rollback(conn);
		} finally {
			close(conn);
		}

		return result;
	}

	
	/** 조회수 증가
	 * 
	 * @param boardNo
	 * @return result
	 */
	public int updateHit(int boardNo) {
		Connection conn = getConnection();
		int result = 0;
		String sql = prop.getProperty("updateHit");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, boardNo);
			
			logger.debug("updateHit query : " + sql);
			
			result = pstmt.executeUpdate();
			commit(conn);
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " +  e.getMessage());
			rollback(conn);
		} finally {
			close(conn);
		}
		
		return result;
	}

	/** 게시글 상세 조회
	 * 
	 * @param conn
	 * @param boardNo
	 * @return b
	 */
	public Board selectBoardDetail(int boardNo) {
		Connection conn = getConnection(true);
		Board b = new Board();
		String sql = prop.getProperty("selectBoardDetail");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, boardNo);
			
			logger.debug("selectBoardDetail query : " + sql);

			try (ResultSet rset = pstmt.executeQuery()) {
				if (rset.next()) {
					b.setNo(rset.getInt("B_NO"));
					b.setTitle(rset.getString("B_TITLE"));
					b.setContent(rset.getString("B_CONTENT"));
					b.setHit(rset.getInt("B_HIT"));
					b.setRegDate(rset.getDate("REG_DATE"));
					b.setModDate(rset.getDate("MOD_DATE"));
				}
			}

		} catch (SQLException e) {
			logger.error("SQLException 발생 : " +  e.getMessage());
		} finally {
			close(conn);
		}

		return b;
	}

	/** 게시글 수정
	 * 
	 * @param conn
	 * @param b
	 * @return result
	 */
	public int updateBoard(Board b) {
		Connection conn = getConnection();
		int result = 0;
		String sql = prop.getProperty("updateBoard");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, b.getTitle());
			pstmt.setString(2, b.getContent());
			pstmt.setInt(3, b.getNo());
			
			logger.debug("updateBoard query : " + sql);
			
			result = pstmt.executeUpdate();
			
			commit(conn);
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " +  e.getMessage());
			rollback(conn);
		} finally {
			close(conn);
		}
		
		return result;
	}

	/** 게시글 삭제
	 * 
	 * @param conn
	 * @param boardNo
	 * @return result
	 */
	public int updateDelYn(int boardNo) {
		Connection conn = getConnection(true);
		int result = 0;
		String sql = prop.getProperty("updateDelYn");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, boardNo);
			
			logger.debug("updateDelYn query : " + sql);
			
			result = pstmt.executeUpdate();
			
			commit(conn);
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " +  e.getMessage());
			rollback(conn);
		} finally {
			close(conn);
		}
		
		return result;
	}

	/** 게시글 검색
	 * 
	 * @param conn
	 * @param keyword
	 * @return list
	 */
	public List<Board> selectSearch(PageInfo page, String keyword) {
		Connection conn = getConnection(true);
		List<Board> list = new ArrayList<>();
		
		String sql = prop.getProperty("selectSearch");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, keyword);
			int startRow = (page.getCurrentPage() - 1) * page.getBoardLimit() + 1;
			int endRow = startRow + page.getBoardLimit() - 1;
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			
			logger.debug("selectSearch query : " + sql);

			try (ResultSet rset = pstmt.executeQuery()) {
				while(rset.next()) {
					Board b = new Board();
					
					b.setNo(rset.getInt("B_NO"));
					b.setTitle(rset.getString("B_TITLE"));
					b.setContent(rset.getString("B_CONTENT"));
					b.setHit(rset.getInt("B_HIT"));
					b.setRegId(rset.getString("REG_ID"));
					b.setRegDate(rset.getDate("REG_DATE"));
					
					list.add(b);
			     }
			}
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " +  e.getMessage());
		} finally {
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
		int listCount = 0;

		String sql = prop.getProperty("selectSearchCount");

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, keyword);
			
			logger.debug("selectSearchCount query : " + sql);
			
			try (ResultSet rset = pstmt.executeQuery()) {
				if (rset.next()) {
					listCount = rset.getInt("COUNT");
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " + e.getMessage());
		} finally {
			close(conn);
		}
		
		return listCount;
	}

	/** 댓글 조회
	 * 
	 * @param boardNo
	 * @return list
	 */
	public List<Reply> selectReply(int boardNo) {
		Connection conn = getConnection(true);
		List<Reply> list = new ArrayList<>();
		
		String sql = prop.getProperty("selectReply");
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, boardNo);
			
			logger.debug("selectReply query : " + sql);
			
			try (ResultSet rset = pstmt.executeQuery()) {
				while(rset.next()) {
					Reply r = new Reply();
					
					r.setrNo(rset.getInt("REPLY_NO"));
					r.setContent(rset.getString("REPLY_CONTENT"));
					r.setRegId(rset.getString("REG_ID"));
					r.setRegDate(rset.getString("REG_DATE"));
					r.setDelYn(rset.getString("DEL_YN"));
					r.setbNo(rset.getInt("B_NO"));
					
					list.add(r);
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " +  e.getMessage());
		} finally {
			close(conn);
		}
		
		return list;
	}

	/** 댓글 등록
	 * 
	 * @param r
	 * @return result
	 */
	public int insertReply(Reply r) {
		Connection conn = getConnection();

		int result = 0;
		String sql = prop.getProperty("insertReply");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, r.getContent());
			pstmt.setInt(2, r.getbNo());
			
			logger.debug("insertReply query : " + sql);
			
			result = pstmt.executeUpdate();
			commit(conn);
		} catch (SQLException e) {
			logger.error("SQLException 발생 : " +  e.getMessage());
			rollback(conn);
		} finally {
			close(conn);
		}
		
		return result;
	}

	

}
