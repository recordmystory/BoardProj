package board.model.vo;

import java.sql.Date;

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
public class BoardVO {
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
}
