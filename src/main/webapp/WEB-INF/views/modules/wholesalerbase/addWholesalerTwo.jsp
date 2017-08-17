<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<head>
<meta name="decorator" content="wholesaler" />
<title>
	<c:if test="${newORold==0 }">批发商管理-新增第2步-网站信息</c:if>
	<c:if test="${newORold==1 }">批发商管理-修改第2步-网站信息</c:if>
</title>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"  type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/ckeditor/ckeditor.js"></script>
<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
<!--供应商模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.supplier.js"></script>
<script type="text/javascript">
	$(function(){
		// 返回
		$("#back").click(function(){
			// 跳回批发商列表
			location.href=contextPath + "/manage/saler/salerlist";
		});
		// 上一步
		$("#last").click(function(){
			// 跳转到上一步页面
			location.href=contextPath + "/manage/saler/backWholeOfficeOne/${office.id}/${newORold}";
		});
		// 下一步
		$("#next").click(function(){
			$("input[name=saveOrNext]").val(0);
			valForm();
			
		});
		// 提交
		$("#save").click(function(){
			$("input[name=saveOrNext]").val(1);
			valForm();
		});
	});
	// 检查 数据是否合法
	function valForm(){
		var n = 0;
		var email = $("input[name=loginAMail]").val();
		//var emailReg = new RegExp("^/w+((-/w+)|(/./w+))*/@[A-Za-z0-9]+((/.|-)[A-Za-z0-9]+)*/.[A-Za-z0-9]+$");
		//var emailReg = new RegExp("\^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-[A-Za-z0-9]+)*\\.[A-Za-z0-9]+\$");
		var emailReg = new RegExp("\^(\\w-*\\.*)+\\@(\\w-?)+(\\.\\w{2,})+");
		if(!emailReg.test(email)){
			$.jBox.tip("请检查邮箱格式","info");
			n++;
		}
		var loginMPhone = $("input[name=loginMPhone]").val();
		var numReg = new RegExp("^[0-9]*$");
		if(!numReg.test(loginMPhone)){
			$.jBox.tip("网站负责人电话只能为数字","info");
			n++;
		}
		var loginSPhone = $("input[name=loginSPhone]").val();
		if(!numReg.test(loginSPhone)){
			$.jBox.tip("网站客服电话只能为数字");
			n++;
		}
		var loginSQQ = $("input[name=loginSQQ]").val();
		if(!numReg.test(loginSQQ)){
			$.jBox.tip("QQ号码只能为数字");
			n++;
		}
		if(n==0){
			subForm();
		}
	}
	// ajax 提交表单
	function subForm(){
		var the_param = $("#inputForm").serialize();
		$.ajax({
			type:"POST",
			url:contextPath+"/manage/saler/addWholeOfficeTwo",
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
					var saveOrnext = $("input[name=saveOrNext]").val();
					if(saveOrnext==1){
						// 跳回批发商列表
						location.href=contextPath + "/manage/saler/salerlist";
					}else if(saveOrnext==0){
						// 跳转到下一步页面
						location.href=contextPath + "/manage/saler/gotoAddWholeOfficeThree/${office.id}/${newORold}";
					}
				}else if(json.res=="data_error"){
					jBox.tip(json.mes,"info");
				}else{
					jBox.tip("系统繁忙，请稍后再试", 'error');
				}
			}
		});
	};
	/**
     * 附件上传回调方法
     * @param {Object} obj button对象
     * @param {Object} fileIDList  文件表id
     * @param {Object} fileNameList 文件原名称
     * @param {Object} filePathList 文件url
     */
    function commenFunction(obj,fileIDList,fileNameList,filePathList){
    	//var name = obj.name;
   // 	$("#upfileShow").append("<p class='seach_r'><span  class='seach_checkbox'  id='"+obj.name+"'></span></p>");
     	if(fileIDList){
     		var arrID = new Array();
     		arrID = fileIDList.split(';');
     		var arrName = new Array();
     		arrName = fileNameList.split(';');
     		var arrPath = new Array();
     		arrPath = filePathList.split(';');
     		$("#upfileShow").empty();
     		for(var n=0;n<arrID.length;n++){
     			if(arrID[n]){
     				var $a = $("<a>"+arrName[n]+"</a>");
     				$a.append("<input type='hidden' name='salerTripFileId' value='"+arrID[n]+"'/>");
     				$a.append("<input type='hidden' name='salerTripFileName' value='"+arrName[n]+"'/>");
     				$a.append("<input type='hidden' name='salerTipFilePath' value='"+arrPath[n]+"'/>");
     				$("#upfileShow").append($a);
     			}
     		}
     	}
     }
</script>
</head>
<body>
        <!--右侧内容部分开始-->
		<div class="supplierLine">
			<a href="javascript:void(0)">基本信息填写</a>
			<a href="javascript:void(0)" class="select">网站信息</a>
			<a href="javascript:void(0)">银行账户</a>
			<a href="javascript:void(0)">资质上传</a>
			<!-- 用于区分新增/修改批发商；0：新增；1：修改; 默认为新增 -->
			<input type="hidden"  name="newORold" value="0"/>
		</div>
        <form class="form-horizontal" id="inputForm"  name="subForm">
        	<input type="hidden" name="companyId" value="${office.id }">
       	  <div class="sysdiv sysdiv_coupon qdgl-cen">
       	  	<p>
				<label>网站状态：</label>
				<span>
					<select name="loginStatus">
	             		<option value="1"  <c:if test="${office.loginStatus==1 }">selected</c:if>>启用</option>
	             		<option value="2"  <c:if test="${office.loginStatus==2 }">selected</c:if>>停用</option>
	             	</select>
             	</span>
			</p>
			<p>
				<label>浏览权限：</label>
				<span>
					<select name="loginShow">
	             		<option value="1"  <c:if test="${office.loginShow==1 }">selected</c:if>>开放浏览</option>
	             		<option value="2"  <c:if test="${office.loginShow==2 }">selected</c:if>>关闭浏览</option>
	             	</select>
             	</span>
			</p>
			<!-- 
            <p>
              <label>登陆账号：</label>
              <span>
              <input type="text" maxlength="50" name="loginCode">
              </span>
			</p>
            <p>
              <label>登录密码：</label>
              <span>
              <input type="text" maxlength="50" name="loginPW">
              </span>如果不填，默认密码将设为：111111
			</p> -->
			<p>
              <label>网站名称：</label>
              <span>
              	<input type="text" maxlength="50" name="loginName" value="${office.loginName }">
              </span>	
			</p>
			<p>
              <label>域名/网址：</label>
              <span>
              	<input type="text" maxlength="150" name="loginArr"   value="${office.loginArr }"/>
              </span>	
			</p>
				<label>网站LOGO：</label>
				<input type="button" name="passport"  id="uploadMoreFile" class="btn btn-primary" value="上传"  onclick="uploadFiles(' ${ctx}','passportfile',this,'true');"/>
      			   <span id="upfileShow" class="">
      			   		<a>
      			   			${office.loginLogoName }
      			   			<input type='hidden' name='salerTripFileId' value='${office.logo }'/>
     						<input type='hidden' name='salerTripFileName' value='${office.loginLogoName }'/>
     						<input type='hidden' name='salerTipFilePath' value='${office.loginLogoPath }'/>
      			   		</a>
      			   </span>
      			   <span class="fileLogo"></span>
            <p>
              <label>网站负责人：</label>
             <span><input type="text" name="loginMaster" maxlength="50"   value="${office.loginMaster }"/></span>
			</p>
            <p>
              <label>网站负责人电话：</label>
             <span><input type="text" name="loginMPhone" maxlength="30"   value="${office.loginMPhone }"/></span>旅行社网站负责人电话
			</p>
            <p>
              <label>网站客服电话：</label>
             <span><input type="text" name="loginSPhone" maxlength="30"  value="${office.loginSPhone }"/></span>旅行社网站客服电话
			</p>
            <p>
              <label>网站管理员邮箱：</label>
             <span><input type="text" name="loginAMail" maxlength="50"   value="${office.loginAMail }"/></span>旅行社网站负责人邮箱
			</p>
            <p>
              <label>QQ在线客服号码：</label>
             <span><input type="text" name="loginSQQ" maxlength="20"   value="${office.loginSQQ }"/></span>
			</p>
        </div>
        <div class="dbaniu " style=" margin-left:100px;">
        	<input type="hidden" name="saveOrNext" value="0"/>
	        <a class="ydbz_s gray" href="javascript:void(0)" id="back">返回</a>
	        <a class="ydbz_s" href="javascript:void(0)" id="last">上一步</a>
	         <a class="ydbz_s" href="javascript:void(0)" id="next">下一步</a>
	         <a class="ydbz_s" href="javascript:void(0)" id="save">提交</a>							
        </div>
       </form>
        <!--右侧内容部分结束--> 
</body>
</html>
