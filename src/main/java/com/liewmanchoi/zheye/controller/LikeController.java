package com.liewmanchoi.zheye.controller;

import com.liewmanchoi.zheye.model.EntityType;
import com.liewmanchoi.zheye.model.HostHolder;
import com.liewmanchoi.zheye.model.Reply;
import com.liewmanchoi.zheye.service.LikeService;
import com.liewmanchoi.zheye.service.ReplyService;
import com.liewmanchoi.zheye.utils.JsonUtil;
import com.liewmanchoi.zheye.utils.JsonUtil.STATUS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wangsheng
 * @date 2019/8/10
 */
@Slf4j
@Controller
public class LikeController {
  @Autowired
  private LikeService likeService;
  @Autowired
  private HostHolder hostHolder;
  @Autowired
  private ReplyService replyService;

  @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
  @ResponseBody
  public String like(@RequestParam("commentId") int commentId) {
    if (hostHolder.getUser() == null) {
      // TODO: 必须修正这个诡异的数字
      return JsonUtil.getJSONString(999);
    }

    Reply reply = replyService.getReplyById(commentId);
    long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.COMMENT, commentId);
    return JsonUtil.getJSONString(STATUS.SUCCESS, String.valueOf(likeCount));
  }

  @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
  @ResponseBody
  public String dislike(@RequestParam("commentId") int commentId) {
    if (hostHolder.getUser() == null) {
      // TODO: 必须修正这个诡异的数字
      return JsonUtil.getJSONString(999);
    }
    long likeCount = likeService.dislike(hostHolder.getUser().getId(), EntityType.COMMENT, commentId);
    return JsonUtil.getJSONString(STATUS.SUCCESS, String.valueOf(likeCount));
  }
}
