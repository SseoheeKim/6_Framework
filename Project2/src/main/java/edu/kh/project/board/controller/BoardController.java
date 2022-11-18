package edu.kh.project.board.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.board.model.vo.Board;

@Controller
public class BoardController {

	@Autowired
	private BoardService service;
	
	
	// 특정 게시판 목록 조회
	// /board/1
	// /board/2
	// /board/3
	// /board/4
	
	// @PathVariable 사용
	// - URL 경로에 있는 값을 파라미터(변수)로 사용할 수 있게 하는 어노테이션
	// - 자동으로 request scope에 등록되어 EL구문으로 jsp에서 출력 가능
	@GetMapping("/board/{boardCode}")
	public String selectBoardList(@PathVariable("boardCode") int boardCode,
						Model model,
						@RequestParam(value="cp", required=false, defaultValue="1") int cp
						) {
		
		
		// 요청주소 ?K=V&K=V&K=V.. (queryString)
		// Model : 값 전달용 객체
		// model.addAttribute("K", V) : request scope에 세팅(forward시 유지)
		
		Map<String, Object> map = service.selectBoardList(boardCode, cp);
		model.addAttribute("map", map); // request scope 세팅
		
		return "board/boardList"; // forward		
	}
	
	
	// 게시글 상세 조회
	@GetMapping("/board/{boardCode}/{boardNo}")
	public String boardDetail(@PathVariable("boardNo") int boardNo,
			@PathVariable("boardCode") int boardCode, Model model) {
	
		//게시글 상세조회 서비스 호출
		Board board = service.selectBoardDetail(boardNo);
		// + 좋아요 수, 좋아요 여부
		// + 조회수 증가(쿠키 사용으로 해당 IP당 하루 한 번)
		
		
		model.addAttribute(board);
		
		return "board/boardDetail";
	}
	

	
	
}
