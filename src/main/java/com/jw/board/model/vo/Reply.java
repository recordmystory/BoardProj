package com.jw.board.model.vo;

public class Reply {
	
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
	
	public Reply() { }


	public Reply(int rNo, String content, String regId, String regDate, String delYn, int bNo) {
		super();
		this.rNo = rNo;
		this.content = content;
		this.regId = regId;
		this.regDate = regDate;
		this.delYn = delYn;
		this.bNo = bNo;
	}

	public int getrNo() {
		return rNo;
	}

	public void setrNo(int rNo) {
		this.rNo = rNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	public int getbNo() {
		return bNo;
	}

	public void setbNo(int bNo) {
		this.bNo = bNo;
	}

	@Override
	public String toString() {
		return "Reply [rNo=" + rNo + ", content=" + content + ", regId=" + regId + ", regDate=" + regDate + ", delYn="
				+ delYn + ", bNo=" + bNo + "]";
	}

}
