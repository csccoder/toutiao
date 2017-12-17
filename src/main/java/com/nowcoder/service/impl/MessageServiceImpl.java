package com.nowcoder.service.impl;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: toutiao
 * @description:
 * @author: chenny
 * @create: 2017-12-15 00:15
 **/
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDAO messageDAO;

    @Override
    public void sendMessage(Message message) {
        messageDAO.addMessage(message);
    }

    @Override
    public List<Message> getConversationDetail(String conversationId,int offset,int limit) {
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }
    @Override
    public List<Message> getConversationList(int userId, int offset, int limit) {
        // conversation的总条数存在id里
        return messageDAO.getConversationList(userId, offset, limit);
    }

    @Override
    public int getUnreadCount(int localUserId, String conversationId) {
        return messageDAO.getConversationUnReadCount(localUserId,conversationId);
    }
}
