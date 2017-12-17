package com.nowcoder.service;

import com.nowcoder.model.Message;

import java.util.List;

/**
 * @program: toutiao
 * @description:
 * @author: chenny
 * @create: 2017-12-15 00:15
 **/
public interface MessageService {
    void sendMessage(Message message);

    List<Message> getConversationDetail(String conversationId,int offset,int limit);

    List<Message> getConversationList(int userId, int offset, int limit) ;

    int getUnreadCount(int localUserId, String conversationId);
}
