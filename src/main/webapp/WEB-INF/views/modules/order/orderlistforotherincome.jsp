<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>其他收入收款</title>
<meta name="decorator" content="wholesaler"/>  
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/payedConfirm.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
//如果展开部分有查询条件的话，默认展开，否则收起	
function closeOrExpand(){
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='groupCode']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	for(var i=0;i<searchFormInput.length;i++) {
		var inputValue = $(searchFormInput[i]).val();
		if(inputValue != "" && inputValue != null) {
			inputRequest = true;
			break;
		}
	}
	for(var i=0;i<searchFormselect.length;i++) {
		var selectValue = $(searchFormselect[i]).children("option:selected").val();
		if(selectValue != "" && selectValue != null && selectValue != 0) {
			selectRequest = true;
			break;
		}
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}
}

$(function(){
	//展开、收起筛选
	launch();
	//如果展开部分有查询条件的话，默认展开，否则收起
	closeOrExpand();
	//操作浮框
	operateHandler();
	//输入金额提示
	inputTips();
	//计调模糊匹配
	$("[name=jd]").comboboxSingle();
	//切位渠道改为可输入的select
    $("#modifyAgentInfo").comboboxInquiry();
	//切位渠道改为可输入的select
	$("[name=toBankNname]").comboboxSingle();

	$("#contentTable").delegate("ul.caption > li","click",function(){
		var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
		$(this).addClass("on").siblings().removeClass('on');
		$(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
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
        
	var _$orderBy = $("#orderBy").val();
	if(_$orderBy==""){
		_$orderBy="updateDate DESC";
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function(){
		if ($(this).hasClass("li"+orderBy[0])){
			orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
			$(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
			$(this).attr("class","activitylist_paixu_moren");
		}
	});

	//首次打印提醒
	$(".uiPrint").hover(function(){
		$(this).find("span").show();
	},function(){
		$(this).find("span").hide();
	})
	$(document).scrollLeft(0);
});

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
function query() {
	$('#searchForm').submit();
}

/**
 * 查询条件重置
 * 
 */
var resetSearchParams = function(){
   var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
	var selectArray = $('#searchForm').find("select");
	for(var i=0;i<inputArray.length;i++){
		if($(inputArray[i]).val()){
			$(inputArray[i]).val('');
		}
	}
	for(var i=0;i<selectArray.length;i++){
		var selectOption = $(selectArray[i]).children("option");
		$(selectOption[0]).attr("selected","selected");
	}
}
function exportToExcel(ctx){
	//表单所有参数
	var args = $('#searchForm').serialize();
	window.open(ctx + "/orderCommon/manage/exporexcel?1=1&" + args);
}

//批量打印的选择
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

function batchPrint(){
	var $checked = $("input:checkbox[name='checkBox']:checked");
	if(!$checked[0]){
		$.jBox.tip('请选择数据','warnning');
		return false;
	}
	var dataArr = [];
	$checked.each(function(){
		var data = {};
		data.payId = $(this).val();
		data.prdType = $(this).next().val();
		dataArr.push(data);
	});
	$("#printInfo").val(JSON.stringify(dataArr));
	$("#printForm").submit();

//	var msg="确认批量打印其他收入收款吗？";
//	$.jBox.confirm(msg,"提示",function(v,h,f){
//		if(v=='ok'){
//
//		}
//		if(v=='cancel'){
//
//		}
//	});
}
</script>

<style type="text/css">
a{display: inline-block;}
	label{ cursor:inherit;}
	.disableCss{
pointer-events:none;
color:#afafaf;
cursor:default
} 
a{
    display: inline-block;
}
label{ cursor:inherit;}
.activitylist_team_co3_text { width: 80px; }
.ipt-tips {
    position: absolute;
    left: 0px;
    white-space: nowrap;
    color: #B2B2B2;
    z-index: 1;
}
</style>
</head>
<body>
<%--<page:applyDecorator name="finance_op_head" >--%>
    <%--<page:param name="showType">${showType}</page:param>--%>
<%--</page:applyDecorator>--%>
<%@ include file="/WEB-INF/views/head/financeHead.jsp"%>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%" id="zhmdiv">
 <form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/orderCommon/manage/showOrderListForOther" method="post" >
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
        <input id="showType" name="showType" type="hidden" value="${showType}" />
     	<div class="activitylist_bodyer_right_team_co">
            <div class="activitylist_bodyer_right_team_co2">
	            <input id="groupCode"  name="groupCode" class="inputTxt searchInput inputTxtlong" value="${groupCode }" placeholder="请输入团号"/>
            </div>
			<div class="zksx">筛选</div>
            <div class="form_submit">
                 <input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
                 <input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件" />
				<input class="btn ydbz_x" onclick="exportToExcel('${ctx}')" value="导出Excel" type="button">
            </div>

            <div class="ydxbd">
				<span></span>
				<div class="activitylist_bodyer_right_team_co1" >
                   	<label class="activitylist_team_co3_text">团队类型：</label>
					 <div class="selectStyle">
						<select name="orderS" id="orderS">
							<c:forEach var="order" items="${orderTypes }">
							<option value="${order.value }" <c:if test="${orderType==order.value}">selected="selected"</c:if>>${order.label }</option>
							</c:forEach>
						</select>
					 </div>
				 </div>
				<div class="activitylist_bodyer_right_team_co1">
					 <label class="activitylist_team_co3_text">地接社：</label>
					 <div class="selectStyle">
						 <select name="supplierInfo">
							<option value="">全部</option>
							<c:forEach var="s" items="${supplierList }">
								<c:if test="${s.supplierName ne '' }"> <option value="${s.id}" <c:if test="${supplierInfo==s.id}">selected="selected"</c:if>>${s.supplierName}</option></c:if>
							</c:forEach>
						 </select>
					 </div>
				 </div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co4_text">银行到账日期：</label><input id="accountDateBegin" name="accountDateBegin" class="inputTxt dateinput" value="${accountDateBegin }"
																					readonly onClick="WdatePicker()"/> 至
					<input id="accountDateEnd" name="accountDateEnd" value="${accountDateEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
				</div>
				<div class="activitylist_bodyer_right_team_co1" id="jd">
					<label class="activitylist_team_co3_text">计调：</label>
					<select name="jd">
						<option value="">全部</option>
						<c:forEach var="jds" items="${agentJd }">
							<option value="${jds.key }" <c:if test="${jd==jds.key}">selected="selected"</c:if>>${jds.value }</option>
						</c:forEach>
					</select>
				</div>
		     	<div class="activitylist_bodyer_right_team_co1">
				  <label class="activitylist_team_co3_text">渠道选择：</label>
				  <!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
				  <select id="modifyAgentInfo" name="agentId">
					  <option value="">全部</option>
					  <c:choose>
						  <c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }"><option value="-1" <c:if test="${agentId==-1}">selected="selected"</c:if>>未签</option></c:when>
						  <c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' }"><option value="-1" <c:if test="${agentId==-1}">selected="selected"</c:if>>直客</option></c:when>
						  <c:otherwise><option value="-1" <c:if test="${agentId==-1}">selected="selected"</c:if>>非签约渠道</option></c:otherwise>
					  </c:choose>
					  <c:forEach var="agentinfo" items="${agentinfoList }">
					  <c:choose>
						  <c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agentinfo.id==-1}">
						  		<option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>直客</option>
						  </c:when>
						  <c:otherwise>
						  		<option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
						  	</c:otherwise>
					  </c:choose>
					  </c:forEach>
				  </select>
           		</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">收款日期：</label><!-- {dateFmt:'yyyy-MM-dd HH:mm:ss'} -->
					<input type="text" class="inputTxt dateinput" value="${startCreateDate }" id="startCreateDate" name="startCreateDate" onClick="WdatePicker()"  onchange="isnull()"/>至
					<input type="text" class="inputTxt dateinput" value="${endCreateDate }" id="endCreateDate" name="endCreateDate" onClick="WdatePicker()"  onchange="isnull()"/>
				</div>
				<div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text">是否到账：</label>
					<div class="selectStyle">
						<select name="isAsAccount">
							<option value="" <c:if test="${isAsAccount eq '' }">selected="selected"</c:if>>全部</option>
							<option value="Y" <c:if test="${isAsAccount eq 'Y'}">selected="selected"</c:if>>已到账</option>
							<option value="N" <c:if test="${isAsAccount eq 'N'}">selected="selected"</c:if>>未到账</option>
							<option value="C" <c:if test="${isAsAccount eq 'C'}">selected="selected"</c:if>>已驳回</option>
						</select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">打印状态：</label>
					<div class="selectStyle">
						<select name="printFlag">
							<option value="">请选择</option>
							<option value="0" <c:if test="${printFlag eq '0' }">selected="selected"</c:if>>未打印</option>
							<option value="1" <c:if test="${printFlag eq '1' }">selected="selected"</c:if>>已打印</option>
						</select>
					</div>
				</div>
	        	<div class="activitylist_bodyer_right_team_co4">
					<label class="activitylist_team_co3_text">收款金额：</label>
					<div class="selectStyle">
						<select id="currency" name="currency">
							<option value="">全部</option>
							<c:forEach items="${currencyList}" var="c">
								<option value="${c.id}" <c:if test="${currency==c.id}">selected="selected"</c:if>>${c.currencyName}</option>
							</c:forEach>
						</select>
					</div>
                   <input type="text" value="${startMoney }" name="startMoney" id="startMoney" class="inputTxt" onkeyup="validNum(this)" onafterpaste="validNum(this))" placeholder="输入金额"/>
					<span style="font-size:12px;font-family:'宋体';">~</span>
             	   <input type="text" value="${endMoney }" name="endMoney" id="endMoney" class="inputTxt"onkeyup="validNum(this)" onafterpaste="validNum(this))" placeholder="输入金额"/>
	           </div>
				<!--支付方式查询条件，sys_dict字典表有支付方式的数据，但是各系统平台维护的有点乱，所以在此直接写死了  -->
				<div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text">收款方式：</label>
					<div class="selectStyle">
						<select name="payType">
							<option value="">全部</option>
							<option value="1" <c:if test="${payType eq '1'}">selected="selected"</c:if>>支票</option>
							<option value="3" <c:if test="${payType eq '3' or payType eq '5'}">selected="selected"</c:if>>现金</option>
							<option value="4" <c:if test="${payType eq '4'}">selected="selected"</c:if>>汇款</option>
							<option value="6" <c:if test="${payType eq '6'}">selected="selected"</c:if>>转账</option>
							<option value="7" <c:if test="${payType eq '7'}">selected="selected"</c:if>>汇票</option>
							<option value="8" <c:if test="${payType eq '8'}">selected="selected"</c:if>>POS</option>
							<c:if test="${isLMT }">
								<option value="9" <c:if test="${payType eq '9'}">selected="selected"</c:if>>因公支付宝</option>
							</c:if>
						</select>
					</div>
				</div>
            	<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">来款单位：</label>
					<input id="payerName" name="payerName" class="inputTxt inputTxtlong" value="${payerName }"/>
            	</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">收款确认日期：</label>
						<input id="receive_confirm_date_begin" name="receiveConfirmDateBegin"
							   class="inputTxt dateinput" value="${receiveConfirmDateBegin}" readonly onClick="WdatePicker()"/> 至
						<input id="receive_confirm_date_end" name="receiveConfirmDateEnd" value="${receiveConfirmDateEnd}"
							   readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
				</div>
             	<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">收款银行：</label>
					<select name="toBankNname">
						<option value="">全部</option>
						<c:forEach items="${banks}" var="bank">
							<option value="${bank.text}" <c:if test="${bank.text==toBankNname}">selected="selected"</c:if>>${bank.text}</option>
						</c:forEach>
					</select>
            	</div>
            	<%-- <div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
					<select name="paymentType">
						<option value="">不限</option>
						<c:forEach var="pType" items="${fns:findAllPaymentType()}">
							<option value="${pType[0] }"
								<c:if test="${paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
						</c:forEach>
					</select>
				</div> --%>
				<c:if test = "${showKb eq 1}">
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">是否为kb款：</label>
						<div class="selectStyle">
							<select name="isKb">
							   <option value="" <c:if test="${isKb eq '' }">selected="selected"</c:if>>全部</option>
							   <option value="1" <c:if test="${isKb eq '1'}">selected="selected"</c:if>>是</option>
							   <option value="-1" <c:if test="${isKb eq '-1'}">selected="selected"</c:if>>否</option>
							</select>
						</div>
					</div>
                </c:if>
				<c:if test="${RXGG eq companyUuid}">
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">出团日期：</label>
						<input id="groupDateBegin" name="groupDateBegin" class="inputTxt dateinput" value="${groupDateBegin }"
																					  readonly onClick="WdatePicker()"/> 至
						<input id="groupDateEnd" name="groupDateEnd" value="${groupDateEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
					</div>
				</c:if>
             </div>
          </div>
    </form:form>
    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
	         <div class="activitylist_paixu_left">
	            <ul>
		            <li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
		            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
	            </ul>
	            
	         </div>
	         
	         <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
	         <div class="kong"></div>
        </div>
   </div>
   <table id="contentTable" class="table mainTable table-striped table-condensed activitylist_bodyer_table">
        <!--  需求0307 起航假期  王洋   -->
        <thead>
        <tr>
            <th width="5%">序号</th>
            <th width="6%">收款日期<br>银行到账日期</th>
			<th width="5%">收款确认日期</th>
			<c:choose>
				<c:when test="${isQHJQ }">
					<th width="7%">团号</th>
					<th width="7%">产品名称</th>
				</c:when>
				<c:otherwise>
					<th width="10%">
						<span class="tuanhao on">团号</span>/
						<span class="chanpin">产品名称</span>
					</th>
				</c:otherwise>
			</c:choose>
			<c:if test="${RXGG eq companyUuid}"><th width="8%">出团日期</th></c:if>
			<th width="7%">团队类型</th>
<!-- 			<th width="6%">收款日期</th> -->
			<th width="6%">计调</th>
			<th width="10%">渠道商/地接社</th>
			 <th width="6%">来款单位</th>
			 <th width="6%">收款银行</th>
			<th width="6%">款项</th>
			<th width="10%">已收金额<br>到账金额</th>	
			<th width="6%">收款确认</th>
			<th width="4%">打印状态</th>
			<th width="5%">操作</th>
	    </tr>
	    </thead>
        <tbody>
         <c:if test="${fn:length(page.list) <= 0 }">
			 <tr class="toptr">
		         <td colspan="20" style="text-align:center;">暂无搜索结果</td>
			 </tr>
        </c:if>
		 <c:forEach items="${page.list }" var="orders" varStatus="s">
		        <tr class="toptr" height="50px">
					<td>
						<input type="checkbox" name="checkBox" value="${orders.payGroupId}" check-action="Normal">
						<input type="hidden" name="prdType" value="${orders.orderType}">
						${s.count }
					</td>
		             <td>
						 <div class="out-date">
							<c:if test="${not empty orders.createDate }"><fmt:formatDate value="${orders.createDate}" pattern="yyyy-MM-dd"/></c:if>
						 </div>
 						<div class="close-date"> 
							<c:if test="${not empty orders.accountDate and orders.isAsAccount eq '1' }">
								<fmt:formatDate value="${orders.accountDate}" pattern="yyyy-MM-dd"/>
							</c:if>
						</div>
                    </td>
					<td><fmt:formatDate value="${orders.receiptConfirmationDate}" pattern="yyyy-MM-dd"/></td>
                    <c:choose>
                    	<c:when test="${isQHJQ }">
                    		<td><div title="${orders.groupCode}">${orders.groupCode}</div></td>
                    		<td>
                    			<div title="${orders.productName}">
									<c:choose>
										<c:when test="${orders.orderType == 6}"> <!-- 签证 -->
											<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/visa/visaProducts/visaProductsDetail/${orders.productId}')">
								    			${orders.productName}
	               		 					</a>
										</c:when>
										<c:when test="${orders.orderType == 7}"> <!-- 机票 -->
											<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${orders.productId}')">
								    			${fns:getProductName(orders.productId, orders.orderType)}
	               		 					</a>
										</c:when>
										<c:when test="${orders.orderType == 12}"> <!-- 海岛 -->
											<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityIsland/showActivityIslandDetail/${orders.productUUID }?type=product')">
								      			${orders.productName}
	               		 					</a>
										</c:when>
										<c:when test="${orders.orderType == 11}"> <!-- 酒店 -->
											<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityHotel/showActivityHotelDetail/${orders.productUUID }?type=product')">
								      			${orders.productName}
	               		 					</a>
										</c:when>
										<c:otherwise>
		               		 				<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${orders.productId}')">
								    			${orders.productName}
	               		 					</a>
	               		 				</c:otherwise>
									</c:choose>
               					</div>
                    		</td>
                    	</c:when>
                    	<c:otherwise>
                    		 <td>
                    			<div class="tuanhao_cen onshow" title="${orders.groupCode}"> 
                    				${orders.groupCode}</div>
								<div class="chanpin_cen qtip" title="${orders.productName}">
									<c:choose>
										<c:when test="${orders.orderType == 6}"> <!-- 签证 -->
											<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/visa/visaProducts/visaProductsDetail/${orders.productId}')">
								    			${orders.productName}
	               		 					</a>
										</c:when>
										<c:when test="${orders.orderType == 7}"> <!-- 机票 -->
											<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${orders.productId}')">
								    			${fns:getProductName(orders.productId, orders.orderType)}
	               		 					</a>
										</c:when>
										<c:when test="${orders.orderType == 12}"> <!-- 海岛 -->
											<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityIsland/showActivityIslandDetail/${orders.productUUID }?type=product')">
								      			${orders.productName}
	               		 					</a>
										</c:when>
										<c:when test="${orders.orderType == 11}"> <!-- 酒店 -->
											<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityHotel/showActivityHotelDetail/${orders.productUUID }?type=product')">
								      			${orders.productName}
	               		 					</a>
										</c:when>
										<c:otherwise>
		               		 				<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${orders.productId}')">
								    			${orders.productName}
	               		 					</a>
	               		 				</c:otherwise>
									</c:choose>
               					</div>
               				</td>
                    	</c:otherwise>
                    </c:choose>
               		<!-- 12792 modify by wangyang 2016.3.8 -->
					<c:if test="${RXGG eq companyUuid}">
						<td>
							<!-- 先将String转换成Date，然后再格式化 update by shijun.liu 2016.03.08-->
							<fmt:parseDate value="${orders.groupOpenDate}" pattern="yyyy-MM-dd" var="mydate" />
							<fmt:formatDate value="${mydate}" pattern="yyyy-MM-dd"/>
						</td>
					</c:if>
               		<td>${fns:getStringOrderStatus(orders.orderType)}</td>
<%--                     <td><fmt:formatDate value="${orders.createDate }" pattern="yyyy-MM-dd"/> </td> --%>
               		<td>
               			<c:if test="${orders.operatorId != '' && orders.operatorId != null}">
               				${fns:getUserById(orders.operatorId).name}
               			</c:if>
               		</td>
                    <td class="tr">
                    	<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
						<c:choose>
						   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && orders.supplyName eq '非签约渠道'}"> 
						       直客
						   </c:when>
						   <c:otherwise>
						      ${orders.supplyName }
						   </c:otherwise>
						</c:choose> 
                    </td>
                    <td>${orders.payerName }</td>
                    <td>${orders.toBankNname }</td>
                    <c:if test = "${showKb eq 1}">
	                    <td class="p0 tr pr">
	                    <c:if test="${orders.isKb == 1}">
	                    <p><span class="icon-mode-of-kb-check"></span></p>
	                    </c:if>
                    </c:if>
                     <c:if test = "${showKb ne 1}">
                     	<td class="tr">
                     </c:if>
                    ${orders.name }</td>
	            	<td class="p0 tr pr">	
	            	<c:choose>
		            	  <c:when test="${orders.payType == 1}"><p><span class="icon-mode-of-payment-check"></span></p></c:when>
		            	  <c:when test="${orders.payType == 3 or orders.payType==5}"><p><span class="icon-mode-of-payment-cash"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 4 }"><p><span class="icon-mode-of-payment-remit"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 6}"><p><span class="icon-mode-of-payment-transfer"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 7}"><p><span class="icon-mode-of-payment-bill"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 8 or orders.payType==2}"><span class="icon-mode-of-payment-pos"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 9}"><p><span class="icon-mode-of-payment-alipay"> </span></p></c:when>
		            	</c:choose>
                        <div class="yfje_dd">	
							 <span class="fbold">${fns:getCurrencyNameOrFlag(orders.currencyId,'0')}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${orders.amount}"/></span>
						</div>
						<div class="dzje_dd">
						  <c:if test="${orders.isAsAccount eq '1' }">
							 <span class="fbold">${fns:getCurrencyNameOrFlag(orders.currencyId,'0')}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${orders.amount}"/>&nbsp;</span>
					      </c:if>
						</div>
		            </td>
		            <td>
			            <c:if test="${orders.isAsAccount eq '1'}">已达账<br>
			            	<shiro:hasPermission name="other:operation:revoke">
			            	<a onclick="cancelRecepits('${ctx}',${orders.payGroupId},this);" href="javascript:void(0)">撤销</a>
			            	</shiro:hasPermission>
			            </c:if>
			            <c:if test="${orders.isAsAccount eq '102'}">已驳回</c:if>
			            <c:if test="${orders.isAsAccount eq '101' || empty orders.isAsAccount}">
			            	<a onclick="recepitsConfim('${ctx}',${orders.payGroupId},this);" href="javascript:void(0)">收款</a>  
			            	<a onclick="recepitsReject('${ctx}',${orders.payGroupId},this);" href="javascript:void(0)">驳回</a>
			            </c:if>
		            </td>
		            <td class="invoice_yes">
		            	<c:if test="${empty orders.printFlag || orders.printFlag == 0 }">未打印</c:if>
		            	<c:if test="${orders.printFlag == 1 }"><p class="uiPrint">已打印<span style="display: none;">首次打印时间<br>
		            	<fmt:formatDate value="${orders.printTime}" type="both" dateStyle="default" pattern="yyyy/MM/dd HH:mm"/>
		            	</span></p></c:if>
		            </td>
		            <td>
                    	<c:if test="${orders.orderType == 6}">
<!--                         	<a href="${ctx }/cost/review/visaRead/${orders.productId}/0" target="_blank">查看详情</a> -->
							<a href="${ctx}/costReview/visa/visaCostReviewDetail/${orders.productId}/2?read=1" target="_blank">查看</a>
                        </c:if>
                        <c:if test="${orders.orderType == 7}">
<!--                         	<a target="_blank" href="${ctx}/cost/review/airTicketRead/${orders.productId}/0">查看详情</a> -->
							<a href="${ctx}/costReview/airticket/airticketCostReviewDetail/${orders.productId}/2?read=1" target="_blank">查看</a>
                        </c:if>
                        <c:if test="${orders.orderType < 6 || orders.orderType == 10}">
<!--                         	<a target="_blank" href="${ctx}/cost/review/read/${orders.productId}/${orders.groupId}/0?from=operatorPre">查看详情</a> -->
							<a href="${ctx}/costReview/activity/activityCostReviewDetail/${orders.productId}/${orders.groupId}/2?read=1" target="_blank">查看</a>
                        </c:if>
                        <c:if test="${orders.orderType == 11}">  <!-- 酒店 -->
                        	<a target="_blank" href="${ctx}/cost/island/hotelDetail/${orders.productUUID }/${orders.groupUUID}/11?from=operatorPre">查看详情</a>
                        </c:if>
                        <c:if test="${orders.orderType == 12}">  <!-- 海岛游 -->
                        	<a target="_blank" href="${ctx}/cost/island/islandDetail/${orders.productUUID }/${orders.groupUUID}/12?from=operatorPre">查看详情</a>
                        </c:if>
                    	<a target="_blank" href="${ctx}/cost/manager/accountPrint/${orders.payGroupId}/${orders.orderType}">打印</a>
                    </td>
		        </tr>
            </c:forEach>
        </tbody>
    </table>
	<%--<div id="contentTable_foot">--%>
		<%--<label><input check-action="All" type="checkbox"> 全选</label>--%>
		<%--<label><input check-action="Reverse" type="checkbox"> 反选</label>--%>
		<%--<input type="button" class="btn-primary" value="批量打印" onclick="batchPrint()" />--%>
	<%--</div>--%>
	<form method="post" action="${ctx}/cost/manager/accountBatchPrint" id="printForm" target="_blank">
		<input id="printInfo" name="printInfo" type="hidden" value=""/>
	</form>
</div>

<div class="page" id="contentTable_foot">
	<div class="pagination">
		<dl>
			<dt><input check-action="All" type="checkbox">全选</dt>
			<dt><input check-action="Reverse" type="checkbox">反选</dt>
			<dd>
				<input type="button" class="btn ydbz_x " onclick="batchPrint();" value="批量打印">
			</dd>
		</dl>
	</div>

	<div class="pagination clearFix">
		${page}
	</div>
</div>
<%--<div class="pagination clearFix">--%>
   <%--${page}</div>--%>
 <style type="text/css">
.orderList_dosome{ text-align:left; margin-left:11px;}
.orderList_line{ height:100%; width:50px;float:left; }
 </style>
</body>
</html>