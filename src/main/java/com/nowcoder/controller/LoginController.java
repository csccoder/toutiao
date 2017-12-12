package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController extends BaseController {


    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/reg"}, method = RequestMethod.POST)
    @ResponseBody
    public String reg(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0") int remember,
                      HttpServletResponse response) {
       try{
           Map<String, String> msg = userService.register(username, password,remember);
           if (msg.containsKey("ticket")) {
               Cookie cookie = new Cookie("ticket", msg.get("ticket"));
               cookie.setPath("/");
               if(remember>0){
                   cookie.setMaxAge(ToutiaoUtil.getLoginTicketExpired(remember)/1000);
               }
               response.addCookie(cookie);
               return ToutiaoUtil.getJSONString(0, "注册成功");
           } else {
               return ToutiaoUtil.getJSONString(1, msg);
           }
       }catch (Exception e){
           logger.error("注册异常:"+e.getMessage());
           return ToutiaoUtil.getJSONString(1,"注册异常");
       }

    }

    @RequestMapping(path = {"/login"}, method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember",defaultValue = "0") int remember,
                        HttpServletResponse response) {
        try{
            Map<String, String> msg = userService.login(username, password,remember);
            if(msg.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", msg.get("ticket"));
                cookie.setPath("/");
                if(remember>0){
                    cookie.setMaxAge(ToutiaoUtil.getLoginTicketExpired(remember)/1000);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0,"登录成功");
            }else{
                return ToutiaoUtil.getJSONString(1,msg);
            }
        }catch (Exception e){
            logger.error("登录异常:"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登录异常");
        }
    }


    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket")String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

}
