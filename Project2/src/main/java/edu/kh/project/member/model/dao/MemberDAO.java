package edu.kh.project.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.project.member.model.vo.Member;

@Repository // 퍼시스턴스 레이어 == 영속성(파일, DB)을 가지는 클래스, bean 등록
public class MemberDAO {
// DAO를 직접 수정하기보다는 sql문을 수정하면 되기 때문에 인터페이스는 사용하지 않는 편

	
	// DBCP + Mybatis 이용 객체 DI(의존성 주입)
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	
	
	/** 로그인 DAO
	 * @param memberEmail
	 * @return loginMember
	 */
	public Member login(String memberEmail) {
		// sqlSession.selectOne("매퍼이름.태그id", sql작성에 필요한 값);
		return sqlSession.selectOne("memberMapper.login", memberEmail);
	}



	/** 회원가입 DAO
	 * @param inputMember
	 * @return result
	 */
	public int signUp(Member inputMember) {
		return sqlSession.insert("memberMapper.signUp", inputMember);
		// return 0; // 테스트용 강제 실패
	}
}
