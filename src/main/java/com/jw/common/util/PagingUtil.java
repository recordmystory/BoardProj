package com.jw.common.util;

import com.jw.board.model.vo.PageInfoVO;

/** 
 * 페이징 Util
 */
public class PagingUtil {

	/** 페이징 처리
	 * 
	 * @param listCount - 현재 게시글의 총 개수
	 * @param currentPage - 현재 페이지 (사용자 요청 페이지)
	 * @param pageLimit - 페이징바의 페이지 최대개수
	 * @param boardLimit - 한 페이지에 보여질 게시글 최대 개수
	 * 
	 * @return PageInfoVO(listCount, currentPage, pageLimit, boardLimit, maxPage, startPage, endPage)
	 */
	public static PageInfoVO getPageInfo(int listCount, int currentPage, int pageLimit, int boardLimit) {

		int maxPage = (int) Math.ceil((double) listCount / boardLimit) == 0 ? 1 : (int) Math.ceil((double) listCount / boardLimit);
		int startPage = (currentPage - 1) / pageLimit * pageLimit + 1;
		int endPage = startPage + pageLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		return new PageInfoVO(listCount, currentPage, pageLimit, boardLimit, maxPage, startPage, endPage);
	}
}
