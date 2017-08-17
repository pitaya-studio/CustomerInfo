<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基础信息维护-交通信息-机场信息</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<style>
a.newBtn{background:#5F7795;color:#fff;}
a.newBtn:hover {background:#28B2E6;}
.jbox-width100{float:left}
</style>
</head>
<body>
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">airport</page:param>
</page:applyDecorator>
<div class="activitylist_bodyer_right_team_co_bgcolor">
<form:form id="selForm" action="${ctx}/sys/airport/saveDispStatus" method="post" >
	<input id="area" name="area" type="hidden" value="${area}"/>
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<input id="airportIds" type="hidden" name="airportIds" value="${airportIds}"/>
<!--右侧内容部分开始-->
	<!--查询结果筛选条件排序开始-->

	<%--<div class="filterbox">--%>
    <div class="activitylist_bodyer_right_team_co">
        <%--modified for UG_V2 改变新增按钮位置 by tlw at 20170302 start--%>
        <p class="main-right-topbutt">
            <a class="primary" href="javascript:void(0);" onclick="operAirport('new');">新增机场信息</a>
        </p>
        <%--modified for UG_V2 改变新增按钮位置 by tlw at 20170302 end--%>

		<%--<div class="filter_check">--%>
			<%--<span>信息筛选：</span>--%>
			<%--<label><input type="radio" name="filterTable" onclick="changeTab('1')" <c:if test="${area eq '1'}">checked="checked"</c:if> />国内</label>--%>
			<%--<label><input type="radio" name="filterTable" onclick="changeTab('2')" <c:if test="${area eq '2'}">checked="checked"</c:if> />国外</label>--%>
		<%--</div>--%>
	</div>
    <%--modified for UG_V2 替换页签样式与线上统一 更改查询结果位置 by tlw at 20170302 start--%>
    <div class="supplierLine">
        <a onclick="changeTab('1')" href="javascript:void(0)" id="groupLabel" <c:if test="${area eq '1'}">class="select"</c:if>>国内</a>
        <a onclick="changeTab('2')" href="javascript:void(0)" id="orderLabel" <c:if test="${area eq '2'}">class="select"</c:if>>国外</a>
        <div class="filter_num">查询结果<strong>${page.count}</strong>条</div>
    </div>
    <%--modified for UG_V2 替换页签样式与线上统一 更改查询结果位置 by tlw at 20170302 end--%>

    <!--查询结果筛选条件排序结束-->
<table class="activitylist_bodyer_table mainTable" id="contentTable">
	<thead>
		<tr>
			<th width="10%">序号</th>
			<th width="20%">国家</th>
			<th width="25%">城市</th>
			<th width="20%">机场名称</th>
			<th width="15%">机场三字码</th>
			<th width="10%">设置</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${page.list}" var="keyword" varStatus="s">
		<tr>
			<td class="tc">${s.count}</td>
			<td class="tc">${keyword[0]}</td>
			<td>${keyword[1]}</td>
			<td>${keyword[2]}</td>
			<td class="tc">${keyword[3]}</td>
			<td class="table_borderLeftN"><input type="checkbox" name="activityId" value="${keyword[5]}" <c:if test="${keyword[4] eq '1'}">checked</c:if> /></td>
		</tr>
	</c:forEach>
	</tbody>
</table>
<c:if test="${page.count eq 0 }">
	<div class="wtjqw">无机场信息</div>
</c:if>
<c:if test="${page.count ne 0 }">
		<div class="pagination">
			${page}
		</div>
</c:if>
<c:if test="${page.count ne 0 }">
<tags:message content="${message}"/>
<div class="ydBtn fixed_rb">
    <%--modified for UG_V2 按钮样式 by tlw at 20170302 start--%>
    <%--<a class="ydbz_s" onclick="saveDispStatus()">保存</a>--%>
    <input id="btn_search" class="btn btn-primary ydbz_x" type="button" onclick="saveDispStatus()" value="保存">
    <%--modified for UG_V2 按钮样式 by tlw at 20170302 end--%>
</div>
</c:if>
</form:form>
</div>
<!--右侧内容部分结束-->

<script type="text/javascript">

$(document).ready(function() {
    $("div[style]").each(function() {
        if ($(this).css("clear") == "both") {
        	$(this).removeAttr("style");
        }
    })
});

function checkAll(obj){
	if($(obj).attr("checked")){
		$("input[name='allChk']").attr("checked",'true');
		$("input[name='activityId']").attr("checked",'true');
	} else {
		$("input[name='allChk']").removeAttr("checked");
		$("input[name='activityId']").removeAttr("checked");
	}
}

function checkSub(){
	var subBox = $("input[name='activityId']");
	var checkedBoxs = $("input[name='activityId']:checked");
	if(subBox.length == checkedBoxs.length){
		$("input[name='allChk']").attr("checked", 'true');	
	} else {
		$("input[name='allChk']").removeAttr("checked");
	}
}

//基础信息维护-交通信息-机场信息-新增机场信息
function operAirport(type,airportId) {
	
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<dl style="overflow:hidden; padding-right:5px;">';
	html += '<dt style="height:30px; float:left; width:100%;">';
	html += '	<span class="fl jbox-span-radio"><input name="airportArea" type="radio" style="margin:0;" value="1" checked onclick="changeArea()"/> 国内机场</span>';
	html += '	<span class="fl jbox-span-radio"><input name="airportArea" style="margin:0;" type="radio" value="2" onclick="changeArea()"/> 国外机场</span>';
	html += '</dt>';
	html += '<dd class="jbox-margin0 fl">';
	html += '	<label class="jbox-label"><font color="red">*&nbsp;</font>国家：</label><select id="country" name="country" class="jbox-width100" onchange="changeCountry()"></select>';
	html += '	<label class="jbox-label"><font color="red">*&nbsp;</font>城市：</label><select id="city" name="city" class="jbox-width100"></select><br/>';
	html += '	<div class="jbox-margin0 fl">';
	html += '		<label class="jbox-label"><font color="red">*&nbsp;</font>机场：</label><input type="text" id="airportName" name="airportName" class="fl jbox-width100" maxlength="200">';
	html += '		<label class="jbox-label"><font color="red">*&nbsp;</font>三字码：</label><input type="text" id="airportCode" name="airportCode" class="fl jbox-width100" maxlength="3">';
	html += '		<div class="ydbz_s jbox-zj">增加</div>';
	html += '	</div>';
	html += '</dd>';
	html += '</dl>';
	html += '</div>';
	$.jBox(html, { title: "新增机场信息",buttons:{'取消': 0,'提交':1}, 
		loaded:function(h){
			if(type == 'new'){
				return;
			}

			var jsonRes = {airportId : airportId};
			$.ajax({
				type: "GET",
				url: "${ctx}/sys/airport/getAirportInfoById",
				cache:false,
				dataType:"json",
				async:false,
				data:jsonRes,
				success: function(data){
					var airportInfo = data.airportInfo;
					if(airportInfo.area == 1){
						$("input:radio[name='airportArea']").eq(0).attr("checked",'checked');
					} else {
						$("input:radio[name='airportArea']").eq(1).attr("checked",'checked');
					}
					
					changeArea();
					$("#country").val(airportInfo.countryId);
					changeCountry();
					$("#city").val(airportInfo.cityId);
					
					$("#airportName").val(airportInfo.airportName);
					$("#airportCode").val(airportInfo.airportCode);
					
					$(".ydbz_s.jbox-zj").hide();
				},
				error : function(e){
					top.$.jBox.tip('请求失败。','error:' + e);
					return false;
				}
			 });
		},
		
		submit:function(v, h, f){
		if (v=="1"){
			// 国家
			var country = $("#country").val();
			if(country == ""){
				top.$.jBox.tip('国家不能为空', 'error', { focusId: 'country' }); 
				return false;
			}
			
			// 城市
			var city = $("#city").val();
			if(city == ""){
				top.$.jBox.tip('城市不能为空', 'error', { focusId: 'city' }); 
				return false;
			}
			
			// 机场名称
			var airportNameObj = $("input[id='airportName']");
			for(var i = 0;i < airportNameObj.length; i++){
				if($.trim(airportNameObj[i].value) == ""){
					$(airportNameObj[i]).focus();
					top.$.jBox.tip('机场不能为空', 'error'); 
					return false;
				}
			}
			
			// 三字码数据取得
			var airportCodeObj = $("input[id='airportCode']");
			for(var i = 0;i < airportCodeObj.length; i++){
				var re=/^[a-zA-Z]+$/;
				if($.trim(airportCodeObj[i].value) == ""){
					$(airportCodeObj[i]).focus();
					top.$.jBox.tip('三字码不能为空', 'error'); 
					return false;
				} else if(!re.test($.trim(airportCodeObj[i].value))) {
					$(airportCodeObj[i]).focus();
					top.$.jBox.tip('三字码必须输入英文', 'error'); 
					return false;
				}
			}
			
			// 机场数据取得
			var airportNames = "";
			var airportNameObj = $("input[id='airportName']");
			if(!checkSameAirportInfo(airportNameObj)){
				top.$.jBox.tip('机场名称在本页面有重复', 'error');
				return false;
			}
			
			for(var i = 0;i < airportNameObj.length; i++){
				airportNames += (airportNameObj[i].value + "|");
			}
			airportNames = airportNames.substring(0,airportNames.length-1);
			
			// 三字码数据取得
			var airportCodes = "";
			var airportCodeObj = $("input[id='airportCode']");
			
			for(var i = 0;i < airportCodeObj.length; i++){
				airportCodes += (airportCodeObj[i].value + "|");
			}
			airportCodes = airportCodes.substring(0,airportCodes.length-1);
			
			var checkSameFlg = false;
			/*
			var jsonRes = {
					area : $("input[name='airportArea']:checked").val(),
					countryId : $("#country").val(),
					cityId : $("#city").val(),
					airportNames : airportNames 
					};

			$.ajax({
				type: "POST",
				url: "${ctx}/sys/airport/checkSameAirPortName",
				cache:false,
				dataType:"json",
				async:false,
				data:jsonRes,
				success: function(data){
					if(data.flag == "error"){
						top.$.jBox.tip('机场名称为' + data.sameName + '的数据与已保存完的数据有重复', 'error');
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
			// 三字码重复检查
			if(!checkSameAirportInfo(airportCodeObj)){
				top.$.jBox.tip('三字码在本页面有重复', 'error');
				return false;
			}
			
			if(airportId == null || "" == airportId){
				airportId = -1;
			}

			checkSameFlg = false;
			var jsonRes = {
					airportId : airportId,
					area : $("input[name='airportArea']:checked").val(),
					countryId : $("#country").val(),
					airportCodes : airportCodes 
					};
			$.ajax({
				type: "POST",
				url: "${ctx}/sys/airport/checkSameAirportCode",
				cache:false,
				dataType:"json",
				async:false,
				data:jsonRes,
				success: function(data){
					if(data.flag == "error"){
						top.$.jBox.tip('三字码' + data.sameName + '的数据与已保存完的数据有重复', 'error');
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
			
			var jsonRes = {
					airportId : airportId,
					area : $("input[name='airportArea']:checked").val(),
					countryId : $("#country").val(),
					cityId : $("#city").val(),
					airportNames : airportNames, 
					airportCodes : airportCodes
					};
			$.ajax({
				type: "POST",
				url: "${ctx}/sys/airport/save",
				cache:false,
				dataType:"json",
				async:false,
				data:jsonRes,
				success: function(array){
					window.location.href = "${ctx}/sys/airport/list/" + $("input[name='airportArea']:checked").val();
				},
				error : function(e){
					top.$.jBox.tip('请求失败。','error' ,{ focusId: 'currencyName' });
					return false;
				}
			 });

			return true;
		} else {
			return true;
		}
	},height:320,width:520});
	
	$('.jbox-zj').click(function(){				 
		$(this).parent().after('<div class="jbox-margin0 fl"><label class="jbox-label"><font color="red">*&nbsp;</font>机场：</label><input type="text" id="airportName" name="airportName" value="" class="fl jbox-width100" maxlength="200"><label class="jbox-label"><font color="red">*&nbsp;</font>三字码：</label><input type="text" id="airportCode" name="airportCode" value="" class="fl jbox-width100" maxlength="3"><div class="ydbz_s jbox-dle gray">删除</div></div>');					 
	});
	$('.jbox-dle').live('click',function(){
		$(this).parent().remove();									 
	});

	// 初始设置
	changeArea();
}

function checkSameAirportInfo(airportInfoObj){
	for(var i = 0;i < airportInfoObj.length; i++){
		for(var j = i + 1;j < airportInfoObj.length; j++){
			if(airportInfoObj[i].value == airportInfoObj[j].value){
				return false;
			}
		}
	}
	
	return true;
}

function changeTab(area){
	window.location.href = "${ctx}/sys/airport/list/" + area;
}

function changeArea(){
	// 初始
	var countryObj = document.getElementById("country");
	countryObj.options.length = 0;
	
	var cityObj = document.getElementById("city");
	cityObj.options.length = 0;
	
	var area = $("input[name='airportArea']:checked").val();
    if ("1" == area) {
    	$.ajax({
    		type: "POST",
    		url: "${ctx}/sys/airport/getCountryIdByName",
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
    		url: "${ctx}/sys/airport/getCountryList",
    		cache:false,
    		dataType:"json",
    		async:false,
    		data:{airportArea : "2"},
    		success: function(result){
    			$(result).each(function(i,obj){
    				//var opt = document.createElement("option");    
    				//opt.value = obj.id;    
    				//opt.text = obj.label; 
    				//countryObj.appendChild(opt);
    				countryObj.options.add(new Option(obj.label,obj.id));
                });
    		},
    		error : function(e){
    			top.$.jBox.info("请求失败。", "错误");
    			return false;
    		}
    	 });
    }
}

function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#selForm").attr("action","${ctx}/sys/airport/list/${area}");
	$("#selForm").submit();
}

function changeCountry(){
	
	var countryId = $("#country").val();
	if(countryId == ""){
		return;
	}
	
	$.ajax({
		type: "GET",
		url: "${ctx}/sys/airport/getCitysByCountryId",
		cache:false,
		dataType:"json",
		async:false,
		data:{countryId : countryId},
		success: function(result){
			var cityObj = document.getElementById("city");
			cityObj.options.length = 0;
			$(result).each(function(i,obj){
				//var opt = document.createElement ("option");    
				//opt.value = obj.id;    
				//opt.text = obj.label; 
				//cityObj.appendChild(opt);
				cityObj.options.add(new Option(obj.label,obj.id));
            });
		},
		error : function(e){
			top.$.jBox.info("请求失败。", "错误");
			return false;
		}
	 });
}

function saveDispStatus(){
	$("#selForm").attr("action","${ctx}/sys/airport/saveDispStatus");
	$("#selForm").submit();
}

function show(id, area){
	window.open("${ctx}/sys/airport/show/" + id +"?area=" + area );
}

</script>
</body>
</html>
