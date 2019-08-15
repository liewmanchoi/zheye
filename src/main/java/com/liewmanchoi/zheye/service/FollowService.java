package com.liewmanchoi.zheye.service;

import com.liewmanchoi.zheye.dao.RedisDAO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 关注服务
 *
 * @author wangsheng
 * @date 2019/8/11
 */
@Slf4j
@Service
public class FollowService {
  @Autowired private RedisDAO redisDAO;

  /** 用户关注某个实体：问题/评论/用户等 */
  public boolean follow(int userId, int entityType, int entityId) {
    return redisDAO.follow(userId, entityType, entityId);
  }

  public boolean unfollow(int userId, int entityType, int entityId) {
    return redisDAO.unfollow(userId, entityType, entityId);
  }

  public List<Integer> getFollowers(int entityType, int entityId, int count) {
    return getFollowers(entityType, entityId, 0, count);
  }

  public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
    return redisDAO.getFollowers(entityType, entityId, offset, count);
  }

  public long getFollowerCount(int entityType, int entityId) {
    return redisDAO.getFollowerCount(entityType, entityId);
  }

  public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
    return redisDAO.getFollowees(userId, entityType, offset, count);
  }

  public List<Integer> getFollowees(int userId, int entityType, int count) {
    return getFollowees(userId, entityType, 0, count);
  }

  public long getFolloweeCount(int userId, int entityType) {
    return redisDAO.getFolloweeCount(userId, entityType);
  }

  public boolean isFollower(int userId, int entityType, int entityId) {
    return redisDAO.isFollower(userId, entityType, entityId);
  }
}
