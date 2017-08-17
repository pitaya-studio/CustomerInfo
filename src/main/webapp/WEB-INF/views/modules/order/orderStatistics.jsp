<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>订单统计</title>
<meta name="decorator" content="wholesaler"/>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>


<script type="text/javascript">

$(function(){
//	added for 展开搜索 at 20170223 by tlw start.
	launch();
//	added for 展开搜索 at 20170223 by tlw end.
    $(document).scrollLeft(0);
	$("#salerId").val("${salerId}");
	$("#deptSelect").val("${departmentId}");
    
	<%--    前端js效果部分--%>

	//如果展开部分有查询条件的话，默认展开，否则收起
	var salerId = $("#salerId").val();
	var deptSelect = $("#deptSelect").val();
	if(salerId != "" || deptSelect !="") {
		$('.zksx').click();
	}
});
   
//刷新 
function refresh(){
  setTimeout(location.reload(true),10000);   
}

$(function(){
    $.fn.datepicker=function(option){
        var opt = {}||option;
        this.click(function(){
           WdatePicker(option);            
        });
    }
    
    $("#startDate").datepicker({
        dateFormat:"yy-mm-dd",
       dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
       closeText:"关闭", 
       prevText:"前一月", 
       nextText:"后一月",
       monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
       });
    
    $("#endDate").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
           
   $("#orderTimeBegin").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
     $("#orderTimeEnd").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
    
});

//改变搜索部门分组
function changeDeptId() {
	var departmentId = $("#deptSelect").val();
	$("#departmentId").val(departmentId);
	if(departmentId == "") {
		departmentId = $("#tempDepartmentId").val();
	}
	var isParentsAndChildren = $("#isParentsAndChildren").val();
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
            	$("#salerId").val("");
            } else {
            	var salerArr = eval(msg);
            	$(".activitylist_bodyer_right_team_co3").show();
            	$("#salerId").empty();
            	
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

/**
 * 订单查询
 * @param orderStatus 订单状态
 * @param orderType 订单类型
 * @param orderOrGroup 订单或者团期
 */
function getOrderList(orderStatus, orderType, orderOrGroup) {
	$('#searchForm').attr("action", contextPath + "/orderList/manage/showOrderList/" + orderStatus + "/" + orderType + ".htm?orderOrGroup=" + orderOrGroup);
	$('#searchForm').submit();
}

//点击部门分区
function getDepartment(departmentId) {
	$("#departmentId").val(departmentId);
	$("#salerId").val('');
	$("#searchForm").submit();
}

</script>

</head>
<body>
<style type="text/css">label{ cursor:inherit;}</style>
	<c:set var="orderTypeStr" value=""></c:set>
	<c:choose>
		<c:when test="${orderStatus==1}"><c:set var="orderTypeStr" value="single"></c:set></c:when>
		<c:when test="${orderStatus==2}"><c:set var="orderTypeStr" value="loose"></c:set></c:when>
		<c:when test="${orderStatus==3}"><c:set var="orderTypeStr" value="study"></c:set></c:when>
		<c:when test="${orderStatus==4}"><c:set var="orderTypeStr" value="bigCustomer"></c:set></c:when>
		<c:when test="${orderStatus==5}"><c:set var="orderTypeStr" value="free"></c:set></c:when>
	</c:choose>
	<!-- 顶部参数 -->
    <page:applyDecorator name="order_op_head" >
        <page:param name="showType">statistics</page:param>
       	<page:param name="orderStatus">${orderStatus}</page:param>
    </page:applyDecorator>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
	<form method="post" id="searchForm" action="${ctx}/orderStatistics/manage/orderStatistics/${orderStatus}.htm">
		<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
		<!-- 没有特别用途，只是保存当前显示部门ID -->
		<input id="tempDepartmentId" name="tempDepartmentId" type="hidden" value="${departmentId }" />
		<!-- 没有特别用途，只是用来标识是否是页面主动查询 -->
		<input id="dateFlag" name="dateFlag" type="hidden" value="true" />
		<!-- 标记显示区域是否是父子关系区域 -->
		<input id="isParentsAndChildren" name="isParentsAndChildren" type="hidden" value="${isParentsAndChildren}" />
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr">
				<label>订单日期：</label><input readonly="" value='${startDate}' onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('endDate').value==''){$dp.$('endDate').value=vvv;}}})"  value="" name="startDate" class="inputTxt radius4 dateinput" id="startDate">
				<span>至</span>
				<input readonly="" value='${endDate}' onclick="WdatePicker()"  value="" name="endDate" class="inputTxt radius4 dateinput" id="endDate">
			</div>
			<c:if test="${not empty showAreaList}">
			<div class="zksx filterButton_solo">筛选</div>
			</c:if>
			<div class="form_submit">
				<input type="submit" value="搜索" id="seachbutton" class="btn btn-primary ydbz_x">
			</div>

			<div style="display: none;" class="ydxbd">
				<span></span>
				<c:if test="${not empty showAreaList}">
					<div class="activitylist_bodyer_right_team_co3">
						<label class="activitylist_team_co3_text">部门分组：</label>
						<div class="selectStyle">
							<select id="deptSelect" onchange="changeDeptId()">
								<option value="">全部</option>
								<c:forEach var="department" items="${showAreaList}" varStatus="status">
									<c:if test="${not empty department.parent}">
										<option value="${department.id}">${department.name}</option>
									</c:if>
								</c:forEach>
							</select>
						</div>
					</div>
				</c:if>
				<c:if test="${not empty salerList}">
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">销售：</label>
						<div class="selectStyle">
							<select id="salerId" name="salerId">
								<option value="">全部</option>
								<c:forEach var="saler" items="${salerList}" varStatus="status">
									<option value="${saler.id}">${saler.name}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</c:if>
			</div>
			<div class="kong"></div>
		</div>
	</form>
	
    <!-- 部门分区 -->
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


	<c:if test="${empty orderMap }">
		<div class="ydbz_tit">

		</div>
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th width="8%">姓名</th>
	                <th width="9%">团期</th>
	                <th width="15%">渠道</th>
	                <th width="20%">产品名称</th>
	                <th width="10%">产品类型</th>
	                <th width="18%">确认人数</th>
	                <th width="18%">预报人数</th>
				</tr>
			</thead>
			<tbody>
	              <tr class="fbold">
	                <td colspan="5">总计：</td>
	                <td class="tr">确认人数：0</td>
	                <td class="tr">预报人数：0</td>
	              </tr> 
	        </tbody>
		</table>
	</c:if>

	<c:forEach var="map" items="${orderMap}" varStatus="status">
		<div class="ydbz_tit">${map.key}</div>
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th width="8%">姓名</th>
	                <th width="9%">团期</th>
	                <th width="15%">渠道</th>
	                <th width="20%">产品名称</th>
	                <th width="10%">产品类型</th>
	                <th width="18%">确认人数</th>
	                <th width="18%">预报人数</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="orderTotal" value="0"></c:set>
				<c:set var="preOrderTotal" value="0"></c:set>
				<c:forEach var="order" items="${map.value}" varStatus="status">
					<c:if test="${fn:contains(order[0],map.key) || map.key eq departmentName || map.key == '数据统计'}">
						<c:set var="orderTotal" value="${orderTotal + order[5]}"></c:set>
						<c:set var="preOrderTotal" value="${preOrderTotal + order[6]}"></c:set>
						<tr>
			                <td>${order[1]}</td>
			                <td>${order[2]}</td>
			                <td>${order[3]}</td>
			                <td>${order[4]}</td>
			                <td>${orderType}</td>
			                <td class="tr">${order[5]}</td>
			                <td class="tr">${order[6]}</td>
						</tr> 
					</c:if>
				</c:forEach>
				
	              <tr class="fbold">
	                <td colspan="5">总计：</td>
	                <td class="tr">确认人数：${orderTotal }</td>
	                <td class="tr">预报人数：${preOrderTotal}</td>
	              </tr> 
	            </tbody>
		</table>
	</c:forEach>
</div>

</body>
</html>
