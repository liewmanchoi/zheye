package com.liewmanchoi.zheye.event;

import com.liewmanchoi.zheye.config.KafkaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author wangsheng
 * @date 2019/8/11
 */
@Service
public class EventProducer {
  @Autowired private KafkaTemplate<String, Event> kafkaTemplate;

  /** 触发事件，将Event对象加入到Kafka消息队列中 */
  public boolean fireEvent(Event event) {
    if (event == null) {
      return false;
    }

    kafkaTemplate.send(KafkaConfig.TOPIC, event);
    return true;
  }
}
