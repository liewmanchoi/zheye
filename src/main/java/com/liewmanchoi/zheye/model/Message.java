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
public class Message {
  private int id;
  private int fromId;
  private int toId;
  private String content;
  private Date createdDate;
  private int hasRead;
  private String conversationId;

  public String getConversationId() {
    if (fromId < toId) {
      return String.format("%d_%d", fromId, toId);
    }
    return String.format("%d_%d", toId, fromId);
  }
}
