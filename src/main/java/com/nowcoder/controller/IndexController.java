package com.nowcoder.controller;

import com.nowcoder.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

//@Controller
public class IndexController {

    @RequestMapping(path = {"/", "/index"})
    @ResponseBody
    public String index() {
        return "hello world";
    }

    @RequestMapping(value = "/test1/{userId}")
    @ResponseBody
    public String test1(@PathVariable("userId") String userId, int page, int size) {
        return "[userId]:" + userId + ",page:" + page + ",size:" + size;
    }

    @RequestMapping(value = "/test2/{userId}")
    @ResponseBody
    public String test2(@PathVariable("userId") String userId,
                        @RequestParam(defaultValue = "1") Integer page,
                        @RequestParam(defaultValue = "1") Integer size) {
        return "[userId]:" + userId + ",page:" + page + ",size:" + size;
    }

    @RequestMapping("view1")
    public String view1() {
        return "news";
    }

    @RequestMapping("view2")
    public String view2(Model model) {
        ArrayList addrs = new ArrayList<String>();
        addrs.add("hunan");
        addrs.add("beijing");
        addrs.add("shanghai");

        ArrayList users = new ArrayList<User>();
        users.add(new User(1, "jack"));
        users.add(new User(2, "peter"));
        users.add(new User(3, "marin"));

        model.addAttribute("addrs", addrs);
        model.addAttribute("users", users);
        return "news";
    }

    @RequestMapping("cookieValue")
    @ResponseBody
    public String jessionId(@CookieValue(name = "_ga", defaultValue = "未知") String jessionId) {
        return jessionId;
    }

    @RequestMapping("throw")
    public String throwExp() {
        throw new IllegalArgumentException("参数错误");
    }

    /**
     * 301 永久从定向， 浏览器会缓存内容，以后的请求会直接从缓存中读取
     * 302 临时从定向
     * @param code
     * @return
     */
    @RequestMapping("redirect/{code}")
    public RedirectView redirectView(@PathVariable int code) {
        RedirectView redirectView=new RedirectView("/");
        if(code!=302){
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return redirectView;
    }


}
