package edu.kh.project.board.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.project.board.model.vo.Board;
import edu.kh.project.board.model.vo.Pagination;

@Repository
public class BoardDAO {

	@Autowired
	private SqlSessionTemplate sqlsession;

	/**게시판 이름 목록 조회
	 * @return boardTypeList
	 */
	public List<Map<String, Object>> selectBoardType() {		
		return sqlsession.selectList("boardMapper.selectBoardType") ;
	}

	/**게시글 수 조회
	 * @param boardCode
	 * @return listCount
	 */
	public int getListCount(int boardCode) {
		return sqlsession.selectOne("boardMapper.getListCount", boardCode);
	}

	
	
	/** 특정 게시판 목록 조회
	 * @param pagination
	 * @param boardCode
	 * @return boardList
	 */
	public List<Board> selectBoardList(Pagination pagination, int boardCode) {
		
		// RowBounds객체(마이바티스)
		// - 여려 행 조회 결과 중 특정 위치부터 지정된 행의 개수만 조회하는 객체
		// 						(특정위치는 몇 행을 건너뛸 것인가?)
		
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
								//RowBounds(int offset, int limit)
		
		return sqlsession.selectList("boardMapper.selectBoardList", boardCode, rowBounds);
									// namespace.id				, 파라미터, RowBounds 객체
									//							파라미터가 없는 경우 null 대입

	}
	
	
}
