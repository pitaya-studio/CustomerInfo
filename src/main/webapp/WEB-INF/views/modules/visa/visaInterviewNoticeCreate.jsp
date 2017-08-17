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
var orderId="${param.orderId}";
$(function(){
	
	 $(document).on('click', '#vendorOperatorShow > em', function () {
    	 
    	 var $newOne = $(this).parent().parent().parent();
         $newOne.find('input').val('');
     })
	
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
					$("#areaOperatorShow").append('<a value="'+$(this).val()+'">{0}</a>'.replace("{0}",data));
					$("#areaOperatorShow").parent().find('input').val("请选择");
				}
			}
		}
	});
	

    //新增询价多选计调和线路国家
	inquiryCheckBOX();
	

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
	
	var curArea; 
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
		     var country=$("#country").val();
        if(country=="0"){
        	jBox.tip("请选择国家！");
        	return;
        } 
        var address=$("#address").val();
		var areaIds="";
		
		var areaNames =$("#area").val();
		$("#areaOperatorShow").find("a").each(function(){
			areaIds+=$(this).attr("value")+",";
			areaNames+=$(this).html()+",";
		}); 
		/* if(areaIds==""){
			jBox.tip("请选择领区！");
			return;
		} */ 
		
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
			orderId:orderId,
		    country:$("#country").val(),
		//	area:curArea.area,
		    areaIds:$("#areaIds").val(),
		    areaNames:areaNames,
		    address:$("#address").val(),
			interviewTime:interviewTime.val(),
			explainationTime:explainationTime.val(),
			contactMan:contactMan.val(),
			contactWay:contactWay.val()
		};
		var travelers="";
		$("#vendorOperatorShow").find("a").each(function(){
				travelers+="&"+$(this).attr("value")+":"+$(this).html();
		});
		travelers=travelers.substring(1);
		data.travelers=travelers;
		$.ajax({
			type:"post",
			url:"${ctx}/visa/interviewNotice/doCreate",
			dataType:"json",
			data:data,
			success:function(response){
				window.location.href="${ctx}/visa/interviewNotice/list?orderId="+orderId;
				return false;
			}
		})
	});
	var countryId="";
	$("#country").next().find(".custom-combobox-input").blur(function(){
		if(countryId != $("#country").val()) {
			countryId = $("#country").val();
			getArea($("#country").val());
		}
	}); 
})



function changeTime(obj){
	var value = $(obj).val();
	if(value==""){
		return;
	}
	value = value.split(" ")[0];
	var explainationTimeVal = $("#explainationTime").val();
	$("#explainationTime").val(getYestoday(new Date(value)));
}

function getYestoday(date){       
    var yesterday_milliseconds=date.getTime()-1000*60*60*24;        
    var yesterday = new Date();        
        yesterday.setTime(yesterday_milliseconds);        
         
    var strYear = yesterday.getFullYear();     
    var strDay = yesterday.getDate();     
    var strMonth = yesterday.getMonth()+1;   
    if(strMonth<10)     
    {     
        strMonth="0"+strMonth;     
    }     
    datastr = strYear+"-"+strMonth+"-"+strDay+" 00:00";   
    return datastr;   
  }   
  

	 //国家、领区联动
	 function getArea(countryId) {
		if(countryId != ''&&countryId != null) {
			$.ajax({
				type:"POST",
				url:"${ctx}/visa/visaProducts/getArea?countryId="+countryId,
				async : false,
				success:function(result){
					$("#area").empty().append('<option value="0" selected="selected">请选择</option>');
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
									if($(this).val()=="0"){
										return
									}
									$("#areaOperatorShow").append('<a value="'+$(this).val()+'">{0}</a>'.replace("{0}",data));
									$("#areaOperatorShow").parent().find('input').val("请选择");
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
		$("#areaOperatorShow").empty();
	} 

</script>
</head>
<body>
	<div class="ydbz_tit">
		新建面签通知
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
			<span class="seach_checkbox" id="vendorOperatorShow"></span>
		</p>
	</div>
	<!-- 需求198-版本0419-将国家.领区由下拉形式改为文本形式-djw--start -->
	<%-- <div class="seach25 seach100">
		<p>选择国家：</p>
		<p class="seach_r">
			<span class="seach_check">
				<select id="country" name="country">
					<option value="0" selected="selected" >可编辑可选择</option>
					<c:forEach items="${countryInfoList}" var="visaCountry">
               			<option value="${visaCountry[0]}"><c:out value="${visaCountry[1]}" /></option>
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
					 	<option value="${area['area'] }">${fns:getDictLabel(area['area'],'from_area','')}</option>
					</c:forEach>-->
				</select>
			</span>
			<span class="seach_checkbox" id="areaOperatorShow"></span>
		</p>
	</div> --%>
	<div class="seach25 seach100">
		<p>签证国家：</p><span>${country.countryName_cn }</span>
	</div>
	<div class="seach25 seach100">
		<p>领区：</p><span>${fns:getDictLabel(visaProducts.collarZoning,'from_area','')}</span>
	<div class="seach25 seach100">
		<input type="hidden" id="country" name="country" value="${visaProducts.sysCountryId }"></input>
	</div>
	<div class="seach25 seach100">
		<input type="hidden" id="area" value="${fns:getDictLabel(visaProducts.collarZoning,'from_area','')}"></input>
	</div>
	<div>
		<input type="hidden" id="areaIds" value="${visaProducts.collarZoning}"></input>
	</div> 
	<!-- 需求198-版本0419-将国家.领区由下拉形式改为文本形式-djw--end -->
	<div class="seach25 seach100">
		<p>预约地点：</p><input type="text" id="address"  /></span>
	</div>
	<div class="seach25 seach100">
		<p>约签时间：</p>
		<p class="seach_r">
			<input type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"  onchange="changeTime(this)" readonly="readonly" class="dateinput" id="interviewTime" style="width:125px;">
		</p>
	</div>
	<div class="seach25 seach100">
		<p>说明会时间：</p>
		<p class="seach_r">
			<input type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" readonly="readonly" class="dateinput" id="explainationTime" style="width:125px;">
		</p>
	</div>
	<div class="seach25 seach100">
		<p>联系人：</p>
		<input id="contactMan" type="text" value="${contractPersion }"/>
	</div>
	<div class="seach25 seach100">
		<p>联系方式：</p>
		<input id="contactWay" type="text"/>
	</div>
	<div class="seach_btnbox">
		<input id="save" type="button" onclick="TwoToOne()" class="btn btn-primary" value="保存">
		<input type="button" onclick="window.location.href='${ctx}/visa/interviewNotice/list?orderId=${param.orderId}'" class="btn btn-primary" value="取消">
	</div>
</div>
 
</body>
</html>