package edu.kh.project.member.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.project.member.model.vo.Member;

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



	/** 닉네임 중복검사 DAO
	 * @param memberNickname
	 * @return result
	 */
	public int nickDupCheck(String memberNickname) {
		return sqlsession.selectOne("ajaxMapper.nickDupCheck", memberNickname);
	}



	/** 회원정보 조회 DAO
	 * @param email
	 * @return 회원정보객체
	 */
	public Member selectEmail(String email) {
		return sqlsession.selectOne("ajaxMapper.selectEmail", email);
	}



// selectList() 
// 조회결과의 각 행을 resultType 또는 resultMap에 맞는 VO객체에 담아 List에 추가하여 반환
	/** 회원 목록 조회 DAO 
	 * @return List<Member>
	 */
	public List<Member> selectMemberList() {
		return sqlsession.selectList("ajaxMapper.selectMemberList");
	}

}
