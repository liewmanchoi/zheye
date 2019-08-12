package com.liewmanchoi.zheye.controller;

import com.liewmanchoi.zheye.model.EntityType;
import com.liewmanchoi.zheye.model.HostHolder;
import com.liewmanchoi.zheye.model.Question;
import com.liewmanchoi.zheye.model.Reply;
import com.liewmanchoi.zheye.model.User;
import com.liewmanchoi.zheye.model.ViewObject;
import com.liewmanchoi.zheye.service.FollowService;
import com.liewmanchoi.zheye.service.LikeService;
import com.liewmanchoi.zheye.service.QuestionService;
import com.liewmanchoi.zheye.service.ReplyService;
import com.liewmanchoi.zheye.service.UserService;
import com.liewmanchoi.zheye.utils.JsonUtil;
import com.liewmanchoi.zheye.utils.JsonUtil.STATUS;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * @date 2019/5/19
 */
@Controller
@Slf4j
public class QuestionController {
  @Autowired private HostHolder hostHolder;
  @Autowired private QuestionService questionService;
  @Autowired private ReplyService replyService;
  @Autowired private UserService userService;
  @Autowired private LikeService likeService;
  @Autowired private FollowService followService;

  @RequestMapping(value = "question/{qid}", method = RequestMethod.GET)
  public String questionDetail(Model model, @PathVariable("qid") int qid) {
    Question question = questionService.getById(qid);
    model.addAttribute("question", question);
    List<Reply> replyList = replyService.getRepliesByEntity(qid, EntityType.QUESTION);
    List<ViewObject> viewObjects = new ArrayList<>();

    for (Reply comment : replyList) {
      ViewObject viewObject = new ViewObject();

      if (hostHolder.getUser() == null) {
        viewObject.set("liked", 0);
      } else {
        viewObject.set(
            "liked",
            likeService.getLikeStatus(
                hostHolder.getUser().getId(), EntityType.COMMENT, comment.getId()));
      }

      viewObject.set("comment", comment);
      viewObject.set("likeCount", likeService.getLikeCount(EntityType.COMMENT, comment.getId()));
      viewObject.set("user", userService.getUser(comment.getUserId()));
      viewObjects.add(viewObject);
    }
    model.addAttribute("comments", viewObjects);

    List<ViewObject> followUsers = new ArrayList<>();
    // 获取关注的用户信息
    List<Integer> users = followService.getFollowers(EntityType.QUESTION, qid, 20);
    for (Integer userId : users) {
      ViewObject vo = new ViewObject();
      User user = userService.getUser(userId);
      if (user == null) {
        continue;
      }
      vo.set("name", user.getName());
      vo.set("avatarUrl", user.getAvatarUrl());
      vo.set("id", user.getId());
      followUsers.add(vo);
    }
    model.addAttribute("followUsers", followUsers);
    if (hostHolder.getUser() != null) {
      model.addAttribute(
          "followed",
          followService.isFollower(hostHolder.getUser().getId(), EntityType.QUESTION, qid));
    } else {
      model.addAttribute("followed", false);
    }

    return "detail";
  }

  @RequestMapping(value = "/question/add", method = RequestMethod.POST)
  @ResponseBody
  public String addQuestion(
      @RequestParam("title") String title, @RequestParam("content") String content) {
    try {
      Question question = new Question();
      question.setTitle(title);
      question.setContent(content);
      question.setCreatedDate(new Date());
      question.setUserId(hostHolder.getUser().getId());

      if (questionService.addQuestion(question) > 0) {
        return JsonUtil.getJSONString(0);
      }

    } catch (Exception exception) {
      log.error("增加题目失败 " + exception.getMessage());
    }

    return JsonUtil.getJSONString(STATUS.FAILURE, "失败");
  }
}
