package edu.kh.project.member.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.service.MyPageService;
import edu.kh.project.member.model.vo.Member;

// 클래스 레벨에 작성된 @RequestMapping
// -> 요청 주소 중 앞의 공통된 부분을 작성하여 해당 경로의 요청을 모두 받아들인다고 알리는 어노테이션

@SessionAttributes("loginMember") // 탈퇴 성공시 로그아웃에 사용하는 세션 무효화
@RequestMapping("/member/myPage")
@Controller // bean 등록
public class MyPageController {

	@Autowired // DI
	private MyPageService service;
	
	// 내 정보 페이지로 이동
	@GetMapping("/info") // 클래스 레벨에 작성된 @RequestMapping("/member/myPage") 어노테이션이 있기 때문에 '나머지 주소'만 작성
	public String info() {
		return "/member/myPage-info";
	}
	
	// 내 정보 수정
	@PostMapping("/info")
	public String updateInfo(Member inputMember, 
							String[] memberAddress,
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra ) {
		// inputMember : 입력 받은 memberNickname, memberTel, memberAddress(가공 필요)
		// memberAddress : 입력 받은 우편번호, 주소, 상세주소가 담긴 배열
		
		// 기존 방법으로 servlet에서 loginMember 가져오기
		// HttpSession session = req.getSession();
		// Member loginMember = (Member)session.getAttribute("loginMember");
		
		// 스프링에서 loginMember 가져오기
		// @SessionAttribute("loginMember") Member loginMember
		// session 속성 중 loginMember를 키로 가지는 값을 매개변수에 대입
		
		
		
		// 1. 로그인 된 회원 정보에서 회원번호를 얻어와서 inputMember에 저장
		inputMember.setMemberNo(loginMember.getMemberNo());
		
		// 2. inputMember.memberAddress의 값에 따라 address값 변경
		
		if(inputMember.getMemberAddress().equals(",,")) { // 주소 미작성
			
			inputMember.setMemberAddress(null);
			
		} else {
			
			String address = String.join(",,", memberAddress);
			inputMember.setMemberAddress(address);
			// inputMember.setMemberAddress(String.join(",,", memberAddress));
			
		}
		
		int result =service.updateInfo(inputMember);
		
		String path = null;
		String message = null;
		
		if(result > 0) {
			message = "회원 정보가 정상적으로 수정되었습니다.";
			
			// 회원정보가 정상적으로 수정되면 동기화 작업 필요(DB-Session)
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());
			
		} else {
			message = "회원 정보 수정이 실패했습니다. ";
			
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:info"; // 내 정보 재요청
	}
	
	
	
	// 비밀번호 변경 페이지로 이동
	@GetMapping("/changePw") 
	public String changePw() {
		return "member/myPage-changePw";
	}
	
	
	// 비밀번호 변경
	@PostMapping("/changePw")
	public String changePw(@SessionAttribute("loginMember") Member loginMember,
							  /* 파라미터 각각 전달 받기 String currentPw, String newPw */
						@RequestParam Map<String, Object> paramMap,
						RedirectAttributes ra ) {
		
		// @RequestParam Map<String, Object> paraMap
		// - 모든 파라미터를 맵 형식으로 얻어와 저장
		// - 원하는 커맨드 객체가 존재하지 않을 때 주로 사용
		
		// 1. loginMember에서 회원번호를 얻어와 paraMap에 추가
		paramMap.put("memberNo", loginMember.getMemberNo());
		
		// 2. 서비스 호출 후 결과 반환 받기
		int result =service.changePw(paramMap);
		
		String path = null;
		String message = null; 
		
		if(result > 0) {
			path = "info";
			message = "비밀번호가 정상적으로 변경되었습니다.";
			
		} else {
			path = "changePw";
			message = " 현재 비밀번호가 일치하지 않습니다.";
		}

		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	
	// 회원 탈퇴 화면
	@GetMapping("/delete")
	public String memberDelete() {
		return "/member/myPage-delete";
	}
	
	
	// 회원 탈퇴 
	@PostMapping("/delete")
	public String memerDelete(@SessionAttribute("loginMember") Member loginMember,
							String memberPw,
							SessionStatus status,
							RedirectAttributes ra) {
		
		
		int result = service.memberDelete(loginMember.getMemberNo(), memberPw);
							// MEMBER_DEL_FL = 'Y' 상태로 변경
		
		String message = null;
		String path = null;
	
		if(result > 0 ) {
			message = "회원 탈퇴가 정상적으로 처리되었습니다.";
			path = "/";
			
			
			// 로그아웃 코드 추가
			status.setComplete(); // 세션 무효화
			// -> 클래스 레벨에 작성된 @SessionAttributes("key") 에 작성된 key가 일치하는 값만 무효화
			
			// session에서 "loginMember"를 없애야한다
			// == @SessionAttributes("loginMember") - 클래스레벨에 추가 필요
			//		....
			//	  status.setCompleate() 
			
			
		} else { 
			message = "비밀번호가 일치하지 않습니다.";
			path = "delete";
		}
		
		// message 전달 코드 작성
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
		
	}
	
	

}
