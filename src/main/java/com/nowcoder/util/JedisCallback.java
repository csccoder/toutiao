package com.nowcoder.util;

import redis.clients.jedis.Jedis;

public interface JedisCallback<T> {
    T callback(Jedis jedis);
}
