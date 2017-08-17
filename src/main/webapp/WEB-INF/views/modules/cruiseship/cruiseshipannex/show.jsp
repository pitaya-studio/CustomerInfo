<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>CruiseshipAnnex信息</title>
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
	<div class="ydbz_tit pl20">CruiseshipAnnex信息</div>
	<div class="maintain_add">
		
		<p>
			<label>uuid：</label> 
			<span>
				${cruiseshipAnnex.uuid}
			</span>
		</p>
		<p>
			<label>主表ID：</label> 
			<span>
				${cruiseshipAnnex.mainUuid}
			</span>
		</p>
		<p>
			<label>附件表ID：</label> 
			<span>
				${cruiseshipAnnex.docId}
			</span>
		</p>
		<p>
			<label>类型。1:游轮；2：游轮库存；：</label> 
			<span>
				${cruiseshipAnnex.type}
			</span>
		</p>
		<p>
			<label>批发商id：</label> 
			<span>
				${cruiseshipAnnex.wholesalerId}
			</span>
		</p>
		<p>
			<label>文件的名称：</label> 
			<span>
				${cruiseshipAnnex.docName}
			</span>
		</p>
		<p>
			<label>存放文件的路径：</label> 
			<span>
				${cruiseshipAnnex.docPath}
			</span>
		</p>
		<p>
			<label>上传的文件类型：</label> 
			<span>
				${cruiseshipAnnex.docType}
			</span>
		</p>
		<p>
			<label>创建人：</label> 
			<span>
				${cruiseshipAnnex.createBy}
			</span>
		</p>
		<p>
			<label>创建时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${cruiseshipAnnex.createDate}"/>
			</span>
		</p>
		<p>
			<label>修改人：</label> 
			<span>
				${cruiseshipAnnex.updateBy}
			</span>
		</p>
		<p>
			<label>修改时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${cruiseshipAnnex.updateDate}"/>
			</span>
		</p>
		<p>
			<label>删除标识：</label> 
			<span>
				${cruiseshipAnnex.delFlag}
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
