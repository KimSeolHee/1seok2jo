package com.seok.home.b_comment;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDAO {
	
	@Autowired
	private SqlSession session;
	private static String NAMESPACE="com.seok.home.b_comment.CommentDAO.";
	
	//강사답글
	public CommentDTO getCommentDetail(CommentDTO commentDTO)throws Exception {
		return session.selectOne(NAMESPACE+"getCommentDetail", commentDTO);
	}
	
	public int setCommentAdd(CommentDTO commentDTO)throws Exception{
		return session.insert(NAMESPACE+"setCommentAdd", commentDTO);
	}
	
	public int setCommentDelete(CommentDTO commentDTO)throws Exception{
		return session.delete(NAMESPACE+"setCommentDelete", commentDTO);
	}
		
	//게시글 댓글
	public List<CommentDTO> getSB_CommentList(CommentDTO commentDTO)throws Exception{
		return session.selectList(NAMESPACE+"getSB_CommentList", commentDTO);
	}
	
	public int setSB_CommentAdd(CommentDTO commentDTO)throws Exception{
		return session.insert(NAMESPACE+"setSB_CommentAdd", commentDTO);
	}

}
