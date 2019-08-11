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

  public static String getLikeKey(int entityType, int entityId) {
    return APP + SPLIT + BIZ_LIKE + SPLIT + entityType + SPLIT + entityId;
  }

  public static String getDislikeKey(int entityType, int entityId) {
    return APP + SPLIT + BIZ_DISLIKE + SPLIT + entityType + SPLIT + entityId;
  }
}
