package com.liewmanchoi.zheye.controller;

import com.liewmanchoi.zheye.model.EntityType;
import com.liewmanchoi.zheye.model.HostHolder;
import com.liewmanchoi.zheye.model.Question;
import com.liewmanchoi.zheye.model.Reply;
import com.liewmanchoi.zheye.model.ViewObject;
import com.liewmanchoi.zheye.service.QuestionService;
import com.liewmanchoi.zheye.service.ReplyService;
import com.liewmanchoi.zheye.service.UserService;
import com.liewmanchoi.zheye.utils.JSONUtils;
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
  @Autowired HostHolder hostHolder;

  @Autowired QuestionService questionService;

  @Autowired ReplyService replyService;

  @Autowired UserService userService;

  @RequestMapping(value = "question/{qid}", method = RequestMethod.GET)
  public String questionDetail(Model model, @PathVariable("qid") int qid) {
    Question question = questionService.getById(qid);
    model.addAttribute("question", question);
    List<Reply> replyList = replyService.getRepliesByEntity(qid, EntityType.QUESTION);
    List<ViewObject> viewObjects = new ArrayList<>();

    for (Reply comment : replyList) {
      ViewObject viewObject = new ViewObject();
      viewObject.set("comment", comment);
      viewObject.set("user", userService.getUser(comment.getUserId()));
      viewObjects.add(viewObject);
    }
    model.addAttribute("comments", viewObjects);

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
        return JSONUtils.getJSONString(0);
      }

    } catch (Exception exception) {
      log.error("增加题目失败 " + exception.getMessage());
    }

    return JSONUtils.getJSONString(1, "失败");
  }
}
