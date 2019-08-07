package com.liewmanchoi.zheye.dao;

import com.liewmanchoi.zheye.model.LoginTicket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author wangsheng
 * @date 2019/5/16 本来应该在Redis中实现，而非数据库
 */
@Repository
public interface LoginTicketDAO {
  String TABLE_NAME = " login_ticket ";
  String INSERT_FIELDS = " user_id, ticket, expired, status ";
  String SELECTED_FIELDS = " id, " + INSERT_FIELDS;

  /**
   * addTicket
   *
   * @param loginTicket 添加的loginTicket POJO
   * @return int
   * @date 2019/5/16
   */
  @Insert({
    "INSERT INTO",
    TABLE_NAME,
    "(",
    INSERT_FIELDS,
    ")",
    "VALUES(#{userId}, #{ticket}, #{expired}, #{status})"
  })
  int addTicket(LoginTicket loginTicket);

  /**
   * selectByTicket
   *
   * @param ticket token值
   * @return com.liewmanchoi.zheye.model.LoginTicket
   * @date 2019/5/16
   */
  @Select({"SELECT", SELECTED_FIELDS, "FROM", TABLE_NAME, "WHERE ticket=#{ticket}"})
  LoginTicket selectByTicket(@Param("ticket") String ticket);

  /**
   * updateStatus
   *
   * @param ticket token值
   * @param status 是否有效
   * @date 2019/5/16
   */
  @Update({"UPDATE", TABLE_NAME, "SET status = #{status} WHERE ticket = #{ticket}"})
  void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
