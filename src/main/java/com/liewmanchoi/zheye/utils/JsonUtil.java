package com.liewmanchoi.zheye.utils;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;

/**
 * @author wangsheng
 * @date 2019/5/19
 */
public class JsonUtil {
  public static String getJSONString(int code) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("code", code);
    return jsonObject.toJSONString();
  }

  public static String getJSONString(int code, String msg) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("code", code);
    jsonObject.put("msg", msg);
    return jsonObject.toJSONString();
  }

  public static String getJSONString(int code, Map<String, Object> map) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("code", code);
    map.forEach(jsonObject::put);

    return jsonObject.toJSONString();
  }

  public static class STATUS {
    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
  }
}
