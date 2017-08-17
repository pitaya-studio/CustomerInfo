 <%@ page contentType="text/html;charset=UTF-8"%>
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
<div class="orderdetails_tit">
    <div class="ydbz_tit">预订人信息</div>
</div>
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

<div flag="messageDiv">
	<form id="orderpersonMesdtail">
		<p class="ydbz_qdmc">预订渠道：
		<!-- 315需求 针对越柬行踪 将非签约渠道改为直客 -->
		<c:choose>
			<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and agentinfo.id eq -1 }">
				未签
				<input type="text" name="orderCompanyName" <c:if test='${agentinfo_modifiability ne 1 or fn:length(agentinfoList) eq 1}'>readOnly="readOnly"</c:if> id="orderCompanyName" class="required" value="${productorder.orderCompanyName }"/>
				<input type="hidden" value="${productorder.orderCompany}" id="orderCompany" name="orderCompany">
				<input type="hidden" value="${productorder.orderCompanyName}" id="orderCompanyNameShow" name="orderCompanyNameShow">
			</c:when>
			<c:when test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and agentinfo.id eq -1 }">
				<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' }">
					直客
				</c:if>
				<c:if test="${companyUuid ne  '7a81b21a77a811e5bc1e000c29cf2586' }">
					非签约渠道
				</c:if>
				<input type="text" name="orderCompanyName" <c:if test='${agentinfo_modifiability ne 1 or fn:length(agentinfoList) eq 1}'>readOnly="readOnly"</c:if> id="orderCompanyName" class="required" value="${productorder.orderCompanyName }"/>
				<input type="hidden" value="${productorder.orderCompany}" id="orderCompany" name="orderCompany">
				<input type="hidden" value="${productorder.orderCompanyName}" id="orderCompanyNameShow" name="orderCompanyNameShow">
			</c:when>
			<c:otherwise>
				<%-- <input type="hidden" name="orderCompanyName" id="orderCompanyName" value="${productorder.orderCompanyName }"/> --%>
				<input type="hidden" name="orderCompanyNameShow" id="orderCompanyNameShow" value="${productorder.orderCompanyName}">
				<c:choose>
	            	<c:when test="${agentinfo_modifiability ne 1 or fn:length(agentinfoList) eq 1}">
	            		<span>${productorder.orderCompanyName }</span>
	            		<select name="agentShow" id="orderCompany" <c:if test='${agentinfo_modifiability ne 1 or fn:length(agentinfoList) eq 1}'>style="display: none;"</c:if> onChange="getAllContactsByAgentId(this)" >
		                    <c:if test="${not empty agentinfoList }">
		                        <c:forEach items="${agentinfoList}" var="agentinfo">
		                            <option value="${agentinfo.id }" <c:if test="${productorder.orderCompany == agentinfo.id  }">selected="selected"</c:if> >${agentinfo.agentName }</option>
		                        </c:forEach>
		                    </c:if>
		                </select>
	            	</c:when>
	            	<c:otherwise>
		            	<select name="agentShow" id="orderCompany" onChange="getAllContactsByAgentId(this)" >
		                    <c:if test="${not empty agentinfoList }">
		                        <c:forEach items="${agentinfoList}" var="agentinfo">
		                            <option value="${agentinfo.id }" <c:if test="${productorder.orderCompany == agentinfo.id  }">selected="selected"</c:if> >${agentinfo.agentName }</option>
		                        </c:forEach>
		                    </c:if>
		                </select>
	            	</c:otherwise>
	            </c:choose>
			</c:otherwise>
		</c:choose>
		</p>
		
		<div id="ordercontact" <c:if test="${fns:getUser().id ne productorder.createBy.id and fns:getUser().id ne productorder.salerId and (empty agentinfo_visibility or agentinfo_visibility ne 1) }">style="visibility:hidden;"</c:if>>       
			<c:choose>
            	<%-- 非签约渠道,显示普通的输入框 --%>
            	<c:when test="${agentinfo.id eq -1}">
             		<c:forEach items="${orderContactsSrc }" var="orderContact" varStatus="s1">
             			<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
                			<input type="hidden" value = "${orderContact.agentId}" name="agentId"/>
                			<input type="hidden" value = "${orderContact.id}" name="id"/>
		                   <li>
		                       <label><span class="xing">*</span>渠道联系人<span name="contactNo"></span>：</label>
		                       <input maxlength="45" type="text" name="contactsName" value="${orderContact.contactsName }" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/>
		                   </li>
		                   <li class="ydbz_qd_lilong">
		                       <label><span class="xing">*</span>渠道联系人电话：</label>
		                       <input maxlength="20" type="text" name="contactsTel" value="${orderContact.contactsTel }" class="required"
		                              onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/>
		                       <div class="zksx modify-order boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
		                       <shiro:hasPermission name="${orderTypeStr}Order:orderContact:addibility">
			                       <c:if test="${s1.count == 1 }">
					           			<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
				           			</c:if>
				           			<c:if test="${s1.count != 1}">
				           				<span class="ydbz_x gray" name="delContactButton" onclick="yd1DelPeople(this)">删除联系人</span>
				           			</c:if>
			           			</shiro:hasPermission>
		                   </li>
		                   <li flag="messageDiv"  class="ydbz_qd_close">
		                   <ul class="view">
		                   	   <li><label>固定电话：</label><input maxlength="20" name="contactsTixedTel" id="agentTel" type="text" value="${orderContact.contactsTixedTel}" onblur="checkTelPhone()"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /></li>
		                       <li><label>渠道地址：</label><input maxlength="50" onblur="updataInputTitle(this);" title="${orderContact.contactsAddress }" name="contactsAddress" type="text" value="${orderContact.contactsAddress }"/></li>
		                       <li><label>传真：</label><input maxlength="20" name="contactsFax" id="faxNum" type="text" value="${orderContact.contactsFax }" onblur="checkFax()" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/></li>
		                       <li><label>QQ：</label><input maxlength="20" name="contactsQQ" type="text" value="${orderContact.contactsQQ }" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
		                       <li><label>Email：</label><input maxlength="50" name="contactsEmail" type="text" value="${orderContact.contactsEmail }"/></li>
		                       <li><label>渠道邮编：</label><input maxlength="20" maxlength="" type="text" name="contactsZipCode" value="${orderContact.contactsZipCode}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
		                       <li><label>其他：</label><input maxlength="200" onblur="updataInputTitle(this);" title="${orderContact.remark }" name="remark" type="text" value="${orderContact.remark }"/></li>
		                   </ul>
		                   </li>
            			</ul>
             		</c:forEach>
				</c:when>
				<c:otherwise>
            		<c:forEach items="${orderContactsSrc }" var="orderContact" varStatus="s1">
             			<ul class="ydbz_qd min-height" id="orderpersonMes" name="orderpersonMes">
                			<input type="hidden" value="${orderContact.agentId}" name="agentId"/>
                			<input type="hidden" value="${orderContact.id}" name="id"/>
	             			<li>
	                       <label><span class="xing">*</span>渠道联系人<span name="contactNo"></span>：</label>
	                       <span name="channelConcat"></span>
		                   </li>
		                   <li class="ydbz_qd_lilong">
		                       <label><span class="xing">*</span>渠道联系人电话：</label>
		                       <input maxlength="20" type="text" name="contactsTel" value="${orderContact.contactsTel }" class="required"
		                              onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/>
		                       <div class="zksx modify-order boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
		                       <shiro:hasPermission name="${orderTypeStr}Order:orderContact:addibility">
			                       <c:if test="${s1.count == 1 }">
					           			<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
				           			</c:if>
				           			<c:if test="${s1.count != 1}">
				           				<span class="ydbz_x gray" name="delContactButton" onclick="yd1DelPeople(this)">删除联系人</span>
				           			</c:if>
				           		</shiro:hasPermission>
		                   </li>
		                   <li flag="messageDiv"   class="ydbz_qd_close">
		                   <ul class="view">
		                   	   <li><label>固定电话：</label><input maxlength="20" name="contactsTixedTel" type="text" value="${orderContact.contactsTixedTel}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /></li>
		                       <li><label>渠道地址：</label><input maxlength="50" onblur="updataInputTitle(this);" title="${orderContact.contactsAddress }" name="contactsAddress" type="text" value="${orderContact.contactsAddress }"/></li>
		                       <li><label>传真：</label><input maxlength="20" name="contactsFax" type="text" value="${orderContact.contactsFax }" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
		                       <li><label>QQ：</label><input maxlength="20" name="contactsQQ" type="text" value="${orderContact.contactsQQ }" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
		                       <li><label>Email：</label><input maxlength="50" name="contactsEmail" type="text" value="${orderContact.contactsEmail }"/></li>
		                       <li><label>渠道邮编：</label><input maxlength="20" type="text" name="contactsZipCode" value="${orderContact.contactsZipCode}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
		                       <li><label>其他：</label><input maxlength="200" onblur="updataInputTitle(this);" title="${orderContact.remark }" name="remark" type="text" value="${orderContact.remark }"/></li>
		                   </ul>
		                   </li>
            			</ul>
             		</c:forEach>
				</c:otherwise>
        	</c:choose>
		</div>
	
	</form>
</div>