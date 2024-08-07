package reply.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import common.util.MapUtil;
import reply.model.dao.ReplyDao;
import reply.model.vo.ReplyVO;

public class ReplyService {
	private static ReplyDao rDao = new ReplyDao();

	/** 댓글 조회 (AJAX)
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws NullPointerException 파라미터가 담긴 map에 필요한 key가 없거나 값이 null인 경우 발생
	 * @throws SQLException SQL문 관련한 예외 발생
	 * @throws ReflectiveOperationException 리플렉션 동작 중 예외 발생
	 * @throws IllegalArgumentException 잘못된 파라미터가 메소드에 전달될 때 발생
	 * 
	 * @apiNote 이 메소드는 AJAX 요청에 의해 호출되고,
	 * 			댓글을 조회한 후 결과를 맵 형태로 반환한다.
	 * 			댓글 조회 결과는 list(key)를 통해 알 수 있다.
	 */
	public Map<String, Object> listReply(Map<String, String> paramMap) throws NullPointerException, SQLException, ReflectiveOperationException, IllegalArgumentException {
		List<ReplyVO> list = rDao.listReply(Integer.parseInt(paramMap.get("no")));

		return MapUtil.createResultMap("list", list);
	
	}
	
	/** 댓글 등록 (AJAX)
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws NullPointerException 파라미터가 담긴 map에 필요한 key가 없거나 값이 null인 경우 발생
	 * @throws SQLException SQL문 관련한 예외 발생
	 * @throws IllegalArgumentException 잘못된 파라미터가 메소드에 전달될 때 발생
	 * 
	 * @apiNote 이 메소드는 AJAX 요청에 의해 호출되고,
	 * 			댓글 등록 후 결과를 맵 형태로 반환한다.
	 * 			성공여부는 flag(key)를 통해 확인 가능하다.
	 */
	public Map<String, Object> insertReply(Map<String, String> paramMap) throws NullPointerException, SQLException, IllegalArgumentException {
		int result = rDao.updateExecute("insertReply", paramMap.get("content"), Integer.parseInt(paramMap.get("no")));
		
		return MapUtil.createResultMap("flag", result > 0 ? "success" : "fail");
	}
	
	
}
