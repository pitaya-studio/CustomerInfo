<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审核-还签证收据批次审核</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />


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

		<div class="ydbz_tit">
			<span class="fl">退签证收据</span><span class="fr wpr20">报批日期：${revCreateDate }&nbsp;&nbsp;批次：${batchno}</span>
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
			   <c:forEach items="${returnreceiptinfolist}" var="travlerinfo">
			   	  <tr>
					 <td>${travlerinfo.travelername } </td>
					 <td>人民币</td>
					 <td class="tr">￥${travlerinfo.receiptAmount}</td>
					 <td>${travlerinfo.receiptor}</td>
					 <td>${travlerinfo.returnReceiptRemark}</td>
				  </tr>
			   </c:forEach>
		   </tbody>
		</table>
		<div style="margin-top:20px;"></div>
		
		
		<%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>
			<form id="searchForm" action="${ctx}/visa/workflow/returnreceipt/batchReviewvisaReturnReceiptbyBatchNo" method="post">
					<!-- 添加提交请求所需数据 -->
					<input type = "hidden"  id = "revid" name="revid" value = "${revid}"/>
					<input type = "hidden"  id = "result" name="result"/>
					<input type = "hidden"  id = "denyReason" name="denyReason"/>
					<input type = "hidden"  id = "nowLevel" name="nowLevel" value = "${nowLevel}"/>
					<input type = "hidden"  id = "orderId" name="orderId" value = "${orderId}"/>
					<input type = "hidden"  id = "travelerId" name="travelerId" value = "${travelerId}"/>
					<input type = "hidden"  id = "flowType" name="flowType" value = "${flowType}"/>
					<input type = "hidden"  id = "flag" name="flag" value = "${flag}"/>
					
					<input type = "hidden"  id = "revids" name="revids" value = "${revids}"/>
					<input type = "hidden"  id = "remarks" name="remarks" value = "${remarks}"/>
					<input type = "hidden"  id = "batchno" name="batchno" value = "${batchno}"/>
					
					<input type = "hidden"  id = "fromflag" name="fromflag" value = "${fromflag}"/>
					
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