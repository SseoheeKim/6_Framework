package edu.kh.project.board.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.board.model.vo.Board;
import edu.kh.project.member.model.vo.Member;

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
			@PathVariable("boardCode") int boardCode, Model model,
			@SessionAttribute(value="loginMember", required=false) Member loginMember,
			HttpServletRequest req, HttpServletResponse resp) throws ParseException {
	
		//게시글 상세조회 서비스 호출
		Board board = service.selectBoardDetail(boardNo);
		
		// + 좋아요 수, 좋아요 여부
		if(board != null) {
			
			// BOARD_LIKE 테이블에 게시글번호, 로그인한 회원 번호가 일치하는 행이 있는지 확인
			
			if(loginMember != null)  { // 로그인 상태인 경우
				Map<String, Object> map = new HashMap<>();
				map.put("boardNo", boardNo);
				map.put("memberNo", loginMember.getMemberNo());
				
				int result = service.boardLikeCheck(map);
				
				if(result > 0 )
					model.addAttribute("likeCheck", "on");
			}
			
		}
		
		// + 게시글 상세 조회 성공 시 조회수 증가(쿠키 사용으로 해당 IP당 하루 한 번)
		if(board != null) { // 게시글이 존재할 때(상세 조회 성공)
			
			// 게시글마다 컴퓨터 1대당 1일 1번씩만 조회수 증가 => 쿠키 이용
		
			// Cookie (브라우저 내에 file과 같은 형태로 저장)
			// - 사용되는 경로와 수명이 존재
			// -> 경로를 지정하고 해당 경로 또는 그 이하로 요청을 할때 쿠키를 서버로 같이 전달
			
			// 쿠키에 "readBoardNo"을 얻어와서 
			// 현재 조회하려는 게시글 번호가 없으면 조회수 1 증가 후 쿠키에 게시글 번호를 추가
			// 만약에 현재 조회하려는 게시글 번호가 있다면 조회수 증가 X
			
			// 쿠키 사용 1) 매개변수에 HttpServletRequest req, HttpServletResponse resp 추가
			
			// 쿠키 사용 2) 쿠키 얻어오기
			Cookie[] cookies = req.getCookies();			
			
			// 쿠키 사용 3) 쿠키 배열 내에서 "readBoardNo"이 있는지 확인
			Cookie c = null; // "readBoardNo" 쿠키를 담을 저장용 변수 선언
			if(cookies != null) { // 쿠키가 존재하는 경우
				for(Cookie temp : cookies) { // 쿠키 배열 내에서 쿠키 검색
					if(temp.getName().equals("readBoardNo")) { // 얻어온 쿠키 이름이 "readBoardNo"인 경우
						c = temp; // 쿠키에 "readBoardNo" 저장
					}
				}
			}
			
			int result = 0; // 조회수 증가 service 호출 결과 저장을 위한 변수

			
			// 쿠키 사용 4) "readBoardNo" 쿠키가 없는 경우( 오늘 상세조회가 없는 경우 )
			if(c == null) { 
				result = service.updateReadCount(boardNo);
				
				// boardNo 게시글을 상세조회했음을 쿠키에 기록 (readBoardNo)
				c =  new Cookie("readBoardNo", "|" + boardNo + "|");
				// 톰캣 8.5 이상부터 쿠키의 값으로 ; , = 공백은 사용 불가
				
			} else { // "readBoardNo"쿠키가 있는 경우
				
				// c.getValue() :  "readBoardNo"쿠키에 저장된 값 (|1998|)

				// 쿠키에 저장된 값 중 " |게시글 번호| "가 존재하는지 확인
				if(c.getValue().indexOf("|"+boardNo+"|") == -1) {

					// 존재하지 않는 경우 == 오늘 처음 조회하는 게시글 번호 
					result = service.updateReadCount(boardNo);
					
					// 현재 조회한 게시글 번호를 쿠키에 값으로 추가
					c.setValue(c.getValue() + "|"+  boardNo + "|");
							
				}
			}
			
			if(result > 0) { // 게시글 상세 조회 후 READ_COUNT 1 증가! 
				// 조회수 증가 성공 시 DB + 위에서 조회된 board 내용 동기화
				board.setReadCount(board.getReadCount() + 1);
				
				// 쿠키 적용 경로, 수명 설정 후 클라이언트에게 전달
				c.setPath("/"); // 경로: localhost(최상위 경로) /  이하로 적용
				// c.setMaxAge(600); // 수명 
				
				// c.setMaxAge(600); // 10분(임시) -- c.setMaxAge(초단위)
				// 수명은 당.일.하.루.!
				// 오늘 23시 59분 59초까지 남은 시간을 초단위로 구하기
				// Date : 날짜용 객체
				Date a =  new Date(); // 현재 시간
				
				// Calendar : Date 업그레이드 객체 (기본생성자 없음)
				Calendar cal = Calendar.getInstance();
				// cal.add(단위, 추가할 값);
				cal.add(cal.DATE, 1); // 날짜에 1 추가
				
				// SimpleDateFormat : 날짜를 원하는 형태의 문자열로 변환
				// SimpleDateFormat을 이용해서 cal 날짜 중 시, 분, 초를 0:0:0으로 바꿈
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				Date temp = new Date(cal.getTimeInMillis());
				// 하루 증가한 내일 날짜의 ms값을 이용해 Date 객체 생성
				
				Date b = sdf.parse(sdf.format(temp));
						// parse : 날짜 형식 String -> Date로 변환 

				// 내일 자정 ms - 현재 시간 ms
				long diff = b.getTime() - a.getTime();
				c.setMaxAge( (int)(diff / 1000) ); 
	
				resp.addCookie(c);
								
			}
		}
		
		model.addAttribute(board);
		
		return "board/boardDetail";
	}
	

	
	// 좋아요 수 증가
	@GetMapping("/boardLikeUp")
	@ResponseBody
	public int boardLikeUp(@RequestParam Map<String, Object> paramMap) {
		// @RequestParam Map<String, Object>
		// -> 요청 시 전달된 파라미터를 하나의 Map으로 반환
		return service.boardLikeUp(paramMap);
	}
	
	
	// 좋아요 수 감소
	@GetMapping("/boardLikeDown")
	@ResponseBody
	public int boardLikeDown(@RequestParam Map<String, Object> paramMap) {
		return service.boardLikeDown(paramMap);
	}
	
}
