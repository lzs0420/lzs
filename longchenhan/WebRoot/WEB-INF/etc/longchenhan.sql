-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: localhost    Database: longchenhan
-- ------------------------------------------------------
-- Server version	5.6.24-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `data_show`
--

DROP TABLE IF EXISTS `data_show`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_show` (
  `serialno` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据编号',
  `dataname` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '数据名称中文',
  `data` varchar(32) DEFAULT NULL COMMENT '数据名称',
  `amount` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '数量',
  `isshow` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '是否显示',
  `inputtime` varchar(19) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '录入时间',
  `status` varchar(1) DEFAULT '1',
  `lastupdateTime` varchar(19) DEFAULT NULL,
  PRIMARY KEY (`serialno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='前台显示数据配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_show`
--

LOCK TABLES `data_show` WRITE;
/*!40000 ALTER TABLE `data_show` DISABLE KEYS */;
INSERT INTO `data_show` VALUES ('201601160000001','访问的sb数','sbnum','23','1','2016/01/16 22:27:00','1','2016/01/16 22:27:00'),('201601160000002','访问的人数','membernum','0','1','2016/01/16 22:27:00','1','2016/01/16 22:27:00');
/*!40000 ALTER TABLE `data_show` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `USERID` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `USERNAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `PHONETEL` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `EMAIL` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `TRANSPWD` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `INPUTTIME` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `UPDATETIME` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `IsVIP` varchar(10) DEFAULT NULL COMMENT '是否为vip',
  `SECURITYQUESTION` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `SECURITYANSWER` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `LOCKFLAG` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `SECURITYQUESTION2` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `SECURITYANSWER2` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `PASSWORD` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `usercode` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '用户邀请码',
  `invitecode` varchar(40) DEFAULT NULL,
  `SECURITYANSWER3` varchar(4000) DEFAULT NULL,
  `SECURITYQUESTION3` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`USERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-19 16:44:28
