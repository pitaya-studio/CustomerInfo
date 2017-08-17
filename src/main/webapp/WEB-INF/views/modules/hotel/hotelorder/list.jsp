<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>HotelOrder维护</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
<script type="text/javascript">
$(function(){
	//搜索条件筛选
	launch();
	//产品名称文本框提示信息
	inputTips();
	//操作浮框
	operateHandler();
	
	//取消一个checkbox 就要联动取消 全选的选中状态
	$("input[name='ids']").click(function(){
		if($(this).attr("checked")){
			
		}else{
			$("input[name='allChoose']").removeAttr("checked");
		}
	});
});


function show(uuid){
	window.open("${ctx}/hotelOrder/show/" + uuid );
}
function edit(uuid){
	window.open( "${ctx}/hotelOrder/edit/" + uuid );
}
function del(uuid){
	var ids = [];
	ids.push(uuid);
	v_deleteItems(ids);
}
function checkall(obj){
	if($(obj).attr("checked")){
		$("input[name='ids']").attr("checked",'true');
		$("input[name='allChoose']").attr("checked",'true');
	}else{
		$("input[name='ids']").removeAttr("checked");
		$("input[name='allChoose']").removeAttr("checked");
	}
}

function alldel(){
	if($("[name=ids]:checkbox:checked").length>0){
		var ids = [];
		$("[name=ids]:checkbox:checked").each(function(){ids.push($(this).val())});
		v_deleteItems(ids);
	}else{
		top.$.jBox.tip('请选择后进行删除操作','warning');
	}
}

function v_deleteItems(ids){
	
	$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.jBox.tip("正在删除数据...", 'loading');
			$.post( "${ctx}/hotelOrder/delete", {"uuids":ids.join(",")}, 
				function(data){
					if(data.result=="1"){
						$.jBox.prompt("删除成功!", 'success', 'info', { closed: function () { $("#searchForm").submit(); } });
					}else{
						top.$.jBox.tip('删除失败','warning');
					}
				}
			);
			
		} else if (v == 'cancel') {
                
		}
	});
	
}
function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").attr("action"," ${ctx}/hotelOrder/list");
	$("#searchForm").submit();
}
</script>	
</head>
<body>
	<div>
<!--右侧内容部分开始-->
   <div class="activitylist_bodyer_right_team_co_bgcolor">
   <form id="searchForm" modelAttribute="hotelOrderQuery" action=" ${ctx}/hotelOrder/list" method="post">
   		<input id="pageNo" name="pageNo" type="hidden" value="<c:out value="${pageNo}" />"/>
		<input id="pageSize" name="pageSize" type="hidden" value="<c:out value="${pageSize}" />"/>
		<input id="orderBy" name="orderBy" type="hidden" value="<c:out value="${orderBy}" />"/>
        <div class="activitylist_bodyer_right_team_co">
        
           <div class="activitylist_bodyer_right_team_co2 pr wpr20">
               <label>**名称：</label><input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey" name="wholeSalerKey" value="" type="text" flag="istips" />
                        <span class="ipt-tips">仅支持**名称的搜索</span>
                    </div>
                    <div class="form_submit">
                            <input class="btn btn-primary" value="搜索" type="submit">
                    </div>
                    <a class="zksx">展开筛选</a>
                    <div class="ydxbd">
                        
                        
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
订单uuid：
</div>
<input value="${hotelOrderQuery.uuid}" id="uuid" name="uuid"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
酒店产品uuid：
</div>
<input value="${hotelOrderQuery.activityHotelUuid}" id="activityHotelUuid" name="activityHotelUuid"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
酒店产品团期uuid：
</div>
<input value="${hotelOrderQuery.activityHotelGroupUuid}" id="activityHotelGroupUuid" name="activityHotelGroupUuid"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
订单单号：
</div>
<input value="${hotelOrderQuery.orderNum}" id="orderNum" name="orderNum"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
订单状态：0 全部订单；1 待确认报名；2 已确认报名；3 已取消；：
</div>
<input value="${hotelOrderQuery.orderStatus}" id="orderStatus" name="orderStatus"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
预订单位-即渠道：
</div>
<input value="${hotelOrderQuery.orderCompany}" id="orderCompany" name="orderCompany"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
预订单位名称：
</div>
<input value="${hotelOrderQuery.orderCompanyName}" id="orderCompanyName" name="orderCompanyName"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
跟进销售员id：
</div>
<input value="${hotelOrderQuery.orderSalerId}" id="orderSalerId" name="orderSalerId"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
预订人名称：
</div>
<input value="${hotelOrderQuery.orderPersonName}" id="orderPersonName" name="orderPersonName"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
预订人联系电话：
</div>
<input value="${hotelOrderQuery.orderPersonPhoneNum}" id="orderPersonPhoneNum" name="orderPersonPhoneNum"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
预订日期：
</div>
<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrder.orderTime}"/>" id="orderTime" name="orderTime"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
预定人数：
</div>
<input value="${hotelOrderQuery.orderPersonNum}" id="orderPersonNum" name="orderPersonNum"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
订金金额UUID：
</div>
<input value="${hotelOrderQuery.frontMoney}" id="frontMoney" name="frontMoney"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消：
</div>
<input value="${hotelOrderQuery.payStatus}" id="payStatus" name="payStatus"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
已付金额UUID：
</div>
<input value="${hotelOrderQuery.payedMoney}" id="payedMoney" name="payedMoney"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
支付方式1-支票 2-POS机付款 3-现金支付 4-汇款 5-快速支付：
</div>
<input value="${hotelOrderQuery.payType}" id="payType" name="payType"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
创建者：
</div>
<input value="${hotelOrderQuery.createBy}" id="createBy" name="createBy"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
创建日期：
</div>
<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrder.createDate}"/>" id="createDate" name="createDate"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
更新者：
</div>
<input value="${hotelOrderQuery.updateBy}" id="updateBy" name="updateBy"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
更新日期：
</div>
<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrder.updateDate}"/>" id="updateDate" name="updateDate"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
删除标记：
</div>
<input value="${hotelOrderQuery.delFlag}" id="delFlag" name="delFlag"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
当前退换记录Id：
</div>
<input value="${hotelOrderQuery.changeGroupId}" id="changeGroupId" name="changeGroupId"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
退换类型：
</div>
<input value="${hotelOrderQuery.groupChangeType}" id="groupChangeType" name="groupChangeType"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
订单成本金额：
</div>
<input value="${hotelOrderQuery.costMoney}" id="costMoney" name="costMoney"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
达账状态：
</div>
<input value="${hotelOrderQuery.asAcountType}" id="asAcountType" name="asAcountType"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
达账金额UUID：
</div>
<input value="${hotelOrderQuery.accountedMoney}" id="accountedMoney" name="accountedMoney"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
下订单时产品的预收定金：
</div>
<input value="${hotelOrderQuery.payDeposit}" id="payDeposit" name="payDeposit"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
占位类型 如果为0 或者为空 表示是占位 如果为1 表示是切位：
</div>
<input value="${hotelOrderQuery.placeHolderType}" id="placeHolderType" name="placeHolderType"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
下订单时的单房差：
</div>
<input value="${hotelOrderQuery.singleDiff}" id="singleDiff" name="singleDiff"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
取消原因：
</div>
<input value="${hotelOrderQuery.cancelDescription}" id="cancelDescription" name="cancelDescription"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
0 未付款 1 已付首款 2 已付尾款（全款）3 首款已达账 4 尾款（全款）已达账 5 开发票申请 6 已开发票：
</div>
<input value="${hotelOrderQuery.isPayment}" id="isPayment" name="isPayment"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
付款方式：
</div>
<input value="${hotelOrderQuery.payMode}" id="payMode" name="payMode"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
保留天数：
</div>
<input value="${hotelOrderQuery.remainDays}" id="remainDays" name="remainDays"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
激活时间：
</div>
<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${hotelOrder.activationDate}"/>" id="activationDate" name="activationDate"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
订单锁定状态：0:正常 1：锁定(订单锁定状态不允许操作订单)：
</div>
<input value="${hotelOrderQuery.lockStatus}" id="lockStatus" name="lockStatus"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
特殊需求：
</div>
<input value="${hotelOrderQuery.specialDemand}" id="specialDemand" name="specialDemand"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
订单总价UUID：
</div>
<input value="${hotelOrderQuery.totalMoney}" id="totalMoney" name="totalMoney"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
文件ids：
</div>
<input value="${hotelOrderQuery.fileIds}" id="fileIds" name="fileIds"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
原始应收价 一次生成永不改变：
</div>
<input value="${hotelOrderQuery.originalTotalMoney}" id="originalTotalMoney" name="originalTotalMoney"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
是否是补单产品，0：否，1：是：
</div>
<input value="${hotelOrderQuery.isAfterSupplement}" id="isAfterSupplement" name="isAfterSupplement"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
原始订金金额（乘人数后金额）：
</div>
<input value="${hotelOrderQuery.originalFrontMoney}" id="originalFrontMoney" name="originalFrontMoney"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4：
</div>
<input value="${hotelOrderQuery.paymentType}" id="paymentType" name="paymentType"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
预报名间数：
</div>
<input value="${hotelOrderQuery.forecaseReportNum}" id="forecaseReportNum" name="forecaseReportNum"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
酒店扣减控房间数：
</div>
<input value="${hotelOrderQuery.subControlNum}" id="subControlNum" name="subControlNum"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
酒店扣减非控房间数：
</div>
<input value="${hotelOrderQuery.subUnControlNum}" id="subUnControlNum" name="subUnControlNum"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
备注：
</div>
<input value="${hotelOrderQuery.remark}" id="remark" name="remark"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
报名状态（1、预报名；2、报名）：
</div>
<input value="${hotelOrderQuery.bookingStatus}" id="bookingStatus" name="bookingStatus"/>								</div>
                        
                    <div class="kong"></div>            
                 </div> 
                 </div>
            </form>
            </div>
            <div class="filter_btn">
	<a class="btn btn-primary" href=" ${ctx}/hotelOrder/form" target="_blank">添加</a>
</div>
<table class="t-type t-type100">
                <thead>
                    <tr>
                    	<th width="">序号</th>
                    	<th width=""><input name="allChoose" type="checkbox" onclick="checkall(this)"/></th>
							<th width="">订单uuid</th>
							<th width="">酒店产品uuid</th>
							<th width="">酒店产品团期uuid</th>
							<th width="">订单单号</th>
							<th width="">订单状态：0 全部订单；1 待确认报名；2 已确认报名；3 已取消；</th>
							<th width="">预订单位-即渠道</th>
							<th width="">预订单位名称</th>
							<th width="">跟进销售员id</th>
							<th width="">预订人名称</th>
							<th width="">预订人联系电话</th>
							<th width="">预订日期</th>
							<th width="">预定人数</th>
							<th width="">订金金额UUID</th>
							<th width="">支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消</th>
							<th width="">已付金额UUID</th>
							<th width="">支付方式1-支票 2-POS机付款 3-现金支付 4-汇款 5-快速支付</th>
							<th width="">创建者</th>
							<th width="">创建日期</th>
							<th width="">更新者</th>
							<th width="">更新日期</th>
							<th width="">删除标记</th>
							<th width="">当前退换记录Id</th>
							<th width="">退换类型</th>
							<th width="">订单成本金额</th>
							<th width="">达账状态</th>
							<th width="">达账金额UUID</th>
							<th width="">下订单时产品的预收定金</th>
							<th width="">占位类型    如果为0  或者为空  表示是占位  如果为1  表示是切位</th>
							<th width="">下订单时的单房差</th>
							<th width="">取消原因</th>
							<th width="">0 未付款 1 已付首款 2 已付尾款（全款）3 首款已达账 4 尾款（全款）已达账 5 开发票申请 6 已开发票</th>
							<th width="">付款方式</th>
							<th width="">保留天数</th>
							<th width="">激活时间</th>
							<th width="">订单锁定状态：0:正常  1：锁定(订单锁定状态不允许操作订单)</th>
							<th width="">特殊需求</th>
							<th width="">订单总价UUID</th>
							<th width="">文件ids</th>
							<th width="">原始应收价 一次生成永不改变</th>
							<th width="">是否是补单产品，0：否，1：是</th>
							<th width="">原始订金金额（乘人数后金额）</th>
							<th width="">结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4</th>
							<th width="">预报名间数</th>
							<th width="">酒店扣减控房间数</th>
							<th width="">酒店扣减非控房间数</th>
							<th width="">备注</th>
							<th width="">报名状态（1、预报名；2、报名）</th>
                        <th width="">操作</th>
                    </tr>
                </thead>
                <tbody>
                	
		            <c:forEach var="entry" items="${page.list}" varStatus="v">
			          <tr>
			            <td>${v.index + 1 }</td>
			            <td align="center"><input name="ids" type="checkbox" value="${entry.uuid}" /></td>
							<td>${entry.uuid}</td>
							<td>${entry.activityHotelUuid}</td>
							<td>${entry.activityHotelGroupUuid}</td>
							<td>${entry.orderNum}</td>
							<td>${entry.orderStatus}</td>
							<td>${entry.orderCompany}</td>
							<td>${entry.orderCompanyName}</td>
							<td>${entry.orderSalerId}</td>
							<td>${entry.orderPersonName}</td>
							<td>${entry.orderPersonPhoneNum}</td>
							<td>${entry.orderTimeString}</td>
							<td>${entry.orderPersonNum}</td>
							<td>${entry.frontMoney}</td>
							<td>${entry.payStatus}</td>
							<td>${entry.payedMoney}</td>
							<td>${entry.payType}</td>
							<td>${entry.createBy}</td>
							<td>${entry.createDateString}</td>
							<td>${entry.updateBy}</td>
							<td>${entry.updateDateString}</td>
							<td>${entry.delFlag}</td>
							<td>${entry.changeGroupId}</td>
							<td>${entry.groupChangeType}</td>
							<td>${entry.costMoney}</td>
							<td>${entry.asAcountType}</td>
							<td>${entry.accountedMoney}</td>
							<td>${entry.payDeposit}</td>
							<td>${entry.placeHolderType}</td>
							<td>${entry.singleDiff}</td>
							<td>${entry.cancelDescription}</td>
							<td>${entry.isPayment}</td>
							<td>${entry.payMode}</td>
							<td>${entry.remainDays}</td>
							<td>${entry.activationDateString}</td>
							<td>${entry.lockStatus}</td>
							<td>${entry.specialDemand}</td>
							<td>${entry.totalMoney}</td>
							<td>${entry.fileIds}</td>
							<td>${entry.originalTotalMoney}</td>
							<td>${entry.isAfterSupplement}</td>
							<td>${entry.originalFrontMoney}</td>
							<td>${entry.paymentType}</td>
							<td>${entry.forecaseReportNum}</td>
							<td>${entry.subControlNum}</td>
							<td>${entry.subUnControlNum}</td>
							<td>${entry.remark}</td>
							<td>${entry.bookingStatus}</td>
			            
			            <td class="p0">
	                    	<dl class="handle">
	                    		<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
	                    		<dd class="">
	               				<p>
	               					<span></span>
									<a href="javascript:void(0)" onClick="show('${entry.uuid}')">详情</a>
									<br/>
									<a href="javascript:void(0)" onClick="edit('${entry.uuid}')">修改</a>
									<br/>
									<a href="javascript:void(0)" onClick="del('${entry.uuid}')">删除</a>
								</p>
								</dd>
							</dl>
						</td>
			            
			          </tr>
		           </c:forEach>
		           
		       </tbody>
   </table>
	<div class="page"></div>
	<div class="pagination">
   		<dl>
			<dt><input name="allChoose" type="checkbox" onclick="checkall(this)"/>全选</dt>
			<dd><a onClick="alldel()">批量删除</a></dd>
		</dl>
		<div class="endPage">${page}</div>
	</div>
	<br/>

            <!--右侧内容部分结束-->
</div>
</body>
</html>
<script type="text/javascript">


</script>
