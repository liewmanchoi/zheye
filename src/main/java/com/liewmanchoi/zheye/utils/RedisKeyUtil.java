package com.liewmanchoi.zheye.utils;

/**
 * @author wangsheng
 * @date 2019/8/10
 */
public class RedisKeyUtil {
  private static final String SPLIT = ":";
  private static final String APP = "ZHEYE";
  private static final String BIZ_LIKE = "LIKE";
  private static final String BIZ_DISLIKE = "DISLIKE";
  /** 粉丝、关注者 */
  private static final String BIZ_FOLLOWER = "FOLLOWER";
  /** 大V、被关注者 */
  private static final String BIZ_FOLLOWEE = "FOLLOWEE";

  public static String getLikeKey(int entityType, int entityId) {
    return APP + SPLIT + BIZ_LIKE + SPLIT + entityType + SPLIT + entityId;
  }

  public static String getDislikeKey(int entityType, int entityId) {
    return APP + SPLIT + BIZ_DISLIKE + SPLIT + entityType + SPLIT + entityId;
  }

  public static String getFollowerKey(int entityType, int entityId) {
    return APP + SPLIT + BIZ_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
  }

  public static String getFolloweeKey(int userId, int entityType) {
    return APP + SPLIT + BIZ_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
  }
}
