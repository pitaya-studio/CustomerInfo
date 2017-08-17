<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>报名-酒店-预报名</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<!--[if lte IE 6]>
		<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
		<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
	<![endif]-->
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/css/j.suggest.css" rel="stylesheet" type="text/css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
		
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	
	<script src="${ctxStatic}/js/input-comp/j.dimensions.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/input-comp/j.suggest.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<%--     <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script> --%>
    <!--<script type="text/javascript" src="js/manageorder.js"></script>-->
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
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
	var tempFirstContactName = '${agentinfo.agentContact}';
	var agentId = '${agent.id}';
	var address = '${address}';
	var dataArray = eval('${contactArrayView}');
   	var allowModifyAgentInfo = ${allowModifyAgentInfo };
   	var allowAddAgentInfo = ${allowAddAgentInfo };
	
	$(function(){
	    	//可以修改时
	        var dataStr = ${contactsJsonStr};  //自己组织的data
// 	        var dataArrayStr = '${contactArray }';  //conta转换的jsonArray
// 	        var dataArray = eval(dataArrayStr);
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
	    
		    //如果不是可输入修改，disabled下拉和除了“其他”之外的input
			var initDom = $("input[name=orderContacts_contactsName]:first").prev();
			fillContactInfo2(initDom);
			//初始化时为第一联系人填充信息(可修改)
			var firstDom = $("#signChannelList").find("span[name=channelConcat]:first");
			fillContactInfo(firstDom, dataArray[0]);
			if(allowModifyAgentInfo == 0){
				//不可修改时，把联系人input置为只读（'其他'除外）
				setContactInfoReadonly();
				//非签约渠道不受输入修改配置影响
				$("#nonChannelList").find("input").attr("disabled", false);
    		}
    });
    
//可下拉可修改
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
        $border.append('<input type="text" name="orderContacts_contactsName" value="' + options.firstContactName + '" class="'+options.inputCss+'"/>');
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
 * 切换渠道时，获取当前渠道的所有联系人信息
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
			$("#signChannelList").find("ul[name=orderpersonMes]:not(:first)").remove();
		    //移除span的内容
		    var $span = $("#signChannelList").find("span[name=channelConcat]");
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
		    	$("#signChannelList").find("input[name=orderContacts_contactsName]").attr("disabled", true);
		    }
// 		       给第一联系人填充信息
		    fillContactInfo($("#signChannelList").find("span[name=channelConcat]:first"), eval(myData)[0]);
		}		
	});
}
    
/**
 * 不可修改时，把联系人input置为只读（'其他'除外）
 */    
function setContactInfoReadonly(){
	var $mesDom = $("#signChannelList").find("ul[name=orderpersonMes]");
	if(agentId != -1){		
		$mesDom.find("[name=orderContacts_contactsTel]").attr("disabled",true);  //联系电话为必填项
	}
	$mesDom.find("[name=orderContacts_contactsTixedTel]").attr("disabled",true);
	$mesDom.find("[name=orderContacts_contactsFax]").attr("disabled",true);
	$mesDom.find("[name=orderContacts_contactsQQ]").attr("disabled",true);
	$mesDom.find("[name=orderContacts_contactsEmail]").attr("disabled",true);
	$mesDom.find("[name=orderContacts_contactsAddress]").attr("disabled",true);
	$mesDom.find("[name=orderContacts_contactsZipCode]").attr("disabled",true);
	$("#signChannelList").find("input[name=orderContacts_contactsName]").attr("disabled", true);
}

/**
 * 选择联系人，填充对应信息（对应可修改）
 */
function fillContactInfo(obj, tempData){
	if(tempData == undefined) {
		return ;
	}
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsName]").val(tempData.text);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsTel]").val(tempData.contactMobile);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsTixedTel]").val(tempData.contactPhone);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsFax]").val(tempData.contactFax);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsQQ]").val(tempData.contactQQ);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsEmail]").val(tempData.contactEmail);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsAddress]").val(tempData.agentAddressFull);
}
/**
 * 选择联系人，填充对应信息（对应不可修改）(test)
 */
function fillContactInfo2(obj){
	if($(obj).val() == undefined || $(obj).val() == null){return;}
	var contactDataArray = $(obj).val().split(',');
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsName]").val(contactDataArray[1]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsTel]").val(contactDataArray[2]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsTixedTel]").val(contactDataArray[3]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsFax]").val(contactDataArray[4]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsQQ]").val(contactDataArray[5]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsEmail]").val(contactDataArray[6]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=orderContacts_contactsAddress]").val(address);
}

var contactNo = 1;  //联系人临时编号
/**
 * 添加渠道联系人(新有控件)
 */
function addAgentContactNew(obj){		
	contactNo++;
    var contactPeopleNum = $("ul[name=orderpersonMes]").length;
    var $currentUl = $(obj).parents("ul[name=orderpersonMes]");
    var $newUl = $currentUl.clone();
//     $newUl.find("input[name=nagentName]").parent().remove();
    //修改联系人序号
    $newUl.find("span[name=contactNo]").html(contactNo);
    $newUl.find('input').val('');
    $newUl.find('select').append('<option value="" selected="selected"></option>');
    $newUl.children('li').eq(0).find('label font').html(parseInt(contactPeopleNum) + 1);
    $newUl.children('li').eq(1).find('span.yd1AddPeople').remove();
    $newUl.children('li').eq(1).append('<span class="ydbz_x gray" onclick="yd1DelPeople(this)">删除联系人</span>');
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
		//获取选中项信息
		for(var i=0; i<dataArray.length; i++){
			var tempData = dataArray[i];
			if(tempData.uuid == $(this).find('a').attr('uuid')){
           		fillContactInfo(this, tempData);
        	}
		}
    });
}

/**
 * 删除渠道联系人
 */
function yd1DelPeople(obj) {
	contactNo--;
    $(obj).parent().parent().remove();
    $("ul[name=orderpersonMes]").each(function (index, element) {
        $(element).children("li").find("span[name=contactNo]").text(index + 1);
    });
}

//===============

		$(function(){
			//自备签类型
            ydbz2zibeiqian();
            //是否占位联运
            ydbz2interradio();
			$("#inputForm").validate({
				rules:{
				},
				submitHandler: function(form){
					var url = "${ctx}/hotelOrder/saveHotelOrder";
					
					//渠道信息验证
					var flag = true;
					var channelType = $("#channelType").val();
	            	var orderCompanyVal='';
	            	var linkmanVal='';
	            	var linkmanphone='';
	            	var tipInfo = '';
	            	if(channelType == '1') {
	            		$("#signChannelList").find("input[name=orderContacts_contactsName]").each(function(){
	            			if($(this).val() == '') {
	            				tipInfo = "请输入渠道联系人";
	            				$(this).focus();
	            				flag = false;
		        				return false;
	            			}
	            		});
	            		
	            		flag && $("#signChannelList").find("input[name=orderContacts_contactsTel]").each(function(){
	            			if($(this).val() == '') {
	            				tipInfo = "请输入渠道联系人电话";
	            				$(this).focus();
	            				flag = false;
		        				return false;
	            			}
	            		});
	            	} else if(channelType == '2') {
			        	$("#nonChannelList").find("input[name=orderContacts_contactsName]").each(function(){
	            			if($(this).val() == '') {
	            				tipInfo = "请输入渠道联系人";
	            				$(this).focus();
	            				flag = false;
		        				return false;
	            			}
	            		});
	            		
	            		flag && $("#nonChannelList").find("input[name=orderContacts_contactsTel]").each(function(){
	            			if($(this).val() == '') {
	            				tipInfo = "请输入渠道联系人电话";
	            				$(this).focus();
	            				flag = false;
		        				return false;
	            			}
	            		});
	            	}
	            	
	            	if(!flag) {
	            		$.jBox.tip(tipInfo);
	            		return false;
	            	}
					
					/** <tr id="${groupPrice.uuid}" class="groupPrices_tr">
							<td class="tc">
								${groupPrice.groupAirline.spaceLevelStr}
							</td>
							<td class="tc">
								<input type="hidden" name="islandOrderTravelerType" value="${groupPrice.type}">
								<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.type}"/>
								<input type="hidden" name="islandOrderPrice_activityIslandGroupPriceUuid" value="${groupPrice.uuid}" />
								<input type="hidden" name="islandOrderPrice_remark" value="_"/>
								<input type="hidden" name="islandOrderPrice_priceType" value="1" />
								<input type="hidden" name="islandOrderPrice_priceName" value="_" />
							</td>
							<td class="tc">
								<span><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/>${groupPrice.price }</span>
								<input type="hidden" name="islandOrderPrice_currencyId" value="${groupPrice.currencyId }" />
								<input type="hidden" name="islandOrderPrice_price" value="${groupPrice.price }" />
							</td>
							<td class="tc"><input type="text" class="price_sale_house_w100" data-type="number" name="islandOrderPrice_num"/></td>
							<td class="tc"><span></span>
							</td>
						</tr> */
					var map = {}; // Map map = new HashMap();
					$("#passengerInfoTable select[name=hotelTraveler_personType] option:selected").each(function(){
						var key = $(this).val(); 
						if(key in map) {
							map[key] = map[key] + 1;
						} else {
							map[key] = 1;
						}
					});
					
					var personTypeVali = false;
					var personTypeName = '';
					var personTypeNum = 0;
					//游客信息验证
					$(".groupPrices_tr").each(function(){
						var orderPriceNum = $(this).find("input[name=hotelOrderPrice_num]").val();
						var travelerType = $(this).find("input[name=hotelOrderTravelerType]").val();
						var OrderTravelerTypeName = $(this).find("span[name=OrderTravelerTypeName]").text();
						orderPriceNum = orderPriceNum == '' ? 0 : orderPriceNum;
						if(map[travelerType] > orderPriceNum) {
							personTypeName = OrderTravelerTypeName;
							personTypeNum = orderPriceNum;
							personTypeVali = true;
						}
					});
					
					
					if(personTypeVali) {
						top.$.jBox.tip(personTypeName+'游客人数不能大于'+personTypeNum+'！');
						return false;
					}
					
					
					/**
					拼接规则 country#visaTypeId 如： [111#222；444#555] 如有空值用“_”占位
					<td class="tc table_padings_none">
						<input type="hidden" name="hotelTraveler_visaInfo" />
						<p class="hotelTraveler_visaInfo">
							<select name="country" class="w40b">
								<option value="c89e0a6661b64d1e809d8873cf85bc80">中国</option>
								<option value="e4ea7467bdb6497fa456b6addcb8fb9a">美国</option>
							</select>
							<select name="visaTypeId" class="w40b">
								<c:forEach items="${visaTypes }" var="visa">
									<option value="${visa.id }">${visa.label}</option>
								</c:forEach>
							</select>
							<i class="price_sale_house_01"></i>
						</p>
					</td>
					*/
					$(".passenger_info_tr p.hotelTraveler_visaInfo").each(function(){
						var countryArray = $(this).parent().find("input[name=country]");
						var visaTypeIdArray = $(this).parent().find("select[name=visaTypeId]");
						var hotelTraveler_visaInfo = $(this).parent().find("input[name=hotelTraveler_visaInfo]");
						var visaInfoStr = "";
						for(var i=0; i<countryArray.length; i++) {
							visaInfoStr += $(countryArray[i]).val()==''?'_':$(countryArray[i]).val();
							visaInfoStr += "#";
							visaInfoStr += $(visaTypeIdArray[i]).val()==''?'_':$(visaTypeIdArray[i]).val();
							if(i != countryArray.length-1) {
								visaInfoStr += ";";
							}
						}
						hotelTraveler_visaInfo.val(visaInfoStr);
					});
					
					/**
					拼接规则 papersType#idCard#validityDate 如： [111#222#333；444#555#666] 如有空值用“_”占位
					<td>
						<input type="hidden" name="hotelTraveler_papersType" />
						<p class="hotelTraveler_papersType">
							<select name="papersType" class="w80">
								<option value="">请选择</option>
								<option value="1">身份证</option>
								<option value="2">护照</option>
								<option value="3">警官证</option>
								<option value="4">军官证</option>
								<option value="5">其他</option>
							</select> 
							<input type="text" name="idCard" class="w130 input_pad" />
							<input type="text" name="validityDate" onclick="WdatePicker()" class="dateinput required w90 input_pad" />
							<i class="price_sale_house_01"></i>
						</p>
					</td>
					*/
					$(".passenger_info_tr p.hotelTraveler_papersType").each(function(){
						var papersTypeArray = $(this).parent().find("select[name=papersType]");
						var idCardArray = $(this).parent().find("input[name=idCard]");
						var validityDateArray = $(this).parent().find("input[name=validityDate]");
						var hotelTraveler_papersType = $(this).parent().find("input[name=hotelTraveler_papersType]");
						var papersTypeStr = "";
						for(var i=0; i<papersTypeArray.length; i++) {
							if($(papersTypeArray[i]).val() == '' && $(idCardArray[i]).val()=='' && $(validityDateArray[i]).val()=='') {
								continue;
							}
							
							papersTypeStr += $(papersTypeArray[i]).val()==''?'_':$(papersTypeArray[i]).val();
							papersTypeStr += "#";
							papersTypeStr += $(idCardArray[i]).val()==''?'_':$(idCardArray[i]).val();
							papersTypeStr += "#";
							papersTypeStr += $(validityDateArray[i]).val()==''?'_':$(validityDateArray[i]).val();
							if(i != papersTypeArray.length-1) {
								papersTypeStr += ";";
							}
						}
						hotelTraveler_papersType.val(papersTypeStr);
					});
					
					/**
					拼接规则 currencyId#amount 如： [111#222;444#555] 如有空值用“_”占位
					<td class="tc">
						<input type="hidden" name="hotelTraveler_amount" />
						<p class="hotelTraveler_amount">
							<select name="currency" class="w30b">
								<c:forEach items="${currencyList }" var="currency">
									<option value="${currency.id }">${currency.currencyName}</option>
								</c:forEach>
							</select> 
							<input type="text" name="price" class="w30b " /> 
							<i class="price_sale_house_01"></i>
						</p>
					</td>
					*/
					
					$(".passenger_info_tr p.hotelTraveler_amount").each(function(){
						var currencyArray = $(this).parent().find("select[name=currency]");
						var priceArray = $(this).parent().find("input[name=price]");
						var hotelTraveler_amount = $(this).parent().find("input[name=hotelTraveler_amount]");
						var travelerAmountStr = "";
						for(var i=0; i<currencyArray.length; i++) {
							travelerAmountStr += $(currencyArray[i]).val()==''?'_':$(currencyArray[i]).val();
							travelerAmountStr += "#";
							travelerAmountStr += $(priceArray[i]).val()==''?'_':$(priceArray[i]).val();
							if(i != currencyArray.length-1) {
								travelerAmountStr += ";";
							}
						}
						hotelTraveler_amount.val(travelerAmountStr);
					});
					
					/**
					拼接规则 docId#docName#docPath 如： [111#222#333；444#555#666] 如有空值用“_”占位
					 <li>
					 	 <a class="padr10" href="javascript:void(0)" onclick="downloads()"></a>
						 <input type="hidden" name="hotelTraveler_files" />
						 <input type="hidden" name="docId" />
						 <input type="hidden" name="docName"/>
						 <input type="hidden" name="docPath"/>
					 </li>
					 */
					 
					 $("input[name=hotelTraveler_files]").each(function(){
					 	var docId = $(this).parent().find("input[name=docId]");
					 	var docName = $(this).parent().find("input[name=docName]");
					 	var docPath = $(this).parent().find("input[name=docPath]");
					 	var travelerFilesStr = "";
					 	for(var i=0; i<docId.length; i++) {
					 		travelerFilesStr += $(docId[i]).val()==''?'_':$(docId[i]).val();
							travelerFilesStr += "#";
					 		travelerFilesStr += $(docName[i]).val()==''?'_':$(docName[i]).val();
							travelerFilesStr += "#";
					 		travelerFilesStr += $(docPath[i]).val()==''?'_':$(docPath[i]).val();
					 		if(i != travelerFilesStr.length-1) {
								travelerFilesStr += ";";
							}
					 	}
					 	$(this).val(travelerFilesStr);
					 });
					 
					 //设置其他金额正负显示值
					 /**
					 <tr class="add_other_charges" style="display:none;">
						<td class="tr">金额名称：
							<input type="hidden" name="hotelOrderPrice_priceType" value="4"/>
							<input type="hidden" name="hotelOrderPrice_num" value="_" />
							<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="_" />
						</td>
						<td>
							<span class="tl">
								<input type="text" class="price_sale_house_w93" name="hotelOrderPrice_priceName" id="input8" maxlength="" />
							</span>
						</td>
						<td class="tr tr_other_u">
							<input name="other_u" type="radio" class="dis_inlineblock" id="u138_input" value="radio" checked="checked" data-label="增加" />
							<label for="u138_input">增加</label>
							<input name="other_u" type="radio" id="u138_input2" value="radio" data-label="减少" />
							<label for="u140_input">减少：</label>
						</td>
						<td class="tl">
							<label>
								<select name="hotelOrderPrice_currencyId" class="w80">
									<c:forEach items="${currencyList }" var="currency">
										<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
									</c:forEach>
								</select>
							</label>
							<input type="text" class="price_sale_house_w93 price" name="hotelOrderPrice_price" id="input7" />
						</td>
						<td class="tr">备注：</td>
						<td class="tl">
							<input type="text" class="price_sale_house_w93" name="hotelOrderPrice_remark" id="input11" />
							<span class="padr10"></span>
							<i class="price_sale_house_02" onclick="del_other_charges(this);"></i>
						</td>
					</tr>
					 */
					 
					 $(".clone_other_charges").each(function(){
					 	//当选中减少时,将相应金额改成负数
					 	if($(this).find("input:radio").prop("checked") == false) {
					 		var priceBakObj = $(this).find("input[name=hotelOrderPrice_priceBak]");
					 		var priceObj = $(this).find("input[name=hotelOrderPrice_price]");
					 		if(priceBakObj.val() != '') {
					 			priceObj.val(0-priceBakObj.val());
					 		}  
					 	} else {
					 		var priceBakObj = $(this).find("input[name=hotelOrderPrice_priceBak]");
					 		var priceObj = $(this).find("input[name=hotelOrderPrice_price]");
					 		if(priceBakObj.val() != '') {
					 			priceObj.val(priceBakObj.val());
					 		}
					 	}
					 });
					 
					 //计算订单总额和结算总额(游客类型)
					 /**
					 <c:forEach items="${groupPrices }" var="groupPrice">
						<tr id="${groupPrice.uuid}">
							<td class="tc">
								<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.type}"/>
								<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="${groupPrice.type}" />
								<input type="hidden" name="hotelOrderPrice_remark" value="_"/>
								<input type="hidden" name="hotelOrderPrice_priceType" value="1" />
								<input type="hidden" name="hotelOrderPrice_priceName" value="_" />
							</td>
							<td class="tc">
								<span><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/>${groupPrice.price }</span>
								<input type="hidden" name="hotelOrderPrice_currencyId" value="${groupPrice.currencyId }" />
								<input type="hidden" name="hotelOrderPrice_price" value="${groupPrice.price }" />
							</td>
							<td class="tc"><input type="text" class="price_sale_house_w100" name="hotelOrderPrice_num"/></td>
							<td class="tc"><span></span>
							</td>
						</tr>
					</c:forEach>
					 */
					 var currencyIdArr = [];
					 var priceArr = [];
					 $(".groupPrices_tr").each(function(){
					 	var currency = $(this).find("input[name=hotelOrderPrice_currencyId]").val();
					 	
					 	var price = $(this).find("input[name=hotelOrderPrice_price]").val();
					 	var num = $(this).find("input[name=hotelOrderPrice_num]").val();
					 	
					 	if(num != "0" && num != "") {
						 	currencyIdArr.push(currency);
						 	priceArr.push(price*num);
					 	}
					 });
					 
					 //订单总额
					 $("#total_currency").val(currencyIdArr.join(";"));
					 $("#total_amount").val(priceArr.join(";"));
					 
					 //计算结算总额(包括订单总额、优惠金额、退款金额和其他费用)
					 $("#add_other_charges_table").find("tr.calc_amount_tr").each(function(i){
					 	var currency = $(this).find("select[name=hotelOrderPrice_currencyId]").val();
					 	
					 	var price = $(this).find("input[name=hotelOrderPrice_price]").val();
					 	if(price !="0" && price != "") {
					 		currencyIdArr.push(currency);
					 		
						 	if(i < 1) {
						 		priceArr.push(0 - price);
						 	} else {
						 		priceArr.push(price);
						 	}
					 	}
					 });
					 
					//结算总额
					$("#result_currency").val(currencyIdArr.join(";"));
					$("#result_amount").val(priceArr.join(";"));
					
					//应收金额
					$("#to_receive_currency").val(currencyIdArr.join(";"));
					$("#to_receive_amount").val(priceArr.join(";"));
					
					if($("#passengerInfoTable").is(":visible")) {
						$('#passengerInfoTable tr').find('input,select').removeAttr('disabled');
					} else {
						$('#passengerInfoTable tr').find('input,select').attr('disabled','disabled');
					}
					
					//过滤掉人数为空的团期价格
					/**
					 <c:forEach items="${groupPrices }" var="groupPrice">
							<tr id="${groupPrice.uuid}" class="groupPrices_tr">
								<td class="tc">
									<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.type}"/>
									<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="${groupPrice.uuid}" />
									<input type="hidden" name="hotelOrderPrice_remark" value="_"/>
									<input type="hidden" name="hotelOrderPrice_priceType" value="1" />
									<input type="hidden" name="hotelOrderPrice_priceName" value="_" />
								</td>
								<td class="tc">
									<span><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/>${groupPrice.price }</span>
									<input type="hidden" name="hotelOrderPrice_currencyId" value="${groupPrice.currencyId }" />
									<input type="hidden" name="hotelOrderPrice_price" value="${groupPrice.price }" />
								</td>
								<td class="tc"><input type="text" class="price_sale_house_w100" name="hotelOrderPrice_num"/></td>
								<td class="tc"><span></span>
								</td>
							</tr>
						</c:forEach>
					 */
					 var groupPriceVali = false;
					 $(".groupPrices_tr").each(function(){
					 	var num = $(this).find("input[name=hotelOrderPrice_num]").val();
					 	if(num == '' || num == 0) {
					 		$(this).find("input[name=hotelOrderPrice_num]").val(0);
					 	} else {
					 		groupPriceVali = true;
					 	}
					 });
					 
					 if(!groupPriceVali) {
					 	top.$.jBox.tip("最少输入一个游客");
					 	//将文本框还原
						$(".groupPrices_tr").each(function(){
						 	$(this).find("input").removeAttr('disabled');
					 	});
				   		return false;
					 }
					
					var flag = true;
					//金额格式校验
					$(".price").each(function(){
						if(($(this).val() != '') && (!isMoney($(this).val()))) {
							flag = false;
							$(this).focus();
							top.$.jBox.tip("请输入正确的金额");
							return false;
						}
					});
					
					if(!flag) {
						return false;
					}
					
					//移除被隐藏的联系人,避免被误提交
		        	var $contactListRmv;
		        	if($("#signChannelList").is(":hidden")){
						$contactListRmv = $("#signChannelList");
					} else {
						$contactListRmv = $("#nonChannelList");
					}
		        	$contactListRmv.remove();
					
					$("#submitAndReceiveButton").attr("disabled", "disabled");
					$.post(url,$("#inputForm").serialize(),function(data){
						if(data.message=="1"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("保存成功!");
							
							if(($("#isReceive").val() == "1") && data.orderUuid) {
								var resultCurrency = $("#result_currency").val();
								var resultAmount = $("#result_amount").val();
								var cancelPayUrl = encodeURIComponent("/hotelOrder/list/0?_m=417&_mc=852");
								
								window.location="${ctx}/hotelOrder/payHotelOrder/" + data.orderUuid+"?resultCurrency="+resultCurrency+"&resultAmount="+resultAmount+"&cancelPayUrl="+cancelPayUrl;
							} else {
								setTimeout(function(){window.close();},500);
							};
						}else if(data.message=="2"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("修改成功!");
							setTimeout(function(){window.close();},500);
						}else if(data.message=="3"){
							$.jBox.tip(data.error,'warning');
							$("#btnSubmit").attr("disabled", false);
						}else{
							$.jBox.tip('系统异常，请重新操作!','warning');
							$("#btnSubmit").attr("disabled", false);
						}
						
						//将文本框还原
						$(".groupPrices_tr").each(function(){
						 	$(this).find("input").removeAttr('disabled');
					 	});
					 	
        				$("#submitSignUpButton").removeAttr("disabled");
        				$("#submitAndReceiveButton").removeAttr("disabled");
					});
					
				},
				messages:{
				}
			});
			
			//加载供应商信息
// 			loadAgentInfo();
			
			//将渠道改为可输入的select
			$("#orderCompany").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
				getAllContactsByAgentId(obj.item);
			});
			
			//将非签约渠道设置为不可用
            $("#nonChannelList").find("input").attr("disabled", false);
		});
		
		function openUrl(url){
			window.open(url);
	    }
		//房型添加或减少
		function hotel_room_xing_add_con() {
			var r_c_add = $('.hotel_room_xing_add_con').html();
			$('.hotel_room_xing_add_con').after(r_c_add);
		}
		function hotel_room_xing_del_con(obj) {
			$(obj).parent().remove();
		}
		//费用调整添加其他费用
		var ot_id=0;
		function add_other_charges() {
			ot_id+=1;
			var html = $('.add_other_charges').clone();
			html.find('td.tr_other_u').replaceWith('<td class="tr"><input name="other_u'+ot_id+'" type="radio" class="dis_inlineblock" id="u138_input" value="radio" checked="checked" data-label="增加" /><label for="u138_input">增加</label> <input id="u138_input2" value="radio" data-label="减少" name="other_u'+ot_id+'" type="radio" /><label for="u140_input">减少：</label></td>');
			html.attr('class', 'clone_other_charges calc_amount_tr').show();
			$('#add_other_charges_table').append(html);
		}
		function del_other_charges(obj) {
			$(obj).parent().parent().remove();
			offersChange();
		}
		//保存游客信息
		function save_tours_obj(obj){
			if($(obj).text()=="保存"){
				$('#passengerInfoTable tr').has(obj).find('input,select').attr('disabled','disabled');
				$(obj).text('修改');
			}else{
				$('#passengerInfoTable tr').has(obj).find('input,select').removeAttr('disabled');
				$(obj).text('保存');
			}
		}
		//删除游客信息
		function del_tours_obj(obj){
			$('#passengerInfoTable tr').has(obj).remove();
			updateSequence();
		}
		//添加游客信息
		var idGen = 1;
		function add_tours_obj(){
			if ($('#passengerInfoTable tbody tr').length >= orderInfo.totalCount) return;
			
			/* $('#passengerInfoTable tr').find('input,select').removeAttr('disabled'); */
			$('#passengerInfoTable').show();
			var htmlstr = $('#add_tours_obj_tr').html().replace("id=\"suggestcountry\"","id=\"suggestcountry"+idGen+"\"");
			htmlstr = htmlstr.replace("id=\"country\"","id=\"country"+idGen+"\"");
			htmlstr = htmlstr.replace("id=\"Divsuggestcountry\"","id=\"Divsuggestcountry"+idGen+"\"");
			htmlstr = htmlstr.replace("add_tours_obj_tr_index", idGen);
			var html='<tr class="passenger_info_tr">'+htmlstr+'</tr>';
			$('#passengerInfoTable tbody').append(html);
			idGen++;
			updateSequence();
			$("select[name=hotelTraveler_personType]").each(function(){
	   			 $(this).change();
			});
		}
		
		function updateSequence() {
			$("#passengerInfoTable").children("tbody").children("tr").each(function(i) {
				$(this).children("td").first().text(i+1);
			});
		}
		/**
		 *校验金额（必须是正负数且小数点后只能两位）
		 */
		function isMoney(money){
			if(!money) return false;
			var strP=/^(-?\d+)(\.\d+)?$/;
			
			if(!strP.test(money)) return false;
			try{
				if(parseFloat(money)!=money) return false;
			}catch(ex){
				return false;
			}
			return true;	
		}
		
		function wordsDeal()
		{
			var curLength=$("#hotelOrderRemark").val().length;
			if(curLength>500) {
				var num=$("#hotelOrderRemark").val().substr(0,500);
				$("#hotelOrderRemark").val(num);
				$.jBox.tip('超过字数限制，多出的字将被截断！','warning');
			} else{
				$("#textCount").text(500-$("#hotelOrderRemark").val().length);
			}
		}
	</script>
<script type="text/javascript">
        var orderInfo = {
        	//游客数据格式
        	// { type: "adult", name: "成人", cost: { "￥": 1000} },
            //    { type: "baby", name: "儿童", cost: { "￥": 300 } },
            //  { type: "baby1", name: "儿童占床", cost: { "￥": 500 } }
            traveller: [
            	<c:forEach items="${groupPrices }" var="groupPrice" varStatus="status">
                	{ type: "${groupPrice.uuid}", name: "<trekiz:autoId2Name4Table tableName='traveler_type' sourceColumnName='uuid' srcColumnName='name' value='${groupPrice.type}'/>",
                		cost: { '<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId}"/>':'${groupPrice.price}'}},
            	</c:forEach>
            ],
            // 订单总额
            totalCost: {},
            // 应收金额
            accounts: {},
            // 已收金额
            receipted: {},
            // 总人数
            totalCount: 0
        }


        $(document).ready(function (e) {
            var leftmenuid = $("#leftmenuid").val();
            $(".main-nav").find("li").each(function (index, element) {
                if ($(this).attr("menuid") == leftmenuid) {
                    $(this).css("background", "url(/resource/images/img/pfs_nav_hover.png)");
                }
            });
            $("#channelType").on("change", function () {
            	//切换到签约渠道
                if ($("#signChannel").is(":hidden")) {
                    $("#signChannel").show();
                    $("#signChannelList").show();
                    $("#nonChannel").hide();
                    $("#nonChannelList").hide();
                    
                	$("#orderCompanyName").attr("disabled", true);
                	$("#orderCompany").attr("disabled", false);
                    //加载渠道商联系人信息
                	loadAgentInfo();
                } else {
                    $("#signChannel").hide();
                    $("#signChannelList").hide();
                    $("#nonChannel").show();
                    $("#nonChannelList").show();
                    
                	$("#orderCompanyName").attr("disabled", false);
                	$("#orderCompany").attr("disabled", true);
                }
                
            });
            // 联系人模板
            var $contactsTemp = $("#orderpersonMesdtail ul:first").clone();
            //原有增删联系人
//             $("#orderpersonMesdtail").on("click", "span.yd1AddPeople", function () {
//                 var $newContacts = $contactsTemp.clone();
//                 $newContacts.find('span.yd1AddPeople').replaceWith('<span class="ydbz_x yd1DelPeople gray">删除联系人</span>');
//                 var $container = $("#signChannelList, #nonChannelList").has(this);
//                 $container.append($newContacts);

//             }).on("click", "span.yd1DelPeople", function () {
// 				$(this).parent().parent().remove();
// 			});
            $("#passengerInfoTable").on('click', 'i.price_sale_house_01', function () {
                // 添加签证
                var tempP = $(this).parent().clone();
                tempP.find('i.price_sale_house_01').replaceWith('<i class="price_sale_house_02"></i>');
                $(this).parent().parent().append(tempP);
            }).on('click', 'i.price_sale_house_02', function () {
                // 删除签证
                $(this).parent().remove();
            });
            // 折叠展开
            /* $("span.ydExpand").on('click', function () {
                var $this = $(this);
                var target = $(this).attr("data-target");
                /* $this.toggleClass("ydClose"); */
                /* $this.toggle(function(){
                	$(this).addClass("ydClose");
                },function(){
                	$(this).removeAttr("ydClose");
                });
            }); */
            $("#costTable").on("blur", "input:text", function () {
                peopleCountChange();
            });
            //$("#adjustCost").on("blur", "input:text.price", function () {
                //offersChange();
            //});
            $("#adjustCost").on("change", "input:text.price", function () {
				offersChange();
			}).on("change", "input:radio", function() {
				offersChange();
			}).on("change", "select", function() {
				offersChange();
			});
			
            $("#avg_btn").on('click', function () {
                if (orderInfo.totalCount < 1 || !orderInfo.accounts) return;
                var avgCost = {}, lastCost = {};
                for (var k in orderInfo.accounts) {
                    if (orderInfo.accounts[k]) {
                        avgCost[k] = Math.round(orderInfo.accounts[k] / orderInfo.totalCount * 100) / 100;
                        lastCost[k] = Math.round((avgCost[k] + (orderInfo.accounts[k] - avgCost[k] * orderInfo.totalCount)) * 100) / 100;
                    }
                }
                var $trs = $("#passengerInfoTable > tbody >tr:visible");
                $trs.each(function (i) {
                    var cost = (i == $trs.length - 1) ? lastCost : avgCost;
                    var $td = $(this).find("td:eq(7)");
                    var first = true;
                    $td.find("p:not(:first)").remove();
                    for (var k in cost) {
                        if (!first) {
                            $td.find("i.price_sale_house_01").click();
                        }
                        first = false;
                        var $p = $td.find("p:last");
                        $p.find("select option[data-currency='" + k + "']").prop("selected", true);
                        $p.find("input").val(cost[k]);
                    }
                });
            });
            $("#clear_avg_btn").on('click', function () {
				$("#passengerInfoTable > tbody >tr:visible > td:nth-child(8) input").val('');
            });
        });

        function peopleCountChange() {
            // 总人数
            var totalCount = 0;
            // 总费用
            var totalCost = {};
            for (var l = orderInfo.traveller.length; l--;) {
                var traveller = orderInfo.traveller[l];
                var count = parseInt($("#" + traveller.type).find("td:eq(2) input").val());
                if (count) {
                    var cost = {};
                    for (var k in traveller.cost) {
                        totalCost[k] || (totalCost[k] = 0);
                        cost[k] = traveller.cost[k] * count;
                        totalCost[k] += cost[k];
                    }
                    $("#" + traveller.type).find("td:eq(3)").text(formatCost(cost));
                    totalCount += count;
                } else {
                	$("#" + traveller.type).find("td:eq(3)").text('');
                }
            }
            orderInfo.totalCost = totalCost;
            orderInfo.totalCount = totalCount;
            $("#totalPeopleCount").text(totalCount);
            $("#orderPersonNum").val(totalCount);
            showCostMsg();
            var $tbody = $("#passengerInfoTable > tbody");
            var subCount = totalCount - $tbody.children("tr:visible").length;
            
            if (subCount < 0) {
                $tbody.children("tr:gt(" + (totalCount - 1) + ")").remove();
            }
        }

		function offersChange() {
			var offersCost = {};
			$("#adjustCost tr:visible").each(function(i) {
				var $this = $(this);
				var currency, money;
				if (i < 3) {
					if ($this.find("input:text:first").is(".price")) {
						currency = $this.find("select option:selected").attr("data-currency");
						money = parseFloat($this.find("input:text:first").val());
					}
				} else {
					currency = $this.find("select option:selected").attr("data-currency");
					money = parseFloat($this.find("input:text:eq(1)").val());
					if ($this.find("input:radio").prop("checked") && money) {
						money = 0 - money;
					}
				}
				if (currency && money) {
					offersCost[currency] || (offersCost[currency] = 0);
					offersCost[currency] += money;
				}
			});
			orderInfo.offersCost = offersCost;
			showCostMsg();
		}

        function showCostMsg() {
            orderInfo.accounts = subCost(orderInfo.totalCost, orderInfo.offersCost);
            orderInfo.unReceipted = subCost(orderInfo.accounts, orderInfo.receipted);
            
            // 订单总额
            $("span.totalCost").text(formatCost(orderInfo.totalCost));
            // 结算总额
            $("span.accounts").text(formatCost(orderInfo.accounts));
            // 未收总额
            $("span.unReceipted").text(formatCost(orderInfo.unReceipted));
        }

        function formatCost(cost) {
            var str = []
            for (var k in cost) {
                if (cost[k]) {
                    if (cost[k] > 0 && str.length) {
                        str.push(" + ");
                    }
                    else if (cost[k] < 0) {
                        str.push(" - ");
                    }
                    str.push(k + Math.abs(cost[k],2).toFixed(2));
                }
            }
            return str.join('');
        }

        function subCost(cost1, cost2) {
            var cost = $.extend({}, cost1);
            if (!cost2) return cost;
            for (var k in cost2) {
                cost[k] = (cost[k] || 0) - cost2[k];
            }
            return cost;
        }

        $(function () {
            $('.closeNotice').click(function () {
                var par = $(this).parent().parent();
                par.hide();
                par.prev().removeClass('border-bottom');
                par.prev().find('.notice-date').show();
            });
            $('.showNotice').click(function () {
                $(this).parent().hide();
                var par = $(this).parent().parent();
                par.addClass('border-bottom');
                par.next().show();
            });
            
        });
        $(function () {
            $('.main-nav li').click(function () {
                $(this).addClass('select').siblings().removeClass('select');
            })
        });
        String.prototype.formatNumberMoney = function (pattern) {
            var strarr = this ? this.toString().split('.') : ['0'];
            var fmtarr = pattern ? pattern.split('.') : [''];
            var retstr = '';
            var str = strarr[0];
            var fmt = fmtarr[0];
            var i = str.length - 1;
            var comma = false;
            for (var f = fmt.length - 1; f >= 0; f--) {
                switch (fmt.substr(f, 1)) {
                    case '#':
                        if (i >= 0) retstr = str.substr(i--, 1) + retstr;
                        break;
                    case '0':
                        if (i >= 0) retstr = str.substr(i--, 1) + retstr;
                        else retstr = '0' + retstr;
                        break;
                    case ',':
                        comma = true;
                        retstr = ',' + retstr;
                        break;
                }
            }
            if (i >= 0) {
                if (comma) {
                    var l = str.length;
                    for (; i >= 0; i--) {
                        retstr = str.substr(i, 1) + retstr;
                        if (i > 0 && ((l - i) % 3) == 0) retstr = ',' + retstr;
                    }
                } else retstr = str.substr(0, i + 1) + retstr;
            }
            retstr = retstr + '.';
            str = strarr.length > 1 ? strarr[1] : '';
            fmt = fmtarr.length > 1 ? fmtarr[1] : '';
            i = 0;
            for (var f = 0; f < fmt.length; f++) {
                switch (fmt.substr(f, 1)) {
                    case '#':
                        if (i < str.length) retstr += str.substr(i++, 1);
                        break;
                    case '0':
                        if (i < str.length) retstr += str.substr(i++, 1);
                        else retstr += '0';
                        break;
                }
            }
            return retstr.replace(/^,+/, '').replace(/\.$/, '');
        }
        String.prototype.replaceSpecialChars = function (regEx) {
            if (!regEx) {
                regEx = /[\`\"\~\!\@#$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@#\¥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？]/g;
            }
            return this.replace(regEx, '');
        };
        
        //加载渠道商联系人信息
        function loadAgentInfo() {
        	var channelType = $("#channelType").val();
			var agentId = $("#orderCompany").val();
			if(channelType == '1' && agentId != '') {
				$.post("${ctx}/islandOrder/loadAgentInfo",{"id":agentId }, function(data){
					if(data.agentinfo){
						//初始化渠道商联系人信息
						var signChannel = $("#signChannelList ul").eq(0);
						signChannel.find("input[name=orderContacts_contactsName]").val(data.agentinfo.agentContact);
						signChannel.find("input[name=orderContacts_contactsTel]").val(data.agentinfo.agentContactMobile);
						signChannel.find("input[name=orderContacts_contactsTixedTel]").val(data.agentinfo.agentContactTel);
						signChannel.find("input[name=orderContacts_contactsAddress]").val(data.agentinfo.agentAddress);
						signChannel.find("input[name=orderContacts_contactsFax]").val(data.agentinfo.agentContactFax);
						signChannel.find("input[name=orderContacts_contactsQQ]").val(data.agentinfo.agentContactQQ);
						signChannel.find("input[name=orderContacts_contactsEmail]").val(data.agentinfo.agentContactEmail);
						signChannel.find("input[name=orderContacts_contactsZipCode]").val(data.agentinfo.agentPostcode);
						signChannel.find("input[name=orderContacts_remark]").val(data.agentinfo.remarks);
					}
				});
			}
        }
        
        //提交预报名
        function submitSignUp() {
	        $.jBox($("#forecast-name-submit").html(), {
		        title: "确认预报名", buttons: { '确认': 1}, submit: function (v, h, f) {
		            if (v == "1") {
			        	$("#isReceive").val("0");
			        	
			        	$("#inputForm").submit();
		                return true;
		            }
		        }, height: '140', width: 300
		    });
        }
        
        //提交并收款
        function submitAndReceive() {
	        $.jBox($("#forecast-name-submit-collections").html(), {
		        title: "确认预报名并收款", buttons: { '确认': 1}, submit: function (v, h, f) {
		            if (v == "1") {
			        	$("#isReceive").val("1");
			        	//移除被隐藏的联系人
// 			        	var $contactListRmv;
// 			        	if($("#signChannelList").is(":hidden")){
// 							$contactListRmv = $("#nonChannelList")
// 						} else {
// 							$contactListRmv = $("#signChannelList")
// 						}
// 			        	$contactListRmv.remove();
			        	$("#inputForm").submit();
		                return true;
		            }
		        }, height: '200', width: 300
		    });
        }
        
		//附件管理弹窗
		function up_files_pop(obj) {
			var up_files_pop = $(obj).parent().find(".up_files_pop");
			var html = '<div style="min-width:300px;margin:0 auto;padding:20px;" class="up_files_pop">';
			html += up_files_pop.html();
			html += '</div>';
			$.jBox(html, {
				title: "附件管理", buttons: { '关闭': 1 }, submit: function (v, h, f) {
					if (v == "1") {
						up_files_pop.html($(h).find(".up_files_pop").html());
						return true;
					}
				}, width: 400
			});
		}
        
        function commenFunction(obj,fileIDList,fileNameList,filePathList) {
			var fileIdArr = fileIDList.split(";");
			var fileNameArr = fileNameList.split(";");
			var filePathArr = filePathList.split(";");
			for(var i=0; i<fileIdArr.length-1; i++) {
				var html = '<li><a class="padr10" href="javascript:void(0)" onclick="downloads('+ fileIdArr[i] +')">'+ fileNameArr[i] +'</a>';
				html += '<span class="tdred" style="cursor:pointer;" onclick="deleteFileInfo(null,\'hotelFeaturesAnnexDocId\',this)">删除</span>';
				html += '<input type="hidden" name="hotelTraveler_files" />';
				html += '<input type="hidden" name="docId" value="' + fileIdArr[i] + '" />';
				html += '<input type="hidden" name="docName" value="'+ fileNameArr[i] +'"/>';
				html += '<input type="hidden" name="docPath" value="'+ filePathArr[i] +'"/>';
				html += '</li>';
				$(obj).parent().find("ul").append(html);
			}
        }
        
        //下载文件
		function downloads(docid){
			window.open("${ctx}/sys/docinfo/download/"+docid);
	    }
	    
	    //删除现有的文件
		function deleteFileInfo(inputVal, objName, obj) {
			top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
				if(v=='ok'){
					if(inputVal != null && objName != null) {
						var delInput = $(".uploadPath").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
						delInput.next().eq(0).remove();
						delInput.next().eq(0).remove();
						delInput.remove();
						
						/*删除上传文件后，文件信息会存放在id为currentFiles的div中，也需要把该div相关的上传文件信息删除*/
						var docName = $(obj).parent("li").parent("ol").parent().find("#currentFiles").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
						docName.next().eq(0).remove();
						docName.next().eq(0).remove();
						docName.remove();
					
						
					}else if(inputVal == null && objName == null) {
						$(obj).parent().remove();
					}
					$(obj).parent("li").remove();
	
				}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');		
		}
		
		$(function(){
			initSuggestClass("${ctx}/geography/getAllConListAjax",{});
			
			$(".suggest").each(function(i,o){
				var id = $(this).attr("id");
				var name = $(this).attr("name");
				$(this).parent().append('<input id="'+id.replace("suggest","")+'" type="hidden" name="'+name.replace("suggest","")+'" value="">');
				$(this).parent().append('<span id="Div'+id+'" class="suggest_ac_results ac_results" style="top: 363px; left: 168px; display: none;"></span>');
			});
							
	        $(document).on("click",".suggest",function(){
	        	var id = $(this).attr("id");
				var name = $(this).attr("name");
	        	$(this).suggest(countrys, {
					hot_list : commoncountrys,
					dataContainer : '#'+id.replace("suggest",""),
					attachObject : "#Div"+id
				});
	        }).on("mouseover",".suggest",function(){
	        	var id = $(this).attr("id");
				var name = $(this).attr("name");
	        	$(this).suggest(countrys, {
					hot_list : commoncountrys,
					dataContainer : '#'+id.replace("suggest",""),
					attachObject : "#Div"+id
				});
	        });
	
		});
		function checkedRoom(obj,remObj){
			if(obj.value>=remObj){
				$.jBox.tip("请填写小于余位的整数");
				obj.value='';
				return false;
			}else{
				return true;
			}
		}
    </script>
    <style type="text/css"> 
		/*解决Firefox下table不显示下边框的问题*/  
		table#costTable2 
		{
			border-collapse:separate; 
			border-bottom: 1px solid #d7e5f2;
		}
		table#costTable2 tr td
		{ 
			border-bottom: 0; 
		}
	</style>
</head>
<body>
	<div>
		<!--右侧内容部分开始-->
		<div class="mod_nav">报名 &gt; 酒店 &gt; 预报名</div>
		<form:form method="post" modelAttribute="hotelOrderInput" action="" class="form-horizontal" id="inputForm" novalidate="" target="_blank">
			<!-- 酒店订单所有金额隐藏域  -->
			<!-- 订单总额 -->
			<input type="hidden" name="hotelMoneyAmount_currencyId" id="total_currency" />
			<input type="hidden" name="hotelMoneyAmount_amount" id="total_amount" />
			<input type="hidden" name="hotelMoneyAmount_moneyType" value="28"/>
			
			<!-- 结算总额 -->
			<input type="hidden" name="hotelMoneyAmount_currencyId" id="result_currency" />
			<input type="hidden" name="hotelMoneyAmount_amount" id="result_amount" />
			<input type="hidden" name="hotelMoneyAmount_moneyType" value="13"/>
			
			<!-- 应收金额 -->
			<input type="hidden" name="hotelMoneyAmount_currencyId" id="to_receive_currency" />
			<input type="hidden" name="hotelMoneyAmount_amount" id="to_receive_amount" />
			<input type="hidden" name="hotelMoneyAmount_moneyType" value="19"/>
			
			<%--
			<!-- 已收总额 -->
			<input type="hidden" name="hotelMoneyAmount_currencyId" id="receive_currency" />
			<input type="hidden" name="hotelMoneyAmount_amount" value="0"/>
			<input type="hidden" name="hotelMoneyAmount_moneyType" value="5"/>
			--%>
			
			<%-- 酒店产品信息 --%>
			<input type="hidden" name="activityHotelUuid" value="${activityHotel.uuid }" />
			<input type="hidden" name="activityHotelGroupUuid" value="${activityHotelGroup.uuid }" />

			<%-- 预定人数 --%>
			<input type="hidden" name="orderPersonNum" id="orderPersonNum" />		
			<%-- 是否收款 --%>
			<input type="hidden" name="isReceive" id="isReceive">
			
			<div class="ydbzbox fs">
				<div class="ydbz_tit">
					<span class="ydExpand" data-target="#baseInfo"></span>订单基本信息
				</div>
				<div id="baseInfo">
					<div style="color: #048601; font-size: 18px;font-weight:bold;padding-left:40px;padding-top:30px;">${activityHotel.activityName }</div>
					<ul class="ydbz_qd">
						<li><label>团号：</label>${activityHotelGroup.groupCode }</li>
						<li><label>计调员：</label>${productCreateBy }</li>
						<li><label>下单人：</label>${loginUserName }</li>
					</ul>
					<p class="ydbz_qdmc" style="padding:0;padding-top:10px;"></p>
					<div id="signChannelList2">
						<ul class="ydbz_qd">
							<!-- 根据需求隐藏空房单号  zhangchao 2016/01/07-->
							<%--< li  style="display:none;"><label>控房单号：</label> ${activityHotel.activitySerNum }</li> --%>
							<!-- <li><label>团号：</label> ${activityHotel.activitySerNum }</li> -->
							<li><label>国家：</label> <trekiz:autoId2Name4Table tableName='sys_geography' sourceColumnName='uuid' srcColumnName='name_cn' value='${activityHotel.country }'/></li>
							<li><label>岛屿：</label><trekiz:autoId2Name4Class classzName="Island" sourceProName="uuid" srcProName="islandName" value="${activityHotel.islandUuid }"/></li>
							<li><label>酒店名称：</label> <trekiz:autoId2Name4Class classzName="Hotel" sourceProName="uuid" srcProName="nameCn" value="${activityHotel.hotelUuid }"/></li>
							<li><label>酒店星级：</label> <span class="y_xing"><c:if test="${hotelStar != null && hotelStar !=0 }"><c:forEach begin="1" end="${hotelStar }">★</c:forEach></c:if></span></li>
							<li><label>日期：</label> <fmt:formatDate value="${activityHotelGroup.groupOpenDate }" pattern="yyyy.MM.dd" /></li>
							<li><label>上岛方式：</label> <trekiz:defineDict name="islandWay" type="islands_way" input="checkbox" defaultValue="${activityHotelGroup.islandWay }" readonly="true" regex=";"/></li>
							<li><label>参考航班：</label> ${activityHotelGroup.airline }</li>
							<li><label>余位：</label>${activityHotelGroup.remNumber } 间（控房${activityHotelGroup.controlNum }间，非控房${activityHotelGroup.uncontrolNum }间）</li>
							<li><label>预报名：</label>${bookingPersonNum }</li>
							<li><label>备注：</label> ${activityHotelGroup.memo }</li>
						</ul>
					</div>
					<table class="table activitylist_bodyer_table_new contentTable_preventive" id="costTable2" style="width:60% !important;">
						<thead>
							<tr>
								<th width="15%">房型&amp;晚数</th>
								<th width="15%">基础餐型</th>
								<th width="15%">升级餐型&amp;升级价格</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${groupRooms }" var="groupRoom" varStatus="groupRoomStatus">
								<tr>
									<td rowspan="${groupRoom.roomMealRiseRowspan}" class="tc"><p><trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${groupRoom.hotelRoomUuid}"/> * ${groupRoom.nights }</p></td>
									<c:forEach items="${groupRoom.activityHotelGroupMealList }" var="groupMeal" varStatus="groupMealStatus">
										<c:if test="${groupMealStatus.index == 0 }">
											<td rowspan="${fn:length(groupMeal.activityHotelGroupMealsRiseList) }" class="tc">
												<p><trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${groupMeal.hotelMealUuid}"/>&nbsp;</p>
											</td>
											<c:if test="${fn:length(groupMeal.activityHotelGroupMealsRiseList) == 0 }">
												<td class="tc">&nbsp;
												</td>
											</c:if>
											<c:forEach items="${groupMeal.activityHotelGroupMealsRiseList }" var="groupMealRise"  varStatus="groupMealRiseStatus">
												<c:if test="${groupMealRiseStatus.index == 0 }">
													<td class="tc">
														<p><trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${groupMealRise.hotelMealUuid}"/>
														<trekiz:autoId2Name4Table tableName='currency' sourceColumnName='currency_id' srcColumnName='currency_mark' value='${groupMealRise.currencyId }'/>
														${groupMealRise.price }/人</p>
													</td>
												</c:if>
												<c:if test="${groupMealRiseStatus.index > 0 }">
													</tr>
													<tr>
													<td class="tc">
														<p><trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${groupMealRise.hotelMealUuid}"/>
														<trekiz:autoId2Name4Table tableName='currency' sourceColumnName='currency_id' srcColumnName='currency_mark' value='${groupMealRise.currencyId }'/>
														${groupMealRise.price }/人</p>
													</td>
												</c:if>
											</c:forEach>
										</c:if>
									</c:forEach>
								</tr>
								
								<c:forEach items="${groupRoom.activityHotelGroupMealList }" var="groupMeal" varStatus="groupMealStatus">
									<c:if test="${groupMealStatus.index > 0 }">
										<tr>
											<td rowspan="${fn:length(groupMeal.activityHotelGroupMealsRiseList) }" class="tc">
												<p><trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${groupMeal.hotelMealUuid}"/>&nbsp;</p>
											</td>
											<c:if test="${fn:length(groupMeal.activityHotelGroupMealsRiseList) == 0 }">
												<td class="tc">&nbsp;
												</td>
											</c:if>
											<c:forEach items="${groupMeal.activityHotelGroupMealsRiseList }" var="groupMealRise"  varStatus="groupMealRiseStatus">
												<c:if test="${groupMealRiseStatus.index == 0 }">
													<td class="tc">
														<p><trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${groupMealRise.hotelMealUuid}"/>
														<trekiz:autoId2Name4Table tableName='currency' sourceColumnName='currency_id' srcColumnName='currency_mark' value='${groupMealRise.currencyId }'/>
														${groupMealRise.price }/人</p>
													</td>
												</c:if>
												<c:if test="${groupMealRiseStatus.index > 0 }">
													</tr>
													<tr>
													<td class="tc">
														<p><trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${groupMealRise.hotelMealUuid}"/>
														<trekiz:autoId2Name4Table tableName='currency' sourceColumnName='currency_id' srcColumnName='currency_mark' value='${groupMealRise.currencyId }'/>
														${groupMealRise.price }/人</p>
													</td>
												</c:if>
											</c:forEach>
										</tr>
									</c:if>
								</c:forEach>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="ydbz_tit">
					<span class="ydExpand" data-target="#bookingPeopleInfo"></span>填写预订人信息
				</div>
				<div id="bookingPeopleInfo">
					<div id="orderpersonMesdtail">
						<div class="mod_information_dzhan" id="secondStepDiv">
							<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style=" overflow:hidden;">
								<div class="mod_information_d2 ">
									<label><span class="xing">*</span>渠道：</label> 
									<select name="xunjiafgs" id="channelType" class="required">
										<option value="1">签约渠道</option>
										<option value="2">非签约渠道</option>
									</select>
								</div>
								<div class="mod_information_d2" id="signChannel">
									<label><span class="xing">*</span>渠道总社：</label>
									<%-- <select name="orderCompany" id="orderCompany" onchange="loadAgentInfo();" class="required"> --%>
									<select name="orderCompany" id="orderCompany" class="required">
										<c:forEach items="${agentList}" var="agentinfo">
											<option value="${agentinfo.id}">${agentinfo.agentName}</option>
										</c:forEach>
									</select>
								</div>
								<div class="mod_information_d2" id="nonChannel" style="display:none;">
									<label class="price_sale_house_label02"><span class="xing">*</span>非签约渠道名称：</label>
									<input type="text" name="orderCompanyName" id="orderCompanyName" class="required"/>
								</div>
							</div>
						</div>
						<p class="ydbz_qdmc"></p>
						<%-- 
						<!--签约渠道-->
						<div id="signChannelList">
							<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
								<li><label><span class="xing">*</span>渠道联系人：</label>
									<input type="text" name="orderContacts_contactsName" onafterpaste="this.value=this.value.replaceSpecialChars()" onkeyup="this.value=this.value.replaceSpecialChars()" maxlength="10" />
								</li>
								<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
									<input type="text" name="orderContacts_contactsTel" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" id="orderPersonPhoneNum" maxlength="15" />
									<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
									<span class="ydbz_x yd1AddPeople">添加联系人</span>
								</li>
								<li flag="messageDiv" class="ydbz_qd_close">
									<ul>
										<li><label>固定电话：</label> <input type="text"  name="orderContacts_contactsTixedTel" /></li>
										<li><label>渠道地址：</label> <input type="text"  name="orderContacts_contactsAddress" /></li>
										<li><label>传真：</label> <input type="text"  name="orderContacts_contactsFax" /></li>
										<li><label>QQ：</label> <input type="text"  name="orderContacts_contactsQQ" /></li>
										<li><label>Email：</label> <input type="text"  name="orderContacts_contactsEmail" /></li>
										<li><label>渠道邮编：</label> <input type="text"  name="orderContacts_contactsZipCode" /></li>
										<li><label>其他：</label> <input type="text"  name="orderContacts_remark" />
										</li>
									</ul></li>
							</ul>
						</div>
						<!--非签约渠道-->
						<div id="nonChannelList" style="display:none;">
							<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
								<li class="kong kongnofl"></li>
								<li><label><span class="xing">*</span>渠道联系人：</label> 
									<input type="text" name="orderContacts_contactsName" onkeyup="this.value=this.value.replaceSpecialChars()" onafterpaste="this.value=this.value.replaceSpecialChars()" maxlength="10"/>
								</li>
								<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
									<input type="text" name="orderContacts_contactsTel" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="15" />
									<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
									<span class="ydbz_x yd1AddPeople">添加联系人</span>
								</li>
								<li flag="messageDiv" class="ydbz_qd_close">
									<ul>
										<li>
											<label>固定电话：</label>
											<input type="text" name="orderContacts_contactsTixedTel" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" />
										</li>
										<li><label>渠道地址：</label> <input maxlength="" type="text" name="orderContacts_contactsAddress" />
										</li>
										<li><label>传真：</label> <input maxlength="" type="text" name="orderContacts_contactsFax" />
										</li>
										<li><label>QQ：</label> <input maxlength="" type="text" name="orderContacts_contactsQQ" />
										</li>
										<li><label>Email：</label> <input maxlength="" type="text" name="orderContacts_contactsEmail" />
										</li>
										<li><label>渠道邮编：</label> <input maxlength="" type="text" name="orderContacts_contactsZipCode" />
										</li>
										<li><label>其他：</label> <input maxlength="" type="text" name="orderContacts_remark" />
										</li>
									</ul>
								</li>
							</ul>
						</div>
						 --%>
						 
						 <!--签约渠道-->
						<div id="signChannelList">
							<ul class="ydbz_qd min-height" id="orderpersonMes" name="orderpersonMes">
								<li>
									<label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label>
									<span name="channelConcat"></span>
								</li>
								<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
									<input type="text" name="orderContacts_contactsTel" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" id="orderPersonPhoneNum" maxlength="15" />
									<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
									<c:if test="${allowAddAgentInfo == 1 }">
										<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
									</c:if>
								</li>
								<li flag="messageDiv" class="ydbz_qd_close">
									<ul>
										<li><label>固定电话：</label> <input type="text"  name="orderContacts_contactsTixedTel"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" /></li>
										<li><label>渠道地址：</label> <input type="text"  name="orderContacts_contactsAddress" onblur="updataInputTitle(this);" title=""/></li>
										<li><label>传真：</label> <input type="text"  name="orderContacts_contactsFax"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" /></li>
										<li><label>QQ：</label> <input type="text"  name="orderContacts_contactsQQ"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" /></li>
										<li><label>Email：</label> <input type="text"  name="orderContacts_contactsEmail" /></li>
										<li><label>渠道邮编：</label> <input type="text"  name="orderContacts_contactsZipCode"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" /></li>
										<li><label>其他：</label> <input type="text"  name="orderContacts_remark" onblur="updataInputTitle(this);" title=""/>
										</li>
									</ul></li>
							</ul>
						</div>
						<!--非签约渠道-->
						<div id="nonChannelList" style="display:none;">
							<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
								<li><label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label> 
									<input type="text" name="orderContacts_contactsName" onkeyup="this.value=this.value.replaceSpecialChars()" onafterpaste="this.value=this.value.replaceSpecialChars()" maxlength="10"/>
								</li>
								<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
									<input type="text" name="orderContacts_contactsTel" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="15" />
									<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
									<c:if test="${allowAddAgentInfo == 1 }">
										<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
									</c:if>
								</li>
								<li flag="messageDiv" class="ydbz_qd_close">
									<ul>
										<li>
											<label>固定电话：</label>
											<input type="text" name="orderContacts_contactsTixedTel" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" />
										</li>
										<li><label>渠道地址：</label> <input maxlength="" type="text" name="orderContacts_contactsAddress" onblur="updataInputTitle(this);" title="" />
										</li>
										<li><label>传真：</label> <input maxlength="" type="text" name="orderContacts_contactsFax"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" />
										</li>
										<li><label>QQ：</label> <input maxlength="" type="text" name="orderContacts_contactsQQ"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" />
										</li>
										<li><label>Email：</label> <input maxlength="" type="text" name="orderContacts_contactsEmail" />
										</li>
										<li><label>渠道邮编：</label> <input maxlength="" type="text" name="orderContacts_contactsZipCode"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" />
										</li>
										<li><label>其他：</label> <input maxlength="" type="text" name="orderContacts_remark" onblur="updataInputTitle(this);" title=""/>
										</li>
									</ul>
								</li>
							</ul>
						</div>
						 
					</div>
				</div>
				<div class="ydbz_tit">
					<span class="ydExpand" data-target="#costTable"></span>费用及人数
				</div>
				<div id="costTable">
					<table class="table activitylist_bodyer_table_new contentTable_preventive">
						<thead>
							<tr>
								<th width="13%">游客类型</th>
								<th width="25%">同行价/人</th>
								<th width="25%"><span class="xing">*</span>人数</th>
								<th width="23%">小计</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${groupPrices }" var="groupPrice">
								<tr id="${groupPrice.uuid}" class="groupPrices_tr">
									<td class="tc">
										<input type="hidden" name="hotelOrderTravelerType" value="${groupPrice.type}">
										<span name="OrderTravelerTypeName"><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.type}"/></span>
										<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="${groupPrice.uuid}" />
										<input type="hidden" name="hotelOrderPrice_remark" value="_"/>
										<input type="hidden" name="hotelOrderPrice_priceType" value="1" />
										<input type="hidden" name="hotelOrderPrice_priceName" value="_" />
										<input type="hidden" name="hotelOrderPrice_travelerType" value="${groupPrice.type}" />
									</td>
									<td class="tc">
										<span><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${groupPrice.price }" /><%-- ${groupPrice.price } --%></span>
										<input type="hidden" name="hotelOrderPrice_currencyId" value="${groupPrice.currencyId }" />
										<input type="hidden" name="hotelOrderPrice_price" value="${groupPrice.price }" />
									</td>
									<td class="tc"><input type="text" class="price_sale_house_w100" data-type="number" name="hotelOrderPrice_num"/></td>
									<td class="tc"><span></span>
									</td>
								</tr>
							</c:forEach>
							<tr>
								<td colspan="4" class="tr">
									<span class="price_sale_houser_25"><label>合计人数：</label>
										<em> <span id="totalPeopleCount"></span> 人</em>
									</span> 
									<span class="price_sale_houser_25"><label>合计金额：</label>
										<em>
											<i><span class="totalCost"></span></i>
										</em>
									</span>
								</td>
							</tr>
							<tr>
								<td colspan="5" class="tl">
									<span class="price_sale_houser_25" style="margin-left:89px;"><label>单房差：</label><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityHotelGroup.currencyId}"/> <fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activityHotelGroup.singlePrice}" /><%-- ${activityHotelGroup.singlePrice} --%></span>
									<span class="price_sale_houser_25" style="margin-left:200px;"><label>需交订金：</label><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityHotelGroup.frontMoneyCurrencyId}"/> <fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activityHotelGroup.frontMoney}" /><%-- ${activityHotelGroup.frontMoney} --%></span>
								</td>
							</tr>
						</tbody>
					</table>
					
	                <div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text" style="width:130px;">预报名间数：</div>
						<input id="kfInput" type="text" name="forecaseReportNum" data-type="number" class="inputTxt" onblur="checkedRoom(this,${activityHotelGroup.remNumber });"/>间
					</div>
					<br/>
				</div>
				
				<!--费用调整开始-->
				<div class="ydbz_tit">
					<span class="ydExpand" data-target="#adjustCost"></span>费用调整
				</div>
				<div id="adjustCost">
					<div class="mod_information_dzhan" id="dddddStepDiv">
						<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow:hidden;">
							<table class="contentTable_preventive table_padings" id="add_other_charges_table">
								<tr>
									<td width="100" class="tr">
										返佣金额：<input type="hidden" name="hotelOrderPrice_priceType" value="2" />
										<input type="hidden" name="hotelOrderPrice_num" value="_" />
										<input type="hidden" name="hotelOrderPrice_priceName" value="返佣金额" />
										<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="_" />
										<input type="hidden" name="hotelOrderPrice_travelerType" value="_" />
									</td>
									<td width="280" class="tl">
										<label> 
											<select name="hotelOrderPrice_currencyId" class="w80">
												<c:forEach items="${currencyList }" var="currency">
													<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
												</c:forEach>
											</select> 
										</label> 
										<input type="text" class="price_sale_house_w93" name="hotelOrderPrice_price" id="input5" />
									</td>
									<td width="180" class="tr">备注：</td>
									<td colspan="3">
										<span class="tl"><input type="text" class="price_sale_house_w300" name="hotelOrderPrice_remark" id="input9" /></span>
									</td>
								</tr>
								<tr class="calc_amount_tr">
									<td class="tr">
										优惠金额：<input type="hidden" name="hotelOrderPrice_priceType" value="3" />
										<input type="hidden" name="hotelOrderPrice_num" value="_" />
										<input type="hidden" name="hotelOrderPrice_priceName" value="优惠金额" />
										<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="_" />
										<input type="hidden" name="hotelOrderPrice_travelerType" value="_" />
									</td>
									<td class="tl">
										<label> 
											<select name="hotelOrderPrice_currencyId" class="w80">
												<c:forEach items="${currencyList }" var="currency">
													<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
												</c:forEach>
											</select> 
										</label> 
										<input type="text" class="price_sale_house_w93 price" name="hotelOrderPrice_price" id="input6" maxlength="" />
									</td>
									<td class="tr">备注：</td>
									<td colspan="3">
										<span class="tl"> 
											<input type="text" class="price_sale_house_w300" name="hotelOrderPrice_remark" id="input10" maxlength="" />
										</span>
									</td>
								</tr>
								<tr>
									<td class="tr">退款金额：<input type="hidden" name="hotelOrderPrice_priceType" value="5" />
										<input type="hidden" name="hotelOrderPrice_num" value="_" />
										<input type="hidden" name="hotelOrderPrice_priceName" value="退款金额" />
										<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="_" />
										<input type="hidden" name="hotelOrderPrice_travelerType" value="_" />
									</td>
									<td class="tl">
										<label>
											<select name="hotelOrderPrice_currencyId" class="w80">
												<c:forEach items="${currencyList }" var="currency">
													<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
												</c:forEach>
											</select>
										</label>
										<input type="text" name="hotelOrderPrice_price" class="price_sale_house_w93" id="input6" />
									</td>
									<td class="tr">备注：</td>
									<td colspan="3">
										<span class="tl">
                                            <input type="text" name="hotelOrderPrice_remark" class="price_sale_house_w300" id="input10" />
                                        </span>
										<span class="padr10"></span>
                                        <input value="增加其他费用" style="width:auto;" class="btn btn-primary" type="button" onclick="add_other_charges();" />
									</td>
								</tr>
								
							</table>
						</div>
					</div>
				</div>
				<!--费用调整结束-->
				<!--费用结算开始-->
				<div class="ydbz_tit">
					<span class="ydExpand" data-target="#costSettlement"></span>费用结算
				</div>
				<div id="costSettlement">
					<div class="mod_information_dzhan">
						<div class="mod_information_dzhan_d error_add1"
							id="oneStepContent" style=" overflow:hidden;">
							<div class="mod_information_d2">
								<label>订单总额：</label><span class="totalCost"></span>
							</div>
							<div class="mod_information_d2 ">
								<label>结算总额：</label><span class="red accounts"></span>
							</div>
						</div>
					</div>
					<p class="price_sale_houser_line"></p>
					<div class="mod_information_dzhan">
						<div class="mod_information_dzhan_d error_add1"
							id="oneStepContent" style=" overflow:hidden;">
							<div class="mod_information_d2">
								<label>应收金额：</label><span class="accounts"></span>
							</div>
							<div class="mod_information_d2 ">
								<label>已收金额：</label><span><!-- ￥ --></span><span>0</span>
							</div>
							<div class="mod_information_d2 ">
								<label>未收金额：</label><span class="green unReceipted"></span>
							</div>
						</div>
					</div>
				</div>
				<!--费用结算结束-->
				<!--旅客信息开始-->
				<div class="ydbz_tit">
					<span class="ydExpand" data-target="#passengerInfo"></span>游客信息
				</div>
				<div id="passengerInfo">
					<input value="添加游客信息" onclick="add_tours_obj();" class="btn btn-primary" type="button" />
					<table id="passengerInfoTable" class="table activitylist_bodyer_table_new contentTable_preventive" style="min-width:1400px;display:none;">
						<thead>
							<tr>
								<th width="3%">序号</th>
								<th width="6%"><span class="xing">*</span>姓名</th>
								<th width="3%">英文姓名</th>
								<th width="8%">游客类型</th>
								<th width="7%">性别</th>
								<th width="12%">签证国家及类型</th>
								<th width="25%">证件类型/证件号码/有效期</th>
								<th width="13%">价格</th>
								<th width="8%">备注</th>
								<th width="6%">资料上传</th>
								<th width="6%">操作</th>
							</tr>
						</thead>
						<tbody>
							
						</tbody>
					</table>
					<!--旅客信息结束--> 
					<!--备注开始-->
					<div flag="messageDiv" class="ydbz2_lxr">
	                    <p class="hotel_discount_count_mar20">
	                        <label>备注：</label>
	                        <textarea style="width: 786px; height: 83px;" name="remark" onkeyup="wordsDeal();" id="hotelOrderRemark"></textarea>
	                                                                剩余<span id="textCount">500</span>个字<br />
	                    </p>
	                </div>
					<!--备注结束-->
				</div>
				<div class="release_next_add">
					<input value="取消" onclick="window.close()" class="btn btn-primary gray" type="button" /> 
					<input value="提交预报名" onclick="submitSignUp();" class="btn btn-primary" type="button" id="submitSignUpButton" />
					<input value="提交并收款" onclick="submitAndReceive();" class="btn btn-primary" type="button" id="submitAndReceiveButton" />
				</div>
			</div>
		</form:form>
		<!--右侧内容部分结束-->
		
		<!-- 其他费用模板 -->
		<table>
			<tr class="add_other_charges calc_amount_tr" style="display:none;">
				<td class="tr">金额名称：
					<input type="hidden" name="hotelOrderPrice_priceType" value="4"/>
					<input type="hidden" name="hotelOrderPrice_num" value="_" />
					<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="_" />
					<input type="hidden" name="hotelOrderPrice_travelerType" value="_" />
				</td>
				<td>
					<span class="tl">
						<input type="text" class="price_sale_house_w93" name="hotelOrderPrice_priceName" id="input8" maxlength="" />
					</span>
				</td>
				<td class="tr tr_other_u">
					<input name="other_u" type="radio" class="dis_inlineblock" id="u138_input" value="radio" checked="checked" data-label="增加" />
					<label for="u138_input">增加</label>
					<input name="other_u" type="radio" id="u138_input2" value="radio" data-label="减少" />
					<label for="u140_input">减少：</label>
				</td>
				<td class="tl">
					<label>
						<select name="hotelOrderPrice_currencyId" class="w80">
							<c:forEach items="${currencyList }" var="currency">
								<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
							</c:forEach>
						</select>
					</label>
					<input type="text" class="price_sale_house_w93 price" name="hotelOrderPrice_priceBak" id="input7" />
					<input type="hidden" name="hotelOrderPrice_price"/>
				</td>
				<td class="tr">备注：</td>
				<td class="tl">
					<input type="text" class="price_sale_house_w93" name="hotelOrderPrice_remark" id="input11" />
					<span class="padr10"></span>
					<i class="price_sale_house_02" onclick="del_other_charges(this);"></i>
				</td>
			</tr>
		</table>
		<!-- 其他费用模板 -->
		
		<!-- 游客模板信息 -->
		<table>
			<tr style="display:none;" id="add_tours_obj_tr">
				<td class="tc">add_tours_obj_tr_index</td>
				<td class="tc"><input type="text" name="hotelTraveler_name" class="price_sale_house_w93 required"/></td>
				<td class="tc"><input type="text" name="hotelTraveler_nameSpell" class="price_sale_house_w93" /></td>
				<td class="tc">
					<select name="hotelTraveler_personType" class="w80 display_inline" onchange="setTitle(this);">
						<c:forEach items="${groupPrices }" var="groupPrice">
							<option value="${groupPrice.type}"><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.type}"/></option>
						</c:forEach>
					</select>
					<script type="text/javascript" language="javascript">
						function setTitle(obj){
							$(obj).attr('title',$(obj).find("option:selected").text());
						}
					</script>
				</td>
				<td class="tc">
					<select name="hotelTraveler_sex" class="price_sale_house_w60 display_inline">
						<option value="1">男</option>
						<option value="2">女</option>
					</select>
				</td>
				<td class="tc table_padings_none">
					<input type="hidden" name="hotelTraveler_visaInfo" />
					<p class="hotelTraveler_visaInfo">
						<input id="suggestcountry" type="text" autocomplete="off" name="suggestcountry" class="suggest" style="width: 70px;" value="" />
						<select name="visaTypeId" class="w40b">
							<c:forEach items="${visaTypes }" var="visa">
								<option value="${visa.id }">${visa.label}</option>
							</c:forEach>
						</select>
						<i class="price_sale_house_01"></i>
					</p>
				</td>
				<td>
					<input type="hidden" name="hotelTraveler_papersType" />
					<p class="hotelTraveler_papersType">
						<select name="papersType" class="w80">
							<option value="">请选择</option>
							<option value="1">身份证</option>
							<option value="2">护照</option>
							<option value="3">警官证</option>
							<option value="4">军官证</option>
							<option value="5">其他</option>
						</select> 
						<input type="text" name="idCard" class="w130 input_pad" />
						<input type="text" name="validityDate" onclick="WdatePicker()" class="dateinput w90 input_pad" />
						<i class="price_sale_house_01"></i>
					</p>
				</td>
				<td class="tc">
					<input type="hidden" name="hotelTraveler_amount" />
					<p class="hotelTraveler_amount">
						<select name="currency" class="w30b">
							<c:forEach items="${currencyList }" var="currency">
								<option value="${currency.id }">${currency.currencyName}</option>
							</c:forEach>
						</select> 
						<input type="text" name="price" class="w30b price" /> 
						<i class="price_sale_house_01"></i>
					</p>
				</td>
				<td class="tc"><input type="text" name="hotelTraveler_remark" class="price_sale_house_w93" /></td>
				<td class="tc">
					<a name="" class="btn_addBlue_file" id="addcost" onclick="up_files_pop(this);">附件管理</a> 
					<!--上传附件弹窗层开始-->
					<div class="up_files_pop" style="display:none;">
						<ul style="margin-left:0;">
						</ul>
						<a name="addFiles" class="btn_addBlue_file" onclick="uploadFiles('${ctx}','',this,'false')">上传附件</a>
					</div>
					<!--上传附件弹窗层开始--></td>
				<td class="tc"><a onclick="save_tours_obj(this)">保存</a> | <a onclick="del_tours_obj(this)">删除</a></td>
			</tr>
		</table>
		<!-- 游客模板信息 -->
	</div>
	
	<!--提交预报名弹窗层开始-->
	<div class="forecast-name-submit" style="display:none;" id="forecast-name-submit">
		<ul>
			<li style="height:50px; line-height:50px; text-align:center;">您确认提交预报名吗？&nbsp;&nbsp;&nbsp;&nbsp;</li>
		</ul>
	</div>
	<!--提交预报名弹窗层结束-->
	
	<!--提交并收款弹窗层开始-->
	<div class="forecast-name-submit-collections" style="display:none;" id="forecast-name-submit-collections">
		<ul>
			<li style="height:50px; line-height:50px; text-align:center;">您确认提交预报名并进行收款吗？&nbsp;&nbsp;&nbsp;&nbsp;</li>
		</ul>
	</div>
	<!--提交并收款弹窗层结束-->
	
</body>
</html>
