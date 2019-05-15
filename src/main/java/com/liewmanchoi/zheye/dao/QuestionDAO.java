package com.liewmanchoi.zheye.dao;

import com.liewmanchoi.zheye.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangsheng
 * @date 2019/5/15
 */
@Repository
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECTED_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * addQuestion
     *
     * @param question 添加的问题
     * @return int
     * @date 2019/5/15
     */
    @Insert({"INSERT INTO", TABLE_NAME, "(", INSERT_FIELDS, ")", "VALUES(#{title}, #{content}, " +
            "#{createdDate}, #{userId}, #{commentCount})"})
    int addQuestion(Question question);

    /**
     * selectLatestQuestions
     *
     * @param userId 用户id
     * @param offset 偏移值
     * @param limit 偏移值
     * @return java.util.List<com.liewmanchoi.zheye.model.Question>
     * @date 2019/5/15
     */
    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                          @Param("limit") int limit);
}
