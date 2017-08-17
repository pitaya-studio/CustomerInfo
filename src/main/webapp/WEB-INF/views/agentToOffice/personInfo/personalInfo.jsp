<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible"content="IE=8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>个人信息</title>
    <link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>
    
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/common/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1t2.js"></script>
    <style>
    	.PL{
    		padding-left:15px;
    	}
    	.spanInput{
    		margin-left:0;
    	}
    	img{
    		width:100px;
    	}
    </style>
    <script type="text/javascript">
    
    	$(function(){
    		if("${not empty agentInfo.logo}"=="true"){
    			$("#logo").attr("src","${ctx}/person/info/getLogo?id=${agentInfo.logo}");
    		}
    	})
    	function saveInfo(){
    		var agentInfoJson=JSON.stringify(getAgentInfo());
    		var agentPersonJson=JSON.stringify(getAgentPerson());
    		$.ajax({
    			type:"post",
    			url:"${ctx}/person/info/updateInfo?token=${token}",
    			data:{
    				"agentInfoJson":agentInfoJson,
    				"agentPersonJson":agentPersonJson
    			},
    			success:function(result){
    				if(result){
    					top.$.jBox.tip("保存成功");
    					location.reload(true);
    				}else{
    					top.$.jBox.tip("保存失败");
    				}
    			}
    		});
    	}
    	//获得基本信息的提交数据
    	function getAgentInfo(){
    		data={};
    		data.id="${agentInfo.id}";
    		data.agentNameEn=$("input[name='agentNameEn']").val();
    		data.belongsArea=$("#belongsArea").val();
    		data.belongsAreaProvince=$("#belongsAreaProvince").val();
    		data.belongsAreaCity=$("#belongsAreaCity").val();
    		data.agentAddress=$("#agentAddress").val();
    		data.agentAddressProvince=$("#agentAddressProvince").val();
    		data.agentAddressCity=$("#agentAddressCity").val();
    		data.agentAddressStreet=$("input[name='agentAddressStreet']").val();
    		data.agentTelAreaCode=$("input[name='agentTelAreaCode']").val();
    		data.agentTel=$("input[name='agentTel']").val();
    		data.agentFaxAreaCode=$("input[name='agentFaxAreaCode']").val();
    		data.agentFax=$("input[name='agentFax']").val();
    		data.logo=$("input[name='logo']").val();
    		return data;
    	}
    	//获得渠道联系人的基本数据
    	function getAgentPerson(){
    		var datas=[];
    		var div=$("div[class='contect']");
    		for(var i=0;i<div.size();i++){
    			var data={};
    			var p=$(div[i]);
    			if(div.prev()!=undefined){
    				data.id=p.prev().val();
    			}else{
    				data.id="";
    			}
    			data.agentId="${agentInfo.id}";
    			data.contactName=p.find("input[name='contactName']").val();
    			data.contactMobile=p.find("input[name='contactMobile']").val();
    			data.contactPhone=p.find("input[name='contactPhone']").val();
    			data.contactFax=p.find("input[name='contactFax']").val();
    			data.contactEmail=p.find("input[name='contactEmail']").val();
    			data.contactQQ=p.find("input[name='contactQQ']").val();
    			data.remarks=p.find("input[name='remarks']").val();
    			datas.push(data);
    		}
    		return datas;
    	}
    	//获得银行账号信息
    	function getBank(){
    		var datas=[];
    		var tables=$("table[class='Account']");
    		for(var i=0;i<tables.size();i++){
    			var data={};
    			var table=$(tables[i]);
    			if(table.prev()!=undefined){
    				data.id=table.prev().val();
    			}else{
    				data.id="";
    			}
    			data.agentId="${agentInfo.id}"
    			data.defaultFlag=table.find("input[name='defaultFlag']:checked").val();
    			data.accountName=table.find("input[name='accountName']").val();
    			data.bankName=table.find("input[name='bankName']").val();
    			data.bankAddr=table.find("input[name='bankAddr']").val();
    			data.bankAccountCode=table.find("input[name='bankAccountCode']").val();
    			data.remarks=table.find("input[name='remarks']").val();
    			datas.push(data);
    		}
    		return datas;
    	}
    	//保存所有的银行帐户
    	function saveBank(){
    		var bankInfo=JSON.stringify(getBank());
    		$.ajax({
    			type:"POST",
    			url:"${ctx}/person/info/saveBank?token=${token}",
    			data:{
    				bankInfo:bankInfo
    			},
    			success:function(result){
    				location.reload(true);
    			}
    		});
    	}
    	
    	function uploadFiles(ctx, inputId, obj, isSimple) {
    		var imgUrl=$("#logo").attr("src");
    		$("#logoUrl").val(imgUrl);
    		var fls=flashChecker();
    		var s="";
    		if(fls.f) {
//    			alert("您安装了flash,当前flash版本为: "+fls.v+".x");
    		} else {
    			alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件"); 
    			return;
    		}
    		
    		//新建一个隐藏的div，用来保存文件上传后返回的数据
    		if($(obj).parent().find(".uploadPath").length == 0)
    			$(obj).parent().append('<div class="uploadPath" style="display: none" id="uploadPathDiv"></div>');
    		
    		$(obj).addClass("clickBtn");
    		
    		//默认为多文件上传
    		if(isSimple == null) {
    			isSimple = "false";
    		}
    		
    		$.jBox("iframe:"+ ctx +"/MulUploadFile/uploadFilesPage?isSimple=" + isSimple, {
    		//$.jBox("iframe:"+ ctx, {
    		    title: "文件上传",
    			width: 340,
    	   		height: 365,
    	   		buttons: {'完成上传':true},
    	   		persistent:true,
    	   		loaded: function (h) {},
    	   		submit: function (v, h, f) {
    				$(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
    				//这里拼接本次上传文件的原名称
    				var fileIDList = "";
    				var fileNameList = "";
    				var filePathList = "";
    				//
    				if($(obj).parent().find("[name='docID']").length != 0) {
    					$(obj).parent().find("[name='docID']").each(function(index, obj) {
    						if(null != isSimple && "false"!=isSimple && inputId!="elseFile") {
    							fileIDList = $(obj).val();
    						}else{
    							fileIDList += $(obj).val() + ";";
    						}
    					});
    				}
    				if($(obj).parent().find("[name='docOriName']").length != 0) {
    					$(obj).parent().find("[name='docOriName']").each(function(index, obj) {
    						if(null != isSimple && "false"!=isSimple && inputId!="elseFile") {
    							fileNameList = $(obj).val();
    						}else{
    							fileNameList += $(obj).val() + ";";
    						}
    					});
    				}
    				if($(obj).parent().find("[name='docPath']").length != 0) {
    					$(obj).parent().find("[name='docPath']").each(function(index, obj) {
    						if(null != isSimple && "1"!=isSimple && inputId!="elseFile") {
    							filePathList = $(obj).val();
    						}else{
    							filePathList += $(obj).val() + ";";
    						}
    					});
    				}
    				//在这里将原名称写入到指定id的input中
    				//if(inputId)
    				//	$("#" + inputId).val(fileNameList);
    				//该函数各自业务jsp都写一个，里面的内容根据自身页面要求自我实现
    				commenFunction(obj,fileIDList,inputId,fileNameList,filePathList);
    				$("#uploadPathDiv").remove();
    				$(".clickBtn",window.parent.document).removeClass("clickBtn");
    				fileNameList = "";
    	   		}
    		});
    		$(".jbox-close").hide();
    	}
    	
    	//上传文件后回显文件
   	 function commenFunction(obj,fileIDList,inputId,fileNameList,filePathList) {
   	 		var fileIdArr = fileIDList.split(";");
   	 		var fileNameArr = fileNameList.split(";");
   	 		var filePathArr = filePathList.split(";");
   	 		if(inputId!='elseFile'){
   	 			$(obj).next().html('');
   	 		}
   	 		for(var i=0; i<fileIdArr.length; i++) {
   					if(fileIdArr[i]!=""){
	   	    			var html = [];
	   	    			html.push('<li>');
	   	    			if(inputId=="logo"){
	   	   	    			html.push('<input type="hidden" name="'+inputId+'"  value="' + fileIdArr[i] + '" />');
	   	   	    			html.push('<input type="hidden" name="docName" value="'+ fileNameArr[i] +'"/>');
	   	   	    			html.push('<input type="hidden" name="docPath" value="'+ filePathArr[i] +'"/>');
	   	   	    			$("#logo").attr("src","${ctx}/person/info/getLogo?id="+fileIdArr[i]);
	   	   	    			
	   	    			}else{
	   	    				html.push('<a onclick="downloads('+ fileIdArr[i] +')">'+fileNameArr[i]+'</a>');
	   	   	    			html.push('<a class="ico-download" title="删除" onclick="deleteFile(this)">删除</a>');
	   	   	    			html.push('<input type="hidden" name="'+inputId+'"  value="' + fileIdArr[i] + '" />');
	   	   	    			html.push('<input type="hidden" name="docName" value="'+ fileNameArr[i] +'"/>');
	   	   	    			html.push('<input type="hidden" name="docPath" value="'+ filePathArr[i] +'"/>');
	   	    			}
	   	    			html.push('</li>');
	   	    			$(obj).next().append(html.join(''));
	   				}
   	 			}
   	    }
    	
    	function saveQualifications(){
    		var businessLicense=$("input[name='businessLicense']").val();
    		var license=$("input[name='license']").val();
    		var taxCertificate=$("input[name='taxCertificate']").val();
    		var organizeCertificate=$("input[name='organizeCertificate']").val();
    		var bankOpenLicense=$("input[name='bankOpenLicense']").val();
    		var travelAptitudes=$("input[name='travelAptitudes']").val();
    		var elseFile=getElseFile();
    		var idCard=$("input[name='idCard']").val();
    		var id="${agentInfo.id}";
    		$.ajax({
    			type:"post",
    			url:"${ctx}/person/info/saveQualifications?token=${token}",
    			data:{
					"id":id,    				
    				"businessLicense":businessLicense,
    				"license":license,
    				"taxCertificate":taxCertificate,
    				"organizeCertificate":organizeCertificate,
    				"bankOpenLicense":bankOpenLicense,
    				"travelAptitudes":travelAptitudes,
    				"elseFile":elseFile,
    				"idCard":idCard
    			},
    			success:function(result){
    				location.reload(true);
    			}
    		});
    	}
    	
    	function getElseFile(){
    		var elseFiles=$("input[name='elseFile']");
    		var elseFile="";
    		for(var i=0;i<elseFiles.size();i++){
    			if(elseFile==""){
    			elseFile=$(elseFiles[i]).val();
    			}else{
    			elseFile=elseFile+","+$(elseFiles[i]).val();
    			}
    		}
    		return elseFile;
    	}
    	
    	function downloads(docid) {
            window.open("${ctx}/sys/docinfo/download/" + docid);
        }
    	function deleteFiles(id,obj){
    		$.ajax({
    			type:"post",
    			url:"${ctx}/person/info/deleteFiles",
    			data:{
    				id:id
    			},
    			success:function(result){
    				if(result){
    					$(obj).parent().remove();
    					top.$.jBox.tip("删除成功");
    				}else{
    					top.$.jBox.tip("删除失败");
    				}
    			}
    		});
    	}
    	function deleteFile(obj){
    		$(obj).parent().remove();
    		top.$.jBox.tip("删除成功");
    	}
    	
    	function baseInfo(){
    		location.href="${ctx}/person/info/getAgentInfo";
    	}
    	//获得所属地省
    	function getBelongsProvince(){
    		var id=$("#belongsArea").val();
    		$.ajax({
    			type:"post",
    			url:"${ctx}/person/info/getBelongsProvince",
    			data:{
    				parentId:id
    			},
    			success:function(result){
    				$("#belongsAreaProvince").html("");
    				var c=$('<option value="">省（直辖市）</option>');
    				$("#belongsAreaProvince").append(c);
    				$.each(result,function(i,a){
						   var option=$('<option value="'+a.id+'">'+a.name+'</option>'); 		
						   $("#belongsAreaProvince").append(option);
    				});
    			}
    		});
    		
    	}
    	//获得所属地市
    	function getBelongsCity(){
    		var id=$("#belongsAreaProvince").val();
    		$.ajax({
    			type:"post",
    			url:"${ctx}/person/info/getBelongsProvince",
    			data:{
    				parentId:id
    			},
    			success:function(result){
    				$("#belongsAreaCity").html("");
    				var c=$('<option value="">市（区）</option>');
    				$("#belongsAreaCity").append(c);
    				$.each(result,function(i,a){
						   var option=$('<option value="'+a.id+'">'+a.name+'</option>'); 		
						   $("#belongsAreaCity").append(option);
    				});
    			}
    		});
    	}
    	//获得公司所属省
    	function getAddressProvince(){
    		var id=$("#agentAddress").val();
    		$.ajax({
    			type:"post",
    			url:"${ctx}/person/info/getBelongsProvince",
    			data:{
    				parentId:id
    			},
    			success:function(result){
    				$("#agentAddressProvince").html("");
    				var c=$('<option value="">省（直辖市）</option>');
    				$("#agentAddressProvince").append(c);
    				$.each(result,function(i,a){
						   var option=$('<option value="'+a.id+'">'+a.name+'</option>'); 		
						   $("#agentAddressProvince").append(option);
    				});
    			}
    		});
    	}
    	//获得公司所属市
    	function getAddressCity(){
    		var id=$("#agentAddressProvince").val();
    		$.ajax({
    			type:"post",
    			url:"${ctx}/person/info/getBelongsProvince",
    			data:{
    				parentId:id
    			},
    			success:function(result){
    				$("#agentAddressCity").html("");
    				var c=$('<option value="">市（区）</option>');
    				$("#agentAddressCity").append(c);
    				$.each(result,function(i,a){
						   var option=$('<option value="'+a.id+'">'+a.name+'</option>'); 		
						   $("#agentAddressCity").append(option);
    				});
    			}
    		});
    	}
    	//删除联系人
    	function delContects(obj,id){
    		$.jBox.confirm("您确认删除此联系人么？", "系统提示", function(v, h, f) {
    			if(v=='ok'){    		
		    		$.ajax({
		    			type:"post",
		    			url:"${ctx}/person/info/deleteContacts",
		    			data:{
		    				id:id
		    			},
		    			success:function(result){
		    				if(result){
		    					top.$.jBox.tip("删除成功");
		    					delContect(obj);
		    				}else{
		    					top.$.jBox.tip("删除失败");
		    				}
		    			}
		    		});
    			}else{
    						
    			}
    		});	
    	}
    	
    	function delAccounts(obj,id){
    		$.jBox.confirm("您确认删除此账户么？", "系统提示", function(v, h, f) {
    			if(v=='ok'){    	    		
		    		$.ajax({
		    			type:"post",
		    			url:"${ctx}/person/info/deleteBank",
		    			data:{
		    				id:id
		    			},
		    			success:function(result){
		    				if(result){
		    					top.$.jBox.tip("删除成功");
		    					delAccount(obj);
		    				}else{
		    					top.$.jBox.tip("删除失败");
		    				}
		    			}
		    		});
    			}
    		});	
    	}
    	var partt =/^[\u4e00-\u9fa5][A-Za-z0-9]+$/;
    	$(document).ready(function(){
    		 $('input[name=bankName]').keyup(function(){
                 if(!partten.test($(this).val())){
                     var a= $(this).val();
                     var b=a.replace(/[^a-z0-9\u4e00-\u9fa5]+/gi,'');
                     $(this).val(b)
                 }
             }) 

        });
    	var partten = /^\d+$/;
        $(document).ready(function(){
            $('input[name=bankAccountCode]').keyup(function(){
                if(!partten.test($(this).val())){
                	var a= $(this).val();
                    var b=a.replace(/[^\d]+/gi,'');
                    $(this).val(b);
                }
            });
        });
    </script>
</head>
<body>
<%@ include file="/WEB-INF/views/modules/homepage/T1Head.jsp"%>
	<div class="sea">
	 <div class="main">
            <div class="middle">
           <c:if test="${updateShow != 1 }">
            <div class="main-left">
                <ul>
                    <li  onclick="javascript:targetToOrderList();"><i></i>订单管理</li>
					<li class="li-active" onclick="javascript:targetToUserInfo();"><i></i>个人信息</li>
					<%-- <li onclick="javascript:targetToPswMng();"><i></i>修改密码</li> --%>
					<a class="passwordChange" href="javascript:void(0)" onclick="change_password()"><em></em>修改密码</a>
                </ul>
            </div>
           </c:if> 
       <div class="main-right">
      <div class="content">
      		<c:choose>
      			<c:when test="${updateShow != 1 }">  <div class="bread"><i></i>您的位置：首页 > 个人中心</div></c:when>
      		</c:choose>
        
          <div class="tabs">
          	<c:if test="${updateShow != 1 }">
              <a class="edit" onclick="edit()" href="javascript:void(0)">修改</a>
         	</c:if> 
              <ul class="nav nav-tabs">
                <li role="presentation" class="active"><a id="baseInfoTab"onclick="changeTab(this)"href="javascript:void(0)">基本信息</a></li>
                <li role="presentation" ><a id="bankAccountTab" onclick="changeTab(this)"href="javascript:void(0)">银行账户</a></li>
                <li role="presentation" ><a id="credentialsTab"onclick="changeTab(this)" href="javascript:void(0)">资质信息</a></li>
              </ul>
          </div>
          <div id="baseInfo">
              <div class="baseInfo">
              <table>
                  <tbody>
                      <tr>
                          <td class="details_d1">公司LOGO：</td>
                          <td class="details_d2"><div style="position:relative">
                          <input type="hidden" name="logoUrl" id="logoUrl" value="${ctx }/person/info/getLogo?id=${agentInfo.logo}"/>
                          <c:choose>
                          	<c:when test="${empty agentInfo.logo }">
                          		<img src="${ctxStatic }/images/t1t2/no-logo.jpg"   alt="${id}" height="100" width="100" id="logo">
                          	</c:when>
                          	<c:otherwise>
                          		<img src="${ctx }/person/info/getLogo?id=${agentInfo.logo}"   alt="${id}" style="width:100px;height:80px" id="logo">
                          	</c:otherwise>
                         </c:choose> 	
                              <div class="editImg hide"><i></i>修改<input class="ulImg"type="file" /></div>
                              <div class="uploadImg hide"><i></i>上传
                              	<input type="button"  style="opacity: 0;" class="ulImg" name="logo2" id="logo2"  value="上传" onclick="uploadFiles('${ctx}','logo',this,1);"/>
                             	<ol class="batch-ol">
                                       <li>
                                           <input name="logo" value="${agentInfo.logo }" type="hidden"/>
                                       </li>
                                   </ol> 	
                             </div>
                          </div></td>
                      </tr>
                      <tr>
                          <td class="details_d1">渠道品牌：</td>
                          <td class="details_d1"><span>${agentInfo.agentBrand }</span></td>
                      </tr>
                      <tr>  
                          <td class="details_d1">公司名称：</td>
                          <td class="details_d1"><span>${agentInfo.agentName }</span></td>
                      </tr>
                      <tr>
                          <td class="details_d1">英文名称：</td>
                          <td class="details_d2"><span>${agentInfo.agentNameEn }</span><input class="hide" maxlength="50" type="text" name="agentNameEn" value="${agentInfo.agentNameEn }"></td>
                      </tr>
                      <tr>   
                          <td class="details_d1">所属地区：</td>
                          <td class="details_d2_long"><span>${belongsArea.name}<c:if test="${! empty belongsAreaProvince.name}">&nbsp;${belongsAreaProvince.name }</c:if>&nbsp;${belongsAreaCity.name }</span>
                          				<select name="belongsArea" id="belongsArea" class="hide" onchange="getBelongsProvince()">
                                            <option value="">国家</option>
                                            <c:forEach items="${areaMap }" var="area">
                                            	<option <c:if test="${belongsArea.id==area.id }">selected='selected'</c:if> value="${area.id }">${area.name }</option>
                                            </c:forEach>
                                        </select>
                                        <select name="belongsAreaProvince" id="belongsAreaProvince" class="hide" onchange="getBelongsCity()">
                                            <option value="">省（直辖市）</option>
                                            <c:forEach items="${belongsAreaProvinces }" var="area">
                                            	<option <c:if test="${belongsAreaProvince.id==area.id }">selected='selected'</c:if> value="${area.id }">${area.name }</option>
                                            </c:forEach>
                                        </select>
                                        <select name="belongsAreaCity" id="belongsAreaCity" class="hide">
                                            <option value="">市（区）</option>
                                            <c:forEach items="${belongsCityMap }" var="area">
                                            	<option <c:if test="${belongsAreaCity.id==area.id }">selected='selected'</c:if> value="${area.id }">${area.name }</option>
                                            </c:forEach>
                                        </select>
                          </td>
                      </tr>
                      <tr>
                          <td class="details_d1">公司地址：</td>
                          <td class="details_d2_long"><span>${agentAddress.name }<c:if test="${! empty agentAddressProvince.name}">&nbsp;${agentAddressProvince.name }</c:if>
                          	<c:if test="${! empty agentAddressCity.name}">&nbsp;${agentAddressCity.name }</c:if>
                          		&nbsp;${agentInfo.agentAddressStreet }</span>
                          		<select name="agentAddress" id="agentAddress" class="hide" onchange="getAddressProvince()">
                                            <option value="">国家</option>
                                            <c:forEach items="${areaMap }" var="area">
                                            	<option <c:if test="${agentAddress.id==area.id }">selected='selected'</c:if> value="${area.id }">${area.name }</option>
                                            </c:forEach>
                                        </select>
                                        <select name="agentAddressProvince" id="agentAddressProvince" class="hide" onchange="getAddressCity()">
                                            <option value="">省（直辖市）</option>
                                            <c:forEach items="${addressProvinceMap }" var="area">
                                            	<option <c:if test="${agentAddressProvince.id==area.id }">selected='selected'</c:if> value="${area.id }">${area.name }</option>
                                            </c:forEach>
                                        </select>
                                        <select name="agentAddressCity" id="agentAddressCity" class="hide">
                                            <option value="">市（区）</option>
                                            <c:forEach items="${addressCityMap }" var="area">
                                            	<option <c:if test="${agentAddressCity.id==area.id }">selected='selected'</c:if> value="${area.id }">${area.name }</option>
                                            </c:forEach>
                                        </select>
                                        <input class="hide" type="text" name="agentAddressStreet" value="${agentInfo.agentAddressStreet }" maxlength="99">
                          </td>
                       </tr>
                       <tr>   
                          <td class="details_d1">电话号码：</td>
                          <td class="details_d2" ><span>${agentInfo.agentTelAreaCode} -${agentInfo.agentTel }</span><span class="spanInput hide"><input class="hide"type="text" name="agentTelAreaCode" value="${agentInfo.agentTelAreaCode}" maxlength="7" style="width:38px">-<input class="hide"type="text" name="agentTel" value="${agentInfo.agentTel }" maxlength="14"></span></td>
                      </tr>
                      <tr>
                          <td class="details_d1">传真：</td>
                          <td class="details_d2"><span>${agentInfo.agentFaxAreaCode}-${agentInfo.agentFax }</span><span class="spanInput hide"><input class="hide"type="text" name="agentFaxAreaCode" value="${agentInfo.agentFaxAreaCode}" maxlength="7" style="width:38px">-<input class="hide"type="text" name="agentFax" value="${agentInfo.agentFax }" maxlength="13"></span></td>
                      </tr>
                  </tbody>
              </table>
              </div>
              <div class="contects">
              <div class="addContect hide" onclick="addContect()"></div>
              <c:forEach items="${supplyContacts }" var="s" varStatus="status">
              <input type="hidden" name="id" value="${s.id }"/>
              <div class="contect">
              <div class="contectName">
                  <div class="delContect hide" onclick="delContects(this,${s.id})"></div>
                  <table>
                      <tbody>
                          <tr>
                              <td class="details_d1">联系人<span class="contectNum">${status.count }</span>：</td>
                              <td class="details_d2"><span class="nameOfContect">${s.contactName }</span><input class="hide"type="text" name="contactName" value="${s.contactName }" maxlength="9"></td>
                          </tr>
                      </tbody>
                  </table>

                  
              </div>
              <div class="contectIfo">
                  <table>
                      <tbody>
                          <tr>
                              <td class="details_d1">电话号码：</td>
                              <td class="details_d2 PL"><span>${s.contactMobile }</span><input class="hide"type="text" name="contactMobile" value="${s.contactMobile }" maxlength="11"></td> 
                              <td class="details_d1">固定电话：</td>
                              <td class="details_d3 " style="width:300px"><span>${s.contactPhone }</span><input class="hide"type="text" name="contactPhone" value="${s.contactPhone }" maxlength="12"></td> 
                          </tr>
                          <tr>
                              <td class="details_d1">传真号码：</td>
                              <td class="details_d2 PL" style="width:300px"><span>${s.contactFax }</span><input class="hide"type="text" name="contactFax" value="${s.contactFax }" maxlength="20"></td> 
                              <td class="details_d1">电子邮箱：</td>
                              <td class="details_d3"><span>${s.contactEmail }</span><input class="hide"type="text" name="contactEmail" value="${s.contactEmail }" maxlength="50"></td> 
                          </tr>
                          <tr>
                              <td class="details_d1">qq号码：</td>
                              <td class="details_d2 PL" ><span>${s.contactQQ }</span><input class="hide"type="text" name="contactQQ" value="${s.contactQQ }" maxlength="14"></td> 
                          </tr>
                          <tr>
                              <td class="details_d1">描述：</td>
                              <td class="details_d2 PL" ><span>${s.remarks }</span><input class="hide"type="text" name="remarks" value="${s.remarks }" maxlength="200"></td> 
                          </tr>
                      </tbody>
                  </table>
              </div>
              </div>
			</c:forEach>
              <div class="cancelSave hide">
                  <input type="button"value="取&nbsp;消" onclick="cancelthis(this)"class="button1 gray">
                  <input type="button"value="保&nbsp;存" onclick="saveInfo()" class="button1 savebutton">
              </div>
           </div>
          </div>
          <div id="bankAccount" class="hide">
          <c:choose>
          	<c:when test="${! empty platBankInfos }">
          		<c:forEach items="${platBankInfos }" var="platBankInfo">
          	 <input type="hidden" name="bankId" value="${platBankInfo.id }"/>
              <table class="Account">
                  <tbody>
                  		<tr>
                                <td class="details_d1 disabled"><input type="radio" name="defaultFlag" disabled="true" value="0" <c:if test="${platBankInfo.defaultFlag==0 }">checked='checked'</c:if>/></td>
                                <td class="details_d2 append">设为默认账户
                                    <div class="delAccount hide" onclick="delAccounts(this,'${platBankInfo.id}')"></div>
                                </td>
                            </tr>
                      <tr>
                          <td class="details_d1">账户名称：</td>
                          <td class="details_d2"><span>${platBankInfo.accountName }</span><input name="accountName" class="hide" type="text" value="${platBankInfo.accountName }" maxlength="18"></td>
                      </tr>
                      <tr>
                          <td class="details_d1">开户行名称：</td>
                          <td class="details_d2"><span>${platBankInfo.bankName }</span><input class="hide" type="text" name="bankName" value="${platBankInfo.bankName }" maxlength="18"></td>
                      </tr>
                      <tr>
                          <td class="details_d1">开户行地址：</td>
                          <td class="details_d2"><span>${platBankInfo.bankAddr }</span><input class="hide" type="text" name="bankAddr" value="${platBankInfo.bankAddr }" maxlength="200"></td>
                      </tr>
                      <tr>
                          <td class="details_d1">账户号码：</td>
                          <td class="details_d2"><span>${platBankInfo.bankAccountCode }</span><input class="hide" type="text" name="bankAccountCode" value="${platBankInfo.bankAccountCode }" maxlength="37"></td>
                      </tr>
                      <tr>
                          <td class="details_d1">备注：</td>
                          <td class="details_d2"><span>${platBankInfo.remarks }</span><input class="hide" type="text" name="remarks" value="${platBankInfo.remarks }" maxlength="200"></td>
                      </tr>
                  </tbody>
              </table>
		</c:forEach>
          	</c:when>
          	<c:otherwise>
          		<table class="Account">
                  <tbody>
                  		<tr>
                                <td class="details_d1 disabled"><input type="radio" name="defaultFlag" disabled="true" value="0"/></td>
                                <td class="details_d2 append">设为默认账户
                                    <div class="delAccount hide" onclick="delAccount(this)"></div>
                                </td>
                            </tr>
                      <tr>
                          <td class="details_d1">账户名称：</td>
                          <td class="details_d2"><span>${platBankInfo.accountName }</span><input name="accountName" class="hide" type="text" value="${platBankInfo.accountName }" maxlength="18"></td>
                      </tr>
                      <tr>
                          <td class="details_d1">开户行名称：</td>
                          <td class="details_d2"><span>${platBankInfo.bankName }</span><input class="hide" type="text" name="bankName" value="${platBankInfo.bankName }" maxlength="18"></td>
                      </tr>
                      <tr>
                          <td class="details_d1">开户行地址：</td>
                          <td class="details_d2"><span>${platBankInfo.bankAddr }</span><input class="hide" type="text" name="bankAddr" value="${platBankInfo.bankAddr }" ></td>
                      </tr>
                      <tr>
                          <td class="details_d1">账户号码：</td>
                          <td class="details_d2"><span>${platBankInfo.bankAccountCode }</span><input class="hide" type="text" name="bankAccountCode" value="${platBankInfo.bankAccountCode }" maxlength="37"></td>
                      </tr>
                      <tr>
                          <td class="details_d1">备注：</td>
                          <td class="details_d2"><span>${platBankInfo.remarks }</span><input class="hide" type="text" name="remarks" value="${platBankInfo.remarks }"></td>
                      </tr>
                  </tbody>
              </table>
          	</c:otherwise>
          </c:choose>
          	
			  <div class="addAccount hide">
                     <span onclick="addAccount()">继续添加账户+</span>
                </div>
              <div class="cancelSave hide">
                  <input type="button"value="取&nbsp;消" onclick="cancelthis(this)"class="button1 gray">
                  <input type="button"value="保&nbsp;存" onclick="saveBank()" class="button1 savebutton">
              </div>

          </div>
          <div id="credentials" class="hide">
              <table>
                  <tbody>
                      <tr>
                          <td class="details_d1">营业执照：</td>
                          <td class="details_d2" >
                              <span class="imgspan" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ business.docName}</span>
                              <c:if test="${!empty business }">
                              <a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ business.id}')" class="a">下载</a> 
							  </c:if>
                              <div class="hide ulFile">
                                  <input type="button" name="agentinfo.businessLicense2" id="businessLicense"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','businessLicense',this,1);"/>
                             	  <ol class="batch-ol">
                                       <li>
                                       	 <c:if test="${!empty business }">
                                       	 	<span class="b" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ business.docName}</span>
		                                     <input type="hidden" name="businessLicense" value="${ business.id}" />
											<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ business.id}')" class="b">下载</a> 
											<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ business.id}',this)" class="b">删除</a>
										</c:if>	
                                       </li>
                                   </ol>
                              </div>
                              <div class="showimg hide" >
                                  <img src="images/t1t2/no-logo.jpg" height="auto" width="500">
                              </div>
                          </td>
                      </tr>
                      <tr>
                          <td class="details_d1">经营许可证：</td>
                          <td class="details_d2">
                              <span class="imgspan" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ license.docName}</span>
                             <c:if test="${!empty license }">
                              	<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ license.id}')" class="a">下载</a> 
							  </c:if>
                              <div class="hide ulFile">
                                  <input type="button" name="agentinfo.license2" id="license"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','license',this,1);"/>
                                  <ol class="batch-ol">
                                       <li>
                                           <c:if test="${!empty license }">
                                           	<span class="b" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ license.docName}</span>
	                                     	<input type="hidden" name="license" value="${ license.id}">
											<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ license.id}')" class="b">下载</a> 
											<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ license.id}',this)" class="b">删除</a>
										</c:if>
                                       </li>
                                   </ol>
                              </div>
                              <div class="showimg hide" >
                                  <img src="images/t1t2/no-logo.jpg" height="auto" width="500">
                              </div>
                          </td>
                      </tr>
                      <tr>
                          <td class="details_d1">税务登记证：</td>
                          <td class="details_d2">
                              <span class="imgspan" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ taxCertificate.docName}</span>
								<c:if test="${!empty taxCertificate }">
									<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ taxCertificate.id}','${ taxCertificate.docName}',1,true)" class="a">下载</a> 
								</c:if>	
                              <div class="hide ulFile">
                                  <input type="button" name="agentinfo.taxCertificate2" id="taxCertificate"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','taxCertificate',this,1);"/>
                             	  <ol class="batch-ol">
                                       <li>
                                       		<c:if test="${!empty taxCertificate }">
                                       			<span class="a" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)" class="b">${ taxCertificate.docName}</span>
                                       			<input type="hidden" name="taxCertificate" value="${ taxCertificate.id}">
												<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ taxCertificate.id}')" class="b">下载</a> 
												<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ taxCertificate.id}',this)" class="b">删除</a>
                                       		</c:if>
                                       </li>
                                   </ol>
                              </div>
                              <div class="showimg hide" >
                                  <img src="images/t1t2/no-logo.jpg" height="auto" width="500">
                              </div>
                          </td>
                      </tr>
                      <tr>
                          <td class="details_d1">组织机构代码：</td>
                          <td class="details_d2">
                              <span class="imgspan" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ organizeCertificate.docName}</span>
                              <c:if test="${!empty organizeCertificate }">
                              		<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ organizeCertificate.id}')" class="a">下载</a> 
								</c:if>	
                              <div class="hide ulFile">
                                  <input type="button" name="agentinfo.organizeCertificate2" id="organizeCertificate"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','organizeCertificate',this,1);"/>
                             	 <ol class="batch-ol">
                                       <li>
                                           <c:if test="${!empty organizeCertificate }">
                                           		<span class="b" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ organizeCertificate.docName}</span>
                                           		<input type="hidden" name="organizeCertificate" value="${ organizeCertificate.id}">
												<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ organizeCertificate.id}')" class="b">下载</a> 
												<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ organizeCertificate.id}',this)" class="b">删除</a>
                                           </c:if>
                                       </li>
                                   </ol>
                              </div>
                              <div class="showimg hide" >
                                  <img src="images/t1t2/no-logo.jpg" height="auto" width="500">
                              </div>
                          </td>
                      </tr>
                      <tr>
                                <td class="details_d1">公司法人身份证<br/>（正反面在一起）：</td>
                                <td class="details_d2">
                                    <span class="imgspan" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ idCard.docName}</span>
                                    <c:if test="${!empty idCard }">
                                    	<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ idCard.id}')" class="a">下载</a> 
                                    </c:if>
                                    <div class="hide ulFile">
                                        <input type="button" name="agentinfo.idCard2" id="idCard"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','idCard',this,1);"/>
                                        <ol class="batch-ol">
                                       <li>
                                           <c:if test="${!empty idCard }">
                                           		<span class="b" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ idCard.docName}</span>
                                           		<input type="hidden" name="idCard" value="${ idCard.id}">
											<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ idCard.id}')" class="b">下载</a> 
											<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ idCard.id}',this)" class="b">删除</a>
                                           </c:if>
                                       </li>
                                   </ol>
                                        
                                     
                                    </div>
                                    <div class="showimg hide" >
                                        <img src="images/t1t2/no-logo.jpg" height="auto" width="500">
                                    </div>
                                </td>
                            </tr>
                      <tr>
                          <td class="details_d1">公司银行开户许可证：</td>
                          <td class="details_d2">
                              <span class="imgspan" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ bankOpenLicense.docName}</span>
                              <c:if test="${!empty bankOpenLicense }">
                              		<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ bankOpenLicense.id}')" class="a">下载</a> 
                            	</c:if>
                              <div class="hide ulFile">
                                 <input type="button" name="agentinfo.bankOpenLicense2" id="bankOpenLicense"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','bankOpenLicense',this,1);"/>
                             	<ol class="batch-ol">
                                       <li>
                                           <c:if test="${!empty bankOpenLicense }">
                                           		<span class="b" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ bankOpenLicense.docName}</span>
                                           		<input type="hidden" name="bankOpenLicense" value="${ bankOpenLicense.id}">
												<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ bankOpenLicense.id}','${ bankOpenLicense.docName}',1,true)" class="b" >下载</a> 
												<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ bankOpenLicense.id}',this)" class="b" >删除</a>
                                           </c:if>
                                       </li>
                                   </ol>
                              </div>
                              <div class="showimg hide" >
                                  <img src="images/t1t2/no-logo.jpg" height="auto" width="500"/>
                              </div>
                          </td>
                      </tr>
                      <tr>
                          <td class="details_d1">旅游业资质：</td>
                          <td class="details_d2">
                              <span class="imgspan" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ travelAptitudes.docName}</span>
                              <c:if test="${!empty bankOpenLicense }">	
                              		<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ travelAptitudes.id}')" class="a">下载</a> 
							 </c:if>		
                              <div class="hide ulFile">
                                  <input type="button" name="agentinfo.travelAptitudes2" id="travelAptitudes"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','travelAptitudes',this,1);"/>
                              	 <ol class="batch-ol">
                                       <li>
                                           <c:if test="${!empty bankOpenLicense }">
                                           		<span class="b" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${ travelAptitudes.docName}</span>
                                           		<input type="hidden" name="travelAptitudes" value="${ travelAptitudes.id}">
												<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ travelAptitudes.id}')" class="b" >下载</a> 
												<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ travelAptitudes.id}',this)" class="b" >删除</a>
                                           </c:if>
                                       </li>
                                   </ol>
                              </div>
                              <div class="showimg hide" >
                                  <img src="images/t1t2/no-logo.jpg" height="auto" width="500">
                              </div>
                          </td>
                        
                      </tr>
                      <tr>
                          <td class="details_d1">其他文件：</td>
                          <td class="details_d2">
                           <c:forEach varStatus="status" items="${elseFileList }" var="docinfo">	
                              <p><span class="imgspan" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${docinfo.docName }</span>
                              <a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ docinfo.id}')" class="a">下载</a> </p>
                             </c:forEach>  
                              <div class="hide ulFile">
                                  <input type="button" name="elseFileId[${status.index }]"   class="btn btn-primary fileUploadButton" value="上传"  onclick="uploadFiles('${ctx}','elseFile',this,1);"/>
                              	 <ol class="batch-ol">
                                       <li>
                                           <c:forEach varStatus="status" items="${elseFileList }" var="docinfo">	
                                           		<span class="b" onmouseover="showImg(this)"onmouseout="hideImg(this)"onmousemove="moveImg(this)">${docinfo.docName }</span>
                              					<input type="hidden" name="elseFile" value="${ docinfo.id}">
                              					<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ docinfo.id}')" class="b" >下载</a> 
							  					<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ docinfo.id}',this)" class="b" >删除</a>
                                           </c:forEach>
                                       </li>
                                       
                                   </ol>
                              </div>
                             <!--  <div class="showimg hide" >
                                  <img src="images/t1t2/no-logo.jpg" height="auto" width="500">
                              </div> -->
                          </td>
                      </tr>
                  </tbody>
              </table>
              <div class="cancelSave hide">
                  <input type="button"value="取&nbsp;消" onclick="cancelthis(this)"class="button1 gray">
                  <input type="button"value="保&nbsp;存" onclick="saveQualifications()" class="button1 savebutton">
              </div>
          </div>
      </div>
      </div>
      </div>
  <!--main end-->
  		   </div>
    <!--footer start-->
    <div class="footer">
         Copyright © 2016 旅游交易管理系统　　客服电话：010-85718666
    </div>
</body>

</html>
