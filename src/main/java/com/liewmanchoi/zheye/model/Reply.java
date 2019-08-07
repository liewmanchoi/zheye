package com.liewmanchoi.zheye.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangsheng
 * @date 2019/5/20
 */
@Getter
@Setter
public class Reply {
  private int id;
  /** userId 回复发表者id */
  private int userId;

  /** entityId 被回复的对象id */
  private int entityId;
  /** entityId 被回复对象的类型 */
  private int entityType;

  private String content;
  private Date createdDate;
  private int status;
}
