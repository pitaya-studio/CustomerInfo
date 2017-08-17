<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>团期费率管理</title>
	<meta name="decorator" content="wholesaler"/>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
	
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
	<link type="text/css"  rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css" /> 
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<!-- 团期费率相关样式 -->
	<link type="text/css"  rel="stylesheet" href="${ctxStatic}/css/common.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/input.css" />
   <%--  <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/nav.css" /> --%>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/page-T2.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/rateSetting-2.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/search.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/setRate.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/table.css" />
    
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/quauq.base64.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.plus.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/boxScroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/rateSetting2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/search.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/agentToOffice/GroupRateStrategyList.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
</head>
<body>
	<content tag="three_level_menu" id="threeLevelMenu" class="bottom">
		 <div class="bottom">
	       <span class="bottom-first">费率管理<strong>——</strong>${company.name } </span>
	    </div> 
    </content>
           
    <div class="table_external">
		<ul class="tab-switch" id="activityKind">
			<c:forEach items="${ activityKinds}" var="aKind">
			    <c:if test="${aKind.activityKind == '2' }"><li <c:if test="${activityKind == 2 || empty activityKind }">class="select-tab"</c:if> ><input type="hidden" value="2"><a href="#">散拼</a></li></c:if>
			    <c:if test="${aKind.activityKind == '1' }"><li><input type="hidden"><a href="#">单团</a></li></c:if>
			   	<c:if test="${aKind.activityKind == '6' }"> <li><input type="hidden"><a href="#">签证</a></li></c:if>
			   	<c:if test="${aKind.activityKind == '5' }"> <li><input type="hidden"><a href="#">自由行</a></li></c:if>
		   		<c:if test="${aKind.activityKind == '3' }"> <li><input type="hidden"><a href="#">游学</a></li></c:if>
	   			<c:if test="${aKind.activityKind == '4' }"> <li><input type="hidden"><a href="#">大客户</a></li></c:if>
		    </c:forEach>
		</ul>
		<form id="queryList" method="post" action="${ctx }/group/strategy/GroupRateStrategyList"  style="display: none;" accept-charset="utf-8">
            	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
				<div>
				<input type="hidden" name="companyId" value="${company.id }"/>
				<input type="hidden" name="activityKind" value="${activityKind }"/>
				<input type="hidden" name="nameCode" value="${activityName }"/>
            	<input type="hidden" name="contentWrap1" value="${contentWrap1 }"/><!-- 出发城市 -->
    			<input type="hidden" name="contentWrap2" value="${contentWrap2 }"/><!-- 线路 -->
    			<input type="hidden" name="contentWrap3" value="${contentWrap3 }"/>
    		<%-- 	<input type="hidden" name="travelType" value="${travelType }"/>
    			<input type="hidden" name="activityType" value="${activityType }"/>
    			<input type="hidden" name="activityLevel" value="${activityLevel }"/> --%>
    			<input type="hidden" name="orderByQuauqPrice" value="${orderByQuauqPrice }"/>
    			<input type="hidden" name="orderByIndustryPrice" value="${orderByIndustryPrice }"/>
    			<input type="hidden" name="orderByDate" value="${orderByDate }"/>
    			<input type="hidden" name="departureDate" value="${departureDate }"/>
    			<input type="hidden" name="startDate" value="${startDate }"/>
    			<input type="hidden" name="endDate" value="${endDate }"/></div>
            </form>
            <div class="search-content">
                <input id="nameCode" name="nameCode" type="text" class="input_name" value="${activityName }" flag="istips">
                <span class="ipt-tips" style="left: 38px;top: 111px;">请输入产品名称、团号</span>
                <div class="search-btn" onclick="queryList();"><em class="fa fa-search"></em> 搜索</div>
                <span class="clear-all">清空所有条件</span>
            </div>
            <div class="search-container">
                <dl class="search-item">
                    <dt>出发城市</dt>
                    <dd>
                        <div class="items-container" name="contentWrap1" id="contentWrap1">
                            <span <c:if test="${empty contentWrap1}">class="selected"</c:if> ><input type="hidden">全部</span>
                            <c:forEach items="${fromAreas }" var="fromArea">
                            	<span <c:if test="${contentWrap1 == fromArea.fromArea}">class="selected"</c:if>>
                            		<input type="hidden" value=" ${fromArea.fromArea }">
	                                ${fromArea.label }
                               </span>
                            </c:forEach>
                            <em class="more-item">更多<i class="fa fa-caret-down" aria-hidden="true"></i></em>
                        </div>
                    </dd>
                </dl>
                <dl class="search-item">
                    <dt>目的地</dt>
                    <dd>
                        <div class="items-container" id="contentWrap3" name="contentWrap3">
                            <span <c:if test="${empty contentWrap3}">class="selected"</c:if> ><input type="hidden">全部</span>
                             <c:forEach items="${destinations }" var="destination">
                            	<span <c:if test="${contentWrap3 == destination.id }">class="selected"</c:if>>
                            		 <input type="hidden" value="${destination.id }">
                            		 ${destination.name }
                            	</span>
                            </c:forEach>
                            <em class="more-item">更多<i class="fa fa-caret-down" aria-hidden="true"></i></em>
                        </div>
                    </dd>
                </dl>
                <dl class="search-item">
                    <dt>抵达城市</dt>
                    <dd>
                        <div class="items-container" name="contentWrap2" id="contentWrap2">
                            <span <c:if test="${empty contentWrap2}">class="selected"</c:if>><input type="hidden">全部</span>
                            <c:forEach items="${targetAreas }" var="targetArea">
                            	<span <c:if test="${contentWrap2 == targetArea.targetAreaId }">class="selected"</c:if>>
                            		 <input type="hidden" value="${targetArea.targetAreaId }">
                            		 ${targetArea.name }
                            	</span>
                            </c:forEach>
                            <em class="more-item">更多<i class="fa fa-caret-down" aria-hidden="true"></i></em>
                        </div>
                    </dd>
                </dl>
                <dl class="search-item">
                    <dt>出团日期</dt>
                    <dd  id="departureDate" class="last-item">
                        <div class="items-container" id="departureDate">
                            <span <c:if test="${empty departureDate}">class="selected"</c:if>><input type="hidden">全部</span>
                            <span <c:if test="${departureDate == '1'}">class="selected"</c:if>><input type="hidden" value="1">1月</span>
                            <span <c:if test="${departureDate == '2'}">class="selected"</c:if>><input type="hidden" value="2">2月</span>
                            <span <c:if test="${departureDate == '3'}">class="selected"</c:if>><input type="hidden" value="3">3月</span>
                            <span <c:if test="${departureDate == '4'}">class="selected"</c:if>><input type="hidden" value="4">4月</span>
                            <span <c:if test="${departureDate == '5'}">class="selected"</c:if>><input type="hidden" value="5">5月</span>
                            <span <c:if test="${departureDate == '6'}">class="selected"</c:if>><input type="hidden" value="6">6月</span>
                            <span <c:if test="${departureDate == '7'}">class="selected"</c:if>><input type="hidden" value="7">7月</span>
                            <span <c:if test="${departureDate == '8'}">class="selected"</c:if>><input type="hidden" value="8">8月</span>
                            <span <c:if test="${departureDate == '9'}">class="selected"</c:if>><input type="hidden" value="9">9月</span>
                            <span <c:if test="${departureDate == '10'}">class="selected"</c:if>><input type="hidden" value="10">10月</span>
                            <span <c:if test="${departureDate == '11'}">class="selected"</c:if>><input type="hidden" value="11">11月</span>
                            <span <c:if test="${departureDate == '12'}">class="selected"</c:if>><input type="hidden" value="12">12月</span>
                    <span class="date-text">
                        <input type="text" class="dateinput" id="startDate" onclick="WdatePicker();" value="${startDate }" onchange="changeDate(this,'startDate')">
                        至
                        <input type="text" class="dateinput" id="endDate" onclick="WdatePicker();" value="${endDate }"  onchange="changeDate(this,'endDate')">
                    </span>
                        </div>
                    </dd>
                </dl>
                <!--需要收起和展开的搜索条件添加name属性-->
                <%-- <dl name="hideItem" class="search-item display-none">
                    <dt>旅游类型</dt>
                    <dd>
                        <div class="items-container">
                            <span class="selected">全部</span>
                           <c:forEach items="${travelTypes }" var="travelType">
                           	<span>
                           		<input type="hidden" value=" ${travelType.traveltypeid }">
                           		${travelType.traveltypename }
                           	</span>
                           </c:forEach> 
                            <span>无</span>
                        </div>
                    </dd>
                </dl>--%>
               <%--  <dl name="hideItem" class="search-item display-none">
                    <dt>产品类型</dt>
                    <dd>
                        <div class="items-container">
                            <span class="selected">全部</span>
	                           <c:forEach items="${activityTypes }" var="activityType">
	                           <span><input type="hidden" value="${activityType.activitytypeid }">${activityType.activitytypename }</span>
	                           </c:forEach>
                            <span>无</span>
                        </div>
                    </dd>
                </dl> --%>
                <span class="oper-condition" style="display: none;">更多条件<i class="fa fa-caret-down" aria-hidden="true"></i></span>
            </div>
           
            <div class="table-sort">
                <!--align-left排列在左边的元素-->
                <span class="align-left no-select"><input type="checkbox" class="v-align"  id="selectAll">  全选</span>
                <span class="align-left" id="selectElse">反选</span>
                <span  class="align-left batch-setting"><a  href="javascript:void()"  onclick="batchSetGroupRate('${ctx }/group/strategy/targetToBatchGrouprate','${company.uuid}',${activityKind });" >批量设置费率</a></span>
                <style>
                </style>
                <span class="up_and_down" id="orderByQuauqPrice" >QUAUQ价 <span ><i class="fa fa-sort-asc <c:if test="${orderByQuauqPrice == 'asc'}">order_selected</c:if>" ></i><i class="fa fa-sort-desc <c:if test="${orderByQuauqPrice == 'desc'}">order_selected</c:if>"></i></span></span>
                <span class="up_and_down" id="orderByIndustryPrice" >同行价 <span ><i class="fa fa-sort-asc <c:if test="${orderByIndustryPrice == 'asc'}">order_selected</c:if>"></i><i class="fa fa-sort-desc <c:if test="${orderByIndustryPrice == 'desc'}">order_selected</c:if>"></i></span></span>
                <span class="up_and_down" id="orderByDate" >出团日期 <span ><i class="fa fa-sort-asc <c:if test="${orderByDate == 'asc'}">order_selected</c:if>"></i><i class="fa fa-sort-desc <c:if test="${orderByDate == 'desc'}">order_selected</c:if>"></i></span></span>
                <span class="no-select">查询结果<strong>${page.count }</strong>条</span>
                <span class="simple-page no-select">
                 <c:if test="${page.pageNo == 1 }"><i class="fa fa-angle-left" aria-hidden="true" ></i></c:if>
                     <c:if test="${page.pageNo != 1 }"><i class="fa fa-angle-left i-weight" aria-hidden="true" onclick="javascript:page(${page.prev },${page.pageSize });"></i></c:if>
                    <em>${currentPage }</em>/<em>${totalPage }</em>
                    <c:if test="${page.pageNo == totalPage}"> <i class="fa fa-angle-right" aria-hidden="true"></i></c:if>
                    <c:if test="${page.pageNo != totalPage }"> <i class="fa fa-angle-right i-weight" aria-hidden="true" onclick="javascript:page(${page.next },${page.pageSize });"></i></c:if><!--i-weight加粗显示的样式-->
                </span>
            </div>
            <table class="table_third">
                <thead>
                <tr>
                    <th class="table_per_7">序号</th>
                    <th class="table_per_21">产品名称</th>
                    <th class="table_per_9">出团日期</th>
                    <th class="table_per_13">同行价</th>
                    <th class="table_per_13">定价策略</th>
                    <th class="table_per_13">QUAUQ价</th>
                    <th class="table_per_13">供应价</th>
                    <th class="table_per_8">操作</th>
                </tr>
                </thead>
                <tbody id="listContent">
                <c:if test="${!empty page.list }">
	                     			<c:forEach items="${page.list }" var="activityGroup" varStatus="status">
                <tr>
                    <td><input type="checkbox" name="indexBox" class="table_checkbox" value="${activityGroup.id }"> ${status.index+1 }</td>
                    <td>${activityGroup.acitivityName }<br>
                        <span class="group-num">团号：${activityGroup.groupCode }</span>
                    </td>
                    <td>${activityGroup.groupOpenDate }</td>
                    <td>
                        <span>成人:</span>${activityGroup.settlementAdultPrice }<br>
                        <span>儿童:</span>${activityGroup.settlementcChildPrice }<br>
                        <span>特殊人群:</span>${activityGroup.settlementSpecialPrice }
                    </td>
                    <td>
                        <span>成人:</span>${activityGroup.adultPricingStrategy eq ''?'-':activityGroup.adultPricingStrategy}<br>
                        <span>儿童:</span>${activityGroup.childrenPricingStrategy eq ''?'-':activityGroup.childrenPricingStrategy}<br>
                        <span>特殊人群:</span>${activityGroup.specialPricingStrategy eq ''?'-':activityGroup.specialPricingStrategy}
                    </td>
                    <td>
                        <span>成人:</span>${activityGroup.quauqAdultPrice }<br>
                        <span>儿童:</span>${activityGroup.quauqChildPrice }<br>
                        <span>特殊人群:</span>${activityGroup.quauqSpecialPrice }
                    </td>
                    <td>
                        <span>成人:</span>${activityGroup.supplyAdultPrice }<br>
                        <span>儿童:</span>${activityGroup.supplyChildPrice }<br>
                        <span>特殊人群:</span>${activityGroup.supplySpecialPrice } 
                    </td>
                    <td>
                        <a href="${ctx }/group/strategy/getNotSetStrategyAgentList?companyUuid=${company.uuid }&productType=${activityGroup.activity_kind}&groupId=${activityGroup.id}"  target="_blank"  class="opera-text">设置费率</a>
                    </td>
                </tr>
               </c:forEach></c:if> </tbody>
            </table>
            <!--分页开始-->
			<div class="pagination clearFix" style="margin-right: 35px;">
				${page}
			</div>
        </div>
</body>
</html>