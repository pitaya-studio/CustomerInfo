<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>IslandOrder信息</title>
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
	<div class="ydbz_tit pl20">IslandOrder信息</div>
	<div class="maintain_add">
		
		<p>
			<label>订单uuid：</label> 
			<span>
				${islandOrder.uuid}
			</span>
		</p>
		<p>
			<label>海岛游产品uuid：</label> 
			<span>
				${islandOrder.activityIslandUuid}
			</span>
		</p>
		<p>
			<label>海岛游产品团期uuid：</label> 
			<span>
				${islandOrder.activityIslandGroupUuid}
			</span>
		</p>
		<p>
			<label>订单单号：</label> 
			<span>
				${islandOrder.orderNum}
			</span>
		</p>
		<p>
			<label>订单状态 0-全部 1-全款未支付   预订后没有支付 2-订金未支付   订金占位后没有支付 3-已占位   非订金占位 4-订金已经支付 5-已经支付 89-已申请退团 99-已经取消订单 100-可操作状态-正向平台同步 101-查看状态-正向平台同步 111-已经删除订单 199-财务订单 ：</label> 
			<span>
				${islandOrder.orderStatus}
			</span>
		</p>
		<p>
			<label>预订单位-即渠道：</label> 
			<span>
				${islandOrder.orderCompany}
			</span>
		</p>
		<p>
			<label>预订单位名称：</label> 
			<span>
				${islandOrder.orderCompanyName}
			</span>
		</p>
		<p>
			<label>跟进销售员id：</label> 
			<span>
				${islandOrder.orderSalerId}
			</span>
		</p>
		<p>
			<label>预订人名称：</label> 
			<span>
				${islandOrder.orderPersonName}
			</span>
		</p>
		<p>
			<label>预订人联系电话：</label> 
			<span>
				${islandOrder.orderPersonPhoneNum}
			</span>
		</p>
		<p>
			<label>预订日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${islandOrder.orderTime}"/>
			</span>
		</p>
		<p>
			<label>预定人数：</label> 
			<span>
				${islandOrder.orderPersonNum}
			</span>
		</p>
		<p>
			<label>订金金额UUID：</label> 
			<span>
				${islandOrder.frontMoney}
			</span>
		</p>
		<p>
			<label>支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消：</label> 
			<span>
				${islandOrder.payStatus}
			</span>
		</p>
		<p>
			<label>已付金额UUID：</label> 
			<span>
				${islandOrder.payedMoney}
			</span>
		</p>
		<p>
			<label>支付方式1-支票 2-POS机付款 3-现金支付 4-汇款 5-快速支付：</label> 
			<span>
				${islandOrder.payType}
			</span>
		</p>
		<p>
			<label>创建者：</label> 
			<span>
				${islandOrder.createBy}
			</span>
		</p>
		<p>
			<label>创建日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${islandOrder.createDate}"/>
			</span>
		</p>
		<p>
			<label>更新者：</label> 
			<span>
				${islandOrder.updateBy}
			</span>
		</p>
		<p>
			<label>更新日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${islandOrder.updateDate}"/>
			</span>
		</p>
		<p>
			<label>删除标记：</label> 
			<span>
				${islandOrder.delFlag}
			</span>
		</p>
		<p>
			<label>当前退换记录Id：</label> 
			<span>
				${islandOrder.changeGroupId}
			</span>
		</p>
		<p>
			<label>退换类型：</label> 
			<span>
				${islandOrder.groupChangeType}
			</span>
		</p>
		<p>
			<label>订单成本金额：</label> 
			<span>
				${islandOrder.costMoney}
			</span>
		</p>
		<p>
			<label>达账状态：</label> 
			<span>
				${islandOrder.asAcountType}
			</span>
		</p>
		<p>
			<label>达账金额UUID：</label> 
			<span>
				${islandOrder.accountedMoney}
			</span>
		</p>
		<p>
			<label>下订单时产品的预收定金：</label> 
			<span>
				${islandOrder.payDeposit}
			</span>
		</p>
		<p>
			<label>占位类型    如果为0  或者为空  表示是占位  如果为1  表示是切位：</label> 
			<span>
				${islandOrder.placeHolderType}
			</span>
		</p>
		<p>
			<label>下订单时的单房差：</label> 
			<span>
				${islandOrder.singleDiff}
			</span>
		</p>
		<p>
			<label>取消原因：</label> 
			<span>
				${islandOrder.cancelDescription}
			</span>
		</p>
		<p>
			<label>0 未付款 1 已付首款 2 已付尾款（全款）3 首款已达账 4 尾款（全款）已达账 5 开发票申请 6 已开发票：</label> 
			<span>
				${islandOrder.isPayment}
			</span>
		</p>
		<p>
			<label>付款方式：</label> 
			<span>
				${islandOrder.payMode}
			</span>
		</p>
		<p>
			<label>保留天数：</label> 
			<span>
				${islandOrder.remainDays}
			</span>
		</p>
		<p>
			<label>激活时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${islandOrder.activationDate}"/>
			</span>
		</p>
		<p>
			<label>订单锁定状态：0:正常  1：锁定(订单锁定状态不允许操作订单)：</label> 
			<span>
				${islandOrder.lockStatus}
			</span>
		</p>
		<p>
			<label>特殊需求：</label> 
			<span>
				${islandOrder.specialDemand}
			</span>
		</p>
		<p>
			<label>订单总价UUID：</label> 
			<span>
				${islandOrder.totalMoney}
			</span>
		</p>
		<p>
			<label>确认单文件id：</label> 
			<span>
				${islandOrder.confirmationFileId}
			</span>
		</p>
		<p>
			<label>原始应收价 一次生成永不改变：</label> 
			<span>
				${islandOrder.originalTotalMoney}
			</span>
		</p>
		<p>
			<label>是否是补单产品，0：否，1：是：</label> 
			<span>
				${islandOrder.isAfterSupplement}
			</span>
		</p>
		<p>
			<label>原始订金金额（乘人数后金额）：</label> 
			<span>
				${islandOrder.originalFrontMoney}
			</span>
		</p>
		<p>
			<label>结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4：</label> 
			<span>
				${islandOrder.paymentType}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
