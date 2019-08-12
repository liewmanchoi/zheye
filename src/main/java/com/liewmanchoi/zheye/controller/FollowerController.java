package com.liewmanchoi.zheye.controller;

import com.liewmanchoi.zheye.model.EntityType;
import com.liewmanchoi.zheye.model.HostHolder;
import com.liewmanchoi.zheye.model.Question;
import com.liewmanchoi.zheye.model.User;
import com.liewmanchoi.zheye.model.ViewObject;
import com.liewmanchoi.zheye.service.FollowService;
import com.liewmanchoi.zheye.service.QuestionService;
import com.liewmanchoi.zheye.service.ReplyService;
import com.liewmanchoi.zheye.service.UserService;
import com.liewmanchoi.zheye.utils.JsonUtil;
import com.liewmanchoi.zheye.utils.JsonUtil.STATUS;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wangsheng
 * @date 2019/8/12
 */
@Slf4j
@Controller
public class FollowerController {
  @Autowired private FollowService followService;
  @Autowired private ReplyService replyService;
  @Autowired private QuestionService questionService;
  @Autowired private UserService userService;
  @Autowired private HostHolder hostHolder;
  // TODO: Event事件触发

  @RequestMapping(
      path = "/followUser",
      method = {RequestMethod.POST, RequestMethod.GET})
  @ResponseBody
  public String followUser(@RequestParam("userId") int userId) {
    if (hostHolder.getUser() == null) {
      return JsonUtil.getJSONString(999);
    }

    boolean result = followService.follow(hostHolder.getUser().getId(), EntityType.USER, userId);
    log.info("用户[{}]关注了用户[{}]", hostHolder.getUser().getId(), userId);
    // 返回当前用户关注的大V的数量
    return JsonUtil.getJSONString(
        result ? STATUS.SUCCESS : STATUS.FAILURE,
        String.valueOf(
            followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.USER)));
  }

  @RequestMapping(
      path = "/unfollowUser",
      method = {RequestMethod.POST})
  @ResponseBody
  public String unfollowUser(@RequestParam("userId") int userId) {
    if (hostHolder.getUser() == null) {
      return JsonUtil.getJSONString(999);
    }

    boolean result = followService.unfollow(hostHolder.getUser().getId(), EntityType.USER, userId);
    log.info("用户[{}]取消关注了用户[{}]", hostHolder.getUser().getId(), userId);
    return JsonUtil.getJSONString(
        result ? STATUS.SUCCESS : STATUS.FAILURE,
        String.valueOf(
            followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.USER)));
  }

  @RequestMapping(
      path = {"/followQuestion"},
      method = {RequestMethod.POST})
  @ResponseBody
  public String followQuestion(@RequestParam("questionId") int questionId) {
    if (hostHolder.getUser() == null) {
      return JsonUtil.getJSONString(999);
    }

    Question question = questionService.getById(questionId);
    if (question == null) {
      return JsonUtil.getJSONString(STATUS.FAILURE, "问题不存在");
    }

    // 关注问题
    boolean result =
        followService.follow(hostHolder.getUser().getId(), EntityType.QUESTION, questionId);
    log.info("用户[{}]关注了问题[{}]", hostHolder.getUser().getId(), questionId);

    Map<String, Object> info = new HashMap<>(4);
    // TODO: 此处命名可能会有问题
    info.put("headUrl", hostHolder.getUser().getAvatarUrl());
    info.put("name", hostHolder.getUser().getName());
    info.put("id", hostHolder.getUser().getId());
    info.put("count", followService.getFollowerCount(EntityType.QUESTION, questionId));

    return JsonUtil.getJSONString(result ? STATUS.SUCCESS : STATUS.FAILURE, info);
  }

  @RequestMapping(
      path = {"/unfollowQuestion"},
      method = {RequestMethod.POST})
  @ResponseBody
  public String unfollowQuestion(@RequestParam("questionId") int questionId) {
    if (hostHolder.getUser() == null) {
      return JsonUtil.getJSONString(999);
    }

    Question question = questionService.getById(questionId);
    if (question == null) {
      return JsonUtil.getJSONString(STATUS.FAILURE, "问题不存在");
    }

    // 关注问题
    boolean result =
        followService.unfollow(hostHolder.getUser().getId(), EntityType.QUESTION, questionId);
    log.info("用户[{}]取消关注了问题[{}]", hostHolder.getUser().getId(), questionId);

    Map<String, Object> info = new HashMap<>(2);
    info.put("id", hostHolder.getUser().getId());
    info.put("count", followService.getFollowerCount(EntityType.QUESTION, questionId));

    return JsonUtil.getJSONString(result ? STATUS.SUCCESS : STATUS.FAILURE, info);
  }

  @RequestMapping(
      path = {"/user/{uid}/followers"},
      method = {RequestMethod.GET})
  public String followers(Model model, @PathVariable("uid") int userId) {
    List<Integer> followerIds = followService.getFollowers(EntityType.USER, userId, 0, 10);

    if (hostHolder.getUser() != null) {
      model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
    } else {
      model.addAttribute("followers", getUsersInfo(0, followerIds));
    }

    model.addAttribute("followerCount", followService.getFollowerCount(EntityType.USER, userId));
    model.addAttribute("curUser", userService.getUser(userId));
    return "followers";
  }

  @RequestMapping(
      path = {"/user/{uid}/followees"},
      method = {RequestMethod.GET})
  public String followees(Model model, @PathVariable("uid") int userId) {
    List<Integer> followeeIds = followService.getFollowees(userId, EntityType.USER, 0, 10);

    if (hostHolder.getUser() != null) {
      model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
    } else {
      model.addAttribute("followees", getUsersInfo(0, followeeIds));
    }

    model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.USER));
    model.addAttribute("curUser", userService.getUser(userId));
    return "followees";
  }

  private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
    log.info("getUsersInfo输入参数：localUserId[{}], userIds[{}]",localUserId,userIds);
    List<ViewObject> userInfos = new ArrayList<>();
    for (Integer uid : userIds) {
      User user = userService.getUser(uid);

      if (user == null) {
        // 如果查找不到相应的用户，则跳过之
        continue;
      }

      ViewObject viewObject = new ViewObject();
      viewObject.set("user", user);
      // 发表的评论数
      viewObject.set("commentCount", replyService.getUserReplyCount(uid));
      // 有多少关注者
      viewObject.set("followerCount", followService.getFollowerCount(EntityType.USER, uid));
      // 关注了多少人
      viewObject.set("followeeCount", followService.getFolloweeCount(uid, EntityType.USER));
      if (localUserId != 0) {
        // 如果用户已经登陆
        viewObject.set("followed", followService.isFollower(localUserId, EntityType.USER, uid));
      } else {
        viewObject.set("followed", false);
      }
      userInfos.add(viewObject);
    }

    log.info("调用getUsersInfo，结果为[{}]", userInfos);
    return userInfos;
  }
}
