package edu.kh.project.chatting.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.project.chatting.model.dao.ChattingDAO;
import edu.kh.project.chatting.model.vo.ChattingRoom;
import edu.kh.project.chatting.model.vo.Message;
import edu.kh.project.common.Util;

@Service
public class ChattingServiceImpl implements ChattingService{
	
	@Autowired
	private ChattingDAO dao;

	// 기존 채팅방 존재 여부 확인
	@Override
	public int checkChattingNo(Map<String, Integer> map) {
		return dao.checkChattingNo(map);
	}

	
	// 새로운 채팅방 생성
	@Override
	public int createChattingRoom(Map<String, Integer> map) {
		return dao.createChattingRoom(map);
	}


	// 참여중인 채팅방 리스트 조회
	@Override
    public List<ChattingRoom> selectRoomList(int memberNo) {
        return dao.selectRoomList(memberNo);
    }
	
	
	// 읽음 처리
    @Override
    public int insertMessage(Message msg) {
//        msg.setMessageContent(Util.XSSHandling(msg.getMessageContent()));
        msg.setMessageContent(Util.newLineHandling(msg.getMessageContent()));
        return dao.insertMessage(msg);
    }

    @Override
    public int updateReadFlag(Map<String, Object> paramMap) {
        return dao.updateReadFlag(paramMap);
    }

    @Override
    public List<Message> selectMessageList( Map<String, Object> paramMap) {
        System.out.println(paramMap);
        List<Message> messageList = dao.selectMessageList(  Integer.parseInt( String.valueOf(paramMap.get("chattingNo") )));
        
        if(!messageList.isEmpty()) {
            int result = dao.updateReadFlag(paramMap);
        }
        return messageList;
    }
}
