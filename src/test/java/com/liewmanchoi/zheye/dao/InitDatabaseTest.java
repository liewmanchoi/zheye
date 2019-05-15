package com.liewmanchoi.zheye.dao;

import com.liewmanchoi.zheye.model.Question;
import com.liewmanchoi.zheye.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDatabaseTest {
    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Test
    public void test() {
        for (int i = 0; i < 11; ++i) {
            Random random = new Random();
            User user = new User();
            user.setAvatarUrl(String.format("../images/avatars/%d.jpg", i));
            user.setName(String.format("person%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            user.setPassword("password");
            user.setId(i + 1);
            userDAO.updatePassword(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * random.nextInt());
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("Hello Offer Content %d", i));
            questionDAO.addQuestion(question);
        }

        Assert.assertEquals("password", userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));
    }
}