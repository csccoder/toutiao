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
    String INSERT_FIELDS = "id,title,link,image,like_count,comment_count,created_date,user_id";
    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{id},#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    void addNews(News news);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);

    @Delete({"delete from ",TABLE_NAME," where id = #{id}"})
    void deleteNews(int id);


}
