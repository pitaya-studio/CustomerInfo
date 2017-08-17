<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>基础信息维护-岛屿信息</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	 <script type="text/javascript">
	 function downloads(docId){
        	window.open("${ctx}/sys/docinfo/download/"+docId);
      }
	</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">岛屿信息详情</div>
	<div class="maintain_add">
		<p>
			<label>岛屿名称：</label> 
			<span>
				${island.islandName}
			</span>
		</p>
		<p>
			<label>名称缩写：</label> 
			<span>
				${island.shortName}
			</span>
		</p>
		<p>
			<label>全拼：</label> 
			<span>
				${island.spelling}
			</span>
		</p>
		<p>
			<label>全拼缩写：</label> 
			<span>
				${island.shortSpelling}
			</span>
		</p>
		<p>
			<label>英文名称：</label> 
			<span>
				${island.islandNameEn}
			</span>
		</p>
		<p>
			<label>英文缩写：</label> 
			<span>
				${island.shortNameEn}
			</span>
		</p>
		<p>
			<label>位置：</label> 
			<span>
				<c:choose>
					<c:when test="${island.position == 1}">境内岛屿</c:when>
					<c:when test="${island.position == 2}">境外岛屿</c:when>
				</c:choose>
			</span>
		</p>
		<p>
			<label>国家：</label> 
			<span>
				<trekiz:autoId2Name4Class classzName="SysGeography" sourceProName="uuid" srcProName="nameCn" value="${island.country}"/>
			</span>
		</p>
		<p>
			<label>省：</label> 
			<span>
				<trekiz:autoId2Name4Class classzName="SysGeography" sourceProName="uuid" srcProName="nameCn" value="${island.province}"/>
			</span>
		</p>
		<p>
			<label>市：</label> 
			<span>
				<trekiz:autoId2Name4Class classzName="SysGeography" sourceProName="uuid" srcProName="nameCn" value="${island.city}"/>
			</span>
		</p>
		<p>
			<label>区：</label> 
			<span>
				<trekiz:autoId2Name4Class classzName="SysGeography" sourceProName="uuid" srcProName="nameCn" value="${island.district}"/>
			</span>
		</p>
		<p>
			<label>地址后缀：</label> 
			<span>
				${island.shortAddress}
			</span>
		</p>
		<p>
			<label>岛屿类型：</label> 
			<span>
				<trekiz:defineDict name="type" type="islands_type" className="required" defaultValue="${island.type}" readonly="true" />
			</span>
		</p>
		<p>
			<label>岛屿主题：</label> 
			<span>
				<trekiz:defineDict name="topic" type="islands_topic" input="checkbox" className="required" defaultValue="${island.topic}" readonly="true" />
			</span>
		</p>
		<p>
			<label>上岛方式：</label> 
			<span>
				<trekiz:defineDict name="islandWay" type="islands_way" input="checkbox" className="required" defaultValue="${island.islandWay}" readonly="true" />
			</span>
		</p>
		<p>
			<label>排序：</label> 
			<span>
				${island.sort}
			</span>
		</p>
		<p>
			<label>描述：</label> 
			<span>
				${island.description}
			</span>
		</p>
		<p>
			<label>上传的资料：</label> 
			<c:forEach items="${prodSchList}" var="fileprod">
					<i class="sq_bz_icon"></i>
					<a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${fileprod.docId})">${fileprod.docName }</a></br>
					<!--<i class="del_fj_icon"></i>-->
			</c:forEach>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
		
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
