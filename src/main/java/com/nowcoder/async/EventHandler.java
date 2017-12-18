package com.nowcoder.async;

import java.util.List;

/**
 * 事件处理器接口
 */
public interface EventHandler {
    /**
     * 处理事件调用的方法
     * @param eventModel
     */
    void doHandle(EventModel eventModel);

    /**
     * 获取当前事件处理器支持处理的事件类型
     * @return
     */
    List<EventType> getSupports();
}
