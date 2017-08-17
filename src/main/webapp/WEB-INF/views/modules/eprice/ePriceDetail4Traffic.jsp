<%@ page contentType="text/html;charset=UTF-8" %>

<div class="ydbz_tit" >机票询价内容</div>
<div flag="messageDiv" class="messageDiv">
  <form class="">
  	
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
 
 <div id="epirce-traffic-line-list-id">
  <c:forEach items="${lineList}" var="line" varStatus="status">
  	<div class="title_con">第${status.count }段</div>
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
    <c:when test="${line.aircraftSpaceLevel==0}">头等舱</c:when>
	<c:when test="${line.aircraftSpaceLevel==1}">头等舱</c:when>
	<c:when test="${line.aircraftSpaceLevel==2}">公务舱</c:when>
	<c:when test="${line.aircraftSpaceLevel==3}"> 经济舱</c:when>
	<c:otherwise>其他</c:otherwise>
</c:choose>
      
  	 </p>
    </div>
    <div class="seach25">
      <p>舱位：</p>
    <c:choose>    
    <c:when test="${line.aircraftSpace=='0'}">不限</c:when>
	<c:otherwise>  ${line.aircraftSpace}舱</c:otherwise>
</c:choose>
    </div>
    <div class="kong"></div>
   </div>
  </c:forEach>
 </div>
 
 
 <div class="title_con">机票申请基本信息</div>
 <div class="seach25">
   <p>申请总人数：</p>${record.trafficRequirements.allPersonSum}
 </div>
 <div class="seach25">
   <p>成人人数：</p>${record.trafficRequirements.adultSum}
 </div>
	<div class="seach25 pr">
   <p>儿童人数：</p>${record.trafficRequirements.childSum}
 </div>
 <div class="seach25">
   <p>特殊人群人数：</p>${record.trafficRequirements.specialPersonSum}
 </div>
 <div class="kong"></div>
 <div class="seach25 seach100">
   <p>特殊要求：</p>
   <p class="seach_r">${record.trafficRequirements.specialDescn}</p>
 </div>
 
 <div class="title_con">选择报价</div>
 <c:forEach items="${flist}" var="f">

  <div class="inquiry_choseprice">
  	<label for="radio-reply-id-${f.id }">
  			<c:if test="${f.id==record.acceptToperatorReply.id}">
  				<input type="radio"   id="radio-reply-id-${f.id }" disabled="disabled" checked="checked" name="chosePriceB" value="${f.id}">${f.operatorUserName}
    				</c:if>
    				<c:if test="${f.id!=record.acceptToperatorReply.id}">
    					<input type="radio"   id="radio-reply-id-${f.id }" disabled="disabled" name="chosePriceB" value="${f.id}">${f.operatorUserName}
    				</c:if>
     		
     	</label>
     	<div class="inquiry_chosepri_r" name="reply-info-div" vstat="${f.status}">
     		<c:if test="${f.status>=2}">
     		<p>${f.content}</p>
      	<p class="inquiry_chosepri_r2">
       	<span>接待社报价：</span>
       		<c:if test="${not empty f.operatorTotalPrice}">
       			<span name="reply-price-detail-name" style="display: none;">${f.priceDetail}</span>
<span>总计：</span>
<em class="red20"><span class="moneyFormat">¥${f.operatorTotalPrice}</span></em>

       		</c:if>
       		<c:if test="${empty f.operatorTotalPrice }">
         			<br/>
<span>总计： </span>
<br/>
<span>成人</span>
	<c:forEach items="${f.adultTop }"  var="adultTop"> 
<em class="adultmark">${fns:getCurrencyNameOrFlag(adultTop.currencyId,0)}</em> <em class="red20">${adultTop.sumPrice}</em>
</c:forEach>
<br/>
<span>儿童</span>
	<c:forEach items="${f.childTop }"  var="childTop"> 
<em class="childmark">${fns:getCurrencyNameOrFlag(childTop.currencyId,0)}</em> <em class="red20">${childTop.sumPrice}</em>
</c:forEach>
<br/>
<span>特殊人群</span>
	<c:forEach items="${f.specialTop }"  var="specialTop"> 
<em class="specialmark">${fns:getCurrencyNameOrFlag(specialTop.currencyId,0)}</em><em class="red20"> ${specialTop.sumPrice}</em>
</c:forEach>
<br/>
         		</c:if>
<c:if test="${not empty f.remark }">
<br/>
<span>备注：</span>
<span class="seach_r">${f.remark}</span>
</c:if>
      		</p>
     		</c:if>
     	</div>
     	<!-- 机票备注 -->

 </div>
</c:forEach>
   
  </form>
</div>