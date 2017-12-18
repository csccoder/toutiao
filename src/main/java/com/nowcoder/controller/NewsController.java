package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController extends BaseController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/news/deleteComment/{newsId}/{commentId}"}, method = RequestMethod.GET)
    public String deleteComment(@PathVariable("newsId") int newsId, @PathVariable("commentId") int commentId) {
        try {
            commentService.deleteComment(commentId);
            int count = commentService.selectCount(newsId, Entitype.NEWS.getValue());
            //异步实现更新评论数
            //newsService.updateCommentCount(newsId,count);
            News news = newsService.selectById(newsId);
            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(hostHolder.get().getId()).setEntityId(newsId).setEntityType(Entitype.NEWS.getValue()).setEntityOwnerId(news.getUserId()).setExt("commentCount",Integer.toString(count)));
            return "redirect:/news/" + newsId;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除资讯评论失败" + e.getMessage());
            return "error";
        }
    }

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.get().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(Entitype.NEWS.getValue());
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            int count = commentService.selectCount(newsId, Entitype.NEWS.getValue());
            //异步实现更新评论数
            //newsService.updateCommentCount(newsId,count);
            News news = newsService.selectById(newsId);
            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(hostHolder.get().getId()).setEntityId(newsId).setEntityType(Entitype.NEWS.getValue()).setEntityOwnerId(news.getUserId()).setExt("commentCount",Integer.toString(count)).setExt("content",String.format("你的文章【%s】被评论！\n评论者：%s\n评论内容：%s",news.getTitle(),hostHolder.get().getName(),comment.getContent())));
            return "redirect:/news/" + newsId;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("插入资讯评论失败" + e.getMessage());
            return "error";
        }
    }


    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(Model model, @PathVariable("newsId") int newsId) {
        try {
            News news = newsService.selectById(newsId);
            if (news != null) {
                User owner = userService.selectUserById(news.getUserId());
                List<ViewObject> commentVOs = new ArrayList<ViewObject>();
                List<Comment> comments = commentService.selectComment(news.getId(), Entitype.NEWS.getValue());
                for (Comment comment : comments) {
                    ViewObject viewObject = new ViewObject();
                    viewObject.set("comment", comment);
                    viewObject.set("user", userService.selectUserById(comment.getUserId()));
                    commentVOs.add(viewObject);
                }
                int likeStatus = 0;
                if (hostHolder.get() != null) {
                    likeStatus = likeService.likeStatus(hostHolder.get().getId(), Entitype.NEWS.getValue(), newsId);
                }
                model.addAttribute("like", likeStatus);
                model.addAttribute("news", news);
                model.addAttribute("owner", owner);
                model.addAttribute("comments", commentVOs);
                return "detail";
            }
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查询资讯详细信息失败" + e.getMessage());
        }
        return "error";
    }

    @RequestMapping(path = {"/user/addNews/"}, method = RequestMethod.POST)
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setTitle(title);
            news.setLink(link);
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setCommentCount(0);
            news.setLikeCount(0);
            //如果HostHolder未存在用户信息，则设置匿名用户id
            if (hostHolder.get() == null) {
                news.setUserId(1);
            } else {
                news.setUserId(hostHolder.get().getId());
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加资讯失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布失败");
        }
    }

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        response.setContentType("image/jpeg");
        try {
            //StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_FILE_STORAGE_DIR+imageName)),response.getOutputStream());
            Files.copy(new File(ToutiaoUtil.IMAGE_FILE_STORAGE_DIR + imageName).toPath(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("获取图片失败");
        }
    }

    @RequestMapping(path = {"/uploadImage"}, method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestPart("file") MultipartFile file) {
        try {
            String fileUrl = newsService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "图片上传失败");
            }
            return ToutiaoUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("图片上传失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "图片上传失败");
        }
    }
}
