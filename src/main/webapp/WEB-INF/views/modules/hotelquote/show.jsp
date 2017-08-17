<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>HotelQuote信息</title>
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
	<div class="ydbz_tit pl20">HotelQuote信息</div>
	<div class="maintain_add">
		
		<p>
			<label>uuid：</label> 
			<span>
				${hotelQuote.uuid}
			</span>
		</p>
		<p>
			<label>批发商id：</label> 
			<span>
				${hotelQuote.wholesalerId}
			</span>
		</p>
		<p>
			<label>销售人员ID ：</label> 
			<span>
				${hotelQuote.userId}
			</span>
		</p>
		<p>
			<label>报价类型（1、直客；2、同行；3、其他）：</label> 
			<span>
				${hotelQuote.quoteType}
			</span>
		</p>
		<p>
			<label>报价对象（报价类型quote_type等于2时对应渠道ID，等于3时对应客户姓名）：</label> 
			<span>
				${hotelQuote.quoteObject}
			</span>
		</p>
		<p>
			<label>联系人名称 ：</label> 
			<span>
				${hotelQuote.linkName}
			</span>
		</p>
		<p>
			<label>联系电话：</label> 
			<span>
				${hotelQuote.linkPhone}
			</span>
		</p>
		<p>
			<label>币种id：</label> 
			<span>
				${hotelQuote.currencyId}
			</span>
		</p>
		<p>
			<label>备注：</label> 
			<span>
				${hotelQuote.memo}
			</span>
		</p>
		<p>
			<label>创建人：</label> 
			<span>
				${hotelQuote.createBy}
			</span>
		</p>
		<p>
			<label>创建时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelQuote.createDate}"/>
			</span>
		</p>
		<p>
			<label>修改人：</label> 
			<span>
				${hotelQuote.updateBy}
			</span>
		</p>
		<p>
			<label>修改时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelQuote.updateDate}"/>
			</span>
		</p>
		<p>
			<label>删除标识：</label> 
			<span>
				${hotelQuote.delFlag}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
