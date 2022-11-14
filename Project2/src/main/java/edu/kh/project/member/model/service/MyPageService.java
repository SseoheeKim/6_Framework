package edu.kh.project.member.model.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.vo.Member;

// 인터페이스 사용 이유 : 설계 + 유지보수 + AOP
public interface MyPageService {

	
	/** 회원 정보 수정 서비스 
	 * @param inputMember
	 * @return result
	 */
	int updateInfo(Member inputMember);

	
	
	/** 비밀번호 변경 서비스
	 * @param paramMap
	 * @return result
	 */
	int changePw(Map<String, Object> paramMap);



	/** 회원 탈퇴
	 * @param memberNo
	 * @param memberPw
	 * @return result 
	 */
	int memberDelete(int memberNo, String memberPw);



	/** 프로필 이미지 수정
	 * @param webPath
	 * @param filePath
	 * @param profileImage
	 * @param loginMember
	 * @return result
	 */
	int updateProfile(String webPath, String filePath, MultipartFile profileImage, Member loginMember) throws Exception;

}
