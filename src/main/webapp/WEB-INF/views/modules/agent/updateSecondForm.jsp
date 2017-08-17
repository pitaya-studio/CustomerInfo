<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>渠道管理-修改银行账户信息</title>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	//添加 银行账户
	//account_tj();
	account_tjk();
	<%--bug17525为统一按钮样式 将a替换成input--%>
	$("input.preButton").click(function(){
		var url = "${ctx}/agent/manager/updateFirstForm/${agentId }";
		window.location.href=url;
	});
	
});

function account_tjk(){
		//$('.account dt em').live('click',function(){$(this).parents('.account').remove();});
		$('.account_tj').click(function(){
			var i = $("dl.account").length-1;
			var ykhtml=$(this).parent().next('.account').html();
			ykhtml = ykhtml.replace(/name=\"/g,"name=\"banks["+i+"].");
			$(this).parent().before('<dl class="account">'+ykhtml+'</dl>');
			$("input[type='radio']").attr("name","radio");
		});
	/*	
		$('.account dt em').live('click',function(){
			$(this).parents('.account').remove();
			$('.account:visible').each(function(index,element){
				//查找account下的不是radio的input遍历更改其name属性
				//console.debug($(element).find('span input[type="hidden"]').attr("name"));
				var radioName=$(element).find('span input[type="hidden"]').attr("name");
				var radioArr = radioName.split(".");
				var radioNameTrue = radioArr[1];
				radioNameTrue = "banks["+index+"]."+radioNameTrue;
				$(element).find('span input[type="hidden"]').attr("name",radioNameTrue);
				
				//更改不是radio的name
				$(element).find("dd input").each(function(i,elt){
					var elementName = $(elt).attr("name");
					var nameArr = elementName.split(".");
					var trueName = nameArr[1];
					trueName = "banks["+index+"]."+trueName;
					$(elt).attr("name",trueName);
				});
				//更改textArea的name
				var textAreaName=$(element).find('dd textarea').attr("name");
				var textAreaNameArr = textAreaName.split(".");
				var textAreaNameTrue = textAreaNameArr[1];
				textAreaNameTrue = "banks["+index+"]."+textAreaNameTrue;
				$(element).find('dd textarea').attr("name",textAreaNameTrue);
				//console.debug($(element).html());
				
				//更改dl下的input type="hidden" 的name
				var idName=$(element).find('input[type="hidden"]').last().attr("name");
				var idNameArr = idName.split(".");
				var idNameTrue = idNameArr[1];
				idNameTrue = "banks["+index+"]."+idNameTrue;
				$(element).find('input[type="hidden"]').last().attr("name",idNameTrue);
			});
			
		}
	);*/
}
function changeName (){
	$('.account:visible').each(function(index,element){
		//查找account下的不是radio的input遍历更改其name属性
		//console.debug($(element).find('span input[type="hidden"]').attr("name"));
		var radioName=$(element).find('span input[type="hidden"]').attr("name");
		var radioArr = radioName.split(".");
		var radioNameTrue = radioArr[1];
		radioNameTrue = "banks["+index+"]."+radioNameTrue;
		$(element).find('span input[type="hidden"]').attr("name",radioNameTrue);
		
		//更改不是radio的name
		$(element).find("dd input").each(function(i,elt){
			var elementName = $(elt).attr("name");
			var nameArr = elementName.split(".");
			var trueName = nameArr[1];
			trueName = "banks["+index+"]."+trueName;
			$(elt).attr("name",trueName);
		});
		//更改textArea的name
		var textAreaName=$(element).find('dd textarea').attr("name");
		var textAreaNameArr = textAreaName.split(".");
		var textAreaNameTrue = textAreaNameArr[1];
		textAreaNameTrue = "banks["+index+"]."+textAreaNameTrue;
		$(element).find('dd textarea').attr("name",textAreaNameTrue);
		//console.debug($(element).html());
		
		//更改dl下的input type="hidden" 的name
		var idName=$(element).find('input[type="hidden"]').last().attr("name");
		var idNameArr = idName.split(".");
		var idNameTrue = idNameArr[1];
		idNameTrue = "banks["+index+"]."+idNameTrue;
		$(element).find('input[type="hidden"]').last().attr("name",idNameTrue);
	});
}
function removeEmpty(obj){
	//$(obj).parents('.account').remove();
	$(obj).parents('.account').remove();
	changeName();
}
//删除账户
function deleteBankInfo(obj,id){
	top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
		if(v=='ok'){
			var url = "${ctx}/agent/manager/delBankInfoById/"+id;
			$.ajax({
				   type: "POST",
				   url: url,
				   data: "",
				   success: function(msg){
				     	if(msg==1){
				     		$(obj).parents('.account').remove();
				     		changeName();
				     	}else{
				     		$(obj).parents('.account').show();
				     	}
				   },
				   error:function(){
					   alert('删除失败');
				   }
				});
		}
		},{buttonsFocus:1});
}
//开户行名称只有字母和汉字组成，且长度最大为100  需求号:89
function checkBankName(obj){
	var bankName = $(obj).val().trim();
	bankName = bankName.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'');
	bankName = bankName.length>100?bankName.substr(0,100):bankName;
	$(obj).val(bankName);
}
//账号只有数字组成，且长度最大为100    需求号:89
function checkAccount(obj){
	var bankAccount = $(obj).val().trim();
	bankAccount = bankAccount.replace(/[^0-9\s]/g,'');
	bankAccount = bankAccount.length>100?bankAccount.substr(0,100):bankAccount;
	$(obj).val(bankAccount);
}
function submitForm(){
	var arr = $("input[type='radio']");
	$("input[type='hidden'][defaultFlag='defaultFlag']").val("1");
	for(var i=0;i<arr.length;i++){
		if($(arr[i]).attr("checked")=="checked"){
			//$(arr[i]).next("input [type='hidden']").val("1");
			$(arr[i]).parent().find("input[type='hidden']").val("0");
		}
	}
	$("#bankForm").submit();
}
</script>
</head>
<body>
<style type="text/css">label{ cursor:inherit;}</style>
<page:applyDecorator name="agent_op_head" >

</page:applyDecorator>
<!-- <div id="sea"> -->
<!-- 	<div class="main"> -->
<!--         <div class="main-right"> -->
<!--             <ul class="nav nav-tabs"> -->
<!--             </ul> -->
<!--             <div class="bgMainRight"> -->
            	<!--右侧内容部分开始-->
				<div class="supplierLine">
					<a href="javascript:void(0)">基本信息填写</a>
					<a href="javascript:void(0)" class="select">银行账户</a>
					<a href="javascript:void(0)">资质上传</a>
				</div>
				<div class="qdgl-cen">
				<form:form modelAttribute="banks" id="bankForm" method="POST" action="${ctx }/agent/manager/updateSecond/${agentId}">
				   <c:forEach items="${banks }" var="bank" varStatus="status">
					<dl class="account"> 
						<dt><span><input name="radio" type="radio" altattr="radio"  <c:if test="${bank.defaultFlag == 0 }">checked="checked"</c:if>/><input type="hidden" defaultFlag="defaultFlag" name="banks[${status.index}].defaultFlag" value="${bank.defaultFlag}"/></span><b>设为默认账户</b><c:if test="${status.index > 0}"><em onclick="deleteBankInfo(this,'${bank.id}');">X 删除</em></c:if></dt>
						<dd><span><!-- <i>*</i> -->账户名：</span><input  name="banks[${status.index}].accountName" type="text" value="${bank.accountName}" maxlength="30"/></dd>
						<dd><span><!--<i>*</i> -->开户行名称：</span><input onkeyup="checkBankName(this)" name="banks[${status.index}].bankName" type="text" value="${bank.bankName}" maxlength="30"/></dd>
						<dd><span><!--<i>*</i> -->开户行地址：</span><input name="banks[${status.index}].bankAddr" type="text" value="${bank.bankAddr}" maxlength="50"/></dd>
						<dd><span><!--<i>*</i> -->账户号码：</span><input onkeyup="checkAccount(this)" name="banks[${status.index}].bankAccountCode" type="text" value="${bank.bankAccountCode}" maxlength="37"/></dd>
						<dd><span>备注：</span><textarea name="banks[${status.index}].remarks" cols="" rows="" maxlength="150">${bank.remarks} </textarea></dd>
						<input type="hidden" name="banks[${status.index}].id" value="${bank.id}"/>
					</dl>
					</c:forEach>
					<div class="yh-account">
						<%--bug17525统一按钮样式--%>
						<%--<div class="ydbz_s account_tj">继续添加账户+</div>--%>
						<input type="button"  class="btn btn-primary account_tj" value="继续添加账户">

					</div>
					<dl class="account" style="display:none;">
						<dt><span><input name="radio" type="radio"   value="0"/><input type="hidden" defaultFlag="defaultFlag" name="defaultFlag" value="1"/></span><b>设为默认账户</b><em onclick="removeEmpty(this);">X 删除</em></dt>
						<dd><span><!--<i>*</i> -->账户名：</span><input  name="accountName" type="text" value="" maxlength="30"/></dd>
						<dd><span><!--<i>*</i> -->开户行名称：</span><input onkeyup="checkBankName(this)" class="bankName" name="bankName" type="text" value="" maxlength="30"/></dd>
						<dd><span><!--<i>*</i> -->开户行地址：</span><input name="bankAddr" type="text" value="" maxlength="50"/></dd>
						<dd><span><!--<i>*</i> -->账户号码：</span><input onkeyup="checkAccount(this)" class="bankAccountCode" name="bankAccountCode" type="text" value="" maxlength="37"/></dd>
						<dd><span>备注：</span><textarea name="remarks" cols="" rows="" maxlength="150"></textarea></dd>
						<input type="hidden" name="id" />
					</dl>
				</form:form>
				</div>
				<div class="dbaniu " style=" margin-left:100px;">
<!-- 					<a class="ydbz_s gray">返回</a> -->
					<%--bug17525统一按钮样式--%>
					<%--<a class="ydbz_s preButton" href="javascript:void(0)" >上一步</a>--%>
					<%--<a class="ydbz_s" href="javascript:void(0)" onclick="submitForm();">下一步</a>--%>
					<input type="button"  class="btn btn-primary preButton" value="上一步">
					<input type="button"  class="btn btn-primary" onclick="submitForm();" value="下一步">
					<!-- 					<a class="ydbz_s">提交</a> -->
				</div>
				<!--右侧内容部分结束-->
<!--             </div> -->
<!--         </div> -->
<!-- 	</div> -->
    <!--footer
    <div class="bs-footer">
        <p>公司电话：010-85711691 | 技术支持电话：010-85711691-8006 | 客服电话：400-018-5090  | 传真：010-85711891<br/>Copyright &copy; 2012-2014 接待社交易管理后台</p>
        <div class="footer-by">Powered By Trekiz Technology</div>
    </div>-->
    <!--footer***end-->
<!-- </div> -->
</body>
</html>
