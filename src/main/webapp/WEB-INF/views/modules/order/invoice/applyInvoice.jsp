<!-- 
author:chenry
describe:订单列表中  申请发票页面
createDate：2014-11-12
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>申请发票
</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 modified by Tlw--%>
<%--<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/huanqiu-style.css" />--%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	var groupCodeVal = $("#groupCodeEle").text();
	if(groupCodeVal.length > 20) {
		groupCodeVal = groupCodeVal.substring(0, 20) + "...";
	}
	$("#groupCodeEle").text(groupCodeVal);
	// 给开票客户，注册控件
	$("select[name=invoiceCustomer]").comboboxInquiry({
		removeIfInvalid:false
	});
	
});


//提交页面数据
function formSubmit(){
	var orderIds=[];
	var groupCodes=[];
	var orderInvoiceAmounts=[];
	var orderNums=[];
	var orderTypes=[];
	var flag=false;
	$(".invoicemain").each(function(index, element){
	     var b=this.style.display;
	     if(b!="none"){	     
	     	var invoiceAmount=$(this).find("input[name='orderInvoiceAmount']");
	     	invoiceAmount.each(function(index, element){
	     		var p = $(invoiceAmount[index]);
	     		var invoiceAmountValue =p.val();
	     		if(undefined==invoiceAmountValue||""==invoiceAmountValue){
	     			flag=true;
	     		}
	     		var orderId=p.parent().parent().parent().parent().parent().find("input[name='myOrderId']").val();
	     		var groupCode=p.parent().parent().parent().parent().parent().find("input[name='myGroupCode']").val();
	     		var orderNum=p.parent().parent().parent().parent().parent().find("input[name='myOrderNum']").val();
	     		var orderType=p.parent().parent().parent().parent().parent().find("input[name='myOrderType']").val();
	     		if(invoiceAmountValue!=undefined&&invoiceAmountValue!=""){     		
	     			orderInvoiceAmounts.push(invoiceAmountValue);
	     			orderIds.push(orderId);
	     			groupCodes.push(groupCode);
	     			orderNums.push(orderNum);
	     			orderTypes.push(orderType);
	     		}	     			     		
	     		
	     	});
	     }
     });
	if(flag){
		top.$.jBox.tip('开票金额不能为空！', 'error', { focusId: 'name' });
		return false;
	}
	var invoiceMode=$("select[name='invoiceMode']").val();
	var invoiceType=$("select[name='invoiceType']").val();
	var invoicePayMan=$("select[name='invoicePayMan']").val();
	var invoiceHead=$("input[name='invoiceHead']").val();
	// 0444
	var applyInvoiceWay=$("input[name='applyInvoiceWay']:checked").val();
	// 0444
	var invoiceCustomer=$("select[name='invoiceCustomer']").next().find("input").val();
	var invoiceSubject=$("select[name='invoiceSubject']").val();
	// 需求0414 增加来款单位 update by pengfei.shang
	var invoiceComingUnit=$("input[name='invoiceComingUnit']").val();
	// 0568增加出票单位 --鼎鸿假期invoiceComeFromCompany
	var invoiceComeFromCompany=$("select[name='invoiceComeFromCompany']").val();
	var remark=$("textarea[name='remark']").val();
	if(undefined==invoiceHead||""==invoiceHead){
		top.$.jBox.tip('发票抬头不能为空！', 'error', { focusId: 'name' });
		return false;
	}
	if(undefined==invoiceCustomer||""==invoiceCustomer){
		top.$.jBox.tip('开票客户不能为空！', 'error', { focusId: 'name' });
		return false;
	}
// 	if(!$("select[name='invoiceCustomer']").is("select")) {
// 		top.$.jBox.tip('发票抬头不能为空！', 'error', { focusId: 'name' });
// 		return false;
// 	}
	$("#submitForm").removeClass("btn-submit"); 
	$.ajax({
                type: "POST",
                url: "${ctx}/orderInvoice/manage/saveApplyInvoice",
                data: {
                orderInvoiceAmount:orderInvoiceAmounts.join(','),
                orderId:orderIds.join(','),
                groupCode:groupCodes.join(','),
                orderNum:orderNums.join(','),
                orderTypes:orderTypes.join(','),
                invoiceMode:invoiceMode,
                invoiceType:invoiceType,
                invoicePayMan:invoicePayMan,
                invoiceHead:invoiceHead,
                // 0444
                applyInvoiceWay:applyInvoiceWay,
                // 0444
                invoiceCustomer:invoiceCustomer,
                invoiceSubject:invoiceSubject,
                invoiceComingUnit:invoiceComingUnit,
                invoiceComeFromCompany:invoiceComeFromCompany,
                orderType:"${orderType}",
                remarks:remark
                },
                success: function(msg){
	                if("1"==msg){
	                	top.$.jBox.tip('保存成功！');
	                	setTimeout(function(){
	                		window.location.href ="${ctx}/orderInvoice/manage/getInvoiceListByOrderNum?orderNum=${orderNum}&orderType=${orderType}&orderId=${orderId}";
	                	}, 1000);
	                }else if("0" == msg){
	                	top.$.jBox.tip('已取消或已删除订单不能开发票！', 'error', { focusId: 'name' });
	                }else{
	                	top.$.jBox.tip('保存失败！', 'error', { focusId: 'name' });
	                }                
                }
            });
}
	//返回
	function formBack(){
		window.location.href ="${ctx}/orderInvoice/manage/getInvoiceListByOrderNum?orderNum=${orderNum}&orderType=${orderType}&orderId=${orderId}";
	}

</script>
<script src="${ctxStatic}/modules/order/applyInvoice.js" type="text/javascript"></script>
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
.clear_both{clear:both}

/*申请发票*/
.invoicemain{ display:none}
.invoiceAdd{ margin-bottom:20px;}
.invoicediv input[type="text"] { margin:0}
.invoicediv{ margin-bottom:30px;}
.exchangerate_mouse {display:none;line-height:12px; position:absolute; border:1px solid #ccc; background:#fff;padding:10px 10px 0;width:auto;box-shadow: 0 2px 2px #ccc;}
.vote-ul .li100{width:100%; overflow:visible;height:auto; vertical-align:top;white-space:normal; line-height:20px;}
.vote-ul .li100 label{line-height:20px;}
/*form包排序*/
.formpaixu{margin-bottom:0}
.formpaixu .supplierLine{margin-top:20px;}
.formpaixu .activitylist_bodyer_right_team_co_paixu{margin: 10px 0 0}
.mod_details2_d1_five{ width:5%;text-align: right; /*height:30px;*/ text-align:right;}
.mod_details2_d2_five{ width:15%;text-align: left;  /*height:30px;*/ padding-left:5px;}
.messageDiv{width:auto;}
.seach25{width:20%;}
.seach25 p{width:75px;}
.seach25 .seach_r {width: 62%;}
.seach100{width:100%}
/*bug 17058 修复换行样式*/
.ta_orderinfo tr td{white-space: nowrap;}
</style>

</head>

<body>
<div id="sea">
<input id="ctx" value="${ctx}" type="hidden">
 <!--右侧内容部分开始-->
 	<div class="mod_nav">订单 > <c:if test="${orderType==1 }">单团</c:if>
 	<c:if test="${orderType==2 }">散拼</c:if>
 	<c:if test="${orderType==3 }">游学</c:if>
 	<c:if test="${orderType==5 }">自由行</c:if>
 	<c:if test="${orderType==4 }">大客户</c:if>
 	<c:if test="${orderType==10 }">游轮</c:if>
 	<c:if test="${orderType==11 }">酒店</c:if>
 	<c:if test="${orderType==12 }">海岛游</c:if>
 	 > 申请发票</div>
	<div class="messageDiv">
	<!-- 0444需求 -->
		<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
	        <div class="seach25" >
	        	<p>申请方式：</p>
	        	<p class="seach_r">
	        		<input type="radio" name="applyInvoiceWay" value="0" checked="checked">正常申请
	        		<input type="radio" name="applyInvoiceWay" value="1">预开发票
	        	</p>
	        </div>
        </c:if>
        <!-- 0444需求 -->
		<div class="seach25">
              <p>开票方式：</p>
              <p class="seach_r">
              <select onchange="invoiceTypeChg()" class="invoiceTypeChg" name="invoiceMode">
              	<c:forEach items="${invoiceModes}" var="ins" varStatus="s">
              		<option value="${ins.value }">${ins.label }</option>
              	</c:forEach>
              </select>
              </p>
        </div>
		 <!-- 0453需求,开票类型替换成开票项目-->
        <c:choose>
            <c:when test="${fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021'}"> 
	            <div class="seach25">
		              <p>开票项目：</p>
		              <p class="seach_r"><select name="invoiceSubject">
		              <c:forEach items="${invoiceSubject_qhjq}" var="ins" varStatus="s">
		              	<option value="${ins.value }">${ins.label }</option>
		              </c:forEach>
		              </select></p>
		        </div>
         	</c:when>
	       	<c:otherwise>
				<div class="seach25">
		              <p>开票类型：</p>
		              <p class="seach_r"><select name="invoiceType">
		              <c:forEach items="${invoiceTypes}" var="ins" varStatus="s">
		              	<option value="${ins.value }">${ins.label }</option>
		              </c:forEach>
		              </select></p>
		        </div>
		    </c:otherwise>
        </c:choose>
        <!-- 0453需求,开票类型替换成开票项目-->
        <div class="seach25">
              <p>开票金额：</p>
              <p class="seach_r"><input type="text" name="totalInvoice" class="rmb totalinvoice" disabled="disabled"></p>
            </div>
        <div class="seach25">
              <p>开票付款人：</p>
              <p class="seach_r"><select name="invoicePayMan">
                <option value="客户">客户</option>
                <option value="本公司">本公司</option>
              </select></p>
        </div>
		<div class="seach25 clear_both">
        	<p>发票抬头：</p>
        	<p class="seach_r"><input type="text" name="invoiceHead" maxlength="50"></p>
        </div>
        
        <div class="seach25 ">
			<p>开票客户：</p>
			<%-- <p class="seach_r"><input type="text" name="invoiceCustomer"></p> --%>
			<p class="seach_r seach_s">
				<select name="invoiceCustomer">
					<c:forEach items="${invoiceCustomers}" var="cstm" varStatus="s">
						<option value="${cstm.id }">${cstm.agentName }</option>
					</c:forEach>
				</select>
			</p>
        </div>
        <!-- 0411需求,隐藏开票项目,限定为批发商越柬行踪     0453起航假期也需隐藏-->
        <c:if test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586' and fns:getUser().company.uuid ne '5c05dfc65cd24c239cd1528e03965021'}">
	        <div class="seach25">
				<p>开票项目：</p>
				<p class="seach_r">
					<select name="invoiceSubject" style="width: 107px;">
						<c:forEach items="${invoiceSubjects}" var="ins" varStatus="s">
							<option value="${ins.value }">${ins.label }</option>
						</c:forEach>
					</select>
				</p>
	        </div>
        </c:if>
        <!-- 0411需求,隐藏开票项目,限定为批发商越柬行踪 -->
        <!-- 需求0414 增加来款单位 update by pengfei.shang=====================================================以下============================================================== -->
        <div class="seach25">
        	<p>来款单位：</p>
        	<p class="seach_r"><input type="text" name="invoiceComingUnit" maxlength="50"></p>
        </div>
         <!-- 568需求：增加出票单位字段 -->
         <c:if test="${companyUUID == '049984365af44db592d1cd529f3008c3'}">
         <div class="seach25 clear_both">
        	<p>出票单位：</p>
        	<p class="seach_r seach_s">
				<select style="width:220px;" name="invoiceComeFromCompany">
						<option value="3">北京鼎鸿假日国际旅行社有限公司</option>
						<option value="2">北京鼎鸿假期旅行社有限公司</option>
				</select>
			</p>
        </div>
        </c:if>
        
	</div> 
        <!--按单--><input type="hidden" name="salerId" id ="salerId" value="${invoiceOrderInfo.salerId }">
          <div class="invoicemain">
            <div class="invoicediv">
              <div class="ydbz_tit orderdetails_titpr">订单</div>
              <table class = "ta_orderinfo" style="margin-bottom:10px" border="0" width="100%">
                <tbody>
                  <tr>
                    <td class="mod_details2_d1_five">团号：</td>
                    <td class="mod_details2_d2_five" id="groupCodeEle" title="${invoiceOrderInfo.groupCode}">${invoiceOrderInfo.groupCode}</td>
                    <td class="mod_details2_d1_five">出团日期：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.groupOpenDate}</td>
                    <td class="mod_details2_d1_five">订单号：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.orderNum}</td>
                    <td class="mod_details2_d1_five">预定日期：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.orderTime}</td>
                    <td class="mod_details2_d1_five">订单类型：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.orderTypeName}</td>
                  </tr>
                  <tr>
                    <td class="mod_details2_d1_five">人数：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.orderPersonNum}</td>
                    <td class="mod_details2_d1_five">应收金额：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.orderyTotal}</td>
                    <td class="mod_details2_d1_five">财务到账：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.totalAsAcount}</td>
                    <td class="mod_details2_d1_five">已开发票：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.invoiceAmount}</td>
                    
                  </tr>
                </tbody>
              </table>              
              <input type="hidden" name="myGroupCode" value="${invoiceOrderInfo.groupCode}" >
              <input type="hidden" name="myOrderId" value="${invoiceOrderInfo.id}" >
              <input type="hidden" name="myOrderNum" value="${invoiceOrderInfo.orderNum}" >
              <input type="hidden" name="myOrderType" value="${invoiceOrderInfo.orderType}" >
              <table class="table table-striped table-bordered">
                <thead>
                  <tr>
                    <th width="10%">收款金额</th>
                    <th width="10%">达账金额</th>
                    <th width="10%">退款金额</th>
                    <th width="10%">已开票金额</th>
                    <th width="10%">可开票金额</th>
                    <th width="10%">本次开票金额</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="tr">${invoiceOrderInfo.alreadyPaid}</td>
                    <td class="tr" >${invoiceOrderInfo.totalAsAcount}</td>
                    <td class="tr">${invoiceOrderInfo.refundTotalStr}</td>
                    <td class="tr">${invoiceOrderInfo.invoiceAmount}</td>
                    <td class="tr">${invoiceOrderInfo.refundableAmount}</td>
                    <td class="tc"><input type="text" name="orderInvoiceAmount" class="rmb invoicetd" maxlength="10" onblur="refundInputs(this)" onafterpaste="invoiceInputin(this)" onkeyup="invoiceInputin(this)" ></td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <!--按团-->
          <div class="invoicemain">
          <c:forEach var="invoiceOrders" items="${invoiceOrderInfos}" varStatus="status">
          <div class="invoicediv">
              <div class="ydbz_tit orderdetails_titpr">订单</div>
              <table class = "ta_orderinfo" border="0"  width="100%" style="margin-bottom:10px">
                <tbody>
                  <tr>
                    <td class="mod_details2_d1_five">团号：</td>
                    <td class="mod_details2_d2_five">${invoiceOrders.groupCode}</td>
                    <td class="mod_details2_d1_five">出团日期：</td>
                    <td class="mod_details2_d2_five">${invoiceOrders.groupOpenDate}</td>
                    <td class="mod_details2_d1_five">订单号：</td>
                    <td class="mod_details2_d2_five">${invoiceOrders.orderNum}</td>
                    <td class="mod_details2_d1_five">预定日期：</td>
                    <td class="mod_details2_d2_five">${invoiceOrders.orderTime}</td>
                    <td class="mod_details2_d1_five">订单类型：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.orderTypeName}</td>
                  </tr>
                  <tr>
                    <td class="mod_details2_d1_five">人数：</td>
                    <td class="mod_details2_d2_five">${invoiceOrders.orderPersonNum}</td>
                    <td class="mod_details2_d1_five">应收金额：</td>
                    <td class="mod_details2_d2_five">${invoiceOrders.orderyTotal}</td>
                    <td class="mod_details2_d1_five">财务到账：</td>
                    <td class="mod_details2_d2_five">${invoiceOrders.totalAsAcount}</td>
                    <td class="mod_details2_d1_five">已开发票：</td>
                    <td class="mod_details2_d2_five">${invoiceOrders.invoiceAmount}</td>
                  </tr>
                </tbody>
              </table>              
              <input type="hidden" name="myGroupCode" value="${invoiceOrders.groupCode}" >
              <input type="hidden" name="myOrderId" value="${invoiceOrders.id}" >
              <input type="hidden" name="myOrderNum" value="${invoiceOrders.orderNum}" >
              <input type="hidden" name="myOrderType" value="${invoiceOrders.orderType}" >
              <table class="table table-striped table-bordered">
                <thead>
                  <tr>
                    <th width="10%">收款金额</th>
                    <th width="10%">达账金额</th>
                    <th width="10%">退款金额</th>
                    <th width="10%">已开票金额</th>
                    <th width="10%">可开票金额</th>
                    <th width="10%">本次开票金额</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="tr">${invoiceOrders.alreadyPaid}</td>
                    <td class="tr">${invoiceOrders.totalAsAcount}</td>
                    <td class="tr">${invoiceOrders.refundTotalStr}</td>
                    <td class="tr">${invoiceOrders.invoiceAmount}</td>
                    <td class="tr">${invoiceOrders.refundableAmount}</td>
                    <td class="tc"><input type="text" name="orderInvoiceAmount" class="rmb invoicetd"  maxlength="10"  onblur="refundInputs(this)" onafterpaste="invoiceInputin(this)" onkeyup="invoiceInputin(this)"></td>
                  </tr>
                </tbody>
              </table>
            </div>
          
          </c:forEach>
            
            
          </div>
          <!--合开订单-->
          <div class="invoicemain">
            <div class="invoicediv">
              <div class="ydbz_tit orderdetails_titpr">订单</div>
              <table class = "ta_orderinfo" border="0" style="margin-bottom:10px">
                <tbody>
                  <tr>
                    <td class="mod_details2_d1_five">团号：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.groupCode}</td>
                    <td class="mod_details2_d1_five">出团日期：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.groupOpenDate}</td>
                    <td class="mod_details2_d1_five">订单号：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.orderNum}</td>
                    <td class="mod_details2_d1_five">预定日期：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.orderTime}</td>
                    <td class="mod_details2_d1_five">订单类型：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.orderTypeName}</td>
                  </tr>
                  <tr>
                    <td class="mod_details2_d1_five">人数：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.orderPersonNum}</td>
                    <td class="mod_details2_d1_five">应收金额：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.orderyTotal}</td>
                    <td class="mod_details2_d1_five">财务到账：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.totalAsAcount}</td>
                    <td class="mod_details2_d1_five">已开发票：</td>
                    <td class="mod_details2_d2_five">${invoiceOrderInfo.invoiceAmount}</td>
                  </tr>
                </tbody>
              </table>              
              <input type="hidden" name="myGroupCode" value="${invoiceOrderInfo.groupCode}" >
              <input type="hidden" name="myOrderId" value="${invoiceOrderInfo.id}" >
              <input type="hidden" name="myOrderNum" value="${invoiceOrderInfo.orderNum}" >
              <input type="hidden" name="myOrderType" value="${invoiceOrderInfo.orderType}" >
              <table class="table table-striped table-bordered">
                <thead>
                  <tr>
                    <th width="10%">收款金额</th>
                    <th width="10%">达账金额</th>
                    <th width="10%">退款金额</th>
                    <th width="10%">已开票金额</th>
                    <th width="10%">可开票金额</th>
                    <th width="10%">本次开票金额</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="tr">${invoiceOrderInfo.alreadyPaid}</td>
                    <td class="tr" >${invoiceOrderInfo.totalAsAcount}</td>
                    <td class="tr">${invoiceOrderInfo.refundTotalStr}</td>
                    <td class="tr">${invoiceOrderInfo.invoiceAmount}</td>
                    <td class="tr">${invoiceOrderInfo.refundableAmount}</td>
                    <td class="tc"><input type="text" name="orderInvoiceAmount" class="rmb invoicetd"  maxlength="10"  onblur="refundInputs(this)" onafterpaste="invoiceInputin(this)" onkeyup="invoiceInputin(this)"></td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="seach100 invoiceAdd"><a class="ydbz_x">添加合开订单</a></div>
          </div>
          <div class="seach25 seach100">
            <p>备注：</p>
            <p class="seach_r">
              <textarea name ="remark" maxlength="150"></textarea>
            </p>
          </div>
          <div class="seach_btnbox"> <a class="btn btn-primary gray btn-back">返回</a><a class="btn btn-primary  btn-submit" id="submitForm" >申请</a> </div>
 <!--          <div class="exchangerate_mouse"><p>美元汇率：6.3</p><p>日元汇率：20.6</p></div>--> 
          
          
      </div>
</body>
</html>
