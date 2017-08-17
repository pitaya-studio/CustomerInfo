<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>票务计调回复询价</title>
	<meta name="decorator" content="wholesaler"/>
	<%@ include file="commonVar.jsp"%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/travelRequirementsData.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/replyEPriceInfo4Traffic.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/epriceCommon.js" type="text/javascript"></script>
	<script type="text/javascript">
	
	var record = {};
	record.id = '${record.id}';
	
	$(function(){
		//各块信息展开与收起
		boxCloseOn();
		//上传动作
		btfile();
	    //添加单价
		//tianjia();
	});
	</script>
<style type="text/css">
</style>
</head>
<body>
	
		<page:applyDecorator name="eprice_top_repay" >
		</page:applyDecorator>
		
		<!-- 询价基本信息 -->
		<%@ include file="/WEB-INF/views/modules/eprice/ePriceDetailBaseInfo.jsp"%>
					
		<div class="ydbz_tit">机票询价内容</div>
		<div class="title_con">
          	临时机票申请（
          	<c:choose>
   			<c:when test="${record.trafficRequirements.trafficLineType==1}">往返</c:when>
   			<c:when test="${record.trafficRequirements.trafficLineType==2}">单程</c:when>
   			<c:when test="${record.trafficRequirements.trafficLineType==3}">多段</c:when>
   			<c:otherwise>其他</c:otherwise>
			</c:choose>
          	）
		</div>
		
			<div class="messageDiv" flag="messageDiv">
				 <div id="epirce-traffic-line-list-id">
		             <c:forEach items="${lineList}" var="line" varStatus="loop">
		            <c:if test="${record.trafficRequirements.trafficLineType!=2}"> 
		            <div class="title_samil"><strong>第${loop.count }段：</strong></div></c:if>
		            
		             <div class="kong"></div>
		            	<div name="line">
			            	<div class="seach25">
				              <p>出发城市：</p>
				              <p class="seach_r">${line.startCityName}</p>
				            </div>
				            <div class="seach25">
				              <p>到达城市：</p>
				              <p class="seach_r">${line.endCityName}</p>
				            </div>
				            <div class="kong"></div>
				            <div class="seach25">
				              <p>出发日期：</p>
				             <p class="seach_r"><fmt:formatDate value="${line.startDate}" pattern="yyyy-MM-dd"/></p>
				            </div>
				            <div class="seach25">
				              <p>出发时刻：</p>
				              <p class="seach_r" name="startTimeType" vtype="${line.startTimeType}">
								<c:choose>    
							   			<c:when test="${line.startTimeType==1}">早</c:when>
							   			<c:when test="${line.startTimeType==2}">中</c:when>
							   			<c:when test="${line.startTimeType==3}">晚</c:when>
							   			<c:otherwise>其他</c:otherwise>
									</c:choose></p>
				            </div>
				               <div class="seach25">
				              <p>时间区间：</p>
				              <p class="seach_r" name="startTimeType" vtype="${line.startTimeType}">
							   			 ${line.startTime1}:00 - ${line.startTime2}:00
				            </div>
				            <div class="kong"></div>
				            <div class="seach25">
				              <p>舱位等级：</p>
				              <p class="seach_r" name="aircraftSpaceLevel" vtype="${line.aircraftSpaceLevel}"> 
								  <c:choose>    
								        <c:when test="${line.aircraftSpaceLevel==0}">不限</c:when>
							   			<c:when test="${line.aircraftSpaceLevel==1}">头等舱</c:when>
							   			<c:when test="${line.aircraftSpaceLevel==2}">公务舱</c:when>
							   			<c:when test="${line.aircraftSpaceLevel==3}"> 经济舱</c:when>
							   			<c:otherwise>其他</c:otherwise>
									</c:choose>
				              
				          	 </p>
				            </div>
				            <div class="seach25">
				              <p>舱位：</p>
				              <p class="seach_r" name="aircraftSpace" vtype="${line.aircraftSpace}">
				              	 <c:choose>    
								        <c:when test="${line.aircraftSpace=='0'}">不限</c:when>
							   			<c:otherwise>  ${line.aircraftSpace}舱</c:otherwise>
									</c:choose>
				            </div>
				            <div class="kong"></div>
			            </div>
		            </c:forEach>
	            </div>
				
				<div class="seach25 cl-both">
					<p class="seach25">机票申请总人数：</p>${record.trafficRequirements.allPersonSum}
				</div>
				<div class="seach25">
					<p class="seach_r">成人人数：${record.trafficRequirements.adultSum}</p>
				</div>
				<div class="seach25">
					<p class="seach_r">儿童人数：${record.trafficRequirements.childSum}</p>
				</div>
				<div class="seach25">
					<p class="seach_r">特殊人群数：${record.trafficRequirements.specialPersonSum}</p>
				</div>
				<div class="seach100">
					<p class="seach_r">特殊要求：${record.trafficRequirements.specialDescn}</p>
				</div>
		</div>
		
		<div class="xuxian"></div>
		<div class="ydbz_tit">票务计调回复</div>
		
		<div>
			<form id="replay-price-form-id"  action="${ctx }/eprice/manager/project/replyTopEpriceForm" method="post">
				<input type="hidden" name="pid"  value="${project.id }"/>
				<input type="hidden" name="rid"  value="${record.id }"/>
				<input type="hidden" name="rpid"  value="${reply.id }"/>
				<input type="hidden" name="adultSum"  value="${record.trafficRequirements.adultSum}"/>
				<input type="hidden" name="childSum"  value="${record.trafficRequirements.childSum}"/>
				<input type="hidden" name="specialSum"  value="${record.trafficRequirements.specialPersonSum}"/>
                <div class="seach100  wbyu">
					<textarea name="remark" cols="" rows="" class="" ></textarea>
					<p>&nbsp;</p>
					<div  id="price-detaill-dl-all-id">
		                    	<ul class="adult-dt-price">
		                    		<li class="adult-dt-price" style = "display:none">
		                    			<label class="adultTitle">成&nbsp;&nbsp;人&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
		                    			<select name="adultCurrency"  style="width:150px" >
		                    				<option>请选择币种</option>
		                    				<c:forEach items="${currencyList }"  var="currency">
		                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
		                    				</c:forEach>
		                    			</select>
		                    			&nbsp;&nbsp;
		                    			<label>汇率：</label>
		                    			<input type="text"  style="width:50px"   class="exchangerate"   class="exchangerate"   name="adultExchangerate"  maxlength="10" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();" value=""/>
		                    			&nbsp;&nbsp;
		                    			<label>价格：</label>
		                    			<span class="adultCurrencyMark"></span>
		                    			<input type="hidden" name="adultCurrencyId"/>
		                    			<input type="hidden" name="adultCurrencyName"/>
		                    			<input type="text"  style="width:150px" name="adultAmount"  maxlength="10"  class="allNum"/>
		                    			<input type="button" class="btn btn-primary mar_xjia_add"  name="addNewPrice"  value="增加"/>
		                    		</li>
		                    		<li class="adult-dt-price">
		                    			<label class="adultTitle">成&nbsp;&nbsp;人&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
		                    			<select name="adultCurrency"  style="width:150px" >
		                    				<option>请选择币种</option>
		                    				<c:forEach items="${currencyList }"  var="currency">
		                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
		                    				</c:forEach>
		                    			</select>
		                    			&nbsp;&nbsp;
		                    			<label>汇率：</label>
		                    			<input type="text"  style="width:50px"   class="exchangerate"    class="exchangerate"  name="adultExchangerate" maxlength="10"  onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"  value=""/>
		                    			&nbsp;&nbsp;
		                    			<label>价格：</label>
		                    			<span class="adultCurrencyMark"></span>
		                    			<input type="hidden" name="adultCurrencyId"/>
		                    			<input type="hidden" name="adultCurrencyName"/>
		                    			<input type="text"  style="width:150px" name="adultAmount"  maxlength="10"  class="allNum"/>
		                    			<input type="button" class="btn btn-primary mar_xjia_add"  name="addNewPrice"  value="增加"/>
		                    		</li>
		                    	</ul>
		                    	<ul class="child-dt-price">
		                    		<li class="child-dt-price" style="display:none">
		                    			<label  class="childTitle">儿&nbsp;&nbsp;童&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
		                    			<select name="childCurrency"  style="width:150px" >
		                    				<option>请选择币种</option>
		                    				<c:forEach items="${currencyList }"  var="currency">
		                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
		                    				</c:forEach>
		                    			</select>
		                    			&nbsp;&nbsp;
		                    			<label>汇率：</label>
		                    			<input type="text"   style="width:50px"   class="exchangerate"    name="childExchangerate"  maxlength="10"  onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"    value=""/>
		                    			&nbsp;&nbsp;
		                    			<label>价格：</label>
		                    			<span class="childCurrencyMark"></span>
		                    			<input type="hidden" name="childCurrencyId"/>
		                    			<input type="hidden" name="childCurrencyName"/>
		                    			<input type="text"   style="width:150px"   name="childAmount"  maxlength="10"  class="allNum"/>
		                    			<input type="button" class="btn btn-primary mar_xjia_add"  name="addNewPrice"  value="增加"/>
		                    		</li>
		                    		<li class="child-dt-price">
		                    			<label  class="childTitle">儿&nbsp;&nbsp;童&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
		                    			<select name="childCurrency"  style="width:150px" >
		                    				<option>请选择币种</option>
		                    				<c:forEach items="${currencyList }"  var="currency">
		                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
		                    				</c:forEach>
		                    			</select>
		                    			&nbsp;&nbsp;
		                    			<label>汇率：</label>
		                    			<input type="text"   style="width:50px"    class="exchangerate"   name="childExchangerate" maxlength="10"  onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"     value=""/>
		                    			&nbsp;&nbsp;
		                    			<label>价格：</label>
		                    			<span class="childCurrencyMark"></span>
		                    			<input type="hidden" name="childCurrencyId"/>
		                    			<input type="hidden" name="childCurrencyName"/>
		                    			<input type="text"   style="width:150px"   name="childAmount" maxlength="10"   class="allNum"/>
		                    			<input type="button" class="btn btn-primary mar_xjia_add"  name="addNewPrice"  value="增加"/>
		                    		</li>
		                    	</ul>
		                    	
		                    	<ul class="special-dt-price">
		                    		<li class="special-dt-price"   style="display:none">
		                    			<label  class="specialTitle">特殊人群单价：</label>
		                    			<select name="specialCurrency"  style="width:150px" >
		                    				<option>请选择币种</option>
		                    				<c:forEach items="${currencyList }"  var="currency">
		                    					<option id="${currency.id }"  title="${currency.currencyMark }"   value="${currency.currencyExchangerate }">${currency.currencyName}</option>
		                    				</c:forEach>
		                    			</select>
		                    			&nbsp;&nbsp;
		                    			<label>汇率：</label>
		                    			<input type="text"   style="width:50px"   class="exchangerate"    name="specialExchangerate"  maxlength="10"   onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"   value=""/>
		                    			&nbsp;&nbsp;
		                    			<label>价格：</label>
		                    			<span class="specialCurrencyMark"></span>
		                    			<input type="hidden" name="specialCurrencyId"/>
		                    			<input type="hidden" name="specialCurrencyName"/>
		                    			<input type="text"   style="width:150px"  name="specialAmount"  maxlength="10"    class="allNum"/>
		                    			<input type="button" class=""  name="addNewPrice"  value="增加"/>
		                    		</li>
		                    		<li class="special-dt-price">
		                    			<label  class="specialTitle">特殊人群单价：</label>
		                    			<select name="specialCurrency"  style="width:150px" >
		                    				<option>请选择币种</option>
		                    				<c:forEach items="${currencyList }"  var="currency">
		                    					<option id="${currency.id }"  title="${currency.currencyMark }"   value="${currency.currencyExchangerate }">${currency.currencyName}</option>
		                    				</c:forEach>
		                    			</select>
		                    			&nbsp;&nbsp;
		                    			<label>汇率：</label>
		                    			<input type="text"   style="width:50px"   class="exchangerate"    name="specialExchangerate"  maxlength="10"   onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"   value=""/>
		                    			&nbsp;&nbsp;
		                    			<label>价格：</label>
		                    			<span class="specialCurrencyMark"></span>
		                    			<input type="hidden" name="specialCurrencyId"/>
		                    			<input type="hidden" name="specialCurrencyName"/>
		                    			<input type="text"   style="width:150px"  name="specialAmount"  maxlength="10"   class="allNum"/>
		                    			<input type="button" class="btn btn-primary mar_xjia_add"  name="addNewPrice"  value="增加"/>
		                    		</li>
		                    	</ul>
		                    	<div class="seach_allprice" id="price-all-div-id" >
		                    		<p>成人：${record.trafficRequirements.adultSum}人；儿童：${record.trafficRequirements.childSum}  人；特殊人群：${record.trafficRequirements.specialPersonSum}人；共计：${record.trafficRequirements.adultSum+record.trafficRequirements.childSum+record.trafficRequirements.specialPersonSum } 人</p>
		                    		<p class="strAll"></p>
		                    	</div>
	                    	</div>
	                    	<!-- 
					<div class="seach_allprice">
						合计：<em class="gray14">¥</em><em id="all_count" class="red20"></em>
						<input type="hidden" id="operatorTotalPrice" name="operatorTotalPrice"/>
					</div> -->
				</div>
				
				<div class="dbaniu">
					<a class="ydbz_s gray" name="history-back">返回</a>
					<input type="button" class="btn btn-primary" value="报价"  id="return_price"/>
				</div>
			</form>
		</div>		
<!--右侧内容部分结束-->
	
</body>
</html>