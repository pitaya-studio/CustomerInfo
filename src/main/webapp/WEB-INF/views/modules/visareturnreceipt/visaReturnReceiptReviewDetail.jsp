<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审核-还签证收据审核</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />

<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<link type="text/css" rel="stylesheet"  href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet"  href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript"  src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript"  src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
	$(function() {
		//AA码
		AAHover();
		if ("${reply}" != null && "${reply}" != '') {
			alert("${reply}");
		}
	});
	
	//驳回
	function jbox_bohui(){
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				$("#result").val(0);
				$("#denyReason").val(f.reason);
				$("#searchForm").submit();
			}
		},height:250,width:500});
	};
	//审核通过
	function review(){
		//alert("审核通过");
		$("#result").val(1);
		$("#searchForm").submit();
	}
</script>
</head>
<body>
		
		<!--右侧内容部分开始-->
		<div class="mod_nav">审核 > 还签证收据 > 还签证收据审批</div>
		<div class="ydbz_tit">订单详情</div>
		<div class="orderdetails1">
             		<table border="0" width="98%" style="margin-left: 25px">
				<tbody>
					<tr>
						<td class="mod_details2_d1">下单人：</td>
				        <td class="mod_details2_d2">${visaOrder.createBy.name}</td>
				        
						<td class="mod_details2_d1">下单时间：</td>
						<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaOrder.createDate}"/></td> 
						
                        <td class="mod_details2_d1">团队类型：</td>
				        <td class="mod_details2_d2">
								 单办签
				        </td>	
				        
                         <td class="mod_details2_d1">收客人：</td>
				        <td class="mod_details2_d2">${visaOrder.createBy.name }</td>
					</tr>
					<tr> 
                        <td class="mod_details2_d1">订单编号：</td>
						<td class="mod_details2_d2">${visaOrder.orderNo }</td>
						
                        <td class="mod_details2_d1">订单团号：</td>
						<td class="mod_details2_d2">${visaOrder.groupCode }</td>
						
						<td class="mod_details2_d1">销售：</td>
						<td class="mod_details2_d2">${visaOrder.salerName}</td>
                    </tr>
					<tr>
						<td class="mod_details2_d1">订单总额：</td>
						<td class="mod_details2_d2">${totalMoney }</td>
                              <td class="mod_details2_d1">订单状态：</td>
						<td class="mod_details2_d2">
						<c:if test="${visaOrder.visaOrderStatus==0 }">未收款</c:if>
								<c:if test="${visaOrder.visaOrderStatus==1 }">已收款</c:if>
								<c:if test="${visaOrder.visaOrderStatus==2 }">已取消</c:if>
						</td>	 
						<td class="mod_details2_d1">操作人：</td>
				        <td class="mod_details2_d2">${visaProduct.createBy.name }</td>     
					</tr>
				</tbody>
			</table>
              </div>
		<div class="ydbz_tit"><span class="fl">办签信息</span></div>
		<table cellspacing="10" cellpadding="10" style="width:98%; line-height:25px;margin-left: 32px; margin-bottom:50px; position:relative; z-index:9;">
			<tbody>
				<tr> 
					<td>产品编号：${visaProduct.productCode }</td>
					<td>领区：
						<c:if test="${not empty visaProduct.collarZoning }">
				                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
			             </c:if>
					</td>
					<td>签证国家：${country.countryName_cn }</td>
				</tr>
				<tr> 
					<td>签证类别：${visaType.label }</td>
					<td colspan="2">签证状态：${visaStauts.label }</td>
				</tr>
				<tr><td colspan="3"><div class="mod_information_d7"></div></td></tr>
				<tr> 
					<td>预计出团时间：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visa.forecastStartOut }"/></td>
					<td>预计约签时间：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visa.forecastContract }"/></td>
				</tr>
				<tr> 
					<td>实际出团时间：<fmt:formatDate pattern="yyyy-MM-dd" value="${visa.startOut }"/></td>
					<td>实际约签时间：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visa.contract }"/></td>
				</tr>
				<tr> 
					<td>申办签AA码：${visa.AACode }</td>
					<td>制表人姓名：${visa.makeTable }</td>
				</tr>
			</tbody>
		</table>
		<div class="ydbz_tit">
			<span class="fl">还签证收据</span><span class="fr wpr20">报批日期：${revCreateDate }</span>
      		</div>
		<table id="contentTable" class="activitylist_bodyer_table">
		   <thead>
			  <tr>
				 <th width="10%">游客</th>
				 <th width="10%">币种</th>
				 <th width="15%">申请还收据金额</th>
				 <th width="10%">收据领取人</th>
				 <th width="25%">备注</th>
			  </tr>
		   </thead>
		   <tbody>
			  <tr>
				 <td>${traveler.name } </td>
				 <td>${revCurrency.currencyName } </td>
				 <td class="tr">${revCurrency.currencyMark } ${revReceiptAmount }</td>
				 <td>${revReceiptor }</td>
				 <td>${revCreateReason }</td>
			  </tr>
		   </tbody>
		</table>
		<div style="margin-top:20px;"></div>
		<div class="allzj tr f18">
                  <div class="all-money">退签证收据总金额：<font class="f14"></font><span class="f20">${revCurrency.currencyMark }</span>${revReceiptAmount }</div>
		</div>
		<%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>
			<form id="searchForm" action="${ctx}/visa/workflow/returnreceipt/reviewVisaReturnReceipt" method="post">
					<!-- 添加提交请求所需数据 -->
					<input type = "hidden"  id = "revid" name="revid" value = "${revid}"/>
					<input type = "hidden"  id = "result" name="result"/>
					<input type = "hidden"  id = "denyReason" name="denyReason"/>
					<input type = "hidden"  id = "nowLevel" name="nowLevel" value = "${nowLevel}"/>
					<input type = "hidden"  id = "orderId" name="orderId" value = "${orderId}"/>
					<input type = "hidden"  id = "travelerId" name="travelerId" value = "${travelerId}"/>
					<input type = "hidden"  id = "flowType" name="flowType" value = "${flowType}"/>
					<input type = "hidden"  id = "flag" name="flag" value = "${flag}"/>
					<div class="dbaniu"  style="text-align:center;">
					      <c:choose> 
						     <c:when test="${flag==0}"> 
				      				<a class="ydbz_s gray" onclick="javaScript:history.back();">取消</a>
									<a class="ydbz_s" onclick="jbox_bohui();">驳回</a>
									<a class="ydbz_s" onclick="review();">审核通过</a>
						     </c:when> 
						     <c:otherwise> 
						      	   <a class="ydbz_s gray" onclick="javaScript:history.back();">返回</a>
						     </c:otherwise>
						</c:choose>
					</div>
			 </form>
		<!--右侧内容部分结束-->

</body>
</html>