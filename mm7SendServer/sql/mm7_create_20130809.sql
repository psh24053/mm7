/*
SQLyog Ultimate v9.62 
MySQL - 5.0.90-community-nt : Database - mm7
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mm7` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `mm7`;

/*Table structure for table `mm7_content` */

DROP TABLE IF EXISTS `mm7_content`;

CREATE TABLE `mm7_content` (
  `contentId` int(6) NOT NULL auto_increment COMMENT '主键',
  `contentType` int(6) default NULL COMMENT '内容Type',
  `contentByte` text collate utf8_unicode_ci COMMENT '内容Byte,采用blob存储',
  `sendTaskId` int(6) default NULL COMMENT '所属发送任务',
  `sort` int(6) default NULL COMMENT '排序权重，从1开始',
  PRIMARY KEY  (`contentId`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='彩信内容表';

/*Data for the table `mm7_content` */

insert  into `mm7_content`(`contentId`,`contentType`,`contentByte`,`sendTaskId`,`sort`) values (1,1,'asdasldkaslaksdlkasldkasd',1,1),(2,1,'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb',1,2),(3,1,'sga23123123123123',1,3);

/*Table structure for table `mm7_failnumber` */

DROP TABLE IF EXISTS `mm7_failnumber`;

CREATE TABLE `mm7_failnumber` (
  `failNumberId` int(6) NOT NULL auto_increment COMMENT '主键',
  `number` varchar(32) collate utf8_unicode_ci default NULL COMMENT '号码',
  `sendTaskId` int(6) default NULL COMMENT '所属发送任务ID',
  PRIMARY KEY  (`failNumberId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='失败号码表';

/*Data for the table `mm7_failnumber` */

/*Table structure for table `mm7_number` */

DROP TABLE IF EXISTS `mm7_number`;

CREATE TABLE `mm7_number` (
  `numberId` int(6) NOT NULL auto_increment COMMENT '主键',
  `number` varchar(32) collate utf8_unicode_ci default NULL COMMENT '号码',
  PRIMARY KEY  (`numberId`),
  UNIQUE KEY `number_unique` (`number`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='号码表';

/*Data for the table `mm7_number` */

/*Table structure for table `mm7_sendtask` */

DROP TABLE IF EXISTS `mm7_sendtask`;

CREATE TABLE `mm7_sendtask` (
  `sendTaskId` int(6) NOT NULL auto_increment COMMENT '主键',
  `name` varchar(255) collate utf8_unicode_ci default NULL COMMENT '发送任务名称',
  `createTime` datetime default NULL COMMENT '创建时间',
  `toCount` int(10) default NULL COMMENT '接收号码数量',
  `customTo` text collate utf8_unicode_ci COMMENT '单独发送号码，用逗号分隔',
  `state` int(1) default NULL COMMENT '任务状态，1代表正在发送，2代表正在重发，3代表已发送',
  `successCount` int(10) default NULL COMMENT '成功数量',
  `failCount` int(10) default NULL COMMENT '失败数量',
  `completeTime` datetime default NULL COMMENT '完成时间',
  `subject` text collate utf8_unicode_ci COMMENT '任务内容标题',
  PRIMARY KEY  (`sendTaskId`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='发送任务表';

/*Data for the table `mm7_sendtask` */

insert  into `mm7_sendtask`(`sendTaskId`,`name`,`createTime`,`toCount`,`customTo`,`state`,`successCount`,`failCount`,`completeTime`,`subject`) values (1,'任务a','2013-08-06 22:29:38',20000,'18684012650',1,1,1,'2013-08-07 22:29:58',NULL);

/*Table structure for table `mm7_smcfailnumber` */

DROP TABLE IF EXISTS `mm7_smcfailnumber`;

CREATE TABLE `mm7_smcfailnumber` (
  `failNumberId` int(6) NOT NULL auto_increment COMMENT '主键',
  `number` varchar(32) collate utf8_unicode_ci default NULL COMMENT '号码',
  `SmcTaskId` int(6) default NULL COMMENT '所属发送任务ID',
  PRIMARY KEY  (`failNumberId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `mm7_smcfailnumber` */

/*Table structure for table `mm7_smctask` */

DROP TABLE IF EXISTS `mm7_smctask`;

CREATE TABLE `mm7_smctask` (
  `smcId` int(6) NOT NULL auto_increment COMMENT '主键',
  `content` text collate utf8_unicode_ci COMMENT '短信内容',
  `createTime` datetime default NULL COMMENT '短信创建时间',
  `toCount` int(10) default NULL COMMENT '接收号码数量',
  `customTo` text collate utf8_unicode_ci COMMENT '单独发送号码，用逗号分隔',
  `state` int(1) default NULL COMMENT '任务状态，1代表正在发送，2代表正在重发，3代表已发送',
  `successCount` int(10) default NULL COMMENT '成功数量',
  `failCount` int(10) default NULL COMMENT '失败数量',
  `completeTime` datetime default NULL COMMENT '完成时间',
  `subject` text collate utf8_unicode_ci COMMENT '短信主题',
  PRIMARY KEY  (`smcId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='短信任务表';

/*Data for the table `mm7_smctask` */

/*Table structure for table `mm7_user` */

DROP TABLE IF EXISTS `mm7_user`;

CREATE TABLE `mm7_user` (
  `userId` int(6) NOT NULL auto_increment COMMENT '主键',
  `username` varchar(255) collate utf8_unicode_ci default NULL COMMENT '用户名',
  `password` varchar(255) collate utf8_unicode_ci default NULL COMMENT '密码',
  `lastLoginTime` datetime default NULL COMMENT '最后登录时间',
  `grade` int(1) default NULL COMMENT '权限，3代表管理员，其他为普通用户',
  PRIMARY KEY  (`userId`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户表';

/*Data for the table `mm7_user` */

insert  into `mm7_user`(`userId`,`username`,`password`,`lastLoginTime`,`grade`) values (1,'admin','21232f297a57a5a743894a0e4a801fc3','2013-08-09 15:18:24',3);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
