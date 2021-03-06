<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>SysRegion信息</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">SysRegion信息</div>
	<div class="maintain_add">
		
		<p>
			<label>uuid：</label> 
			<span>
				${sysRegion.uuid}
			</span>
		</p>
		<p>
			<label>区域名称：</label> 
			<span>
				${sysRegion.name}
			</span>
		</p>
		<p>
			<label>区域范围：</label> 
			<span>
				${sysRegion.domain}
			</span>
		</p>
		<p>
			<label>状态0启用1停用：</label> 
			<span>
				${sysRegion.status}
			</span>
		</p>
		<p>
			<label>删除状态：</label> 
			<span>
				${sysRegion.delFlag}
			</span>
		</p>
		<p>
			<label>国内国际标记1国内2国际：</label> 
			<span>
				${sysRegion.isHome}
			</span>
		</p>
		<p>
			<label>签证创建时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${sysRegion.createDate}"/>
			</span>
		</p>
		<p>
			<label>修改时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${sysRegion.updateDate}"/>
			</span>
		</p>
		<p>
			<label>创建人：</label> 
			<span>
				${sysRegion.createBy}
			</span>
		</p>
		<p>
			<label>修改人：</label> 
			<span>
				${sysRegion.updateBy}
			</span>
		</p>
		<p>
			<label>级别：</label> 
			<span>
				${sysRegion.level}
			</span>
		</p>
		<p>
			<label>描述：</label> 
			<span>
				${sysRegion.description}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="返&nbsp;&nbsp;&nbsp;回" class="btn btn-primary gray" onclick="history.go(-1)" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
