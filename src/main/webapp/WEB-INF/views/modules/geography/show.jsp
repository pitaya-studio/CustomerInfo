<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>SysGeography信息</title>
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
	<div class="ydbz_tit pl20">SysGeography信息</div>
	<div class="maintain_add">
		
		<p>
			<label>uuid：</label> 
			<span>
				${sysGeography.uuid}
			</span>
		</p>
		<p>
			<label>父级ID：</label> 
			<span>
				${sysGeography.parentId}
			</span>
		</p>
		<p>
			<label>父级UUID：</label> 
			<span>
				${sysGeography.parentUuid}
			</span>
		</p>
		<p>
			<label>级别：</label> 
			<span>
				${sysGeography.level}
			</span>
		</p>
		<p>
			<label>排序：</label> 
			<span>
				${sysGeography.sort}
			</span>
		</p>
		<p>
			<label>中文名称：</label> 
			<span>
				${sysGeography.nameCn}
			</span>
		</p>
		<p>
			<label>中文缩写：</label> 
			<span>
				${sysGeography.nameShortCn}
			</span>
		</p>
		<p>
			<label>英文名称：</label> 
			<span>
				${sysGeography.nameEn}
			</span>
		</p>
		<p>
			<label>英文缩写：</label> 
			<span>
				${sysGeography.nameShortEn}
			</span>
		</p>
		<p>
			<label>拼音：</label> 
			<span>
				${sysGeography.namePinyin}
			</span>
		</p>
		<p>
			<label>拼音缩写：</label> 
			<span>
				${sysGeography.nameShortPinyin}
			</span>
		</p>
		<p>
			<label>交叉栏目：</label> 
			<span>
				${sysGeography.crossSection}
			</span>
		</p>
		<p>
			<label>描述：</label> 
			<span>
				${sysGeography.description}
			</span>
		</p>
		<p>
			<label>创建人：</label> 
			<span>
				${sysGeography.createBy}
			</span>
		</p>
		<p>
			<label>创建时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${sysGeography.createDate}"/>
			</span>
		</p>
		<p>
			<label>修改人：</label> 
			<span>
				${sysGeography.updateBy}
			</span>
		</p>
		<p>
			<label>修改时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${sysGeography.updateDate}"/>
			</span>
		</p>
		<p>
			<label>状态：</label> 
			<span>
				${sysGeography.status}
			</span>
		</p>
		<p>
			<label>删除状态：</label> 
			<span>
				${sysGeography.delFlag}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="返&nbsp;&nbsp;&nbsp;回" class="btn btn-primary gray" onclick="history.go(-1)" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
