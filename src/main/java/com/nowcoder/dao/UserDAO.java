package com.nowcoder.dao;

import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDAO {
    // 注意空格
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    @Results({
            @Result(property = "id" ,column ="id"),
            @Result(property = "name" , column = "name"),
            @Result(property= "password",column ="password"),
            @Result(property= "salt",column="salt"),
            @Result(property="headUrl",column="head_url")
    })
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    @Results({
            @Result(property = "id" ,column ="id"),
            @Result(property = "name" , column = "name"),
            @Result(property= "password",column ="password"),
            @Result(property= "salt",column="salt"),
            @Result(property="headUrl",column="head_url")
    })
    User selectByName(String name);




    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);
}
