<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
<title>团队管理</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>

<script type="text/javascript">
$(function() {
	//如果展开部分有查询条件的话，默认展开，否则收起
	launch();
	//加载操作按键
	operateHandler();
	//计调模糊查询
	$("[name=operatorId]").comboboxSingle();
	$("[name=salerId]").comboboxSingle();

	var args = $('#searchForm').serialize(); //查询条件参数
	$("#iframepage").attr("src", "${ctx}/cost/review/statistics/${orderType }?" + args);

	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	for (var i = 0; i < searchFormInput.length; i++) {
		if ($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
			inputRequest = true;
		}
	}
	for (var i = 0; i < searchFormselect.length; i++) {
		if ($(searchFormselect[i]).children("option:selected").val() != "" && $(searchFormselect[i]).children("option:selected").val() != null) {
			selectRequest = true;
		}
	}
	if (inputRequest || selectRequest) {
		$('.zksx').click();
	}

	$('.nav-tabs li').hover(function() {
		$('.nav-tabs li').removeClass('current');
		$(this).parent().removeClass('nav_current');
		if ($(this).hasClass('ernav')) {
			if (!$(this).hasClass('current')) {
				$(this).addClass('current');
				$(this).parent().addClass('nav_current');
			}
		}
	},
	function() {
		$('.nav-tabs li').removeClass('current');
		$(this).parent().removeClass('nav_current');
		var _active = $(".totalnav .active").eq(0);
		if (_active.hasClass('ernav')) {
			_active.addClass('current');
			$(this).parent().addClass('nav_current');
		}
	});
	var _$orderBy = $("#orderBy").val();
	if (_$orderBy == "") {
		_$orderBy = "createDate";
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function() {
		if ($(this).hasClass("li" + orderBy[0])) {
			orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "ASC" ? "up": "down";
			$(this).find("a").eq(0).html($(this).find("a").eq(0).html() + "<i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
			$(this).attr("class", "activitylist_paixu_moren");
		}
	});

});

function expand(child, obj, groupCode, activityId, orderType, count) {
	$.ajax({
		url: "${ctx}/cost/review/listGroupDetail/",
		type: "POST",
		data: {
			activityId: activityId,
			groupCode: groupCode,
			orderType: orderType
		},
		success: function(data) {
			var htmlstr = ""
			var num = data.length;
			if (num > 0) {
				for (var i = 0; i < num; i++) {
					htmlstr += "<tr><td class='tc'>" + (i + 1) + "</td><td class='tc'>"
					+ data[i].groupCode + "</td><td class='tc'>" + data[i].supplyName
					+ "</td><td class='tc'>" + data[i].costName + "</td><td class='tc'>"
					+ data[i].quantity + "</td><td class='tc'>" + data[i].currencyName
					+ "</td><td class='tc'>" + data[i].detailPayedMoney + "</td><td class='tc'>"
					+ data[i].comment + "</td></tr>";
				}
			} else {
				htmlstr += "<tr><td colspan='8' style='text-align: center;'>经搜索暂无数据，请尝试改变搜索条件再次搜索</td></tr>"
			}
			$("#rpi_" + groupCode + "_" + count).html(htmlstr);
		}
	});
	if ($(child).is(":hidden")) {
		$(obj).html("收起");
		$(obj).parents("tr").addClass("tr-hover");
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
	} else {
		if (!$(child).is(":hidden")) {
			$(obj).parents("tr").removeClass("tr-hover");
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("展开");
		}
	}
};

function page(n, s) {
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").attr("action", "${ctx}/cost/review/listGroup/${orderType}/${reviewLevel}");
    $("#searchForm").submit();
};

function sortby(sortBy, obj) {
	var temporderBy = $("#orderBy").val();
	if (temporderBy.match(sortBy)) {
		sortBy = temporderBy;
		if (sortBy.match(/ASC/g)) {
			sortBy = $.trim(sortBy.replace(/ASC/gi, "")) + " DESC";
		} else {
			sortBy = $.trim(sortBy.replace(/DESC/gi, "")) + " ASC";
		}
	} else {
		sortBy = sortBy + " DESC";
	}
	$("#orderBy").val(sortBy);
	$("#searchForm").submit();
};

function updateIscommissionStatus(groupId, status, orderType) {
	var tipInfo = '';
	if (status == 1) {
		tipInfo = '确认已计算提成？';
	} else if (status == 0) {
		tipInfo = '取消计算提成确认？';
	}
	$.jBox.confirm(tipInfo, '提示',
	function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type: "POST",
				url: "${ctx}/cost/review/updateIscommissionStatus",
				data: {
					groupId: groupId,
					status: status,
					orderType: orderType
				},
				dataType: "json",
				success: function(data) {
					if ("ok" == data.flag) {
						$("#searchForm").submit();
					} else {
						$.jBox.tip("操作失败!", "error");
					}
				}
			});
		}
	});
};

function resetForm() {
	var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
	var selectArray = $('#searchForm').find("select");
	for (var i = 0; i < inputArray.length; i++) {
		if ($(inputArray[i]).val()) {
			$(inputArray[i]).val('');
		}
	}
	for (var i = 0; i < selectArray.length; i++) {
		var selectOption = $(selectArray[i]).children("option");
		$(selectOption[0]).attr("selected", "selected");
	}
}

function exportExcel(ctx) {
	var args = $('#searchForm').serialize(); //查询条件参数
	window.location.href = ctx + "/cost/review/exportExcel/${orderType}?" + args;
}

function iFrameHeight() {
	var ifm = document.getElementById("iframepage");
	var subWeb = document.frames ? document.frames["iframepage"].document: ifm.contentDocument;
	if (ifm != null && subWeb != null) {
//		ifm.height = subWeb.body.scrollHeight;
		ifm.width = subWeb.body.scrollWidth;
	}
}
//全选
function checkedAllUse(obj){
	$(".tdCheckBox").each(function(){
		$(obj).attr('checked')=='checked'?this.checked=true:this.checked=false;
	});
	this.checked=true;
	$(obj).parent().next().children("input").attr('checked',false);
}
//反选
function checkedNotUse(obj){
	$(".tdCheckBox").each(function(){
		this.checked=!$(this).attr("checked");
	});
	this.checked=true;
	$(obj).parent().prev().children("input").attr('checked',false);
}
//判断是否全选
function groupControl() {
	var _checkBox=$(".tdCheckBox");
	var _length=_checkBox.length;
	var _num=0;
	var _input=$("input[name='allGroupControl']");
	_checkBox.each(function(){
		this.checked==true?_num++:null;
	});
	_num==_length?_input.attr("checked","checked"):_input.removeAttr("checked");
}
//批量下载
	function allDownload() {
		var _checkBox=$(".tdCheckBox");
		var _id='',_type='',_code='',_num=0;
		_checkBox.each(function(){
			if(this.checked==true){
				_id+=$(this).attr('aId')+',';
				_type+=$(this).attr('aType')+',';
				_code+=$(this).attr('aCode')+',';
				_num++;
			}
		});
		if(_num ==0){
			top.$.jBox.tip('请选择产品');
			return false;
		}else{
			window.open("${ctx}" + "/cost/manager/batchDownLoadSettletList?activityIds=" + _id + "&orderTypes=" + _type + "&groupCodes=" + _code);
		}
	}
</script>

<style>

</style>
</head>
<body>
	 <page:applyDecorator name="new_cost_review_head">
		 <page:param name="showType"><c:choose><c:when test="${orderType==0}">all</c:when><c:when test="${orderType==6}">visa</c:when><c:when test="${orderType==7}">airticket</c:when><c:when test="${orderType==1}">single</c:when><c:when test="${orderType==2}">loose</c:when><c:when test="${orderType==3}">study</c:when><c:when test="${orderType==5}">free</c:when><c:when test="${orderType==4}">bigCustomer</c:when><c:when test="${orderType==10}">cruise</c:when><c:when test="${orderType==12}">island</c:when><c:when test="${orderType==11}">hotel</c:when></c:choose></page:param>
         <page:param name="isLMT">${isLMT }</page:param>
	 </page:applyDecorator>
	 <div class="activitylist_bodyer_right_team_co_bgcolor inventory_management">
	 <form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/cost/review/listGroup/${orderType}/${reviewLevel}" method="post" >
        <input type="hidden" id="typeId" value="${orderType}"/>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="activityIds" type="hidden" name="activityIds"/>
	    <input id="review" name="review"  type="hidden"  value="${review}">
	    <input id="reviewLevel"  type="hidden"  name="reviewLevel"  value="${reviewLevel}">
     <div class="activitylist_bodyer_right_team_co ">
    	 <div class="activitylist_bodyer_right_team_co2 pr">
         	<input class="txtPro inputTxt searchInput" id="wholeSalerKey" name="groupCode" value="${groupCode }"placeholder="请输入团号"/>
         </div>
		 <div class="zksx" >筛选</div>
		 <div class="form_submit">
			<input class="btn btn-primary ydbz_x" type="submit" value="搜索"/>
			<input class="btn ydbz_x" type="button" onclick="resetForm();" value="清空所有条件" />
			<input type="button" value="导出Excel" class="btn ydbz_x" onclick="exportExcel('${ctx}')">
		 </div>
		<div class="ydxbd">
			<span></span>
			<div class="activitylist_bodyer_right_team_co1">
				 <label class="activitylist_team_co3_text">产品名称：</label>
				 <input id="productName" type="text" name="productName" value="${productName }" class="txtPro inputTxt inquiry_left_text"/>
			</div>
			<c:if test="${orderType==0}">
				<div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text">团队类型：</label>
					<div class="selectStyle">
					<select name="orderS" id="orderS">
						<!-- 由于签证订单收款已经抽取为一个单独的功能，因此类型选择去掉签证类型（签证类型的值在数据库中为6） -->
						<c:forEach var="order" items="${orderTypes }">
							<c:if test="${order.value != 6}">
								<option value="${order.value }" <c:if test="${orderS==order.value}">selected="selected"</c:if>>${order.label }</option>
							</c:if>
						</c:forEach>
					</select>
					</div>
				</div>
			</c:if>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">计调：</label>
				<select name="operatorId">
					<option value="" selected="selected">请选择</option>
					<c:forEach var="jd" items="${agentJd }">
						<option value="${jd.key }" <c:if test="${operatorId==jd.key}">selected="selected"</c:if>>${jd.value }</option>
					</c:forEach>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				 <label class="activitylist_team_co3_text">销售：</label>
				 <select name="salerId">
					 <option value="" selected="selected">请选择</option>
					 <c:forEach var="saler" items="${agentSaler }">
						 <option value="${saler.key }" <c:if test="${salerId==saler.key}">selected="selected"</c:if>>${saler.value }</option>
					 </c:forEach>
				 </select>
			</div>
			<shiro:hasPermission name="listGroup:statustc">
				<div class="activitylist_bodyer_right_team_co3">
					<label class="activitylist_team_co3_text">提成状态：</label>
					<div class="selectStyle">
						<select name="iscommission">
							<option value="" selected="selected">请选择</option>
							<option value="N" <c:if test="${iscommission eq 'N'}">selected="selected"</c:if>>未计算提成状态</option>
							<option value="Y" <c:if test="${iscommission eq 'Y'}">selected="selected"</c:if>>已计算提成状态</option>
						</select>
					</div>
				</div>
			</shiro:hasPermission>
			<div class="activitylist_bodyer_right_team_co3">
				<label class="activitylist_team_co3_text">部门：</label>
				<div class="selectStyle">
					<select name="departmentId">
						<option value="" selected="selected">全部</option>
						<c:forEach items="${departmentList }" var="department">
							<option value="${department.id }" <c:if test="${departmentId==department.id}">selected="selected"</c:if>>${department.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">出团日期：</label>
				<input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate" value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>'onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly/>
				<span style="font-size:12px; font-family:'宋体';"> 至</span>
				<input id="groupCloseDate" class="inputTxt dateinput" name="groupCloseDate" value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>'onClick="WdatePicker()" readonly/>
			</div>
     	</div>
	 </div>
	</form:form>
	 <iframe id="iframepage" width="100%" height="60px" frameborder="0" onLoad="iFrameHeight()"></iframe>
	 <div class="activitylist_bodyer_right_team_co_paixu  total_sum">
        <div class="activitylist_paixu">
          	<div class="activitylist_paixu_left">
             	<ul>
            		<li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
            		<li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
            	</ul>
      		</div>
          	<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
        </div>
	 </div>

	<table id="contentTable" class="table activitylist_bodyer_table mainTable" >
		<thead >
			<tr>
				<th width="4%">序号</th>
                <th width="7%">团号</th>
                <th width="4%">团队类型</th>
                <th width="9%">产品名称</th>
                <th width="5%">出团日期</th>
                <th width="4%">人数</th>
                <th width="9%">部门</th>
                <th width="5%">计调</th>
                <th width="5%">销售</th>
				<!-- 拉美途用户不显示 应收金额、应付金额、预计利润、预计毛利率 -->
				<c:choose>
					<c:when test="${isLMT}">
						<th width="8%">实收金额</th>
						<th width="6%">实付金额</th>
						<th width="6%">实际利润</th>
						<th width="6%">实际毛利率</th>
					</c:when>
					<c:otherwise>
						<th width="8%">应收金额<br />实收金额</th>
						<th width="6%">应付金额<br />实付金额</th>
						<th width="6%">预计利润<br />实际利润</th>
						<th width="6%">预计毛利率<br />实际毛利率</th>
					</c:otherwise>
				</c:choose>
                <th width="5%">预报单状态</th>
                <th width="5%">结算单状态</th>
                <shiro:hasPermission name="listGroup:statustc">
                	<th width="5%">提成状态</th>
                </shiro:hasPermission>
                <th width="5%">操作</th>
            </tr>
		</thead>
		<tbody>
		<c:if test="${fn:length(entityList) <= 0 }">
			<tr class="toptr" >
				<td colspan="30" style="text-align: center;">
					暂无搜索结果
				</td>
			</tr>
		</c:if>
		<c:if test="${fn:length(entityList) > 0 }">
		<c:forEach items="${entityList}" var="activity" varStatus="s">
			<tr id="parent${s.count}">
				<td class="tc">
					<span class="sqcq-fj">
						<c:if test="${isLMT }">
							<input type="checkbox" aId="<c:if test='${activity.groupId eq -1 }'>${activity.productId}</c:if>
														<c:if test='${activity.groupId ne -1}'>${activity.groupId }</c:if>" aType="${activity.orderType }" aCode="${activity.groupCode}" class="tdCheckBox" value="" name="groupControl" onclick="groupControl()"/>
						</c:if>			
							${s.count}
					</span>
				</td>
		        <td class="tc">${activity.groupCode}</td>
		        <td class='tc'>${fns:getDictLabel(activity.orderType, "order_type", "")}</td>
		        <td class="tc">${activity.productName}</td>
		        <td class="tc">${activity.groupOpenDate}</td>
		        <td class="tc">${activity.personNum}</td>
		        <td class="tc">${fns:getDeptNameById(activity.deptId)}</td>
		        <td class="tc">${activity.operator}</td>
		        <td class="tc">
		        	<c:choose>
		            	<c:when test="${orderType eq 2 or saler eq '散拼' }">散拼</c:when>
		            	<c:otherwise>${activity.saler }</c:otherwise>
		            </c:choose>
		        </td>
				<!--针对拉美图的用户，显示实收金额，实付金额，实际利润，实际利润率-->
				<c:choose>
					<c:when test="${isLMT}">
						<td class="tc">¥${activity.realMoney}</td>
						<td class="tc">¥${activity.realPayMoney}</td>
						<td class="tc">¥${activity.realProfit }</td>
						<td class="tc">${activity.realProfitRate}</td>
					</c:when>
					<c:otherwise>
						<td class="p0 tr">
							<div class="yfje_dd">
								<span class="fbold">¥${activity.totalMoney}</span>
							</div>
							<div class="dzje_dd">
								<span class="fbold">¥${activity.realMoney}</span>
							</div>
						</td>
						<td class="p0 tr">
							<div class="yfje_dd">
								<span class="fbold">¥${activity.actualMoney}</span>
							</div>
							<div class="dzje_dd">
								<span class="fbold">¥${activity.realPayMoney}</span>
							</div>
						</td>
						<td class="p0 tr">
							<div class="yfje_dd">
								<span class="fbold">¥${activity.profit }</span>
							</div>
							<div class="dzje_dd">
								<span class="fbold">¥${activity.realProfit}</span>
							</div>
						</td>
						<td class="p0 tr">
							<div class="yfje_dd">
								<span class="fbold">${activity.profitRate }</span>
							</div>
							<div class="dzje_dd">
								<span class="fbold">${activity.realProfitRate}</span>
							</div>
						</td>
					</c:otherwise>
				</c:choose>
                    <td class="tc">
                    	<c:choose>
                    		<c:when test="${activity.forcastStatus eq '00' or activity.forcastStatus eq null or activity.forcastStatus eq '' }">未锁定</c:when>
                    		<c:when test="${activity.forcastStatus eq '10' }">锁定</c:when>
                    	</c:choose>
                    </td>
                    <td class="tc">
                    	<c:choose>
                    		<c:when test="${activity.lockStatus eq '0' or activity.lockStatus eq null or activity.lockStatus eq '' }">未锁定</c:when>
                    		<c:when test="${activity.lockStatus eq '1' }">锁定</c:when>
                    	</c:choose>
                    </td>
					<shiro:hasPermission name="listGroup:statustc">
                    <td class="tc">
                    	<c:choose>
                    	    <c:when test="${activity.iscommission eq '0' }">未计算提成</c:when>
                    	    <c:when test="${activity.iscommission eq '1' }">已计算提成</c:when>
                    	</c:choose>
                    </td>
                    </shiro:hasPermission>
                    <td class="p00">
                      <dl class="handle">
                        	<dt><img title="操作" src="${ctxStatic }/images/handle_cz.png"></dt>
		                   	<dd><p>
                            	<span></span>
                            	<c:if test="${orderType ne 0 }">
	                            	<c:if test="${orderType<6 or orderType==10 }">
	                                	<a onclick="expand('#child1_${activity.groupCode}_${s.count }',this,'${activity.groupCode}',${activity.groupId},${orderType },${s.count })"  href="javascript:void(0)">展开</a>
	                                   	<a href="${ctx}/costReview/activity/activityCostReviewDetail/${activity.productId}/${activity.groupId}/2?read=1&head=2" target="_blank">查看</a>
	                                  	<a href="${ctx }/cost/manager/forcastList/${activity.groupId}/${orderType}" target="_blank">预报单</a>
	                              	 	<a href="${ctx }/cost/manager/settleList/${activity.groupId}/${orderType}" target="_blank">结算单</a>
	                                </c:if>
	                                <c:if test="${orderType==7 }">
	                                 	<a onclick="expand('#child1_${activity.groupCode}_${s.count }',this,'${activity.groupCode}',${activity.productId},${orderType },${s.count })"  href="javascript:void(0)">展开</a>
	                                 	<a href="${ctx}/costReview/airticket/airticketCostReviewDetail/${activity.productId}/2?read=1&head=2" target="_blank">查看</a>
	                                 	<a href="${ctx }/cost/manager/forcastList/${activity.productId}/${orderType}" target="_blank">预报单</a>
	                              	 	<a href="${ctx }/cost/manager/settleList/${activity.productId}/${orderType}" target="_blank">结算单</a>
	                                </c:if>
	                                <c:if test="${orderType==6}">
	                                 	<a onclick="expand('#child1_${activity.groupCode}_${s.count }',this,'${activity.groupCode}',${activity.productId},${orderType },${s.count })"  href="javascript:void(0)">展开</a>
	                                  	<a href="${ctx}/costReview/visa/visaCostReviewDetail/${activity.productId}/2?read=1&head=2" target="_blank">查看</a>
	                                  	<a href="${ctx }/cost/manager/forcastList/${activity.productId}/${orderType}" target="_blank">预报单</a>
	                              	 	<a href="${ctx }/cost/manager/settleList/${activity.productId}/${orderType}" target="_blank">结算单</a>
	                                </c:if>
	                                <c:if test="${orderType == 11 }">
	                                 	<a onclick="expand('#child1_${activity.groupCode}_${s.count }',this,'${activity.groupCode}','${activity.activityUuid}',${orderType },${s.count })" href="javascript:void(0)">展开</a>
	                                 	<a target="_blank" href="${ctx}/cost/review/hotelRead/${activity.productUuid}/${activity.activityUuid}/${reviewLevel}?from=operatorPre&flag=1&sitemap=1">查看</a>
	                                  	<a href="${ctx }/cost/manager/forcastList/${activity.activityUuid}/${orderType}" target="_blank">预报单</a>
	                              	 	<a href="${ctx }/cost/manager/settleList/${activity.activityUuid}/${orderType}" target="_blank">结算单</a>
	                                </c:if>
	                                <c:if test="${orderType == 12 }">
	                                 	<a onclick="expand('#child1_${activity.groupCode}_${s.count }',this,'${activity.groupCode}','${activity.activityUuid}',${orderType },${s.count })"  href="javascript:void(0)">展开</a>
	                                 	<a target="_blank" href="${ctx}/cost/review/islandRead/${activity.productUuid}/${activity.activityUuid}/${reviewLevel}?from=operatorPre&flag=1&sitemap=1">查看</a>
	                                  	<a href="${ctx }/cost/manager/forcastList/${activity.activityUuid}/${orderType}" target="_blank">预报单</a>
	                              	 	<a href="${ctx }/cost/manager/settleList/${activity.activityUuid}/${orderType}" target="_blank">结算单</a>
	                                </c:if>
                                </c:if>
                                <c:if test="${orderType eq 0 }">
	                            	<c:if test="${activity.orderType<6 or activity.orderType==10 }">
	                                	<a onclick="expand('#child1_${activity.groupCode}_${s.count }',this,'${activity.groupCode}',${activity.groupId},${activity.orderType },${s.count })"  href="javascript:void(0)">展开</a>
	                                	<a href="${ctx}/costReview/activity/activityCostReviewDetail/${activity.productId}/${activity.groupId}/2?read=1&head=2" target="_blank">查看</a>
	                                	<a href="${ctx }/cost/manager/forcastList/${activity.groupId}/${activity.orderType}" target="_blank">预报单</a>
	                              		<a href="${ctx }/cost/manager/settleList/${activity.groupId}/${activity.orderType}" target="_blank">结算单</a>
	                                </c:if>
	                                <c:if test="${activity.orderType==7 }">
	                                 	<a onclick="expand('#child1_${activity.groupCode}_${s.count }',this,'${activity.groupCode}',${activity.productId},${activity.orderType },${s.count })"  href="javascript:void(0)">展开</a>
                                        <a href="${ctx}/costReview/airticket/airticketCostReviewDetail/${activity.productId}/2?read=1&head=2" target="_blank">查看</a>
	                                 	<a href="${ctx }/cost/manager/forcastList/${activity.productId}/${activity.orderType}" target="_blank">预报单</a>
	                              	 	<a href="${ctx }/cost/manager/settleList/${activity.productId}/${activity.orderType}" target="_blank">结算单</a>
	                                </c:if>
	                                <c:if test="${activity.orderType==6}">
	                                 	<a onclick="expand('#child1_${activity.groupCode}_${s.count }',this,'${activity.groupCode}',${activity.productId},${activity.orderType },${s.count })"  href="javascript:void(0)">展开</a>
                                     	<a href="${ctx}/costReview/visa/visaCostReviewDetail/${activity.productId}/2?read=1&head=2" target="_blank">查看</a>
	                                 	<a href="${ctx }/cost/manager/forcastList/${activity.productId}/${activity.orderType}" target="_blank">预报单</a>
	                              	 	<a href="${ctx }/cost/manager/settleList/${activity.productId}/${activity.orderType}" target="_blank">结算单</a>
	                                </c:if>
	                                <c:if test="${activity.orderType == 11 }">
	                                 	<a onclick="expand('#child1_${activity.groupCode}_${s.count }',this,'${activity.groupCode}','${activity.activityUuid}',${activity.orderType },${s.count })"  href="javascript:void(0)">展开</a>
	                                 	<a target="_blank" href="${ctx}/cost/review/hotelRead/${activity.productUuid}/${activity.activityUuid}/${reviewLevel}?from=operatorPre&flag=1&sitemap=1">查看</a>
									 	<a href="${ctx }/cost/manager/forcastList/${activity.activityUuid}/${activity.orderType}" target="_blank">预报单</a>
	                              	 	<a href="${ctx }/cost/manager/settleList/${activity.activityUuid}/${activity.orderType}" target="_blank">结算单</a>
	                                </c:if>
	                                 <c:if test="${activity.orderType == 12 }">
	                                 <a onclick="expand('#child1_${activity.groupCode}_${s.count }',this,'${activity.groupCode}','${activity.activityUuid}',${activity.orderType },${s.count })"  href="javascript:void(0)">展开</a>
	                                 <a target="_blank" href="${ctx}/cost/review/islandRead/${activity.productUuid}/${activity.activityUuid}/${reviewLevel}?from=operatorPre&flag=1&sitemap=1">查看</a>
	                                 <a href="${ctx }/cost/manager/forcastList/${activity.activityUuid}/${activity.orderType}" target="_blank">预报单</a>
	                              	 <a href="${ctx }/cost/manager/settleList/${activity.activityUuid}/${activity.orderType}" target="_blank">结算单</a>
	                                 </c:if>
                                 </c:if>
								<c:choose>
									<c:when test="${activity.iscommission eq '0' }">
                                        <shiro:hasPermission name="listGroup:optc">
                                           <c:choose>
                                              <c:when test="${activity.orderType==6 or activity.orderType ==7 }">
                                                  <a onclick="updateIscommissionStatus('${activity.productId}','1',${activity.orderType })">提成确认</a>
                                              </c:when>
                                              <c:otherwise>
                                               	   <a onclick="updateIscommissionStatus('${activity.groupId}','1',${activity.orderType })">提成确认</a>
                                              </c:otherwise>
                                           </c:choose>
		                            	</shiro:hasPermission>
									</c:when>
									<c:when test="${activity.iscommission eq '1' }">
                                        <shiro:hasPermission name="listGroup:optc">
                                        <c:choose>
											<c:when test="${activity.orderType==6 or activity.orderType ==7 }">
                                                <a onclick="updateIscommissionStatus('${activity.productId}','0',${activity.orderType })">撤销提成</a>
                                            </c:when>
                                            <c:otherwise>
												<a onclick="updateIscommissionStatus('${activity.groupId}','0',${activity.orderType })">撤销提成</a>
											</c:otherwise>
										</c:choose>
		                            	</shiro:hasPermission>
									</c:when>
								</c:choose>
		                     </p></dd>
                        </dl>
		            </td>
             </tr>
             <tr id="child1_${activity.groupCode}_${s.count }" class="activity_team_top1" style="display:none">
				<td colspan="17" class="team_top">
					<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
						<thead>
							<tr>
								<th class="tc" width="10%">序号</th>
								<th class="tc" width="10%">团号</th>
								<th class="tc" width="15%">供应商/地接社</th>
								<th class="tc" width="10%">款项</th>
								<th class="tc" width="25%">数量</th>
								<th class="tc" width="10%">币种</th>
								<th class="tc" width="15%">应付金额</th>
								<th class="tc" width="15%">备注</th>
							</tr>
						</thead>
						<tbody id='rpi_${activity.groupCode}_${s.count }'>

						</tbody>
					</table>
				</td>
			</tr>
			</c:forEach>
			<c:if test="${isLMT}">
			<tr class="checkalltd">
				<td colspan='19' class="t1">
					<label> <input type="checkbox" name="allGroupControl" onclick="checkedAllUse(this)" /> 全选 </label>
					<label> <input type="checkbox" name="notGroupControl" onclick="checkedNotUse(this)" /> 反选 </label>
						<input type="button" class="btn" value="批量下载结算单" onclick="allDownload()">

				</td>
			</tr>
			</c:if>
		</c:if>
		</tbody>
	</table>
	</div>
	 <div class="page">
		 <div class="pagination">
			<div class="endPage">${page}</div>
			<div style="clear:both;"></div>
		 </div>
	 </div>
	</body>
</html>