<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <meta name="decorator" content="wholesaler"/> -->
<title>财务-成本管理-预报单</title>

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
<script> 
  function locker(activityIdOrUUID,orderType,lockStatus,_this){
       var $this = $(_this);
	   var message = (lockStatus == '00') ? "确认要锁定预报单吗？" : "预报单解锁后将更新单内的数据，是否确定解锁？";
	   var status = (lockStatus == '00') ?'10':'00';
	   var tipMsg = (lockStatus == '00')?"锁定成功":"解锁成功";
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
			        	budgetType:'0'
			        },
			        success: function(data){
                        if(data.flag == 'success'){
				            $.jBox.tip(tipMsg,'success');
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
	$("#remarkTextDiv").width("97%");
	$("#remark").width("95%");
	//显示字数统计框
	var remark = $("#remark").val();
	var remarkLength = remark.length;
	var remarkCount = remarkLength + "/" + (100-remarkLength);
	$("#remarkCount").html(remarkCount);
	$("#remarkCountDiv").show();               
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
			option:'forecast'
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
	$("#remarkCountDiv").hide();
	$("#editRemark").show();
	
	$("#remarkTextDiv").width("100%");
	$("#remark").width("98%");
	$("#remark").attr("disabled", "disabled"); 	
}

//点击取消按钮，文本框恢复更改前的内容，并使用编辑按钮替代保存/取消按钮
function cancelRemark(forecastRemark) {
	$("#remark").val(forecastRemark);		//	为备注文本框恢复原内容
	var length = $("#remark").val().length;
	var remarkCount = length + "/" + (100-length);
	$("#remarkCount").html(remarkCount);
	
	//隐藏保存/取消按钮，显示编辑按钮
	$("#saveAndCancelEdit").hide();
	$("#remarkCountDiv").hide();
	$("#editRemark").show();
	
	$("#remarkTextDiv").width("100%");
	$("#remark").width("98%");
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
	<!--右侧内容部分开始-->
	<div id="printDiv">
		<div class="dayinzys">
			<table width="700" border="0" height="50">
				<tr class="noborder" height="40">
					<td align="center" valign="top" class="text_title noborder" height="30" style="line-height:30px;" colspan="2">团队预报单</td>
				</tr>
			</table>
			<table width="700" border="0" cellspacing="0" cellpadding="0">
				<tr class="noborder">
					<td width="30%" height="40" class="text_little_title noborder">操作：${vo.createBy}</td>
					<td width="30%" height="40" class="text_little_title noborder" title="${vo.createByLeaderFull}">操作负责人：${vo.createByLeader}</td>
					<td height="40" class="text_little_title noborder">销售：${vo.salers}</td>
				</tr>
			</table>
			<table width="700" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="81" align="center" class="text_normal bgcolorccc" style="border-bottom:none;">线路</td>
					<td colspan="5" align="left" class="text_normal danhangnormal" style="border-bottom:none;">${vo.productName}</td>
				</tr>
			</table>
			<table width="700" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="81" height="10" align="center" class="text_normal bgcolorccc">团号</td>
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
					预<br />计<br />收<br />款
				</div>
				<table width="670" border="0" cellspacing="0" cellpadding="0" bgcolor="#cccccc" style="float:left;">
					<tr class="text_normal ie7shiying"  style="border-top:none;">
						<td width="51" height="10" align="center" class="text_normal bgcolorccc">销售</td>
						<td align="center" bgcolor="#F5F5F5" class="text_normal bgcolorccc">客户单位</td>
						<td width="50" align="center" class="text_normal bgcolorccc">人数</td>
						<td width="100" align="center" class="text_normal bgcolorccc">预计收款</td>
						<td width="100" align="center" class="text_normal bgcolorccc">预计退款</td>
						<td width="100" align="center" class="text_normal bgcolorccc">实际收款</td>
						<td width="100" align="center" class="text_normal bgcolorccc">未收款项</td>
					</tr>
					<c:forEach items="${vo.expectIncome}" var="income">
					<tr>
						<td height="10" align="center" bgcolor="#FFFFFF" class="text_normal danhangnormal">
							<c:if test="${not empty income.saler}">${income.saler}</c:if>
						</td>
						<td bgcolor="#FFFFFF" class="text_normal danhangnormal">${income.agentName}</td>
						<td align="center" bgcolor="#FFFFFF" class="text_normal">${income.orderPersonNum}</td>
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
						<td align="center" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">${vo.orderPersonNumSum}</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">¥${vo.totalMoneySum}</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">¥${vo.backMoneySum}</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">¥${vo.accountedMoneySum}</td>
						<td align="right" bgcolor="#FFFFFF" class="text_normal" style="border-bottom:none;">¥${vo.notAccountedMoneySum}</td>
					</tr>
				</table>
				<div class="clear"></div>
			</div>
			<div class="danwkk">
				<div class="leftmenu" id="leftmenu2">
					预<br />计<br />境<br />内<br />付<br />款
				</div>
				<table width="670" border="0" cellspacing="0" cellpadding="0" bgcolor="#cccccc" style="float:left;">
					<tr style="border-top:none;"  class="ie7shiying">
						<td width="215" height="10" align="center" class="text_normal bgcolorccc">项目</td>
						<td align="center" class="text_normal bgcolorccc">地接社/渠道商</td>
						<td width="100" align="center" class="text_normal bgcolorccc">单价</td>
						<td width="100" align="center" class="text_normal bgcolorccc">数量</td>
						<td width="100" align="center" class="text_normal bgcolorccc">金额</td>
					</tr>
					<c:forEach items="${vo.actualInList}" var="record">
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
						</tr>
					</c:forEach>
					<c:if test="${fn:length(vo.actualInList)<4}">
						<c:forEach begin="1" end="${4-fn:length(vo.actualInList)}">
						<tr>
							<td height="10" align="center" bgcolor="#FFFFFF" class="text_normal duohangnormal">&nbsp;</td>
							<td align="left" bgcolor="#FFFFFF" class="text_normal duohangnormal"><div class="w334">&nbsp;</div></td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
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
					预<br />计<br />境<br />外<br />付<br />款
				</div>
				<table width="670" border="0" cellspacing="0" cellpadding="0" bgcolor="#cccccc" style="float:left;">
					<tr class="text_normal ie7shiying"  style="border-top:none;">
						<td height="10" align="center" class="text_normal bgcolorccc">地接社/渠道商</td>
						<td width="45" align="center" class="text_normal bgcolorccc">币种</td>
						<td width="45" align="center" class="text_normal bgcolorccc">汇率</td>
						<td width="100" align="center" class="text_normal bgcolorccc">外币</td>
						<td width="100" align="center" class="text_normal bgcolorccc">金额</td>
					</tr>
					<c:forEach items="${vo.actualOutList}" var="outList"> 
						<tr>
							<td height="10" align="left" bgcolor="#FFFFFF" class="text_normal danhangnormal"><div class="w364">${outList.supplyName}</div></td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">${outList.name}</td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">
								<fmt:formatNumber minFractionDigits="3" maxFractionDigits="3" type="number" pattern="#,##0.000" value="${outList.rate}"/>
							</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">${outList.formatPrice}</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${outList.formatPriceAfter}</td>
						</tr>
					</c:forEach>
					<c:if test="${fn:length(vo.actualOutList)<4}">
						<c:forEach begin="1" end="${4-fn:length(vo.actualOutList)}"> 
						<tr>
							<td height="10" align="left" bgcolor="#FFFFFF" class="text_normal danhangnormal"><div class="w364">&nbsp;</div></td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="center" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
							<td align="right" bgcolor="#FFFFFF" class="text_normal">&nbsp;</td>
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
					<td height="10" align="center" class="text_normal bgcolorccc">预计收入合计</td>
					<td align="center" class=" bgcolorccc">预计退款合计</td>
					<td align="center" class=" bgcolorccc">预计实际收入</td>
					<td align="center" class=" bgcolorccc">预计支出合计</td>
					<td align="center" class="text_normal bgcolorccc">预计毛利</td>
					<td align="center" class="text_normal bgcolorccc">预计毛利率</td>
				</tr>
				<tr>
					<td height="10" align="right" bgcolor="#FFFFFF" class="text_normal">¥${vo.totalMoneySum}</td>
					<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${vo.backMoneySum}</td>
					<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${vo.realMoneySum}</td>
					<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${vo.outMoneySum}</td>
					<td align="right" bgcolor="#FFFFFF" class="text_normal">¥${vo.profitSum}</td>
					<td align="center" bgcolor="#FFFFFF" class="text_normal">${vo.profitRate}</td>
				</tr>
			</table>
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
							<c:if test="${'00' eq forcastStatus}">
								<shiro:hasPermission name="forecast:remark:edit">
									<div align="right" class="text_little_title print_disappear">
										<a id="editRemark" href="javascript:void" onclick="javascript:editRemark(this)">编辑</a>
									</div>
								</shiro:hasPermission>
								<div id="saveAndCancelEdit" align="right" style="display:none;" class="text_little_title bgcolorccc print_disappear">
									<a href="javascript:void" onclick="javascript:saveRemark('${activityIdOrUUID}',${orderType})">保存</a>&nbsp;
									<a href="javascript:void" onclick="javascript:cancelRemark('${vo.remark}')">取消</a>
								</div>
							</c:if>
						</div>						
					</td>
				</tr>
				<tr height="10" bgcolor="#FFFFFF">
					<td width="685" height="65">
						<div id="remarkTextDiv" style="width:100%;float:left">
							<textarea id="remark" disabled="disabled" style="width:98%; height:40px" maxlength="100" class="noborder print_expand"  onkeyup="javascript:setRemarkCount(this)">${vo.remark}</textarea>
						</div>
						<div id="remarkCountDiv" style="width:3%;float:right;display:none">
							<span id="remarkCount" style="width:40px; height:30px; line-height:65px; text-align:center; float:right;" class="print_disappear"></span>
						</div>
					</td>
				</tr>
			</table>
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
		<shiro:hasPermission name="forcast:lock && forcast:unlock">
			<c:if test="${'00' eq forcastStatus }">
					<input id="" name="" type="button" class="ydbz_s" value="锁定" onclick="javascript:locker('${activityIdOrUUID}',${orderType },'${forcastStatus }',this);">
			</c:if>
			<c:if test="${'10' eq forcastStatus }">
					<input id="" name="" type="button" class="ydbz_s" value="解锁" onclick="javascript:locker('${activityIdOrUUID}',${orderType },'${forcastStatus }',this);">
			</c:if>
		</shiro:hasPermission>
		<shiro:hasPermission name="forcast:lock && not forcast:unlock">
			<c:if test="${'00' eq forcastStatus }">
					<input id="" name="" type="button" class="ydbz_s" value="锁定" onclick="javascript:locker('${activityIdOrUUID}',${orderType },'${forcastStatus }',this);">
			</c:if>
			<c:if test="${'10' eq forcastStatus}">
					<input id="" name="" type="button" disabled="disabled" class="ydbz_s gray" value="解锁">
			</c:if>
		</shiro:hasPermission>
		<shiro:hasPermission name="forcast:unlock && not forcast:lock">
			<c:if test="${'00' eq forcastStatus}">
					<input id="" name="" type="button" class="ydbz_s gray" disabled="disabled" value="锁定">
			</c:if>
			<c:if test="${'10' eq forcastStatus }">
					<input id="" name="" type="button" class="ydbz_s" value="解锁" onclick="javascript:locker('${activityIdOrUUID}',${orderType },'${forcastStatus }',this);">
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
		$(function(){
			setTimeout('leftmenu()',50);
		})
		function leftmenu(){
			var h1=$('#leftmenu1').parent().height();
			var h2=$('#leftmenu2').parent().height();
			var h3=$('#leftmenu3').parent().height();
			$('#leftmenu1').css('padding-top',(h1-2-76)/2+'px');
			$('#leftmenu2').css('padding-top',(h2-2-114)/2+'px');
			$('#leftmenu3').css('padding-top',(h3-2-114)/2+'px');
		}
		
		function download(){
			window.location.href="${ctx}/cost/manager/downLoadForcastList?activityId=${activityIdOrUUID}&orderType=${orderType}&type=0";
		}
	</script>
</body>
</html>