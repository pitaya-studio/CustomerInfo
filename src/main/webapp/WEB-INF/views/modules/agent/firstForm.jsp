<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>渠道管理-新增基本信息</title>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

<script type="text/javascript">

function delSalaryPerson(obj){
		$(obj).next().next().remove();
		$(obj).next().remove();
		$(obj).prev().remove();
		$(obj).remove();
	}
	
$(function(){
	//S 185
	inquiryCheckBOX();
     $("select[id='agentSalerUser']").comboboxInquiry({
        "afterInvalid":function(event,data){
            var selectValue = $(this).val();
            if(selectValue == undefined || selectValue == null || selectValue == "") {
            	return;
            }
            var Array_default = new Array("搜索渠道跟进");
            if(-1 == $.inArray(data,Array_default)){
                var isIncluded = 0;
                $("#salerShow a").each(function(index, element) {
                    if(data == $(element).text()){
                        isIncluded = 1;
                        return;
                    }
                });
                if(isIncluded){
                    jBox.tip("您已选择  " + data);
                }else{
                    $("#salerShow").append('<a>{0}<a class="delete" href="javascript:void(0);"  onclick="delSalaryPerson(this)">删除</a><input type="hidden" name="agentinfo.salerIdArray" value="{1}"/><input type="hidden" name="agentinfo.salerNameArray" value="{2}"/></a>'
                    	.replace("{0}",data).replace("{1}",selectValue).replace("{2}",data));
                    	$("input.custom-combobox-input").val("请选择");
                }
            }
        }
	});
	
	
    //E 185
	$("#agentSalerUser").comboboxInquiry();
	
	//输入渠道公司名称后，自动输出其首字母 ， 且第一个字符不能为特殊字符 
	$("#agentName").on('blur',function(){
// 		$(this).keyup(function(){
// 			$(this).val($(this).val().replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,''));
// 		}).bind("paste",function(){
// 			$(this).val($(this).val().replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,''));
// 		});
		
		$.ajax({
			type:"POST",
			url: "${ctx}/agent/manager/firstLetter",
			data: {"agentName":$('#agentName').val()},
			datatype:"json",
			success: function(data){
				$("#agentFirstLetter").val(data);
			}
		});
	 });
	
	//渠道首字母统一为大写, 且只能为数字或者英文字符 
	$("#agentFirstLetter").on('blur',function(){
// 		$(this).keyup(function(){
// 			$(this).val($(this).val().replace(/[^a-zA-Z0-9]/,''));
// 		}).bind("paste",function(){
// 			$(this).val($(this).val().replace(/[^a-zA-Z0-9]/,''));
// 		});		
		
		var agentFirstLetterUpper = $("#agentFirstLetter").val().toUpperCase();
		$("#agentFirstLetter").val(agentFirstLetterUpper);
	});
	
	//所属地区 国家、省（直辖市）、市区联动下拉框
	$("span.countryCityArea select").change(function(){
			var parentId = $(this).val();
			var url = "${ctx}"+"/agent/manager/getAreaInfoById/"+parentId;
			var currentClass = $(this).attr('class');
			if(parentId.length>0){
				$.ajax({
					   type: "POST",
					   url: url,
					   dataType:"json",
					   success: function(msg){
					     	if(currentClass=="country"){
			     		  		$("span.countryCityArea .province").empty();
			     		  		$("span.countryCityArea .city").empty();
			     		  		$("span.countryCityArea .province").append("<option value=''>省(直辖市)</option>");
			     		  		$("span.countryCityArea .city").append("<option value=''>市(区)</option>");
					     		$.each(msg,function(i, n){
					     		  		var optionStr = "<option value='"+n.id+"'>"+n.name+"</option>";

					     		  		$("span.countryCityArea .province").append(optionStr);
					     		});
					     	}
				     		if(currentClass=="province"){
				     			$("span.countryCityArea .city").empty();
				     			$("span.countryCityArea .city").append("<option value=''>市(区)</option>");
				     			$.each(msg,function(i, n){
					     		var optionStr = "<option value='"+n.id+"'>"+n.name+"</option>";
				     			$("span.countryCityArea .city").append(optionStr);
				     			});
				     		}
					   }
					});
			}else{
		     	if(currentClass=="country"){
	 		  		$("span.countryCityArea .province").empty();
	 		  		$("span.countryCityArea .city").empty();
	 		  		$("span.countryCityArea .province").append("<option value=''>省(直辖市)</option>");
	 		  		$("span.countryCityArea .city").append("<option value=''>市(区)</option>");
		     	}
	     		if(currentClass=="province"){
	     			$("span.countryCityArea .city").empty();
	     			$("span.countryCityArea .city").append("<option value=''>市(区)</option>");
	     		}
			}
	});
	// 公司地址 国家、省（直辖市）、市区联动下拉框
	$("span.countryCityAddress select").change(function(){
		var parentId = $(this).val();
		var url = "${ctx}"+"/agent/manager/getAreaInfoById/"+parentId;
		var currentClass = $(this).attr('class');
		if(parentId.length>0){
			$.ajax({
				   type: "POST",
				   url: url,
				   dataType:"json",
				   success: function(msg){
				     	if(currentClass=="country"){
		     		  		$("span.countryCityAddress .province").empty();
		     		  		$("span.countryCityAddress .city").empty();
		     		  		$("span.countryCityAddress .province").append("<option value=''>省(直辖市)</option>");
		     		  		$("span.countryCityAddress .city").append("<option value=''>市(区)</option>");
				     		$.each(msg,function(i, n){
				     		  		var optionStr = "<option value='"+n.id+"'>"+n.name+"</option>";

				     		  		$("span.countryCityAddress .province").append(optionStr);
				     		});
				     	}
			     		if(currentClass=="province"){
			     			$("span.countryCityAddress .city").empty();
			     			$("span.countryCityAddress .city").append("<option value=''>市(区)</option>");
			     			$.each(msg,function(i, n){
				     		var optionStr = "<option value='"+n.id+"'>"+n.name+"</option>";
			     			$("span.countryCityAddress .city").append(optionStr);
			     			});
			     		}
					}
				});
		}else{
	     	if(currentClass=="country"){
 		  		$("span.countryCityAddress .province").empty();
 		  		$("span.countryCityAddress .city").empty();
 		  		$("span.countryCityAddress .province").append("<option value=''>省(直辖市)</option>");
 		  		$("span.countryCityAddress .city").append("<option value=''>市(区)</option>");
	     	}
     		if(currentClass=="province"){
     			$("span.countryCityAddress .city").empty();
     			$("span.countryCityAddress .city").append("<option value=''>市(区)</option>");
     		}
		}
	});
	
	
	//渠道结款方式
	//agentpayfor();
	//上传动作
	btfile();
	//日期，点击
	$( ".spinner" ).spinner({
		spin: function( event, ui ) {
			var month=ui.value;
			if ( ui.value > 31 ) {
				$( this ).spinner( "value", 1 );
				$(this).parents("span").find("i").text("重复：每月1日");
				return false;
			} else if ( ui.value < 1 ) {
				$( this ).spinner( "value", 31 );
				$(this).parents("span").find("i").text("重复：每月最后一天");
				return false;
			} else if ( ui.value==31){
				$(this).parents("span").find("i").text("重复：每月最后一天");
			}else{
				$(this).parents("span").find("i").text("重复：每月"+month+"日");
			}
		}
	});
	//默认缓存判断
    var spinnerval=$(".spinner").val();
	if(spinnerval==31){
		$(".spinner").parents("span").find("i").text("重复：每月最后一天");
	}else{
		$(".spinner").parents("span").find("i").text("重复：每月"+spinnerval+"日");
	}
	
	$(".weekSettlement").change(function(){
		var weekText = $(this).find("option:selected").text();
		$(this).parents("span").find("i").text("重复：每周"+weekText);
	});
	
});	
//输入判断
function spinnerInput(obj){
	var ms = obj.value.replace(/\D/g,'');
	if(obj.value>=31){
		$(".spinner").parents("span").find("i").text("重复：每月最后一天");
		if(obj.value > 31){
			obj.value = 31;
		}
	}else{
		$(".spinner").parents("span").find("i").text("重复：每月"+obj.value+"日");
	}
}
//文件上传回调
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
     		for(var n=0;n<arrID.length;n++){
     			if(arrID[n]){//<a href='javascript:void(0);' onclick=\"deleteFiles('"+arrID[n]+"');\">删除</a>
     				var $a = $("<li><span><b><a>"+arrName[n]+"</a></b><a href='javascript:void(0);' onclick=\"downloads('"+arrID[n]+"');\">下载</a><a href='javascript:void(0);' onclick=\"deleteFiles(this);\">删除</a><span></li>");
     				$a.append("<input type='hidden' name='agentinfo.logo' value='"+arrID[n]+"'/>");
     				$a.append("<input type='hidden' name='salerTripFileName' value='"+arrName[n]+"'/>");
     				$a.append("<input type='hidden' name='salerTipFilePath' value='"+arrPath[n]+"'/>");
     				$("#upfileShow").append($a);
     			}
     		}
     	}
     }

	//渠道商联系人添加
	function shopPeopleAdd2(obj){
		 var id=$(obj).parent().parent().find('p').length;
	     var cloneDiv = $(".shopPeopleNone").clone(true);
	     cloneDiv.appendTo($(obj).parent().parent());
	     cloneDiv.show().removeClass('shopPeopleNone').addClass("shopPeopleP").find('em').text(id);
	     valueHtml = cloneDiv.html().replace(/name=\"/g,"name=\"contacts.supplyContactses["+(parseInt(id)-2)+"].");
	     cloneDiv.html(valueHtml);
	}
	function shopPeopleDel2(obj){
		$(obj).parent().remove();
		$('.shopPeopleP').each(function(index, element){
		   $(this).find('em').text(index+1);
		   if(index>0){
			   $(this).find("input").each(function(i,elt){
				   var str = $(this).attr("name");
				   var arr = str.split(".");
				   var trueName =  arr[2];
				   trueName = "contacts.supplyContactses["+(index-1)+"]."+trueName;
				   $(this).attr("name",trueName);
			   });
		   }
	      });
		}
	//文件下载
	function downloads(docid){
		window.open("${ctx}/sys/docinfo/download/"+docid);
    }
	//删除现有的文件
	function deleteFiles(obj) {
		top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
			if(v=='ok'){
				$(obj).closest("li").remove();
			}
			},{buttonsFocus:1});
	}
	function submitForm(){
		var brandVal = $("#agentBrand").val();
		var agentName = $("#agentName").val();
		var agentFirstLetter = $("#agentFirstLetter").val().toUpperCase();
// 		var salerUser = $("#agentSalerUser").val();
		var salerNum = $("#salerShow").children().size();
		var paymentType = $("#paymentType").val();
		var agentContact = $("#agentContact").val();
		var agentContactMobile = $("#agentContactMobile").val();
		var agentNameShort = $("#agentNameShort").val();
		//越柬行踪添加QQ必填验证
		var agentContactQQ = $("#agentContactQQ").val();
		var isValidateQQ = $("#isValidateQQ").val();
		var b = true;
		if(brandVal==""||brandVal==null|| !$.trim(brandVal)){
			b = false;
			top.$.jBox.info("渠道品牌为必填项", "警告");
			return;
		}
		if(agentName==""||agentName==null){
			b = false;
			top.$.jBox.info("渠道公司名称为必填项", "警告");
			return;
		}
		if(agentFirstLetter==""||agentFirstLetter == null){
			b = false;
			top.$.jBox.info("渠道名称首字母为必填项", "警告");
		}
		if(salerNum=="" || salerNum==null || salerNum==0){
			b = false;
			top.$.jBox.info("跟进销售为必填项", "警告");
			return ;
		}
		if(paymentType==""||paymentType==null){
			b = false;
			top.$.jBox.info("结款方式为必填项", "警告");
			return ;
		}
		if(agentContact==""||agentContact==null||!$.trim(agentContact)){
			b = false;
			top.$.jBox.info("联系人1姓名为必填项", "警告");
			return ;
		}
		if(agentContactMobile==""||agentContactMobile==null ||!$.trim(agentContactMobile)){
			b = false;
			top.$.jBox.info("联系人1手机为必填项", "警告");
			return ;
		}
		//越柬行踪添加QQ必填验证
		if(isValidateQQ&&(agentContactQQ==""||agentContactQQ==null||!$.trim(agentContactQQ))){
			b = false;
			top.$.jBox.info("联系人1QQ为必填项", "警告");
			return ;
		}
		//美途国际添加简称校验
		if(("${agentNameShortFlag}" == '1') && ($.trim(agentNameShort) == "")) {
			b = false;
			top.$.jBox.info("简称为必填项", "警告");
			$("#agentNameShort").focus();
			return ;
		}
		//非按月结算和按周结算时，将借款日期变为不可用
		var sysselect_s=$(".agentpayfor option:selected").index();
		if(sysselect_s != 1 || sysselect_s != 4) {
			$(".spinner").attr("disabled", true);
			$(".weekSettlement").attr("disabled", true);
		}
		
			//结款日期设置
		if($("#paymentType").find("option:selected").text().trim() == '按月结算' ){
			$("#paymentDay1").val($("#paymentDayShow").attr("aria-valuenow"));
		}else if($("#paymentType").find("option:selected").text().trim() == '按周结算'){
			$("#paymentDay1").val($("#paymentDayShowWeek").val());
		}else if($("#paymentType").find("option:selected").text().trim() != '按周结算'){
			$("#paymentDay1").val(null);
		}
		if(b){
			$("#inputForm").submit();
		}
	}
</script>
</head>
<body>
<style type="text/css">label{ cursor:inherit;}</style>
<page:applyDecorator name="agent_op_head" >

</page:applyDecorator>
        <!--右侧内容部分开始-->
		<div class="supplierLine">
			<a href="javascript:void(0)" class="select">基本信息填写</a>
			<a href="javascript:void(0)">银行账户</a>
			<a href="javascript:void(0)">资质上传</a>
		</div>
        <div class="sysdiv sysdiv_coupon">

          <form  method="post" action="${ctx}/agent/manager/saveFirstForm" class="form-horizontal" id="inputForm" name="inputForm" >
            <p>
              <label><em class="xing">*</em>渠道品牌：</label>
              <span>
              <input id="agentBrand" name="agentinfo.agentBrand" type="text" maxlength="50">
              </span>
			</p>
            <p>
              <label><em class="xing">*</em>渠道公司名称：</label>
              <span>
              <input id="agentName" name="agentinfo.agentName" type="text" maxlength="50"
             	 onkeyup="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"
             	 onkeydown="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"
              	 onafterpast="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')">
              </span>
			</p>
			<p>
				<label><em class="xing">*</em>渠道名称首字母：</label> 
				<span><input id="agentFirstLetter" name="agentinfo.agentFirstLetter" type="text" maxlength="1" value="${agentinfo.agentFirstLetter}"
					onkeyup="this.value=this.value.replace(/[^a-zA-Z0-9]/,'')"
					onafterpaste="this.value=this.value.replace(/[^a-zA-Z0-9]/,'')">
				</span>
			</p>
            <p>
			  <label>英文名称：</label>
              <span>
              <input name="agentinfo.agentNameEn" type="text" maxlength="50">
              </span>
              <c:if test="${agentNameShortFlag == 1}">
	              <label><em class="xing">*</em>简称：</label>
	              <span>
	              <input name="agentinfo.agentNameShort" id="agentNameShort"  onkeyup="value=value.replace(/[\W]/g,'') "   type="text" maxlength="50">
	              </span>
              </c:if>
			</p>
            <p>
              <label>所属地区：</label>
              <span class="sysselect_s countryCityArea">
                <select class="country" name="agentinfo.belongsArea">
                	<option value="">国家</option>
                	<c:forEach  items="${areaMap}" var="aa">
                		<option value="${aa.key}">${aa.value}</option>
                	</c:forEach>
                </select>
                <select class="province" name="agentinfo.belongsAreaProvince">
                	<option value="">省(直辖市)</option>
                </select>
                <select class="city" name="agentinfo.belongsAreaCity">
                 	<option value="">市(区)</option>
                </select>
              </span>
			</p>
			<!--S 185-->
			<p>
				<label><em class="xing">*</em>跟进销售人员：</label>
                <span class="sysselect_s">
                    <select name="agentinfo.agentSalerUser.id" id="agentSalerUser" style="display: none;">
                    	<option value="" selected="selected">请选择</option>
                    	<c:forEach items="${agentSalers}" var="as">
                			<option value="${as.key}">${as.value}</option>
                		</c:forEach>
                    </select>
                </span>
               	<div id="salerShow" class="seach_checkbox"></div>
            </p>
            <!--E 185-->
             <p>
             <input type="hidden" name="agentinfo.paymentDay" id="paymentDay1"/>
              <label><em class="xing">*</em>结款方式：</label>
              <span class="sysselect_s agentpayfor">
                <select id="paymentType" onchange="agentpayfor(this)" name="agentinfo.paymentType">
                	<c:forEach items="${paymentMap}" var="pm">
                		<option value="${pm[0]}">${pm[1]}</option>
                	</c:forEach>
                </select>
              </span>
              <!-- 月结算控制 -->
              <span class="agentdatetips"><label>结款日期：</label><input name="" id="paymentDayShow" value="1" class="spinner daySettlement" maxlength="2" onafterpaste="spinnerInput(this)" onkeyup="spinnerInput(this)" onblur="spinnerInput(this)">日<i>重复：每月1日</i></span>
              <!-- 周结算控制 -->
              <span class="agentdatetips"><label>结款日期：</label>
              		<select name="" class="weekSettlement" style="width:100px;" id="paymentDayShowWeek">
              			<option value="1">周一</option>
              			<option value="2">周二</option>
              			<option value="3">周三</option>
              			<option value="4">周四</option>
              			<option value="5">周五</option>
              			<option value="6">周六</option>
              			<option value="7">周日</option>
              		</select>
              		<i>重复：每周周一</i>
              </span>
               <span><label>门市名称：</label><input name="agentinfo.salesRoom"  type="text" maxlength="50"
             	 onkeyup="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"
             	 onkeydown="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"
              	 onafterpast="this.value=this.value.replace(/^[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"></span>
			</p>
            <p>
              <label>公司地址：</label>
              <span class="sysselect_s countryCityAddress">
                 <select class="country" name="agentinfo.agentAddress">
                	<option value="">国家</option>
                	<c:forEach  items="${areaMap}" var="aa">
                		<option value="${aa.key}">${aa.value}</option>
                	</c:forEach>
                </select>
                <select class="province" name="agentinfo.agentAddressProvince">
                	<option value="">省(直辖市)</option>
                </select>
                <select class="city" name="agentinfo.agentAddressCity">
                 	<option value="">市(区)</option>
                </select>
                <input name="agentinfo.agentAddressStreet" type="text" class="" maxlength="100">
              </span>
			</p>
			<p>
              <label>渠道邮编：</label>
             <span><input name="agentinfo.agentPostcode" type="text"  maxlength="20"></span>
			</p>
            <p>
              <label>电话：</label>
              <span><input name="agentinfo.agentTelAreaCode" type="text" class="sysinput_s" maxlength="12"><span class="sysinput_span">-</span><input name="agentinfo.agentTel" type="text" class="inputTxt" maxlength="20">例如：010-87475943</span>
			</p>
            <p>
              <label>传真：</label>
             <span><input name="agentinfo.agentFaxAreaCode" type="text" class="sysinput_s" maxlength="8"><span class="sysinput_span">-</span><input name="agentinfo.agentFax" type="text" class="inputTxt" maxlength="20">例如：010-87475943</span>
			</p>
            <div>
             <p class="shopPeopleP"><label><a style="color:#f00;">*</a>&nbsp;联系人<em>1</em>：</label>
               <span><input id="agentContact" name="agentinfo.agentContact" type="text" maxlength="9"></span>
               <label><a style="color:#f00;">*</a>&nbsp;手机：</label>
               <span><input id="agentContactMobile" name="agentinfo.agentContactMobile" type="text" maxlength="11"></span>
                 <%--bug17525 统一按钮样式 by tlw at20170309--%>
                 <%--<a class="ydbz_x" onclick="shopPeopleAdd2(this)">添加</a>--%>
                 <input type="button"  class="btn btn-primary" value="添加"onclick="shopPeopleAdd2(this)">
                 <span class="kongr20"></span>
               <label>固定电话：</label>
               <span><input name="agentinfo.agentContactTel" type="text" maxlength="12"></span>
               <label>传真：</label>
               <span><input name="agentinfo.agentContactFax" type="text" maxlength="20"></span>
               <span class="kongr20"></span>
               <label>Email：</label>
               <span><input name="agentinfo.agentContactEmail" type="text" maxlength="50"></span>
               <label>
               	<c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
               		<a style="color:#f00;">*</a>&nbsp;<input id="isValidateQQ"  value="true" type="hidden">
               	</c:if>
              		 QQ：
               </label>
               <span><input id="agentContactQQ"  name="agentinfo.agentContactQQ" type="text" maxlength="14"></span>
             </p>
             <p style="display:none" class="shopPeopleNone"><label>联系人<em></em>：</label>
               <span><input name="contactName" type="text" maxlength="9"></span>
               <label>手机：</label>
               <span><input name="contactMobile" type="text" maxlength="11"></span>
                 <%--bug17525 统一按钮样式 by tlw at20170309--%>
                 <%--<a class="ydbz_x gray" onclick="shopPeopleDel2(this)">删除</a>--%>
                 <input type="button"  class="btn" value="删除"onclick="shopPeopleDel2(this)">
                 <span class="kongr20"></span>
               <label>固定电话：</label>
               <span><input name="contactPhone" type="text" maxlength="12"></span>
               <label>传真：</label>
               <span><input name="contactFax" type="text" maxlength="20"></span>
               <span class="kongr20"></span>
               <label>Email：</label>
               <span><input name="contactEmail" type="text" maxlength="50"></span>
               <label>QQ：</label>
               <span><input name="contactQQ" type="text" maxlength="14"></span>
              </p>
            </div>
			<p>
              <label>描述：</label>
              <span>
              <textarea name="agentinfo.remarks" rows="3" class="input-xlarge" maxlength="200"></textarea>
              </span>
			</p>
			<p>
			  <label>公司LOGO：</label>
			  <span>
			  		   <input type="button" name="passport"  id="uploadMoreFile" class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','passportfile',this,1);"/>
           			   <span id="upfileShow" class="seach_checkbox_no_event"></span>
           			   <span class="fileLogo"></span>
			</p>
            <p>
              <label>&nbsp;</label>
              <span>
                  <%--bug17525 统一按钮样式 by tlw at20170309--%>
                <%--<a class="ydbz_x submit" href="javascript:void(0);" onclick="submitForm();">下一步</a>--%>
                  <input type="button"  class="btn btn-primary" value="下一步"onclick="submitForm();">
              </span>
			</p>
          </form>
        </div>
        <!--右侧内容部分结束-->
</body>
</html>
