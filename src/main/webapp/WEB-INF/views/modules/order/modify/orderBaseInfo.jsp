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
					<p>1/2人出行人数：<input type="text" id="orderPersonNumAdult" name="orderPersonNumAdult" maxlength="3"  onkeyup="this.value=this.value.replace(/\D/g,'')"   onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="${productorder.orderPersonNumAdult}" class="required"> 人</p>
					<p>&nbsp;</p>
					<p>3/4人出行人数：<input type="text" id="orderPersonNumChild" name="orderPersonNumChild" maxlength="3"  onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="${productorder.orderPersonNumChild}" class="required"> 人</p>
					<p style="display: none">特殊人群： <input type="text" id="orderPersonNumSpecial" name="orderPersonNumSpecial" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="${productorder.orderPersonNumSpecial}" class="required"> 人</p>
				</li>
				<li style="background:none;">
					<p>&nbsp;</p>
					<p><span class="xing">*</span>总计：<input type="text" id="roomNumber" name="roomNumber"  onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="${productorder.roomNumber }" class="required">间 </p>
					<p>&nbsp;</p>
				</li>
	    	</c:when>
	    	<c:otherwise>
				<li><span class="ydtips">单价</span>
					<p>成人<c:choose><c:when test="${priceType eq 1}">直客价</c:when><c:when test="${priceType eq 2 }">价</c:when><c:otherwise>同行价</c:otherwise></c:choose>：
						<c:if test="${empty (priceType eq 2 ? retailAdultPrice : adultPrice)}">—</c:if>
						<c:if test="${not empty (priceType eq 2 ? retailAdultPrice : adultPrice)}"><font color="#FF0000">${adultCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${priceType eq 2 ? retailAdultPrice : adultPrice}" /></font></c:if>
					</p>
					<p>儿童<c:choose><c:when test="${priceType eq 1}">直客价</c:when><c:when test="${priceType eq 2 }">价</c:when><c:otherwise>同行价</c:otherwise></c:choose>：
						<c:if test="${empty (priceType eq 2 ? retailChildPrice : childPrice)}">—</c:if>
						<c:if test="${not empty (priceType eq 2 ? retailChildPrice : childPrice)}"><font color="#FF0000">${childCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${priceType eq 2 ? retailChildPrice : childPrice}" /></font></c:if>
					</p>
					<p>特殊人群<c:choose><c:when test="${priceType eq 1}">直客价</c:when><c:when test="${priceType eq 2 }">价</c:when><c:otherwise>同行价</c:otherwise></c:choose>：
						<c:if test="${empty (priceType eq 2 ? retailSpecialPrice : specialPrice)}">—</c:if>
						<c:if test="${not empty (priceType eq 2 ? retailSpecialPrice : specialPrice)}"><font color="#FF0000">${specialCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${priceType eq 2 ? retailSpecialPrice : specialPrice}" /></font></c:if>
					</p>
				</li>
				<li>
					<span class="ydtips"><span class="xing">*</span>出行人数</span>
					<p>    成人：${productorder.orderPersonNumAdult}
		                <input type="hidden" id="orderPersonNumAdult" name="orderPersonNumAdult" value="${productorder.orderPersonNumAdult}"> 人
					</p>
					<p>     儿童：${productorder.orderPersonNumChild}
		                <input type="hidden" id="orderPersonNumChild" name="orderPersonNumChild" value="${productorder.orderPersonNumChild}"> 人
					</p>
					<p>特殊人群：${productorder.orderPersonNumSpecial}
		                <input type="hidden" id="orderPersonNumSpecial" name="orderPersonNumSpecial" value="${productorder.orderPersonNumSpecial}"> 人
					</p>
					<p style="display: none;"><span class="xing">*</span>总计：<input type="text" id="roomNumber" name="roomNumber"  onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="${productorder.roomNumber }" class="required">间 </p>
				</li>
	    	</c:otherwise>
	    </c:choose>
	</ul>
</form>  
<div class="ydbz_tit">预订人信息</div>
<shiro:hasPermission name="${orderTypeStr}Order:orderContact:modifiability">
	<input type="hidden" value="1" id="orderContact_modifiability">
	<c:set var="orderContact_modifiability" value="1"></c:set>
</shiro:hasPermission>
<shiro:hasPermission name="${orderTypeStr}Order:orderContact:addibility">
	<input type="hidden" value="1" id="orderContact_addibility">
	<c:set var="orderContact_addibility" value="1"></c:set>
</shiro:hasPermission>
<shiro:hasPermission name="${orderTypeStr}Order:agentinfo:modifiability">
	<input type="hidden" value="1" id="agentinfo_modifiability">
	<c:set var="agentinfo_modifiability" value="1"></c:set>
</shiro:hasPermission>
<shiro:hasPermission name="${orderTypeStr}Order:agentinfo:visibility">
	<input type="hidden" value="1" id="agentinfo_visibility">
	<c:set var="agentinfo_visibility" value="1"></c:set>
</shiro:hasPermission>
<!-- 渠道联系人 -->
<div flag="messageDiv">
	<form id="orderpersonMesdtail">
		<p class="ydbz_qdmc">预订渠道：
			<c:choose>
	   			<c:when test="${agentinfo.id == -1 }" >${productorder.orderCompanyName}</c:when>
	   			<c:otherwise>
	   				<select id="modifyAgentInfo" onchange="setOrdercontactInfo(this)">
						<c:forEach items="${agentinfoList }" var="agentinfo">
							<option value="${agentinfo.id},${agentinfo.agentName},${agentinfo.agentContact},${agentinfo.agentContactMobile},${agentinfo.agentTel},${agentinfo.agentAddress},${agentinfo.agentContactFax},${agentinfo.agentContactQQ},${agentinfo.agentContactEmail},${agentinfo.agentPostcode}" <c:if test="${agentinfo.id eq (productorder.orderCompany) }">selected</c:if> >${agentinfo.agentName}</option>
						</c:forEach>
					</select>
	   			</c:otherwise>
	   		</c:choose>
		</p>
		<c:if test="${fns:getUser().id eq productorder.createBy.id or fns:getUser().id eq productorder.salerId or (not empty agentinfo_visibility and agentinfo_visibility eq 1) }">
		<div id="ordercontact">
			<c:forEach items="${orderContacts}" var="orderContact">
		    	<ul class="ydbz_qd" name="orderpersonMes">
					<li><label><span class="xing">*</span>渠道联系人：</label><input maxlength="10" type="text" name="contactsName" value="${orderContact.contactsName}" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/></li>
					<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label><input maxlength="15" type="text" name="contactsTel" value="${orderContact.contactsTel}" class="required" onkeyup="this.value=this.value.replace(/[^\\d\+\\-]/g,\\)"  onafterpaste="this.value=this.value.replace(/[^\\d\+\\-]/g,\\)"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div></li>
					<li flag="messageDiv" style="display:none" class="ydbz_qd_close">
						<ul>
							<li><label>固定电话：</label><input type="text" name="contactsTixedTel" value="${orderContact.contactsTixedTel}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/></li>
							<li><label>渠道地址：</label><input type="text" name="contactsAddress" value="${orderContact.contactsAddress}"/></li>
					        <li><label>传真：</label><input type="text" name="contactsFax" value="${orderContact.contactsFax}"/></li>
					        <li><label>QQ：</label><input type="text" name="contactsQQ" class="qq" value="${orderContact.contactsQQ}"/></li>
					        <li><label>Email：</label><input type="text" name="contactsEmail" class="email" value="${orderContact.contactsEmail}"/></li>
					        <li><label>渠道邮编：</label><input type="text" name="contactsZipCode" class="zipCode" value="${orderContact.contactsZipCode}"/></li>
					    	<li><label>其他：</label><input type="text" name="remark" value="${orderContact.remark}"/></li>
				        </ul>
			    	</li>
				</ul>
			</c:forEach>
	    </div>
	    </c:if>
	    <!-- 是否可以更改批发商渠道：0为否，1为是 -->
		<%-- <c:if test="${agentinfo_modifiability == 1}">
			<script type="text/javascript">
				$('#modifyAgentInfo').prop('disabled',true);
				$('[name="contactsName"]').prop('readOnly', true);
				$('[name="contactsTel"]').prop('readOnly', true);
				$('[name="contactsTixedTel"]').prop('readOnly', true);
				$('[name="contactsAddress"]').prop('readOnly', true);
				$('[name="contactsFax"]').prop('readOnly', true);
				$('[name="contactsQQ"]').prop('readOnly', true);
				$('[name="contactsEmail"]').prop('readOnly', true);
				$('[name="contactsZipCode"]').prop('readOnly', true);
				$('[name="remark"]').prop('readOnly', true)
			</script>
		</c:if> --%>
	</form>
</div>
<div class="ydBtn-down orderPersonMsg"><span>&nbsp;</span></div>
<!-- 特殊需求 -->
<div id="manageOrder_m">
	<div id="contact">
	   <div class="ydbz_tit">特殊需求</div>
	   <div class="ydbz2_lxr" flag="messageDiv">
	      <form class="contactTable">
	          <p>
	             <label style="vertical-align:top">特殊需求：</label>
	             <textarea id="specialDemand" name="specialDemand" maxlength="500" style="word-break:break-all">${productorder.specialDemand}</textarea>
	          </p>
	      </form>
	   </div>
		<div class="ydbz2_lxr" flag="messageDiv">
			<form class="contactTable">
				<p class="pr">
					<label style="vertical-align:top">附件下载：</label>
					<c:forEach items="${fns:getDocInfosByIds(productorder.specialDemandFileIds)}" var="docInfo">
						<br>
						<span class="seach_checkbox_2" style="position:relative;margin-left:105px">
							<a href="javascript:void(0)" class="downloadFile" onclick="downloads4SpecialDeman(${docInfo.id})">${docInfo.docName}</a>
						</span>
					</c:forEach>
				</p>
			</form>
		</div>
	</div>
</div>