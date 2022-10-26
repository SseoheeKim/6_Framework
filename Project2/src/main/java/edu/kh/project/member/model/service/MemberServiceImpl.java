package edu.kh.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dao.MemberDAO;
import edu.kh.project.member.model.vo.Member;

// @Service			: bean 등록 시 이름을 클래스명으로 등록(memberServiceImlp)
// @Service("이름") : bean 동록 시 지정된 이름으로 등록

@Service // Service 레이어 == 비즈니스 로직을 가진 클래스, bean 생성
public class MemberServiceImpl implements MemberService{
	
	// MemberDAO bean 의존성 주입(DI)
	@Autowired
	private MemberDAO dao;
	
	
	// spring-security.xml에서 등록한 bean을 의존성 주입(DI)
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	// 로그인 서비스
	@Override
	public Member login(Member inputMember) {
		
//		System.out.println("입력한 비밀번호 : " + inputMember.getMemberPw());
//		System.out.println("암호화 비밀번호 : " + bcrypt.encode(inputMember.getMemberPw()));

		
		/*  bcrypt 이용 시 로그인 방법
		 1. 이메일이 일치하는 회원정보를 DB에서 조회
		 	반드시 비밀번호(MEMBER_PW) 포함 */
		 	
		 Member loginMember = dao.login(inputMember.getMemberEmail());	
		 	
		 /* 
		  2. 입력 받은 비밀번호(평문)와 조회한 암호회된 비밀번호를 비교
		 	--> BCryptPasswordEncoder.matches(평문, 암호문) 이용하여
		 		같으면 true, 아니면 false 반환 */
		 
		 
		 if(loginMember != null) { // 아이디 정상 입력
			 if(bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
				 // 3-1. 조건식을 통해 비밀번호가 일치하면 조회된 회원 정보를 반환 
				 //     (단, 비밀번호는 제거-> null인 상태로 변경)
				 loginMember.setMemberPw(null);
				 
			 } else {
				 // 3-2. 비밀번호가 일치하지 않으면 없는 회원 null 반환 
				 loginMember = null;
			 }
		 }
		 
		return loginMember;
	}
	
	/** 스프링에서 트랜잭션을 처리하는 방법 **
	  
	  - 선언적 트랜잭션 처리
	  1) <tx:advice> 를 이용한 XML 작성
	  
	  
	  
	  2) @Transactional(어노테이션) 작성
	  	- 클래스나 메서드 위에 작성 가능
	  	- 사용 조건 : AOP를 이용한 기술
	  				 -> Service Interface 필요
	  				 -> 트랜잭션 매니저가 bean으로 등록(root-context.xml)
	  				 
		@Transactional 특징
		  - 예외가 발생한 경우 자동 rollback (예외가 없으면 자동 commit)
		  - 예외의 기본값은 RuntimeException
		  - SQL 진행 시 발생하는 예외인 SQLException(RuntimeException의 형제)는 다형성 적용 X
		  	-> 다른 예외에도 rollback이 되게하려고 한다면,  
		  		@Transactional( rollbackFor = 예외클래스 ) 작성
		  	-> mybatis에서는 생략해도 가능
	 */

	
	
	// 회원가입 서비스
	@Transactional(rollbackFor = Exception.class) // 모든 예외 발생 시 롤백
	@Override
	public int signUp(Member inputMember) {
		
		// 비밀번호 암호화
		String encPw = bcrypt.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);
		
		// DAO 호출 후 결과 반환 받기
		int result = dao.signUp(inputMember);
		
		return result;
	}

}
