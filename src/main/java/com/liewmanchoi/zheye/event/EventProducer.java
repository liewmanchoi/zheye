package com.liewmanchoi.zheye.event;

import com.liewmanchoi.zheye.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author wangsheng
 * @date 2019/8/11
 */
@Slf4j
@Service
public class EventProducer {
  @Autowired private KafkaTemplate<String, Event> kafkaTemplate;

  /** 触发事件，将Event对象加入到Kafka消息队列中 */
  public boolean fireEvent(Event event) {
    if (event == null) {
      return false;
    }

    kafkaTemplate.send(KafkaConfig.TOPIC, event);
    log.info("生成事件[{}]", event);
    return true;
  }
}
