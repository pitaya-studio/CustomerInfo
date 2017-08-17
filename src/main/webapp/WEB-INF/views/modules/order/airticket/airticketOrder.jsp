<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<title>预定-机票填写下单信息</title>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" /><meta http-equiv="Pragma" content="no-cache" /><meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/airticket/airticketOrder.js" type="text/javascript"></script>
<%--t2改版 去掉重复引用的样式 modified by Tlw--%>
<style type="text/css">
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
    cursor:auto;
    background:transparent;
    border:0px;
    box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
}
</style>
<style>
	.ydbzbox {
     background: #FFF none repeat scroll 0% 0%;
     font-size: 12px;
     height: auto !important;
     min-height: 581px;
     overflow: hidden;
 	}
 	input.required[readonly] {
    cursor: not-allowed;
    background-color: #eee;
	}
</style>

<script type="text/javascript">
g_context_url = "${ctx}";
	var tempFirstContactName = '${agentinfo.agentContact}';
	var agentId = ${agent.id};
	var address = '${address}';	
//    	var allowModifyAgentInfo = ${allowModifyAgentInfo };
    var dataArray = eval('${contactArrayView}');
	$(function(){
	    
		//各块信息展开与收起
		$(".ydClose").click(function(){
			var obj_this = $(this);
			if(obj_this.attr("class").match("ydExpand")) {
				obj_this.removeClass("ydExpand");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
			} else {
				obj_this.addClass("ydExpand");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
			}
		});
		
	});
	
	$(function(){
		var allowModifyAgentInfo = $("#orderContact_modifiability").val();
	    if(!allowModifyAgentInfo){
			allowModifyAgentInfo = 0;
		}
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
	    
	    	//初始化时为第一联系人填充信息(不可修改)(只针对签约渠道)
	    	if(agentId != -1){
				fillContactInfo($("input[name=contactsName]:first"), dataArray[0]);
	    	}
			//初始化时为第一联系人填充信息(可修改)
// 			var firstDom = $("span[name=channelConcat]:first");
// 			fillContactInfo(firstDom, dataArray[0]);
		if(allowModifyAgentInfo == 0 && agentId != -1){			
			//不可修改时，把联系人input置为只读（'其他'除外）
			setContactInfoReadonly();
    	}
    	//重新排序
		resortContacts();

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
        $border.append('<input maxlength="45" type="text" name="contactsName" value="' + options.firstContactName + '" class="'+options.inputCss+'"/>');
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
    
/**
 * 不可修改时，把联系人input置为只读（'其他'除外）
 */    
function setContactInfoReadonly(){
	var $mesDom = $("ul[name=orderpersonMes]");
	if(agentId != -1){		
		$mesDom.find("[name=contactsTel]").attr("disabled",true);  //联系电话为必填项
	}
	$mesDom.find("[name=contactsTixedTel]").attr("disabled",true);
	$mesDom.find("[name=contactsFax]").attr("disabled",true);
	$mesDom.find("[name=contactsQQ]").attr("disabled",true);
	$mesDom.find("[name=contactsEmail]").attr("disabled",true);
	$mesDom.find("[name=contactsAddress]").attr("disabled",true);
	$mesDom.find("[name=contactsZipCode]").attr("disabled",true);
	$("input[name=contactsName]").attr("disabled", true);
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
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsAddress]").val(tempData.agentAddressFull);
}
/**
 * 选择联系人，填充对应信息（对应不可修改）(test)
 */
function fillContactInfo2(obj){
	if($(obj).val() == undefined || $(obj).val() == null){return;}
	var contactDataArray = $(obj).val().split(',');
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsName]").val(contactDataArray[1]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsTel]").val(contactDataArray[2]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsTixedTel]").val(contactDataArray[3]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsFax]").val(contactDataArray[4]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsQQ]").val(contactDataArray[5]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsEmail]").val(contactDataArray[6]);
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsAddress]").val(address);
// 	给隐藏的名字赋值
// 	$("input[name=contactsName][type=hidden]").val(contactDataArray[1]);
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
    // forbug 不能添加联系人
	$newUl.find("[name=contactsTel]").attr("disabled",false);
	$newUl.find("[name=contactsTixedTel]").attr("disabled",false);
	$newUl.find("[name=contactsFax]").attr("disabled",false);
	$newUl.find("[name=contactsQQ]").attr("disabled",false);
	$newUl.find("[name=contactsEmail]").attr("disabled",false);
	$newUl.find("[name=contactsAddress]").attr("disabled",false);
	$newUl.find("[name=contactsZipCode]").attr("disabled",false);
	$newUl.find("input[name=contactsName]").attr("disabled", false);
	
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
    resortContacts();
}
/**
 * 删除渠道联系人
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

//得到焦点事件：隐藏填写费用名称提示
function payforotherIn(doc) {
    var obj = $(doc);
    obj.siblings(".ipt-tips2").hide();
}

//失去焦点事件：如果输入框中没有值，则提示填写费用名称
function payforotherOut(doc){
    var obj = $(doc);
    if(!obj.val()){
        obj.siblings(".ipt-tips2").show();
    }
}

//点击提示错误信息中 "修改" 后错误输入框得到焦点
function focusIpt(doc){
    $(doc).parent().find('input[type=text].ipt2').trigger("focus");
}

function validateIdCard(obj){
    var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
   var idCard = obj.value;
    if(!reg.test(idCard) && idCard != ""){
        obj.value="";
        var name = $(obj).parent().children("label").text();
        top.$.jBox.tip('请输入正确的' + name.replace("：",""),'warning');
               $(this).focus();
               throw "error！"; 
    }
}
	
function valdGR(grvalue){
	
	var result = -1;		
	//整数
	var rex0 = new RegExp("\^\\d+\$");
	if (rex0.test(grvalue)) {
		result = 0;
	}
	//一位小数
	var rex1 = new RegExp("\^\\d+\\.\\d\$");
	if (rex1.test(grvalue)) {
		result = 1;
	}
	//两位小数
	var rex2 = new RegExp("\^\\d+\\.\\d{2}\$");
	if (rex2.test(grvalue)) {
		result = 2;
	}
	return result;
}
	
//验证返佣费用合法性
function checkRebatesValue(obj){
	var money = obj.value;  
	if(money){
		if(money >= 0){
			var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
			var txt = ms.split(".");
			obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
		}else{
			obj.value = '0';
		}
	}
}
</script>

</head>
<body>
<input type="hidden" value="${fns:getUser().company.uuid }" id="companyUuid" name="companyUuid">
<input type="hidden" value="${payMode }" id="payMode" name="payMode">
<input type="hidden" value="${placeHolderType }" id="placeHolderType" name="placeHolderType">
<input type="hidden" value="${activity.id }" id="productId">
<input type="hidden" value="${activity.depositamt}" id="depositamt">
<input type="hidden" value="${freePosion }" id="freePosition">
<input id="orderid" type="hidden" value="">
<input id="orderno" type="hidden" value="">
<input id="businessType" type="hidden" value="1">
<input id="isCommonOrder" type="hidden" value="airticket">

<input id="orderPosition" type="hidden" value="0">
<input id="agentId" type="hidden" value="${agent.id }">
<input type="hidden" value="${activity.settlementcChildPrice}" id="etj">
<input type="hidden" value="${activity.settlementAdultPrice}" id="crj">
<input type="hidden" value="${activity.settlementSpecialPrice}" id="tsj">
<input type="hidden" value="${activity.taxamt}" id="taxamt">
<input type="hidden" value="${currencyMark}" id="bz" name="currencyMark">
<input type="hidden" value='${airticketbz}' id="airticketbz">
<input type="hidden" value='${bzJson}' id="bzJson">
<input type="hidden" value='${orderType}' id="orderType">
<input type="hidden" value='${salerId}' id="salerId">
<input type="hidden" value="${activity.istax ==0 ? 0 : activity.taxamt }" id="texamt">

<!-- 预订单位名称和id -->
<input id="orderCompanyName" type="hidden" value="非签约渠道">
<input id="orderCompany" type="hidden" value="-1">

<!-- 游客模板 -->
<div id="travelerTemplate" style="display: none;">
<form>
	<div class="tourist">
		<div class="tourist-t">
			<a class="btn-del" style="cursor:pointer;" name="deleteTraveler" >删除</a>

            <label class="ydLable">游客<em class="travelerIndex" style="vertical-align: baseline;"></em>:</label>
            <label><input type="radio" class="traveler" id="personTypeinner1" name="personTypeinner" value="1" checked="checked"/>成人</label>
			<label><input type="radio" class="traveler" id="personTypeinner2" name="personTypeinner" value="2" />儿童</label>
			<label><input type="radio" class="traveler" id="personTypeinner3" name="personTypeinner" value="3" />特殊人群</label>
					<div class="tourist-t-off">
						<span class="fr"> <b style="font-size: 18px">同行价：</b><span
							class="ydFont2"></span> <b style="font-size: 18px">结算价：</b><span
							id="spjg" class="ydFont2"></span>
						</span>
						<span name="tName"></span>
					</div>

				</div>
		
		<div class="tourist-con" flag="messageDiv">
			<!--游客信息左侧开始-->
			<div class="tourist-left">
			<div class="tourist-t-r">
              <p><em class="xing">*</em >是否联运：
              		<label><input type="radio" class="ydbz2intermodal1" name="ydbz2intermodalType" value="0" checked="checked" onclick="ydbz2intermodal(this)"/>不需要</label>
                 	<c:if test="${activity.intermodalType!=0}">
                 	<label><input type="radio" name="ydbz2intermodalType"  value="1" onclick="ydbz2intermodal(this)" />需要</label>
                 	</c:if>
                 <span style="display:none">
	                 <select onchange="ydbz2lyselect(this)" name="lysel">
	                      <option value="0"  selected="selected">请选择</option>
	                 	<c:forEach items="${activity.intermodalStrategies}" var="list" varStatus="s">                                
	                        <option  value="${list.id},${list.priceCurrency.id},${list.priceCurrency.currencyName},${list.priceCurrency.currencyMark},${list.price}"  >${list.groupPart}</option>
	                    </c:forEach>
	                 </select>
	              		币种：<i id="bzName"></i>联运价格：<span id="bzMark"></span><em></em><input type="hidden" name="intermodalId"  value=""/>
              	</span>
              	
              	</p>
            </div>
            <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
                     <input type="hidden"  name="intermodalId"  value=""/>
      				<ul class="tourist-info1 clearfix" flag="messageDiv">
	                <li>
	                	<label class="ydLable"><span class="xing">*</span>姓名：</label>
	                	<input type="text" maxlength="30" name="travelerName" class="traveler required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
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
	                    <select name="nationality" class="selCountry" id="conturySel">
	                          <c:forEach items="${countrys}" var="country">
	                             <option value="${country.key }">${country.value }</option>
	                          </c:forEach>
                                
                            </select>
                        </li>
                        
                        <li>
                            <label class="ydLable">出生日期：</label>
                            <input type="text" maxlength="" name="birthDay" class="traveler dateinput" onclick="WdatePicker()">
                        </li>
                        
                        <li>
                            <label class="ydLable"><span class="xing"></span>联系电话：</label>
                            <input type="text" style="130px;" name="telephone" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
                        </li>
                        <li>
                        	<label class="ydLable">护照号：</label>
	                            <input type="text" name="passportCode" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/^(P\d{8}|G\d{9})$/,'')"  onafterpaste="this.value=this.value.replace(/^(P\d{8}|G\d{9})$/,'')" >
                        	
                        </li>
                        <li>
                        	<label class="ydLable">护照有效期：</label>
	                            <input type="text" maxlength="" name="validityDate" class="traveler dateinput" onclick="WdatePicker()">
                        	
                        </li>
                        <li>
                            <label class="ydLable">身份证号：</label>
                            <input type="text" name="idCard" style="width: 130px;" class="traveler" maxlength="18" onkeyup=""  onafterpaste="validateIdCard(this)" onblur="validateIdCard(this);getBirthday(this);">
                        </li>	                
                    </ul>
                <div class="ydbz_tit ydbz_tit_child">　　备注：<textarea class="textarea_long" name="remark"></textarea></div>
                
			</div> 
			
			<!--游客信息右侧开始-->
            <div class="tourist-right">
            	<div class="bj-info">            		
					<div class="clearfix">
						<div class="traveler-rebatesDiv">
							<!-- 265需求，针对鼎鸿假期，将所有返佣字段改为宣传费 -->
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
	               			<input type="text" class="ipt-rebates"  name="rebatesMoney"  maxlength="9" onafterpaste="checkRebatesValue(this)" onkeyup="checkRebatesValue(this)"/>
						</div>
					</div>					
            	</div>
                <div class="yd-total clearfix">
					<div class="fr">
						<label class="ydLable2">同行价：</label><span class="ydFont2"></span>
						<input type="hidden" value="" class="traveler" name="payPrice"/>
				    	<input type="hidden" value="-1,0,0,0" class="traveler" name="lyPrice"/>
					</div>
					<div class="fr">
						<label class="ydLable2">结算价：</label><span data="newJsPrice" class="ydFont2"></span>						
					</div>
				</div>				 
			</div>
			<!--游客信息右侧结束-->
            
		</div>
        <!--保存、取消按钮开始-->
            <div class="rightBtn"><a class="btn" name="savePeople" onclick="savePeopleTableData(this)"/>保存</a></div>
            <!--保存、取消按钮结束-->
	</div>
</form>
</div>

<div class="ydbz yd-step1 " id="stepbar" >&nbsp;</div>
  <div class="ydbz_tit">订单基本信息</div>
  <p class="ydbz_mc">
  
  ${fns:getDictLabel(activity.departureCity, 'from_area', '无')}
  &mdash;
						
						  <c:forEach items="${arrivedareas}" var="area">
                        <c:if test="${area.id eq activity.arrivedCity}">
                            ${area.name}
                        </c:if>
                    </c:forEach>
						：
						<c:choose><c:when test="${activity.airType eq 3 }">单程</c:when>
						  <c:when test="${activity.airType eq 2 }">往返</c:when>
						  <c:when test="${activity.airType eq 1 }">多段</c:when>
						</c:choose>
									
  <c:forEach items="${activity.flightInfos}" var="list" varStatus="status">
  <c:if test="${status.index==0&&activity.airType!=1}">
  <c:choose><c:when test="${list.ticket_area_type==1}">（内陆）</c:when>
	  <c:when test="${list.ticket_area_type==2}">（国际）</c:when>
	  <c:when test="${list.ticket_area_type==3}">（内陆+国际）</c:when>
	</c:choose></p>
  </c:if>
  
  <c:choose>
  <c:when test="${activity.airType==3}">
  <!--单程-->
  </c:when>
  <c:when test="${activity.airType==2}">
  <!--往返-->
  <c:if test="${status.index==0}">
  <div class="ydbz_tit ydbz_tit_child">去程：</div>
  </c:if>
  <c:if test="${status.index==1}">
  <div class="ydbz_tit ydbz_tit_child">返程：</div>
  </c:if>
  </c:when>
  <c:when test="${activity.airType==1}">
  <!--多段-->
	<div class="ydbz_tit ydbz_tit_child">第${status.count}段：
		<c:choose>
			<c:when test="${list.ticket_area_type==1}">（内陆）</c:when>
			<c:when test="${list.ticket_area_type==2}">（国际）</c:when>
			<c:when test="${list.ticket_area_type==3}">（国际+内陆）</c:when>
			<c:when test="${list.ticket_area_type==4}">（国内）</c:when>
		</c:choose>
	</div>
  </c:when>
  </c:choose>
  
  <table width="90%" border="0">
		<tbody><tr>
			<td class="mod_details2_d1">出发地机场：</td>
			<td class="mod_details2_d2">
			<c:forEach items="${airportMap}" var="entry" varStatus="vs">
			   <c:if test="${ entry.key == list.leaveAirport}">
			   ${ entry.value.airportName }
			   </c:if>
			</c:forEach>
			
			<td class="mod_details2_d1">到达城市机场：</td>
			<td class="mod_details2_d2">
            <c:forEach items="${ airportMap}" var="entry" varStatus="vs">
			   <c:if test="${ entry.key == list.destinationAirpost}">
			   ${ entry.value.airportName }
			   </c:if>
			</c:forEach>
            </td>
            
		 </tr>
		 <tr>
			<td class="mod_details2_d1">出发时刻：</td>
			<td class="mod_details2_d2"><fmt:formatDate value="${list.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
			<td class="mod_details2_d1">到达时刻：</td>
			<td class="mod_details2_d2"><fmt:formatDate value="${list.arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>		
			<td class="mod_details2_d1">航班号：</td>
            <td class="mod_details2_d2">${list.flightNumber}</td>
		</tr>
		 <tr>
			<td class="mod_details2_d1">航空公司：</td>
			<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(list.airlines)}</td>
			<td class="mod_details2_d1">舱位等级：</td>
			<td class="mod_details2_d2"> ${fns:getDictLabel(list.spaceGrade, "spaceGrade_Type", "")}</td>		
			<td class="mod_details2_d1">舱位：</td>
			<td class="mod_details2_d2">${fns:getDictLabel(list.airspace, "airspace_Type", "")}</td>
		 </tr>
		 <tr>
		 	<td class="mod_details2_d1">销售：</td>
			<td class="mod_details2_d2">${fns:getUserById(salerId).name}</td>
			<td class="mod_details2_d1">下单人：</td>
			<td class="mod_details2_d2">${fns:getUser().name}</td>
		 </tr>
		</tbody>
	  </table> 					
  </c:forEach>
  
   <div class="seach25 seach100">
      <p class="fbold f14">预收人数：</p>
      <div>${activity.reservationsNum}</div> 
      <p class="fbold f14"> 余位数：</p>
      <div>${freePosion}</div> 
  </div>
  <div class="mod_information_d7"></div>
                            
       <form id="productOrderTotal">
           <ul class="ydbz_dj specialPrice">
           <li style="display: none;">
           <input type="text" id="orderPersonelNum" onkeyup="this.value=this.value.replace(/\D/g,'')" 
  			onafterpaste="this.value=this.value.replace(/\D/g,'')" value="0" class="required" >
           
           </li>
              <li><span class="ydtips">单价</span>
                 <p>成人：<font color="#FF0000" >${currencyMark }<span id="sAdultPrice"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activity.settlementAdultPrice }" /></span></font> </p>
                 <p>儿童：<font color="#FF0000">${currencyMark }<span id="sChildPrice"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activity.settlementcChildPrice }" /></span></font></p>
                 <p>特殊人群：<font color="#FF0000">${currencyMark }<span id="sSpecialPrice"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activity.settlementSpecialPrice }" /></span></font></p>
              </li>
            
              <li>
              	<span class="ydtips"><span class="xing">*</span>出行人数</span>
                 	<p>成人：
                 		<input type="text" id="orderPersonNumAdult"  
                 		onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')"
                              onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" value="0" class="required">人
    				</p>
                   <p>儿童：
                   		<input type="text" id="orderPersonNumChild" 
                   		onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')" 
  					onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" value="0" 
  					class="required" ${activity.maxChildrenCount <=  (counts.orderPersonNumChild ==null?0:counts.orderPersonNumChild)?"readOnly='true'":""}
  					> 人
  					<input type="hidden" id="maxChildrenCount" value="${activity.maxChildrenCount }" />
  					<input type="hidden" id="lastChildrenCount" value="${counts.orderPersonNumChild ==null?0:counts.orderPersonNumChild}" />
  				</p>
  				<p>特殊人群：
                 		<input type="text" id="orderPersonNumSpecial" 
                 		onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')" 
  					onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" value="0" ${activity.maxPeopleCount <=  (counts.orderPersonNumSpecial==null?0:counts.orderPersonNumSpecial)?"readOnly='true'":""}
  					class="required" 
  					> 人
  					<input type="hidden" id="maxPeopleCount" value="${activity.maxPeopleCount }" />
  					<input type="hidden" id="lastPeopleCount" value="${counts.orderPersonNumSpecial==null?0:counts.orderPersonNumSpecial }" />
  				</p>
              </li>
              
              <li class="ydbz_single">
				 <span class="">税费：</span>
				 <c:if test="${activity.istax ==0  }">
					 	<font color="#FF0000">已含</font>
				 </c:if>
				 <c:if test="${activity.istax !=0  }">
				 	<font color="#FF0000">${currencyMark }
					 <fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activity.istax ==0 ? 0 : activity.taxamt }" />
					 </font> 
				 </c:if>
				 <div style="width:300px; white-space:nowrap; text-overflow:ellipsis; overflow:hidden;"><span>特殊人群备注: </span>
				 <span title="${activity.specialremark}">${activity.specialremark}</span>
				 </div>
			  </li>
           </ul>
       </form>
                 
        <div class="ydbz_tit"><span class="ydExpand closeOrExpand"></span>填写预订人信息</div>
        <shiro:hasPermission name="airticket:orderContact:modifiability">
			<input type="hidden" value="1" id="orderContact_modifiability">
			<c:set var="orderContact_modifiability" value="1"></c:set>
		</shiro:hasPermission>
		<shiro:hasPermission name="airticket:orderContact:addibility">
			<input type="hidden" value="1" id="orderContact_addibility">
			<c:set var="orderContact_addibility" value="1"></c:set>
		</shiro:hasPermission>
		<div flag="messageDiv">
			<form id="orderpersonMesdtail">
				<p class="ydbz_qdmc">
					<c:choose>
						<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and agent.id eq -1 }"><label><span class="xing">*</span>未签约渠道名称：</label><input maxlength="50" type="text" name="nagentName"  id="nagentName" class="required"/></c:when>
						<c:when test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and agent.id eq -1 }"><label><span class="xing">*</span>
						<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
							直客名称：
						</c:if>
						<c:if test="${companyUuid ne  '7a81b21a77a811e5bc1e000c29cf2586'}">
							非签约渠道名称：
						</c:if>
						
						</label><input maxlength="50" type="text" name="nagentName"  id="nagentName" class="required"/></c:when>
						<c:otherwise>签约渠道名称:${agent.agentName}</c:otherwise>
					</c:choose>
				</p>
				<div id="ordercontact">
	            	<c:choose>
		            	<%-- 非签约渠道,显示普通的输入框 --%>
	                	<c:when test="${agent.id eq -1}">
		              		<ul class="ydbz_qd" id="unSignedAgentInfo" name="orderpersonMes">
			                    <li>
			                    	<label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label>
			                    	<input maxlength="45" type="text" id="orderPersonName" name="contactsName" value="" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/>
		                    	</li>
		                    	<li class="ydbz_qd_lilong">
			                    	<label><span class="xing">*</span>渠道联系人电话：</label>
				                    <input maxlength="20" name="contactsTel" type="text" id="orderPersonPhoneNum" value="${agent.agentContactMobile}" class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/>
				                    <div class="zksx modify-order boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
				                    <shiro:hasPermission name="airticket:orderContact:addibility">
						           		<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
					           		</shiro:hasPermission>
			                    </li>
			                    <li flag="messageDiv" style="display:none" class="ydbz_qd_close">
			                    	<ul>
				                    	<li><label>固定电话：</label><input name="contactsTixedTel" type="text" value="${agent.agentFixedPhone}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="20"/></li>
				                        <li><label>渠道地址：</label><input onblur="updataInputTitle(this);" title="${agent.agentAddress}"  name="contactsAddress" type="text" value="${agent.agentAddress}" maxlength="200"/></li>
				                        <li><label>传真：</label><input  type="text" name="contactsFax" value="${agent.agentFax}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/></li>
				                        <li><label>QQ：</label><input  type="text" name="contactsQQ" value="${agent.agentQQ}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/></li>
				                        <li><label>Email：</label><input  type="text" name="contactsEmail" value="${agent.agentEmail}" maxlength="50"/></li>
				                        <li><label>渠道邮编：</label><input  type="text" name="contactsZipCode" value="${agent.agentPostcode}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/></li>
				                        <li><label>其他：</label><input onblur="updataInputTitle(this);" title="" maxlength="" type="text" name="remark" value="" maxlength="200"/></li>
				                	</ul>
			                	</li>
			                </ul>
	                		</c:when>
		                	<%-- 签约渠道,显示下拉等效果 --%>
	                		<c:otherwise>
							<ul class="ydbz_qd min-height" id="unSignedAgentInfo" name="orderpersonMes">
			                    <li>
			                    	<label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label>
			                    	<span name="channelConcat"></span>
		                    	</li>
			                    <li class="ydbz_qd_lilong">
			                    	<label><span class="xing">*</span>渠道联系人电话：</label>
				                    <input maxlength="20" name="contactsTel" type="text" id="orderPersonPhoneNum" value="${agent.agentContactMobile}" class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/>
				                    <div class="zksx modify-order boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
				                    <shiro:hasPermission name="airticket:orderContact:addibility">
						           		<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
					           		</shiro:hasPermission>
			                    </li>
			                    <li flag="messageDiv" style="display:none" class="ydbz_qd_close">
			                    	<ul>
				                    	<li><label>固定电话：</label><input name="contactsTixedTel" type="text" value="${agent.agentFixedPhone}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="20"/></li>
				                        <li><label>渠道地址：</label><input onblur="updataInputTitle(this);" title="${agent.agentAddress}" name="contactsAddress" type="text" value="${agent.agentAddress}" maxlength="200"/></li>
				                        <li><label>传真：</label><input type="text" name="contactsFax" value="${agent.agentFax}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/></li>
				                        <li><label>QQ：</label><input type="text" name="contactsQQ" value="${agent.agentQQ}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/></li>
				                        <li><label>Email：</label><input type="text" name="contactsEmail" value="${agent.agentEmail}" maxlength="50"/></li>
				                        <li><label>渠道邮编：</label><input type="text" name="contactsZipCode" value="${agent.agentPostcode}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/></li>
				                        <li><label>其他：</label><input onblur="updataInputTitle(this);" title="" type="text" name="remark" value="" maxlength="200"/></li>
				                	</ul>
			                	</li>
		                	</ul>
	                	</c:otherwise>
	            	</c:choose>
            	</div>
			</form>
		</div>
		
		<div class="ydBtn-down orderPersonMsg" style="display:none;" ><span>&nbsp;</span></div>

		<div class="ydbz_sxb clear" id="oneToSecondOutStepDiv" ><div class="ydBtn" id="oneToSecondStepDiv"><span class="ydbz_x" >下一步</span></div></div>

  <div id="manageOrder_m"  style='display:none;' >
  <div id="contact">
  <div class="ydbz_tit"> <span class="ydExpand closeOrExpand"></span> 特殊需求</div>
  <div class="ydbz2_lxr" flag="messageDiv">
     <form class="contactTable">
                 <p>
                    <label style="vertical-align:top">特殊需求：</label><input type="hidden" name ="id" value="">
                    <textarea name="remark" id="specialDemand" placeholder="最多可输入500字" maxlength="500" 
							  onkeyup="this.value=this.value.replaceSpecialDemand();" onafterpast="this.value=this.value.replaceSpecialDemand();"></textarea>
                 </p>
     </form>
  </div>
  <div class="ydBtn-down orderPersonMsg" style="display:none;" ><span>&nbsp;</span></div>
  </div>
  </div>
  <div  id="manageOrder_new"  style='display:none;' >
  <div class="ydbz_tit">请填写游客信息</div>
 <div class="warningtravelerNum">暂无游客信息</div>
 <div id="traveler"></div>
  
    <!--添加游客按钮开始-->
    <div class="touristBtn"><a class="btn-addGrey" id="addTraveler">添加游客</a></div>
    <!--添加游客按钮结束-->
    
    <!-- 预计团队返佣 add start by jiangyang -->
    <div  class="traveler-rebatesDiv">
    	<form name="groupRebates">
    	<!-- 265需求，针对鼎鸿假期，将所有返佣字段改为宣传费 -->
	         <c:choose>
	         	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					<label class="ydLable2 ydColor1">预计团队宣传费：</label>
				</c:when>
	            <c:otherwise>
	               <label class="ydLable2 ydColor1">预计团队返佣：</label>
	             </c:otherwise>
	         </c:choose>   
			<select name="groupRebatesCurrency">
				<c:forEach var="bz" items="${bzList }">
					<option value="${bz.currencyId }">${bz.currencyName }</option>
				</c:forEach>								
			</select>
		   	<input type="text" class="required ipt-rebates" name="groupRebatesMoney"  maxlength="9" onafterpaste="checkRebatesValue(this)" onkeyup="checkRebatesValue(this)" 
		   	placeholder="金额" onblur="		
				if(valdGR(this.value)==1){this.value=this.value + '0'}
				else if(valdGR(this.value)==0){this.value=this.value + '.00'}
				else if(valdGR(this.value)==2){}
				else if(valdGR(this.value)==-1){this.value=''}"/>
	   	</form>
    </div>
  	<!-- 预计团队返佣 add end   by jiangyang -->
  
    <div class="ydbz_sxb" id="secondDiv"  style='display:none;' >
        <div class="ydBtn ydBtn2">
            <a class="ydbz_s" id="secondToOneStepDiv" >上一步</a>
            <a class="ydbz_x" id="secondToThirdStepDiv">下一步</a>
        </div>
    </div>
    <div class="ydbz_sxb" id="thirdDiv"  style='display:none;'>
        <div class="ydBtn ydBtn2">
            <div class="ydbz_s" id="thirdToSecondTStepDiv" >上一步</div>
            <div class="ydbz_x" id="thirdToFourthStepDiv" onClick="$(this).attr('id','');$(this).attr('class','ydbz_s gray');" <c:if test="${payMode eq '2'}">style="display:none"</c:if>>
       <c:choose>
       <c:when test="${payMode eq '7' or payMode eq '8'}">保存</c:when>
       <c:otherwise>保存并收款</c:otherwise>
       </c:choose>     
            </div>
            <div class="ydbz_x" id="closeButon" onClick="closeOrder();" <c:if test="${payMode ne '2'}">style="display:none"</c:if>>保存并关闭</div>
        </div>
    </div>
        <div style="text-align:right; font-size:12px; margin-top:20px;  padding-top:10px; padding-right:10px;">
        	<b style="font-size:18px">订单总同行价：</b><span id="travelerSumPrice" class="tdred f20"></span><br>
        	<div style="padding-top:10px;"><b style="font-size:18px">订单总结算价：</b><span id="travelerSumPrice_js" class="tdred f20">0</span></div>
        </div>
  </div>
<script type="text/javascript">
//默认选中中国
$(document).ready(function() {
	$(':input[name=nationality] option[value=461]').attr('selected',true);


	$("textarea[id=specialDemand]").focusin(function(){
		$(this).removeAttr("placeholder");
	}).focusout(function(){
		$(this).attr("placeholder","最多可输入500字");
	});

	//112 特殊需求过滤特殊字符
	String.prototype.replaceSpecialDemand= function(regEx){
		if (!regEx){
			regEx = /[\“\”\‘\’\"\'\<\>\\]/g;
		}
		return this.replace(regEx, '');
	};
});
</script>

<script type="text/javascript">
$(document).ready(function(e) {
    var leftmenuid = $("#leftmenuid").val();
    $(".main-nav").find("li").each(function(index, element) {
        if($(this).attr("menuid")==leftmenuid){
            $(this).css("background", "url(/resource/images/img/pfs_nav_hover.png)");
        }
    });
});

$(function(){

    $('.closeNotice').click(function(){
        var par = $(this).parent().parent();
        par.hide();
        par.prev().removeClass('border-bottom');
        par.prev().find('.notice-date').show();
    });
    $('.showNotice').click(function(){
        $(this).parent().hide();
        var par = $(this).parent().parent();
        par.addClass('border-bottom');
        par.next().show();
    });
});

$(function(){
	$('.main-nav li').click(function(){
		$(this).addClass('select').siblings().removeClass('select');
	})
})

String.prototype.formatNumberMoney= function(pattern){
	  var strarr = this?this.toString().split('.'):['0'];   
	  var fmtarr = pattern?pattern.split('.'):[''];   
	  var retstr='';   
	  var str = strarr[0];
	  var fmt = fmtarr[0];
	  var i = str.length-1;     
	  var comma = false;   
	  for(var f=fmt.length-1;f>=0;f--){   
	    switch(fmt.substr(f,1)){   
	      case '#':   
	        if(i>=0 ) retstr = str.substr(i--,1) + retstr;   
	        break;   
	      case '0':   
	        if(i>=0) retstr = str.substr(i--,1) + retstr;   
	        else retstr = '0' + retstr;   
	        break;   
	      case ',':   
	         comma = true;   
	         retstr=','+retstr;   
	        break;   
	     }   
	   }   
	  if(i>=0){   
	    if(comma){   
	      var l = str.length;   
	      for(;i>=0;i--){   
	         retstr = str.substr(i,1) + retstr;   
	        if(i>0 && ((l-i)%3)==0) retstr = ',' + retstr;   
	       }   
	     }   
	    else retstr = str.substr(0,i+1) + retstr;   
	   }   
	  
	   retstr = retstr+'.';   
	   
	   str=strarr.length>1?strarr[1]:'';   
	   fmt=fmtarr.length>1?fmtarr[1]:'';   
	   i=0;   
	  for(var f=0;f<fmt.length;f++){   
	    switch(fmt.substr(f,1)){   
	      case '#':   
	        if(i<str.length) retstr+=str.substr(i++,1);   
	        break;   
	      case '0':   
	        if(i<str.length) retstr+= str.substr(i++,1);   
	        else retstr+='0';   
	        break;   
	     }   
	   }   
	  return retstr.replace(/^,+/,'').replace(/\.$/,'');   
}

String.prototype.replaceSpecialChars=function(regEx){
	if(!regEx){
		regEx = /[\`\"\~\!\@#$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@#\¥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？]/g;
	}
	return this.replace(regEx,'');
	
};



</script>
</body>
</html>
