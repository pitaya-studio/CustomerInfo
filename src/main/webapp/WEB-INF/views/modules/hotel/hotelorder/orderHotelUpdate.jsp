<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<c:if test="${isTransfer }">
	<title>订单转报名</title>
</c:if>
<c:if test="${!isTransfer }">
	<title>订单修改</title>
</c:if>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/css/jquery.validate.min.css" />
<script type="text/javascript" src="${ctxStatic}/static/jquery-jbox/2.3/jquery-1.4.2.min.js"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<script	src="${ctxStatic}/modules/hotel/hotelorder/hotelOrderCommon.js"	type="text/javascript"></script>
<style>
	input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
	input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
		cursor:auto;
		background:transparent;
		border:0px;
		box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
	}
</style>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
 	var ctx = '${ctx}';
	var dataArrayStr2 = '${contactArray}';
	var dataStr = ${contactsJsonStr};
	var tempFirstContactName = '${agentinfo.agentContact}';
	var dataArrayStr = '${contactArray }';  //conta转换的jsonArray
	var dataArray = eval(dataArrayStr);
	var agentinfoId = ${agent.id };
	var address = '${address }';
	var firstAgentId = ${firstAgent.id };
    var allowModifyAgentInfo = ${allowModifyAgentInfo };
	var allowAddAgentInfo = ${allowAddAgentInfo };
	
	$(function(){
		//初始加载时，订单的联系人
        var orderContactsList = eval(${orderContactsListJsonArray });
        var orderContactsNum = orderContactsList.length;        
        //
	    $('[name="channelConcat"]').combox({datas:dataStr,
	// 	        回调函数,可在此函数中获取选择的联系人的uuid,但只对原始的联系人框有效,对于新增的联系人在第28行获取选中项信息
	        onSelect:function(obj){	        	
	        	//选择联系人填充其他信息
				for(var i=0; i<dataArray.length; i++){
					var tempData = dataArray[i];
					if(tempData.uuid == $(obj).find('a').attr('uuid')){
	            		fillContactInfo(obj, tempData);
		        	}
				}
	        }
	    });
	// 	    过滤特殊字符
	    $(document).on('keyup','[name="channelConcat"] input',function(){
	        $(this).val(replaceSpecialChars.apply($(this).val()));
	    });
	    $(document).on('afterpaste','[name="channelConcat"] input',function(){
	        $(this).val(replaceSpecialChars.apply($(this).val()));
	    });
	    
	    for(var i=1; i<orderContactsList.length; i++){
		    var js = {};
		    // 比如属性名为name
		    orderContactsList[i];
		    addAgentContactNew($("span[name=addContactButton]"));
		}		
		//初始化时，如果是签约渠道，为ordercontact联系人填充信息		
		if(agentinfoId != -1){
		    $("#signChannelList").find("ul[name=orderpersonMes]").each(function(index, element){
		    	initialContactInfo(element, orderContactsList[index]);
		    });
		}
	    //如果不是可输入修改，disabled下拉和除了“其他”之外的input
	    if(allowModifyAgentInfo == 0){
	    	$("#signChannelList").find("input[name=contactsName]").attr("disabled", true);
		    setContactInfoReadonly();
	    }
	    //如果不是可添加，隐藏掉添加联系人按钮
	    if(allowAddAgentInfo == 0){
	    	$("span[name=addContactButton]").hide();
	    }
	    //将渠道改为可输入的select
		$("#orderCompany").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
			getAllContactsByAgentId(getAgentId(obj.item, 1));
		});
});
    
/**
 * 初始化填充对应信息（对应可修改）
 */
function initialContactInfo(obj, tempData){
	if($(obj).attr("name") == "orderpersonMes"){
		$(obj).find("[name=contactsName]").val(tempData.contactsName);
		$(obj).find("[name=contactsTel]").val(tempData.contactsTel);
		$(obj).find("[name=contactsTixedTel]").val(tempData.contactsTixedTel);
		$(obj).find("[name=contactsFax]").val(tempData.contactsFax);
		$(obj).find("[name=contactsQQ]").val(tempData.contactsQQ);
		$(obj).find("[name=contactsEmail]").val(tempData.contactsEmail);
		if(tempData.contactsAddress=="null"){
			$(obj).find("[name=contactsAddress]").val("");
		}else{
			$(obj).find("[name=contactsAddress]").val(tempData.contactsAddress);
		}
		return;
	}
}

/**
 * 不可修改时，把联系人input置为只读（'其他'除外）,其实是在其后添加了一个span用于展示
 */    
function setContactInfoReadonly(){
	var $mesDom = $("#signChannelList").find("ul[name=orderpersonMes]");
	$mesDom.find("[name=contactsTel]").attr("disabled",true);
	$mesDom.find("[name=contactsTixedTel]").attr("disabled",true);
	$mesDom.find("[name=contactsFax]").attr("disabled",true);
	$mesDom.find("[name=contactsQQ]").attr("disabled",true);
	$mesDom.find("[name=contactsEmail]").attr("disabled",true);
	$mesDom.find("[name=contactsAddress]").attr("disabled",true);
	$mesDom.find("[name=contactsZipCode]").attr("disabled",true);
}
    
/**
 * 选择联系人，填充对应信息（对应可修改）
 */
function fillContactInfo(obj, tempData){
	if($(obj).attr("name") == "orderpersonMes"){
		$(obj).find("[name=contactsName]").val(tempData.text);
		$(obj).find("[name=contactsTel]").val(tempData.contactMobile);
		$(obj).find("[name=contactsTixedTel]").val(tempData.contactPhone);
		$(obj).find("[name=contactsFax]").val(tempData.contactFax);
		$(obj).find("[name=contactsQQ]").val(tempData.contactQQ);
		$(obj).find("[name=contactsEmail]").val(tempData.contactEmail);
		$(obj).find("[name=contactsAddress]").val(tempData.agentAddressFull);
		return;
	}
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsName]").val(tempData.text);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsTel]").val(tempData.contactMobile);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsTixedTel]").val(tempData.contactPhone);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsFax]").val(tempData.contactFax);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsQQ]").val(tempData.contactQQ);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsEmail]").val(tempData.contactEmail);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsAddress]").val(tempData.agentAddressFull);
}

/**
 * 获取agentId
 * param0 获取依据
 * param1 获取方法
 */
function getAgentId(param0, param1){
	//获取方法1
	if(param1 == 1){
		return $(param0).val().split(",")[0];
	} else {
		alert("渠道获取方法出错！");
	}
}

/**
 * 获取当前渠道的所有联系人信息
 */
function getAllContactsByAgentId(tempAgentId){
	contactNo = 1;
	$.ajax({
		type : "POST",
		url : ctx + "/orderCommon/manage/getAllAgentContactInfo",
		data : {agentId : tempAgentId},
		success : function(myData) {
			//删除其余联系人ul
			dataArray = eval(myData);
			address = dataArray[0].agentAddressFull;
			$("#signChannelList").find("ul[name=orderpersonMes]:not(:first)").remove();
		    //移除span的内容
		    var $span = $('span[name="channelConcat"]');
		    $span.after('<span name="channelConcat"></span>');
		    $span.remove();
		    //变更下拉列表内容
			$('span[name="channelConcat"]').combox({datas:eval(myData),
// 				点击
				onSelect:function(obj){
// 		        	选择联系人填充其他信息
					for(var i=0; i<eval(myData).length; i++){
						var tempData = eval(myData)[i];
						if(tempData.uuid == $(obj).find('a').attr('uuid')){
		            		fillContactInfo(obj, tempData);
			        	}
					}
		        }
		    });
		    //由于前面的span被移除了，故而新增的combox当不可输入修改时应该把input只读
		    if(allowModifyAgentInfo == 0){
		    	$("#signChannelList").find("input[name=contactsName]").attr("disabled", true);
		    }
// 		        给第一联系人填充信息
		    fillContactInfo($("#signChannelList").find("ul[name=orderpersonMes]:first"), eval(myData)[0]);
		}		
	});
}

/**
 * 可下拉可修改
 */
$.fn.combox = function(options) {
    var defaults = {
        borderCss: "combox_border",
        inputCss: "combox_input required",
        buttonCss: "combox_button",
        selectCss: "combox_select",
        firstContactName : tempFirstContactName,
        datas:[],
        onSelect:null
    };
    var options = $.extend(defaults, options);

    function _initBorder($border) {//初始化外框CSS
        $border.css({'display':'inline-block', 'position':'relative'}).addClass(options.borderCss);
        return $border;
    }

    function _initInput($border){//初始化输入框
        //删除原有的内容
        $border.children().remove();
        $border.append('<input type="text" name="contactsName" value="' + options.firstContactName + '" class="'+options.inputCss+'"/>');
        $border.append('<em></em>');
       //绑定下拉特效
        $border.delegate('em', 'click', function() {
            var $ul = $border.children('ul');
            if($ul.css('display') == 'none') {
                $ul.slideDown('fast');
            }else {
                $ul.slideUp('fast');
            }
        });
        return $border;//IE6需要返回值
    }

    function _initSelect($border) {//初始化下拉列表
        $border.append('<ul style="position:absolute;left:-1px;display:none;z-index:999" class="'+options.selectCss+'">');
        var $ul = $border.children('ul');
        $ul.css('top',$border.height()+1);
        var length = options.datas.length;
        for(var i=0; i<length ;i++)
            $ul.append('<li><a href="javascript:void(0)" uuid="'+options.datas[i].uuid+'">'+options.datas[i].text+'</a></li>');
        $ul.delegate('li', 'click', function() {
            $border.children(':text').val($(this).text()).attr('uuid',$(this).find('a').attr('uuid'));
            $ul.hide();
            options.onSelect(this);
        });
        return $border;
    }
    
    this.each(function() {
        var _this = $(this);
        _this = _initBorder(_this);//初始化外框CSS
        _this = _initInput(_this);//初始化输入框
        _initSelect(_this);//初始化下拉列表
    });
};

var contactNo = 1;  //联系人临时编号
/**
 * 添加渠道联系人(新有控件)Now
 */
function addAgentContactNew(obj){		
	contactNo++;
    var contactPeopleNum = $("ul[name=orderpersonMes]").length;
    var $currentUl = $(obj).parents("ul[name=orderpersonMes]");
    var $newUl = $currentUl.clone();
    //修改联系人序号
    $newUl.find("span[name=contactNo]").html(contactNo);
    $newUl.find('input').val('');
    $newUl.find('select').append('<option value="" selected="selected"></option>');
    $newUl.children('li').eq(0).find('label font').html(parseInt(contactPeopleNum) + 1);
    $newUl.children('li').eq(1).find('span.yd1AddPeople').remove();
    $newUl.children('li').eq(1).append('<span class="ydbz_x gray" name="delContactButton" onclick="yd1DelPeople(this)">删除联系人</span>');
    $currentUl.parent().append($newUl);
    //绑定事件
    var $border = $newUl.find('[name="channelConcat"]');
    var $ul = $border.find('ul');
    $border.delegate('em', 'click', function() {
        var $ul = $border.children('ul');
        if($ul.css('display') == 'none') {
            $ul.slideDown('fast');
        }else {
            $ul.slideUp('fast');
        }
    });
    //移出收起
    $border.delegate('ul', 'mouseleave', function(){
        $(this).hide();
    });
    $ul.delegate('li', 'click', function() {
        $border.children(':text').val($(this).text()).attr('uuid',$(this).find('a').attr('uuid'));
        $ul.hide();
        //此处调用选中联系人处理逻辑
        //选择联系人填充其他信息						
		for(var i=0; i<dataArray.length; i++){
			var tempData = dataArray[i];
			if(tempData.uuid == $(this).find('a').attr('uuid')){
        		fillContactInfo(this, tempData);
        	}
		}
    });
}

/**
 * 删除联系人
 */
function yd1DelPeople(obj) {
	contactNo--;
    $(obj).parent().parent().remove();
    $("ul[name=orderpersonMes]").each(function (index, element) {
        $(element).children("li").find("span[name=contactNo]").text(index + 1);
    });
}

//==================

var orderInfo = {
    traveller: 
    [
		<c:forEach items="${groupPrices }" var="groupPrice" varStatus="status">
		{ type: "${groupPrice.uuid}", 
		  name: "<trekiz:autoId2Name4Table tableName='traveler_type' sourceColumnName='uuid' srcColumnName='name' value='${groupPrice.travelerType}'/>",
		  cost: { 
			  "${groupPrice.currencyId}":{
				  	  code:'<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId}"/>',
				  	  price:"${groupPrice.price}",
				  }
			  }
		},
		</c:forEach>
    ],
 	// 订单总额
    totalCost: {},
    // 应收金额
    accounts: {},
    // 已收金额
    receipted: 
	    {
    	<c:forEach items="${payedMoneyList }" var="payedMoney" varStatus="status">
	    	"${payedMoney.currencyId}":{
	    		code:'<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${payedMoney.currencyId}"/>', 
	    		price:"${payedMoney.amount}"},
        </c:forEach>
		},
	    
    // 总人数
    totalCount: 0,
};

</script>

</head>
<body>
	<input type="hidden" name="ctx" id="ctx" value="${ctx}" />
	<input type="hidden" name="isTransfer" id="isTransfer" value="${isTransfer}" />
	<input type="hidden" name="hotelOrderUuid" id="hotelOrderUuid" value="${hotelOrder.uuid }" />
	<input type="hidden" name="hotelOrderId" id="hotelOrderId" value="${hotelOrder.id }" />	
	<input type="hidden" name="costMoneyUuid" id="costMoneyUuid" value="${hotelOrder.costMoney }">
	<input type="hidden" name="payedMoneyUuid" id="payedMoneyUuid" value="${hotelOrder.payedMoney }">
	<input type="hidden" name="totalMoneyUuid" id="totalMoneyUuid" value="${hotelOrder.totalMoney }">

	<c:if test="${isTransfer }">
		<page:applyDecorator name="show_head">
		    <page:param name="desc">订单转报名</page:param>
		</page:applyDecorator>
	</c:if>
	<c:if test="${!isTransfer }">
		<page:applyDecorator name="show_head">
		    <page:param name="desc">订单修改</page:param>
		</page:applyDecorator>
	</c:if>

	<div class="ydbzbox fs">
			<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderHotelBaseinfo.jsp"%>

			<div class="ydbz_tit"><!--  <span class="ydExpand" data-target="#bookingPeopleInfo"></span>-->填写预订人信息</div>
			<div id="bookingPeopleInfo">
				<form id="orderpersonMesdtail">
					<div class="mod_information_dzhan" id="secondStepDiv">
						<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow: hidden;">
							<div class="mod_information_d2 ">
								<label><span class="xing">*</span>渠道：</label> 
								<select name="channelType" id="channelType" class="required">
									<option >签约渠道</option>
									<option value="-1" <c:if test="${hotelOrder.orderCompany == -1 }">selected="selected"</c:if> >非签约渠道</option>
								</select>
							</div>
							<div class="mod_information_d2" id="signChannel" <c:if test="${hotelOrder.orderCompany == -1 }">style="display: none"</c:if>>
								<label><span class="xing">*</span>渠道总社：</label> 
								<%-- <select name="orderCompany" id="orderCompany" onchange="loadAgentInfo();" class="required"> --%>
								<select name="orderCompany" id="orderCompany" class="required">
									<c:forEach items="${agentList}" var="agentinfo">
										<option value="${agentinfo.id}" <c:if test="${hotelOrder.orderCompany == agentinfo.id }">selected="selected"</c:if> >${agentinfo.agentName}</option>
									</c:forEach>
								</select>
							</div>
							<div class="mod_information_d2" id="nonChannel" <c:if test="${hotelOrder.orderCompany != -1 }">style="display: none"</c:if> >
								<label class="price_sale_house_label02"><span class="xing">*</span>非签约渠道名称：</label> 
								<input class="valid" type="text" name="orderCompanyName" id="orderCompanyName" value="${hotelOrder.orderCompanyName }"/>
							</div>
						</div>
					</div>
					<p class="ydbz_qdmc" style="padding: 12px 0px;"></p>
					
					<!--签约渠道-->
					<%-- 
					<div id="signChannelList">
						<c:forEach items="${orderContactsList }" var="orderContacts" varStatus="status">
							<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
								<li><label><span class="xing">*</span>渠道联系人：</label> 
									<input type="hidden" name="contactsId" id="contactsId" value="${orderContacts.id }" />
									<input type="text" id="contactsName" name="contactsName" value="${orderContacts.contactsName }"  onafterpaste="this.value=this.value.replaceSpecialChars()" onkeyup="this.value=this.value.replaceSpecialChars()" class="required valid"  maxlength="10" />
								</li>
								<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
									<input type="text"	id="contactsTel" name="contactsTel" value="${orderContacts.contactsTel }" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"	onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" class="required valid"  maxlength="15" />
									<div class="zksx boxCloseOnAdd"	onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
										<c:if test="${status.first == true }"><span class="ydbz_x yd1AddPeople">添加联系人</span></c:if>
										<c:if test="${status.first == false }"><span class="ydbz_x yd1DelPeople gray" data="${orderContacts.id }">删除联系人</span></c:if>
								</li>
								<li flag="messageDiv" class="ydbz_qd_close">
									<ul>
										<li><label>固定电话：</label> <input type="text" name="contactsTixedTel" id="contactsTixedTel" value="${orderContacts.contactsTixedTel }" class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"	 onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"  /></li>
										<li><label>渠道地址：</label> <input type="text" name="contactsAddress" id="contactsAddress" value="${orderContacts.contactsAddress }" class="required" /></li>
										<li><label>传真：</label> <input type="text"  name="contactsFax" id="contactsFax" value="${orderContacts.contactsFax }" class="required" /></li>
										<li><label>QQ：</label> <input type="text" name="contactsQQ" id="contactsQQ" value="${orderContacts.contactsQQ }"  class="required" /></li>
										<li><label>Email：</label> <input type="text"  name="contactsEmail" id="contactsEmail" value="${orderContacts.contactsEmail }" class="required" /></li>
										<li><label>渠道邮编：</label> <input type="text"  name="contactsZipCode" id="contactsZipCode" value="${orderContacts.contactsZipCode }" class="required" /></li>
										<li><label>其他：</label> <input type="text"  name="remark" id="remark" value="${orderContacts.remark }" class="required" /></li>
									</ul>
								</li>
							</ul>
						</c:forEach>
					</div>
					 --%>
					 
					 
						 	<div id="nonChannelList" <c:if test='${hotelOrder.orderCompany != -1 }'>style="display:none;"</c:if> >
						 	<c:forEach items="${orderContactsListNon }" var="orderContacts" varStatus="status">
								<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
									<li><label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label> 
										<input type="hidden" name="contactsId" id="contactsId" value="${orderContacts.id }" />
										<input type="text" id="contactsName" name="contactsName" value="${orderContacts.contactsName }" class="required valid"  />
									</li>
									<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
										<input type="text"	id="contactsTel" name="contactsTel" value="${orderContacts.contactsTel }" class="required valid"  />
										<div class="zksx boxCloseOnAdd"	onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
											<c:if test="${allowAddAgentInfo == 1 and status.count == 1}">
								           		<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
							           		</c:if>
							           		<c:if test="${status.count != 1}">
							           			<span class="ydbz_x gray" name="delContactButton" onclick="yd1DelPeople(this)">删除联系人</span>
							           		</c:if>
									</li>
									<li flag="messageDiv" class="ydbz_qd_close">
										<ul>
											<li><label>固定电话：</label> <input type="text" name="contactsTixedTel" id="contactsTixedTel" value="${orderContacts.contactsTixedTel }"   /></li>
											<li><label>渠道地址：</label> <input type="text" name="contactsAddress" id="contactsAddress"
																			onblur="updataInputTitle(this);" title="${orderContacts.contactsAddress }" value="${orderContacts.contactsAddress}"   /></li>
											<li><label>传真：</label> <input type="text"  name="contactsFax" id="contactsFax" value="${orderContacts.contactsFax }"   /></li>
											<li><label>QQ：</label> <input type="text" name="contactsQQ" id="contactsQQ" value="${orderContacts.contactsQQ }"   /></li>
											<li><label>Email：</label> <input type="text"  name="contactsEmail" id="contactsEmail" value="${orderContacts.contactsEmail }"  /></li>
											<li><label>渠道邮编：</label> <input type="text"  name="contactsZipCode" id="contactsZipCode" value="${orderContacts.contactsZipCode }"   /></li>
											<li><label>其他：</label> <input type="text"  name="remark" id="remark" value="${orderContacts.remark}" onblur="updataInputTitle(this);"
																		   title="${orderContacts.remark}" /></li>
										</ul>
									</li>
								</ul>
							</c:forEach>
						 	</div>
					 	
					 		<%-- 签约渠道,显示输入框新控件 --%>
						 	<div id="signChannelList" <c:if test='${hotelOrder.orderCompany == -1 }'>style="display:none;"</c:if>>
				 			<c:forEach items="${orderContactsList }" var="orderContacts" varStatus="status">
								<ul class="ydbz_qd min-height" id="orderpersonMes" name="orderpersonMes">
									<li><label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label> 
										<input type="hidden" name="contactsId" id="contactsId" value="${orderContacts.id }" />
										<span name="channelConcat"></span>
									</li>
									<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
										<input type="text"	id="contactsTel" name="contactsTel" value="${orderContacts.contactsTel }"  class="required valid"  />
										<div class="zksx boxCloseOnAdd"	onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
											<c:if test="${allowAddAgentInfo == 1 and status.count == 1 }">
								           		<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
							           		</c:if>
							           		<c:if test="${status.count != 1}">
							           			<span class="ydbz_x gray" name="delContactButton" onclick="yd1DelPeople(this)">删除联系人</span>
							           		</c:if>
									</li>
									<li flag="messageDiv" class="ydbz_qd_close">
										<ul>
											<li><label>固定电话：</label> <input type="text" name="contactsTixedTel" id="contactsTixedTel" value="${orderContacts.contactsTixedTel }"  /></li>
											<li><label>渠道地址：</label> <input type="text" name="contactsAddress" id="contactsAddress" onblur="updataInputTitle(this);"
																			value="${orderContacts.contactsAddress }" title="${orderContacts.contactsAddress}"
											/></li>
											<li><label>传真：</label> <input type="text"  name="contactsFax" id="contactsFax" value="${orderContacts.contactsFax }"   /></li>
											<li><label>QQ：</label> <input type="text" name="contactsQQ" id="contactsQQ" value="${orderContacts.contactsQQ }"    /></li>
											<li><label>Email：</label> <input type="text"  name="contactsEmail" id="contactsEmail" value="${orderContacts.contactsEmail }"  /></li>
											<li><label>渠道邮编：</label> <input type="text"  name="contactsZipCode" id="contactsZipCode" value="${orderContacts.contactsZipCode }"  /></li>
											<li><label>其他：</label>
												<input type="text"  name="remark" id="remark" value="${orderContacts.remark }" onblur="updataInputTitle(this);"  title="${orderContacts.remark }" /></li>
										</ul>
									</li>
								</ul>
							</c:forEach>
							</div>
					 	
				</form>
			</div>
			
			<div class="ydbz_tit"><!-- <span class="ydExpand" data-target="#costTable"></span> -->费用及人数</div>
			<div id="costTable">
				<table id="moneyAndPeopleTab" class="table activitylist_bodyer_table_new contentTable_preventive">
					<thead>
						<tr>
							<th width="13%">游客类型</th>
							<th width="25%">同行价/人</th>
							<th width="25%"><span class="xing">*</span>人数</th>
							<th width="25%">小计</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${groupPrices }" var="groupPrice">
							<tr id="${groupPrice.uuid}" class="groupPrices_tr">
								<td class="tc">
									<span class="personType">
									<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.travelerType}"/>
									</span>
									<input type="hidden" name="groupPriceUuid" id="groupPriceUuid" value="${groupPrice.uuid}" />
								</td>
								<td class="tc">
									<span><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/><fmt:formatNumber  type="currency" pattern="##0.00" value="${groupPrice.price}" /></span>
								</td>
								<td class="tc"><input type="text" class="price_sale_house_w100" name="orderPersonNum" id="orderPersonNum" value="${groupPrice.num }"/></td>
								<td class="tc"><span>${groupPrice.subTotal }</span></td>
							</tr>
						</c:forEach>
						<tr>
							<td colspan="5" class="tr">
								<span class="price_sale_houser_25"><label>合计人数：</label>
									<em> <span id="totalPeopleCount"></span> 人</em>
								</span> 
								<span class="price_sale_houser_25"><label>合计金额：</label>
									<em><i><span class="totalCost" id="totalPeopleMoney"></span></i></em>
								</span>
							</td>
						</tr>
						<tr>
							<td colspan="5" class="tl">
								<span class="price_sale_houser_25" style="margin-left:89px;"><label>单房差：</label>
									<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityHotelGroup.currencyId }"/>&nbsp;${activityHotelGroup.singlePrice }
								</span>
								<span class="price_sale_houser_25" style="margin-left:200px;"><label>需交定金：</label>
									<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityHotelGroup.frontMoneyCurrencyId }"/>&nbsp;${activityHotelGroup.frontMoney }
								</span>
							</td>
						</tr>
					</tbody>
				</table>

				<div class="activitylist_bodyer_right_team_co1" style="width: 150px;">
					 <label>预报名间数：</label>${hotelOrder.forecaseReportNum}&nbsp;间
				</div>
				<div class="activitylist_bodyer_right_team_co1" style="width: 320px;">
					<strong><div class="activitylist_team_co3_text" style="width:130px; font-weight:bold;">酒店扣减间数：</div></strong> 
					控房<input id="subControlNum" name="subControlNum" type="text" data-type="number" class="inputTxt" readonly="readonly" value="${hotelOrder.subControlNum }"/>间
					   <a id="kfSel" style="position:relative;">选择
					   <div style="display: none; top: 30px; left: -20px;"	class="pop_inner_outer">
							<div class="confirm_inner_outer_sel">
								<span class="mr25"> <input 	class="redio_martop_4 procurement" data-text="內采" type="checkbox" checked="checked"  /> 內采</span> 
								<span class="mr25"> <input class="redio_martop_4 procurement" data-text="外采" type="checkbox" /> 外采</span>
							</div>
							<table class="table  activitylist_bodyer_table_new">
								<thead>
									<tr>
										<th width="15%">入住日期</th>
										<th width="9%">房型&amp;晚数</th>
										<th width="10%">餐型</th>
										<th width="10%">上岛方式</th>
										<th width="10%">余位/库存</th>
										<th width="10%">地接供应商</th>
										<th width="10%">采购类型</th>
										<th width="10%">使用库存数</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${hotelControlDetailList }" var="hotelControlDetail">
										<c:if test="${hotelControlDetail.purchaseType == 0 }">
											<tr data-type="内采">
												<td class="tc font_c66 new_hotel_p_table2_tdf"><p><fmt:formatDate value="${hotelControlDetail.inDate }" pattern="yyyy.MM.dd"/></p></td>
												<td class="tc font_c66 ">
													<c:forEach items="${hotelControlDetail.rooms }" var="room">
														<p><trekiz:autoId2Name4Class  classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.roomUuid}"/>*${room.night }</p>
													</c:forEach>
												</td>
												<td class="tc font_c66">
													<trekiz:defineDict name="hotelMeals" type="hotel_meal_type" defaultValue="${hotelControlDetail.hotelMeal}" readonly="true" />
												</td>
												<td class="tc font_c66">
													<trekiz:defineDict name="island_way" type="island_way" defaultValue="${hotelControlDetail.islandWay}" readonly="true" />
												</td>
												<td class="tc font_c66">
													${hotelControlDetail.stock - hotelControlDetail.sellStock }/${hotelControlDetail.stock }
												</td>
												<td class="tc font_c66">${hotelControlDetail.groundSupplier }</td>
												<td class="tc font_c66">内采</td>
												<td class="tc"><input data-type="number" data-min="0" class="inputTxt w50_30 spread " type="text" /></td>
											</tr>
										</c:if>
									</c:forEach>
									<c:forEach items="${hotelControlDetailList }" var="hotelControlDetail">
										<c:if test="${hotelControlDetail.purchaseType == 1 }">
											<tr style="display: none;" data-type="外采">
												<td class="tc font_c66 new_hotel_p_table2_tdf"><p><fmt:formatDate value="${hotelControlDetail.inDate }" pattern="yyyy.MM.dd"/></p></td>
												<td class="tc font_c66 ">
													<c:forEach items="${hotelControlDetail.rooms }" var="room">
														<p><trekiz:autoId2Name4Class  classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.roomUuid}"/>*${room.night }</p>
													</c:forEach>
												</td>
												<td class="tc font_c66">
													<trekiz:defineDict name="hotelMeals" type="hotel_meal_type" defaultValue="${hotelControlDetail.hotelMeal}" readonly="true" />
												</td>
												<td class="tc font_c66">
													<trekiz:defineDict name="island_way" type="island_way" defaultValue="${hotelControlDetail.islandWay}" readonly="true" />
												</td>
												<td class="tc font_c66">
													${hotelControlDetail.stock - hotelControlDetail.sellStock }/${hotelControlDetail.stock }
												</td>
												<td class="tc font_c66">${hotelControlDetail.groundSupplier }</td>
												<td class="tc font_c66">外采</td>
												<td class="tc"><input data-type="number" data-min="0" class="inputTxt w50_30 spread " type="text" /></td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
							<div class="btn_confirm_inner_outer">
								<input	class="btn_confirm_inner_outer02 maring_bottom10 up_load_visa_info_btn_del" value="确定" type="button" />
							</div>
						</div>
					</a>
				</div>
				<div class="activitylist_bodyer_right_team_co1"	style="width: 320px;">
					<div class="activitylist_team_co3_text" style="width: 100px; font-weight: normal;">非控房</div>
					<input type="text" id="subUnControlNum" name="subUnControlNum"  value="${hotelOrder.subUnControlNum }" data-type="number"  class="inputTxt" /> 间
				</div>
				<ul class="ydbz_qd_02"><li><label>合计：</label> <span id="hotelRoomTotalNumber"></span>间</li></ul>
			</div>
			</div>
			
	<!--费用调整开始-->
	<div class="ydbz_tit"><!-- <span class="ydExpand" data-target="#adjustCost"></span>-->费用调整</div>
	<form 	class=" form-search" id="adjustCost" novalidate="novalidate">
				<div class="mod_information_dzhan" id="dddddStepDiv">
					<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow: hidden;">
						<table class="contentTable_preventive table_padings" id="add_other_charges_table">
							<c:forEach items="${hotelOrderPriceList }" var="hotelOrderPrice" varStatus="status" >
								<!-- 指定类型费用展示 -->
								<c:if test="${hotelOrderPrice.priceType != 4 && hotelOrderPrice.priceType != 1}">
									<tr>
										<td width="100" class="tr">
											${hotelOrderPrice.priceTypeStr }金额：
											<input type="hidden" name="orderPriceUuid" id="orderPriceUuid" value="${hotelOrderPrice.uuid }">
											<input type="hidden" name="orderPriceId" id="orderPriceId" value="${hotelOrderPrice.id }">
											<input type="hidden" name="priceType" id="priceType" value="${hotelOrderPrice.priceType }">
										</td>
										<td width="280" class="tl">
											<label> 
												<select class="w80" name="currencyId" id="currencyId">
													<c:forEach items="${currencyList }" var="currency">
														<option value="${currency.id }" <c:if test="${hotelOrderPrice.currencyId == currency.id }">selected="selected"</c:if> data-currency="${currency.currencyMark}">${currency.currencyName}</option>
													</c:forEach>
												</select> 
											</label> 
											<input type="text" data-type="float" id="orderPrice" name="orderPrice" class="price_sale_house_w93 <c:if test="${hotelOrderPrice.priceType == 3 }">price</c:if>" value='<fmt:formatNumber  type="currency" pattern="##0.00" value="${hotelOrderPrice.price}" />'  />
										</td>
										<td width="180" class="tr">备注：</td>
										<td colspan="3">
											<span class="tl"> <input type="text" id="orderPriceRemark" name="orderPriceRemark" value="${hotelOrderPrice.remark }"  class="price_sale_house_w300"  /></span>
											<span class="padr10"></span>
											<input id="addOtherCharges" value="增加其他费用" style="width:auto;" class="btn btn-primary" type="button">
										</td>
									</tr>
								</c:if>
								<!-- 其他费用展示 -->
								<c:if test="${hotelOrderPrice.priceType == 4 || hotelOrderPrice.priceType == '4' }">
									<tr>
										<td class="tr">金额名称：</td>
										<td>
											<input type="hidden" name="orderPriceUuid" id="orderPriceUuid" value="${hotelOrderPrice.uuid }">
											<input type="hidden" name="orderPriceId" id="orderPriceId" value="${hotelOrderPrice.id }">
											<input type="hidden" name="priceType" id="priceType" value="4">
											<span class="tl"> <input type="text" id="priceName" name="priceName" class="price_sale_house_w93" value="${hotelOrderPrice.priceName }"   /></span>
										</td>
										<td class="tr tr_other_u">
											<input  type="radio" name="other_u_${status.index }" id="u138_input" class="dis_inlineblock"  value="1"  data-label="增加" <c:if test="${hotelOrderPrice.price >= 0 }">checked="checked"</c:if> /><label for="u138_input">增加</label> 
											<input type="radio" name="other_u_${status.index }"  id="u138_input2" value="0" data-label="减少"  <c:if test="${hotelOrderPrice.price < 0 }">checked="checked"</c:if> /><label for="u140_input">减少：</label>
										</td>
										<td class="tl">
											<label> 
												<select class="w80" name="currencyId" id="currencyId">
													<c:forEach items="${currencyList }" var="currency">
														<option value="${currency.id }" <c:if test="${hotelOrderPrice.currencyId == currency.id }">selected="selected"</c:if> data-currency="${currency.currencyMark}">${currency.currencyName}</option>
													</c:forEach>
												</select>
											</label>
											<input type="text" id="orderPrice" name="orderPrice" value='<fmt:formatNumber  type="currency" pattern="##0.00" value="${hotelOrderPrice.price < 0 ? -hotelOrderPrice.price : hotelOrderPrice.price}" />'  data-type="float" class="price_sale_house_w93 price" />
										</td>
										<td class="tr">备注：</td>
										<td class="tl">
											<input type="text" class="price_sale_house_w93" value="${hotelOrderPrice.remark }" id="orderPriceRemark" name="orderPriceRemark"/> 
											<span class="padr10"></span> 
											<i class="price_sale_house_02 delOtherCharges" data="${hotelOrderPrice.uuid }"></i>
										</td>
									</tr>
								</c:if>
							</c:forEach>
						</table>
					</div>
				</div>
			</form>
	<!--费用调整结束-->
			
	<!--费用结算开始-->
	<div class="ydbz_tit"><!--  <span class="ydExpand" data-target="#costSettlement"></span>-->费用结算</div>
	<div id="costSettlement">
		<div class="mod_information_dzhan">
			<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow: hidden;">
				<div class="mod_information_d2">
					<label>订单总额：</label><span class="totalCost" id="costMoneySpan"><%-- ${costMoneyStr } --%></span>
				</div>
				<div class="mod_information_d2 " style="width: 60%">
					<label>结算总额：</label><span class="red accounts" id="totalMoneySpan"><%-- ${totalMoneyStr } --%></span>
				</div>
			</div>
		</div>
		<!-- <p class="ydbz_qdmc"></p>-->
		<p class="price_sale_houser_line"></p>
		<div class="mod_information_dzhan">
			<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow: hidden;">
				<div class="mod_information_d2">
					<label>应收金额：</label><span class="accounts"><%-- ${totalMoneyStr } --%></span>
				</div>
				<div class="mod_information_d2 ">
					<label>已收金额：</label><span class="payedMoney" id="payedMoneySpan"><%-- ${payedMoneyStr } --%></span>
				</div>
				<div class="mod_information_d2 ">
					<label>未收金额：</label><span class="green unReceipted"><%-- ${noPayMoneyStr } --%></span>
				</div>
			</div>
		</div>
	</div>
	<!--费用结算结束-->
	<!--旅客信息开始-->
	<div class="ydbz_tit"><!-- <span class="ydExpand" data-target="#passengerInfo"></span>-->游客信息</div>
	<div id="passengerInfo">
				<input id="addpassengerInfo" value="添加游客信息" class="btn btn-primary" type="button" />
				<c:if test="${travelerList != null }">
				<table id="passengerInfoTable" class="table activitylist_bodyer_table_new contentTable_preventive" 	style="min-width: 1600px;">
					<thead>
						<tr>
							<th width="3%">序号</th>
							<th width="6%"><span class="xing">*</span>姓名</th>
							<th width="3%">英文姓名</th>
							<th width="8%">游客类型</th>
							<th width="7%">性别</th>
							<th width="12%">签证国家及类型</th>
							<th width="25%">证件类型/证件号码/有效期</th>
							<th width="13%"><span class="xing">*</span>价格</th>
							<th width="8%">备注</th>
							<th width="6%">资料上传</th>
							<th width="6%">操作</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${travelerList }" var="traveler" varStatus="status">
						<tr>
							<td class="tc">${status.index + 1 }</td>
							<td class="tc">
								<input type="hidden" name="travelerUuid" id="travelerUuid" value="${traveler.uuid }" />
								<input type="text" class="price_sale_house_w93" id="travelerName" name="travelerName"  value="${traveler.name }"/>
							</td>
							<td class="tc"><input type="text" class="price_sale_house_w93" name="nameSpell" id="nameSpell" value="${traveler.nameSpell }"/></td>
							<td class="tc">
								<select name="personTypeSelect" id="personTypeSelect" class="w80 display_inline">
									<c:forEach items="${travelerTypeList }" var="travelerType">
										<option value="${travelerType.travelerType }" <c:if test="${travelerType.travelerType == traveler.personType }">selected="selected"</c:if> ><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${travelerType.travelerType}"/></option>
									</c:forEach>
								</select>
							</td>
							<td class="tc">
								<select name="sexSelect" id="sexSelect" class="price_sale_house_w60 display_inline">
									<option value="1" <c:if test="${traveler.sex == '1' }">selected="selected"</c:if>>男</option>
									<option value="2" <c:if test="${traveler.sex == '2' }">selected="selected"</c:if>>女</option>
								</select>
							</td>
							<td class="tc table_padings_none" id="travelerVisa_td">
								<c:forEach items="${traveler.hotelTravelervisaList }" var="travelerVisa">
									<p>
										<input type="hidden" name="travelerVisaUuid" id="travelerVisaUuid" value="${travelerVisa.uuid }">
										<select name="countrySelect" id="countrySelect" class="w40b">
											<option value="">不限</option>
											<c:forEach items="${sysGeographyList }" var="sysGeography">
												<option value="${sysGeography.uuid}" <c:if test="${travelerVisa.country == sysGeography.uuid }">selected="selected"</c:if> >${sysGeography.nameCn }</option>
											</c:forEach>
										</select>
										<select name="visaTypeSelect" id="visaTypeSelect" class="w40b">
											<c:forEach items="${visaTypes }" var="visa">
												<option value="${visa.id }" <c:if test="${visa.id == travelerVisa.visaTypeId }">selected="selected"</c:if>>${visa.label}</option>
											</c:forEach>
										</select>
										<i class="price_sale_house_01"></i>
									</p>
								</c:forEach>
							</td>
							<td id="travelerPapers_td">
								<c:forEach items="${traveler.hotelTravelerPapersTypeList }" var="travelerPapers">
									<p>
										<input type="hidden" name="travelerPapersUuid" id="travelerPapersUuid" value="${travelerPapers.uuid }" />
										<select name="papersTypeSelect" id="papersTypeSelect" class="w80">
											<option value="0" <c:if test="${travelerPapers.papersType == '0' }">selected="selected"</c:if> >请选择</option>
											<option value="1" <c:if test="${travelerPapers.papersType == '1' }">selected="selected"</c:if> >身份证</option>
											<option value="2" <c:if test="${travelerPapers.papersType == '2' }">selected="selected"</c:if> >护照</option>
											<option value="3" <c:if test="${travelerPapers.papersType == '3' }">selected="selected"</c:if> >警官证</option>
											<option value="4" <c:if test="${travelerPapers.papersType == '4' }">selected="selected"</c:if> >军官证</option>
											<option value="5" <c:if test="${travelerPapers.papersType == '5' }">selected="selected"</c:if> >其他</option>
										</select> 
										<input type="text" class="w130 input_pad" name="idCard" id="idCard" value="${travelerPapers.idCard }" /> 
										<input name="validityDate" id="validityDate" type="text" onclick="WdatePicker()" class="dateinput required w90 input_pad"  value="<fmt:formatDate value="${travelerPapers.validityDate }" pattern="yyyy-MM-dd"/>" /> 
										<i class="price_sale_house_01"></i>
									</p>
								</c:forEach>
							</td>
							<td class="tc" id="moneyAmount_td">
								<c:forEach items="${traveler.hotelMoneyAmountList }" var="moneyAmount">
									<p>
										<input type="hidden" name="travelerMoneyUuid" id="travelerMoneyUuid"  value="${moneyAmount.uuid }" />
										<select name="currencyIdSelect" id="currencyIdSelect" class="w30b">
											<c:forEach items="${currencyList }" var="currency">
												<option value="${currency.id }" <c:if test="${moneyAmount.currencyId == currency.id }">selected="selected"</c:if> data-currency="${currency.currencyMark}">${currency.currencyName}</option>
											</c:forEach>
										</select> 
										<input type="text" name="travelerMoney" id="travelerMoney" data-type="float" class="w30b " value="${moneyAmount.amount }" /><i class="price_sale_house_01"></i>
									</p>
								</c:forEach>
							</td>
							<td class="tc"><input type="text" name="travelerRemark" id="travelerRemark" class="price_sale_house_w93" value="${traveler.remark }" /></td>
							<td class="tc" id="file_td">
								<a name="#" class="btn_addBlue_file" id="addcost" onclick="up_files_pop(this);">附件管理</a> <!--上传附件弹窗层开始-->
								<div class="up_files_pop" style="display: none;" id="up_files_pop">
									<ul style="margin-left: 0;">
										<c:forEach items="${traveler.hotelTravelerFilesList}" var="travelerFiles">
											<li>
												<a class="padr10" href="javascript:void(0)" onclick="downloads('${travelerFiles.docId}')" >${travelerFiles.docName }</a> <span class="tdred" style="cursor: pointer;" onclick="deleteFileInfo('${travelerFiles.uuid }',this)">删除</span>
												<input type="hidden" name="hotelAnnexUuid" value="${travelerFiles.uuid }" />
											</li>
										</c:forEach>
									</ul>
									<a name="addcost" class="btn_addBlue_file" onclick="uploadFiles(this)">上传附件</a>
								</div> <!--上传附件弹窗层开始-->
							</td>
							<td class="tc">
								<a onclick="save_tours_obj(this)">保存</a> | <a class="delLink" onclick="delete_traveler(this,'${traveler.uuid}')" >删除</a>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</c:if>
				<!--旅客信息结束-->
				<div flag="messageDiv" class="ydbz2_lxr">
					<p class="hotel_discount_count_mar20">
						<label>备注：</label>
						<textarea style="width: 786px; height: 83px;" name="hotelOrderRemark" id="hotelOrderRemark" maxlength="200">${hotelOrder.remark}</textarea>
					</p>
				</div>
			</div>
	<div class="release_next_add">
				<input value="保存" onclick="jbox_finance_change_add_records() " class="btn btn-primary" type="button" /> 
				<input value="关闭" onclick="window.close();" class="btn btn-primary" type="button" />
			</div>

	<!-- 其他费用模板 -->
	<table>
		<tr class="add_other_charges" style="display:none;" >
			<td class="tr">金额名称：</td>
			<td>
				<input type="hidden" name="orderPriceUuid" id="orderPriceUuid" value="">
				<input type="hidden" name="orderPriceId" id="orderPriceId" value="">
				<input type="hidden" name="priceType" id="priceType" value="4">
				<span class="tl"> <input type="text"	class="price_sale_house_w93" value="" id="priceName" name="priceName" maxlength="" /></span>
			</td>
			<td class="tr tr_other_u">
				<input  type="radio" name="other_u" id="u138_input"  value="1" class="dis_inlineblock"   data-label="增加" checked="checked" /><label for="u138_input">增加</label> 
				<input type="radio" name="other_u"  id="u138_input2" value="0" data-label="减少"   /><label for="u140_input">减少：</label>
			</td>
			<td class="tl">
				<label> 
					<select class="w80" name="currencyId" id="currencyId">
						<c:forEach items="${currencyList }" var="currency">
							<option value="${currency.id }"  data-currency="${currency.currencyMark}">${currency.currencyName}</option>
						</c:forEach>
					</select>
				</label>
				<input type="text" value=""  data-type="float" class="price_sale_house_w93 price" id="orderPrice"  name="orderPrice"/>
			</td>
			<td class="tr">备注：</td>
			<td class="tl">
				<input type="text" class="price_sale_house_w93" value="" id="orderPriceRemark" name="orderPriceRemark"/> 
				<span class="padr10"></span>
				<i class="price_sale_house_02 delOtherCharges" data="" ></i>
			</td>
		</tr>
	</table>
	<!-- 其他费用模板 -->
	
	<!-- 游客信息模板 -->
	<table>
		<tr style="display: none;" id="add_tours_obj_tr">
			<td class="tc"></td>
			<td class="tc">
				<input type="hidden" name="travelerUuid" id="travelerUuid" value="${traveler.uuid }" />
				<input type="text" class="price_sale_house_w93" id="travelerName" name="travelerName"  value=""/></td>
			<td class="tc"><input type="text" class="price_sale_house_w93" name="nameSpell" id="nameSpell" value=""/></td>
			<td class="tc">
				<select name="personTypeSelect" id="personTypeSelect" class="w80 display_inline">
					<c:forEach items="${travelerTypeList }" var="travelerType">
						<option value="${travelerType.travelerType }"  ><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${travelerType.travelerType}"/></option>
					</c:forEach>
				</select>
			</td>
			<td class="tc">
				<select name="sexSelect" id="sexSelect" class="price_sale_house_w60 display_inline">
					<option value="1" >男</option>
					<option value="2" >女</option>
				</select>
			</td>
			<td class="tc table_padings_none" id="travelerVisa_td">
				<p>
					<input type="hidden" name="travelerVisaUuid" id="travelerVisaUuid" value="">
					<select name="countrySelect" id="countrySelect" class="w40b">
						<c:forEach items="${sysGeographyList }" var="sysGeography">
							<option value="${sysGeography.uuid}" <c:if test="${travelerVisa.country == sysGeography.uuid }">selected="selected"</c:if> >${sysGeography.nameCn }</option>
						</c:forEach>
					</select>
					<select name="visaTypeSelect" id="visaTypeSelect" class="w40b">
						<c:forEach items="${visaTypes }" var="visa">
							<option value="${visa.id }" >${visa.label}</option>
						</c:forEach>
					</select> <i class="price_sale_house_01"></i>
				</p>
			</td>
			<td id="travelerPapers_td">
				<p>
					<input type="hidden" name="travelerPapersUuid" id="travelerPapersUuid" value="" />
					<select name="papersTypeSelect" id="papersTypeSelect" class="w80">
						<option value="0"  >请选择</option>
						<option value="1"  >身份证</option>
						<option value="2"  >护照</option>
						<option value="3"  >警官证</option>
						<option value="4"  >军官证</option>
						<option value="5"  >其他</option>
					</select> 
					<input type="text" class="w130 input_pad" name="idCard" id="idCard" value="" /> 
					<input name="validityDate" id="validityDate"  type="text" onclick="WdatePicker()" class="dateinput required w90 input_pad" /> 
					<i class="price_sale_house_01"></i>
				</p>
			</td>
			<td class="tc" id="moneyAmount_td">
				<p>
					<input type="hidden" name="travelerMoneyUuid" id="travelerMoneyUuid"  value="${moneyAmount.uuid }" />
					<select name="currencyIdSelect" id="currencyIdSelect" class="w30b">
						<c:forEach items="${currencyList }" var="currency">
							<option value="${currency.id }"  data-currency="${currency.currencyMark}">${currency.currencyName}</option>
						</c:forEach>
					</select> 
					<input name="travelerMoney" id="travelerMoney"  type="text" data-type="float" class="w30b " /><i class="price_sale_house_01"></i>
				</p>
			</td>
			<td class="tc"><input type="text" class="price_sale_house_w93" name="travelerRemark" id="travelerRemark" /></td>
			<td class="tc" id="file_td">
				<a name="#" class="btn_addBlue_file" id="addcost" onclick="up_files_pop(this);">附件管理</a> <!--上传附件弹窗层开始-->
				<div class="up_files_pop" style="display: none;" id="up_files_pop">
					<ul style="margin-left: 0;">
						
					</ul>
					<a name="addcost" class="btn_addBlue_file" onclick="uploadFiles(this)" >上传附件</a>
				</div> <!--上传附件弹窗层开始-->
			</td>
			<td class="tc">
				<a onclick="save_tours_obj(this)">保存</a> | <a class="delLink"  onclick="delete_traveler(this)" >删除</a>
			</td>
		</tr>
	</table>
	<!-- 游客信息模板 -->
	
</body>
</html>