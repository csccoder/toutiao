package com.nowcoder.dao;

import com.nowcoder.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * news表对应的数据访问对象
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = "title,link,image,like_count,comment_count,created_date,user_id";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    void addNews(News news);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Delete({"delete from ", TABLE_NAME, " where id = #{id}"})
    void deleteNews(int id);

    @Select({"select ", SELECT_FIELDS, "from ", TABLE_NAME, " where id =#{newsId}"})
    News selectById(int newsId);

    @Update({"update ",TABLE_NAME,"set comment_count = #{count} where id =#{newsId}"})
    void updateCommentCount(@Param("newsId") int newsId,@Param("count") int count);

    @Update({"update ",TABLE_NAME," set like_count =#{count} where id=#{newsId}"})
    void updateLikeCount(@Param("newsId") int newsId,@Param("count") int count);
}
