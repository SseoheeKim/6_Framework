package edu.kh.project.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.project.board.model.dao.BoardDAO;
import edu.kh.project.board.model.vo.Board;
import edu.kh.project.board.model.vo.Pagination;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDAO dao;

	
	@Override
	public List<Map<String, Object>> selectBoardType() {
		return dao.selectBoardType();
	}

	
	// 특정 게시판 목록조회
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {
		// 1. 특정 게시판의 전체 게시글 수 조회;
		int listCount = dao.getListCount(boardCode);
		
		// 2. 전체 게시글 수와 cp(현재 페이지)를 이용해 페이징 처리 객체 생성
		Pagination pagination = new Pagination(listCount, cp);
		
		// 3. 페이징 처리객체를 이용해 게시글 목록조회
		List<Board> boardList = dao.selectBoardList(pagination, boardCode);
		
		// 4. 2~3번의 결과를 map에 담아 return
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}

}
