<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审批优化-主配置-单据配置</title>
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
	$(function(){
		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();
		//取消一个checkbox 就要联动取消 全选的选中状态
		$("input[name='ids']").click(function(){
			if($(this).attr("checked")){

			}else{
				$("input[name='allChk']").removeAttr("checked");
			}
		});

		var resetSearchParams = function(){
			$(':input','#searchForm')
					.not(':button, :submit, :reset, :hidden')
					.val('')
					.removeAttr('checked')
					.removeAttr('selected');
			$('#country').val("");
		}
		$('#contentTable').on('change','input[type="checkbox"]',function(){
			if( $('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length ){
				$('[name="allChk" ]').attr('checked',true);
			}else{
				$('[name="allChk" ]').removeAttr('checked');
			}
		});
	});

	//展开收起
	function expand(child, obj) {
		if($(child).is(":hidden")){
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
			$(obj).parents("tr").addClass("tr-hover");

		}else{
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");

		}
	}
	function checkall(obj){
		if($(obj).attr("checked")){
			$('#contentTable input[type="checkbox"]').attr("checked",'true');
			$("input[name='allChk']").attr("checked",'true');
		}else{
			$('#contentTable input[type="checkbox"]').removeAttr("checked");
			$("input[name='allChk']").removeAttr("checked");
		}
	}

	function checkreverse(obj){
		var $contentTable = $('#contentTable');
		$contentTable.find('input[type="checkbox"]').each(function(){
			var $checkbox = $(this);
			if($checkbox.is(':checked')){
				$checkbox.removeAttr('checked');
			}else{
				$checkbox.attr('checked',true);
			}
		});
	}
</script>
</head>

<body>
<!--查询结果筛选条件排序结束-->
<table id="contentTable" class="table mainTable activitylist_bodyer_table">
	<thead>
	<tr>
		<th width="7%">序号</th>
		<th width="42%">单据名称</th>
		<th width="41%">审批流程名称</th>
		<th width="10%">操作</th>
	</tr>
	</thead>
	<tbody>
	<c:if test="${not empty receiptProcesses}">
		<c:forEach var="receiptProcess" items="${receiptProcesses}" varStatus="status">
		<tr id="vertical-align-top2">
			<td class="tc"><label for="checkbox"></label>
				${status.index+1}</td >
			<td class="tl">
				<span>${receiptProcess.receiptDescription}</span>
			</td>
			<td>
				<p>${receiptProcess.processNames}</p>
			</td>
			<td class="tc">
				<%--<a class="btn-primary-approve" href="${ctx}/review/receipt/show/${receiptProcess.receiptType}" target="_blank">数据配置</a>--%>
				<input class="btn btn-primary" type="button" onclick="window.open('${ctx}/review/receipt/show/${receiptProcess.receiptType}')" value="数据配置">
			</td>
		</tr>
		</c:forEach>
	</c:if>
	</tbody>
</table>
<!--右侧内容部分结束-->
</body>
</html>