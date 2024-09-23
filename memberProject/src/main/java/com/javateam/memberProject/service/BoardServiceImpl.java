package com.javateam.memberProject.service;

import java.util.ArrayList;
import java.util.List;

//import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.javateam.memberProject.domain.BoardVO;
import com.javateam.memberProject.repository.BoardDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardDAO boardDAO;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BoardVO insertBoard(BoardVO boardVO) {

		return boardDAO.save(boardVO);
	}

	@Override
	@Transactional(readOnly = true)
	public int selectBoardsCount() {

		return (int)boardDAO.count();
	} //

	@Override
	@Transactional(readOnly = true)
	public List<BoardVO> selectBoardsByPaging(int currPage, int limit) {

		Pageable pageable = PageRequest.of(currPage-1, limit, Sort.by(Direction.DESC, "boardNum"));
		return boardDAO.findAll(pageable).getContent();
	} //

	@Override
	@Transactional(readOnly = true)
	public BoardVO selectBoard(int boardNum) {

		return boardDAO.findById(boardNum);
	}

	@Override
	@Transactional(readOnly = true)
	public int selectBoardsCountBySearching(String searchKey, String searchWord) {

		// return searchKey.equals("board_subject") ? boardDAO.countByBoardSubjectLike("%"+searchWord+"%") :
		return searchKey.equals("board_subject") ? boardDAO.countByBoardSubjectContaining(searchWord) :
			   searchKey.equals("board_content") ? boardDAO.countByBoardContentContaining(searchWord) :
			   boardDAO.countByBoardWriterContaining(searchWord);

	}

	@Override
	@Transactional(readOnly = true)
	public List<BoardVO> selectBoardsBySearching(int currPage, int limit, String searchKey, String searchWord) {

		Pageable pageable = PageRequest.of(currPage-1, limit, Sort.by(Direction.DESC, "boardNum"));

		// return searchKey.equals("board_subject") ? boardDAO.findByBoardSubjectLike("%"+searchWord+"%", pageable).getContent() :
		return searchKey.equals("board_subject") ? boardDAO.findByBoardSubjectContaining(searchWord, pageable).getContent() :
			   searchKey.equals("board_content") ? boardDAO.findByBoardContentContaining(searchWord, pageable).getContent() :
			   boardDAO.findByBoardWriterContaining(searchWord, pageable).getContent();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BoardVO updateBoard(BoardVO boardVO) {

		return boardDAO.save(boardVO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<BoardVO> selectReplysById(int boardNum) {

		return boardDAO.findByBoardReRef(boardNum);
	}

	@Override
	@Transactional(readOnly = true)
	public int selectBoardsCountWithoutReplies() {

		return (int)boardDAO.countByBoardReRef(0); // (댓글 아닌)원글만 추출 : board_re_ref = 0
	} //

	@Override
	@Transactional(readOnly = true)
	public List<BoardVO> selectBoardsByPagingWithoutReplies(int currPage, int limit) {

		Pageable pageable = PageRequest.of(currPage-1, limit, Sort.by(Direction.DESC, "boardNum"));
		// return boardDAO.findAll(pageable).getContent();
		return boardDAO.findByBoardReRef(0, pageable).getContent(); // (댓글 아닌)원글만 추출 : board_re_ref = 0
	} //

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteReplysById(int boardNum) {

		boolean result = false;

		try {
			boardDAO.deleteById(boardNum);
			result = true;
		} catch (Exception e) {
			log.error("deleteReplyById error : {}", e);
			result = false;
		}

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public int selectBoardsCountWithReplies(int boardNum) {

		return (int)boardDAO.countByBoardReRef(boardNum); // 댓글의 갯수 추출 : board_re_ref = boardNum
	} //

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteById(int boardNum) {

		boolean result = false;

		try {
			boardDAO.deleteById(boardNum);
			result = true;
		} catch (Exception e) {
			log.error("deleteById error : {}", e);
			result = false;
		}

		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateBoardReadcountByBoardNum(int boardNum) {

		boolean result = false;

		try {
			boardDAO.updateBoardReadcountByBoardNum(boardNum);
			result = true;
		} catch (Exception e) {
			log.error("updateBoardReadcountByBoardNum error : {}", e);
			result = false;
		}

		return result;
	}

	// 09.23 스크랩 기능 구현
	@Override
	@Transactional(readOnly = true)
	public List<BoardVO> findAll() {
		return (List<BoardVO>) boardDAO.findAll(Sort.by("boardNum"));
	}

	@Override
	public BoardVO scrap(String id, int board_number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean scrapCancel(int board_number, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int scrapCount(int board_number, String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int scrapCheck(int board_number, String id) {
		// TODO Auto-generated method stub
		return false;
	}

}