package com.liewmanchoi.zheye.dao;

import com.liewmanchoi.zheye.model.Feed;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author wangsheng
 * @date 2019/8/14
 */
@Repository
public interface FeedDAO {
  String TABLE_NAME = " feed ";
  String INSERT_FIELDS =
      " actor_id, type, entity_type, entity_id, created_date, summary_data, status ";
  String SELECT_FIELDS = " id, " + INSERT_FIELDS;

  @Insert({
    "INSERT INTO",
    TABLE_NAME,
    "(",
    INSERT_FIELDS,
    ")",
    "VALUES (#{actorId}, #{type}, #{entityType}, #{entityId}, #{createdDate}, #{summaryData}, #{status})"
  })
  @Options(useGeneratedKeys=true, keyProperty="id", keyColumn = "id")
  int addFeed(Feed feed);

  @Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME, "WHERE id = #{id}"})
  Feed getFeedById(int id);

  List<Feed> pullFeedsByActorId(
      @Param("maxId") int maxId,
      @Param("actorIds") List<Integer> actorIds,
      @Param("count") int count);

  List<Feed> getFeedsById(@Param("ids") List<Integer> ids);
}
