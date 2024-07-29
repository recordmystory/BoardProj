package com.jw.board.model.vo;

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
public class ReplyVO {
	
	/**
	 * 댓글 번호
	 */
	private int rNo; 
	/**
	 * 댓글 내용
	 */
	private String content; 
	/**
	 * 작성자 아이디
	 */
	private String regId; 
	/**
	 * 작성일
	 */
	private String regDate;
	/**
	 * 삭제여부
	 */
	private String delYn; 
	/**
	 * 게시글 번호
	 */
	private int bNo; 
	
}
