<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计调详情</title>
	<meta name="decorator" content="wholesaler"/>
	<%@ include file="commonVar.jsp"%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/travelRequirementsData.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/ePriceInfo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/epriceCommon.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		var record = {};
		record.type='${record.type}';
		record.isAppFlight='${record.isAppFlight}';
		record.operatorPrice='${record.operatorPrice}';
		// 地接计调
		var aoperTotalPrice = '${record.acceptAoperatorReply}';
		if(aoperTotalPrice!=null){
			record.aoperatorTotalPrice='${record.acceptAoperatorReply.operatorTotalPrice}';
		}
		// 机票计调
		var aoperTotalPrice = '${record.acceptToperatorReply}';
		if(aoperTotalPrice!=null){
			record.toperatorTotalPrice='${record.acceptToperatorReply.operatorTotalPrice}';
		}
		
		record.outPrice='${record.outPrice}';
		// 返回计调列表
		$(function(){
			$("#history-back").click(function(){
				location.href=contextPath+"/eprice/manager/project/erecordtrafficlist";
			});
		});
	</script>
<style type="text/css">
</style>
</head>
<body>
	<input  type="hidden" id="recordType" value="${record.type}"/>
	<!--右侧内容部分开始-->
	<page:applyDecorator name="eprice_repay_record" ></page:applyDecorator>
	
	<!-- 询价基本信息 -->
	<%@ include file="/WEB-INF/views/modules/eprice/ePriceDetailBaseInfo.jsp"%>						
	
	<!--第二步 接待社询价内容-->
	<c:if test="${operMenu == '1'}">
		<c:if test="${record.type==1||record.type==3||record.type==4||record.type==5}">			
			<!--第二步-->
			<div class="ydbz_tit"><span class="ydExpand closeOrExpand"></span>接待社询价内容</div>
			<div flag="messageDiv" class="messageDiv">
				<form>
					<!-- 询价项目 -->
					<%@ include file="/WEB-INF/views/modules/eprice/ePriceDetail4Admit.jsp"%>
					
					<div class="title_con">选择报价</div>
					<div id="admit-eprice-list-id">
						<c:forEach items="${alist}" var="a">
							<c:if test="${a.operatorUserId == theoperator }">
								<div class="inquiry_choseprice">
									<label for="radio-reply-id-${a.id }">
										<c:if test="${a.id==record.acceptAoperatorReply.id}">
											<input type="radio"   id="radio-reply-id-${a.id }" disabled="disabled" checked="checked" name="chosePriceA" value="${a.id}">${a.operatorUserName}
										</c:if>
										<c:if test="${a.id!=record.acceptAoperatorReply.id}">
											<input type="radio"   id="radio-reply-id-${a.id }" disabled="disabled" name="chosePriceA" value="${a.id}">${a.operatorUserName}
										</c:if>
									</label>
									<div class="inquiry_chosepri_r" name="reply-info-div" vstat="${a.status}">
										<c:if test="${a.status>=2}">
											<p>${a.content}</p>
											<p class="inquiry_chosepri_r2">
												<span>接待社报价：</span>
												<span name="reply-price-detail-name" style="display: none;">${a.priceDetail}</span>
												<c:if test="${not empty a.operatorTotalPrice}">
													<span name="reply-price-detail-name" style="display: none;">${a.priceDetail}</span>
													<span>总计：</span>
													<em class="red20"><span class="moneyFormat">¥${a.operatorTotalPrice}</span></em>
												</c:if>
												<c:if test="${empty a.operatorTotalPrice }">
													<br/><span>总计： </span>
													<br/><span>成人</span>
													<c:forEach items="${a.adult }"  var="adult"> 
														<em class="adultmark">${fns:getCurrencyNameOrFlag(adult.currencyId,0)}</em>
														<em class="red20">${adult.sumPrice}</em>
													</c:forEach>
													<br/><span>儿童</span>
													<c:forEach items="${a.child }"  var="child"> 
														<em class="childmark">${fns:getCurrencyNameOrFlag(child.currencyId,0)}</em>
														<em class="red20">${child.sumPrice}</em>
													</c:forEach>
													<br/><span>特殊人群</span>
													<c:forEach items="${a.special }"  var="special"> 
														<em class="specialmark">${fns:getCurrencyNameOrFlag(special.currencyId,0)}</em>
														<em class="red20"> ${special.sumPrice}</em>
													</c:forEach>
													<br/>
												</c:if>
											</p>
											<c:forEach items="${upRelayFileList}" var="file">
												<div><strong>&nbsp;&nbsp;&nbsp;	·</strong>${file.fileName}</div>
											</c:forEach>
											<c:if test="${upRelayFileList.size()>=1}" >
												<a class="ydbz_s" href="${ctx}/sys/docinfo/zipdownload/${upRelayFileDocIds}/aditFiles">下载</a>
											</c:if>
										</c:if>
									</div>
								</div>
							</c:if>
						</c:forEach>
					</div>
				</form>			
			</div>			
		</c:if>
	</c:if>
	
	<!--第三步 机票相关-->
	<c:if test="${record.isAppFlight==1}">
		<%@ include file="/WEB-INF/views/modules/eprice/ePriceDetail4Traffic.jsp"%>
	</c:if>
	
     	<!-- 当前计调报价被选中  -->
        <c:if test="${ operMenu == '1' && record.acceptAoperatorReply.operatorUserId == theoperator }">
	        <c:if test="${record.estimateStatus == '3' }">
		        <!-- 销售报价 -->
		        <div class="ydbz_tit"><span class="ydExpand closeOrExpand"></span>销售报价</div>
		        <div class="messageDiv" flag="messageDiv">
		        	<div class="seach25">
			              <p>成本价：</p>
			              <c:if test="${not empty record.operatorPrice}">
			             	<span>¥</span>
			             	 <span class="moneyFormat">${record.operatorPrice}</span>
			             </c:if>
			             <c:if test="${empty record.operatorPrice}">
			             	<span id="operator-price-span-id"  class="moneyFormat"></span>
		              		<span id="operator-price-top-span-id"  class="moneyFormat"></span>
			             </c:if>
		            </div>
		            <div class="seach25">
		              <p>外报价：</p><span>¥</span>
		              <span class="moneyFormat">${record.outPrice}</span>
		            </div>
		            <!-- 
		            <div class="seach25">
		              <p>结算价：</p>
		              <span class="moneyFormat">¥ ${countprice}</span>
		            </div> -->
		            <div class="kong"></div>
		        </div>
	        </c:if>
        </c:if>
        
        <div class="dbaniu">
			<a class="ydbz_s gray" id="close" href="javascript:window.opener=null;window.close();">关闭</a>
		</div>
      <!--右侧内容部分结束--> 
      
	
</body>
</html>