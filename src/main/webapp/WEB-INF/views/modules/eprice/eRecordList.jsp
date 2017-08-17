<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>计调--报价记录</title>
<meta name="decorator" content="wholesaler" />
<%@ include file="commonVar.jsp"%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery/ajaxfileupload.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/modules/eprice/eRecordList.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/modules/eprice/epriceCommon.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>

<script type="text/javascript">
	$(function() {
		//展开筛选
		launch();
		//文本框提示信息显示隐藏
		//inputTips();
		//操作浮框
		operateHandler();
		//成本价滑过显示具体内容
		inquiryPriceCon();
		$("#test").click(function(){
			var url = contextPath+"/eprice/manager/project/exportEstimateExcel";
			$("#eprice-search-form-id").attr("action",url);
			$("#eprice-search-form-id").submit();
		});
		
	});
	//展开收起
	function expand(child, obj, srcActivityId, payMode_deposit,
			payMode_advance, payMode_full, payMode_op, payMode_cw, payMode_data, payMode_guarantee,
			payMode_express) {
		if ($(child).is(":hidden")) {
			var selectdata = $("#agentIdSel").length ? $("#agentIdSel option:selected")
					: $("input[name='agentId']");
			var agentId = selectdata.val();
			if (agentId != null && agentId != "") {
				$.ajax({

				});
			} else {
				$(obj).html("收起");
				$(child).show();
				$(obj).parents("td").addClass("td-extend");
			}
		} else {
			if (!$(child).is(":hidden")) {
				$(child).hide();
				$(obj).parents("td").removeClass("td-extend");
				$(obj).html("展开");
			}
		}
	}
	function getDepartment(departmentId) {
		$("#departmentId").val(departmentId);
		$("#eprice-search-form-id").submit();
	}
</script>
<style type="text/css">
</style>
</head>
<body>

	<page:applyDecorator name="eprice_aop_repay" >
    </page:applyDecorator>		 
		<div class="activitylist_bodyer_right_team_co_bgcolor">
			<form id="eprice-search-form-id">
			<c:if test="${fns:getUser().company.uuid eq '7a81a26b77a811e5bc1e000c29cf2586'}"> 
				<shiro:hasPermission name="eprice:list:dijie:exportexcel">
					<p  class="main-right-topbutt">
						<a id="test" href="javascript:void(0)">导出Excel</a>
					</p>
				</shiro:hasPermission>
			</c:if>
			<input type="hidden" name ="replayType" value="1"/>
				<div style="display: none;" class="lmels-ts">
					<img src="${ctxStatic}/logo/lmels-ts.png">如需预定，请与浪漫俄罗斯相关销售人员联系
					010-52877517；010-52899377；010-52906039；13581525134
				</div>
				<input type="hidden" value="1" name="pageNo" id="pageNo" /> <input
					type="hidden" value="10" name="pageSize" id="pageSize" />
				<input type="hidden" value="" name="orderBy" id="orderBy" />
				<input type="hidden" value="" name="agentId" id="agentId" />
				<div class="activitylist_bodyer_right_team_co">
					<div class="activitylist_bodyer_right_team_co2 pr">
							 <input class="inputTxt inputTxtlong searchInput"
							id="wholeSalerKey" name="keyword" placeholder="销售姓名/线路国家/客户" />
					</div>
					<div class="zksx">筛选</div>
					<div class="form_submit">
						<input type="button" value="搜索" id="eprice-search-form-btn-id" class="btn btn-primary ydbz_x" />
						 <%--<input class="btn ydbz_x" type="reset" value="清空所有条件"/>--%>
                        <input class="btn ydbz_x" type="button"	onclick="resetSearchParams()" value="清空所有条件" />
					</div>
					<div style="display: none;" class="ydxbd">
						<span></span>
						<input type="hidden" name="operatorUid" value="${operatorUid }" />
						<div class="activitylist_bodyer_right_team_co3">
							<div class="activitylist_team_co3_text">销售员：</div>
							<div class="selectStyle">
								<select name="salerId">
									<option value="0">不限</option>
									<c:forEach items="${sellUser}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">询价状态：</div>
							<div class="selectStyle">
								<select name="estimateStatus">
									<option value="-1">全部</option>
									<!-- <option value="6">待分配</option> -->
									<option value="1">待报价</option>
									<option value="2">已报价</option>
									<option value="3">确定报价</option>
									<option value="4">已发产品</option>
									<option value="5">已生成订单</option>
									<option value="0">已取消</option>
								</select>
							</div>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<label>出团日期：</label> <input readonly="readonly"
									 onclick="WdatePicker()"
								value="" name="groupOpenDate" class="inputTxt dateinput"
								id="groupOpenDate"> <span>至</span> <input
								readonly="readonly" onclick="WdatePicker()" value=""
								name="groupCloseDate" class="inputTxt dateinput"
								id="groupCloseDate">
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">线路国家：</div>
							<div class="selectStyle">
							<select name="travelCountryId" id="travel-country-id">
									<option value="-1">不限</option>
									<c:forEach items="${areamapList }" var = "country">
					                	<option value="${country.id }">${country.name }</option>
					                </c:forEach>
							</select>
							</div>
						</div>

						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">询价类型：</div>
							<div class="selectStyle">
								<select name="type" id="eprice-type-id">
									<option value="-1">不限</option>
									<option value="1">单团</option>
									<option value="4">大客户</option>
									<option value="3">游学</option>
									<option value="5">自由行</option>
									<!--
                          <option value="-1">全部</option>
                          <option value="1">单团</option>
                          <option value="2">机票</option>
                           -->
								</select>
							</div>
						</div>
						<!-- <div class="activitylist_bodyer_right_team_co2">
							<label>最近询价日期：</label> <input readonly="readonly"
									 onclick="WdatePicker()"
								value="" name="epriceStartDate" class="inputTxt dateinput"
								id="epriceStartDate"> <span>至</span> <input
								readonly="readonly" onclick="WdatePicker()" value=""
								name="epriceEndDate" class="inputTxt dateinput"
								id="epriceEndDate">
						</div> -->
					</div>
				</div>
			<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
			</form>


<!-- 部门分区 -->
	<form:form id="searchForm" modelAttribute="estimatePriceProject" action="" method="post" >
	    <div class="supplierLine" <c:if test="${empty showAreaList || fn:length(showAreaList) == 1}">style="display: none;"</c:if>>
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
	</form:form>

		<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul id="sort-list-ul-id">
						<li class="activitylist_paixu_left_biankuang liid"
							sortc="lastEstimatePriceTime" sortv="1"><a>询价时间<i
								class="icon" style="display: none;"></i></a></li>
						<li class="activitylist_paixu_left_biankuang liupdateDate"
							sortc="lastOperatorGivenTime" sortv="1"><a>报价时间<i
								class="icon" style="display: none;"></i></a></li>
						<li
							class="activitylist_paixu_left_biankuang lisettlementAdultPrice"  style="width:100px"
							sortc="lastCreateProductTime" sortv="1"><a>生成产品时间<i
								class="icon" style="display: none;"></i></a></li>
						<li class="activitylist_paixu_left_biankuang "
							sortc="lastCancelTime" sortv="1"><a>取消时间<i class="icon"
								style="display: none;"></i></a></li>
					</ul>
				</div>
				<div class="activitylist_paixu_right">
					查询结果&nbsp;&nbsp;<strong id="eprice-list-count-id">0</strong>&nbsp;条
				</div>
			</div>
		</div>
		<form method="post" name="groupform" id="groupform">
			<table class="table mainTable activitylist_bodyer_table"
				id="eprice-project-list-id">
				<thead style="background: #403738">
					<tr>
						<th width="5%">编号</th>
						<th width="10%">销售员</th>
						<th width="10%">询价客户</th>
						<shiro:hasPermission name="enquiry:agentinfo:visibility">
							<th width="8%">客户联系人<input type="hidden" id="agentinfo_visibility" value="1" ></th>
						</shiro:hasPermission>
						<th width="10%">询价类型</th>
						<th width="10%">最近询价日期</th>
						<th width="10%">最近报价日期</th>
						<th width="9%">询价次数</th>
						<th width="8%">状态</th>
						<th width="10%">操作</th>
					</tr>
				</thead>
				<tbody>


				</tbody>
			</table>

			<input type="hidden" value="admin" id="tempUserName">
		</form>

		<div class="pagination" id="eprice-project-list-page-id"></div>
		</div>
		<!--右侧内容部分结束-->

</body>
</html>