package com.liewmanchoi.zheye.controller;

import com.liewmanchoi.zheye.model.Feed;
import com.liewmanchoi.zheye.model.HostHolder;
import com.liewmanchoi.zheye.service.FeedService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author wangsheng
 * @date 2019/8/14
 */
@Slf4j
@Controller
public class FeedController {
  @Autowired private HostHolder hostHolder;
  @Autowired private FeedService feedService;

  @RequestMapping(
      path = {"/pushfeeds"},
      method = {RequestMethod.GET, RequestMethod.POST})
  public String getPushFeeds(Model model) {
    int localUserId = hostHolder.getUser().getId();
    List<Feed> feeds = feedService.getInboxFeeds(localUserId, 10);
    model.addAttribute("feeds", feeds);
    return "feeds";
  }

  @RequestMapping(
      path = {"/pullfeeds"},
      method = {RequestMethod.GET, RequestMethod.POST})
  public String getPullFeeds(Model model) {
    int localUserId = hostHolder.getUser().getId();

    List<Feed> feeds = feedService.pullFeeds(localUserId, 10);
    model.addAttribute("feeds", feeds);
    return "feeds";
  }
}
