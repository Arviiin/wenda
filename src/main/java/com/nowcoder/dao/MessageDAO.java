package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,  //未与INSERT_FIELDS里定义的字段对齐，导致错误。
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME ,
            " where conversation_id = #{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    @Results({
            @Result(property = "id" ,column="id"),//前面是bean中的属性，后面是数据库的列
            @Result(property = "fromId" ,column="from_id"),
            @Result(property = "toId" ,column="to_id"),
            @Result(property = "content" ,column="content"),
            @Result(property = "createdDate" ,column="created_date"),
            @Result(property = "hasRead" ,column="has_read"),
            @Result(property = "conversationId" ,column="conversation_id")})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);


    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME,
            " where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})

    @Results({
            @Result(property = "id" ,column="id"),//前面是bean中的属性，后面是数据库的列
            @Result(property = "fromId" ,column="from_id"),
            @Result(property = "toId" ,column="to_id"),
            @Result(property = "content" ,column="content"),
            @Result(property = "createdDate" ,column="created_date"),
            @Result(property = "hasRead" ,column="has_read"),
            @Result(property = "conversationId" ,column="conversation_id")})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConvesationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

}