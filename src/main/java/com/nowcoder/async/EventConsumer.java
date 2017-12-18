package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.service.JedisService;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件的消费者，负责从jedis阻塞任务队列获取任务，并将任务委托给对应的事件处理器处理
 */
@Service
public class EventConsumer implements InitializingBean ,ApplicationContextAware{
    private Map<EventType,List<EventHandler>> mappings=new HashMap<>();
    private ApplicationContext applicationContext;
    @Autowired
    JedisService jedisService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> handlerMap = applicationContext.getBeansOfType(EventHandler.class);
        for(Map.Entry<String,EventHandler> entry:handlerMap.entrySet()){
            EventHandler handler=entry.getValue();
            List<EventType> supports = handler.getSupports();
            for(EventType eventType:supports){
                if(!mappings.containsKey(eventType)){
                    mappings.put(eventType,new ArrayList<>());
                }
                mappings.get(eventType).add(handler);
            }
        }

        Thread thread =new Thread(){
            @Override
            public void run() {
                super.run();
               while (true){
                   //从阻塞队列获取任务，如果阻塞队列为空，会阻塞此线程直至获得任务
                   List<String> list = jedisService.brpop(0, RedisKeyUtil.getEventQueueKey());
                   EventModel eventModel=null;
                   for(String result:list){
                       if(RedisKeyUtil.getEventQueueKey().equals(result)){
                           continue;
                       }
                       eventModel= JSONObject.parseObject(result,EventModel.class);
                       break;
                   }

                   List<EventHandler> eventHandlers = mappings.get(eventModel.getEventType());
                   for(EventHandler eventHandler:eventHandlers){
                       eventHandler.doHandle(eventModel);
                   }
               }
            }
        };
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
