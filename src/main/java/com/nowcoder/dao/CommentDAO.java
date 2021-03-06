package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @program: toutiao
 * @description:
 * @author: chenny
 * @create: 2017-12-14 22:46
 **/
@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = "content,user_id,entity_id,entity_type,created_date,status";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, "from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} and status = 0 order by created_date desc"})
    List<Comment> selectComment(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(1) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} and status = 0"})
    int selectCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update ", TABLE_NAME, " set status=#{status} where id = #{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);
}
