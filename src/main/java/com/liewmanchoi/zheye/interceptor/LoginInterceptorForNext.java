package com.liewmanchoi.zheye.interceptor;

import com.liewmanchoi.zheye.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangsheng
 * @date 2019/5/20
 */
@Component
public class LoginInterceptorForNext implements HandlerInterceptor {
    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (hostHolder.getUser() == null) {
            response.sendRedirect("/reglogin?next=" + request.getRequestURI());
            return false;
        }
        return true;
    }
}
