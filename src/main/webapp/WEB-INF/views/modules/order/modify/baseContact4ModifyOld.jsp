 <%@ page contentType="text/html;charset=UTF-8"%>
    <!-- 渠道联系人 -->
<div flag="messageDiv">
	<form id="orderpersonMesdtail">
	   <p class="ydbz_qdmc">预订渠道：
	   		<c:choose>
	   			<c:when test="${agentinfo.id == -1 }" >${productorder.orderCompanyName}</c:when>
	   			<c:otherwise>	   				
	   				<select id="modifyAgentInfo" onchange="getAllContactsByAgentId(this)">	   						   				
						<c:forEach items="${agentinfoList }" var="agentinfo">
							<option value="${agentinfo.id},${agentinfo.agentName},${agentinfo.agentContact},
							${agentinfo.agentContactMobile},${agentinfo.agentTel},
							${agentinfo.agentAddress},${agentinfo.agentContactFax},
							${agentinfo.agentContactQQ},${agentinfo.agentContactEmail},
							${agentinfo.agentPostcode}" 
							<c:if test="${agentinfo.agentName eq (productorder.orderCompanyName) }">selected</c:if> >${agentinfo.agentName}</option>
						</c:forEach>
					</select>
	   			</c:otherwise>
	   		</c:choose>
	   </p>
	    <div id="ordercontact">	    
			<!-- mod start by yang.jiang 2016-1-14 23:02:54 -->
			<c:choose>
	   			<c:when test="${agentinfo.id == -1 }" >
	   				<c:forEach items="${orderContactsSrc }" var="orderContact" varStatus="s1">
	   				<ul class="ydbz_qd" name="orderpersonMes" varStatus="s2">
		   				<li>
							<label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label>
							<input maxlength="10" type="text" name="contactsName" value="${orderContact.contactsName}" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/>
						</li>
						<li class="ydbz_qd_lilong">
							<label><span class="xing">*</span>渠道联系人电话：</label>
							<input maxlength="15" type="text" name="contactsTel" id="contactsTel${s2.count}" value="${orderContact.contactsTel}" class="required" onkeyup="this.value=this.value.replace(/[^\\d\+\\-]/g,\\)"  onafterpaste="this.value=this.value.replace(/[^\\d\+\\-]/g,\\)"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
							<c:if test="${allowAddAgentInfo == 1 and s1.count == 1 }">
				           		<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
			           		</c:if>
			           		<c:if test="${s1.count != 1 }">
			           			<span class="ydbz_x gray" onclick="yd1DelPeople(this)">删除联系人</span>
			           		</c:if>
		           		</li>
						<li flag="messageDiv" style="display:none" class="ydbz_qd_close">
							<ul>
								<li><label>固定电话：</label><input type="text" name="contactsTixedTel" id="contactsTixedTel${s2.count}" value="${orderContact.contactsTixedTel}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/></li>
								<li><label>渠道地址：</label><input type="text" name="contactsAddress" id="contactsAddress${s2.count}" value="${address}"/></li>
						        <li><label>传真：</label><input type="text" name="contactsFax" id="contactsFax${s2.count}" value="${orderContact.contactsFax}"/></li>
						        <li><label>QQ：</label><input type="text" name="contactsQQ" id="contactsQQ${s2.count}" value="${orderContact.contactsQQ}"/></li>
						        <li><label>Email：</label><input type="text" name="contactsEmail" id="contactsEmail${s2.count}" value="${orderContact.contactsEmail}"/></li>
						        <li><label>渠道邮编：</label><input type="text" name="contactsZipCode" id="contactsZipCode${s2.count}" value="${orderContact.contactsZipCode}"/></li>
						        <li><label>其他：</label><input type="text" name="remark" id="remark${s2.count}" value="${orderContact.remark}"/></li>
					        </ul>
				        </li>
			        </ul>
			        </c:forEach>
	   			</c:when>
	   			<c:otherwise>
		     		<c:set var="orderContact" value="${orderContacts[0] }"></c:set>
			         <ul class="ydbz_qd min-height" name="orderpersonMes" varStatus="s2">
	 					<li>
							<label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label>
							<span name="channelConcat"></span>
						</li>
						<li class="ydbz_qd_lilong">
							<label><span class="xing">*</span>渠道联系人电话：</label>
							<input maxlength="15" type="text" name="contactsTel" id="contactsTel${s2.count}" value="${orderContact.contactsTel}" class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
				           	<span class="ydbz_x yd1AddPeople" name="addContactButton" onclick="addAgentContactNew(this)">添加联系人</span>
		           		</li>
						<li flag="messageDiv" style="display:none" class="ydbz_qd_close">
							<ul>
								<li><label>固定电话：</label><input type="text" name="contactsTixedTel" id="contactsTixedTel${s2.count}" value="${orderContact.contactsTixedTel}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/></li>
								<li><label>渠道地址：</label><input type="text" name="contactsAddress" id="contactsAddress${s2.count}" value="${orderContact.contactsAddress}"/></li>
						        <li><label>传真：</label><input type="text" name="contactsFax" id="contactsFax${s2.count}" value="${orderContact.contactsFax}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
						        <li><label>QQ：</label><input type="text" name="contactsQQ" id="contactsQQ${s2.count}" value="${orderContact.contactsQQ}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
						        <li><label>Email：</label><input type="text" name="contactsEmail" id="contactsEmail${s2.count}" value="${orderContact.contactsEmail}"/></li>
						        <li><label>渠道邮编：</label><input type="text" name="contactsZipCode" id="contactsZipCode${s2.count}" value="${orderContact.contactsZipCode}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
						        <li><label>其他：</label><input type="text" name="remark" id="remark${s2.count}" value="${orderContact.remark}"/></li>
					        </ul>
				        </li>
				     </ul>
				</c:otherwise>
			</c:choose>
			<!-- mod end   by yang.jiang 2016-1-14 23:02:54 -->
		     
	    </div>
	    	    <!-- 是否可以更改批发商渠道：0为否，1为是 -->
		<c:if test="${fns:getUser().company.isAllowModify == 0}">
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
		</c:if>
	</form>
</div>