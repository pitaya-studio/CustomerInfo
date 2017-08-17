<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>IslandTraveler维护</title>
	<meta name="decorator" content="wholesaler"/>
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
	window.open("${ctx}/islandTraveler/show/" + uuid );
}
function edit(uuid){
	window.open( "${ctx}/islandTraveler/edit/" + uuid );
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
			$.post( "${ctx}/islandTraveler/delete", {"uuids":ids.join(",")}, 
				function(data){
					if(data.result=="1"){
						top.$.jBox.tip("删除成功!");
						$("#searchForm").submit();
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
	$("#searchForm").attr("action"," ${ctx}/islandTraveler/list");
	$("#searchForm").submit();
}
</script>	
</head>
<body>
	<div>
<!--右侧内容部分开始-->
   <div class="activitylist_bodyer_right_team_co_bgcolor">
   <form id="searchForm" modelAttribute="islandTravelerQuery" action=" ${ctx}/islandTraveler/list" method="post">
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
游客UUID：
</div>
<input value="${islandTravelerQuery.travelerUuid}" id="travelerUuid" name="travelerUuid"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
订单UUID：
</div>
<input value="${islandTravelerQuery.orderUuid}" id="orderUuid" name="orderUuid"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
订单id 关联productorder,visa_order,airticket_order等表的id，暂保留兼容以前的设计，以后以订单uuid为关联：
</div>
<input value="${islandTravelerQuery.orderId}" id="orderId" name="orderId"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
游客姓名：
</div>
<input value="${islandTravelerQuery.name}" id="name" name="name"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
游客名称拼音：
</div>
<input value="${islandTravelerQuery.nameSpell}" id="nameSpell" name="nameSpell"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
房屋类型 1-单人间 2-双人间：
</div>
<input value="${islandTravelerQuery.hotelDemand}" id="hotelDemand" name="hotelDemand"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
单房差：
</div>
<input value="${islandTravelerQuery.singleDiff}" id="singleDiff" name="singleDiff"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
单房差几晚：
</div>
<input value="${islandTravelerQuery.singleDiffNight}" id="singleDiffNight" name="singleDiffNight"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
papersType：
</div>
<input value="${islandTravelerQuery.papersType}" id="papersType" name="papersType"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
身份证号：
</div>
<input value="${islandTravelerQuery.idCard}" id="idCard" name="idCard"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
国籍：
</div>
<input value="${islandTravelerQuery.nationality}" id="nationality" name="nationality"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
性别 1-男 2-女：
</div>
<input value="${islandTravelerQuery.sex}" id="sex" name="sex"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
出生日期：
</div>
<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${islandTraveler.birthDay}"/>" id="birthDay" name="birthDay"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
issuePlace：
</div>
<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${islandTraveler.issuePlace}"/>" id="issuePlace" name="issuePlace"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
validityDate：
</div>
<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${islandTraveler.validityDate}"/>" id="validityDate" name="validityDate"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
手机号：
</div>
<input value="${islandTravelerQuery.telephone}" id="telephone" name="telephone"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
备注：
</div>
<input value="${islandTravelerQuery.remark}" id="remark" name="remark"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
单价（发布产品时的定价）：
</div>
<input value="${islandTravelerQuery.srcPrice}" id="srcPrice" name="srcPrice"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
人员类型（1-成人 2-儿童 3-特殊人群）：
</div>
<input value="${islandTravelerQuery.personType}" id="personType" name="personType"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
passportCode：
</div>
<input value="${islandTravelerQuery.passportCode}" id="passportCode" name="passportCode"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
护照有效期：
</div>
<input onclick="WdatePicker()" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${islandTraveler.passportValidity}"/>" id="passportValidity" name="passportValidity"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
护照种类 1：因公护照 2：因私护照：
</div>
<input value="${islandTravelerQuery.passportType}" id="passportType" name="passportType"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
是否需要联运 0：不需要，1：需要：
</div>
<input value="${islandTravelerQuery.intermodalType}" id="intermodalType" name="intermodalType"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
产品联运信息表主键 关联intermodal_strategy表：
</div>
<input value="${islandTravelerQuery.intermodalId}" id="intermodalId" name="intermodalId"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
护照状态 1:借出;2:归还客户;3:未签收;4:已签收：
</div>
<input value="${islandTravelerQuery.passportStatus}" id="passportStatus" name="passportStatus"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
单价币种：
</div>
<input value="${islandTravelerQuery.srcPriceCurrency}" id="srcPriceCurrency" name="srcPriceCurrency"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
单房差币种：
</div>
<input value="${islandTravelerQuery.singleDiffCurrency}" id="singleDiffCurrency" name="singleDiffCurrency"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
记录游客属于那种订单（预报名：0 单团类、散拼、签证、机票同产品类型）：
</div>
<input value="${islandTravelerQuery.orderType}" id="orderType" name="orderType"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
删除标记 0:正常 1：删除 2:退团审核中 3：已退团 4：转团审核中 5：已转团：
</div>
<input value="${islandTravelerQuery.delFlag}" id="delFlag" name="delFlag"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
游客结算价流水号：
</div>
<input value="${islandTravelerQuery.payPriceSerialNum}" id="payPriceSerialNum" name="payPriceSerialNum"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
是否有机票标志 0 标示无机票 或已退票 1标示有机票：
</div>
<input value="${islandTravelerQuery.isAirticketFlag}" id="isAirticketFlag" name="isAirticketFlag"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
游客原始应收价 一次生成 永不改变：
</div>
<input value="${islandTravelerQuery.originalPayPriceSerialNum}" id="originalPayPriceSerialNum" name="originalPayPriceSerialNum"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
游客已付款流水号：
</div>
<input value="${islandTravelerQuery.payedMoneySerialNum}" id="payedMoneySerialNum" name="payedMoneySerialNum"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
costPriceSerialNum：
</div>
<input value="${islandTravelerQuery.costPriceSerialNum}" id="costPriceSerialNum" name="costPriceSerialNum"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
rebatesMoneySerialNum：
</div>
<input value="${islandTravelerQuery.rebatesMoneySerialNum}" id="rebatesMoneySerialNum" name="rebatesMoneySerialNum"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
paymentType：
</div>
<input value="${islandTravelerQuery.paymentType}" id="paymentType" name="paymentType"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
id：
</div>
<input value="${islandTravelerQuery.mainOrderId}" id="mainOrderId" name="mainOrderId"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
id：
</div>
<input value="${islandTravelerQuery.mainOrderTravelerid}" id="mainOrderTravelerid" name="mainOrderTravelerid"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
UUID：
</div>
<input value="${islandTravelerQuery.accountedMoney}" id="accountedMoney" name="accountedMoney"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
批量借护照批次号：
</div>
<input value="${islandTravelerQuery.borrowPassportBatchNo}" id="borrowPassportBatchNo" name="borrowPassportBatchNo"/>								</div>
								<div class="activitylist_bodyer_right_team_co1">
<div class="activitylist_team_co3_text">
游客借款UUID：
</div>
<input value="${islandTravelerQuery.jkSerialNum}" id="jkSerialNum" name="jkSerialNum"/>								</div>
                        
                    <div class="kong"></div>            
                 </div> 
                 </div>
            </form>
            </div>
            <div class="filter_btn">
	<a class="btn btn-primary" href=" ${ctx}/islandTraveler/form" target="_blank">添加</a>
</div>
<table class="t-type t-type100">
                <thead>
                    <tr>
                    	<th width="">序号</th>
                    	<th width=""><input name="allChoose" type="checkbox" onclick="checkall(this)"/></th>
							<th width=""> 游客UUID</th>
							<th width=""> 订单UUID</th>
							<th width="">订单id 关联productorder,visa_order,airticket_order等表的id，暂保留兼容以前的设计，以后以订单uuid为关联</th>
							<th width="">游客姓名</th>
							<th width="">游客名称拼音</th>
							<th width="">房屋类型 1-单人间 2-双人间</th>
							<th width="">单房差</th>
							<th width="">单房差几晚</th>
							<th width="">papersType</th>
							<th width="">身份证号</th>
							<th width="">国籍</th>
							<th width="">性别 1-男 2-女</th>
							<th width="">出生日期</th>
							<th width="">issuePlace</th>
							<th width="">validityDate</th>
							<th width="">手机号</th>
							<th width="">备注</th>
							<th width="">单价（发布产品时的定价）</th>
							<th width="">人员类型（1-成人 2-儿童 3-特殊人群）</th>
							<th width="">passportCode</th>
							<th width="">护照有效期</th>
							<th width="">护照种类 1：因公护照 2：因私护照</th>
							<th width="">是否需要联运 0：不需要，1：需要</th>
							<th width="">产品联运信息表主键 关联intermodal_strategy表</th>
							<th width="">护照状态 1:借出;2:归还客户;3:未签收;4:已签收</th>
							<th width="">单价币种</th>
							<th width="">单房差币种</th>
							<th width="">记录游客属于那种订单（预报名：0 单团类、散拼、签证、机票同产品类型）</th>
							<th width="">删除标记   0:正常   1：删除    2:退团审核中  3：已退团   4：转团审核中   5：已转团</th>
							<th width="">游客结算价流水号</th>
							<th width="">是否有机票标志 0 标示无机票 或已退票 1标示有机票</th>
							<th width="">游客原始应收价 一次生成 永不改变</th>
							<th width="">游客已付款流水号</th>
							<th width="">costPriceSerialNum</th>
							<th width="">rebatesMoneySerialNum</th>
							<th width="">paymentType</th>
							<th width="">id</th>
							<th width="">id</th>
							<th width=""> UUID</th>
							<th width="">批量借护照批次号</th>
							<th width="">游客借款UUID</th>
                        <th width="">操作</th>
                    </tr>
                </thead>
                <tbody>
                	
		            <c:forEach var="entry" items="${page.list}" varStatus="v">
			          <tr>
			            <td>${v.index + 1 }</td>
			            <td align="center"><input name="ids" type="checkbox" value="${entry.uuid}" /></td>
							<td>${entry.travelerUuid}</td>
							<td>${entry.orderUuid}</td>
							<td>${entry.orderId}</td>
							<td>${entry.name}</td>
							<td>${entry.nameSpell}</td>
							<td>${entry.hotelDemand}</td>
							<td>${entry.singleDiff}</td>
							<td>${entry.singleDiffNight}</td>
							<td>${entry.papersType}</td>
							<td>${entry.idCard}</td>
							<td>${entry.nationality}</td>
							<td>${entry.sex}</td>
							<td>${entry.birthDayString}</td>
							<td>${entry.issuePlaceString}</td>
							<td>${entry.validityDateString}</td>
							<td>${entry.telephone}</td>
							<td>${entry.remark}</td>
							<td>${entry.srcPrice}</td>
							<td>${entry.personType}</td>
							<td>${entry.passportCode}</td>
							<td>${entry.passportValidityString}</td>
							<td>${entry.passportType}</td>
							<td>${entry.intermodalType}</td>
							<td>${entry.intermodalId}</td>
							<td>${entry.passportStatus}</td>
							<td>${entry.srcPriceCurrency}</td>
							<td>${entry.singleDiffCurrency}</td>
							<td>${entry.orderType}</td>
							<td>${entry.delFlag}</td>
							<td>${entry.payPriceSerialNum}</td>
							<td>${entry.isAirticketFlag}</td>
							<td>${entry.originalPayPriceSerialNum}</td>
							<td>${entry.payedMoneySerialNum}</td>
							<td>${entry.costPriceSerialNum}</td>
							<td>${entry.rebatesMoneySerialNum}</td>
							<td>${entry.paymentType}</td>
							<td>${entry.mainOrderId}</td>
							<td>${entry.mainOrderTravelerid}</td>
							<td>${entry.accountedMoney}</td>
							<td>${entry.borrowPassportBatchNo}</td>
							<td>${entry.jkSerialNum}</td>
			            
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
