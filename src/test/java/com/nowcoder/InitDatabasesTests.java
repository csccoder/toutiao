package com.nowcoder;

import com.nowcoder.dao.NewsDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.News;
import com.nowcoder.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")
public class InitDatabasesTests {
	@Autowired
	UserDAO userDAO;

	@Autowired
	NewsDAO newsDAO;

	@Test
	public void contextLoads() {
		Random random=new Random();
		for(int i=1;i<11;i++){
			User user=new User();
			user.setId(i);
			user.setName(String.format("user%d",i));
			user.setPassword("");
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(10*i)));
			user.setSalt(UUID.randomUUID().toString().substring(10));
			userDAO.addUser(user);
			user.setPassword(String.format("password%d",i));
			userDAO.updatePassword(user);
		}
		userDAO.deleteById(1);
		User user=userDAO.selectUserById(2);
		System.out.println(user);

		for(int i=1;i<10;i++){
			News news =new News();
			news.setId(i);
			news.setImage(String.format("http://images.nowcoder.com/head/%dm.png",random.nextInt(10*i)));
			news.setLikeCount(random.nextInt(i*10));
			news.setCommentCount(random.nextInt(i*10));
			Date date = new Date();
			date.setTime(date.getTime() + 1000*3600*5*i);
			news.setCreatedDate(date);
			news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
			news.setUserId(i+1);
			news.setTitle(String.format("TITLE{%d}", i));
			newsDAO.addNews(news);
		}

		System.out.println(newsDAO.selectByUserIdAndOffset(0,0,10));

	}



}
