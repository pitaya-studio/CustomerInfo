<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>基础信息维护-编号规则设置</title>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="kiben" content="no-cache">
<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" ></script>
<script type="text/javascript">
	$(function() {
		var $selectedRules = $("div.rules_add_title_c");
		$("#ruleList").on("click","a.rules_add_btn",function() {
				var text = $(this).attr("data-param");
				var spanHtml = '<span class="add_span"><span class="add_word">'+ text + '</span><a href="javascript:;" class="add_a"></a></span>';
				var $lastChild = $selectedRules.children(":last");
				if ($lastChild.is('input') && !$lastChild.val()) {
					$lastChild.before(spanHtml);
				} else {
					$selectedRules.append(spanHtml + '<input class="input_clear" />');
				}
			});
		$selectedRules.on("click", "a.add_a", function() {
			$(this).parent().remove();
		});
		$(".rules_add_title_r").on("click", function() {
			$selectedRules.empty();
			$selectedRules.append('<input class="input_clear" />');
		});
	});

	function selectChanged(obj) {
		var text = $(obj).val() + "位数字编号";
		$(obj).parent().prev().find("a.rules_add_btn").attr("data-param", text);
	}
	
	//添加或者修改编号
	function saveOrupdate(){
		var id = $("input[name='id']").val();
		var numberType = $("input[name='numberType']").val();
		var markName= $("input[name='markName']").val();
		var numberValue = getNumberRule();
		
		if(numberType == null || numberType == "" ){
			alert("编号类型不能为空!");
			$("input[name='numberType']").focus();
			return false;
		}
		if(numberValue == null || numberValue == "" ){
			alert("自定义规则不能为空!");
			$(".input_clear").focus();
			return false;
		}
		
		$.ajax({
				type : "POST",
				url :  "${ctx}/sys/numberRule/save",
				cache : false,
				data : {
					id : id,
					numberType : numberType,
					markName : markName,
					numberValue: numberValue
				},
				success:function(msg){
					if("success" == msg){
						$.jBox.prompt('保存成功', '系统提示', 'info', {closed:function(){ 
									window.location.href = "${ctx}/sys/numberRule/list"; 
								}
							}
						);  
					}else{
						$.jBox.error('保存失败', '系统提示');
					}
				},
				error: function(){
					$.jBox.error('保存失败', '系统提示');
				}
			});
	}
	
	//获取编号规则
	function getNumberRule(){
		var $selectedRules = $("div.rules_add_title_c");
		var $children = $selectedRules.children("");
		var result ="";
		$children.each(function(key,child){
			if($(child).is('span') && !$(child).val()) {
				result += $(child).children("span").text();
			} 
			if($(child).is('input') && $(child).val() != "") {
				result += $(child).val();
			} 
		});
		return result;
	}
	
	//判断是否存在
	function checkIsExist(numberType){
		$.ajax({
				type : "POST",
				url :  "${ctx}/sys/numberRule/checkIsExist",
				cache : false,
				data : {
					numberType : numberType
				},
				success:function(msg){
					//不存在
					if(0 == msg){
						createMarkName(numberType);
					}
					//已经存在
					else{
						alert("编号类型已经存在!");
						$("input[name='numberType']").attr("value","");
					}
				},
			});
	}
	
	
	//拼接编号类型的首字母
	function createMarkName(numberType){
		$.ajax({
				type : "POST",
				url :  "${ctx}/sys/numberRule/createMarkName",
				cache : false,
				data : {
					numberType : numberType
				},
				success:function(msg){
					if(msg){
						$("input[name='markName']").attr("value",msg);
					}
				},
			});
	}
</script>

</head>
<body>
	<page:applyDecorator name="sys_menu_head">
		<page:param name="showType">numberRule</page:param>
	</page:applyDecorator>

		<!--右侧内容部分开始-->
		<c:if test="${ empty numberRule }">
			<input type="hidden" name="id"  value=""  />
			<input type="hidden" name="markName"  value=""  />
			<div class="ydbz_tit pl20">编号规则添加</div>
			<span>编号类型:</span><input type="text" name="numberType"  value=""  onblur="checkIsExist(this.value)" onafterpaste='checkIsExist(this.value)' />
			<div class="rules_add_title">
				<div class="rules_add_title_l">自定义规则：</div>
				<div class="rules_add_title_c">
					<input class="input_clear" />
				</div>
				<div class="rules_add_title_r">清空</div>
			</div>
		</c:if>
		
		<c:if test="${not empty numberRule }">
			<input type="hidden" name="id"  value="${numberRule.id }"  />
			<input type="hidden" name="markName"  value="${numberRule.markName }"  />
			<div class="ydbz_tit pl20">编号规则修改</div>
			<span>编号类型:</span><input type="text" name="numberType"  value="${numberRule.numberType }"  readonly="readonly"  />
			<div class="rules_add_title">
				<div class="rules_add_title_l">自定义规则：</div>
				<div class="rules_add_title_c">
					<input class="input_clear" />
				</div>
				<div class="rules_add_title_r">清空</div>
			</div>
		</c:if>
		
	
		<h5 class="pl15">可用规则说明</h5>
		<table id="ruleList" class="table table-striped table-bordered table-mod2-group table-toggel wth100">
			<thead>
				<tr>
					<th width="5%">操作</th>
					<th width="10%">参数名称</th>
					<th width="15%">参数</th>
					<th width="10%">参数样例</th>
					<th width="20%">参数描述</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${paraList}" var="rulePara"  >
					<tr>
						<td><a href="javascript:;" class="rules_add_btn" data-param="${rulePara.paraValue }"></a></td>
						<td>${rulePara.paraName }</td>
						<td>
							<span class="para fl">${rulePara.paraValue }</span>
							<div class="notice_price"><span>详细编写注解帮助<br />${rulePara.paraValue }</span></div>
						</td>
						<td>${rulePara.paraExample }</td>
						<td>${rulePara.paraDesc }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="release_next_add">
			<input type="button" class="btn btn-primary" onclick="saveOrupdate()" value="提&nbsp;&nbsp;&nbsp;交"> 
			<input type="button"  class="btn btn-primary gray" onclick="javascript:window.location.href='${ctx}/sys/numberRule/list'" value="返&nbsp;&nbsp;&nbsp;回">
		</div>

	<!--右侧内容部分结束-->

</body>
</html>
