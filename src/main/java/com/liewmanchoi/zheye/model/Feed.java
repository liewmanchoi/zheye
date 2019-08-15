package com.liewmanchoi.zheye.model;

import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Feed流元数据实体，持久化到数据库中
 *
 * @author wangsheng
 * @date 2019/8/13
 */
@Getter
@Setter
public class Feed {
  private int id;
  /** EventType#getValue()返回的值，feed内容类型 */
  private int type;
  /** 触发feed内容的用户id */
  private int actorId;
  /** （影响到的）资源的类型 */
  private int entityType;
  /** （影响到的）资源的id */
  private int entityId;

  private Date createdDate;
  /** 状态，0表示正常（默认值），1表示删除 */
  private int status;
  /** 存储摘要信息，格式为json字符串 */
  private String summaryData;

  public void setSummaryData(Map<String, Object> map) {
    this.summaryData = JSONObject.toJSONString(map);
  }

  public Object get(String key) {
    return summaryData == null ? null : JSONObject.parseObject(summaryData, Map.class).get(key);
  }
}
