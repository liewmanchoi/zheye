package com.liewmanchoi.zheye.event.handler;

import com.liewmanchoi.zheye.event.Event;
import com.liewmanchoi.zheye.event.EventHandler;
import com.liewmanchoi.zheye.event.EventType;
import com.liewmanchoi.zheye.model.Feed;
import com.liewmanchoi.zheye.model.User;
import com.liewmanchoi.zheye.service.FeedService;
import com.liewmanchoi.zheye.service.FollowService;
import com.liewmanchoi.zheye.service.QuestionService;
import com.liewmanchoi.zheye.service.UserService;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangsheng
 * @date 2019/8/14
 */
@Slf4j
@Component
public class FeedHandler implements EventHandler {
  @Autowired private FollowService followService;
  @Autowired private UserService userService;
  @Autowired private FeedService feedService;
  @Autowired private QuestionService questionService;

  @Override
  public void doHandle(Event event) {
    log.info("准备处理事件[{}]", event);
    Feed feed = new Feed();
    feed.setCreatedDate(new Date());
    feed.setType(event.getOperation().getValue());
    feed.setActorId(event.getActorId());
    feed.setEntityType(event.getEntityType());
    feed.setEntityId(event.getEntityId());
    feed.setStatus(0);
    Map<String, Object> map = new HashMap<>(4);

    User actor = userService.getUser(event.getActorId());
    if (actor == null) {
      return;
    }
    map.put("userId", actor.getId());
    map.put("userHead", actor.getAvatarUrl());
    map.put("userName", actor.getName());
    feed.setSummaryData(map);

    // 将feed加入到数据库
    feedService.addFeed(feed);
    log.info("feedId=[{}]",feed.getId());
    // 更新收、发件箱
    feedService.refreshBox(event.getActorId(), feed.getId());
    log.info("执行完毕");
  }

  @Override
  public List<EventType> getSupportedEventTypes() {
    return Arrays.asList(EventType.FOLLOW, EventType.COMMENT);
  }
}
