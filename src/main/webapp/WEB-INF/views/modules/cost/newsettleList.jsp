<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>财务-结算管理-结算单</title>

<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap.min.css"/>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="${ctxStatic}/js/bootstrap-ie.min.js"></script>
<![endif]-->
<!--[if IE 7]>
<style>
	td{border:1px solid gray;}
	.ie7shiying td{border-top:none;}
</style>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/dayinbdzy.css" />
<style type="text/css">
	.dbaniu {
		overflow: hidden;
		margin-top: 50px;
		margin-right: auto;
		margin-bottom: 50px;
		margin-left: auto;
		text-align: center;
	}
	.ydbz_s {
		height: 28px;
		padding: 0 15px;
		text-align: center;
		display: inline-block;
		margin: 0 3px;
		cursor: pointer;
	}
	.ydbz_s {
		color: #fff;
		border: medium none;
		background: #5f7795;
		box-shadow: none;
		text-shadow: none;
		font-size: 12px;
		border-radius: 4px;
		height: 28px;
		line-height: 28px;
	}
	.ydbz_s:hover {
		text-decoration: none;
		background: #28b2e6;
		color: #fff;
	}
	.gray {
		color: #FFF;
		border: medium none;
		background: #B3B3B3 none repeat scroll 0% 0%;
		box-shadow: none;
		text-shadow: none;
		font-size: 12px;
		height: 28px;
		padding: 0px 15px;
	}
</style>

<style media="print">
	.print_disappear {
		display:none;
	}
	.print_expand {
		width:100%;
	}
</style>

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
function locker(activityIdOrUUID,orderType,lockStatus,_this){
	//lockStatus 0:未锁定状态，1:表示锁定状态
    if(0 == lockStatus){//锁定
    	$.ajax({
            type: "POST",
            url: "${ctx}/cost/manager/canLock",
            dataType:"json",
            data: {activityIdOrUUID:activityIdOrUUID,orderType:orderType},
            success: function(data){
            	if(data.flag == 'success'){
            		//$.jBox.tip("Loading...", 'loading');
            		_lock(activityIdOrUUID,orderType,lockStatus,_this);
            	}else if(data.flag == 'fail' && data.mag != '' && data.msg != undefined){
            		$.jBox.confirm(data.msg, "系统提示", function(v, h, f){
            			if (v == 'ok') {
            				//$.jBox.tip("Loading...", 'loading');
            				_lock(activityIdOrUUID,orderType,lockStatus,_this);
            			}
            		});
            	}
            }
       	});
    }else if(1 == lockStatus){//解锁
    	_lock(activityIdOrUUID,orderType,lockStatus,_this);
    }
}
/**
 * 确定锁定结算单
 */
function _lock(activityIdOrUUID,orderType,lockStatus,_this){
	var $this = $(_this);
	var message = (lockStatus == 0) ? "确认要锁定结算单吗？" : "结算单解锁后将更新单内的数据，是否确定解锁？";
	var status = (lockStatus == 0) ?1:0;
	var tipMsg = (lockStatus == 0)?"锁定成功":"解锁成功";
	$.jBox.confirm(message, "系统提示", function(v, h, f){
		if (v == 'ok') {
			$.ajax({
				type: "POST",
				url: "${ctx}/cost/manager/locker",
				dataType:"json",
				data: {
					activityIdOrUUID:activityIdOrUUID,
					orderType:orderType,
					lockStatus:status,
					budgetType:'1'
				},
				success: function(data){
					if(data.flag == 'success'){
						top.$.jBox.tip(tipMsg,'success');
						$this.remove();
						location.replace(location.href);
						window.opener.location.reload();
					}
				}
			});
		}
	});
}

  //点击备注的编辑按钮，文本框可编辑，并使用保存和 取消按钮替换编辑按钮
  function editRemark(obj) {
  	$("#remark").removeAttr("disabled");  	//撤销备注文本框的disable属性，变为可编辑状态
  	$(obj).hide();                        	//隐藏编辑框
  	$("#saveAndCancelEdit").show();			//显示保存和取消按钮
  }
  
  //点击保存按钮，保存备注内容,隐藏保存/取消按钮，显示编辑按钮
  function saveRemark(activityIdOrUUID, orderType) {
  	//异步保存备注内容
  	var $remark = $("#remark").val();
  	$.ajax({
  		type : "POST",
  		url : "${ctx}/cost/manager/saveRemark",
  		dataType : "json",
  		data : {
  			activityIdOrUUID:activityIdOrUUID,
			orderType:orderType,
			remark:$remark,
			option:'settle'
  		},
  		success:function(data) {
  			if(data.flag == 'success') {
  				$.jBox.tip('保存备注成功');
  				window.location.reload();				
  			}
  		}
  	});
  	//隐藏保存/取消按钮，显示编辑按钮
  	$("#saveAndCancelEdit").hide();
  	$("#editRemark").show();
  	$("#remark").attr("disabled", "disabled");
  }
  
  //点击取消按钮，文本框恢复更改前的内容，并使用编辑按钮替代保存/取消按钮
  function cancelRemark(settleRemark) {
  	$("#remark").val(settleRemark);		//	为备注文本框恢复原内容
  	//隐藏保存/取消按钮，显示编辑按钮
  	$("#saveAndCancelEdit").hide();
  	$("#editRemark").show();
  	$("#remark").attr("disabled", "disabled");
  }
   
  //限制备注文本框输入的字符数
  function setRemarkCount(_this) {
  	var $this = $(_this);
 
  	var length = $this.val().length;
  	var remarkCount = length + "/" + (100-length);
  	$("#remarkCount").html(remarkCount);
  }
</script>
</head>
<body>
	<div id="printDiv">
		<div class="dayinzys">
			<table width="700" border="0" height="50">
				<tr class="noborder" height="40">
					<td align="center" valign="top" class="text_title noborder" height="30" style="line-height:30px;" colspan="2">团队结算单</td>
				</tr>
			</table>
			<table width="700" border="0" cellspacing="0" cellpadding="0">
				<tr class="noborder">
					<td width="20%" height="40" class="text_little_title noborder">操作：${vo.createBy}</td>
					<td width="50%" height="40" class="text_little_title noborder" title="${vo.createByLeaderFull}">操作负责人：${vo.createByLeader}</td>
					<td height="40" class="text_little_title noborder">销售：${vo.salers}</td>
				</tr>
			</table>
			<table width="700" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="90" align="center" class="text_normal bgcolorccc" style="border-bottom:none;">线路</td>
					<td colspan="5" align="left" class="text_normal danhangnormal" style="border-bottom:none;">${vo.productName}</td>
				</tr>
			</table>
			<table width="700" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="90" height="10" align="center" class="text_normal bgcolorccc">团号</td>
					<td align="left" bgcolor="#FFFFFF" class="text_normal danhangnormal">${vo.groupCode}</td>
					<td width="64" align="center"  class="text_normal bgcolorccc">人数</td>
					<td width="65" align="center" bgcolor="#FFFFFF" class="text_normal">${vo.orderPersonNumSum}</td>
					<td width="60" align="center" class="text_normal bgcolorccc">人天数</td>
					<td width="100" align="center" bgcolor="#FFFFFF" class="text_normal">${vo.personDay}</td>
				</tr>
				<tr>
					<td height="10" align="center" class="text_normal bgcolorccc">日期</td>
					<td align="left" bgcolor="#FFFFFF" class="text_normal">${vo.groupDate}</td>
					<td align="center" class="text_normal bgcolorccc">天数</td>
					<td align="center" bgcolor="#FFFFFF" class="text_normal">${vo.activityDuration}</td>
					<td align="center" class="text_normal bgcolorccc">领队</td>
					<td align="center" bgcolor="#FFFFFF" class="text_normal">${vo.grouplead}</td>
				</tr>
				<tr>
					<td height="10" align="center" class="text_normal bgcolorccc">地接社</td>
					<td align="left" bgcolor="#FFFFFF" class="text_normal"></td>
					<td align="center" class="text_normal bgcolorccc">报价</td>
					<td colspan="3" align="right" bgcolor="#FFFFFF" class="text_normal">¥${vo.totalMoneySum}</td>
				</tr>
			</table>
			<div class="danwkk">
				<div class="leftmenu" id="leftmenu1">
					收<br />款<br />明<br />细
				</div>
				<table width="670" border="0" cellspacing="0" cellpadding="0" bgcolor="#cccccc" style="float:left;">
					<tr class="text_normal ie7shiying"  style="border-top:none;">
						<td width="51" height="10" align="center" class="text_normal bgcolorccc">销售</td>
						<td align="center" bgcolor="#F5F5F5" class="text_normal bgcolorccc">客户单位</td>
						<td width="50" align="center" class="text_normal bgcolorccc">人数</td>
						<td width="100" align="center" class="text_normal bgcolorccc"><c:if test="${isHQX}">收款</c:if><c:if test="${not isHQX}">实际应收</c:if></td>
						<td width="100" align="center" class="text_normal bgcolorccc">退款</td>
						<td width="100" align="center" class="text_normal bgcolorccc">实际收款</td>
						<td width="100" align="center" class="text_normal bgcolorccc">未收团款</td>
					</tr>
					<c:forEach items="${vo.expectIncome}" var="income">
					<tr>
						<td height="10" align="center" bgcolor="#FFFFFF" class="text_normal danhangnormal">
							<c:if test="${not empty income.saler}">${income.saler}</c:if>
						</td>
						<td bgcolor="#FFFFFF" class="text_normal danhangnormal">
							<c:choose>
								<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and income.agentName eq '非签约渠道'}">未签</c:when>
								<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' and income.agentName eq '非签约渠道'}">直客</c:when>
								<c:otherwise>${income.agentName}</c:otherwise>
							</c:choose>
						</td>
						<td align="center" bgcolor="#FFFFFF" class="text_normal">${income.orderPersonNum }</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${income.totalMoney}</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${income.refundprice}</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${income.accountedMoney}</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${income.notAccountedMoney}</td>
					</tr>
					</c:forEach>
					<c:if test="${fn:length(vo.expectIncome)<2}">
						<c:forEach begin="1" end="${2-fn:length(vo.expectIncome)}">
						<tr>
							<td height="10" align="center" bgcolor="#FFFFFF" class="text_normal danhangnormal">&nbsp;</td>
							<td bgcolor="#FFFFFF" class="text_normal danhangnormal">&nbsp;</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
						</tr>
						</c:forEach>
					</c:if>
					<tr>
						<td height="10" colspan="2" align="right" bgcolor="#F5F5F5" class="text_little_title bgcolorccc" style="border-bottom:none;">合计</td>
						<td align="center" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">${vo.orderPersonNumSum }</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">¥${vo.totalMoneySum}</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">¥${vo.backMoneySum}</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">¥${vo.accountedMoneySum}</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">¥${vo.notAccountedMoneySum}</td>
					</tr>
				</table>
				<div class="clear"></div>
			</div>
			<%--<c:choose>&lt;%&ndash; 越谏行踪境内境外多了一列付款状态，所以在这里设置一下，小计和合计行的跨列数 yudong.xu 2016.5.26 &ndash;%&gt;
				<c:when test="${isYJXZ}"><c:set var="colspanNum" value="5"></c:set> </c:when>
				<c:otherwise><c:set var="colspanNum" value="4"></c:set></c:otherwise>
			</c:choose>--%>
			<div class="danwkk">
				<div class="leftmenu" id="leftmenu2">
					境<br />内<br />支<br />出
				</div>
				<table width="670" border="0" cellspacing="0" cellpadding="0" bgcolor="#cccccc" style="float:left;">
					<tr style="border-top:none;" class=" ie7shiying">
						<td width="51" height="10" align="center" class="text_normal bgcolorccc">项目</td>
						<td align="center" class="text_normal bgcolorccc">地接社/渠道商</td>
						<td width="100" align="center" class="text_normal bgcolorccc">单价</td>
						<td width="100" align="center" class="text_normal bgcolorccc">数量</td>
						<td width="100" align="center" class="text_normal bgcolorccc">金额</td>
						<%--<c:if test="${isYJXZ}">
						<td width="100" align="center" class="text_normal bgcolorccc">付款状态</td>
						</c:if>--%>
					</tr>
					<c:forEach items="${vo.actualInList}" var="record">
						<tr>
							<td height="10" align="center" bgcolor="#FFFFFF" class="text_normal duohangnormal">${record.name}</td>
							<td align="left" bgcolor="#FFFFFF" class="text_normal duohangnormal"><div class="w334">
								<c:choose>
									<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and record.supplyName eq '非签约渠道'}">未签</c:when>
									<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' and record.supplyName eq '非签约渠道'}">直客</c:when>
									<c:otherwise>${record.supplyName}</c:otherwise>
								</c:choose>
							</div></td>
							<c:if test="${record.formatPrice eq '-'}">
								<td align="center" bgcolor="#FFFFFF" class="text_normal">${record.formatPrice}</td>
							</c:if>
							<c:if test="${record.formatPrice ne '-'}">
								<td align="right" bgcolor="#FFFFFF" class="text_normal">${record.formatPrice}</td>
							</c:if>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">
								<c:if test="${not empty record.quantity}">${record.quantity}</c:if>
								<c:if test="${empty record.quantity}">0</c:if>
							</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${record.formatPriceAfter}</td>
						</tr>
					</c:forEach>
					<%--<c:forEach items="${vo.actualInGrouped}" var="entry">
						<c:forEach items="${entry.value}" var="record">
							<tr>
								<td height="10" align="center" bgcolor="#FFFFFF" class="text_normal duohangnormal">${record.name}</td>
								<td align="left" bgcolor="#FFFFFF" class="text_normal duohangnormal"><div class="w334">${record.supplyName}</div></td>
								<c:if test="${record.formatPrice eq '-'}">
									<td align="center" bgcolor="#FFFFFF" class="text_normal">${record.formatPrice}</td>
								</c:if>
								<c:if test="${record.formatPrice ne '-'}">
									<td align="right" bgcolor="#FFFFFF" class="text_normal">${record.formatPrice}</td>
								</c:if>
								<td align="center" bgcolor="#FFFFFF" class="text_normal">
									<c:if test="${not empty record.quantity}">${record.quantity}</c:if>
									<c:if test="${empty record.quantity}">0</c:if>
								</td>
								<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${record.formatPriceAfter}</td>
								<c:if test="${isYJXZ}">
									<td align="center" bgcolor="#FFFFFF" class="text_normal">
										<c:choose>
											<c:when test="${record.payStatus == 1}">已付</c:when>
											<c:when test="${record.payStatus == 0}">未付</c:when>
											<c:otherwise></c:otherwise>
										</c:choose>
									</td>
								</c:if>

							</tr>
						</c:forEach>
						<tr>
							<td height="10" colspan="${colspanNum}" align="right" bgcolor="#F5F5F5" class="text_little_title bgcolorccc" style="border-bottom:none;">小计</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">¥${vo.inSubtotals[entry.key]}</td>
						</tr>
					</c:forEach>--%>
					<c:if test="${fn:length(vo.actualInList)<4}">
						<c:forEach begin="1" end="${4-fn:length(vo.actualInList)}">
						<tr>
							<td height="10" align="center" bgcolor="#FFFFFF" class="text_normal duohangnormal">&nbsp;</td>
							<td align="left" bgcolor="#FFFFFF" class="text_normal duohangnormal"><div class="w334">&nbsp;</div></td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<%--<c:if test="${isYJXZ}">
								<td align="center" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							</c:if>--%>
						</tr>
						</c:forEach>
					</c:if>
					<tr>
						<td height="10" colspan="4" align="right" bgcolor="#F5F5F5" class="text_little_title bgcolorccc" style="border-bottom:none;">合计</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">¥${vo.expectedInMoneySum}</td>
					</tr>
				</table>
				<div class="clear"></div>
			</div>
			<div class="danwkk">
				<div class="leftmenu" id="leftmenu3">
					境<br />外<br />支<br />出
				</div>
				<table width="670" border="0" cellspacing="0" cellpadding="0" bgcolor="#cccccc" style="float:left;">
					<tr class="text_normal ie7shiying"  style="border-top:none;">
						<td height="10" align="center" class="text_normal bgcolorccc">地接社/渠道商</td>
						<td width="45" align="center" class="text_normal bgcolorccc">币种</td>
						<td width="45" align="center" class="text_normal bgcolorccc">汇率</td>
						<td width="100" align="center" class="text_normal bgcolorccc">外币</td>
						<td width="100" align="center" class="text_normal bgcolorccc">金额</td>
						<%--<c:if test="${isYJXZ}">
							<td width="100" align="center" class="text_normal bgcolorccc">付款状态</td>
						</c:if>--%>
					</tr>
					<c:forEach items="${vo.actualOutList}" var="outList">
						<tr>
							<td height="10" align="left" bgcolor="#FFFFFF" class="text_normal danhangnormal"><div class="w364">
								<c:choose>
									<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and outList.supplyName eq '非签约渠道'}">未签</c:when>
									<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' and outList.supplyName eq '非签约渠道'}">直客</c:when>
									<c:otherwise>${outList.supplyName}</c:otherwise>
								</c:choose>
							</div></td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">${outList.name}</td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">
								<fmt:formatNumber minFractionDigits="3" maxFractionDigits="3" type="number" pattern="#,##0.000" value="${outList.rate}"/>
							</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">${outList.formatPrice}</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${outList.formatPriceAfter}</td>
						</tr>
					</c:forEach>
					<%--<c:forEach items="${vo.actualOutGrouped}" var="entry">
						<c:forEach items="${entry.value}" var="outList">
							<tr>
								<td height="10" align="left" bgcolor="#FFFFFF" class="text_normal danhangnormal"><div class="w364">${outList.supplyName}</div></td>
								<td align="center" bgcolor="#FFFFFF" class="text_normal">${outList.name}</td>
								<td align="center" bgcolor="#FFFFFF" class="text_normal">
									<fmt:formatNumber minFractionDigits="3" maxFractionDigits="3" type="number" pattern="#,##0.000" value="${outList.rate}"/>
								</td>
								<td align="right" bgcolor="#FFFFFF" class="text_normal">${outList.formatPrice}</td>
								<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${outList.formatPriceAfter}</td>
								<c:if test="${isYJXZ}">
									<td align="center" bgcolor="#FFFFFF" class="text_normal">
										<c:choose>
											<c:when test="${outList.payStatus == 1}">已付</c:when>
											<c:when test="${outList.payStatus == 0}">未付</c:when>
											<c:otherwise></c:otherwise>
										</c:choose>
									</td>
								</c:if>
							</tr>
						</c:forEach>
						<tr>
							<td height="10" colspan="${colspanNum}" align="right" bgcolor="#F5F5F5" class="text_little_title bgcolorccc" style="border-bottom:none;">小计</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">¥${vo.outSubtotals[entry.key]}</td>
						</tr>
					</c:forEach>--%>
					<c:if test="${fn:length(vo.actualOutList)<4}">
						<c:forEach begin="1" end="${4-fn:length(vo.actualOutList)}"> 
						<tr>
							<td height="10" align="left" bgcolor="#FFFFFF" class="text_normal danhangnormal"><div class="w364">&nbsp;</div></td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<%--<c:if test="${isYJXZ}">
								<td align="center" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							</c:if>--%>
						</tr>
						</c:forEach>
					</c:if>
					<tr class="text_normal">
						<td height="10" colspan="4" align="right" bgcolor="#F5F5F5" class="text_little_title bgcolorccc" style="border-bottom:none;">合计</td>
						<td align="right" bgcolor="#FFFFFF" style="border-bottom:none;">¥${vo.expectedOutMoneySum}</td>
					</tr>
				</table>
				<div class="clear"></div>
			</div>
			<table width="700" border="0" cellspacing="0" cellpadding="0">
				<tr class="noborder">
					<td height="1"  class="noborder"></td>
				</tr>
			</table>
			<table width="700" border="0" cellspacing="0" cellpadding="0" bgcolor="#cccccc">
				<tr class="text_normal">
				<c:if test="${!isLaMeiTu }">
					<td height="10" align="center" class="text_normal bgcolorccc">收入合计</td>
					<td align="center" class=" bgcolorccc">退款合计</td>
					<td align="center" class=" bgcolorccc">实际收入</td>
					<td width="100" align="center" class=" bgcolorccc">支出合计</td>
					<!-- 0258 懿洋假期  王洋  2016.3.31 -->
					<c:choose>
						<c:when test="${isYYJQ }">
							<td width="100" align="center" class="text_normal bgcolorccc">税款</td>
							<td width="100" align="center" class="text_normal bgcolorccc">税后利润</td>
						</c:when>
						<c:otherwise>
							<td width="100" align="center" class="text_normal bgcolorccc">毛利</td>
							<td width="100" align="center" class="text_normal bgcolorccc">毛利率</td>
						</c:otherwise>
					</c:choose>
				</c:if>
				<c:if test="${isLaMeiTu }">
					<td align="center" class=" bgcolorccc">实际收入</td>
					<td width="175" align="center" class=" bgcolorccc">支出合计</td>
					<!-- 0258 懿洋假期  王洋  2016.3.31 -->
					<c:choose>
						<c:when test="${isYYJQ }">
							<td width="175" align="center" class="text_normal bgcolorccc">税款</td>
							<td width="175" align="center" class="text_normal bgcolorccc">税后利润</td>
						</c:when>
						<c:otherwise>
							<td width="175" align="center" class="text_normal bgcolorccc">毛利</td>
							<td width="175" align="center" class="text_normal bgcolorccc">毛利率</td>
						</c:otherwise>
					</c:choose>
				</c:if>
				</tr>
				<tr>
				<c:if test="${!isLaMeiTu }">
					<td height="10" align="center" bgcolor="#FFFFFF" class="text_normal">¥${vo.totalMoneySum}</td>
					<td align="center" bgcolor="#FFFFFF" class="text_normal">¥${vo.backMoneySum}</td>
				</c:if>
					<td align="center" bgcolor="#FFFFFF" class="text_normal">¥${vo.realMoneySum}</td>
					<td align="center" bgcolor="#FFFFFF" class="text_normal">¥${vo.outMoneySum}</td>
					<c:choose>
						<c:when test="${isYYJQ }">
							<td align="center" bgcolor="#FFFFFF" class="text_normal">¥${vo.invoiceMoney }</td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">¥${vo.profitAfterTax }</td>
						</c:when>
						<c:otherwise>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">¥${vo.profitSum}</td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">${vo.profitRate}</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
			<c:if test="${isLaMeiTu}">
				<!-- 0359需求 增加备注 -->
				<table width="700" border="0" cellspacing="0" cellpadding="0">
					<tr class="noborder">
						<td height="1"  class="noborder"></td>
					</tr>
				</table>
				<table width="700" border="0" cellspacing="0" cellpadding="0">
					<tr height="30" class="bgcolorccc">
						<td width="685">
							<div style="width:100%">
								<div align="left" class="text_little_title  print_expand">备注</div>
								<shiro:hasPermission name="settle:remark:edit">
									<div align="right" class="text_little_title print_disappear">
										<a id="editRemark" href="javascript:void" onclick="javascript:editRemark(this)">编辑</a>
									</div>
								</shiro:hasPermission>
								<div id="saveAndCancelEdit" align="right" style="display:none;" class="text_little_title bgcolorccc print_disappear">
									<a href="javascript:void" onclick="javascript:saveRemark('${activityIdOrUUID}',${orderType})">保存</a>&nbsp;
									<a href="javascript:void" onclick="javascript:cancelRemark('${vo.remark}')">取消</a>
								</div>
							</div>						
						</td>
					</tr>
					<tr height="10" bgcolor="#FFFFFF">
						<td width="685" height="65" valign="bottom">
							<div style="width:97%;float:left">
								<textarea id="remark" disabled="disabled" style="width:95%; height:40px" maxlength="100" class="noborder print_expand"  onkeyup="javascript:setRemarkCount(this)">${vo.remark}</textarea>
							</div>
							<div style="width:3%;float:right">
								<span id="remarkCount" style="width:40px; height:30px; line-height:65px; text-align:center; float:right;" class="print_disappear"></span>
							</div>
						</td>
					</tr>
				</table>
			</c:if>
			<table width="700" border="0" cellspacing="0" cellpadding="0" height="40">
				<tr class="noborder" height="30">
					<td class="text_little_title noborder" height="30" style="line-height:30px;">经理签字：</td>
					<td class="noborder" height="30" style="line-height:30px;">&nbsp;</td>
					<td class="text_little_title noborder" height="30" style="line-height:30px;">填表人签字：</td>
					<td class="noborder" height="30" style="line-height:30px;">&nbsp;</td>
				</tr>
			</table>
			</div>
		</div> 
	<!--S打印&下载按钮-->
	<div class="dbaniu">
		<shiro:hasPermission name="settle:lock && settle:unlock">
			<c:if test="${lockStatus == 0 }">
					<input id="" name="" type="button" class="ydbz_s" value="锁定" onclick="javascript:locker('${activityIdOrUUID}',${orderType },'${lockStatus }',this);">
			</c:if>
			<c:if test="${lockStatus == 1 }">
					<input id="" name="" type="button" class="ydbz_s" value="解锁" onclick="javascript:locker('${activityIdOrUUID}',${orderType },'${lockStatus }',this);">
			</c:if>
		</shiro:hasPermission>
		<shiro:hasPermission name="settle:lock && not settle:unlock">
			<c:if test="${lockStatus == 0 }">
					<input id="" name="" type="button" class="ydbz_s" value="锁定" onclick="javascript:locker('${activityIdOrUUID}',${orderType },'${lockStatus }',this);">
			</c:if>
			<c:if test="${lockStatus == 1 }">
					<input id="" name="" type="button" disabled="disabled" class="ydbz_s gray" value="解锁">
			</c:if>
		</shiro:hasPermission>
		<shiro:hasPermission name="settle:unlock && not settle:lock">
			<c:if test="${lockStatus == 0 }">
					<input id="" name="" type="button" class="ydbz_s gray" disabled="disabled" value="锁定">
			</c:if>
			<c:if test="${lockStatus == 1 }">
					<input id="" name="" type="button" class="ydbz_s" value="解锁" onclick="javascript:locker('${activityIdOrUUID}',${orderType },'${lockStatus }',this);">
			</c:if>
		</shiro:hasPermission>
		<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure();">
		<input id="" name="" type="button" class="ydbz_s" value="下载" onclick="download();">
	</div>
	
	<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript">
		function printTure() {
			printPage(document.getElementById("printDiv"));
		}
		function download(){
			window.location.href="${ctx}/cost/manager/downLoadSettletList?activityId=${activityIdOrUUID}&orderType=${orderType}&type=1"
		}
	</script>
</body>
</html>
