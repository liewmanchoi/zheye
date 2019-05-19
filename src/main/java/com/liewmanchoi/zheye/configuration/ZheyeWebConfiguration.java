package com.liewmanchoi.zheye.configuration;

import com.liewmanchoi.zheye.interceptor.LoginInterceptorWithCallback;
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
public class ZheyeWebConfiguration implements WebMvcConfigurer {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginInterceptorWithCallback loginInterceptorWithCallback;

    @Autowired
    RegLoginInterceptor regLoginInterceptor;

    /**
     * 配置拦截器
     *
     * @param registry InterceptorRegistry
     * @date 2019/5/19
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginInterceptorWithCallback).addPathPatterns("/user/**");
        registry.addInterceptor(regLoginInterceptor).addPathPatterns("/reglogin");
    }
}
