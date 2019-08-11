package com.liewmanchoi.zheye.event;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangsheng
 * @date 2019/8/11
 */
@Getter
public class Event {
  /** 时间类型 */
  private EventType eventType;
  /** 事件触发者 */
  private int actorId;

  private int entityType;
  private int entityId;
  private int entityOwnerId;
  /** 附加字段 */
  @Setter private Map<String, Object> attachments = new HashMap<>();

  public Event setActorId(int actorId) {
    this.actorId = actorId;
    return this;
  }

  public Event setEntityType(int entityType) {
    this.entityType = entityType;
    return this;
  }

  public Event setEntityId(int entityId) {
    this.entityId = entityId;
    return this;
  }

  public Event setEntityOwnerId(int entityOwnerId) {
    this.entityOwnerId = entityOwnerId;
    return this;
  }

  public Event setAttachments(String key, String value) {
    attachments.put(key, value);
    return this;
  }

  public Object getAttachments(String key) {
    return attachments.get(key);
  }
}
