<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单-签证-面签通知</title>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="${ctxStatic }/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic }/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor2.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/i18n/jquery-ui-i18n.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.date.format.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.file.filter.js" type="text/javascript"></script>
<script type="text/javascript">
var cache={};
$(function(){
	//搜索计调名
	$("select[name='vendorOperator']" ).comboboxInquiry({
		"afterInvalid":function(event,data){
			var Array_default = new Array("选约签人");
			if(-1 == $.inArray(data,Array_default)){
				var isIncluded = 0;
				$("#vendorOperatorShow a").each(function(index, element) {
					if(data == $(element).text()){
						isIncluded = 1;
						return;
					}
				});
				if(isIncluded){
					jBox.tip("您已选择");
				}else{
					cache[$(this).val()]=data;
					$("#vendorOperatorShow").append('<a value="'+$(this).val()+'">{0}</a>'.replace("{0}",data));
					$("#vendorOperatorShow").parent().find('input').val("选约签人");
				}
			}
		}
	});
	
	
	$("select[name='country']" ).comboboxInquiry({
		"afterInvalid":function(event,data){
			
		}
	});
	
	
	$("select[name='selectArea']" ).comboboxInquiry({
		"afterInvalid":function(event,data){
			var Array_default = new Array("选择领区");
			if(-1 == $.inArray(data,Array_default)){
				var isIncluded = 0;
				$("#areaOperatorShow a").each(function(index, element) {
					if(data == $(element).text()){
						isIncluded = 1;
						return;
					}
				});
				if(isIncluded){
					jBox.tip("您已选择");
				}else{
					cache[$(this).val()]=data;
					$("#areaOperatorShow").append('<a value="'+$(this).val()+'">{0}</a>'.replace("{0}",data));
				}
			}
		}
	});
	
	$(".seach_checkbox").on("click","em",function(){
		var val=$(this).parent().attr("value");
		$("#areaOperatorShow").parent().find('input').val("请选择");
		delete cache[val];
        $(this).parent().remove();
    })

    //新增询价多选计调和线路国家
	inquiryCheckBOX();
	
	
	
	
})
</script>
</head>
<body>
	<div class="ydbz_tit">
		面签通知管理
     </div>
	<div class="seach25 seach100">
		<p>选约签人：</p>
		<p class="seach_r">
			<span class="seach_check">
				<select id="traveler" name="vendorOperator">
					<option value="0" selected="selected">选约签人</option>
					<c:forEach items="${travelers }" var="item">
						<option value="${item[0] }">${item[1] }</option>
					</c:forEach>
				</select>
			</span>
			<span class="seach_checkbox" id="vendorOperatorShow">
				<c:forEach items="${myTravelers }" var="item">
					<a value="${item[0] }">${item[1] }</a>
					<script type="text/javascript">
					cache['${item[0] }']="${item[1] }";
					</script>
				</c:forEach>
			</span>
		</p>
	</div>
	<div class="seach25 seach100">
		<p>选择国家：</p>
		<p class="seach_r">
			<span class="seach_check">
			<select id="country" name="country">
			<option value="">选择国家</option>
			<c:forEach items="${countryInfoList }" var="item">
				<c:choose>
					<c:when test="${item[1] eq interview.country }">
						<option value="${item[0] }" selected="true">${item[1] }</option>
					</c:when>
					<c:otherwise>
						<option value="${item[0] }">${item[1] }</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			</select>
			</span>
		</p>	
	</div>
	<div class="seach25 seach100">
		<p>选择领区：</p>
		<p class="seach_r">
			<span class="seach_check">
				<select id="area" name="selectArea">
					<option value="0" selected="selected" >选择领区</option>
				 	<!-- <option value="${item['area'] }">${item['country'] }</option> //以前用这个，取到的是国家不是领区城市
					<c:forEach items="${areas }" var="area">  
					 	<option value="${area['area'] }" >${fns:getDictLabel(area['area'],'from_area','')}</option>
					</c:forEach>-->
	   			</select>
			  <span class="seach_checkbox" id="areaOperatorShow">
			    <c:forEach var="i" items="${arr}" varStatus="loop">
					<a value="${arrId[loop.count-1]}">${i }</a>
				</c:forEach>
			  </span>	
			</span>
		</p>
	</div>
	<div class="seach25 seach100">
		<p>预约地点：</p><input type="text" id="address"  value="${interview.address }"/></span>
	</div>
	<div class="seach25 seach100">
		<p>约签时间：</p>
		<p class="seach_r">
			<input type="text" style="width:125px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" readonly="readonly" class="dateinput" id="interviewTime" value="<fmt:formatDate value="${interview.interviewTime }" pattern="yyyy-MM-dd HH:mm"/>"/>
		</p>
	</div>
	<div class="seach25 seach100">
		<p>说明会时间：</p>
		<p class="seach_r">
			<input type="text" style="width:125px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" readonly="readonly" class="dateinput" id="explainationTime" value="<fmt:formatDate value="${interview.explainationTime }" pattern="yyyy-MM-dd HH:mm"/>"/>
		</p>
	</div>
	<div class="seach25 seach100">
		<p>联系人：</p>
		<input id="contactMan" type="text" value="${interview.contactMan }"/>
	</div>
	<div class="seach25 seach100">
		<p>联系方式：</p>
		<input id="contactWay" type="text" value="${interview.contactWay }"/>
	</div>
	<div class="seach_btnbox">
		<input id="save" type="button" onclick="TwoToOne()" class="btn btn-primary" value="保存">
		<input type="button" onclick="window.location.href='${ctx}/visa/interviewNotice/list?orderId=${param.orderId}'" class="btn btn-primary" value="取消">
	</div>
</div>
<script type="text/javascript">
var orderId="${param.orderId}";
var interviewId="${param.interviewId}";
$(function(){
	 initArea();
	var areas;
	$.ajax({
		url:"${ctx}/visa/interviewNotice/areaInfo",
		dataType:"json",
		async:false,
		data:{
			orderId:"${param.orderId}"
		},
		success:function(response){
			areas=response;
		}
	});
	
	   var curArea={
		country:'${interview.country}',
		area:'${interview.area}',
		address:'${interview.address}'
	}; 
	$("#area").change(function(){
		if($(this).val()==""){
			$("#address").val("");
			return;
		}
		if(!areas){
			return;
		}
		for(var i=0;i<areas.length;i++){
			if(areas[i].area==$(this).val()){
				curArea=areas[i];
				$("#address").val(areas[i].address);
				break;
			}
		}
	});
	
	$("#save").click(function(){
		var traveler=$("#vendorOperatorShow");
		if(traveler.html()==""){
			jBox.tip("请选择约签人！");
			return;
		}
		
		var country=$("#country");
		if(country.val()=="0"){
			jBox.tip("请选择国家！");
			return;
		}
		
		//var area=$("#area");
		//if(area.val()==""){
		//	jBox.tip("请选择领区！");
		//	return;
		//}
		
		var address=$("#address");
		var areaIds="";
		var   areaNames ="";
		$("#areaOperatorShow").find("a").each(function(){
			areaIds+=$(this).attr("value")+",";
			areaNames+=$(this).html()+",";
		});
		if(areaIds==""){
			jBox.tip("请选择领区！");
			return;
		}
		
		var interviewTime=$("#interviewTime");
		if(interviewTime.val()==""){
			jBox.tip("请选择约签时间！");
			return;
		}
		var explainationTime=$("#explainationTime");
		if(explainationTime.val()==""){
			jBox.tip("请选择说明会时间！");
			return;
		}
		var contactMan=$("#contactMan");
		if($.trim(contactMan.val())==""){
			jBox.tip("请填写联系人！");
			return;
		}
		var contactWay=$("#contactWay");
		if($.trim(contactWay.val())==""){
			jBox.tip("请填写联系方式！");
			return;
		}
		var data={
			interviewId:interviewId,
			country:country.val(),
			//area:area.val(),
		    areaIds:areaIds,
		    areaNames:areaNames,
			address:address.val(),
			interviewTime:interviewTime.val(),
			explainationTime:explainationTime.val(),
			contactMan:contactMan.val(),
			contactWay:contactWay.val(),	
		};
		var travelers="";
		//for(var p in cache){
		//	travelers+="&"+p+":"+cache[p];
		//}
		$("#vendorOperatorShow").find("a").each(function(){
				travelers+="&"+$(this).attr("value")+":"+$(this).html();
		});
		travelers=travelers.substring(1);
		data.travelers=travelers;
		$.ajax({
			type:"post",
			url:"${ctx}/visa/interviewNotice/doUpdate",
			dataType:"json",
			data:data,
			success:function(response){
				window.location.href="${ctx}/visa/interviewNotice/list?orderId="+orderId;
				return false;
			}
		});
		
	})
	
	var countryId="";
	$("#country").next().find(".custom-combobox-input").blur(function(){
		if(countryId != $("#country").val()) {
			countryId = $("#country").val();
			getArea($("#country").val());
		}
	});
})

function initArea(){
	countryId = $("#country").val();
	if(countryId != ''&&countryId != null) {
		$.ajax({
			type:"POST",
			url:"${ctx}/visa/visaProducts/getArea?countryId="+countryId,
			async : false,
			success:function(result){
				$("#area").empty().append('<option value="0">请选择</option>');
				if(result != null && result.length != 0) {
					for(var index = 0; index < result.length; index++) {
						$("#area").append('<option value=\'' + result[index][0] + '\'>' + result[index][1] + '</option>');
					}
				}
				$("#area" ).comboboxInquiry({
					"afterInvalid":function(event,data){
						var Array_default = new Array("选择领区");
						if(-1 == $.inArray(data,Array_default)){
							var isIncluded = 0;
							$("#areaOperatorShow a").each(function(index, element) {
								if(data == $(element).text()){
									isIncluded = 1;
									return;
								}
							});
							if(isIncluded){
								jBox.tip("您已选择");
							}else{
								$("#areaOperatorShow").append('<a value="'+$(this).val()+'">{0}</a>'.replace("{0}",data));
							}
						}
					}
				});
				$("#area").next().find(".custom-combobox-input").val("请选择");
			}
		});
	}else {
		$("#area").empty().append('<option value="0">请选择</option>');
		$("#area").next().find(".custom-combobox-input").val("请选择");
	}
}

//国家、领区联动
	function getArea(countryId) {
		if(countryId != ''&&countryId != null) {
			$.ajax({
				type:"POST",
				url:"${ctx}/visa/visaProducts/getArea?countryId="+countryId,
				async : false,
				success:function(result){
					$("#area").empty().append('<option value="0">请选择</option>');
					if(result != null && result.length != 0) {
						for(var index = 0; index < result.length; index++) {
							$("#area").append('<option value=\'' + result[index][0] + '\'>' + result[index][1] + '</option>');
						}
					}
					$("#area" ).comboboxInquiry({
						"afterInvalid":function(event,data){
							var Array_default = new Array("选择领区");
							if(-1 == $.inArray(data,Array_default)){
								var isIncluded = 0;
								$("#areaOperatorShow a").each(function(index, element) {
									if(data == $(element).text()){
										isIncluded = 1;
										return;
									}
								});
								if(isIncluded){
									$.jBox.tip("您已选择");
								}else{
									if($(this).val()=="0"){
										return;
									}
									$("#areaOperatorShow").append('<a value="'+$(this).val()+'">{0}</a>'.replace("{0}",data));
								}
							}
						}
					});
					$("#area").next().find(".custom-combobox-input").val("请选择");
				}
			});
		}else {
			$("#area").empty().append('<option value=" ">请选择</option>');
			$("#area").next().find(".custom-combobox-input").val("请选择");
		}
		$("#areaOperatorShow").empty();
	}

</script>
</body>
</html>