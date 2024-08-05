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

import com.jw.common.util.Configuration;

public abstract class BaseDao {
	private static Logger logger = Logger.getLogger(BaseDao.class);
	private Properties prop;
	
	/**
	 * ConfigUtil 클래스를 통해 mapper 파일 로드 
	 */
	public BaseDao() {
		Configuration configUtil = Configuration.getInstance();
        configUtil.loadXmlFile();
        
        this.prop = configUtil.getProperties();
	}
	
	@FunctionalInterface
	public interface ResultSetHandler<T> {
	    T handle(ResultSet rset) throws SQLException;
	}
	
	/**
	 * SQL 실행 메서드 (SELECT 문)
	 * 
     * SQL key를 사용해 SELECT 쿼리를 실행하고, 결과를 ResultSetHandler을 통해 처리
     * 
     * @param <T> : 해당 메소드가 반환할 데이터 타입
     * @param sqlKey : 실행할 쿼리의 키 (프로퍼티 파일에 저장된 SQL문의 key값을 식별)
     * @param handler : ResultSet 처리 핸들러
     * @param params : 쿼리 파라미터 (가변 인자)
     * @return result : 처리된 결과
     * @throws NullPointerException SQL 문이나 핸들러가 null인 경우 발생
     * @throws SQLException SQL 실행 중 발생할 수 있는 예외
     * @throws IllegalArgumentException 부적절한 파라미터가 전달된 경우 발생
     */
	public <T>T selectExecute(String sqlKey, ResultSetHandler<T> handler, Object... params) throws NullPointerException, SQLException, IllegalArgumentException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		T result = null;
		
		String sql = prop.getProperty(sqlKey);
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);

			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			rset = pstmt.executeQuery();
			result = handler.handle(rset);
		} catch (NullPointerException | SQLException | IllegalArgumentException e) {
			logger.error(e.getClass().getName() + "발생 : " + e.getMessage());
			throw e;
		} finally {
			close(rset, pstmt, conn);
		}
		
		return result;
		
	}
	
	/**
	 * SQL 실행 메서드 (INSERT, UPDATE, DELETE 문)
	 * 
     * SQL key를 사용해 INSERT, UPDATE, DELETE 쿼리를 실행
     * 정상적으로 처리됐을 때는 commit, 예외 발생 시 rollback
     * 
     * @param sqlKey : 실행할 쿼리의 키 (프로퍼티 파일에 저장된 SQL문의 key값을 식별)
     * @param params : 쿼리 파라미터 (가변 인자)
     * @return result : 업데이트된 행의 개수
     * @throws SQLException SQL 실행 중 발생할 수 있는 예외
     * @throws IllegalArgumentException 잘못된 파라미터가 전달된 경우 발생
	 */
	public int updateExecute(String sqlKey, Object... params) throws SQLException, IllegalArgumentException {
		
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = prop.getProperty(sqlKey);
		int result = -1;
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

		} catch (NullPointerException |SQLException | IllegalArgumentException e) {
			logger.error(e.getClass().getName() + "발생 : " + e.getMessage());
			rollback(conn);
			throw e; // Exception 던진 후 Controller에서 받아서 처리
		} 
		finally {
			close(pstmt, conn);
		}

		return result;
	}
}
