package com.liewmanchoi.zheye.dao;

import com.liewmanchoi.zheye.utils.RedisKeyUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

/**
 * @author wangsheng
 * @date 2019/8/10
 */
@Slf4j
@Repository
public class RedisDAO {
  @Autowired
  @Qualifier(value = "stringIntegerTemplate")
  private RedisTemplate<String, Integer> redisTemplate;

  @Autowired
  @Qualifier(value = "likeServiceOperations")
  private SetOperations<String, Integer> likeServiceOperations;

  @Autowired
  @Qualifier(value = "followerServiceOperations")
  private ZSetOperations<String, Integer> followerServiceOperations;

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

  /** 关注某个实体 */
  public boolean follow(int userId, int entityType, int entityId) {
    log.info("用户[{}]关注类型为[{}]的实体[{}]", userId, entityType, entityId);
    String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
    String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

    Date date = new Date();
    final long time = date.getTime();

    List<Object> result =
        redisTemplate.execute(
            new SessionCallback<List<Object>>() {
              @Override
              public <K, V> List<Object> execute(RedisOperations<K, V> operations)
                  throws DataAccessException {
                // 开启事务
                redisTemplate.multi();
                // 增加当前用户作为实体的follower
                followerServiceOperations.add(followerKey, userId, time);
                // 将实体加入到当前用户的关注列表
                followerServiceOperations.add(followeeKey, entityId, time);
                return redisTemplate.exec();
              }
            });

    return result != null
        && result.size() == 2
        && result.get(0).equals(Boolean.TRUE)
        && result.get(1).equals(Boolean.TRUE);
  }

  /** 取消关注某个实体 */
  public boolean unfollow(int userId, int entityType, int entityId) {
    log.info("用户[{}]取消关注类型为[{}]的实体[{}]", userId, entityType, entityId);
    String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
    String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

    List<Object> result =
        redisTemplate.execute(
            new SessionCallback<List<Object>>() {
              @Override
              public <K, V> List<Object> execute(RedisOperations<K, V> operations)
                  throws DataAccessException {
                // 开启事务
                redisTemplate.multi();
                // 将当前用户从实体的follower列表中删除
                followerServiceOperations.remove(followerKey, userId);
                // 将实体从当前用户的关注列表中删除
                followerServiceOperations.remove(followeeKey, entityId);
                return redisTemplate.exec();
              }
            });

    return result != null
        && result.size() == 2
        && (Long) result.get(0) > 0
        && (Long) result.get(1) > 0;
  }

  public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
    log.info("获取类型为[{}]的实体[{}]的粉丝", entityType, entityId);
    String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
    Set<Integer> result =
        followerServiceOperations.rangeByScore(followerKey, offset, offset + count);
    return result != null ? new ArrayList<>(result) : null;
  }

  /** 获取实体的follower数量 */
  public long getFollowerCount(int entityType, int entityId) {
    log.info("获取类型为[{}]的实体[{}]的粉丝数量", entityType, entityId);
    String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
    return Optional.ofNullable(followerServiceOperations.zCard(followerKey)).orElse(0L);
  }

  public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
    log.info("获取用户[{}]关注的类型为[{}]的实体", userId, entityType);
    String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
    Set<Integer> result =
        followerServiceOperations.rangeByScore(followeeKey, offset, offset + count);
    return result != null ? new ArrayList<>(result) : null;
  }

  /** 获取当前用户userId关注的实体的数量 */
  public long getFolloweeCount(int userId, int entityType) {
    log.info("获取用户[{}]关注的类型为[{}]的实体数量", userId, entityType);
    String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
    return Optional.ofNullable(followerServiceOperations.zCard(followeeKey)).orElse(0L);
  }

  /** 判断当前用户是否是某个实体的粉丝 */
  public boolean isFollower(int userId, int entityType, int entityId) {
    String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
    boolean result = followerServiceOperations.score(followerKey, userId) != null;
    log.info("用户[{}]是类型为[{}]的实体[{}]的粉丝：[{}]", userId, entityType, entityId, result);
    return result;
  }
}
