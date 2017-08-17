<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机票订单发票记录</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">

$(function(){
    $(document).scrollLeft(0);
	$("#targetAreaId").val("${travelActivity.targetAreaIds}");
	$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
	<%-- 前端js效果部分 --%>
			
    $("#contentTable").delegate("ul.caption > li","click",function() {
        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
        $(this).addClass("on").siblings().removeClass('on');
        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
    });
		
    $('.handle').hover(function() {
    	if(0 != $(this).find('a').length){
    		$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
    	}
	},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
  		
    //展开筛选按钮
  		$('.zksx').click(function(){
		if($('.ydxbd').is(":hidden")==true)
		{
			$('.ydxbd').show();
			$(this).text('收起筛选');
			$(this).addClass('zksx-on');
		}else
		{
			$('.ydxbd').hide();
			$(this).text('展开筛选');
			$(this).removeClass('zksx-on');
		}
	});
	//如果展开部分有查询条件的话，默认展开，否则收起	
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
	var searchFormselect = $("#searchForm").find("select[id!='orderShowType']");
	var inputRequest = false;
	var selectRequest = false;
	for(var i = 0; i<searchFormInput.length; i++) {
		if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
			inputRequest = true;
		}
	}
	for(var i = 0; i<searchFormselect.length; i++) {
		if($(searchFormselect[i]).children("option:selected").val() != "" && 
				$(searchFormselect[i]).children("option:selected").val() != null) {
			selectRequest = true;
		}
	}
	if($("#orderShowType").children("option:selected").val() != "0") {
		selectRequest = true;
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}
			
    
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


});

<%-- 产品名称获得焦点显示隐藏 --%>
$().ready(function() {
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

    
//刷新 
function refresh(){
	setTimeout(location.reload(true),10000);   
}


function page(n,s){
	var url = "${ctx}/orderInvoice/manage/getInvoiceListByOrderNum?orderNum="+"${orderNum }";
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").attr("action",url);
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
           
   $("#orderTimeBegin").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
	$("#orderTimeEnd").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
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

/**
 * 查询条件重置
 * 
 */
var resetSearchParams = function(){
    $(':input','#searchForm')
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
    $('#targetAreaId').val('');
    $('#orderShowType').val('${showType}');
}

function query() {
	$('#searchForm').submit();
}

function expand(child, obj, srcActivityId, payMode_deposit, payMode_advance, payMode_full,payMode_op,payMode_cw,payMode_data,payMode_guarantee,payMode_express,showType) {
    if($(child).is(":hidden")){
        if (payMode_deposit != "") {

        	//暂时取消，币种，订单金额，计调人员的查询
        	var currencyId = "";//$("#c_cny").val();
        	var orderMoneyBegin = "";//$("#m_order_money_begin").val();
        	var orderMoneyEnd = "";//$("#m_order_money_end").val();
        	var operator = "";//$("#p_operator").val();
            $.ajax({
            	type : "post",
				url : "${ctx}/invoice/limit/supplyviewdetailopen/" + payMode_deposit + "?1=1&currencyId="
						              + currencyId+"&orderMoneyBegin=" +orderMoneyBegin + "&orderMoneyEnd=" 
						              + orderMoneyEnd + "&operator=" + operator,
				success : function(msg) {
					if(msg.length > 0){
						var record = '';
						for(var i = 0; i < msg.length; i++) {
							if(msg[i][6] == null)
								msg[i][6] = "";
							if(msg[i][7] == null)
								msg[i][7] = "";
							if(msg[i][8] == null)
								msg[i][8] = "";
							if(msg[i][9] == null)
								msg[i][9] = "";
							record += "<tr><td class='tc'>" + (i+1) + "</td><td class='tc'>" + msg[i][12] + "</td><td class='tc'>" + msg[i][0] 
							+ "</td><td class='tc'>" + msg[i][2] + "</td><td class='tc'>" + msg[i][1] + "</td><td class='tc'>" + msg[i][3] +"</td><td class='tc'>" +  msg[i][15]
							+ "</td><td class='tc'>" + msg[i][4] + "</td><td class='tc'>" + msg[i][5] + "</td><td style='padding: 0px;' class='tc'><div class='out-date'>" + msg[i][6] 
							+ "</div><div class='close-date'>" + msg[i][7] + "</div></td><td class='tr'>" + msg[i][14] + "</td><td class='tr'>" + msg[i][9] 
							+ "</td><td class='tr'>" + msg[i][10] + "</td><td class='tr'>" + msg[i][11] + "</td></tr>";
						}
						$(child).find("td table tbody").html(record);
                	}else {
                		$(child).find("td table tbody").html('<tr><td colspan="14" class="tc">暂无数据</td></tr>');
                	}
					$(obj).html("关闭订单列表");
					$(child).show();
					$(obj).parents("td").addClass("td-extend");
				}
            });
        }else{
        	$(obj).html("关闭订单列表");
            $(child).show();
            $(obj).parents("td").addClass("td-extend");
        }
    }else{
        if(!$(child).is(":hidden")){
            $(child).hide();
            $(obj).parents("td").removeClass("td-extend");
            $(obj).html("展开订单列表");
        }
    }
}
function viewdetail(invoiceNum,verifyStatus){
	window.open("${ctx}/orderInvoice/manage/viewInvoiceInfo/" + invoiceNum + "/-2/${orderType}");
}
function makinvoice(invoiceNum,verifyStatus){
	
	window.location.href = "${ctx}/orderInvoice/manage/supplyviewrecorddetail/" + invoiceNum + "/" + verifyStatus;
	
}
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
				window.location.href ="${ctx}/orderInvoice/manage/applyInvoice?orderNum=${orderNum}&orderId=${orderId}&orderType=${orderType}";
			else if("canOrDel"==msg.msg)
				top.$.jBox.tip('已取消或已删除订单不能开发票！','success');
			else
				top.$.jBox.tip('申请失败，订单创建人和申请人不一致！','success'); 
		}
    });
	
}
function receive(invoiceNum){
	$("#receiveInvoiceHid").val("true");
	$("#searchForm").attr("action","${ctx}/orderInvoice/manage/verifyinvoice/"+invoiceNum);
	$("#searchForm").submit();
}
// 撤销未审核发票
function cancelInvoice(orderId,orderType,orderInvoice){
	var content = {
			content : "确认撤销？",
			buttons : {"确认":1,"取消":0},
			buttonsFocus : 0,
			submit : function(v,h,f){
				if(v==0){
					return true;
				}else{
					if(orderId && orderType && orderInvoice){
						$.ajax({
							type:"POST",
							url:"${ctx}/orderInvoice/manage/cancelOrderInvoice",
							data : "{orderId:"+orderId+",orderType:"+orderType+",orderInvoice:"+orderInvoice+"}",
							success : function(msg) {
								if(msg.res=="success"){
									$.jBox.tip("操作成功",success);
								}else{
									$.jBox.tip("mes",info);
								}
							}
						});
					}
					return false;
				}
			}
	};
	$.jBox(content);
}
</script>

<style type="text/css">
a{
    display: inline-block;
}

*{ margin:0px; padding:0px;}
body{ background:#fff; margin:0px auto;}
.pop_gj{ padding:10px 24px; margin:0px; border-bottom:#b3b3b3 1px dashed; overflow:hidden;}
.pop_gj dt{ float:left; width:100%;}
.pop_gj dt span{ float:left; width:80px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:25px; line-height:180%;}
.pop_gj dt p{ float:left; width:300px;color:#000; font-size:12px;line-height:180%;}
.pop_xg{ padding:10px 4px; margin:0px; overflow:hidden;}
.pop_xg dt{ float:left; width:100%; margin-top:10px; height:30px;}
.pop_xg dt span{ float:left; width:100px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:30px; line-height:30px;}
.pop_xg dt p{ float:left; width:110px;color:#333; font-size:12px;height:30px; line-height:30px;overflow:hidden; position:relative;}
.pop_xg dt p font{ color:#e60012; font-size:12px;}
.pop_xg dt p input{width:60px; height:28px; line-height:28px; padding:0px 5px 0px 18px; color:#403738; font-size:12px; position:relative; z-index:3; }
.pop_xg dt p i{ position:absolute; height:28px; top:2px; width:10px; text-align:center; left:5px; z-index:5; font-style:normal; line-height:28px;}
.release_next_add button{ cursor:pointer; border-radius:4px;}
label{ cursor:inherit;}
</style>

</head>

<body>
<div class="mod_nav">订单 > 机票订单> 发票记录</div>
<div class="ydbz_tit orderdetails_titpr" id="invoiceApply">发票记录<c:if test="${applySign ==1 }"><a class="ydbz_x" href="javascript:applyInvoice('${orderNum}',${orderId},${orderType})">申请发票</a></c:if></div>
	

	<c:if test="${fn:length(page.list) ne 0}">	
  <table class="activitylist_bodyer_table" id="contentTable">
          <thead>
            <tr>
              <th width="5%">序号</th>
              <th width="9%">开票类型</th>
              <th width="10%">开票客户</th>
              <c:if test="${param.verifyStatus ne '0'}">
              <th width="8%">申请日期</th>
              <th width="8%">开票日期</th>
              </c:if>
              <th width="10%">申请人</th>
              <th width="10%">开票金额</th>
              <c:if test="${param.verifyStatus eq '1' }">
              <th>开票状态</th>
              </c:if>
              <th width="10%">发票状态</th>
              <th width="13%">开票原因</th>
              <th width="10%">操作</th>
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${page.list}" var="orderinvoice" varStatus="s">
            <tr>
              <td>${s.index+1}</td>
              <td><c:if test="${orderinvoice.invoiceType == 1 }">团款</c:if>
              <c:if test="${orderinvoice.receiveStatus == 1 }"><font style="color:red">（已领取）</font></c:if>
               </td>
              <td>${orderinvoice['invoiceCustomer']}</td>
              <c:if test="${param.verifyStatus ne '0'}">
              <td class="tc">${orderinvoice['createDate']}</td>
              <td class="tc"><fmt:formatDate value="${orderinvoice['updateDate']}" pattern="yyyy-MM-dd"/></td>
              </c:if>
              <td>${orderinvoice['createName']}</td>
              <td class="tr">¥<span class="fbold tdorange">${orderinvoice['invoiceAmount']}</span></td>
              <c:if test="${param.verifyStatus eq '1' }">
              <c:if test="${orderinvoice['createStatus'] eq '0'}">
						<td class="invoice_no">待开票</td>
					</c:if>
					<c:if test="${orderinvoice['createStatus'] eq  '1'}">
						<td class="invoice_yes">已开票</td>
					</c:if>
			</c:if>			
              <c:if test="${orderinvoice['verifyStatus'] eq '0'}">
						<td class="invoice_no">未审核</td>
					</c:if>
					<c:if test="${orderinvoice['verifyStatus'] eq  '1'}">
						<td class="invoice_yes">审核通过</td>
					</c:if>
					<c:if test="${orderinvoice['verifyStatus'] eq  '2'}">
						<td class="invoice_back">被驳回</td>
					</c:if>
					<c:if test="${orderinvoice['verifyStatus'] eq  '3'}">
						<td class="invoice_back">已取消</td>
					</c:if>
              <td>${orderinvoice['remarks']}</td>
              <td>
              	<c:if test="${orderinvoice['verifyStatus'] eq '0'}">
                	<a class="team_a_click" href="javascript:void(0)" onClick="cancelInvoice('${orderId}','${orderType}','${orderinvoice['invoiceNum']}')">撤销</a>
                </c:if>
              	<a href="javascript:void(0)" onClick="viewdetail('${orderinvoice['invoiceNum']}')">详情</a>
                <a class="team_a_click" href="javascript:void(0)" onclick="expand('#child${s.count}',this,'${s.count}','${orderinvoice['invoiceNum']}')" >展开订单列表</a>
              </td>
            </tr>
            <tr id="child${s.count}" style="display: none;" class="activity_team_top1">
              <td colspan="10" class="team_top">
              <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
                  <thead>
                  	<tr>
                      <th class="tc" width="4%">序号</th>
                      <th class="tc" width="6%">订单类型</th>
                      <th class="tc" width="9%">订单号</th>
                      <th class="tc" width="9%">开票客户</th>
                      <th class="tc" width="10%">团号</th>
                      <th class="tc" width="6%">销售</th>
                      <th class="tc" width="6%">计调</th>
                      <th class="tc" width="7%">下单时间</th>
                      <th class="tc" width="5%">人数</th>
                      <th class="tc" width="7%">出/截团日期</th>
                      <th class="tr" width="9%">应收金额</th>
                      <th class="tr" width="9%">财务到账</th>
                      <th class="tr" width="9%">已开票金额</th>
                      <th class="tr" width="10%">开票金额</th>
                    </tr>
                  </thead>
                  <tbody>
                  </tbody>
                </table>
                </td>
            </tr>
           </c:forEach>
          </tbody>
        </table>
        </c:if>
        <c:if test="${fn:length(page.list) eq 0}">
			<table id="contentTable" class="table activitylist_bodyer_table" >
				<thead>
					 <tr>
                      <th class="tc" width="4%">序号</th>
                      <th class="tc" width="6%">订单类型</th>
                      <th class="tc" width="9%">订单号</th>
                      <th class="tc" width="9%">开票客户</th>
                      <th class="tc" width="10%">团号</th>
                      <th class="tc" width="6%">销售</th>
                      <th class="tc" width="6%">计调</th>
                      <th class="tc" width="7%">下单时间</th>
                      <th class="tc" width="5%">人数</th>
                      <th class="tc" width="7%">出/截团日期</th>
                      <th class="tr" width="9%">应收金额</th>
                      <th class="tr" width="9%">财务到账</th>
                      <th class="tr" width="9%">已开票金额</th>
                      <th class="tr" width="10%">开票金额</th>
                    </tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="13" style="text-align: center;">暂无数据</td>						
					</tr>
				</tbody>
			</table>
		</c:if>
</body>
</html>
