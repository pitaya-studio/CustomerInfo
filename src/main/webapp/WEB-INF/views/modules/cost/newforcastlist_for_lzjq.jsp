<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>财务-成本管理-预报单</title>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" /> 
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/table-forcast.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" /> 
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
	.sub-list{
		width: 100%;
		border-collapse: collapse;
	}
    .sub-list tr td{
        word-break: break-all;
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
	</script>
  </head>
  
  <body>
  	<div id="printDiv">
    <h2>骡子假期团队预报单</h2>
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
			<td>${vo.groupOpenDate }</td>
			<td class="nav-info">截团日期</td>
			<td>${vo.groupCloseDate }</td>
			<td class="nav-info">操作姓名</td>
			<td>${vo.createBy}</td>
		</tr>
		<tr>
			<td class="nav-info">团队类型</td>
			<td>${vo.orderType }</td>
			<td class="nav-info">出团人数</td>
			<td>
				<c:choose>
					<c:when test="${vo.orderPersonNumSum > 10 }">${vo.orderPersonNumSum}+1</c:when>
					<c:otherwise>${vo.orderPersonNumSum}</c:otherwise>
				</c:choose>
			</td>
			<td class="nav-info">领队姓名</td>
			<td>${vo.grouplead}</td>
		</tr>
		<tr>
			<td class="nav-info">应收总额</td>
			<td colspan="2">¥${vo.realMoneySum}</td>
			<td class="nav-info">应付总额</td>
			<td colspan="2">¥${vo.outMoneySum}</td>
		</tr>
		<tr>
			<td class="nav-info">同行价</td>
			<td class="nav-info">不降价价格</td>
			<td>¥${vo.settlementAdultPrice }</td>
			<td class="nav-info">降价价格</td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td colspan="6" class="nav-info">预算收入</td>
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
						<c:forEach items="${vo.expectIncome }" var="income">
							<tr>
								<td>${income.agentName }</td>
								<td>¥${income.price }</td>
								<td>
									${income.personNum }
								</td>
								<td>¥${income.totalPrice }</td>
								<td>${income.saler }</td>
								<td class="no-right">
									${income.remark }
								</td >
							</tr>
						</c:forEach>
						<c:if test="${fn:length(vo.expectIncome) < 3 }">
							<c:forEach begin="1" end="${3 - fn:length(vo.expectIncome)}">
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td class="no-right"></td>
								</tr>
							</c:forEach>
						</c:if>
						<tr>
							<td colspan="2" class="nav-info">应收合计</td>
							<td>${vo.orderPersonNumSum}</td>
							<td>¥${vo.totalMoneySum}</td>
							<td colspan="2" class="no-right">${vo.groupRefundSum }</td>
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
						<c:forEach items="${vo.otherRecordList }" var="income">
							<tr>
								<td>${income.name}</td>
								<td>¥${income.totalMoney}</td>
								<td>${income.agentName}</td>
								<td class="no-right">${income.comment}</td>
							</tr>
						</c:forEach>
						<c:if test="${fn:length(vo.otherRecordList) < 3 }">
							<c:forEach begin="1" end="${3 - fn:length(vo.otherRecordList)}">
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td class="no-right"></td>
								</tr>
							</c:forEach>
						</c:if>
						<tr>
							<td colspan="1" class="nav-info">应收合计</td>
							<td>¥${vo.otherSum}</td>
							<td colspan="3" class="no-right"></td>
						</tr>
						<tr>
							<td colspan="1" class="nav-info">应收总计</td>
							<td colspan="3 " class="no-right">¥${vo.realMoneySum}</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="6" class="nav-info">预算支出</td>
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
						<c:forEach items="${vo.actualInList }" var="record">
							<tr>
								<td>${record.name}</td>
								<td>¥${record.formatPrice}</td>
								<td>
									<c:if test="${not empty record.quantity}">${record.quantity}</c:if>
									<c:if test="${empty record.quantity}">0</c:if>
								</td>
								<td>¥${record.formatPriceAfter}</td>
								<td>${record.supplyName}</td>
								<td class="no-right">${record.comment }</td>
							</tr>
						</c:forEach>
						<c:forEach items="${vo.actualOutList }" var="record">
							<tr>
								<td>${record.name}</td>
								<td><c:if test="${record.formatPrice ne null }">¥</c:if>${record.formatPrice}</td>
								<td>
									<c:if test="${not empty record.quantity}">${record.quantity}</c:if>
									<c:if test="${empty record.quantity && record.formatPrice ne null}">0</c:if>
								</td>
								<td>¥${record.formatPriceAfter}</td>
								<td>${record.supplyName}</td>
								<td class="no-right">${record.comment }</td>
							</tr>
						</c:forEach>
						<c:if test="${fn:length(vo.actualInList) + fn:length(vo.actualOutList) < 3 }">
							<c:forEach begin="1" end="${3 - (fn:length(vo.actualInList) + fn:length(vo.actualOutList)) }">
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td class="no-right"></td>
								</tr>
							</c:forEach>
						</c:if>
						<tr>
							<td  colspan="3" class="nav-info">应付合计</td>
							<td>¥${vo.totalExpenditureMoneySum}</td>
							<td  colspan="2" class="no-right"></td>
						</tr>
						<tr>
							<td  colspan="3" class="nav-info">预算利润</td>
							<td>¥${vo.profitSum}</td>
							<td  colspan="2" class="no-right"></td>
						</tr>
						<tr>
							<td  colspan="3" class="nav-info">预算毛利</td>
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
			window.location.href="${ctx}/cost/manager/downLoadForcastList?activityId=${activityIdOrUUID}&orderType=${orderType}&type=0"
		}
	</script>
</body>
</html>
