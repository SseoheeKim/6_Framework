package edu.kh.project.member.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

// 회원 관련 요청을 받는 컨트롤러
// 로그인, 로그아웃, 회원가입, 아이디(이메일) 중복 검사 등

/* 
  @Controller 
  : 프레젠테이션 레이어(보여지는 층)
  웹 애플리케이션으로 전달 받은 클라이언트의 요청을 알맞은 서비스로 연결하고(Mapping)
  서비스에서 반환된 결과에 따라
  알맞은 화면으로 응답하는 방법을 제어하는 역할 
  
  */
				
@Controller
public class MemberController {

	// @RequestMapping
	// : 클라이언트의 요청을 처리할 클래스/메서드를 지정하는 어노테이션==> @RequestMapping
	
	
	// *** 파라미터 전달 받는 방법 ***
	// 1. HttpServletRequest를 이용하는 방법
	
	
	// 로그인 요청(POST)
	// @RequestMapping(value="/member/login", method=RequestMethod.POST)
	public String login(HttpServletRequest req) {
		
		String inputEmail = req.getParameter("inputEmail");
		String inputPw = req.getParameter("inputPw");
		
		System.out.println(inputEmail);
		System.out.println(inputPw);
		
		// * forward 방법 :  prefix/sufiix를 제외한 나머지 jsp경고
		
		// * redirect 방법 : "redirect:요청주소";
		
		
		return "redirect:/";
	}
	
	
	
	
	// 2. requestParam 어노테이션 사용
	// - 메서드 매개변수에 전달받은 파라미터를 주입하는 어노테이션
	// ** 속성도 사용 가능!!
	// value : 전달 받은 input 태그의 name 속성값
   
	// required : 입력된 name 속성값 파라미터 필수 여부 지정(기본값 true)
	// -> required = true인 파라미터가 존재하지 않는다면 400 Bad Request 에러 발생
	// -> required = true인 파라미터가 null인 경우에도 400 Bad Request
	// -> required = false인 경우 전달된 파라미터가 없으면 null 

	// defaultValue : 파라미터 중 일치하는 name 속성 값이 없을 경우에 대입할 값 지정.
	// -> required = false인 경우 사용 ** 입력되는 값이 없어도 placeholder기능 존재!!!!!!
	
	
	// **** RequestParam 생략 가능 ****
	// 단, 필수 조건은 매개변수 이름 == input name 속성값

	//@RequestMapping(value="/member/login", method=RequestMethod.POST)
	public String login(@RequestParam("inputEmail") String email,
						@RequestParam(value="inputPw2", required=false, defaultValue="1234") String Pw,
						String inputPw){
		
		System.out.println(email);
		System.out.println(Pw);
		System.out.println(inputPw); // RequestParam 생략
		return "redirect:/"; 
	}
	
	
	// RequestParam 생략을 이용해 짧게 코드 작성 가능
	@RequestMapping(value="/member/login", method=RequestMethod.POST)
	public String login(String inputEmail, String inputPw) {
		
		System.out.println(inputEmail);
		System.out.println(inputPw); // RequestParam 생략
		
		return "redirect:/"; 
	}
}
