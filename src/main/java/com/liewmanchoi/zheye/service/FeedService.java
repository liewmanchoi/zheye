package com.liewmanchoi.zheye.service;

import com.liewmanchoi.zheye.dao.FeedDAO;
import com.liewmanchoi.zheye.dao.RedisDAO;
import com.liewmanchoi.zheye.model.EntityType;
import com.liewmanchoi.zheye.model.Feed;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangsheng
 * @date 2019/8/14
 */
@Slf4j
@Service
public class FeedService {
  @Autowired private FeedDAO feedDAO;
  @Autowired private RedisDAO redisDAO;
  @Autowired private FollowService followService;

  /** 用于拉模式 */
  public List<Feed> pullFeeds(int userId, int count) {
    // 获取userId所关注的大V列表
    List<Integer> followees =
        followService.getFollowees(userId, EntityType.USER, Integer.MAX_VALUE);
    log.info("用户[{}]关注的大V数量为[{}]", userId, followees.size());
    // 获取所有大V的发件箱的有序集合
    Set<Integer> feedIds = new TreeSet<>(Comparator.reverseOrder());

    for (int followee : followees) {
      feedIds.addAll(redisDAO.getFeedOutbox(followee));
    }

    if (feedIds.isEmpty()) {
      return null;
    }

    return feedDAO.getFeedsById(feedIds.stream().limit(count).collect(Collectors.toList()));
  }

  public List<Feed> getInboxFeeds(int userId, int count) {
    List<Integer> feedIds = redisDAO.getFeedInbox(userId, -1 * count, -1);
    if (feedIds.isEmpty()) {
      return null;
    }

    return feedDAO.getFeedsById(feedIds.stream().limit(count).collect(Collectors.toList()));
  }

  public void refreshBox(int userId, int feedId) {
    // 1. 更新userId的发件箱
    redisDAO.addFeedOutbox(userId, feedId);
    // 2. 更新userId对应的followers的收件箱
    List<Integer> followers =
        followService.getFollowers(EntityType.USER, userId, Integer.MAX_VALUE);
    for (int follower : followers) {
      redisDAO.addFeedInbox(follower, feedId);
    }
  }

  public int addFeed(Feed feed) {
    return feedDAO.addFeed(feed);
  }

  public Feed getFeedById(int id) {
    return feedDAO.getFeedById(id);
  }
}
