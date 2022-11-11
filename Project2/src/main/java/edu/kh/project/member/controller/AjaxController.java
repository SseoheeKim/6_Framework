package edu.kh.project.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import edu.kh.project.member.model.service.AjaxService;
import edu.kh.project.member.model.vo.Member;

@Controller // 요청-> 알맞은 서비스 -> 결과반환 -> 알맞은 view응답 제어 + bean등록
public class AjaxController {

	@Autowired
	private AjaxService service;
	
	
	// 이메일 중복검사
	@GetMapping("/emailDupCheck")
	@ResponseBody // 반환되는 값을 jsp경로가 아닌 값 자체로 인식
	public int emailDupCheck(String memberEmail) {
		//System.out.println(memberEmail);
		
		// 이메일 중복검사 서비스 호출
		int result = service.emailDupCheck(memberEmail);
		
		// @ResponseBody 어노테이션 덕분에 result가 View Resolver로 전달되지 않고,
		// 호출한 ajax가 반환
		return result;
	}
	
	
	// 닉네임 중복검사
	@GetMapping("/nickDupCheck")
	@ResponseBody
	public int nickDupCheck(String memberNickname) {	
		return service.nickDupCheck(memberNickname);
	}
	
	
	
	 // 이메일로 회원 정보 조회(JSON, GSON활용)
	  @PostMapping("/selectEmail")
	  @ResponseBody 
	  public String selectEmail(String email) { 
		  Member member=service.selectEmail(email); 
		  System.out.println(member);
		  
		  // JSON형식으로 Member객체 생성 
		  // { "memberEmail" : member.getMemberEmail(), "memberNickname" : member.getMemberNickname()} 
		  //String result ="{ \"memberEmail\" : \"user01@kh.or.kr\", \"memberNickname\" : \"123\"}";
		  
		  
		  // GSON : JSON을 쉽게 사용할 수 있도록 하는 구글 JSON 라이브러리 
		  // GSON 라이브러리를 이용해 Member객체를 JSON 형태로 변환(String) 
		  return new Gson().toJson(member); 
	  }
	
	  
//	// 이메일로 회원 정보 조회( jackson-databind 활용)
//	@PostMapping("/selectEmail")
//	@ResponseBody
//	public Member selectEmail(String email) {
//		// jackson이란?
//		// JSON -> JAVA객체
//		
//		// jackson-databind 
//		// JS객체 <-> JAVA객체 <-> JSON 
//		
//		Member member =service.selectEmail(email);
//		return member;
//		// 자바 객체를 반환하면 Jackson라이브러리가 JS객체로 반환 
//	}
	
	  
	  // 10초마다 회원 목록조회
	  @GetMapping("/selectMemberList")
	  @ResponseBody
	  public String selectMemberList() {
		  
		  List<Member> memberList = service.selectMemberList();
		  // 객체 1개를 표현 == JSON
		  // 객체 여러개가 담긴 배열 == JSONArray
		  return new Gson().toJson(memberList);
	  }
}
