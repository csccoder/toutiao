package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Service
public class MessageEventHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Override
    public void doHandle(EventModel eventModel) {
        String content=eventModel.getExt("content");
        if(content!=null){
            Message message = new Message();
            //message.setFromId(eventModel.getActorId());
            message.setFromId(21);//管理员账号
            message.setToId(eventModel.getEntityOwnerId());
            message.setContent(content);
            message.setConversationId(ToutiaoUtil.getConversationId(message.getFromId(),message.getToId()));
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            message.setStatus(0);
            messageService.sendMessage(message);
        }
    }

    @Override
    public List<EventType> getSupports() {
        return Arrays.asList(EventType.LIKE,EventType.COMMENT,EventType.LOGIN);
    }
}
