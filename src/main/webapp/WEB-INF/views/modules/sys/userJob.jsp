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
			//var iframe = "iframe:${ctx}/sys/userDept/userJobEdit?reviewcompanyid=${reviewcompanyid}&id="+id;
			var iframe = "iframe:${ctx}/sys/userDept/userJobEdit?userdeptid=${userdeptid}&userid=${userid}";		  
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
        <li><a href="${ctx}/sys/profile/info">个人信息</a></li>
        <li><a href="${ctx}/sys/profile/modifyPwd">修改密码</a></li>
		<shiro:hasPermission name="sys:user:view"><li class="active"><a href="${ctx}/sys/user/">账号管理</a></li></shiro:hasPermission>
		<shiro:hasPermission name="sys:user:edit"><li><a href="${ctx}/sys/user/form">账号添加</a></li></shiro:hasPermission>
		<shiro:hasPermission name="transfer:leave:account"><li><a href="${ctx}/sys/user/transferLeaveAccountForm">离职账户转移</a></li></shiro:hasPermission>
	</content>
	
	<div class="ydbzbox fs">
	<div class="ydbz_tit">${deptName}&nbsp;&nbsp;&nbsp; 用户所在部门职位列表</div>

	<div class="tableDiv flight">	 
	<table id="contentTable" class="t-type">
		<thead class="destination_title">
			<tr>
				<th width="10%">职位ID</th>
	            <th width="10%">职位名称</th>
	            <th width="10%">操作</th>		     
		     </tr>
		</thead>
		<tbody>
		<c:forEach items="${jobList}" var="jobList">
			<tr>				
				
				<td class="tc">${jobList.jobid}</td>
				<td class="tc">${jobList.jobName}</td>
				 
				
				<td class="tc">    				
				

				<a href="${ctx}/sys/userDept/deleteJob?jobid=${jobList.jobid}&userdeptid=${userdeptid}&userid=${userid}" onClick="return confirmx('您确认删除此记录吗？', this.href)">删除</a>
				 
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
		
			<br> 
	<div class="t-type-addss">
		<table  width="70%"><tr><td width="50%">&nbsp;</td><td><a class="ydbz_s" href="#" onClick="addType('','')">添加职务</a>&nbsp;&nbsp;&nbsp;
		</td><td><a class="ydbz_s" href="${ctx}/sys/userDept/deptList?userid=${userid}&companyid=${companyid}">用户部门列表</a>
		 </td></tr></table>
		
	</div>
	</div>
	</div>
</body>
</html>