package com.liewmanchoi.zheye.dao;

import com.liewmanchoi.zheye.model.Reply;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author wangsheng
 * @date 2019/5/20
 */
@Repository
public interface ReplyDAO {
  String TABLE_NAME = " reply ";
  String INSERT_FIELDS = " user_id, entity_id, entity_type, content, created_date, status ";
  String SELECTED_FIELDS = " id, " + INSERT_FIELDS;

  /** addReply */
  @Insert({
    "INSERT INTO",
    TABLE_NAME,
    "(",
    INSERT_FIELDS,
    ")",
    "VALUES (#{userId}, #{entityId}, #{entityType}, " + "#{content}, #{createdDate}, #{status})"
  })
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  int addReply(Reply reply);

  /** updateStatus */
  @Update({
    "UPDATE",
    TABLE_NAME,
    "SET status = #{status} WHERE entity_id = #{entityId} AND entity_type = #{entityType}"
  })
  void updateStatus(
      @Param("entityId") int entityId,
      @Param("entityType") int entityType,
      @Param("status") int status);

  /** selectByEntity */
  @Select({
    "SELECT",
    SELECTED_FIELDS,
    "FROM",
    TABLE_NAME,
    "WHERE entity_id = #{entityId} and entity_type = #{entityType} ORDER BY id DESC"
  })
  List<Reply> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

  /** getRepliesCount */
  @Select({
    "SELECT COUNT(id) FROM",
    TABLE_NAME,
    "WHERE entity_id = #{entityId} and entity_type = #{entityType}"
  })
  int getRepliesCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

  @Select({"SELECT", SELECTED_FIELDS, "FROM", TABLE_NAME, "WHERE id = #{id}"})
  Reply getReplyById(int id);

  @Select({"SELECT COUNT(id) FROM", TABLE_NAME, "WHERE user_id = #{userId}"})
  int getUserReplyCount(int userId);
}
