<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<head>
<meta name="decorator" content="wholesaler" />
<title>
	<c:if test="${newORold==0 }">批发商管理-新增第3步-银行账户信息</c:if>
	<c:if test="${newORold==1 }">批发商管理-修改第3步-银行账户信息</c:if>
</title>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"  type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/ckeditor/ckeditor.js"></script>
<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
<!--供应商模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.supplier.js"></script>
<script type="text/javascript">
	$(function(){
		//添加 银行账户
		account_tj();
		// 返回
		$("#back").click(function(){
			location.href=contextPath + "/manage/saler/salerlist";
		});
		$("#last").click(function(){
			// 跳转到上一步页面
			location.href=contextPath + "/manage/saler/backWholeOfficeTwo/${office.id}/${newORold}";
		});
		// 下一步
		$("#next").click(function(){
			// 跳转到下一步页面
			location.href=contextPath + "/manage/saler/gotoAddWholeOfficeFour/${office.id}/${newORold}";
		});
		// 提交
		$("#save").click(function(){
			// 跳回批发商列表
			location.href=contextPath + "/manage/saler/salerlist";
		});
	});
	
	// 格式化账户信息
	function paramBank(obj){
		var id=$(obj).parents("dl.account").find("input[name=id]").val();
		 var accountName =  $(obj).parents("dl.account").find("input[name=accountName]").val();
		 var bankName =  $(obj).parents("dl.account").find("input[name=bankName]").val();
		 var bankAddr =  $(obj).parents("dl.account").find("input[name=bankAddr]").val();
		 var bankAccountCode =  $(obj).parents("dl.account").find("input[name=bankAccountCode]").val();
		 var remarks =  $(obj).parents("dl.account").find("textarea[name=remarks]").val();
		 var defaultFlag=$(obj).parents("dl.account").find("input[name=defaultFlag]:checked").val();
		 if(defaultFlag){
			 defaultFlag = "0"; // 默认账户
		 }else{
			 defaultFlag ="1";  // 非默认账户
		 }
		 var belongParentPlatId = "${office.id}";
		 
		 var param = "&accountName="+accountName;
		 param+="&bankName="+bankName;
		 param+="&bankAddr="+bankAddr;
		 param+="&bankAccountCode="+bankAccountCode;
		 param+="&remarks="+remarks;
		 param+="&defaultFlag="+defaultFlag;
		 param+="&belongParentPlatId="+belongParentPlatId;
		 if(id){
			 param+="&id="+id;
		 }
		 return param;
	}
	// 校验账户信息
	function saveBank(obj){
		var the_param = paramBank(obj);
		var n = 0;
		var accountName =  $(obj).parents("dl.account").find("input[name=accountName]").val();
		 var bankName =  $(obj).parents("dl.account").find("input[name=bankName]").val();
		 var bankAddr =  $(obj).parents("dl.account").find("input[name=bankAddr]").val();
		 var bankAccountCode =  $(obj).parents("dl.account").find("input[name=bankAccountCode]").val();
		 
		 if(!accountName){
			 $.jBox.tip("账户名称不可为空","info");
			 n++;
		 }
		 if(!bankName){
			 $.jBox.tip("银行名称不可为空","info");
			 n++;
		 }
		 if(!bankAddr){
			 $.jBox.tip("银行地址不可为空","info");
			 n++;
		 }
		 if(!bankAccountCode){
			 $.jBox.tip("账户号码不可为空","info");
			 n++;
		 }
		
		if(n==0){
			ajaxForm(the_param);
		}
	}
	// ajax 提交银行账户表单
	function ajaxForm(the_param){
		$.ajax({
			type : "POST",
			url : contextPath+"/manage/saler/addNewBlankCode",
			data:the_param,
			dataType:"text",
			success:function(html){
				var json;
				try{
					json = $.parseJSON(html);
				}catch(e){
					json = {res:"error"};
				}
				if(json.res=="success"){
					jBox.tip("提交成功", 'info');
					
				}else if(json.res=="data_error"){
					jBox.tip(json.mes,"info");
				}else{
					jBox.tip("系统繁忙，请稍后再试", 'error');
				}
			}
		})
	}
	
	// ajax 删除账户
	function delBank(){
		var accountName = $(this).parents("dl.account").find("input[name=accountName]").val();
		var bankName = $(this).parents("dl.account").find("input[name=bankName]").val();
		var bankAddr = $(this).parents("dl.account").find("input[name=bankAddr]").val();
		var bankAccountCode = $(this).parents("dl.account").find("input[name=bankAccountCode]").val();
		var remarks = $(this).parents("dl.account").find("input[name=remarks]").val();
		var id = $(this).parents("dl.account").find("input[name=id]").val();
		if(id){ // 判断，如果id 存在，则从数据库删除该记录，否则只删除页面div
			$("#delBank").empty();
			$("#delBank").append("<input type='hidden' name='accountName' value='"+accountName+"'/>");
			$("#delBank").append("<input type='hidden' name='bankName' value='"+bankName+"'/>");
			$("#delBank").append("<input type='hidden' name='bankAddr' value='"+bankAddr+"'/>");
			$("#delBank").append("<input type='hidden' name='bankAccountCode' value='"+bankAccountCode+"'/>");
			$("#delBank").append("<input type='hidden' name='remarks' value='"+remarks+"'/>");
			$("#delBank").append("<input type='hidden' name='id' value='"+id+"'/>");
			var the_param = $("#delBank").serialize();
			$.ajax({
				type : "POST",
				url:contextPath+"/manage/saler/delNewBlankCode",
				data:the_param,
				dataType:"text",
				success:function(html){
					var json;
					try{
						json = $.parseJSON(html);
					}catch(e){
						json = {res:"error"};
					}
					if(json.res=="success"){
						jBox.tip("删除成功", 'info');
					}else if(json.res=="data_error"){
						jBox.tip(json.mes,"info");
					}else{
						jBox.tip("系统繁忙，请稍后再试", 'error');
					}
				}
			});
		}
	}
</script>
</head>
<body>
    <!--右侧内容部分开始-->
	<div class="supplierLine">
		<a href="javascript:void(0)">基本信息填写</a>
		<a href="javascript:void(0)">网站信息</a>
		<a href="javascript:void(0)" class="select">银行账户</a>
		<a href="javascript:void(0)">资质上传</a>
		<!-- 用于区分新增/修改批发商；0：新增；1：修改; 默认为新增 -->
			<input type="hidden"  name="newORold" value="0"/>
	</div>
	<div class="qdgl-cen">
		<dl class="account">
			<dt><span><input name="defaultFlag" type="radio" value="0"  <c:if test="${firstPlat.defaultFlag==0 }">checked="checked"</c:if>/></span>
									<b>设为默认账户</b>
									<input name="id" type="hidden" value="${firstPlat.id }"/></dt>
			<dd><span>账户名：</span><input name="accountName" type="text" value="${firstPlat.accountName }" /></dd>
			<dd><span>开户行名称：</span><input name="bankName" type="text" value="${firstPlat.bankName }" /></dd>
			<dd><span>开户行地址：</span><input name="bankAddr" type="text" value="${firstPlat.bankAddr }" /></dd>
			<dd><span>账户号码：</span><input name="bankAccountCode" type="text" value="${firstPlat.bankAccountCode }" /></dd>
			<dd><span>备注：</span><textarea name="remarks" cols="" rows="" maxlength="200" >${firstPlat.remarks }</textarea></dd>
			<dd><input type="button" value="保存" name="saveBank"  class="ydbz_s"  onclick="saveBank(this)"/> </dd>
		</dl>
		<div class="yh-account">
			<div class="ydbz_s account_tj">继续添加账户+</div>
		</div>
		<dl class="account" style=" display:none;">
			<dt><span><input name="defaultFlag" type="radio" value="0"  /></span>
									<input name="id" type="hidden" value=""/>
									<b>设为默认账户</b><em>X 删除</em></dt>
			<dd><span>账户名：</span><input name="accountName" type="text" value="" /></dd>
			<dd><span>开户行名称：</span><input name="bankName" type="text" value="" /></dd>
			<dd><span>开户行地址：</span><input name="bankAddr" type="text" value="" /></dd>
			<dd><span>账户号码：</span><input name="bankAccountCode" type="text" value="" /></dd>
			<dd><span>备注：</span><textarea name="remarks" cols="" rows=""  maxlength="200"></textarea></dd>
			<dd><input type="button" value="保存" name="saveBank"  class="ydbz_s"  onclick="saveBank(this)"/></dd>
		</dl>
		<!-- 用于从第四步回退回第三步，或者修改批发商时使用，增加其他账户 -->
		<c:forEach items="${platList }" var="plat">
			<dl class="account" style="">
				<dt><span><input name="defaultFlag" type="radio" value="0"  <c:if test="${plat.defaultFlag==0 }">checked="checked"</c:if>/></span>
										<input name="id" type="hidden" value="${plat.id }"/>
										<b>设为默认账户</b><em>X 删除</em></dt>
				<dd><span>账户名：</span><input name="accountName" type="text"   value="${plat.accountName }"/></dd>
				<dd><span>开户行名称：</span><input name="bankName" type="text" value="${plat.bankName }" /></dd>
				<dd><span>开户行地址：</span><input name="bankAddr" type="text" value="${plat.bankAddr }" /></dd>
				<dd><span>账户号码：</span><input name="bankAccountCode" type="text" value="${plat.bankAccountCode }" /></dd>
				<dd><span>备注：</span><textarea name="remarks" cols="" rows=""  maxlength="200">${plat.remarks }</textarea></dd>
				<dd><input type="button" value="保存" name="saveBank"  class="ydbz_s"  onclick="saveBank(this)"/></dd>
			</dl>
		</c:forEach>
	</div>
	<div class="dbaniu " style=" margin-left:100px;">
		<input type="hidden" name="belongParentPlatId"  value="${office.id }"/>
		<a class="ydbz_s gray" href="javascript:void(0)"  id="back">返回</a>
		<a class="ydbz_s" href="javascript:void(0)"  id="last">上一步</a>
		<a class="ydbz_s" href="javascript:void(0)"  id="next">下一步</a>
		<a class="ydbz_s" href="javascript:void(0)"  id="save">提交</a>
	</div>
	<form id="delBank">
	
	</form>
	<!--右侧内容部分结束-->
</body>
</html>
