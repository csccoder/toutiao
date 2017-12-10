package com.nowcoder.controller;

import com.nowcoder.model.News;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;

    public List<ViewObject> getNews(int userId,int offset,int limit){
        List<ViewObject> vos=new ArrayList<ViewObject>();
        List<News> latestNews = newsService.getLatestNews(userId, offset, limit);
        for(News news:latestNews){
            ViewObject vo = new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.selectUserById(news.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path={"/","/index"})
    public String index(Model model){
        model.addAttribute("vos",getNews(0,0,10));
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"})
    public String userIndex(Model model, @RequestParam(value = "userId") int userId){
        model.addAttribute("vos",getNews(userId,0,10));
        return "home";
    }
}
