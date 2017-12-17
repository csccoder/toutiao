package com.nowcoder.service.impl;

import com.nowcoder.service.JedisService;
import com.nowcoder.service.LikeService;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    JedisService jedisService;

    @Override
    public Long like(int userId, int entityType, int entityId) {
        //把userId添加到喜欢的集合
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisService.sadd(likeKey, String.valueOf(userId));
        //从不喜欢集合中删除userId
        String disLike = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisService.srem(disLike, String.valueOf(userId));

        return jedisService.scard(likeKey);
    }

    @Override
    public Long disLike(int userId, int entityType, int entityId) {
        //把userId加入到不喜欢的集合中
        String disLike = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisService.sadd(disLike, String.valueOf(userId));
        //从喜欢的集合中把userId移除
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisService.srem(likeKey, String.valueOf(userId));

        return jedisService.scard(likeKey);
    }

    @Override
    public int likeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        if(jedisService.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }

        String disLike = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        return jedisService.sismember(disLike,String.valueOf(userId))?-1:0;
    }
}
