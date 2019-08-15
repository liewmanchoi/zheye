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
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wangsheng
 * @date 2019/5/15
 */
@Controller
@Slf4j
public class HomeController {
  @Autowired private QuestionService questionService;
  @Autowired private UserService userService;
  @Autowired private FollowService followService;
  @Autowired private ReplyService replyService;
  @Autowired private HostHolder hostHolder;

  private List<ViewObject> getQuestions(int userId, int offset, int limit) {
    List<Question> questions = questionService.getLatestQuestions(userId, offset, limit);
    List<ViewObject> vos = new ArrayList<>();

    for (Question question : questions) {
      ViewObject viewObject = new ViewObject();
      viewObject.set("question", question);
      viewObject.set(
          "followCount", followService.getFollowerCount(EntityType.QUESTION, question.getId()));
      viewObject.set("user", userService.getUser(question.getUserId()));
      vos.add(viewObject);
    }

    return vos;
  }

  @RequestMapping(
      path = {"/", "/index"},
      method = {RequestMethod.GET, RequestMethod.POST})
  public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop) {
    model.addAttribute("vos", getQuestions(0, 0, 10));
    return "index";
  }

  @RequestMapping(
      path = {"/user/{userId}"},
      method = {RequestMethod.GET, RequestMethod.POST})
  public String userIndex(Model model, @PathVariable("userId") int userId) {
    model.addAttribute("vos", getQuestions(userId, 0, 10));
    User user = userService.getUser(userId);
    ViewObject vo = new ViewObject();
    vo.set("user", user);
    vo.set("commentCount", replyService.getUserReplyCount(userId));
    vo.set("followerCount", followService.getFollowerCount(EntityType.USER, userId));
    vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.USER));
    if (hostHolder.getUser() != null) {
      vo.set(
          "followed",
          followService.isFollower(hostHolder.getUser().getId(), EntityType.USER, userId));
    } else {
      vo.set("followed", false);
    }
    model.addAttribute("profileUser", vo);

    return "profile";
  }
}
