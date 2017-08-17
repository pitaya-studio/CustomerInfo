<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>等级字典维护</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	rel="stylesheet" />
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
	rel="stylesheet" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"
	rel="stylesheet" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"
	rel="stylesheet" type="text/css" />
<link
	href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css"
	rel="stylesheet" type="text/css" />

<script src="${ctxStatic}/js/jquery-1.9.1.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"
	type="text/javascript"></script>

<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"
	type="text/javascript"></script>
<link
	href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css"
	type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/js/vendor.service_mode1.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"
	type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/json/json2.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/socket/sockjs-0.3.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/js/socket/stomp.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"
	type="text/javascript"></script>

<script type="text/javascript">
	function addType() {
	   $.jBox('iframe:${ctx}/agentGrade/save',{
		 title: "渠道等级",
		 width: 340,
		height: 365,
		persistent: true,
		buttons: {}
		});

	}
	function deleteOne(id,label,obj){
		$.jBox.confirm("您确认删除&quot"+label+"&quot吗？", "系统提示", function(v, h, f) {
			if(v=='ok'){
				$.ajax({
					type:"post",
					url:"${ctx}/agentGrade/delete",
					data:{
						sysDefineDictId:id
					},
					success:function(result){
						if(result){
							$(obj).parent().parent().remove();
						}else{
							top.$.jBox.tip("无法删除，该等级已经被使用");
						}
					}
				});
			}else if(v=='cancle'){
			
			}
		});		
	}
</script>
<style>
	.bgMainRight{
		background:none;
	}
</style>
</head>
<body>
	<div class="bgMainRight">
	<div class="ydbz_tit">渠道等级</div>
		<br />
		<div class="ydbzbox fs">
			<!--<div class="ydbz_tit"></div>-->
			<div class="tableDiv flight">
				<table id="contentTable" class="t-type">
					<thead class="destination_title">
						<tr>
							<th width="15%">编号</th>
							<th width="25%">渠道等级</th>
							<th width="10%">顺序</th>
							<th width="25%">描述</th>
							<th width="10%">操作</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${list }" var="agentGrade" varStatus="status">
						<tr>
							<td>${status.count }</td>
							<td>${agentGrade.label }</td>
							<td>${agentGrade.sort }</td>
							<td>${agentGrade.description }</td>
							<td class="tc">
								<!--<a href="javascript:void(0)" onClick="addType(629)">修改</a>&nbsp&nbsp&nbsp&nbsp-->
								<a href="javascript:void(0)"
								onClick="deleteOne(${agentGrade.id},'${agentGrade.label }',this)">删除</a>
							</td>
						</tr>
					</c:forEach>	
					</tbody>
				</table>

				<div class="t-type-add">
					<a class="ydbz_s" href="javascript:void(0)" onClick="addType()">添&nbsp;&nbsp;&nbsp;加</a>
				</div>
			</div>
		</div>

	</div>
	</div>
	</div>
</body>
</html>
