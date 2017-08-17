<%@ page contentType="text/html;charset=UTF-8" %>

<div class="ydbz_tit">基本信息 </div>
<div class="messageDiv" flag="messageDiv">

	<c:if test="${record.type==1}">
		<div class="seach25 seach100">
			<p>接待社计调：</p>
			<p class="seach_r">
				<c:forEach items="${aoperators}" var="u">
					<span class="seach_check">${u.userName }</span>
				</c:forEach>
			</p>
		</div>
	</c:if>
	
	<div class="seach25">
		<p>询价客户类型：</p>
		<p class="seach_r">
      		<c:choose>
				<c:when test="${record.baseInfo.customerType==1}">直客</c:when>
				<c:when test="${record.baseInfo.customerType==2}">同行</c:when>
				<c:otherwise>其他</c:otherwise>
			</c:choose>
		</p>
	</div>	
    <div class="seach25">
		<p>团队类型：</p> 
		<p class="seach_r">
      		<c:choose>
				<c:when test="${record.baseInfo.teamType==1}">单团</c:when>
				<c:when test="${record.baseInfo.teamType==4}">大客户</c:when>
				<c:when test="${record.baseInfo.teamType==3}">游学</c:when>
				<c:when test="${record.baseInfo.teamType==5}">自由行</c:when>
				<c:when test="${record.baseInfo.teamType==7}">机票</c:when>
				<c:otherwise>其他</c:otherwise>
			</c:choose>
		</p>
	</div>
	<div class="kong"></div>
	
    <div class="seach25">
		<p>销售姓名：</p>
		<p class="seach_r">${record.baseInfo.salerName }</p>
    </div>
    <div class="seach25">
		<p>销售电话：</p><p class="seach_r">${record.baseInfo.salerPhone }</p>
    </div>
    <div class="seach25">
		<p>销售邮箱：</p>
    	<p class="seach_r">${record.baseInfo.salerEmail}</p>
    </div>
    <div class="kong"></div>
    <div class="seach25">
		<p>询价客户：</p>
		<p class="seach_r">${record.baseInfo.customerName }</p>
    </div>
    <shiro:hasPermission name="enquiry:agentinfo:visibility">
	    <div class="seach25">
			<p>联系人：</p>
			<p class="seach_r">${record.baseInfo.contactPerson }</p>
		</div>
		<div class="seach25">
			<p>电话：</p>
			<p class="seach_r">${record.baseInfo.contactMobile }</p>
	    </div>
		<div class="seach25">
			<p>其他联系方式：</p>
			<p class="seach_r">${record.baseInfo.otherContactWay }</p>
		</div>
	    <div class="kong"></div>
	</shiro:hasPermission>
    
    <div class="seach25">
		<p>申请总人数：</p>
		<span class="seach_r" id="allPersonSum">${record.baseInfo.allPersonSum}</span>
    </div>
    <div class="seach25">
		<p>成人人数：</p>
		<span class="seach_r" id="adultSum">${record.baseInfo.adultSum}</span>
    </div>
    <div class="seach25 pr">
		<p>儿童人数：</p>
		<span class="seach_r" id="childSum">${record.baseInfo.childSum}</span>
    </div>
	<div class="seach25">
		<p>特殊人群数：</p>
		<span class="seach_r" id="specialPersonSum">${record.baseInfo.specialPersonSum}</span>
    </div>
    <div class="kong"></div>
    
</div>