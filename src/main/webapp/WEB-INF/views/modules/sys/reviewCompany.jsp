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
		    if(id==null) id="";
			var iframe = "iframe:${ctx}/sys/review/reviewCompanyEdit?reviewcompanyid="+id;			
			if(id!=null){
				//iframe += "&id="+typeId;		
			}
			$.jBox(iframe, {
				    title: "请输入"+$(${param.type}).text()+"信息",
				    width: 340,
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
		<li><a href="${ctx}/sys/role/">角色列表</a></li>
		<shiro:hasPermission name="sys:role:edit"><li><a href="${ctx}/sys/role/form">角色添加</a></li></shiro:hasPermission>
	</content>
	

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

	<div class="tableDiv flight">
	
<table id="contentTable" class="t-type">
		<thead >
			<tr>
				<th width="10%">部门名称</th>	
				<!-- <th width="10%">部门ID</th>	-->		
	           
	            <th width="10%">流程名称</th>
		        <th width="10%">审核配置层级数</th>
		        <th width="10%">审核分配层级数</th>
		      <!--  <th width="10%">角色分配</th>  -->
		        <th width="10%">一个订单能否提交多个审核</th>
			    <th width="10%">操作</th></tr>
		</thead>
	
		<c:forEach items="${companyReviewList}" var="companyDictList">
			<tr>
				
				<td>${companyDictList.deptName}</td>
				<!-- <td>${companyDictList.id}</td> -->
				
				<td>${companyDictList.flowName}</td>
				<td class="tc">${companyDictList.topLevel}</td>
				<td class="tc">${companyDictList.totalLevel}&nbsp;&nbsp;&nbsp; <a target="_blank" href="${ctx}/sys/review/reviewRole?reviewcompanyid=${companyDictList.id}">分配审核职务</a></td>
			  <!--  <td  class="tc">
			      <c:if test="${companyDictList.topLevel >= companyDictList.totalLevel}"> 完成角色分配 </c:if>  
                 <c:if test="${companyDictList.topLevel < companyDictList.totalLevel}"> <font color="red">没有完成角色分配</font> </c:if>
			</td> -->
			  <td class="tc">
     <c:if test="${companyDictList.redo == true}">支持多个审核</c:if>
      <c:if test="${companyDictList.redo == false}">不支持多个审核</c:if>
			  </td>
				<td class="tc">
    				<a href="javascript:void(0)" onClick="addType(${companyDictList.id})">修改</a>&nbsp&nbsp&nbsp&nbsp
					<a href="${ctx}/sys/review/delete?id=${companyDictList.id}" onClick="return confirmx('您确认删除此记录吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		
	</table>
			<c:if test="${empty companyReviewList}">
				<div class="wtjqw">没有该类型的相关数据</div>
			</c:if>
	<div class="t-type-add">
		<a class="ydbz_s" href="#" onClick="addType()">添加审核流程</a>
	</div>
	</div>
	</div>
</body>
</html>