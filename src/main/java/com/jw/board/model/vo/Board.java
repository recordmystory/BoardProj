package com.jw.board.model.vo;

import java.sql.Date;

/**
 * 
 */
public class Board {
	/**
	 * 게시글 번호
	 */
	private int no;

	/**
	 * 게시글 제목
	 */
	private String title;

	/**
	 * 게시글 내용
	 */
	private String content;

	/**
	 * 조회수
	 */
	private int hit;

	/**
	 * 작성자
	 */
	private String regId;

	/**
	 * 수정자
	 */
	private String modId;

	/**
	 * 작성일
	 */
	private Date regDate;

	/**
	 * 수정일
	 */
	private Date modDate;

	/**
	 * 삭제여부
	 */
	private String delYn;

	public Board() { }

	public Board(int no, String title, String content, int hit, String regId, String modId, Date regDate, Date modDate,
			String delYn) {
		super();
		this.no = no;
		this.title = title;
		this.content = content;
		this.hit = hit;
		this.regId = regId;
		this.modId = modId;
		this.regDate = regDate;
		this.modDate = modDate;
		this.delYn = delYn;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getModId() {
		return modId;
	}

	public void setModId(String modId) {
		this.modId = modId;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	@Override
	public String toString() {
		return "Board [no=" + no + ", title=" + title + ", content=" + content + ", hit=" + hit + ", regId=" + regId
				+ ", modId=" + modId + ", regDate=" + regDate + ", modDate=" + modDate + ", delYn=" + delYn + "]";
	}

}
