package com.jw.board.model.service;

import java.util.List;
import java.util.Map;

import com.jw.board.model.dao.ReplyDao;
import com.jw.board.model.vo.ReplyVO;
import com.jw.common.util.MapUtil;

public class ReplyService {
	private static ReplyDao rDao = new ReplyDao();

	/** 댓글 조회 (ajax)
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception 예외 발생 시 발생한 예외를 Controller에서 처리
	 */
	public Map<String, Object> listReply(Map<String, String> paramMap) throws Exception {
		List<ReplyVO> list = rDao.listReply(Integer.parseInt(paramMap.get("no")));

		return MapUtil.createResultMap("list", list);
	
	}
	
	/** 댓글 등록 (ajax)
	 * 
	 * @param paramMap : 파라미터가 담긴 map
	 * @return resultMap : 결과 map
	 * @throws Exception 예외 발생 시 발생한 예외를 Controller에서 처리
	 */
	public Map<String, Object> insertReply(Map<String, String> paramMap) throws Exception {
		int result = rDao.updateExecute("insertReply", paramMap.get("content"), Integer.parseInt(paramMap.get("no")));
		
		return MapUtil.createResultMap("flag", result > 0 ? "success" : "fail");
	}
	
	
}
