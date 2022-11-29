package edu.kh.project.common.scheduling;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.kh.project.board.model.service.BoardService;

// 스프링이 일정시간마다 동작하도록 스프링에게 제어권한 부여
// IOC(Inversion Of Control, 제어의 역행) 
// -> bean등록 필요

@Component
public class ImageDeleteScheduling {
   /*
    * @Scheduled
    * 
    * * Spring에서 제공하는 스케줄러 - 스케줄러 : 시간에 따른 특정 작업(Job)의 순서를 지정하는 방법.
    * 
    * 설정 방법 
    * 1) servlet-context.xml -> Namespaces 탭 -> task 체크 후 저장
    * 2) servlet-context.xml -> Source 탭 -> <task:annotation-driven/> 추가
    * 
  
    *
    * @Scheduled 속성
    *  - fixedDelay : 이전 작업이 끝난 시점으로 부터 고정된 시간(ms)을 설정.
    *  - fixedRate : 이전 작업이 수행되기 시작한 시점으로 부터 고정된 시간(ms)을 설정.
    *  
    *  - cron : UNIX계열 잡 스케쥴러 표현식으로 작성 - cron="초 분 시 일 월 요일 [년도]" - 요일 : 1(SUN) ~ 7(SAT) 
    * ex) 2019년 9월 16일 월요일 10시 30분 20초 cron="20 30 10 16 9 2" // 연도 생략 가능
    * 
    * - 특수문자 * : 모든 수. 
    * - : 두 수 사이의 값. ex) 10-15 -> 10이상 15이하 
    * , : 특정 값 지정. ex) 3,4,7 -> 3,4,7 지정 
    * / : 값의 증가. ex) 0/5 -> 0부터 시작하여 5마다 
    * ? : 특별한 값이 없음. (월, 요일만 해당) 
    * L : 마지막. (월, 요일만 해당)
    * 
    * * 주의사항 - @Scheduled 어노테이션은 매개변수가 없는 메소드에만 적용 가능.
    * 				매개변수 작성 XXXXXXXXXXXXX
    * 
    */
	
	
	@Autowired
	private BoardService service;
	
	@Autowired
	private ServletContext application; // application 객체
	
	
	
	
	// 로그를 출력하는 객체 얻어오기
	// org.slf4j.Logger 
	private Logger logger = LoggerFactory.getLogger(ImageDeleteScheduling.class);
							// 이 클래스에서 사용하는 로거
	
	
	
	// @Scheduled(fixedDelay = 10000) // 이전 작업이 끝난 후 10초 뒤에 수행
	// @Scheduled(fixedRate = 10000) // 이전 작업이 시작 후 10초 뒤에 수행
	@Scheduled(cron = "0 * * * * *") // 매 분 0초마다
	public void deleteImageFile() {
		// System.out.println("매 분 0초마다 수행");
		
		// 1. DB에서 BOARD_IMG 테이블의 모든 이미지 변경명을 조회
		List<String> dbList = service.selectImageList();
	
		
		
		// 2. Server에 저장된 모든 이미지 파일을 조회
		String folderPath =  application.getRealPath("/resources/images/board");
		
		// 지정된 경로에 존재하는 파일 목록을 배열로 반환
		File[] arr = new File(folderPath).listFiles();
		
		// 배열 -> List로 변환
		List<File> fileList = Arrays.asList(arr);
		
		
		
		
		// 3. 둘을 비교해 Server이미지 목록 중 DB에 없는 이미지를 삭제
		//   -> dbList, fileList 비교
		
		if(!fileList.isEmpty()) { // 서버에 파일이 있다면
			for (File file : fileList) {
				
				// file.toString(); 파일이 저장된 실제 경로를 반환
				// C:\workspace\6_Framework\Project2\src\main\webapp\resources\images\board\20221116105843_00001.gif
				// 문자열.substring(인덱스) : 지정된 인덱스부터 끝까지 잘라서 반환

				String fileName = file.getName();
				// = file.toString().substring(file.toString().lastIndexOf("//") + 1); // 파일명.gif
				
				// dbList에서 fileName과 일치하는 파일명이 없다면 서버에는 있는데 DB에 없는 파일
				if(dbList.indexOf(fileName) == -1) {
					// System.out.println(fileName + "삭제");
					
					// trace, debug, info, warn, error, fatal
					logger.info(fileName + "삭제");
					
					file.delete(); // 서버 파일 삭제
				}
			}
		}
		
		logger.info("이미지 파일 삭제 스케쥴링 완료");
	}
}
