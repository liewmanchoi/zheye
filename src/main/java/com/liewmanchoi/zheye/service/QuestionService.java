package com.liewmanchoi.zheye.service;

import com.liewmanchoi.zheye.dao.QuestionDAO;
import com.liewmanchoi.zheye.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
