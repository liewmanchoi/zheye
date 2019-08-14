package com.liewmanchoi.zheye.event;

import com.liewmanchoi.zheye.config.KafkaConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author wangsheng
 * @date 2019/8/11
 */
@Slf4j
@Service
public class EventConsumer implements ApplicationContextAware, InitializingBean {

  private ApplicationContext context;
  /** EventType和对其感兴趣的EventHandler */
  private Map<EventType, List<EventHandler>> handlerMap = new HashMap<>(3);
  /** 任务线程池 */
  private ExecutorService executor = Executors.newFixedThreadPool(8);

  /** 消费Event事件 */
  @KafkaListener(
      topics = {KafkaConfig.TOPIC},
      groupId = KafkaConfig.GROUP_ID)
  public void listen(@Payload List<Event> eventList) {
    if (eventList == null || eventList.isEmpty()) {
      return;
    }

    for (Event event : eventList) {
      EventType eventType = event.getOperation();

      for (EventHandler eventHandler : handlerMap.get(eventType)) {
        executor.submit(() -> eventHandler.doHandle(event));
      }
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // 获取所有继承了EventHandler接口的bean
    Map<String, EventHandler> beanMap = context.getBeansOfType(EventHandler.class);
    if (beanMap == null || beanMap.isEmpty()) {
      return;
    }

    for (EventHandler eventHandler : beanMap.values()) {
      List<EventType> eventTypes = eventHandler.getSupportedEventTypes();
      for (EventType eventType : eventTypes) {
        if (!handlerMap.containsKey(eventType)) {
          handlerMap.put(eventType, new ArrayList<>());
        } else {
          handlerMap.get(eventType).add(eventHandler);
        }
      }
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }
}
