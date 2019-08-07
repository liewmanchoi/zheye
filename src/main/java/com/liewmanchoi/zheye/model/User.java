package com.liewmanchoi.zheye.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangsheng
 * @date 2019/5/14
 */
@Getter
@Setter
public class User {
  private int id;
  private String name;
  private String password;
  private String salt;
  private String avatarUrl;
}
