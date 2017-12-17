package com.nowcoder.dao;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: toutiao
 * @description:
 * @author: chenny
 * @create: 2017-12-15 00:06
 **/
@Mapper
public interface MessageDAO {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = "from_id,to_id,content,has_read,created_date,conversation_id,status";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{fromId},#{toId},#{content},#{hasRead},#{createdDate},#{conversationId},#{status})"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversation_id=#{conversationId} and status=0 order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({
            "select a.from_id,a.to_id,a.content,a.has_read,a.created_date,a.conversation_id,a.status,b.cnt as id from (",
            " select ",SELECT_FIELDS, " from ",TABLE_NAME ," where id in(",
                   " select max(id) as cnt  from ",TABLE_NAME," where from_id=#{userId} or to_id=#{userId} and status=0  group by conversation_id",
            " )and status=0  order by created_date desc) as a,",
            " (select max(id) as id,count(1) as cnt  from ",TABLE_NAME," where from_id=#{userId} or to_id=#{userId} and status=0   group by conversation_id)  as b",
            " where a.id=b.id order by created_date desc limit #{offset},#{limit} "})
    List<Message> getConversationList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnReadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

}
