package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Entitype;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.model.User;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController extends BaseController {
    @Autowired
    LikeService likeService;
    @Autowired
    NewsService newsService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId) {
        try {
            long likeCount = likeService.like(hostHolder.get().getId(), Entitype.NEWS.getValue(), newsId);
            News news = newsService.selectById(newsId);
            //同步更新喜欢数
            //newsService.updateLikeCount(newsId, (int) likeCount);
            //异步更新喜欢数
            eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.get().getId()).setEntityId(newsId).setEntityType(Entitype.NEWS.getValue()).setEntityOwnerId(newsService.selectById(newsId).getUserId()).setExt("likeCount",String.valueOf((int)likeCount)).setExt("content",String.format("你的资讯【%s】被%s用户点赞啦",news.getTitle(),hostHolder.get().getName())));
            return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新喜欢数异常:" + e.getMessage());
            return ToutiaoUtil.getJSONString(1);
        }
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("newsId") int newsId) {

        try {
            long likeCount = likeService.disLike(hostHolder.get().getId(), Entitype.NEWS.getValue(), newsId);
            News news = newsService.selectById(newsId);
            //同步更新喜欢数
            //newsService.updateLikeCount(newsId, (int) likeCount);
            //异步更新喜欢数
            eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.get().getId()).setEntityId(newsId).setEntityType(Entitype.NEWS.getValue()).setEntityOwnerId(news.getUserId()).setExt("likeCount",String.valueOf((int)likeCount)));
            return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新喜欢数异常:" + e.getMessage());
            return ToutiaoUtil.getJSONString(1);
        }
    }
}
