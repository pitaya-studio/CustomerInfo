<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler" />
<title>渠道管理-列表</title>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
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
			$('form[id=searchForm]').attr('action','${ctx }/mtour/order/getNoAgent'); 
			$("#searchForm").submit();
		});
		$("#exportAngentList").click(function() {
			$('form[id=searchForm]').attr('action','${ctx }/agent/manager/exportAgentList'); 
			$("#searchForm").submit();
		});
	});
	//展开筛选按钮
	function launch() {
		$('.zksx').click(function() {
			if ($('.ydxbd').is(":hidden") == true) {
				$('.ydxbd').show();
				$(this).text('收起筛选');
				$(this).addClass('zksx-on');
			} else {
				$('.ydxbd').hide();
				$(this).text('展开筛选');
				$(this).removeClass('zksx-on');
			}
		});
		//如果展开部分有查询条件的话，默认展开，否则收起	
		var searchFormInput = $("#searchForm")
				.find("input[type!='hidden'][type!='submit'][type!='button'][id!='agentName']");
		var searchFormselect = $("#searchForm").find("select");
		var inputRequest = false;
		var selectRequest = false;
		for ( var i = 0; i < searchFormInput.length; i++) {
			if ($(searchFormInput[i]).val() != ""
					&& $(searchFormInput[i]).val() != null) {
				inputRequest = true;
			}
		}
		for ( var i = 0; i < searchFormselect.length; i++) {
			if ($(searchFormselect[i]).children("option:selected").val() != ""
					&& $(searchFormselect[i]).children("option:selected").val() != null) {
				selectRequest = true;
			}
		}
		if (inputRequest || selectRequest) {
			$('.zksx').click();
		}
	}

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
		if(isSignChannel==0){
			location.href="${ctx}/agent/manager/queryList";
		}else{
			$("#isSignChannel").val(isSignChannel);
			$("#searchForm").submit();
		}
	}
	
	/**
	 * 翻页
	 * n 为页数
	 * s 为条数
	 */
	function page(n, s) {
	    $("#pageNo").val(n);
	    $("#pageSize").val(s);
	    $('form[id=searchForm]').attr('action','${ctx }/mtour/order/getNoAgent'); 
	    $("#searchForm").submit();
	    return false;
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

	<form:form id="searchForm" method="post" action="${ctx }/mtour/order/getNoAgent" modelAttribute="agentinfo">

		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
		<c:set var="signChannel" value="${empty isSignChannel ? '0' : isSignChannel}"></c:set>
		<input id="isSignChannel" name="isSignChannel" type="hidden" value="${signChannel}"/>
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 wpr20 pr">
				<div class="activitylist_team_co3_text">搜索：</div>
				<input value="${nagentName}" class="inputTxt inputTxtlong" name="nagentName" id="" flag="istips"> 
				<span class="ipt-tips">输入渠道名称，渠道品牌</span>
				<%-- <input value="${salesRoom}" class="inputTxt inputTxtlong" name="salesRoom" id="" flag="istips"> 
				<span class="ipt-tips ipt-tips2">输入门市名称</span>
				<input value="${agentTel}" class="inputTxt inputTxtlong" name="agentTel" id="" flag="istips"> 
				<span class="ipt-tips ipt-tips3">输入电话</span> --%>
			</div>
			<div class="form_submit">
				<input type="button" value="搜索" class="btn btn-primary ydbz_x submitButton"> 
				<input type="button" value="重置搜索" class="btn btn-primary ydbz_x " onclick="formReset();">
			</div>
			<a class="zksx">展开筛选</a>
			<div class="ydxbd" style="display:none">
				<div class="activitylist_bodyer_right_team_co">
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">销售：</div>
							<select name="salerId">
								<option value="">请选择</option>
								<c:forEach var="user" items="${agentSalers}">
									<option value="${user.key}" <c:if test="${salerId == user.key}">selected=selected</c:if>>${user.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="activitylist_bodyer_right_team_co2">
							<label>创建时间：</label>
							<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"
								class="inputTxt dateinput" id="" name="minCreateDate" value="${minCreateDate}"> <span>至 </span>
							<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${maxCreateDate}" 
								class="inputTxt dateinput" id="" name="maxCreateDate">
						</div>
					<div class="kong"></div>
				</div>
			</div>
		</div>
	</form:form>
	<!-- 签约渠道、非签约渠道转换标签：0代表签约渠道，1表示非签约渠道  flag ==1 是美途的用户-->
			<div class="supplierLine">
				<a  href="javascript:void(0)" onclick="getChannel('0');">签约渠道</a>
				<a class="select" href="javascript:void(0)" onclick="getChannel('1');">非签约渠道</a>
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
		
			<table class="table activitylist_bodyer_table" id="contentTable">
				<thead>
					<tr>
						<th width="4%">序号</th>
						<th width="10%">渠道名称</th>
						<th width="6%">跟进销售员</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list }" var="agent" varStatus="s">
						<tr>
							<td>${s.count }</td>
							<td class="word-break-all">
								${agent.nagentName}
							</td>
							<td>${agent.createName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		<div class="pagination clearFix">
			${page}
			<div style="clear:both;"></div>
		</div>
</body>
</html>

