package board.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PageInfoVO {
	/**
	 * 현재 게시글의 총 개수
	 */
	private int listCount; 
	
	/**
	 * 현재 페이지 (사용자가 요청한 페이지)
	 */
	private int currentPage; 
	
	/**
	 * 페이징바의 페이지 최대갯수 (몇개 단위씩)
	 */
	private int pageLimit; 
	
	/**
	 * 한 페이지에 보여질 게시글 최대개수 (몇개 단위씩)
	 */
	private int boardLimit; 
	
	/**
	 * 가장 마지막 페이지(총페이지수)
	 */
	private int maxPage; 
	
	/**
	 * 사용자가 요청한 페이지 하단에 보여질 페이징바의 시작수
	 */
	private int startPage; 
	
	/**
	 * 사용자가 요청한 페이지 하단에 보여질 페이징바의 끝수
	 */
	private int endPage; 

}
