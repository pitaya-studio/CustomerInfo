<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>IslandTraveler信息</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">IslandTraveler信息</div>
	<div class="maintain_add">
		
		<p>
			<label> 游客UUID：</label> 
			<span>
				${islandTraveler.travelerUuid}
			</span>
		</p>
		<p>
			<label> 订单UUID：</label> 
			<span>
				${islandTraveler.orderUuid}
			</span>
		</p>
		<p>
			<label>订单id 关联productorder,visa_order,airticket_order等表的id，暂保留兼容以前的设计，以后以订单uuid为关联：</label> 
			<span>
				${islandTraveler.orderId}
			</span>
		</p>
		<p>
			<label>游客姓名：</label> 
			<span>
				${islandTraveler.name}
			</span>
		</p>
		<p>
			<label>游客名称拼音：</label> 
			<span>
				${islandTraveler.nameSpell}
			</span>
		</p>
		<p>
			<label>房屋类型 1-单人间 2-双人间：</label> 
			<span>
				${islandTraveler.hotelDemand}
			</span>
		</p>
		<p>
			<label>单房差：</label> 
			<span>
				${islandTraveler.singleDiff}
			</span>
		</p>
		<p>
			<label>单房差几晚：</label> 
			<span>
				${islandTraveler.singleDiffNight}
			</span>
		</p>
		<p>
			<label>papersType：</label> 
			<span>
				${islandTraveler.papersType}
			</span>
		</p>
		<p>
			<label>身份证号：</label> 
			<span>
				${islandTraveler.idCard}
			</span>
		</p>
		<p>
			<label>国籍：</label> 
			<span>
				${islandTraveler.nationality}
			</span>
		</p>
		<p>
			<label>性别 1-男 2-女：</label> 
			<span>
				${islandTraveler.sex}
			</span>
		</p>
		<p>
			<label>出生日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${islandTraveler.birthDay}"/>
			</span>
		</p>
		<p>
			<label>issuePlace：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${islandTraveler.issuePlace}"/>
			</span>
		</p>
		<p>
			<label>validityDate：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${islandTraveler.validityDate}"/>
			</span>
		</p>
		<p>
			<label>手机号：</label> 
			<span>
				${islandTraveler.telephone}
			</span>
		</p>
		<p>
			<label>备注：</label> 
			<span>
				${islandTraveler.remark}
			</span>
		</p>
		<p>
			<label>单价（发布产品时的定价）：</label> 
			<span>
				${islandTraveler.srcPrice}
			</span>
		</p>
		<p>
			<label>人员类型（1-成人 2-儿童 3-特殊人群）：</label> 
			<span>
				${islandTraveler.personType}
			</span>
		</p>
		<p>
			<label>passportCode：</label> 
			<span>
				${islandTraveler.passportCode}
			</span>
		</p>
		<p>
			<label>护照有效期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${islandTraveler.passportValidity}"/>
			</span>
		</p>
		<p>
			<label>护照种类 1：因公护照 2：因私护照：</label> 
			<span>
				${islandTraveler.passportType}
			</span>
		</p>
		<p>
			<label>是否需要联运 0：不需要，1：需要：</label> 
			<span>
				${islandTraveler.intermodalType}
			</span>
		</p>
		<p>
			<label>产品联运信息表主键 关联intermodal_strategy表：</label> 
			<span>
				${islandTraveler.intermodalId}
			</span>
		</p>
		<p>
			<label>护照状态 1:借出;2:归还客户;3:未签收;4:已签收：</label> 
			<span>
				${islandTraveler.passportStatus}
			</span>
		</p>
		<p>
			<label>单价币种：</label> 
			<span>
				${islandTraveler.srcPriceCurrency}
			</span>
		</p>
		<p>
			<label>单房差币种：</label> 
			<span>
				${islandTraveler.singleDiffCurrency}
			</span>
		</p>
		<p>
			<label>记录游客属于那种订单（预报名：0 单团类、散拼、签证、机票同产品类型）：</label> 
			<span>
				${islandTraveler.orderType}
			</span>
		</p>
		<p>
			<label>删除标记   0:正常   1：删除    2:退团审核中  3：已退团   4：转团审核中   5：已转团：</label> 
			<span>
				${islandTraveler.delFlag}
			</span>
		</p>
		<p>
			<label>游客结算价流水号：</label> 
			<span>
				${islandTraveler.payPriceSerialNum}
			</span>
		</p>
		<p>
			<label>是否有机票标志 0 标示无机票 或已退票 1标示有机票：</label> 
			<span>
				${islandTraveler.isAirticketFlag}
			</span>
		</p>
		<p>
			<label>游客原始应收价 一次生成 永不改变：</label> 
			<span>
				${islandTraveler.originalPayPriceSerialNum}
			</span>
		</p>
		<p>
			<label>游客已付款流水号：</label> 
			<span>
				${islandTraveler.payedMoneySerialNum}
			</span>
		</p>
		<p>
			<label>costPriceSerialNum：</label> 
			<span>
				${islandTraveler.costPriceSerialNum}
			</span>
		</p>
		<p>
			<label>rebatesMoneySerialNum：</label> 
			<span>
				${islandTraveler.rebatesMoneySerialNum}
			</span>
		</p>
		<p>
			<label>paymentType：</label> 
			<span>
				${islandTraveler.paymentType}
			</span>
		</p>
		<p>
			<label>id：</label> 
			<span>
				${islandTraveler.mainOrderId}
			</span>
		</p>
		<p>
			<label>id：</label> 
			<span>
				${islandTraveler.mainOrderTravelerid}
			</span>
		</p>
		<p>
			<label> UUID：</label> 
			<span>
				${islandTraveler.accountedMoney}
			</span>
		</p>
		<p>
			<label>批量借护照批次号：</label> 
			<span>
				${islandTraveler.borrowPassportBatchNo}
			</span>
		</p>
		<p>
			<label>游客借款UUID：</label> 
			<span>
				${islandTraveler.jkSerialNum}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
