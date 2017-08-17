<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>ActivityHotel信息</title>
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
	<div class="ydbz_tit pl20">ActivityHotel信息</div>
	<div class="maintain_add">
		
		<p>
			<label>唯一性标识uuid：</label> 
			<span>
				${activityHotel.uuid}
			</span>
		</p>
		<p>
			<label>产品编号,如SG0001：</label> 
			<span>
				${activityHotel.activitySerNum}
			</span>
		</p>
		<p>
			<label>酒店产品的名称：</label> 
			<span>
				${activityHotel.activityName}
			</span>
		</p>
		<p>
			<label>国家：</label> 
			<span>
				${activityHotel.country}
			</span>
		</p>
		<p>
			<label>海岛：</label> 
			<span>
				${activityHotel.islandUuid}
			</span>
		</p>
		<p>
			<label>酒店：</label> 
			<span>
				${activityHotel.hotelUuid}
			</span>
		</p>
		<p>
			<label>币种：</label> 
			<span>
				${activityHotel.currencyId}
			</span>
		</p>
		<p>
			<label>备注：</label> 
			<span>
				${activityHotel.memo}
			</span>
		</p>
		<p>
			<label>产品所属批发商的ID：</label> 
			<span>
				${activityHotel.wholesalerId}
			</span>
		</p>
		<p>
			<label>产品所属部门(创建者部门)：</label> 
			<span>
				${activityHotel.deptId}
			</span>
		</p>
		<p>
			<label>创建人：</label> 
			<span>
				${activityHotel.createBy}
			</span>
		</p>
		<p>
			<label>创建时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${activityHotel.createDate}"/>
			</span>
		</p>
		<p>
			<label>更新人：</label> 
			<span>
				${activityHotel.updateBy}
			</span>
		</p>
		<p>
			<label>更新时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${activityHotel.updateDate}"/>
			</span>
		</p>
		<p>
			<label>0：正常；1：删除：</label> 
			<span>
				${activityHotel.delFlag}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
