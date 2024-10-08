/**
 *
 */
package com.javateam.memberProject.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;
//import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javateam.memberProject.util.FileUploadUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author oracle
 *
 */
@Entity
@Table(name="board_tbl")
@Slf4j
@Getter
@Setter
public class BoardVO implements Serializable { // 10.25 (sesssion으로 변환할 경우 에러 방지)

	// 각 필드들에 대한 @Column 및 컬럼명 추가

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/** 게시글 번호 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
		    generator = "BOARD_SEQ_GENERATOR")
			@SequenceGenerator(
			name = "BOARD_SEQ_GENERATOR",
			sequenceName = "board_seq",
			initialValue = 1,
			allocationSize = 1)
	@Column(name = "board_num")
	private int boardNum;

	/** 게시글 작성자 */
	@Column(name = "board_writer")
	private String boardWriter;

	/** 게시글 비밀번호 */
	@Column(name = "board_pass")
	private String boardPass;

	/** 게시글 제목 */
	@Column(name = "board_subject")
	private String boardSubject;

	/** 게시글 내용 */
	@Column(name = "board_content")
	private String boardContent;

	/** 첨부 파일(원래 파일명) */
	@Column(name = "board_original_file")
	private String boardOriginalFile;

	/** 첨부 파일(인코딩된 파일명) */
	@Column(name = "board_file")
	private String boardFile;

	/** 게시글 답글의 원 게시글(관련글) 번호 */
	@Column(name = "board_re_ref")
	private int boardReRef;

	/** 게시글 답글 레벨 */
	@Column(name = "board_re_lev")
	private int boardReLev;

	/** 게시글 답글 순서 */
	@Column(name = "board_re_seq")
	private int boardReSeq;

	/** 게시글 조회수 */
	@Column(name = "board_readcount")
	private int boardReadCount = 0;

	/** 게시글 작성일자 */
	@Column(name = "board_date")
	@CreationTimestamp // 작성 날짜(기본값) 생성
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") // JSON 변환시 "년월일 및 시분초"까지 모두 출력
	private Date boardDate;

	public BoardVO() {}

	// BoardDTO -> BoardVO
    public BoardVO(BoardDTO board) {

        this.boardNum = board.getBoardNum();
        this.boardWriter = board.getBoardWriter();
        this.boardPass = board.getBoardPass();
        this.boardSubject = board.getBoardSubject();
        this.boardContent = board.getBoardContent();
        this.boardOriginalFile = board.getBoardFile().getOriginalFilename(); // 파일명 저장
        this.boardFile = board.getBoardFile().getOriginalFilename(); // 파일명 저장

        // 첨부 파일 유무 : 없으면 => "", 있으면 => 암호화
        this.boardFile = board.getBoardFile().getOriginalFilename().trim().equals("") ?
        		"" : FileUploadUtil.encodeFilename(board.getBoardFile().getOriginalFilename());

        this.boardReRef = board.getBoardReRef();
        this.boardReLev = board.getBoardReLev();
        this.boardReSeq = board.getBoardReSeq();
        this.boardReadCount = board.getBoardReadCount();
        this.boardDate = board.getBoardDate();
    }

    // 게시글 수정시 : Map<String, Object> => BoardVO
    public BoardVO(Map<String, Object> map) {

    	log.info("BoardVO 오버로딩 생성자 : Map to VO");

    	this.boardNum = Integer.parseInt(map.get("boardNum").toString());
        this.boardWriter = (String)map.get("boardWriter");
        this.boardPass = (String)map.get("boardPass");
        this.boardSubject = (String)map.get("boardSubject");
        this.boardContent = (String)map.get("boardContent");
        this.boardOriginalFile = (MultipartFile)map.get("boardOriginal") == null ? "" : ((MultipartFile)map.get("boardOriginal")).getOriginalFilename(); // 파일명 저장
        // this.boardFile = (MultipartFile)map.get("boardFile") == null ? "" : ((MultipartFile)map.get("boardFile")).getOriginalFilename(); // 파일명 저장
        this.boardReRef = Integer.parseInt(map.get("boardReRef").toString());
        this.boardReLev = Integer.parseInt(map.get("boardReLev").toString());
        this.boardReSeq = Integer.parseInt(map.get("boardReSeq").toString());
        // this.boardReadCount = Integer.parseInt(map.get("boardReadCount").toString()); // 조회수 제외
        this.boardDate = (Date)map.get("boardDate");
    }

    // 게시글 등록시 Map 형태로 인자를 받을 경우 : Map<String, Object> => BoardVO
    public BoardVO(Map<String, Object> map, MultipartFile boardFile) {

    	log.info("BoardVO 오버로딩 생성자 : Map to VO");

    	// this.boardNum = Integer.parseInt(map.get("boardNum").toString());
    	this.boardNum = map.get("boardNum") == null ? 0 : Integer.parseInt(map.get("boardNum").toString());

        this.boardWriter = (String)map.get("boardWriter");
        this.boardPass = (String)map.get("boardPass");
        this.boardSubject = (String)map.get("boardSubject");
        this.boardContent = (String)map.get("boardContent");

        ////////////////////////////////////////////////////////
        //
        log.info("map.get(\"boardOriginal\") : " + map.get("boardOriginal"));

        if (boardFile.isEmpty() == false) {

	        this.boardOriginalFile = boardFile.getOriginalFilename(); // 파일명 저장

	        // 암호화 파일 부분 추가
	        // 첨부 파일 유무 : 없으면 => "", 있으면 => 암호화
	        this.boardFile = FileUploadUtil.encodeFilename(boardFile.getOriginalFilename());
        }

        ////////////////////////////////////////////////////////

        this.boardReRef = map.get("boardReRef") == null ? 0 : Integer.parseInt(map.get("boardReRef").toString());
        this.boardReLev = map.get("boardReLev") == null ? 0 : Integer.parseInt(map.get("boardReLev").toString());
        this.boardReSeq = map.get("boardReSeq") == null ? 0 : Integer.parseInt(map.get("boardReSeq").toString());
        this.boardDate = (Date)map.get("boardDate");
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BoardVO [boardNum=").append(boardNum).append(", boardWriter=").append(boardWriter)
				.append(", boardPass=").append(boardPass).append(", boardSubject=").append(boardSubject)
				.append(", boardContent=").append(boardContent).append(", boardOriginalFile=").append(boardOriginalFile)
				.append(", boardFile=").append(boardFile).append(", boardReRef=").append(boardReRef)
				.append(", boardReLev=").append(boardReLev).append(", boardReSeq=").append(boardReSeq)
				.append(", boardReadCount=").append(boardReadCount).append(", boardDate=").append(boardDate)
				.append("]");
		return builder.toString();
	}


	// 게시글 수정시 기존 정보와 수정 정보 동일성 여부 점검시 활용
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boardContent == null) ? 0 : boardContent.hashCode());
		result = prime * result + ((boardFile == null) ? 0 : boardFile.hashCode());
		result = prime * result + boardNum;
		result = prime * result + ((boardOriginalFile == null) ? 0 : boardOriginalFile.hashCode());
		result = prime * result + ((boardPass == null) ? 0 : boardPass.hashCode());
		result = prime * result + ((boardSubject == null) ? 0 : boardSubject.hashCode());
		result = prime * result + ((boardWriter == null) ? 0 : boardWriter.hashCode());
		return result;
	}


	// 게시글 수정시 기존 정보와 수정 정보 동일성 여부 점검시 활용
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BoardVO)) {
			return false;
		}
		BoardVO other = (BoardVO) obj;

		if (boardContent == null) {
			if (other.boardContent != null) {
				return false;
			}
		} else if (!boardContent.equals(other.boardContent)) {
			return false;
		}
		if (boardFile == null) {
			if (other.boardFile != null) {
				return false;
			}
		} else if (!boardFile.equals(other.boardFile)) {
			return false;
		}
		if (boardNum != other.boardNum) {
			return false;
		}
		if (boardOriginalFile == null) {
			if (other.boardOriginalFile != null) {
				return false;
			}
		} else if (!boardOriginalFile.equals(other.boardOriginalFile)) {
			return false;
		}

		if (boardPass == null) {
			if (other.boardPass != null) {
				return false;
			}
		} else if (!boardPass.equals(other.boardPass)) {
			return false;
		}
		if (boardSubject == null) {
			if (other.boardSubject != null) {
				return false;
			}
		} else if (!boardSubject.equals(other.boardSubject)) {
			return false;
		}
		if (boardWriter == null) {
			if (other.boardWriter != null) {
				return false;
			}
		} else if (!boardWriter.equals(other.boardWriter)) {
			return false;
		}
		return true;
	}

}