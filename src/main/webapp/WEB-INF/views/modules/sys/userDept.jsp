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
	
	function addType(id) {
		  
		   // if(id==null) id="";
			var iframe = "iframe:${ctx}/sys/userDept/userDeptEdit?userid=${userid}";			
			
			$.jBox(iframe, {
				    title: "添加部门职务",
				    width: 320,
   					height: 300,
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
	 
 
 <!--
 <div class="activitylist_bodyer_right_team_co_bgcolor">	
   <form:form id="searchForm" modelAttribute="visaProducts" action="${ctx}/sys/review/reviewCompany" method="post">
    <div class="activitylist_bodyer_right_team_co">
	  <div class="activitylist_bodyer_right_team_co2 pr wpr20">
                        <label>部门：</label>
                           <select  name="deptid" id="deptid">
                                <option value="" >所有部门</option>
                                <c:forEach items="${deptList}" var="dept">
                                    <option value="${dept.id}"  <c:if test="${deptid eq dept.id}">
                                        selected
                                    </c:if>  >
                                        <c:out value="${dept.name}" /></option>
                                </c:forEach>
                            </select>
                        </div>
                          <div class="form_submit">
                        <input class="btn btn-primary" value="搜索" type="submit">
                    </div> 
                   </div>
          </form:form>
  </div>  
  -->  
<div class="ydbzbox fs">
	
		<div class="ydbz_tit">&nbsp;&nbsp;&nbsp; 用户所在部门职位列表 &nbsp;(用户姓名: &nbsp;${username})</div>
<div class="tableDiv flight">
<table id="contentTable" class="t-type">
		<thead >
			<tr>
				<th width="20%">部门ID</th>	
				<th width="30%">部门名称</th>
				<th width="10%">部门层级</th>	
					<th width="20%">职务</th>
					<th>操作</th>
				</tr>
		</thead>
	
		<c:forEach items="${deptList}" var="deptList">
			<tr>
				
				<td align="center">${deptList.deptId}</td>
				<td align="center">${deptList.deptName}</td>
				<td align="center">${deptList.deptLevel}</td> 
				<td align="center">${deptList.jobName}</td> 			
			    <td class="tc"> 							
					<a href="${ctx}/sys/userDept/delete?id=${deptList.id}&userid=${userid}" onClick="return confirmx('您确认删除此记录吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		
	</table>
	<br>

	<div class="t-type-add">
		<a class="ydbz_s" href="#" onClick="addType()">添加部门职位</a>
	</div>
	</div>
	</div>

</div>
</body>
</html>