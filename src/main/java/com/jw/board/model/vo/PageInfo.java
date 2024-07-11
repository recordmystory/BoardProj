package com.jw.board.model.vo;

public class PageInfo {
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

	public PageInfo() {

	}

	public PageInfo(int listCount, int currentPage, int pageLimit, int boardLimit, int maxPage, int startPage,
			int endPage) {
		super();
		this.listCount = listCount;
		this.currentPage = currentPage;
		this.pageLimit = pageLimit;
		this.boardLimit = boardLimit;
		this.maxPage = maxPage;
		this.startPage = startPage;
		this.endPage = endPage;
	}

	public int getListCount() {
		return listCount;
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageLimit() {
		return pageLimit;
	}

	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}

	public int getBoardLimit() {
		return boardLimit;
	}

	public void setBoardLimit(int boardLimit) {
		this.boardLimit = boardLimit;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	@Override
	public String toString() {
		return "PageInfo [listCount=" + listCount + ", currentPage=" + currentPage + ", pageLimit=" + pageLimit
				+ ", boardLimit=" + boardLimit + ", maxPage=" + maxPage + ", startPage=" + startPage + ", endPage="
				+ endPage + "]";
	}

}
