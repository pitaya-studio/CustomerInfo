<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

	
	<!-- 询价基本信息
	<div class="tr">
		<a class="dyzx-add" href="">下载</a>
		<a class="dyzx-add" href="">打印</a>
	</div> -->
	<div class="ydbz_tit">
		基本信息
	</div>
	<div class="messageDiv" flag="messageDiv">
		<div class="seach25 seach100">
		  <p>接待社计调：</p>
		  <p class="seach_r">
		  	<c:forEach items="${aoperators}" var="u">
		  		<span class="seach_check">${u.userName }</span>
		  	</c:forEach>
		  </p>
		</div>
		<!-- 
		<div class="seach25 seach100">
		  <p>机票计调：</p>
		  <p class="seach_r">
		  	<c:forEach items="${toperators}" var="u">
		  		<span class="seach_check">${u.userName }</span>
		  	</c:forEach>
		  </p>
		</div> -->
		<div class="seach25">
		  <p>销售姓名：</p><p class="seach_r">${record.baseInfo.salerName }</p>
		</div>
		<div class="seach25">
		  <p>销售电话：</p>${record.baseInfo.salerPhone }
		</div>
		<div class="seach25">
		  <p>销售邮箱：</p>
		  <p class="seach50">${record.baseInfo.salerEmail}</p>
		</div>
		<div class="kong"></div>
		<div class="seach25">
		  <p>询价客户类型：</p>
		  <p class="seach_r">
		  	<c:choose>
	   			<c:when test="${record.baseInfo.customerType==1}">直客</c:when>
	   			<c:when test="${record.baseInfo.customerType==2}">同行</c:when>
	   			<c:when test="${record.baseInfo.customerType==3}">其他</c:when>
			</c:choose>
		  </p>
		</div>
		<div class="seach25">
		  <p>询价客户：</p><p class="seach_r">${record.baseInfo.customerName }</p>
		</div>
		<div class="seach25">
		  <p>联系人：</p><p class="seach_r">${record.baseInfo.contactPerson }</p>
		</div>
		<div class="seach25">
		  <p>电话：</p><p class="seach_r">${record.baseInfo.contactMobile }</p>
		</div>
		<div class="kong"></div>
		<div class="seach25">
			<p>申请总人数：</p><span id="allPersonSum">${record.baseInfo.allPersonSum}</span>
		</div>
		<div class="seach25">
			<p>成人人数：</p><span id="adultSum">${record.baseInfo.adultSum}</span>
		</div>
		<div class="seach25">
			<p>儿童人数：</p><span id="childSum">${record.baseInfo.childSum}</span>
		</div>
		<div class="seach25">
			<p>特殊人群数：</p><span id="specialPersonSum">${record.baseInfo.specialPersonSum}</span>
		</div>
		<div class="kong"></div>
	</div>
	