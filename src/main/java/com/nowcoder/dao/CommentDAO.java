package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entityType, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, "from", TABLE_NAME, " where id=#{id}"})
    @Results({//当表名和bean中的字段名不一致的时候需要。此处一样的地方可以不写 //注意上面\"是转意字符"
            @Result(property = "id" ,column="id"),//前面是bean中的属性，后面是数据库的列
            @Result(property = "title" ,column="title"),
            @Result(property = "content" ,column="content"),
            @Result(property = "userId" ,column="user_id"),
            @Result(property = "createdDate" ,column="created_date"),
            @Result(property = "commentCount" ,column="comment_count")})
    Question selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME ,
            " where entity_id = #{entityId} and entity_type=#{entityType} order by created_date desc"})
    /*@Results({//当表名和bean中的字段名不一致的时候需要。此处一样的地方可以不写 //注意上面\"是转意字符"
            @Result(property = "id" ,column="id"),//前面是bean中的属性，后面是数据库的列
            @Result(property = "title" ,column="title"),
            @Result(property = "content" ,column="content"),
            @Result(property = "userId" ,column="user_id"),
            @Result(property = "createdDate" ,column="created_date"),
            @Result(property = "commentCount" ,column="comment_count")})*/
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id = #{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update comment set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);
}