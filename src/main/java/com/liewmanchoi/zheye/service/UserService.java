package com.liewmanchoi.zheye.service;

import com.liewmanchoi.zheye.dao.UserDAO;
import com.liewmanchoi.zheye.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangsheng
 * @date 2019/5/15
 */
@Service
@Slf4j
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
