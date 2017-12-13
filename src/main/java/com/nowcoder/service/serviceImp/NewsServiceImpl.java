package com.nowcoder.service.serviceImp;

import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.News;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.OOSService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsServiceImpl implements NewsService{
    @Autowired
    private NewsDAO newsDAO;
    @Autowired
    private OOSService oosService;
    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public String saveImage(MultipartFile file) throws IOException {
        //图片格式检测
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        //不存在后缀名
        if(dotPos<0){
            return null;
        }
        //取后缀名，进行格式判断
        String fileExt=file.getOriginalFilename().substring(dotPos+1);
        boolean allow=ToutiaoUtil.isFileAllowed(fileExt);

        if(!allow){
            return null;
        }
        /**
         *
         //通过UUID生成唯一文件名，并保存到本地
         String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
         File storageFile=new File(ToutiaoUtil.IMAGE_FILE_STORAGE_DIR+fileName);

         Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_FILE_STORAGE_DIR+fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
         return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+fileName;

         */
        return oosService.saveImage(file);
    }

    public void addNews(News news) {
        newsDAO.addNews(news);
    }
}
