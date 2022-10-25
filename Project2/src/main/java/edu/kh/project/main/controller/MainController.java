package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// 어노테이션이란 컴파일러에게 알려 현재 클래스 MainController가 Controller라고 알려주는 용도
// new없이도 스프링이 Controller 객체를 생성(bean)하여 관리 
@Controller
public class MainController {

	// forward 시 controller 메서드의 반환형은 String 또는 ModelAndView 중 하나 !
	
	// GET방식 "/"로 요청이 오면 해당 메서드로 처리 ==> Handler Mapping
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String mainPage() {

		// 메인 페이지 요청 시 필요한 코드 작성 가능
		
		// ** forward 방법 **
		// - View Resolver의 prefix/suffix를 제외한 jsp 경로를 작성
		return "common/main";
		
		// /WEB-INF/views/common/main.jsp
		// prefix :  /WEB-INF/views/
		// suffix :  .jsp
	}
}
