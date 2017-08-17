<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签约公告信息发布</title>

<meta name="decorator" content="wholesaler" />
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>


<style type="text/css">
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
    cursor:auto;
    background:transparent;
    border:0px;
    box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
}
</style>

<script type="text/javascript">
    $(document).ready(function() {
    	// 提交表单
    	$("#issue").on("click",function(){
    		$("#issue").attr("disabled","disabled");
    		valiParam(1);
    		window.setTimeout(function(){
    			$("#issue").removeAttr("disabled");
    		},3000);
    	});
    });
 // 校验不合格参数
	function valiParam(type){
		var content = $("#content").val();
		$("#index").val(content);
		var title = $("input[name=title]").val(); // 获取标题值
		if(title && content){
			var backTitle = $.trim(title);
			var backContent = $.trim(content);
			var bool = true;
			if(backTitle.length>20 || backTitle.length<1){
				$.jBox.tip("提交失败，公告标题不要超过20个字","提示");
				bool = false;
			}
			if(bool && (backContent.length>10000 || backContent.length<1)){
				$.jBox.tip("提交失败，公告内容不要超过10000个字","提示");
				bool = false;
			}
			if(bool && (backTitle.length>0 && backTitle.length<20 && backContent.length>0 && backContent.length<10000)){
				subForm(type);
				$.jBox.tip("发布成功。", 'success');
				cleanForm();
			}
		}else{
			$.jBox.tip("提交失败，请检查您输入的数据","提示");
		}
	}
	// 提交表单
	function subForm(type){
		
		var the_param = $("#addMessage").serialize();
		the_param += "&saveStatus="+type,
		$.ajax({
			type : "POST",
			url :  "${ctx}/message/addAJaxMsg",
			data : the_param,
			dataType : "text"
		});
	}
	// 清空表单
	function cleanForm(){
		$("input[name=title]").val("");
		$("#index").val(""); // 干掉正文预览
		$('#content').val(""); // 干掉上传正文
	}
</script>
</head>
<body>

  <div class="sysdiv">
    <form id="addMessage" class="form-horizontal" >
        	<input type="hidden"  name="msgType"  value="4"/>
        <div class="control-group">
            <label class="control-label">标题：</label>
            <div class="controls">
                <input id="title" name="title" class="input-xxlarge required measure-input"  type="text"  maxlength="200"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">正文：</label>
            <div class="controls">
                <textarea id="content" name="content"  class="input-xxlarge"  ></textarea>
            </div>
            <input type="hidden"  id="index"  name="index"  value=""/>
        </div>
        <div class="release_next_add" style="width:905px;">
            <input id="issue" class="btn btn-primary" type="button" value="发&nbsp;&nbsp;&nbsp;布"/>&nbsp;
            <input id="cancel" class="btn gray" type="button" value="取&nbsp;&nbsp;&nbsp;消"  onclick="javascript:history.back();" />
        </div>
    </form>
    </div>

</body>
</html>
