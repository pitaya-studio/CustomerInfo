<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色审核配置</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
	/*$(document).ready(function(){
		var active = ${param.type};
		$(active).attr("class","active");
		var title = $(${param.type}).text();
		var splitIndex = title.indexOf("丨");
		if(splitIndex != -1){
			title = title.substring(0,splitIndex);
		}
		$(".ydbz_tit").text(title);
	}); */
	
	function addType(id,cpid) {				
			
			if(id==null) id="";		
			var iframe = "iframe:${ctx}/sys/review/reviewRoleEdit?reviewcompanyid=${reviewcompanyid}&id="+id;		  
			$.jBox(iframe, {
				    title: "${flowName} 审核配置",
				    width: 350,
   					height: 280,
				    buttons: {}
			});
	}	


		
		
		
		
	</script>
    <style type="text/css">
    
	input[type="file"], input[type="image"], input[type="submit"], input[type="reset"], input[type="button"], input[type="radio"], input[type="checkbox"]{ width:72px; height:28px; font-size:14px;}
    </style>
</head>
<body>
    <content tag="three_level_menu">
		<li><a href="${ctx}/sys/role/">角色列表</a></li>
		<shiro:hasPermission name="sys:role:edit"><li><a href="${ctx}/sys/role/form">角色添加</a></li></shiro:hasPermission>
	</content>
	
	<div class="ydbzbox fs">
	<div class="ydbz_tit">${deptName}&nbsp;&nbsp;&nbsp; ${productName}${flowName} 审核配置</div>

	<div class="tableDiv flight">	 
	<table id="contentTable" class="t-type">
		<thead class="destination_title">
		  
		       	<tr>  
				<!--<th width="7%">序号</th> -->				
	            <th width="10%">职务名称</th>
	            <th width="10%">负责审核层级</th>
		       <th width="10%">审核结束标志</th> 
		        <th width="10%">流程名称</th>	

			    <th width="10%">操作</th></tr>
		</thead>
		<tbody>
		<c:forEach items="${reviewList}" var="companyDictList" varStatus="s">
			  <c:if test="${companyDictList.reviewLevel %2 ==1}">	<tr>  </c:if>
			    <c:if test="${companyDictList.reviewLevel %2 ==0}">	<tr style="background-color:honeydew">  </c:if>				
				<!-- <td class="tc">${companyDictList.id}</td> -->
				<td class="tc">${companyDictList.jobName}</td>

				<td class="tc">${companyDictList.reviewLevel}</td>
                <td class="tc">			       
				    <c:if test="${topLevel==companyDictList.reviewLevel || companyDictList.isEnd==1}">	审核结束</c:if>		
				    <c:if test="${topLevel!=companyDictList.reviewLevel && companyDictList.isEnd!=1}">	审核没有结束</c:if>	
				</td> 					 
				<td class="tc">${companyDictList.flowName}</td>		
				<td class="tc">			
					 <a href="#" onClick="addType('${companyDictList.id}','')">更新记录</a>				
					&nbsp;&nbsp;
    				<!--<a href="javascript:void(0)" onClick="addType(${companyDictList.id},${reviewcompanyid})">修改</a>&nbsp&nbsp&nbsp&nbsp -->
    				 <c:if test="${topLevel==companyDictList.reviewLevel || companyDictList.isEnd==1}">
					    <a href="${ctx}/sys/review/deleteRole?id=${companyDictList.id}&reviewcompanyid=${reviewcompanyid}" onClick="return confirmx('删除的同时会将待该层级审核的数据改为已通过，您确认删除此记录吗？', this.href)">删除顶层审核</a>
					</c:if>
					<c:if test="${topLevel!=companyDictList.reviewLevel && companyDictList.isEnd!=1}">
					    <a href="${ctx}/sys/review/deleteRole?id=${companyDictList.id}&reviewcompanyid=${reviewcompanyid}" onClick="return confirmx('您确认删除此记录吗？', this.href)">删除</a>
					</c:if>
    				
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
			<c:if test="${empty reviewList}">
				<div class="wtjqw">没有该类型的相关数据</div>
			</c:if>
			<br> 
	<div class="t-type-addss">
		<table  width="70%"><tr><td width="50%">&nbsp;</td><td><a class="ydbz_s" href="#" onClick="addType('','')">添加审核配置</a>
		</td><td><a class="ydbz_s" href="${ctx}/sys/review/reviewCompany">返回审核列表页</a>
		 </td></tr></table>
		
	</div>
	</div>
	</div>
</body>
</html>