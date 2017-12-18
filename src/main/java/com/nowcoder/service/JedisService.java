package com.nowcoder.service;

import java.util.List;

public interface JedisService {
    Long sadd(String key,String value);

    Long srem(String key,String value);

    Boolean sismember(String key,String value);

    Long scard(String key);

    Long lpush(String key,String value);

    List<String> brpop(int timeout, String key);

    void set(String key,String value);

    void set(String key,String value,int expire);

    void expire(String key,int expire);

    String get(String key);
}
