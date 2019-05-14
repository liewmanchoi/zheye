package com.liewmanchoi.zheye;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ZheyeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZheyeApplication.class, args);
    }

}
