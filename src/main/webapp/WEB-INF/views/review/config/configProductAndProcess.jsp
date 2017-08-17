<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>审批配置</title>
<meta name="decorator" content="wholesaler" />
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<link rel="stylesheet"	href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript">
	$(function() {
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
		//取消一个checkbox 就要联动取消 全选的选中状态
		$("input[name='ids']").click(function() {
			if ($(this).attr("checked")) {

			} else {
				$("input[name='allChk']").removeAttr("checked");
			}
		});

		var resetSearchParams = function() {
			$(':input', '#searchForm').not(':button, :submit, :reset, :hidden')
					.val('').removeAttr('checked').removeAttr('selected');
			$('#country').val("");
		}
		$('#contentTable')
				.on(
						'change',
						'input[type="checkbox"]',
						function() {
							if ($('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length) {
								$('[name="allChk" ]').attr('checked', true);
							} else {
								$('[name="allChk" ]').removeAttr('checked');
							}
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
		$contentTable.find('input[type="checkbox"]').each(function() {
			var $checkbox = $(this);
			if ($checkbox.is(':checked')) {
				$checkbox.removeAttr('checked');
			} else {
				$checkbox.attr('checked', true);
			}
		});
	}
	$(document).ready(
			function() {
				$(document).on('click',
						'.productName input,.workflowName input', function() {
							var $checkbox = $(this);
							if ($checkbox.is(':checked')) {
								$checkbox.parent().addClass('on');
								$checkbox.parent().removeClass('off');
							} else {
								$checkbox.parent().addClass('off');
								$checkbox.parent().removeClass('on');
							}
						})

				$(document).find('.productName input,.workflowName input')
						.each(function() {
							var $checkbox = $(this);
							if ($checkbox.is(':checked')) {
								$checkbox.parent().addClass('on');
								$checkbox.parent().removeClass('off');
							} else {
								$checkbox.parent().addClass('off');
								$checkbox.parent().removeClass('on');
							}
						});
			});

</script>
<script type="text/javascript">

	/* 获取产品类型列表  */
	function getSelectedProductType(){
		var productTypes=[];
		var nodes=$("#productType .on >input");
		for(var i=0;i<nodes.length;i++){
			var node=nodes[i];
			productTypes.push(node.value);
		}
		return productTypes;
	}

	/* 获取流程类型列表  */
	function getSelectedProcessType(){
		var processTypes=[];
		var nodes=$("#processType .on >input");
		for(var i=0;i<nodes.length;i++){
			var node=nodes[i];
			processTypes.push(node.value);
		}
		return processTypes;
	}

	/* 保存流程配置*/
	function save(){
		var contextPath="${ctx}";
		var productTypes=getSelectedProductType();
		var processTypes=getSelectedProcessType();
		if (productTypes.length==0||processTypes.length==0){
			var tips = "您尚未选择 产品类型/流程名称";
			top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return ;
		}
		$.ajax({
			type: "POST",
			async:false,
			url: contextPath + "/sys/review/configuration/saveProductAndProcess",
			dataType:"json",
			data:{
				productTypes : productTypes,
				processTypes : processTypes
			},
			success : function(result) {
				debugger;
				var data = eval(result);
				//保存成功直接跳转到设计页面
				if(data.code==0){
					window.open (contextPath + "/sys/review/configuration/index","_self");
				}else{
					var tips = data.message;
					top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
					top.$('.jbox-body .jbox-icon').css('top','55px');
				}
			}
		});
	}

</script>
</head>

<body>
	<!--右侧内容部分开始-->
	<!--查询结果筛选条件排序开始-->
	<div class="main-flow-container">
		<div class="title">
			<span>产品和流程配置</span>
		</div>
		<div class="flow-body-container">
			<div class="pop-department-pro-flow">
				<table>
					<thead>
						<tr>
							<th class="td-width-second"><label><img
									src="${ctxStatic}/images/main-ico_approve-1_07.png" />产品名称</label></th>
							<th class="td-width-third"><label><img
									src="${ctxStatic}/images/main-ico_approve-1_07.png" />流程名称</label></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="productName vertical-align-top-td" id="productType">
								<c:forEach items="${productTypeMap}" var="productType">
									<p>
										<input name="productType" type="checkbox" value="${productType.key}"  <c:if test="${fns:contains(products,productType.key)}">checked</c:if> /> <label code="${productType.key}">${productType.value}</label>
									</p>								
								</c:forEach>
							</td>
							<td class="workflowName vertical-align-top-td" id="processType">
								<c:forEach items="${reviewFlowTypeMap}" var="reviewFlowType">
									<p>
										<input name="reviewFlowType" type="checkbox" value="${reviewFlowType.key}" <c:if test="${fns:contains(processes,reviewFlowType.key)}">checked</c:if>/> <label code="${reviewFlowType.key}">${reviewFlowType.value}</label>
									</p>									
								</c:forEach>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="dbaniu">
		<!--<a class="ydbz_s gray">返回</a>-->
		<%--<a class="ydbz_s" onclick="save()">保存</a>--%>
		<input class="btn btn-primary ydbz_x" type="button" onclick="save()" value="保存">
	</div>
		<!--查询结果筛选条件排序结束-->
		<!--右侧内容部分结束-->
</body>
</html>