/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : bank

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2020-12-11 16:30:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `cardno` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `money` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('6029621011001', '韩梅梅', '10000');
INSERT INTO `account` VALUES ('6029621011000', '李大雷', '10000');
