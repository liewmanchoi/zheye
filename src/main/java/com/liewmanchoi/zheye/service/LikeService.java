package com.liewmanchoi.zheye.service;

import com.liewmanchoi.zheye.dao.RedisDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 赞踩服务
 *
 * @author wangsheng
 * @date 2019/8/10
 */
@Slf4j
@Service
public class LikeService {
  @Autowired private RedisDAO redisDAO;

  public long getLikeCount(int entityType, int entityId) {
    return redisDAO.getLikeCount(entityType, entityId);
  }

  public int getLikeStatus(int userId, int entityType, int entityId) {
    return redisDAO.getLikeStatus(userId, entityType, entityId);
  }

  public long like(int userId, int entityType, int entityId) {
    redisDAO.like(userId, entityType, entityId);

    return getLikeCount(entityType, entityId);
  }

  public long dislike(int userId, int entityType, int entityId) {
    redisDAO.dislike(userId, entityType, entityId);

    return getLikeCount(entityType, entityId);
  }
}
