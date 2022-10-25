package edu.kh.project.member.model.service;

import edu.kh.project.member.model.vo.Member;

/* Service Interface 사용 이유
 	1. 프로젝트 내 규칙성 부여
 	- 여러명이 동시 작업할 때 충돌이 발생할 가능성이 있기 때문에
 		메서드의 형태를 동일하게 가지고 작업하기 위해서
 	
 	2. 프로젝트의 유지보수성 향상(객체 지향적 설계)
 	- 클래스의 결합도를 약화시키기 위해
 	
 	3. AOP (관점 지향 프로그래밍) 사용 목적
 	- 언제 어떤 방식으로 사용될 지 모르기 때문에 추상적인 형태
 	spring-proxy를 이용하여 AOP 코드가 동작
 	-> spring-proxy는 Service 인터페이스를 상속받아 동작
 */


public interface MemberService {
	
	/**
	 * @param inputMember
	 * @return loginMember
	 */
	public abstract Member login(Member inputMember);
}
