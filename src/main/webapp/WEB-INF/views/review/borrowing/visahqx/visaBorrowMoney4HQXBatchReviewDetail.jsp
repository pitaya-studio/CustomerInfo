<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>
	<c:if test="${flag==0}">签证借款-审批</c:if>
	<c:if test="${flag!=0}">签证借款-详情</c:if>
</title>
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
		$("#result").val("0");
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" cols="" rows="" maxlength="200"></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				$("#result").val(0);//代表驳回按钮
				$("#denyReason").val(f.reason);
				reviewSubmit();
			}
		},height:250,width:500});
	};
	//审批通过
	function review(){
		$("#result").val("1");
		var html = '<div class="add_allactivity"><label>请填写您的审批通过原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" cols="" rows="" maxlength="200"></textarea>';
		html += '</div>';
		$.jBox(html, { title: "审批通过备注",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				$("#result").val(1);//代表审批通过按钮
				$("#denyReason").val(f.reason);
				reviewSubmit();
			}
		},height:250,width:500});
	}

	function reviewSubmit() {
		$.ajax({
			type : "POST",
			url : $("#searchForm").attr("action"),
			data : {
				'result' : $("#result").val(),
				'denyReason' : $("#denyReason").val(),
				'batchNo' : $("#batchno").val()
			},
			success : function(resultMap) {
				if (resultMap.result == 'success') {
					$.jBox.tip(resultMap.msg);
					window.location.href = "${ctx}/visa/borrowMoney/review/visaBorrowMoneyReviewList";
				} else if(resultMap.result == 'fail'){
					$.jBox.tip(resultMap.msg);
				} else {
					$.jBox.tip("系统内部错误！");
				}
			}
		});
	}
</script>
</head>
<body>
		
	<!--右侧内容部分开始-->
	<c:if test="${flag==0}"><div class="mod_nav">审批 > 签证借款 > 签证借款审批</div></c:if>
	<c:if test="${flag!=0}"><div class="mod_nav">审批 > 签证借款 > 签证借款详情</div></c:if>
	<div class="ydbz_tit">
		<span class="fl">借款批量审批</span><span class="fr wpr20">报批日期：${revCreateDate }&nbsp;&nbsp;批次：${batchno} </span>
		</div>
	<table id="contentTable" class="activitylist_bodyer_table">
	   <thead>
		  <tr>
			 <th width="10%">游客</th>
			 <th width="10%">币种</th>
			 <th width="15%">申请借款金额</th>
			 <th width="25%">备注</th>
		  </tr>
	   </thead>
	   <tbody>
	   <c:forEach items="${borrowinfolist}" var="travlerborrowinfo">
		  <tr>
			 <td>${travlerborrowinfo.travelername } </td>
			 <td>人民币</td>
			 <td class="tr">￥${travlerborrowinfo.borrowAmount}</td>
			 <td>${travlerborrowinfo.borrowRemark}</td>
		  </tr>
	   </c:forEach>

	   </tbody>
	</table>
	<div style="margin-top:20px;"></div>
	
	<!-- 270需求增加还款日期-s -->
	<div>
	 <c:if test="${ refundDateOption eq '1'}"><!-- 还款日期配置项为'1'时表示是必填,'0'为非必填 -->
	     <span style="color:red;">*</span>
	 </c:if>
	      还款日期：&nbsp;&nbsp;${refund_date }
	</div>
	<!-- 270需求增加还款日期-e -->
	
		<div class="ydbz_tit">
			<span class="fl">审批动态</span>
		</div>
		<!-- 环球行批次审批    审批 -->
		<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>

		<form id="searchForm" action="${ctx}/visa/borrowMoney/review/reviewVisaBorrowMoneybyBatchNo" method="post">
				<!-- 添加提交请求所需数据 -->
				<!-- 1：审批通过按钮，0：驳回按钮 -->
				<input type = "hidden"  id = "result" name="result"/>
				<input type = "hidden"  id = "denyReason" name="denyReason"/>
				<input type = "hidden"  id = "batchno" name="batchNo" value = "${batchno}"/>

				<div class="dbaniu"  style="text-align:center;">
					 <c:choose>
						 <c:when test="${flag==0}">
								<a class="ydbz_s gray" onclick="javaScript:history.back();">取消</a>
								<a class="ydbz_s" onclick="jbox_bohui();">驳回</a>
								<a class="ydbz_s" onclick="review();">审批通过</a>
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