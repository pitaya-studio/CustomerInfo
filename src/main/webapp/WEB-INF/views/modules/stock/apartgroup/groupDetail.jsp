<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>团期详情</title>
    <meta name="decorator" content="wholesaler"/>
    <style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/flexbox-0.9.6/FlexBox/css/jquery.flexbox.css" />
    <script src="${ctxStatic}/jquery-other/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactorStock.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/flexbox-0.9.6/FlexBox/js/jquery.flexbox.js"></script>
	<script type="text/javascript">
   function orderDetail(orderId){
	window.open("${ctx}/orderCommon/manage/orderDetail/"+orderId,"_blank");
	}
       
   function PrintTable(Id){
                 var mStr;
                 mStr = window.document.body.innerHTML ;
                 var mWindow = window;               
                 window.document.body.innerHTML =Id.innerHTML;
                 mWindow.print();
                 window.document.body.innerHTML = mStr;
         }
   
   function saveRemarks(groupId) {
	   var remarks = $("#remarks").val();
	   		if(remarks!=null && remarks!=""){
			   $.ajax({
			        type: "POST",
			        url: "${ctx}/stock/manager/apartGroup/groupRemarks",
			        data: {
			        	groupId:groupId,
			        	remarks:remarks
			        },
			        dataType: "text",
			         success: function(msg){
				        $(".remarksView").text(msg);
	        		}
			      });  
   		}
   		$("#remarks").hide();
   		$("#saveRemarks").hide();
   }
</script>
<style type="text/css">
body{ font-size:12px;}

</style>
</head>
<body>
            <page:applyDecorator name="show_head">
                <page:param name="desc">团期详情</page:param>
            </page:applyDecorator>
            <tags:message content="${message}" />
            <div class="ydbzbox fs">
<%--            <div class="mod_nav departure_title">库存详情</div>--%>
			<div id="dy">
            <div class="orderdetails_tit"><span>1</span>产品信息</div>
			<p class="ydbz_mc">团号：${groupCode }</p>
               <ul class="ydbz_info">
                   <li><span>产品编号：${travelActivity.activitySerNum }</span></li>
                   <li title="${travelActivity.targetAreaNames }"><span >目的地：<c:if test="${fn:length(travelActivity.targetAreaNames)<=15 }">${travelActivity.targetAreaNames }</c:if><c:if test="${fn:length(travelActivity.targetAreaNames)>15 }">${fn:substring(travelActivity.targetAreaNames , 0, 15) }...</c:if></span></li>
                   <li><span>可报人数：${group.freePosition}</span></li>
<%--                   <span>旅游类型：${travelActivity.travelTypeName }</span>--%>
                  <li> <span>出发城市：${travelActivity.fromAreaName }</span></li><br/>
                   <li><span>出团日期：<fmt:formatDate value="${group.groupOpenDate }" pattern="yyyy-MM-dd"/></span></li>
                   <li><span>产品系列：${travelActivity.travelTypeName }</span></li>
                   <li><span>行程天数：${travelActivity.activityDuration }</span></li>
               </ul>
			<div class="orderdetails_tit">
				<span>2</span>订单信息
			</div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed activitylist_bodyer_table"  style="margin-left:0px;">
        <thead style="background:#403738;">
        <tr>
            <th width="6%">预定渠道</th>
	        <th width="7%">订单号</th>
	        <th width="80">预订时间</th>
           <th width="10%">产品名称</th>
	        
<%--	         <th>产品名称</th>--%>
	        <!-- 
	           <th width="8%">产品编号</th>
	        <th width="5%">团号</th>
	        <th width="5%">出团日期</th>
	        <th width="5%">截团日期</th>
	         
	         -->
	        <th width="10%">出/截团日期</th>
	        <!-- 
	        <th width="4%">预订人</th>
	        <th width="5%">预订人电话</th>
	         -->
	        <th width="30">人数</th>
	        <th width="7%">订单状态</th>
	        <th width="10%">订单总额</th>
	        <th width="10%">已付金额</th>
	        <th width="10%">到账金额</th> 
	        <th width="30">操作</th>
	    </tr>
	    </thead>
        <tbody>
        	<c:forEach items="${orderList }" var="orders" varStatus="s">
        		<tr>
		            <td>${orders.orderCompanyName }</td>
		            <td>${orders.orderNum }</td>
		            <td>
		              <fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
		            </td>
		            
		            
		            <td>
<%--		                <div class="tuanhao_cen onshow">${orders.groupCode }</div>--%>
                        ${travelActivity.acitivityName}
		            </td>
		            
		            <td style="padding: 0px;"><div class="out-date"><fmt:formatDate value="${travelActivity.groupOpenDate}" pattern="yyyy-MM-dd"/></div>
		            
		            <div class="close-date"><fmt:formatDate value="${travelActivity.groupCloseDate}" pattern="yyyy-MM-dd"/></div></td>
		            
		             <td><!-- 
		             <a href="javascript:void(0)" onclick="" title="游客信息未填全"><span class="red">${orders.orderPersonNum }</span></a>
                                <a href="javascript:void(0)" onclick="" title="游客信息已填全"><span class="green-rs">${orders.orderPersonNum }</span></a>
		              -->
                                ${orders.orderPersonNum }
                                </td>
		            
		            <!-- 
		              <td>${orders.orderPersonPhoneNum }</td>
		             -->
		            <td>
		                  ${fns:getDictLabel(orders.payStatus, "order_pay_status", "")}
		            </td>
		            
		            <td class="tr tdrmb_bigred">
		            	<c:if test="${not empty orders.totalMoney }">¥</c:if><fmt:formatNumber value='${orders.totalMoney}' type="currency" pattern="#,##0.00" />
		            </td>
		            <td class="tr tdrmb_bigred">
		            	<c:if test="${not empty orders.payedMoney }">¥</c:if><fmt:formatNumber value='${orders.payedMoney}' type="currency" pattern="#,##0.00" />
		            </td>
		            <td class="tr tdrmb_bigred">
			            <c:if test="${not empty orders.accountedMoney }">¥</c:if><fmt:formatNumber value='${orders.accountedMoney}' type="currency" pattern="#,##0.00" />
		            </td>
             
		            <td>
                         <shiro:hasPermission name="looseOrder:operation:view">
                            <a href="javascript:void(0)" onClick="javascript:orderDetail(${orders.id});">详情 </a>
                        </shiro:hasPermission>
                                     
                    </td>
        		</tr>
        	</c:forEach>
      </tbody>              
           </table>
           <div class="orderdetails_tit"><span>3</span>备注</div>
           	<span class="remarksView">${group.remarks}</span>
           	</div>
           	<textarea rows="3" cols="5" id="remarks" style="display: none" maxlength="100" onChange="$('#saveRemarks').show();$('#cancelRemarks').hide()"></textarea>
           	<input id="saveRemarks" type="button" class="btn btn-primary" style="display: none" onClick="saveRemarks(${group.id})" value="保 存"/>
            <input id="cancelRemarks" type="button" class="btn btn-primary" style="display: none" onClick="$('#remarks').hide();$(this).hide()" value="取消修改"/>
            <div class="release_next_add">
            	<input type="button" class="btn btn-primary" onClick="$('#remarks').show();if($('#saveRemarks').css('display')=='none'){$('#cancelRemarks').show()}" value="修改备注"/>
            	<input type="button" class="btn btn-primary" onClick="return PrintTable(dy)" value="打 印"/>
                <input id="btnCancel" class="btn btn-primary" type="button" value="关 闭" onClick="javascript:window.close();" />
            </div>
  </div>
</body>
</html>