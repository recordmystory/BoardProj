package reply.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import board.model.dao.BaseDao;
import reply.model.vo.ReplyVO;

/**
 * 댓글 DAO
 */
public class ReplyDao extends BaseDao {

	/** 댓글 조회
	 * 
	 * @param params : 글 번호
	 * @return list : 조회된 댓글 정보가 담긴 list 
	 * @throws SQLException SQL 문 실행 중 발생
	 * @throws IllegalArgumentException 부적절한 파라미터가 전달된 경우 발생
	 * @throws NullPointerException 파라미터가 null일 경우, 부적절한 파라미터가 전달된 경우 발생
	 */
	public List<ReplyVO> listReply(Object... params) throws NullPointerException, SQLException, ReflectiveOperationException {
		return selectExecute("listReply", rset -> {
			
			List<ReplyVO> list = new ArrayList<>();
			while (rset.next()) {
				/* 
					ReplyVO r = new ReplyVO();				
					r.setRNo(rset.getInt("RNO"));
					r.setContent(rset.getString("CONTENT"));
					r.setRegId(rset.getString("REGID"));
					r.setRegDate(rset.getString("REGDATE"));
					r.setDelYn(rset.getString("DELYN"));
					r.setBNo(rset.getInt("BNO"));
				*/
				ReplyVO r = setFieldValue(ReplyVO.class, rset);
				list.add(r);
			}
			return list;
		}, params);
	}
	
}
