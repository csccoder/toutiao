package com.nowcoder.service.impl;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.service.JedisService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.RedisKeyUtil;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements com.nowcoder.service.UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private JedisService jedisService;

    private static final int PASSWORD_DEFAULT_LENGTH=6;


    @Override
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
    @Override
    public Map<String,String> register(String username,String email, String password, int remember){
        Map<String,String> map=new HashMap<>();
        //判空检测
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }

        if(StringUtils.isBlank(email)){
            map.put("msgemail","邮箱不能为空");
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

        if(userDAO.selectUserByEmail(email)!=null){
            map.put("msgemail","邮箱已被使用");
            return map;
        }

        Random random = new Random();

        User user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setSalt(UUID.randomUUID().toString().substring(0,8));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(100)));

        userDAO.addUser(user);

        //login_ticket
        map.put("ticket",addLoginTicket(user,remember));
        map.put("userId",String.valueOf(user.getId()));

        return map;
    }

    @Override
    public Map<String,String> login(String username,String email, String password, int remember){
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

        if(StringUtils.isBlank(email)){
            map.put("msgemail","邮箱不能为空");
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
        //判断用户的状态
        if(user.getStatus()!=1){
            map.put("activation","no");//邮箱未激活
            return map;
        }

        //login_ticket
        map.put("ticket",addLoginTicket(user,remember));
        return map;
    }

    @Override
    public String addLoginTicket(User user, int remember){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(user.getId());
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        ticket.setExpired(new Date(System.currentTimeMillis()+ToutiaoUtil.getLoginTicketExpired(remember)));
        ticket.setStatus(0);
        loginTicketDAO.addLoginTicket(ticket);
        return ticket.getTicket();
    }


    @Override
    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket,1);
    }

    @Override
    public Map<String,String> activate(int userId, String token) {
        HashMap<String, String> map = new HashMap<>();
        //判空
        if(userId==0||StringUtils.isBlank(token)){
            map.put("errorMsg","操作非法");
            return map;
        }
        //判断当前用户的状态
        User user = userDAO.selectUserById(userId);
        if(user ==null){//账号不存在
            map.put("errorMsg","操作非法");
            return map;
        }
        if(user.getStatus()>0){//账号已激活
             map.put("errorMsg","此账号已激活");
             return map;
        }

        String value = jedisService.get(RedisKeyUtil.getEmailActivateKey(String.valueOf(userId)));
        if(StringUtils.isBlank(value)){
            map.put("expire","链接已过期，已重新发送激活邮件至邮箱。请前往邮箱激活");
            return map;
        }

        if(!token.equals(value)){
            map.put("errorMsg","操作非法");
            return map;
        }else{
            map.put("success","激活成功");
            userDAO.updateStatus(userId,1);
            return map;
        }




    }
}
