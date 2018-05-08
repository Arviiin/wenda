package com.nowcoder.dao;

import com.nowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketDAO {
    // 注意空格
    String TABLE_NAME = "login_ticket ";
    String INSERT_FIELDS = " user_id, expired, status, ticket ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS ,
            ") values (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where ticket= #{ticket} "})
    @Results({
            @Result(property = "id" ,column ="id"),
            @Result(property = "userId" , column = "user_id"), //踩坑记录：因为没加这行代码，调试几天，拿不到userId。
            @Result(property= "expired",column ="expired"),
            @Result(property= "status",column="status"),
            @Result(property="ticket",column="ticket")
    })
    LoginTicket selectByTicket(String ticket);

    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
