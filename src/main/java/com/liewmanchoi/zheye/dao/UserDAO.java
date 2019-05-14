package com.liewmanchoi.zheye.dao;

import com.liewmanchoi.zheye.model.User;
import org.apache.ibatis.annotations.*;

/**
 * @author wangsheng
 * @date 2019/5/14
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, avatar_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * addUser
     * @param user JavaBean
     * @return int
     * @date 2019/5/14
     */
    @Insert({"INSERT INTO", TABLE_NAME, "(", INSERT_FIELDS, ") VALUES(#{user.name}, #{user.password}, #{user.salt}, " +
            "#{user.avatarUrl})"})
    int addUser(User user);

    /**
     * selectById
     *
     * @param id user id
     * @return User
     * @date 2019/5/14
     */
    @Select({"SELECT", SELECT_FIELDS, "FROM", TABLE_NAME, "WHERE id = #{id}"})
    User selectById(int id);

    /**
     * updatePassword
     *
     * @param user JavaBean
     * @date 2019/5/14
     */
    @Update({"UPDATE", TABLE_NAME, "SET password = #{user.password} WHERE id = #{user.id}"})
    void updatePassword(User user);

    /**
     * deleteById
     *
     * @param id user id
     * @date 2019/5/14
     */
    @Delete({"DELETE FROM", TABLE_NAME, "WHERE id = #{id}"})
    void deleteById(int id);
}
