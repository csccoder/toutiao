package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
@Component
public class LikeEventHandler implements EventHandler{
    @Autowired
    private NewsService newsService;
    @Override
    public void doHandle(EventModel eventModel) {
        int likeCount=Integer.valueOf(eventModel.getExt("likeCount"));
        int newsId=eventModel.getEntityId();
        newsService.updateLikeCount(newsId,likeCount);
    }

    @Override
    public List<EventType> getSupports() {
        return Arrays.asList(EventType.LIKE);
    }
}
