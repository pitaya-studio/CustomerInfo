<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>渠道管理-列表</title>

<link type="text/css" rel="stylesheet" href="${ctxStatic }/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic }/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic }/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic }/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic }/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/common.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		launch();
		//产品名称文本框提示信息
		inputTips();
		$("input.submitButton").click(function() {
			$('form[id=searchForm]').attr('action','${ctx }/agent/manager/queryList'); 
			$("#searchForm").submit();
		});
		$("#exportAngentList").click(function() {
			$('form[id=searchForm]').attr('action','${ctx }/agent/manager/exportAgentList'); 
			$("#searchForm").submit();
		});
	});
	//展开筛选按钮
	//和common.js重复
//	function launch() {
//		$('.zksx').click(function() {
//			if ($('.ydxbd').is(":hidden") == true) {
//				$('.ydxbd').show();
//				$(this).text('收起筛选');
//				$(this).addClass('zksx-on');
//			} else {
//				$('.ydxbd').hide();
//				$(this).text('展开筛选');
//				$(this).removeClass('zksx-on');
//			}
//		});
//		//如果展开部分有查询条件的话，默认展开，否则收起
//		var searchFormInput = $("#searchForm")
//				.find("input[type!='hidden'][type!='submit'][type!='button'][id!='agentName']");
//		var searchFormselect = $("#searchForm").find("select");
//		var inputRequest = false;
//		var selectRequest = false;
//		for ( var i = 0; i < searchFormInput.length; i++) {
//			if ($(searchFormInput[i]).val() != ""
//					&& $(searchFormInput[i]).val() != null) {
//				inputRequest = true;
//			}
//		}
//		for ( var i = 0; i < searchFormselect.length; i++) {
//			if ($(searchFormselect[i]).children("option:selected").val() != ""
//					&& $(searchFormselect[i]).children("option:selected").val() != null) {
//				selectRequest = true;
//			}
//		}
//		if (inputRequest || selectRequest) {
//			$('.zksx').click();
//		}
//	}

	//修改
	function modify(id) {
		window.open("${ctx}/agent/manager/updateFirstForm/" + id);
	}
	//查看
	function review(id) {
		window.open("${ctx}/agent/manager/review/" + id);
	}
	//删除
	function deleteAgentinfo(id) {
		var mess = "确定要删除数据?"
		top.$.jBox.confirm(mess, '系统提示', function(v) {
			if (v == 'ok') {
				$.ajax({
					type : "GET",
					url : "${ctx}/agent/manager/delete/" + id,
					success : function(msg) {
						if( msg == "error"){
							top.$.jBox.tip("该渠道在订单中存在，不能删除");
							return false;
						}
						else{
							top.$.jBox.tip("删除成功");
							window.location.href = "${ctx}/agent/manager/queryList";
						}
					}
				});
			}
		}, {
			buttonsFocus : 1
		});
		top.$('.jbox-body .jbox-icon').css('top', '55px');
		return false;
	}
	//表单重置
	function formReset() {
		$(':input', '#searchForm').not(':button, :submit, :reset, :hidden')
				.val('').removeAttr('checked').removeAttr('selected');
	}
	
	/**
	 * 渠道查询：0为签约渠道，1为非签约渠道
	 * 
	 * param departmentId
	 */
	function getChannel(isSignChannel) {
		$("#isSignChannel").val(isSignChannel);
		$('form[id=searchForm]').attr('action','${ctx }/agent/manager/queryList');
		$("#searchForm").submit();
	}
	
	/**
	 * 翻页
	 * n 为页数
	 * s 为条数
	 */
	function page(n, s) {
	    $("#pageNo").val(n);
	    $("#pageSize").val(s);
	    $('form[id=searchForm]').attr('action','${ctx }/agent/manager/queryList'); 
	    $("#searchForm").submit();
	    return false;
	}
	function getNoAgent(){
		location.href="${ctx}/mtour/order/getNoAgent";
	}
</script>
</head>
<body>
	<style type="text/css">
	.address{ position:absolute;top:0;right:0;font-size:12px; padding:0 3px;height:14px; text-align:center; line-height:14px; border:1px solid black; border-radius:2px; display:inline-block; margin-right:1px; margin-top:1px;}
label {
	cursor: inherit;
}
.ipt-tips2 {
    left: 393px;
    color: #b2b2b2;
}
.ipt-tips3{
	left: 732px;
}
.position_relative{
	position:relative;
}
</style>
	<page:applyDecorator name="agent_op_head">

	</page:applyDecorator>

	<!--右侧内容部分开始-->
	<form:form id="searchForm" method="post" action="${ctx }/agent/manager/queryList" modelAttribute="agentinfo">

		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
		<c:set var="signChannel" value="${empty isSignChannel ? '0' : isSignChannel}"></c:set>
		<input id="isSignChannel" name="isSignChannel" type="hidden" value="${signChannel}"/>

		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr">
				<%--<div class="activitylist_team_co3_text">搜索：</div>--%>
				<input value="${agentName}" class="inputTxt inputTxtlong searchInput" name="agentName" placeholder="输入渠道名称，渠道品牌">
				<%-- <input value="${salesRoom}" class="inputTxt inputTxtlong" name="salesRoom" id="" flag="istips">
				<span class="ipt-tips ipt-tips2">输入门市名称</span>
				<input value="${agentTel}" class="inputTxt inputTxtlong" name="agentTel" id="" flag="istips"> 
				<span class="ipt-tips ipt-tips3">输入电话</span> --%>
			</div>
			<a class="zksx">筛选</a>

			<div class="form_submit">
				<input type="button" value="搜索" class="btn btn-primary ydbz_x submitButton"> 
				<input type="button" value="清空所有条件" class="btn ydbz_x " onclick="formReset();">
				<input type="button" value="导出excel" class="btn ydbz_x" id="exportAngentList"> 
			</div>
			<p class="main-right-topbutt">
				<a class="primary" href="${ctx }/agent/manager/firstForm">新增渠道商</a>
			</p>
			<%--<a class="zksx">展开筛选</a>--%>
			<div class="ydxbd" style="display:none">
				<span></span>
				<%--<div class="activitylist_bodyer_right_team_co">--%>
				<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">门市名称：</div>
						<input value="${salesRoom}" class="inputTxt inputTxtlong" name="salesRoom" id="" flag="istips"> 
					</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">电话：</div>
					<input value="${agentTel}" class="inputTxt inputTxtlong" name="agentTel" id="" flag="istips">
				</div>
				<c:if test="${flag!=1}">
<!-- 				美途1期 隐藏渠道商列表查询部分功能--包括不显示订单类型查询、隐藏营业额等统计数据、隐藏非签约渠道等  update by zhanghao，wangxiaokai 20151107-->
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">订单类型：</div>
						<div class="selectStyle">
							<select name="orderType">
								<option value="">请选择</option>
								<option value="1" <c:if test="${orderType == 1}">selected=selected</c:if>>单团</option>
								<option value="2" <c:if test="${orderType == 2}">selected=selected</c:if>>散拼</option>
								<option value="3" <c:if test="${orderType == 3}">selected=selected</c:if>>游学</option>
								<option value="4" <c:if test="${orderType == 4}">selected=selected</c:if>>大客户</option>
								<option value="5" <c:if test="${orderType == 5}">selected=selected</c:if>>自由行</option>
								<option value="6" <c:if test="${orderType == 6}">selected=selected</c:if>>签证</option>
								<option value="7" <c:if test="${orderType == 7}">selected=selected</c:if>>机票</option>
							</select>
						</div>
					</div>
				</c:if>
				<c:if test="${signChannel == '0'}">
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">销售：</div>
						<div class="selectStyle">
							<select name="salerUserId">
								<option value="">请选择</option>
								<c:forEach var="user" items="${agentSalers}">
									<option value="${user.key}" <c:if test="${salerUserId == user.key}">selected=selected</c:if>>${user.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">创建时间：</div>
							<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"
								class="inputTxt dateinput" id="" name="beginCreateDate" value="${beginCreateDate}"> <span>至 </span>
							<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${endCreateDate}" 
								class="inputTxt dateinput" id="" name="endCreateDate">
						</div>
					</c:if>
				<%--</div>--%>
			</div>
		</div>
	</form:form>
	<!-- 签约渠道、非签约渠道转换标签：0代表签约渠道，1表示非签约渠道  flag ==1 是美途的用户-->
	<c:choose>
		<c:when test="${flag==1}">
			<div class="supplierLine">
				<a <c:if test="${signChannel == '0'}">class="select"</c:if> href="javascript:void(0)" onclick="getChannel('0');">签约渠道</a>
				<a  href="javascript:void(0)" onclick="getNoAgent();">非签约渠道</a>
			</div>
			<div class="activitylist_bodyer_right_team_co_paixu">
				<div class="activitylist_paixu">
					<div class="activitylist_paixu_left">
						<ul>
							<li></li>
						</ul>
					</div>
					<div class="activitylist_paixu_right">
						查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条
					</div>
					<div class="kong"></div>
				</div>
			</div>
		
			<table class="table mainTable activitylist_bodyer_table" id="contentTable">
				<thead>
					<tr>
						<th width="4%">序号</th>
						<th width="10%">渠道名称</th>
						<!-- 如果是非签约渠道，则没有操作项 -->
						<c:if test="${signChannel == '0'}">
							<th width="6%">跟进销售员</th>
							<th width="10%">操作</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list }" var="agent" varStatus="s">
						<tr>
							<td>${s.count }</td>
							<td class="word-break-all">
								<c:if test="${not empty agent.paymentName}">
									<div class="ycq">${agent.paymentName}</div>
								</c:if>
								${agent.agentName}
							</td>
							<!-- 如果是非签约渠道，则没有操作项 -->
							<c:if test="${signChannel == '0'}">
								<%-- <td class="tc">${agent.userName}</td> --%>
								<td class="tc">${fns:getUserById(agent.agentSalerId).name }</td>
								<td class="tda tc">
									<a href="javascript:void(0)" onclick="review('${agent.agentId}');">查看</a> 
									<a href="javascript:void(0)" onclick="modify('${agent.agentId}');">修改</a> 
									<a href="javascript:void(0)" onclick="deleteAgentinfo('${agent.agentId}');">删除</a>
								</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
				<div class="supplierLine">
					<a <c:if test="${signChannel == '0'}">class="select"</c:if> href="javascript:void(0)" onclick="getChannel('0');">
						<c:choose>
							<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }">已签</c:when>
							<c:otherwise>签约渠道</c:otherwise>
						</c:choose>
					</a>
					<a <c:if test="${signChannel == '1'}">class="select"</c:if> href="javascript:void(0)" onclick="getChannel('1');">
						<c:choose>
							<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }">未签</c:when>
							<c:otherwise>
								<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' }">
									直客
								</c:if>
								<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586' }">
									非签约渠道
								</c:if>
							</c:otherwise>
						</c:choose>
					</a>
					<%-- <a <c:if test="${signChannel == '2'}">class="select"</c:if> href="javascript:void(0)" onclick="getChannel('2');">QUAUQ渠道</a> --%>
				</div>
				<div class="activitylist_bodyer_right_team_co_paixu">
					<div class="activitylist_paixu">
						<div class="activitylist_paixu_left">
							<ul>
								<li>应收：${allSumTotalMoney}</li>
								<li>实收：${allSumPayedMoney}</li>
								<li>达账：${allSumAccountedMoney}</li>
								<li>人数：${allSumOrderNum}</li>
							</ul>
						</div>
						<div class="activitylist_paixu_right">
							查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条
						</div>
						<div class="kong"></div>
					</div>
				</div>
			
				<table class="table mainTable activitylist_bodyer_table" id="contentTable">
					<thead>
						<tr>

							<th style="width:4%;">序号</th>
							<th style="width:10%">渠道名称</th>

							<th width="10%">门市名称</th>
							<th width="10%">电话</th>
							<!-- 29 如果是非签约渠道，则没有渠道品牌 -->
							<c:if test="${signChannel == '0'}">
								<th style="width:8%" >渠道品牌</th>
							</c:if>

							<th style="width:10%"  >营业额（应收）</th>
							<th style="width:10%" >营业额（实收）</th>
							<th style="width:10%" >营业额（达账）</th>
							<th style="width:6%" >游客数(人)</th>

							<!-- 如果是非签约渠道，则没有操作项 -->
							<c:if test="${signChannel == '0'}">
								<th style="width:5%;">跟进销售员</th>
								<th style="width:11%">操作</th>
							</c:if>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list }" var="agent" varStatus="s">
							<tr>
								<td class="tc">${s.count }</td>
								<td>
									<c:if test="${not empty agent.paymentName}">
										<div class="ycq">${agent.paymentName}</div>
									</c:if>
									${agent.agentName}
								</td>
								<td class=position_relative>
									<div class="address" style="" title="${agent.agentAddressStreet }">地址</div>
									${agent.salesRoom}
								</td>
								<td>
								<c:if test="${agent.agentTelAreaCode!=null && agent.agentTelAreaCode!='' && agent.agentTel!=null && agent.agentTel != '' }">
									${agent.agentTelAreaCode }-${agent.agentTel}</c:if>
								</td>
								<c:if test="${signChannel == '0'}">
									<td class="tc">${agent.agentBrand}</td>
								</c:if>
								<td class="tc">${agent.sumTotalMoney}</td>
								<td class="tc">${agent.sumPayedMoney}</td>
								<td class="tc">${agent.sumAccountedMoney}</td>
								<td class="tc">${agent.sumOrderNum}</td>
								<!-- 如果是非签约渠道，则没有操作项 -->
								<c:if test="${signChannel == '0'}">
									<c:set var="salerMap" value="${fns:getSalersFromIdStr(agent.agentSalerId) }"></c:set>
									<td class="tc">
										<span style="white-space: nowrap; width:120px; text-overflow:ellipsis; overflow:hidden; display:inline-block; overlow:hidden;" title="${salerMap.salerNameStrWithStop }">${salerMap.salerNameStrWithStop }</span>
									</td>
									<td class="tda tc">
										<a href="javascript:void(0)" onclick="review('${agent.agentId}');">查看</a> 
										<a href="javascript:void(0)" onclick="modify('${agent.agentId}');">修改</a> 
										<a href="javascript:void(0)" onclick="deleteAgentinfo('${agent.agentId}');">删除</a>
									</td>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</table>
		</c:otherwise>
	</c:choose>
	
	<div class="pagination clearFix">
		${page}
		<div style="clear:both;"></div>
	</div>
</body>
</html>

