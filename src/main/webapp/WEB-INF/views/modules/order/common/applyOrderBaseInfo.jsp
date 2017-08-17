<%@ page contentType="text/html;charset=UTF-8" %>
<!-- 价格及人数 -->
<form id="productOrderTotal">
   <ul class="ydbz_dj specialPrice">
    <input type="hidden" id="orderPersonelNum"  name="orderPersonNum" value="${productorder.orderPersonNum}">
    <li><span class="ydtips">单价</span>
       <p>      成人：<font color="#FF0000">${adultCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${adultPrice}" /></font> 元</p>
       <p>      儿童：<font color="#FF0000">${childCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${childPrice}" /></font> 元</p>
       <p>特殊人群：<font color="#FF0000">${specialCurrencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${specialPrice}" /></font> 元</p>
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
      </li>
   </ul>
</form>  
<div class="ydbz_tit"><span class="ydExpand closeOrExpand"></span>填写预订人信息</div>
<!-- 渠道联系人 -->
<div flag="messageDiv">
	<form id="orderpersonMesdtail">
	   <p class="ydbz_qdmc">预订渠道：${productorder.orderCompanyName}<c:if test="${productorder.orderCompany == -1 && empty productorder.orderCompanyName}">非签约渠道</c:if></p>
	    <div id="ordercontact">
	        <ul class="ydbz_qd"  name="orderpersonMes">
	           <li><label><span class="xing">*</span>渠道联系人1：</label>
	           	<input maxlength="10" type="text" name="contactsName" value="${orderContact.contactsName}" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/>
	           </li>
	           <li class="ydbz_qd_lilong">
	           <label><span class="xing">*</span>渠道联系人电话：</label>
		           <input maxlength="15" type="text" name="contactsTel" value="${orderContact.contactsTel}" class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
		           <span class="ydbz_x yd1AddPeople" onclick="yd1AddPeople(this)">添加联系人</span>
	           </li>
	           <li flag="messageDiv" style="display:none" class="ydbz_qd_close">
	           	   <ul>
	                <li><label>固定电话：</label><input type="text" name="contactsTixedTel" value="${orderContact.contactsTixedTel}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /></li>
	                <li><label>渠道地址：</label><input type="text" name="contactsAddress" value="${orderContact.contactsAddress}"/></li>
	                <li><label>传真：</label><input type="text" name="contactsFax" value="${orderContact.contactsFax}"/></li>
	                <li><label>QQ：</label><input type="text" name="contactsQQ" value="${orderContact.contactsQQ}"/></li>
	                <li><label>Email：</label><input type="text" name="contactsEmail" value="${orderContact.contactsEmail}"/></li>
	                <li><label>渠道邮编：</label><input type="text" name="contactsZipCode" value="${orderContact.contactsZipCode}"/></li>
	                <li><label>其他：</label><input type="text" name="remark" value="${orderContact.remark}"/></li>
	               </ul>
	           </li>
	         </ul>
		     <c:forEach items="${orderContacts}" var="orderContact" varStatus="s">
		         <ul class="ydbz_qd" name="orderpersonMes">
					<li><label><span class="xing">*</span>渠道联系人<font>${s.count+1}</font>：</label><input maxlength="10" type="text" name="contactsName" value="${orderContact.contactsName}" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/></li>
					<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label><input maxlength="15" type="text" name="contactsTel" value="${orderContact.contactsTel}" class="required" onkeyup="this.value=this.value.replace(/[^\\d\+\\-]/g,\\)"  onafterpaste="this.value=this.value.replace(/[^\\d\+\\-]/g,\\)"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,\\,\2\)">展开全部</div><span class="ydbz_x gray" onclick="yd1DelPeople(this)">删除联系人</span></li>
					<li flag="messageDiv" style="display:none" class="ydbz_qd_close">
						<ul>
							<li><label>固定电话：</label><input type="text" name="contactsTixedTel" value="${orderContact.contactsTixedTel}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/></li>
							<li><label>渠道地址：</label><input type="text" name="contactsAddress" value="${orderContact.contactsAddress}"/></li>
					        <li><label>传真：</label><input type="text" name="contactsFax" value="${orderContact.contactsFax}"/></li>
					        <li><label>QQ：</label><input type="text" name="contactsQQ" value="${orderContact.contactsQQ}"/></li>
					        <li><label>Email：</label><input type="text" name="contactsEmail" value="${orderContact.contactsEmail}"/></li>
					        <li><label>渠道邮编：</label><input type="text" name="contactsZipCode" value="${orderContact.contactsZipCode}"/></li>
					        <li><label>其他：</label><input type="text" name="remark" value="${orderContact.remark}"/></li>
				        </ul>
			        </li>
			     </ul>
		     </c:forEach>
	    </div>
	</form>
</div>
<div class="ydBtn-down orderPersonMsg" style="display:none;" ><span>&nbsp;</span></div>
<div class="ydbz_sxb" id="oneToSecondOutStepDiv">
   <div class="ydBtn" id="oneToSecondStepDiv"><span class="ydbz_x" >下一步</span></div>
</div>
<!-- 特殊需求 -->
<div id="manageOrder_m" style="display:none;">
	<div id="contact">
	   <div class="ydbz_tit">特殊需求</div>
	   <div class="ydbz2_lxr" flag="messageDiv">
	      <form class="contactTable">
	          <p>
	             <label style="vertical-align:top">特殊需求：</label>
	             <textarea id="specialDemand" name="specialDemand" maxlength="100">${productorder.specialDemand}</textarea>
	          </p>
	      </form>
	   </div>
	</div>
</div>