<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html">
	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
<c:choose>
	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
		<title>订单-机票-宣传费申请</title>
	</c:when>
	<c:otherwise>
		<title>订单-机票-返佣申请</title>
	</c:otherwise>
</c:choose>   

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<!-- 静态资源 -->
<link rel="stylesheet" href="css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/review/rebates/airticket/airticketComdiscount.js"></script>
<style type="text/css">
     .rebateObj select {
         margin-top: 0px;
     }
	.supplyNameSpan,
	.supplyInfoSpan {
	    display: none;
	}
	.rabateInfo {
	    margin-left: 200px;
	}
	.rebateObj select {
	    margin-top: 10px;
	    width: 130px;
	    height: 28px;
	}
</style>
<script type="text/javascript">						
$(function(){
 	inputTips();
 	contextPath = '${ctx}';
 	initRebates("employee");
	loadCurrency();
	//返佣对象的选择 如果为供应商则带出供应商信息---C4750--start
    $('.supplyName').comboboxInquiry();
    $(document).on('change', '.rebateTarget', function () {
		emptyAndAppend(new Array('supplyInfo','accountType','accountName','accountNo'));
        if ($(this).find('option:selected').val() == '2') {
        	$("#supplyInfo").comboboxInquiry('reset');
        	getAjaxSelect('supplyInfo',this);
            $('.supplyNameSpan').show();
        }
        else {
            $('.supplyNameSpan').hide();
            $('.supplyInfoSpan').hide();
        }
    });
    $(document).on('comboboxinquiryselect', '.supplyName', function () {
		emptyAndAppend(new Array('accountType','accountName','accountNo'));
        var selectedSupply = $(this).find('option:selected').val();
        if (selectedSupply != '0') {
            $('.supplyInfoSpan').show();
        }
        else {
            $('.supplyInfoSpan').hide();
        }
    });
    $(document).on('change', '.accountType', function () {
		emptyAndAppend(new Array('accountName','accountNo'));
    	var accountType = $(this).find('option:selected').val();
        if (accountType != '') {
        	var obj = $("#supplyInfo");
        	getAjaxSelect('accountType',obj,accountType);
            $('.supplyNameSpan').show();
        }
    });
    $(document).on('change', '.accountName', function () {
		emptyAndAppend(new Array('accountNo'));
    	var accountName = $(this).find('option:selected').val();
        if (accountType != '') {
        	var obj = $("#supplyInfo");
        	var accountType = $("#accountType").val();
        	getAjaxSelect('accountNo',obj,accountType,accountName);
            $('.supplyNameSpan').show();
        }
    });
    //返佣对象的选择 如果为供应商则带出供应商信息---C4750--end
});
//覆盖common.js的方法
//当输入框输入的为空的时候，不默认替换为0.00
function validNumFinally(dom){
	var thisvalue = $(dom).val();
	var minusSign = false;
	if(thisvalue){
		if(/^\-/.test(thisvalue)){
			minusSign = true;
			thisvalue = thisvalue.substring(1);
		}
		thisvalue = (thisvalue.replace(/^(\d*)$/,"$1.") + "00").replace(/(\d*\.\d\d)\d*/,"$1").replace(/^\./,"0.");
		if(minusSign){
			thisvalue = '-' + thisvalue;
		}
		$(dom).val(thisvalue);
	}else{
		$(dom).val("");
	}
}
//级联查询
function getAjaxSelect(type,obj,accountType,accountName){
	$.ajax({
		type: "POST",
	   	url: "${ctx}/order/newAirticketRebate/ajaxCheck",
	   	async: false,
	   	data: {
				"type":type,
				"val":$(obj).val(),
				"accountType":accountType,
				"accountName":accountName
			  },
		dataType: "json",
	   	success: function(data){
	   		if(data){
	   			if(type=="supplyInfo"){ 
		   			$.each(data,function(i,n){
		   			     $("#supplyInfo").append($("<option/>").text(n.name).attr("value",n.id));
		   			});
	   			}else if(type=="accountType"){
	 	   			$.each(data,function(i,n){
		   			     $("#accountName").append($("<option/>").text(n.name).attr("value",n.name));
		   			});
	   		    }else{
	   		    	$.each(data,function(i,n){
	   		    		$("#accountNo").empty();
	   		    		$("#accountNo").append($("<option/>").text(n.no).attr("value",n.id));
		   			});
	   		    }
	   		}
	   	}
	});
 }
 //清空select选项信息
 function emptyAndAppend(targets){
	 for(var i=0;i<targets.length;i++){
		 var target = targets[i];
		 if(target != 'accountType'){
			 $("#"+target).empty();
			 $("#"+target).append("<option>请选择</option>");
		 }else{
			 $("#"+target).empty();
			 $("#"+target).append("<option>请选择</option>");
			 $("#"+target).append("<option value='1'>境内</option>");
			 $("#"+target).append("<option value='2'>境外</option>"); 
		 }
	}
 }
 </script>
</head>
<body>
	<!--右侧内容部分开始-->
    <%@ include file="/WEB-INF/views/review/rebates/airticket/airticketOrderForRebateInfo.jsp"%>
	<%@ include file="/WEB-INF/views/review/rebates/airticket/airticketRebateApplication.jsp"%>
	<!--右侧内容部分结束-->
</body>
</html>