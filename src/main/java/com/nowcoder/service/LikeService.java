package com.nowcoder.service;

public interface LikeService {
    Long like(int userId,int entityType,int entityId);
    Long disLike(int userId,int entityType,int entityId);
    int likeStatus(int userId,int entityType,int entityId);
}
