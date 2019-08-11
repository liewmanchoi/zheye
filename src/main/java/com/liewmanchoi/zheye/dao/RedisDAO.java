package com.liewmanchoi.zheye.dao;

import com.liewmanchoi.zheye.utils.RedisKeyUtil;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

/**
 * @author wangsheng
 * @date 2019/8/10
 */
@Slf4j
@Repository
public class RedisDAO {
  @Autowired
  @Qualifier(value = "likeServiceOperations")
  private SetOperations<String, Integer> likeServiceOperations;

  /** 获取赞的数量 */
  public long getLikeCount(int entityType, int entityId) {
    String key = RedisKeyUtil.getLikeKey(entityType, entityId);
    Long size = likeServiceOperations.size(key);

    log.info("getLikeCount, result: [{}]", size);
    return Optional.ofNullable(size).orElse(0L);
  }

  /** 获取点赞状态(1：点赞；-1：点踩；0：未评价） */
  public int getLikeStatus(int userId, int entityType, int entityId) {
    log.info("getLikeStatus:  [{}], [{}], [{}]", userId, entityType, entityId);
    String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
    String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);

    if (Optional.ofNullable(likeServiceOperations.isMember(likeKey, userId)).orElse(false)) {
      return 1;
    }

    if (Optional.ofNullable(likeServiceOperations.isMember(dislikeKey, userId)).orElse(false)) {
      return -1;
    }

    return 0;
  }

  /** 点赞 */
  public void like(int userId, int entityType, int entityId) {
    log.info("like: [{}], [{}], [{}]", userId, entityType, entityId);
    String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
    likeServiceOperations.add(likeKey, userId);

    String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
    likeServiceOperations.remove(dislikeKey, userId);
  }

  /** 点踩 */
  public void dislike(int userId, int entityType, int entityId) {
    log.info("dislike: [{}], [{}], [{}]", userId, entityType, entityId);
    String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
    likeServiceOperations.remove(likeKey, userId);

    String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
    likeServiceOperations.add(dislikeKey, userId);
  }
}
