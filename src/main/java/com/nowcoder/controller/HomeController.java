package com.nowcoder.controller;

import com.nowcoder.model.Entitype;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController extends BaseController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;

    public List<ViewObject> getNews(int userId, int offset, int limit) {
        List<ViewObject> vos = new ArrayList<ViewObject>();
        List<News> latestNews = newsService.getLatestNews(userId, offset, limit);
        for (News news : latestNews) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.selectUserById(news.getUserId()));
            if(hostHolder.get()!=null){
                vo.set("like",likeService.likeStatus(hostHolder.get().getId(), Entitype.NEWS.getValue(),news.getId()));
            }else {
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/", "/index"})
    public String index(Model model, @RequestParam(value = "pop",defaultValue = "0") int pop) {
        model.addAttribute("vos", getNews(0, 0, 10));
        model.addAttribute("pop",pop);

        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }
}
