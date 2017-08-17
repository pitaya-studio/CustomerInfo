<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta>
    <c:if test="${queryType == '1'}">
        <title>订单-销售机票订单修改</title>
    </c:if>
    <c:if test="${queryType == '2'}">
        <title>订单-计调机票订单修改</title>
    </c:if>
    <!-- 页面左边和上边的装饰 -->
    <meta name="decorator" content="wholesaler" />
    <!-- 静态资源 -->
    <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
    <script src="${ctxStatic}/modules/order/airticket/airticketOrderMoidfy.js" type="text/javascript"></script>
    <style type="text/css">
        input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
        input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
            cursor:auto;
            background:transparent;
            border:0px;
            box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
        }
        .mod_information_dzhan{
        	width:100%;
        }
    </style>
    
<script type="text/javascript">
    var dataArrayStr2 = '${contactArray}';
	var dataStr = ${contactsJsonStr};
	var dataArrayStr = '${contactArray }';  //conta转换的jsonArray
	var dataArray = eval(dataArrayStr);
	var agentinfoId = ${agentinfo.id };
	var address = '${address }';
	
$(function(){
	var allowModifyAgentInfo = $("#orderContact_modifiability").val();
    if(!allowModifyAgentInfo){
		allowModifyAgentInfo = 0;
	}
	var allowAddAgentInfo = $("#orderContact_addibility").val();
	if(!allowAddAgentInfo){
		allowAddAgentInfo = 0;
	}
		//初始加载时，订单的联系人
        var orderContactsList = eval(${orderContactsListJsonArray });
        var orderContactsNum = orderContactsList.length;    
        if(agentinfoId != -1){        
		    $('[name="channelConcat"]').combox({datas:dataStr,
		        //回调函数,可在此函数中获取选择的联系人的uuid,但只对原始的联系人框有效,对于新增的联系人在第28行获取选中项信息
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
        }    
	    //过滤特殊字符
	    $(document).on('keyup','[name="contactsName"] input',function(){
	        $(this).val(replaceSpecialChars.apply($(this).val()));
	    });
	    $(document).on('afterpaste','[name="contactsName"] input',function(){
	        $(this).val(replaceSpecialChars.apply($(this).val()));
	    });
	    
	    
// 	    for(var i=1; i<orderContactsList.length; i++){
// 		    var js = {};
// 		    比如属性名为name
// 		    orderContactsList[i];
// 		    addAgentContactNew($("span[name=addContactButton]"));
// 		}		
		//初始化时为ordercontact联系人填充信息
		if(agentinfoId != -1){
		    $("#ordercontact").find("ul[name=orderpersonMes]").each(function(index, element){
		    	initialContactInfo(element, orderContactsList[index]);
		    });
		}
	    //如果不是可输入修改，disabled下拉和除了“其他”之外的input
	    if(allowModifyAgentInfo == 0 && agentinfoId != -1){
	    	//$("input[name=contactsName]").attr("disabled", true);
	    	// 什么时候联系人是不可修改的。。。
		    //setContactInfoReadonly();
	    }
	    //如果不是可添加，隐藏掉添加联系人按钮
	    if(allowAddAgentInfo == 0){
	    	$("span[name=addContactButton]").hide();
	    }
	    //初始化时给联系人添加序号
	    resortContacts();

        //特殊需求，当focus时去掉文字提示
        $("textarea[name=comments]").focusin(function(){
            $(this).removeAttr("placeholder");
        }).focusout(function(){
            $(this).attr("placeholder","最多可输入500字");
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
	var $mesDom = $("ul[name=orderpersonMes]");
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
 * 获取当前渠道的所有联系人信息
 */
function getAllContactsByAgentId(obj){
	contactNo = 1;
	var tempAgentId = $(obj).val().split(",")[0];
	$.ajax({
		type : "POST",
		url : "../../orderCommon/manage/getAllAgentContactInfo",
		data : {agentId : tempAgentId},
		success : function(myData) {
			//删除其余联系人ul
			dataArray = eval(myData);
			address = dataArray[0].agentAddressFull;
			$("ul[name=orderpersonMes]:not(:first)").remove();
		    //移除span的内容
		    var $span = $('span[name="channelConcat"]');
		    $span.after('<span name="channelConcat"></span>');
		    $span.remove();
		    //变更下拉列表内容
			$('span[name="channelConcat"]').combox({datas:eval(myData),
                //点击
				onSelect:function(obj){
                    //选择联系人填充其他信息
					for(var i=0; i<eval(myData).length; i++){
						var tempData = eval(myData)[i];
						if(tempData.uuid == $(obj).find('a').attr('uuid')){
		            		fillContactInfo(obj, tempData);
			        	}
					}
		        }
		    });
            //给第一联系人填充信息
		    fillContactInfo($("ul[name=orderpersonMes]:first"), eval(myData)[0]);
		}		
	});
}

/**
 * 修改页面初始加载所有的订单联系人，给他们添加人数序号
 */
function resortContacts(){
	$("ul[name=orderpersonMes]").each(function (index, element) {
        $(element).children("li").find("span[name=contactNo]").text(index + 1);
        if(index > 1){
		    contactNo++;
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
//         firstContactName : tempFirstContactName,
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
        $border.append('<input type="text" maxlength="45" name="contactsName" class="'+options.inputCss+'"/>');
//         $border.append('<input type="text" name="contactsName" value="' + options.firstContactName + '" class="'+options.inputCss+'"/>');
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
        //移出收起
        $border.delegate('ul', 'mouseleave', function () {
            $(this).hide();
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
 * 添加渠道联系人(新有控件)
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
    var $border = $newUl.find('[name="contactsName"]');
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
        for(var i=0; i<dataArray.length; i++){
			var tempData = dataArray[i];
			if(tempData.uuid == $(this).find('a').attr('uuid')){
	       		fillContactInfo(this, tempData);
	       	}
		}
    });
    resortContacts();
}

/**
 * 删除联系人
 */
function yd1DelPeople(obj) {
	contactNo--;
    $(obj).parent().parent().remove();
    //重置联系人序号
    //if ($('#orderpersonss option:selected').val() == '1'|| $(obj).parents().find('#orderpersonss').length==0) {
    $("ul[name=orderpersonMes]").each(function (index, element) {
        $(element).children("li").find("span[name=contactNo]").text(index + 1);
    });
}
	
</script>
</head>
<body>
<!-- tab -->
<page:applyDecorator name="airticket_order_modify">
</page:applyDecorator>
<!--右侧内容部分开始-->
<div class="ydbzbox fs">
    <!-- 页面所有隐藏信息 -->
    <div class="tr">
    	<input type="hidden" value="${fns:getUser().company.uuid }" id="companyUuid" name="companyUuid">
        <input type="hidden" id="placeHolderType" value="${placeHolderType}">                       <!-- 占位或切位 -->
        <input type="hidden" id="freePosition" value="${freePosition}">                             <!-- 余位 -->
        <input type="hidden" id="leftpayReservePosition" value="${leftpayReservePosition}">         <!-- 切位 -->
        <input type="hidden" id="personNum" value="${personNum}">                                   <!-- 订单总人数 -->
        <input type="hidden" id="newPersonNum" value="${personNum}">                                <!-- 订单新的总人数-->
        <input type="hidden" id="maxPeopleCount" value="${maxPeopleCount}"/>   
        <input type="hidden" id="maxChildrenCount" value="${maxChildrenCount}"/>   
        <input type="hidden" id="currentChildrenCount" value="${currentChildrenCount}"/>   
        <input type="hidden" id="currentPeopleCount" value="${currentPeopleCount}"/>                        <!-- 最高特殊人群数-->
        <input type="hidden" id="texamt" value="${activity.istax ==0 ? 0 : activity.taxamt }"/>     <!-- 税费-->
        <input type="hidden" id="crj" value="${activity.settlementAdultPrice}"/>                    <!-- 成人同行价-->
        <input type="hidden" id="etj" value="${activity.settlementcChildPrice}"/>                   <!-- 儿童同行价-->
        <input type="hidden" id="tsj" value="${activity.settlementSpecialPrice}"/>                  <!-- 特殊人群同行价-->
        <input type="hidden" id="airticketbz" value='${airticketbz}'/>                              <!-- 机票币种-->
        <input type="hidden" id="bzJson" value='${bzJson}'/>
        <input id="orderId" type="hidden" value="${orderDetailInfoMap.orderId }">
        <input id="airticketId" type="hidden" value="${orderDetailInfoMap.airticketId }">
        <input id="currencyId" type="hidden" value="${orderDetailInfoMap.currencyId }">
        <input id="queryType" type="hidden" value="${queryType }">
    </div>
    <div class="orderdetails">

        <!-- 订单信息start -->
        <div class="orderdetails_tit">
            <div class="ydbz_tit">订单信息</div>
        </div>
        <div class="orderdetails1">
            <table border="0" width="90%" style="margin-left:0;">
                <tbody>
                <tr>
                    <td class="mod_details2_d1">下单人：</td>
                    <td class="mod_details2_d2">${orderDetailInfoMap.userName }</td>
                    <td class="mod_details2_d1">销售：</td>
                    <td class="mod_details2_d2">${orderDetailInfoMap.salerName }</td>
                    <td class="mod_details2_d1">下单时间：</td>
                    <td class="mod_details2_d2"><fmt:formatDate value="${orderDetailInfoMap.orderCreateDate }" pattern="yyyy-MM-dd HH:mm"/></td>
                    <td class="mod_details2_d1">操作人：</td>
                    <td class="mod_details2_d2">${fns:getUserNameById(orderDetailInfoMap.createBy)}</td>
                </tr>
                <tr>
                    <td class="mod_details2_d1">订单编号：</td>
                    <td class="mod_details2_d2">${orderDetailInfoMap.orderNo }</td>
                    <td class="mod_details2_d1">订单团号：</td>
                    <td class="mod_details2_d2">${orderDetailInfoMap.orderGroupCode }</td>
                    <td class="mod_details2_d1">团队类型：</td>
                    <td class="mod_details2_d2">
                        <c:choose>
                            <c:when test="${orderDetailInfoMap.type == 1 }">单办</c:when>
                            <c:when test="${orderDetailInfoMap.type == 2 }">参团</c:when>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td class="mod_details2_d1">参团订单编号：</td>
                    <td class="mod_details2_d2">${porder.orderNum }</td>
                    <td class="mod_details2_d1">参团订单团号：</td>
                    <td class="mod_details2_d2">${groupNum }</td>
                </tr>
                </tbody>
            </table>
            <div class="mod_information_d7" style="width:auto;margin-left:25px"></div>
            <table border="0" width="90%" style="margin-left:0;">
                <tbody>
                <tr>
                    <td class="mod_details2_d1" style="padding-top:8px;">发票号：</td>
                    <td class="mod_details2_d2" >
                    	<c:forEach var="invoice" items="${invoices }" varStatus="varStatus">
							<c:if test="${varStatus.count > 1 }"></br></c:if>
							<span style="text-overflow:ellipsis; white-space:nowrap; overflow:hidden; padding-left:0px; width:90%; display:inline-block;" title="${invoice.invoiceNum }">
								<a target="_blank" href="${ctx}/orderInvoice/manage/viewInvoiceInfo/${invoice.uuid}/-2/7">${invoice.invoiceNum }</a>
							</span>
						</c:forEach>
                    </td>
                    <td class="mod_details2_d1" style="padding-top:8px;">收据号：</td>
                    <td class="mod_details2_d2">
                    	<c:forEach var="receipt" items="${receipts }" varStatus="varStatus">
                    		<c:if test="${varStatus.count > 1 }"></br></c:if>
							<span style="text-overflow:ellipsis; white-space:nowrap; overflow:hidden; padding-left:0px; width:90%; display:inline-block;" title="${receipt.invoiceNum }">
								<a target="_blank" href="${ctx}/receipt/limit/supplyviewrecorddetail/${receipt.uuid}/-2/7">${receipt.invoiceNum }</a>
							</span>
						</c:forEach>
                    </td>
                    <td class="mod_details2_d1"></td>
                    <td class="mod_details2_d2"></td>
                    <td class="mod_details2_d1"></td>
                    <td class="mod_details2_d2"></td>
                </tr>
                </tbody>
            </table>
        </div>
        <!-- 订单信息end -->
        
        <!-- 航段信息,出行人数start -->
        <div class="mod_information_dzhan" style="overflow:hidden;">
            <div class="mod_information_dzhan_d mod_details2_d">
                <c:choose>
                    <c:when test="${orderDetailInfoMap.airType == 2}">
                        <!--往返-->
                        <span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（往返）</span>
                        <div class="mod_information_d7"></div>
                        <table width="90%" border="0">
                            <tbody>
                            <tr>
                                <td class="mod_details2_d1">出发城市：</td>
                                <td class="mod_details2_d2">
                                        ${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
                                </td>
                                <td class="mod_details2_d1">到达城市：</td>
                                <td class="mod_details2_d2">
                                    <c:forEach items="${arrivedareas}" var="arrivedareas">
                                        <c:if test="${arrivedareas.id == orderDetailInfoMap.arrivedCity}">
                                            ${arrivedareas.name}
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td class="mod_details2_d1">预收人数：</td>
                                <td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
                            </tr>
                            </tbody>
                        </table>
                        <c:forEach items="${orderDetailInfoMap.flightInfoList}" var="flightInfo">
                            <div class="title_samil">
                                <c:choose>
                                    <c:when test="${flightInfo.orderNumber==1 }">去程：</c:when>
                                    <c:when test="${flightInfo.orderNumber==2 }">回程：</c:when>
                                </c:choose>
                            </div>
                            <table width="90%" border="0">
                                <tbody><tr>
                                    <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">${flightInfo.startAirportName }</td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">${flightInfo.endAirportName } </td>
                                </tr>
                                <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
                                </tr>
                                <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
                                </tr>
                                </tbody></table>
                        </c:forEach>
                    </c:when>
                    <c:when test="${orderDetailInfoMap.airType == 3}">
                        <!--单程-->
                        <span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（单程）</span>
                        <div class="mod_information_d7"></div>
                        <table width="90%" border="0">
                            <tbody>
                            <tr>
                                <td class="mod_details2_d1">出发城市：</td>
                                <td class="mod_details2_d2">
                                        ${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
                                </td>
                                <td class="mod_details2_d1">到达城市：</td>
                                <td class="mod_details2_d2">
                                    <c:forEach items="${arrivedareas}" var="arrivedareas">
                                        <c:if test="${arrivedareas.id == orderDetailInfoMap.arrivedCity}">
                                            ${arrivedareas.name}
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td class="mod_details2_d1">预收人数：</td>
                                <td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
                            </tr>
                            </tbody>
                        </table>
                        <c:forEach items="${orderDetailInfoMap.flightInfoList}" var="flightInfo">
                            <table width="90%" border="0">
                                <tbody><tr>
                                    <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">${flightInfo.startAirportName }</td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">${flightInfo.endAirportName } </td>
                                </tr>
                                <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
                                </tr>
                                <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
                                </tr>
                                </tbody></table>
                        </c:forEach>
                    </c:when>
                    <c:when test="${orderDetailInfoMap.airType == 1}">
                        <!--多段-->
                        <span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（多段）</span>
                        <div class="mod_information_d7"></div>
                        <table width="90%" border="0">
                            <tbody>
                            <tr>
                                <td class="mod_details2_d1">出发城市：</td>
                                <td class="mod_details2_d2">
                                        ${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
                                </td>
                                <td class="mod_details2_d1">到达城市：</td>
                                <td class="mod_details2_d2">
                                    <c:forEach items="${arrivedareas}" var="arrivedareas">
                                        <c:if test="${arrivedareas.id == orderDetailInfoMap.arrivedCity}">
                                            ${arrivedareas.name}
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td class="mod_details2_d1">预收人数：</td>
                                <td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
                            </tr>
                            </tbody>
                        </table>
                        <c:forEach items="${orderDetailInfoMap.flightInfoList }" var="flightInfo">
                            <div class="title_samil">第${flightInfo.orderNumber }段：
                                <c:choose>
                                    <c:when test="${flightInfo.ticketAreaType == 1 }">内陆</c:when>
                                    <c:when test="${flightInfo.ticketAreaType == 2 }">国际</c:when>
                                    <c:when test="${flightInfo.ticketAreaType == 3 }">内陆+国际</c:when>
                                </c:choose>
                            </div>
                            <table width="90%" border="0">
                                <tbody><tr>
                                    <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">${flightInfo.startAirportName }</td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">${flightInfo.endAirportName } </td>
                                </tr>
                                <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
                                </tr>
                                <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
                                </tr>
                                </tbody></table>
                        </c:forEach>
                    </c:when>
                </c:choose>
                <div class="mod_information_d7"></div>
                <form id="productOrderTotal" novalidate="novalidate">
                    <ul class="ydbz_dj specialPrice">
                        <li>
                            <span class="ydtips">单价</span>
                            <p>成人：<font color="#FF0000">${orderDetailInfoMap.adultPrice }</font></p>
                            <p>儿童：<font color="#FF0000">${orderDetailInfoMap.childPrice }</font></p>
                            <p>特殊人群：<font color="#FF0000">${orderDetailInfoMap.specialPrice }</font></p>
                        </li>
                        <li><span class="ydtips"> 出行人数</span>
                            <p>成人：
                                <c:if test="${airticketOrder.orderState == '7'}">
                                <input id="orderPersonNumAdult" type="text" data-id="1" value="${orderDetailInfoMap.adultNum }"
                                       onkeyup="this.value=this.value.replace(/\D/g,'')"
                                       onafterpaste="this.value=this.value.replace(/\D/g,'')"
                                       onblur="checkFreePosition(this,${orderDetailInfoMap.adultNum })" disabled="disabled"/>
                                </c:if>
                                <c:if test="${airticketOrder.orderState != '7'}">
                                    <input id="orderPersonNumAdult" type="text" data-id="1" value="${orderDetailInfoMap.adultNum }"
                                           onkeyup="this.value=this.value.replace(/\D/g,'')"
                                           onafterpaste="this.value=this.value.replace(/\D/g,'')"
                                           onblur="checkFreePosition(this,${orderDetailInfoMap.adultNum })" />
                                </c:if>
                            人</p>
                            <p>儿童：
                                <c:if test="${airticketOrder.orderState == '7'}">
                                    <input id="orderPersonNumChild" type="text" data-id="2" value="${orderDetailInfoMap.childNum }"
                                           onkeyup="this.value=this.value.replace(/\D/g,'')"
                                           onafterpaste="this.value=this.value.replace(/\D/g,'')"
                                           onblur="checkFreePosition(this,${orderDetailInfoMap.childNum })" disabled="disabled" />
                                </c:if>
                                <c:if test="${airticketOrder.orderState != '7'}">
                                    <input id="orderPersonNumChild" type="text" data-id="2" value="${orderDetailInfoMap.childNum }"
                                           onkeyup="this.value=this.value.replace(/\D/g,'')"
                                           onafterpaste="this.value=this.value.replace(/\D/g,'')"
                                           onblur="checkFreePosition(this,${orderDetailInfoMap.childNum })">
                                </c:if>
                            人</p>
                            <p>特殊人群：
                                <c:if test="${airticketOrder.orderState == '7'}">
                                    <input id="orderPersonNumSpecial" type="text" data-id="3" value="${orderDetailInfoMap.specialNum }"
                                           onkeyup="this.value=this.value.replace(/\D/g,'')"
                                           onafterpaste="this.value=this.value.replace(/\D/g,'')"
                                           onblur="checkFreePosition(this,${orderDetailInfoMap.specialNum })" disabled="disabled"/>
                                </c:if>
                                <c:if test="${airticketOrder.orderState != '7'}">
                                    <input id="orderPersonNumSpecial" type="text" data-id="3" value="${orderDetailInfoMap.specialNum }"
                                           onkeyup="this.value=this.value.replace(/\D/g,'')"
                                           onafterpaste="this.value=this.value.replace(/\D/g,'')"
                                           onblur="checkFreePosition(this,${orderDetailInfoMap.specialNum })"/>
                                </c:if>
                            人</p>
                        </li>
                        <li class="ydbz_single">
                            <span class="">税费：</span>${orderDetailInfoMap.taxamt }/人
                        </li>
                    </ul>
                </form>
            </div>
        </div>
        <!-- 航段信息,出行人数end -->

        <!-- 预订人部分start -->
        <div class="orderdetails_tit">
            <div class="ydbz_tit">预订人信息</div>
            <shiro:hasPermission name="airticketOrder:orderContact:modifiability">
				<input type="hidden" value="1" id="orderContact_modifiability">
				<c:set var="orderContact_modifiability" value="1"></c:set>
			</shiro:hasPermission>
			<shiro:hasPermission name="airticketOrder:orderContact:addibility">
				<input type="hidden" value="1" id="orderContact_addibility">
				<c:set var="orderContact_addibility" value="1"></c:set>
			</shiro:hasPermission>
			<shiro:hasPermission name="airticketOrder:agentinfo:modifiability">
				<input type="hidden" value="1" id="agentinfo_modifiability">
				<c:set var="agentinfo_modifiability" value="1"></c:set>
			</shiro:hasPermission>
			<c:if test="${queryType == '1'}">
            	<shiro:hasPermission name="airticketOrder4Sale:agentinfo:visibility">
            		<input type="hidden" value="1" id="agentinfo_visibility">
					<c:set var="agentinfo_visibility" value="1"></c:set>
            	</shiro:hasPermission>
            </c:if>
            <c:if test="${queryType == '2'}">
            	<shiro:hasPermission name="airticketOrder4Oper:agentinfo:visibility">
	            	<input type="hidden" value="1" id="agentinfo_visibility">
					<c:set var="agentinfo_visibility" value="1"></c:set>
            	</shiro:hasPermission>
            </c:if>
        </div>
        <div flag="messageDiv">
            <form id="orderpersonMesdtail">
                <p class="ydbz_qdmc">预订渠道：
                <!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
					<c:choose>
						<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and orderDetailInfoMap.agentId == -1 }">未签<input type="text" name="orderCompanyName" id="orderCompanyName" <c:if test='${agentinfo_modifiability ne 1 }'>readOnly="readOnly"</c:if> class="required" value="${orderDetailInfoMap.nagentName }"/></c:when>
						<c:when test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and orderDetailInfoMap.agentId == -1 }">
							<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
								直客
							</c:if>
							<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
								非签约渠道
							</c:if>
						<input type="text" name="orderCompanyName" id="orderCompanyName" <c:if test='${agentinfo_modifiability ne 1 }'>readOnly="readOnly"</c:if> class="required" value="${orderDetailInfoMap.nagentName }"/></c:when>
						<c:otherwise>
						<c:choose>
			            	<c:when test="${agentinfo_modifiability ne 1 }">
			            		<span>${orderDetailInfoMap.agentName }</span>
			            		<select name="agentShow" id="agentShow" <c:if test='${agentinfo_modifiability ne 1 }'>style="display: none;"</c:if> onChange="getAllContactsByAgentId(this)" >
			                        <c:if test="${not empty agentinfoList }">
			                            <c:forEach items="${agentinfoList}" var="agentinfo">
			                                <option value="${agentinfo.id }" <c:if test="${orderDetailInfoMap.agentId == agentinfo.id  }">selected="selected"</c:if> >${agentinfo.agentName }</option>
			                            </c:forEach>
			                        </c:if>
			                    </select>
			            	</c:when>
			            	<c:otherwise>
				                <select name="agentShow" id="agentShow" onChange="getAllContactsByAgentId(this)" >
			                        <c:if test="${not empty agentinfoList }">
			                            <c:forEach items="${agentinfoList}" var="agentinfo">
			                                <option value="${agentinfo.id }" <c:if test="${orderDetailInfoMap.agentId == agentinfo.id  }">selected="selected"</c:if> >${agentinfo.agentName }</option>
			                            </c:forEach>
			                        </c:if>
			                    </select>
			            	</c:otherwise>
			            </c:choose>
						</c:otherwise>
					</c:choose>
                </p>
                
                <div id="ordercontact" <c:if test="${empty agentinfo_visibility or agentinfo_visibility ne 1}">style="visibility:hidden;"</c:if> >
					<c:choose>
		              	<%-- 非签约渠道,显示普通的输入框 --%>
		              	<c:when test="${agentinfo.id eq -1}">
                 		<c:forEach items="${orderContactsSrc }" var="orderContact" varStatus="s1">
                 			<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
                    			<input type="hidden" value = "${orderDetailInfoMap.agentId}" name="agentId"/>
			                    <li>
			                        <label><span class="xing">*</span>渠道联系人<span name="contactNo"></span>：</label>
			                        <input maxlength="45" type="text" name="contactsName" value="${orderContact.contactsName }" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/>
			                    </li>
			                    <li class="ydbz_qd_lilong">
			                        <label><span class="xing">*</span>渠道联系人电话：</label>
			                        <input maxlength="20" type="text" name="contactsTel" value="${orderContact.contactsTel }" class="required"
			                               onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/>
			                        <div class="zksx modify-order boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
			                        <shiro:hasPermission name="airticketOrder:orderContact:addibility">
				                        <c:if test="${s1.count == 1 }">
							           		<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
						           		</c:if>
						           		<c:if test="${s1.count != 1 }">
						           			<span class="ydbz_x gray" name="delContactButton" onclick="yd1DelPeople(this)">删除联系人</span>
						           		</c:if>
					           		</shiro:hasPermission>
			                    </li>
			                    <li flag="messageDiv"  class="ydbz_qd_close">
			                    	<ul>			                    	
			                    	<li><label>固定电话：</label><input maxlength="20" name="contactsTixedTel" type="text" value="${orderContact.contactsTixedTel}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /></li>
			                        <li><label>渠道地址：</label><input maxlength="200" onblur="updataInputTitle(this);" title="${orderContact.contactsAddress }" name="contactsAddress" type="text" value="${orderContact.contactsAddress }"/></li>
			                        <li><label>传真：</label><input maxlength="20" name="contactsFax" type="text" value="${orderContact.contactsFax }" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
			                        <li><label>QQ：</label><input maxlength="20" name="contactsQQ" type="text" value="${orderContact.contactsQQ }" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
			                        <li><label>Email：</label><input maxlength="50" name="contactsEmail" type="text" value="${orderContact.contactsEmail }"/></li>
			                        <li><label>渠道邮编：</label><input maxlength="20" type="text" name="contactsZipCode" value="${orderContact.contactsZipCode}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
			                        <li><label>其他：</label><input maxlength="200" onblur="updataInputTitle(this);" title="${orderContact.contactsAddress }" name="remark" type="text" value="${orderContact.remark }"/></li>
			                        </ul>
			                    </li>
                			</ul>
                 		</c:forEach>
						</c:when>						
	               		<c:otherwise>
	               		<c:forEach items="${orderContactsSrc }" var="orderContact" varStatus="s1">
                 			<ul class="ydbz_qd min-height" id="orderpersonMes" name="orderpersonMes">
                    			<input type="hidden" value = "${orderContact.agentId}" name="agentId"/>
		               			<li>
			                        <label><span class="xing">*</span>渠道联系人<span name="contactNo"></span>：</label>
			                        <span name="channelConcat"></span>
			                    </li>
			                    <li class="ydbz_qd_lilong">
			                        <label><span class="xing">*</span>渠道联系人电话：</label>
			                        <input maxlength="20" type="text" name="contactsTel" value="${orderContact.contactsTel }" class="required"
			                               onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/>
			                        <div class="zksx modify-order boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
			                        <shiro:hasPermission name="airticketOrder:orderContact:addibility">
				                        <c:if test="${s1.count == 1 }">
							           		<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
						           		</c:if>
						           		<c:if test="${s1.count != 1 }">
						           			<span class="ydbz_x gray" name="delContactButton" onclick="yd1DelPeople(this)">删除联系人</span>
						           		</c:if>
					           		</shiro:hasPermission>
			                    </li>
			                    <li flag="messageDiv" class="ydbz_qd_close">
			                    	<ul>
			                    	<li><label>固定电话：</label><input maxlength="20" name="contactsTixedTel" type="text" value="${orderContact.contactsTixedTel}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /></li>
			                        <li><label>渠道地址：</label><input maxlength="200" onblur="updataInputTitle(this);" title="${orderContact.contactsAddress }" name="contactsAddress" type="text" value="${orderContact.contactsAddress }"/></li>
			                        <li><label>传真：</label><input maxlength="20" name="contactsFax" type="text" value="${orderContact.contactsFax }" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
			                        <li><label>QQ：</label><input maxlength="20" name="contactsQQ" type="text" value="${orderContact.contactsQQ }" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
			                        <li><label>Email：</label><input maxlength="50" name="contactsEmail" type="text" value="${orderContact.contactsEmail }"/></li>
			                        <li><label>渠道邮编：</label><input maxlength="20" type="text" name="contactsZipCode" value="${orderContact.contactsZipCode}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
			                        <li><label>其他：</label><input maxlength="200" onblur="updataInputTitle(this);" title="${orderContact.remark }" name="remark" type="text" value="${orderContact.remark }"/></li>
			                        </ul>
			                    </li>
                			</ul>
                 		</c:forEach>
	               		</c:otherwise>
	                </c:choose>
                </div>
            </form>
        </div>
        <!-- 预订人部分end -->

        <!-- 特殊需求部分start -->
        <div id="manageOrder_m" style="">
            <div id="contact">
                <div class="ydbz_tit" style="margin-top: 45px;">特殊需求</div>
                <div class="ydbz2_lxr" flag="messageDiv">
                    <form class="contactTable">
                        <p>
                            <label style="vertical-align:top">特殊需求：</label>
                            <!-- 销售机票可以修改特殊需求，计调机票不能修改特殊需求  queryType： 1：销售机票，2：计调机票 -->
                            <c:if test="${queryType == '1'}">
                                <textarea id="comments" name="comments" maxlength="500" placeholder="最多可输入500字" onkeyup="this.value=this.value.replaceSpecialDemand();"
                                      onafterpast="this.value=this.value.replaceSpecialDemand();">${airticketOrder.comments}</textarea>
                            </c:if>
                            <c:if test="${queryType == '2'}">
                                <span style="word-break:break-all">${airticketOrder.comments}</span>
                                <input name="comments" type="hidden" value="${airticketOrder.comments}" />
                            </c:if>
                        </p>
                    </form>
                </div>
                <div class="ydBtn-down orderPersonMsg" style="display:none;" ><span>&nbsp;</span></div>
            </div>
        </div>
        <!-- 特殊需求部分end -->
        <div id="manageOrder_new" style="">
        <!-- 游客部分start -->
        <div class="orderdetails_tit orderdetails_titpr">
            游客信息
        </div>
        <div id="traveler">
            <c:if test="${not empty travelerList }">
                <c:forEach items="${travelerList }" var="traveler">
                    <form class="travelerTable">
                        <div class="tourist">
                            <!-- 游客编号，游客结算价，游客类型，联运信息展示 -->
                            <div class="tourist-t">
                                <input id="travelerId" type="hidden" name ="travelerId" value="${traveler.id }"  class="traveler" >
                                <span class="add_seachcheck" onclick="boxCloseOnAdd(this,'1')"></span><label class="ydLable">游客<em class="travelerIndex" style="vertical-align: baseline;"></em>:</label>
                                <!-- 游客结算价信息展示 -->
                                <div class="tourist-t-off">
						            <span class="fr">
                                        <b style="font-size: 18px">同行价：</b><span class="ydFont2 showThPrice"></span>
                                        <b style="font-size: 18px">结算价：</b>
                                        <span class="ydFont2 showJsPrice"></span>
                                    </span>
                                    <span name="tName">${traveler.name }</span>
                                </div>
                                <!-- 游客类型，联运信息 -->
                                <div class="tourist-t-on">
                                    <label><input type="radio" class="traveler" name="personType" value="1" <c:if test="${traveler.personType == 1 }">checked="checked"</c:if> disabled="disabled" onClick="changePersonType(this)"/>成人</label>
                                    <label><input type="radio" class="traveler" name="personType" value="2" <c:if test="${traveler.personType == 2 }">checked="checked"</c:if> disabled="disabled" onClick="changePersonType(this)"/>儿童</label>
                                    <label><input type="radio" class="traveler" name="personType" value="3" <c:if test="${traveler.personType == 3 }">checked="checked"</c:if> disabled="disabled" onClick="changePersonType(this)"/>特殊人群</label>

                                    <div class="tourist-t-r"> 是否联运：
                                        <label><input id="intermodalNotNeed" type="radio" class="ydbz2intermodal1" name="ydbz2intermodalType" value="0"  <c:if test="${traveler.intermodalType == 0 }">checked="checked"</c:if>  onclick="ydbz2intermodalSale(this)" disabled="disabled" />不需要</label>
                                        <label><input id="intermodalNeed" type="radio" name="ydbz2intermodalType" value="1"  <c:if test="${traveler.intermodalType == 1 }">checked="checked"</c:if> onclick="ydbz2intermodalSale(this)" disabled="disabled"/>需要</label>
                                        	<c:if test="${traveler.intermodalType == 1 }">
                                            <span>
                                                <select onchange="ydbz2interselectSale(this)" name="intermodal">
                                                    <c:forEach items="${fns:getIntermodalStrategyList(activity.id) }" var="intermodalInfo">
                                                        <option id="${intermodalInfo.id }" value="${intermodalInfo.price }" currenyId="${intermodalInfo.priceCurrency.id}" currenyMark="${intermodalInfo.priceCurrency.currencyMark}" currenyName="${intermodalInfo.priceCurrency.currencyName}"<c:if test="${traveler.intermodalId == intermodalInfo.id }">selected="selected"</c:if>
                                                        >${intermodalInfo.groupPart }</option>
                                                    </c:forEach>
                                                </select>
                                          		币种：<cur><span class="intermodalCurrencyName">${fns:getCurrencyByIntermodalId(traveler.intermodalId).currencyName}</span></cur>
                                          		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联运价格：
                                          		<em><span class="intermodalMoneyAmount">${fns:getCurrencyAmountByIntermodalId(traveler.intermodalId)}</span></em>
                                          		<input type="hidden" class="intermodalCurrencyId" value="${fns:getCurrencyByIntermodalId(traveler.intermodalId).id}"/>
                                          		<!-- 保存修改之前的币种和联运价格 -->
                                          		<input type="hidden" class="orgIntermodalCurrencyName" value="${fns:getCurrencyByIntermodalId(traveler.intermodalId).currencyName}"/>
                                            	<input type="hidden" class="orgIntermodalMoneyAmount" value="${fns:getCurrencyAmountByIntermodalId(traveler.intermodalId)}"/>
                                            	<input type="hidden" class="orgIntermodalCurrencyId" value="${fns:getCurrencyByIntermodalId(traveler.intermodalId).id}"/>
                                                <input type="hidden" class="orgIntermodalId" value="${traveler.intermodalId}"/>
                                            </span>
                                            </c:if>
                                    </div>
                                </div>
                            </div>
                            <!-- 游客基本信息，预计个人返佣信息，同行价/结算价信息展示 -->
                            <div class="tourist-con" flag="messageDiv">
                                <!--游客信息左侧开始-->
                                <div class="tourist-left">
                                    <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
                                    <ul class="tourist-info1 clearfix" flag="messageDiv">
                                        <li>
                                            <label class="ydLable"><span class="xing">*</span>姓名：</label>
                                            <input type="text" maxlength="30" name="travelerName" value="${traveler.name }" class="traveler required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
                                        </li>
                                        <li>
                                            <label class="ydLable">英文／拼音：</label>
                                            <input type="text" maxlength="30" name="travelerPinyin" value="${traveler.nameSpell }" class="traveler">
                                        </li>
                                        <li>
                                            <label class="ydLable">性别：</label>
                                            <select name="travelerSex" class="selSex">
                                                <option value="1"  <c:if test="${traveler.sex == 1 }"> selected="selected"</c:if> >男</option>
                                                <option value="2"  <c:if test="${traveler.sex == 2 }"> selected="selected"</c:if> >女</option>
                                            </select>
                                        </li>
                                        <li>
                                            <label class="ydLable">国籍：</label>
                                            <select name="nationality" class="selCountry">
                                                <c:forEach items="${countryList }" var="country">
                                                    <option value="${country.id}" <c:if test="${traveler.nationality == country.id }">selected="selected
                                                    "</c:if>  >${country.countryName_cn}</option>
                                                </c:forEach>
                                            </select>
                                        </li>
                                        <li>
                                            <label class="ydLable">出生日期：</label>
                                            <input type="text" maxlength="" name="birthDay" value="${traveler.birthDay }" class="traveler dateinput"
                                                   onclick="WdatePicker()">
                                        </li>
                                        <li>
                                            <label class="ydLable">联系电话：</label>
                                            <input type="text" name="telephone" value="${traveler.telephone }" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
                                        </li>
                                        <li>
                                            <label class="ydLable">护照号：</label>
                                            <input type="text" name="passportCode" value="${traveler.passportCode }" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
                                        </li>
                                        <li>
                                            <label class="ydLable">护照有效期：</label>
                                            <input type="text" maxlength="" name="passportValidity" value='<fmt:formatDate value="${traveler.passportValidity}" pattern="yyyy-MM-dd"/>'
                                                   class="traveler dateinput" onclick="WdatePicker()">
                                        </li>
                                        <li>
                                            <label class="ydLable">身份证号：</label>
                                            <input type="text" name="idCard" value="${traveler.idCard }" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
                                        </li>
                                        <li>
                                            <label class="ydLable">护照类型：</label>
                                            <select name="passportType" class="selCountry">
                                                <option value="1"  <c:if test="${traveler.passportType == 1 }">selected</c:if> >因公护照</option>
                                                <option value="2" <c:if test="${traveler.passportType == 2 }">selected</c:if> >因私护照</option>
                                            </select>
                                        </li>
                                    </ul>
                                    <div class="ydbz_tit ydbz_tit_child">备注：<textarea name="remarks" class="textarea_long" value="${traveler.remark }">${traveler.remark }</textarea></div>
                                </div>
                                <!--游客信息左侧结束-->
                                <div class="tourist-right">
                                    <div class="bj-info">
                                        <div class="clearfix">
                                            <div class="traveler-rebatesDiv">
                                            		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
										       		<c:choose>
										      			<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
															<label class="ydLable2 ydColor1">预计个人宣传费：</label>
														</c:when>
										         		<c:otherwise>
										         			<label class="ydLable2 ydColor1">预计个人返佣：</label>
										          		</c:otherwise>
												 </c:choose>   
                                                
                                                <select name="rebatesCurrency">
                                                    <c:forEach var="bz" items="${bzList }">
                                                        <option value="${bz.currencyId }"
                                                                <c:if test="${fns:getCurrencyIdBySerialNum(traveler.rebatesMoneySerialNum) == bz.currencyId }">
                                                                    selected="selected"
                                                                </c:if>>
                                                                ${bz.currencyName }
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                                <input type="text" class="ipt-rebates" value="${fns:getMoneyAmountNUM(traveler.rebatesMoneySerialNum)}"
                                                       name="rebatesMoney"  maxlength="9" data-type="amount"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="yd-total clearfix">
                                        <div class="fr">
                                            <label class="ydLable2">同行价：</label>
                                            <span data="tradePrice" class="ydFont2"></span>
                                            <input type="hidden" class="traveler travelerSrcPrice" value="${traveler.srcPrice }"/>
                                            <input type="hidden" class="traveler travelerSrcPriceCurrencyName" value="${fns:getCurrencyNameOrFlag(traveler.srcPriceCurrency.id, 1)}"/>
                                            <input type="hidden" value="-1,0,0,0" class="traveler" name="lyPrice"/>
                                        </div>
                                        <div class="fr">
                                        	<!-- 
                                        	游客结算价回显，通过游客结算价uuid查询了映射的所有MoneyAmont对象，对这些对象进行了遍历处理。
                                        	-->
                                            <label class="ydLable2">结算价：</label>
                                            <span data="newJsPrice" class="ydFont2">
                                            	<c:if test="${not empty  fns:getMoneyAmountsBySerialNum(traveler.payPriceSerialNum) && fns:getMoneyAmountsBySerialNum(traveler.payPriceSerialNum).size() > 0}">
                                            	<c:forEach items="${fns:getMoneyAmountsBySerialNum(traveler.payPriceSerialNum)}" var="payPriceMoneyAmount" varStatus="status">
                                            		<em class="jsPriceCurrencyNameClass" style="vertical-align: baseline;">${fns:getCurrencyNameOrFlag(payPriceMoneyAmount.currencyId,1)}</em>:
	                                            	<input type="text" maxlength="8" class="jsPriceClass ipt-rebates" name="jsPrice"
                                                           value="${payPriceMoneyAmount.amount }" data="${payPriceMoneyAmount.amount }"
	                                                       onblur="changeJsPrice(this)" onafterpaste="seetlementKeyUp(this))" onkeyup="seetlementKeyUp(this)"
	                                                       data-currencyid="${payPriceMoneyAmount.currencyId }" data-type="amount">
	                                                       <c:set value="${fns:getMoneyAmountsBySerialNum(traveler.payPriceSerialNum).size() - 1}" var="moneyAmountMaxSize"></c:set>
	                                                       <c:if test="${status.index >= 0 && status.index < moneyAmountMaxSize}"><span name="addSign">+</span></c:if>
                                            	</c:forEach>
                                            	</c:if>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--保存、取消按钮开始-->
                            <div class="rightBtn"><a class="btn" name="savePeople" onclick="saveTraveler(this)">修改</a></div>
                            <!--保存、取消按钮结束-->
                        </div>
                    </form>
                </c:forEach>
            </c:if>
        </div>
        <!-- 游客部分 end -->

        <!-- 添加游客模板start -->
        <div id="travelerTemplate" style="display:none;">
            <form>
                <div class="tourist">
                    <!-- 游客编号，游客结算价，游客类型，联运信息展示 -->
                    <div class="tourist-t">
                        <a class="btn-del" style="cursor:pointer;" name="deleteTraveler" >删除</a>
                        <input type="hidden" name ="travelerId" value=""  class="traveler" >
                        <span class="add_seachcheck" onclick="boxCloseOnAdd(this,'1')"></span><label class="ydLable">游客<em class="travelerIndex" style="vertical-align: baseline;"></em>:</label>
                        <div class="tourist-t-off">
						            <span class="fr">
                                        <b style="font-size: 18px">同行价：</b><span class="ydFont2 showThPrice"></span>
                                        <b style="font-size: 18px">结算价：</b><span class="ydFont2 showJsPrice"></span>
                                    </span>
                            <span name="tName"></span>
                        </div>
                        <div class="tourist-t-on">
                            <label><input type="radio" class="traveler" name="personType" value="1" onClick="changePersonType(this)"/>成人</label>
                            <label><input type="radio" class="traveler" name="personType" value="2" onClick="changePersonType(this)"/>儿童</label>
                            <label><input type="radio" class="traveler" name="personType" value="3" onClick="changePersonType(this)"/>特殊人群</label>
                            <div class="tourist-t-r"> 是否联运：
                                <label><input type="radio" id="intermodalNotNeed" class="ydbz2intermodal1" name="ydbz2intermodalType" value="0" checked="checked" onclick="ydbz2intermodalSale(this)"/>不需要</label>
                                <label><input type="radio" id="intermodalNeed" name="ydbz2intermodalType" value="1" onclick="ydbz2intermodalSale(this)"/>需要</label>
								<span style="display:none;">
									<select id="intermodalAreaChoose" onchange="ydbz2interselectSale(this)" name="intermodal">
                                        <option id="-1" value="0" currenyMark="" currenyName="无">请选择</option>
                                        <c:forEach items="${fns:getIntermodalStrategyList(activity.id) }" var="intermodalInfo">
                                            <option id="${intermodalInfo.id }" value="${intermodalInfo.price }" currenyId="${intermodalInfo.priceCurrency.id}" currenyMark="${intermodalInfo.priceCurrency.currencyMark}" currenyName="${intermodalInfo.priceCurrency.currencyName}">${intermodalInfo.groupPart }</option>
                                        </c:forEach>
                                    </select>
									币种：<cur><span class="intermodalCurrencyName">无</span></cur>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;联运价格：<em><span class="intermodalMoneyAmount">0</span></em>
									<input type="hidden" class="intermodalCurrencyId" value=""/>
									<input type="hidden" class="orgIntermodalCurrencyName" value=""/>
                              		<input type="hidden" class="orgIntermodalMoneyAmount" value="0"/>
                              		<input type="hidden" class="orgIntermodalCurrencyId" value=""/>
                                    <input type="hidden" class="orgIntermodalId" value=""/>
								</span>
                            </div>
                        </div>
                    </div>
                    <!-- 游客基本信息，预计个人返佣信息，同行价/结算价信息展示 -->
                    <div class="tourist-con" flag="messageDiv">
                        <!--游客信息左侧开始-->
                        <div class="tourist-left">
                            <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
                            <ul class="tourist-info1 clearfix" flag="messageDiv">
                                <li>
                                    <label class="ydLable"><span class="xing">*</span>姓名：</label>
                                    <input type="text" maxlength="30" name="travelerName" class="traveler" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
                                </li>
                                <li>
                                    <label class="ydLable">英文／拼音：</label>
                                    <input type="text" maxlength="30" name="travelerPinyin" class="traveler">
                                </li>
                                <li>
                                    <label class="ydLable">性别：</label>
                                    <select name="travelerSex" class="selSex">
                                        <option value="1" selected="selected">男</option>
                                        <option value="2" >女</option>
                                    </select>
                                </li>
                                <li>
                                    <label class="ydLable">国籍：</label>
                                    <select name="nationality" class="selCountry">
                                        <c:forEach items="${countryList }" var="country">
                                            <option value="${country.id}">${country.countryName_cn}</option>
                                        </c:forEach>
                                    </select>
                                </li>
                                <li>
                                    <label class="ydLable">出生日期：</label>
                                    <input type="text" maxlength="" name="birthDay" class="traveler dateinput" onclick="WdatePicker()">
                                </li>
                                <li>
                                    <label class="ydLable">联系电话：</label>
                                    <input type="text" name="telephone" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
                                </li>

                                <li>
                                    <label class="ydLable">护照号：</label>
                                    <input type="text" name="passportCode" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
                                </li>

                                <li>
                                    <label class="ydLable">护照有效期：</label>
                                    <input type="text" maxlength="" name="passportValidity" class="traveler dateinput" onclick="WdatePicker()">
                                </li>

                                <li>
                                    <label class="ydLable">身份证号：</label>
                                    <input type="text" name="idCard" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
                                </li>
                                <li>
                                    <label class="ydLable">护照类型：</label>
                                    <select name="passportType" class="selCountry">
                                        <option value="1">因公护照</option>
                                        <option value="2">因私护照</option>
                                    </select>
                                </li>
                            </ul>
                            <div class="ydbz_tit ydbz_tit_child">　　备注：<textarea name="remarks" class="textarea_long"></textarea></div>

                        </div>
                        <!--游客信息左侧结束-->
                        <div class="tourist-right">
                        <div class="bj-info">
                            <div class="clearfix">
                                <div class="traveler-rebatesDiv">
                                		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
						       		<c:choose>
						      			<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
											<label class="ydLable2 ydColor1">预计个人宣传费：</label>
										</c:when>
						         		<c:otherwise>
						         			<label class="ydLable2 ydColor1">预计个人返佣：</label>
						          		</c:otherwise>
								 </c:choose>   
                                    
                                    <select name="rebatesCurrency">
                                        <c:forEach var="bz" items="${bzList }">
                                            <option value="${bz.currencyId }">${bz.currencyName }</option>
                                        </c:forEach>
                                    </select>
                                    <input type="text" class="ipt-rebates"  name="rebatesMoney"  maxlength="9" data-type="amount"/>
                                </div>
                            </div>
                        </div>
                        <div class="yd-total clearfix">
                            <div class="fr">
                                <label class="ydLable2">同行价：</label>
                                <span data="tradePrice" class="ydFont2"></span>
                            </div>
                            <div class="fr">
                                <label class="ydLable2">结算价：</label>
                                <span data="newJsPrice" class="ydFont2"></span>
                            </div>
                        </div>
                    </div>
                    </div>
                    <!--保存、取消按钮开始-->
                    <div class="rightBtn"><a class="btn" name="savePeople" onclick="saveTraveler(this)">保存</a></div>
                    <!--保存、取消按钮结束-->
                </div>
            </form>
        </div>
        <!-- 添加游客模板end -->

        <!-- 添加游客按钮start -->
        <div class="touristBtn"><a class="btn-addGrey" id="addTraveler">添加游客</a></div>
        <!-- 添加游客按钮end -->

        <!-- 预计团队返佣start -->
        <div  class="traveler-rebatesDiv">
            <form name="groupRebates">
            		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		       		<c:choose>
		      			<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
							<label class="ydColor1">预计团队宣传费：</label>
						</c:when>
		         		<c:otherwise>
		         			<label class="ydColor1">预计团队返佣：</label>
		          		</c:otherwise>
				 </c:choose>   
                <select name="groupRebatesCurrency">
                    <c:forEach var="bz" items="${bzList }">
                        <option value="${bz.currencyId }"
                                <c:if test="${fns:getCurrencyIdBySerialNum(airticketOrder.scheduleBackUuid) == bz.currencyId }">
                                selected="selected"</c:if>>
                            ${bz.currencyName }
                        </option>
                    </c:forEach>
                </select>
                <input type="text" class="required ipt-rebates" name="groupRebatesMoney"  maxlength="9"
                       data-type="amount" placeholder="金额"
                       value="${fns:getMoneyAmountNUM(airticketOrder.scheduleBackUuid)}"/>
            </form>
        </div>
        <!-- 预计团队返佣end -->

        <!-- 支付部分start -->
        <!-- 订单总同行价和订单总结算价显示 -->
        <div style="text-align:right; font-size:12px; ">
            <b style="font-size:18px">订单总同行价：</b>
            <span id="travelerSumPrice" class="tdred f20"></span>
            <b style="font-size:18px">订单总结算价：</b>
            <span id="travelerSumClearPrice" class="tdred f20"></span>
        </div>
        <!-- 下一步 -->
        <div class="ydbz_sxb" id="firstDiv"
             style="margin-top:20px; padding-top:10px; padding-right:10px;">
            <div class="ydBtn ydBtn2">
                <a class="ydbz_x" id="oneToSecondStepDiv">下一步</a>
            </div>
        </div>
        <!-- 上一步 -->
        <div class="ydbz_sxb" id="secondDiv"
             style='display:none;margin-top:20px; padding-top:10px; padding-right:10px;'>
            <div class="ydBtn ydBtn2">
                <div class="ydbz_s" id="modSecondToOneStepDiv">上一步</div>
                <div class="ydbz_x" id="secondToThirdStepDiv" onClick="$(this).attr('id','');$(this).attr('class','ydbz_s gray');">保存</div>
            </div>
        </div>
        <!-- 关闭 -->
        <div class="ydbz_sxb" id="closeOperation" style="display: none; margin-top:20px; padding-top:10px; padding-right:10px;">
            <div class="ydBtn ydBtn2">
                <a class="ydbz_s" onClick="window.close();">关闭</a>
            </div>
        </div>
        <!-- 支付部分end -->
        </div>
    </div>
</div>
<!--右侧内容部分结束-->
</body>
</html>