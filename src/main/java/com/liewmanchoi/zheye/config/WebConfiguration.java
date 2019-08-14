package com.liewmanchoi.zheye.config;

import com.liewmanchoi.zheye.interceptor.LoginInterceptorForNext;
import com.liewmanchoi.zheye.interceptor.LoginInterceptorForReferer;
import com.liewmanchoi.zheye.interceptor.PassportInterceptor;
import com.liewmanchoi.zheye.interceptor.RegLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wangsheng
 * @date 2019/5/19
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
  @Autowired PassportInterceptor passportInterceptor;

  @Autowired LoginInterceptorForReferer loginInterceptorForReferer;

  @Autowired LoginInterceptorForNext loginInterceptorForNext;

  @Autowired RegLoginInterceptor regLoginInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(passportInterceptor);
    registry.addInterceptor(loginInterceptorForReferer).addPathPatterns("/addComment");
    registry.addInterceptor(loginInterceptorForNext).addPathPatterns("/user/**", "/pullfeeds", "/pushfeeds");
    registry.addInterceptor(regLoginInterceptor).addPathPatterns("/reglogin");
  }
}
