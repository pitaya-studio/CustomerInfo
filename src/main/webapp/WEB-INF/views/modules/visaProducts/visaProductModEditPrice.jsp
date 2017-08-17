<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>产品-签证产品修改-填写价格</title>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/validationRules.js"></script>
<script type="text/javascript">
$(function(){
	//币种选择
	selectCurrencyVisa();
	
	//验证
	$("#addForm").validate({
		rules:{
			visaPrice:{
				required:true,
				number:true
			},
			visaPay:{
				required:true,
				number:true
			},
			otherCost:"number"
		},
		errorPlacement: function(error, element) {
            error.appendTo ( element.parent() );
        }
	});
	jQuery.extend(jQuery.validator.messages, {
        required: "必填信息",
        number: "输入数字"
    });
});

function secondToThird(){
  $("#addForm").submit();
}


function secondToOne(){
  var proId=$("#productId").val();
  $("#addForm").attr("action","${ctx}/visa/visaProducts/mod/"+proId);
  $("#addForm").submit();
}


</script>
</head>
<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">修改签证产品</page:param>
</page:applyDecorator>
    <!--右侧内容部分开始-->
    <div class="produceDiv">
        <div style="width:100%; height:20px;"></div>
        <div class="visa_num visa_num2" id="dh"></div>
        <form:form id="addForm" modelAttribute="visaProduct" action="${ctx}/visa/visaProducts/toModVisaPriceUploadFile" method="post" cssClass=" form-search" enctype="multipart/form-data">
            <input id="productId" type="hidden" name="id" value="${visaProduct.id }"/>
            <input id="sysCountryId" type="hidden" name="sysCountryId" value="${visaProduct.sysCountryId }"/>
            <input id="visaType" type="hidden" name="visaType" value="${visaProduct.visaType}"/>
            <input id="collarZoning" type="hidden" name="collarZoning" value="${visaProduct.collarZoning }"/>
            <input id="productName" type="hidden" name="productName" value="${visaProduct.productName }"/>
            <input id="contactPerson" type="hidden" name="contactPerson" value="${visaProduct.contactPerson }"/>
            <input id="reserveMethod" type="hidden" name="reserveMethod" value="${visaProduct.reserveMethod}"/>
            <input id="stayTime" type="hidden" name="stayTime" value="${visaProduct.stayTime}"/>
            <input id="remark" type="hidden" name="remark" value="${visaProduct.remark }"/>
            <input id="deptId" type="hidden" name="deptId" value="${visaProduct.deptId }"/>
            <input id="groupCode" type="hidden" name="groupCode" value="${visaProduct.groupCode }"/>
<%--             <input id="payableDate" type="hidden" name="payableDate" value="${visaProduct.payableDate}"/> --%>
            
            <div class="mod_information_dzhan" id="oneStepDiv">
                <div class="kongr"></div>
                <div class="kongr"></div>
                <div class="mod_information_d7"></div>
                <div class="kongr"></div>
                <div class="kongr"></div>
                <div class="mod_information_dzhan_d error_add1" id="oneStepContent">
                    <div class="mod_information_d1">
                        <label><span class="xing displayClick">*</span>产品名称：</label>
                        <span class="disabledshowspan">${visaProduct.productName}</span>
                    </div>
                    <div class="kongr"></div>

                    <div class="mod_information_d2">
                        <label>签证国家：</label>
                        <span class="disabledshowspan">
                          ${sysCountry}
                        </span>
                    </div>
                    <div class="mod_information_d2">
                        <label>签证领区：</label>
                        <span class="disabledshowspan">
                           <c:if test="${not empty visaProduct.collarZoning }">
	                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
                        	</c:if>
                        </span>
                    </div>
                    <div class="mod_information_d2">
                        <label>签证类型：</label>
                        <span class="disabledshowspan">
                        	${visaType}
                        </span>
                    </div>
                    <label>领区联系人：</label>
                          <span class="disabledshowspan">${visaProduct.contactPerson}</span>
                    
                    <div class="kongr"></div>
                    <div class="mod_information_d2">
                    	<label>部门：</label>
                        <span class="disabledshowspan">${deptName}</span>
                    </div>
                    <!-- C460 -->
                    <c:if test="${fns:getUser().company.groupCodeRuleQZ eq 0}"><!-- 0:手动输入 --> 
                    	<div class="mod_information_d2">
                    		<label>团号：</label>
                        	<span class="disabledshowspan">${visaProduct.groupCode}</span>
                   		</div>
                    </c:if>
                    <!-- C460V3 所有批发商展示团号 -->
                    <c:if test="${fns:getUser().company.groupCodeRuleQZ eq 1}"><!-- 1:自动生成（系统默认） -->
                     	<div class="mod_information_d2">
                    		<label>团号：</label>
                        	<span class="disabledshowspan">${visaProduct.groupCode}</span>
                   		</div>
                    </c:if>
                     
                    <div class="kongr"></div>
                 	<div class="mod_information_d2  add-paytype">
                        <label>预定方式：</label>
                        <font style="" id="payModeText"> 
	                        <c:choose>
	                            <c:when test="${visaProduct.reserveMethod eq '1'}">
	                          		预定<!-- （保留${visaProduct.stayTime}天） -->
	                            </c:when>
	                            <c:when test="${visaProduct.reserveMethod eq '2'}">
	                          		付全款
	                            </c:when>
	                            <c:otherwise>
	                            	预定<!-- （保留${visaProduct.stayTime}天） -->,付全款
	                            </c:otherwise>
	                        </c:choose>
                        </font>
                    </div>
                    <div class="kongr"></div>
                    <!--备注开始-->
                    <div style="width: 100%; margin-bottom: 30px;" class="mod_information_d2 pro-marks">
                        <label>备注：</label>
                             <span class="disabledshowspan" style="word-break: break-all">${visaProduct.remark}</span>
                    </div>
                    <!--备注结束-->

                </div>
            </div>

            <div class="mod_information" id="secondStepDiv">
                <div class="mod_information_d" id="secondStepTitle" style="clear: both;" ><span style=" font-weight:bold; padding-left:20px;float:left">签证价格</span></div>
                <div id="secondStepContent">
                    <div style="width:100%; height:10px;"></div>
                    <div class="add2_nei">
                        <table class="table-mod2-group planeTick-table">
                            <tbody>
                            <tr>
                                <td class="add2_nei_table">币种选择：</td>
                                <td class="add2_nei_table_typetext" style="width:200px;">
                                    <select class="sel-currency" id="selectCurrency" name="currencyId" nowClass="rmb">
                                        <c:forEach items="${curlist}" var="currency">
                                            <option value="${currency.id}" id="${currency.currencyMark}" <c:if test="${currency.id==visaProduct.currencyId}">selected="selected"</c:if>>${currency.currencyName}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td class="add2_nei_table">应付账期：</td>
                                <td class="add2_nei_table_typetext" style="width:200px;">
                                    <input maxlength="20" id="payableDate"
                                     name="payableDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${visaProduct.payableDate}"/>"
                                     class="dateinput valid" onfocus="WdatePicker()"
                                     type="text">
                                </td>
                            </tr>
                            <tr>
                                <td class="add2_nei_table">成本价格：</td>
                                <td class="add2_nei_table_typetext" style="width:200px;">
                                	<span></span>
                                	<input type="text" maxlength="8" id="visaPrice" value="<fmt:formatNumber pattern="###0.00" value="${visaProduct.visaPrice}" />" name="visaPrice" class="ipt-currency valid" />
                                </td>
                                <td class="add2_nei_table">应收价格：</td>
                                <td class="add2_nei_table_typetext" style="width:200px;">
                                	<span></span>
                                	<input type="text" maxlength="8" id="visaPay"  value="<fmt:formatNumber pattern="###0.00" value="${visaProduct.visaPay}"/>" name="visaPay" class="ipt-currency valid" />
                               	</td>
                               	<!-- 
                                <td class="add2_nei_table">其它费用：</td>
                                <td class="add2_nei_table_typetext">
                                	<span></span>
                                	<input type="text" maxlength="10" id="otherCost"  value="<fmt:formatNumber pattern="#.00" value="${visaProduct.otherCost}"/>"  name="otherCost" class="ipt-currency valid" />
                                </td>
                                 -->
                            </tr>
                            <!-- 0258-qyl -->
                           
                            <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
                              <tr>
                                <td class="add2_nei_table">发票税：</td>
                                <td class="add2_nei_table_typetext" style="width:200px;">
                                	<input type="text" id="invoiceQZ" value="<fmt:formatNumber pattern="#0.00" value="${visaProduct.invoiceQZ}" />" name="invoiceQZ" class="ipt-currency valid" onkeyup="checkValue(this);" onafterpaste="checkValue(this);" onfocus="checkValue(this);" />&nbsp;%
                                </td>
                              </tr>
                           </c:if>  
                            </tbody></table>
                        <div class="kong"></div>
                    </div>
                </div>
                <div class="mod_information_dzhan_d" id="secondStepBtn">
                    <div class="release_next_add">
                        <input type="button" value="上一步" onclick="secondToOne()" class="btn btn-primary">
                        <input type="button" value="下一步" onclick="secondToThird()" class="btn btn-primary">
                    </div>
                </div>
<%--                这里做隐藏，用于第三步显示--%>
                <div class="mod_information_3" style="display: none">
                    <div id="otherflag" style="margin-top:8px;" >
                        <label>需提供原件项目：</label><span class="seach_check">
			              <input type="checkbox" name="original_Project_Type" value="0" <c:if test="${fn:contains(visaProduct.original_Project_Type,'0')}">checked="checked"</c:if> id=""><label for="">护照</label></span><span class="seach_check">
			              <input type="checkbox" name="original_Project_Type" value="1" <c:if test="${fn:contains(visaProduct.original_Project_Type,'1')}">checked="checked"</c:if> id=""><label for="">身份证</label></span><span class="seach_check">
			              <!-- C197-start -->
			              <input type="checkbox" name="original_Project_Type" value="3" <c:if test="${fn:contains(visaProduct.original_Project_Type,'3')}">checked="checked"</c:if> id=""><label for="">电子照片</label></span><span class="seach_check">
			              <input type="checkbox" name="original_Project_Type" value="4" <c:if test="${fn:contains(visaProduct.original_Project_Type,'4')}">checked="checked"</c:if> id=""><label for="">申请表格</label></span><span class="seach_check">
			              <input type="checkbox" name="original_Project_Type" value="5" <c:if test="${fn:contains(visaProduct.original_Project_Type,'5')}">checked="checked"</c:if> id=""><label for="">户口本</label></span><span class="seach_check">
			              <input type="checkbox" name="original_Project_Type" value="6" <c:if test="${fn:contains(visaProduct.original_Project_Type,'6')}">checked="checked"</c:if> id=""><label for="">房产证</label></span><span class="seach_check">
			              <!-- C197-end -->
			              <span class="seach_check">
				              <input type="checkbox" name="original_Project_Type" value="2" <c:if test="${fn:contains(visaProduct.original_Project_Type,'2')}">checked="checked"</c:if>  id=""><label for="">其他</label>
				              <input type="text" name="original_Project_Name" value="${visaProduct.original_Project_Name}" class="input-mini">
			              </span>
                    </div>
                    <div id="otherflag" style="margin-top:8px;">
                        <label>需提供复印件项目：</label><span class="seach_check">
			              <!-- 197-start -->
			              <input type="checkbox" name="copy_Project_Type" value="3" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'3')}">checked="checked"</c:if>  /><label for="">护照</label></span><span class="seach_check">
			              <input type="checkbox" name="copy_Project_Type" value="4" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'4')}">checked="checked"</c:if> id=""><label for="">身份证</label></span><span class="seach_check">
			              <input type="checkbox" name="copy_Project_Type" value="5" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'5')}">checked="checked"</c:if>  /><label for="">电子照片</label></span><span class="seach_check">
			              <input type="checkbox" name="copy_Project_Type" value="6" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'6')}">checked="checked"</c:if> id=""><label for="">申请表格</label></span><span class="seach_check">
			              <input type="checkbox" name="copy_Project_Type" value="0" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'0')}">checked="checked"</c:if>  /><label for="">户口本</label></span><span class="seach_check">
			              <input type="checkbox" name="copy_Project_Type" value="1" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'1')}">checked="checked"</c:if> id=""><label for="">房产证</label></span><span class="seach_check">
			              <!-- 197-end -->
			              <span class="seach_check">
				              <input type="checkbox" name="copy_Project_Type"  value="2" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'2')}">checked="checked"</c:if> id=""><label for="">其他</label>
				              <input type="text" name="copy_Project_Name" value="${visaProduct.copy_Project_Name}" class="input-mini">
			              </span>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
    <!--右侧内容部分结束-->
</body>
</html>