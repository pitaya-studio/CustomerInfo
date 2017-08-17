var sysCtx;
var departs;
var anArray = [];
//ztree 设置
var setting = {
	check : {
		enable : true
	},
	data : {
		simpleData : {
			enable : true
		}
	}
};
setting.check.chkboxType = {
	"Y" : "",
	"N" : ""
}; //取消子父节点关联选中效果

function toggleDiv() {
	if ($("#userType").val() == 1) {
		$("#agentdiv").show();
		$("#agent\\.id").attr("class", "required");
	} else if ($("#userType").val() != 1) {
		$("#agentdiv").hide();
		$("#agent\\.id").val('');
		$("#agent\\.id").attr("class", "");
	}
	if ($("#userType").val() != "") {
		$("#roleTitle").show();
	} else {
		$("#roleTitle").hide();
	}
}

$(document).ready(function () {
	departs = $("#departmentJson").val();
	var roleIds = $("#roleIds").val().replace(" ","");
	anArray = eval("[" + roleIds + "]");
	sysCtx = $("#sysCtx").val();
	inquiryCheckBOX();
	var $oldRoleList = $('.sys_adduser_check').children().clone();

	//$("#loginName").focus();
	$("#inputForm").validate({
		rules : {
			loginName : {
				required : true,
				remote : sysCtx + "/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent($("#hiddenLoginName").val()) + "&dom=" + Math.random()
			},
			no : {
				required : true,
				number : true
			},
			name : "required",
			email : "email",
			mobile : {
				required : true
			},
			newPassword : "valiSpace",
			userType : "required",
			'agent.id' : "required",
			groupeSurname : {
				required : true,
				alnum : true,
				remote : sysCtx + "/sys/user/checkGroupeSurname?oldGroupeSurname=" + encodeURIComponent($("#hiddenGroupeSurname").val()) + "&dom=" + Math.random()
			}
		},
		messages : {
			loginName : {
				required : "必填信息",
				onlyLetterNumber : "包含非法字符",
				remote : "用户登录名已存在"
			},
			no : {
				required : "必填信息",
				number : "请输入数字"
				//                        remote:"工号已存在"
			},
			name : "必填信息",
			email : "请输入正确的邮箱",
			mobile : {
				required : "必填信息"

			},
			userType : "必填信息",
			newPassword : {
				required : "必填信息",
				minlength : "密码不能小于3个字符"
			},
			confirmNewPassword : {
				minlength : "确认密码不能小于3个字符",
				equalTo : "输入与上面相同的密码"
			},
			'agent.id' : "必填信息",
			groupeSurname : {
				required : "必填信息",
				alnum : "包含非法字符",
				remote : "团号姓氏缩写已存在"
			}
		},
		submitHandler : function (form) {
			/*loading('正在提交，请稍等...');
			form.submit();*/
			//IE10不显示图标问题，临时修复bug的解决方案
			window.setTimeout(function () { $.jBox.tip('正在提交，请稍等...', 'loading');}, 0);
			form.submit();
		},
		errorContainer : "#messageBox",
		errorPlacement : function (error, element) {
			$("#messageBox").text("输入有误，请先更正。");
			if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append"))
				error.appendTo(element.parent().parent());
			else if (element.attr("id") == 'agent.id')
				error.appendTo(element.parent());
			else
				error.insertAfter(element);
		}

	});
	toggleDiv();
	function toggleCheckboxes() {
		$("#control_xon span").each(function () {
			if ($(this).attr("userType") == $("#userType").val()) {
				var obj = $(this).children(":checkbox");
				var val = obj.val();
				$.each(anArray, function (n, value) {
					if (val == value) {
						obj.attr("checked", 'true');
						return false;
					}
				});
				$(this).show();
			} else {
				var obj = $(this).children(":checkbox");
				obj.removeAttr("checked");
				$(this).hide();
			}
		});
	}
	toggleCheckboxes();
	$("#userType").change(function () {
		toggleDiv();
		toggleCheckboxes();
	});
	//密码框提示
	$("#newPassword").focusin(function () {
		var obj_this = $(this);
		obj_this.next("span").hide();
	}).focusout(function () {
		var obj_this = $(this);
		if (obj_this.val() != "") {
			obj_this.next("span").hide();
		} else
			obj_this.next("span").show();
	});
	if ($("#newPassword").val() != "") {
		$("#newPassword").next("span").hide();
	}
	jQuery.validator.addMethod("valiSpace", function (value, element) {
		return value.indexOf(" ") <= -1;
	}, $.validator.format("输入信息包含非法字符"));
	jQuery.extend(jQuery.validator.messages, {
		required : "必填信息",
		digits : "请输入正确的数字",
		number : "请输入正确的数字价格"
	});
	jQuery.validator.addMethod("alnum", function (value, element) {
		return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
	}, "包含非法字符");

	if ($("#userId").val() == 1) {
		$("#userType").change(function () {
			if ($("#userId").val() == 1) {
				if ($('#userType').val() == 1 || $('#userType').val() == "") {
					$('#companyList').hide();
					if ($('#userType').val() == "") {
						$(".sys_adduser_check").hide();
					} else {
						$(".sys_adduser_check").show();
					}
				} else {
					$('#companyList').show();
					$(".sys_adduser_check").show();
				}
			}
		});
	}
	$(".companySelect").change(function () {
		var companyId = $(".companySelect option:selected").val();

		$.ajax({
			type : "POST",
			url : sysCtx + "/sys/user/roleList",
			data : {
				companyId : companyId
			},
			success : function (msg) {
				$("#control_xon").children().remove();
				$("#control_xon").append($(msg));
				$("#control_xon").find("[usertype='1']").hide();
			}
		});
		if ($("#sys_adduser_check").find("#roleTitle").attr("class") != "old") {
			$("#agentdiv").next().next().children().remove();
			$("#agentdiv").next().next().append($oldRoleList);
		}
	});

	//------------------------------20151225 add begin--------------------------------//
	var isSecondSelect = false; //修改页面时，取消选中'审批督查'框 ，视为清空所有选择，再次选中时不再加载选中状态

	$(document).on('change', '[name="approSupervision"]', function () {
		var $this = $(this);
		$('[name="approvalExaminationError"]').hide();
		if ($this.attr('checked') == 'checked') {
			if (!isSecondSelect) {
				// 初始化树结构
				var tree = $.fn.zTree.init($("#departTree"), setting, departs);
				tree.expandAll(true);
				// 默认选择节点
				var ids = deptIdStr.split(",");
				for (var i = 0; i < ids.length; i++) {
					var node = tree.getNodeByParam("id", ids[i]);
					try {
						tree.checkNode(node, true, false);
					} catch (e) {}
				}
			}
			if (isSecondSelect) {
				var treeObj = $.fn.zTree.getZTreeObj("departTree");
				treeObj.checkAllNodes(false); //清空树所有选中节点
			}
			$this.next().show().parent().addClass('active').parents('.top:first').next().show();
		} else {
			$this.next().hide().parent().removeClass('active').parents('.top:first').next().hide().find('input[type="checkbox"]:checked').removeAttr('checked');
			$('#productType').find('p').each(function () {
				$(this).removeClass('on');
			});
			$('#processType').find('p').each(function () {
				$(this).removeClass('on');
			});

			isSecondSelect = true;
		}
	});

	//修改账号时，若部门-产品类型-流程类型内容不为空，展开
	var deptIdStr = $("#selectIds").val();
	if (deptIdStr != "") {
		$("#approSupervision").attr('checked', 'checked');
		// 初始化树结构
		var tree = $.fn.zTree.init($("#departTree"), setting, departs);
		tree.expandAll(true);
		// 默认选择节点
		var ids = deptIdStr.split(",");
		for (var i = 0; i < ids.length; i++) {
			var node = tree.getNodeByParam("id", ids[i]);
			try {
				tree.checkNode(node, true, false);
			} catch (e) {}
		}

		$("#approSupervision").next().show().parent().addClass('active').parents('.top:first').next().show();
	}

	$(document).on('change', '[name="salerId"]', function () {
		getDeptAndDuties();
		if (!validateDeptAndDuty()) {
			$.jBox.tip('“部门-职务”重复，请修改', 'error');
		}
	});
	$(document).on('change', '[name="departmentId"]', function () {
		getDeptAndDuties();
		if (!validateDeptAndDuty()) {
			$.jBox.tip('“部门-职务”重复，请修改', 'error');
		}
	});

	//------------------------------20151225 add end--------------------------------//

	//****************************20151117 add  begin******************************//

	$(document).on('click', '.add_post_department_s em', function () {

		var $newOne = $(this).parent().parent().clone();
		$newOne.find('input').val('');
		$newOne.find('select').val('');

		$newOne.find('input').val();

		$newOne.find('i').show();
		$('.add_post_department_s_container').append($newOne);
	})
	$(document).on('click', '.add_post_department_s i', function () {
		$(this).parent().parent().remove();
	})

	$(document).on('click',
		'.productName input,.workflowName input', function () {
		var $checkbox = $(this);
		if ($checkbox.is(':checked')) {
			$checkbox.parent().addClass('on');
			$checkbox.parent().removeClass('off');
		} else {
			$checkbox.parent().addClass('off');
			$checkbox.parent().removeClass('on');
		}
	})

	$(document).on('click', '.departmentButton', function () {
		var $currentClick = $(this);
		var url = "/sys/department/treeData?officeId=" + $("#deptId").val();
		// 正常打开
		top.$.jBox.open("iframe:" + sysCtx + "/tag/treeselect4User?url=" + encodeURIComponent(url) + "&module=&checked=&extId=&selectIds=" + $currentClick.prev().prev().val(), "选择部门", 300, 420, {
			buttons : {
				"确定" : "ok",
				"关闭" : true
			},
			submit : function (v, h, f) {
				if (v == "ok") {
					var tree = h.find("iframe")[0].contentWindow.tree; //h.find("iframe").contents();
					var ids = [],
					names = [],
					nodes = [];
					if ("" == "true") {
						nodes = tree.getCheckedNodes(true);
					} else {
						nodes = tree.getSelectedNodes();
					}
					for (var i = 0; i < nodes.length; i++) { //
						ids.push(nodes[i].id);
						names.push(nodes[i].name); //
						break; // 如果为非复选框选择，则返回第一个选择
					}
					$currentClick.prev().val(names);
					$currentClick.prev().prev().val(ids);
					//				 					$("#departmentId").val(ids);
					//				 					$("#departmentName").val(names);
					$("#departmentName").focus();
					$("#departmentName").blur();
				}
			},
			loaded : function (h) {
				$(".jbox-content", top.document).css("overflow-y", "hidden");
			},
			persistent : true
		});
	});
	// 添加用户时，去除邮箱和密码默认值（浏览器兼容问题，本不应该有默认值）


});

/*部门-职务 重复校验   begin*/
var deptAndDuties = [];
function getDeptAndDuties() {
	deptAndDuties = [];
	$('[name="departmentId"]').each(function () {
		var deptAndDuty = {};
		deptAndDuty.dept = $(this).val();
		deptAndDuty.duty = $(this).parents('li:first').next().find('[name="salerId"]').val();
		deptAndDuties.push(deptAndDuty);
	});
}
var validateDeptAndDuties;
//重复的
var repeatDeptAndDuties;
function validateDeptAndDuty() {
	validateDeptAndDuties = {};
	repeatDeptAndDuties = {};
	for (var i = 0, len = deptAndDuties.length; i < len; i++) {
		var dept = deptAndDuties[i].dept;
		var duty = deptAndDuties[i].duty;
		if (!dept || !duty) {
			continue;
		}
		var validateDeptAndDuty = validateDeptAndDuties[dept];
		if (validateDeptAndDuty) {
			if (validateDeptAndDuty[duty]) {
				repeatDeptAndDuties = {
					dept : dept,
					duty : duty
				};
				return false;
			} else {
				validateDeptAndDuty[duty] = 1;
			}
		} else {
			validateDeptAndDuties[dept] = {};
			validateDeptAndDuties[dept][duty] = 1;
		}
	}
	return true;
}
/*部门-职务 重复校验   end*/

/*
获取选择的部门id列表
 */
function getSelectedDept() {
	var depts = [];
	var treeObj = $.fn.zTree.getZTreeObj("departTree");
	var nodes = treeObj.getCheckedNodes(true);
	for (var i = 0; i < nodes.length; i++) {
		var node = nodes[i];
		//var children=node.children;
		//if(children.length==0){
		depts.push(node.id);
		//}
	}
	return depts;
}

/* 获取产品类型列表  */
function getSelectedProductType() {
	var productTypes = "";
	$("#productType").find("input[type='checkbox']:checked").each(function () {
		productTypes += $(this).val() + ",";
	});
	productTypes = productTypes.substr(0, productTypes.length - 1);
	return productTypes;
}

/* 获取流程类型列表  */
function getSelectedProcessType() {
	var processTypes = '';
	$("#processType").find("input[type='checkbox']:checked").each(function () {
		processTypes += $(this).val() + ",";
	});
	processTypes = processTypes.substr(0, processTypes.length - 1);
	return processTypes;
}

function saveUser() {
	var companyId =$("#companyId").val();
	var isSubmitFlag = true; //是否满足提交表单条件 ，默认满足

	var oldLoginName = encodeURIComponent($("#hiddenLoginName").val());
	var loginName = $("#loginName").val();
	var oldGroupeSurname = encodeURIComponent($("#hiddenGroupeSurname").val());
	var groupeSurname = $("#groupeSurname").val();
	var roleIds = "";
	// 如果没有选择报名，则值为0
	if ($("#quauqBookOrderPermission").is(':checked')) {
		$("#quauqBookOrderPermission").val("1");
	} else {
		$("#quauqBookOrderPermission").val("0");
	}
	// 如果没有选，则值为0
	if ($("#hasPricingStrategyPermission").is(':checked')) {
		$("#hasPricingStrategyPermission").val("1");
	} else {
		$("#hasPricingStrategyPermission").val("0");
	}
	$("[name=roleIdList]:checked").each(function (index, obj) {
		roleIds += $(this).val() + ",";
	});
	$.ajax({
		type : "POST",
		url : sysCtx + "/sys/user/validateUser?oldLoginName=" + encodeURIComponent($("#hiddenLoginName").val()) + "&loginName=" + loginName + "&oldGroupeSurname=" + encodeURIComponent($("#hiddenGroupeSurname").val()) + "&groupeSurname=" + groupeSurname + "&roleIds=" + roleIds + "&dom=" + Math.random(),
		data : {},
		async : false,
		success : function (msg) {
			if ("true" == msg) {
				//保存账号权限
				var values = "";
				$("#reviewLisenceUL").find("input[type=checkbox][name=is_jump_task_permit]").each(function () {
					if ($(this).attr("checked")) {
						var lisenceValue = $(this).attr("lisenceValue");
						values += lisenceValue + ",";
					}
				});
				$("#lisenceValue").val(values);
				//为丢失的批发商Id赋值
				$("#companyId").val(companyId);
				
				var relation = getDepartAndPosition(); //获取选择的部门-职务关系
				$("#relationValue").val(relation);

				getDeptAndDuties();
				var isRepeat = validateDeptAndDuty();
				$('[name="departmentId"]').each(function () {
					var dept = $(this).val();
					var duty = $(this).parents('li:first').next().find('[name="salerId"]').val();
					var $deptArea = $(this).parents('li:first');
					$deptArea.find('.error').remove();
					var $dutyArea = $(this).parents('li:first').next();
					$dutyArea.find('.error').remove();
					if (!isRepeat) {
						if (dept == repeatDeptAndDuties.dept && duty == repeatDeptAndDuties.duty) {
							$deptArea.append('<label for="newPassword" class="error">“部门-职务”重复</label>');
							$dutyArea.append('<label for="newPassword" class="error">“部门-职务”重复</label>');
							isSubmitFlag = false;
						}
					} else if (!dept || duty == '') {
						if (!dept) {
							$deptArea.append('<label for="newPassword" class="error">必填信息</label>');
							isSubmitFlag = false;
						}
						if (duty == '') {
							$dutyArea.append('<label for="newPassword" class="error">必填信息</label>');
							isSubmitFlag = false;
						}
					}

				});

				if ($('[name="approSupervision"]:checked').length > 0) { //审批监督框选中
					//保存部门-产品类型-流程类型关系
					var depts = getSelectedDept();
					var productTypes = getSelectedProductType();
					var processTypes = getSelectedProcessType();
					$("#saveDepartTree").val(depts);
					$("#saveProductType").val(productTypes);
					$("#saveReviewFlowType").val(processTypes);

					//$("#isSave").val("Y");//审批监督框选中状态
					if (!validateApprovalExamination(depts, productTypes, processTypes)) {
						isSubmitFlag = false;
					}
				}

				if (isSubmitFlag) {
					$("#inputForm").submit();
				}

			} else {
				top.$.jBox.info(msg, "警告");
			}
		}
	});
}

function getDepartAndPosition() {

	var deptAndPos = '';
	$('.add_post_department_s').each(function () {

		deptAndPos += $(this).find('input[name="departmentId"]').val() + '-' + $(this).find('select[name="salerId"] option:selected').val() + ',';

	});
	deptAndPos = deptAndPos.substr(0, deptAndPos.length - 1);
	return deptAndPos;
}

function validateApprovalExamination(depts, productTypes, processTypes) {
	//判断审批督查是否被选中
	if ($('[name="approSupervision"]:checked').length > 0) {
		if (!((depts == '' && productTypes == '' && processTypes == '') || (depts != '' && productTypes != '' && processTypes != ''))) {
			$('[name="approvalExaminationError"]').show();
			return false;
		}
	}
	return true;
}
//****************************20151117 add  end******************************//
