package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务事件的包装类
 *
 * 链式设计，执行set方法会返回当前对象，方便链式方法调用
 */
public class EventModel {
    private EventType eventType;
    private int actorId;//事件的触发者

    //这两个entity属性可以抽象表示任何一个资源，这里表示事件的核心
    private int entityType;//事件所属实体的类型
    private int entityId;//事件所属实体的id
    private int entityOwnerId;//实体的拥有者

    private Map<String,String> exts=new HashMap<>();//触发事件时，需要传递的现场信息

    public EventModel() {
    }

    public EventModel(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> ext) {
        this.exts= ext;
        return this;
    }

    public EventModel setExt(String key,String value){
        exts.put(key,value);
        return this;
    }

    public String getExt(String key){
        return exts.get(key);
    }
}
