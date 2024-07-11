package com.jw.board.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import static com.jw.common.template.JDBCTemplate.*;

import com.jw.board.controller.BoardListController;
import com.jw.board.model.vo.Board;
import com.jw.board.model.vo.PageInfo;
import com.jw.board.model.vo.Reply;

public class BoardDao {
	private Properties prop = new Properties();
	private static final Logger logger = Logger.getLogger(BoardListController.class);

	public BoardDao() {
		try {
			prop.loadFromXML(new FileInputStream(BoardDao.class.getResource("/db/mappers/board-mapper.xml").getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 게시글 목록 조회 및 페이징 
	 * 
	 * @param conn
	 * @param page
	 * @return list
	 */
	public List<Board> selectBoard(Connection conn, PageInfo page) {
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
	    }  catch (SQLException e) {
			e.printStackTrace();
		} 

		return list;
	}
	

	/** 게시글 목록 조회 및 페이징 
	 * @param conn
	 * @return listCount
	 */
	public int selectBoardCount(Connection conn) {
		int listCount = 0;

		String sql = prop.getProperty("selectBoardCount");

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			try (ResultSet rset = pstmt.executeQuery()) {
				if (rset.next()) {
					listCount = rset.getInt("COUNT");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return listCount;
	}

	/** 게시글 등록
	 * 
	 * @param conn
	 * @param b
	 * @return result
	 */
	public int insertBoard(Connection conn, Board b) {
		int result = 0;
		String sql = prop.getProperty("insertBoard");

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, b.getTitle());
			pstmt.setString(2, b.getContent());

			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 

		return result;
	}

	/** 조회수 증가
	 * 
	 * @param conn
	 * @param boardNo
	 * @return result
	 */
	public int updateHit(Connection conn, int boardNo) {
		int result = 0;
		String sql = prop.getProperty("updateHit");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return result;
	}

	/** 게시글 상세 조회
	 * 
	 * @param conn
	 * @param boardNo
	 * @return b
	 */
	public Board selectBoardDetail(Connection conn, int boardNo) {
		Board b = new Board();
		String sql = prop.getProperty("selectBoardDetail");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, boardNo);
			
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
			e.printStackTrace();
		} 

		return b;
	}

	/** 게시글 수정
	 * 
	 * @param conn
	 * @param b
	 * @return result
	 */
	public int updateBoard(Connection conn, Board b) {
		int result = 0;
		String sql = prop.getProperty("updateBoard");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, b.getTitle());
			pstmt.setString(2, b.getContent());
			pstmt.setInt(3, b.getNo());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return result;
	}

	/** 게시글 삭제
	 * 
	 * @param conn
	 * @param boardNo
	 * @return result
	 */
	public int updateDelYn(Connection conn, int boardNo) {
		int result = 0;
		String sql = prop.getProperty("updateDelYn");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return result;
	}

	/** 게시글 검색
	 * 
	 * @param conn
	 * @param keyword
	 * @return list
	 */
	public List<Board> selectSearch(Connection conn, String keyword) {
		List<Board> list = new ArrayList<>();
		
		String sql = prop.getProperty("selectSearch");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, keyword);
			
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
			e.printStackTrace();
		} 
		
		return list;
	}

	public List<Reply> selectReply(Connection conn, int boardNo) {
		List<Reply> list = new ArrayList<>();
		
		String sql = prop.getProperty("selectReply");
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, boardNo);
			
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
			e.printStackTrace();
		}
		
		return list;
	}

	public int insertReply(Connection conn, Reply r) {
		int result = 0;
		String sql = prop.getProperty("insertReply");
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, r.getContent());
			pstmt.setInt(2, r.getbNo());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
