<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<shiro:hasPermission name="looseOrder:book"><c:if test="${showType==1}"><title>预订-游轮</title></c:if>
	<c:if test="${showType==2}"><title>预报名-游轮</title></c:if>
	</shiro:hasPermission>
	<meta name="decorator" content="wholesaler"/>
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
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
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/single/activityListForOrder.js" type="text/javascript"></script>
	<script type="text/javascript">
	   $(function(){
	       $("#targetAreaId").val("${travelActivity.targetAreaIds}");
    	   $("#targetAreaName").val("${travelActivity.targetAreaNamess}");
    	   //给筛选条件（操作员）注册可输入下拉框
    	  $("#activityCreate").comboboxInquiry();
    	  // renderSelects(selectQuery());
	   });
	   function selectQuery(){
	   		$("#activityCreate" ).comboboxInquiry();
	   	}
	</script>
	<!-- 需求223 -->
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>css/quauq.css" />
    <script type="text/javascript">
    	var $ctx = '${ctx}';
    </script>
    <script type="text/javascript" src="${ctxStatic}/js/quauq.base64.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.plus.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/boxScroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/store/billboard.js"></script>
    <script type="text/javascript">
    	$(document).ready(function() {
    		var $ctx = '${ctx}';
    	});
    	function save(){
    		alert();
    	}
    </script>
<!-- 需求223 -->
</head>
<body>
    <content tag="three_level_menu">
    	<shiro:hasPermission name="loose:book:order">
    		<li <c:if test="${showType==1}"> class='active' </c:if>><a href="${ctx}/activity/managerforOrder/list/1/10">预订</a></li>
    		<c:choose>
    			<c:when test="${youjiaCompanyUuid==companyUuid}">
    				<!-- 如果是优加，则隐藏 -->
    			</c:when>
    			<c:otherwise>
    				<li <c:if test="${showType==2}"> class='active' </c:if>><a href="${ctx}/activity/managerforOrder/list/2/10">预报名</a></li>
    			</c:otherwise>
    		</c:choose>
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
	<shiro:hasPermission name="${orderTypeStr}:book:addAgent">
		<input id="isAddAgent" name="isAddAgent" type="hidden" value="1" />
	</shiro:hasPermission>
	<!-- 需求223 看板 -->
	<shiro:hasPermission name="cruiseshipStockList:stock:bmboard">
		<c:if test="${orderTypeStr eq 'cruise' }">
			<%@ include file="/WEB-INF/views/modules/cruiseship/cruiseshipboard/cruiseshipboard.jsp"%>
		</c:if>
	</shiro:hasPermission>
	<div class="activitylist_bodyer_right_team_co_bgcolor" >
	    
		<!-- 搜索查询 -->
		<%@ include file="/WEB-INF/views/modules/order/forOrder/form.jsp"%>
		<!-- 产品系列 -->
		<%@ include file="/WEB-INF/views/modules/order/forOrder/productType.jsp"%>
		<!-- 部门分区 -->
		<%@ include file="/WEB-INF/views/common/departmentDiv.jsp"%>
		<!-- 产品列表、团期列表 -->
		<%@ include file="/WEB-INF/views/modules/order/forOrder/swith.jsp"%>
		<!-- 排序 -->
		<%@ include file="/WEB-INF/views/modules/order/forOrder/orderByDiv.jsp"%>
		<c:choose>
			<c:when test="${productOrGroup == 'product'}">
				<!-- 产品列表 -->
				<%@ include file="/WEB-INF/views/modules/order/forOrder/looseProductForm.jsp"%>
			</c:when>
			<c:otherwise>
				<!-- 团期列表 -->
				<%@ include file="/WEB-INF/views/modules/order/forOrder/pulleyGroupForm.jsp"%>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="pagination clearFix">${page}</div>
	<div class="page"> </div>	 
</body>
</html>