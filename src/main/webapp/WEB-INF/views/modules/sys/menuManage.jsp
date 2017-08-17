<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>菜单权限管理</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		
		function exportJsonData(){
			var url = "${ctx}/sys/menu/exportMenu";
			var menuIds = $("#parentId").val();
			if(menuIds == '') {
				$.jBox.tip("导出菜单id不能为空！");
			}
			
			$.ajax({
				type: "POST",
			   	url: url,
			   	async: false,
			   	data: {
						"menuIds":menuIds
					  },
				dataType: "json",
			   	success: function(data){
			   		if(data){
			   			if(data.result == 1) {
			   				$.jBox.tip("导出完成！");
			   				$("#exportJsonData").val(data.menuDatas);
			   			} else if(data.result == 2) {
			   				$.jBox.tip(data.message);
			   			} else if(data.result == 3) {
			   				$.jBox.tip(data.message);
			   			}
			   		}
			   	}
			});
		}
		
		function importJsonData(){
			var url = "${ctx}/sys/menu/importMenu";
			var importJsonData = $("#importJsonData").val();
			if(importJsonData == '') {
				$.jBox.tip("导入json数据不能为空！");
			}
			
			var menuJsonData = importJsonData;
			
			$.ajax({
				type: "POST",
			   	url: url,
			   	async: false,
			   	data: {
						"menuJsonData":menuJsonData
					  },
				dataType: "json",
			   	success: function(data){
			   		if(data){
			   			if(data.result == 1) {
			   				location.reload();
			   				$.jBox.tip(data.message);
			   			} else if(data.result == 2) {
			   				$.jBox.tip(data.message);
			   			} else if(data.result == 3) {
			   				$.jBox.tip(data.message);
			   			}
			   		}
			   	}
			});
		}
	</script>
	<style type="text/css">
		body {
			margin: 0;
			padding: 0;
		}
		.maintain_add {
			width: 700px;
			height: 320px;
			margin: 0 auto;
		}
		.maintain_add textarea {
			margin: 0;
			padding: 0;
			width: 70%;
			box-sizing: border-box;
			-webkit-box-sizing: border-box;
			-moz-box-sizing: border-box;
			-o-box-sizing: border-box;
		}
	</style>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">菜单管理</div>
	
	<div class="maintain_add" style="width:100%;"> 
            <p style="width:100%;">
              <label><em class="xing">*</em>导出菜单id：</label>
              <input type="input" name="parentId" id="parentId" />
			</p>
            <p class="maintain_kong"></p>
            <p style="width:100%;">
            	<label>&nbsp;</label>
            	<input  value="导&nbsp;&nbsp;&nbsp;出" type="button" class="btn btn-primary" id="exportBtn" onclick="exportJsonData();"/>
            </p>
            <p class="maintain_kong"></p>
            <p style="width:100%;">
              <label>导出json数据：</label>
              <textarea id="exportJsonData" name="exportJsonData" rows="10"></textarea>
			</p>
            <p class="maintain_kong"></p>
            <p style="width:100%;">
              	<label>导入json数据：</label>
				<textarea id="importJsonData" name="importJsonData" rows="10"></textarea>
			</p>
            <p class="maintain_kong"></p>
            <p style="width:100%;">
            	<label>&nbsp;</label>
            	<input  value="导&nbsp;&nbsp;&nbsp;入" type="button" class="btn btn-primary" id="exportBtn" onclick="importJsonData();"/>
            </p>
            <p class="maintain_kong"></p>
    </div>
	<!--右侧内容部分结束-->
</body>
</html>
