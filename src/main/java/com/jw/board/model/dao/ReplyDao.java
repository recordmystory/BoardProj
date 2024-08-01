package com.jw.board.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jw.board.model.vo.ReplyVO;

/**
 * @Description 댓글 DAO
 */
public class ReplyDao extends BaseDao {
	
	/** 댓글 조회
	 * 
	 * @param boardNo
	 * @return list
	 * @throws SQLException 
	 * @throws IllegalArgumentException 
	 * @throws NullPointerException 
	 */
	public List<ReplyVO> listReply(int boardNo) throws NullPointerException, IllegalArgumentException, SQLException {
		return selectExecute("listReply", rset -> {
			ReplyVO r = new ReplyVO();
			List<ReplyVO> list = new ArrayList<>();
			while (rset.next()) {
				
				r.setRNo(rset.getInt("RNO"));
				r.setContent(rset.getString("CONTENT"));
				r.setRegId(rset.getString("REGID"));
				r.setRegDate(rset.getString("REGDATE"));
				r.setDelYn(rset.getString("DELYN"));
				r.setBNo(rset.getInt("BNO"));
	
				list.add(r);
			}
			return list;
		}, boardNo);
	}
	
}
