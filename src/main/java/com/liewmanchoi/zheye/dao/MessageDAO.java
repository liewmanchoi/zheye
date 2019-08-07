package com.liewmanchoi.zheye.dao;

import com.liewmanchoi.zheye.model.Message;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author wangsheng
 * @date 2019/5/20
 */
@Repository
public interface MessageDAO {
  String TABLE_NAME = "message";
  String INSERT_FIELDS = "from_id, to_id, content, created_date, has_read, conversation_id";
  String SELECTED_FIELDS = "id, " + INSERT_FIELDS;

  /**
   * addMessage
   *
   * @param message Message
   * @return int
   * @date 2019/5/20
   */
  @Insert({
    "INSERT INTO",
    TABLE_NAME,
    "(",
    INSERT_FIELDS,
    ") VALUES(#{fromId}, #{toId}, #{content}, #{createdDate}, " + "#{hasRead}, #{conversationId})"
  })
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  int addMessage(Message message);

  /**
   * getConversationDetail
   *
   * @param conversationId String
   * @param offset int
   * @param limit int
   * @return java.util.List<com.liewmanchoi.zheye.model.Message>
   * @date 2019/5/20
   */
  @Select({
    "SELECT",
    SELECTED_FIELDS,
    "FROM",
    TABLE_NAME,
    "WHERE conversation_id = #{conversationId} ORDER BY id DESC " + "limit #{offset}, #{limit}"
  })
  List<Message> getConversationDetail(
      @Param("conversationId") String conversationId,
      @Param("offset") int offset,
      @Param("limit") int limit);

  /**
   * getConversationUnreadCount
   *
   * @param userId int
   * @param conversationId String
   * @return int
   * @date 2019/5/20
   */
  @Select({
    "SELECT COUNT(id) FROM",
    TABLE_NAME,
    "WHERE has_read = 0 AND to_id = #{userId} AND conversation_id = " + "#{conversationId}"
  })
  int getConversationUnreadCount(
      @Param("userId") int userId, @Param("conversationId") String conversationId);

  /**
   * getConversationList
   *
   * @param userId int
   * @param offset int
   * @param limit int
   * @return java.util.List<com.liewmanchoi.zheye.model.Message>
   * @date 2019/5/20
   */
  @Select({
    "SELECT b.cnt as id, a.from_id, a.to_id, a.content, a.created_date, a.has_read, a.conversation_id FROM message AS a,",
    "(SELECT conversation_id, COUNT(conversation_id) as cnt, MAX(created_date) AS created_date FROM message "
        + "WHERE from_id = #{userId} OR to_id = #{userId} GROUP BY conversation_id) AS b",
    "WHERE a.created_date = b.created_date AND a.conversation_id = b.conversation_id ORDER BY created_date "
        + "DESC limit #{offset}, #{limit}"
  })
  List<Message> getConversationList(
      @Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);
}
