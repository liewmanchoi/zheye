package com.liewmanchoi.zheye.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangsheng
 * @date 2019/5/15
 */
@Getter
@Setter
public class Question {
  private int id;
  private String title;
  private String content;
  private Date createdDate;
  private int userId;
  private int commentCount;
}
