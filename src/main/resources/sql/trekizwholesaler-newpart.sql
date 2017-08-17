#2014-05-30
ALTER TABLE productorder ADD cancel_description VARCHAR(600) COMMENT '取消/退团原因描述';

#2014-06-10
ALTER TABLE travelactivity ADD is_sync_success tinyint(1) NULL DEFAULT 0 COMMENT '同步是否成功，0未同步，1-新增成功，2-修改成功，3-删除成功。-1-新增失败，-2-修改失败，-3-删除失败';

#2014-06-12
ALTER TABLE activityfile ADD orderSalerId  int(11) NULL DEFAULT NULL COMMENT '预订人id'; 

#2014-06-12
ALTER TABLE sys_country ADD country_code int(11) COMMENT '国家编码';
UPDATE sys_country SET country_code = 1 where countryName_cn = '南非';
UPDATE sys_country SET country_code = 11 where countryName_cn = '埃及';
UPDATE sys_country SET country_code = 21 where countryName_cn = '肯尼亚';
UPDATE sys_country SET country_code = 31 where countryName_cn = '阿联酋';
UPDATE sys_country SET country_code = 41 where countryName_cn = '尼泊尔';
UPDATE sys_country SET country_code = 51 where countryName_cn = '新加坡';
UPDATE sys_country SET country_code = 61 where countryName_cn = '日本';
UPDATE sys_country SET country_code = 71 where countryName_cn = '柬埔寨';
UPDATE sys_country SET country_code = 81 where countryName_cn = '泰国';
UPDATE sys_country SET country_code = 91 where countryName_cn = '越南';
UPDATE sys_country SET country_code = 101 where countryName_cn = '韩国';
UPDATE sys_country SET country_code = 111 where countryName_cn = '马来西亚';
UPDATE sys_country SET country_code = 141 where countryName_cn = '土耳其';
UPDATE sys_country SET country_code = 151 where countryName_cn = '奥地利';
UPDATE sys_country SET country_code = 161 where countryName_cn = '德国';
UPDATE sys_country SET country_code = 171 where countryName_cn = '意大利';
UPDATE sys_country SET country_code = 181 where countryName_cn = '挪威';
UPDATE sys_country SET country_code = 191 where countryName_cn = '法国';
UPDATE sys_country SET country_code = 201 where countryName_cn = '瑞士';
UPDATE sys_country SET country_code = 211 where countryName_cn = '英国';
UPDATE sys_country SET country_code = 221 where countryName_cn = '西班牙';
UPDATE sys_country SET country_code = 231 where countryName_cn = '新西兰';
UPDATE sys_country SET country_code = 241 where countryName_cn = '澳大利亚';
UPDATE sys_country SET country_code = 251 where countryName_cn = '加拿大';
UPDATE sys_country SET country_code = 261 where countryName_cn = '巴西';
UPDATE sys_country SET country_code = 271 where countryName_cn = '美国';
UPDATE sys_country SET country_code = 272 where countryName_cn = '希腊';

#2014-6-13
#ALTER TABLE activitygroup ADD trekizPrice  int(11) NULL DEFAULT NULL COMMENT 'trekiz成人价';
#ALTER TABLE activitygroup ADD trekizChildPrice  int(11) NULL DEFAULT NULL COMMENT 'trekiz儿童价';

#2014-06-30 trekizbeta5表需要导入签证
insert into visa_countries values(273,'欧洲','俄罗斯','Russian.doc');
insert into visa_countries values(274,'非洲','阿尔及利亚','Algeria.doc');
insert into visa_countries values(275,'亚洲','蒙古','Mongolia.doc');

#2014-06-16
ALTER TABLE orderinvoice ADD COLUMN createStatus smallint;

#2014-06-17
#update activitygroup set trekizPrice = settlementAdultPrice;
#update activitygroup set trekizChildPrice = settlementcChildPrice;

#2014-06-18
INSERT INTO agentinfo (agentName, supplyId, supplyName, is_synchronize,delFlag) SELECT '思锐创途',t.id,t.name,1,0 FROM sys_office t;
#2014-06-23
ALTER TABLE travelactivity ADD is_after_supplement TINYINT(1) NULL DEFAULT 0 COMMENT '是否是补单产品，0：否，1：是';

#2014-07-01
ALTER TABLE `sys_country` CHANGE `cc_fips` `cc_fips` varchar(2) COLLATE utf8_general_ci DEFAULT NULL;
ALTER TABLE `sys_country` CHANGE `cc_iso` `cc_iso` varchar(2) COLLATE utf8_general_ci DEFAULT NULL;
ALTER TABLE `sys_country` CHANGE `tld` `tld` varchar(3) COLLATE utf8_general_ci DEFAULT NULL;
ALTER TABLE `sys_country` CHANGE `countryName` `countryName` varchar(100) COLLATE utf8_general_ci DEFAULT NULL COMMENT '国家名英文';
ALTER TABLE `sys_country` CHANGE `countryName_cn` `countryName_cn` varchar(100) COLLATE utf8_general_ci DEFAULT NULL COMMENT '国家名中文';
ALTER TABLE `sys_country` CHANGE `continentName` `continentName` varchar(255) COLLATE utf8_general_ci DEFAULT NULL COMMENT '所属洲';
#2014-07-03
ALTER TABLE sys_office ADD front_vendor_id int(11) NULL COMMENT '正向批发商id';

#2014-07-07
ALTER TABLE travelactivity CHANGE `payMode` `payMode` varchar(20) COLLATE utf8_general_ci DEFAULT NULL;

ALTER TABLE productorder ADD payMode varchar(20) DEFAULT NULL COMMENT '付款方式';
ALTER TABLE productorder ADD remainDays int(11) DEFAULT NULL COMMENT '保留天数';
update productorder pro set pro.payMode = (select tra.payMode from travelactivity tra where pro.productId = tra.id and pro.productId is not null);
update productorder pro set pro.remainDays = (select tra.remainDays from travelactivity tra where pro.productId = tra.id and pro.productId is not null);

#2014-07-08
ALTER TABLE orderinvoice ADD invoiceHead varchar(50) NULL COMMENT '发票抬头';
#2014-07-08
update sys_menu set href = '/activity/manager/list/2' where name = '产品';
update sys_menu set href = '/activity/manager/list/1' where name = '草稿中产品';
update sys_menu set href = '/activity/manager/list/3' where name = '已下架产品';
update sys_menu set href = '/activity/manager/form' where name = '发布产品';

#2014-07-10
ALTER TABLE travelactivity ADD payMode_full int(10) DEFAULT '0' COMMENT '支付方式：全款支付 0表示没有使用，1表示使用';
ALTER TABLE travelactivity ADD payMode_deposit int(10) DEFAULT '0' COMMENT '付款方式：订金支付 0表示没有使用，1表示使用';
ALTER TABLE travelactivity ADD payMode_advance int(10) DEFAULT '0' COMMENT '付款方式：预占位 0表示没有使用，1表示使用';
ALTER TABLE travelactivity ADD remainDays_advance int(10) DEFAULT '0' COMMENT '预占位保留天数';
ALTER TABLE travelactivity ADD remainDays_deposit int(10) DEFAULT '0' COMMENT '订金占位保留天数';

update travelactivity set payMode_deposit = 1,payMode_advance = 0,payMode_full = 0,remainDays_advance = remainDays,remainDays_deposit = remainDays where payMode = '1';
update travelactivity set payMode_deposit = 0,payMode_advance = 1,payMode_full = 0,remainDays_advance = remainDays,remainDays_deposit = remainDays where payMode = '2';
update travelactivity set payMode_deposit = 0,payMode_advance = 0,payMode_full = 1,remainDays_advance = remainDays,remainDays_deposit = remainDays where payMode = '3';
update travelactivity set payMode_deposit = 1,payMode_advance = 1,payMode_full = 0,remainDays_advance = remainDays,remainDays_deposit = remainDays where payMode = '1,2';
update travelactivity set payMode_deposit = 1,payMode_advance = 0,payMode_full = 1,remainDays_advance = remainDays,remainDays_deposit = remainDays where payMode = '1,3';
update travelactivity set payMode_deposit = 0,payMode_advance = 1,payMode_full = 1,remainDays_advance = remainDays,remainDays_deposit = remainDays where payMode = '2,3';
update travelactivity set payMode_deposit = 1,payMode_advance = 1,payMode_full = 1,remainDays_advance = remainDays,remainDays_deposit = remainDays where payMode = '1,2,3';

ALTER TABLE productorder ADD activationDate datetime DEFAULT NULL;
update productorder set activationDate = orderTime;

#2014-07-23
ALTER TABLE sys_user ADD COLUMN two_psw varchar(100) DEFAULT NULL COMMENT '64位base加密';

update sys_user set two_psw = 'Z21kbnk=' where loginName = 'gmdny';
update sys_user set two_psw = 'Z21teg==' where loginName = 'gmmz';
update sys_user set two_psw = 'Z21veg==' where loginName = 'gmoz';
update sys_user set two_psw = 'Z21yaA==' where loginName = 'gmrh';
update sys_user set two_psw = 'Z210dw==' where loginName = 'gmtw';
update sys_user set two_psw = 'Z215eDE=' where loginName = 'gmyx1';
update sys_user set two_psw = 'Z215eDI=' where loginName = 'gmyx2';
update sys_user set two_psw = 'Z215eDM=' where loginName = 'gmyx3';
update sys_user set two_psw = 'Z215eDQ=' where loginName = 'gmyx4';
update sys_user set two_psw = 'Z216ZGY=' where loginName = 'gmzdf';

#2014-07-29
update sys_dict set label = '快速支付' where type = 'offlineorder_pay_type' and label ='其他';
update orderpay set payTypeName = '快速支付' where payTypeName = '其他';

#2014-07-25
ALTER TABLE activitygroup ADD COLUMN cost_status int(3) DEFAULT 0 NULL COMMENT '成本状态';
#2014-08-05
ALTER  TABLE  orderpay ADD  COLUMN payPriceBack int(11) COMMENT '支付金额back';
#2014-08-05
ALTER TABLE docinfo ADD COLUMN payOrderId int(11) COMMENT '被关联的订单ID'
UPDATE docinfo doc SET doc.payOrderId = (SELECT pay.id FROM orderpay pay WHERE pay.`payVoucher`=doc.`id`);
#2014-08-13
update travelactivity,sys_office s set proCompanyName = s.`name` where name like '%TEST_%' and proCompany = s.id;


#2014-08-18
ALTER TABLE  orderpay ADD COLUMN oldPayPrice varchar(200) NULL COMMENT '原支付金额';

#2014-08-20
ALTER TABLE `trekizwholesaler`.`travelactivity`
ADD COLUMN `payMode_data` INT(10) DEFAULT 0 NULL COMMENT '支付方式：资料占位 0表示没有使用，1表示使用' AFTER `remainDays_deposit`, 
ADD COLUMN `payMode_guarantee` INT(10) DEFAULT 0 NULL COMMENT '支付方式：担保占位 0表示没有使用，1表示使用' AFTER `payMode_data`, 
ADD COLUMN `payMode_express` INT(10) DEFAULT 0 NULL COMMENT '支付方式：快递订单号占位 0表示没有使用，1表示使用' AFTER `payMode_guarantee`, 
ADD COLUMN `remainDays_data` INT(10) DEFAULT 0 NULL COMMENT '资料占位保留天数' AFTER `payMode_express`, 
ADD COLUMN `remainDays_guarantee` INT(10) DEFAULT 0 NULL COMMENT '担保占位保留天数' AFTER `remainDays_data`, 
ADD COLUMN `remainDays_express` INT(10) DEFAULT 0 NULL COMMENT '快递订单号占位保留天数' AFTER `remainDays_guarantee`;

#2014-08-22
ALTER TABLE traveler CHANGE `papersType` `papersType` varchar(20) COLLATE utf8_general_ci DEFAULT NULL;
ALTER TABLE traveler CHANGE `idCard` `idCard` varchar(200) COLLATE utf8_general_ci DEFAULT NULL;

#2014-09-02
ALTER TABLE productorder ADD orderPersonNumSpecial tinyint(4) DEFAULT NULL COMMENT '特殊预定人数';
ALTER TABLE productorder ADD settlementSpecialPrice int(11) DEFAULT NULL COMMENT '下订单时特殊的产品结算价';
ALTER TABLE `activitygroup` ADD COLUMN `settlementSpecialPrice` INT(11) NULL COMMENT '同业价特殊人群'
AFTER `settlementcChildPrice`, ADD COLUMN `suggestSpecialPrice` INT(11) NULL COMMENT '建议特殊人群零售价' AFTER `suggestChildPrice`;

#2014-09-05
-- ----------------------------
-- Table structure for `preproductorder`
-- ----------------------------
DROP TABLE IF EXISTS `preproductorder`;
CREATE TABLE `preproductorder` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `productId` int(11) DEFAULT NULL COMMENT '产品id',
  `productGroupId` int(11) DEFAULT NULL COMMENT '产品出团信息表id',
  `orderNum` varchar(50) DEFAULT NULL COMMENT '订单单号',
  `orderType` tinyint(4) DEFAULT NULL COMMENT '订单状态',
  `orderCompany` int(11) DEFAULT NULL COMMENT '预订单位',
  `orderCompanyName` varchar(100) DEFAULT NULL COMMENT '预订单位名称',
  `orderSalerId` int(11) DEFAULT NULL COMMENT '跟进销售员id',
  `orderPersonName` varchar(50) DEFAULT NULL COMMENT '预订人名称',
  `orderPersonPhoneNum` varchar(50) DEFAULT NULL COMMENT '预订人联系电话',
  `orderTime` datetime DEFAULT NULL COMMENT '预订日期',
  `orderPersonNum` tinyint(4) DEFAULT NULL COMMENT '预定人数',
  `orderPersonNumAdult` tinyint(4) DEFAULT NULL COMMENT '成人预定人数',
  `orderPersonNumChild` tinyint(4) DEFAULT NULL COMMENT '儿童预定人数',
  `frontMoney` int(11) DEFAULT NULL COMMENT '定金金额',
  `payStatus` tinyint(4) DEFAULT NULL COMMENT '支付状态',
  `total_money` int(11) DEFAULT NULL COMMENT '订单总额（应该支付）',
  `alreadyPaid` int(11) DEFAULT NULL COMMENT '已支付金额',
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
  `changePriceFlag` char(1) DEFAULT NULL COMMENT '改价标记',
  `cancel_description` varchar(600) DEFAULT NULL COMMENT '取消原因',
  `payMode` varchar(20) DEFAULT NULL COMMENT '付款方式',
  `remainDays` int(11) DEFAULT NULL COMMENT '保留天数',
  `activationDate` datetime DEFAULT NULL,
  `orderPersonNumSpecial` tinyint(4) DEFAULT NULL COMMENT '特殊预定人数',
  `settlementSpecialPrice` int(11) DEFAULT NULL COMMENT '下订单时特殊的产品结算价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='预报名订单信息表';


#2014-09-01
INSERT INTO sys_dict (
  label,
  VALUE,
  TYPE,
  description,
  sort,
  createby,
  createdate,
  updateby,
  updatedate,
  remarks,
  delFlag
)
  SELECT
  t.label,
  t.value,
  'out_area',
  '离境城市',
  t.sort,
  t.createby,
  t.createdate,
  t.updateby,
  t.updatedate,
  t.remarks,
  t.delFlag
FROM
  sys_dict t
WHERE t.type = 'from_area';

ALTER  TABLE  travelactivity ADD COLUMN outArea int(11) COMMENT '出境城市';

DROP TABLE IF EXISTS `intermodal_strategy`;
CREATE TABLE `intermodal_strategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_part` varchar(100) DEFAULT NULL COMMENT '城市分区',
  `price` bigint(11) DEFAULT NULL COMMENT '联运价格',
  `type` int(4) DEFAULT NULL COMMENT '联运类型，0：无联运，1：全国，2：分区',
  `activity_id` int(11) DEFAULT NULL COMMENT '产品id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


ALTER  TABLE  productorder ADD COLUMN intermodal_type int(4) DEFAULT 0 COMMENT '联运类型id';
ALTER  TABLE  preproductorder ADD COLUMN intermodal_type int(4) DEFAULT 0 COMMENT '联运类型id';
#2014-09-09
update sys_menu set href = "/activity/managerforOrder/list/1" where href = "/activity/managerforOrder";

DROP TABLE IF EXISTS `product_line`;
CREATE TABLE `product_line` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '部门名称',
  `code` varchar(50) NOT NULL COMMENT '部门编号',
  `office_id` int(10) NOT NULL COMMENT '所属公司ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

#2014-09-12
ALTER TABLE travelactivity ADD COLUMN flightInfo varchar(10) DEFAULT NULL COMMENT '产品机场信息';

#2014-09-11
ALTER TABLE travelactivity ADD COLUMN group_lead varchar(20) DEFAULT NULL COMMENT '领队人名称';

#2014-09-19 在附件表中增加机票产品id 关联字段做外键 

DROP TABLE IF EXISTS `activity_airticket`;
CREATE TABLE `activity_airticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `airType` char(1) DEFAULT NULL COMMENT '机票类型(单程3，往返2 ，多段1）',
  `ticket_area_type` char(1) DEFAULT NULL COMMENT '机票区域类型(国内3，国外+国外2 ，国外1）',
  `startingDate` date DEFAULT NULL COMMENT '出发日期',
  `returnDate` date DEFAULT NULL COMMENT '返程日期',
  `departureCity` varchar(50) DEFAULT NULL COMMENT '出发城市',
  `arrivedCity` varchar(50) DEFAULT NULL COMMENT '到达城市',
  `currency` int(3) DEFAULT NULL,
  `costPrice` decimal(11,2) DEFAULT NULL COMMENT '成本价',
  `payPrice` decimal(11,2) DEFAULT NULL COMMENT ' 应收价格',
  `airlines` varchar(50) DEFAULT NULL COMMENT '航空公司',
  `whetherTrip` char(1) DEFAULT NULL COMMENT '是否往返',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `createBy` varchar(20) DEFAULT NULL COMMENT '创建人',
  `updateBy` varchar(20) DEFAULT NULL COMMENT '修改人',
  `delflag` char(1) DEFAULT NULL COMMENT '`删除标识',
  `product_status` char(1) DEFAULT NULL COMMENT '产品发布状态',
  `wholeSalerId` int(11) DEFAULT NULL COMMENT '产品所属批发商的ID',
  `proCompany` int(11) DEFAULT NULL COMMENT '产品机构ID',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `depositTime` date DEFAULT NULL COMMENT '订金时限',
  `cancelTimeLimit` date DEFAULT NULL COMMENT '取消时限',
  `deptId` int(11) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `activity_flight_info`;
CREATE TABLE `activity_flight_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `airticketId` int(11) DEFAULT NULL COMMENT '关联机票Id',
  `leaveAirport` varchar(50) DEFAULT NULL COMMENT '出发城市机场',
  `destinationAirpost` varchar(50) DEFAULT NULL COMMENT '到达城市机场',
  `airlines` varchar(50) DEFAULT NULL COMMENT '航空公司',
  `reservationsNum` int(11) DEFAULT NULL COMMENT '预定人数',
  `spaceGrade` char(1) DEFAULT NULL COMMENT '舱位等级',
  `airspace` varchar(20) DEFAULT NULL COMMENT '舱位',
  `startDate` date DEFAULT NULL COMMENT '出发日期',
  `arrivalDate` date DEFAULT NULL COMMENT '到达日期',
  `timeInterval` char(1) DEFAULT NULL COMMENT '出发时段',
  `startTime` datetime DEFAULT NULL COMMENT '开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`),
  KEY `airticketId` (`airticketId`),
  CONSTRAINT `FK_airticket` FOREIGN KEY (`airticketId`) REFERENCES `activity_airticket` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE visafile  ADD visaProductssId  int(11) NULL DEFAULT NULL COMMENT '签证产品id 关联外键'; 
ALTER TABLE activityfile  ADD srcAirticketId  int(11) NULL DEFAULT NULL COMMENT '机票产品id 关联外键'; 

ALTER TABLE travelactivity ADD COLUMN currency_type varchar(50) DEFAULT NULL COMMENT '金额币种ID(按照表中出现的顺序，用逗号隔开)';
ALTER TABLE travelactivity ADD COLUMN activity_kind int(2) DEFAULT NULL COMMENT '产品种类ID';
ALTER TABLE travelactivity ADD COLUMN activity_kind_name varchar(20) DEFAULT NULL COMMENT '产品种类名称';
ALTER TABLE activitygroup ADD COLUMN currency_type varchar(50) DEFAULT NULL COMMENT '金额币种ID(按照表中出现的顺序，用逗号隔开)';
ALTER TABLE activitygroup ADD COLUMN special_remark varchar(50) DEFAULT NULL COMMENT '特殊人群备注';
ALTER TABLE activitygroup ADD COLUMN singleDiff_unit varchar(50) DEFAULT NULL COMMENT '单房差单位';

#2014-09-24
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `office_id` bigint(20) NOT NULL COMMENT '所属公司ID',
  `name` varchar(200) NOT NULL COMMENT '部门名称（中文）',
  `name_en` varchar(200) DEFAULT NULL COMMENT '部门名称（英文）',
  `code` varchar(50) NOT NULL COMMENT '部门编号',
  `parent_id` bigint(20) NOT NULL COMMENT '父级部门ID',
  `parent_ids` varchar(200) NOT NULL COMMENT '所有父类ID',
  `announcement` char(1) NOT NULL DEFAULT '0' COMMENT '0：不启用公告；1：启用公告（默认为0）',
  `lowestLevel` char(1) NOT NULL DEFAULT '0' COMMENT '0：不是最低级别；1：最低级别部门（默认为0）',
  `sort` int(11) DEFAULT NULL COMMENT '排序值',
  `description` varchar(200) DEFAULT NULL COMMENT '部门描述',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0：正常；1：删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='部门表';

DROP TABLE IF EXISTS `dept_menu`;
CREATE TABLE `dept_menu` (
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `menu_id` int(11) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`dept_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE sys_role ADD COLUMN `deptId` int(11) DEFAULT NULL COMMENT '部门ID';

DROP TABLE IF EXISTS `product_line`;


#2014-09-25 增加数据字典 
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('单程', '3', 'air_Type', '机票类型', 3, 1, '2014-9-23 14:53:10', 1, '2014-9-23 15:43:21', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('往返', '2', 'air_Type', '机票类型', 2, 1, '2014-9-23 14:53:57', 1, '2014-9-23 15:40:40', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('多程', '1', 'air_Type', '机票类型', 1, 1, '2014-9-23 14:54:36', 1, '2014-9-23 15:43:07', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('头等舱', '1', 'spaceGrade_Type', '舱位等级', 1, 1, '2014-9-25 11:31:16', 1, '2014-9-25 11:31:16', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('公务舱', '2', 'spaceGrade_Type', '舱位等级', 2, 1, '2014-9-25 11:32:22', 1, '2014-9-25 11:32:22', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('经济舱', '3', 'spaceGrade_Type', '舱位等级', 3, 1, '2014-9-25 11:33:21', 1, '2014-9-25 11:33:21', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('G舱', '1', 'airspace_Type', '舱位', 1, 1, '2014-9-25 11:35:16', 1, '2014-9-25 11:41:37', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('A舱', '7', 'airspace_Type', '舱位', 7, 1, '2014-9-25 11:41:46', 1, '2014-9-25 11:41:46', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('B舱', '6', 'airspace_Type', '舱位', 6, 1, '2014-9-25 11:42:22', 1, '2014-9-25 11:42:22', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('C舱', '5', 'airspace_Type', '舱位', 5, 1, '2014-9-25 11:42:58', 1, '2014-9-25 11:42:58', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('D舱', '4', 'airspace_Type', '舱位', 4, 1, '2014-9-25 11:43:35', 1, '2014-9-25 11:43:35', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('E舱', '3', 'airspace_Type', '舱位', 3, 1, '2014-9-25 11:44:23', 1, '2014-9-25 11:44:23', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('F舱', '2', 'airspace_Type', '舱位', 2, 1, '2014-9-25 11:44:49', 1, '2014-9-25 11:44:49', NULL, '0');

ALTER TABLE travelactivity ADD COLUMN airticket_id int(11) DEFAULT NULL COMMENT '关联机票产品ID';

#2014-09-28
ALTER TABLE department ADD COLUMN `level` int(2) DEFAULT 0 COMMENT '菜单的级别：0为最大';
ALTER TABLE currency ADD COLUMN currency_style varchar(20) DEFAULT NULL COMMENT '币种样式标识';

#2014-10-08
ALTER TABLE productorder ADD COLUMN orderStatus tinyint(4) DEFAULT NULL COMMENT '订单种类，1：单团，2：散拼，3：游行，4大客户，5：自由行';

#2014-10-21 添加游客附件表
CREATE TABLE `travelerfile` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键,自主递增' ,
`srcTravelerId`  int(11) NULL DEFAULT NULL COMMENT '游客信息表ID外键' ,
`srcDocId`  int(11) NULL DEFAULT NULL COMMENT '文件上传附件表ID外键' ,
`fileName`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传的文件名称' ,
`fileType`  int(11) NULL DEFAULT NULL COMMENT '上传的文件资料类型' ,
`createBy`  bigint(20) NULL DEFAULT NULL ,
`createDate`  date NULL DEFAULT NULL ,
`updateBy`  bigint(20) NULL DEFAULT NULL ,
`updateDate`  date NULL DEFAULT NULL ,
`delFlag`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`remarks`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1
COMMENT='游客附件表';
ALTER TABLE `travelerfile` AUTO_INCREMENT=1;

#2014-10-28
update sys_menu set href = '/activity/manager/list/2',name = '已上架产品' where name = '发布产品';
update sys_menu set href = '/activity/manager/list/3' where name = '已下架产品';
update sys_menu set href = '/activity/manager/list/1' where name = '草稿中产品';
update sys_menu set href = '/stock/manager',name = '库存查询' where name = '库存';

#2014-11-03 订单预定联系人表
CREATE TABLE `ordercontacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `orderId` int(11) DEFAULT NULL COMMENT '订单id',
  `contactsName` varchar(50) DEFAULT NULL COMMENT '联系人名称',
  `contactsTel` varchar(20) DEFAULT NULL COMMENT '电话',
  `contactsEmail` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `contactsFax` varchar(50) DEFAULT NULL COMMENT '传真',
  `remark` text COMMENT '其他',
  `contactsTixedTel` varchar(20) DEFAULT NULL COMMENT '固定电话',
  `contactsAddress` varchar(50) DEFAULT NULL COMMENT '地址',
  `contactsZipCode` varchar(20) DEFAULT NULL COMMENT '邮编',
  `contactsQQ` varchar(20) DEFAULT NULL COMMENT 'QQ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='联系人信息';
#2014-11-03 游客自备签信息表
CREATE TABLE `travelervisa` (
`id`  bigint(11) NOT NULL AUTO_INCREMENT ,
`applyCountryId`  int(11) NULL DEFAULT NULL COMMENT '申请国家ID' ,
`manorId`  int(11) NULL DEFAULT NULL COMMENT '领区' ,
`visaTypeId`  int(11) NULL DEFAULT NULL COMMENT '签证类型' ,
`groupOpenDate`  date NULL DEFAULT NULL COMMENT '预计出团时间' ,
`contractDate`  date NULL DEFAULT NULL COMMENT '预签约时间' ,
`zbqType`  int(11) NULL DEFAULT NULL COMMENT '是否自备签（办签：0 自备签：1）' ,
`visaDate`  date NULL DEFAULT NULL COMMENT '自备签有效期' ,
`createBy`  bigint(20) NULL DEFAULT NULL ,
`createDate`  date NULL DEFAULT NULL ,
`updateBy`  bigint(20) NULL DEFAULT NULL ,
`updateDate`  date NULL DEFAULT NULL ,
`delFlag`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`remarks`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注' ,
`travelerId`  bigint(20) NULL DEFAULT NULL COMMENT '游客信息表ID' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='游客自备签信息表'
AUTO_INCREMENT=1;

#2014-11-3 单团 散拼 大客户 游学 自由行链接
update sys_menu t set t.href = '/activity/managerforOrder/list/1/5' where t.id = 213;
update sys_menu t set t.href = '/activity/managerforOrder/list/1/4' where t.id = 212;
update sys_menu t set t.href = '/activity/managerforOrder/list/1/3' where t.id = 211;
update sys_menu t set t.href = '/activity/managerforOrder/list/1/2' where t.id = 210;
update sys_menu t set t.href = '/activity/managerforOrder/list/1/1' where t.id = 209;

#2014-11-3 游客信息扩展字段
ALTER TABLE traveler ADD COLUMN singleDiffNight int(11) COMMENT '单房差几晚';
ALTER TABLE traveler ADD COLUMN passportCode varchar(20) COMMENT '护照号码';
ALTER TABLE traveler ADD COLUMN passportValidity date COMMENT '护照有效期';
ALTER TABLE traveler ADD COLUMN passportType int(5) COMMENT '护照类型';
ALTER TABLE traveler ADD COLUMN intermodalType  int(11) COMMENT '是否需要联运 0：不需要，1：需要';
ALTER TABLE traveler ADD COLUMN intermodalId  int(11) COMMENT '产品联运信息表主键';
ALTER TABLE traveler ADD COLUMN srcPriceCurrency  decimal(10) COMMENT '单价币种';
ALTER TABLE traveler ADD COLUMN singleDiffCurrency  decimal(10) COMMENT '单房差币种';
#2014-11-4 游客信息费用字段修改为小数
ALTER TABLE traveler MODIFY singleDiff decimal(10);
ALTER TABLE traveler MODIFY srcPrice decimal(10);
ALTER TABLE traveler MODIFY payPrice decimal(10);

INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('因公护照', '1', 'passport_type', '护照类型', 1, 1, '2014-9-23 14:53:10', 1, '2014-9-23 15:43:21', NULL, '0');
INSERT INTO `sys_dict` (`label`, `value`, `type`, `description`, `sort`, `createBy`, `createDate`, `updateBy`, `updateDate`, `remarks`, `delFlag`) VALUES ('因私护照', '2', 'passport_type', '护照类型', 2, 1, '2014-9-23 14:53:57', 1, '2014-9-23 15:40:40', NULL, '0');

#2014-11-3 机票预订链接
update sys_menu t
set t.href = '/order/airticket/activityList'
where t.id = 215;

#2014-11-5
ALTER TABLE travelactivity ADD COLUMN airticket_id int(11) DEFAULT NULL COMMENT '关联机票产品ID';
ALTER TABLE travelactivity ADD COLUMN original varchar(200) DEFAULT NULL COMMENT '需提供原件项目';
ALTER TABLE travelactivity ADD COLUMN original_copy varchar(200) DEFAULT NULL COMMENT '需提供复印件项目';
ALTER TABLE intermodal_strategy ADD COLUMN price_currency int(11) DEFAULT NULL COMMENT '价格币种';

ALTER TABLE travelactivity ADD COLUMN groupOpenCode varchar(500) DEFAULT NULL COMMENT '该产品的最早出团团编码';
ALTER TABLE travelactivity ADD COLUMN groupCloseCode varchar(500) DEFAULT NULL COMMENT '该产品的最晚出团团编码 ';

#2014-11-5
ALTER TABLE preproductorder ADD COLUMN orderStatus tinyint(4) DEFAULT NULL COMMENT '订单种类，1：单团，2：散拼，3：游行，4大客户，5：自由行';
update preproductorder set orderStatus = 2;



#2014-11-04 切位
ALTER TABLE activity_airticket ADD COLUMN `nopayReservePosition` mediumint(9) NOT NULL DEFAULT 0 COMMENT '各渠道总的切位人数';
ALTER TABLE activity_airticket ADD COLUMN `payReservePosition` mediumint(9) NOT NULL DEFAULT 0 COMMENT '各渠道总的已支付切位人数';
ALTER TABLE activity_airticket ADD COLUMN `soldNopayPosition` mediumint(9) NOT NULL DEFAULT 0 COMMENT '已占位人数';
ALTER TABLE activity_airticket ADD COLUMN `soldPayPosition` mediumint(9) NOT NULL DEFAULT 0 COMMENT '已支付占位人数';
ALTER TABLE airticket_order ADD COLUMN `place_holder_type` MEDIUMINT(9) NOT NULL DEFAULT 0 COMMENT '切位订单还是普通订单，0-普通订单，1-切位订单';

#2014-11-05 切位
CREATE TABLE `airticketreservefile` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `airticketActivityId` int(11) NOT NULL COMMENT '机票产品表ID外键',
  `agentId` int(11) NOT NULL COMMENT '渠道商基本信息表id',
  `srcDocId` int(11) NOT NULL COMMENT '附件表id',
  `fileName` varchar(50) NOT NULL COMMENT '文件名称',
  `createDate` datetime DEFAULT NULL COMMENT '创建日期',
  `createBy` int(11) DEFAULT NULL COMMENT '创建人',
  `updateDate` datetime DEFAULT NULL COMMENT '更新日期',
  `updateBy` int(11) DEFAULT NULL COMMENT '更新人',
  `delFlag` char(1) DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8 COMMENT='机票切位定金凭证文件表';

#2014-11-06 切位
CREATE TABLE `airticketactivityreserve` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `activityId` int(11) NOT NULL COMMENT '机票产品信息表ID外键',
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='机票产品切位信息';


#2014-11-10 机票订单链接
INSERT INTO sys_menu (parentId,parentIds,NAME,href,sort,isShow,permission,createBy,createDate,updateBy,updateDate,delFlag)VALUES(320,'0,1,320','机票订单','/order/manage/airticketOrderList',60,1,'airticketOrder:list:allorder',1,SYSDATE(),1,SYSDATE(),'0');


#2014-11-11
alter table activity_flight_info add COLUMN `flightNumber` varchar(20) DEFAULT NULL COMMENT '航班号';

#2014-11-12 是否录入成本
ALTER TABLE activity_airticket ADD COLUMN `isRecord` smallint(9) NOT NULL DEFAULT 0 COMMENT '是否录入成本，1待录入，2未通过，3已通过';
#2014-11-12 产品上架状态
ALTER TABLE activity_airticket ADD COLUMN `productStatus` int(1) NOT NULL DEFAULT 0 COMMENT '产品状态 1,草稿；2，上架；3下架';

#2014-11-18 订单定金币种
ALTER TABLE productorder ADD COLUMN frontMoneyCurrency tinyint(4) DEFAULT NULL COMMENT '定金币种';
ALTER TABLE preproductorder ADD COLUMN frontMoneyCurrency tinyint(4) DEFAULT NULL COMMENT '定金币种';

ALTER TABLE traveler ADD COLUMN payPriceSerialNum  varchar(255) COMMENT '游客结算价流水号';

#2014-11-18 支付方式
ALTER TABLE activity_airticket ADD COLUMN `payMode_full` int(1) NOT NULL DEFAULT 0 COMMENT '支付方式：全款支付 0表示没有使用，1表示使用';
ALTER TABLE activity_airticket ADD COLUMN `payMode_deposit` int(1) NOT NULL DEFAULT 0 COMMENT '付款方式：订金支付 0表示没有使用，1表示使用';
ALTER TABLE activity_airticket ADD COLUMN `payMode_advance` int(1) NOT NULL DEFAULT 0 COMMENT '付款方式：预占位 0表示没有使用，1表示使用';
ALTER TABLE activity_airticket ADD COLUMN `remainDays_deposit` int(10) NOT NULL DEFAULT 0 COMMENT '订金占位保留天数';
ALTER TABLE activity_airticket ADD COLUMN `remainDays_advance` int(10) NOT NULL DEFAULT 0 COMMENT '预占位保留天数';


ALTER TABLE traveler ADD COLUMN delFlag tinyint(4) COMMENT '0: 正常状态,1:删除状态(退团、转团时修改标识)';
ALTER TABLE traveler ADD COLUMN order_type tinyint(4) COMMENT '订单类型';
ALTER TABLE `visa_order`
ADD COLUMN `lockStatus`  tinyint(4) NULL DEFAULT 0 COMMENT '订单锁定状态：0:正常  1：锁定(当前订单所挂接的主订单锁定时修改状态)   主订单锁定，子订单不能解锁。' ;
ALTER TABLE `airticket_order`
ADD COLUMN `lockStatus`  tinyint NULL DEFAULT 0 COMMENT '订单锁定状态：0:正常  1：锁定(当前订单所挂接的主订单锁定时修改状态)   主订单锁定，子订单不能解锁。' ;
ALTER TABLE `productorder`
ADD COLUMN `lockStatus`  tinyint NULL DEFAULT 0 COMMENT '订单锁定状态：0:正常  1：锁定(订单锁定状态不允许操作订单)';



#2014 11 10 询价相关
ALTER TABLE `trekizwholesaler`.`estimate_price_base_info` CHANGE `budget` `budget` DECIMAL NULL COMMENT '预算   询价客户类型为0（其他）时，使用此字段'; 
ALTER TABLE `trekizwholesaler`.`estimate_price_base_info` ADD COLUMN `budget_pay_type_id` INT(11) NULL COMMENT '预算币种ID' AFTER `budget_type`; 
ALTER TABLE `trekizwholesaler`.`estimate_price_admit_requirements` CHANGE `travel_country_id` `travel_country_id` VARCHAR(32) NULL COMMENT '线路国家id,多个国家ID用“，”分开'; 
ALTER TABLE `trekizwholesaler`.`estimate_price_admit_requirements` CHANGE `travel_country` `travel_country` VARCHAR(32) CHARSET utf8 COLLATE utf8_general_ci NULL COMMENT '线路国家，多个国家名称用“，”分开';

ALTER TABLE `trekizwholesaler`.`estimate_price_record` CHANGE `operator_price` `operator_price` DECIMAL(10,3) NULL COMMENT '计调报价：被采纳的接待计调报价+被采纳的票务报价',
ADD COLUMN `operator_aop_price` DECIMAL(10,3) NULL COMMENT '地接计调报价' AFTER `operator_price`, 
ADD COLUMN `operator_top_price` DECIMAL(10,3) NULL COMMENT '机票计调报价' AFTER `operator_aop_price`, CHANGE `out_price` `out_price` DECIMAL(10,3) NULL COMMENT '外报价'; 

ALTER TABLE `trekizwholesalertts`.`estimate_price_file` CHANGE `ptype` `ptype` INT(11) NULL COMMENT '父级类型:\r\n            1 接待询价行程文档 ；2  接待计调回复行程文档  父级id（pid）根
据类型不同，关联的表不同，type为1时，pid代表接待询价内容id；当type为2时，pid代表询价回复id；type=3时，代表机票询价文件，type=4时，代表机票询价回复文件',
ADD COLUMN `doc_info_id` INT(11) NULL COMMENT '新增字段，表示和附件表docInfo的对应关系' AFTER `ptype`;

# 2014 11 12 修改机票计调回复表增加三个单价字段，并且修正两个报价字段类型。
ALTER TABLE `trekizwholesalertts`.`estimate_pricer_reply` 
ADD COLUMN `adult_price` DECIMAL(10,2) NULL COMMENT '成人单价' AFTER `special_person_sum`, 
ADD COLUMN `child_price` DECIMAL(10,2) NULL COMMENT '儿童单价' AFTER `adult_price`, 
ADD COLUMN `special_person_price` DECIMAL(10,2) NULL COMMENT '特殊人群单价' AFTER `child_price`, 
CHANGE `saler_price` `saler_price` DECIMAL(10,2) NULL COMMENT '销售报价:单位：元', 
CHANGE `operator_total_price` `operator_total_price` DECIMAL(10,2) NULL COMMENT '计调回复总报价:单位：元'; 

#  2014 11 19 修改销售询价记录表，增加三个字段，供销售页面使用
ALTER TABLE `trekizwholesalertts`.`estimate_price_record` 
ADD COLUMN `last_toperator_start_out_time` DATETIME NULL COMMENT '机票询价出发时间（供销售页面使用）' AFTER `remark`, 
ADD COLUMN `start_city` VARCHAR(100) NULL COMMENT '机票询价出发城市（供销售页面使用）' AFTER `last_toperator_start_out_time`, 
ADD COLUMN `end_city` VARCHAR(100) NULL COMMENT '机票询价目的地城市（供销售页面使用，多段航线，取最后一段航线的目的城市，如果目的城市和出发城市相同，则取最后一段航程的出发城市）' AFTER `start_city`;

#2014-11-19 渠道表里添加固定电话和邮编字段
ALTER TABLE agentinfo ADD COLUMN `agentFixedPhone` varchar(20) DEFAULT NULL COMMENT '渠道固定电话';
ALTER TABLE agentinfo ADD COLUMN `agentPostcode` varchar(20) DEFAULT NULL COMMENT '渠道邮编'; 

#2014-11-21
CREATE TABLE `visa_product_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `visa_product_id` int(11) DEFAULT NULL COMMENT '签证产品id',
  `docInfo_id` int(11) DEFAULT NULL COMMENT '文件表id',
  `create_by` int(11) DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_date` date DEFAULT NULL,
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='签证产品文件中间表';

ALTER TABLE travelactivity ADD COLUMN estimate_price_id int(11) DEFAULT NULL COMMENT '单团产品询价记录';
ALTER TABLE productorder ADD COLUMN specialDemand text DEFAULT NULL COMMENT '特殊需求'; 

#2014-11-25 修改机票订单菜单
update sys_menu set href = '' where id =406;
INSERT INTO sys_menu (parentId,parentIds,NAME,href,sort,isShow,permission,createBy,createDate,updateBy,updateDate,delFlag)VALUES(406,'0,1,320,406','销售机票订单','/order/manage/airticketOrderListForSale',10,1,'airticketOrderForSale:list:allorder',1,SYSDATE(),1,SYSDATE(),'0');
INSERT INTO sys_menu (parentId,parentIds,NAME,href,sort,isShow,permission,createBy,createDate,updateBy,updateDate,delFlag)VALUES(406,'0,1,320,406','计调机票订单','/order/manage/airticketOrderListForOp',20,1,'airticketOrderForOp:list:allorder',1,SYSDATE(),1,SYSDATE(),'0');

#2014-11-27
CREATE TABLE `money_amount` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `serialNum` varchar(48) DEFAULT NULL COMMENT '流水号UUID',
  `currencyId` tinyint(4) DEFAULT NULL COMMENT '币种ID',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `uid` bigint(20) DEFAULT NULL COMMENT '订单ID或游客ID',
  `moneyType` tinyint(4) DEFAULT NULL COMMENT '款项类型',
  `orderType` tinyint(4) DEFAULT NULL COMMENT '订单产品类型',
  `businessType` tinyint(4) DEFAULT NULL COMMENT '业务类型(1表示订单，2表示游客)',
  `createdBy` int(11) DEFAULT NULL COMMENT '记录人ID',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8 COMMENT='订单流水表';


INSERT INTO `sys_dict` VALUES ('1237', '单团', '1', 'order_type', '订单类型', '1', null, null, null, null, '', '0');
INSERT INTO `sys_dict` VALUES ('1238', '散拼', '2', 'order_type', '订单类型', '2', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1239', '游学', '3', 'order_type', '订单类型', '3', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1240', '大客户', '4', 'order_type', '订单类型', '4', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1241', '自由行', '5', 'order_type', '订单类型', '5', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1242', '签证', '6', 'order_type', '订单类型', '6', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1243', '机票', '7', 'order_type', '订单类型', '7', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1244', '退款', '11', 'money_type', '款项类型', '11', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1245', '借款', '12', 'money_type', '款项类型', '12', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1246', '订金', '3', 'money_type', '款项类型', '3', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1247', '达帐', '4', 'money_type', '款项类型', '4', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1248', '到账', '5', 'money_type', '款项类型', '5', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1249', '返佣', '6', 'money_type', '款项类型', '6', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1250', '押金', '7', 'money_type', '款项类型', '7', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1251', '改价', '8', 'money_type', '款项类型', '8', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1252', '转款', '9', 'money_type', '款项类型', '9', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1253', '担保', '10', 'money_type', '款项类型', '10', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1254', '全款', '1', 'money_type', '款项类型', '1', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1255', '尾款', '2', 'money_type', '款项类型', '2', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1256', '按团', '2', 'invoice_mode', '开票方式', '1', null, null, null, null, null, '0');
INSERT INTO `sys_dict` VALUES ('1257', '合开发票', '3', 'invoice_mode', '开票方式', '1', null, null, null, null, null, '0');

ALTER TABLE productorder ADD COLUMN `confirmationFileId` int(11) NULL COMMENT '确认单文件ID';

#2014-12-11
ALTER TABLE productorder DROP COLUMN fronMoneyCurrency;
ALTER TABLE productorder CHANGE `fronMoney` `front_money` varchar(255) COLLATE utf8_general_ci DEFAULT NULL COMMENT '订金金额UUID';
ALTER TABLE productorder DROP COLUMN orderTotalSerialNum;
ALTER TABLE productorder DROP COLUMN orderSrcTotal;

ALTER TABLE productorder CHANGE `orderyTotal` `total_money` varchar(255) COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单总价UUID';
ALTER TABLE productorder CHANGE `alreadyPaid` `payed_money` varchar(255) COLLATE utf8_general_ci DEFAULT NULL COMMENT '已付金额UUID';
ALTER TABLE productorder CHANGE `totalAsAcount` `accounted_money` varchar(255) COLLATE utf8_general_ci DEFAULT NULL COMMENT '达账金额UUID';


ALTER TABLE preproductorder DROP COLUMN fronMoneyCurrency;
ALTER TABLE preproductorder CHANGE `fronMoney` `front_money` varchar(255) COLLATE utf8_general_ci DEFAULT NULL COMMENT '订金金额UUID';
ALTER TABLE preproductorder DROP COLUMN orderTotalSerialNum;
ALTER TABLE preproductorder DROP COLUMN orderSrcTotal;

ALTER TABLE preproductorder CHANGE `orderyTotal` `total_money` varchar(255) COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单总价UUID';
ALTER TABLE preproductorder CHANGE `alreadyPaid` `payed_money` varchar(255) COLLATE utf8_general_ci DEFAULT NULL COMMENT '已付金额UUID';
ALTER TABLE preproductorder CHANGE `totalAsAcount` `accounted_money` varchar(255) COLLATE utf8_general_ci DEFAULT NULL COMMENT '达账金额UUID';
 
 
 #20141217
 ALTER TABLE estimate_pricer_reply  MODIFY COLUMN status int(11) COMMENT '状态:0 被删除；1 待回复；2  已经回复；3 被销售采纳';
 alter table estimate_pricer_reply modify column `type` int(11) DEFAULT NULL COMMENT '消息类型:1 单团询价，3游学询价 4,大客户询价 5 自由行询价 ，7  机票内容';
alter table estimate_price_record   modify `type` int(11) DEFAULT NULL COMMENT '消息类型:1 单团询价，3游学询价 4,大客户询价 5 自由行询价 ，7  机票内容';
alter table estimate_price_project   modify `type` int(11) DEFAULT NULL COMMENT '消息类型:1 单团询价，3游学询价 4,大客户询价 5 自由行询价 ，7  机票内容';
#地接询价表与出国路线关联表
CREATE TABLE `estimate_price_admit_lines_area` (
  `admit_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '对应：estimate_price_admit_requirements.id',
  `area_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '对应:sys_area.id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 
 #2014-12-26
 ALTER TABLE airticket_order ADD activationDate datetime COMMENT '激活时间';
 ALTER TABLE visa_order ADD activationDate datetime COMMENT '激活时间';
 
 #2015-01-29
ALTER TABLE productorder ADD is_after_supplement TINYINT(1) NULL DEFAULT 0 COMMENT '是否是补单产品，0：否，1：是';
ALTER TABLE sys_office ADD is_allow_supplement TINYINT(1) NULL DEFAULT 1 COMMENT '是否允许补单，0：否，1：是';

#2015-02-03

CREATE TABLE `msg_announcement` (
  `id` BIGINT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(45) DEFAULT NULL COMMENT '标题',
  `content` VARCHAR(45) DEFAULT NULL COMMENT '内容',
  `create_time` DATETIME NOT NULL COMMENT '发布时间',
  `if_cancel` VARCHAR(45) DEFAULT '0' COMMENT '是否撤销,1为撤销，0为非撤销',
  `cancel_time` VARCHAR(45) DEFAULT NULL COMMENT '撤销时间',
  `level` INT(10) UNSIGNED NOT NULL DEFAULT '1' COMMENT '优先级',
  `create_user_id` BIGINT(11) UNSIGNED NOT NULL COMMENT '用户编号',
 `company_id` BIGINT(11) UNSIGNED NOT NULL COMMENT '公司ID',
  `msg_type`  INT(10)   NOT NULL COMMENT '消息类型，1为公告，2为流程通知。。。。。',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='消息内容';

 

CREATE TABLE `msg_user_mark` (
  `msg_announcement_id` BIGINT(11) UNSIGNED NOT NULL COMMENT '通告编号',
  `user_id` BIGINT(11) UNSIGNED NOT NULL COMMENT '用户编号',
  `if_read` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否已读',
  `read_time` DATETIME DEFAULT NULL COMMENT '已读时间',
  UNIQUE KEY `announcement_index` (`msg_announcement_id`),
  KEY `user_id_index` (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='用户标记';

Alter table `trekizwholesalertts`.`sys_user` add column `login_status` int(1) DEFAULT 0  NOT NULL  COMMENT '登录状态：0：未登录，1：已登陆' after `two_psw`;
  
#2015-2-11
UPDATE sys_dict SET delFlag=1 WHERE type='new_visa_type' AND label='团签' OR type='new_visa_type' AND label='商务签';  

#2015-2-12
ALTER TABLE productorder ADD paymentType TINYINT(1) NULL DEFAULT 1 COMMENT '结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4';

ALTER TABLE productorder ADD COLUMN cost_money varchar(255) DEFAULT NULL COMMENT '订单成本价'; 
ALTER TABLE traveler ADD COLUMN costPriceSerialNum varchar(255) DEFAULT NULL COMMENT '游客成本价流水号'; 

#2015-2-28
ALTER TABLE traveler ADD COLUMN rebates_moneySerialNum varchar(255) DEFAULT NULL COMMENT '返佣费用';
#2015-3-3 add by yue.wang
ALTER TABLE visa ADD borrow_passport_batch_no VARCHAR(20)  DEFAULT NULL COMMENT '批量借护照批次号';
CREATE TABLE `sys_batch_no` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cur_date` varchar(8) DEFAULT NULL COMMENT '日期',
  `code` varchar(20) DEFAULT NULL COMMENT '主键',
  `value` int(11) DEFAULT NULL COMMENT '值',
  `step` int(11) DEFAULT NULL COMMENT '缓存数值（暂时不用）',
  PRIMARY KEY (`id`)
)  ;
#2015-3-3
ALTER TABLE department ADD COLUMN city varchar(50) DEFAULT NULL COMMENT '部门所在城市缩写'; 

#2015-3-3 addby 赵海明
alter table orderpay add column accountDate datetime  COMMENT '银行到账日期';

#2015-3-3 addby 栗欣云
ALTER TABLE `pay`
ADD COLUMN `status`  char(1) NULL COMMENT '0：未达账；1：已达账；2：驳回；3：撤消' AFTER `update_date`;

alter table traveler   
 add accounted_money varchar(40)  DEFAULT NULL COMMENT '达账金额 UUID'
 
#2015-3-4 addby 贾晨
INSERT INTO sys_dict (label,value,type,description,sort,createBy,createDate,updateBy,updateDate,remarks,delFlag) VALUES ('续签',8,'new_visa_type','新签证类型',8,null,null,null,null,null,0);

#2015-3-5 addby 宋扬
alter table visa_products  add column contactPerson varchar(20) NULL COMMENT '领区联系人'

 #2015-3-5 addby 温建野
alter table traveler 
 add payment_type  tinyint(1) DEFAULT '1' COMMENT '结算方式：1:即时结算;2:按月结算;3:担保结算;4:后续费'
 #2015-3-6 addby 温建野
alter table traveler modify column payment_type int(1);
alter table productorder modify column paymentType int(1);
  #2015-3-16 addby 何旭东
 CREATE INDEX visa_order_orderno_index ON visa_order (order_no);
CREATE INDEX orderpay_orderNum_index ON orderpay (orderNum);
CREATE INDEX visa_products_id_index ON visa_products (id);
CREATE INDEX agentinfo_id_index ON agentinfo (id);
CREATE INDEX traveler_id_index ON traveler (id);
CREATE INDEX sys_user_id_index ON sys_user (id);

 #2015-3-11 addby 吕佳楠
INSERT INTO `sys_menu` VALUES (NULL, 575, '0,1,417,575,', '下载预审表', '', '', '', 30, '1', 'visaOrderForOp:download:pretrial', 1, '2015-3-11 16:13:18', 1, '2015-3-11 16:13:18', NULL, '0');
INSERT INTO `sys_menu` VALUES (NULL, 575, '0,1,417,575,', '下载预约表', '', '', '', 40, '1', 'visaOrderForOp:download:reservation', 1, '2015-3-11 16:13:38', 1, '2015-3-11 16:13:38', NULL, '0');
 
#2015-3-13 add by 白亚昆
drop table contacts;

 #2015-3-11 addby 高建宁
CREATE TABLE `msg_user_mark` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `msg_announcement_id` bigint(11) NOT NULL COMMENT '通告编号',
  `user_id` bigint(11) NOT NULL COMMENT '用户编号',
  `if_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读 0 未读，1 已读',
  `read_time` datetime DEFAULT NULL COMMENT '已读时间',
  `message_status` int(1) DEFAULT '0' COMMENT '通知状态 0 未通知，1 已通知',
  `createBy` bigint(11) DEFAULT NULL,
  `updateBy` bigint(11) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `delFlag` varchar(1) NOT NULL COMMENT '删除标记（0：正常；1：删除；2：审核）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4255 DEFAULT CHARSET=utf8 COMMENT='公告/消息 发布关联表';
CREATE TABLE `msg_announcement` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) DEFAULT NULL COMMENT '标题',
  `title_vulgar_css` int(1) DEFAULT '0' COMMENT '标题样式 0:无加粗；1：有加粗',
  `title_light_css` int(1) DEFAULT '0' COMMENT '标题样式 0:无高亮；1：有高亮',
  `content` varchar(20000) DEFAULT NULL COMMENT '内容',
  `contentinfo` varchar(200) DEFAULT NULL COMMENT '缩略内容',
  `content_url` varchar(200) DEFAULT NULL COMMENT '内容链接 (通知/消息专用)',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '记录状态 0:保存；1：发布；2：已删除；3：已过期',
  `company_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '公司ID',
  `agntinfo_id` bigint(11) DEFAULT '0' COMMENT '渠道商ID',
  `msg_type` int(1) NOT NULL DEFAULT '0' COMMENT '类型，0：全部消息和公告；1：全站公告；2：部门公告;3：渠道公告; 4：约签公告；5:消息；',
  `msg_notice_type` int(1) NOT NULL DEFAULT '0' COMMENT '公告类型：1:单团；2：散拼；3：游学;4:大客户；5：自由行；6：签证；7：机票；8：其他；',
  `over_time` date DEFAULT NULL COMMENT '过期时间',
  `docinfo_ids` varchar(200) DEFAULT NULL COMMENT '附件ID组字符串',
  `createBy` bigint(11) NOT NULL DEFAULT '0' COMMENT '创建人id',
  `updateBy` bigint(11) DEFAULT '0' COMMENT '修改人id',
  `createDate` date NOT NULL COMMENT '创建时间',
  `updateDate` date DEFAULT NULL COMMENT '修改时间',
  `delFlag` varchar(1) DEFAULT '0' COMMENT '记录删除标记 （0：正常；1：删除；2：审核）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8 COMMENT='公告/消息 表';
CREATE TABLE `msg_to_department` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `msg_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '公告ID',
  `department_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '部门id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8 COMMENT='公告部门关联表';
CREATE ALGORITHM = UNDEFINED DEFINER = `opuser` @`%` SQL SECURITY DEFINER VIEW `msg_mark_view` AS 
SELECT 
  `msg`.`id` AS `msgId`,
  `msg`.`title` AS `title`,
  `msg`.`title_vulgar_css` AS `titleVulgarCss`,
  `msg`.`title_light_css` AS `titleLightCss`,
  `msg`.`content` AS `content`,
  `msg`.`contentinfo`  AS `contentinfo`,
  `msg`.`content_url` AS `contentUrl`,
  `msg`.`status` AS `status`,
  `msg`.`company_id` AS `companyId`,
  `msg`.`agntinfo_id` AS `agntinfoId`,
  `msg`.`msg_type` AS `msgType`,
  `msg`.`msg_notice_type` AS `msgNoticeType`,
  `msg`.`over_time` AS `overTime`,
  `msg`.`docinfo_ids` AS `docinfoIds`,
  `mark`.`id` AS `id`,
  `mark`.`msg_announcement_id` AS `msgAnnouncementId`,
  `mark`.`user_id` AS `userId`,
  `mark`.`if_read` AS `ifRead`,
  `mark`.`read_time` AS `readTime`,
  `mark`.`message_status` AS `messageStatus`,
  `mark`.`createBy` AS `createBy`,
  `mark`.`createDate` AS `createDate` 
FROM
  (
    `msg_announcement` `msg` 
    JOIN `msg_user_mark` `mark` 
      ON (
        (
          `msg`.`id` = `mark`.`msg_announcement_id`
        )
      )
  );
  #2015-3-19 addby 温建野-栗欣云
select * from sys_dict t where t.type = 'payment_type' and t.value = '4';
update sys_dict t set t.label = '后付费' where t.type = 'payment_type' and t.value = '4';

DROP VIEW `trekizwholesalertts`.`amount_currency`;
USE `trekizwholesalertts`;
CREATE  OR REPLACE VIEW `amount_currency` AS

select `ma`.`id` AS `id`,`c`.`currency_id` AS `currency_id`,`ma`.`serialNum` AS `serialNum`,group_concat(concat(`c`.`currency_mark`,`ma`.`amount`) separator ',') AS `amount`,`ma`.`moneyType` AS `moneyType`,`ma`.`createTime` AS `createTime` from (`money_amount` `ma` left join `currency` `c` on((`ma`.`currencyId` = `c`.`currency_id`))) group by `ma`.`serialNum` order by `ma`.`createTime` desc;
 #2015-3-31 addby 高建宁
ALTER TABLE `trekizwholesalertts`.`msg_announcement` CHANGE `msg_notice_type` `msg_notice_type` INT(1) DEFAULT 0 NOT NULL COMMENT '公告类型：1:单团；2：散拼；3：游学;4:大客户；5：自由行；6：签证；7：机票；8：套餐；9：其他；';
ALTER TABLE `trekizwholesalertts`.`estimate_price_base_info` ADD COLUMN `special_remark` VARCHAR(1024) NULL COMMENT '特殊人群备注' AFTER `special_person_sum`;
ALTER TABLE `trekizwholesalertts`.`estimate_pricer_reply` CHANGE `remark` `remark` TEXT CHARSET utf8 COLLATE utf8_general_ci NULL COMMENT '备注'; 

#2015-3-31 addby 吕佳楠
INSERT INTO `sys_dict` VALUES (NULL, '餐厅', '11', 'supplier_type', '地接社类型', 11, NULL, NULL, NULL, NULL, NULL, '0');


#2015-4-07 addby 王新伟
ALTER TABLE traveler ADD COLUMN jkSerialNum  varchar(40) COMMENT '游客借款UUID';

#2015-4-13 addby 白亚昆
update sys_menu set href = REPLACE(href,'orderCommon/manage/showOrderList','orderList/manage/showOrderList');
update sys_menu set href = REPLACE(href,'orderList/manage/showOrderList/199','orderCommon/manage/showOrderList/199');
update sys_menu set href = REPLACE(href,'orderList/manage/showOrderList/101','orderCommon/manage/showOrderList/101');

#2015-4-14 addby 王新伟
alter table review modify column printTime datetime;

#2015-4-21
update money_amount m,currency c set m.exchangerate = c.currency_exchangerate where m.exchangerate is null and m.currencyId = c.currency_id;

#2015-4-30 addby 白亚昆
ALTER TABLE travelactivity ADD opUserId bigint(11) NULL COMMENT '单团产品时：哪个计调可见';

#2015-05-11 addby 王新伟

ALTER TABLE visa_order ADD COLUMN jk_serialnum varchar(40) COMMENT '订单借款UUID';

#2016-05-03 addby 张潮
alter table sys_office add shelfRightsStatus  int(1) DEFAULT 1 COMMENT '上架权限 0：启用，1：禁用';

#2016-05-04 addby 白亚昆
ALTER TABLE productorder ADD quauq_service_charge varchar(255) DEFAULT NULL COMMENT 'quauq服务费UUID';

#  去掉字段  quauq_currency_type 统一用   currency_type 字段保存币种
-- ALTER TABLE activitygroup ADD quauq_currency_type varchar(50) DEFAULT NULL COMMENT 'quauq金额币种ID(按照表中出现的顺序，用逗号隔开)';
ALTER TABLE activitygroup ADD quauqAdultPrice decimal(18,2) DEFAULT NULL COMMENT 'quauq成人价';
ALTER TABLE activitygroup ADD quauqChildPrice decimal(18,2) DEFAULT NULL COMMENT 'quauq儿童价';
ALTER TABLE activitygroup ADD quauqSpecialPrice decimal(18,2) DEFAULT NULL COMMENT 'quauq特殊人群价';

DROP TABLE IF EXISTS `price_strategy`;
CREATE TABLE `price_strategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agentId` int(11) DEFAULT NULL COMMENT '渠道ID',
  `fromAreaIds` varchar(255) DEFAULT NULL COMMENT '出发地ids',
  `fromAreaNames` text COMMENT '出发地',
  `targetAreaIds` varchar(255) DEFAULT NULL COMMENT '目的地ids',
  `targetAreaNames` text COMMENT '目的地',
  `travelTypeIds` varchar(255) DEFAULT NULL COMMENT '旅游类型ids',
  `travelTypeNames` text COMMENT '旅游类型',
  `activityTypeIds` varchar(255) DEFAULT NULL COMMENT '产品类型ids',
  `activityTypeIdNames` text COMMENT '产品类型',
  `productLevelIds` varchar(255) DEFAULT NULL COMMENT '产品系列ids',
  `productLevelNames` text COMMENT '产品系列',
  `supplyId` int(11) DEFAULT NULL COMMENT '批发商ID',
  `supplyName` varchar(20) DEFAULT NULL COMMENT '批发商名称',
  `createBy` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `updateDate` date DEFAULT NULL COMMENT '更新时间',
  `delFlag` char(1) DEFAULT '0' COMMENT '删除标识',
  `remarks` text COMMENT '备注',
  `state` tinyint(1) DEFAULT NULL COMMENT '价格策略状态：启用（默认状态）1，禁用 2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='定价策略表';

DROP TABLE IF EXISTS `agent_price_strategy`;
CREATE TABLE `agent_price_strategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `priceStrategyId` int(11) DEFAULT NULL COMMENT '价格策略ID',
  `agentTypeIds` varchar(255) DEFAULT NULL COMMENT '渠道类型ids',
  `agentTypeNames` text COMMENT '渠道类型',
  `agentLevelIds` varchar(255) DEFAULT NULL COMMENT '渠道等级ids',
  `agentLevelNames` text COMMENT '渠道等级',
  `adultPriceStrategy` varchar(255) DEFAULT NULL COMMENT '成人价格策略：以json方式保存',
  `childrenPriceStrategy` varchar(255) DEFAULT NULL COMMENT '成人价格策略：以json方式保存',
  `specialPriceStrategy` varchar(255) DEFAULT NULL COMMENT '特殊人群价格策略：以json方式保存',
  `discountIds` varchar(255) DEFAULT NULL COMMENT '优惠ids',
  `discountNames` text COMMENT '优惠名称',
  `priceStrategyDesc` text COMMENT '价格策略描述',
  `createBy` bigint(20) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `updateBy` bigint(20) DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `delFlag` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `priceStrategyId` (`priceStrategyId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='渠道定价策略表';

#2016-5-4 addby 蒋扬
ALTER TABLE `agentinfo` ADD COLUMN `is_quauq_agent` VARCHAR(1) DEFAULT '0' COMMENT '是否是quauq渠道  0:否；1：是';
ALTER TABLE `agentinfo` ADD COLUMN `enable_quauq_agent` varchar(1) DEFAULT '0' COMMENT '是否启用此quauq渠道  0:否；1：是';
ALTER TABLE `sys_user` ADD COLUMN `is_quauq_agent_login_user` VARCHAR(1) DEFAULT '0' COMMENT '是否是quauq渠道的登陆账户   0:否；1：是';

#2016-05-09 addby 张潮
CREATE TABLE `price_strategy_line` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `lineName` varchar(50) NOT NULL COMMENT '线路名称',
  `sort` int(11) DEFAULT NULL COMMENT '顺序',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `areas` text  COMMENT '所含区域id',
  `companyId`   int(11) DEFAULT NULL COMMENT '批发商id',  
  `createBy` int(11) DEFAULT NULL COMMENT '创建人',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` int(11) DEFAULT NULL COMMENT '修改人',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `delFlag` char(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='价格策略线路';

/* 添加菜单 */
DROP PROCEDURE IF EXISTS addMenu;
CREATE PROCEDURE addMenu (IN newMenuParentId BIGINT (20), IN menuParentIds VARCHAR(255))
BEGIN
	INSERT INTO `sys_menu` VALUES (null, newMenuParentId, menuParentIds, '渠道账号列表', '/quauqAgent/manage/list', '', 'iconMenu-21', '10', '1', '', null, '1', now(), '1', now(), null, '0');
	INSERT INTO `sys_menu` VALUES (null, newMenuParentId, menuParentIds, '渠道账号添加', '/quauqAgent/manage/firstForm', '', 'iconMenu-21', '20', '1', '', null, '1', now(), '1', now(), null, '0');
	INSERT INTO `sys_menu` VALUES (null, newMenuParentId, menuParentIds, 'QUAUQ渠道统计', '/quauqAgent/manage/quauqAgentStatistics', '', '', '30', '1', '', null, '1', now(), '1', now(), null, '0');
	INSERT INTO `sys_menu` VALUES (null, newMenuParentId, menuParentIds, '批发商上架权限', '/shelfRights/list', '', '', '40', '1', '', null, '1', now(), '1', now(), null, '0');
END;

DROP PROCEDURE IF EXISTS changeMenu;
CREATE PROCEDURE changeMenu()
BEGIN

DECLARE orderId INT DEFAULT 0;
DECLARE newMenuParentId INT DEFAULT 0;
DECLARE menuParentIds VARCHAR(255);

INSERT INTO `sys_menu` VALUES (null, '1', '0,1,', 'QUAUQ后台', '', '', 'iconMenu-24', '240', '1', 'quauqAgent:manage:view', null, '1', now(), '1', now(), null, '0');

SET orderId = (SELECT id FROM sys_menu WHERE NAME = 'QUAUQ后台');
SET newMenuParentId = orderId;
SET menuParentIds = CONCAT ('0,1,', orderId, ',');

CALL addMenu(newMenuParentId, menuParentIds);
END; 
CALL changeMenu;
DROP PROCEDURE IF EXISTS addMenu;
DROP PROCEDURE IF EXISTS changeMenu;

ALTER TABLE sys_user ADD quauqBookOrderPermission varchar(1) DEFAULT '0' COMMENT '是否可以用quauq渠道报名 0:否；1：是';

ALTER TABLE productorder ADD hasseen  char(1) DEFAULT '1' COMMENT 'T1 平台，是否查看,1：没有查看，0：已查看'; 

#2016-05-09 addby 张潮
INSERT INTO `sys_menu` VALUES (null, 163, '0,1,163,', '渠道等级', '/agentGrade/list', '', '', 30, '1', 'agent:grade:agent', NULL, 1, sysdate(), 1, sysdate(), NULL, '0');
INSERT INTO `sys_menu` VALUES (null, 163, '0,1,163,', '渠道类型', '/agentType/list', '', '', 30, '1', 'agent:type:agent', NULL, 1, sysdate(), 1, sysdate(), NULL, '0');
INSERT INTO `sys_menu` VALUES (null, 163, '0,1,163,', '线路设置', '/priceStrategy/line/getAllLines', '', '', 30, '1', '', NULL, 1, sysdate(), 1, sysdate(), NULL, '0');
-- 权限设置： 添加menu--->设置菜单级别以及鉴权字段permission--->编辑角色的权限--->编辑用户角色--->修改页面shiro鉴权
-- 
-- 预报单结算单备注功能：
-- 	预报单--备注 permission：forecast:remark:edit
-- 	结算单--备注 permission：settle:remark:edit

-- 创建存储过程，将菜单权限数据库中所有的角色关联。
DROP PROCEDURE if EXISTS copy_menu2role ;
CREATE PROCEDURE copy_menu2role(IN parName VARCHAR(50),IN childMenu VARCHAR(20),IN perVar VARCHAR(50),IN link VARCHAR(50))
	begin
	
	DECLARE parId BIGINT;
	DECLARE parParId VARCHAR(30);
	

-- 查询父级菜单
  	select id,parentIds into parId, parParId from sys_menu where name = parName and delFlag = 0;
-- 在父级菜单下插入childMenu
  	IF (parId is not null) THEN
		insert into sys_menu(parentid,parentids,name,sort,isshow,permission,createBy,createDate,delFlag,href) values (parId,CONCAT(parParId,CONCAT( parId,',')),childMenu,30,1,perVar,366,NOW(),0,link);
  	end IF; 
-- 
	END;

 call  copy_menu2role('顶级菜单','渠道价格策略','price:strategy','');
 call  copy_menu2role('渠道价格策略','新增价格策略','price:add','');
 call  copy_menu2role('渠道价格策略','修改价格策略','price:update','');
  call  copy_menu2role('渠道价格策略','渠道价格策略列表','','/pricingStrategy/manager/list');
drop PROCEDURE copy_menu2role;
ALTER TABLE visa_order ADD COLUMN jk_serialnum varchar(40) COMMENT '订单借款UUID';

#2015-05-11 addby 李鑫
ALTER TABLE agentinfo ADD COLUMN salesRoom varchar(40) COMMENT '门市名称';
#2016-06-14 addby 蒋扬
ALTER TABLE `sys_office` ADD COLUMN `charge_rate` decimal(5,4) DEFAULT 0.01 COMMENT '服务费率（供应服务费占QUAUQ价的比率）';
#2016-06-15 addby 蒋扬 (删除订单表的quauq总价)
ALTER TABLE `productorder` DROP COLUMN `quauq_total_money`;
ALTER TABLE `productorder` ADD COLUMN isPayedCharge tinyint(4) DEFAULT NULL COMMENT 't2是否向quauq结清服务费  0 未结清 1 已结清';

#2016-06-16 addby 李鑫 (添加价格策略表和价格策略关系表)
CREATE TABLE `pricingStrategy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `personType` int(2) NOT NULL COMMENT '优惠策略针对人群',
  `favorableType` int(1) NOT NULL COMMENT '优惠类型',
  `favorableNum` decimal(18,2) DEFAULT NULL COMMENT '优惠数额',
  `remarks` varchar(255) DEFAULT NULL,
  `createBy` int(11) DEFAULT NULL COMMENT '创建人',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` int(11) DEFAULT NULL COMMENT '更新人',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `delFlag` char(1) DEFAULT NULL COMMENT '0：正常；1：删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `activity_pricingStrategy` (
  `travelactivityId` bigint(20) NOT NULL COMMENT '产品编号',
  `activitygroupId` bigint(20) NOT NULL COMMENT '团期编号',
  `pricingStrategyId` bigint(20) NOT NULL COMMENT '价格策略编号',
  `usageState` int(1) DEFAULT NULL COMMENT '0;1',
  `createBy` int(11) DEFAULT NULL COMMENT '创建人',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateBy` int(11) DEFAULT NULL COMMENT '更新人',
  `updateDate` datetime DEFAULT NULL COMMENT '更新时间',
  `delFlag` char(1) DEFAULT NULL COMMENT '0：正常；1：删除',
  PRIMARY KEY (`travelactivityId`,`activitygroupId`,`pricingStrategyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品-团期-价格策略';


#2016-06-28 addby 李鑫
DROP PROCEDURE if EXISTS copy_menu2roleOrder ;
CREATE PROCEDURE copy_menu2roleOrder(IN parMenu VARCHAR(225),IN parName VARCHAR(50),IN childMenu VARCHAR(20),IN perVar VARCHAR(50))
	begin
	
	DECLARE parId BIGINT;
	DECLARE parParId VARCHAR(30);

	select id,parentIds into parId, parParId from sys_menu where permission = parMenu and name = parName and delFlag = 0;

	IF (parId is not null) THEN
		insert into sys_menu(parentid,parentids,name,sort,isshow,permission,createBy,createDate,delFlag) values(parId,CONCAT(parParId,CONCAT( parId,',')),childMenu,30,1,perVar,366,NOW(),0);
	end IF; 
	
	END;

call  copy_menu2roleOrder('singleOrder:list:allorder','单团订单','结算单','singleOrder:list:costpayl');

call  copy_menu2roleOrder('looseOrder:list:allorder','散拼订单','结算单','looseOrder:list:costpayl');

call  copy_menu2roleOrder('airticketOrderForSale:list:allorder','销售机票订单','结算单','airticketOrderForSale:list:costpayl');

call  copy_menu2roleOrder('visaOrderForSale:list:allorder','销售签证订单','结算单','visaOrderForSale:list:costpayl');

call  copy_menu2roleOrder('cruiseOrder:list:allorder','游轮订单','结算单','cruiseOrder:list:costpayl');

call  copy_menu2roleOrder('studyOrder:list:allorder','游学订单','结算单','studyOrder:list:costpayl');

call  copy_menu2roleOrder('bigCustomerOrder:list:allorder','大客户订单','结算单','bigCustomerOrder:list:costpayl');

call  copy_menu2roleOrder('freeOrder:list:allorder','自由行订单','结算单','freeOrder:list:costpayl');

drop PROCEDURE copy_menu2roleOrder;

#2016-06-15 addby 李鑫
ALTER TABLE productorder modify column confirmationFileId varchar(255);
ALTER TABLE productorder ADD COLUMN downloadFileIds varchar(225) COMMENT '已下载文件ID';
ALTER TABLE productorder ADD COLUMN confirmFlag tinyint(1) COMMENT '是否提醒';

ALTER TABLE airticket_order modify column confirmationFileId varchar(255);
ALTER TABLE airticket_order ADD COLUMN downloadFileIds varchar(225) COMMENT '已下载文件ID';
ALTER TABLE airticket_order ADD COLUMN confirmFlag tinyint(1) COMMENT '是否提醒';

ALTER TABLE visa_order modify column confirmationFileId varchar(255);
ALTER TABLE visa_order ADD COLUMN downloadFileIds varchar(225) COMMENT '已下载文件ID';
ALTER TABLE visa_order ADD COLUMN confirmFlag tinyint(1) COMMENT '是否提醒';

#2016-07-29 addby 李鑫
ALTER TABLE activitygroup  ADD COLUMN maxChildrenCount int(8) DEFAULT null COMMENT '儿童最高人数';

#2016-08-01 addby 李鑫
ALTER TABLE activity_airticket  ADD COLUMN maxChildrenCount int(8) DEFAULT null COMMENT '儿童最高人数';

#2016-10-13 addby 李鑫  暂不使用
alter table activitygroup ADD COLUMN eyelessAgentIds VARCHAR(255) DEFAULT '' COMMENT '不能查看该团期的T1渠道商';

#2016-11-01 addby 李鑫  544 545 需求
alter table traveler add column issuePlace1 varchar(10) default '' COMMENT '签发地';
alter table traveler add column issueDate date default null COMMENT '签发日期';
#2016-11-04 addby 李鑫  544 545 需求
alter table traveler add column hometown varchar(50) default '' COMMENT '出生地';

#2016-11-9 addby 李鑫
ALTER TABLE activitygroup  ADD COLUMN hasEyelessAgents int(1) DEFAULT 0 COMMENT '是否存在T1登录渠道商不可见';