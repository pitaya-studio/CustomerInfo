<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>MtourInterfaceLog信息</title>
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
	<div class="ydbz_tit pl20">MtourInterfaceLog信息</div>
	<div class="maintain_add">
		
		<p>
			<label>uuid：</label> 
			<span>
				${mtourInterfaceLog.uuid}
			</span>
		</p>
		<p>
			<label>操作IP地址：</label> 
			<span>
				${mtourInterfaceLog.remoteAddr}
			</span>
		</p>
		<p>
			<label>用户代理：</label> 
			<span>
				${mtourInterfaceLog.userAgent}
			</span>
		</p>
		<p>
			<label>请求URI：</label> 
			<span>
				${mtourInterfaceLog.requestUri}
			</span>
		</p>
		<p>
			<label>操作方式：</label> 
			<span>
				${mtourInterfaceLog.method}
			</span>
		</p>
		<p>
			<label>操作提交的数据：</label> 
			<span>
				${mtourInterfaceLog.params}
			</span>
		</p>
		<p>
			<label>操作返回的数据：</label> 
			<span>
				${mtourInterfaceLog.response}
			</span>
		</p>
		<p>
			<label>异常信息：</label> 
			<span>
				${mtourInterfaceLog.exception}
			</span>
		</p>
		<p>
			<label>创建人：</label> 
			<span>
				${mtourInterfaceLog.createBy}
			</span>
		</p>
		<p>
			<label>创建时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${mtourInterfaceLog.createDate}"/>
			</span>
		</p>
		<p>
			<label>修改人：</label> 
			<span>
				${mtourInterfaceLog.updateBy}
			</span>
		</p>
		<p>
			<label>创建时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${mtourInterfaceLog.updateDate}"/>
			</span>
		</p>
		<p>
			<label>删除标识：</label> 
			<span>
				${mtourInterfaceLog.delFlag}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
