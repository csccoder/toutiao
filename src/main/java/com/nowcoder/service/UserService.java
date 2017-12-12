package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.ToutiaoUtil;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    private static final int PASSWORD_DEFAULT_LENGTH=6;


    public User selectUserById(int userId){
        return userDAO.selectUserById(userId);
    }

    /**
     * 注册用户
     * a.判空检测
     * b.密码长度校验
     * c.用户名合法性检查
     *      c1.是否唯一
     *      c2.是否包含特殊字符
     *      c3.是否包含敏感词
     * d.注册
     * @param username
     * @param password
     * @return
     */
    public Map<String,String> register(String username,String password,int remember){
        Map<String,String> map=new HashMap<>();
        //判空检测
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }

        //密码长度校验
        if(StringUtils.length(password)<PASSWORD_DEFAULT_LENGTH){
            map.put("msgpwd",String.format("密码长度不能少于%d位",PASSWORD_DEFAULT_LENGTH));
            return map;
        }

        // TODO: 17-12-11 用户名包含特殊符号校验
        // TODO: 17-12-11 用户名敏感词校验

        if(userDAO.selectUserByName(username)!=null){
            map.put("msgname","用户名已被注册");
            return map;
        }

        Random random = new Random();

        User user = new User();
        user.setName(username);
        user.setEmail(String.format("%s@qq.com", UUID.randomUUID().toString().substring(0,8)));
        user.setSalt(UUID.randomUUID().toString().substring(0,8));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(100)));

        userDAO.addUser(user);

        //login_ticket
        map.put("ticket",addLoginTicket(user,remember));

        return map;
    }

    public Map<String,String> login(String username,String password,int remember){
        Map<String,String> map=new HashMap<>();
        //判空检测
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }

        //密码长度校验
        if(StringUtils.length(password)<PASSWORD_DEFAULT_LENGTH){
            map.put("msgpwd",String.format("密码长度不能少于%d位",PASSWORD_DEFAULT_LENGTH));
            return map;
        }

        User user=userDAO.selectUserByName(username);
        if(user == null){
            map.put("msgname","用户名不存在");
            return map;
        }

        if(!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码错误");
            return map;
        }

        //login_ticket
        map.put("ticket",addLoginTicket(user,remember));
        return map;
    }

    public String addLoginTicket(User user,int remember){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(user.getId());
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        ticket.setExpired(new Date(System.currentTimeMillis()+ToutiaoUtil.getLoginTicketExpired(remember)));
        ticket.setStatus(0);
        loginTicketDAO.addLoginTicket(ticket);
        return ticket.getTicket();
    }


    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket,1);
    }
}
