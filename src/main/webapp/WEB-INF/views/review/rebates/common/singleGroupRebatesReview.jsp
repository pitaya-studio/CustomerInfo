<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
     <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费-->
	<c:choose>
		<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
		    <title>宣传费-审批</title>
		</c:when>
		<c:otherwise>
		    <title>返佣-审批</title>
		</c:otherwise>
	</c:choose> 
    
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<style type="text/css">
		.rabateInfo {
			margin-left: 50px;
		}
	</style>
	<script type="text/javascript">
	function jbox_bohui(rid){
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" maxlength="100" oninput="this.value=this.value.substring(0,100)"></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v == "1"){
				$.ajax({
		            type: "POST",
		            url: "${ctx}/newRebatesReview/rebatesReview",
		            data: {
			            reviewId: rid,
			            result: 0,
			            denyReason: $("#denyReason").val()		            
		            },
		            success: function(data){
		            	if ("success" == data.result) {
		            		$("input[name='review']").attr('disabled',"true");
	           				$("input[name='bohui']").attr('disabled',"true");
	           				window.opener.location.href = window.opener.location.href;
		            		window.close();
		            	} else {
		            		jBox.tip("操作失败");
		            	}
		            }
		        });
			}
		},height:250,width:500});
	}
	function reviewPass(rid){
		var html = '<div class="add_allactivity"><label>请填写您的备注!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" maxlength="100" oninput="this.value=this.value.substring(0,100)"></textarea>';
		html += '</div>';
		$.jBox(html, { title: "备注",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v == "1"){
				$.ajax({
		            type: "POST",
		            url: "${ctx}/newRebatesReview/rebatesReview",
		            data: {
			            reviewId: rid,
			            result: 1,
			            denyReason: $("#denyReason").val()		            
		            },
		            success: function(data){
		            	if ("success" == data.result) {
		            		$("input[name='review']").attr('disabled',"true");
	           				$("input[name='bohui']").attr('disabled',"true");
	           				window.opener.location.href = window.opener.location.href;
		            		window.close();
		            	} else {
		            		jBox.tip("操作失败");
		            	}
		            }
		        });
			}
		},height:250,width:500});
	}
	</script>
</head>
<body>
	<!-- 顶部参数 -->
    <page:applyDecorator name="show_head">
    	 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费-->
		<c:choose>
			<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			    <page:param name="desc">宣传费审批</page:param>
			</c:when>
			<c:otherwise>
			    <page:param name="desc">返佣审批</page:param>
			</c:otherwise>
		</c:choose> 
	</page:applyDecorator>
	<!--右侧内容部分开始-->
		 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费-->
		<c:choose>
			<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			   <div class="mod_nav">审批 > 宣传费申请 > 宣传费审批</div>
			</c:when>
			<c:otherwise>
			   <div class="mod_nav">审批 > 返佣申请 > 返佣审批</div>
			</c:otherwise>
		</c:choose> 
    
	<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>

	<%--返佣对象信息--%>
	<c:if test="${not empty multiRebateObject and multiRebateObject eq true and not empty reviewInfos}">
		<div class="ydbz_tit">
			 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费-->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				   <span class="fl">宣传费对象</span>
				</c:when>
				<c:otherwise>
				   <span class="fl">返佣对象</span>
				</c:otherwise>
			</c:choose> 
		</div>
		<div>
			<span>
				<label>对象类型：</label>
				<c:choose>
					<c:when test="${reviewInfos.relatedObjectType eq 2}"><span>供应商</span></c:when>
					<c:otherwise><span>渠道</span></c:otherwise>
				</c:choose>
			</span>
			<span class="rabateInfo">
				<c:choose>
					<c:when test="${reviewInfos.relatedObjectType eq 2}">
						<label>供应商名称：</label>
						<span>${reviewInfos.relatedObjectName}</span>
					</c:when>
					<c:otherwise>
						<label>渠道名称：</label>
						<span>${reviewInfos.agentName}</span>
					</c:otherwise>
				</c:choose>
			</span>
			<c:if test="${reviewInfos.relatedObjectType eq 2}">
				<span class="rabateInfo">
					<label>账户类型：</label>
					<c:choose>
						<c:when test="${reviewInfos.rebatesObjectAccountType eq '2'}"><span>境外账户</span></c:when>
						<c:when test="${reviewInfos.rebatesObjectAccountType eq '1'}"><span>境外账户</span></c:when>
					</c:choose>
				</span>
				<span class="rabateInfo">
					<label>开户行名称：</label>
					<span>${reviewInfos.rebatesObjectAccountBank}</span>
				</span>
				<span class="rabateInfo">
					<label>账户号码：</label>
					<span>${reviewInfos.rebatesObjectAccountCode}</span>
				</span>
			</c:if>
		</div>
	</c:if>

	<div class="ydbz_tit"><span class="fl"><c:if test="${not empty rebates.traveler}">个人</c:if><c:if test="${empty rebates.traveler}">团队</c:if>
	 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费-->
	<c:choose>
		<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
		    宣传费
		</c:when>
		<c:otherwise>
		    返佣
		</c:otherwise>
	</c:choose> 
	</span></div>
	<table id="contentTable" class="activitylist_bodyer_table">
	   	<thead>
		  <tr>
			 <th width="8%">姓名</th>
	         <th width="12%">币种</th>
	         <th width="11%">款项</th>
	          <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费-->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				  <th width="12%">预计宣传费金额</th>
				  <th width="12%">原宣传费金额</th>
				  <th width="13%">宣传费差额</th>
				</c:when>
				<c:otherwise>
				  <th width="12%">预计返佣金额</th>
				  <th width="12%">原返佣金额</th>
				  <th width="13%">返佣差额</th>
				</c:otherwise>
			</c:choose> 
			 <th width="20%">备注</th>
<!-- 	         <th width="13%">改后返佣金额</th> -->
		  </tr>
	  	</thead>
	   	<tbody>
		  <tr>
			 <td rowspan="2"><c:if test="${not empty rebates.traveler}">${rebates.traveler.name}</c:if><c:if test="${empty rebates.traveler}">团队</c:if></td>
             <td>${rebates.currency.currencyName}</td>
			 <td class="tr">${rebates.costname}</td>
			 <td class="tr">
			 	<c:if test="${not empty rebatesStr}">${rebatesStr}</c:if>
			 	<c:if test="${empty rebatesStr}">-</c:if>
			 </td>
			 <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.oldRebates,2)}</td>
             <td class="tr">${rebates.currency.currencyMark}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></td>
			 <td>${rebates.remark}</td>
		  </tr>
	   </tbody>
	</table>
	<div class="allzj tr f18">
		  <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费-->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				 	原宣传费金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.oldRebates)}<br />
      				宣传费差额：<font class="f14">${rebates.currency.currencyName}</font><span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></span><br />
				</c:when>
				<c:otherwise>
				  	原返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.oldRebates)}<br />
      				返佣差额：<font class="f14">${rebates.currency.currencyName}</font><span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></span><br />
				</c:otherwise>
			</c:choose> 
	</div>
	<div class="ydbz_tit">
		<span class="fl">审批动态</span>
	</div>
	<c:set var="rid" value="${rebates.review.id}"></c:set>
	<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>
	<div class="dbaniu" style="width:230px;">
		<a class="ydbz_s" href="javascript:window.close();">关闭</a>
		<input type="button" name="bohui" value="驳回" class="btn btn-primary" onclick="jbox_bohui('${rebates.review.id}');">
		<input type="button" name="review" value="审批通过" class="btn btn-primary" onclick="reviewPass('${rebates.review.id}');">
	</div>
	<!--右侧内容部分结束-->
</body>
</html>