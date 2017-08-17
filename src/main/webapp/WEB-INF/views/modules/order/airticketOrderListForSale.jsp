<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta>
<title>订单-机票订单</title>

<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/huanqiu-style.css" />
<style type="text/css">
	.activitylist_bodyer_right_team_co1 .writableMenu{
		width:72px;
		border-top-right-radius:0px;
		border-bottom-right-radius:0px;
	}
	.word-wrap{
		word-wrap:break-word;
	}
</style>

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script type="text/javascript">
	$(function(){
		$("#op" ).comboboxInquiry();
		$("#saler" ).comboboxInquiry();
		$("#picker" ).comboboxInquiry();
		//操作浮框
		operateHandler();
		//展开筛选按钮
		launch();
		//搜索聚焦失焦
		inputTips();
		//团号和产品切换
		switchNumAndPro();
		showSearchPanel();
		//收款确认提醒
		$(".notice_price").hover(function(){
			$(this).find("span").show();
		},function(){
			$(this).find("span").hide();
		});

		//销售与下单人相互切换
	    $("#contentTable").delegate(".salerId","click",function(){
	        $(this).addClass("on").siblings().removeClass('on');
	        $('.createBy_cen').removeClass('onshow');
	        $('.salerId_cen').addClass('onshow');
	    });
	    
	    $("#contentTable").delegate(".createBy","click",function(){
	         $(this).addClass("on").siblings().removeClass('on');
	         $('.salerId_cen').removeClass('onshow');
	         $('.createBy_cen').addClass('onshow');
	        
	    });
	});
	
/**
 * 修改支付凭证
 * 
 * param payId
 * param orderId
 */
function changepayVoucher(payId, orderId){
	window.open ("${ctx}/order/manage/modifypayAirticketVoucher/" + payId + "/" + orderId);
}
	
	// 	有查询条件的时候，DIV不隐藏
	function showSearchPanel(){
		var ticketType = $("input[name='ticketType']:checked").val();
		var airType = $("input[name='airType']:checked").val();
		var fromAreaId = $("select[name='fromAreaId'] option:selected").val();
		var contact = $("input[name='contact']").val();
		var showType = $("select[name='showType'] option:selected").val();
		var targetAreaNameList = $("#targetAreaName").val();
		var op = $("select[name='op'] option:selected").val();
		var agentId = $("select[name='agentId'] option:selected").val();
		var saler = $("select[name='saler'] option:selected").val();
		var picker = $("select[name='picker'] option:selected").val();
		var invoiceStatus = $("select[name='invoiceStatus'] option:selected").val();
		var receiptStatus = $("select[name='receiptStatus'] option:selected").val();
		
		if(isNotEmpty(ticketType) || isNotEmpty(airType)
				||isNotEmpty(fromAreaId) || isNotEmpty(contact)
				||(isNotEmpty(showType) && showType != 0) || isNotEmpty(targetAreaNameList)
				|| isNotEmpty(agentId) || isNotEmpty(invoiceStatus) || isNotEmpty(receiptStatus)
				|| isNotEmpty(op) || isNotEmpty(saler) || isNotEmpty(picker)){
			$('.zksx').click();
		}
	}
	
	// 判定不为空值
	function isNotEmpty(str){
		if(str != ""){
			return true;
		}
		return false;
	}
	
	//展开子table
	function expand(child,obj){
		if($(child).css("display")=="none"){
			$(obj).html("收起").parents("tr").addClass("tr-hover");
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
		}else{
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("展开").parents("tr").removeClass("tr-hover");
		}
	}
	
	//展开子table 支付记录专用
	function expandPayRecord(child,obj){
		if($(child).css("display")=="none"){
			$(obj).parents("tr").addClass("tr-hover");//html("收起").
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
		}else{
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");//html("展开").
		}
	}
	
	//分页
	function page(n,s){
	    $("#pageNo").val(n);
	    $("#pageSize").val(s);
	    $("#searchForm").submit();
	    return false;
	}
	
	//查询条件重置
	function resetSearchParams(){
	    $(':input','#searchForm')
	     .not(':button, :submit, :reset, :hidden')
	     .val('')	     
	     .removeAttr('checked')
	     .removeAttr('selected');
	    $('#op').val('');
	    $('#saler').val('');
	    $('#picker').val('');
	}
	
	//排序
	function sort(element, sortNum){
		$("#sortNum").val(sortNum);
		var _this = $(element);
		
		//按钮高亮
		_this.parent("li").attr("class","activitylist_paixu_moren");
		//原先高亮的同级元素置灰
		_this.parent("li").siblings(".activitylist_paixu_moren").attr("class","activitylist_paixu_left_biankuang");

		//高亮按钮隐藏input赋值
		_this.next().val("activitylist_paixu_moren");

		//原先高亮按钮隐藏input值清空
		_this.parent("li").siblings(".activitylist_paixu_moren").children("a").next().val("");
		
		var sortFlag = _this.children().attr("class");
		//降序
		if(sortFlag == undefined || sortFlag == "icon icon-arrow-up" ){
		
			//改变箭头的方向
			_this.children().attr("class","icon icon-arrow-down");
			
			//降序
			_this.prev().val("desc");
		}
		//降序
		else if(sortFlag == "icon icon-arrow-down"){
			//改变箭头方向
			_this.children().attr("class","icon icon-arrow-up");
			
			//shengx
			_this.prev().val("asc");
		}
		
		$("#searchForm").submit();
		
	    return false;
	}
	
	function clickChoose(){
		if($("#showChoose").html()=="展开筛选"){
			$("#showChooseSelect").val(1);
		}else{
			$("#showChooseSelect").val(0);
		}
	}
	
	//出票确认单
	function uploadTicket(){
	
		var orderId = $("#airticketOrderId").val();
		var html = '<div style="margin-top:20px; padding-left:45px;">';
		html += '<form id="uploadConfirmation" method="post" enctype="multipart/form-data" action="${ctx}/order/manage/uploadConfirmation">';
		html += '<input type="hidden" name="orderId" value="'+orderId+'"/>';
		html += '<p>上传出票确认单：<input name="orderConfirmation" type="file" value="上传"/></p>';
		html += '</form>';
		html += '</div>';
		$.jBox(html, { title: "本地上传",buttons:{'取消': 0,'确定': 1}, submit:function(v, h, f){
			if (v=="1"){
				$("#uploadConfirmation").submit();
				return true;
			}
		},height:180,width:410});	
	}
	
	/**
 *  机票订单页面查看订单发票信息列表
 * param orderNum   订单号
 * author     ruyi.chen
 * createDate 2014-12-04
 */
function showInvoiceInfo(orderId,orderNum){
	window.open("${ctx}/orderInvoice/manage/getInvoiceListByOrderNum?orderNum="+orderNum+"&orderId="+orderId+"&orderType=7");
	}
	
	
	
function showReceptInfo(orderId,orderNum){
	window.open("${ctx}/orderInvoice/manage/supplyreceiptlist?orderNum="+orderNum+"&orderId="+orderId + "&orderType=7");
	}	
	
	
	
	function downloads(docid){
		if(docid){
    		window.open("${ctx}/sys/docinfo/download/"+docid);
		}else{
			top.$.jBox.tip("没有确认单");
		}
    }
    
/**
 * 取消订单
 * 
 * param orderId
 */
function cancelOrder(orderId) {
	$.ajax({
        type: "POST",
        url: contextPath + "/orderCommon/manage/canCancelOrder",
        data: {
            orderId:orderId
        },
        success: function(result){
            if(result == '0'){
            var $div = $('<div class=\"tanchukuang\"></div>')
    .append('<div class="msg-orderCancel"><div class="msg-orderCancel-t">请输入取消原因（输入字数为100字以内）：</div><textarea cols="30" rows="3" name="description"></textarea></div>');
	var html = $div.html();
    var submit = function(v, h, f){
    	if(v === 0){
            return true;
        }
        if(f.description.length > 100){
            top.$.jBox.tip('输入字数为100字以内。','warning');
            return false;
        }
        else if(v === 1){
            $.ajax({
                type: "POST",
                url: "${ctx}/order/manage/cancelOrder",
                data: {
                    orderId:orderId,
                    description : f.description
                },
                success: function(msg){
                	if(msg == 'fail') {
                    	top.$.jBox.tip('订单不能取消','warning');
                        top.$('.jbox-body .jbox-icon').css('top','55px');
                    } else if(msg == 'ok') {
                       top.$.jBox.tip('订单已取消','warning');
                       top.$('.jbox-body .jbox-icon').css('top','55px');
                    	  $("#btn_search").click();
                    } else {
                    	top.$.jBox.tip(msg,'warning');
                        top.$('.jbox-body .jbox-icon').css('top','55px');
                    }
                }
             });
        }
        return false;
    };
    $.jBox(html, {title: "取消原因", buttons:{'确定' : 1, '取消' : 0 }, submit: submit});
            }else{
            	top.$.jBox.tip(result,'warning');
            }
        }
     });
    
}

/**
 * 订单删除
 * 
 * param orderId
 */
function deleteOrderByFlag(orderId) {
	$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
	            type: "POST",
	            url: "${ctx}/order/manage/deleteOrderByFlag",
	            data: {
	            	orderId:orderId
	            },
	            success: function(msg) {
	            	if(msg == 'ok'){
						top.$.jBox.tip('删除成功', 'warning');
						$("#btn_search").click();
					}else{
						top.$.jBox.tip(msg, 'warning');
					}
	            }
            });
		} else if (v == 'cancel') {
                
		}
	});
}

/**
 * 订单激活
 *
 * param orderId
 */
function invokeOrder(orderId) {
	$.ajax({
        type: "POST",
        url: "${ctx}/order/manage/invokeOrder",
        data: {
            orderId : orderId
        },
        success: function(msg) {
            if(msg == 'fail') {
            	top.$.jBox.tip('激活失败','warning');
                top.$('.jbox-body .jbox-icon').css('top','55px');
            } else if(msg == 'success') {
            	top.$.jBox.tip('激活成功','warning');
                top.$('.jbox-body .jbox-icon').css('top','55px');
                $("#btn_search").click();
            } else {
            	top.$.jBox.tip(msg,'warning');
                top.$('.jbox-body .jbox-icon').css('top','55px');
            }
        }
    });
}

/**
 * 查看订单是否可支付
 * @param orderId
 * @param payPriceType
 * @returns {Boolean}
 */
function orderPay(payPriceType, orderId, orderNum, orderType, orderCompany,totalMoney,orderDetailUrl,mainId) {
	if(mainId!=""&&payPriceType=="2"){
		top.$.jBox.tip('参团机票订单无需付款','warning');
        top.$('.jbox-body .jbox-icon').css('top','55px');
		return false;
	}else{
		$.ajax({
	        type: "POST",
	        async:false,
	        url: "${ctx}/order/manage/whetherCanPay",
	        dataType:"json",
	        data:{orderId : orderId, payPriceType : payPriceType},
	        success : function(result) {
	        	var data = eval(result);
	        	if(data && data[0].flag == "true") {
	        		
	        		var param = "orderId=" + orderId 
	    			+ "&orderNum=" + orderNum 
	    			+ "&payPriceType=" + data[0].payPriceType  
	    			+ "&orderType=" + orderType 
	    			+ "&businessType=1" 
	    			+ "&isCommonOrder=airticket" 
	    			+ "&agentId=" + orderCompany 
	    			+ "&paramCurrencyId=" + data[0].moneyCurrencyId 
	    			+ "&paramCurrencyPrice=" + data[0].moneyCurrencyPrice
	    			+ "&paramTotalCurrencyId=" + data[0].totalMoneyCurrencyId 
	    			+ "&paramTotalCurrencyPrice=" + data[0].totalMoneyCurrencyPrice
	        		+ "&orderDetailUrl=" + decodeURIComponent(orderDetailUrl);
	    	
	        		window.open("${ctx}/orderPay/pay?" + param);
	        	} else {
	        		var tips = data[0].warning;
	        		top.$.jBox.info(tips, "警告", { width: 250, showType:"slide", icon: "info",draggable: "true" });
					top.$('.jbox-body .jbox-icon').css('top','55px');
	        	}
			}
		});
	}
}

/**
 * 支付记录
 * 
 * param orderId
 * param obj
 */
function showOrderPay(orderId, obj){
    var sbrtr = $(obj).closest(".toptr").next();
    var sbrtd = sbrtr.children().eq(0);
    var table = sbrtr.find("table[id=table_orderPay]");
    if(table.length<=0){
        $.ajax({
            type: "POST",
            url: "${ctx}/orderCommon/manage/getPayList",
            data: {
                orderId:orderId
            },
            success: function(msg){
                var $table = $("<table class=\"table activitylist_bodyer_table\" style=\"margin:0 auto;\"></table>").append("<thead style=\"background:#62AFE7\"><tr><th>付款方式</th><th>金额</th><th>日期</th><th>支付款类型</th><th>是否已确认达账</th><th>支付凭证</th><th>操作</th></tr></thead>");
             
                $.each(msg.orderList,function(key,value){
                    var payTypeName = value.payTypeName;
                    var payPriceType = value.payPriceType;
                    var isAsAccount = value.isAsAccount;
                    
                    if(payTypeName==null||payTypeName==undefined){
                        payTypeName="";
                    }
                    if(payPriceType=="1"){
                        payPriceType="支付全款";
                    }else if(payPriceType=="2"){
                        payPriceType="交订金";
                    }if(payPriceType=="3"){
                        payPriceType="支付尾款";
                    }
                    
                    if(isAsAccount=="0"||isAsAccount=="null"||isAsAccount==undefined){
                        isAsAccount="否";
                    }else if(isAsAccount=="1"){
                        isAsAccount="是";
                    }
                    
                    var payvoucher = "<td><a class=\"downloadzfpz\" lang=\""+value.payVoucher+"\">支付凭证ee</a></td>";
                    
                    if(payvoucher==""||payvoucher==undefined||payvoucher==null){
                        payvoucher = "<td>暂无支付凭证</td>";
                    }
                    
                    var paypricechange="<td><a class=\"changepayPrice\" lang=\""+value.payVoucher+"\""+"payprice=\""+value.payPrice+"\">修改</a></td>"
                    $table.append($("<tr></tr>")
                            .append("<td>"+payTypeName+"</td>")
                            .append("<td>${fns:getMoneyAmountBySerialNum("+value.moneySerialNum+",2)}</td>")
                            .append("<td>"+value.createDate+"</td>")
                            .append("<td>"+payPriceType+"</td>")
                            .append("<td>"+isAsAccount+"</td>")
                            .append(payvoucher)
                            .append(paypricechange)
                    )
                });
                sbrtd.append($table);
            }
         });
    }
    if($(obj).hasClass("jtk")){
        var td = $(obj).closest("td");
        if(sbrtr.is(":hidden")){
            sbrtr.show();
            td.addClass("td-extend");
        }else{
            sbrtr.hide();
            td.removeClass("td-extend");
        }
    }
}


//申请发票
function applyInvoice(orderNum,orderId,orderType){
    $.ajax({
    	type : "post",
		url : "${ctx}/orderInvoice/manage/validateOrder",
		data:{
				orderNum : orderNum,
				orderId: orderId,
				orderType:orderType
		},
		success : function(msg) 
		{
			if("success"== msg.msg)
			window.location.href ="${ctx}/orderInvoice/manage/applyInvoice?orderNum=" +orderNum+"&orderId="+orderId+"&orderType="+orderType;  
			//${orderNum}&orderId=${orderId}&orderType=${orderType}";
			else if("canOrDel"==msg.msg)
				top.$.jBox.tip('已取消或已删除订单不能开发票！','success');
			else
				top.$.jBox.tip('申请失败，订单创建人和申请人不一致！','success'); 
		}
    });
	
}

// 申请收据
function applyReceipt(orderNum,orderId,orderType){
    $.ajax({
    	type : "post",
		url : "${ctx}/orderInvoice/manage/validateOrder",
		data:{
				orderNum : orderNum,
				orderId: orderId,
				orderType:orderType
		},
		success : function(msg) 
		{
			if("success"== msg.msg)
				window.location.href ="${ctx}/orderReceipt/manage/applyReceipt?orderNum=" +orderNum+"&orderId="+orderId+"&orderType="+orderType; 
			else if("canOrDel"==msg.msg)
				top.$.jBox.tip('已取消或已删除订单不能开收据！','success');
			else
				top.$.jBox.tip('申请失败，订单创建人和申请人不一致！','success'); 
		}
    });
	
}



/**
 * 修改支付凭证
 * 
 * param payId
 * param orderId
 */
function changepayVoucher(payId, orderId){
	window.open ("${ctx}/order/manage/modifypayVoucher/" + payId + "/" + orderId);
}

/* 展示支付凭证 */
$(document).delegate(".showpayVoucher","click",function() {
	var orderIDValue = $(this).attr("lang");
	$.ajax({
        type: "POST",
        url: "${ctx}/sys/docinfo/payVoucherList/"+orderIDValue,
        data: {
            orderId:orderIDValue
        },
        success: function(msg){
        	var htmls = "<table class='activitylist_bodyer_table t-type-jbox'><thead><tr><th>凭证所属ID</th><th>凭证名称</th><th>下载链接</th></tr></thead><tbody>";
        	$.each(msg,function(key,value){
        		var docName = value.docName;
        		var orderId = value.payOrderId;
        		var id = value.id;
        		htmls = htmls+"<tr><td>"+orderId+"</td><td>"+docName+"</td><td><a class='downloadzfpz' lang="+id+">支付凭证</a></td></tr>";
        	});
        	htmls = htmls+"</tbody></table>";
        	$.jBox.open(htmls,"凭证列表",600,240);
        }
     });
});

$(document).delegate(".downloadzfpz","click",function(){
    window.open("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
});

function writableMenuEvaluation(obj){
	var menuVal = $(obj).find('option:selected').text();
	$(obj).parent().next().children().val(menuVal);
}

</script>
</head>

<body>
	
	<input id="userNames" type= "hidden" value="${userNames }">
	<!-- 列表 content -->
	<div style="float:left;width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
        
        <c:if test="${not empty conditionsMap }">
			
			<form id="searchForm" action="${ctx}/order/manage/airticketOrderListForSale" method="post" class="formpaixu">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		        <input id="showChooseSelect" name="showChooseSelect" type="hidden" value="${conditionsMap.showChooseSelect}"/>
				<div class="activitylist_bodyer_right_team_co2 wpr20 pr">
					<div class="activitylist_team_co3_text">搜索：</div>
					<input value="${conditionsMap.orderNumOrOrderGroupCode }" class="inputTxt inputTxtlong" name="orderNumOrOrderGroupCode" id="orderNum" flag="istips"> 
					<span class="ipt-tips">输入团号、订单号</span>
				</div>
				<div class="form_submit">
					 <input type="submit" id="btn_search" value="搜索"  class="btn btn-primary ydbz_x">
					 <input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x">
				</div>
				<a class="zksx" id="showChoose" onclick="clickChoose()">筛选</a>
				<div class="ydxbd">
					<div class="messageDiv">
						<div class="seach25 seach100">
							<span class="seach_check"><label for="inquiry_radio_flights0"><input type="radio" name="ticketType" id="inquiry_radio_flights0" value="" checked="checked"> 不限</label></span>
							<span class="seach_check"><label for="inquiry_radio_flights1"><input type="radio" name="ticketType" id="inquiry_radio_flights1" value="2" <c:if test="${conditionsMap.ticketType==2 }"> checked="checked"</c:if> > 国际机票</label></span>
							<span class="seach_check"><label for="inquiry_radio_flights2"><input type="radio" name="ticketType" id="inquiry_radio_flights2" value="1" <c:if test="${conditionsMap.ticketType==1 }"> checked="checked"</c:if> > 内陆机票</label></span>
							<span class="seach_check"><label for="inquiry_radio_flights3"><input type="radio" name="ticketType" id="inquiry_radio_flights3" value="3" <c:if test="${conditionsMap.ticketType==3 }"> checked="checked"</c:if> > 国际+内陆机票</label></span>
						</div>
						<div class="seach25 seach100">
						  <span class="seach_check"><label for="radio10"><input type="radio" name="airType" id="radio10" value="" checked="checked"> 不限</label>　　</span>
						  <span class="seach_check"><label for="radio11"><input type="radio" name="airType" id="radio11" value="3" <c:if test="${conditionsMap.airType==3 }"> checked="checked"</c:if> > 单程</label>　　</span>
						  <span class="seach_check"><label for="radio12"><input type="radio" name="airType" id="radio12" value="2" <c:if test="${conditionsMap.airType==2 }"> checked="checked"</c:if> > 往返</label>　　</span>
						  <span class="seach_check"><label for="radio13"><input type="radio" name="airType" id="radio13" value="1" <c:if test="${conditionsMap.airType==1 }"> checked="checked"</c:if> > 多段</label>	</span>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co">
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">出发地：</div>
                              <select name="fromAreaId" >
								<option value="">不限</option>
								<c:forEach items="${fromAreasMap}" var="entry">
									<option value="${entry.key}"  <c:if test="${conditionsMap.fromAreaId==entry.key }">selected="selected"</c:if> >${entry.value}</option>
								</c:forEach>
							</select>
						</div>
						  
						<div class="activitylist_bodyer_right_team_co1">
                                 <div class="activitylist_team_co3_text">联系人：</div><input type="text" class="" name="contact" value="${conditionsMap.contact }">
						</div>
						<div class="activitylist_bodyer_right_team_co1">
                              <div class="activitylist_team_co3_text">订单状态：</div>
                              <select id="trafficName" name="showType">      
                                 <option value="0"  <c:if test="${conditionsMap.showType == 0 }">selected="selected"</c:if> >全部订单</option>
                                 <option value="1"  <c:if test="${conditionsMap.showType == 1 }">selected="selected"</c:if> >未收全款</option>
                                 <option value="2"  <c:if test="${conditionsMap.showType == 2 }">selected="selected"</c:if> >未收订金</option>
                                 <option value="5"  <c:if test="${conditionsMap.showType == 5 }">selected="selected"</c:if> >已收全款</option>
                                 <option value="4"  <c:if test="${conditionsMap.showType == 4 }">selected="selected"</c:if> >已收订金</option>
                                 <option value="3"  <c:if test="${conditionsMap.showType == 3 }">selected="selected"</c:if> >已占位</option>
                                 <option value="7"  <c:if test="${conditionsMap.showType == 7 }">selected="selected"</c:if> >待计调确认</option>
                                 <option value="99"  <c:if test="${conditionsMap.showType == 99 }">selected="selected"</c:if> >已取消</option>
                                 <option value="111"  <c:if test="${conditionsMap.showType == 111 }">selected="selected"</c:if> >已删除</option>
							  </select>
						</div>
						<div class="kong"></div>
						<div class="activitylist_bodyer_right_team_co1">  
							<div class="activitylist_team_co3_text">目的地：</div>
                            <tags:treeselect id="targetArea" name="targetAreaIdList" value=""  labelName="targetAreaNameList" labelValue="" title="区域" url="/sys/area/treeData" cssClass="required targetArea_no_input" checked="true"/>
						</div>
						
						<!-- mod start by jiangyang -->
<!-- 						<div class="activitylist_bodyer_right_team_co1"> -->
<!--                               <div class="activitylist_team_co3_text">计调：</div><input type="text" class="" name="op" value="${conditionsMap.op }"> -->
<!-- 						</div> -->
						<div class="activitylist_bodyer_right_team_co1">
                        	<div class="activitylist_team_co3_text">计调：</div>
                        	<span style="position:absolute">
								<select id="op" name="op">
									<option value="" >不限</option>
									<c:forEach items="${users}" var="userinfo">
										<option value="${userinfo.id }" <c:if test="${userinfo.id eq conditionsMap.op}">selected="selected"</c:if>>${userinfo.name }</option>
									</c:forEach>
	                        	</select>
	                    	</span>
						</div>						
						<!-- mod end   by jiangyang -->
						
						<div class="activitylist_bodyer_right_team_co1">
                              <div class="activitylist_team_co3_text">渠道选择：</div>
                              <select id="" name="agentId">
                              	<option value="">不限</option>    
                                <c:if test="${not empty agentinfoList }">
									<c:forEach items="${agentinfoList}" var="agentinfo">
										<option value="${agentinfo.id }"  <c:if test="${agentinfo.id == conditionsMap.agentId}">selected="selected"</c:if> >${agentinfo.agentName }</option>
									</c:forEach>
								</c:if>
                              </select>
						</div>
						<div class="kong"></div>
						<!-- add start by jiangyang -->
						<div class="activitylist_bodyer_right_team_co1">
                        	<div class="activitylist_team_co3_text">销售：</div>
                        	<span style="position:absolute">
								<select id="saler" name="saler">
									<option value="" >不限</option>
									<c:forEach items="${users}" var="userinfo">
										<option value="${userinfo.id }" <c:if test="${userinfo.id eq conditionsMap.saler}">selected="selected"</c:if>>${userinfo.name }</option>
									</c:forEach>
	                        	</select>
	                    	</span>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
                        	<div class="activitylist_team_co3_text">下单人：</div>
                        	<span style="position:absolute">
								<select id="picker" name="picker">
									<option value="" >不限</option>
									<c:forEach items="${users}" var="userinfo">
										<option value="${userinfo.id }" <c:if test="${userinfo.id eq conditionsMap.picker}">selected="selected"</c:if>>${userinfo.name }</option>
									</c:forEach>
	                        	</select>
	                    	</span>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
                        	<div class="activitylist_team_co3_text">发票状态：</div>
								<select id="" name="invoiceStatus">
	                              	<option value="" >不限</option>
									<option value="1" <c:if test="${conditionsMap.invoiceStatus==1}">selected="selected"</c:if>>未开发票</option>
									<option value="2" <c:if test="${conditionsMap.invoiceStatus==2}">selected="selected"</c:if>>已开发票</option>
	                        	</select>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
                        	<div class="activitylist_team_co3_text">收据状态：</div>
							<select id="" name="receiptStatus">
	                        	<option value="" >不限</option>
								<option value="1" <c:if test="${conditionsMap.receiptStatus==1}">selected="selected"</c:if>>未开收据</option>
								<option value="2" <c:if test="${conditionsMap.receiptStatus==2}">selected="selected"</c:if>>已开收据</option>
	                        </select>
						</div>						
						<div class="kong"></div>
						<!-- add end   by jiangyang -->
					</div>
					
				</div>
				<div class="kong"></div>
       
		<!-- 产品线路分区 -->
<%-- 		<div class="supplierLine">
			<a class="select" href="javascript:void(0)" onclick="">订单列表</a>
			<c:if test="${order.orderType == 2 }">
				<a href="javascript:void(0)" onclick="">团号列表</a>
			</c:if>
		</div>
 --%>	<input type ="hidden" id="sortNum" name = "sortNum" value = "${conditionsMap.sortNum}"/>
		<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
	                    <li>排序</li>
	                    <c:choose>
	                    <c:when test="${conditionsMap.orderCreateDateCss == 'activitylist_paixu_moren' }">
	                    <li class="activitylist_paixu_moren">
	                    </c:when>
	                    <c:otherwise>
	                    <li class="activitylist_paixu_left_biankuang">
	                    </c:otherwise>
	                    </c:choose>
	                    	<input type="hidden" value="${conditionsMap.orderCreateDateSort}" name="orderCreateDateSort">
	                    	<a onclick="sort(this, 0)">创建日期
	                    		<c:choose>
	                    			<c:when test="${conditionsMap.orderCreateDateSort == 'desc' }">
	                    			<i class="icon icon-arrow-down"></i>
	                    			</c:when>
	                    			<c:when test="${conditionsMap.orderCreateDateSort == 'asc' }">
	                    			<i class="icon icon-arrow-up"></i>
	                    			</c:when>
	                    		</c:choose> 
	                    	</a>
	                    	<input type="hidden" value="${conditionsMap.orderCreateDateCss}" name="orderCreateDateCss">
	                    </li>
	                    
	                    <c:choose>
	                    <c:when test="${conditionsMap.orderUpdateDateCss == 'activitylist_paixu_moren' }">
	                    <li class="activitylist_paixu_moren">
	                    </c:when>
	                    <c:otherwise>
	                    <li class="activitylist_paixu_left_biankuang">
	                    </c:otherwise>
	                    </c:choose>
	                    	<input type="hidden" value="${conditionsMap.orderUpdateDateSort}" name="orderUpdateDateSort">
	                    	<a onclick="sort(this, 1)">更新日期
	                    		<c:choose>
	                    			<c:when test="${conditionsMap.orderUpdateDateSort == 'desc' }">
	                    			<i class="icon icon-arrow-down"></i>
	                    			</c:when>
	                    			<c:when test="${conditionsMap.orderUpdateDateSort == 'asc' }">
	                    			<i class="icon icon-arrow-up"></i>
	                    			</c:when>
	                    		</c:choose>
	                    	</a>
	                    	<input type="hidden" value="${conditionsMap.orderUpdateDateCss}" name="orderUpdateDateCss">
	                    </li>
	                    
	                    <c:choose>
	                    <c:when test="${conditionsMap.startFlightDateCss == 'activitylist_paixu_moren' }">
	                    <li class="activitylist_paixu_moren">
	                    </c:when>
	                    <c:otherwise>
	                    <li class="activitylist_paixu_left_biankuang">
	                    </c:otherwise>
	                    </c:choose>
							<input type="hidden" value="${conditionsMap.startFlightDateSort}" name="startFlightDateSort">
							<a onclick="sort(this, 2)">起飞日期
								<c:choose>
	                    			<c:when test="${conditionsMap.startFlightDateSort == 'desc' }">
	                    			<i class="icon icon-arrow-down"></i>
	                    			</c:when>
	                    			<c:when test="${conditionsMap.startFlightDateSort == 'asc' }">
	                    			<i class="icon icon-arrow-up"></i>
	                    			</c:when>
	                    		</c:choose>
	                    	</a>
	                    	<input type="hidden" value="${conditionsMap.startFlightDateCss}" name="startFlightDateCss">
						</li>
						
						<c:choose>
	                    <c:when test="${conditionsMap.arrivalFlightDateCss == 'activitylist_paixu_moren' }">
	                    <li class="activitylist_paixu_moren">
	                    </c:when>
	                    <c:otherwise>
	                    <li class="activitylist_paixu_left_biankuang">
	                    </c:otherwise>
	                    </c:choose>
	                    	<input type="hidden" value="${conditionsMap.arrivalFlightDateSort}" name="arrivalFlightDateSort">
	                    	<a onclick="sort(this, 3)">到达日期
	                    		<c:choose>
	                    			<c:when test="${conditionsMap.arrivalFlightDateSort == 'desc' }">
	                    			<i class="icon icon-arrow-down"></i>
	                    			</c:when>
	                    			<c:when test="${conditionsMap.arrivalFlightDateSort == 'asc' }">
	                    			<i class="icon icon-arrow-up"></i>
	                    			</c:when>
	                    		</c:choose>
	                    	</a>
	                    	<input type="hidden" value="${conditionsMap.arrivalFlightDateCss}" name="arrivalFlightDateCss">
	                    </li>
                	</ul>
				</div>
				<div class="activitylist_paixu_right">
					查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条
				</div>
				<div class="kong"></div>
			</div>
		</div>
     </form>
		</c:if>
		<table class="table activitylist_bodyer_table" id="contentTable">
			<thead>
				<tr>
					<th width="6%">预定渠道</th>
					<th width="6%">订单编号</th>
					<th width="6%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品编号</span></th>
					<th width="5%">参团类型</th>
					<th width="7%"><span class="salerId on">销售</span>/<span class="createBy">下单人</span></th>
					<th width="11%">预订/剩余时间</th>
					<th width="8%">出/截团日期</th>
					<th width="6%">机票类型</th>
					<th width="4%">人数</th>
					<!-- add by jiangyang -->
					<th width="6%">发票/收据</th>
					<th width="6%">订单状态</th>
					<th width="6%">订单总额</th>
					<th width="6%">已付金额<br>到账金额</th>
					<th width="6%">航班备注</th><!-- 原为备注 经和产品硕宇确定改为航班备注  by chy2015年1月7日15:42:08  -->
					<th width="4%">操作</th>
					<th width="4%">下载</th>
					<th width="4%">财务</th>
				</tr>
			</thead>
			<tbody>
				
				<c:if test="${fn:length(page.list) <= 0 }">
	                 <tr class="toptr" >
	                 	<td colspan="17" style="text-align: center;"> 暂无搜索结果</td>
	                 </tr>
        		</c:if>
        		
				<c:forEach items="${page.list }" var="order">
				
					<tr class="toptr">
						<input id="airticketOrderId" type="hidden" value="${order.id }"/>
						<td>
							
							<c:if test="${not empty order.agentName }">
								<c:if test="${not empty order.paymentStatus }">
										<c:choose>
											<c:when test="${order.paymentStatus == 2 }"><div class="ycq"> 签约渠道 </div><div class="ycq yj" style="margin-top:1px;"> 按月结算 </div></c:when>
											<c:when test="${order.paymentStatus == 3 }"><div class="ycq"> 签约渠道 </div><div class="ycq yj" style="margin-top:1px;"> 担保结算 </div></c:when>
											<c:when test="${order.paymentStatus == 4 }"><div class="ycq"> 签约渠道 </div><div class="ycq yj" style="margin-top:1px;"> 后付费 </div></c:when>
											<c:otherwise>
												<div class="ycq"> 签约渠道 </div>
												${order.nagentName }
											</c:otherwise>
										</c:choose>
								</c:if>
								<c:if test="${order.paymentStatus eq null }"><div class="ycq"> 非签约渠道 </div></c:if>
							 	${order.agentName }
							</c:if>
							
							
						</td>
						<td>
							<c:if test="${not empty order.orderNo }">${order.orderNo }</c:if>
						</td>
						<td>
							<div class="tuanhao_cen onshow">${order.orderGroupCode }</div>
							<div class="chanpin_cen qtip">
								<a href="${ctx}/airTicket/actityAirTickettail/${order.airticketId}" target="_blank">${order.chanpName}</a>
							</div>
						</td>
						<td>
							<c:choose>
								<c:when test="${order.orderType == 1 }">单办</c:when>
								<c:when test="${order.orderType == 2 }">参团</c:when>
							</c:choose>
						</td>
						<td>
							<div class="salerId_cen onshow">
								${order.salerName}
							</div>
							<div class="createBy_cen qtip">
								${order.createUserName}
							</div>
						</td>
						<td class="p0">
							<div class="out-date">
								<fmt:formatDate value="${order.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<div class="close-date">
								${order.leftDays }
							</div>
						</td>
						
						<td class="p0">
							<div class="out-date">
								<c:if test ="${not empty order.groupOpenDate }"><fmt:formatDate value="${order.groupOpenDate }" pattern="yyyy-MM-dd"/></c:if>
							</div>
							<div class="close-date">
								<c:if test="${not empty order.groupCloseDate }"><fmt:formatDate value="${order.groupCloseDate }" pattern="yyyy-MM-dd"/></c:if>
							</div>
						</td>
						
						<td>
							<c:choose>
								<c:when test="${order.airType==1 }">多段</c:when>
								<c:when test="${order.airType==2 }">往返</c:when>
								<c:when test="${order.airType==3 }">单程</c:when>
							</c:choose>
						</td>
						<td><c:if test="${not empty order.personNum }">${order.personNum }</c:if></td>
						
						<!-- add start by jiangyang -->
						<td>
							${fns:getOrderInvoiceReceiptStatus(1,order.realOrderType,order.id) }<br/>
							${fns:getOrderInvoiceReceiptStatus(2,order.realOrderType,order.id) }
						</td>
						<!-- add end   by jiangyang -->
						<td>${fns:getDictLabel(order.order_state,"order_pay_status" , "无")}</td>
						
						<!-- mod by jiangyang 2015-8-3 start -->
						<!-- <td class="tr"><span class="tdorange fbold">${order.totalMoney }</span></td> -->
						<td class="tr pr">
							<c:set var="isrb" value="false"></c:set>
							<c:forEach var="rebat" items="${order.airticketOrderRebatesList }">							
								<c:if test="${not empty rebat.prebt or not empty rebat.infbt }"><c:set var="isrb" value="true"></c:set></c:if>							
							</c:forEach>
                        	<c:if test="${isrb == true }">
	                        	<span class="icon-rebate">
									<span>
										<c:forEach var="rebat" items="${order.airticketOrderRebatesList }">
											<c:if test="${not empty rebat.prebt }">预计返佣:${rebat.prebt }</br></c:if>
											<c:if test="${not empty rebat.infbt }">实际返佣:${rebat.infbt }</c:if>
										</c:forEach>
									</span>
	                        	</span>
                        	</c:if>
                        	<span class="tdorange fbold">${order.totalMoney }</span>
                        </td>
						<!-- mod by jiangyang 2015-8-3 end   -->					
						<td class="p0 tr">
						
										<c:if test="${not empty order.orderPrompt}">
											<div class="notice_price"><span>${order.orderPrompt }</span></div>
										</c:if>		
						
							<div class="yfje_dd">
								<span class="fbold">${order.payedMoney }</span>
							</div>
							<div class="dzje_dd">
								<span class="fbold">${order.accountedMoney }</span>
							</div></td>
						<td class="word-wrap"><c:if test="${not empty order.remark }">${order.remark }</c:if></td>
						<td class="p0">
							<dl class="handle">
								<dt>
									<img src="${ctxStatic}/images/handle_cz.png" title="操作">
								</dt>
								<dd>
									<p>
										<span></span>
										<a target="_blank" href="${ctx}/order/manage/airticketOrderDetail?orderId=${order.id}">详情 </a>
										<c:if test="${order.orderType == 1&& order.activityLockStatus != 1  && order.lockStatus != 1 && order.order_state != 7}">
											<!-- 计调锁死订单时，销售只能进行查看详情操作，update by zhanghao 蔡晓明确认的需求 20150508 -->
<!-- 											<c:if test="${order.order_state!=99 && order.order_state!=111}"> -->
<!-- 												<a target="_blank" href="${ctx}/order/manage/airticketOrderSaleModify?orderId=${order.id}">修改</a> -->
<!-- 											</c:if> -->
											
											<c:if test="${order.order_state != 99 && order.order_state != 111}">
												<a href="${ctx}/airTicketUpProces/list?orderId=${order.id}&productType=7&flowType=10">改价</a>
												<a href="${ctx}/airticketreturn/airticketReturnList?orderId=${order.id}&flowType=3&orderType=7">退票</a>
												<a href="javascript:void(0)"  onclick="uploadTicket()">上传确认单1</a>
												<a href="${ctx}/order/manage/airticketApprovalHistoryList?orderId=${order.id}">改签</a>
											</c:if>
											<c:if test="${order.order_state!=99 && order.order_state!=111 && order.order_state!=3 && order.accounted_money != null}">
												<a href="${ctx}/order/refund/viewList?orderId=${order.id}">退款</a>
											</c:if>
										   	<shiro:hasPermission name="airticketOrderForSale:operation:cancel">
<!-- 												订单状态等于全款未支付，预订后没有支付，或者等于订金未支付，订金占位后没有支付，或者等于已占位 ，或者等于（切位订单并且订金已经支付） -->
												<c:if test="${order.order_state==1 || order.order_state==2 || order.order_state==3 || (order.placeHolderType==1&&order.order_state==4)}">
													<a href="javascript:cancelOrder(${order.id});">取消</a>
												</c:if>
											</shiro:hasPermission>
											<shiro:hasPermission name="airticketOrderForSale:operation:invoke">
												<c:if test="${(order.order_state==99 || order.order_state==2 || order.order_state==3) && order.orderTypeFlag == 'ok'}">
													<a href="javascript:void(0)" onClick="invokeOrder(${order.id})">激活</a>
												</c:if>
											</shiro:hasPermission>
											<a href="${ctx}/order/lendmoney/borrowAmountList?flowType=19&productType=7&orderId=${order.id}"  target="_blank">借款记录</a>
											<a href="${ctx}/order/comdiscount/showComdiscountList?flowType=9&productType=7&orderId=${order.id}"  target="_blank">返佣</a>
											<a href="javascript:applyInvoice('${order.orderNo}',${order.id},7)">申请发票</a>
											<a href="javascript:applyReceipt('${order.orderNo}',${order.id},7)">申请收据</a>
										</c:if>
<!-- 										订单状态不等于已删除，并且不等于订金已经支付，并且不等于已支付全款，并且结算单没锁定，并且订单没锁定,并且订单不等于计调占位 -->
										<c:if test="${order.order_state!=111 && order.order_state!=4 && order.order_state!=5 && order.activityLockStatus != 1 && order.lockStatus != 1 && order.order_state != 7}">
											<a href="javascript:deleteOrderByFlag(${order.id});">删除</a>
										</c:if>
										<a onclick="expand('#child${order.id}',this)">展开</a>
									</p>
								</dd>
							</dl></td>
						<td class="p0">
							<dl class="handle">
								<dt>
									<img src="${ctxStatic}/images/handle_xz.png" title="下载">
								</dt>
								<dd>
									<p>
										<span></span> 
										<c:if test="${order.order_state != 7 }">
											<a onclick="" href="${ctx}/order/manage/airticketOrderTravelExport?orderId=${order.id}">游客资料</a> 
											<a target="" href="${ctx}/order/manage/airticketOrderNameList?orderId=${order.id}">出票名单</a>
										</c:if>
									</p>
								</dd>
							</dl></td>
						<td class="p0">
							<dl class="handle">
								<dt>
									<img src="${ctxStatic}/images/handle_fk.png" title="财务">
								</dt>
								<dd>
									<p>
										<span></span>
										<c:if test="${fn:length(order.orderPayList)>0 && order.order_state != 7}">
											<a href="javascript:void(0)" onClick="javascript:showOrderPay(${order.id},this);" class="jtk">支付记录</a>
										</c:if>
										
										<!-- 付定金、付全款、付尾款 -->
										<c:if test="${order.order_state!=99&&order.order_state!=111&&order.order_state!=89&&order.activityLockStatus != 1&&order.lockStatus != 1 && order.order_state != 7}">
											<c:if test="${order.order_state==1||order.order_state==2||order.order_state==3}">
												<a href="javascript:void(0)" onClick="orderPay('1', '${order.id}', '${order.orderNo }', '7', '${order.agentId}','${order.totalMoney }','${ctx }/order/manage/airticketOrderDetail?orderId=${order.id}','${order.main_order_id }');">付全款</a>
												<a href="javascript:void(0)" onClick="orderPay('3', '${order.id}', '${order.orderNo }', '7', '${order.agentId}','${order.totalMoney }','${ctx }/order/manage/airticketOrderDetail?orderId=${order.id}','${order.main_order_id }');">付订金</a>
												<c:set var="shiroOption" value="trues"></c:set>
											</c:if>
											<c:if test="${order.order_state==4 || (order.order_state==5 && not empty order.lastMoneyCurrencyId)}">
												<a href="javascript:void(0)" onClick="orderPay('2', '${order.id}', '${order.orderNo }', '7', '${order.agentId}','${order.totalMoney }','${ctx }/order/manage/airticketOrderDetail?orderId=${order.id}','${order.main_order_id }');">付尾款</a>
												<c:set var="shiroOption" value="trues"></c:set>
											</c:if>
										</c:if>
										
								   		<!-- 发票明细 -->
								   		<c:if test="${order.order_state != 7 }">
								   			<a  href="javascript:showInvoiceInfo('${order.id}','${order.orderNo}')">发票明细</a><br>
											<a  href="javascript:showReceptInfo('${order.id}','${order.orderNo}')">收据明细</a>
								   		</c:if>
									</p>
								</dd>
							</dl></td>
					 </tr>
					 
				 	<!-- 支付记录 -->
					<tr name="subtr" style="display: none;" class="activity_team_top1">
						<td colspan="17" class="team_top" style="background-color:#dde7ef;">
							<table  class="table activitylist_bodyer_table" style="margin:0 auto;" id="table_orderPay">
								<thead>
									<tr>
										<th>付款方式</th>
										<th>金额</th>
										<th>日期</th>
										<th>支付款类型</th>
										<th>是否已确认达账</th>
										<th>支付凭证</th>
										<c:if test="${order.order_state==4 || order.order_state==5}">
											<th colspan=2>操作</th>
										</c:if>
									</tr>
								</thead> 
								<c:forEach items="${order.orderPayList }" var="orderPay">
									<tr>
										<td>${orderPay.payTypeName }</td>
										<td class="tr">
											<c:choose>
												<c:when test="${not empty orderPay.moneySerialNum}">${orderPay.moneySerialNum }</c:when>
												<c:otherwise>¥ 0.00</c:otherwise>
											</c:choose>
										</td>
										<td class="tc">
											<fmt:formatDate value="${orderPay.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
										</td>
										<td>${fns:getDictLabel(orderPay.payPriceType, "payprice_Type", "")}</td>
										<%-- <td>${fns:getDictLabel(orderPay.isAsAccount, "yes_no", "否")}</td> --%>
										<!--1 达账  0或空 未达账  -->
									
									   <td>
											<c:if test="${orderPay.isAsAccount eq '0'}">
												已撤销
											</c:if>
											<c:if test="${orderPay.isAsAccount eq '1'}">
												已达账
											</c:if>
											<c:if test="${orderPay.isAsAccount eq '2'}">
												已驳回
											</c:if>
											
									   </td>
										
										
										<c:if test="${empty orderPay.payVoucher}">
											<td>暂无支付凭证</td>
										</c:if>
										<c:if test="${not empty orderPay.payVoucher}">
											<td><a class="showpayVoucher" lang="${orderPay.id}">支付凭证</a></td>
										</c:if>
					                         
										<c:if test="${order.order_state==4 || order.order_state==5}">
											<td><a href="javascript:void(0)" onClick="changepayVoucher(${orderPay.id},${order.id})">改支付凭证</a></td>
										</c:if>
									</tr>
								</c:forEach>
							</table>
						</td>
					</tr>
					
						 <tr id="child${order.id}" style="display:none;" class="activity_team_top1">
							<td colspan="17" class="team_top" style="background-color:#d1e5f5;">
								<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
									<thead>
										<tr>
											<th class="tc" width="5%">行程段</th>
											<th class="tc" width="14%">航空公司</th>
											<th class="tc" width="8%">航班号</th>
											<th class="tc" width="14%">出发地机场</th>
											<th class="tc" width="11%">起飞时间</th>
<!-- 											<th width="10%">转机点</th> -->
											<th class="tc" width="14%">到达城市/机场</th>
											<th  class="tc" width="11%">到达时间</th>
											<th class="tc" width="7%">舱位</th>
											<th class="tr" width="10%">价格/人</th>
										</tr>
									</thead>
										<tbody>
										<c:set value="true" var="flag" />
										<c:set value="true" var="flag2" />
										 <c:forEach items="${order.airticketOrderFlights }" var="airticketOrderFlight">
											<tr>
												<td class="tc">${airticketOrderFlight.orderNumber }</td>
												<td class="tc">${fns:getAirlineNameByAirlineCode(airticketOrderFlight.airlines)}</td>
												<td class="tc">${airticketOrderFlight.flight_number}</td>
												<td class="tc">${airticketOrderFlight.startAirportName }</td>
												<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${airticketOrderFlight.startTime}" /></td>
<!-- 												<td class="tc"></td> -->
												<td class="tc">${airticketOrderFlight.endAirportName}</td>
												<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${airticketOrderFlight.arrivalTime}" /></td>
												<td class="tc" style="border-right:1px solid #DDD">${fns:getDictLabel(airticketOrderFlight.spaceGrade,"spaceGrade_Type" , "无")}</td>
												<c:choose>
													<c:when test="${order.airType==1}">
														<c:if test="${order.isSection == 1 }">
														<td class="tr">${airticketOrderFlight.formatedSubAdultPrice}</td>
														</c:if>
														<c:if test="${order.isSection != 1 && flag2 == true}">
														<td class="tr" rowspan="${order.airticketOrderFlights.size()}" style="text-align:center; vertical-align:middle;">${order.formatedAdultPrice }</td>
														<c:set value="false" var="flag2" />
														</c:if>
													</c:when>
													<c:when test="${order.airType==3}">
														<td class="tr">${order.formatedAdultPrice }</td>
													</c:when>
													<c:when test="${order.airType==2 && flag == true}">
														<td class="tr" rowspan="2" style="text-align:center; vertical-align:middle;">${order.formatedAdultPrice }</td>
														<c:set value="false" var="flag" />
													</c:when>
												</c:choose>	
											</tr>
										 </c:forEach>
										</tbody>
									</table>
							</td>
						</tr>
				</c:forEach>
				
			</tbody>
		</table>
	</div>

	<!-- 分页部分 -->
	<div class="pagination clearFix">
		${page}
	</div>
	
</body>
</html>