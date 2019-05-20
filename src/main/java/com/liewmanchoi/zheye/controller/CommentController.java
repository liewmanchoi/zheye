package com.liewmanchoi.zheye.controller;

import com.liewmanchoi.zheye.model.EntityType;
import com.liewmanchoi.zheye.model.HostHolder;
import com.liewmanchoi.zheye.model.Reply;
import com.liewmanchoi.zheye.service.QuestionService;
import com.liewmanchoi.zheye.service.ReplyService;
import com.liewmanchoi.zheye.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;


/**
 * @author wangsheng
 * @date 2019/5/20
 */
@Controller
@Slf4j
public class CommentController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    ReplyService replyService;

    @Autowired
    QuestionService questionService;

    @PostMapping(path = "/addComment")
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        try {
            content = HtmlUtils.htmlEscape(content);
            Reply reply = new Reply();

            reply.setUserId(hostHolder.getUser().getId());
            reply.setContent(content);
            reply.setEntityId(questionId);
            reply.setEntityType(EntityType.QUESTION);
            reply.setCreatedDate(new Date());
            reply.setStatus(0);

            replyService.addReply(reply);

            int count = replyService.getRepliesCount(reply.getEntityId(), reply.getEntityType());
            questionService.updateCommentCount(reply.getEntityId(), count);
        } catch (Exception exception) {
            log.error("增加评论失败" + exception.getMessage());
        }

        return "redirect:/question/" + questionId;
    }
}
