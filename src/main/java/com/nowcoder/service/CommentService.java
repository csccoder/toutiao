package com.nowcoder.service;

import com.nowcoder.model.Comment;

import java.util.List;

/**
 * @program: toutiao
 * @description:
 * @author: chenny
 * @create: 2017-12-14 23:13
 **/
public interface CommentService {

     int addComment(Comment comment);

     List<Comment> selectComment(int entityId, int entityType);

     int selectCount(int entityId,int entityType);

     void deleteComment(int id);


}
