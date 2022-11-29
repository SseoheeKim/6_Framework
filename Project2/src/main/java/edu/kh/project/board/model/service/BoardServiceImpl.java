package edu.kh.project.board.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import edu.kh.project.board.model.dao.BoardDAO;
import edu.kh.project.board.model.exception.BoardUpdateException;
import edu.kh.project.board.model.vo.Board;
import edu.kh.project.board.model.vo.BoardImage;
import edu.kh.project.board.model.vo.Pagination;
import edu.kh.project.common.Util;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDAO dao;

	// 게시판 이름 목록 조회
	@Override
	public List<Map<String, Object>> selectBoardType() {
		return dao.selectBoardType();
	}

	
	// 특정 게시판 목록조회
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {
		// 1. 특정 게시판의 전체 게시글 수 조회;
		int listCount = dao.getListCount(boardCode);
		
		// 2. 전체 게시글 수와 cp(현재 페이지)를 이용해 페이징 처리 객체 생성
		Pagination pagination = new Pagination(listCount, cp);
		
		// 3. 페이징 처리객체를 이용해 게시글 목록조회
		List<Board> boardList = dao.selectBoardList(pagination, boardCode);
		
		// 4. 2~3번의 결과를 map에 담아 return
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}


	// 게시글 상세조회 + 이미지 목록 조회 + 댓글 조회
	@Override
	public Board selectBoardDetail(int boardNo) {	
		return dao.selectBoardDetail(boardNo);
	}

	
	// 게시글 상세 조회로 조회수 증가
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateReadCount(int boardNo) {
		return dao.updateReadCount(boardNo);
	}


	// 좋아요 여부 체크
	@Override
	public int boardLikeCheck(Map<String, Object> map) {
		return dao.boardLikeCheck(map);
	}


	// 좋아요 수 증가
	@Override
	public int boardLikeUp(Map<String, Object> paramMap) {
		return dao.boardLikeUp(paramMap);
	}


	// 좋아요 수 감소
	@Override
	public int boardLikeDown(Map<String, Object> paramMap) {
		return dao.boardLikeDown(paramMap);
	}

	
	// 게시글 삭제
	@Override
	public int deleteBoard(int boardNo) {
		return dao.deleteBoard(boardNo);
	}


	// 게시글 작성
	
	// @Transactional() : 예외 발생 시 서비스 내에서 수행한 모든 DML을 롤백
	// 					단, RuntimeException 발생 시에만 롤백
	@Transactional(rollbackFor=Exception.class)
	@Override
	public int boardWrite(Board board, List<MultipartFile> imageList, String webPath, String folderPath) throws IOException{
		
		// 1. 게시글만 삽입
		// 1-1) XSS(크로스 사이트 스크립트 공격) 방지 
		board.setBoardTitle(Util.XSSHandling(board.getBoardTitle()));
		board.setBoardContent(Util.XSSHandling(board.getBoardContent()));
		
		// 개행 문자 처리(반드시 XSS방지 처리 먼저 수행)
		board.setBoardContent(Util.newLineHandling(board.getBoardContent()));
		
		// 추가로 띄어쓰기 등을 방지하는 코드를 추가하는 것도 가능
		
		// 1-2) 게시글 삽입 DAO호출 후 결과로 insert된 게시글 번호를 반환
		int boardNo = dao.boardWrite(board); // 0 또는 게시글 번호
		
		
		
		// 2. 이미지만 삽입
		if (boardNo > 0 ) {
			// imageList : 실제 파일이 담겨있는 리스트
			// boardImageList : DB에 삽입할 이미지 정보만 담겨있는 리스트 
			// reNameList : 변경된 파일명만 담겨있는 리스트 
			
			List<BoardImage> boardImageList = new ArrayList<>();
			List<String> reNameList = new ArrayList<>();
			
			
			// 2-1) imageList에 담겨있는 파일 중 실제로 업로드 된 파일만 분류(size() 확인)
			for(int i=0; i<imageList.size(); i++) {
				
				
				// i번째 파일의 크기가 0보다 크다 == 업로드 된 파일 존재
				if(imageList.get(i).getSize() > 0 ) {
					
					// boardImage 객체 생성
					BoardImage img = new BoardImage();
					
					// boardImage 값 세팅
					img.setImagePath(webPath);
					
					// 원본파일 -> 변경된 파일명으로 변경
					String reName = Util.fileRename(imageList.get(i).getOriginalFilename());
					img.setImageReName(reName);
					reNameList.add(reName); // 변경파일명 리스트에 추가
					
					// 원본 파일명
					img.setImageOriginal(imageList.get(i).getOriginalFilename());
					
					img.setBoardNo(boardNo); // 첨부된 게시글 번호
					img.setImageOrder(i); // 이미지 순서
					
					
					// boardImageList에 추가
					boardImageList.add(img);
				}
				
			}
			
			// boardImageList가 비어있지 않다면
			// 업로드 된 파일이 있기에 분류된 내용이 존재
			if (!boardImageList.isEmpty()) {
				
				// DB에 업로드 된 파일 정보를 INSERT
				int result = dao.insertBoardImageList(boardImageList);
				
				// 삽입 결과 행의 수 == DB에 삽입하려고 분류하여 담긴 리스트의 크기
				// 전부 다 삽입된 경우
				if( result == boardImageList.size() ) {
					
					// 파일로 변환
					for(int i=0; i<boardImageList.size(); i++) {
						
						// 순서 == imageList의 인덱스
						int index= boardImageList.get(i).getImageOrder();
						
						// 실제 파일로 변환
						imageList.get(index).transferTo(new File(folderPath+reNameList.get(i)));
					}
				}
			}
			
		}
		
		return boardNo;
	}


	// 게시글 수정
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int boardUpdate(Board board, List<MultipartFile> imageList, String webPath, String folderPath,
			String deleteList) throws Exception {
		
		// 1. 게시글만 삽입
		// 1-1) XSS(크로스 사이트 스크립트 공격) 방지 
		board.setBoardTitle(Util.XSSHandling(board.getBoardTitle()));
		board.setBoardContent(Util.XSSHandling(board.getBoardContent()));
		
		// 개행 문자 처리(반드시 XSS방지 처리 먼저 수행)
		board.setBoardContent(Util.newLineHandling(board.getBoardContent()));
		
		// 1-2) DAO 호출
		int result = dao.boardUpdate(board); 
		
		// 2. 이미지 수정
		if(result > 0) {  // 게시글이 정상적으로 수정된 경우
			
			// 2-1) 삭제된 이미지가 있을 경우 삭제 진행
			if(!deleteList.equals("")) { 
				// deleteList : "1,3"과 같은 문자열
				
				// deleteList / boardNo를 DAO로 전달해서 삭제하는 sql 필요
				// 방법1) map형식으로 파라미터 전달하기
				// 방법2) sql조건을 하나의 문자열로 만들어 전달 
				String condition = "WHERE BOARD_NO = " + board.getBoardNo() 
								+ " AND IMG_ORDER IN(" +deleteList+")";
				
				// DAO호출
				result = dao.boardImageDelete(condition);
				
				
				//result = 0; // 강제 예외 발생 테스트 용도 
				
				// 삭제 실패 시 
				if(result == 0) { 
					// 강제로 예외를 발생시켜 롤백 수행
					throw new BoardUpdateException("이미지 삭제 실패");
				}
			}
			
			
			
			// 2-2) imageList에서 실제 업로드된 파일을 찾아 분류하는 작업
	        
			// imageList : 실제 파일이 담겨있는 리스트
	        // boardImageList : DB에 삽입할 이미지 정보만 담겨있는 리스트
	        // reNameList : 변경된 파일명만 담겨있는 리스트
	         
	         List<BoardImage> boardImageList = new ArrayList<BoardImage>();
	         List<String> reNameList = new ArrayList<String>();
	         
	         // imageList에 담겨있는 파일 중
	         // 실제로 업로드된 파일만 분류하는 작업 진행
	         for(int i=0 ; i<imageList.size() ; i ++) {
	            
	            // i번째 파일의 크기가 0보다 크다 == 업로드된 파일이 있다
	            if(imageList.get(i).getSize() > 0) {
	               
	               // BoardImage 객체 생성
	               BoardImage img = new BoardImage();
	               
	               // BoardImage 값 세팅
	               img.setImagePath(webPath);
	               
	                  // 원본 파일명 -> 변경된 파일명
	               String reName = Util.fileRename(imageList.get(i).getOriginalFilename());
	               img.setImageReName(reName);
	               reNameList.add(reName); // 변경파일명 리스트에 추가
	               
	                  // 원본 파일명
	               img.setImageOriginal(imageList.get(i).getOriginalFilename()); 
	               img.setBoardNo(board.getBoardNo()); // 첨부된 게시글 번호
	               img.setImageOrder(i); // 이미지 순서
	               
	               // boardImageList에 추가
	               boardImageList.add(img);
	               
	               // 새로 업로드 된 이미지를 이용해서 DB정보를 수정
	               // -> 새로운 이미지가 기존에 존재했는데 수정한건지
	               //	없었는데 새로 추가한건지 현재 알 수 없다.
	               // --> 순서(IMG_ORDER)를 이용해서 수정했지만
	               //	만약 BOARD_IMG 테이블에 IMG_ORDER가 일치하는 행이 없다면 수정 실패! 
	               // 	== 0 반환 == 기존 미존재 == 새로운 이미지 
	               
	               result = dao.boardImageUpdate(img); // 일단 업데이트 시도
	               
	               if (result == 0) { // 기존에 없던 이미지라면 업데이트 결과는 0 반환
	            	   result = dao.boardImageInsert(img); // 새로운 이미지 삽입 
	            	   
	            	   if(result == 0) { // 이미지 삽입 실패 시
	            		   throw new BoardUpdateException("이미지 수정/삽입 중 예외 발생");
	            	   }
	               }
	            } 
	         } // for끝
	         
	         // 분류 작업이 끝난 후 boardImageList, reNameList 반환하여 파일을 서버에 저장
	         if(!boardImageList.isEmpty()) { 
	        	 // 결과물이 있다면 서버에 이미지 저장
	        	 for(int i=0; i<boardImageList.size(); i++) {
	        		 int index = boardImageList.get(i).getImageOrder();
	        		 imageList.get(index).transferTo( new File(folderPath + reNameList.get(i)));
	        	 }
	         }
		}
		
		return result;
	}


	// 검색 게시글 조회
	@Override
	public Map<String, Object> selectBoardList(Map<String, Object> pm, int cp) {
		// 1. 검색 조건이 일치하는 전체 게시글 수 조회(단, 삭제 제외);
		int listCount = dao.getListCount(pm);
				
		// 2. 검색 조건 일치 게시글 수와 cp(현재 페이지)를 이용해 페이징 처리 객체 생성
		Pagination pagination = new Pagination(listCount, cp);
		
		// 3. 페이징 처리객체를 이용해 검색 조건이 일치하는 게시글 목록조회
		List<Board> boardList = dao.selectBoardList(pagination, pm);
		
		// 4. 2~3번의 결과를 map에 담아 return
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}

	// 이미지 목록 조회
	@Override
	public List<String> selectImageList() {
		return dao.selectImageList();
	}



	


}
