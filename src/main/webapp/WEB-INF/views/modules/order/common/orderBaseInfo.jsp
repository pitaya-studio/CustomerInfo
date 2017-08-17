<%@ page contentType="text/html;charset=UTF-8" %>

<style>
	.ydbzbox {
     background: #FFF none repeat scroll 0% 0%;
     font-size: 12px;
     height: auto !important;
     min-height: 581px;
     overflow: hidden;
 	}
 	input.required[readonly] {
    cursor: not-allowed;
    background-color: #eee;
	}
</style>
<script type="text/javascript">
	//检查固定电话是否符合标准  即000-88888888 或者 0000-7777777
	function checkTelPhone(){
		var reg = new RegExp(/^\d{3}-\d{8}|\d{4}-\d{7}$/);
		check("agentTel",reg);
	}
	//检测传真，规则同上
	function checkFax(){
		var reg = new RegExp(/^\d{3}-\d{8}|\d{4}-\d{7}$/);
		check("faxNum",reg);
	}
	//nameId即标签的id属性，reg即正则表达式，如果不符合正则表达式，则清空所填信息
	function check(nameId,reg){
		var phoneNum = document.getElementById(nameId);
		if(!reg.test(phoneNum.value)){
			$(phoneNum).val("");
		}
	}
</script>
<!-- 价格及人数 -->
<form id="productOrderTotal">
	<c:choose>
		<c:when test="${activityKind eq '2' and agentSourceType eq '2' }">
		</c:when>
		<c:when test="${(activityKind eq '2' and agentSourceType ne '2') or activityKind == '10' }">
			<!-- 报名时选择价格标准，同行价、直客价 -->
			<label>报名价方式：</label>
			<input type="radio" name="priceTypeRadio" id="priceTypeSettlement" value="0" onclick="chosePriceType(this)" checked="checked">同行价
			<input type="radio" name="priceTypeRadio" id="priceTypeSuggest" value="1" onclick="chosePriceType(this)">直客价
		</c:when>
		<c:otherwise></c:otherwise>
	</c:choose>
   	<ul class="ydbz_dj specialPrice">
	    <input type="hidden" id="orderPersonelNum"  name="orderPersonNum" value="${productorder.orderPersonNum}">
	    <input type="hidden" id="activityKind"  name="activityKind" value="${activityKind}">
	    <input type="hidden" id="maxPeopleCount" value="${productGroup.maxPeopleCount }" />
	    <input type="hidden" id="maxChildrenCount" value="${productGroup.maxChildrenCount }" />
	    <input type="hidden" id="lastPeopleCount" value="${counts.orderPersonNumSpecial==null?0:counts.orderPersonNumSpecial }" />
	    <input type="hidden" id="lastChildrenCount" value="${counts.orderPersonNumChild==null?0:counts.orderPersonNumChild }" />
	    <input type="hidden" id="priceType" value="${priceType }" />
	    <input type="hidden" id="groupCode" value="${productGroup.groupCode}" />
	    <!-- 查看用户是否有补位申请权限，如果有则余位不足时可进行补位申请，如果没有则提示余位不足 -->
	    <c:set var="coverFlag" value="0"></c:set>
	    <shiro:hasPermission name="looseActivity:cover">
	    	<c:set var="coverFlag" value="1"></c:set>
		</shiro:hasPermission>
	    <input type="hidden" id="hasCoverRight" value="${coverFlag}">
    	<c:choose>
			<c:when test="${activityKind == '10'}">
	      		<li name="settlementPriceShow">
					<span class="ydtips">舱型&nbsp;${fns:getDictLabel(productGroup.spaceType, 'cruise_type', '-')}</span>
					<p>1/2人同行价：<font color="#FF0000">${adultCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${adultPrice}" /></font></p>
					<p>&nbsp;</p>
					<p>3/4人同行价：<font color="#FF0000">${childCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${childPrice}" /></font></p>
					<p style="display: none">特殊人群：<font color="#FF0000">${specialCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${specialPrice}" /></font></p>
			    </li>
			    <li name="suggestPriceShow">
					<span class="ydtips">舱型&nbsp;${fns:getDictLabel(productGroup.spaceType, 'cruise_type', '-')}</span>
					<p>1/2人直客价：<font color="#FF0000">${suggestAdultCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${suggestAdultPrice}" /></font></p>
					<p>&nbsp;</p>
					<p>3/4人直客价：<font color="#FF0000">${suggestChildCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${suggestChildPrice}" /></font></p>
					<p style="display: none">特殊人群：<font color="#FF0000">${suggestSpecialCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${suggestSpecialPrice}" /></font></p>
			    </li>
			    <li>
			         <span class="ydtips"><span class="xing">*</span>出行人数</span>
			         <p>1/2人出行人数：<input type="text" id="orderPersonNumAdult" name="orderPersonNumAdult" maxlength="3" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')"   onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="${productorder.orderPersonNumAdult}" class="required"> 人</p>
			         <p>&nbsp;</p>
			         <p>3/4人出行人数：<input type="text" id="orderPersonNumChild" name="orderPersonNumChild" maxlength="3" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="${productorder.orderPersonNumChild}" class="required"> 人</p>
			    	 <p style="display: none">特殊人群： <input type="text" id="orderPersonNumSpecial" name="orderPersonNumSpecial" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="${productorder.orderPersonNumSpecial}" class="required"> 人</p>
			    </li>
			    <li style="background:none;">
			       <p>&nbsp;</p>
			       <p><span class="xing">*</span>总计：<input type="text" id="roomNumber" name="roomNumber"  onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="" class="required">间 </p>
			       <p>&nbsp;</p>
			    </li>
	      	</c:when>
      	
	      	<c:otherwise>
	      		<!-- 报名价格(默认显示同行价) -->
	      		<li name="settlementPriceShow">
	      			<span class="ydtips">单价</span>
			    	<p>成人同行价：
			    		<c:if test="${empty adultPrice }">—</c:if>
			    		<c:if test="${not empty adultPrice }"><font color="#FF0000">${adultCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${adultPrice}" /></font></c:if>
			    	</p>
			    	<p>儿童同行价：
			    		<c:if test="${empty childPrice }">—</c:if>
			    		<c:if test="${not empty childPrice }"><font color="#FF0000">${childCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${childPrice}" /></font></c:if>
			    	</p>
			    	<p>特殊人群同行价：
			    		<c:if test="${empty specialPrice }">—</c:if>
			    		<c:if test="${not empty specialPrice }"><font color="#FF0000">${specialCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${specialPrice}" /></font></c:if>
			    	</p>
			    </li>
			    <!-- 直客价 -->
			    <li name="suggestPriceShow">
	      			<span class="ydtips">单价</span>
			    	<p>成人直客价：
			    		<c:if test="${empty suggestAdultPrice }">—</c:if>
			    		<c:if test="${not empty suggestAdultPrice }"><font color="#FF0000">${suggestAdultCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${suggestAdultPrice}" /></font></c:if>
			    	</p>
			    	<p>儿童直客价：
			    		<c:if test="${empty suggestChildPrice }">—</c:if>
			    		<c:if test="${not empty suggestChildPrice }"><font color="#FF0000">${suggestChildCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${suggestChildPrice}" /></font></c:if>
			    	</p>
			    	<p>特殊人群直客价：
			    		<c:if test="${empty suggestSpecialPrice }">—</c:if>
			    		<c:if test="${not empty suggestSpecialPrice }"><font color="#FF0000">${suggestSpecialCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${suggestSpecialPrice}" /></font></c:if>
			    	</p>
			    </li>
			    <!-- 推广价 -->
			    <li name="quauqPriceShow">
	      			<span class="ydtips">单价</span>
			    	<p>成人价：
			    		<c:if test="${empty retailAdultPrice }">—</c:if>
			    		<c:if test="${not empty retailAdultPrice }"><font color="#FF0000">${retailAdultCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${retailAdultPrice}" /></font></c:if>
			    	</p>
			    	<p>儿童价：
			    		<c:if test="${empty retailChildPrice }">—</c:if>
			    		<c:if test="${not empty retailChildPrice }"><font color="#FF0000">${retailChildCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${retailChildPrice}" /></font></c:if>
			    	</p>
			    	<p>特殊人群价：
			    		<c:if test="${empty retailSpecialPrice }">—</c:if>
			    		<c:if test="${not empty retailSpecialPrice }"><font color="#FF0000">${retailSpecialCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${retailSpecialPrice}" /></font></c:if>
			    	</p>
			    </li>
			    <li>
			        <span class="ydtips"><span class="xing">*</span>出行人数</span>
			         <p>    成人：
			                <input type="text" id="orderPersonNumAdult" name="orderPersonNumAdult"
			                   onkeyup="this.value=this.value.replace(/\D|^0.+/g,'');" 
			                   onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" 
			                   value="${productorder.orderPersonNumAdult}" class="required"> 人
			        </p>
			        <p>     儿童：
			                <input type="text" id="orderPersonNumChild" name="orderPersonNumChild" 
			                 onkeyup="this.value=this.value.replace(/\D|^0.+/g,'');" 
			                 onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" 
			                 value="${productorder.orderPersonNumChild}" class="required"  ${productGroup.maxChildrenCount <= (counts.orderPersonNumChild==null?0:counts.orderPersonNumChild)?"readOnly='true'":""}> 人
			        </p>
			        <p>特殊人群：
			                <input type="text" id="orderPersonNumSpecial" name="orderPersonNumSpecial"
			                  onkeyup="this.value=this.value.replace(/\D|^0.+/g,'');" 
			                 onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" 
			                 value="${productorder.orderPersonNumSpecial}" class="required" disabled="disabled" ${productGroup.maxPeopleCount <= (counts.orderPersonNumSpecial==null?0:counts.orderPersonNumSpecial )?"readOnly='true'":""} > 人
			        </p>
			        <!-- 预报名没有房间数 -->
			        <c:if test="${travelerKind != '0'}">
			        	<p style="display: none;"><span class="xing">*</span>总计：<input type="text" id="roomNumber" name="roomNumber"  onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')"  onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')"  value="${productorder.roomNumber }" class="required">间 </p>
			        </c:if>
				</li>
	      	</c:otherwise>
		</c:choose>
    
    <!-- 预报名没有补单属性 -->
    <c:if test="${travelerKind != '0'}">
    	<c:set var="isAfterSupplement"></c:set>
		<c:choose>
			<c:when test="${not empty productorder.isAfterSupplement}">
	      		<c:set var="isAfterSupplement" value="${productorder.isAfterSupplement}"></c:set>
	      	</c:when>
	      	<c:otherwise>
	      		<c:set var="isAfterSupplement" value="0"></c:set>
	      	</c:otherwise>
		</c:choose>
    </c:if>
      
      <!-- 修改的订单没有创建时间，正常产品在产生订单的时候也没有创建时间，只有在补单产品产生订单的时候才能填写创建时间 -->
      <c:choose>
      <c:when test="${empty productorder.id && productGroup.leftdays <= 0}">
			<li style="background:none; height:auto; padding-top:20px;">
				<span style="width:171px;" >创建时间（补单时填写）：</span>
				<input id="createDate" class="inputTxt dateinput" name="createDate" type="text">
				<div style="white-space:nowrap; text-overflow:ellipsis; overflow:hidden; width:300px;">
				<span>特殊人群备注： </span>
				<span title="${product.specialRemark}">${product.specialRemark}</span>
				</div>
				
				<c:set var="isAfterSupplement" value="1"></c:set>
			</li>
		</c:when>
		<c:otherwise>
				<%-- <br><br>
				<div style="white-space:nowrap; text-overflow:ellipsis; overflow:hidden; width:300px;">
				<span>特殊人群备注: </span>
				<span title="${product.specialRemark}">${product.specialRemark}</span>
				</div> --%>
				<li style="background:none;">
			        <p> &nbsp;</p>
			        <p> &nbsp;</p>
			        <p>特殊人群备注:<span title="${product.specialRemark}">${product.specialRemark}</span></p>
				</li>
		</c:otherwise>
		</c:choose>
		<input id="isAfterSupplement" name="isAfterSupplement" type="hidden" value="${isAfterSupplement}">
		
	</ul>
   
</form>  
<div class="ydbz_tit"><span class="ydExpand closeOrExpand"></span>填写预订人信息</div>
<shiro:hasPermission name="${orderTypeStr}:orderContact:modifiability">
	<input type="hidden" value="1" id="orderContact_modifiability">
	<c:set var="orderContact_modifiability" value="1"></c:set>
</shiro:hasPermission>
<shiro:hasPermission name="${orderTypeStr}:orderContact:addibility">
	<input type="hidden" value="1" id="orderContact_addibility">
	<c:set var="orderContact_addibility" value="1"></c:set>
</shiro:hasPermission>
<!-- 渠道联系人 -->
<div class="channel_sel" flag="messageDiv">
	<form id="orderpersonMesdtail">
	   <p class="ydbz_qdmc">
		   <c:choose>
				<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and productorder.orderCompany eq -1 }"><label><span class="xing">*</span>未签约渠道名称：</label><input maxlength="50" type="text" name="orderCompanyNameShow"  id="orderCompanyNameShow" class="required" onblur="setCompanyName(this)"/></c:when>
				<c:when test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and productorder.orderCompany eq -1 }"><label><span class="xing">*</span>
				<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
				<c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
       				直客名称：
   				</c:if>
				<c:if test="${fns:getUser().company.uuid ne  '7a81b21a77a811e5bc1e000c29cf2586'}"> 
       				非签约渠道名称：
   				</c:if>
				</label><input maxlength="50" type="text" name="orderCompanyNameShow" id="orderCompanyNameShow" class="required" onblur="setCompanyName(this)"/></c:when>
				<c:otherwise>签约渠道名称:${productorder.orderCompanyName}</c:otherwise>
			</c:choose>
	   </p>
			<div id="ordercontact">
			<c:choose>
				<c:when test="${productorder.orderCompany == -1 }">
					<ul class="ydbz_qd" name="orderpersonMes" id="orderpersonMes">
						<li>
		            		<label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label>
							<input maxlength="45" type="text" name="contactsName" value="${agentinfo.agentContact}" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/>
		            	</li>
		            	<li class="ydbz_qd_lilong">
			           		<label><span class="xing">*</span>渠道联系人电话：</label>
			           		<input maxlength="20" type="text" id="personPhoneNum" name="contactsTel" value="${agentinfo.agentContactMobile}" class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/><div class="zksx modify-order boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
			           		 <shiro:hasPermission name="${orderTypeStr}:orderContact:addibility">
				           		<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
			           		</shiro:hasPermission>
			           	</li>
			           	<li flag="messageDiv" class="ydbz_qd_close">
			           		<ul class="view">
			           		<%--<li> <label>固定电话aa：</label><input name="contactsTixedTel" id="agentTel" value="${agentinfo.agentContactTel}" onblur="checkPhoneNum()" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="20"/></li> --%>
				                <li><label>固定电话：</label><input name="contactsTixedTel" id="agentTel" value="${agentinfo.agentContactTel}" onblur="checkTelPhone()" type="text" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="20"/></li>
				                <li><label>渠道地址：</label><input onblur="updataInputTitle(this);" title="${address }" name="contactsAddress" value="${address }" type="text" maxlength="50"/></li>
				                <li><label>传真：</label><input name="contactsFax" id="faxNum" value="${agentinfo.agentContactFax}" type="text" onblur="checkFax()" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="20"/></li>
				                <li><label>QQ：</label><input name="contactsQQ" value="${agentinfo.agentContactQQ}" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/></li>
				                <li><label>Email：</label><input name="contactsEmail" value="${agentinfo.agentContactEmail}" type="text" maxlength="50"/></li>
				                <li><label>渠道邮编：</label><input name="contactsZipCode" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/></li>
				                <li><label>其他：</label><input onblur="updataInputTitle(this);" title="" name="remark" value="" type="text" maxlength="200"/></li>
			            	</ul>
			        	</li>
					</ul>
				</c:when>
				
				<c:otherwise>
					<ul class="ydbz_qd min-height" name="orderpersonMes" id="orderpersonMes">
			            <c:choose>
			            	<c:when test="${orderContact_modifiability eq 1 and productorder.orderCompany ne -1 }">
				            	<li>
				            		<label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label>
				            		<span name="channelConcat" ></span>
				            	</li>
			            	</c:when>
			            	<c:otherwise>
			            		<li>
				            		<label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label>
			                    	<select onchange="fillContactInfo2(this)" style="width:200px;height:26px;margin:-2px;">
					                    <c:forEach items="${contactsView }" var="contacts" varStatus="status">
					                    	<option value="${contacts.id },${contacts.contactName },${contacts.contactMobile },${contacts.contactPhone },${contacts.contactFax },${contacts.contactQQ },${contacts.contactEmail },${contacts.agentAddressFull},${contacts.agentPostcode}">${contacts.contactName }</option>
					                    </c:forEach>
			                    	</select>
			                    	<input maxlength="45" type="hidden" name="contactsName" value="${agentinfo.agentContact}"/>			            		
				            	</li>
			            	</c:otherwise>
			            </c:choose>
			            <li class="ydbz_qd_lilong">
			           		<label><span class="xing">*</span>渠道联系人电话：</label>
			           		<input maxlength="20" type="text" name="contactsTel" value="${agentinfo.agentContactMobile}" class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/><div class="zksx modify-order boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
			           		<shiro:hasPermission name="${orderTypeStr}:orderContact:addibility">
				           		<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
			           		</shiro:hasPermission>
			           	</li>
			           	<li flag="messageDiv" class="ydbz_qd_close">
			           		<ul class="view">
				                <li><label>固定电话：</label><input name="contactsTixedTel" value="${agentinfo.agentContactTel}" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="20"/></li>
				                <li><label>渠道地址：</label><input onblur="updataInputTitle(this);" title="${address }" name="contactsAddress" value="${address }" type="text" maxlength="50"/></li>
				                <li><label>传真：</label><input name="contactsFax" value="${agentinfo.agentContactFax}" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/></li>
				                <li><label>QQ：</label><input name="contactsQQ" value="${agentinfo.agentContactQQ}" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20" style="width:150px"/></li>
				                <li><label>Email：</label><input name="contactsEmail" value="${agentinfo.agentContactEmail}" type="text" maxlength="50"/></li>
				                <li><label>渠道邮编：</label><input name="contactsZipCode" value="${zipCode }" title="${zipCode }" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="20"/></li>
				                <li><label>其他：</label><input onblur="updataInputTitle(this);" title="" name="remark" value="" type="text" maxlength="200"/></li>
			            	</ul>
			        	</li>
					</ul>
				</c:otherwise>
			</c:choose>
	    </div>
	</form>
</div>
<div class="ydBtn-down orderPersonMsg" style="display:none;" ><span>&nbsp;</span></div>
<!-- 20150729 订单预定返佣金额 
<div>
	<ul>
		<li>
			<label>预计团队返佣：</label>
			<select id="scheduleBackCurrency">${currencyOptions}</select>
			<input id="scheduleBackPrice" type="text" value="" maxlength="11"/>
		</li>
	</ul>
</div>-->
<div class="ydbz_sxb clear" id="oneToSecondOutStepDiv">
   <div class="ydBtn" id="oneToSecondStepDiv"><span class="ydbz_x" >下一步</span></div>
</div>
<!-- 特殊需求 -->
<div id="manageOrder_m" style="display:none;">
	<div id="contact">
		<div class="ydbz_tit">特殊需求</div>
		<div class="ydbz2_lxr" flag="messageDiv">
			<form class="contactTable">
				<p class="pr">
				 <label style="vertical-align:top">特殊需求：</label>
				 <textarea id="specialDemand" name="specialDemand" class="txtPro inputTxt" maxlength="500" placeholder="最多可输入500字"
				 onkeyup="this.value=this.value.replaceSpecialDemand();" onafterpast="this.value=this.value.replaceSpecialDemand();"></textarea>
				</p>
			</form>
		</div>
		<div class="ydbz2_lxr" flag="messageDiv">
			<form class="contactTable">
				<p class="pr">
					<label style="vertical-align:top">上传附件：</label>
					<input type="hidden" name="fileIdList" id="fileIdList" value="">
					<input type="button" name="specialDemanFiles" class="btn btn-primary" value="选择文件" onclick="uploadFiles4SpecilDeman('specialDemanFiles',this);">
				</p>
			</form>
		</div>
	</div>
</div>