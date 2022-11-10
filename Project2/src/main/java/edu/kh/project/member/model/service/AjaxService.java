package edu.kh.project.member.model.service;


// 서비스 인터페이스 왜 사용?
// 설계, 유지보수성 향상, AOP
public interface AjaxService {

	int emailDupCheck(String memberEmail);

}
