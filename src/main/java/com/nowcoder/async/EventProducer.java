package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.service.JedisService;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 事件的生产者，负责发送事件到redis任务队列
 */
@Service
public class EventProducer {
    @Autowired
    JedisService jedisService;
    public boolean fireEvent(EventModel eventModel){
        try{
            String key=RedisKeyUtil.getEventQueueKey();
            String value=JSONObject.toJSONString(eventModel);
            jedisService.lpush(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
