package com.liewmanchoi.zheye.event;

import java.util.List;

/**
 * @author wangsheng
 * @date 2019/8/11
 */
public interface EventHandler {
  /** 处理事件 */
  void doHandle(Event event);

  List<EventType> getSupportedEventTypes();
}
