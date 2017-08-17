<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>填写签证订单信息</title>

<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
 <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script> 
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/visa/visapreordercommon.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/visa/visaOrder.js" type="text/javascript"></script>
<style type="text/css">
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
    cursor:auto;
    background:transparent; 
    border:0px;   
    box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
}

.ipt3, input[type="text"].ipt3 {
 width: 38px;
  margin-left: 10px;
  margin-right: 0;
  padding-left: 17px;
 }
 .ydbzbox {
     background: #FFF none repeat scroll 0% 0%;
     font-size: 12px;
     height: auto !important;
     min-height: 581px;
     overflow: hidden;
 	}
</style>
<script type="text/javascript">
/**
 * 对应签证填写下单信息页面
 * C346&C406---wenchao
 */
 var isAllowModifyAgentInfo=${isAllowModifyAgentInfo};//渠道联系人是否允许被修改
 var isAllowAddAgentInfo=${isAllowAddAgentInfo};//渠道联系人是否允许被添加
 
 //对应需求号  0211 星辉四海  签证预定添加  预计反团时间
 var companyUUIDvar= '${fns:getUser().company.uuid}';
 
 //可下拉可修改
 $.fn.combox = function(options) {
     var defaults = {
         borderCss: "combox_border",
         inputCss: "combox_input required",
         buttonCss: "combox_button",
         selectCss: "combox_select",
         value:"${agentinfo.agentContact}",//初始化相关属性和值
         datas:[],
         onSelect:null
     };
     var options = $.extend(defaults, options);

     function _initBorder($border) {//初始化外框CSS
         $border.css({'display':'inline-block', 'position':'relative'}).addClass(options.borderCss);
         return $border;
     }

     function _initInput($border){//初始化输入框
    	 if(isAllowModifyAgentInfo=="0"){
            $border.append('<input maxlength="45" type="text" name="contactsName" class="'+options.inputCss+'" value="'+options.value+'" readonly="readonly"/>');
    	 }else{ 
         	$border.append('<input maxlength="45" type="text" name="contactsName" class="'+options.inputCss+'" value="'+options.value+'"/>');
    	 }
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

var agentInfoId = ${agentId };//渠道id
var agentAddressStr  =  "${agentAddressStr}";  //渠道地址
var zipCode = '${zipCode}' == "null"?"":'${zipCode}';  // 渠道邮编
//给相关属性赋值
//result 当前响应返回的结果对象
//相应的document对象
function contacts(result,obj){
	if(result==null){
		  obj.find("input[name=contactsName]").val("${agentinfo.agentContact}");
		  obj.find("input[name=contactsQQ]").val("${agentinfo.agentContactQQ}");
		  obj.find("input[name=contactsTel]").val("${agentinfo.agentContactMobile}");
		  obj.find("input[name=contactsTixedTel]").val("${agentinfo.agentContactTel}");
		  obj.find("input[name=contactsFax]").val("${agentinfo.agentContactFax}");
		  obj.find("input[name=contactsEmail]").val("${agentinfo.agentContactEmail}");
		  obj.find("input[name=contactsZipCode]").val(zipCode);
		  obj.find("input[name=contactsAddress]").val(agentAddressStr);
		  obj.find("input[name=remark]").val("");
	}else{
		  obj.find("input[name=contactsName]").val(result.contactName);//联系人名字
		  obj.find("input[name=contactsQQ]").val(result.contactQQ);//联系人QQ
		  obj.find("input[name=contactsTel]").val(result.contactMobile);//渠道联系人电话
		  obj.find("input[name=contactsTixedTel]").val(result.contactPhone);//联系人固定电话
		  obj.find("input[name=contactsFax]").val(result.contactFax);//联系人传真
		  obj.find("input[name=contactsEmail]").val(result.contactEmail);//联系人Email
		  obj.find("input[name=contactsZipCode]").val(zipCode);//渠道邮编
		  obj.find("input[name=remark]").val("");//其他
		  obj.find("input[name=contactsAddress]").val(agentAddressStr);//渠道地址
	}
}

  function supplier(){
     if(${supplierContacts}=='-1'){
    	 supplierContacts='';
     }
 } 
 var supplierContacts=${supplierContacts};//当前渠道的联系人、非签约渠道为空
$(function(){
	supplier();
    $('[name="channelConcat"]').combox({datas:supplierContacts,
        //回调函数,可在此函数中获取选择的联系人的uuid,但只对原始的联系人框有效,对于新增的联系人在第28行获取选中项信息
        onSelect:function(obj){
        	//当选值变化时会发送一次异步请求
        	var $obj=$(obj).parents("ul[name=orderpersonMes]");
        	$.ajax({
        		type:"POST",
        		url:"${ctx}/visa/preorder/findOneSupplyContacts",
        		dataType:"json",
				data : {
						"id" : $(obj).find('a').attr('uuid'),
						"agentInfoId" : agentInfoId
		                },
				  async: false,
				  success:function(result){
					  contacts(result,$obj);
				  }
       			 });
        }
    });
    //为添加的联系人绑定事件,因第一个已添加,所以过滤掉
    $(document).on('click','[name="channelConcat"]:not(:first) em',function(){
        var $ul = $(this).next();
        if(!$ul.is(':visible')){
            $ul.show();
        }else{
            $ul.hide();
        }
    });
    //为下拉项添加点击事件,同样过滤掉第一个
    $(document).on('click','[name="channelConcat"]:not(:first) ul li',function(){
        //根据当前选中的li为文本框赋值        
        var $obj = $(this).parents('.combox_border:first').parents("ul[name=orderpersonMes]");
        $(this).parents('.combox_border:first').children(':text').val($(this).text()).attr('uuid',$(this).find('a').attr('uuid'));
        $(this).parent().hide();
        //获取选中项信息
        $.ajax({
        		type:"POST",
        		url:"${ctx}/visa/preorder/findOneSupplyContacts",
        		dataType:"json",
				data : {
						"id" :$(this).find('a').attr('uuid'),
						"agentInfoId" : agentInfoId
		                },
				  async: false,
				  success:function(result){
					  contacts(result,$obj);
				  }
       			 });
    })
    //过滤特殊字符
    $(document).on('keyup','[name="channelConcat"] input',function(){
        $(this).val(replaceSpecialChars.apply($(this).val()));
    })
    $(document).on('afterpaste','[name="channelConcat"] input',function(){
        $(this).val(replaceSpecialChars.apply($(this).val()));
    })
})
</script>
<script type="text/javascript">
g_context_url = "${ctx}";
var currencyList = "";

	$(function(){
		/***************************************************************/
		/*112需求-特殊备注的提示展示										   */
		/***************************************************************/
            $("#specialremark").focus(function(){
                $(".ipt-tips").hide();
            });

            $("#specialremark").blur(function(){
                if($("#specialremark").val()==''){
                    $(".ipt-tips").show();
                }else{
                $(".ipt-tips").hide();
            }
            });
		
		//各块信息展开与收起

		
		$(".closeOrExpand").click(function(){
			var obj_this = $(this);alert(obj_this.attr("class"));
			if(obj_this.attr("class").match("ydClose")) {
				obj_this.removeClass("ydClose");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
			} else {
				obj_this.addClass("ydClose");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
			}
		});
		
	    //加载渠道信息
		loadAgent();
		
	});
	function getCurDate(){
			var curDate = new Date();
			return curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+(curDate.getDate()+1);
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
	
    $(function(){
        //加载币种数据源
        currencyList = eval(${currencyListJsonArray});
        //添加其他费用时触发
        var addcostindex = 0;
        $("#traveler").delegate("a[name='addcost']","click",function() {
            var $this = $(this);
            var $table = $this.next();
            var travelerIndex = $this.closest("form").find(
                    ".travelerIndex");
            addcostindex++;
            var _div = $('<div class="payfor-other cost costrmb"><input type="hidden" name="id">'
                    + '<select name="currency" onchange="changeCostCurrency(this)">'
                    + '${currencyList}'
                    +'</select>'
                    + '<input type="text" name="name" onfocus="payforotherIn(this)" maxlength="50" onblur="payforotherOut(this)" id="costname'
                    + travelerIndex.text()
                    + addcostindex
                    + '" class="required ipt2" />'
                    + '<span class="ipt-tips2" onclick="focusIpt(this)">费用名称</span>'
                    + '<input type="text"  id="costvalue'
                    + travelerIndex.text()
                    + addcostindex
                    + '" name="sum" style="" value="0"  maxlength="15" class="required number ipt3" onafterpaste="changeSum(this)" onkeyup="changeSum(this)">'
                    + '<a name="deleltecost" class="btn-del1"></a>'
                    + '</div>');
            $table.append(_div);
            
            changePayPriceByCostChange($this.closest("form"));
        });
    });
    
  //预定第一步添加信息

    function yd1AddPeoples(obj) {
       
        var contactPeopleNum = $("ul[name=orderpersonMes]").length;
        
        var $currentUl = $(obj).parents('ul[name="orderpersonMes"]');
        var $newUl = $currentUl.clone();
        //$newUl.find('li:first').remove();
    	$newUl.find('input').val('');
        $newUl.children('li').eq(0).find('label font').html(parseInt(contactPeopleNum) + 1);
        $newUl.children('li').eq(1).find('span.yd1AddPeople').remove();
        $newUl.children('li').eq(1).append('<span class="ydbz_x gray" onclick="yd1DelPeople(this)">删除联系人</span>');
        $currentUl.parent().append($newUl);
    }
</script>
<script type="text/javascript">
/***************************************************************/
		/*0073需求-签证附件添加展示										   */
/***************************************************************/
//上传文件时，点击后弹窗进行上传文件(多文件上传)
//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
function uploadFilesvisa(ctx, inputId, obj, isSimple) {
	var fls=flashChecker();
	var s="";
	if(fls.f) {
//		alert("您安装了flash,当前flash版本为: "+fls.v+".x");
	} else {
		alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件"); 
		return;
	}
	
	//新建一个隐藏的div，用来保存文件上传后返回的数据
	if($(obj).parent().find(".uploadPath").length == 0)
		$(obj).parent().append('<div class="uploadPath" style="display: none" id="uploadPathDiv"></div>');
	
	$(obj).addClass("clickBtn");
	
	//默认为多文件上传
	if(isSimple == null) {
		isSimple = "false";
	}
	
	$.jBox("iframe:"+ ctx +"/MulUploadFile/uploadFilesPage?isSimple=" + isSimple, {
	//$.jBox("iframe:"+ ctx, {
	    title: "文件上传",
		width: 340,
		height: 365,
		buttons: {'完成上传':true},
		persistent:true,
		loaded: function (h) {},
		submit: function (v, h, f) {
			$(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
			//这里拼接本次上传文件的原名称
			var fileIDList = "";
			var fileNameList = "";
			var filePathList = "";
			//
			if($(obj).parent().find("[name='docID']").length != 0) {
				$(obj).parent().find("[name='docID']").each(function(index, obj) {
					if(null != isSimple && "false"!=isSimple) {
						fileIDList = $(obj).val();
					}else{
						fileIDList +=$(obj).val()+",";
					}
				});
			}
			if($(obj).parent().find("[name='docOriName']").length != 0) {
				$(obj).parent().find("[name='docOriName']").each(function(index, obj) {
					if(null != isSimple && "false"!=isSimple) {
						fileNameList = $(obj).val();
					}else{
						fileNameList +=$(obj).val()+";" ;
					}
				});
			}
			if($(obj).parent().find("[name='docPath']").length != 0) {
				$(obj).parent().find("[name='docPath']").each(function(index, obj) {
					if(null != isSimple && "false"!=isSimple) {
						filePathList = $(obj).val();
					}else{
						filePathList += $(obj).val()+",";
					}
				});
			}
			//在这里将原名称写入到指定id的input中
			//if(inputId)
			//	$("#" + inputId).val(fileNameList);
			//该函数各自业务jsp都写一个，里面的内容根据自身页面要求自我实现
			commenFunction(obj,fileIDList,fileNameList,filePathList);
			$("#uploadPathDiv").remove();
			$(".clickBtn",window.parent.document).removeClass("clickBtn");
			fileNameList = "";
		}
	});
	$(".jbox-close").hide();
}
</script>
<style type="text/css">
.travelerIndex {
	margin-bottom: 9px;
    display: inline-block;
}
</style>
</head>
<body>

<div class="ydbzbox fs">
<input type="hidden" value="${empty groupReserve.leftpayReservePosition?0:groupReserve.leftpayReservePosition}" id="leftpayReservePosition">
    <!--  订单ID 顶单保存成功后返回 -->
    <input type="hidden" value="" id="orderid" name="orderid" >
    <!-- 记录订单的付款方式 -->
    <input type="hidden" value="${payType}" id="payType" name="payType" >
    
    <input type="hidden" value="${productorder.placeHolderType}" id="placeHolderType" name="placeHolderType">
    <input type="hidden" value="${productorder.payMode}" id="payMode" name="payMode">
    <input type="hidden" value="${productorder.remainDays}" id="remainDays" name="remainDays">
    <input type="hidden" value="${fns:getUser().company.uuid }" id="companyUuid" name="companyUuid">
    <input type="hidden" value="${productorder.payDeposit}" id="payDeposit" name="payDeposit">
    <input type="hidden" value="${productorder.frontMoney}" id="frontMoney" name="frontMoney">
    <input type="hidden" value="${frontMoneyCurrencyId}" id="frontMoneyCurrencyId" name="frontMoneyCurrencyId">
    <!-- 应收价格 -->
    <input type="hidden" value="${visaPay}" name="settlementAdultPrice" id="crj">
    
    <input type="hidden" value="${productorder.orderPersonNum}" id="orderPosition" name="orderPosition" >
    <input type="hidden" value="${productorder.orderCompany}" id="orderCompany" name="orderCompany">
    <input type="hidden" value="${productorder.orderCompanyName}<c:if test='${productorder.orderCompany == -1 && empty productorder.orderCompanyName}'>非签约渠道</c:if>" id="orderCompanyName" name="orderCompanyName">
    
    <!-- 签证订单付款状态 -->
    <input type="hidden" value="" id="payStatus" name="payStatus">
    
    <input type="hidden" value="${product.id}" id="productId" name="productId">
    <input type="hidden" value="${productGroup.id}" id="productGroupId" name="productGroupId">
    <input type="hidden" value="${productGroup.singleDiff }" id="singleDiff" name="singleDiff">

    
    <input type="hidden" value="${agentId}" id="agentId" name="agentId" >
    <input type="hidden" value="${activityKind}" id="activityKind" name="activityKind" >
    
    <input type="hidden" value="${salerId}" id="salerId" name="salerId" >
   
    
    <!-- 符号名称 -->
    <input type="hidden" value="${currency.id}" id="crbz" name="adultCurrencyId">
    <input type="hidden" value="${currency.currencyMark}"  id="crbzm" name="adultCurrencyMark">
    <input type="hidden" value="${currency.currencyName}"  id="crbmc" name="adultCurrencyName">
    
 
<!-- 预订单位名称和id -->
<input id="orderCompanyName" type="hidden" value="非签约渠道">
<input id="orderCompany" type="hidden" value="-1">

<!-- 游客模板 -->
<div id="travelerTemplate" style="display: none;">
<form name="travelerForm">
    <input type="hidden" name="travelerOrderId">
    <input type="hidden" value="<fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/>" name="groupOpenDate">
    <input type="hidden" value="${singleDiffCurrencyId}" name="singleDiffCurrencyId">
    <input type="hidden" name="travelerId">
    

	<div class="tourist">
		<div class="tourist-t">
			<a class="btn-del" style="cursor:pointer;" name="deleteTraveler" >删除</a>
			<input type="hidden" name ="id" value=""  class="traveler" >
			<span class="add_seachcheck" onclick="travelerBoxCloseOnAdd(this)"></span>
            <label class="ydLable">游客<em class="travelerIndex" style="vertical-align: baseline;"></em>:</label>
            <div class="tourist-t-off">
                <span class="fr">应收价格：<span name="jsPrice" class="ydFont2" style="margin-right:10px;"></span>结算价：<span name="travelerClearPrice" class="ydFont2"></span></span>
                <span name="tName"></span>       
            </div>
			<div class="tourist-t-on">
             </div>
		</div>
		
		<div class="tourist-con" flag="messageDiv">
			<!--游客信息左侧开始-->
			<div class="tourist-left">
            <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
				<ul class="tourist-info1 clearfix"  flag="messageDiv">
	                <li>
	                	<label class="ydLable"><span class="xing">*</span>姓名：</label>
	                	<c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
                  			<input type="text" maxlength="30" name="travelerName"  loginName="${fns:getUser().company.uuid }"  value="" class="traveler required"  onafterpaste="this.value=this.value.replaceSpecialChars()">
                  		</c:if>
						<c:if test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
		                	<input type="text" maxlength="30" name="travelerName" loginName="${fns:getUser().company.uuid }" class="traveler required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
						</c:if>
	                </li>
	                <li>
	                 	<label class="ydLable">英文／拼音：</label>
                        <input type="text" maxlength="30" name="travelerPinyin" class="traveler">
	               	</li>
	               	<li>
	                    <label class="ydLable"><span class="xing"></span>性别：</label>
                         <select name="travelerSex" class="selSex required">
                            <option value="1" selected="selected">男</option>
                            <option value="2" >女</option>
                        </select> 
	                </li>                        
                        <li>
                            <label class="ydLable">出生日期：</label>
                             <input type="text" name="birthDay" class="traveler traveler2 dateinput" onclick="WdatePicker()">
                           
                        </li>
                        <li>
                            <label class="ydLable"><span class="xing"></span>联系电话：</label>
                            <input type="text" name="telephone" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
                        </li>
                            <li>
							<label class="ydLable"><span class="xing"></span>护照类型：</label>
	                        <select name="passportType" class="selCountry">
	                            <c:forEach items="${passportTypeList}" var="passportType">
	                                <option value="${passportType.key}">${passportType.value}</option>
	                            </c:forEach>
	                        </select>
						</li> 
                        <li>
                            <!-- 去掉必填* 和 required  -->
                            <label class="ydLable"><span class="xing"></span>护照号：</label>
                            <input type="text" name="passportCode" class="traveler" maxlength="50"  >
                        </li>
                         <li>
                             <!-- 去掉必填* 和 required  -->
                            <label class="ydLable"><span class="xing"></span>护照签发日期:</label>
                            <input type="text" name="issuePlace" class="traveler traveler2 dateinput" >
                        </li>
                        <li>
                            <!-- 去掉必填* 和 required  -->
                            <label class="ydLable"><span class="xing"></span>有效期至：</label>
                            <input type="text" name="passportValidity" class="traveler traveler2 dateinput" onclick="WdatePicker({minDate:getCurDate()})">
                        </li>
	                <c:if test="${fns:getUser().company.uuid eq '980e4c74b7684136afd89df7f89b2bee'}"> 
	               <li>
	                		 <label class="ydLable"><span class="xing"></span>签发地：</label>
		                	<input type="text" maxlength="10" name="issuePlace1"  class="traveler " onblur="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')"  onafterpaste="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')">
						</li>  </c:if>
                    </ul>
                    <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand" onclick="boxCloseOnAdd(this)"></em>申请办签</div>
                    <ul flag="messageDiv">
                        <div class="ydbz_scleft">
                            <table class="table-visa">
                                <thead><tr>
                                    <th width="10%">申请国家</th>
                                    <th width="10%">领区</th>
                                    <th width="10%">签证类别</th>
                                    <th width="10%"><c:if test="${fns:getUser().company.uuid eq '0e19ac500f78483d8a9f4bb768608629'}"><span class="xing">*</span></c:if>预计出团时间 </th>
                                    <th width="15%">预计约签时间</th>
                                    <!-- 对应需求号   0211  添加预计回团时间 -->
                                    <c:if test="${fns:getUser().company.uuid eq '0e19ac500f78483d8a9f4bb768608629'}">
                                        <th width="10%"><span class="xing">*</span>预计回团时间</th>
                                    </c:if>
                                    <c:if test="${(fns:getUser().company.uuid == '7a81a03577a811e5bc1e000c29cf2586')}">
                                    <th width="10%">是否需要押金</th>
                                    <th width="10%">是否上交资料</th>
                                    </c:if>
                                </tr></thead>
                                <tbody><tr>
                                    <td class="tl">${countryName}</td>
                                    <td>
	                                    <c:if test="${not empty collarZoning }">
				                           ${fns:getDictLabel(collarZoning,'from_area','')}
			                        	</c:if>
	                               </td>
                                    <td>
	                                    ${visaType}
                                    </td>
                                    <td><input type="text" onclick="WdatePicker()" name="forecastStartOut"  class="inputTxt dateinput"  id=""></td>
                                    <td><input type="text" onclick="WdatePicker()" name="forecastContract"  class="inputTxt dateinput"  id=""></td>
                                    
                                    <!-- 对应需求号   0211  添加预计回团时间 -->
                                    <c:if test="${fns:getUser().company.uuid eq '0e19ac500f78483d8a9f4bb768608629'}">
                                      <td><input type="text" onclick="WdatePicker()" name="forecastBackDate"  class="inputTxt dateinput"  id=""></td>
                                    </c:if>
                                </tr></tbody>
                            </table>
                        </div>
                        <!-- 上传资料部分 -->

					  <div class="ydbz_tit ydbz_tit_child">上传资料</div>
		                <ul flag="messageDiv" class="ydbz_2uploadfile ydbz_scleft">
		                    <li class="seach25 seach33"><p>护照首页：</p><input name="passportfile" type="text" style="display:none;" disabled="disabled"><input type="button" name="passport" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','passportfile',this,'true');"/><span class="fileLogo"></span></li>
		                    <li class="seach25 seach33"><p>身份证正面：</p><input name="idcardfrontfile" type="text" style="display:none;" disabled="disabled"><input type="button" name="idcardfront" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','idcardfrontfile',this,'true');"><span class="fileLogo"></span></li>
		                    <li class="seach25 seach33"><p>申请表格：</p><input name="entryformfile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="entry_form" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','entryformfile',this,'true');"><span class="fileLogo"></span></li>
		                    
		                    <p class="kong"></p>
		                    <li class="seach25 seach33"><p>电子照片：</p><input name="photofile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="photo" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','photofile',this,'true');"><span class="fileLogo"></span></li>
		                    <li class="seach25 seach33"><p>身份证反面：</p><input name="idcardbackfile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="idcardback" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','idcardbackfile',this,'true');"><span class="fileLogo"></span></li>
		                    <li class="seach25 seach33"><p>签证附件：</p><input name="visaannexfile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="visa_annex" class="btn btn-primary" value="上传" onclick="uploadFilesvisa('${ctx}','visaannexfile',this,'false');"><span class="fileLogo"></span></li>
		                    
		                    <p class="kong"></p>
		                    <li class="seach25 seach33"><p>户口本：</p><input name="familyRegisterfile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="familyRegister" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','familyRegisterfile',this,'true');"><span class="fileLogo"></span></li>
		                    <li class="seach25 seach33"><p>房产证：</p><input name="houseEvidencefile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="houseEvidence" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','houseEvidencefile',this,'true');"><span class="fileLogo"></span></li>
		                    <li class="seach25 seach33"><p>其　它：</p><input name="otherfile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="other" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','otherfile',this,'true');"><span class="fileLogo"></span></li> 
		                </ul>
			   
			   
                <div class="ydbz_tit ydbz_tit_child">需提交办签资料</div>
                <ul class="seach25 seach100 ydbz_2uploadfile" id="ul4VisaOrginalCopy">
                                     <!-- ************197-start -->
                                     <p>资料原件：</p>   
                                     <span class="seach_check">           
                                     <input type="checkbox" name="original_Project_Type" value="0" <c:if test="${fn:contains(originalProjectType,'0')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">护照</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="1" <c:if test="${fn:contains(originalProjectType,'1')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">身份证</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="3" <c:if test="${fn:contains(originalProjectType,'3')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">电子照片</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="4" <c:if test="${fn:contains(originalProjectType,'4')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">申请表格</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="5" <c:if test="${fn:contains(originalProjectType,'5')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">户口本</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="6" <c:if test="${fn:contains(originalProjectType,'6')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">房产证</label>
                                     </span>
                                     <span class="seach_check">
                                      <input type="checkbox" name="original_Project_Type" value="2" <c:if test="${fn:contains(originalProjectType,'2')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">其他</label><input type="text" name="original_Project_Name" value="${originalProjectName}" class="input-mini"  disabled="disabled" >
                                      </span><br/>
                                     <p>复印件：</p> 
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="3"   <c:if test="${fn:contains(copyProjectType,'3')}">checked="checked"</c:if> id=""   disabled="disabled"  ><label for="">护照</label></span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="4"   <c:if test="${fn:contains(copyProjectType,'4')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">身份证</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="5"   <c:if test="${fn:contains(copyProjectType,'5')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">电子照片</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="6"   <c:if test="${fn:contains(copyProjectType,'6')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">申请表格</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="0"   <c:if test="${fn:contains(copyProjectType,'0')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">户口本</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="1"   <c:if test="${fn:contains(copyProjectType,'1')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">房产证</label>
                                     </span>
                                     <span class="seach_check">
                                      <input type="checkbox" name="copy_Project_Type"  value="2" <c:if test="${fn:contains(copyProjectType,'2')}">checked="checked"</c:if> id=""  disabled="disabled" ><label for="">其他</label><input type="text" name="copy_Project_Name" value="${copyProjectName}" class="input-mini"  disabled="disabled" ></span>
                                      </ul>
                                     <!-- ************197-end -->
                </ul>
               
                <div class="ydbz_tit ydbz_tit_child"></em>备注: <textarea class="textarea_long"  name="remark"></textarea></div>
                
			</div>
            <!--游客信息左侧结束-->
            <!--游客信息右侧开始-->
			<div class="tourist-right">
			  <div class="bj-info">
                    <div class="ydbz_tit ydbz_tit_child">报价</div>
			    <!-- C225将游客信息中的“应收价格”删除,将“成本价”改为“应收价格”,取签证产品发布时的“应收价格”,“订单总成本价”改为“应收总计”,取游客应收价格之和
			     	 changed by 2015-10-12
			    <div class="clearfix"> 
			        <ul class="tourist-info2">
			            <li><label class="ydLable2">应收价格：</label> ${currency.currencyMark }<span class="ydFont1"><fmt:formatNumber pattern="#.00" value="${visaPay}" /></span><input type="hidden" name="singleDiff"  class="traveler" value="0" > </li>
			        </ul>			      
			    </div>			    
			    <div class="yd-line"></div>
			    -->
                    <div class="clearfix">
                        <a name="addcost" class="btn-addBlue">添加其他费用</a>
                        <div class="payfor-otherDiv"> 
                        </div>
                    </div>
                 </div>
			    <div class="yd-line"></div>
                <div class="yd-total clearfix"><a name="bjyyqb" class="ydbz_x" onclick="useAllPrice(this)">报价应用全部</a></div>
                <div class="yd-total clearfix">
                  <div class="fr">
                    <label class="ydLable2">应收价格：</label><span name="innerJsPrice" class="ydFont2"></span>
                    <input type="hidden" name="srcPrice" class="traveler">
                    <input type="hidden" name="srcPriceCurrency" class="traveler">
                    <input type="hidden" name="jsPrice" class="traveler">
                    <input type="hidden" name="payPrice" class="traveler">
                  </div>
                </div>
                <div class="yd-total clearfix">
					<div class="fr">
                  	<label class="ydLable2">结算价：</label><span name="clearPrice" class="ydFont2"></span>
                  	<input type="hidden" name="travelerClearPrice" class="traveler">
                  </div>
				</div>
				<!--     0820上  -->
			
				<div class="clearfix1">
						<div class="traveler-rebatesDiv" >
							<!-- 265需求，针对鼎鸿假期，将所有返佣字段改为宣传费 -->
					         <c:choose>
					         	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
									<label class="ydLable2 ydColor1" >预计个人宣传费：</label>
								</c:when>
					            <c:otherwise>
					                 <label class="ydLable2 ydColor1" >预计个人返佣：</label>
					             </c:otherwise>
					         </c:choose>   
							<select name="rebatesCurrency">
	               				 <c:forEach items="${currencyList4Rebates}" var="cu">
	                                <option value="${cu.id}">${cu.currencyName}</option>
	                            </c:forEach>
	               			</select>
	               			<input type="text"  maxlength="9" name="rebatesMoney" style="display: inline-block;width: 40px;"  onafterpaste="validNum(this)" onkeyup="validNum(this)">
						</div>
					</div>
			
			<!--     0820上  	-->	
			<div >

	               			<input type="hidden"   maxlength="9" name="rebatesCurrency" class="ipt-rebates" value="33">
	               			<input type="hidden"  maxlength="9" name="rebatesMoney" class="ipt-rebates"  value="0">
						
			</div>	
			
				
            </div>
          </div>
          <!--保存、取消按钮开始-->
            <div class="rightBtn"><a class="btn" onclick="SavePeopleTableData(this)"/>保存</a></div>
            <!--保存、取消按钮结束-->
        </div>
        
</form>
</div>


<!--  订单基本信息  -->
<div class="ydbz yd-step1 " id="stepbar" >&nbsp;</div>
  <div class="ydbz_tit">订单基本信息</div>
  <p class="ydbz_mc">${countryName} ${visaType}
                            (<c:if test="${not empty collarZoning }">
				                    ${fns:getDictLabel(collarZoning,'from_area','')}
			                </c:if>)
  </p>
  
                 <ul class="ydbz_info">
	         
	                    <li><span>签证国家：</span>${countryName}</li>
	                    <li><span>签证类别：</span>
	                    ${visaType}
	                  </li>
	                    <li><span>领区：</span>
	                                     <c:if test="${not empty collarZoning }">
				                           ${fns:getDictLabel(collarZoning,'from_area','')}
			                        	</c:if>
			            </li>
	                            <!--
	                     <li><span>是否面试：</span>${needSpotAudition eq "0" ? "否":"是"}</li>
	                     <li><span>预计工作日：</span>${forecastWorkingTime}天</li>
	                     <li><span>入境次数：</span>${enterNum}次</li>
	                       -->
	                     <c:if test="${visaCostPrice eq '11'}">
	                     	<li>
	                        	<span>成本价格：</span> ${currency.currencyMark }<fmt:formatNumber pattern="#.00" value="${visaPrice}" />/人
	                    	</li>
	                    </c:if>
	                     <li>
	                        <span>应收价格：</span> ${currency.currencyMark }<fmt:formatNumber pattern="#.00" value="${visaPay}" />/人
	                    </li>
	                    <!-- 对应需求编号  C460V3 添加团号 -->
	                    <li>
	                        <span>团号：</span>  ${product.groupCode}
	                    </li>
	                   
                 </ul>
                <form id="productOrderTotal">
                	<ul class="ydbz_info">
                   		<li style="display: none;">
                   			<input type="text" id="orderPersonelNum" onkeyup="this.value=this.value.replace(/(\D|^0)/g,'')" onafterpaste="this.value=this.value.replace(/(\D|^0)/g,'')"  class="required" />
                        </li>
                        <li>
                            <span><em class="xing">*</em>办签人数：</span>
                                <input type="text" id="orderPersonNumAdult"   onkeyup="this.value=this.value.replace(/(\D|^0)/g,'')" onafterpaste="this.value=this.value.replace(/(\D|^0)/g,'')"   class="required inputTxt"> 人
                        </li>
                    </ul>
                </form>   
         <!--  预定人信息  主要是渠道联系人信息  -->        
        <div class="ydbz_tit"><em class="ydExpand"  onclick="boxCloseOnAdd(this)" ></em>填写预订人信息</div>
          <div class="channel_sel"  flag="messageDiv">
          <form id="orderpersonMesdtail">
            <p class="ydbz_qdmc" style="display: none;">预订渠道：<select id="agentIdIn" name="agentIdIn"   disabled="disabled">
            								<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
											<c:choose>
											   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
											       <option value="-1" >直客</option>
											   </c:when>
											   <c:otherwise>
											       <option value="-1" >非签约渠道</option>
											   </c:otherwise>
											</c:choose>
							                
							            </select>
           </p>
           
           <p class="ydbz_qdmc">预订渠道：
           <!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
			<c:choose>																
			   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'  &&  agentName eq '非签约渠道'}"> 
			       直客
			   </c:when>
			   <c:otherwise>
			        ${agentName}
			   </c:otherwise>
			</c:choose>
          </p>
              <c:if test="${agentId=='-1'}">
                   <label style="margin-left: 50px;"><span class="xing">*</span><c:choose><c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }">未签</c:when><c:otherwise>
                   <c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
                   		直客
                   	</c:if>
                   	<c:if test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
                   	非签约渠道
                   	</c:if>
                   </c:otherwise></c:choose>名称：</label>
                         <input id="agentinfoNameId"  maxlength="100"  type="text"  name="agentinfoName" value="${agentinfoName}"/>
                    
              </c:if>
              <div id="ordercontact">
                  <!--渠道-->
              	<ul class="ydbz_qd min-height" id="unSignedAgentInfo" name="orderpersonMes"> 
					<c:choose>
						<c:when test="${agentId=='-1'}">
						<li><label> <span class="xing">*</span> 渠道联系人<font>1</font>：
							</label> <input maxlength="45" name="contactsName" type="text" value=""/>
						<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label><input maxlength="20" name="contactsTel" type="text" id="orderPersonPhoneNum" value="${agentinfo.agentContactMobile}" class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div><span class="ydbz_x yd1AddPeople"  id="addContact"  <c:if test="${fn:contains(isAllowAddAgentInfo,'0')}">style="display:none;"</c:if> onclick="yd1AddPeoples(this)">添加联系人</span></li>
                    <li flag="messageDiv" style="display:none" class="ydbz_qd_close">
                      <ul class="view">
                       <li><label>固定电话：</label><input id="tel" maxlength="20" value="${agentinfo.agentContactTel}"  name="contactsTixedTel" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/></li>
                       <li><label>传真：</label><input id="fix"  maxlength="20" type="text" name="contactsFax" value="${agentinfo.agentContactFax}" /></li>
                       <li><label>渠道地址：</label><input maxlength="200" name="contactsAddress" type="text" value="${agentAddressStr}" onblur="updataInputTitle(this);" title="${agentAddressStr}"/></li>
                       <li><label>QQ：</label><input id="qq"  maxlength="20" type="text" name="contactsQQ" value="${agentinfo.agentContactQQ}" /></li>
                       <li><label>Email：</label><input id="email"  maxlength="50" type="text" name="contactsEmail" value="${agentinfo.agentContactEmail}" /></li>
                       <li><label>渠道邮编：</label><input  maxlength="20" type="text" name="contactsZipCode" value="${agentinfo.agentPostcode}"/></li>
                       <li><label>其他：</label><input   maxlength="200" type="text" name="remark" value="" onblur="updataInputTitle(this);" title=""/></li>
                      </ul>
					</li>
					</c:when>
					<c:otherwise>
						 <li><label> <span class="xing">*</span> 渠道联系人<font>1</font>：
								</label> <span name="channelConcat"></span>
					     <li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label><input maxlength="20" name="contactsTel" type="text" id="orderPersonPhoneNum" value="${agentinfo.agentContactMobile}" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if> class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div><span class="ydbz_x yd1AddPeople"  id="addContact"  <c:if test="${fn:contains(isAllowAddAgentInfo,'0')}">style="display:none;"</c:if> onclick="yd1AddPeoples(this)">添加联系人</span></li>
	                     <li flag="messageDiv" style="display:none" class="ydbz_qd_close">
	                      <ul>
	                    	<li><label>固定电话：</label><input id="tel" maxlength="20" value="${agentinfo.agentContactTel}" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if>  name="contactsTixedTel" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /></li>
	                        <li><label>传真：</label><input id="fix"  maxlength="20" type="text" name="contactsFax" value="${agentinfo.agentContactFax}" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if>/></li>
	                        <li><label>渠道地址：</label><input maxlength="200" name="contactsAddress" type="text"  value="${agentAddressStr}"  onblur="updataInputTitle(this);" title="${agentAddressStr}" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if>/></li>
	                        <li><label>QQ：</label><input id="qq"  maxlength="20" type="text" name="contactsQQ" value="${agentinfo.agentContactQQ}" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if>/></li>
	                        <li><label>Email：</label><input id="email"  maxlength="50" type="text" name="contactsEmail" value="${agentinfo.agentContactEmail}" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if>/></li>
	                        <li><label>渠道邮编：</label><input  maxlength="20" type="text" name="contactsZipCode" value="${zipCode}" <c:if test="${fn:contains(isAllowModifyAgentInfo,'0')}">readonly="readonly"</c:if>/></li>
	                        <li><label>其他：</label><input  maxlength="200" type="text" name="remark" value="" onblur="updataInputTitle(this);" title=""/></li>
	                      </ul>
	                     </li>
					 </c:otherwise>	
					</c:choose>
                    </ul>
               </div>
                   </form>
              </div> 
             
<div class="ydBtn-down orderPersonMsg" style="display:none;" ><span>&nbsp;</span></div>
                 

<div class="ydbz_sxb clear" id="oneToSecondOutStepDiv" ><div class="ydBtn" id="oneToSecondStepDiv"><span class="ydbz_x" >下一步</span></div></div>

  <div id="manageOrder_m"  style='display:none;' >
  <div id="contact">
  <div class="ydbz_tit"> <em class="ydExpand"  onclick="boxCloseOnAdd(this)" ></em></span> 特殊需求</div>
  
                 <div class="ydbz2_lxr" flag="messageDiv">
                    <form class="contactTable">
                    			<div class="textarea pr wpr20">
                                <p>
                                   <label style="vertical-align:top">特殊需求：</label><input type="hidden" name ="id" value="">
                                   <textarea name="remark" flag="istips" class="textarea_long" maxlength="500" rows="3" cols="50" onkeyup="this.value=this.value.replaceSpecialChars4SpecialRemark()" onafterpaste="this.value=this.value.replaceSpecialChars4SpecialRemark()" id="specialremark"></textarea>
                                   <span id="promptSpan" class="ipt-tips" style="text-indent:1cm;">最多输入500字</span>    
                                </p>
                                </div>
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
    <div class="touristBtn"><a class="btn-addGrey"  id="addTraveler">添加游客</a></div>
    <!--添加游客按钮结束-->
  
	<div class="clearfix2">
		<div class="traveler-rebatesDiv" >
			<!-- 265需求，针对鼎鸿假期，将所有返佣字段改为宣传费 -->
	         <c:choose>
	         	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					<label class="ydLable2 ydColor1" style="width: 100px;">预计团队宣传费：</label>
				</c:when>
	            <c:otherwise>
	                 <label class="ydLable2 ydColor1" style="width: 100px;">预计团队返佣：</label>
	             </c:otherwise>
	         </c:choose>   
			<select id="groupRebatesCurrency" name="groupRebatesCurrency">
				<c:forEach items="${currencyList4Rebates}" var="cu">
					<option value="${cu.id}">${cu.currencyName}</option>
				</c:forEach>
			</select>
			<input type="text" class="required ipt-rebates" maxlength="15" id="groupRebatesMoney" name="groupRebatesMoney" onafterpaste="validNum(this)" onkeyup="validNum(this)" placeholder="金额">
		</div>
	</div>
  
    <div class="ydbz_sxb" id="secondDiv"  style='display:none;' >
        <div class="ydBtn ydBtn2">
            <a class="ydbz_s" id="secondToOneStepDiv" >上一步</a>
            <a class="ydbz_x" id="secondToThirdStepDiv">下一步</a>
        </div>
    </div>
    <div class="ydbz_sxb" id="thirdDiv"  style='display:none;'>
        <div class="ydBtn ydBtn2">
            <div class="ydbz_s" id="thirdToSecondTStepDiv" >上一步</div>
                    <div class="ydbz_x" id="thirdToFourthStepDiv" onClick="$(this).attr('id','');$(this).attr('class','ydbz_s gray');">保存并收款</div>
        </div>
    </div>
        <div style="text-align:right; font-size:12px; margin-top:20px;  padding-top:10px; padding-right:10px;">
             	<b style="font-size:18px">应收总计：</b><span id="travelerSumPrice" class="tdred f20"></span>
        </div>
        <div style="text-align:right; font-size:12px; margin-top:20px;  padding-top:10px; padding-right:10px;">
        	<b style="font-size:18px">订单总结算价：</b><span id="travelerSumClearPrice" class="tdred f20"></span>
        </div>
  </div>
</div>
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

/******************************************************/
/*112需求:特殊需求过滤字符:"<",">","\"," "" "," '' "的函数方法      */
/******************************************************/
String.prototype.replaceSpecialChars4SpecialRemark= function (regEx) {//112-my
   if (!regEx){
     regEx = /[\`\"\'\'\‘\”\“\’\<\>\\]/g;
     }
   return this.replace(regEx, '');
    };   
   
</script>
</body>
</html>
