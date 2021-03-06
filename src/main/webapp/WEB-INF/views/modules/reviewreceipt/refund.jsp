<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审批优化-审批配置界面-单据-退款单</title>
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

		//获取财务的审批标签记录
		var $financial=$("#financial").find("label");
		var financialValue='';
		if ($financial.length){
			$financial.each(function(){
				financialValue+=$(this).attr("code")+",";
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
		// 获取经理的审批标签记录
		var $manager=$("#manager").find("label");
		var managerValue='';
		if ($manager.length){
			$manager.each(function(){
				managerValue+=$(this).attr("code")+",";
			});
		}

		var configMap='{"1":"'+financialValue+'","2":"'+reviewerValue+'","3":"'+managerValue+'"}';
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
		<th class="wd">&nbsp;</th>
		<th class="wd">&nbsp;</th>
		<th width="124" class="wd">&nbsp;</th>
		<th width="91" class="wd">&nbsp;</th>
		<th width="122" class="wd">&nbsp;</th>
		<th width="132" class="wd">&nbsp;</th>
		<th width="119" class="wd">&nbsp;</th>
		<th class="wd">&nbsp;</th>
	</tr>
	<tr>
		<th class="" colspan="8">退款单</th>
	</tr>
	<tr>
		<th class=" tl" colspan="5">填写日期：<em class="kong_block"></em>年<em class="kong_block"></em>月<em class="kong_block"></em>	日</th>
		<th class=" tr " colspan="3">第<em class="kong_block"></em>号</th>
	</tr>
	</thead>
	<tbody>
	<tr>
		<td width="112" class="tc ">团号</td>
		<td width="112" class="tc ">&nbsp;</td>
		<td class="tc  paddl">人数</td>
		<td class="tc  paddl">&nbsp;</td>
		<td class="tc  paddl">出发日期</td>
		<td class="tc  paddl">&nbsp;</td>
		<td class="tc  paddl">返回日期</td>
		<td class="  paddl">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2" class="tc ">产品路线</td>
		<td class=" " colspan="6">
			<div class="duohangnormal"></div>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="tc ">应收金额</td>
		<td class=" ">&nbsp;</td>
		<td class="tc ">已收金额</td>
		<td class="tc ">&nbsp;</td>
		<td class="tc ">退款金额</td>
		<td colspan="2" class="tc ">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2" class=" tc">退款说明</td>
		<td colspan="2" class=" tc">&nbsp;</td>
		<td colspan="2" class=" tc">退款金额(大写)</td>
		<td colspan="2" class=" tc">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2" class=" tc">客户</td>
		<td colspan="2" class=" tc">&nbsp;</td>
		<td colspan="2" class=" tc">电话</td>
		<td colspan="2" class=" tc">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2" class=" tc">收款人全称</td>
		<td colspan="2" class=" tc">&nbsp;</td>
		<td colspan="2" class=" tc">卡户行/账号</td>
		<td colspan="2" class=" tc">&nbsp;</td>
	</tr>
	<!--<tr>
        <td colspan="2" class=" tc">经办人：</td>
        <td colspan="2" class=" tc">财务：</td>
        <td colspan="2" class=" tc">审批：</td>
        <td colspan="2" class=" tc">总经理：</td>
    </tr>-->
	<tr>
		<td colspan="2" class=" tc approve-bill-list-td">
			<div class="input-appends">
				经办人
			</div>
			<div class="approve-bill-list">
			</div>
		</td>
		<td colspan="2" class=" tc approve-bill-list-td">
			<div class="input-appends">
				财务<input class="btn"  value="选择" onclick="jbox__pop_select_duty_fab(this);" type="button">
			</div>
			<div class="approve-bill-list" id="financial">
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
		<td colspan="2" class=" tc approve-bill-list-td">
			<div class="input-appends">
				审批 <input class="btn"  value="选择" onclick="jbox__pop_select_duty_fab(this);" type="button">
			</div>
			<div class="approve-bill-list" id="reviewer">
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
				总经理 <input class="btn"  value="选择" onclick="jbox__pop_select_duty_fab(this);" type="button">
			</div>
			<div class="approve-bill-list" id="manager">
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