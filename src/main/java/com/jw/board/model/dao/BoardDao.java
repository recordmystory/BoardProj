package com.jw.board.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.jw.board.model.vo.Board;

public class BoardDao extends BaseDao {
	
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
			int count = 0;
			if(rset.next()) {
				count = rset.getInt("COUNT");
			}
			return count;
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
			}
			return b;
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
			int count = 0;
			if(rset.next()) {
				count = rset.getInt("COUNT");
			}
			return count;
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