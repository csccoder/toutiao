package com.nowcoder.model;

import java.util.HashMap;

/**
 *  用来包装model数据的类
 *  方便视图层取controller传过来的数据
 */
public class ViewObject {
    private HashMap<String,Object> map=new HashMap<>();

    public void set(String key,Object value){
        map.put(key,value);
    }

    public Object get(String key){
        return map.get(key);
    }
}
