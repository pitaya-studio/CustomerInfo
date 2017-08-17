<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审批配置-详细页面</title>
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
<script type="text/javascript" src="${ctxStatic}/review/configuration/reviewConfiguration.js"></script>
<script type="text/javascript">
	//选择审批后显示成本付款审批
	$(document).on('change','[name="reviewFlowType"]',function(){
		if($('[name="reviewFlowType"][value="18"]:checked').length>0){
			$('[name="costPaymentApproval"]').show();
		}else{
			$('#paymentEqualsCost').removeAttr('checked');
			$('[name="costPaymentApproval"]').hide();
		};
	})
	$(document).ready(function(){
		$(".main-flow-container .title input[name='edit']").click(function(){
			$("#btnReturn").hide();
			$("#btnSave").show();
			$("#btnSaveAndReturn").show();
		});
	});
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
		//是否显示成本金额等于实际成本
		if($('[name="reviewFlowType"][value="18"]:checked').length>0) {
			$('[name="costPaymentApproval"]').show();
		}else{
			$('[name="costPaymentApproval"]').hide();
		}
		//显示保存按钮
		$("#btnReturn").show();
		$("#btnSave").hide();
		$("#btnSaveAndReturn").hide();
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

	function setSpecial() {
		var isCheck = $(document).find('#special').is(':checked');
		if (isCheck) {
			$(document).find('#special').parent().siblings().find('input')
					.removeAttr('disabled');
			$(document).find('#special').parent().siblings().find('label')
					.removeClass('disabled');
		} else {
			$(document).find('#special').parent().siblings().find('input')
					.attr('disabled', 'disabled');
			$(document).find('#special').parent().siblings().find('label')
					.addClass('disabled');
			$(document).find('#special').parent().siblings().find('input')
					.removeAttr('checked');
		}
	}
	$(document).ready(function() {
		setSpecial()
	})
	$(document).on('click', '#special', function() {
		setSpecial()
	})
</script>
<script type="text/javascript">
	//ztree 设置
	var setting = {

		check : {
			enable : true,
			chkboxType : { "Y" : "", "N" : "" }
		},
		data : {
			simpleData : {
				enable : true
			}
		}
	};


	var departs=${departmentJson};
	$(function() {
		var tree = $.fn.zTree.init($("#departTree"), setting, departs);
		tree.expandAll(true);
		//初始化时将配置的部门选中
		var nodes = tree.transformToArray(tree.getNodes());
		var depts=${depts};
		for(var i=0;i<nodes.length;i++){
			var node=nodes[i];
			var u=depts.indexOf(node.id);
			if(u!=-1){
				tree.checkNode(node, true, false);
			}
			tree.setChkDisabled(node, true);
		}
	});
</script>
</head>

<body>
	<!--右侧内容部分开始-->
	<!--查询结果筛选条件排序开始-->
	<!--右侧内容部分开始--><!--查询结果筛选条件排序开始-->
                        <div class="main-flow-container">
							<div class="process-ico-step-third"></div>
                            <div class="title">
                                <span>流程定义</span>
                                <em>
                                    <input value="编辑" onclick="edit()" class="btn btn-primary " name="edit"
                                           type="button">
                                </em>
                            </div>
                            <div class="flow-body-container">
                                <div class="pop-department-pro-flow page">
                                    <table>
                                        <thead>
                                            <tr>
												<th class="td-width-first"><label><img
														src="${ctxStatic}/images/main-ico_approve-1_07.png" />部门</label></th>
												<th class="td-width-second"><label><img
														src="${ctxStatic}/images/main-ico_approve-1_07.png" />产品类型</label></th>
												<th class="td-width-third"><label><img
														src="${ctxStatic}/images/main-ico_approve-1_07.png" />流程名称</label></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td class="vertical-align-top-td">
                                                    <div class="">
                                                        <ul id="departTree" class="ztree"></ul>
                                                    </div>
                                                </td>
												<td class="productName vertical-align-top-td" id="productType">
													<c:forEach items="${productTypeMap}" var="productType">
														<p>
															<input name="productType" type="checkbox" value="${productType.key}" disabled <c:if test="${fns:contains(products,productType.key)}">checked</c:if>/> <label code="${productType.key}">${productType.value}</label>
														</p>								
													</c:forEach>
												</td>
												<td class="workflowName vertical-align-top-td" id="processType">
													<c:forEach items="${reviewFlowTypeMap}" var="reviewFlowType">
														<p>
															<input name="reviewFlowType" type="checkbox" value="${reviewFlowType.key}" disabled <c:if test="${fns:contains(processes,reviewFlowType.key)}">checked</c:if>/> <label code="${reviewFlowType.key}">${reviewFlowType.value}</label>
														</p>									
													</c:forEach>
												</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="special-need">
								<ul>
									<li><label>特殊要求</label></li>
									<li><input id="needNoReview" name="specialType" type="checkbox" disabled value=0 <c:if test="${processKey eq needNoReviewProcessKey}">checked</c:if> /><label>无需审批</label></li>
									<%--<li><input name="specialType" type="checkbox" value=1 /><label>可选起点</label></li>--%>
									<li class="display-none" ><input id="multiApplyPermit" name="specialType" type="checkbox" disabled value=2 <c:if test="${(not empty reviewConfig) and (reviewConfig.isMultiApplyPermit eq 0)}">checked</c:if> /><label>再次申请受限</label></li>
								</ul>
                            </div>
							<div class="special-need">
								<ul  name="costPaymentApproval">
									<li><label>成本付款审批:</label></li>
									<li><input id="paymentEqualsCost" name="specialType" type="checkbox" value="3" <c:if test="${(not empty reviewCostPaymentConfiguration) and (reviewCostPaymentConfiguration.isPaymentEqualsCost eq 1)}">checked</c:if> /><label>付款申请金额须等于实际成本金额</label></li>
								</ul>
							</div>
							<c:if test="${processKey ne needNoReviewProcessKey}">
                            <div class="title">
                                <span>流程配置</span>
                                <em>
                                    <input value="编辑" onclick="editModel( '${ctx}','${needNoReviewProcessKey}','${modelId}','${processKey}','${serialNumber}' )" class="btn btn-primary "
                                           type="button">
                                </em>
                            </div>
                            <div class="flow-body-container">
                                <div class="block" >
                                    <img src="${ctx}/sys/review/configuration/model/diagram/${modelId}" onerror="javascript:this.src='${ctxStatic}/images/configure-flow-no-pic.jpg';"/>
                                </div>
                            </div>
							</c:if>
                        </div>
                        <div class="dbaniu">
							<a class="ydbz_s" id="btnReturn" href="${ctx}/sys/review/configuration/index" >返回</a>
                            <a class="ydbz_s" id="btnSave"  href="javascript:void(0);" onclick="modifyReviewDefinition('${ctx}','${processKey}','${serialNumber}','${modelId}')">保存</a>
							<a class="ydbz_s" id="btnSaveAndReturn" href="javascript:void(0);" onclick="modifyReviewDefinitionAndReturn('${ctx}','${processKey}','${serialNumber}','${modelId}')">保存并返回</a>
                        </div>
		<!--查询结果筛选条件排序结束-->
		<!--右侧内容部分结束-->
</body>
</html>