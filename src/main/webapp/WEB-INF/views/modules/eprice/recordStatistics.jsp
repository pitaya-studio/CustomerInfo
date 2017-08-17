<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>询价-询价统计</title>
<meta name="decorator" content="wholesaler"/>
<%@ include file="commonVar.jsp"%>
<!--<link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />-->

<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/eprice/recordStatistics.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	//展开筛选
	launch();
	//文本框提示信息显示隐藏
	inputTips();
	//操作浮框
	operateHandler();
	//成本价滑过显示具体内容
	inquiryPriceCon();
	
	//如果展开部分有查询条件的话，默认展开，否则收起
	var salerId = $("#salerId").val();
	var deptSelect = $("#deptSelect").val();
	if(salerId != "" || deptSelect !="") {
		$('.zksx').click();
	}
	
});
//展开收起
		   function expand(child, obj, srcActivityId, payMode_deposit, payMode_advance, payMode_full,payMode_op,payMode_cw,payMode_data,payMode_guarantee,payMode_express) {
            if($(child).is(":hidden")){
                var selectdata = $("#agentIdSel").length ? $("#agentIdSel option:selected") : $("input[name='agentId']");
                var agentId = selectdata.val();
                if(agentId!=null&&agentId!="") {
                    $.ajax({
                        
                    });
                }else{
                	$(obj).html("收起");
					$(obj).parent().parent().addClass('tr-hover');
                    $(child).show();
                    $(obj).parents("td").addClass("td-extend");
                }
            }else{
                if(!$(child).is(":hidden")){
					$(obj).parent().parent().removeClass('tr-hover');
                    $(child).hide();
                    $(obj).parents("td").removeClass("td-extend");
                    $(obj).html("展开");
                }
            }
        }
        
		 //改变搜索部门分组
		   function changeDeptId() {
		   	var departmentId = $("#deptSelect").val();
		   	$("#departmentId").val(departmentId);
		   	if(departmentId == "") {
		   		departmentId = $("#tempDepartmentId").val();
		   	}
		   	$.ajax({
		           type: "POST",
		           url: "${ctx}/orderStatistics/manage/getSalerByDept?dom=" + Math.random(),
		           data: {
		           	departmentId : departmentId,
		           	isParentsAndChildren : isParentsAndChildren
		           },
		           success: function(msg) {
		               if(msg == "") {
		               	$(".activitylist_bodyer_right_team_co3").hide();
		               } else {
		               	var salerArr = eval(msg);
		               	$(".activitylist_bodyer_right_team_co3").show();
		               	$("#salerId").empty();
		             //  	var deptId = $("#departmentId").val();
		              // 	var html = "<option value='" + deptId + "'>全部</option>";
		               	var html = "<option value=''>全部</option>";
		           		$("#salerId").append(html);
		               	$.each(salerArr, function(key,value) {
		               		var html = "<option value='" + value.salerId + "'>" + value.salerName + "</option>";
		               		$("#salerId").append(html);
		               	});
		               }
		           }
		       });
		   }


		   //点击部门分区
		   function getDepartment(departmentId) {
		   	$("#departmentId").val(departmentId);
		   	$("#searchForm").submit();
		   }
</script>

</head>
<body>
	<!-- 
      <ul class="nav nav-tabs" <c:if test='${showType==202 or showType==216}'>nav_current</c:if>>
      	<shiro:hasPermission name="eprice:list:eprices">
      	  	<li <c:if test="${showType==216}">class='active'</c:if>><a href="${ctx}/eprice/manager/project/list4saler">询价记录</a></li>
	      </shiro:hasPermission>
	      <shiro:hasPermission name="eprice:list:recordStatistics">
	      	<li <c:if test="${showType==202}">class='active'</c:if>><a href="${ctx}/eprice/manager/statistics/recordstatistics">询价统计</a></li>
	      </shiro:hasPermission>
      </ul>-->
      
      <page:applyDecorator name="eprice_record_statistics" >
      </page:applyDecorator>
      <!--右侧内容部分开始-->
      	<c:if test="${ departBoolean == 1}">
      		<p>
      			<span> ${theUser.name } 暂未分组，无法显示。如果希望显示，请在权限管理中将 ${theUser.name }  编入相应组中. </span>
      		</p>
      	</c:if>
      	<c:if test="${ departBoolean != 1}">
      	  <form method="post" id="searchForm" >
	          <input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
				<!-- 没有特别用途，只是保存当前显示部门ID -->
				<input id="tempDepartmentId" name="tempDepartmentId" type="hidden" value="${departmentId }" />
				<!-- 没有特别用途，只是用来标识是否是页面主动查询 -->
				<input id="dateFlag" name="dateFlag" type="hidden" value="${dateFlag }" />
				<!-- 标记显示区域是否是父子关系区域 -->
				<input id="isParentsAndChildren" name="isParentsAndChildren" type="hidden" value="${isParentsAndChildren}" />
	          <div class="activitylist_bodyer_right_team_co">
	           <div class="activitylist_bodyer_right_team_co2">
		            <label>询单日期：</label><input readonly=""  onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})"  
		            value="${epsForm.beginTime }" name="groupOpenDate" class="inputTxt dateinput" id="groupOpenDate">
	                <span>至</span>
	                <input readonly="" onclick="WdatePicker()"  
	                value="${epsForm.endTime }" name="groupCloseDate" class="inputTxt dateinput" id="groupCloseDate">
		        </div>
				  <div class="zksx filterButton_solo">筛选</div>
	            <div class="form_submit">
	              <input type="submit" value="搜索" id="seachbutton" class="btn btn-primary ydbz_x">
	            </div>
	            <div class="ydxbd">
					<span></span>
		            <%--<c:if test="${not empty showAreaList}">--%>
			            <div class="activitylist_bodyer_right_team_co3">
			                <label class="activitylist_team_co3_text">部门分组：</label>
							<div class="selectStyle">
								<select id="deptSelect" onchange="changeDeptId()">
								  <option value="">全部</option>
								  <c:forEach var="department" items="${showAreaList }" varStatus="status">
									<c:if test="${not empty department.parent }">

										<c:if test="${epsForm.deptId==department.id}">
											<option value="${department.id }"  selected="selected" >${department.name }</option>
										</c:if>
										<c:if test="${epsForm.deptId!=department.id}">
											<option value="${department.id }">${department.name }</option>
										</c:if>
									</c:if>
								  </c:forEach>
								</select>
							</div>
			              </div>
		            <%--</c:if>--%>
	                <%--<c:if test="${not empty salerList}">--%>
		                <div class="activitylist_bodyer_right_team_co1">
			                <label class="activitylist_team_co3_text">销售：</label>
							<div class="selectStyle">
								<select id="salerId" name="salerId">
									<option value="">全部</option>
									<c:forEach var="saler" items="${salerList}" varStatus="status">
										<c:if test="${epsForm.salerId==saler.id }">
											<option value="${saler.id}"  selected="selected" >${saler.name}</option>
										</c:if>
										<c:if test="${epsForm.salerId!=saler.id }">
											<option value="${saler.id}">${saler.name}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
			              </div>
	                <%--</c:if>--%>
	               
	               <div class="activitylist_bodyer_right_team_co1">
					   <label class="activitylist_team_co3_text">线路国家：</label>
					   <div class="selectStyle">
						   <select name="countryId">
								<option value="">全部</option>
								<c:forEach items="${countryList }"  var="city">
									<c:choose>
										<c:when test="${epsForm.countryId == city.id }">
											<option value="${city.id }"  selected="selected">${city.name }</option>
										</c:when>
										<c:otherwise>
											<option value="${city.id }" >${city.name }</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						   </select>
					   </div>
	               <!-- 
	                <div class="activitylist_team_co3_text">线路国家：</div>
	                <tags:treeselect id="targetArea" name="countryId" value="${epsForm.countryId}" labelName="countryName"  labelValue="${epsForm.countryName}"
                     title="区域" url="/activity/manager/filterTreeData"/>
                      -->
	              </div>
	            </div>
	            <div class="kong"></div>
	            <div class="seach25"></div>
	             <div class="kong"></div>
	            <div class="seach25"></div>
	             <div class="kong"></div>
	            	<c:set var="departmentName" value=""></c:set>
				    <div class="supplierLine" <c:if test="${empty showAreaList || fn:length(showAreaList) == 1}">style="display: none;"</c:if>>
				    	<c:forEach var="department" items="${showAreaList}" varStatus="status">
							<c:choose>
								<c:when test="${(department.id == departmentId) || (empty departmentId && status.index == 0)}">
									<c:set var="departmentName" value="${department.name}"></c:set>
									<a class="select" href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
								</c:otherwise>
							</c:choose>
						</c:forEach>
				    </div>
			    </div>
	        </form>
	        <form method="post">
	        	<c:forEach var="statistics" items="${statisticsList }" varStatus="status">
	        		<div class="ydbz_tit"> ${statistics.depName } </div>
	        		<!-- 统计表 -->
	        		<table class="table table-striped table-bordered"  name="tableStatistics" >
	        			<thead>
	        				<tr>
	        					<th class="tablehead" style=" table-layout: fixed;" nowrap="nowrap">线路国家</th>
	        					<!--  销售列表 -->
	        					<c:if test="${not empty statistics.mapSalers }">
	        						<c:forEach items="${statistics.mapSalers }"  var = "saler"  varStatus="code">
		        						<th class="tablehead" nowrap="nowrap">${saler.name}</th>
		        					</c:forEach>
	        					</c:if>
	        					<c:if test="${empty statistics.mapSalers  }">
	        						<th class="tablehead" nowrap="nowrap">没有任何销售</th>
	        					</c:if>
	        				</tr>
	        			</thead>
	        			<tbody>
	        				<c:if test="${empty statistics.mapStatistic }">
	        					<!-- 查询指定国家统计（该国家无统计结果时使用） -->
	        					<c:if test="${not empty epsForm.countryId }">
	        						<!-- 分组不为空，销售不为空 -->
	        						<c:if test="${not empty statistics.mapSalers }">
	        							<tr>
		        							<td class="tablehead" style=" table-layout: fixed;" >${epsForm.countryName }</td>
		        							<td colspan="${fn:length(statistics.mapSalers) }"  class="none"  style="text-align:center;">该国家暂时没有统计数据</td>
		        						</tr>
	        						</c:if>
	        						<!-- 分组不为空，销售为空 -->
	        						<c:if test="${empty statistics.mapSalers }">
	        							<tr>
		        							<td class="tablehead" style=" table-layout: fixed;" >${epsForm.countryName }</td>
		        							<td colspan="${fn:length(statistics.mapSalers) }"  class="none"  style="text-align:center;">暂时没有统计数据</td>
		        						</tr>
	        						</c:if>	        						
	        					</c:if>
	        					
	        					<!-- 查询指定国家统计（该国家无统计结果时使用） -->
	        					<c:if test="${empty epsForm.countryId }">
	        						<!-- 分组为空，销售不为空 -->
	        						<c:if test="${not empty statistics.mapSalers }">
	        							<tr>
		        							<td class="tablehead" style=" table-layout: fixed;" >${epsForm.countryName }</td>
		        							<td colspan="${fn:length(statistics.mapSalers) }"  class="none"  style="text-align:center;">该国家暂时没有统计数据</td>
		        						</tr>
	        						</c:if>
	        						<!-- 分组为空，销售为空 -->
	        						<c:if test="${empty statistics.mapSalers }">
	        							<tr>
		        							<td class="tablehead" style=" table-layout: fixed;" >${epsForm.countryName }</td>
		        							<td colspan="${fn:length(statistics.mapSalers) }"  class="none"  style="text-align:center;">暂时没有统计数据</td>
		        						</tr>
	        						</c:if>	        						
	        					</c:if>
	        				</c:if>
	        				
	        				<c:if test="${not empty statistics.mapStatistic }">
		        				<!-- 按条件查询国家统计 -->
		        				<c:forEach items="${statistics.mapStatistic }" var ="stat"  varStatus="count">
		        					<tr>
		        						<td class="tablehead" style=" table-layout: fixed;" >${stat.countryName }</td>
		        						<c:forEach var="stic" items="${stat.statisticsList }"  varStatus = "code">
		        							<td class="stat${code.index}" style="text-align:right;">${stic}</td>
		        						</c:forEach>
		        					</tr>
		        				</c:forEach>
		        				<tr>
		        					<th nowrap="nowrap">销售分组总计</th>
		        					<c:forEach items="${statistics.mapStatistic[0].statisticsList}"  varStatus="count">
		        						<td class="count${count.index }" style="text-align:right;">0</td>
		        					</c:forEach>
		        				</tr>
		        				<tr>
		        					<th nowrap="nowrap">销售总计</th>
		        					<td colspan="${fn:length(statistics.mapStatistic[0].statisticsList) }"  class="all"  style="text-align:right;">0</td>
		        				</tr>
	        				</c:if>
	        			</tbody>
	        		</table>
	        	</c:forEach>
	        </form>
      	</c:if>
        
      <!--右侧内容部分结束--> 
</body>
</html>
