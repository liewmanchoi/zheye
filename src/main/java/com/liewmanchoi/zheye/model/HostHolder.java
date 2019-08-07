package com.liewmanchoi.zheye.model;

import org.springframework.stereotype.Component;

/**
 * @author wangsheng
 * @date 2019/5/19
 */
@Component
public class HostHolder {
  private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

  public User getUser() {
    return userThreadLocal.get();
  }

  public void setUser(User user) {
    userThreadLocal.set(user);
  }

  public void clear() {
    userThreadLocal.remove();
  }
}
