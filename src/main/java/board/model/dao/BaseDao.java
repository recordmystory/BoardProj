package board.model.dao;

import static common.template.JDBCTemplate.close;
import static common.template.JDBCTemplate.commit;
import static common.template.JDBCTemplate.getConnection;
import static common.template.JDBCTemplate.rollback;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import common.util.Configuration;

public abstract class BaseDao {
	private static Logger logger = Logger.getLogger(BaseDao.class);
	private Properties prop;
	
	/**
	 * Configuration 클래스를 통해 mapper 파일 로드 
	 */
	public BaseDao() {
		Configuration configUtil = Configuration.getInstance();
        configUtil.loadXmlFile();
        
        this.prop = configUtil.getProperties();
	}
	
	@FunctionalInterface
	public interface ResultSetHandler<T> {
	    T handle(ResultSet rset) throws NullPointerException, IllegalArgumentException, SQLException, ReflectiveOperationException;
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
	public <T>T selectExecute(String sqlKey, ResultSetHandler<T> handler, Object... params) throws NullPointerException, SQLException, ReflectiveOperationException {
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
	public int updateExecute(String sqlKey, Object... params) throws NullPointerException, SQLException, IllegalArgumentException {
		
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = prop.getProperty(sqlKey);
		int result = -1;
		
		try {
			
	        pstmt = conn.prepareStatement(sql);
	        
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			
			/*
			 * executeUpdate() : insert / update / delete => 즉, DML문에서는 반영된 행 수를 반환하고,
			 * 					 CREATE, DROP문에서는 -1 반환
			 */
			result = pstmt.executeUpdate();

			if (result == 0) {
	            logger.debug("result : 0 (insert || update || delete 된 행의 개수 0)");
	        } else if (result == -1) {
	            logger.debug("result : -1");
	        } else if (result >= 1) {
	            logger.debug("result : " + result + " (insert || update || delete 성공)");
	        }
			
			commit(conn);

		} catch (NullPointerException | SQLException | IllegalArgumentException e) {
			logger.error(e.getClass().getName() + "발생 : " + e.getMessage());
			rollback(conn);
			throw e; // Exception 던진 후 Controller에서 받아서 처리
		} 
		finally {
			close(pstmt, conn);
		}

		return result;
	}
	
	/** 클래스의 모든 필드명을 가져옴
	 * 
	 * @param <T> 클래스의 타입
	 * @param clazz 필드명을 가져올 클래스
	 * @return DTO의 필드명이 담긴 List
	 */
	protected static <T> List<String> getFieldNames(Class<T> clazz) {
		return Arrays.stream(clazz.getDeclaredFields())
						          .map(Field::getName)
						          .collect(Collectors.toList()); 
	}
	
	/** 객체의 정보를 가져와 필드의 타입에 맞게 ResultSet 값을 담음 
	 * 
	 * @param <T> 객체 타입
	 * @param clazz 데이터를 set할 객체의 클래스 타입
	 * @param rset ResultSet
	 * @return instance ResultSet 값이 set된 객체
	 * @throws SQLException SQL문 실행 중 예외가 발생하면 상위 클래스로 예외를 던짐
	 * @throws ReflectiveOperationException 리플렉션 동작 중 예외가 발생하면 상위 클래스로 예외를 던짐
	 */
	protected <T> T setFieldValue(Class<T> clazz, ResultSet rset) throws SQLException, ReflectiveOperationException {
		T instance = clazz.getDeclaredConstructor().newInstance();
		List<String> filedNames = getFieldNames(instance.getClass());
		
		for (String fieldName : filedNames) {
			try {

				Field field = instance.getClass().getDeclaredField(fieldName);
				field.setAccessible(true); // private 필드에 접근
				
				// 필드 타입에 맞게 set
				if (field.getType() == int.class) {
					field.setInt(instance, rset.getInt(fieldName));
				} else if (field.getType() == String.class) {
					field.set(instance, rset.getString(fieldName));
				} else if (field.getType() == java.sql.Date.class) {
					field.set(instance, rset.getDate(fieldName));
				}

			} catch (NoSuchFieldException | IllegalAccessException e) {
				logger.error(e.getClass().getName() + "발생 ==> " + e.getMessage());
				throw e; // 예외를 상위 클래스로 던짐
			}
		}
		return instance;
	}
}

