<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="decorator" content="wholesaler" />
    <title>服务费统计-交易明细</title>
	<script type="text/javascript" src="${ctxStatic }/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic }/js/common.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/agentToOffice/t2/tradeDetail.js" type="text/javascript"></script>
	<script type="text/javascript">
	var officeID = $("#officeId").val();
	//全选&反选操作
	function checkall(obj){
	    if($(obj).attr("checked")){
	        $('#contentTable_quauq input[type="checkbox"]').attr("checked",'true');
	        $("input[name='allChk']").attr("checked",'true');
	    }else{
	        $('#contentTable_quauq input[type="checkbox"]').removeAttr("checked");
	        $("input[name='allChk']").removeAttr("checked");
	    }
	}
	function checkreverse(obj){
	    var $contentTable = $('#contentTable_quauq');
	    $contentTable.find('input[type="checkbox"]').each(function(){
	        var $checkbox = $(this);
	        $("input[name='allChk']").removeAttr("checked");
	        if($checkbox.is(':checked')){
	            $checkbox.removeAttr('checked');
	        }else{
	            $checkbox.attr('checked',true);
	        }
			 $checkbox.trigger('change');
	    });
	}
	$(function(){
		//added for UG_V2 点击筛选按钮 at 20170223 by tlw
		launch();
	})
	</script>
</head>
<body>
<%--added for UG_V2 添加tab at 20170223 by tlw start.--%>
<content tag="three_level_menu">
	<li class="active">
		<a href="javascript:void(0)">交易服务费统计</a>
	</li>
</content>
<%--added for UG_V2 添加tab at 20170223 by tlw end.--%>
               <!--右侧内容部分开始-->
	<div class="activitylist_bodyer_right_team_co_bgcolor">
    	<form id="searchForm" method="post" action="${ctx}/quauqAgent/manage/tradeDetail?officeId=${officeId}" modelAttribute="agentinfo">
	    	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
			<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
			<input id="officeId" name="officeId" type="hidden" value="${officeId}" />
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2 pr">
					<input  class="txtPro inputTxt searchInput" id="groupCode" name="groupCode" value="${groupCode}" type="text" placeholder="输入团号">
				</div>
			    <div class="zksx zksx-on">筛选</div>
			    <div class="form_submit">
					<input class="btn btn-primary ydbz_x" id="seachbutton" type="button" value="搜索" onclick="search(1)">
					<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"><%--bug17503 type=reset换成type=button，添加点击事件resetSearchParams--%>
					<input class="btn ydbz_x" value="导出Excel" type="button" onclick="search(2)">
			    </div>
			    <div style="display: block;" class="ydxbd">
				   <span></span>
				   <c:if test="${useruuid == '2060' && empty officeId}">
					   <div class="activitylist_bodyer_right_team_co3" id="officeDiv" >
							<label class="activitylist_team_co3_text" >供应商名称：</label>
							<select name="officeIds" id="officeIds" >
								<option value="" selected="">全部</option>
								<c:forEach var="office" items="${officeList }">
									<option value="${office.id}" <c:if test="${officeIds==office.id}">selected="selected"</c:if>>${office.name}</option>
								</c:forEach>
							</select>
						</div>
				   </c:if>
				   <div class="activitylist_bodyer_right_team_co3"  >
					<label class="activitylist_team_co3_text" >渠道名称：</label>
					<select name="agentId" id="agentId" >
						<option value="" selected="">全部</option>
						<c:forEach var="agentinfo" items="${agentList }">
							<option value="${agentinfo.id}" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName}</option>
						</c:forEach>
					</select>
				</div>
				   <div class="activitylist_bodyer_right_team_co1" >
					   <div class="activitylist_team_co3_text" >出团日期：</div>
					   <input readonly="readonly" onclick="WdatePicker()" onfocus="WdatePicker({vel:'groupOpenDateTo'})" value="${groupTimeBegin}" name="groupTimeBegin" class="inputTxt dateinput">
					   <span>至</span>
					   <input id="groupOpenDateTo" readonly="readonly" onclick="WdatePicker()" value="${groupTimeEnd}" name="groupTimeEnd" class="inputTxt dateinput">
				   </div>
				   <div class="activitylist_bodyer_right_team_co1" >
						   <div class="activitylist_team_co3_text" >下单日期：</div>
						   <input readonly="readonly" onclick="WdatePicker()" onfocus="WdatePicker({vel:'orderDateTo'})" value="${orderTimeBegin}" name="orderTimeBegin" class="inputTxt dateinput">
						   <span>至</span>
						   <input id="orderDateTo" readonly="readonly" onclick="WdatePicker()" value="${orderTimeEnd}" name="orderTimeEnd" class="inputTxt dateinput">
				   </div>
					 <c:if test="${empty officeId}">
					 <%--<div class="kong"></div>--%>
					 </c:if>
				   <div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text">订单号：</label>
					<input id="orderNum" name="orderNum" value="${orderNum}" type="text" style="width:100px;"/>
				</div>
				   <div class="activitylist_bodyer_right_team_co1" >
						   <label class="activitylist_team_co3_text" >订单状态：</label>
						   <div class="selectStyle">
							   <select name="orderStatus" id="orderStatus">
								   <option value="" <c:if test="${empty orderStatus}">selected="selected"</c:if>>全部订单</option>
								   <option value="1" <c:if test="${orderStatus==1 }">selected="selected"</c:if>>未收全款</option>
								   <option value="2" <c:if test="${orderStatus==2 }">selected="selected"</c:if>>未收订金</option>
								   <option value="5" <c:if test="${orderStatus==5 }">selected="selected"</c:if>>已收全款</option>
								   <option value="4" <c:if test="${orderStatus==4 }">selected="selected"</c:if>>已收订金</option>
								   <option value="3" <c:if test="${orderStatus==3 }">selected="selected"</c:if>>已占位</option>
								   <option value="7" <c:if test="${orderStatus==7 }">selected="selected"</c:if>>待计调确认</option>
								   <option value="8" <c:if test="${orderStatus==8 }">selected="selected"</c:if>>待财务确认</option>
								   <option value="9" <c:if test="${orderStatus==9 }">selected="selected"</c:if>>已撤销占位</option>
								   <option value="99" <c:if test="${orderStatus==99 }">selected="selected"</c:if>>已取消</option>
								   <option value="111" <c:if test="${orderStatus==111 }">selected="selected"</c:if>>已删除</option>
							   </select>
						   </div>
				   </div>

				<%--<div class="activitylist_bodyer_right_team_co1" >--%>
					<%--<div class="activitylist_team_co3_text" >团号：</div>--%>
					<%--<input id="groupCode" name="groupCode" value="${groupCode}" type="text"/>--%>
				<%--</div>--%>

				<div class="activitylist_bodyer_right_team_co1" >
					<div class="activitylist_team_co3_text" >缴费状态：</div>
					<div class="selectStyle">
						<select name="isPayedCharge" id="">
							<option value=""  <c:if test="${empty isPayedCharge}">selected="selected"</c:if>>全部</option>
							<option value="1" <c:if test="${isPayedCharge=='1' }">selected="selected"</c:if>>已缴费</option>
							<option value="0" <c:if test="${isPayedCharge=='0' }">selected="selected"</c:if>>未缴费</option>
						</select>
					</div>
				</div>
			  </div>
			</div>
		</form>


               <div class="cwxt-qbdd">
                   <span class="summation">合计：</span>
                   订单总人数：
                   <span>${sumList.personCount }</span>
                   订单总额：
                   <span>${sumList.orderTotalMoney }</span>
		服务费总额：
                   <span>${sumList.allCharge }
                   <c:if test="${empty sumList.allCharge}"><c:if test="${not empty sumList.allCharge}">¥0.00</c:if></c:if></span>
	<c:if test="${useruuid == '2060'}">
      QUAUQ服务费：
                   <span>${sumList.quauqTotalCharge }
                   <c:if test="${empty sumList.quauqTotalCharge}"><c:if test="${not empty sumList.quauqTotalCharge}">¥0.00</c:if></c:if></span>
                   渠道服务费：
                   <span>${sumList.agentTotalCharge }
                   <c:if test="${empty sumList.agentTotalCharge}"><c:if test="${not empty sumList.agentTotalCharge}">¥0.00</c:if></c:if></span>
                   抽成服务费：
                   <span>${sumList.cutTotalCharge }
                   <c:if test="${empty sumList.cutTotalCharge}"><c:if test="${not empty sumList.cutTotalCharge}">¥0.00</c:if></c:if></span>
	</c:if>
                   已缴费：
                   <span id="payedCharge">${sumList.payedCharge }
                   <c:if test="${empty sumList.payedCharge}"><c:if test="${not empty sumList.orderTotalMoney}">¥0.00</c:if></c:if></span>
                   未缴费：
                   <span id="unpayedCharge">${sumList.unpayedCharge }
                   <c:if test="${empty sumList.unpayedCharge}"><c:if test="${not empty sumList.orderTotalMoney}">¥0.00</c:if></c:if></span>
               </div>
               
               <table class="activitylist_bodyer_table mainTable" id="contentTable_quauq">
                   <thead>
                   <tr>
                       <th width="2%">
                           <!--<input type="checkbox"/>-->
                       </th>
                       <th width="3%">序号</th>
                      <c:if test="${useruuid == '2060' && empty officeId}">
                       	<th width="6%">供应商名称</th>
                       </c:if>
                       <th width="6%">渠道名称</th>
                       <th width="6%">下单日期</th>
                       <th width="5%">出团日期</th>
                       <th width="5%">团号</th>
                       <th width="6%">订单号</th>
                       <th width="3%">订单人数</th>
                       <th width="6%">销售</th>
                       <th width="7%">订单总额</th>
                       <th width="7%">服务费总额</th>
                       <c:if test="${useruuid == '2060'}">
							<th width="6%">QUAUQ服务费</th>
                      		<th width="6%">渠道服务费</th>
                       		<th width="6%">抽成服务费</th>
                       </c:if>
                       <th width="5%">订单状态</th>
                       <th width="4%">缴费状态</th>
                       <th width="6%">操作</th>
                   </tr>
                   </thead>
                   <tbody class="orderOrGroup_group_tbody">
				   <c:if test="${fn:length(page.list) <= 0 }">
				 		<tr class="toptr" >
                       <c:choose>
                       		<c:when test="${listFlag eq 'quauq'}">
								<td colspan="18" style="text-align: center;">暂无搜索结果</td>
                       		</c:when>
                       		<c:otherwise>
								<td colspan="17" style="text-align: center;">暂无搜索结果</td>
                       		</c:otherwise>
                       </c:choose>
						</tr>
				   </c:if>
	       
					<c:forEach items="${page.list}" var="order" varStatus="s">
	                   <tr>
	                       <td class="tc">
	                           <input type="checkbox" class="delivery" value="${order.orderId }"/>
	                       </td>
	                       <td class="tc">${s.index + 1}</td>
	                       <c:if test="${useruuid == '2060' && empty officeId}">
	                       	<td class="tc">${order.officeName}</td>
	                       </c:if>
	                       <td class="tc">${order.agentName}</td>
	                       <td class="tc"><fmt:formatDate value="${order.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                      	   <td class="tc"><fmt:formatDate value="${order.groupOpenDate}"/></td>
	                       <td class="tc">${order.groupCode}</td>
	                       <td class="tc">${order.orderNum}</td>
	                       <td class="tc">${order.orderPersonNum}</td>
	                       <td class="tc">${order.salename}</td>
	                      <%--  <td class="tc"><fmt:formatNumber value="${order.totalMoney}" pattern="¥#0.00"/></td>
	                       <td class="tc"><fmt:formatNumber value="${order.chargeMoney }" pattern="¥#0.00"/></td> --%>
	                       <td class="tc">${order.totalMoney}</td>
	                       <td class="tc"><c:if test="${not empty order.chargeMoney}">${order.chargeMoney}</c:if>
	                       				<c:if test="${empty order.chargeMoney}">¥ 0.00</c:if></td>
	                       <c:if test="${useruuid == '2060'}">
		                       <td class="tc"><c:if test="${not empty order.quauqChargeMoney}">${order.quauqChargeMoney}</c:if>
		                       				<c:if test="${empty order.quauqChargeMoney}">¥ 0.00</c:if></td>
		                       <td class="tc"><c:if test="${not empty order.agentChargeMoney}">${order.agentChargeMoney}</c:if>
		                       				<c:if test="${empty order.agentChargeMoney}">¥ 0.00</c:if></td>
		                       <td class="tc"><c:if test="${not empty order.cutChargeMoney}">${order.cutChargeMoney}</c:if>
	                       					<c:if test="${empty order.cutChargeMoney}">¥ 0.00</c:if></td>
	                       </c:if>
	                       <td class="tc">
								<c:choose>
									<c:when test="${order.delFlag eq '3'}">待生成订单</c:when>
									<c:when test="${order.delFlag eq '4'}">未生成订单</c:when>
									<c:otherwise>${fns:getDictLabel(order.orderStatus, "order_pay_status", "")}</c:otherwise>
								</c:choose>
						   </td>
	                       <td class="tc" id="${order.orderId }">
	                       	   <c:choose>
	                       	     <c:when test="${order.isPayedCharge eq '1'}"><span class="already"><a style="color: #008800;">已缴费</a></span><span class="notPay hide" style="color: #FF0000">未缴费</span></c:when>
	                       	     <c:when test="${order.isPayedCharge eq '0'}"><span class="already hide"><a style="color: #008800;">已缴费</a></span><span class="notPay" style="color: #FF0000">未缴费</span></c:when>
	                           </c:choose>
	                       </td>
	                       <td class="tc">
	                           <a href="javascript:void(0)" onClick="javascript:orderDetail(${order.orderId});">订单详情</a>
	                           &nbsp;&nbsp;<a href="javascript:void(0);" onclick="openPayPriceLog(this);">操作记录</a>
	                       </td>
	                       <input type="hidden" name="orderId" value="${order.orderId }"/>
	                   </tr>
	                 </c:forEach>
                </tbody>
               </table>
               <div class="pagination">
		            <dl>
		              	<dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
		              	<dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</dt>
               			<dd>
						   <shiro:hasPermission name="transaction:payTheFees">
							   <input class="btn ydbz_x" value="设为已缴费"  type="button" onclick="already()">
							   <input class="btn ydbz_x" value="设为未缴费"  type="button" onclick="notPay()">
						   </shiro:hasPermission>
			   			</dd>
					</dl>
               </div>

               <div class="pagination clearFix">
					${page}
					<div style="clear:both;"></div>
			   </div>
               <!--右侧内容部分结束-->

	</div>
	<div id="payprice_log" class="display-none">
		<div class="select_account_pop" style="padding:20px">
			<div style="height:358px;overflow-y:auto;">
				<table class="activitylist_bodyer_table">
					<thead>
						<tr>
							<th>序号</th>
							<th>操作人</th>
							<th>操作功能</th>
							<th>操作时间</th>
							<th>操作内容</th>
						</tr>
					</thead>
					<tbody id="paypricelogTable">
						<!-- 由js解析json生成 -->
					</tbody>
				</table>
			</div>
		</div>
	</div>

</body>
</html>