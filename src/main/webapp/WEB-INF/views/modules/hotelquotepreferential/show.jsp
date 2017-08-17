<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>HotelQuotePreferential信息</title>
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
	<div class="ydbz_tit pl20">HotelQuotePreferential信息</div>
	<div class="maintain_add">
		
		<p>
			<label>uuid：</label> 
			<span>
				${hotelQuotePreferential.uuid}
			</span>
		</p>
		<p>
			<label>酒店价单UUID：</label> 
			<span>
				${hotelQuotePreferential.hotelPlUuid}
			</span>
		</p>
		<p>
			<label>岛屿UUID：</label> 
			<span>
				${hotelQuotePreferential.islandUuid}
			</span>
		</p>
		<p>
			<label>酒店UUID：</label> 
			<span>
				${hotelQuotePreferential.hotelUuid}
			</span>
		</p>
		<p>
			<label>优惠信息名称：</label> 
			<span>
				${hotelQuotePreferential.preferentialName}
			</span>
		</p>
		<p>
			<label>下单代码：</label> 
			<span>
				${hotelQuotePreferential.bookingCode}
			</span>
		</p>
		<p>
			<label>入住日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelQuotePreferential.inDate}"/>
			</span>
		</p>
		<p>
			<label>离店日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelQuotePreferential.outDate}"/>
			</span>
		</p>
		<p>
			<label>预订起始日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelQuotePreferential.bookingStartDate}"/>
			</span>
		</p>
		<p>
			<label>预订结束日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelQuotePreferential.bookingEndDate}"/>
			</span>
		</p>
		<p>
			<label>交通（上岛方式字典UUID，多个用；分隔）：</label> 
			<span>
				${hotelQuotePreferential.islandWay}
			</span>
		</p>
		<p>
			<label>是否关联酒店（0不关联，1关联。关联酒店的信息存储在hotel_pl_preferential_relHotel表中）：</label> 
			<span>
				${hotelQuotePreferential.isRelation}
			</span>
		</p>
		<p>
			<label>创建人：</label> 
			<span>
				${hotelQuotePreferential.createBy}
			</span>
		</p>
		<p>
			<label>创建时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelQuotePreferential.createDate}"/>
			</span>
		</p>
		<p>
			<label>修改人：</label> 
			<span>
				${hotelQuotePreferential.updateBy}
			</span>
		</p>
		<p>
			<label>修改时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelQuotePreferential.updateDate}"/>
			</span>
		</p>
		<p>
			<label>删除标识：</label> 
			<span>
				${hotelQuotePreferential.delFlag}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
