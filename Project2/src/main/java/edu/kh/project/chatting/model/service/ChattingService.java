package edu.kh.project.chatting.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.chatting.model.vo.ChattingRoom;
import edu.kh.project.chatting.model.vo.Message;

/**
 * @author user1
 *
 */
public interface ChattingService {

	/** 기존 채팅방 존재 여부 확인
	 * @param map
	 * @return chattingNo (기존 채팅룸 번호)
	 */
	int checkChattingNo(Map<String, Integer> map);

	
	/** 새로운 채팅방 생성
	 * @param map
	 * @return chattingNo (새로운 채팅룸 번호)
	 */
	int createChattingRoom(Map<String, Integer> map);


	/** 참여중인 채팅방 리스트 조회
	 * @param memberNo
	 * @return roomList
	 */
	List<ChattingRoom> selectRoomList(int memberNo);

	/** 메세지 삽입
	 * @param msg
	 * @return result
	 */
	int insertMessage(Message msg);

	
	/** 읽음 처리
	 * @param paramMap
	 * @return result
	 */
	int updateReadFlag(Map<String, Object> paramMap);

	
    /** 특정 채팅방의 메세지 내용 조회
     * @param paramMap
     * @return
     */
    List<Message> selectMessageList( Map<String, Object> paramMap);

}
