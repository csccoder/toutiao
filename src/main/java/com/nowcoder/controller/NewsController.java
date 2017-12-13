package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.service.NewsService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@Controller
public class NewsController extends BaseController{
    @Autowired
    private NewsService newsService;
    @Autowired
    private HostHolder hostHolder;


    @RequestMapping(path = {"/user/addNews/"},method = RequestMethod.POST)
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try{
            News news = new News();
            news.setTitle(title);
            news.setLink(link);
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setCommentCount(0);
            news.setLikeCount(0);
            //如果HostHolder未存在用户信息，则设置匿名用户id
            if(hostHolder.get()==null){
                news.setUserId(1);
            }else {
                news.setUserId(hostHolder.get().getId());
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("添加资讯失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布失败");
        }
    }

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response){
        response.setContentType("image/jpeg");
        try {
            //StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_FILE_STORAGE_DIR+imageName)),response.getOutputStream());
            Files.copy(new File(ToutiaoUtil.IMAGE_FILE_STORAGE_DIR+imageName).toPath(),response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("获取图片失败");
        }
    }

    @RequestMapping(path={"/uploadImage"},method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestPart("file")MultipartFile file){
       try{
           String fileUrl= newsService.saveImage(file);
           if(fileUrl==null){
               return ToutiaoUtil.getJSONString(1,"图片上传失败");
           }
           return ToutiaoUtil.getJSONString(0,fileUrl);
       }catch (Exception e){
           e.printStackTrace();
           logger.error("图片上传失败"+e.getMessage());
           return ToutiaoUtil.getJSONString(1,"图片上传失败");
       }
    }
}
