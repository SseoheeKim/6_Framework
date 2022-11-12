package edu.kh.project.member.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.service.MemberService;
import edu.kh.project.member.model.service.MemberServiceImpl;
import edu.kh.project.member.model.vo.Member;

// 회원 관련 요청을 받는 컨트롤러
// 로그인, 로그아웃, 회원가입, 아이디(이메일) 중복 검사 등

/* 
  @Controller 
  : 프레젠테이션 레이어(보여지는 층)
  웹 애플리케이션으로 전달 받은 클라이언트의 요청을 알맞은 서비스로 연결하고(Mapping)
  서비스에서 반환된 결과에 따라 알맞은 화면으로 응답하는 방법을 제어하는 역할  
*/

// 어노테이션 작성 순서는 상관없음!
@Controller // 스프링이 만들어 관리하는 객체 
@SessionAttributes({"loginMember", "message"}) 
// -> Model에 추가된 속성 중 Key가 일치하는 속성을 session scope 속성으로 추가
// @SessionAttributes({"K1", "K2", "K3", ... })
public class MemberController {
	
	// * 공용으로 사용할 Service 객체 생성 *
	// @Autowired
	// -> bean scanning을 통해 bean에 등록된 객체 중 알맞은 객체를 DI(의존성 주입)해주는 어노테이션 
	
	// 자동 연결 규칙 : 타입이 같거나 상속 관계인 bean을 자동으로 DI
	@Autowired
	private MemberService service ;
	
	

	// @RequestMapping
	// : 클라이언트의 요청을 처리할 클래스/메서드를 지정하는 어노테이션==> @RequestMapping
	
	
	// *** 파라미터 전달 받는 방법 ***
	// 1. HttpServletRequest를 이용하는 방법
	
	// 로그인 요청(POST)
	// @RequestMapping(value="/member/login", method=RequestMethod.POST)
	public String login(HttpServletRequest req) {
		
		String inputEmail = req.getParameter("inputEmail");
		String inputPw = req.getParameter("inputPw");
		
		// * forward 방법 :  prefix/sufix를 제외한 나머지 jsp경로
		// * redirect 방법  : "redirect:요청주소";
		
		return "redirect:/";
	}
	
	
	
	// 2. requestParam 어노테이션 사용(value / required / defaultValue속성 이용)
	// 		=> 메서드 매개변수에 전달받은 파라미터를 주입하는 어노테이션
	
	// value : 전달 받은 input 태그의 name 속성값
   
	// required : 입력된 name 속성값 파라미터 필수 여부 지정(기본값 true)
	// -> required = true인 파라미터가 존재하지 않는다면 400 Bad Request 에러 발생
	// -> required = true인 파라미터가 null인 경우에도 400 Bad Request
	// -> required = false인 경우 전달된 파라미터가 없으면 null 

	// defaultValue : 파라미터 중 일치하는 name 속성 값이 없을 경우에 대입할 값 지정.
	// -> required = false인 경우 사용 ** 입력되는 값이 없어도 placeholder기능 존재!!!!!!
	// @RequestMapping(value="/member/login", method=RequestMethod.POST)
	public String login(@RequestParam("inputEmail") String email,
						@RequestParam(value="inputPw2", required=false, defaultValue="1234") String Pw,
						String inputPw){
		
		System.out.println(email);
		System.out.println(Pw);
		System.out.println(inputPw); // RequestParam 생략
		return "redirect:/"; 
	}
	
	
	// **** RequestParam 생략 가능 ****
	// 단, 필수 조건은 매개변수 이름 == input name 속성값
	//@RequestMapping(value="/member/login", method=RequestMethod.POST)
	public String login(String inputEmail, String inputPw) {
		
		System.out.println(inputEmail);
		System.out.println(inputPw); // RequestParam 생략
		
		return "redirect:/"; 
	}
	
	

	/*  3. @ModelAttribute (ModelAttribute 어노테이션) 이용
		[작성방법]
	  	@ModelAttribute VO타입 매개변수명
	  	-> 파라미터의 name 속성값이 지정된 VO의 필드명과 같다면 
	  	해당 VO객체의 필드에 파라미터를 세팅
	  	
	  	[조건]
	  	1. VO 내 반드시 기본 생성자 필요
	  	2. VO 내 반드시 setter 필요
	  	3. name 속성값과 필드명이 동일해야함! 
	 */
	
	//@PostMapping("/member/login")
	public String login(/*@ModelAttribute*/ Member inputMember) {
		System.out.println(inputMember);
		return "redirect:/"; 
	}

	
	
	
	// == @RequestMapping(value="/member/login", method=RequestMethod.POST)
	@PostMapping("/member/login") // Post방식의 /member/login요청을 연결
	public String login(@ModelAttribute Member inputMember, 
						Model model, 
						RedirectAttributes ra,
						@RequestParam(value="saveId", required=false) String saveId, //체크박스 값 가져오기
						HttpServletResponse resp, // 쿠키 전달용
						@RequestHeader(value="referer") String referer // 요청 이전 주소
						) {
		
		// Model : 데이터 전달용 객체
		// - 데이터를 Map형식으로 저장하여 전달하는 객체
		// - request scope가 기본값
		// - @SessionAttribute와 함께 사용시 session scope로 변경 가능
		
		// RedirectAttributes
		// - redirect 시 값을 전달하는 용도의 객체
		// - 응답 전 	 : request scope
		// - redirect 중 : session scope
		// - 응답 후 	 : request scope
		// request scope 상태에서 값을 그대로 유지
		
		/* 이전에 배운 Servlet 프로젝트는 
	  	Service 객체를 생성하고, try ~ catch 내부에 코드를 작성하는 방식 */
		
		// String 프로젝트 
		// 서비스 호출 후 결과 반환 받기
		Member loginMember = service.login(inputMember);
		
		String path = null;  // 리다이렉트 경로를 저장할 변수
		
		// 로그인 성공과 실패에 따른 결과 -> 세션이 필요
		// 로그인 실패시 "아이디 또는 비밀번호가 일치하지 않습니다" 세션에 추가
		if(loginMember != null) { 
			// 로그인 성공 시 loginMember를 세션에 추가
			path = "/"; // 메인페이지
			
			model.addAttribute("loginMember", loginMember);
			// addAttribute("K", V) == req.setAttribute("K", V)
			// @SessionAttribute("loginMember") 어노테이션을 클래스 위에 추가하면 session scope로 변환
			
			// *********************************************************************************************
			
			// 쿠키 생성
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			
			// 쿠키 유지 시간 지정
			if(saveId != null) { // 체크 되었을 때
				
				// 1년동안 쿠키 유지
				cookie.setMaxAge(60 * 60 * 24 * 365);
				
			} else { // 체크 안되었을 때 
				
				// 생성과 동시에 삭제 -> 쿠키 0초 유지 -> 클라이언트의 쿠키파일을 삭제
				cookie.setMaxAge(0);
				
			}
			
			// 정해진 쿠키의 수명에 따라 사용될 경로를 지정
			cookie.setPath("/"); //localhost 밑의 모든 경로에서 사용
			
			// 생성된 쿠키를 응답 객체에 담아 클라이언트에게 전달
			resp.addCookie(cookie);
			
			// *********************************************************************************************
			
		} else {
			// 기존 : HttpServletRequest req;
			// 		  path = req.getHeader("referer");
			
			// new  : @RequestHeader(value="referer") String referer
			//		  path = referer;
			
			path = referer; // 로그인 요청 전 페이지 주소(referer)
			
			
			
			// 로그인 실패시 "아이디 또는 비밀번호가 일치하지 않습니다" 세션에 추가
			// model.addAttribute( "message", "아이디 또는 비밀번호가 일치하지 않습니다");
			// -> 메인페이지 주소창에 message값 노출되는 문제점 발생
			// -> RedirectAttributes로 변환 필요
			
			ra.addFlashAttribute( "message", "아이디 또는 비밀번호가 일치하지 않습니다");
			// addFlashAttribute() : 잠깐 session scope에 message를 추가
		}
		
		
		
		return "redirect:" + path;
	}
	
	
	
	// 로그인 페이지로 이동
	@GetMapping("/member/login")
	public String login() {
		return "member/login";
	}
	
	
	
	
	// 로그아웃 
	@GetMapping("/member/logout")
	public String logout(SessionStatus status) {
		
		// 기존 servlet 
		// HttpServeletRequest req;
		// HttpSession session = req.getSession();
		// session.invalidate();
		// -> 불가능 
		//	  @SessionAttributes(클래스 위)로 session scope에 등록된 loginMember를 무효화 시키려면
		//	  SessionStatus라는 별도의 객체가 필요
		
		status.setComplete(); // SessionStatus객체의 setComplete()메서드를 통해 세션 상태를 무효화
		
		return "redirect:/";
	}
	
	
	
	// 회원가입 창으로 이동
	@GetMapping("/member/signUp")
	public String signUp() {
		return "member/signUp";
	}
	
	
	// 회원가입 
	@PostMapping("/member/signUp")
	public String signUp(Member inputMember /* 커맨드 객체 */,
						String[] memberAddress /* name속성값이 memberAddress인 값을 배열로 반환 */,
						RedirectAttributes ra,
						@RequestHeader("referer") String referer) {
		
		
		// POST 요청 시 인코딩 처리 필요
		// -> web.xml에 인코딩 필터 추가하여 처리
		
		
		/* Spring
		  1) 같은 name 속성을 가진 input태그의 값을 값1,값2,값3,...
		  	자동으로 하나의 문자열로 반환
		  
		  2) input type="text"내에 값이 작성되지 않은 경우에는 
		  	null이 아닌 빈칸으로 값을 얻어온다. 		     	*/
	
		
		
		if(inputMember.getMemberAddress().equals(",,")) {
			inputMember.setMemberAddress(null);
			// 주소가 작성되지 않은 경우 ==> null
			
		} else {
			inputMember.setMemberAddress( String.join(",,", memberAddress) );
			// 주소가 작성된 경우 ==> 주소,,주소,,주소
		}
		
		// 서비스 호출 후 결과 반환 받기
		int result = service.signUp(inputMember);	
		
		String path = null; // 리다이렉트 경로 지정
		String message = null; // 전달할 메세지 저장 변수
		
		if(result > 0) { 
			path = "/";
			message = "회원가입 성공!!";
			
		} else {
			path = referer;
			message = "회원 가입을 다시 시도해주세요.";

			// 이전 페이지로 돌아갔을 때 입력했던 값을 같이 전달
			inputMember.setMemberPw(null); // 비밀번호는 삭제
			ra.addFlashAttribute("tempMember", inputMember);
		}
		
		ra.addFlashAttribute("message", message);
		
		
		return "redirect:" + path;
	}
	
		
	/* 참고 *
	 - Controller 메서드의 매개 변수에 객체를 작성하면 자동으로 생성되거나 얻어오는게 가능
	 
	 HOW? 
	 Spring Container에서 Argument Resolver(매개변수 해결사)를 제공해서
	 유연하게 처리가 가능  */
	
	
	/* 스프링 예외 처리 방법 (3종류, 중복 사용 가능)
	 
	 1순위 : try-catch / throws 예외 처리 구문 
	 		-> 메서드 단위로 처리
	 		
	 2순위 : @ExceptionHandler
	 		-> 클래스 단위 처리
	 		  - 하나의 컨트롤러에서 발생하는 예외를 하나의 메서드에 모아서 처리
 	 
	 3순위 : @ControllerAdvice
			-> 웹 애플리케이션 전역에서 발생하는 예외를 모아서 처리
			  - 별도 클래스로 작성
	 */
	
	
	// MemberController에서 발생하는 모든 예외를 하나의 메서드에 모아서 처리
	// @ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {
		
		// Exception e : 발생한 예외를 전달 받는 매개변수
		e.printStackTrace();
		
		model.addAttribute("errorMessage","회원 관련 서비스 이용 중 문제가 발생했습니다.");
		model.addAttribute("e", e);
		
		return "common/error";
	}
	
	
	
}
