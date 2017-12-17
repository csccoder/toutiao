package com.nowcoder.service;

public interface JedisService {
    Long sadd(String key,String value);

    Long srem(String key,String value);

    Boolean sismember(String key,String value);

    Long scard(String key);
}
