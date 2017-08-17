<%@ page contentType="text/html;charset=UTF-8" %>
<style type="text/css">
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
    cursor:auto;
    background:transparent;
    border:0px;
    box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
}
</style>
<script src="${ctxStatic}/common/jquery.number.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/single/ordercommon.js?v=1209" type="text/javascript"></script>
<script type="text/javascript">
    var currencyList = "";
    var tempFirstContactName = '${agentinfo.agentContact}';
    var agentId = ${productorder.orderCompany };
    var address = '${address}' == "null"?"":'${address}';
    var zipCode = '${zipCode}' == "null"?"":'${zipCode}';
    $(function(){
	    //加载币种数据源
	    currencyList = eval(${currencyListJsonArray});
	    // 散拼产品使用quauq价报名，显示quauq价
	    if ($("#activityKind").val() == '2' && $("#agentSourceType").val() == '2') {
	    	$("li[name=quauqPriceShow]").show();
			$("li[name=settlementPriceShow]").hide();
		   	$("li[name=suggestPriceShow]").hide();
		   	$("#priceType").val(2);
		   	$("#totalSPGPrice").text("QUAUQ价");
		   	switchPirce(2);  // 切换使用quauq价
		   	
		   	if ($("#preOrderId").val() != "") {
		   		$("#orderPosition").val($("#allNum").val());
		   		$("#orderPersonNumAdult").val($("#adultNum").val()).trigger("blur");
		   		$("#orderPersonNumChild").val($("#childNum").val()).trigger("blur");
		   		$("#orderPersonNumSpecial").val($("#specialNum").val()).trigger("blur");
		   	}
		   	
		} else {  // 否则默认显示同行价
		    $("li[name=settlementPriceShow]").show();
		   	$("li[name=suggestPriceShow]").hide();
		   	$("li[name=quauqPriceShow]").hide();
		   	if($("#priceType").val()==null){
		   		$("#priceType").val(0);
		   	}
		   	$("#totalSPGPrice").text("同行价");
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
            $border.append('<input type="text" maxlength="45" name="contactsName" value="' + options.firstContactName + '" class="'+options.inputCss+'"/>');
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
    
//     	var allowModifyAgentInfo = ${allowModifyAgentInfo };
    $(function(){
		var allowModifyAgentInfo = $("#orderContact_modifiability").val();
	    if(!allowModifyAgentInfo){
			allowModifyAgentInfo = 0;
		}
	    if(allowModifyAgentInfo == 0 && agentId != -1 ){
			//初始化时为第一联系人填充信息(不可修改)
			var initDom = $("input[name=contactsName][type=hidden]").prev();
			fillContactInfo2(initDom);
			//不可修改时，把联系人input置为只读（'其他'除外）
			setContactInfoReadonly();
	    } 
	    if (agentId != -1) {
	    	//可以修改时
	        var dataStr = ${contactsJsonStr};  //自己组织的data
// 	        var dataArrayStr = '${contactArray }';  //conta转换的jsonArray
// 	        var dataArray = eval(dataArrayStr);
	        var dataArray = eval('${contactArrayView}');
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
	// 	    为添加的联系人绑定事件,因第一个已添加,所以过滤掉
		    $(document).on('click','[name="channelConcat"]:not(:first) em',function(){
		        var $ul = $(this).next();
		        if(!$ul.is(':visible')){
		            $ul.show();
		        }else{
		            $ul.hide();
		        }
		    });
	// 	    为下拉项添加点击事件,同样过滤掉第一个
		    $(document).on('click','[name="channelConcat"]:not(:first) ul li',function(){
	// 	        根据当前选中的li为文本框赋值
		        $(this).parents('.combox_border:first').children(':text').val($(this).text()).attr('uuid',$(this).find('a').attr('uuid'));
		        $(this).parent().hide();
	// 	        获取选中项信息
				//选择联系人填充其他信息						
				for(var i=0; i<dataArray.length; i++){
					var tempData = dataArray[i];
					if(tempData.uuid == $(this).find('a').attr('uuid')){
	            		fillContactInfo(this, tempData);
		        	}
				}
// 		        alert("uuid:"+$(this).find('a').attr('uuid'));
		    });
	// 	    过滤特殊字符
		    $(document).on('keyup','[name="channelConcat"] input',function(){
		        $(this).val(replaceSpecialChars.apply($(this).val()));
		    });
		    $(document).on('afterpaste','[name="channelConcat"] input',function(){
		        $(this).val(replaceSpecialChars.apply($(this).val()));
		    });
	    
			//初始化时为第一联系人填充信息(可修改)
			var firstDom = $(document).find("span[name=channelConcat]:first");
			fillContactInfo(firstDom, dataArray[0]);	    	    	
	  		//重新给联系人排序
			resortContacts();
	    }
    });
    
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
    
    //检查adult直客价是否为空
    function isAdultBlank(){
    	if($("input[name=suggestAdultPrice]").val() == undefined || $("input[name=suggestAdultPrice]").val() == null || $("input[name=suggestAdultPrice]").val() == ""){
    		return true;
    	}
    	return false;
    }
    //检查child直客价是否为空
    function isChildBlank(){
    	if($("input[name=suggestChildPrice]").val() == undefined || $("input[name=suggestChildPrice]").val() == null || $("input[name=suggestChildPrice]").val() == ""){
    		return true;
    	}
    	return false;
    }
    //检查special直客价是否为空
    function isSpecialBlank(){
    	if($("input[name=suggestSpecialPrice]").val() == undefined || $("input[name=suggestSpecialPrice]").val() == null || $("input[name=suggestSpecialPrice]").val() == ""){
    		return true;
    	}
    	return false;
    }
    
    //选取报名基本价格类型，同行价、直客价
	function chosePriceType(obj){
    	var priceTypeCode = $(obj).val();
    	if (priceTypeCode == 0) {
    		$("li[name=settlementPriceShow]").show();
    		$("li[name=suggestPriceShow]").hide();
    		$("li[name=quauqPriceShow]").hide();
    		$("#priceType").val(0);
	   		switchPirce(priceTypeCode);
	   		$("#oneToSecondStepDiv").show();
	   		//改变文字
	   		$("#totalSPGPrice").text("同行价");
    	} else if (priceTypeCode == 1) {
	    	//检查直客价为空（所有游客类型都为空才message）
	    	if(isAdultBlank() == true && isChildBlank() == true && isSpecialBlank() == true){
	    		top.$.jBox.tip("直客价为空不能报名！", "error");
		    	$("#oneToSecondStepDiv").hide();
	    	}
    		$("li[name=settlementPriceShow]").hide();
    		$("li[name=suggestPriceShow]").show();
    		$("li[name=quauqPriceShow]").hide();
    		$("#priceType").val(1);
	   		switchPirce(priceTypeCode);
	   		//改变文字
	   		$("#totalSPGPrice").text("直客价");
    	} else if (priceTypeCode == 2) {
    		$("li[name=quauqPriceShow]").show();
    		$("li[name=settlementPriceShow]").hide();
    		$("li[name=suggestPriceShow]").hide();
    		$("#priceType").val(2);
    		//改变文字
	   		$("#totalSPGPrice").text("QUAUQ价");
    	} else {
    		$("li[name=settlementPriceShow]").show();
    		$("li[name=suggestPriceShow]").hide();
    		$("#priceType").val(0);
    		$.jBox.tip("获取报名价格类型失败！");
    		//改变文字
	   		$("#totalSPGPrice").text("同行价");
    	}
    	// 切换价格体系之后，改变相应游客人数输入框状态
    	disableInput4NullPrice();
    }
    
    //切换使用的价格类型
    function switchPirce(priceTypeCode){
    	//同行价
    	if(priceTypeCode == 0){
    		$("input[name=adultCurrencyId]").val($("input[name=settlementAdultCurrencyId]").val());
    		$("input[name=childCurrencyId]").val($("input[name=settlementChildCurrencyId]").val());
    		$("input[name=specialCurrencyId]").val($("input[name=settlementSpecialCurrencyId]").val());
    		$("input[name=adultCurrencyMark]").val($("input[name=settlementAdultCurrencyMark]").val());
    		$("input[name=childCurrencyMark]").val($("input[name=settlementChildCurrencyMark]").val());
    		$("input[name=specialCurrencyMark]").val($("input[name=settlementSpecialCurrencyMark]").val());
    		$("input[name=adultCurrencyName]").val($("input[name=settlementAdultCurrencyName]").val());
    		$("input[name=childCurrencyName]").val($("input[name=settlementChildCurrencyName]").val());
    		$("input[name=specialCurrencyName]").val($("input[name=settlementSpecialCurrencyName]").val());
    		$("input[name=settlementAdultPrice]").val($("input[name=settlementAdultPriceSrc]").val());
    		$("input[name=settlementcChildPrice]").val($("input[name=settlementChildPriceSrc]").val());
    		$("input[name=settlementSpecialPrice]").val($("input[name=settlementSpecialPriceSrc]").val());
    	} else if (priceTypeCode == 1) {
    		$("input[name=adultCurrencyId]").val($("input[name=suggestAdultCurrencyId]").val());
    		$("input[name=childCurrencyId]").val($("input[name=suggestChildCurrencyId]").val());
    		$("input[name=specialCurrencyId]").val($("input[name=suggestSpecialCurrencyId]").val());
    		$("input[name=adultCurrencyMark]").val($("input[name=suggestAdultCurrencyMark]").val());
    		$("input[name=childCurrencyMark]").val($("input[name=suggestChildCurrencyMark]").val());
    		$("input[name=specialCurrencyMark]").val($("input[name=suggestSpecialCurrencyMark]").val());
    		$("input[name=adultCurrencyName]").val($("input[name=suggestAdultCurrencyName]").val());
    		$("input[name=childCurrencyName]").val($("input[name=suggestChildCurrencyName]").val());
    		$("input[name=specialCurrencyName]").val($("input[name=suggestSpecialCurrencyName]").val());
    		$("input[name=settlementAdultPrice]").val($("input[name=suggestAdultPrice]").val());
    		$("input[name=settlementcChildPrice]").val($("input[name=suggestChildPrice]").val());
    		$("input[name=settlementSpecialPrice]").val($("input[name=suggestSpecialPrice]").val());
    	} else if (priceTypeCode == 2) {
    		$("input[name=adultCurrencyId]").val($("input[name=retailAdultCurrencyId]").val());
    		$("input[name=childCurrencyId]").val($("input[name=retailChildCurrencyId]").val());
    		$("input[name=specialCurrencyId]").val($("input[name=retailSpecialCurrencyId]").val());
    		$("input[name=adultCurrencyMark]").val($("input[name=retailAdultCurrencyMark]").val());
    		$("input[name=childCurrencyMark]").val($("input[name=retailChildCurrencyMark]").val());
    		$("input[name=specialCurrencyMark]").val($("input[name=retailSpecialCurrencyMark]").val());
    		$("input[name=adultCurrencyName]").val($("input[name=retailAdultCurrencyName]").val());
    		$("input[name=childCurrencyName]").val($("input[name=retailChildCurrencyName]").val());
    		$("input[name=specialCurrencyName]").val($("input[name=retailSpecialCurrencyName]").val());
    		$("input[name=settlementAdultPrice]").val($("input[name=retailAdultPrice]").val());
    		$("input[name=settlementcChildPrice]").val($("input[name=retailChildPrice]").val());
    		$("input[name=settlementSpecialPrice]").val($("input[name=retailSpecialPrice]").val());
    	} else {
    		// TODO 其他价格来源
    	}
    }

/**
 * 不可修改时，把联系人input置为只读（'其他'除外）
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
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsName]").val(tempData.text);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsTel]").val(tempData.contactMobile);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsTixedTel]").val(tempData.contactPhone);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsFax]").val(tempData.contactFax);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsQQ]").val(tempData.contactQQ);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsEmail]").val(tempData.contactEmail);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsZipCode]").val(zipCode);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsAddress]").val(address);
}
/**
 * 选择联系人，填充对应信息（对应不可修改）(test)
 */
function fillContactInfo2(obj){
	var contactDataArray = $(obj).val().split(',');
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsName]").val(contactDataArray[1]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsTel]").val(contactDataArray[2]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsTixedTel]").val(contactDataArray[3]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsFax]").val(contactDataArray[4]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsQQ]").val(contactDataArray[5]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsEmail]").val(contactDataArray[6]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsAddress]").val(address);
	if(contactDataArray[8]) {
		$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsZipCode]").val(contactDataArray[8]);
	} else {
		$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsZipCode]").val(zipCode);
	}
// 	给隐藏的名字赋值
// 	$("input[name=contactsName][type=hidden]").val(contactDataArray[1]);
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
    $newUl.find("input[name=nagentName]").parent().remove();
    //修改联系人序号
    $newUl.find("span[name=contactNo]").html(contactNo);
    $newUl.find('input').val('');
    $newUl.find('select').append('<option value="" selected="selected"></option>');
    $newUl.children('li').eq(0).find('label font').html(parseInt(contactPeopleNum) + 1);
    $newUl.children('li').eq(1).find('span.yd1AddPeople').remove();
    $newUl.children('li').eq(1).append('<span class="ydbz_x gray" onclick="yd1DelPeople(this)">删除联系人</span>');
    $currentUl.parent().append($newUl);
    //重新给联系人排序
	resortContacts();
}
function yd1DelPeople(obj) {
	contactNo--;
    $(obj).parent().parent().remove();
    //重置联系人序号
    //if ($('#orderpersonss option:selected').val() == '1'|| $(obj).parents().find('#orderpersonss').length==0) {
    $("ul[name=orderpersonMes]").each(function (index, element) {
        $(element).children("li").find("span[name=contactNo]").text(index + 1);
    });
}

/**
 * 填写非签约渠道是给渠道名称赋值
 */
function setCompanyName(obj){
	$("#orderCompanyName").val($(obj).val());
}

</script>