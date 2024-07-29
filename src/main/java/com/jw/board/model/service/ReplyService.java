package com.jw.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jw.board.model.dao.ReplyDao;
import com.jw.board.model.vo.ReplyVO;
import com.jw.common.util.MapUtil;

public class ReplyService {
	private static ReplyDao rDao = new ReplyDao();

	/** 댓글 조회 (ajax)
	 * 
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> listReply(Map<String, String> paramMap) throws Exception {
		List<ReplyVO> list = rDao.listReply(Integer.parseInt(paramMap.get("no")));

		return MapUtil.createResultMap("list", list);
	
	}
	
	/** 댓글 등록 (ajax)
	 * 
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> insertReply(Map<String, String> paramMap) throws Exception {
		int result = rDao.updateExecute("insertReply", paramMap.get("content"), Integer.parseInt(paramMap.get("no")));
		
		return MapUtil.createResultMap("flag", result > 0 ? "success" : "fail");
	}
	
	
}
