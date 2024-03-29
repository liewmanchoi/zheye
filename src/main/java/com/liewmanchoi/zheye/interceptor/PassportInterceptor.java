package com.liewmanchoi.zheye.interceptor;

import com.liewmanchoi.zheye.dao.LoginTicketDAO;
import com.liewmanchoi.zheye.dao.UserDAO;
import com.liewmanchoi.zheye.model.HostHolder;
import com.liewmanchoi.zheye.model.LoginTicket;
import com.liewmanchoi.zheye.model.User;
import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wangsheng
 * @date 2019/5/19
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {
  @Autowired private LoginTicketDAO loginTicketDAO;

  @Autowired private UserDAO userDAO;

  @Autowired private HostHolder hostHolder;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String ticket = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("ticket".equals(cookie.getName())) {
          ticket = cookie.getValue();
          break;
        }
      }
    }

    if (ticket != null) {
      LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
      if (loginTicket == null
          || loginTicket.getExpired().before(new Date())
          || loginTicket.getStatus() != 0) {
        return true;
      }

      User user = userDAO.selectById(loginTicket.getUserId());
      hostHolder.setUser(user);
    }
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    if (modelAndView != null && hostHolder.getUser() != null) {
      modelAndView.addObject("user", hostHolder.getUser());
    }
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    hostHolder.clear();
  }
}
