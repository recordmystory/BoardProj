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
import com.jw.common.util.ConfigUtil;

public class BoardDao {
	private Properties prop;
	private static final Logger logger = Logger.getLogger(BoardDao.class);

	public BoardDao() { 
		ConfigUtil configUtil = ConfigUtil.getInstance();
        configUtil.loadXmlFile();
        
        this.prop = configUtil.getProperties();
        
        // 여기서 처음에 선
//        logger.info("prop : " + prop.toString());
	}
	
	/*	static { // 클래스 최초 로드 시만 getProperties() 메서드 호출
			prop = BoardController.getProperties();
		}*/
	
	/*	public static Properties getProperties() {
	    return prop;
	}*/
	

	@FunctionalInterface
	public interface ResultSetHandler<T> {
	    T handle(ResultSet rset) throws SQLException;
	}

	/** select 메서드 통합
	 * 
	 * @param <T>
	 * @param sqlKey
	 * @param handler
	 * @param params
	 * @return result
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
			e.printStackTrace();
			logger.error(e.getClass().getName() + "발생 : " + e.getMessage());
			rollback(conn);
		} finally {
			close(pstmt, conn);
		}

		return result;
	}
	/** 게시글 목록 조회 및 페이징 
	 * 
	 * @param page
	 * @return list
	 */
	public List<Board> listBoard(Object... params) {
		return selectExecute("listBoard", rset -> {
			List<Board> list = new ArrayList<>();
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
	        return list;
		}, params);
	}
	
	
	/** 게시글 목록 조회 및 페이징 
	 * 
	 * @return listCount
	 */
	public int selectBoardCount() {
		return selectExecute("selectBoardCount", rset -> {
			if(rset.next()) {
				return rset.getInt("COUNT");
			}
			return 1;
		});
	}
	
	
	/** 게시글 상세 조회
	 * 
	 * @param conn
	 * @param boardNo
	 * @return b
	 */
	public Board detailBoard(int boardNo) {
		return selectExecute("detailBoard", rset -> {
			Board b = new Board();
			
			if (rset.next()) {
				b.setNo(rset.getInt("NO"));
				b.setTitle(rset.getString("TITLE"));
				b.setContent(rset.getString("CONTENT"));
				b.setHit(rset.getInt("HIT"));
				b.setRegDate(rset.getDate("REGDATE"));
				b.setModDate(rset.getDate("MODDATE"));
				return b;
			}
			return null;
		}, boardNo);
				
	}
	
	/** 게시글 검색
	 * 
	 * @param conn
	 * @param keyword
	 * @return list
	 */
	public List<Board> listSearchBoard(Object... params) {
		return selectExecute("listSearchBoard", rset -> {
			List<Board> list = new ArrayList<>();
			while(rset.next()) {
				Board b = new Board();
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
	
	/** 검색된 게시물 count
	 * 
	 * @param keyword
	 * @return listCount
	 */
	public int selectSearchCount(Object... params) {
		return selectExecute("selectSearchCount", rset -> {
			if(rset.next()) {
				return rset.getInt("COUNT");
			}
			return 1;
		}, params);
	}
}

/*private void setFieldsFromResultSet(Board b, ResultSet rset) throws SQLException {
    ResultSetMetaData metaData = rset.getMetaData();
    int columnCount = metaData.getColumnCount();
    Field[] fields = Board.class.getDeclaredFields();
    
    for (int i = 1; i <= columnCount; i++) {
        String columnName = metaData.getColumnName(i).toLowerCase();
        for (Field field : fields) {
            String fieldName = field.getName().toLowerCase();
            if (columnName.equals(fieldName)) {
                Logger.getLogger(getClass().getName()).info("columnName : " + columnName + ", fieldName : " + fieldName);
                try {
                    field.setAccessible(true);
                    Object value = rset.getObject(columnName);
                    if (value != null) {
                        setFieldValue(field, b, value);
                    }
                } catch (IllegalAccessException e) {
                    Logger.getLogger(getClass().getName()).info("Field access error: " + fieldName);
                }
                break;
            }
        }
    }
}

private void setFieldValue(Field field, Board b, Object value) throws IllegalAccessException {
	Class<?> fieldType = field.getType();

    if (fieldType == int.class || fieldType == Integer.class) {
        if (value instanceof BigDecimal) {
            field.set(b, ((BigDecimal) value).intValue());
        } else if (value instanceof Number) {
            field.set(b, ((Number) value).intValue());
        }
    } else if (fieldType == long.class || fieldType == Long.class) {
        if (value instanceof BigDecimal) {
            field.set(b, ((BigDecimal) value).longValue());
        } else if (value instanceof Number) {
            field.set(b, ((Number) value).longValue());
        }
    } else if (fieldType == double.class || fieldType == Double.class) {
        if (value instanceof BigDecimal) {
            field.set(b, ((BigDecimal) value).doubleValue());
        } else if (value instanceof Number) {
            field.set(b, ((Number) value).doubleValue());
        }
    } else if (fieldType == Date.class) {
        if (value instanceof String) {
            field.set(b, Date.valueOf((String) value));
        } else if (value instanceof Date) {
            field.set(b, (Date) value);
        }
    } else {
        field.set(b, value);
    }
}
}*/

	/*public List<Board> listBoard(Object... params) {
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
			close(rset, pstmt, conn);
		}
	
		return list;
	}*/
	
	
	
	
	/*
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
			close(rset, pstmt, conn);
		}
		
		return listCount;
	}*/
	
	/*public Board detailBoard(int boardNo) {
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
		close(pstmt, conn, rset);
	}
	
	return b;
	}*/

	/** 댓글 조회
	 * 
	 * @param boardNo
	 * @return list
	 */
	/*public List<Reply> listReply(int boardNo) {
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
	}*/

	/*	public List<Board> listSearchBoard(PageInfo page, String keyword) {
			Connection conn = getConnection(true);
			PreparedStatement pstmt = null;
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
				logger.error("SQLException 발생 : " +  e.getMessage());
			} finally {
				close(rset, pstmt, conn);
			}
			
			return list;
			
		}*/

	/*		public int selectSearchCount(String keyword) {
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
					close(rset, pstmt, conn);
				}
				
				return listCount;
			}*/