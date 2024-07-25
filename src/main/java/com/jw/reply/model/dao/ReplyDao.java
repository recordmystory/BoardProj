package com.jw.reply.model.dao;

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

import com.jw.board.model.dao.BoardDao;
import com.jw.board.model.vo.Reply;
import com.jw.common.util.ConfigUtil;

public class ReplyDao {
	private Properties prop;
	private static final Logger logger = Logger.getLogger(BoardDao.class);

	
	public ReplyDao() { 
		ConfigUtil configUtil = ConfigUtil.getInstance();
        configUtil.loadXmlFile();

        this.prop = configUtil.getProperties();
	}
	
	/** 댓글 조회
	 * 
	 * @param boardNo
	 * @return list
	 */
	public List<Reply> listReply(int boardNo) {
		Connection conn = getConnection(true);
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Reply> list = new ArrayList<>();

		String sql = prop.getProperty("listReply");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);

			rset = pstmt.executeQuery();

			while (rset.next()) {
				Reply r = new Reply();
				
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
			close(rset, pstmt, conn);
			
		}
		
		return list;
	}
	
	public int updateExecute(String sqlKey, Object... params) {
		
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = prop.getProperty(sqlKey);
		int result = 0;
		try {

	        pstmt = conn.prepareStatement(sql);
	        
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}

			result = pstmt.executeUpdate();

			if (result == 0) {
	            logger.debug("result : 0 (insert || update || delete 된 행의 개수 0)");
	        } else if (result == -1) {
	            logger.debug("result : -1 (비정상 결과)");
	        } else if (result >= 1) {
	            logger.debug("result : " + result + " (insert || update || delete 성공)");
	        }

			commit(conn);

		} catch (SQLException | IllegalArgumentException e) {
			e.printStackTrace();
			logger.error(e.getClass().getName() + "발생 : " + e.getMessage());
			rollback(conn);
		} finally {
			close(pstmt, conn);
		}

		return result;
	}
}
