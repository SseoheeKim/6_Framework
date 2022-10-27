package edu.kh.project.member.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dao.MyPageDAO;
import edu.kh.project.member.model.vo.Member;

@Service // bean 등록 
public class MyPageServiceImpl implements MyPageService {

	@Autowired // DI
	private MyPageDAO dao; 
	
	@Autowired // DI
	private BCryptPasswordEncoder bcrypt;
	
	
	// 내 정보 수정 서비스 
	@Transactional
	@Override
	public int updateInfo(Member inputMember) {
		int result = dao.updateInfo(inputMember);
		return result;
	}


	// 비밀번호 변경 서비스
	@Transactional
	@Override
	public int changePw(Map<String, Object> paramMap) {
		// 현재 비밀번호와 일치할 때 새 비밀번호로 변경
		
		// 1. 회원번호를 이용해 암호화 된 현재 비밀번호를 DB에서 조회
		String encPw = dao.selectEncPw((int)paramMap.get("memberNo"));
		
		// 2. maches를 이용해 입력Pw / 암호화Pw를 비교 
		//	  결과가 true인 경우, 새 비밀번호로 수정(UPDATE)하는 DAO코드 호출
		
		if(bcrypt.matches((String)paramMap.get("curruntPw"), encPw)) {
			
			// 현재 비밀번호와 암호화 된 비밀번호가 일치하면 새 비밀번호를 암호화
			String newPw = bcrypt.encode( (String)paramMap.get("newPw") );
			
			// paramMap에 존재하는 기존 newPw를 덮어쓰기
			paramMap.put("newPw", newPw);

			// DAO 호출
			int result = dao.changePw(paramMap);
			
			return result;
		} 
		
		return 0; // 비밀번호 불일치시 0을 반환
	}


	
	
	// 회원 탈퇴 서비스
	@Transactional
	@Override
	public int memberDelete(int memberNo, String memberPw) {
		
		// 1. 회원번호를 이용해 암호화 된 현재 비밀번호를 DB에서 조회
		String encPw = dao.selectEncPw(memberNo);
				
		// 2. maches를 이용해 입력Pw / 암호화Pw를 비교 
		//	  결과가 true인 경우, 회원 탈퇴 DAO 호출
				
		if(bcrypt.matches(memberPw, encPw)) {
			
			return dao.memberDelete(memberNo);
		} 
		
		return 0; // 비밀번호 불일치시 0을 반환
	}
	
}
