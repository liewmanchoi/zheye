package com.liewmanchoi.zheye.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangsheng
 * @date 2019/5/15
 */
public class ViewObject {
  private Map<String, Object> map = new HashMap<>(16);

  public void set(String key, Object value) {
    map.put(key, value);
  }

  public Object get(String key) {
    return map.get(key);
  }
}
