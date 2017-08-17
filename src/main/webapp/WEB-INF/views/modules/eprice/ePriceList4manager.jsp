<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>计调主管--询价记录</title>
<meta name="decorator" content="wholesaler" />
<%@ include file="commonVar.jsp"%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/eprice/ePriceList4manager.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/eprice/epriceCommon.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		//展开筛选
		launch();
		//操作浮框
		operateHandler();
		//成本价滑过显示具体内容
		inquiryPriceCon();
		$("#test").click(function(){
			var url = contextPath+"/eprice/manager/project/exportManagerEstimateExcel";
			$("#eprice-search-form-id").attr("action",url);
			$("#eprice-search-form-id").submit();
		});
	});
	//展开收起
	function expand(child, obj, srcActivityId, payMode_deposit,
			payMode_advance, payMode_full, payMode_op ,payMode_cw, payMode_data, payMode_guarantee,
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

</script>
</head>
<body>
	<page:applyDecorator name="show_head">
	    <page:param name="desc">询价分配</page:param>
	</page:applyDecorator>
	<!--右侧内容部分开始-->
	<div class="activitylist_bodyer_right_team_co_bgcolor">
			<form id="eprice-search-form-id">
				<c:if test="${fns:getUser().company.uuid eq '7a81a26b77a811e5bc1e000c29cf2586'}"> 
					<shiro:hasPermission name="	eprice:list:managerexportexcel">
						<p  class="main-right-topbutt">
							<a id="test" href="javascript:void(0)">导出Excel</a>
						</p>
					</shiro:hasPermission>
				</c:if>
				<div style="display: none;" class="lmels-ts">
					<img src="${ctxStatic}/logo/lmels-ts.png">如需预定，请与浪漫俄罗斯相关销售人员联系
					010-52877517；010-52899377；010-52906039；13581525134
				</div>
				<input type="hidden" value="1" name="pageNo" id="pageNo" /> <input
					type="hidden" value="10" name="pageSize" id="pageSize" /> <input
					type="hidden" value="" name="orderBy" id="orderBy" /> <input
					type="hidden" value="" name="agentId" id="agentId" />
				<div class="activitylist_bodyer_right_team_co">
					<div class="activitylist_bodyer_right_team_co2 pr">
						<input class="txtPro searchInput inputTxt"id="wholeSalerKey" name="keyword" placeholder="计调姓名/线路国家/询价客户" />
					</div>
					<div class="zksx">筛选</div>
					<div class="form_submit">
						<input type="button" value="搜索" id="eprice-search-form-btn-id"  class="btn btn-primary ydbz_x" />
						  <%--<input class="btn ydbz_x" type="reset"   value="清空所有条件"/>--%>
                        <input class="btn ydbz_x" type="button"	onclick="resetSearchParams()" value="清空所有条件" />
					</div>
					<div style="display: none;" class="ydxbd">
						<span></span>
						<div class="activitylist_bodyer_right_team_co3">
							<div class="activitylist_team_co3_text">计调：</div>
							<select name="operatorUid">
								<option value="-1">不限</option>
								<c:forEach items="${operators}" var="u">
									<option value="${u.id}">${u.name}</option>
								</c:forEach>
							</select>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">询价状态：</div>
							<div class="selectStyle">
							<select name="estimateStatus">
								<option value="-1">全部</option>
								<option value="6">待分配</option>
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
							<label class="activitylist_team_co3_text">出团日期：</label> <input readonly="readonly"
								onclick="WdatePicker()"
								value="" name="groupOpenDate" class="inputTxt dateinput"
								id="groupOpenDate"> <span>至</span> <input
								readonly="readonly" onclick="WdatePicker()" value=""
								name="groupCloseDate" class="inputTxt dateinput"
								id="groupCloseDate">
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">线路国家：</div>
							<div class="selectInput">
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
							<div class="selectInput">
							<select name="type" id="eprice-type-id">
								<option value="0">不限</option>
								<option value="1">单团</option>
								<option value="4">大客户</option>
								<option value="3">游学</option>
								<option value="5">自由行</option>
								<option value="7">机票</option>
							</select>
								</div>
						</div>
					<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">客户：</div>
								<select name="custId">
								<option value="-1">不限</option>
								<c:forEach items="${agentInfoList}" var="u">
									<option value="${u.id}">${u.agentName}</option>
								</c:forEach>
							</select>
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
						<div class="activitylist_bodyer_right_team_co3">
							<div class="activitylist_team_co3_text">销售员：</div>
							<select name="salerId">
								<option value="0">不限</option>
								<c:forEach items="${sellUser}" var="u">
									<option value="${u.id}">${u.name}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
			<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
			</form>

		
		<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul id="sort-list-ul-id">
						<li
						<li class="activitylist_paixu_left_biankuang liid"
							sortc="lastEstimatePriceTime" sortv="1"><a>询价时间<i
								class="icon" style="display: none;"></i></a></li>
						<li class="activitylist_paixu_left_biankuang liupdateDate"
							sortc="lastOperatorGivenTime" sortv="1"><a>报价时间<i
								class="icon" style="display: none;"></i></a></li>
						<li
							class="activitylist_paixu_left_biankuang lisettlementAdultPrice" style="width:100px"
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
							<th width="8%">客户联系人 <input type="hidden" id="agentinfo_visibility" value="1" ></th>
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