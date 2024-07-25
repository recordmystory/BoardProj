package com.jw.common.template;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;


public class JDBCTemplate {
	private static final Logger logger = Logger.getLogger(JDBCTemplate.class);

	/** Connection 객체 생성 ( 기본값 : autoCommit(false) )
	 * 
	 * @return getConnection(false)
	 */
	public static Connection getConnection() {
		return getConnection(false);
	}
	
	/**
	 * Connection 객체 생성 
	 * 
	 * 파라미터로 commit (true | false) 를 받아 autoCommit 설정
	 * 
	 * @return conn
	 */
	public static Connection getConnection(boolean autoCommit) {
		Connection conn = null;
		Properties prop = new Properties();

		String filePath = JDBCTemplate.class.getResource("/db/driver/driver.properties").getPath();

		try {
			prop.load(new FileInputStream(filePath));

			Class.forName(prop.getProperty("driver"));
			conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"));
			
			if(conn != null) {
				conn.setAutoCommit(autoCommit);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}
	// get가여조는 메서드하 

	/**
	 * commit
	 * 
	 * @param conn
	 */
	public static void commit(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * rollback
	 * 
	 * @param conn
	 */
	public static void rollback(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * resource close
	 * 
	 * @param resources : connection || Statement || ResultSet 
	 */
	public static void close(AutoCloseable... resources) {
		for (AutoCloseable resource : resources) {
			if(resource != null) {
				try {
					resource.close();
				} catch (Exception e) {
					logger.error("close 실패 ==> " + resource);
					logger.error("에러 메시지 : " + e.getClass().getName() + e.getMessage());
				}
			}
		}
	}
	

}
