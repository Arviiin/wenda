package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_Type, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);


    /**
     * 找出一个实体里的所有评论
     * @param entityId
     * @param entityType
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME ,
            " where entity_id = #{entityId} and entity_type=#{entityType} order by created_date desc"})
    @Results({//当表名和bean中的字段名不一致的时候需要。此处一样的地方可以不写 //注意上面\"是转意字符"
            @Result(property = "id" ,column="id"),//前面是bean中的属性，后面是数据库的列
            @Result(property = "userId" ,column="user_id"),
            @Result(property = "entityId" ,column="entity_id"),
            @Result(property = "entityType" ,column="entity_type"),
            @Result(property = "content" ,column="content"),
            @Result(property = "createdDate" ,column="created_date"),
            @Result(property = "status" ,column="status")})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);


    @Select({"select count(id) from ", TABLE_NAME , " where entity_id = #{entityId} and entity_type=#{entityType}"})
            //注：此处没有选出字段，所有根本没有下面Results。所以一直报错。
    /*@Results({
            @Result(property = "id" ,column="id"),
            @Result(property = "content" ,column="content"),
            @Result(property = "userId" ,column="user_id"),
            @Result(property = "entityId" ,column="entity_id"),
            @Result(property = "entityType" ,column="entity_type"),
            @Result(property = "createdDate" ,column="created_date"),
            @Result(property = "status" ,column="status")})*/
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update comment set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);
}