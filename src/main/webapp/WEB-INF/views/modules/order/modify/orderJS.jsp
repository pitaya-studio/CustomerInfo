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
<%-- <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script> --%>
<script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<link href="${ctxStatic}/css/order-style.css" type="text/css" rel="stylesheet" />
<c:if test="${orderTitle ne '修改' }">
	<script src="${ctxStatic}/modules/order/single/ordercommon.js?v=1209" type="text/javascript"></script>
</c:if>
<script type="text/javascript">
    var currencyList = "";
    var priceType = '${priceType}';
    var dataArrayStr2 = '${contactArray}';
//     var dataStr = "[" + '${contactsJsonStr}' + "]";
	var dataStr = ${contactsJsonStr};
	var tempFirstContactName = '${agentinfo.agentContact}';
	var dataArrayStr = '${contactArray }';  //conta转换的jsonArray
	var dataArray = eval(dataArrayStr);
	var agentinfoId = ${agentinfo.id };
	var address = '${address }';
//  var allowModifyAgentInfo = ${allowModifyAgentInfo };
// 	var allowAddAgentInfo = ${allowAddAgentInfo };
    var priceJson = '';
    <c:if test="${not empty productGroup.priceJson}">
        priceJson = ${productGroup.priceJson};
    </c:if>
    $(function(){
    	var allowModifyAgentInfo = $("#orderContact_modifiability").val();
		if(!allowModifyAgentInfo){
			allowModifyAgentInfo = 0;
		}
		var allowAddAgentInfo = $("#orderContact_addibility").val();
		if(!allowAddAgentInfo){
			allowAddAgentInfo = 0;
		}
        //加载币种数据源
        currencyList = eval(${currencyListJsonArray});
        
        //初始加载时，订单的联系人
        var orderContactsList = eval(${orderContactsListJsonArray });
        var orderContactsNum = orderContactsList.length;        
	    
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
	    
		//初始化时为ordercontact联系人填充信息
	    $("#ordercontact").find("ul[name=orderpersonMes]").each(function(index, element){
	    	initialContactInfo(element, orderContactsList[index]);
	    });
	    //如果不是可输入修改，disabled下拉和除了“其他”之外的input
	    if(allowModifyAgentInfo == 0 && agentinfoId != -1){
	    	$("input[name=contactsName]").attr("disabled", true);
		    setContactInfoReadonly();
	    }
	    //如果不是可添加，隐藏掉添加联系人按钮
	    if(allowAddAgentInfo == 0){
	    	$("span[name=addContactButton]").hide();
	    }
	    //初始化时未联系人分配序号
	    resortContacts();
    });

/**
 * 初始化填充对应信息（对应可修改）
 */
function initialContactInfo(obj, tempData){
	if($(obj).attr("name") == "orderpersonMes"){
		$(obj).find("[name=contactsName]").val(tempData.contactsName);
		$(obj).find("[name=contactsName]").attr("maxlength", 45);
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
 * 给联系人排序号
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
        for(var i=0; i<dataArray.length; i++){
			var tempData = dataArray[i];
			if(tempData.uuid == $(this).find('a').attr('uuid')){
	       		fillContactInfo(this, tempData);
	       	}
		}
    });
    //重拍序号
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
$(function(){
$(".what").hover(function(){
	$(".what_child").show();
},function(){
		$(".what_child").hide();
})
});
</script>