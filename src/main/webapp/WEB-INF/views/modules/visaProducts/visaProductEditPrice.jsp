
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>产品-签证产品发布-填写价格</title>

<!-- 	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script> -->
	<script src="${ctxStatic}/modules/visa/visaProductsList.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<!-- 0258-qyl -->
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
    
    //返回时，保留数据
    var visaPrice = "${visaPrice}";
    var visaPay = "${visaPay}";
    var otherCost = "${otherCost}";
    var currencyId = "${currencyId}";
    if(visaPrice != "" && visaPrice != null){
    	$("#visaPrice").val(visaPrice);
    	$("#visaPay").val(visaPay);
    	if(otherCost != null && otherCost != null){
    		$("#otherCost").val(otherCost);
    	}
    	$("#selectCurrency option").each(function(){
    		var txt = $(this).val();
    		if(txt == currencyId){
    			$(this).attr("selected","true");
    		}
    	});
    }
    
    var currencyIdChk = "${currencyIdChk}";
    if(currencyIdChk != "" && currencyIdChk != null){
    	$("#selectCurrency option").each(function(){
    		var txt = $(this).val();
    		if(txt == currencyIdChk){
    			$(this).attr("selected","true");
    		}
    	});
    }
});

function secondToThird(){
	//成本价格
// 	var visaPrice = $("#visaPrice").val();
	//应收价格
// 	var visaPay = $("#visaPay").val();
// 	if(parseInt(visaPay) < parseInt(visaPrice))
// 		{
// 			$.jBox.info("应收价格必须大于或者等于成本价格！", "警告");
// 			return false;
// 		}
  $("#addForm").submit();
}


function secondToOne(){
  //$("#addForm").attr("action","${ctx}/visa/visaProducts/addVisaInformation.htm");
  //$("#addForm").submit();
  var countryVisaArea = $.trim($("#countryVisaArea").text());
  var payModeText = $.trim($("#payModeText").text());
  var valueString = "${sysCountry}" + ";" + countryVisaArea + ";" + "${visaType}" + ";" + payModeText;
  
  var selectCurrency = $("#selectCurrency").val();
  var visaPrice = $("#visaPrice").val();
  var visaPay = $("#visaPay").val();
  var otherCost = $("#otherCost").val();
  var payList = selectCurrency + "," + visaPrice + "," + visaPay + "," + otherCost;
 
  window.location.href="${ctx}/visa/visaProducts/addVisaInformation?valueString="+valueString+"&payList="+payList;
}

</script>
</head>
<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">发布签证产品</page:param>
</page:applyDecorator>
    <!--右侧内容部分开始-->
    <div class="produceDiv">
        <div style="width:100%; height:20px;"></div>
        <div class="visa_num visa_num2" id="dh"></div>
        <form:form id="addForm" modelAttribute="visaProduct" action="${ctx}/visa/visaProducts/toVisaPriceUploadFile" method="post" class=" form-search" enctype="multipart/form-data">
            <input id="sysCountryId" type="hidden" name="sysCountryId" value="${visaProduct.sysCountryId }"/>
            <input id="sysCountryId" type="hidden" name="sysCountryId" value="${visaProduct.sysCountryId }"/>
            <input id="visaType" type="hidden" name="visaType" value="${visaProduct.visaType}"/>
            <input id="collarZoning" type="hidden" name="collarZoning" value="${visaProduct.collarZoning }"/>
            <form:hidden path="deptId"/>
            <form:hidden path="groupCode"/>
            <input id="productName" type="hidden" name="productName" value="${visaProduct.productName }"/>
            <input id="contactPerson" type="hidden" name="contactPerson" value="${visaProduct.contactPerson }"/>
            <input id="reserveMethod" type="hidden" name="reserveMethod" value="${visaProduct.reserveMethod}"/>
            <input id="stayTime" type="hidden" name="stayTime" value="${visaProduct.stayTime}"/>
            <input id="remark" type="hidden" name="remark" value="${visaProduct.remark }"/>
            <div class="mod_information_dzhan" id="oneStepDiv">
                <div class="kongr"></div>
                <div class="kongr"></div>
                <div class="mod_information_d7"></div>
                <div class="kongr"></div>
                <div class="kongr"></div>
                <div class="mod_information_dzhan_d error_add1 iptToTxt" id="oneStepContent">
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
                        <span class="disabledshowspan" id="countryVisaArea">
                        	<c:if test="${not empty visaProduct.collarZoning }">
	                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
                        	</c:if>
                        </span>
                    </div>
                    <label>领区联系人：</label>
                          <span class="disabledshowspan">${visaProduct.contactPerson}</span>
                    <div class="mod_information_d2">
                        <label>签证类型：</label>
                        <span class="disabledshowspan">
                        	${visaType}
                        </span>
                    </div>
                    <div class="kongr"></div>
                    <div class="mod_information_d2">
                        <label>部门：</label>
                        <span class="disabledshowspan">
                        	${deptName}
                        </span>
                    </div>
                    <!-- C460 团号 -->
					<c:if test="${fns:getUser().company.groupCodeRuleQZ eq 0}"><!-- 0:手动输入 -->
						<div class="mod_information_d2">
							<label>团号：</label> 
							<span class="disabledshowspan">
								${groupCode} 
							</span>
						</div>
					</c:if>

					<div class="mod_information_d2  add-paytype">
                        <label>预定方式：</label>
                        <font style="" id="payModeText"> 
	                        <c:choose>
	                            <c:when test="${visaProduct.reserveMethod eq '1'}">
	                          		预定（保留${visaProduct.stayTime}天）
	                            </c:when>
	                            <c:when test="${visaProduct.reserveMethod eq '2'}">
	                          		付全款
	                            </c:when>
	                            <c:otherwise>
	                            	预定（保留${visaProduct.stayTime}天）,付全款
	                            </c:otherwise>
	                        </c:choose>
                        </font>
                    </div>
                    
                    <div class="kongr"></div>
                    <!--备注开始-->
                    <div style="width: 100%; margin-bottom: 30px;height: auto;" class="mod_information_d2 pro-marks">
                        <label>备注：</label>
	                    <span class="disabledshowspan" style="word-break: break-all">
	                            ${visaProduct.remark}
	                    </span>
                    </div>
                    <!--备注结束-->

                </div>
            </div>

            <div class="mod_information" id="secondStepDiv">
                <div class="mod_information_d" id="secondStepTitle" style="clear: both;"><span style=" font-weight:bold; padding-left:20px;float:left">签证价格</span></div>
                <div id="secondStepContent">
                    <div style="width:100%; height:10px;"></div>
                    <div class="add2_nei">
                        <table class="table-mod2-group planeTick-table">
                            <tbody><tr>
                                <td class="add2_nei_table">币种选择：</td>
                                <td class="add2_nei_table_typetext">

                                    <select class="sel-currency" id="selectCurrency" name="currencyId" nowClass="rmb">
                                        <c:forEach items="${curlist}" var="currency">
                                            <option value="${currency.id}" <c:if test="${currency.currencyStyle eq 'rmb'}">selected="selected"</c:if> id="${currency.currencyMark}">${currency.currencyName}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                
                                <td >应付账期：</td>
                                     <td>
                                         <input maxlength="20" id="payableDate"
                                                name="payableDate" value=""
                                                class="dateinput valid" onfocus="WdatePicker()"
                                                type="text">
                                     </td>
                            </tr>
                            <tr>
                                <td class="add2_nei_table"><span class="xing">*</span>成本价格：</td>
                                <td class="add2_nei_table_typetext" style="width:200px;">
                                	<span></span>
                                	<input type="text" maxlength="8" id="visaPrice" value="${visaPriceChk}" name="visaPrice" class="ipt-currency valid" />
                                </td>
                                <td class="add2_nei_table"><span class="xing">*</span>应收价格：</td>
                                <td class="add2_nei_table_typetext" style="width:200px;">
                                	<span></span>
                                	<input type="text" maxlength="8" id="visaPay" value="${visaPayChk}" name="visaPay" class="ipt-currency valid" />
                                </td>
                                <!-- 
                                <td class="add2_nei_table">其它费用：</td>
                                <td class="add2_nei_table_typetext">
                                	<span></span>
                                	<input type="text" maxlength="10" id="otherCost" value="${otherCostChk}"  name="otherCost" class="ipt-currency valid" />
                                </td>
                                 -->
                            </tr>
                            <!--0258-qyl-s:限定为懿洋假期  -->
                            <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
                              <tr>
                                <td class="add2_nei_table">发票税：</td>
                                <td class="add2_nei_table_typetext" style="width:200px;">
                                	<input type="text"  id="invoiceQZ" value="${invoiceQZ}" name="invoiceQZ" class="ipt-currency valid" onkeyup="checkValue(this);" onafterpaste="checkValue(this);" onfocus="checkValue(this);" />&nbsp;%
                                </td>
                            </tr>
                            </c:if>
                             <!--0258-qyl-e  -->
                            </tbody></table>
                        <div class="kong"></div>
                    </div>

                </div>
                <div class="mod_information_dzhan_d" id="secondStepBtn">
                    <div class="release_next_add">
                        <!-- 
                        <input type="button" value="上一步" onclick="javascript:window.location.href='${ctx}/visa/visaProducts/addVisaInformation.htm'" class="btn btn-primary">
                         -->
                         <!-- <input type="button" value="上一步" onclick="secondToOne()" class="btn btn-primary"> -->
                        <input type="button" value="下一步" onclick="secondToThird()" class="btn btn-primary">
                    </div>
                </div>
            </div>
        </form:form>
    </div>
    <!--右侧内容部分结束-->
</body>
</html>