DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL auto_increment,
  `name` VARCHAR (64) NOT NULL DEFAULT '',
  `password` VARCHAR (128) NOT NULL DEFAULT '',
  `salt` VARCHAR (32) NOT NULL DEFAULT '',  -- 盐
  `head_url` VARCHAR (256) NOT NULL DEFAULT '', -- 头像地址
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
)ENGINE=Innodb DEFAULT charset=utf8;

DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(128) NOT NULL DEFAULT '',
  `link` varchar(256) NOT NULL DEFAULT '', -- 文章链接
  `image` varchar(256) NOT NULL DEFAULT '',-- 文章展示图片
  `like_count` int NOT NULL, -- 点赞数
  `comment_count` int NOT NULL, -- 评论数 （增设冗余字段，查询时避免做关联查询）
  `created_date` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;