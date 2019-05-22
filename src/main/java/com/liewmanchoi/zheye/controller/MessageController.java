package com.liewmanchoi.zheye.controller;

import com.liewmanchoi.zheye.model.HostHolder;
import com.liewmanchoi.zheye.model.Message;
import com.liewmanchoi.zheye.model.User;
import com.liewmanchoi.zheye.model.ViewObject;
import com.liewmanchoi.zheye.service.MessageService;
import com.liewmanchoi.zheye.service.UserService;
import com.liewmanchoi.zheye.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangsheng
 * @date 2019/5/20
 */
@Controller
@Slf4j
public class MessageController {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = "/msg/list", method = RequestMethod.GET)
    public String conversationDetail(Model model) {
        try {
            List<ViewObject> conversations = new ArrayList<>();
            int localUserId = hostHolder.getUser().getId();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);

            for (Message message : conversationList) {
                ViewObject viewObject = new ViewObject();
                viewObject.set("conversation", message);
                int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
                User user = userService.getUser(targetId);
                viewObject.set("user", user);
                viewObject.set("unread", messageService.getConversationUnreadCount(localUserId,
                        message.getConversationId()));
                conversations.add(viewObject);
            }
            model.addAttribute("conversations", conversations);
        } catch (Exception e) {
            log.error("获取站内信列表失败 " + e.getMessage());
        }

        return "letter";
    }

    @RequestMapping(path = "/msg/detail", method = RequestMethod.GET)
    public String conversationDetail(Model model, @Param("conversationId") String conversationId) {
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> viewObjectList = new ArrayList<>();

            for (Message message : conversationList) {
                ViewObject viewObject = new ViewObject();
                viewObject.set("message", message);
                User user = userService.getUser(message.getFromId());
                if (user == null) {
                    continue;
                }
                viewObject.set("headURL", user.getAvatarUrl());
                viewObject.set("userId", user.getId());
                viewObjectList.add(viewObject);
            }

            model.addAttribute("messages", viewObjectList);
        } catch (Exception e) {
            log.error("获取消息详情失败 " + e.getMessage());
        }

        return "letterDetail";
    }

    @PostMapping(path = "/msg/addMessage")
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            if (hostHolder.getUser() == null) {
                return JSONUtils.getJSONString(999, "未登录");
            }

            User user = userService.selectByName(toName);
            if (user == null) {
                return JSONUtils.getJSONString(1, "用户不存在");
            }

            Message message = new Message();
            message.setContent(content);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setCreatedDate(new Date());
            messageService.addMessage(message);

            return JSONUtils.getJSONString(0);
        } catch (Exception e) {
            log.error("增加站内信失败 " + e.getMessage());
            return JSONUtils.getJSONString(1, "插入站内信失败");
        }
    }

    @PostMapping(path = "/msg/jsonAddMessage")
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            Message message = new Message();
            message.setContent(content);
            message.setFromId(fromId);
            message.setToId(toId);
            message.setCreatedDate(new Date());
            messageService.addMessage(message);

            return JSONUtils.getJSONString(message.getId());
        } catch (Exception e) {
            log.error("增加评论失败 " + e.getMessage());
            return JSONUtils.getJSONString(1, "插入评论失败");
        }
    }
}
