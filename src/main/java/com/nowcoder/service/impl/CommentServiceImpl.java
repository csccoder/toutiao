package com.nowcoder.service.impl;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: toutiao
 * @description:
 * @author: chenny
 * @create: 2017-12-14 23:15
 **/
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDAO commentDAO;

    @Override
    public int addComment(Comment comment) {
        return commentDAO.addComment(comment);
    }

    @Override
    public List<Comment> selectComment(int entityId, int entityType) {
        return commentDAO.selectComment(entityId, entityType);
    }

    @Override
    public int selectCount(int entityId, int entityType) {
        return commentDAO.selectCount(entityId, entityType);
    }

    @Override
    public void deleteComment(int id) {
        commentDAO.updateStatus(id, 1);
    }
}
