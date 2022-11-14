package edu.kh.project.member.model.dao;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.project.member.model.vo.Member;

@Repository // spring이 bean으로 등록 및 관리 (IOC)
public class MyPageDAO {
	
	@Autowired // spring으로 부터 bean을 주입 받는 의존성 주입(DI)
	private SqlSessionTemplate sqlSession;
	
	
	public int updateInfo(Member inputMember) {
		return sqlSession.update("myPageMapper.updateInfo", inputMember );
	}



	/** 회원 번호가 일치하는 암호화 된 비밀번호 조회 DAO
	 * @param int memberNo
	 * @return encPw
	 */
	public String selectEncPw(int memberNo) {
		return sqlSession.selectOne("myPageMapper.selectEncPw", memberNo);
	}
	
	
	
	/** 비밀번호 수정 DAO
	 * @param paramMap
	 * @return
	 */
	public int changePw(Map<String, Object> paramMap) {
		return sqlSession.update("myPageMapper.changePw", paramMap);
	}


	/** 회원 탈퇴 DAO
	 * @param memberNo
	 * @return
	 */
	public int memberDelete(int memberNo) {
		return sqlSession.update("myPageMapper.memberDelete", memberNo);
	}



	/** 프로필 이미지 수정 DAO
	 * @param loginMember
	 * @return result
	 */
	public int updateProfile(Member loginMember) {
		return sqlSession.update("myPageMapper.updateProfile", loginMember);
	}




	
	

	
	
}
