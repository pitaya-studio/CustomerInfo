<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>AirticketactivityreserveTemp信息</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
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
	<div class="ydbz_tit pl20">AirticketactivityreserveTemp信息</div>
	<div class="maintain_add">
		
		<p>
			<label>uuid：</label> 
			<span>
				${airticketactivityreserveTemp.uuid}
			</span>
		</p>
		<p>
			<label>机票产品信息表ID外键：</label> 
			<span>
				${airticketactivityreserveTemp.activityId}
			</span>
		</p>
		<p>
			<label>渠道商基本信息表id：</label> 
			<span>
				${airticketactivityreserveTemp.agentId}
			</span>
		</p>
		<p>
			<label>0,定金占位；1,全款占位：</label> 
			<span>
				${airticketactivityreserveTemp.reserveType}
			</span>
		</p>
		<p>
			<label>切位人数：</label> 
			<span>
				${airticketactivityreserveTemp.payReservePosition}
			</span>
		</p>
		<p>
			<label>售出切位人数：</label> 
			<span>
				${airticketactivityreserveTemp.soldPayPosition}
			</span>
		</p>
		<p>
			<label>定金金额：</label> 
			<span>
				${airticketactivityreserveTemp.frontMoney}
			</span>
		</p>
		<p>
			<label>剩余的切位人数：</label> 
			<span>
				${airticketactivityreserveTemp.leftpayReservePosition}
			</span>
		</p>
		<p>
			<label>剩余的定金金额：</label> 
			<span>
				${airticketactivityreserveTemp.leftFontMoney}
			</span>
		</p>
		<p>
			<label>预订人：</label> 
			<span>
				${airticketactivityreserveTemp.reservation}
			</span>
		</p>
		<p>
			<label>支付方式：</label> 
			<span>
				${airticketactivityreserveTemp.payType}
			</span>
		</p>
		<p>
			<label>切位备注：</label> 
			<span>
				${airticketactivityreserveTemp.remark}
			</span>
		</p>
		<p>
			<label>还位备注：</label> 
			<span>
				${airticketactivityreserveTemp.returnRemark}
			</span>
		</p>
		<p>
			<label>createBy：</label> 
			<span>
				${airticketactivityreserveTemp.createBy}
			</span>
		</p>
		<p>
			<label>createDate：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${airticketactivityreserveTemp.createDate}"/>
			</span>
		</p>
		<p>
			<label>updateBy：</label> 
			<span>
				${airticketactivityreserveTemp.updateBy}
			</span>
		</p>
		<p>
			<label>updateDate：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${airticketactivityreserveTemp.updateDate}"/>
			</span>
		</p>
		<p>
			<label>delFlag：</label> 
			<span>
				${airticketactivityreserveTemp.delFlag}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
