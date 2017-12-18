package com.nowcoder.service.impl;

import com.nowcoder.service.JedisService;
import com.nowcoder.util.JedisCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;

@Service
public class JedisServiceImpl implements JedisService, InitializingBean {
    private Logger logger= LoggerFactory.getLogger(getClass());

    private JedisPool jedisPool;

    public<T> T run(JedisCallback<T> callback){
        Jedis jedis =null;
        try{
            jedis=jedisPool.getResource();
            return callback.callback(jedis);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("jedis操作异常:"+e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    @Override
    public Long sadd(String key, String value) {
        return run(jedis -> jedis.sadd(key,value));
    }

    @Override
    public Long srem(String key, String value) {
       return run(jedis -> jedis.srem(key,value));
    }

    @Override
    public Boolean sismember(String key, String value) {
        return run(jedis -> jedis.sismember(key,value));
    }

    @Override
    public Long scard(String key) {
        return run(jedis -> jedis.scard(key));
    }

    @Override
    public Long lpush(String key, String value) {
        return run(new JedisCallback<Long>() {
            @Override
            public Long callback(Jedis jedis) {
                return jedis.lpush(key,value);
            }
        });
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        return run(new JedisCallback<List<String>>() {
            @Override
            public List<String> callback(Jedis jedis) {
                return jedis.brpop(timeout, key);
            }
        });
    }

    @Override
    public void set(String key, String value) {
         run(new JedisCallback<Object>() {
             @Override
             public Object callback(Jedis jedis) {
                 jedis.set(key, value);
                 return null;
             }
         });
    }

    @Override
    public void set(String key, String value, int time) {
        run(new JedisCallback<Object>() {
            @Override
            public Object callback(Jedis jedis) {
                jedis.set(key,value);
                jedis.expire(key,time);
                return null;
            }
        });
    }

    @Override
    public void expire(String key, int expire) {
        run(new JedisCallback<Object>() {
            @Override
            public Object callback(Jedis jedis) {
                jedis.expire(key,expire);
                return null;
            }
        });
    }

    @Override
    public String get(String key) {
        return run(new JedisCallback<String>() {
            @Override
            public String callback(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
    }
}
