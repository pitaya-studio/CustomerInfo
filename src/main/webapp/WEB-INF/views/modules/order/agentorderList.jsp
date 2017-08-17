<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>渠道商全部订单</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript">

$(function(){
    $(document).delegate(".downloadzfpz","click",function(){
        window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
        
    });
    
    	$("#contentTable").delegate(".tuanhao","click",function(){
			$(this).addClass("on").siblings().removeClass('on');
			$('.chanpin_cen').removeClass('onshow');
			$('.tuanhao_cen').addClass('onshow');
		});
		$("#contentTable").delegate(".chanpin","click",function(){
			$(this).addClass("on").siblings().removeClass('on');
			$('.tuanhao_cen').removeClass('onshow');
			$('.chanpin_cen').addClass('onshow');
		});
    
        $( ".spinner" ).spinner({
                spin: function( event, ui ) {
                if ( ui.value > 365 ) {
                    $( this ).spinner( "value", 1 );
                    return false;
                } else if ( ui.value < 0 ) {
                    $( this ).spinner( "value", 365 );
                    return false;
                }
                }
            });

        var _$orderBy = $("#orderBy").val();
        if(_$orderBy==""){
            _$orderBy="id DESC";
        }
        var orderBy = _$orderBy.split(" ");
        $(".activitylist_paixu_left li").each(function(){
            if ($(this).hasClass("li"+orderBy[0])){
                orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
                $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
                $(this).attr("class","activitylist_paixu_moren");
            }
        });

		$(document).scrollLeft(0);
		
		$("#targetAreaId").val("${travelActivity.targetAreaIds}");
		$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
		

});
$().ready(function(){
    //产品名称获得焦点显示隐藏
            $("#wholeSalerKey").focusin(function(){
                var obj_this = $(this);
                    obj_this.next("span").hide();
            }).focusout(function(){
                var obj_this = $(this);
                if(obj_this.val()!="") {
                obj_this.next("span").hide();
            }else
                obj_this.next("span").show();
            });
            if($("#wholeSalerKey").val()!="") {
                $("#wholeSalerKey").next("span").hide();
            }
});

function cancelOrder(orderId){
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
		        url: "${ctx}/orderCommon/manage/cancelOrder",
		        data: {
		        	orderId:orderId,
		        	description : f.description
		        },
		        success: function(msg){
		            /* top.$.jBox.tip('删除成功','warning');*/
					/*	top.$('.jbox-body .jbox-icon').css('top','55px');*/
		            location.reload();
		        }
		     });
		}
		return false;
	};
	$.jBox(html, {title: "取消原因", buttons:{'确定' : 1, '取消' : 0 }, submit: submit});
}

function saveAsAcount(orderpayId,_this){
       var $this = $(_this);
        $.jBox.confirm("确定已经到账吗？", "系统提示", function(v, h, f){
            if (v == 'ok') {
                $.ajax({
                type: "POST",
                url: "${ctx}/orderCommon/manage/saveAsAcount",
                data: {
                    orderpayId:orderpayId
                },
                success: function(msg){
                    showOrderPay(msg.id,$this.parent().eq(0),true);
                    var acount= msg.accountedMoney.toString();
                    $this.closest("td").prev().eq(0).text(acount);
                    top.$.jBox.tip('确定成功','success');                   
                    $this.remove();
                }
             });
            }else if (v == 'cancel'){
                
            }
    });
}

function showOrderPay(orderId,thisa,refresh){
    var sbrtr = $(thisa).closest(".toptr").next();
    var sbrtd = sbrtr.children().eq(0);
    if(refresh){
        sbrtd.empty();
    }
    var table = sbrtr.find("table");
    if(table.length<=0){
        $.ajax({
            type: "POST",
            url: "${ctx}/orderCommon/manage/getPayList",
            data: {
                orderId:orderId
            },
            success: function(msg){
                var $table = $("<table  class=\"table activitylist_bodyer_table\" style=\"margin:0 auto;\"></table>").append("<thead style=\"background:#62AFE7\"><tr><th>付款方式</th><th>金额</th><th>日期</th><th>支付款类型</th><th>是否已确认达账</th><th>支付凭证</th></tr></thead>");
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
                    
                    var payvoucher = "<td><a class=\"downloadzfpz\" lang=\""+value.payVoucher+"\">支付凭证</a></td>";
                    
                    if(payvoucher==""||payvoucher==undefined||payvoucher==null){
                        payvoucher = "<td>暂无支付凭证</td>";
                    }
                    $table.append($("<tr></tr>")
                            .append("<td>"+payTypeName+"</td>")
                            .append("<td>"+value.payPrice+"</td>")
                            .append("<td>"+value.createDate+"</td>")
                            .append("<td>"+payPriceType+"</td>")
                            .append("<td>"+isAsAccount+"</td>")
                            .append(payvoucher)
                    )
                });
                sbrtd.append($table);
            }
         });
    }
    if($(thisa).hasClass("jtk")){
        //var td = sbrtr.prev().find("td").eq(6);
        var td = $(thisa).closest("td");
        if(sbrtr.is(":hidden")){
            sbrtr.show();
            td.addClass("td-extend");
        }else{
            sbrtr.hide();
            td.removeClass("td-extend");
        }
    }
}

function changeGroup(orderId){
    window.open("${ctx}/orderCommon/manage/changGroup/"+orderId,"_blank");
}

function orderDetail(orderId){
    window.open("${ctx}/orderCommon/manage/orderDetail/"+orderId,"_blank");
}

function changePrice(htmlheard,orderId){
    var html=htmlheard;
    var innerHtml = "${ctx}/orderCommon/manage/saveChangePrice";
    $.ajax({
        type: "POST",
        url: html,
        data: {
            orderId:orderId
        },
        success: function(msg){
            var $div = $("<div style=\"padding:10px;\"></div>");
            $div.append($("<dl class=\"pop_gj\"></dl>")
                    .append("<dt><span>订单号：</span><p>"+msg.productorder.orderNum+"</p></dt>")
                    .append("<dt><span>产品名称：</span><p>"+msg.acitivityName+"</p></dt>")
            );
            $dl = $("<dl class=\"pop_xg\"></dl>");
            if(msg.productorder.payStatus!='4'){
               $dl.append("<dt><span>订金原价：</span><p>¥ <font>"+msg.productorder.frontMoney+"</font></p><span>订金改后价：</span><p><input type=\"text\" name=\"frontMoney\" id=\"frontMoney\"><i>¥</i></p></dt>");
            }else{
               $dl.append("<dt style='display:none' ><span>订金原价：</span><p>¥ <font>"+msg.productorder.frontMoney+"</font></p><span>订金改后价：</span><p><input type=\"text\" name=\"frontMoney\" id=\"frontMoney\"><i>¥</i></p></dt>");
            }
               $dl.append("<dt><span>全款原价：</span><p>¥ <font>"+msg.productorder.totalMoney+"</font></p><span>全款改后价：</span><p><input type=\"text\" name=\"totalMoney\" id=\"totalMoney\"><i>¥</i></p></dt>");
            $div.append($dl);
            
            var html = "<div style='padding:10px;'>"+$div.html()+"</div>";
            var submit = function (v, h, f) {
                var totalMoney = f.totalMoney;
                var frontMoney = f.frontMoney;
                var reg = /^\d+$/;
                if(frontMoney!=""&&frontMoney!=undefined){
                    if(!reg.test(frontMoney)){
                        top.$.jBox.tip('请输入正整数','error');
                        return false;
                    }
                }
                
                if(totalMoney!=""&&totalMoney!=undefined){
                    if(!reg.test(totalMoney)){
                        top.$.jBox.tip('请输入正整数','error');
                        return false;
                    }
                }
                
                if((frontMoney==""||frontMoney==undefined)&&(totalMoney==""||totalMoney==undefined)){
                    return true;
                }
                
                var tempfrontMoney = frontMoney;
                var temptotalMoney=totalMoney;
                
                if(frontMoney==""||frontMoney==undefined){
                    tempfrontMoney = msg.productorder.frontMoney;
                }
                if(totalMoney==""||totalMoney==undefined){
                    temptotalMoney = msg.productorder.totalMoney;
                }
                
                if(Number(frontMoney)>Number(temptotalMoney)){
                    top.$.jBox.tip('订金不可大于总价','error');
                    return false;
                }
                
                $.ajax({
                    type: "POST",
                    url: innerHtml,
                    data: {
                        orderId:orderId,
                        totalMoney:totalMoney,
                        frontMoney:frontMoney
                    },
                    success: function(msg){
                        location.reload();
                    }
                 });
                return false;
            };
            $.jBox(html, { title: "输入需要修改的金额？", submit: submit,width:450});
        }
     });
    
}

function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    if($("#listtype").val() == "") {
    	$("#listtype").val(${param.type})
    }
    $("#searchForm").submit();
    return false;
}

$(function(){
    $.fn.datepicker=function(option){
        var opt = {}||option;
        this.click(function(){
           WdatePicker(option);            
        });
    }
    
    $("#groupOpenDate").datepicker({
        dateFormat:"yy-mm-dd",
       dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
       closeText:"关闭", 
       prevText:"前一月", 
       nextText:"后一月",
       monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
       });
    
    $("#groupCloseDate").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
    
            // var Class=$('.nav-tabs li');
             //if(Class.hasClass('current')&&Class.hasClass('ernav')&&Class.hasClass('active'))
            // {
             //    Class.parent().addClass('nav_current');
           //  }
    
            $('.nav-tabs li').hover(function(){
                $('.nav-tabs li').removeClass('current');
                $(this).parent().removeClass('nav_current');
                 if($(this).hasClass('ernav'))
                 {
                     if(!$(this).hasClass('current')){
                        $(this).addClass('current');
                        $(this).parent().addClass('nav_current');
                     }
                 }
                },function(){
                    $('.nav-tabs li').removeClass('current');
                    $(this).parent().removeClass('nav_current');
                    var _active = $(".totalnav .active").eq(0);
                    if(_active.hasClass('ernav')){
                        _active.addClass('current');
                        $(this).parent().addClass('nav_current');
                    }
                });
        });

function sortby(sortBy,obj){
    var temporderBy = $("#orderBy").val();
    if(temporderBy.match(sortBy)){
        sortBy = temporderBy;
        if(sortBy.match(/ASC/g)){
            sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
        }else{
            sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
        }
    }else{
        sortBy = sortBy+" DESC";
    }
    
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}


function applyLeague(orderId){
    
	var $div = $('<div class=\"tanchukuang\"></div>')
	.append('<div class="msg-orderCancel"><div class="msg-orderCancel-t">请输入退团原因（输入字数为100字以内）：</div><textarea cols="30" rows="3" name="description"></textarea></div>');
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
		    	url: "${ctx}/orderCommon/manage/applyLeague",
		    	data: {
		    		orderId:orderId,
		    		description : f.description
		    	},
		   	 	success: function(msg){
		   	 		/*top.$.jBox.tip('退团成功','warning');*/
					/*	top.$('.jbox-body .jbox-icon').css('top','55px');*/
		        	location.reload();
		    	}
		 	});
		}
		return false;
	};
	$.jBox(html, {title: "退团原因", buttons:{'确定' : 1, '取消' : 0 }, submit: submit});
	
}

</script>

</head>
<body>
<style type="text/css">label{ cursor:inherit;}</style>
    <content tag="three_level_menu">
        <li class="active"><a href="javascript:void(0)"><c:if test="${param.type==1 or listtype==1}">按销售额订单详情</c:if><c:if test="${param.type==2 or listtype==2}">按游客数订单详情</c:if></a></li>
    </content>
    <div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
<form:form id="searchForm" action="${ctx}/orderCommon/manage/showAgentOrderList/${agentId}" method="post" >
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="orderTime desc"/>
        <input id="listtype" name="type" type="hidden" value="${listtype }"/>
        
        <div class="activitylist_bodyer_right_team_co">
        <div class="activitylist_bodyer_right_team_co2"  style="margin-bottom: 20px;">
                            <span>渠道商名称：${agentName}</span>
                         </div>
        </div>
          <div class="kong"></div>
        </form:form>
        
<table id="contentTable" class="table table-striped table-bordered table-condensed activitylist_bodyer_table"  style="margin-left:0px;">
        <thead style="background:#403738;">
        <tr>
            <th width="3%">序号</th>
            <th width="8%">预定渠道</th>
            <th width="10%">订单号</th>
            <th width="10%">产品编号</th>
            <th width="11%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span></th>
            <th width="5%">预订人</th>
  			<th width="11%">预订时间</th>
            <!--<th width="5%">预订人电话</th>-->
            <th width="5%">预订</th>
   			<th width="7%">出/截团日期</th>
            <th width="7%">订单状态</th>
            <th width="8%">订单总额</th>
            <th width="8%">已付金额<br>到账金额</th>
            <th width="7%">操作</th>

        </tr>
        </thead>
        
        <tbody>
        
         <c:if test="${fn:length(page.list) <= 0 }">
                 <tr class="toptr" >
                 <td colspan="18" style="text-align: center;">
                                      暂无搜索结果
                 </td>
                 </tr>
        </c:if>
        
        <c:forEach items="${page.list }" var="orders" varStatus="s">
                <tr class="toptr">
                    <td>${s.count }</td>
                    <td>${orders.orderCompanyName } </td>
                    <td>${orders.orderNum }</td>
                    <td>${orders.activitySerNum }</td>
                    <td>
                    	<div class="tuanhao_cen onshow">${orders.groupCode }</div>
                    	<div class="chanpin_cen qtip" title="Test-wyy0811-hh">${orders.acitivityName}</div>
                   	</td>
                    <td>${orders.orderPersonName }</td>
                    <td class="tc">
                      <fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                    <td class="tr">${orders.orderPersonNum }</td>
                    <td class="p0">
                    	<div class="out-date"><fmt:formatDate value="${orders.groupOpenDate}" pattern="yyyy-MM-dd"/></div>
                    	<div class="close-date"><fmt:formatDate value="${orders.groupCloseDate}" pattern="yyyy-MM-dd"/></div>
                    </td>
                    <!-- 
                      <td>${orders.orderPersonPhoneNum }</td>
                     -->
                    <td>
                          ${fns:getDictLabel(orders.payStatus, "order_pay_status", "")}
                    </td>
                    <td class="tr">
                    	<c:if test="${not empty orders.totalMoney}">¥</c:if><span class="tdorange fbold"><fmt:formatNumber value='${orders.totalMoney}' type="currency" pattern="#,##0.00" /></span>
                    </td>
             
             		<td class="p0 tr">
             			<div class="yfje_dd"><c:if test="${not empty orders.payedMoney}">¥</c:if><span class="fbold"><fmt:formatNumber value='${orders.payedMoney}' type="currency" pattern="#,##0.00" /></span></div>
             			<div class="dzje_dd"><c:if test="${not empty orders.accountedMoney}">¥</c:if><span class="fbold"><fmt:formatNumber value='${orders.accountedMoney}' type="currency" pattern="#,##0.00" /></span></div>
             		</td>
                    <td class="tc">
                    <%--已经支付的不显示 --%>
                    
                <shiro:hasPermission name="looseOrder:operation:view">
                            	<a href="javascript:void(0)" onClick="javascript:orderDetail(${orders.id});">查看详情 </a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="order:list:payview">
                            <c:if test="${fn:length(orders.orderPayList)>0 }">
                            	<a href="javascript:void(0)" onClick="javascript:showOrderPay(${orders.id},this);" class="jtk">支付记录</a>
                            </c:if>

                        </shiro:hasPermission>
                       
                    </td>
                   
                </tr>
                <tr name="subtr" style="display: none;" class="activity_team_top1">
                      <td colspan="16" class="team_top" style="background-color:#dde7ef;">
                        <table  class="table activitylist_bodyer_table" style="margin:0 auto;">
                    <thead>
                        <tr>
                           <th>付款方式</th>
                           <th>金额</th>
                           <th>日期</th>
                           <th>支付款类型</th>
                           <th>是否已确认达账</th>
                           <th>支付凭证</th>
                        </tr>
                       </thead> 
                         <c:forEach items="${orders.orderPayList }" var="orderPay">
                            <tr>
                            <td>${orderPay.payTypeName }</td>
                            <td>${orderPay.payPrice }</td>
                            <td>
                                <fmt:formatDate value="${orderPay.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>${fns:getDictLabel(orderPay.payPriceType, "payprice_Type", "")}</td>
                            <td>${fns:getDictLabel(orderPay.isAsAccount, "yes_no", "否")}</td>
                            <c:if test="${empty orderPay.payVoucher}">
                                <td>暂无支付凭证</td>
                            </c:if>
                            <c:if test="${not empty orderPay.payVoucher}">
                                <td><a class="downloadzfpz" lang="${orderPay.payVoucher}">支付凭证</a></td>
                            </c:if>
                            </tr>
                        </c:forEach>
                        </table>
                      </td>
                </tr>
            </c:forEach>
        </tbody>
        
    </table>
    <div class="pagination">${page}</div>
	 <div class="ydbz_sxb">
	               <div class="ydBtn ydBtn2">
	                <a class="ydbz_s" onClick="window.close();">关闭</a>
	            </div>
	 </div>
    </div>
 
 
 <style type="text/css">
.orderList_dosome{ text-align:left; margin-left:11px;}
.orderList_line{ height:100%; width:50px;float:left; }
 </style>  
</body>
</html>