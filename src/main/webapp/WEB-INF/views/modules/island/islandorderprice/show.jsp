<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>IslandOrderPrice信息</title>
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
	<div class="ydbz_tit pl20">IslandOrderPrice信息</div>
	<div class="maintain_add">
		
		<p>
			<label>uuid：</label> 
			<span>
				${islandOrderPrice.uuid}
			</span>
		</p>
		<p>
			<label>订单uuid：</label> 
			<span>
				${islandOrderPrice.orderUuid}
			</span>
		</p>
		<p>
			<label>价格类型：1 团期价格类型；2 返佣类型；3 优惠类型 4 其他类型：</label> 
			<span>
				${islandOrderPrice.priceType}
			</span>
		</p>
		<p>
			<label>币种：</label> 
			<span>
				${islandOrderPrice.currencyId}
			</span>
		</p>
		<p>
			<label>价格：</label> 
			<span>
				${islandOrderPrice.price}
			</span>
		</p>
		<p>
			<label>个数：</label> 
			<span>
				${islandOrderPrice.num}
			</span>
		</p>
		<p>
			<label>创建人：</label> 
			<span>
				${islandOrderPrice.createBy}
			</span>
		</p>
		<p>
			<label>创建时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${islandOrderPrice.createDate}"/>
			</span>
		</p>
		<p>
			<label>修改人：</label> 
			<span>
				${islandOrderPrice.updateBy}
			</span>
		</p>
		<p>
			<label>修改时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${islandOrderPrice.updateDate}"/>
			</span>
		</p>
		<p>
			<label>删除状态：</label> 
			<span>
				${islandOrderPrice.delFlag}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
