package edu.kh.project.board.model.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Board {
    private int boardNo;
    private String boardTitle;
    private String boardContent;
    private String boardCreateDate;
    private String boardUpdateDate;
    private int readCount;
    private int commentCount;
    private int likeCount;
    private String memberNickname;
    private int memberNo;
    private String profileImage;
    private String thumbnail;
    private int boardCode; // 게시글 작성(insert) 용도 - resultMap에 추가할 필요 없어
    
    // 게시글 내의 이미지 목록
    private List<BoardImage> imageList;
    
    // 게시글 내의 댓글 목록
    private List<Comment> commentList;
	
}
