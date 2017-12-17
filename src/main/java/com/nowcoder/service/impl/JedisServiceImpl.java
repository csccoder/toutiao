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
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
    }
}
