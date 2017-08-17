<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html><head>
    <title>类型字典维护</title>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="${ctxStatic}/css/jh-style.css" type="text/css" rel="stylesheet">
    <link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
    <script type="text/javascript">
    var partten = /^\d+$/;
    $(document).ready(function(){
        $('input[name=sort]').keyup(function(){
            if(!partten.test($(this).val())){
            	var a= $(this).val();
            	var b=a.replace(/[^\d]+/gi,'');
                $(this).val(b);
            }
        })
    })
    $(function(){
    	$("#add").click(function(){
    		save();
    	});
    });
	function save(){
		if($("#label").val().trim()==""){
			$("#span1").attr("style","color:red;display:inline");
			$("#span1").text("必填信息！");
			return;
		}
		if($("#sort").val().trim()==""){
			$("#span2").attr("style","color:red;display:inline");
			$("#span2").text("必填信息！");
			return;
		}
		if($("#span1").text()!=""){
			top.$.jBox.tip("请重新填写渠道类型！");
			return;
		}
		if($("#span2").text()!=""){
			top.$.jBox.tip("请重新填写顺序！");
			return;
		}
		$.ajax({
			type:"post",
			url:"${ctx}/agentType/saveAgentType?token=${token}",
			data:$("#companyDictForm").serialize(),
			success:function(result){
				if(result=='success'){
					parent.location.reload();
				}else{
					top.$.jBox.tip("添加失败");
				}
			}
		});
	}
	function checklabel(){
		if($("#label").val().trim()==""){
			$("#span1").attr("style","color:red;display:inline");
			$("#span1").text("必填信息！");
			return;
		}
		var boolean=false;
		$.ajax({
			type:"post",
			url:"${ctx}/agentType/check",
			data:{
				label:$("#label").val()
			},
			success:function(result){
				if(result){
					$("#span1").attr("style","color:red;display:inline");
					$("#span1").text("名称已存在！");
					boolean=true;
				}
			}
		});
		if(boolean){
			return;
		}
	}
	function goLabel(){
		$("#span1").attr("style","color:red;display:none");
		$("#span1").text("");
	}
	function checksort(){
		if($("#sort").val().trim()==""){
			$("#span2").attr("style","color:red;display:inline");
			$("#span2").text("必填信息！");
			return;
		}
	}
	function gosort(){
		$("#span2").attr("style","color:red;display:none");
		$("#span2").text("");
	}
</script>
</head>
<style>
	.msg_div p span {
    	width: 80px;
}
</style>
<body>
<form novalidate="novalidate" id="companyDictForm" action="" method="post" target="_parent">
    <div class="msg_div">
        <input id="value" name="value" value="" type="hidden">
        <p><span class="widthQudao"><font style="left:-1px">*</font>
            		渠道类型 ：
           		</span>
            <input id="label" class="inputTxt" name="label" value="" maxlength="50" type="text" onblur="checklabel()" onfocus="goLabel()">
            <span id="span1" style="color:red;display:none;"></span>
        </p>
        <p><span><font>*</font>顺序 ：</span><input id="sort" class="inputTxt" name="sort" value="" maxlength="3" type="text" onblur="checksort()" onfocus="gosort()">
        <span id="span2" style="color:red; display:none;"></span>
        </p>
        <p><span>
            		描述 ：
  				</span>
            <input id="description" class="inputTxt" name="description" value="" maxlength="99" type="text">
        </p>
        <input name="type" id="type" value="travel_type" type="hidden">
        <input name="id" id="id" value="" type="hidden">
        <div class="release_next_add">
            <input class="btn btn-primary gray" value="取消" onclick="window.parent.window.jBox.close()" type="button">
            <input class="btn btn-primary" value="确定" type="button"  id="add">
        </div>
    </div>
</form>


</body></html>
