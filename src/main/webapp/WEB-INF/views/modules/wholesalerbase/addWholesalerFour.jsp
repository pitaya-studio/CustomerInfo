<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<head>
<meta name="decorator" content="wholesaler" />
<title>
	<c:if test="${newORold==0 }">批发商管理-新增第4步-资质</c:if>
	<c:if test="${newORold==1 }">批发商管理-修改第4步-资质</c:if>
</title>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"  type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/ckeditor/ckeditor.js"></script>
<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
<!--供应商模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.supplier.js"></script>
<script type="text/javascript">
	$(function(){
		//上传动作
		btfile();
		//inpout 活的焦点
		inputTips();
		//渠道资质添加
		qdzz_add();
		// 返回
		$("#back").click(function(){
			location.href=contextPath + "/manage/saler/salerlist";
		});
		$("#last").click(function(){
			// 跳转到上一步页面
			location.href=contextPath + "/manage/saler/backWholeOfficeThree/${office.id}/${newORold}";
		});
		// 提交
		$("#save").click(function(){
   		 	$("#save").attr("disabled","disabled");
			//$("input.ydbz_s").removeAttr("disabled");
			subForm();
		});
	});
	
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
   		$(obj).next("span.thecheckbox").empty(); // 清掉原来的对象 
     	if(fileIDList){
     		var arrID = new Array();
     		arrID = fileIDList.split(';');
     		var arrName = new Array();
     		arrName = fileNameList.split(';');
     		var arrPath = new Array();
     		arrPath = filePathList.split(';');
     		for(var n=0;n<arrID.length;n++){
     			if(arrID[n]){
     				var $a = $("<a>"+arrName[n]+"</a>");
     				$a.append("<input type='hidden' name='salerTripFileId' value='"+arrID[n]+"'/>");
     				$a.append("<input type='hidden' name='salerTripFileName' value='"+arrName[n]+"'/>");
     				$a.append("<input type='hidden' name='salerTipFilePath' value='"+arrPath[n]+"'/>");
     				//$(obj).next("span.seach_checkbox").append($a);
     				$(obj).next("span.thecheckbox").append($a);
     			}
     		}
     	}
     }
     /*
     * 遍历已经上传的附件，并把与附件相关的titleType 和 title 一并打包上传
     */
     function subForm(){
    	 var n = 0;
    	 // 遍历已经上传的附件
    	 $("div.batch input[name=salerTripFileId]").each(function(){
    		 var salerTripFileId = $(this).val();
    		var salerTripFileName =	 $(this).parents("div.batch").find("input[name=salerTripFileName]").val();
    		var salerTipFilePath = $(this).parents("div.batch").find("input[name=salerTipFilePath]").val();
    		var titleType = $(this).parents("div.batch").find("input[name=titleType]").val();
    		var title = $(this).parents("div.batch").find("input[name=title]").val();
    		if(!title){
    			n++;
    		}
    		$("#subForm").append("<input type='hidden' name='title' value='"+title+"' />");
    		$("#subForm").append("<input type='hidden' name='titleType' value='"+titleType+"' />");
    		$("#subForm").append("<input type='hidden' name='salerTripFileId' value='"+salerTripFileId+"' />");
    		$("#subForm").append("<input type='hidden' name='salerTripFileName' value='"+salerTripFileName+"' />");
    		$("#subForm").append("<input type='hidden' name='salerTipFilePath' value='"+salerTipFilePath+"' />");
    		
    	 });
    	 $("#subForm").append("<input type='hidden' name='companyId' value='${office.id}' />");
    	 if(n==0){
    		 ajaxSub();
    	 }else{
    		 jBox.tip("'其他文件'的文件名不能为空","info");
    		 $("#save").removeAttr("disabled");
    	 }
     }
     // 提交附件表单
     function ajaxSub(){
    	 var param = $("#subForm").serialize();
    	 $.ajax({
    		 type:"POST",
    		 url : contextPath+"/manage/saler/addWholeOfficeFour",
    		 data : param,
 			dataType : "text",
 			success:function(html){
 				var json;
 				try{
 					json = $.parseJSON(html);
 				}catch(e){
 					json = {res:"error"};
 				}
 				if(json.res=="success"){
					jBox.tip("提交成功", 'info');
					location.href =contextPath + "/manage/saler/salerlist";
				}else if(json.res=="data_error"){
					jBox.tip(json.mes,"info");
					$("#save").removeAttr("disabled");
				}else{
					jBox.tip("系统繁忙，请稍后再试", 'error');
					$("#save").removeAttr("disabled");
				}
 			}
    	 
    	 });
     }
     
     // 增加其他上传框
     function addOther(obj){
    	 //var id =$(div[class='other']).length();
    	 var cloneDiv = $("#other").clone(true);
    	 cloneDiv.appendTo($("#file"));
    	 cloneDiv.show().attr("id","");
    	 cloneDiv.css("margin-top","10px");
     }
     
</script>
</head>
<body>
            	<!--右侧内容部分开始-->
				<div class="supplierLine">
					<a href="javascript:void(0)">基本信息填写</a>
                    <a href="javascript:void(0)">网站信息</a>
					<a href="javascript:void(0)">银行账户</a>
					<a href="javascript:void(0)" class="select">资质上传</a>
					<!-- 用于区分新增/修改批发商；0：新增；1：修改; 默认为新增 -->
					<input type="hidden"  name="newORold" value="0"/>
				</div>
				<div class="qdgl-cen mod_information_3 ">
					<div id="file">
						<div class="batch" style="margin-top:10px;">
	                      <label class="batch-label">营业执照：</label>
	                      
	                      <input type="button" name="acctype1"  class="btn btn-primary" value="上传文件"  onclick="uploadFiles(' ${ctx}','passportfile',this,'true');"/>
	           			   <span class="thecheckbox">
	           			   		<c:if test="${not empty qual1 }">
		           			   		<a>${qual1.docInfoName }
		           			   			<input type="hidden"  name="salerTripFileId"  value="${qual1.docInfoId }"/>
		           			   			<input type="hidden"  name="salerTripFileName"  value="${qual1.docInfoName }"/>
		           			   			<input type="hidden"  name="salerTipFilePath"  value="${qual1.docInfoPath }"/>
		           			   		</a>
	           			   		</c:if>
	           			   </span>
	           			   <span class="fileLogo"></span>
	           			   <input type="hidden" name="titleType"  value="1"/>
	           			   <input type="hidden" name="title"  value="营业执照"/>
	                    </div>
	                    
	                    <div class="batch" style="margin-top:10px;">
	                      <label class="batch-label">经营许可证：</label>
	                      <input type="button" name="acctype2"   class="btn btn-primary" value="上传文件"  onclick="uploadFiles(' ${ctx}','passportfile',this,'true');"/>
	           			   <span class="thecheckbox">
	           			   		<c:if test="${not empty qual2 }">
		           			   		<a>${qual2.docInfoName }
		           			   			<input type="hidden"  name="salerTripFileId"  value="${qual2.docInfoId }"/>
		           			   			<input type="hidden"  name="salerTripFileName"  value="${qual2.docInfoName }"/>
		           			   			<input type="hidden"  name="salerTipFilePath"  value="${qual2.docInfoPath }"/>
		           			   		</a>
	           			   		</c:if>
	           			   </span>
	           			   <span class="fileLogo"></span>
	           			   <input type="hidden" name="titleType"  value="2"/>
	           			   <input type="hidden" name="title"  value="经营许可证"/>
	                    </div>
	                    
	                    <div class="batch" style="margin-top:10px;">
	                      <label class="batch-label">税务登记证：</label>
	                      <input type="button" name="passport"  class="btn btn-primary" value="上传文件"  onclick="uploadFiles(' ${ctx}','passportfile',this,'true');"/>
	           			   <span class="thecheckbox">
	           			   		<c:if test="${not empty qual3 }">
		           			   		<a>${qual3.docInfoName }
		           			   			<input type="hidden"  name="salerTripFileId"  value="${qual3.docInfoId }"/>
		           			   			<input type="hidden"  name="salerTripFileName"  value="${qual3.docInfoName }"/>
		           			   			<input type="hidden"  name="salerTipFilePath"  value="${qual3.docInfoPath }"/>
		           			   		</a>
	           			   		</c:if>
	           			   </span>
	           			   <span class="fileLogo"></span>
	           			   <input type="hidden" name="titleType"  value="3"/>
	           			   <input type="hidden" name="title"  value="税务登记证"/>
	                    </div>
	                    
	                    <div class="batch" style="margin-top:10px;">
	                      <label class="batch-label">组织机构代码证：</label>
	                      <input type="button" name="passport"  class="btn btn-primary" value="上传文件"  onclick="uploadFiles(' ${ctx}','passportfile',this,'true');"/>
	           			   <span class="thecheckbox">
	           			   		<c:if test="${not empty qual4 }">
		           			   		<a>${qual4.docInfoName }
		           			   			<input type="hidden"  name="salerTripFileId"  value="${qual4.docInfoId }"/>
		           			   			<input type="hidden"  name="salerTripFileName"  value="${qual4.docInfoName }"/>
		           			   			<input type="hidden"  name="salerTipFilePath"  value="${qual4.docInfoPath }"/>
		           			   		</a>
	           			   		</c:if>
	           			   </span>
	           			   <span class="fileLogo"></span>
	           			   <input type="hidden" name="titleType"  value="4"/>
	           			   <input type="hidden" name="title"  value="组织机构代码证"/>
	                    </div>
	                    
	                    <div class="batch" style="margin-top:10px;">
	                      <label class="batch-label">公司法人身份证（正反面一起）：</label>
	                      <input type="button" name="passport"  class="btn btn-primary" value="上传文件"  onclick="uploadFiles(' ${ctx}','passportfile',this,'true');"/>
	           			   <span class="thecheckbox">
	           			   		<c:if test="${not empty qual5 }">
		           			   		<a>${qual5.docInfoName }
		           			   			<input type="hidden"  name="salerTripFileId"  value="${qual5.docInfoId }"/>
		           			   			<input type="hidden"  name="salerTripFileName"  value="${qual5.docInfoName }"/>
		           			   			<input type="hidden"  name="salerTipFilePath"  value="${qual5.docInfoPath }"/>
		           			   		</a>
	           			   		</c:if>
	           			   </span>
	           			   <span class="fileLogo"></span>
	           			   <input type="hidden" name="titleType"  value="5"/>
	           			   <input type="hidden" name="title"  value="公司法人身份证（正反面一起）"/>
	                    </div>
	                    
	                    <div class="batch" style="margin-top:10px;">
	                      <label class="batch-label">公司银行开户许可证：</label>
	                      <input type="button" name="passport"  class="btn btn-primary" value="上传文件"  onclick="uploadFiles(' ${ctx}','passportfile',this,'true');"/>
	           			   <span class="thecheckbox">
	           			   		<c:if test="${not empty qual6 }">
		           			   		<a>${qual6.docInfoName }
		           			   			<input type="hidden"  name="salerTripFileId"  value="${qual6.docInfoId }"/>
		           			   			<input type="hidden"  name="salerTripFileName"  value="${qual6.docInfoName }"/>
		           			   			<input type="hidden"  name="salerTipFilePath"  value="${qual6.docInfoPath }"/>
		           			   		</a>
	           			   		</c:if>
	           			   </span>
	           			   <span class="fileLogo"></span>
	           			   <input type="hidden" name="titleType"  value="6"/>
	           			   <input type="hidden" name="title"  value="公司银行开户许可证"/>
	                    </div>
	                    
	                    <div class="batch" style="margin-top:10px;">
	                      <label class="batch-label">旅游业资质：</label>
	                      <input type="button" name="passport"  class="btn btn-primary" value="上传文件"  onclick="uploadFiles(' ${ctx}','passportfile',this,'true');"/>
	           			   <span class="thecheckbox">
	           			   		<c:if test="${not empty qual7 }">
		           			   		<a>${qual7.docInfoName }
		           			   			<input type="hidden"  name="salerTripFileId"  value="${qual7.docInfoId }"/>
		           			   			<input type="hidden"  name="salerTripFileName"  value="${qual7.docInfoName }"/>
		           			   			<input type="hidden"  name="salerTipFilePath"  value="${qual7.docInfoPath }"/>
		           			   		</a>
	           			   		</c:if>
	           			   </span>
	           			   <span class="fileLogo"></span>
	           			   <input type="hidden" name="titleType"  value="7"/>
	           			   <input type="hidden" name="title"  value="旅游业资质"/>
	                    </div>
	                    <!-- 
	                    <div class="batch" style="margin-top:10px;">
	                      <label class="batch-label">其他文件：</label><input type="text" name="title" value=""  maxlength="20"/>
	                      <input type="button" name="passport"  class="btn btn-primary" value="上传文件"  onclick="uploadFiles(' ${ctx}','passportfile',this,'true');"/>
	           			   <span class="thecheckbox"></span>
	           			   <span class="fileLogo"></span>
	           			   <input type="hidden" name="titleType"  value="8"/>
	                    </div> -->
	                    <div class="batch" style="display:none" id="other">
	                      <label class="batch-label">其他文件：</label><input type="text" name="title" value=""  maxlength="20" title="文件名称"/>
	                      <input type="button" name="passport"  class="btn btn-primary" value="上传文件"  onclick="uploadFiles(' ${ctx}','passportfile',this,'true');"/>
	           			   <span class="thecheckbox"></span>
	           			   <span class="fileLogo"></span>
	           			   <input type="hidden" name="titleType"  value="8"/>
	                    </div>
	                    <!-- 导入其他文件 -->
	                    <c:if test="${not empty qualList }">
	                    	<c:forEach items="${qualList }"  var = "qual">
		                    	<div class="batch" style="margin-top:10px;" id="">
			                      <label class="batch-label">其他文件：</label><input type="text" name="title" value="${qual.title }"  maxlength="20" title="文件名称"/>
			                      <input type="button" name="passport"  class="btn btn-primary" value="上传文件"  onclick="uploadFiles(' ${ctx}','passportfile',this,'true');"/>
			           			   <span class="thecheckbox">
			           			   		<c:if test="${not empty qual }">
				           			   		<a>${qual.docInfoName }
				           			   			<input type="hidden"  name="salerTripFileId"  value="${qual.docInfoId }"/>
				           			   			<input type="hidden"  name="salerTripFileName"  value="${qual.docInfoName }"/>
				           			   			<input type="hidden"  name="salerTipFilePath"  value="${qual.docInfoPath }"/>
				           			   		</a>
			           			   		</c:if>
			           			   </span>
			           			   <span class="fileLogo"></span>
			           			   <input type="hidden" name="titleType"  value="8"/>
			                    </div>
	                    	</c:forEach>
	                    </c:if>
                    </div>
                    <!-- 添加其他文件 -->
					<div class="add" style="margin-left:150px;">
						<label class="batch-label" ><input type="button" name="addFileDiv"  value="添加+"  class="ydbz_s" onclick="addOther(this)"/></label>
					</div>
					<form id="subForm" style="display:none">
					
					</form>
				</div>
				<div class="dbaniu " style=" margin-left:100px;">
					<input type="hidden" name="belongParentPlatId"  value="${office.id }"/>
					<a class="ydbz_s gray" href="javascript:void(0)"  id="back">返回</a>
					<a class="ydbz_s" href="javascript:void(0)"  id="last">上一步</a>
					<a class="ydbz_s" href="javascript:void(0)"  id="save">提交</a>
				</div>
				<!--右侧内容部分结束-->
</body>
</html>
