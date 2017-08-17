/*
Navicat MySQL Data Transfer

Source Server         : 210.51.167.144
Source Server Version : 50534
Source Host           : 210.51.167.144:3306
Source Database       : trekizwholesalertts

Target Server Type    : MYSQL
Target Server Version : 50534
File Encoding         : 65001

Date: 2014-05-23 10:04:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for activityfile
-- ----------------------------
DROP TABLE IF EXISTS `activityfile`;
CREATE TABLE `activityfile` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键,自主递增',
  `srcActivityId` int(11) DEFAULT NULL COMMENT '产品信息表ID外键',
  `srcDocId` int(11) DEFAULT NULL COMMENT '文件上传附件表ID外键',
  `fileName` varchar(50) DEFAULT NULL COMMENT '上传的文件名称',
  `fileType` int(11) DEFAULT NULL COMMENT '上传的文件资料类型',
  `createBy` bigint(20) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `delFlag` char(1) DEFAULT NULL,
  `remarks` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1074 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for activitygroup
-- ----------------------------
DROP TABLE IF EXISTS `activitygroup`;
CREATE TABLE `activitygroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键,自助递增',
  `srcActivityId` int(11) DEFAULT NULL COMMENT '产品信息表ID外键',
  `groupCode` varchar(100) DEFAULT NULL,
  `groupOpenDate` date DEFAULT NULL COMMENT '产品中一个确定的出团日期',
  `groupCloseDate` date DEFAULT NULL COMMENT '产品中的一个确定的截团日期',
  `visaCountry` varchar(50) DEFAULT NULL COMMENT '产品的签证国家名称',
  `visaDate` date DEFAULT NULL COMMENT '送达签证的日期',
  `settlementAdultPrice` int(11) NOT NULL COMMENT '同业价成人',
  `settlementcChildPrice` int(11) NOT NULL COMMENT '同业价儿童',
  `suggestAdultPrice` int(11) NOT NULL COMMENT '建议成人零售价',
  `suggestChildPrice` int(11) NOT NULL COMMENT '建议儿童零售价',
  `singleDiff` int(11) NOT NULL COMMENT '单房差',
  `payDeposit` int(11) NOT NULL COMMENT '产品的预收定金',
  `planPosition` mediumint(9) NOT NULL COMMENT '产品的预收人数',
  `freePosition` mediumint(9) NOT NULL COMMENT '产品的剩余位置',
  `nopayReservePosition` mediumint(9) NOT NULL DEFAULT '0' COMMENT '各渠道总的占位人数',
  `payReservePosition` mediumint(9) NOT NULL DEFAULT '0' COMMENT '各渠道总的切位人数',
  `soldNopayPosition` mediumint(9) NOT NULL DEFAULT '0' COMMENT '售出占位',
  `soldPayPosition` mediumint(9) NOT NULL DEFAULT '0' COMMENT '售出切位',
  `srcDocId` mediumint(9) DEFAULT NULL COMMENT '产品上传出团通知书',
  `createBy` bigint(20) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `delFlag` char(1) CHARACTER SET latin1 DEFAULT NULL,
  `remarks` text,
  `version_number` varchar(200) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1273 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for activitygroupreserve
-- ----------------------------
DROP TABLE IF EXISTS `activitygroupreserve`;
CREATE TABLE `activitygroupreserve` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `srcActivityId` int(11) NOT NULL COMMENT '产品信息表ID外键',
  `activityGroupId` int(11) NOT NULL COMMENT '产品团期表ID',
  `agentId` int(11) NOT NULL COMMENT '渠道商基本信息表id',
  `reserveType` smallint(6) NOT NULL DEFAULT '0' COMMENT '0,定金占位；1,全款占位',
  `payReservePosition` mediumint(9) NOT NULL COMMENT '切位人数',
  `frontMoney` int(11) NOT NULL COMMENT '定金金额',
  `leftpayReservePosition` mediumint(9) NOT NULL COMMENT '剩余的切位人数',
  `leftFontMoney` int(11) NOT NULL COMMENT '剩余的定金金额',
  `reservation` varchar(10) DEFAULT NULL COMMENT '预订人',
  `payType` int(11) NOT NULL COMMENT '支付方式',
  `remark` varchar(20) DEFAULT NULL COMMENT '备注',
  `createBy` bigint(20) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `delFlag` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `agentId` (`agentId`,`srcActivityId`,`activityGroupId`) USING BTREE,
  KEY `activityGroupId` (`activityGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=223 DEFAULT CHARSET=utf8 COMMENT='产品每个团期的预收人数在各个总社和批发商自己的预留信息';

-- ----------------------------
-- Table structure for activityreservefile
-- ----------------------------
DROP TABLE IF EXISTS `activityreservefile`;
CREATE TABLE `activityreservefile` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `srcActivityId` int(11) NOT NULL COMMENT '产品信息表ID外键',
  `activityGroupId` int(11) NOT NULL COMMENT '产品团期表ID',
  `agentId` int(11) NOT NULL COMMENT '渠道商基本信息表id',
  `srcDocId` int(11) NOT NULL COMMENT '附件表id',
  `fileName` varchar(50) NOT NULL COMMENT '文件名称',
  `createDate` datetime DEFAULT NULL COMMENT '创建日期',
  `createBy` int(11) DEFAULT NULL COMMENT '创建人',
  `updateDate` datetime DEFAULT NULL COMMENT '更新日期',
  `updateBy` int(11) DEFAULT NULL COMMENT '更新人',
  `delFlag` char(1) DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=75 DEFAULT CHARSET=utf8 COMMENT='切位定金凭证文件表';

-- ----------------------------
-- Table structure for activitytargetarea
-- ----------------------------
DROP TABLE IF EXISTS `activitytargetarea`;
CREATE TABLE `activitytargetarea` (
  `srcActivityId` int(11) NOT NULL COMMENT '产品信息表ID外键',
  `targetAreaId` int(11) NOT NULL COMMENT '目的地id',
  UNIQUE KEY `srcActivityId` (`srcActivityId`,`targetAreaId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for activityvisafile
-- ----------------------------
DROP TABLE IF EXISTS `activityvisafile`;
CREATE TABLE `activityvisafile` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `srcActivityId` int(11) DEFAULT NULL COMMENT '产品id',
  `visaType` int(11) DEFAULT NULL COMMENT '签证类型',
  `countryId` int(11) DEFAULT NULL COMMENT '国家id',
  `countryName` varchar(50) DEFAULT NULL COMMENT '国家名称',
  `srcDocId` int(11) DEFAULT NULL COMMENT '附件表id',
  `createDate` datetime DEFAULT NULL COMMENT '创建日期',
  `createBy` int(11) DEFAULT NULL COMMENT '创建人',
  `updateDate` datetime DEFAULT NULL COMMENT '更新日期',
  `updateBy` int(11) DEFAULT NULL COMMENT '更新人',
  `delFlag` char(1) DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=620 DEFAULT CHARSET=utf8 COMMENT='产品签证信息表';

-- ----------------------------
-- Table structure for agentinfo
-- ----------------------------
DROP TABLE IF EXISTS `agentinfo`;
CREATE TABLE `agentinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agentName` varchar(50) DEFAULT NULL COMMENT '渠道商名称',
  `agentAddress` varchar(100) DEFAULT NULL COMMENT '渠道商地址',
  `agentContact` varchar(20) DEFAULT NULL COMMENT '渠道商联系人',
  `agentTel` varchar(20) DEFAULT NULL COMMENT '渠道电话',
  `agentFax` varchar(20) DEFAULT NULL COMMENT '渠道传真',
  `agentEmail` varchar(50) DEFAULT NULL COMMENT '渠道邮箱',
  `agentQQ` varchar(50) DEFAULT NULL COMMENT '渠道商qq',
  `agentSalerId` int(11) DEFAULT NULL COMMENT '渠道跟进销售员Id',
  `agentSaler` varchar(10) DEFAULT NULL COMMENT '渠道跟进销售员',
  `agentBussiness` float DEFAULT '0' COMMENT '渠道当年营业额',
  `agentTourists` int(11) DEFAULT '0' COMMENT '渠道当年游客数',
  `currentYear` int(11) DEFAULT NULL,
  `agentLevel` smallint(6) DEFAULT NULL COMMENT '渠道等级',
  `agentLevelName` varchar(10) DEFAULT NULL COMMENT '渠道等级名称',
  `cooperateStatus` smallint(6) DEFAULT NULL COMMENT '合作状态',
  `cooperateStatusName` varchar(10) DEFAULT NULL COMMENT '合作状态名称',
  `agentRetComRadio` float DEFAULT NULL COMMENT '返佣比例',
  `supplyId` int(11) DEFAULT NULL,
  `supplyName` varchar(100) DEFAULT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `delFlag` char(1) DEFAULT NULL,
  `remarks` text,
  `strategyLevel` varchar(2) DEFAULT NULL,
  `strategyLevelName` varchar(10) DEFAULT NULL,
  `is_synchronize` tinyint(4) DEFAULT '0' COMMENT '是否只供正反向平台同步的特殊渠道商，1：是；0：否',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='渠道商基本信息表';

-- ----------------------------
-- Table structure for agentstrategy
-- ----------------------------
DROP TABLE IF EXISTS `agentstrategy`;
CREATE TABLE `agentstrategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agentId` int(11) DEFAULT NULL,
  `strategyId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=162 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for areaDefineDict
-- ----------------------------
DROP TABLE IF EXISTS `areaDefineDict`;
CREATE TABLE `areaDefineDict` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `companyId` int(50) DEFAULT NULL,
  `areaId` int(50) DEFAULT NULL,
  `trafficId` int(50) DEFAULT NULL,
  `fromareaId` int(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for changegroup
-- ----------------------------
DROP TABLE IF EXISTS `changegroup`;
CREATE TABLE `changegroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `orderId` int(11) DEFAULT NULL COMMENT '订单表Id',
  `groupChangeType` int(11) DEFAULT NULL COMMENT '退换团类型',
  `groupChangeRemark` varchar(1000) DEFAULT NULL COMMENT '申请说明',
  `applicant` int(11) DEFAULT NULL COMMENT '申请人',
  `ApplicantTime` datetime DEFAULT NULL COMMENT '申请时间',
  `handlerPerson` int(11) DEFAULT NULL COMMENT '处理人',
  `handlerTime` datetime DEFAULT NULL COMMENT '处理时间',
  `handlerRemark` varchar(1000) DEFAULT NULL COMMENT '处理说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='退换团处理表';

-- ----------------------------
-- Table structure for cms_article
-- ----------------------------
DROP TABLE IF EXISTS `cms_article`;
CREATE TABLE `cms_article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `categoryId` bigint(20) NOT NULL COMMENT '栏目编号',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '文章内容',
  `color` varchar(50) DEFAULT NULL COMMENT '标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）',
  `image` varchar(255) DEFAULT NULL COMMENT '文章图片',
  `keywords` varchar(255) DEFAULT NULL COMMENT '关键字',
  `description` varchar(255) DEFAULT NULL COMMENT '描述、摘要',
  `weight` int(11) DEFAULT '0' COMMENT '权重，越大越靠前',
  `weightDate` datetime DEFAULT NULL COMMENT '权重期限，过期后将权重设置为：0',
  `hits` int(11) DEFAULT '0' COMMENT '点击数',
  `posid` varchar(10) DEFAULT NULL COMMENT '推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  `fromDate` date NOT NULL COMMENT '开始时间',
  `toDate` date NOT NULL COMMENT '截止时间',
  PRIMARY KEY (`id`),
  KEY `cmsArticleCreateBy` (`createBy`) USING BTREE,
  KEY `cmsArticleTitle` (`title`) USING BTREE,
  KEY `cmsArticleKeywords` (`keywords`) USING BTREE,
  KEY `cmsArticleDelFlag` (`delFlag`) USING BTREE,
  KEY `cmsArticleWeight` (`weight`) USING BTREE,
  KEY `cmsArticleUpdateDate` (`updateDate`) USING BTREE,
  KEY `cmsArticleCategoryId` (`categoryId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8 COMMENT='文章表';

-- ----------------------------
-- Table structure for cms_article_data
-- ----------------------------
DROP TABLE IF EXISTS `cms_article_data`;
CREATE TABLE `cms_article_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `content` text COMMENT '文章内容',
  `copyfrom` varchar(255) DEFAULT NULL COMMENT '文章来源',
  `relation` varchar(255) DEFAULT NULL COMMENT '相关文章',
  `allowComment` char(1) DEFAULT NULL COMMENT '是否允许评论',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8 COMMENT='文章详表';

-- ----------------------------
-- Table structure for cms_category
-- ----------------------------
DROP TABLE IF EXISTS `cms_category`;
CREATE TABLE `cms_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `siteId` bigint(20) DEFAULT '1' COMMENT '站点编号',
  `officeId` bigint(20) DEFAULT NULL COMMENT '归属机构',
  `parentId` bigint(20) NOT NULL COMMENT '父级编号',
  `parentIds` varchar(255) NOT NULL COMMENT '所有父级编号',
  `module` varchar(20) DEFAULT NULL COMMENT '栏目模块（article：文章；picture：图片；download：下载；link：链接；special：专题）',
  `name` varchar(100) NOT NULL COMMENT '栏目名称',
  `image` varchar(255) DEFAULT NULL COMMENT '栏目图片',
  `href` varchar(255) DEFAULT NULL COMMENT '链接',
  `target` varchar(20) DEFAULT NULL COMMENT '目标（ _blank、_self、_parent、_top）',
  `description` varchar(255) DEFAULT NULL COMMENT '描述，填写有助于搜索引擎优化',
  `keywords` varchar(255) DEFAULT NULL COMMENT '关键字，填写有助于搜索引擎优化',
  `sort` int(11) DEFAULT '30' COMMENT '排序（升序）',
  `inMenu` char(1) DEFAULT '1' COMMENT '是否在导航中显示（1：显示；0：不显示）',
  `inList` char(1) DEFAULT '1' COMMENT '是否在分类页中显示列表（1：显示；0：不显示）',
  `showModes` char(1) DEFAULT '0' COMMENT '展现方式（0:有子栏目显示栏目列表，无子栏目显示内容列表;1：首栏目内容列表；2：栏目第一条内容）',
  `allowComment` char(1) DEFAULT NULL COMMENT '是否允许评论',
  `isAudit` char(1) DEFAULT NULL COMMENT '是否需要审核',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`),
  KEY `cmsCategoryModule` (`module`) USING BTREE,
  KEY `cmsCategoryName` (`name`) USING BTREE,
  KEY `cmsCategorySort` (`sort`) USING BTREE,
  KEY `cmsCategoryOfficeId` (`officeId`) USING BTREE,
  KEY `cmsCategorySiteIDd` (`siteId`) USING BTREE,
  KEY `cmsCategoryParentId` (`parentId`) USING BTREE,
  KEY `cmsCategoryParentIds` (`parentIds`) USING BTREE,
  KEY `cmsCategoryDelFlag` (`delFlag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='栏目表';

-- ----------------------------
-- Table structure for cms_comment
-- ----------------------------
DROP TABLE IF EXISTS `cms_comment`;
CREATE TABLE `cms_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `categoryId` bigint(20) NOT NULL COMMENT '栏目编号',
  `contentId` bigint(20) NOT NULL COMMENT '栏目内容的编号（Article.id、Photo.id、Download.id）',
  `title` varchar(255) DEFAULT NULL COMMENT '栏目内容的标题（Article.title、Photo.title、Download.title）',
  `content` varchar(255) DEFAULT NULL COMMENT '评论内容',
  `name` varchar(100) DEFAULT NULL COMMENT '评论姓名',
  `ip` varchar(100) DEFAULT NULL COMMENT '评论IP',
  `createDate` datetime NOT NULL COMMENT '评论时间',
  `auditUserId` bigint(20) DEFAULT NULL COMMENT '审核人',
  `auditDate` datetime DEFAULT NULL COMMENT '审核时间',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`),
  KEY `cmsCommentCategoryId` (`categoryId`) USING BTREE,
  KEY `cmsCommentContentId` (`contentId`) USING BTREE,
  KEY `cmsCommentStatus` (`delFlag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论表';

-- ----------------------------
-- Table structure for cms_guestbook
-- ----------------------------
DROP TABLE IF EXISTS `cms_guestbook`;
CREATE TABLE `cms_guestbook` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type` char(1) NOT NULL COMMENT '留言分类（1咨询、2建议、3投诉、4其它）',
  `content` varchar(255) NOT NULL COMMENT '留言内容',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `phone` varchar(100) NOT NULL COMMENT '电话',
  `workunit` varchar(100) NOT NULL COMMENT '单位',
  `ip` varchar(100) NOT NULL COMMENT 'IP',
  `createDate` datetime NOT NULL COMMENT '留言时间',
  `reUserId` bigint(20) DEFAULT NULL COMMENT '回复人',
  `reDate` datetime DEFAULT NULL COMMENT '回复时间',
  `reContent` varchar(100) DEFAULT NULL COMMENT '回复内容',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`),
  KEY `cmsGuestbookDelFlag` (`delFlag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='留言板';

-- ----------------------------
-- Table structure for cms_link
-- ----------------------------
DROP TABLE IF EXISTS `cms_link`;
CREATE TABLE `cms_link` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `categoryId` bigint(20) NOT NULL COMMENT '栏目编号',
  `title` varchar(255) NOT NULL COMMENT '链接名称',
  `color` varchar(50) DEFAULT NULL COMMENT '标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）',
  `image` varchar(255) DEFAULT NULL COMMENT '链接图片，如果上传了图片，则显示为图片链接',
  `href` varchar(255) DEFAULT NULL COMMENT '链接地址',
  `weight` int(11) DEFAULT '0' COMMENT '权重，越大越靠前',
  `weightDate` datetime DEFAULT NULL COMMENT '权重期限，过期后将权重设置为：0',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`),
  KEY `cmsLinkCategoryId` (`categoryId`) USING BTREE,
  KEY `cmsLinkTitle` (`title`) USING BTREE,
  KEY `cmsLinkDelFlag` (`delFlag`) USING BTREE,
  KEY `cmsLinkWeight` (`weight`) USING BTREE,
  KEY `cmsLinkCreateBy` (`createBy`) USING BTREE,
  KEY `cmsLinkUpdateDate` (`updateDate`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='友情链接';

-- ----------------------------
-- Table structure for costchange
-- ----------------------------
DROP TABLE IF EXISTS `costchange`;
CREATE TABLE `costchange` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `costName` varchar(50) DEFAULT NULL,
  `costSum` int(11) DEFAULT NULL,
  `travelerId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=721 DEFAULT CHARSET=utf8 COMMENT='费用变更';

-- ----------------------------
-- Table structure for docinfo
-- ----------------------------
DROP TABLE IF EXISTS `docinfo`;
CREATE TABLE `docinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键,自主递增',
  `docName` varchar(255) DEFAULT NULL,
  `docPath` varchar(100) DEFAULT NULL COMMENT '存放文件的路径',
  `docType` int(11) DEFAULT NULL COMMENT '上传的文件类型',
  `createBy` bigint(20) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `delFlag` char(1) DEFAULT NULL,
  `remarks` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2194 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for exception_synchronize_log
-- ----------------------------
DROP TABLE IF EXISTS `exception_synchronize_log`;
CREATE TABLE `exception_synchronize_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '类型, 1：添加 , 2：修改 , 3：删除',
  `id_trekizwholesaler` int(11) NOT NULL COMMENT '反向平台产品ID',
  `groupId_trekizwholesaler` int(11) DEFAULT NULL COMMENT '反向平台团期ID',
  `sql_str` varchar(600) DEFAULT NULL COMMENT '当前执行的sql语句',
  `state` int(11) DEFAULT NULL COMMENT '状态, 0:未读 , 1:已读 , 2.已解决 ',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for orderinvoice
-- ----------------------------
DROP TABLE IF EXISTS `orderinvoice`;
CREATE TABLE `orderinvoice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `orderId` int(11) DEFAULT NULL COMMENT '订单ID',
  `orderNum` varchar(50) DEFAULT NULL COMMENT '订单单号',
  `invoiceNum` varchar(20) DEFAULT NULL,
  `payId` int(11) DEFAULT NULL,
  `groupCode` varchar(20) DEFAULT NULL COMMENT '团号',
  `verifyStatus` smallint(6) DEFAULT NULL COMMENT '发票审核状态',
  `invoiceAmount` int(11) DEFAULT NULL,
  `invoiceMode` smallint(6) DEFAULT NULL,
  `invoiceType` smallint(6) DEFAULT NULL,
  `invoiceCustomer` varchar(100) DEFAULT NULL,
  `invoiceSubject` smallint(6) DEFAULT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `delFlag` char(1) DEFAULT NULL,
  `invoiceCompany` int(11) DEFAULT NULL COMMENT '发票机构ID',
  `invoiceCompanyName` varchar(50) DEFAULT NULL COMMENT '发票机构名称',
  `remarks` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for orderpay
-- ----------------------------
DROP TABLE IF EXISTS `orderpay`;
CREATE TABLE `orderpay` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `payType` int(11) DEFAULT NULL COMMENT '支付方式',
  `payTypeName` varchar(50) DEFAULT NULL COMMENT '支付方式名称',
  `payerName` varchar(100) DEFAULT NULL COMMENT '付款单位',
  `checkNumber` varchar(50) DEFAULT NULL COMMENT '支票号',
  `invoiceDate` datetime DEFAULT NULL COMMENT '开票日期',
  `payVoucher` int(11) DEFAULT NULL COMMENT '支付凭证附件id',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `posNo` varchar(50) DEFAULT NULL COMMENT 'POS单号',
  `posTagEendNo` varchar(50) DEFAULT NULL COMMENT 'POS机终端号',
  `posBank` varchar(100) DEFAULT NULL COMMENT 'POS机所属银行',
  `bankName` varchar(100) DEFAULT NULL COMMENT '开户行名称',
  `toBankNname` varchar(100) DEFAULT NULL COMMENT '转入行名称',
  `bankAccount` varchar(50) DEFAULT NULL COMMENT '开户行账户',
  `toBankAccount` varchar(50) DEFAULT NULL COMMENT '转入行账号',
  `payPrice` int(11) DEFAULT NULL COMMENT '支付金额',
  `payPriceType` tinyint(4) DEFAULT NULL COMMENT '支付款类型（全款、定金、尾款）',
  `orderId` int(11) DEFAULT NULL COMMENT '订单号',
  `createBy` int(11) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建日期',
  `updateBy` int(11) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新日期',
  `delFlag` char(1) DEFAULT NULL COMMENT '删除标记',
  `isAsAccount` smallint(6) DEFAULT NULL COMMENT '是否达账',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=458 DEFAULT CHARSET=utf8 COMMENT='订单支付表';

-- ----------------------------
-- Table structure for pricestrategy
-- ----------------------------
DROP TABLE IF EXISTS `pricestrategy`;
CREATE TABLE `pricestrategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agentId` int(11) DEFAULT NULL COMMENT '渠道商基本信息ID',
  `strategyNum` varchar(10) DEFAULT NULL COMMENT '策略编号',
  `strategyLevel` varchar(6) DEFAULT NULL COMMENT '策略等级',
  `regionId` smallint(6) DEFAULT NULL COMMENT '区域ID',
  `regionName` varchar(20) DEFAULT NULL COMMENT '区域名称',
  `productLevel` smallint(6) DEFAULT NULL COMMENT '产品系列/等级',
  `productLevelName` varchar(20) DEFAULT NULL COMMENT '产品系列名称',
  `strategyDateStart` date DEFAULT NULL COMMENT '策略起始时间',
  `strategyDateEnd` date DEFAULT NULL COMMENT '策略结束时间',
  `strategyPrice` int(11) DEFAULT NULL COMMENT '策略价格',
  `saleAmountStart` int(11) DEFAULT NULL COMMENT '起始销售额',
  `saleAmountEnd` int(11) DEFAULT NULL COMMENT '结束销售额',
  `saleRadio` int(11) DEFAULT '0' COMMENT '按销售额返还比例',
  `zjtouristsStart` int(11) DEFAULT NULL COMMENT '直减起始游客数量',
  `zjtouristsEnd` int(11) DEFAULT NULL COMMENT '直减结束游客数量',
  `touristsStart` int(11) DEFAULT NULL COMMENT '起始游客数量',
  `touristsEnd` int(11) DEFAULT NULL COMMENT '结束游客数量',
  `touristsRadio` int(11) DEFAULT '0' COMMENT '按游客数量返还比例',
  `strategyType` smallint(6) DEFAULT NULL COMMENT '策略类型',
  `strategyName` varchar(10) DEFAULT NULL COMMENT '策略名称',
  `supplyId` int(11) DEFAULT NULL,
  `supplyName` varchar(20) DEFAULT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `delFlag` char(1) DEFAULT '0',
  `remarks` text,
  `hfType` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=286 DEFAULT CHARSET=utf8 COMMENT='定价策略表';

-- ----------------------------
-- Table structure for productorder
-- ----------------------------
DROP TABLE IF EXISTS `productorder`;
CREATE TABLE `productorder` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `productId` int(11) DEFAULT NULL COMMENT '产品id',
  `productGroupId` int(11) DEFAULT NULL COMMENT '产品出团信息表id',
  `orderNum` varchar(50) DEFAULT NULL COMMENT '订单单号',
  `orderType` tinyint(4) DEFAULT NULL COMMENT '订单状态',
  `orderCompany` int(11) DEFAULT NULL COMMENT '预订单位',
  `orderCompanyName` varchar(100) DEFAULT NULL COMMENT '预订单位名称',
  `orderPersonName` varchar(50) DEFAULT NULL COMMENT '预订人名称',
  `orderPersonPhoneNum` varchar(50) DEFAULT NULL COMMENT '预订人联系电话',
  `orderTime` datetime DEFAULT NULL COMMENT '预订日期',
  `orderPersonNum` tinyint(4) DEFAULT NULL COMMENT '预定人数',
  `orderPersonNumAdult` tinyint(4) DEFAULT NULL COMMENT '成人预定人数',
  `orderPersonNumChild` tinyint(4) DEFAULT NULL COMMENT '儿童预定人数',
  `frontMoney` int(11) DEFAULT NULL COMMENT '定金金额',
  `payStatus` tinyint(4) DEFAULT NULL COMMENT '支付状态',
  `total_money` int(11) DEFAULT NULL COMMENT '订单总额（应该支付）',
  `payed_money` int(11) DEFAULT NULL COMMENT '已支付金额',
  `payType` tinyint(4) DEFAULT NULL COMMENT '支付方式',
  `isAlreadyInvoice` smallint(6) DEFAULT NULL COMMENT '是否已开发票',
  `createBy` int(11) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建日期',
  `updateBy` int(11) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新日期',
  `delFlag` char(1) DEFAULT NULL COMMENT '删除标记',
  `changeGroupId` int(11) DEFAULT NULL COMMENT '当前退换记录Id',
  `groupChangeType` int(11) DEFAULT NULL COMMENT '退换类型',
  `asAcountType` smallint(11) DEFAULT NULL COMMENT '达账状态',
  `accounted_money` int(11) DEFAULT NULL COMMENT '总达账金额',
  `settlementAdultPrice` int(11) DEFAULT NULL COMMENT '下订单时成人的产品结算价',
  `settlementcChildPrice` int(11) DEFAULT NULL COMMENT '下订单时儿童的产品结算价',
  `payDeposit` int(11) DEFAULT NULL COMMENT '下订单时产品的预收定金',
  `placeHolderType` tinyint(4) DEFAULT NULL COMMENT '占位类型    如果为0  或者为空  表示是占位  如果为1  表示是切位',
  `singleDiff` int(11) DEFAULT NULL COMMENT '下订单时的单房差',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=639 DEFAULT CHARSET=utf8 COMMENT='订单信息表';

-- ----------------------------
-- Table structure for regist
-- ----------------------------
DROP TABLE IF EXISTS `regist`;
CREATE TABLE `regist` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `loginName` char(20) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机',
  `qq` int(15) DEFAULT NULL COMMENT 'QQ',
  `email` varchar(30) DEFAULT NULL COMMENT 'Email',
  `pName` varchar(30) DEFAULT NULL COMMENT '品牌名',
  `companyName` varchar(30) DEFAULT NULL COMMENT '签约公司名称',
  `manageRange` varchar(30) DEFAULT NULL COMMENT '获许经营范围',
  `fund` bigint(12) DEFAULT NULL COMMENT '注册资金',
  `permitNo` int(50) DEFAULT NULL COMMENT '经营商许可证编号',
  `permitNoValidityStart` date DEFAULT NULL COMMENT '经营商许可证编号有效期时间',
  `permitNoValidityEnd` date DEFAULT NULL COMMENT '经营商许可证编号有效期结束时间',
  `permitNoPapers` varchar(50) DEFAULT NULL COMMENT '经营许可证扫描件',
  `permitRegistNo` int(50) DEFAULT NULL COMMENT '营业执照注册号',
  `permitRegistNoValidityStart` date DEFAULT NULL COMMENT '营业执照注册号有效期时间',
  `permitRegistNoValidityEnd` date DEFAULT NULL COMMENT '营业执照注册号有效期结束时间',
  `permitRegistNoPapers` varchar(50) DEFAULT NULL COMMENT '营业执照扫描件',
  `tax` int(50) DEFAULT NULL COMMENT '税务登记证税字',
  `taxValidityStart` date DEFAULT NULL COMMENT '税务登记证税字有效期时间',
  `taxValidityEnd` date DEFAULT NULL COMMENT '税务登记证税字有效期结束时间',
  `taxPapers` varchar(50) DEFAULT NULL COMMENT '税务登记证扫描件',
  `insure` varchar(50) DEFAULT NULL COMMENT '责任保险单扫描件',
  `insureValidityStart` date DEFAULT NULL COMMENT '责任保险单有效期时间',
  `insureValidityEnd` date DEFAULT NULL COMMENT '责任保险单有效期结束时间',
  `address` varchar(30) DEFAULT NULL COMMENT '公司地址',
  `postcode` int(6) DEFAULT NULL COMMENT '邮编',
  `remark` varchar(100) DEFAULT NULL COMMENT '产品简介',
  `fax` varchar(10) DEFAULT NULL COMMENT '传真',
  `joinType` varchar(10) DEFAULT NULL COMMENT '加盟类型',
  `createDate` date DEFAULT NULL COMMENT '申请时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for registLog
-- ----------------------------
DROP TABLE IF EXISTS `registLog`;
CREATE TABLE `registLog` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `registId` int(50) DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `log` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for success_synchronize_log
-- ----------------------------
DROP TABLE IF EXISTS `success_synchronize_log`;
CREATE TABLE `success_synchronize_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '类型, 1：添加 , 2 修改 , 3.删除',
  `id_trekizwholesaler` int(11) NOT NULL COMMENT '反向平台产品ID',
  `groupId_trekizwholesaler` int(11) DEFAULT NULL COMMENT '反向平台团期ID',
  `sql_str` varchar(600) DEFAULT NULL COMMENT '当前执行的sql语句',
  `state` int(11) DEFAULT NULL COMMENT '状态, 0:未读 , 1:已读',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for supplyerdict
-- ----------------------------
DROP TABLE IF EXISTS `supplyerdict`;
CREATE TABLE `supplyerdict` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典id',
  `code` smallint(6) NOT NULL COMMENT '字典编码',
  `name` varchar(20) NOT NULL COMMENT '字典名称',
  `type` varchar(20) NOT NULL COMMENT '字典类型',
  `supplyId` int(11) NOT NULL COMMENT '批发商id',
  `supplyName` varchar(100) DEFAULT NULL COMMENT '批发商名称',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` date DEFAULT NULL COMMENT '创建日期',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新者',
  `updateDate` date DEFAULT NULL COMMENT '更新日期',
  `delFlag` char(1) DEFAULT NULL COMMENT '删除标志',
  `remarks` text COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='批发商字典表';

-- ----------------------------
-- Table structure for sys_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parentId` bigint(20) NOT NULL COMMENT '父级编号',
  `parentIds` varchar(255) NOT NULL COMMENT '所有父级编号',
  `code` varchar(100) DEFAULT NULL COMMENT '区域编码',
  `name` varchar(100) NOT NULL COMMENT '区域名称',
  `type` char(1) DEFAULT NULL COMMENT '区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`),
  KEY `sysAreaParentId` (`parentId`) USING BTREE,
  KEY `sysAreaParentIds` (`parentIds`) USING BTREE,
  KEY `sysAreaDelFlag` (`delFlag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=200165 DEFAULT CHARSET=utf8 COMMENT='区域表';

-- ----------------------------
-- Table structure for sys_country
-- ----------------------------
DROP TABLE IF EXISTS `sys_country`;
CREATE TABLE `sys_country` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cc_fips` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `cc_iso` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tld` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `countryName` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '国家名英文',
  `continent` int(2) DEFAULT NULL,
  `isHotel` smallint(6) DEFAULT NULL,
  `countryName_cn` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '国家名中文',
  `continentName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '所属洲',
  `displayStatus` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cc_iso` (`cc_iso`),
  KEY `countryName` (`countryName`),
  KEY `countryName_2` (`countryName`,`cc_iso`)
) ENGINE=MyISAM AUTO_INCREMENT=2833 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `label` varchar(100) NOT NULL COMMENT '标签名',
  `value` varchar(100) NOT NULL COMMENT '数据值',
  `type` varchar(100) NOT NULL COMMENT '类型',
  `description` varchar(100) NOT NULL COMMENT '描述',
  `sort` int(11) NOT NULL COMMENT '排序（升序）',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`),
  KEY `sysDictValue` (`value`) USING BTREE,
  KEY `sysDictLabel` (`label`) USING BTREE,
  KEY `sysDictDelFlag` (`delFlag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1269 DEFAULT CHARSET=utf8 COMMENT='字典表';

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type` char(1) DEFAULT '1' COMMENT '日志类型（1：接入日志；2：异常日志）',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `remoteAddr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `userAgent` varchar(255) DEFAULT NULL COMMENT '用户代理',
  `requestUri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `method` varchar(5) DEFAULT NULL COMMENT '操作方式',
  `params` text COMMENT '操作提交的数据',
  `exception` text COMMENT '异常信息',
  PRIMARY KEY (`id`),
  KEY `sysLogCreateBy` (`createBy`) USING BTREE,
  KEY `sysLogRequestUri` (`requestUri`) USING BTREE,
  KEY `sysLogType` (`type`) USING BTREE,
  KEY `sysLogCreateDate` (`createDate`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5417 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_mdict
-- ----------------------------
DROP TABLE IF EXISTS `sys_mdict`;
CREATE TABLE `sys_mdict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parentId` bigint(20) NOT NULL COMMENT '父级编号',
  `parentIds` varchar(255) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `sort` int(11) DEFAULT NULL COMMENT '排序（升序）',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`),
  KEY `sysMdictParentId` (`parentId`) USING BTREE,
  KEY `sysMdictParentIds` (`parentIds`) USING BTREE,
  KEY `sysMdictDelFlag` (`delFlag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='区域表';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parentId` bigint(20) NOT NULL COMMENT '父级编号',
  `parentIds` varchar(255) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '菜单名称',
  `href` varchar(255) DEFAULT NULL COMMENT '链接',
  `target` varchar(20) DEFAULT NULL COMMENT '目标（mainFrame、 _blank、_self、_parent、_top）',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `sort` int(11) NOT NULL COMMENT '排序（升序）',
  `isShow` char(1) NOT NULL COMMENT '是否在菜单中显示（1：显示；0：不显示）',
  `permission` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`),
  KEY `sysMenuParentId` (`parentId`) USING BTREE,
  KEY `sysMenuParentIds` (`parentIds`) USING BTREE,
  KEY `sysMenuDelFlag` (`delFlag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=186 DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Table structure for sys_office
-- ----------------------------
DROP TABLE IF EXISTS `sys_office`;
CREATE TABLE `sys_office` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parentId` bigint(20) NOT NULL COMMENT '父级编号',
  `parentIds` varchar(255) NOT NULL COMMENT '所有父级编号',
  `code` varchar(100) DEFAULT NULL COMMENT '区域编码',
  `name` varchar(100) NOT NULL COMMENT '机构名称',
  `areaId` int(11) NOT NULL DEFAULT '0',
  `address` varchar(255) DEFAULT NULL COMMENT '联系地址',
  `zipCode` varchar(100) DEFAULT NULL COMMENT '邮政编码',
  `master` varchar(100) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `fax` varchar(200) DEFAULT NULL COMMENT '传真',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  `is_check_domainName` tinyint(4) DEFAULT '1' COMMENT '是否验证域名操作，1：验证；0：不验证',
  `domain_name` varchar(200) DEFAULT NULL COMMENT '域名',
  `sign_key` varchar(100) DEFAULT NULL COMMENT '签名，正向平台添加',
  PRIMARY KEY (`id`),
  KEY `sysOfficeParentId` (`parentId`) USING BTREE,
  KEY `sysOfficeParentIds` (`parentIds`) USING BTREE,
  KEY `sysOfficeDelFlag` (`delFlag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='部门表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `officeId` bigint(20) DEFAULT NULL COMMENT '归属机构',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `enname` varchar(255) DEFAULT NULL COMMENT '英文名称',
  `userType` char(1) NOT NULL COMMENT '用户类型:1,渠道商;3,接待社',
  `roleType` varchar(255) DEFAULT NULL COMMENT '角色类型',
  `dataScope` char(1) DEFAULT NULL COMMENT '数据范围（0：所有数据；1：所在公司及以下数据；2：所在公司数据；3：所在部门及以下数据；4：所在部门数据；8：仅本人数据；9：按明细设置）',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`),
  KEY `sysRoleDelFlag` (`delFlag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `roleId` bigint(20) NOT NULL COMMENT '角色编号',
  `menuId` bigint(20) NOT NULL COMMENT '菜单编号',
  PRIMARY KEY (`roleId`,`menuId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-菜单';

-- ----------------------------
-- Table structure for sys_role_office
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_office`;
CREATE TABLE `sys_role_office` (
  `roleId` bigint(20) NOT NULL COMMENT '角色编号',
  `officeId` bigint(20) NOT NULL COMMENT '机构编号',
  PRIMARY KEY (`roleId`,`officeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色-机构';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `companyId` int(11) NOT NULL DEFAULT '0' COMMENT '归属接待社id',
  `agentId` int(11) NOT NULL DEFAULT '0' COMMENT '渠道商基本信息表id',
  `officeId` bigint(20) NOT NULL DEFAULT '0' COMMENT '归属部门',
  `loginName` varchar(100) NOT NULL COMMENT '登录名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `no` varchar(100) DEFAULT NULL COMMENT '工号',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(200) DEFAULT NULL COMMENT '手机',
  `userType` char(1) DEFAULT NULL COMMENT '用户类型:1,渠道商;3,接待社',
  `loginIp` varchar(100) DEFAULT NULL COMMENT '最后登陆IP',
  `loginDate` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建者',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新者',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`),
  KEY `sysUserOfficeId` (`officeId`) USING BTREE,
  KEY `sysUserLoginName` (`loginName`) USING BTREE,
  KEY `sysUserCompanyId` (`companyId`) USING BTREE,
  KEY `sysUserUpdateDate` (`updateDate`) USING BTREE,
  KEY `sysUserDelFlag` (`delFlag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `userId` bigint(20) NOT NULL COMMENT '用户编号',
  `roleId` bigint(20) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`userId`,`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色';

-- ----------------------------
-- Table structure for sysdefinedict
-- ----------------------------
DROP TABLE IF EXISTS `sysdefinedict`;
CREATE TABLE `sysdefinedict` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `label` varchar(100) DEFAULT NULL COMMENT '字典名称',
  `value` varchar(50) DEFAULT NULL COMMENT '字典值',
  `type` varchar(20) DEFAULT NULL COMMENT '字典类型',
  `defaultFlag` char(1) DEFAULT '0' COMMENT '是否默认值，0:不是；1:是',
  `sort` int(11) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL COMMENT '字典描述',
  `companyId` int(11) DEFAULT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `delFlag` char(1) DEFAULT NULL,
  `remarks` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `companyId` (`companyId`,`type`,`value`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=376 DEFAULT CHARSET=utf8 COMMENT='自定义字典表';

-- ----------------------------
-- Table structure for sysincrease
-- ----------------------------
DROP TABLE IF EXISTS `sysincrease`;
CREATE TABLE `sysincrease` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codeName` varchar(10) DEFAULT NULL,
  `codeNum` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `dateMark` varchar(12) DEFAULT NULL COMMENT '日期标志',
  `numLen` smallint(6) DEFAULT '0' COMMENT '数字长度',
  `proCompanyId` smallint(6) DEFAULT NULL COMMENT '批发商ID',
  `codeType` smallint(6) DEFAULT NULL,
  `createBy` bigint(20) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `delFlag` char(1) DEFAULT NULL,
  `remarks` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for travelactivity
-- ----------------------------
DROP TABLE IF EXISTS `travelactivity`;
CREATE TABLE `travelactivity` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键,自主递增',
  `activitySerNum` varchar(100) DEFAULT NULL COMMENT '产品编号,如SG0001',
  `acitivityName` varchar(256) DEFAULT NULL COMMENT '旅游产品的名称',
  `activityTypeId` int(11) DEFAULT NULL COMMENT '产品所属的类型ID',
  `activityTypeName` varchar(50) DEFAULT NULL COMMENT '产品所属的类型名称',
  `travelTypeId` int(11) DEFAULT NULL COMMENT '旅游类型id',
  `travelTypeName` varchar(50) DEFAULT NULL COMMENT '旅游类型名称',
  `activityLevelId` int(11) DEFAULT NULL COMMENT '产品等级id',
  `activityLevelName` varchar(50) DEFAULT NULL COMMENT '产品等级名称',
  `payMode` smallint(11) DEFAULT NULL,
  `remainDays` int(11) DEFAULT NULL,
  `fromArea` int(11) DEFAULT NULL COMMENT '旅游出发城市',
  `targetArea` varchar(50) DEFAULT NULL COMMENT '旅游到达目的城市id，逗号分隔',
  `activityDuration` int(11) DEFAULT NULL COMMENT '旅游产品行程天数',
  `trafficMode` int(11) DEFAULT NULL COMMENT '产品的主要交通方式',
  `trafficName` varchar(10) DEFAULT NULL,
  `groupOpenDate` date DEFAULT NULL COMMENT '该产品的最早出团日期',
  `groupCloseDate` date DEFAULT NULL COMMENT '该产品的最晚出团日期',
  `settlementAdultPrice` int(11) DEFAULT '0' COMMENT '最小成人同行价',
  `suggestAdultPrice` int(11) DEFAULT '0' COMMENT '最小成人建议零售价',
  `createDate` datetime DEFAULT NULL COMMENT '产品的创建日期',
  `activityStatus` tinyint(4) DEFAULT NULL COMMENT '产品的上下架状态',
  `recentUpdateTime` datetime DEFAULT NULL COMMENT '产品信息的更新时间',
  `wholeSalerId` int(11) DEFAULT NULL COMMENT '产品所属批发商的ID',
  `createBy` bigint(20) DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `delFlag` char(1) DEFAULT NULL,
  `proCompany` int(11) DEFAULT NULL COMMENT '产品机构ID',
  `proCompanyName` varchar(50) DEFAULT NULL COMMENT '产品机构名称',
  `remarks` text,
  `overseasFlag` tinyint(4) DEFAULT '0' COMMENT '是否出境游：0,国内；1,国外',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=575 DEFAULT CHARSET=utf8 COMMENT='产品基本信息表';

-- ----------------------------
-- Table structure for traveler
-- ----------------------------
DROP TABLE IF EXISTS `traveler`;
CREATE TABLE `traveler` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderId` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `nameSpell` varchar(100) DEFAULT NULL,
  `hotelDemand` tinyint(4) DEFAULT NULL,
  `singleDiff` float DEFAULT NULL,
  `papersType` tinyint(4) DEFAULT NULL,
  `idCard` varchar(30) DEFAULT NULL,
  `nationality` int(11) DEFAULT NULL,
  `sex` tinyint(4) DEFAULT NULL,
  `birthDay` date DEFAULT NULL,
  `issuePlace` date DEFAULT NULL,
  `validityDate` date DEFAULT NULL,
  `telephone` varchar(50) DEFAULT NULL,
  `remark` text,
  `srcPrice` float DEFAULT NULL,
  `personType` int(11) DEFAULT NULL COMMENT '人员类型（儿童、成人）',
  `payPrice` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=399 DEFAULT CHARSET=utf8 COMMENT='游客信息';

-- ----------------------------
-- Table structure for un_synchronize_log
-- ----------------------------
DROP TABLE IF EXISTS `un_synchronize_log`;
CREATE TABLE `un_synchronize_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '类型, 1：添加 , 2：修改  , 3：删除',
  `id_trekizwholesaler` int(11) NOT NULL COMMENT '反向平台产品ID',
  `groupId_trekizwholesaler` int(11) DEFAULT NULL COMMENT '反向平台团期ID',
  `sql_str` varchar(600) DEFAULT NULL COMMENT '当前执行的sql语句',
  `state` int(11) DEFAULT NULL COMMENT '状态, 0:未读 , 1:已读',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for unPrice_synchronize_log
-- ----------------------------
DROP TABLE IF EXISTS `unPrice_synchronize_log`;
CREATE TABLE `unPrice_synchronize_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '类型, 1：添加 , 2：修改  , 3：删除',
  `id_trekizwholesaler` int(11) NOT NULL COMMENT '反向平台产品ID',
  `groupId_trekizwholesaler` int(11) DEFAULT NULL COMMENT '反向平台团期ID',
  `sql_str` varchar(600) DEFAULT NULL COMMENT '当前执行的sql语句',
  `state` int(11) DEFAULT NULL COMMENT '状态, 0:未读 , 1:已读',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for userdefinedict
-- ----------------------------
DROP TABLE IF EXISTS `userdefinedict`;
CREATE TABLE `userdefinedict` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `companyId` int(50) DEFAULT NULL,
  `dictId` int(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=570 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for visabasics
-- ----------------------------
DROP TABLE IF EXISTS `visabasics`;
CREATE TABLE `visabasics` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `visaType` int(11) DEFAULT NULL COMMENT '签证类型',
  `isNeedAudition` int(11) DEFAULT NULL COMMENT '是否需要面试',
  `isneedFlightOrder` int(11) DEFAULT NULL COMMENT '需机票订单',
  `visaCountry` int(11) DEFAULT NULL COMMENT '签证国家',
  `isNeedSpotAudition` int(11) DEFAULT NULL COMMENT '抽查面试',
  `isNeedHotelOrder` int(11) DEFAULT NULL COMMENT '酒店订单',
  `handleTime` varchar(50) DEFAULT NULL COMMENT '办理所需时间',
  `isNeedBespeak` int(11) DEFAULT NULL COMMENT '是否需要预约',
  `isNeedInsurance` int(11) DEFAULT NULL COMMENT '是否需要保险',
  `pinCheckTime` varchar(50) DEFAULT NULL COMMENT '销签所需时间',
  `isNeedPinCheck` int(11) DEFAULT NULL COMMENT '是否需要销签',
  `selfInvited` int(11) DEFAULT NULL COMMENT '自备邀请',
  `collarZoning` text COMMENT '领区划分',
  `stickshow` text COMMENT '贴示',
  `attention` text COMMENT '注意事项',
  `specialTips` text COMMENT '特别提示',
  `otherDescription` text COMMENT '其他说明',
  `remarks` text COMMENT '备注',
  `fileTableId` int(11) DEFAULT NULL COMMENT '附件id',
  `delFlag` tinyint(4) DEFAULT NULL COMMENT '删除标志位',
  `createBy` int(11) DEFAULT NULL COMMENT '创建者',
  `createDate` date DEFAULT NULL COMMENT '创建日期',
  `updateBy` int(11) DEFAULT NULL COMMENT '更新者',
  `updateDate` date DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='签证基础表';

-- ----------------------------
-- Table structure for visafile
-- ----------------------------
DROP TABLE IF EXISTS `visafile`;
CREATE TABLE `visafile` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `fileName` varchar(50) DEFAULT NULL COMMENT '文件名称',
  `fileTableId` int(11) DEFAULT NULL COMMENT '附件表Id',
  `visabasicsId` int(11) DEFAULT NULL COMMENT '签证基础表id',
  `delFlag` tinyint(4) DEFAULT NULL COMMENT '删除标志位',
  `createBy` int(11) DEFAULT NULL COMMENT '创建者',
  `createDate` date DEFAULT NULL COMMENT '创建日期',
  `updateBy` int(11) DEFAULT NULL COMMENT '更新者',
  `updateDate` date DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='签证附件信息表';

-- ----------------------------
-- Table structure for visapersonneltype
-- ----------------------------
DROP TABLE IF EXISTS `visapersonneltype`;
CREATE TABLE `visapersonneltype` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `personnelType` int(11) DEFAULT NULL COMMENT '人员类型',
  `visabasicsId` int(11) DEFAULT NULL COMMENT '基础表id',
  `passport` text COMMENT '护照',
  `idCard` text COMMENT '身份证',
  `photo` text COMMENT '照片',
  `visaApplicationForm` text COMMENT '签证申请表',
  `otherSupplementaryMaterials` text COMMENT '其他补充材料',
  `certificateDeposit` text COMMENT '存款证明',
  `service` text COMMENT '在职证明/学校准假信/退休证',
  `remarks` text COMMENT '备注',
  `delFlag` tinyint(4) DEFAULT NULL COMMENT '删除标志位',
  `createBy` int(11) DEFAULT NULL COMMENT '创建者',
  `createDate` date DEFAULT NULL COMMENT '创建日期',
  `updateBy` int(11) DEFAULT NULL COMMENT '更新者',
  `updateDate` date DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='签证人员类型表';

ALTER TABLE dept_menu RENAME TO dept_select_menu;