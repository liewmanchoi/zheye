package com.liewmanchoi.zheye.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** @author wangsheng */
@AllArgsConstructor
public enum EventType {
  /** 点赞 */
  LIKE(0),
  /** 评论 */
  COMMENT(1),
  /** 登陆 */
  LOGIN(2);

  @Getter private int value;
}
