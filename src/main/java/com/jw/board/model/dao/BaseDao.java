package com.jw.board.model.dao;

import static com.jw.common.template.JDBCTemplate.close;
import static com.jw.common.template.JDBCTemplate.commit;
import static com.jw.common.template.JDBCTemplate.getConnection;
import static com.jw.common.template.JDBCTemplate.rollback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jw.common.util.ConfigUtil;

public abstract class BaseDao {
	private static final Logger logger = Logger.getLogger(BaseDao.class);
	private Properties prop;
	
	/**
	 * ConfigUtil 클래스를 통해 mapper 파일 로드 
	 */
	public BaseDao() {
		ConfigUtil configUtil = ConfigUtil.getInstance();
        configUtil.loadXmlFile();
        
        this.prop = configUtil.getProperties();
	}
	
	@FunctionalInterface
	public interface ResultSetHandler<T> {
	    T handle(ResultSet rset) throws SQLException;
	}

	/** SQL 실행 (SELECT)
	 * 
	 * @param <T> T : 해당 메소드가 반환할 데이터 타입 
	 * @param sqlKey : 실행할 쿼리 key
	 * @param handler : ResultSet 처리할 handler
	 * @param params : 쿼리 파라미터
	 * @return result : 처리한 결과
	 */
	public <T>T selectExecute(String sqlKey, ResultSetHandler<T> handler, Object... params){
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		T result = null;
		
		String sql = prop.getProperty(sqlKey);
		
		try {
			pstmt = conn.prepareStatement(sql);

			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			rset = pstmt.executeQuery();
			result = handler.handle(rset);
		} catch (SQLException e) {
			logger.error(e.getClass().getName() + "발생 : " + e.getMessage());
		} finally {
			close(rset, pstmt, conn);
		}
		
		return result;
		
	}
	
	/** SQL 실행 (INSERT, UPDATE, DELETE문)
	 * 
	 * @param sql : 실행할 쿼리 key
	 * @param params : 쿼리 파라미터
	 * @return result : 업데이트된 행 개수
	 */
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
			logger.error(e.getClass().getName() + "발생 : " + e.getMessage());
			rollback(conn);
		} finally {
			close(pstmt, conn);
		}

		return result;
	}
}
