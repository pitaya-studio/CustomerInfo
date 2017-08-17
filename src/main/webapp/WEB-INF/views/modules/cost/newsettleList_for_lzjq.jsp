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
<%--<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/dayinbdzy.css" />--%>
<style type="text/css">
	.dayinzys {
		width: 700px;
		/*line-height: 40px;*/
		margin: 0 auto;
		border-collapse: collapse;
		margin-top: 20px;
		white-space: nowrap;
		table-layout: fixed;
		/*font-size: 14px;*/
	}
	.dbaniu {
		overflow: hidden;
		margin-top: 50px;
		margin-right: auto;
		margin-bottom: 50px;
		margin-left: auto;
		text-align: center;
		width:190px;
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

	.main-list{
		border-collapse: collapse;
		border-top: 1px solid gray;
		border-left: 1px solid gray;
		text-align: center;
		width: 700px;
		margin: 0 auto;
		font-family: "Microsoft YaHei UI", arial;
		font-size: 14px;
		color: #555;
	}
	.main-list tr td{
		height: 20px;
		border-right: 1px solid gray;
		border-bottom: 1px solid gray;
		empty-cells: show;
	}
	.main-list tr td.remove-border{
		border-bottom: none;
		padding: 0px;
	}
	.main-list tr td.remove-border td.no-right{
		border-right: none;
	}
	.main-list tr td.nav-info{
		line-height: 20px;
		background-color: #eee;
		overflow: hidden;
		white-space: nowrap;
		table-layout: fixed;
		padding: 0px 5px;
	}
	.sub-list{
		width: 100%;
		border-collapse: collapse;
	}
	.sub-list tr td{
		border-right: 1px solid gray;
		border-bottom: 1px solid gray;
		white-space: normal;
		word-break: break-all;
	}
	h2{
		margin:20px auto;
		width:700px;
		text-align:center;
	}
	.border_R_N{
		border-right: none;
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
			<h2 class="text_title" style="margin-bottom: 20px;">骡子假期团队结算单</h2>
			<table class="main-list">
				<tr>
					<td  class="nav-info">团号</td>
					<td colspan="5">${vo.groupCode}</td>
				</tr>
				<tr>
					<td class="nav-info">所属部门</td>
					<td colspan="5">骡子假期</td>
				</tr>
				<tr>
					<td class="nav-info">旅游线路</td>
					<td colspan="5">${vo.productName}</td>
				</tr>
				<tr>
					<td class="nav-info">出团日期</td>
					<td>${vo.groupOpenDate}</td>
					<td class="nav-info">截团日期</td>
					<td>${vo.groupCloseDate}</td>
					<td class="nav-info">操作姓名</td>
					<td>${vo.createBy}</td>
				</tr>
				<tr>
					<td class="nav-info">团队类型</td>
					<td>${vo.orderType}</td>
					<td class="nav-info">出团人数</td>
					<c:choose>
						<c:when test="${vo.orderPersonNumSum > 10}">
							<td>${vo.orderPersonNumSum}+1</td>
						</c:when>
						<c:otherwise>
							<td>${vo.orderPersonNumSum}</td>
						</c:otherwise>
					</c:choose>
					<td class="nav-info">领队姓名</td>
					<td>${vo.grouplead}</td>
				</tr>
				<tr>
					<td class="nav-info">实收总额</td>
					<td colspan="2">¥${vo.realMoneySum}</td>
					<td class="nav-info">实付总额</td>
					<td colspan="2">¥${vo.outMoneySum}</td>
				</tr>
				<tr>
					<td class="nav-info">同行价</td>
					<td class="nav-info">不降价价格</td>
					<td>${vo.settlementAdultPrice}</td>
					<td class="nav-info">降价价格</td>
					<td colspan="2"></td>
				</tr>
				<tr>
					<td colspan="6" class="nav-info">结算收入</td>
				</tr>
				<tr>
					<td colspan="6"   class="remove-border">
						<table class="sub-list">
							<thead>
							<tr>
								<td  class="nav-info">组团社</td>
								<td  class="nav-info">单价</td>
								<td  class="nav-info">人数</td>
								<td  class="nav-info">金额</td>
								<td  class="nav-info">销售姓名</td>
								<td  class="nav-info no-right">备注</td>
							</tr>
							</thead>
							<tbody>
							<c:set var="incomeItemNum" value="0"/> <%--用于统计收款明细项目的数量--%>
							<c:forEach items="${vo.expectIncome}" var="income">
								<c:if test="${income.adultNum gt 0}">
									<tr>
										<td>${income.agentName}</td>
										<td>¥${income.adultPrice}</td>
										<td>${income.adultNum}</td>
										<td>¥${income.adultMoney}</td>
										<td>${income.saler}</td>
										<td class="no-right">${income.adultRemark }</td>
									</tr>
									<c:set var="incomeItemNum" value="${incomeItemNum + 1}"/>
								</c:if>
								<c:if test="${income.adultNum eq 0}">
									<c:if test="${income.adultPersonCount gt 0 or income.adultZGPersonCount gt '0' }">
										<tr>
											<td>${income.agentName}</td>
											<td>¥${income.adultPrice}</td>
											<td>${income.adultNum}</td>
											<td>¥${income.adultMoney}</td>
											<td>${income.saler}</td>
											<td class="no-right">${income.adultRemark }</td>
										</tr>
										<c:set var="incomeItemNum" value="${incomeItemNum + 1}"/>
									</c:if>
								</c:if>
								<c:if test="${income.childrenNum gt 0}">
									<tr>
										<td>${income.agentName}</td>
										<td>¥${income.childrenPrice}</td>
										<td>${income.childrenNum}</td>
										<td>¥${income.childrenMoney}</td>
										<td>${income.saler}</td>
										<td class="no-right">${income.childrenRemark }</td>
									</tr>
									<c:set var="incomeItemNum" value="${incomeItemNum + 1}"/>
								</c:if>
								<c:if test="${income.childrenNum eq 0}">
									<c:if test="${income.childrenPersonCount gt 0 or income.childrenZGPersonCount gt 0 }">
										<tr>
											<td>${income.agentName}</td>
											<td>¥${income.childrenPrice}</td>
											<td>${income.childrenNum}</td>
											<td>¥${income.childrenMoney}</td>
											<td>${income.saler}</td>
											<td class="no-right">${income.childrenRemark }</td>
										</tr>
										<c:set var="incomeItemNum" value="${incomeItemNum + 1}"/>
								  </c:if>
								</c:if>	
								<c:if test="${income.specialNum gt 0}">
									<tr>
										<td>${income.agentName}</td>
										<td>¥${income.specialPrice}</td>
										<td>${income.specialNum}</td>
										<td>¥${income.specialMoney}</td>
										<td>${income.saler}</td>
										<td class="no-right">${income.specialRemark }</td>
									</tr>
									<c:set var="incomeItemNum" value="${incomeItemNum + 1}"/>
								</c:if>
								<c:if test="${income.specialNum eq 0}">
									<c:if test="${income.specialPersonCount gt 0 or income.specialZGPersonCount gt 0 }">
										<tr>
											<td>${income.agentName}</td>
											<td>¥${income.specialPrice}</td>
											<td>${income.specialNum}</td>
											<td>¥${income.specialMoney}</td>
											<td>${income.saler}</td>
											<td class="no-right">${income.specialRemark }</td>
										</tr>
										<c:set var="incomeItemNum" value="${incomeItemNum + 1}"/>
									</c:if>
								</c:if>	
							</c:forEach>
							<c:if test="${incomeItemNum lt 3}"> <%--空白行填充--%>
								<c:forEach begin="1" end="${3 - incomeItemNum}">
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td class="no-right">&nbsp;</td>
									</tr>
								</c:forEach>
							</c:if>
							<tr>
								<td  colspan="2" class="nav-info">实收合计</td>
								<td>${vo.orderPersonNumSum}</td>
								<td>¥${vo.totalMoneySum}</td>
								<td  colspan="2" class="no-right">${vo.groupRefundSum }</td>
							</tr>
							</tbody>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="6" class="nav-info">其他收入</td>
				</tr>
				<tr>
					<td colspan="6"   class="remove-border">
						<table class="sub-list">
							<thead>
							<tr>
								<td  class="nav-info">项目</td>
								<td  class="nav-info">金额</td>
								<td  class="nav-info">渠道/地接社</td>
								<td  class="nav-info no-right">备注</td>
							</tr>
							</thead>
							<tbody>
							<c:set var="incomeItemNum" value="0"/> <%--用于统计收款明细项目的数量--%>
							<c:forEach items="${vo.otherRecordList}" var="income">
									<tr>
										<td>${income.name}</td>
										<td>¥${income.totalMoney}</td>
										<td>${income.supplyName}</td>
										<td class="no-right">${income.comment}</td>
									</tr>
							</c:forEach>
							<c:if test="${fn:length(vo.otherRecordList) < 3 }">
								<c:forEach begin="1" end="${3 - fn:length(vo.otherRecordList)}">
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td class="no-right">&nbsp;</td>
									</tr>
								</c:forEach>
							</c:if>
							<tr>
								<td  colspan="1" class="nav-info">实收合计</td>
								<td>¥${vo.otherSum}</td>
								<td  colspan="2" class="no-right"></td>
							</tr>
							<tr>
								<td  colspan="1" class="nav-info">实收总计</td>
								<td colspan="3" class="no-right">¥${vo.realMoneySum}</td>
							</tr>
							</tbody>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="6" class="nav-info">结算支出</td>
				</tr>
				<tr>
					<td colspan="6"  class="remove-border">
						<table  class="sub-list">
							<thead>
							<tr>
								<td class="nav-info">项目</td>
								<td class="nav-info">单价</td>
								<td class="nav-info">人数</td>
								<td class="nav-info">金额</td>
								<td class="nav-info">渠道/地接社</td>
								<td class="nav-info no-right">备注</td>
							</tr>
							</thead>
							<tbody>
							<c:set var="recordItemNum" value="0"/> <%--用于统计结算支出项目的数量--%>
							<c:forEach items="${vo.actualInList}" var="record"> <%--境内--%>
								<tr>
									<td>${record.name}</td>
									<td>¥${record.formatPrice}</td>
									<td>
										<c:if test="${not empty record.quantity}">${record.quantity}</c:if>
										<c:if test="${empty record.quantity}">0</c:if>
									</td>
									<td>¥${record.formatPriceAfter}</td>
									<td>${record.supplyName}</td>
									<td class="no-right">${record.comment}</td>
								</tr>
								<c:set var="recordItemNum" value="${recordItemNum + 1}"/>
							</c:forEach>
							<c:forEach items="${vo.actualOutList}" var="record"> <%--境外--%>
								<tr>
									<td>${record.name}</td>
									<td><c:if test="${record.formatPrice ne null}">¥</c:if>${record.formatPrice}</td>
									<td>
										<c:if test="${not empty record.quantity}">${record.quantity}</c:if>
										<c:if test="${empty record.quantity && record.formatPrice ne null}">0</c:if>
									</td>
									<td>¥${record.formatPriceAfter}</td>
									<td>${record.supplyName}</td>
									<td class="no-right">${record.comment}</td>
								</tr>
								<c:set var="recordItemNum" value="${recordItemNum + 1}"/>
							</c:forEach>
							<c:if test="${recordItemNum lt 3}"> <%--空白行填充--%>
								<c:forEach begin="1" end="${3 - recordItemNum}">
									<tr>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
										<td class="no-right">&nbsp;</td>
									</tr>
								</c:forEach>
							</c:if>
							<tr>
								<td  colspan="3" class="nav-info">实付合计</td>
								<td>¥${vo.recordMoneySum}</td>
								<td  colspan="2" class="no-right"></td>
							</tr>
							<tr>
								<td  colspan="3" class="nav-info">结算利润</td>
								<td>¥${vo.profitSum}</td>
								<td  colspan="2" class="no-right"></td>
							</tr>
							<tr>
								<td  colspan="3" class="nav-info">结算毛利</td>
								<td>${vo.profitRate}</td>
								<td  colspan="2" class="no-right"></td>
							</tr>
							<tr>
								<td class="nav-info">制表人</td>
								<td colspan="2"></td>
								<td class="nav-info">日期</td>
								<td colspan="2" class="no-right"></td>
							</tr>
							<tr>
								<td class="nav-info">部门经理</td>
								<td colspan="2"></td>
								<td class="nav-info">日期</td>
								<td colspan="2" class="no-right"></td>
							</tr>
							</tbody>
						</table>
					</td>
				</tr>
			</table>
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
