package com.nowcoder.dao;

import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    //xml版本的写法
    /*List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);*/


    @Select({"<script>select  *  from ", TABLE_NAME ," <if test=\" userId != 0 \"> where user_id = #{userId}</if> ORDER BY id DESC  LIMIT #{offset},#{limit} </script>"})
    @Results({//当表名和bean中的字段名不一致的时候需要。此处一样的地方可以不写 //注意上面\"是转意字符"
            @Result(property = "id" ,column="id"),//前面是bean中的属性，后面是数据库的列
//            @Result(property = "title" ,column="title"),
//            @Result(property = "content" ,column="content"),
            @Result(property = "userId" ,column="user_id"),
            @Result(property = "createdDate" ,column="created_date"),
            @Result(property = "commentCount" ,column="comment_count")})
    List<Question> selectLatestQuestions(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);

}