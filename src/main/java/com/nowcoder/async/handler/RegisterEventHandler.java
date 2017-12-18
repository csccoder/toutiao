package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.JedisService;
import com.nowcoder.util.MailSender;
import com.nowcoder.util.RedisKeyUtil;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class RegisterEventHandler implements EventHandler {
    @Autowired
    private MailSender mailSender;
    @Autowired
    JedisService jedisService;

    @Override
    public void doHandle(EventModel eventModel) {
        HashMap<String, Object> map = new HashMap<>();
        ViewObject viewObject = new ViewObject();
        String userId=eventModel.getExt("userId");
        viewObject.set("userId",eventModel.getExt("userId"));
        //生成邮件验证的token
        String token= UUID.randomUUID().toString().replaceAll("-","");
        viewObject.set("token",token);
        map.put("vo",viewObject);
        jedisService.set(RedisKeyUtil.getEmailActivateKey(userId),token, ToutiaoUtil.getEmailTokenExpireTime());
        mailSender.sendWithHTMLTemplate(eventModel.getExt("toEmail"),"激活邮件","/mails/welcome.html",map);
    }

    @Override
    public List<EventType> getSupports() {
        return Arrays.asList(EventType.REGISTER);
    }
}
