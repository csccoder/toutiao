package com.nowcoder;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.NewsDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.*;
import org.junit.Assert;
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

	@Autowired
	LoginTicketDAO loginTicketDAO;

	@Autowired
	CommentDAO commentDAO;

	@Test
	public void contextLoads() {
		Random random=new Random();
		for(int i=1;i<20;i++){
			User user=new User();
			user.setId(i);
			user.setName(String.format("user%d",i));
			user.setPassword("");
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(10*i)));
			user.setSalt(UUID.randomUUID().toString().substring(10));
			user.setEmail(String.format("%d@qq.com",i));
			userDAO.addUser(user);
			user.setPassword(String.format("password%d",i));
			userDAO.updatePassword(user);

			LoginTicket ticket = new LoginTicket();
			ticket.setUserId(user.getId());
			ticket.setTicket(String.format("ticket%d",i));
			ticket.setExpired(new Date(System.currentTimeMillis()+1000*3600*24));
			ticket.setStatus(0);
			loginTicketDAO.addLoginTicket(ticket);
		}
		userDAO.deleteById(1);
		User user=userDAO.selectUserById(2);
		System.out.println(user);
		Assert.assertEquals(2,loginTicketDAO.selectLoginTicket("ticket2").getUserId());
		loginTicketDAO.updateStatus("ticket2",1);
		Assert.assertEquals(1,loginTicketDAO.selectLoginTicket("ticket2").getStatus());

		for(int i=2;i<20;i++){
			News news =new News();
			news.setId(i);
			news.setImage(String.format("http://images.nowcoder.com/head/%dm.png",random.nextInt(10*i)));
			news.setLikeCount(random.nextInt(i*10));
			news.setCommentCount(random.nextInt(i*10));
			Date date = new Date();
			date.setTime(date.getTime() + 1000*3600*5*i);
			news.setCreatedDate(date);
			news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
			news.setUserId(i);
			news.setTitle(String.format("TITLE{%d}", i));
			newsDAO.addNews(news);

			for(int j=1;j<=3;j++){
				Comment comment = new Comment();
				comment.setUserId(i);
				comment.setContent("hello"+i);
				comment.setCreatedDate(new Date());
				comment.setEntityId(i);
				comment.setEntityType(Entitype.NEWS.getValue());
				comment.setStatus(0);
				commentDAO.addComment(comment);
			}


		}
		System.out.println(newsDAO.selectByUserIdAndOffset(0,0,10));
		commentDAO.updateStatus(3,1);
		Assert.assertEquals(3,commentDAO.selectCount(3,Entitype.NEWS.getValue()));



	}



}
