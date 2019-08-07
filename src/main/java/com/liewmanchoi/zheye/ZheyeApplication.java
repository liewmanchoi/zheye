package com.liewmanchoi.zheye;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** @author wangsheng */
@SpringBootApplication
@MapperScan("com.liewmanchoi.zheye.dao")
public class ZheyeApplication {
  public static void main(String[] args) {
    SpringApplication.run(ZheyeApplication.class, args);
  }
}
