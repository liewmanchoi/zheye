package com.liewmanchoi.zheye.interceptor;

import com.liewmanchoi.zheye.model.HostHolder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author wangsheng
 * @date 2019/5/19
 */
@Component
public class RegLoginInterceptor implements HandlerInterceptor {
  @Autowired HostHolder hostHolder;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (hostHolder.getUser() != null) {
      response.sendRedirect("/");
      return true;
    }

    return true;
  }
}
