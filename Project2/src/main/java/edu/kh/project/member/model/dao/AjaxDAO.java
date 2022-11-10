package edu.kh.project.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository // db 연결과 bean 등록 역할을 하는 객체
public class AjaxDAO {

	@Autowired // 같은 자료형이 bean으로 등록되어 있으면 자동으로 DI
	private SqlSessionTemplate sqlsession;
	// SqlSessionTemplate : 커넥션 + 마이바티스 + 스프링 TX제어

	
	
	/** 이메일 중복검사 DAO
	 * @param memberEmail
	 * @return result
	 */
	public int emailDupCheck(String memberEmail) {
		return sqlsession.selectOne("ajaxMapper.emailDupCheck", memberEmail);
	}
}
