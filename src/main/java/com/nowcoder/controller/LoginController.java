package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.service.UserService;
import com.nowcoder.util.MailSender;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController extends BaseController {


    @Autowired
    private UserService userService;

    @Autowired
    MailSender mailSender;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg"}, method = RequestMethod.POST)
    @ResponseBody
    public String reg(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("email") String email,
                      @RequestParam(value = "rember", defaultValue = "0") int remember,
                      HttpServletResponse response) {
        try {
            Map<String, String> msg = userService.register(username, email, password, remember);
            if (msg.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", msg.get("ticket"));
                cookie.setPath("/");
                if (remember > 0) {
                    cookie.setMaxAge(ToutiaoUtil.getLoginTicketExpired(remember) / 1000);
                }
                response.addCookie(cookie);
                //异步发送注册通知邮件
                eventProducer.fireEvent(new EventModel(EventType.REGISTER).setExt("userId", msg.get("userId")).setExt("username", username).setExt("toEmail", email));
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, msg);
            }
        } catch (Exception e) {
            logger.error("注册异常:" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }

    }

    @RequestMapping(path = {"/login"}, method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("email") String email,
                        @RequestParam(value = "rember", defaultValue = "0") int remember,
                        HttpServletResponse response) {
        try {
            Map<String, String> msg = userService.login(username, email, password, remember);
            if (msg.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", msg.get("ticket"));
                cookie.setPath("/");
                if (remember > 0) {
                    cookie.setMaxAge(ToutiaoUtil.getLoginTicketExpired(remember) / 1000);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "登录成功");
            } else if (msg.containsKey("activation")) {
                response.sendRedirect("/noactivation.vm");
                return null;
            } else {
                return ToutiaoUtil.getJSONString(1, msg);
            }
        } catch (Exception e) {
            logger.error("登录异常:" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "登录异常");
        }
    }


    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

    @RequestMapping(path = "/user/activation", method = RequestMethod.GET)
    public String activate(@RequestParam("userId") int userId, @RequestParam("token") String token, Model model) {
        Map<String, String> msg = userService.activate(userId, token);
        if (msg.containsKey("success")) {
            return "redirect:/activation.html";
        } else if (msg.containsKey("expire")) {
            model.addAttribute("msg", msg.get("expire"));
            //重新发送激活邮件
            String email = userService.selectUserById(userId).getEmail();
            eventProducer.fireEvent(new EventModel(EventType.REGISTER).setExt("userId", String.valueOf(userId)).setExt("toEmail", email));
            return "msg";
        } else {
            model.addAttribute("msg", msg.get("errorMsg"));
            return "msg";
        }

    }
}
