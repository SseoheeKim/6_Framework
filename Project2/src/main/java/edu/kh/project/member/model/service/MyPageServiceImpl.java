package edu.kh.project.member.model.service;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.Util;
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
		
		if(bcrypt.matches((String)paramMap.get("currentPw"), encPw)) {
			
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


	
	// 프로필 이미지 수정
	@Transactional(rollbackFor = Exception.class) // 예외가 발생하면 롤백
	@Override
	public int updateProfile(String webPath, String filePath, MultipartFile profileImage, Member loginMember) throws Exception {
		
		// 프로필 수정 실패를 대비해 이전 이미지 경로를 저장
		String temp = loginMember.getProfileImage();
		
		
		// 중복 파일명 업로드를 대비하기 위해서 파일명 변경
		String rename = null;
	
		if(profileImage.getSize() == 0) { // 업로드 된 파일이 없는 경우
			loginMember.setProfileImage(null);
			
		} else {
			
			// 원본파일명을 이용해서 새로운 파일명 생성
			rename = Util.fileRename(profileImage.getOriginalFilename());
			
			// 로그인된 회원의 프로필 이미지명 수정
			loginMember.setProfileImage(webPath + rename);
			// /resources/images/memberProfile/rename
			
		}
		
			
		int result = dao.updateProfile(loginMember);
			
		if(result > 0) { // 프로필 수정이 성공(DB 수정 성공 시 -> 실제로 서버에 파일 저장)
				
			if(rename != null) {
				// 변경된 이미지가 존재 == 새로운 파일 업로드 
					
			profileImage.transferTo(new File(filePath + rename));
			// 메모리에 임시 저장된 파일을 지정된 경로에 파일 형태로 변환 == 업로드
					
			}
				
		} else {
			// 실패시 다시 이전 이미지를 세팅
			loginMember.setProfileImage(temp);
			throw new Exception("파일 업로드 실패"); // 예외 강제 발생
		}
		return result;
	}
	
}
