package com.liewmanchoi.zheye.controller;

import com.liewmanchoi.zheye.model.HostHolder;
import com.liewmanchoi.zheye.model.Question;
import com.liewmanchoi.zheye.service.QuestionService;
import com.liewmanchoi.zheye.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author wangsheng
 * @date 2019/5/19
 */
@Controller
@Slf4j
public class QuestionController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
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
