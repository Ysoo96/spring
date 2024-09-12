package com.javateam.memberProject.service;

import java.util.List;

import com.javateam.memberProject.domain.BoardVO;

public interface BoardService {

	/**
	 * 게시글 작성
	 *
	 * @param boardVO
	 * @return
	 */
	BoardVO insertBoard(BoardVO boardVO);

	/**
	 * 게시글 갯수 조회
	 *
	 * @return
	 */
	int selectBoardsCount();

	/**
	 * 게시글 조회 (페이징)
	 *
	 * @param currPage 현재 페이지
	 * @param limit 페이지 당 글수
	 * @return
	 */
	List<BoardVO> selectBoardsByPaging(int currPage, int limit);

	/**
	 * 개별 게시글 조회
	 *
	 * @param boardNum 게시글 번호
	 * @return
	 */
	BoardVO selectBoard(int boardNum);

	/**
	 * 게시글 검색시 레코드 갯수 조회
	 *
	 * @param searchKey 검색 구분
	 * @param searchWord 검색어
	 * @return
	 */
	int selectBoardsCountBySearching(String searchKey, String searchWord);

	/**
	 * 게시글 조회 (페이징)
	 *
	 * @param currPage 현재 페이지
	 * @param limit 페이지 당 글수
	 * @param searchKey 검색 구분
	 * @param searchWord 검색어
	 * @return
	 */
	List<BoardVO> selectBoardsBySearching(int currPage, int limit, String searchKey, String searchWord);

	/**
	 * 개별 게시글 수정
	 *
	 * @param boardVO
	 * @return
	 */
	BoardVO updateBoard(BoardVO boardVO);

	/**
	 * 개별 게시글 댓글 조회
	 *
	 * @param boardNum 게시글 번호
	 * @return
	 */
	List<BoardVO> selectReplysById(int boardNum);

	/**
	 * 댓글을 제외한 게시글 갯수 조회
	 *
	 * @return
	 */
	int selectBoardsCountWithoutReplies();

	/**
	 * 댓글을 제외한 게시글 리스트 조회 (페이징)
	 *
	 * @param currPage 현재 페이지
	 * @param limit 게시글 당 글수
	 * @return
	 */
	List<BoardVO> selectBoardsByPagingWithoutReplies(int currPage, int limit);

	/**
	 * 댓글 삭제
	 *
	 * @param boardNum 게시글 번호
	 * @return
	 */
	boolean deleteReplysById(int boardNum);

	/**
	 * 댓글을 제회한 게시글 갯수 조회
	 *
	 * @param boardNum 게시글 번호
	 * @return
	 */
	int selectBoardsCountWithReplies(int boardNum);

	/**
	 * 개별 게시글 삭제
	 *
	 * @param boardNum 게시글 번호
	 * @return
	 */
	boolean deleteById(int boardNum);

	/**
	 * 게시글 조회수 수정(업데이트)
	 *
	 * @param boardNum 게시글 번호
	 * @return
	 */
	boolean updateBoardReadcountByBoardNum(int boardNum);


	/**
	 * 전체 게시글 조회
	 *
	 * @return
	 */
	List<BoardVO> findAll();

}