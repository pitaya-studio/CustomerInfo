<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基础信息维护-交通信息-新增航空公司</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	// 初始设置
	changeArea();
	showViewShipInfo($("#showAirlineInfo"));
	
	if($("#type").val() == 'modify'){
		$("#countryId").val($("#hiddenCountry").val());
	}
});

$(function(){
	//新增航空公司
	$(".airCompany").on("click","[data='addCompany']",function(){
		var $this = $(this);
		$this.parent().before($("#TemplateCompany").html());
		$this.parent().prev().find("[data='companyOrder']").text($(".companyInfo:visible").length);
	}).on("click","[data='deleteCompany']",function(){//删除航空公司
		var $this = $(this);
		$this.parents(".companyInfo").remove();
		$("[data='companyOrder']").each(function(index, element) {
			if($(element).is(":visible")){
            	$(element).text(index+1);
			}
        });
	}).on("click","[data='addShipGrade']",function(){//新增舱位等级
		var $this = $(this);
		$this.parent().before($("#TemplateShip").html());
		$this.parent().prev().prev().find("[data='shipGradeOrder']").text($this.parents(".airInfo").find(".seach25").length);
	}).on("click","[data='deleteShipGrade']",function(){//删除舱位等级
		var $airInfo = $(this).parents(".airInfo");
		var $thisDiv = $(this).parents(".seach25");
		var $line = $thisDiv.next();
		$thisDiv.remove();
		$line.remove();
		$airInfo.find(".seach25").each(function(index, element) {
            $(element).find("[data='shipGradeOrder']").text(index+1);
        });
		
	}).on("click","[data='addShip']",function(){//新增舱位
		var $container = $(this).parents(".seach25").find(".listShip");
		var html = '<li><font color="red">*&nbsp;</font>舱位<span>' + ($container.find("li").length+1) +'</span>：<input type="text" id="shipGrade" name="shipGrade" maxlength="2">&nbsp;<strong>×</strong></li>';
		$container.append(html);
	}).on("click",".listShip li strong",function(){//删除舱位
		var $container = $(this).parents(".listShip");
		$(this).parent("li").remove();
		//重新排序
		$container.find("li").each(function(index, element) {
            $(element).find("span").text(index+1);
        });
	});
	
	
});

//展开补充舱位相关信息
function showViewShipInfo(dom){
	var $this = $(dom);
	var $airInfo = $this.parents(".seach25").next(".airInfo");
	if(!$airInfo.is(":visible")){
		$airInfo.slideDown();
		$this.siblings(".airInfo-arrow").show();
		$this.text($this.text().replace("展开","收起"));
	}
}

//展开补充舱位相关信息
function showShipInfo(dom){
	var $this = $(dom);
	var $airInfo = $this.parents(".seach25").next(".airInfo");
	if($airInfo.is(":visible")){
		$airInfo.slideUp();
		$this.siblings(".airInfo-arrow").hide();
		$this.text($this.text().replace("收起","展开"));
	}else{
		$airInfo.slideDown();
		$this.siblings(".airInfo-arrow").show();
		$this.text($this.text().replace("展开","收起"));
	}
}

function saveForm(){
	// 国家
	var country = $("#countryId").val();
	if(country == ""){
		$.jBox.tip('国家不能为空', 'error', { focusId: 'countryId' });
		return false;
	}
	
	// 航空公司名称
	var airlineName = $("#airlineName").val();
	if(airlineName == ""){
		$.jBox.tip('航空公司名称不能为空', 'error', { focusId: 'airlineName' });
		return false;
	}
	if(!isValidContent_cn_en_num(airlineName)){
		$.jBox.tip('航空公司名称只能输入中文、英文和数字', 'error', { focusId: 'airlineName' });
		return false;
	}
	
	var checkSameFlg = false;
	var jsonRes = {
			area : $("input[name='airlineArea']:checked").val(),
			countryId : $("#countryId").val(),
			airlineName : airlineName, 
			};
	/*
	$.ajax({
		type: "POST",
		url: "${ctx}/sys/airline/checkSameAirlineName",
		cache:false,
		dataType:"json",
		async:false,
		data:jsonRes,
		success: function(data){
			if(data.flag == "error"){
				top.$.jBox.tip('航空公司名称重名', 'error');
				checkSameFlg = true;
				return false;
			}
		},
		error : function(e){
			top.$.jBox.tip('请求失败。','error' ,{ focusId: 'currencyName' });
			return false;
		}
	 });
	
	if(checkSameFlg){
		return false;
	}
	*/
	
	// 航空公司二字码
	var airlineCode = $("#airlineCode").val();
	if(airlineCode == ""){
		$.jBox.tip('航空公司二字码不能为空', 'error', { focusId: 'airlineCode' });
		return false;
	}
	
	checkSameFlg = false;
	var jsonRes = {
			area : $("input[name='airlineArea']:checked").val(),
			countryId : $("#countryId").val(),
			airlineName : airlineName,
			airlineCode : airlineCode, 
			};
	$.ajax({
		type: "POST",
		url: "${ctx}/sys/airline/checkSameAirlineCode",
		cache:false,
		dataType:"json",
		async:false,
		data:jsonRes,
		success: function(data){
			if(data.flag == "error"){
				top.$.jBox.tip('航空公司二字码不能重复', 'error');
				checkSameFlg = true;
				return false;
			}
		},
		error : function(e){
			top.$.jBox.tip('请求失败。','error' ,{ focusId: 'currencyName' });
			return false;
		}
	 });
	
	if(checkSameFlg){
		return false;
	}
	
	var $airInfo = $(".airInfo");
	
	var errorFlg = false;
	$airInfo.find(".seach_r").each(function(index, element) {
		$(element).find("#shipName").each(function(index, element) {
			var shipName = $(element).val();
			if(shipName == ""){
				$.jBox.tip('舱位等级不能为空', 'error');
				errorFlg = true;
				return false;
			}
		});
	});
	if(errorFlg){
		return false;
	}
	
	errorFlg = false;
	$airInfo.find(".seach_r").each(function(index, element) {
		$(element).find("#shipName").each(function(index, element) {
			var shipName = $.trim($(element).val());
			if(!isValidContent_cn(shipName)){
				$.jBox.tip('舱位等级只能输入中文', 'error');
				errorFlg = true;
				return false;
			}
		});
	});
	if(errorFlg){
		return false;
	}
	
	errorFlg = false;
	$airInfo.find(".listShip").each(function(index, element) {
        $(element).find("input").each(function(index, element) {
        	var shipGrade = $(element).val();
        	if(shipGrade == ""){
				$.jBox.tip('舱位不能为空', 'error');
				errorFlg = true;
				return false;
			}
        });
    });
	if(errorFlg){
		return false;
	}
	
	errorFlg = false;
	$airInfo.find(".listShip").each(function(index, element) {
        $(element).find("input").each(function(index, element) {
        	var shipGrade = $(element).val();
        	if(!isValidContent_en(shipGrade)){
				$.jBox.tip('舱位只能输入英文', 'error');
				errorFlg = true;
				return false;
			}
        });
    });
	if(errorFlg){
		return false;
	}
	
	if(checkSpaceAndSpaceLevel()){
		return false;
	}
	
	if(checkSameSpace()){
		return false;
	}
	
	var shipGradeNum = "";
	$airInfo.find(".listShip").each(function(index, element) {
        shipGradeNum += ($(element).find("span").length + ",");
    });
	
	shipGradeNum = shipGradeNum.substring(0,shipGradeNum.length-1);
	$("#shipGradeNum").attr("value",shipGradeNum);
	
	
	$("#inputForm").attr("action","${ctx}/sys/airline/saveForm");
	$("#inputForm").submit();
}

function checkSameSpace(){
	var $airInfo = $(".airInfo");
	var errorFlg = false;
	$airInfo.find(".seach25").each(function(index, element) {
		var spaceLevelNo = $(element).find("[data='shipGradeOrder']").text();
		var spaceObjs = $(element).find(".listShip").find("input");
		if(checkArraySameData(spaceObjs)){
			$.jBox.tip('舱位等级' + spaceLevelNo + "下存在相同的舱位信息", 'error');
			errorFlg = true;
			return;
		}
	});

	return errorFlg;
}

function checkSpaceAndSpaceLevel(){
	var $airInfo = $(".airInfo");
	var errorFlg = false;
	$airInfo.find(".seach25").each(function(index, element) {
		var spaceLevelNo = $(element).find("[data='shipGradeOrder']").text();
		var spaceNum = $(element).find(".listShip").find("li").length;
		if(spaceNum == 0){
			$.jBox.tip('舱位等级' + spaceLevelNo + "下须有相应的舱位信息", 'error');
			errorFlg = true;
			return;
		}
	});

	return errorFlg;
}

// ======================================Form 处理 START=======================================
function changeArea(){
	// 初始
	var countryObj = document.getElementById("countryId");
	countryObj.options.length = 0;
	
	var area = $("input[name='airlineArea']:checked").val();
    if ("1" == area) {
    	$.ajax({
    		type: "POST",
    		url: "${ctx}/sys/airline/getCountryIdByName",
    		cache:false,
    		dataType:"json",
    		async:false,
    		data:{countryName : "中国"},
    		success: function(result){
    			if(result.flag == "success"){
    				var countryId = result.countryId;
    				countryObj.options.add(new Option("",""));
    				countryObj.options.add(new Option("中国",countryId));
    			}
    		},
    		error : function(e){
    			top.$.jBox.info("请求失败。", "错误");
    			return false;
    		}
    	 });
    } else if("2" == area){
    	$.ajax({
    		type: "GET",
    		url: "${ctx}/sys/airline/getCountryList",
    		cache:false,
    		dataType:"json",
    		async:false,
    		data:{airportArea : "2"},
    		success: function(result){
    			$(result).each(function(i,obj){
    				var opt = document.createElement("option");    
    				opt.value = obj.id;    
    				opt.text = obj.label; 
    				countryObj.appendChild(opt);
                });
    		},
    		error : function(e){
    			top.$.jBox.info("请求失败。", "错误");
    			return false;
    		}
    	 });
    }
}

function forwardList(){
	var area = $("input[name='airlineArea']:checked").val();
	window.location.href = "${ctx}/sys/airline/list/" + area;
}

/**
 * 判定输入项为汉字
 */
function isValidContent_cn(strName){
    var str = strName;   
	  var reg = /[^\u4e00-\u9fa5]/;  
    if(reg.test(str)){
     return false;
    }
    return true;
}

function isValidContent_cn_en_num(str){
	var reg = /[^\u4e00-\u9fa5\w\d]/;
	if(reg.test(str)){
		return false;
	}
	return true;
}

function isValidContent_en(str){
	var reg = /^[A-Za-z]+$/;
	if(!reg.test(str)){
		return false;
	}
	return true;
}


/**
 * 判定数组中是否存在重复数据
 */
function checkArraySameData(inputArr){
	for(var i = 0;i < inputArr.length;i++){
		for(var j = i + 1;j < inputArr.length;j++){
			if($(inputArr[i]).val() == $(inputArr[j]).val()){
				return true;
			}
		}
	}

	return false;
}

//确认对话框
function airConfirm(mess){
	
	top.$('.jbox-body .jbox-icon').css('top','55px');
	return false;
}

function modArea(link){
	var area = $("input[name='airlineArea']");
	if(area.attr("disabled") == 'disabled'){
		top.$.jBox.confirm('您确认修改吗？','系统提示',function(v,h,f){
			if(v=='ok'){
				$(area[0]).removeAttr("disabled");
				$(area[1]).removeAttr("disabled");
				$(link).text("取消修改");
			}
		},{buttonsFocus:1});
	} else {
		$(area[0]).attr("disabled","disabled");
		$(area[1]).attr("disabled","disabled");
		$(link).text("修改");
	}
	
	return false;
}

function modCountry(link){
	var country = $("#countryId");
	if(country.attr("disabled") == 'disabled'){
		top.$.jBox.confirm('您确认修改吗？','系统提示',function(v,h,f){
			if(v=='ok'){
				country.removeAttr("disabled");
				$(link).text("取消修改");
			}
		},{buttonsFocus:1});
	} else {
		country.attr("disabled","disabled");
		$(link).text("修改");
	}
}

function modAirlineName(link){
	var airlineName = $("#airlineName");
	if(airlineName.attr("readonly") == 'readonly'){
		top.$.jBox.confirm('您确认修改吗？','系统提示',function(v,h,f){
			if(v=='ok'){
				airlineName.removeAttr("readonly");
				$(link).text("取消修改");
			}
		},{buttonsFocus:1});
	} else {
		airlineName.attr("readonly","readonly");
		$(link).text("修改");
	}
}

function modAirlineCode(link){
	var airlineCode = $("#airlineCode");
	if(airlineCode.attr("readonly") == 'readonly'){
		top.$.jBox.confirm('您确认修改吗？','系统提示',function(v,h,f){
			if(v=='ok'){
				airlineCode.removeAttr("readonly");
				$(link).text("取消修改");
			}
		},{buttonsFocus:1});
	} else {
		airlineCode.attr("readonly","readonly");
		$(link).text("修改");
	}
}

function modSpaceLevel(link){
	var shipName = $("#shipName");
	if(shipName.attr("readonly") == 'readonly'){
		top.$.jBox.confirm('您确认修改吗？','系统提示',function(v,h,f){
			if(v=='ok'){
				shipName.removeAttr("readonly");
				$(link).text("取消修改");
			}
		},{buttonsFocus:1});
	} else {
		shipName.attr("readonly","readonly");
		$(link).text("修改");
	}
}

function modSpace(link){
	var shipGrade = $("#shipGrade");
	if(shipGrade.attr("readonly") == 'readonly'){
		top.$.jBox.confirm('您确认修改吗？','系统提示',function(v,h,f){
			if(v=='ok'){
				shipGrade.removeAttr("readonly");
				$(link).text("取消修改");
			}
		},{buttonsFocus:1});
	} else {
		shipGrade.attr("readonly","readonly");
		$(link).text("修改");
	}
}
//======================================Form 处理 END=======================================
</script>
</head>
<body>
<!--右侧内容部分开始-->
<div class="ydbz_tit">新增航空公司</div>
<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
<input id="type" name="type" type="hidden" value="${type}"/>
<input id="id" name="id" type="hidden" value="${id}"/>
<input id="hiddenCountry" name="hiddenCountry" type="hidden" value="${airlineInfo.countryId}"/>
<input type="hidden" id="shipGradeNum" name="shipGradeNum"/>
<div class="airCompany">
    <div class="seach25 seach100">
    <c:choose>
    <c:when test="${area eq '1'}">
        <p class="seach_check"><label for="inquiry_radio_flights1"><input name="airlineArea" id="airlineArea" type="radio" value="1" disabled="disabled" checked="checked" onclick="changeArea()" />国内航空公司</label></p>
        <p class="seach_check"><label for="inquiry_radio_flights2"><input name="airlineArea" id="airlineArea" type="radio" value="2" disabled="disabled" onclick="changeArea()" />国外航空公司</label></p>
    </c:when>
    <c:when test="${area eq '2'}">
        <p class="seach_check"><label for="inquiry_radio_flights1"><input name="airlineArea" id="airlineArea" type="radio" value="1" disabled="disabled" onclick="changeArea()" />国内航空公司</label></p>
        <p class="seach_check"><label for="inquiry_radio_flights2"><input name="airlineArea" id="airlineArea" type="radio" value="2" disabled="disabled" checked="checked" onclick="changeArea()" />国外航空公司</label></p>
    </c:when>
    </c:choose>
    <a href="javascript:void(0)" onclick="return modArea(this)">修改</a>
    </div>
    <div class="kongr"></div>
    <div class="inquiry_flights1">
        <div class="seach25 seach100">
            <p><font color="red">*&nbsp;</font>国家：</p>
            <p class="seach_r">
                <select id="countryId" name="countryId" disabled="disabled">
                </select>
                &nbsp;&nbsp;<a href="javascript:void(0)" onclick="return modCountry(this)">修改</a>
            </p>
        </div>
        <div class="kongr20"></div>
        <!--航空公司信息开始-->
        <div class="companyInfo" style="border-bottom:none;">
            <div class="seach25 seach100">
                <p><font color="red">*&nbsp;</font>航空公司<span data="companyOrder">1</span>：</p><input type="text" class="airCompanyName" id="airlineName" name="airlineName" maxlength="200" value="${airlineInfo.airlineName}" readonly="readonly"/>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="return modAirlineName(this)">修改</a>&#12288;&#12288;
                <span><font color="red">*&nbsp;</font>二字码：</span><input type="text" id="airlineCode" name="airlineCode" maxlength="2" value="${airlineInfo.airlineCode}" readonly="readonly"/>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="return modAirlineCode(this)">修改</a>
                <span class="linkAir"><span id="showAirlineInfo" onclick="showShipInfo(this)" class="linkAir-spn"><c:if test="${type == 'new'}">展开补充舱位相关信息</c:if></span><div class="airInfo-arrow"><i></i></div></span>
            </div>
            <div class="airInfo" style="display:none;">
                <div class="seach25 seach100">
                  <p><font color="red">*&nbsp;</font>舱位等级<span data="shipGradeOrder" id="spaceLevel">1</span>：</p>
                  <p class="seach_r"><input type="text" id="shipName" name="shipName" maxlength="200"  value="${airlineInfo.spaceLevel}" readonly="readonly"/>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="return modSpaceLevel(this)">修改</a><c:if test="${type == 'new'}"><span class="ml20 linkAir-spn" data="addShip">+新增舱位</span></c:if></p>
                  <div class="kong"></div>
                  <ul class="listShip clearfix">
                      <li><font color="red">*&nbsp;</font>舱位<span id="space"><c:if test="${type == 'new'}">1</c:if></span>：<input type="text" id="shipGrade" name="shipGrade" maxlength="2"  value="${airlineInfo.space}" readonly="readonly"/>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="return modSpace(this)">修改</a>&nbsp;<c:if test="${type == 'new'}"><strong>&times;</strong></c:if></li>
                      <c:if test="${type == 'new'}">
                      <li><font color="red">*&nbsp;</font>舱位<span id="space">2</span>：<input type="text" id="shipGrade" name="shipGrade" maxlength="2"/>&nbsp;<strong>&times;</strong></li>
                      </c:if>
                  </ul>
                </div>
                <div class="airInfo-tit1"></div>
                <c:if test="${type == 'new'}">
                <div class="clearfix"><span class="linkAir-spn" data="addShipGrade">+新增舱位等级</span></div>
                </c:if>
            </div>
        </div>
        <!--航空公司信息结束-->
    </div>
    <br><br>
    <div class="ydBtn ydBtn2" style="width:600px">
    <tags:message content="${message}"/>
       <a class="ydbz_s" onclick="saveForm()">保存</a>&nbsp;&nbsp;
       <a class="ydbz_s" onclick="forwardList();">返回</a>
    </div>
</div>
<!-- </form> -->
</form:form>

<!--舱位等级模板开始-->
<div id="TemplateShip" style="display:none;">
    <div class="seach25 seach100">
      <p><font color="red">*&nbsp;</font>舱位等级<span data="shipGradeOrder">1</span>：</p>
      <p class="seach_r"><input type="text" id="shipName" name="shipName" maxlength="200"/><span class="ml20 linkAir-spn" data="addShip">+新增舱位</span><span class="ml20 ydbz_s gray" data="deleteShipGrade">删除舱位等级</span></p>
      <div class="kong"></div>
      <ul class="listShip clearfix"></ul>
    </div>
    <div class="airInfo-tit1"></div>
</div>
<!--舱位等级模板结束-->
<!--右侧内容部分结束-->
<script type="text/javascript">
</script>
</body>
</html>
