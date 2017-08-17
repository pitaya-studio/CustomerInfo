<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<shiro:hasPermission name="looseOrder:book"><c:if test="${showType==1}"><title>预订-散拼</title></c:if>
	<c:if test="${showType==2}"><title>预报名-散拼</title></c:if>
	</shiro:hasPermission>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">
		.ui-front {z-index: 2100;}
		.sort{color:#0663A2;cursor:pointer;}
        #contentTable th {
            height: 40px;
            border-top: 1px solid #CCC;
            }
        #teamTable{
            border:1px solid #CCC;
            }
	</style>
	<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
    <%--t2改版 去掉重复引用的样式 modified by Tlw--%>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/single/activityListForOrder.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/single/calendarForOrder.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/activityCalendar.css" />
	<script type="text/javascript">
	   $(function(){
	       $("#targetAreaId").val("${travelActivity.targetAreaIds}");
    	   $("#targetAreaName").val("${travelActivity.targetAreaNamess}");
    	   $("#activityCreateCalendar").comboboxInquiry();
    	  // renderSelects(selectQuery());
	   });
	   function selectQuery(){
		   $("#activityCreateCalendar").comboboxInquiry();
		}

	</script>
</head>
<body>
    <content tag="three_level_menu">
    	<shiro:hasPermission name="loose:book:order">
    		<li <c:if test="${showType==1}"> class='active' </c:if>><a href="${ctx}/activity/calendarforOrder/list/1/2">预订</a></li>
    		<li <c:if test="${showType==2}"> class='active' </c:if>><a href="${ctx}/activity/calendarforOrder/list/2/2">预报名</a></li>
    	</shiro:hasPermission>
    </content>
    <!-- 签证公告展示 -->
	<%@ include file="/WEB-INF/views/modules/visa/visaPublicBulletinMarquee.jsp"%>
	<div class="xt-activitylist" style="display:none;">
	    <c:if test="${fns:getUser().userType ==3}">
	        <select name="agentId" id="agentIdSel" class="typeSelected" onchange="getSalerByAgentId(this);">

	        	<c:if test="${fns:getUser().company.id ne 68 }">
	            	<option value="-1" <c:if test="${agentId==-1}">selected="selected"</c:if>>非签约渠道</option>
	            </c:if>

	            <c:forEach var="agentinfo" items="${agentinfoList }">
	                <option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
	            </c:forEach>
	        </select>
	    </c:if>
	</div>
	<input id="companyUuid" type="hidden" value="${companyUuid}" />
	<input id="showType" name="showType" type="hidden" value="${showType}" />
	<input id="activityKind" name="activityKind" type="hidden" value="${activityKind}" />
	<input id="orderTypeStr" name="orderTypeStr" type="hidden" value="${orderTypeStr}" />
	<shiro:hasPermission name="calendar${orderTypeStr}:book:addAgent">
		<input id="isAddAgent" name="isAddAgent" type="hidden" value="1" />
	</shiro:hasPermission>
<div class="activitylist_bodyer_right_team_co_bgcolor" >
	<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/activity/calendarforOrder/list/${showType}/${activityKind}" method="post">
		<div class="lmels-ts" style="display: none;"><img src="${ctxStatic}/logo/lmels-ts.png" />如需预定，请与浪漫俄罗斯相关销售人员联系  010-52877517；010-52899377；010-52906039；13581525134</div>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="agentId" name="agentId" type="hidden" value="${agentId }" />
		<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
		<c:set var="companyUuid" value="${fns:getUser().company.uuid}"></c:set>
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr wpr20" >
	            <label>搜索：</label><input name="wholeSalerKey" id="wholeSalerKey" class="txtPro inputTxt" style="width:260px" flag="istips"  value="${wholeSalerKey}"/>
				<span style="display: block;" class="ipt-tips">输入团号、产品名称</span>
	        </div>
	        <div class="form_submit">
	            <input class="btn btn-primary ydbz_x" id="seachbutton" type="button" value="搜索" onclick="calendarSearch(1)"/>
	            <shiro:hasPermission name="looseActivity:downloadYw">
	            	<input class="btn btn-primary" value="下载余位表" type="button" onclick="calendarSearch(2)" title="此处仅支持下载今天（包含）以后出团的团期余位信息">
	            </shiro:hasPermission>
	    	</div>
         	<div class="zksx">筛选</div>
         	<c:if test="${fns:getUser().userType ==1}">
        	    <input type="hidden" name="agentId" class="inputagentId" value="${fns:getUser().agentId}" />
        	</c:if>
	      	<div class="ydxbd" style="display: none;">
		       <div class="activitylist_bodyer_right_team_co3">
					<div class="activitylist_team_co3_text">行程天数：</div>
					<input class="spinner" maxlength="3" name="activityDuration" value="${travelActivity.activityDuration }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
				</div>  
	            <div class="activitylist_bodyer_right_team_co1">
	            <div class="activitylist_team_co3_text">出发地：</div>
	                <form:select path="fromArea" itemValue="key"
	                        itemLabel="value">
	                        <form:option value="">不限</form:option>
	                        <form:options items="${fromAreas}" />
	                </form:select>
	            </div>
	            <div class="activitylist_bodyer_right_team_co2">
						<label>出团日期：</label><input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate"
							value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>'	 onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly />
							<span> 至</span>
							<input id="groupCloseDate" class="inputTxt dateinput"
							name="groupCloseDate"
							value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>' onClick="WdatePicker()" readonly />
					</div>
	            <div class="kong"></div>
	            <div class="activitylist_bodyer_right_team_co1">  
                  	<div class="activitylist_team_co3_text">目的地：</div>
                 	<tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList" labelValue="${targetAreaNames}"
                     title="区域" url="/activity/manager/filterTreeData" checked="true"/>
            	</div>

            	<!-- C323大洋屏蔽 -->
            	<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
            		<div class="activitylist_bodyer_right_team_co1">
		                <div class="activitylist_team_co3_text">航空公司：</div>
						<form:select id="trafficName" path="trafficName" >      
			                <form:option value="" >不限</form:option>
			                <form:options items="${trafficNames}" itemValue="airlineCode" itemLabel="airlineName"/>
		            	</form:select>	            
	            	</div>
	            </c:if>
	            
	            <div class="activitylist_bodyer_right_team_co2">
	                <label>同行价格：</label><input id="settlementAdultPriceStart" maxlength="8" class="inputTxt" name="settlementAdultPriceStart" value="${settlementAdultPriceStart }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
	                	<span> 至</span>
	                <input id="settlementAdultPriceEnd" class="inputTxt" name="settlementAdultPriceEnd" maxlength="8" value="${settlementAdultPriceEnd }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
	            </div>
	            <div class="kong"></div>
	            <!-- C323大洋屏蔽 -->
            	<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
		            <div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">旅游类型：</div>
							<form:select  path="travelTypeId" itemValue="key" itemLabel="value" >      
								<form:option value="">请选择</form:option>
	                       		<form:options items="${travelTypes}" />
							</form:select>
					</div>
		            <div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">产品系列：</div>
							<form:select id="activityLevelId" path="activityLevelId" itemValue="key" itemLabel="value" >
	                           <form:option value="">请选择</form:option>
	                           <form:options items="${productLevels}" />
	                       </form:select>
					</div>
		            <div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">产品类型：</div>
							 <form:select id="activityTypeId" path="activityTypeId" itemValue="key" itemLabel="value" >
		                       <form:option value="">请选择</form:option>
		                       <form:options items="${productTypes}" />
		                   </form:select>
					</div>
		            <div class="kong"></div>
	            	<div class="activitylist_bodyer_right_team_co3">
					<div class="activitylist_team_co3_text">余位：</div>
						<select name="haveYw" id="haveYw">
							<option value="">请选择</option>
							<option value="1" <c:if test="${haveYw=='1'}">selected="selected"</c:if>>有</option>
							<option value="0" <c:if test="${haveYw=='0'}">selected="selected"</c:if>>无</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co3">
					<div class="activitylist_team_co3_text">切位：</div>
						<select name="haveQw" id="haveQw">
							<option value="">请选择</option>
							<option value="1" <c:if test="${haveQw=='1'}">selected="selected"</c:if>>有</option>
							<option value="0" <c:if test="${haveQw=='0'}">selected="selected"</c:if>>无</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">操作员：</label> 
						<select name="activityCreateCalendar"  id="activityCreateCalendar" >
							<option value="-99999">不限</option>
							<c:forEach var="user" items="${userList}">
								<option value="${user.id }"
									<c:if test="${activityCreateCalendar == user.id}">selected="selected"</c:if>>${user.name}</option>
							</c:forEach>
						</select>
					</div>
				</c:if>
          	</div>
          <div class="kong"></div>
        </div>
	</form:form>
	
	<div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu">
			<div class="activitylist_paixu_left">
	            <ul>
		            <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
		            <li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
		            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
		            <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li>
		            <li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
		            <li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
	            </ul>
			</div>
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
	</div>
	
	<!-- 部门分区 -->
	<div class="supplierLine" <c:if test="${empty showAreaList || fn:length(showAreaList) == 1}">style="display: none;"</c:if>>
		<%--<a <c:if test="${empty departmentId}">class="select"</c:if> href="javascript:void(0)" onclick="getDepartment('');">全部产品</a>--%>
		<c:forEach var="department" items="${showAreaList}" varStatus="status">
			<c:choose>
				<c:when test="${(department.id == departmentId) || (empty departmentId && status.index == 0)}">
					<a class="select" href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
				</c:when>
				<c:otherwise>
					<a href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</div>

	<form id="groupform" name="groupform" action="" method="post" >
	
	<%-- <input type="hidden" id="ctxStatic" value="${ctxStatic}"> --%>
	<input type="hidden" id="ctx" value="${ctx}">
	
	<!-- 日历对比内容：开始 -->
	<div id="compareTableContainer" style="margin-top: 5px">
		<div class="operatorContainer">
			<span class="operator">
				<input class="btn btn-primary" value="下载" type="button" onclick="download();">
			</span> 
			<span style="width: 10px; line-height: 30px; margin: 0px 5px; float: left;">|</span> 
			<span class="operator" id="clear">
				<input class="btn btn-primary" value="清空" type="button">
			</span>
		</div>
		<div class="kong"></div>
		<div style="overflow-x:auto;">
			<table id="compareTable" cellspacing="0" align="center" class="table activitylist_bodyer_table" style="min-width:1424px ">
				<thead>
					<tr>
						<th class="tc" rowspan="2" width="55px">团号</th>
						<th class="tc" rowspan="2" width="55px">产品名称</th>
						<th class="tc" rowspan="2" width="87px" name="groupOpenDate" ordertype="asc" firsttime="1">
							<a href="javascript:void(0)" name="sortHref">出/截团日期</a>
							<img src="${ctxStatic}/images/none.png" class="orderCss" />
						</th>
						<th class="tc" rowspan="2" width="87px" name="deadlineDate" ordertype="asc" firsttime="1">
							<a href="javascript:void(0)" name="sortHref">资料截止日期</a>
							<img src="${ctxStatic}/images/none.png" class="orderCss" />
						</th>
						<th class="tc" colspan="3" width="260px" name="convertedSettlementPrice" ordertype="asc" firsttime="1">
							<a href="javascript:void(0)" name="sortHref">同行价</a> 
							<img src="${ctxStatic}/images/none.png" class="orderCss" />
						</th>
						<th class="tc" colspan="3" width="260px" name="convertedSuggestPrice" ordertype="asc" firsttime="1">
							<a href="javascript:void(0)" name="sortHref">直客价</a>
							<img src="${ctxStatic}/images/none.png" class="orderCss" />
						</th>
						<th class="tr" rowspan="2" width="60px">单房差/间夜</th>
						<th class="tc" rowspan="2" width="60px">预报名
						<br />预收</th>
						<th class="tc" rowspan="2" width="65px">售出切位
						<br />已切位</th>
						<th class="tc" rowspan="2" width="45px">余位</th>
						<th class="tc" rowspan="2" width="75px">产品系列</th>
						<th class="tc" rowspan="2" width="55px">出发地</th>
						<th class="tc" rowspan="2" width="65px">航空公司</th>
						<th class="tc" rowspan="2" width="30px">签证国家</th>
						<th class="tc" rowspan="2" width="50px">操作员</th>
						<th class="tc" rowspan="2" width="95px">操作</th>
					</tr>
					<tr>
						<th class="tcc" width="80px" name="customerPrice">成人</th>
						<th class="tc" width="80px">儿童</th>
						<th class="tc" width="80px">特殊人群</th>
						<th class="tc" width="80px" name="channelPrice">成人</th>
						<th class="tc" width="80px">儿童</th>
						<th class="tc" width="80px">特殊人群</th>
					</tr>
				</thead>
				<tbody>
		        </tbody>
			</table>
		</div>
	</div>
    <!-- 日历对比内容：结束 -->
	
	
	<table id="contentTable" class="table activitylist_bodyer_table" >
		<thead style="background:#403738">
			<tr>
				<th width="5%">序号</th>
				<th width="23%">产品名称</th>
				<th width="8%">操作员</th>
				<th width="10%">出/截团日期</th>
				<th width="7%">签证国家</th>
				<th width="10%">出发地</th>
				<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
					<th width="5%">航空</th><!-- C323大洋屏蔽 -->
				</c:if>
				<th width="7%">同行价</th>
<!-- 				<th width="8%">直客价</th> -->
				<th width="15%">最近出团日期</th>
			</tr>
		</thead>
		<tbody>
		<c:if test="${fn:length(page.list) <= 0 }">
                 <tr class="toptr" >
                 <td colspan="8" style="text-align: center;">
                                                    暂无搜索结果
                 </td>
                 </tr>
        </c:if>
        <!-- 循环产品 -->
        <c:forEach items="${page.list}" var="activity" varStatus="s">
            <c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
           	<c:set var="freePositions" value="0"></c:set>
            <c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">
	            <c:if test="${activity.groupOpenDate eq group.groupOpenDate}">
	            	<c:set var="freePositions" value="${freePositions + group.freePosition }"></c:set>
	            </c:if>
	           	<fmt:formatDate value="${group.groupOpenDate}" var="groupOpenDate" pattern="yyyy-MM-dd"/>
	           	<fmt:formatDate value="${group.groupCloseDate}" var="groupCloseDate" pattern="yyyy-MM-dd"/>
	           	<c:set var="visaCountry" value="${group.visaCountry}"></c:set>
            </c:forEach>
            <tr id="parent${s.count}">
            	<input type="hidden" name="activityTypeName" value="${activity.activityLevelName}"/>
            	<input type="hidden" name="activityId" value="${activity.id}"/>
                <td>${s.count}<br/><br/></td>
                <td class="activity_name_td" name="productName">
                    <a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}')">${activity.acitivityName}</a>
                    <c:if test="${not empty activity.activityAirTicket}">
                   		<c:if test="${not empty activity.activityAirTicket.intermodalStrategies[0]}">
	                   		<c:choose>
	                    		<c:when test="${activity.activityAirTicket.intermodalStrategies[0].type == 1}">
		                            <span class="lianyun_name">全国联运</span>
		                        </c:when>
		                        <c:when test="${activity.activityAirTicket.intermodalStrategies[0].type == 2}">
		                            <span class="lianyun_name">分区联运</span>
		                        </c:when>
		                        <c:otherwise>
		                            <span class="lianyun_name">无联运</span>
		                        </c:otherwise>
		                    </c:choose>
	                    </c:if>   
	                </c:if>
                </td>
                <td class="tc" title="电话：${activity.createBy.mobile}" name="operater">
                    ${activity.createBy.name}
                </td>
               	<td class="p0" name="outDate">
               		<input type="hidden" name="groupId" value="${group.id}">
					<div class="out-date">${groupOpenDate}</div>
					<div class="close-date">${groupCloseDate}</div>
			   	</td>
                <td name="visaCountry">
                    ${visaCountry}
                </td>
                <td name="startPlace">${activity.fromAreaName}</td>
                <c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
	                <td name="airComp">
	                	<c:choose>
	                        <c:when test="${activity.activityAirTicket.airlines == '-1'}">
	                        </c:when>
	                        <c:otherwise>
								<label class="qtip" title="${activity.trafficNameDesc}">${activity.activityAirTicket.airlines}</label>
	                        </c:otherwise>
	                    </c:choose>
	                </td>
                </c:if>
                <td class="tr">
                	<c:if test="${activity.settlementAdultPrice==0}">价格待定</c:if>
                	<c:if test="${activity.settlementAdultPrice>0}">${fns:getCurrencyInfo(activity.currencyType,0,'mark')}
                		<span class="tdred fbold">
                			<fmt:formatNumber value="${activity.settlementAdultPrice}"  type="currency" pattern="#,##0.00"/></span>起
               		</c:if>
            	</td>
<!--                 <td class="tr"><c:if test="${activity.suggestAdultPrice==0}">价格待定</c:if><c:if test="${activity.suggestAdultPrice>0}">${fns:getCurrencyInfo(activity.currencyType,1,'mark')}<span class="tdblue fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activity.suggestAdultPrice}" /></span>起</c:if></td> -->
                <td>
                    <c:if test="${groupsize ne 0 }">
                    	<c:choose>
							<c:when test="${activity.groupOpenDate eq activity.groupCloseDate}">${activity.groupOpenDate}</c:when>
							<c:otherwise>${activity.groupOpenDate}</c:otherwise>
						</c:choose>
						(余位：${freePositions})
		                <br/>
		                <c:if test="${showType=='1'}">
		                	<a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${showType})" onMouseenter="if($(this).html()=='团期预定'){$(this).html('展开团期预定')}" onMouseleave="if($(this).html()=='展开团期预定'){$(this).html('团期预定')}">团期预定</a>
	                    </c:if>
	                    <c:if test="${showType=='2'}">
		                	<a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${showType})" onMouseenter="if($(this).html()=='团期预报名'){$(this).html('展开团期预报名')}" onMouseleave="if($(this).html()=='展开团期预报名'){$(this).html('团期预报名')}">团期预报名</a>
	                    </c:if>
                    </c:if>
                    <c:if test="${groupsize == 0 }">日期待定</c:if> 
                </td>
            </tr>
            <tr id="child${s.count}" style="display:none" class="activity_team_top1">
                <td colspan="8" class="team_top" style="background-color:#d1e5f5;">
                   <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
                       <thead>
                           <tr>
                           		<th class="tc" rowspan="2">推荐</th>
								<th class="tc" width="6%" rowspan="2">团号</th>
								<th class="tc" width="8%" rowspan="2">出/截团日期</th>
								<th class="tc" width="8%" rowspan="2">资料截止日期</th>
								<th class="tc" width="18%" colspan="3" class="t-th2">同行价</th>
								<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
									<th class="tc" width="18%" colspan="3" class="t-th2">直客价</th>
								</c:if>
								<th class="tr" width="5%" rowspan="2">订金</th>
								<th class="tr" width="6%" rowspan="2">单房差</th>
								<th class="tc" width="4%" rowspan="2">预收</th>
								<th class="tc" width="7%" rowspan="2"><div class="qiwei-liebiaot">已切位</div><div class="scqiwei-liebiaot">售出切位</div></th>
								<th class="tc" width="4%" rowspan="2">余位</th>
								<th class="tc" width="5%" rowspan="2">预报名</th> 
								<th class="tc" rowspan="2" style="display:none"  width="4%">切位</th>
								<th class="tc" width="3%" rowspan="2">操作</th>
                           </tr>
                           <tr>
								<th class="tr" width="6%">成人</th>
								<th class="tr" width="6%">儿童</th>
								<th class="tr" width="6%">特殊人群</th>
								<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
									<th class="tr" width="6%">成人</th>
									<th class="tr" width="6%">儿童</th>
									<th class="tr" width="6%">特殊人群</th>
								</c:if>
                           </tr>
                       </thead>
                       <tbody>
	                   <c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">
                        <tr>
                        	<input type="hidden" name="groupId" value="${group.id}">
                        	<input type="hidden" name="recommendGroupYN" value="${group.recommend}">
                        	<td>
                           		<c:if test="${group.recommend == 1}">
                           			<input type="checkbox" checked="checked" disabled="true"/>
                           		</c:if>
                           		<c:if test="${group.recommend == 0}">
                           			<input type="checkbox" disabled="true"/>
                           		</c:if>
                         	</td>
                           <td class="word-break-all" name="groupCode">
                            ${group.groupCode}
                           </td>
                           <td class="p0" width="8%" name="groupDate">
								<div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></div>
								<div class="close-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></div>
							</td>
                           <td class="tc" name="deadlineDate">
                               <span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/></span>
                           </td>
                           <td class="tr" name="customerPrice">
                               <c:if test="${not empty group.settlementAdultPrice}">${fns:getCurrencyInfo(group.currencyType,0,'mark')}
                               <span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice }" /></span>
                               <input type="hidden" class="convertedSettlementPrice" value="${fns:currencyConverter4RMB(group.currencyType, 0, group.settlementAdultPrice) }"/>
                               </c:if>
                           </td>
                           <td class="tr" name="customerchildPrice">
                               <c:if test="${not empty group.settlementcChildPrice}">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</c:if><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }" /></span>
                           </td>
                           <td class="tr" name="customerSpecialPrice">
                               <c:if test="${not empty group.settlementSpecialPrice}">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</c:if><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }" /></span>
                           </td>
                           
                           <c:if test="${fns:getUser().company.id ne 98 }"><!-- C323大洋屏蔽 -->
                           	<td class="tr" name="channelPrice">
                               <c:if test="${not empty group.suggestAdultPrice}">${fns:getCurrencyInfo(group.currencyType,3,'mark')}
                               <span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice }" /></span>
							   <input type="hidden" class="convertedSuggestPrice" value="${fns:currencyConverter4RMB(group.currencyType, 3, group.suggestAdultPrice) }"/>
                               </c:if>
                           	</td>
                           	<td class="tr" name="channelchildPrice">
                               <c:if test="${not empty group.suggestChildPrice}">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</c:if><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice }" /></span>
                           	</td>
                           	<td class="tr" name="channelSpecialPrice">
                               <c:if test="${not empty group.suggestSpecialPrice}">${fns:getCurrencyInfo(group.currencyType,5,'mark')}</c:if><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestSpecialPrice }" /></span>
                           	</td>
                           </c:if>
                           
                           <td class="tr">
                               <c:if test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,6,'mark')}</c:if><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></span>
                           </td>
                           <td class="tr" name="roompriceDiff">
                               <c:if test="${not empty group.singleDiff}">${fns:getCurrencyInfo(group.currencyType,7,'mark')}</c:if><span  class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></span>
                           </td>
                           <td class="tc" name="prePay">
                               <span>${group.planPosition }</span>
                           </td>
                           <td class="p0" width="7%" name="positions">
								<div class="out-date">${group.payReservePosition }</div>
								<div class="close-date">${group.soldPayPosition }</div>
							</td>
                           <td class="tc" name="freePositions">
                               <span>${group.freePosition}</span>
                           </td>
	                       <td style="display:none;" class="soldPayPosition${group.id} tc" width="4%">
	                           <span class="tdred" >${group.payReservePosition }</span>
	                       </td>
                           <td class="tc" name="groupPrePersonNum">
                               <span>${group.orderPersonNum }</span>
                           </td>
                           <td class="tc" name="bookeOrder_${group.groupCode}">
                                 <c:if test="${activity.payMode_full=='1'}">
                                     <a style="display:none;" href="javascript:void(0)" class="normalPayType"
                                     	onClick='occupied(${group.id},${activity.id},3,${group.freePosition})' >
                                     	付全款
                                     </a>
                                 </c:if>
                                 <c:if test="${activity.payMode_op=='1'}">
                                     <a style="display:none;" href="javascript:void(0)" class="opPayType"
                                     	onClick='occupied(${group.id},${activity.id},7,${group.freePosition})' >
                                     	计调确认占位
                                     </a>
                                 </c:if>
                                 <c:if test="${activity.payMode_cw=='1'}">
                                     <a style="display:none;" href="javascript:void(0)" class="cwPayType"
                                     	onClick='occupied(${group.id},${activity.id},8,${group.freePosition})' >
                                     	财务确认占位
                                     </a>
                                 </c:if>
                                 <c:if test="${activity.payMode_deposit=='1'}">
                                     <a style="display:none;" href="javascript:void(0)" class="dingjin_PayType"
                                     	onClick='occupied(${group.id},${activity.id},1,${group.freePosition})'>
                                     	订金占位
                                     </a>
                                 </c:if>
                                 <c:if test="${activity.payMode_advance=='1'}">
                                     <a style="display:none;" href="javascript:void(0)" class="yuzhan_PayType"
                                     	onClick='occupied(${group.id},${activity.id},2,${group.freePosition})'>
                                     	预占位
                                     </a>
                                 </c:if>
                                 <c:if test="${activity.payMode_data=='1'}">
                                     <a style="display:none;" href="javascript:void(0)" class="data_PayType"
                                     	onClick='occupied(${group.id},${activity.id},4,${group.freePosition})'>
                                     	资料占位
                                     </a>
                                 </c:if>
                                 <c:if test="${activity.payMode_guarantee=='1'}">
                                     <a style="display:none;" href="javascript:void(0)" class="guarantee_PayType"
                                     	onClick='occupied(${group.id},${activity.id},5,${group.freePosition})'>
                                     	担保占位
                                     </a>
                                 </c:if>
                                 <c:if test="${activity.payMode_express=='1'}">
                                     <a style="display:none;" href="javascript:void(0)" class="express_PayType"
                                     	onClick='occupied(${group.id},${activity.id},6,${group.freePosition})'>
                                     	确认单占位
                                     </a>
                                 </c:if>
                                 <input hidden="hidden" name="bookOrder" value="${group.freePosition},${group.payReservePosition},${group.soldPayPosition},${group.leftdays},${group.settlementAdultPrice},${group.settlementcChildPrice},${activity.id},${group.id}, ${group.leftpayReservePosition}"/>
                                 
                                 <select style="display:none;">
		                            <c:if test="${activity.payMode_full=='1'}">
					            		<option value='3'>全款支付</option>
					            	</c:if>
		                            <c:if test="${activity.payMode_op=='1'}">
					            		<option value='7'>计调确认占位</option>
					            	</c:if>
		                            <c:if test="${activity.payMode_cw=='1'}">
					            		<option value='8'>财务确认占位</option>
					            	</c:if>
					            	<c:if test="${activity.payMode_deposit=='1'}">
					            		<option value='1'>订金占位</option>
					            	</c:if>
					            	<c:if test="${activity.payMode_advance=='1'}">
					            		<option value='2'>预占位</option>
					            	</c:if>
					            	<c:if test="${activity.payMode_data=='1'}">
					            		<option value='4'>资料占位</option>
					            	</c:if>
					            	<c:if test="${activity.payMode_guarantee=='1'}">
					            		<option value='5'>担保占位</option>
					            	</c:if>
					            	<c:if test="${activity.payMode_express=='1'}">
					            		<option value='6'>确认单占位</option>
					            	</c:if>
					             </select>
					             <shiro:hasPermission name="looseActivity:downloadYw">
                                 	<a href="javascript:void(0)" onclick="downloadYw(${group.id});">下载余位表</a>
								</shiro:hasPermission>
                               </td>
                        </tr>
	                   </c:forEach>
	                  </tbody>
                   </table>
	            </td>
	        </tr>
         </c:forEach>
        </tbody>
	</table>
	<c:set var="userinfo" value="${fns:getUser()}"/>
	<input type="hidden" id="tempUserName" value="${userinfo.loginName }"/>
	<!-- 获取批发商是否允许补单的值：0为否，1为是 -->
    <c:set var="isAllowSupplement" value="${userinfo.company.isAllowSupplement}"></c:set>
    <input type="hidden" id="isAllowSupplement" value="${isAllowSupplement}"/>
    </form>
</div>
<div class="pagination clearFix">${page}</div>
<div class="page"> </div>	
<!-- 下载余位 -->
<form id="exportForm" action="${ctx}/activity/managerforOrder/downloadYw" method="post">
	<input type="hidden" id="groupId" name="groupId">
</form>

<!-- 日历div -->
<div class="container_calendar">
    <div class="addAllCompare">
        <div><input id="chk" type="checkbox" autocomplete="off"><label for="chk">全部加入对比</label></div>
    </div>
    <div class="container_month">
        <div class="scollBtn fl" onclick="prevMonth()"><</div>
        <div class='scollBtn fr' onclick="nextMonth()">&gt;</div>
    </div>
    <div class="container_week">
        <span class="week weekend">Sun</span> <span class="week">Mon</span> <span class="week">Tues</span> <span
            class="week">Wed</span> <span class="week">Thur</span> <span class="week">Fri</span> <span
            class="week weekend">Sat</span>
    </div>
    <div id="div_calendar" class="container_day"></div>
</div>
<div class="groupDetail" templete="true">
    <div name="dayNumAddCom"><a>加入对比</a></div>
    <div name="goToBook">
        <div id="freePosition"></div>
        <div id="customerPrice"></div>
        <div id="channelPrice"></div>
    </div>
</div>
<div class="collapseCompare mouseleaved" id="expandCompare">
    <span>展开对比</span>
</div>
	  
</body>
</html>