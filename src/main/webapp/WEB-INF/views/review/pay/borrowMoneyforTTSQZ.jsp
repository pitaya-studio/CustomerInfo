<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证批量借款付款</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/payOrRefund.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/cost/finance_common.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/modules/visa/visaBorrowMoneyReviewList.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/Remark_New.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery.nicescroll.min.js"></script>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/Remark_New.css">
<script type="text/javascript">
var ctx = '${ctx }';
var companeyUUID = '${fns:getUser().company.uuid}';

Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}  
//如果展开部分有查询条件的话，默认展开，否则收起	
function closeOrExpand(){
	var inputRequest = false;
	var selectRequest = false;
	$("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='groupCode']").each(function(){
		if($(this).val()){
			inputRequest = true;
			return false;
		}
	});
	$("#searchForm").find("select").each(function(){
		if($(this).children("option:selected").val()){
			selectRequest = true;
			return false;
		}
	});
	if(inputRequest || selectRequest) {
		$('.zksx').click();
	}
}
function query() {
	$('#searchForm').submit();
}

function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}
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

function cancelOper(ctx,str,obj){
    $.jBox("iframe:"+ctx+"/refund/manager/cancelPayInfo?refundId="+str+"&flag=edit&orderType=6",{
        title: "付款记录",
		width:830,
   		height: 500,
   		buttons:{'撤销': 1,'关闭':0},
   		persistent:true,
   		loaded: function(h){},
   		submit:function(v,h,f){
   		    if(v==1){
   		         var refundId =  $(h.find("iframe")[0].contentWindow.refundId).val();
   		         var recordId =  $(h.find("iframe")[0].contentWindow.recordId).val();
   		         $.ajax({
   		            type:"GET",
   		            url:ctx+"/refund/manager/undoRefundPayInfo",
   		            dataType:"json",
   		            data:{refundId:refundId,recordId:recordId},
   		            success:function(data){
   		              if(data.flag=='ok'){
   		            	  cancelOptionMoney(obj);
   		              }
   		            }
   		         });
   		    }
   		}
    }).find("#jbox-content").css("overflow", "hidden"); 
}

function cancelOptionMoney(obj){
	var moneyObj = $(obj).parents('.activity_team_top1').prev().find("div.dzje_dd");//主表中的 已付金额  例如 $256.30
    var currencyDzje = moneyObj.text().trim();             
    if(currencyDzje){
    	var operator = currencyDzje.match(/-/g) || [""];
    	var numArray = currencyDzje.replace(new RegExp(/,/g),"").match(/\d+/g);                 //取出金额中的数值 $256.30 --> [256,30]
        var dzje = null;
        if(numArray.length == 2){
        	dzje = parseFloat(operator[0] + numArray[0] + '.' + numArray[1]).toFixed(2);
        }
    	var cny = currencyDzje.replace(dzje,'');                   //获取币种符号 
    	
        var index = $(obj).parent().siblings().first().text().trim();//付款记录里面的索引号
        var id = '#money_' + index;
    	var currencyRecordMoney = $(obj).parent().siblings().filter(id).text().trim(); //付款记录里面的金额
    	console.log(currencyRecordMoney);
    	var recordMoney = null;
    	if(currencyRecordMoney){
    		var recordOperator = currencyRecordMoney.match(/-/g) || [""];
    		var recordNumArray = currencyRecordMoney.replace(new RegExp(/,/g),"").match(/\d+/g);
    		if(recordNumArray.length==2){
    			recordMoney = parseFloat(recordOperator[0] + recordNumArray[0] + '.' + recordNumArray[1]).toFixed(2);
    		}else{
    			recordMoney = parseFloat(recordOperator[0] + recordNumArray[0] + '.00').toFixed(2);
    		}
    		var cha = parseFloat(dzje - recordMoney).toFixed(2);
    		console.log(dzje);
    		console.log(recordMoney);
    		console.log(cha);
    		if(cha == 0){
    			moneyObj.html('<span class="fbold"> </span>');
    		}else{
    			moneyObj.html("<span class='fbold'>"+(cny + cha)+"</span>");
    		}
    	}                  //移除撤销按钮 
    }
    $(obj).parent().prev().html('已撤销');//状态改成已撤销 
    $(obj).next().remove();             //移除打印按钮 
    $(obj).remove();                    //移除撤销按钮 
}

function cancelDetailOper(ctx,refundId,recordId,obj){
	$.ajax({
		type:"GET",
       	url:ctx+"/refund/manager/undoRefundPayInfo",
       	dataType:"json",
       	data:{refundId:refundId,recordId:recordId},
       	success:function(data){
			if(data.flag=='ok'){
            	$(obj).parents("table[id=contentTable]").siblings('#searchForm').submit();
         	}
       	}
	});
}

function expand(child,obj,batchId,orderType,index, orderId, reviewId, reviewUUID, batchNo, isPrintFlag, reviewFlag) {
	$.ajax({
		url:"${ctx}/costNew/payManager/getTTSQZPayRecord",
       	type:"POST",
       	data:{batchId:batchId,orderType:orderType},
       	success:function(data){
			var htmlstr="";
           	if(data != null && data != '' && data.length > 0){
				for(var i=0;i<data.length;i++){
					var payOrder = '';//支付凭证
            	   	var payOrderArray = data[i].payVoucher.split("|");
                   	for(var j=0;j<payOrderArray.length;j++){
                    	payOrder+=payOrderArray[j]+"<br/>";
                   	}
        		   	var status = '';
        		   	if(data[i].status == 0){
        				status = '已撤销';
        		   	}else{
        			   	status = '已支付';
        		   	}
					if (!data[i].payType){ <%-- 拉美图的因公支付宝结算方式暂未保存到字典表中。后期如做调整可删除 --%>
						data[i].payType = "因公支付宝";
					}
                   	htmlstr+="<tr><td class='tc'>"+(i+1)+"</td><td class='tc'>"+data[i].payType+
                            "</td><td id='money_"+(i+1)+"' style='text-align:center'>"+data[i].amount+"</td><td class='tc'>"+
                            (new Date(data[i].payDate)).Format("yyyy-MM-dd hh:mm:ss")+
                            "</td><td class='tc'>借款付款</td><td class='tc'>"+payOrder+
                            "</td><td class='tc'>"+status+"</td><td class='tc'>";
                   	if(data[i].status == undefined){
                   		if(reviewFlag == '1') {
                   			 htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,6,this)\" href='javascript:void(0)'>查看</a>&nbsp;&nbsp;"+
                   			    "<a onclick=\"cancelOper('${ctx}','"+data[i].id+"',this)\" >撤销</a>&nbsp;&nbsp;"+
                   			    "<a target=\"_blank\" href=\"${ctx}/visa/workflow/borrowmoney/visaBorrowMoneyBatchFeePrint?revid="+reviewId+"&batchno="+batchNo+"&isPrintFlag="+isPrintFlag+"&payId="+data[i].id+"&option=pay"+"\">打印</a>"+
                   			    "</td></tr>";
                   		}else {
                   			 htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,6,this)\" href='javascript:void(0)'>查看</a>&nbsp;&nbsp;"+
        		               "<a onclick=\"cancelOper('${ctx}','"+data[i].id+"',this)\">撤销</a>&nbsp;&nbsp;"+
        		               "<a target=\"_blank\" href=\"${ctx}/visa/hqx/borrowmoney/visaBorrowMoney4HQXBatchFeePrint?revid="+reviewUUID+"&batchno="+batchNo+"&isPrintFlag="+isPrintFlag+"&payId="+data[i].id+"&option=pay"+"\">打印</a>"+
        		               "</td></tr>";
                   		}                 
                   	}else{
                    	htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,6,this)\" href='javascript:void(0)'>查看</a>&nbsp;&nbsp;</td></tr>";
                   	}
        	   	}
           	}else{
        	   	htmlstr+="<tr><td colspan='8' style='text-align: center;'>经搜索暂无数据，请尝试改变搜索条件再次搜索</td></tr>"; 
           	}
           	$("#rpi_" + batchId + "_" + index).html(htmlstr);
        }
    });
	if ($(child).is(":hidden")) {
		$(obj).html("收起");
		$(obj).parents("tr").addClass("tr-hover");
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
	}else{
		if (!$(child).is(":hidden")) {
			$(obj).parents("tr").removeClass("tr-hover");
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("支付记录");
		}
	}
}

function getTravelerListOld(child, ctx, obj, batch_no, index){
	$.ajax({
		type:"POST",
		url:ctx+"/visa/workflow/borrowmoney/getTravelerList",
		data:{batchNo : batch_no, busynessType : '2'},
		success:function(data){
			var htmlVal = '';
			if(null != data && data != undefined){
				for(var i=0;i<data.length;i++){
				var hrefVal = ctx + "/visa/workflow/borrowmoney/visaBorrowMoneyReviewDetail?"
	                 + "orderId=" + data[i].orderId + "&travelerId=" + data[i].tid
	                 + "&flag=1&revid=" + data[i].reviewId + "&flowType=5";
			  htmlVal += "<tr><td class='tc'>" + (i+1) + "</td>"
				      + "<td class='tc'>" + data[i].tname + "</td>" 
				      + "<td class='tc'>" + data[i].orderCreateBy + "</td>"
				      + "<td class='tc'>" + data[i].groupCode + "</td>"
				      + "<td class='tc'>" + data[i].orderNo + "</td>"
				      + "<td class='tc'>" + data[i].visaType + "</td>"
				      + "<td class='tc'>" + data[i].visaCountry + "</td>"
				      + "<td class='tc'>¥"+data[i].borrowAmount+"</td>"
					  + "<td class='tc'>" + data[i].borrowRemark + "</td>"
					  + "<td class='tc'><a href='" + hrefVal + "' target='_blank'>查看</a></td>"
					  + "</tr>";
				}
			}else {
				htmlVal.append("<td colspan='15' style='text-align: center;'>查询无数据</td></tr>"); 
			}
			$("#traveler_" + batch_no + "_" + index).html(htmlVal);
		}
	});
	if ($(child).is(":hidden")) {
		$(obj).html("收起游客");
		$(obj).parents("tr").addClass("tr-hover");
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
	}else{
		if (!$(child).is(":hidden")) {
			$(obj).parents("tr").removeClass("tr-hover");
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("游客列表");
		}
	}
}
$(function(){
	//展开、收起筛选
	launch();
	//如果展开部分有查询条件的话，默认展开，否则收起
	closeOrExpand();
	//操作浮框
	operateHandler();
	//产品名称获得焦点显示隐藏
	inputTips();
	//计调模糊匹配
	$("[name=operatorId]").comboboxSingle();
	//销售模糊匹配
	$("[name=salerId]").comboboxSingle();

	var _$orderBy = $("#orderBy").val().trim() || "update_time DESC";
	$("#orderBy").val(_$orderBy);
    var orderBy = _$orderBy.split(" ");
    $(".activitylist_paixu_left li").each(function(){
        if($(this).hasClass("li"+orderBy[0])){
            orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
            $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
            $(this).attr("class","activitylist_paixu_moren");
         }
    });
    
    $("#contentTable").delegate("ul.caption > li","click",function(){
        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
        $(this).addClass("on").siblings().removeClass('on');
        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
    });
    
  	//首次打印提醒
	$(".uiPrint").hover(function(){
		$(this).find("span").show();
	},function(){
		$(this).find("span").hide();
	});
});
/**
 * 表单重置，不能改成 $('#searchForm')[0].reset() 
 * 输入条件点击查询之后，再点击条件重置按钮，此方法在Google Chrome 下失效 
 * @author shijun.liu
 */
function resetForm(){
	$('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']").each(function(){
		if($(this).val()){
			$(this).val('');
		}
	});
	$('#searchForm').find("select").each(function(){
		var select_val = $(this).children("option");
		$(select_val[0]).attr("selected","selected");
	});
}

//批量付款的处理
$(document).on('change','#contentTable,#contentTable_foot, [type="checkbox"]',function(){
    var $this = $(this);
    var $contentContainer = $("#contentTable,#contentTable_foot");
    var $all =$contentContainer.find('[check-action="All"]');
    var $reverse = $contentContainer.find('[check-action="Reverse"]');
    var $chks=$contentContainer.find('[check-action="Normal"]:enabled');
    if($this.is('[check-action="All"]')){
        if($this.is(':checked')){
            $chks.attr('checked',true);
        }else{
            $chks.removeAttr('checked');
        }
    }
    if($this.is('[check-action="Reverse"]')){
        $chks.each(function(){
            var $chk = $(this);
            if($chk.is(':checked')){
                $chk.removeAttr('checked');
            }else{
                $chk.attr('checked',true);
            }
        });
    }
    if($chks.length && ($chks.length ==$chks.filter(':checked').length)){
        $all.attr('checked',true);
    }else{
        $all.removeAttr('checked');
    }
});

/**
 * 借款付款导出游客列表到Excel文件
 * @author shijun.liu
 */
function exportToExcel(ctx){
	//表单所有参数
	var args = $('#searchForm').serialize();
	window.location.href=ctx + "/costNew/payManager/downloadTraveler?1=1&" + args;
}

function batchPrint(){
	var tmp = '';
	$("input[name='checkBox']").each(function(){
		if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
			tmp=tmp+$(this).attr('value')+",";
		}
	});
	if(tmp==""){
		$.jBox.tip('请选择数据','warnning');
		return;
	}
	$("#printInfo").val(tmp);
	$("#printForm").submit();
}
function batchConfirmOrCannePay(ctx,id,type,status){
	var tmp = '';
	var havePayed = false; // 用户是否有选择已确认付款的条目
	$("input[name='"+id+"']").each(function(){
		if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
			tmp=tmp+$(this).attr('value')+",";
			if ($(this).attr('payStatus') == '1'){ // 表示有选择确认付款的
				havePayed = true;
			}
		}
	});
	if(tmp==""){
		$.jBox.tip('请选择数据','warnning');
		return;
	}
	if (havePayed){
		$.jBox.tip('已有数据付款，请重新选择','warnning');
		return;
	}
	var msg="确认付款吗？";
	$.jBox.confirm(msg,"提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:ctx+"/orderCommon/manage/batchConfirmPay",
				data:{id:tmp,type:type,status:status},
				success:function(data){
					if('ok' == data.flag){
						$("#searchForm").submit();
					}else{

					}
				}
			})
		}
		if(v=='cancel'){

		}
	})
}
</script>

<style type="text/css">
	a{display: inline-block;}
	label{ cursor:inherit;}
</style>
</head>
<body>
<%--<page:applyDecorator name="finance_op_head" >--%>
    <%--<page:param name="showType">${payType }</page:param>--%>
	<%--<page:param name="current">visa_batch_borrowmoney</page:param>--%>
<%--</page:applyDecorator>--%>
<c:set var="showType" value="${payType }" />
<c:set var="current" value="visa_batch_borrowmoney" />
<%@ include file="/WEB-INF/views/head/financeHead.jsp"%>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/costNew/payManager/borrowMoneyForTTSQZ/203" method="post" >
   <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
   <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
   <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
   <div class="activitylist_bodyer_right_team_co">
       <div class="activitylist_bodyer_right_team_co2">
	       <input id="groupCode" name="groupCode" class="txtPro searchInput inputTxt" value="${groupCode}" placeholder="请输入团号"/>
       </div>
	   <div class="zksx">筛选</div>
       <div class="form_submit">
           <input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
           <input class="btn ydbz_x" type="button" onclick="resetForm();" value="清空所有条件" />
           <input class="btn ydbz_x" onclick="exportToExcel('${ctx}')" value="导出Excel" type="button">
       </div>
           <div class="ydxbd">
               <div class="activitylist_bodyer_right_team_co1">
       		       <div class="activitylist_team_co3_text">签证类型：</div>
				   <div class="selectStyle">
					   <select name="visaType">
							<option value="">全部</option>
							<c:forEach var="v" items="${visaTypeList }">
							   <option value="${v.key }" <c:if test="${visaType==v.key}">selected="selected"</c:if>>${v.value }</option>
							</c:forEach>
					   </select>
				   </div>
               </div> 
               <div class="activitylist_bodyer_right_team_co1">
       			   <div class="activitylist_team_co3_text">付款状态：</div>
				   <div class="selectStyle">
					   <select name="payStatus">
							<option value="">全部</option>
							<option value="0" <c:if test="${payStatus eq '0'}">selected="selected"</c:if>>未付</option>
							<option value="1" <c:if test="${payStatus eq '1'}">selected="selected"</c:if>>已付</option>
					   </select>
				   </div>
               </div>
               <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">报批日期：</div>
                    <input class="inputTxt dateinput" value="${reportDateStart}" name="reportDateStart" readonly="" onclick="WdatePicker()">
                    	至
                    <input class="inputTxt dateinput" value="${reportDateEnd}" name="reportDateEnd" readonly="" onclick="WdatePicker()">
               </div>
               <div class="activitylist_bodyer_right_team_co1">
       		       <div class="activitylist_team_co3_text">签证国家：</div>
				   <div class="selectStyle">
					   <select name="visaContry">
							<option value="">全部</option>
							<c:forEach var="visaCountry" items="${countryList }">
							   <option value="${visaCountry.key }" <c:if test="${visaContry==visaCountry.key}">selected="selected"</c:if>>${visaCountry.value }</option>
							</c:forEach>
					   </select>
				   </div>
               </div> 
               <div class="activitylist_bodyer_right_team_co1">
       			   <div class="activitylist_team_co3_text">销售：</div>
                   <select name="salerId">
                        <option value="">全部</option>
				    	<c:forEach var="saler" items="${salerList }">
	                       <option value="${saler.key }" <c:if test="${salerId==saler.key}">selected="selected"</c:if>>${saler.value }</option>
	                    </c:forEach>
                   </select>
               </div>
			   <div class="activitylist_bodyer_right_team_co1">
				   <div class="activitylist_team_co3_text">申请日期：</div>
				   <input class="inputTxt dateinput" value="${createTimeMin}" name="createTimeMin" readonly="" onclick="WdatePicker()">
				   至
				   <input class="inputTxt dateinput" value="${createTimeMax}" name="createTimeMax" readonly="" onclick="WdatePicker()">
			   </div>
               <div class="activitylist_bodyer_right_team_co1">
       			   <div class="activitylist_team_co3_text">打印状态：</div>
				   <div class="selectStyle">
					   <select name="printStatus">
							<option value="">全部</option>
							<option value="0" <c:if test="${printStatus eq '0'}">selected="selected"</c:if>>未打印</option>
							<option value="1" <c:if test="${printStatus eq '1'}">selected="selected"</c:if>>已打印</option>
					   </select>
				   </div>
               </div>
			   <div class="activitylist_bodyer_right_team_co1">
				   <div class="activitylist_team_co3_text">计调：</div>
				   <select name="operatorId">
					   <option value="">全部</option>
					   <c:forEach var="operator" items="${operatorList }">
						   <option value="${operator.key }" <c:if test="${operatorId==operator.key}">selected="selected"</c:if>>${operator.value }</option>
					   </c:forEach>
				   </select>
			   </div>
               <div class="activitylist_bodyer_right_team_co4">
               	   <div class="activitylist_team_co3_text">付款金额：</div>
				   <div class="selectStyle">
					   <select id="currency" name="currencyId" >
						   <option value="">全部</option>
						   <c:forEach items="${currencyList}" var="c">
								<option value="${c.id}" <c:if test="${currencyId==c.id}">selected="selected"</c:if>>${c.currencyName}</option>
						   </c:forEach>
					   </select>
				   </div>
				   <input type="text" value="${moneyBegin }" name="moneyBegin" id="startMoney" class="inputTxt"
							   onkeyup="validNum(this)" onafterpaste="validNum(this))" placeholder="输入金额"/>
				   <span>~</span>
				   <input type="text" value="${moneyEnd }" name="moneyEnd" id="endMoney" class="inputTxt"
							   onkeyup="validNum(this)" onafterpaste="validNum(this))" placeholder="输入金额"/>
               </div>
		</div>
   </div>
</form:form>

    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
            <ul>
            <li class="activitylist_paixu_left_biankuang licreate_time"><a onClick="sortby('create_time',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang liupdate_time"><a onClick="sortby('update_time',this)">更新时间</a></li>
            </ul>
      </div>
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
      </div>

<table id="contentTable" class="table mainTable activitylist_bodyer_table"  style="margin-left:0px;">
        <thead style="background:#403738;">
	        <tr>
	            <th colspan="2" width="6%">序号</th>
	            <th width="10%">批次号</th>
	            <th width="10%">申请人</th>
	            <th width="10%">申请日期</th>
	            <th width="10%">团队类型</th>
	            <th width="8%">借款金额</th>
	            <th width="8%">借款单价</th>
	            <th width="8%">借款人数</th>
	            <th width="10%">付款金额<br />已付金额</th>
	            <th width="8%">出纳确认</th>
	            <th width="8%">打印状态</th>
	            <th width="8%">操作</th>
	        </tr>
        </thead>
        <tbody>
        <c:if test="${fn:length(page.list) <= 0 }">
            <tr>
	            <td colspan="20" style="text-align: center;">暂无搜索结果</td>
            </tr>
        </c:if>
        <c:forEach items="${page.list }" var="pay" varStatus="s">
            <tr>
            	<td class="tc" ><input type="checkbox" name="checkBox" value="${pay.batch_no}_6_${pay.reviewflag}_${pay.reviewflag eq 1 ? pay.reviewId : pay.review_uuid}_${empty pay.isPrintFlag ? '0' : pay.isPrintFlag}" check-action="Normal"  payStatus="${pay.payStatus}" <%--<c:if test="${pay.payStatus eq '1' }">disabled="disabled"</c:if>/>--%> </td>
                <td class="tc">${s.count }</td>
                <td class="tc batchNoTD">${pay.batch_no }</td>
				<td class="tc">${fns:getUserNameById(pay.proposer) }</td>
				<td class="tc"><fmt:formatDate value="${pay.create_time}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="tc">${fns:getStringOrderStatus("6")}</td>
                <td class="tr">¥<fmt:formatNumber value="${pay.batch_total_money}" pattern="#,##0.00"/></td>
                <td class="tr">¥<fmt:formatNumber value="${pay.price}" pattern="#,##0.00"/></td>
                <td class="tc">${pay.batch_person_count }</td>
                <td class="tr">
                	<div class="yfje_dd">
                	<span class="fbold">¥<fmt:formatNumber value="${pay.batch_total_money}" pattern="#,##0.00"/></span> </div>
                	<div class="dzje_dd">
	                	<span class="fbold">
	                		<c:if test="${not empty fns:getTTSQZPayedMoney(pay.batch_id,'6')}">
		                		¥<fmt:formatNumber value="${fns:getTTSQZPayedMoney(pay.batch_id,'6')}" pattern="#,##0.00"/>
		                	</c:if>
	                	</span>
                	</div>
                </td>
                <td class="tc">
               		<c:if test="${pay.payStatus eq '0' }">
               			<font style="color:red">未付</font>
               		</c:if>
               		<c:if test="${pay.payStatus eq '1' }">
               			<font style="color:green">已付</font>
               		</c:if>
               		
               		<!--548&&549-->
               		<c:if test="${pay.hasRemarks and pay.isShowRemark eq 1 }">
                    	<div class="div_548">
                    		<div class="remark_548">备注
                        		<div class="remark_div">
                            		<em class="arrows_548"></em>
                                	<div class="remark_div_head">
                                		<div class="remark_head_left">审批备注</div>
                                    	<div class="remark_head_right">
                                    		<input type="checkbox"/>
                                    		<input type="hidden" value="${pay.review_uuid }" />
                                        	<div>不再显示</div>
                                    	</div>
                                	</div>
                                	<div class="remark_div_body">
                                		<div class="remark_div_child">
                                        	<c:forEach items="${pay.logs}" var="reviewLog" varStatus="status">
                                        		<ul>
                                        			<li class="left_548">
                                        				<div class="color_black">
                                        					<fmt:formatDate value="${reviewLog.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        				</div>
                                        				<div class="green_548">审批通过</div>
                                        			</li>
                                        			<li class="center_548"><em class="point-time step_yes"></em></li>
                                        			<li class="right_548">
                                        				<div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
                                        				（<span class="people_name_548">${fns:getUserById(reviewLog.createBy).name}</span>）
                                        				<div class="right_body"><div class="remarks">备注：</div>${reviewLog.remark}</div>
                                        			</li>
                                        		</ul>
                                        	</c:forEach>
                                        	<ul>
                                        		<li class="left_548"><div class="color_black"></div><div class="green_548">审批通过</div></li>
                                                <li class="center_548"><em class="point-time yes"></em></li>
                                                <li class="right_548"><div class="right_head"></div></li>
                                        	</ul>	
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
					</c:if>
                    <!--548&&549end--> 
                </td>
                <c:choose>
				     <c:when test="${pay.isPrintFlag == '1'}">
				     	 <td class="invoice_yes tc"><p class="uiPrint">已打印<span style="display: none;">首次打印日期<br><fmt:formatDate pattern="yyyy/MM/dd HH:mm" value="${pay.printTime}"/></span></p></td>
				     </c:when>
				     <c:otherwise>
				     	<td class="tc" style="color: green">未打印</td>
				     	<!-- <td class="invoice_back tc">未打印</td> -->
				     </c:otherwise>
				</c:choose>
                <td class="p0">
                    <dl class="handle">
                       	<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
	                   	<dd><p>
                           	<span></span>
                            <a href="${ctx}/costNew/payManager/borrowMoneyPayForTTSQZ?orderType=6&batchId=${pay.batch_id}&orderId=${pay.orderId}&payPrice=${pay.batch_total_money}" 
                            target="_blank">付款 </a>
                           	<a href="${ctx }/costReview/visa/visaCostReviewDetail/${pay.productId}/2?read=1" target="_blank">查看</a>
                           <a href="${ctx }/cost/manager/forcastList/${pay.productId}/6" target="_blank">预报单</a>
                            <a href="${ctx }/cost/manager/settleList/${pay.productId}/6" target="_blank">结算单</a>
                            <a onclick="expand('#child1_${pay.batch_id}_${s.count}', this, ${pay.batch_id}, '6', ${s.count}, ${pay.orderId}, ${pay.reviewId}, '${pay.review_uuid}', '${pay.batch_no}', '${pay.isPrintFlag}', ${pay.reviewflag})" href="javascript:void(0)">支付记录</a>
                            
                            
                            <!-- 旧审核   游客列表、游客明细 -->
                            <c:if test="${pay.reviewflag eq 1}">
                                <a href="javascript:void(0)" onClick="getTravelerListOld('#child2_${pay.batch_no}_${s.count}','${ctx}', this, '${pay.batch_no}', ${s.count});">游客列表</a>
                                <a href="${ctx}/visa/workflow/borrowmoney/exportTravelerInfo?batchNo=${pay.batch_no}&busynessType=2">游客明细</a>
                            </c:if>
                            <!-- 新审核   游客列表、游客明细 -->
                            <c:if test="${pay.reviewflag eq 2}">
								<a openFlag="false" href="javascript:void(0)" onClick="getTravelerList(this,'${ctx}','2');">游客列表</a><br/>
								<a href="${ctx}/visa/borrowMoney/review/exportTravelerInfo?batchNo=${pay.batch_no}&busynessType=2">游客明细</a><br/>
                            </c:if>
                            
                            
                            <c:choose>
                            	<c:when test="${pay.payStatus eq '0' }">
                            		<a href="javascript:void(0)" onclick="confirmOrCancelPayForTTS('${ctx}', ${pay.batch_id}, '1', '0')">确认付款</a>
                            	</c:when>
                            	<c:otherwise>
                            		<a href="javascript:void(0)" onclick="confirmOrCancelPayForTTS('${ctx}', ${pay.batch_id}, '0', '1')">取消付款</a>
                            	</c:otherwise>
                            </c:choose>
                            
                            
                            <shiro:hasPermission name="visabatchloan:operation:print">
                            	<!-- 旧审核 -->
                      	  		<c:if test="${pay.reviewflag eq 1}">
                               		<a target="_blank" href="${ctx}/visa/workflow/borrowmoney/visaBorrowMoneyBatchFeePrint?orderId=${pay.orderId}&revid=${pay.reviewId}&batchno=${pay.batch_no}&isPrintFlag=${pay.isPrintFlag}&option=order">打印</a>
                            	</c:if> 
                            	<!-- 新审核打印 -->
                       			<c:if test="${pay.reviewflag eq 2}">
                                 	<a target="_blank" href="${ctx}/visa/hqx/borrowmoney/visaBorrowMoney4HQXBatchFeePrint?orderId=${pay.orderId}&revid=${pay.review_uuid}&batchno=${pay.batch_no}&isPrintFlag=${pay.isPrintFlag}&option=order">打印</a>
                            	</c:if>
                            </shiro:hasPermission>
                           </p>
                        </dd>
                    </dl>
                </td>
            </tr>
            <tr id="child1_${pay.batch_id}_${s.count}" class="activity_team_top1" style="display:none"> 
				<td colspan="15" class="team_top">
					<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
						<thead>
							<tr>
								<th class="tc" width="5%">序号</th>
								<th class="tc" width="15%">付款方式</th>
								<th class="tc" width="10%">金额</th>
								<th class="tc" width="10%">日期</th>
								<th class="tc" width="10%">支付类型</th>
								<th class="tc" width="15%">支付凭证</th>
								<th class="tc" width="10%">状态</th>
								<th class="tc" width="10%">操作</th>
							</tr>
						</thead>
						<tbody id='rpi_${pay.batch_id}_${s.count}'>
						
						</tbody>
					</table>
				</td>
			</tr>
			<tr id="child2_${pay.batch_no}_${s.count}" class="activity_team_top1" style="display:none"> 
				<td colspan="15" class="team_top">
					<table id="teamTable2" class="table activitylist_bodyer_table" style="margin:0 auto;">
						<thead>
							<tr>
								<th class="tc" width="5%">序号</th>
								<th class="tc" width="8%">姓名</th>
								<th class="tc" width="8%">销售</th>
								<th class="tc" width="10%">团号</th>
								<th class="tc" width="10%">订单号</th>
								<th class="tc" width="8%">签证类型</th>
								<th class="tc" width="8%">签证国家</th>
								<th class="tc" width="8%">借款金额</th>
								<th class="tc" width="10%">借款原因</th>
								<th class="tc" width="5%">操作</th>
							</tr>
						</thead>
						<tbody id='traveler_${pay.batch_no}_${s.count}'>
						
						</tbody>
					</table>
				</td>
			</tr>
		</c:forEach>
        </tbody> 
    </table>
	<form method="post" action="${ctx}/visa/hqx/borrowmoney/visaBorrowMoneyBatchPrint" id="printForm" target="_blank">
		<input id="printInfo" name="printInfo" type="hidden" value=""/>
	</form>
	<div class="page" id="contentTable_foot">
		<div class="pagination">
			<dl>
				<dt><input check-action="All" type="checkbox">全选</dt>
				<dt><input check-action="Reverse" type="checkbox">反选</dt>
				<dd>
					<input type="button" class="btn ydbz_x" value="批量确认付款" onclick="batchConfirmOrCannePay('${ctx }','checkBox','13',1)" />
					<shiro:hasPermission name="visabatchloan:operation:print">
						<input type="button" class="btn ydbz_x" value="批量打印" onclick="batchPrint()" />
					</shiro:hasPermission>
				</dd>
			</dl>
		</div>

		<div class="pagination clearFix">
			${page}
		</div>
	</div>
</div>

</body>
</html>