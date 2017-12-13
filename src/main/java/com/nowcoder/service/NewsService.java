package com.nowcoder.service;

import com.nowcoder.model.News;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NewsService {
    List<News> getLatestNews(int userId, int offset, int limit);

    String saveImage(MultipartFile file) throws IOException;

    void addNews(News news);
}
