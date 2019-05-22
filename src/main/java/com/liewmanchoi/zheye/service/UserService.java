package com.liewmanchoi.zheye.service;

import com.liewmanchoi.zheye.dao.LoginTicketDAO;
import com.liewmanchoi.zheye.dao.UserDAO;
import com.liewmanchoi.zheye.model.LoginTicket;
import com.liewmanchoi.zheye.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;

/**
 * @author wangsheng
 * @date 2019/5/15
 */
@Service
@Slf4j
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public User selectByName(String name) {
        return userDAO.selectByName(name);
    }

    /**
     * 注册新用户
     *
     * @param username 用户名
     * @param password 用户密码
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @date 2019/5/16
     */
    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>(1);
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }

        // 设置密码，对密码加盐后，存储MD5值
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 6));
        String avatarUrl = String.format("../images/avatars/%d.jpg", new Random().nextInt(11));
        user.setAvatarUrl(avatarUrl);
        String saltedPassword = password + user.getSalt();
        user.setPassword(DigestUtils.md5DigestAsHex(saltedPassword.getBytes()));
        userDAO.addUser(user);

        // 账户注册成功后，自动设置token
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    /**
     * login
     *
     * @param username 用户名
     * @param password 密码
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @date 2019/5/16
     */
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>(1);

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        String saltedPassword = password + user.getSalt();
        if (!DigestUtils.md5DigestAsHex(saltedPassword.getBytes()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    private String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(loginTicket);

        return loginTicket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
