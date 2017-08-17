<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审批优化-审批配置界面-单据-支出凭单</title>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<link rel="stylesheet" href="${ctx}/static/css/jbox.css" />
<link type="text/css" rel="stylesheet"	href="${ctxStatic}/css/jquery.validate.min.css" />

<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

<script type="text/javascript">
	$(function () {
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
		//取消一个checkbox 就要联动取消 全选的选中状态
		$("input[name='ids']").click(function () {
			if ($(this).attr("checked")) {

			} else {
				$("input[name='allChk']").removeAttr("checked");
			}
		});

		var resetSearchParams = function () {
			$(':input', '#searchForm')
					.not(':button, :submit, :reset, :hidden')
					.val('')
					.removeAttr('checked')
					.removeAttr('selected');
			$('#country').val("");
		}
		$('#contentTable').on('change', 'input[type="checkbox"]', function () {
			if ($('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length) {
				$('[name="allChk" ]').attr('checked', true);
			} else {
				$('[name="allChk" ]').removeAttr('checked');
			}
		});

		$('.approve-bill-list').on('click', '.remove', function () {
			$(this).parent().remove();
		});
	});

	//展开收起
	function expand(child, obj) {
		if ($(child).is(":hidden")) {
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
			$(obj).parents("tr").addClass("tr-hover");

		} else {
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");

		}
	}
	function checkall(obj) {
		if ($(obj).attr("checked")) {
			$('#contentTable input[type="checkbox"]').attr("checked", 'true');
			$("input[name='allChk']").attr("checked", 'true');
		} else {
			$('#contentTable input[type="checkbox"]').removeAttr("checked");
			$("input[name='allChk']").removeAttr("checked");
		}
	}

	function checkreverse(obj) {
		var $contentTable = $('#contentTable');
		$contentTable.find('input[type="checkbox"]').each(function () {
			var $checkbox = $(this);
			if ($checkbox.is(':checked')) {
				$checkbox.removeAttr('checked');
			} else {
				$checkbox.attr('checked', true);
			}
		});
	}

	function jbox__pop_select_duty_fab(obj) {
		var currentEle = $(obj);
		var approverHtml = '';
		var selectedApprovel = '';
		var $pop = $.jBox($("#pop_select_duty_fab").html(), {
			title: "选择职务", buttons: {'确定': 1, '取消': 0}, submit: function (v, h, f) {
				if (v == "1") {
					if ($pop.find('ul li input:checked').length > 0) {


						$pop.find('ul li input:checked').each(function () {
							currentEle.parent().siblings().empty();
//                                if (!currentEle.parent().siblings().find('[code=' + $(this).siblings().attr('code') + ']').length) {
							approverHtml += '<label code="' + $(this).siblings().attr('code') + '"><em>' + $(this).siblings().text() + '</em><span class="remove">x</span></label>';
//                                }
						});
						currentEle.parent().siblings().append(approverHtml);
					}
					else {
						currentEle.parent().siblings().html('');
					}
				}
			}, height: '342', width: '320'
		});
		currentEle.parent().siblings().find('label').each(function () {
			var currentApproval = $(this);
			$pop.find('ul li em').each(function () {
				if (currentApproval.attr('code') == $(this).attr('code')) {
					$(this).siblings().attr('checked', 'checked');
				}
			})
		});

		inquiryCheckBOX();
	}
	function save(){
		var receiptType="${receiptType}";

		//获取主管的审批标签记录
		var $executive=$("#executive").find("label");
		var executiveValue='';
		if ($executive.length){
			$executive.each(function(){
				executiveValue+=$(this).attr("code")+",";
			});
		}

		// 获取总经理的审批标签记录
		var $generalManager=$("#generalManager").find("label");
		var generalManagerValue='';
		if ($generalManager.length){
			$generalManager.each(function(){
				generalManagerValue+=$(this).attr("code")+",";
			});
		}

		//获取财务主管的审批标签记录
		var $financialExecutive=$("#financialExecutive").find("label");
		var financialExecutiveValue='';
		if ($financialExecutive.length){
			$financialExecutive.each(function(){
				financialExecutiveValue+=$(this).attr("code")+",";
			});
		}

		//获取出纳的审批标签记录
		var $cashier=$("#cashier").find("label");
		var cashierValue='';
		if ($cashier.length){
			$cashier.each(function(){
				cashierValue+=$(this).attr("code")+",";
			});
		}

		//获取审核的审批标签记录
		var $reviewer=$("#reviewer").find("label");
		var reviewerValue='';
		if ($reviewer.length){
			$reviewer.each(function(){
				reviewerValue+=$(this).attr("code")+",";
			});
		}

		var configMap='{"1":"'+executiveValue+'","2":"'+generalManagerValue+'","3":"'+financialExecutiveValue+'","4":"'+cashierValue+'","5":"'+reviewerValue+'"}';
		var contextPath ="${ctx}";
		$.ajax({
			type: "POST",
			async:false,
			url: contextPath + "/review/receipt/save",
			dataType:"json",
			data:{
				receiptType : receiptType,
				configMap : configMap
			},
			success : function(result) {
				var data = eval(result);
				//保存成功直接跳转到设计页面
				if(data.code==0){
					window.open (contextPath + "/review/receipt/index","_self");
				}else{
					var tips = data.message;
					top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
					top.$('.jbox-body .jbox-icon').css('top','55px');
				}
			}
		});

	}
</script>
	<style type="text/css">
		.remove {
			margin-left: 10px;
			cursor: pointer;
			color: blue;
		}

		.approve-bill-list label {
			margin-left: 5px;
		}
	</style>
</head>

<body>
<!--右侧内容部分开始-->
<table class="print-base-form">
	<thead>
	<tr>
		<th class="" colspan="8"></th>
	</tr>
	<tr>
		<th class="" colspan="8">支 出 凭 单</th>
	</tr>
	<tr>
		<th class=" tl" colspan="4">填写日期：<em class="kong_block"></em>年<em class="kong_block"></em>月<em class="kong_block"></em>	日</th>
		<th class=" tr " colspan="4">第<em class="kong_block"></em>号</th>
	</tr>
	</thead>
	<tbody>
	<tr>
		<td class="tc f2" width="120">团号</td>
		<td class=" f3" colspan="2">&nbsp;</td>
		<td class="tc f2" width="163">款项</td>
		<td class=" f3" colspan="2">&nbsp;</td>
		<td class="tc f2" width="85">经办人</td>
		<td class=" f3" width="121">&nbsp;</td>
	</tr>
	<tr>
		<td class="tc f2">摘要</td>
		<td class=" f3 paddl" colspan="7">&nbsp;</td>
	</tr>
	<tr>
		<td class="tc f2">收款单位</td>
		<td class=" f3" colspan="7">
			<div class="duohangnormal"></div>
		</td>
	</tr>
	<tr>
		<td class="tc f2">人民币</td>
		<td width="114" class=" f3 ">&nbsp;</td>
		<td width="81" class="tc f2 ">美元<span class="tc f2"></span></td>
		<td class="tc f3">汇率</td>
		<td width="71" class=" f3">&nbsp;</td>
		<td width="109" class="tc f2">加币</td>
		<td class="tc f3">汇率</td>
		<td class=" f3">&nbsp;</td>
	</tr>
	<tr>
		<td class="tc f2">合计人民币</td>
		<td class=" f3" colspan="4">&nbsp;</td>
		<td class="tc f3">￥</td>
		<td class=" f3" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="tc f2">领款人</td>
		<td class=" f3" colspan="3">&nbsp;</td>
		<td colspan="4"   colspan="3" class=" tc approve-bill-list-td">
			<div class="input-appends tl">
				主管审批<input class="btn"  value="选择" onclick="jbox__pop_select_duty_fab(this);" type="button">
			</div>
			<div class="approve-bill-list" id="executive">
				<c:if test="${not empty configurationMap['1']}">
					<c:forEach var="reviewerKey" items="${configurationMap['1']}">
						<label code="${reviewerKey}">
							<em>${sysJobMap[reviewerKey].name}</em>
							<span class="remove">x</span>
						</label>
					</c:forEach>
				</c:if>
			</div>
		</td>
	</tr>
	<!--<tr>
        <td colspan="2" class="tc ">总经理<span class="tc f2">：</span></td>
        <td colspan="2" class="tc f2">财务主管：</td>
        <td colspan="2" class="tc f2">出纳：</td>
        <td colspan="2" class="tc f2">审核：</td>
    </tr>-->
	<tr>
		<td colspan="2" class=" tc approve-bill-list-td">
			<div class="input-appends">
				总经理<input class="btn"  value="选择" onclick="jbox__pop_select_duty_fab(this);" type="button">
			</div>
			<div class="approve-bill-list" id="generalManager">
				<c:if test="${not empty configurationMap['2']}">
					<c:forEach var="reviewerKey" items="${configurationMap['2']}">
						<label code="${reviewerKey}">
							<em>${sysJobMap[reviewerKey].name}</em>
							<span class="remove">x</span>
						</label>
					</c:forEach>
				</c:if>
			</div>
		</td>
		<td colspan="2" class=" tc approve-bill-list-td">
			<div class="input-appends">
				财务主管<input class="btn"  value="选择" onclick="jbox__pop_select_duty_fab(this);" type="button">
			</div>
			<div class="approve-bill-list" id="financialExecutive">
				<c:if test="${not empty configurationMap['3']}">
					<c:forEach var="reviewerKey" items="${configurationMap['3']}">
						<label code="${reviewerKey}">
							<em>${sysJobMap[reviewerKey].name}</em>
							<span class="remove">x</span>
						</label>
					</c:forEach>
				</c:if>
			</div>
		</td>
		<td colspan="2" class=" tc approve-bill-list-td">
			<div class="input-appends">
				出纳 :<input class="btn"  value="选择" onclick="jbox__pop_select_duty_fab(this);" type="button">
			</div>
			<div class="approve-bill-list" id="cashier">
				<c:if test="${not empty configurationMap['4']}">
					<c:forEach var="reviewerKey" items="${configurationMap['4']}">
						<label code="${reviewerKey}">
							<em>${sysJobMap[reviewerKey].name}</em>
							<span class="remove">x</span>
						</label>
					</c:forEach>
				</c:if>
			</div>
		</td>
		<td colspan="2" class=" tc approve-bill-list-td">
			<div class="input-appends">
				审批<input class="btn"  value="选择" onclick="jbox__pop_select_duty_fab(this);" type="button">
			</div>
			<div class="approve-bill-list" id="reviewer">
				<c:if test="${not empty configurationMap['5']}">
					<c:forEach var="reviewerKey" items="${configurationMap['5']}">
						<label code="${reviewerKey}">
							<em>${sysJobMap[reviewerKey].name}</em>
							<span class="remove">x</span>
						</label>
					</c:forEach>
				</c:if>
			</div>
		</td>
	</tr>
	</tbody>
</table>
<div class="release_next_add">
	<input class="btn btn-primary" value="保 存" onclick="save()" type="button">

</div>
<!--右侧内容部分结束-->
<div id="pop_select_duty_fab">
	<div class="pop_select_duty_fab">
		<ul>
			<c:forEach var="sysJob" items="${sysJobs}">
			<li>
				<input type="checkbox">
				<em code="${sysJob.id}">${sysJob.name}</em>
			</li>
			</c:forEach>
		</ul>
	</div>
</div>
</body>
</html>