<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>数据选择</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<meta http-equiv="Cache-Control" content="no-store">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10">
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
<link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css" />

<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<!-- <script type="text/javascript" src="${ctxStatic}/js/common.js"></script> -->

<script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.all-3.5.jss"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script>
	
<script type="text/javascript">
	$(function() {
		// 获取后台传入的 接收人 json
// 		receivers = receiverJson = getReceiverJson();
		
		//取消一个checkbox 就要联动取消 全选的选中状态
		$("input[name='ids']").click(function() {
			if ($(this).attr("checked")) {

			} else {
				$("input[name='allChk']").removeAttr("checked");
			}
		});
		//              显示历史版本
		$(document).ready(function() {
			$('[name="display-history-img"]').click(function() {
				$(".history-flow").show();
			});
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
	
	function getReceiverJson(){
		$.ajax({
				type : "POST",
				url : "${ctx}/sys/remind/getReceiver",
				async: false,
				success : function(resultMap) {
					if(resultMap){
							receiverJson = resultMap;
					} else {
						top.$.jBox.tip('获取接收人信息失败!', 'error');
					}
				}
			});
			return receiverJson;
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

	// 提醒接收者
	var receivers = ${recStr};

	$(function() {
		var tree = $.fn.zTree.init($("#departTree"), setting, receivers);
		tree.expandAll(false);
	});

	function search() {
		$("#search").slideToggle(200);
		$("#txt").toggle();
		$("#key").focus();
	}
</script>
<style type="text/css">
[class ^="icon-"],[class *=" icon-"] {
	background-position: -48px 0;
	display: inline-block;
	width: 14px;
	height: 14px;
	margin-top: 1px;
	line-height: 14px;
	vertical-align: text-top;
	background-image: url("css/img/glyphicons-halflings.png");
	background-repeat: no-repeat;
}
</style>
</head>
<body>
	<div style="position:absolute;right:28px;top:12px;cursor:pointer;"
		onclick="search();">
		<i class="icon-search"></i><label id="txt">搜索</label>
	</div>
	<div id="search" class="control-group hide"
		style="padding:10px 0 0 15px;">
		<label for="key" class="control-label"
			style="float:left;padding:5px 5px 3px;">关键字：</label> <input
			type="text" class="empty" id="key" name="key" maxlength="50"
			style="width:140px;height:30px;font-family: Verdana,Arial,Helvetica,AppleGothic,sans-serif;">
	</div>
	<div style="padding:30px 20px;">
		<ul id="departTree" class="ztree">
			
		</ul>
	</div>
</body>
</html>