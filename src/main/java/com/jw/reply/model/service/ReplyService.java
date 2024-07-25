package com.jw.reply.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jw.board.model.vo.Reply;
import com.jw.reply.model.dao.ReplyDao;

public class ReplyService {
	private static ReplyDao rDao = new ReplyDao();

	/** 댓글 조회 (ajax)
	 * 
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> listReply(Map<String, String> paramMap) throws Exception {
		List<Reply> list = rDao.listReply(Integer.parseInt(paramMap.get("no")));

		Map<String, Object> resultMap = new HashMap<>();
	    resultMap.put("list", list);
		
		return resultMap;
	}
	
	/** 댓글 등록 (ajax)
	 * 
	 * @param paramMap
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> insertReply(Map<String, String> paramMap) throws Exception {
		int result = rDao.updateExecute("insertReply", paramMap.get("content"), Integer.parseInt(paramMap.get("no")));
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("flag", result > 0 ? "success" : "fail");
	    return resultMap;
	}
}
