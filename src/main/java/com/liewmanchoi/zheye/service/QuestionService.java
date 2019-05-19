package com.liewmanchoi.zheye.service;

import com.liewmanchoi.zheye.dao.QuestionDAO;
import com.liewmanchoi.zheye.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author wangsheng
 * @date 2019/5/15
 */
@Service
@Slf4j
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public Question getById(int id) {
        return questionDAO.getById(id);
    }

    public int updateCommentCount(int id, int commentCount) {
        return questionDAO.updateCommentCount(id, commentCount);
    }

    public int addQuestion(Question question) {
        // 过滤脚本
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));

        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }
}
