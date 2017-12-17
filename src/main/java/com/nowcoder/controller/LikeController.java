package com.nowcoder.controller;

import com.nowcoder.model.Entitype;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
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

    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId) {
        try {
            long likeCount = likeService.like(hostHolder.get().getId(), Entitype.NEWS.getValue(), newsId);
            // 更新喜欢数
            newsService.updateLikeCount(newsId, (int) likeCount);
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
            // 更新喜欢数
            newsService.updateLikeCount(newsId, (int) likeCount);
            return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新喜欢数异常:" + e.getMessage());
            return ToutiaoUtil.getJSONString(1);
        }
    }
}
