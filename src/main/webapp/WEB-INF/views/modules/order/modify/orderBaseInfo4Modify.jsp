<%@ page contentType="text/html;charset=UTF-8" %>
<!-- 价格及人数 -->
<form id="productOrderTotal">
	<ul class="ydbz_dj specialPrice">
	    <input type="hidden" id="orderPersonelNum"  name="orderPersonNum" value="${productorder.orderPersonNum}">
	    <c:choose>
	    	<c:when test="${productorder.orderStatus == '10'}">
				<li><span class="ydtips">舱型&nbsp;${fns:getDictLabel(productGroup.spaceType, 'cruise_type', '-')}</span>
					<p>1/2人<c:if test="${priceType eq 1}">直客价</c:if><c:if test="${priceType ne 1 }">同行价</c:if>：<font color="#FF0000">${adultCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${adultPrice}" /></font></p>
					<p>&nbsp;</p>
					<p>3/4人<c:if test="${priceType eq 1}">直客价</c:if><c:if test="${priceType ne 1 }">同行价</c:if>：<font color="#FF0000">${childCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${childPrice}" /></font></p>
					<p style="display: none">特殊人群：<font color="#FF0000">${specialCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${specialPrice}" /></font></p>
				</li>
				<li>
					<span class="ydtips"><span class="xing">*</span>出行人数</span>
					<p>
					1/2人出行人数：
					<input type="text" id="orderPersonNumAdult" name="orderPersonNumAdult"  
					onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="3"  onafterpaste="this.value=this.value.replace(/\D/g,'')"  
					value="${productorder.orderPersonNumAdult}" class="required"
						   onblur="checkFreePosition2(this, ${productorder.orderPersonNumAdult});"> 人</p>
					<p>&nbsp;</p>
					<p>
					3/4人出行人数：
					<input type="text" id="orderPersonNumChild" name="orderPersonNumChild"  
					onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="3"
					onafterpaste="this.value=this.value.replace(/\D/g,'')"  
					value="${productorder.orderPersonNumChild}" class="required"
						   onblur="checkFreePosition2(this, ${productorder.orderPersonNumChild});"> 人</p>
					<p style="display: none">特殊人群： <input type="text" id="orderPersonNumSpecial" name="orderPersonNumSpecial" 
					onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"  
					value="${productorder.orderPersonNumSpecial}" class="required"> 人</p>
				</li>
				<li style="background:none;">
					<p>&nbsp;</p>
					<p><span class="xing">*</span>总计：<input type="text" id="roomNumber" name="roomNumber"  onkeyup="this.value=this.value.replace(/\D/g,'')"  
					onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="${productorder.roomNumber }"
								 onblur="checkFreePosition2(this, ${productorder.roomNumber})" class="required">间 </p>
					<p>&nbsp;</p>
				</li>
	    	</c:when>
	    	<c:otherwise>
				<li><span class="ydtips">单价</span>
					<p>成人
						<c:choose>
							<c:when test="${priceType eq 1}">直客价</c:when>
							<c:when test="${priceType eq 2 }">价</c:when>
							<c:otherwise>同行价</c:otherwise></c:choose>：
							<c:if test="${empty (priceType eq 2 ? retailAdultPrice : adultPrice)}">—</c:if>
							<c:if test="${not empty (priceType eq 2 ? retailAdultPrice : adultPrice) }"><font color="#FF0000">${adultCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${priceType eq 2 ? retailAdultPrice : adultPrice}" /></font></c:if>
					</p>
					<p>儿童
						<c:choose>
							<c:when test="${priceType eq 1}">直客价</c:when>
							<c:when test="${priceType eq 2 }">价</c:when>
							<c:otherwise>同行价</c:otherwise>
						</c:choose>：
						<c:if test="${empty (priceType eq 2 ? retailChildPrice : childPrice)}">—</c:if>
						<c:if test="${not empty (priceType eq 2 ? retailChildPrice : childPrice)}"><font color="#FF0000">${childCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${priceType eq 2 ? retailChildPrice : childPrice}" /></font></c:if>
					</p>
					<p>特殊人群
						<c:choose>
							<c:when test="${priceType eq 1}">直客价</c:when>
							<c:when test="${priceType eq 2 }">价</c:when>
							<c:otherwise>同行价</c:otherwise></c:choose>：
							<c:if test="${empty (priceType eq 2 ? retailSpecialPrice : specialPrice)}">—</c:if>
							<c:if test="${not empty (priceType eq 2 ? retailSpecialPrice : specialPrice)}"><font color="#FF0000">${specialCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${priceType eq 2 ? retailSpecialPrice : specialPrice}" /></font></c:if>
					</p>
				</li>
				<li>
					<span class="ydtips"><span class="xing">*</span>出行人数</span>
					<p>    成人：
						<c:if test="${productorder.payMode eq '7'}">
						<input type="hidden" id="orderPersonNumAdult" name="orderPersonNumAdult" value="${productorder.orderPersonNumAdult}" />
							${productorder.orderPersonNumAdult}
						</c:if>
						<c:if test="${productorder.payMode ne '7'}">
		                <input type="text" id="orderPersonNumAdult" name="orderPersonNumAdult" value="${productorder.orderPersonNumAdult}" 
		                	onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"
							   onblur="checkFreePosition2(this, ${productorder.orderPersonNumAdult});">
	                	</c:if>
		                	 人
					</p>
					<p>     儿童：
						<c:if test="${productorder.payMode eq '7'}">
							<input type="hidden" id="orderPersonNumChild" name="orderPersonNumChild" value="${productorder.orderPersonNumChild}" />
							${productorder.orderPersonNumChild}
						</c:if>
						<c:if test="${productorder.payMode ne '7'}">
		                <input type="text" id="orderPersonNumChild" name="orderPersonNumChild" value="${productorder.orderPersonNumChild}" 
		                onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"
							   onblur="checkFreePosition2(this, ${productorder.orderPersonNumChild});">
		                </c:if>
		                	 人
					</p>
					<p>特殊人群：
						<c:if test="${productorder.payMode eq '7'}">
							<input type="hidden" id="orderPersonNumSpecial" name="orderPersonNumSpecial" value="${productorder.orderPersonNumSpecial}">
							${productorder.orderPersonNumSpecial}
						</c:if>
						<c:if test="${productorder.payMode ne '7'}">
		                <input type="text" id="orderPersonNumSpecial" name="orderPersonNumSpecial" value="${productorder.orderPersonNumSpecial}" 
		                onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"
							   onblur="checkFreePosition2(this, ${productorder.orderPersonNumSpecial});">
		                 </c:if>
		                	 人
					</p>
					<p style="display: none;"><span class="xing">*</span>总计：<input type="text" id="roomNumber" name="roomNumber"  
					onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="${productorder.roomNumber }" class="required">间 </p>
				</li>
	    	</c:otherwise>
	    </c:choose>
	</ul>
</form>  

<!-- 联系人信息 -->
<%@ include file="/WEB-INF/views/modules/order/modify/baseContact4Modify.jsp"%>

<div class="ydBtn-down orderPersonMsg"><span>&nbsp;</span></div>
<!-- 特殊需求 -->
<div id="manageOrder_m">
	<div id="contact">
	   <div class="ydbz_tit">特殊需求</div>
	   <div class="ydbz2_lxr" flag="messageDiv">
	      <form class="contactTable">
	          <p>
	             <label style="vertical-align:top">特殊需求：</label>
	             <textarea id="specialDemand" name="specialDemand" maxlength="500" placeholder="最多可输入500字">${productorder.specialDemand}
				 </textarea>
	          </p>
	      </form>
	   </div>
		<div class="ydbz2_lxr" flag="messageDiv">
			<form class="contactTable">
				<p class="pr">
					<label style="vertical-align:top">上传附件：</label>
					<input type="hidden" name="fileIdList" id="fileIdList" value="${productorder.specialDemandFileIds}">
					<input type="button" name="specialDemanFiles" class="btn btn-primary" value="选择文件" onclick="uploadFiles4SpecilDeman('specialDemanFiles',this);">
					<c:forEach items="${fns:getDocInfosByIds(productorder.specialDemandFileIds)}" var="docInfo">
						<br>
						<span class="seach_checkbox_2" style="position:relative;margin-left:105px">
							<a href="javascript:void(0)" class="downloadFile" onclick="downloads4SpecialDeman(${docInfo.id})">${docInfo.docName}</a>
							<a style="margin-left:10px;" class="deleteicon" href="javascript:void(0)" onclick="deleteFiles4SpecialDeman(${docInfo.id},event)">x</a>
						</span>
					</c:forEach>
				</p>
			</form>
		</div>
	</div>
</div>