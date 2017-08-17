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

<script	src="${ctxStatic}/modules/island/islandorder/islandOrderCommon.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<style>
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
    cursor:auto;
    background:transparent; 
    border:0px;   
    box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
}
</style>
<script type="text/javascript">
var contextPath = "${ctx}";
var agentinfoFirst;
var agentAddress;

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
//     receipted: 
// 	    {
//     	<c:forEach items="${payedMoneyList }" var="payedMoney" varStatus="status">
// 	    	"${payedMoney.currencyId}":{ 
// 	    		code:'<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${payedMoney.currencyId}"/>', 
// 	    		price:"${payedMoney.amount}"},
//         </c:forEach>
// 		},
	    
    // 总人数
    totalCount: 0
};
//填写人数的时候，控制人数不应比对应舱位的余位要多
function controlNumByRem(obj, spaceLevel){
	$(obj).val($(obj).val().replace(/\s/g, ""));//去除空格
	if($(obj).val() == null || $(obj).val() == '' || isNaN($(obj).val())){
		$(obj).val(0);//为空则置为0
	}else{
		$(obj).val(parseInt($(obj).val()));//正常数据转换成int
	}
	var num = 0;//某种舱位等级的人数总数
	$("input[id^=orderPersonNum"+spaceLevel+"]").each(function(){
		num = num + parseInt($(this).val());
	});
	var rem = $("#remNumber"+spaceLevel).val();
	var spcLevelName =  $("#spaceLevelName"+spaceLevel).val();
	rem = parseInt(rem);
	//当人数比对应舱位的余位少，提示
	var travelerNum = $('#passengerInfoTable tbody tr').length;
	if(rem < num){
		$(obj).val(travelerNum);
		$.jBox.tip('舱位等级为'+spcLevelName+'的余位不足请重新输入','warning', { focusId : $(obj).attr("id") });
	}
	
}

</script>

<script type="text/javascript">
var contactsNameValue = $("input[name=contactsNameValue]").val();

var isAllowModifyAgentInfo=${isAllowModifyAgentInfo};//渠道联系人是否允许被修改
var isAllowAddAgentInfo=${isAllowAddAgentInfo};//渠道联系人是否允许被添加

//可下拉可修改  define combox()
$.fn.combox = function(options) {
    var defaults = {
        borderCss: "combox_border",
        inputCss: "combox_input",
        buttonCss: "combox_button",
        selectCss: "combox_select",
        datas:[],
        onSelect:null
    };
    var options = $.extend(defaults, options);

    function _initBorder($border) {//初始化外框CSS
        $border.css({'display':'inline-block', 'position':'relative'}).addClass(options.borderCss);
        return $border;
    }

    function _initInput($border){//初始化输入框
        $border.append('<input type="text" name="contactsName" value="${orderContacts.contactsName}" class="'+options.inputCss+'"/>');
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

	/**
	 * 对应海岛游 填写单下信息页面
	 * C346&C406
	 */
	$(function(){
		
	    $('[name="channelConcat"]').combox({datas:[{uuid:"uuid",text:"选项1"},{uuid:"uuid",text:'选项2'},{uuid:"uuid",text:'选项3'}],
	        onSelect:function(obj){
	        	var _id = $(obj).find('a').attr('uuid');
				var signChannel = $(obj).parent().parent().parent().parent();

				//处理第一联系人
				if(_id == null || _id == '' ){
					signChannel.find("input[name=contactsTel]").val(agentinfoFirst.agentContactMobile);
					signChannel.find("input[name=contactsTixedTel]").val(agentinfoFirst.agentContactTel);
					signChannel.find("input[name=contactsAddress]").val(agentAddress);
					signChannel.find("input[name=contactsFax]").val(agentinfoFirst.agentContactFax);
					signChannel.find("input[name=contactsQQ]").val(agentinfoFirst.agentContactQQ);
					signChannel.find("input[name=contactsEmail]").val(agentinfoFirst.agentContactEmail);
				}else {
					$.ajax({
						url: contextPath + "/islandOrder/findSupplyContacts",
						data: {"id": _id},
						type: "POST",
						success: function (data) {
//        				_data = data;
							if (data) {
								signChannel.find("input[name=contactsTel]").val(data.contactMobile);
								signChannel.find("input[name=contactsTixedTel]").val(data.contactPhone);
								signChannel.find("input[name=contactsAddress]").val(agentAddress);
								signChannel.find("input[name=contactsFax]").val(data.contactFax);
								signChannel.find("input[name=contactsQQ]").val(data.contactQQ);
								signChannel.find("input[name=contactsEmail]").val(data.contactEmail);

								// ordercontacts 表里的id(主键)
//								if (data.id) {
//									signChannel.find("input[name=contactsId]").val(data.id);
//								} else {
//									signChannel.find("input[name=contactsId]").val("");
//								}
							}
						}
					});
				}
	        }
	    });


		if(isAllowModifyAgentInfo==0){
			$("input[name=orderContacts_contactsName]").attr("readonly", "readonly");
		}
		

		initAgentInfo();
		initSelect();
	    
	})


/**
 * 初始化联系人姓名
 */
function initAgentInfo() {
	$("#signChannelList ul").find("[name='contactsName']").each(function(index) {
		$(this).val($("#contactsNameValue" + index).val());
	});
}

/**
 * 初始化下拉框
 */
function initSelect(){
	var agentId = $("#orderCompany").val();
	$.ajax({
		type:"POST",
		url: contextPath + "/islandOrder/loadAgentInfo",
		data:{
			"id":agentId
		},
		success:function(data) {
			agentinfoFirst = data.agentinfo;  //将第一联系人置为全局变量
			agentAddress = data.address;

			$("#signChannelList ul").find("[class=combox_select]").each(function(index){
				$(this).empty();
				$(this).append("<li><a href='javascript:void(0)' uuid=''>" + data.agentinfo.agentContact + "</a></li>");

				if(data.supplyContacts){
					for(var i=0; i<data.supplyContacts.length; i++){
						$(this).append("<li><a href='javascript:void(0)' uuid="+ data.supplyContacts[i].id +">" + data.supplyContacts[i].contactName + "</a></li>");
					}
				}

			});

		}
	});
}


</script>

</head>
<body>
	<input type="hidden" name="ctx" id="ctx" value="${ctx}" />
	<input type="hidden" name="isTransfer" id="isTransfer" value="${isTransfer}" />
	<input type="hidden" name="islandOrderUuid" id="islandOrderUuid" value="${islandOrder.uuid }" />
	<input type="hidden" name="islandOrderId" id="islandOrderId" value="${islandOrder.id }" />	
	<input type="hidden" name="costMoneyUuid" id="costMoneyUuid" value="${islandOrder.costMoney }">
	<input type="hidden" name="payedMoneyUuid" id="payedMoneyUuid" value="${islandOrder.payedMoney }">
	<input type="hidden" name="totalMoneyUuid" id="totalMoneyUuid" value="${islandOrder.totalMoney }">
<!-- 	<input type="hidden" name="travelerNum" id="travelerNum" value="${fn:length(travelerList) }"> -->

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
			<%@ include file="/WEB-INF/views/modules/island/islandorder/orderIslandBaseinfo.jsp"%>

			<div class="ydbz_tit"><span class="ydExpand" data-target="#bookingPeopleInfo"></span>填写预订人信息</div>
			<div id="bookingPeopleInfo">
				<form id="orderpersonMesdtail">
					<div class="mod_information_dzhan" id="secondStepDiv">
						<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow: hidden;">
							<div class="mod_information_d2 ">
								<label><span class="xing">*</span>渠道：</label> 
								<select name="channelType" id="channelType" class="required">
									<option >签约渠道</option>
									<option value="-1" <c:if test="${islandOrder.orderCompany == -1 }">selected="selected"</c:if> >非签约渠道</option>
								</select>
							</div>
							<div class="mod_information_d2" id="signChannel" <c:if test="${islandOrder.orderCompany == -1 }">style="display: none"</c:if>>
								<label><span class="xing">*</span>渠道总社：</label> 
								<select name="orderCompany" id="orderCompany" onchange="loadAgentInfo();" class="required">
									<c:forEach items="${agentList}" var="agentinfo">
										<option value="${agentinfo.id}" <c:if test="${islandOrder.orderCompany == agentinfo.id }">selected="selected"</c:if> >${agentinfo.agentName}</option>
									</c:forEach>
								</select>
							</div>
							<div class="mod_information_d2" id="nonChannel" <c:if test="${islandOrder.orderCompany != -1 }">style="display:none"</c:if> >
								<label class="price_sale_house_label02"><span class="xing">*</span>非签约渠道名称：</label> 
								<input class="valid" type="text" name="orderCompanyName" id="orderCompanyName"
									   <c:if test="${islandOrder.orderCompany == -1}">value="${islandOrder.orderCompanyName }"</c:if> />
							</div>
						</div>
					</div>
					<p class="ydbz_qdmc" style="padding: 12px 0px;"></p>
					
					<!--签约渠道-->
						<div id="signChannelList" <c:if test="${islandOrder.orderCompany == -1 }">style="display:none;"</c:if> >
					 	<c:forEach items="${orderContactsList }" var="orderContacts" varStatus="status">
							<ul class="ydbz_qd min-height" id="orderpersonMes" name="orderpersonMes">
								<li><label><span class="xing">*</span>渠道联系人<font>${status.count}</font>：</label>
									<span name="channelConcat"></span>
									<input type="hidden" name="contactsId" id="contactsId" value="${orderContacts.id }" />
									<input type="hidden" name="contactsNameValue" value="${orderContacts.contactsName}" id="contactsNameValue${status.index}"> 
<%-- 										<input type="text" id="contactsName" name="contactsName" value="${orderContacts.contactsName }"  onafterpaste="this.value=this.value.replaceSpecialChars()" onkeyup="this.value=this.value.replaceSpecialChars()" class="required valid"  maxlength="10" /> --%>
								</li>
								<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
									<%--<input type="text"	id="contactsTel" name="contactsTel" value="${orderContacts.contactsTel }" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"	onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" class="required valid"  maxlength="15" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> />--%>
									<input type="text"	id="contactsTel" name="contactsTel" value="${orderContacts.contactsTel }" class="required valid" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> />
									<div class="zksx boxCloseOnAdd"	onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
<%-- 										<span class="ydbz_x yd1AddPeople" <c:if test="${fn:contains(isAllowAddAgentInfo,'0')}">style="display:none;"</c:if>>添加联系人</span>  --%>
										<c:if test="${status.first == true }"><span class="ydbz_x yd1AddPeople" <c:if
												test="${fn:contains(isAllowAddAgentInfo,'0')}">style="display:none;"</c:if>>添加联系人</span></c:if>
										<c:if test="${status.first == false }"><span class="ydbz_x yd1DelPeople gray" data="${orderContacts.id }">删除联系人</span></c:if>
								</li>
								<li flag="messageDiv" class="ydbz_qd_close">
									<ul>
										<li><label>固定电话：</label> <input type="text" name="contactsTixedTel" id="contactsTixedTel" value="${orderContacts.contactsTixedTel }"
											 <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>渠道地址：</label> <input type="text" name="contactsAddress" id="contactsAddress" onblur="updataInputTitle(this);"
																		title="${orderContacts.contactsAddress}" value="${orderContacts.contactsAddress }"
																		<c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>传真：</label> <input type="text"  name="contactsFax" id="contactsFax" value="${orderContacts.contactsFax }" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>QQ：</label> <input type="text" name="contactsQQ" id="contactsQQ" value="${orderContacts.contactsQQ }"   <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>Email：</label> <input type="text"  name="contactsEmail" id="contactsEmail" value="${orderContacts.contactsEmail }"  <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>渠道邮编：</label> <input type="text"  name="contactsZipCode" id="contactsZipCode" value="${orderContacts.contactsZipCode }"  <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>其他：</label> <input type="text"  name="remark" id="remark" value="${orderContacts.remark }"
																	  onblur="updataInputTitle(this);" title="${orderContacts.remark }"/></li>
									</ul>
								</li>
							</ul>
							</c:forEach>
						</div>		
						
					<!--非签约渠道-->
					<div id="nonChannelList"  <c:if test="${islandOrder.orderCompany != -1 }">style="display:none;"</c:if> >
					<c:forEach items="${orderContactsListNon }" var="orderContacts" varStatus="status">
						<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
							<li><label><span class="xing">*</span>渠道联系人<font>${status.count}</font>：</label> 
								<%--<input type="text" id="contactsName" name="contactsName"  value="${orderContacts.contactsName }"--%>
									   <%--onafterpaste="this.value=this.value.replaceSpecialChars()" onkeyup="this.value=this.value.replaceSpecialChars()" class="required valid"  maxlength="10" />--%>
								<input type="text" id="contactsName" name="contactsName"  value="${orderContacts.contactsName }" class="required valid"  />
								<input type="hidden" name="contactsId" id="contactsId" value="${orderContacts.id }" />
							</li>
							<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
								<%--<input type="text" name="contactsTel" value="${orderContacts.contactsTel }" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="15" />--%>
								<input type="text" name="contactsTel" value="${orderContacts.contactsTel }" />
								<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
								<c:if test="${status.first == true }"><span class="ydbz_x yd1AddPeople" <c:if test="${fn:contains(isAllowAddAgentInfo,'0')}">style="display:none;"</c:if>>添加联系人</span></c:if>
								<c:if test="${status.first == false }"><span class="ydbz_x yd1DelPeople gray" data="${orderContacts.id }">删除联系人</span></c:if>
							</li>
							<li flag="messageDiv" class="ydbz_qd_close">
								<ul>
									<li><label>固定电话：</label><input type="text" name="contactsTixedTel" value="${orderContacts.contactsTixedTel }" /></li>
									<li><label>渠道地址：</label> <input  type="text" name="contactsAddress" value="${orderContacts.contactsAddress }"
																	 onblur="updataInputTitle(this);"  title="${orderContacts.contactsAddress }"/></li>
									<li><label>传真：</label> <input  type="text" name="contactsFax" value="${orderContacts.contactsFax }" /></li>
									<li><label>QQ：</label> <input  type="text" name="contactsQQ" value="${orderContacts.contactsQQ }" /></li>
									<li><label>Email：</label> <input type="text" name="contactsEmail" value="${orderContacts.contactsEmail }"/></li>
									<li><label>渠道邮编：</label> <input type="text" name="contactsZipCode"  value="${orderContacts.contactsZipCode }"/></li>
									<li><label>其他：</label> <input type="text" name="remark" value="${orderContacts.remark }" onblur="updataInputTitle(this);"
																  title="${orderContacts.remark }"/></li>
								</ul>
							</li>
						</ul>
					</c:forEach>
					</div>

				 	<!-- ***重写上述代码*** -->
						<!--签约渠道-->
<!--  			 
						<div id="signChannelList">
							<ul class="ydbz_qd min-height" id="orderpersonMesyes" name="orderpersonMesyes">
								<li><label><span class="xing">*</span>渠道联系人<font>1</font>：</label>
									<span name="channelConcat"></span>
										<input type="hidden" name="contactsId" id="contactsId" value="" />
								</li>
								<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
									<input type="text" name="contactsTel" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" class="valid" id="orderPersonPhoneNum" maxlength="15" />
									<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
									<span class="ydbz_x yd1AddPeople" <c:if test="${fn:contains(isAllowAddAgentInfo,'0')}">style="display:none;"</c:if>>添加联系人</span>
								</li>
								<li flag="messageDiv" class="ydbz_qd_close">
									<ul>
										<li><label>固定电话：</label> <input type="text"  name="contactsTixedTel" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>渠道地址：</label> <input type="text"  name="contactsAddress" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>传真：</label> <input type="text"  name="contactsFax" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>QQ：</label> <input type="text"  name="contactsQQ" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>Email：</label> <input type="text"  name="contactsEmail" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>渠道邮编：</label> <input type="text"  name="contactsZipCode" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> /></li>
										<li><label>其他：</label> <input type="text" name="remark" /></li>
									</ul>
								</li>
							</ul>
						</div>
	-->
						<!--非签约渠道-->
	<!-- 					
						<div id="nonChannelList" style="display:none;">
							<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
								<li><label><span class="xing">*</span>渠道联系人<font>1</font>：</label> 
									<input type="text" name="contactsName" onkeyup="this.value=this.value.replaceSpecialChars()" onafterpaste="this.value=this.value.replaceSpecialChars()" maxlength="10"/>
								</li>
								<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
									<input type="text" name="contactsTel" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="15" />
									<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
									<span class="ydbz_x yd1AddPeople" <c:if test="${fn:contains(isAllowAddAgentInfo,'0')}">style="display:none;"</c:if>>添加联系人</span>
								</li>
								<li flag="messageDiv" class="ydbz_qd_close">
									<ul>
										<li>
											<label>固定电话：</label>
											<input type="text" name="contactsTixedTel" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" />
										</li>
										<li><label>渠道地址：</label> <input maxlength="" type="text" name="contactsAddress" /></li>
										<li><label>传真：</label> <input maxlength="" type="text" name="contactsFax" /></li>
										<li><label>QQ：</label> <input maxlength="" type="text" name="contactsQQ" /></li>
										<li><label>Email：</label> <input maxlength="" type="text" name="contactsEmail" /></li>
										<li><label>渠道邮编：</label> <input maxlength="" type="text" name="contactsZipCode" /></li>
										<li><label>其他：</label> <input maxlength="" type="text" name="remark" /></li>
									</ul>
								</li>
							</ul>
						</div>
	-->				
					
					
				</form>
			</div>

			<div class="ydbz_tit"><span class="ydExpand" data-target="#costTable"></span>费用及人数</div>
			<div id="costTable">
				<table id="moneyAndPeopleTab" class="table activitylist_bodyer_table_new contentTable_preventive">
					<thead>
						<tr>
							<th width="12%">舱位等级</th>
							<th width="13%">游客类型</th>
							<th width="25%">同行价/人</th>
							<th width="25%"><span class="xing">*</span>人数</th>
							<th width="25%">小计</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${groupPrices }" var="groupPrice">
							<tr id="${groupPrice.uuid}" class="groupPrices_tr">
								<td class="tc spaceLevelStr">
								${fns:getDictLabel(groupPrice.spaceLevel, 'spaceGrade_Type', '-')}
								
								<c:choose>
									<c:when test="${empty groupPrice.spaceLevel || groupPrice.spaceLevel == ''}">										
										<input type="hidden" value="${fns:getDictLabel(groupPrice.spaceLevel, 'spaceGrade_Type', '-')}" id="spaceLevelName-9999">
									</c:when>
									<c:otherwise>
										<input type="hidden" value="${fns:getDictLabel(groupPrice.spaceLevel, 'spaceGrade_Type', '-')}" id="spaceLevelName${groupPrice.spaceLevel }">
									</c:otherwise>
								</c:choose>
								</td>
								<td class="tc">
									<span class="personType" name="OrderTravelerTypeName">
									<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.travelerType}"/>
									</span>
									<input type="hidden" name="groupPriceUuid" id="groupPriceUuid" value="${groupPrice.uuid}" />
									<input type="hidden" name="islandOrderPrice_travelerType" value="${groupPrice.travelerType }">
									<input type="hidden" name="islandOrderPrice_spaceLevel" value="${groupPrice.spaceLevel }">
								</td>
								<td class="tc">
									<span><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/><fmt:formatNumber  type="currency" pattern="##0.00" value="${groupPrice.price }" /></span>
								</td>
								<!-- mod by jyang -->
								<td class="tc">
									<c:choose>
										<c:when test="${groupPrice.activityIslandGroupAirline.remNumber>0}">
											<c:choose>
												<c:when test="${empty groupPrice.spaceLevel || groupPrice.spaceLevel == ''}">												
													<input type="text" class="price_sale_house_w100" name="orderPersonNum" id="orderPersonNum-9999${groupPrice.travelerType }" value="${groupPrice.num }" onchange="controlNumByRem(this, -9999 )" />
												</c:when>
												<c:otherwise>												
													<input type="text" class="price_sale_house_w100" name="orderPersonNum" id="orderPersonNum${groupPrice.spaceLevel}${groupPrice.travelerType }" value="${groupPrice.num }" onchange="controlNumByRem(this, ${groupPrice.spaceLevel })" />
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<input type="text" class="price_sale_house_w100" readonly="readonly" value="已无余位"/>
											<input type="hidden" class="price_sale_house_w100" name="orderPersonNum" id="orderPersonNum${groupPrice.spaceLevel}${groupPrice.travelerType }" value="${groupPrice.num }"/>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${empty groupPrice.spaceLevel || groupPrice.spaceLevel == ''}">
											<input type="hidden" value="${groupPrice.activityIslandGroupAirline.remNumber}" id="remNumber-9999" >
										</c:when>
										<c:otherwise>
											<input type="hidden" value="${groupPrice.activityIslandGroupAirline.remNumber}" id="remNumber${groupPrice.spaceLevel }" >
										</c:otherwise>
									</c:choose>
								</td>
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
									<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityIslandGroup.currencyId }"/>&nbsp;${activityIslandGroup.singlePrice }
								</span>
								<span class="price_sale_houser_25" style="margin-left:200px;"><label>需交定金：</label>
									<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityIslandGroup.frontMoneyCurrencyId }"/>&nbsp;${activityIslandGroup.frontMoney }
								</span>
							</td>
						</tr>
					</tbody>
				</table>

				<div class="activitylist_bodyer_right_team_co1" style="width: 150px;">
					 <label>预报名间数：</label>${islandOrder.forecaseReportRoomNum }&nbsp;间
				</div>
				<div class="activitylist_bodyer_right_team_co1" style="width: 320px;">
					<strong><div class="activitylist_team_co3_text" style="width:130px; font-weight:bold;">酒店扣减间数：</div></strong> 
					控房<input id="subControlNum" name="subControlNum" type="text" data-type="number" class="inputTxt" readonly="readonly" value="${islandOrder.subControlNum }"/>间
					<a id="kfSel" style="position:relative;">选择
					   <div style="display: none; top: 30px; left: -20px;"	class="pop_inner_outer">
							<div class="confirm_inner_outer_sel">
								<span class="mr25"> <input 	class="redio_martop_4 procurement" data-text="內采" type="checkbox" checked="checked"  /> 內采</span> 
								<span class="mr25"> <input class="redio_martop_4 procurement" data-text="外采" type="checkbox" /> 外采</span>
							</div>
							<table class="table  activitylist_bodyer_table_new" id="controlDetail_table">
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
												<td class="tc font_c66 new_hotel_p_table2_tdf">
													<input type="hidden" name="hotelControlDetailUuid" id="hotelControlDetailUuid" value="${hotelControlDetail.hotelControlDetailUuid }" />
													<input type="hidden" name="islandOrderControlDetailUuid" id="islandOrderControlDetailUuid" value="${hotelControlDetail.islandOrderControlDetailUuid }" />
													<p><fmt:formatDate value="${hotelControlDetail.inDate }" pattern="yyyy.MM.dd"/></p>
												</td>
												<td class="tc font_c66 ">
													<c:forEach items="${hotelControlDetail.rooms }" var="room">
														<p><trekiz:autoId2Name4Class  classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.roomUuid}"/>*${room.night }</p>
													</c:forEach>
												</td>
												<td class="tc font_c66">
													<trekiz:defineDict name="hotelMeals" type="hotel_meal_type" defaultValue="${hotelControlDetail.hotelMeal}" readonly="true" />
												</td>
												<td class="tc font_c66">
													<trekiz:defineDict name="islands_way" type="islands_way" defaultValue="${hotelControlDetail.islandWay}" readonly="true" />
												</td>
												<td class="tc font_c66">
													${hotelControlDetail.stock - hotelControlDetail.sellStock }/${hotelControlDetail.stock }
												</td>
												<td class="tc font_c66">${hotelControlDetail.groundSupplier }</td>
												<td class="tc font_c66">内采</td>
												<td class="tc"><input data-type="number" name="number" id="number" data-min="0" class="inputTxt w50_30 spread " type="text" value="${hotelControlDetail.islandOrderControlDetailNumber }" /></td>
											</tr>
										</c:if>
									</c:forEach>
									<c:forEach items="${hotelControlDetailList }" var="hotelControlDetail">
										<c:if test="${hotelControlDetail.purchaseType == 1 }">
											<tr style="display: none;" data-type="外采">
												<td class="tc font_c66 new_hotel_p_table2_tdf">
													<input type="hidden" name="hotelControlDetailUuid" id="hotelControlDetailUuid" value="${hotelControlDetail.hotelControlDetailUuid }" />
													<input type="hidden" name="islandOrderControlDetailUuid" id="islandOrderControlDetailUuid" value="${hotelControlDetail.islandOrderControlDetailUuid }" />
													<p><fmt:formatDate value="${hotelControlDetail.inDate }" pattern="yyyy.MM.dd"/></p>
												</td>
												<td class="tc font_c66 ">
													<c:forEach items="${hotelControlDetail.rooms }" var="room">
														<p><trekiz:autoId2Name4Class  classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.roomUuid}"/>*${room.night }</p>
													</c:forEach>
												</td>
												<td class="tc font_c66">
													<trekiz:defineDict name="hotelMeals" type="hotel_meal_type" defaultValue="${hotelControlDetail.hotelMeal}" readonly="true" />
												</td>
												<td class="tc font_c66">
													<trekiz:defineDict name="islands_way" type="islands_way" defaultValue="${hotelControlDetail.islandWay}" readonly="true" />
												</td>
												<td class="tc font_c66">
													${hotelControlDetail.stock - hotelControlDetail.sellStock }/${hotelControlDetail.stock }
												</td>
												<td class="tc font_c66">${hotelControlDetail.groundSupplier }</td>
												<td class="tc font_c66">外采</td>
												<td class="tc"><input data-type="number" name="number" id="number" data-min="0" class="inputTxt w50_30 spread " type="text" value="${hotelControlDetail.islandOrderControlDetailNumber }"  /></td>
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
					<input type="text" id="subUnControlNum" name="subUnControlNum"  value="${islandOrder.subUnControlNum }" data-type="number"  class="inputTxt" /> 间
				</div>
				<ul class="ydbz_qd_02"><li><label>合计：</label> <span id="hotelRoomTotalNumber"></span>间</li></ul>
			</div>
			<!-- 预报名票数 -->
			<div class="activitylist_bodyer_right_team_co1"  style="width:150px;">
				<label>预报名票数：</label>&nbsp;${islandOrder.forecaseReportTicketNum }张
			</div>
			<div class="activitylist_bodyer_right_team_co1" style="width:320px;">
				<strong><div class="activitylist_team_co3_text" style="width:130px; font-weight:bold;">机票扣减张数：</div></strong> 控票
				<input id="subControlTicketNum" name="subControlTicketNum" value="${islandOrder.subControlTicketNum }"  type="text" data-type="number" class="inputTxt" />张
			</div>
			<div class="activitylist_bodyer_right_team_co1"  style="width:320px;">
				<div class="activitylist_team_co3_text" style="width: 100px; font-weight: normal;">非控票</div>
				<input type="text" id="subUnControlTicketNum" name="subUnControlTicketNum" value="${islandOrder.subUnControlTicketNum }" data-type="number" class="inputTxt"  /> 张
			</div>
			<ul class="ydbz_qd_02">
				<li>
					<label>合计：</label>
					<span id="ticketTotalNumber">0</span>张
				</li>
			</ul>
		</div>
			
	<!--费用调整开始-->
	<div class="ydbz_tit"><span class="ydExpand" data-target="#adjustCost"></span>费用调整</div>
	<form 	class=" form-search" id="adjustCost" novalidate="novalidate">
				<div class="mod_information_dzhan" id="dddddStepDiv">
					<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow: hidden;">
						<table class="contentTable_preventive table_padings" id="add_other_charges_table">
							<c:forEach items="${islandOrderPriceList }" var="islandOrderPrice" varStatus="status" >
								<!-- 指定类型费用展示 -->
								<c:if test="${islandOrderPrice.priceType != 4 && islandOrderPrice.priceType != 1}">
									<tr>
										<td width="100" class="tr">
											${islandOrderPrice.priceTypeStr }金额：
											<input type="hidden" name="orderPriceUuid" id="orderPriceUuid" value="${islandOrderPrice.uuid }">
											<input type="hidden" name="orderPriceId" id="orderPriceId" value="${islandOrderPrice.id }">
											<input type="hidden" name="priceType" id="priceType" value="${islandOrderPrice.priceType }">
										</td>
										<td width="280" class="tl">
											<label> 
												<select class="w80" name="currencyId" id="currencyId">
													<c:forEach items="${currencyList }" var="currency">
														<option value="${currency.id }" <c:if test="${islandOrderPrice.currencyId == currency.id }">selected="selected"</c:if> data-currency="${currency.currencyMark}">${currency.currencyName}</option>
													</c:forEach>
												</select> 
											</label> 
											<input type="text" data-type="float" id="orderPrice" name="orderPrice" class="price_sale_house_w93 <c:if test="${islandOrderPrice.priceType == 3 }">price</c:if>" value="${islandOrderPrice.price }"  />
										</td>
										<td width="180" class="tr">备注：</td>
										<td colspan="3">
											<span class="tl"> <input type="text" id="orderPriceRemark" name="orderPriceRemark" value="${islandOrderPrice.remark }"  class="price_sale_house_w300"  /></span>
											<span class="padr10"></span>
											<input id="addOtherCharges" value="增加其他费用" style="width:auto;" class="btn btn-primary" type="button">
										</td>
									</tr>
								</c:if>
								<!-- 其他费用展示 -->
								<c:if test="${islandOrderPrice.priceType == 4 || islandOrderPrice.priceType == '4' }">
									<tr>
										<td class="tr">金额名称：</td>
										<td>
											<input type="hidden" name="orderPriceUuid" id="orderPriceUuid" value="${islandOrderPrice.uuid }">
											<input type="hidden" name="orderPriceId" id="orderPriceId" value="${islandOrderPrice.id }">
											<input type="hidden" name="priceType" id="priceType" value="4">
											<span class="tl"> <input type="text" id="priceName" name="priceName" class="price_sale_house_w93" value="${islandOrderPrice.priceName }"  maxlength="" /></span>
										</td>
										<td class="tr tr_other_u">
											<input  type="radio" name="other_u_${status.index }" id="u138_input" class="dis_inlineblock"  value="1"  data-label="增加" <c:if test="${islandOrderPrice.price >= 0 }">checked="checked"</c:if> /><label for="u138_input">增加</label> 
											<input type="radio" name="other_u_${status.index }"  id="u138_input2" value="0" data-label="减少"  <c:if test="${islandOrderPrice.price < 0 }">checked="checked"</c:if> /><label for="u140_input">减少：</label>
										</td>
										<td class="tl">
											<label> 
												<select class="w80" name="currencyId" id="currencyId">
													<c:forEach items="${currencyList }" var="currency">
														<option value="${currency.id }" <c:if test="${islandOrderPrice.currencyId == currency.id }">selected="selected"</c:if> data-currency="${currency.currencyMark}">${currency.currencyName}</option>
													</c:forEach>
												</select>
											</label>
											<input type="text" id="orderPrice" name="orderPrice" value="${islandOrderPrice.price < 0 ? -islandOrderPrice.price : islandOrderPrice.price}"  data-type="float" class="price_sale_house_w93 price" />
										</td>
										<td class="tr">备注：</td>
										<td class="tl">
											<input type="text" class="price_sale_house_w93" value="${islandOrderPrice.remark }" id="orderPriceRemark" name="orderPriceRemark"/> 
											<span class="padr10"></span> 
											<i class="price_sale_house_02 delOtherCharges" data="${islandOrderPrice.uuid }"></i>
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
	<div class="ydbz_tit"><span class="ydExpand" data-target="#costSettlement"></span>费用结算</div>
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
						<div class="mod_information_d2 " style="width: 40%">
							<label>未收金额：</label><span class="green unReceipted"><%-- ${noPayMoneyStr } --%></span>
						</div>
					</div>
				</div>
			</div>
	<!--费用结算结束-->
	<!--旅客信息开始-->
	<div class="ydbz_tit"><span class="ydExpand" data-target="#passengerInfo"></span>游客信息</div>
	<div id="passengerInfo">
				<input id="addpassengerInfo" value="添加游客信息" class="btn btn-primary" type="button" />
				<c:if test="${travelerList != null }">
				<table id="passengerInfoTable" class="table activitylist_bodyer_table_new contentTable_preventive" 	style="min-width: 1600px;">
					<thead>
						<tr>
							<th width="3%">序号</th>
							<th width="6%"><span class="xing">*</span>姓名</th>
							<th width="3%">英文姓名</th>
							<th width="3%">舱位等级</th>
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
						
						<tr class="passenger_info_tr">
							<td class="tc">${status.index + 1 }</td>
							<td class="tc">
								<input type="hidden" name="travelerUuid" id="travelerUuid" value="${traveler.uuid }" />
								<input type="text" class="price_sale_house_w93" id="travelerName" name="travelerName"  value="${traveler.name }"/>
							</td>
							<td class="tc"><input type="text" class="price_sale_house_w93" name="nameSpell" id="nameSpell" value="${traveler.nameSpell }"/></td>
							<td class="tc">
								<select name="spaceLevelSelect" id="spaceLevelSelect" class="w80 display_inline">
									<c:forEach items="${spaceLevelList }" var="spaceLevel">
										<option value="${spaceLevel.spaceLevel }" <c:if test="${spaceLevel.spaceLevel == traveler.spaceLevel}">selected="selected"</c:if>>${spaceLevel.spaceLevelStr }</option>
									</c:forEach>
								</select>
							</td>
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
								<c:forEach items="${traveler.islandTravelervisaList }" var="travelerVisa">
									<p>
										<input type="hidden" name="travelerVisaUuid" id="travelerVisaUuid" value="${travelerVisa.uuid }">
										<select name="countrySelect" id="countrySelect" class="w40b">
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
								<c:forEach items="${traveler.islandTravelerPapersTypeList }" var="travelerPapers">
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
								<c:forEach items="${traveler.islandMoneyAmountList }" var="moneyAmount">
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
										<c:forEach items="${traveler.islandTravelerFilesList}" var="travelerFiles">
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
						<textarea style="width: 786px; height: 83px;" name="islandOrderRemark" id="islandOrderRemark" maxlength="200">${islandOrder.remark}</textarea>
					</p>
				</div>
			</div>
	<div class="release_next_add">
		<input value="保存" onclick="jbox_finance_change_add_records();" class="btn btn-primary" type="button" /> 
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
				<input type="hidden" name="travelerUuid" id="travelerUuid" value="" />
				<input type="text" class="price_sale_house_w93" id="travelerName" name="travelerName"  value=""/></td>
			<td class="tc"><input type="text" class="price_sale_house_w93" name="nameSpell" id="nameSpell" value=""/></td>
			<td class="tc">
				<select name="spaceLevelSelect" id="spaceLevelSelect" class="w80 display_inline">
					<c:forEach items="${spaceLevelList }" var="spaceLevel">
						<option value="${spaceLevel.spaceLevel }" >${spaceLevel.spaceLevelStr }</option>
					</c:forEach>
				</select>
			</td>
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