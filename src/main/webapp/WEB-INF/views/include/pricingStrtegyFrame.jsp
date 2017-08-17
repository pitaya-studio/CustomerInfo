<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>


<%--bug16393 ie9以下input不能输入时置灰--%>
<style>
	input[disabled]{
		background-color: #eee;
	}
</style>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css">
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/pricingStrategyFrame.css">
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/agentToOffice/pricingStrategyFrame.js"></script>
<div class="wrap">

   <!-- <div class="title">
   	  <span class="title-text">设置策略</span>
   	  <a class="title-close" title="关闭"></a>
   </div> -->
   <div class="content">
   <c:if test="${groupActivity != null}">
   	    <div class="trial"  onclick="showTrial(this);">
   	    	<span>QUAUQ价试算</span>
   	    	<em class="trial-icon trial-icon-up"></em>
   	    </div>
   	    <div class="price-container" style="display: block;">
   	    	<div class="top-price">
   	    		<div class="top-text"  style="margin-right:50px;">
   	    		<c:if test="${groupActivity.settlementAdultPrice == null ||  groupActivity.settlementAdultPrice == ''}">
   	    				<span class="top-text-edit">成人同行价</span><input type="text" class="price-edit" disabled="disabled"><br/>
   	    			</c:if>
   	    			<c:if test="${groupActivity.settlementAdultPrice != null &&  groupActivity.settlementAdultPrice != ''}">
   	    				<span class="top-text-edit">成人同行价</span><input type="text" class="price-edit" value="${ groupActivity.settlementAdultPrice}" onkeyup="changeClearPriceSum(this);changeResult(this,1);" onblur="judgeInput(this,'adultPricingStrategy',1)" id="standard1"><br/>
   	    			</c:if>
   	    			<span class="top-text-edit">计算结果</span><span class="result-no"  id="result1">--</span>
   	    		</div>
   	    		<div class="top-text"  style="margin-right:50px;">
   	    			<c:if test="${groupActivity.settlementcChildPrice == null ||  groupActivity.settlementcChildPrice == ''}">
   	    				<span class="top-text-edit">儿童同行价</span><input type="text" class="price-edit" disabled="disabled"><br/>
   	    			</c:if>
   	    			<c:if test="${groupActivity.settlementcChildPrice != null &&  groupActivity.settlementcChildPrice != ''}">
   	    				<span class="top-text-edit">儿童同行价</span><input type="text" class="price-edit" value="${ groupActivity.settlementcChildPrice}" onkeyup="changeClearPriceSum(this);changeResult(this,2);" onblur="judgeInput(this,'childPricingStrategy',2)" id="standard2"><br/>
   	    			</c:if>
   	    			<span class="top-text-edit">计算结果</span><span class="result-no"  id="result2">--</span>
   	    		</div>
   	    		<div class="top-text last_special"  style="margin-right:0px;">
   	    		<c:if test="${groupActivity.settlementSpecialPrice == null ||  groupActivity.settlementSpecialPrice == ''}">
   	    				<span class="top-text-edit">特殊人群同行价</span><input type="text" class="price-edit" disabled="disabled"><br/>
   	    			</c:if>
   	    			<c:if test="${groupActivity.settlementSpecialPrice != null &&  groupActivity.settlementSpecialPrice != ''}">
   	    				<span class="top-text-edit">特殊人群同行价</span><input type="text" class="price-edit"  value="${ groupActivity.settlementSpecialPrice}" onkeyup="changeClearPriceSum(this);changeResult(this,3);" onblur="judgeInput(this,'specialPricingStrategy',3)" id="standard3"><br/>
   	    			</c:if>
   	    			<span class="top-text-edit">计算结果</span><span class="result-no"  id="result3">--</span>
   	    		</div>
   	    	</div>
   	    </div>
    </c:if>
    <c:if test="${priningStrategy != null && fn:length(priningStrategy) > 0}">
   	    <div class="op-container">
   	        <div class="top-price">
   	        	<div class="top-text" id="adultPricingStrategy">
   	        		<span class="text-marker">成人</span><br/>
   	        		<c:forEach items="${priningStrategy }" var="strategy">
   	        		<c:if test="${strategy.personType == 0 }">
	   	    	    <div class="items"  onmouseover="showOperaIcon(this);"  onmouseout="hideOperaIcon(this);">
		   	    		<span class="price-title"  onclick="getStrage(this);">
		   	    			<c:if test="${strategy.favorableType == 2 }"><span name="opera1">直减</span></c:if>
		   	    			<c:if test="${strategy.favorableType == 3 }"><span name="opera1">折扣</span></c:if>
		   	    			<em class="title-icon"></em>
		   	    			<ul class="price-strategy">
		   	    				<li onclick="selectTrial(this,1);">直减</li>	<!-- 2 -->
		   	    				<li onclick="selectTrial(this,1);">折扣</li>	<!-- 3 -->
		   	    			</ul>
		   	    		</span>
		   	    		<c:choose>  
						   <c:when test="${groupActivity.settlementAdultPrice == null ||  groupActivity.settlementAdultPrice == '' }"> 	<input name="priceValue1" type="text" class="price-input" onkeyup="calculatePrice(this,1);" value="${strategy.favorableNum }" disabled="disabled">
						   <c:if test="${strategy.favorableType == 2 }"><span style="display: none;" class="percentage">%</span></c:if>
		   	    		<c:if test="${strategy.favorableType == 3 }"><span style="display: inline;" class="percentage">%</span></c:if>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,1,true);"></em><em class="minus" onclick="removeItems(this,1);"></em></span>
		   	    		</c:when>  
						   <c:otherwise><input name="priceValue1" type="text" class="price-input" onkeyup="calculatePrice(this,1);" value="${strategy.favorableNum }">
						   <c:if test="${strategy.favorableType == 2 }"><span style="display: none;" class="percentage">%</span></c:if>
		   	    		<c:if test="${strategy.favorableType == 3 }"><span style="display: inline;" class="percentage">%</span></c:if>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,1);"></em><em class="minus" onclick="removeItems(this,1);"></em></span></c:otherwise>  
						</c:choose> 
		   	    		
	   	    		</div></c:if></c:forEach>
	   	    		<c:if test="${ fn:length(adultList) == 0 }">
	   	    			<div class="items"  onmouseover="showOperaIcon(this);"  onmouseout="hideOperaIcon(this);">
		   	    		<span class="price-title"  onclick="getStrage(this);">
		   	    			<span name="opera1">直减</span>
		   	    			<em class="title-icon"></em>
		   	    			<ul class="price-strategy">
		   	    				<li  onclick="selectTrial(this,1);">直减</li>
		   	    				<li  onclick="selectTrial(this,1);">折扣</li>
		   	    			</ul>
		   	    		</span>
		   	    		<c:choose>  
						   <c:when test="${groupActivity.settlementAdultPrice == null ||  groupActivity.settlementAdultPrice == '' }">  <input name="priceValue1" type="text" class="price-input" onkeyup="calculatePrice(this,1);" disabled="disabled">
						   <span class="percentage">%</span>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,1,true);"></em><em class="minus"  onclick="removeItems(this,1);"></em></span></c:when>  
						   <c:otherwise><input name="priceValue1" type="text" class="price-input" onkeyup="calculatePrice(this,1);" >
						   <span class="percentage">%</span>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,1);"></em><em class="minus"  onclick="removeItems(this,1);"></em></span>
						   </c:otherwise>  
						</c:choose> 
		   	    		
	   	    		</div>
	   	    		</c:if>
   	        	</div>
   	        	<div class="top-text" id="childPricingStrategy">
   	        		<span class="text-marker">儿童</span><br/>
   	        		<c:forEach items="${priningStrategy }" var="strategy">
   	        		<c:if test="${strategy.personType == 1 }">
	   	    	    <div class="items"  onmouseover="showOperaIcon(this);"  onmouseout="hideOperaIcon(this);">
		   	    		<span class="price-title"  onclick="getStrage(this);">
		   	    			<c:if test="${strategy.favorableType == 2 }"><span name="opera2">直减</span></c:if>
		   	    			<c:if test="${strategy.favorableType == 3 }"><span name="opera2">折扣</span></c:if>
		   	    			<em class="title-icon"></em>
		   	    			<ul class="price-strategy" id=2>
		   	    				<li  onclick="selectTrial(this,2);">直减</li>
		   	    				<li  onclick="selectTrial(this,2);">折扣</li>
		   	    			</ul>
		   	    		</span>
		   	    		<c:choose>  
						   <c:when test="${groupActivity.settlementcChildPrice == null ||  groupActivity.settlementcChildPrice == ''}">
						   <input type="text" name="priceValue2" class="price-input" onkeyup="calculatePrice(this,2);" value="${strategy.favorableNum }" disabled="disabled">
						   <c:if test="${strategy.favorableType == 2 }"><span style="display: none;" class="percentage">%</span></c:if>
		   	    		<c:if test="${strategy.favorableType == 3 }"><span style="display: inline;" class="percentage">%</span></c:if>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,2,true);"></em><em class="minus"  onclick="removeItems(this,2);"></em></span>
						   </c:when>  
						   <c:otherwise>
						   <input type="text" name="priceValue2" class="price-input" onkeyup="calculatePrice(this,2);" value="${strategy.favorableNum }">
						   <c:if test="${strategy.favorableType == 2 }"><span style="display: none;" class="percentage">%</span></c:if>
		   	    		<c:if test="${strategy.favorableType == 3 }"><span style="display: inline;" class="percentage">%</span></c:if>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,2);"></em><em class="minus"  onclick="removeItems(this,2);"></em></span>
						   </c:otherwise>  
						</c:choose>
		   	    		
	   	    		</div>
	   	    		</c:if></c:forEach>
	   	    		<c:if test="${ fn:length(childrenList) == 0 }">
	   	    			<div class="items"  onmouseover="showOperaIcon(this);"  onmouseout="hideOperaIcon(this);">
		   	    		<span class="price-title"  onclick="getStrage(this);">
		   	    			<span name="opera2">直减</span>
		   	    			<em class="title-icon"></em>
		   	    			<ul class="price-strategy" id=2>
		   	    				<li  onclick="selectTrial(this,2);">直减</li>
		   	    				<li  onclick="selectTrial(this,2);">折扣</li>
		   	    			</ul>
		   	    		</span>
		   	    		<c:choose>  
						   <c:when test="${groupActivity.settlementcChildPrice == null ||  groupActivity.settlementcChildPrice == ''}"> 
						   <input name="priceValue2" type="text" class="price-input" onkeyup="calculatePrice(this,2);" disabled="disabled">
						   <span class="percentage">%</span>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,2,true);"></em><em class="minus"  onclick="removeItems(this,2);"></em></span>
						   </c:when>  
						   <c:otherwise>
						   <input name="priceValue2" type="text" class="price-input" onkeyup="calculatePrice(this,2);">
						   <span class="percentage">%</span>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,2);"></em><em class="minus"  onclick="removeItems(this,2);"></em></span>
						   </c:otherwise>  
						</c:choose>
		   	    		
	   	    		</div>
	   	    		</c:if>
   	        	</div>
   	        	<div class="top-text"  style="margin-right:0px;" id="specialPricingStrategy">
   	        		<span class="text-marker">特殊人群</span><br/>
   	        		<c:forEach items="${priningStrategy }" var="strategy">
   	        		<c:if test="${strategy.personType == 2 }">
	   	    	    <div class="items"  onmouseover="showOperaIcon(this);"  onmouseout="hideOperaIcon(this);">
		   	    		<span class="price-title"  onclick="getStrage(this);">
		   	    			<c:if test="${strategy.favorableType == 2 }"><span name="opera3">直减</span></c:if>
		   	    			<c:if test="${strategy.favorableType == 3 }"><span name="opera3">折扣</span></c:if>
		   	    			<em class="title-icon"></em>
		   	    			<ul class="price-strategy" id=3>
		   	    				<li  onclick="selectTrial(this,3);">直减</li>
		   	    				<li  onclick="selectTrial(this,3);">折扣</li>
		   	    			</ul>
		   	    		</span>
		   	    		<c:choose>  
						   <c:when test="${groupActivity.settlementSpecialPrice == null ||  groupActivity.settlementSpecialPrice == ''}">
						    <input name="priceValue3" type="text" class="price-input"  onkeyup="calculatePrice(this,3);" value="${strategy.favorableNum }" disabled="disabled">
						    <c:if test="${strategy.favorableType == 2 }"><span style="display: none;" class="percentage">%</span></c:if>
		   	    		<c:if test="${strategy.favorableType == 3 }"><span style="display: inline;" class="percentage">%</span></c:if>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,3,true);"></em><em class="minus"  onclick="removeItems(this,3);"></em></span>
						   </c:when>  
						   <c:otherwise>
						   <input name="priceValue3" type="text" class="price-input"  onkeyup="calculatePrice(this,3);" value="${strategy.favorableNum }">
						   <c:if test="${strategy.favorableType == 2 }"><span style="display: none;" class="percentage">%</span></c:if>
		   	    		<c:if test="${strategy.favorableType == 3 }"><span style="display: inline;" class="percentage">%</span></c:if>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,3);"></em><em class="minus"  onclick="removeItems(this,3);"></em></span>
						   </c:otherwise>  
						</c:choose> 
		   	    		
	   	    		</div></c:if></c:forEach>
	   	    		<c:if test="${fn:length(specialList) == 0  }">
	   	    			 <div class="items"  onmouseover="showOperaIcon(this);"  onmouseout="hideOperaIcon(this);">
		   	    		<span class="price-title"  onclick="getStrage(this);">
		   	    			<span name="opera3">直减</span>
		   	    			<em class="title-icon"></em>
		   	    			<ul class="price-strategy" id=3>
		   	    				<li  onclick="selectTrial(this,3);">直减</li>
		   	    				<li  onclick="selectTrial(this,3);">折扣</li>
		   	    			</ul>
		   	    		</span>
		   	    		<c:choose>  
						   <c:when test="${groupActivity.settlementSpecialPrice == null ||  groupActivity.settlementSpecialPrice == ''}">  
						   <input name="priceValue3" type="text" class="price-input"  onkeyup="calculatePrice(this,3);" disabled="disabled">
						   <span class="percentage">%</span>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,3,true);"></em><em class="minus"  onclick="removeItems(this,3);"></em></span>
						   </c:when>  
						   <c:otherwise>
						   <input name="priceValue3" type="text" class="price-input"  onkeyup="calculatePrice(this,3);">
						   <span class="percentage">%</span>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,3);"></em><em class="minus"  onclick="removeItems(this,3);"></em></span>
						   </c:otherwise>  
						</c:choose> 
		   	    		
	   	    		</div>
	   	    		</c:if>
   	        	</div>
   	        </div>
   	    </div>
   </div>
   </c:if>
    <c:if test="${priningStrategy == null || fn:length(priningStrategy) == 0}">
    	<div class="op-container">
   	        <div class="top-price" style="display:table;margin-left: 15px;width: 100%;"><%--bug16393--%>
   	        	<div class="top-text" id="adultPricingStrategy" style="display:table-cell"><%--bug16393--%>
   	        		<span class="text-marker">成人</span><br/>
	   	    	    <div class="items"  onmouseover="showOperaIcon(this);"  onmouseout="hideOperaIcon(this);">
		   	    		<span class="price-title"  onclick="getStrage(this);">
		   	    			<span name="opera1">直减</span>
		   	    			<em class="title-icon"></em>
		   	    			<ul class="price-strategy">
		   	    				<li onclick="selectTrial(this,1);">直减</li>	<!-- 2 -->
		   	    				<li onclick="selectTrial(this,1);">折扣</li>	<!-- 3 -->
		   	    			</ul>
		   	    		</span>
		   	    		<c:choose>  
						   <c:when test="${(groupActivity.settlementAdultPrice == null ||  groupActivity.settlementAdultPrice == '') && groupActivity != null }">
						   <input name="priceValue1" type="text" class="price-input" onkeyup="calculatePrice(this,1);" disabled="disabled">
						   <span class="percentage">%</span>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,1,true);"></em><em class="minus" onclick="removeItems(this,1);"></em></span>
						   </c:when>  
						   <c:otherwise>
						   <input name="priceValue1" type="text" class="price-input" onkeyup="calculatePrice(this,1);">
						   <span class="percentage">%</span>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,1);"></em><em class="minus" onclick="removeItems(this,1);"></em></span>
						   </c:otherwise>  
						</c:choose>
		   	    		
	   	    		</div>
   	        	</div>
   	        	<div class="top-text" id="childPricingStrategy" style="display:table-cell"><%--bug16393--%>
   	        		<span class="text-marker">儿童</span><br/>
	   	    	    <div class="items"  onmouseover="showOperaIcon(this);"  onmouseout="hideOperaIcon(this);">
		   	    		<span class="price-title"  onclick="getStrage(this);"> 
		   	    			<span name="opera2">直减</span>
		   	    			<em class="title-icon"></em>
		   	    			<ul class="price-strategy" id=2>
		   	    				<li  onclick="selectTrial(this,2);">直减</li>
		   	    				<li  onclick="selectTrial(this,2);">折扣</li>
		   	    			</ul>
		   	    		</span>
		   	    		<c:choose>  
						   <c:when test="${(groupActivity.settlementcChildPrice == null ||  groupActivity.settlementcChildPrice == '') && groupActivity != null  }">
						    <input name="priceValue2" type="text" class="price-input" onkeyup="calculatePrice(this,2);" disabled="disabled">
						    <span class="percentage">%</span>
		   	    		<span class="opera-icon"><em class="plus"  onclick="addItems(this,2,true);"></em><em class="minus"  onclick="removeItems(this,2);"></em></span>
						   </c:when>  
						   <c:otherwise>
						   <input name="priceValue2" type="text" class="price-input" onkeyup="calculatePrice(this,2);">
						   <span class="percentage">%</span>
		   	    			<span class="opera-icon"><em class="plus"  onclick="addItems(this,2);"></em><em class="minus"  onclick="removeItems(this,2);"></em></span>
						   </c:otherwise>  
						</c:choose>
		   	    		
	   	    		</div>
   	        	</div>
   	        	<div class="top-text"  style="margin-right:0px; display:table-cell" id="specialPricingStrategy"><%--bug16393--%>
   	        		<span class="text-marker">特殊人群</span><br/>
	   	    	    <div class="items"  onmouseover="showOperaIcon(this);"  onmouseout="hideOperaIcon(this);">
		   	    		<span class="price-title"  onclick="getStrage(this);">
		   	    			<span name="opera3">直减</span>
		   	    			<em class="title-icon"></em>
		   	    			<ul class="price-strategy" id=3>
		   	    				<li  onclick="selectTrial(this,3);">直减</li>
		   	    				<li  onclick="selectTrial(this,3);">折扣</li>
		   	    			</ul>
		   	    		</span>
		   	    		<c:choose>  
						   <c:when test="${(groupActivity.settlementSpecialPrice == null ||  groupActivity.settlementSpecialPrice == '') && groupActivity != null }"> 
						    <input name="priceValue3" type="text" class="price-input"  onkeyup="calculatePrice(this,3);" disabled="disabled">
						    <span class="percentage">%</span>
		   	    			<span class="opera-icon"><em class="plus"  onclick="addItems(this,3,true);"></em><em class="minus"  onclick="removeItems(this,3);"></em></span>
						   </c:when>  
						   <c:otherwise>
						   		<input name="priceValue3" type="text" class="price-input"  onkeyup="calculatePrice(this,3);">
						   		<span class="percentage">%</span>
		   	    				<span class="opera-icon"><em class="plus"  onclick="addItems(this,3);"></em><em class="minus"  onclick="removeItems(this,3);"></em></span>
						   </c:otherwise>  
						</c:choose> 
		   	    	<!-- 	<input name="priceValue3" type="text" class="price-input"  onkeyup="calculatePrice(this,3);"> -->
		   	    		
	   	    		</div>
   	        	</div>
   	        </div>
   	    </div>
   </div>
    </c:if>
   <!-- <div class="footer">
       <button class="butn cancel">取消</button>
   	   <button class="butn">确定</button>
   </div> -->
</div>