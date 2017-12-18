package com.nowcoder.dao;

import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

/**
 * 关联User表的数据访问对象接口
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = "name,password,salt,head_url,email,status";
    String SELECT_FIELDS = "id,name,password,salt,head_Url,email,status";


    @Insert({"insert into", TABLE_NAME, " (", INSERT_FIELDS, ") values(#{name},#{password},#{salt},#{headUrl},#{email},#{status})"})
    void addUser(User user);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    User selectUserById(int id);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where name = #{name}"})
    User selectUserByName(String name);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where email = #{email}"})
    User selectUserByEmail(String email);

    @Delete({"delete from ", TABLE_NAME, " where id = #{id}"})
    void deleteById(int id);
    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    void updateStatus(@Param("id") int userId,@Param("status") int status);
}
