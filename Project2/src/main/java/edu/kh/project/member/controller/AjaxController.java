package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.member.model.service.AjaxService;

@Controller // 요청-> 알맞은 서비스 -> 결과반환 -> 알맞은 view응답 제어 + bean등록
public class AjaxController {

	@Autowired
	private AjaxService service;
	
	
	// 이메일 중복검사
	@GetMapping("/emailDupCheck")
	@ResponseBody // 반환되는 값을 jsp경로가 아닌 값 자체로 인식
	public int emailDupCheck(String memberEmail) {
		 System.out.println(memberEmail);
		
		// 이메일 중복검사 서비스 호출
		int result = service.emailDupCheck(memberEmail);
		
		
		// @ResponseBody 어노테이션 덕분에 result가 View Resolver로 전달되지 않고,
		// 호출한 ajax가 반환
		return result;
	}
	
}
