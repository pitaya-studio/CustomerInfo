<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
	<head>
		<c:choose>
			<c:when test="${not empty isReview and isReview}">
				<title>转团-审批</title>
			</c:when>
			<c:otherwise>
				<title>转团-详情</title>
			</c:otherwise>
		</c:choose>	
		<meta name="decorator" content="wholesaler"/>
		<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
		<script src="${ctxStatic}/modules/order/transfergroup/transferGroupApply.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(function(){
				//搜索条件筛选
				launch();
				//操作浮框
				operateHandler();
				//团号和产品切换
				switchNumAndPro();
			});
			// 跳转到产品详情
			function gotoProduct(productId) {
				window.open(contextPath + "/activity/manager/detail/" + productId + "?isOp=0");
			}
			
			function jbox_bohui(rid){
				var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
				html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" maxlength="200"></textarea>';
				html += '</div>';
				$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
					if (v == "1"){
						$.ajax({
				            type: "POST",
				            url: "${ctx}/newTransferGroupReview/transferGroupReview",
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
				            		jBox.tip(data.msg);
				            	}
				            }
				        });
					}
				},height:250,width:500});
			}
			function reviewPass(rid){
				var html = '<div class="add_allactivity"><label>请填写您的备注!</label>';
				html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" maxlength="200"></textarea>';
				html += '</div>';
				$.jBox(html, { title: "备注",buttons:{"提 交":"1"}, submit:function(v, h, f){
					if (v == "1"){
						$.ajax({
				            type: "POST",
				            url: "${ctx}/newTransferGroupReview/transferGroupReview",
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
				            		top.$.jBox.tip(data.msg, "warning");
				            	}
				            }
				        });
					}
				},height:250,width:500});
			}
		</script>
		<style>
			.inner_cont{
				overflow: hidden;
				text-overflow:ellipsis;
				white-space: nowrap;
			}
		</style>
	</head>
<body>
	<page:applyDecorator name="show_head">
    	<page:param name="desc">转团详情</page:param>
	</page:applyDecorator>
	<!--右侧内容部分开始-->
	<div class="mod_nav"> 订单 > ${orderStatus} > 转团详情</div>
		<div class="ydbzbox fs">
			<div class="orderdetails">
				<div class="orderdetails_tit">订单信息</div>
				<div class="orderdetails1">
					<table border="0" style="margin-left: 25px" width="98%">
						<tbody>
							<tr>
								<td class="mod_details2_d1">下单人：</td>
						        <td class="mod_details2_d2">${order.createBy.name}</td>
						        <td class="mod_details2_d1">销售：</td>
						        <td class="mod_details2_d2">${order.salerName}</td>
								<td class="mod_details2_d1">下单时间：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
                                <td class="mod_details2_d1">团队类型：</td>
                                <td class="mod_details2_d2">
                                	<c:if test="${order.orderStatus==1}">单团</c:if>
                                	<c:if test="${order.orderStatus==2}">散拼</c:if>
                                	<c:if test="${order.orderStatus==3}">游学</c:if>
                                	<c:if test="${order.orderStatus==4}">大客户</c:if>
                                	<c:if test="${order.orderStatus==5}">自由行</c:if>
                                </td>
							</tr>
							<tr> 
                                <td class="mod_details2_d1">订单编号：</td>
								<td class="mod_details2_d2">${order.orderNum }</td>
                                <td class="mod_details2_d1">订单团号：</td>
								<td class="mod_details2_d2" id="oldGroupCode">${group.groupCode}</td>
                                <td class="mod_details2_d1">订单总额：</td>
								<td class="mod_details2_d2"><em class="tdred">${totalMoney}</em></td>
                                <td class="mod_details2_d1">订单状态：</td>
								<td class="mod_details2_d2">${fns:getDictLabel(order.payStatus, "order_pay_status", "")}</td>	 
							</tr>
							<tr>
								<td class="mod_details2_d1">操作人：</td>
						        <td class="mod_details2_d2">${product.createBy.name}</td>     
							</tr>
						</tbody>
					</table>
				</div>
				<div class="orderdetails_tit">产品信息</div>
				<div class="orderdetails2">
					<p class="ydbz_mc">${product.acitivityName}</p>
					<ul class="ydbz_info">
						<li><span>出发城市：</span>${fns:getDictLabel(product.fromArea, "from_area", "")}</li>
						<li><span>出团日期：</span><fmt:formatDate value="${group.groupOpenDate}" pattern="yyyy-MM-dd HH:mm:ss"/></li>
						<li><span>行程天数：</span>${product.activityDuration }天</li>
						<li><span>离境城市：</span>${outArea.name}</li>
						<li><span></span></li>
						<li id="mddtargetAreaNames" title="${product.targetAreaNames}" class="orderdetails2_text"><span >目的地：</span>
							${product.targetAreaNames}
						</li>
					</ul>
					<ul class="ydbz_dj specialPrice">
						<li>
							<span class="ydtips">单价</span>
							<p>成人：${settlementAdultPrice}</p>
	                        <p>儿童：${settlementcChildPrice}</p>
	                        <p>特殊人群：${settlementSpecialPrice}</p>
						</li>
						<li>
							<span class="ydtips"> 出行人数</span>
							<p>成人：<span> <c:if test="${not empty order.orderPersonNumAdult }">${order.orderPersonNumAdult }</c:if> 
                        									<c:if test="${empty order.orderPersonNumAdult }">0</c:if> </span> 人</p>
							<p>儿童：<span> <c:if test="${not empty order.orderPersonNumChild }">${order.orderPersonNumChild }</c:if> 
                        									<c:if test="${empty order.orderPersonNumChild }">0</c:if></span> 人</p>
							<p>特殊人群：<span><c:if test="${not empty order.orderPersonNumSpecial }">${order.orderPersonNumSpecial }</c:if> 
                        									<c:if test="${empty order.orderPersonNumSpecial }">0</c:if> </span> 人</p>
						</li>
					</ul>
				</div>
				<!-- 转入团 -->
				<div class="orderdetails_tit">转入团</div>
				<table class="activitylist_bodyer_table">
					<thead>
						<tr>
							<th width="10%">团号</th>
							<th width="15%">操作人</th>
							<th width="15%">出/截团日期</th>
							<th width="15%">签证国家</th>
							<th width="15%">资料截止日期</th>
							<th width="5%">预收</th>
							<th width="5%">已切位</th>
							<th width="5%">余位</th>
							<th width="10%">操作</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${newGroup.groupCode }</td>
							<td>${product.createBy.name}</td>
							<td>
								<fmt:formatDate value="${newGroup.groupOpenDate }" pattern="yyyy-MM-dd"/> <br/>
								<fmt:formatDate value="${newGroup.groupCloseDate }" pattern="yyyy-MM-dd"/>
							</td>
							<td>${newGroup.visaCountry }</td>
							<td><fmt:formatDate value="${newGroup.visaDate }" pattern="yyyy-MM-dd"/></td>
							<td>${newGroup.planPosition }</td>
							<td>${newGroup.soldPayPosition }</td>
							<td>${newGroup.freePosition }</td>
							<td><a class="ydbz_x" onclick="gotoProduct(${newGroup.srcActivityId})">产品详情</a></td>
						</tr>
					</tbody>
				</table>
               
				<!-- 游客转团 -->
				<div class="orderdetails_tit">游客转团</div>
				<p><span>报批日期：<fmt:formatDate value="${reviewDetailMap.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>
				<table class="activitylist_bodyer_table" style="width:100%; table-layout:fixed;">
					<thead>
						<tr>
							<th width="10%">姓名</th>
							<th width="10%">签证状态</th>
							<th width="12%">实际约签时间</th>
							<th width="12%">应收金额</th>
							<th width="10%">护照号</th>
							<th width="10%">护照有效期</th>
							<th width="16%">转团原因</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${reviewDetailMap.travelerMapList}" var="travelerMap" varStatus="status">
						<tr>
							<td class="tc inner_cont" title="${travelerMap.travellerName }">${travelerMap.travellerName }</td>
							<td>
								<c:if test="${travelerMap.traveler.visa.visaStatus==0 }">未送签</c:if>
								<c:if test="${travelerMap.traveler.visa.visaStatus==1 }">送签</c:if>			
								<c:if test="${travelerMap.traveler.visa.visaStatus==2 }">约签</c:if>			
								<c:if test="${travelerMap.traveler.visa.visaStatus==3 }">出签</c:if>			
								<c:if test="${travelerMap.traveler.visa.visaStatus==4 }">申请撤签</c:if>			
								<c:if test="${travelerMap.traveler.visa.visaStatus==5 }">撤签成功</c:if>			
								<c:if test="${travelerMap.traveler.visa.visaStatus==6 }">撤签失败</c:if>			
								<c:if test="${travelerMap.traveler.visa.visaStatus==7 }">拒签</c:if>	
								<c:if test="${empty travelerMap.traveler.visa.visaStatus }">无签证</c:if>										 
							</td>
							<td>${travelerMap.traveler.visa.contract}</td>
							<td class="tc inner_cont tdorange fbold" title="${travelerMap.payPrice}">${travelerMap.payPrice}</td>
							<td>${travelerMap.traveler.passportCode }</td>
							<td><fmt:formatDate value="${travelerMap.traveler.passportValidity}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="tc inner_cont" title="${travelerMap.remark}">${travelerMap.remark}</td>
						</tr>
						</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="ydbz_tit">
			<span class="fl">审批动态</span>
		</div>
		<c:set var="rid" value="${reviewDetailMap.id}"></c:set>
		<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>
		
		<c:choose>
			<c:when test="${not empty isReview and isReview}">
				<div class="dbaniu" style="width:230px;">
					<a class="ydbz_s" href="javascript:window.close();">关闭</a>
					<input type="button" name="bohui" value="驳回" class="btn btn-primary" onclick="jbox_bohui('${reviewDetailMap.id}');">
					<input type="button" name="review" value="审批通过" class="btn btn-primary" onclick="reviewPass('${reviewDetailMap.id}');">
				</div>
			</c:when>
			<c:otherwise>
				<div class="ydBtn ydBtn2">
					<a class="ydbz_s" onClick="window.close();">关闭</a>
				</div>
			</c:otherwise>
		</c:choose>		
	</div>
</body>
</html>
