
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <title>修改签证基本信息</title>
    
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
    <script src="${ctxStatic}/modules/visa/visa.js" type="text/javascript"></script>
    


    
    
     <script type="text/javascript">
     
     var countryId = "";
     
  	$(function() {
        var spinner = $("#stay_time").spinner();

        $(".spinner").spinner({
            spin: function (event, ui) {
                if (ui.value > 365) {
                    $(this).spinner("value", 1);
                    return false;
                } else if (ui.value <= 0) {
                    $(this).spinner("value", 365);
                    return false;
                }
            }
        });
  	
  	//国家和领区选择框控件
		$("#sysCountryId" ).comboboxInquiry({
			"afterInvalid":function(event,data){
				$(this).trigger("click");
			}
		});
		$("#collarZoning" ).comboboxInquiry({
			"afterInvalid":function(event,data){
				$(this).trigger("click");
			}
		});
		
		$("#sysCountryId").next().find(".custom-combobox-input").blur(function(){
			if(countryId != $("#sysCountryId").val()) {
				countryId = $("#sysCountryId").val();
				getArea();
			}
		});
		
		//国家、领区联动
		function getArea() {
			if(countryId != '') {
				$.ajax({
					type:"POST",
					url:"${ctx}/visa/visaProducts/getArea?countryId="+countryId,
					async : false,
					success:function(result){
						$("#collarZoning").empty().append('<option value=" ">请选择</option>');
						if(result != null && result.length != 0) {
							for(var index = 0; index < result.length; index++) {
								$("#collarZoning").append('<option value=\'' + result[index][0] + '\'>' + result[index][1] + '</option>');
							}
						}
						$("#collarZoning" ).comboboxInquiry({
							"afterInvalid":function(event,data){
								$(this).trigger("click");
							}
						});
						$("#collarZoning").next().find(".custom-combobox-input").val("请选择");
					}
				});
			}else if(countryId == ''){
				$("#collarZoning").empty().append('<option value=" ">请选择</option>');
				$("#collarZoning").next().find(".custom-combobox-input").val("请选择");
			}
		}
		
		//验证
		$("#addForm").validate({
			rules:{
				productName:"required",
				sysCountryId:"visaRequired",
				collarZoning:"visaRequired",
				reserveMethod:"required",
				visaType:"required",
				groupCode:"required"
			},
			errorPlacement: function(error, element) {
                error.appendTo ( element.parent() );
            },
			ignore: ""
		});
		jQuery.extend(jQuery.validator.messages, {
            required: "必填信息"
        });
		
		//选中checkbox
		var methodArr = "${visaProduct.reserveMethod}".split(",");
		$(methodArr).each(function(index, obj) {
			$("[name='reserveMethod'][value='" + obj + "']").attr("checked","checked");
		});
    });
  	
  	/**
	  * 团号超过50字提醒
	  * @param {} obj
	  */
	 var flag=10;
	 function validateLong(obj)
	 {
	 	replaceStr(obj);
	 	if($(obj).val().length<=49){
	 		flag = 10;
	 	}
	 	if($(obj).val().length>=50)
	 		{ 
	 			if($(obj).val().length==50 && flag==10){
	 				//$.jBox.tip("团号只能输入50个字符","true");
	 				flag++;
	 			}else{
	 				$.jBox.tip("团号超过50个字符，请修改","error");  
	 			}
	 			return false;  
	 		}
	 }

  </script> 
  </head>
  
  <body>
  <page:applyDecorator name="show_head">
      <page:param name="desc">修改签证基本信息</page:param>
  </page:applyDecorator>
          <!--右侧内容部分开始-->
          <div class="produceDiv">
              <div style="width:100%; height:20px;"></div>
              <div class="visa_num"></div>
              <form:form id="addForm" modelAttribute="visaProduct" action="${ctx}/visa/visaProducts/editModVisaPrice" method="post" class=" form-search" enctype="multipart/form-data">
                  <!-- C460 判断当前团号是否为手动输入 -->
              	  <input type="hidden" id="groupCodeRuleQZ" value="${fns:getUser().company.groupCodeRuleQZ }" />
              	  <!-- 产品修改前的团号 -->
              	  <input type="hidden" id="groupCodeOld" value="${visaProduct.groupCode}" />
                  <div class="mod_information_dzhan" id="oneStepDiv">
                      <div class="kongr"></div>
                      <div class="kongr"></div>
                      <div class="mod_information_d7"></div>
                      <div class="kongr"></div>
                      <div class="kongr"></div>
                      <div class="mod_information_dzhan_d error_add1" id="oneStepContent">
                          <div class="mod_information_d1">

                              <label><span class="xing">*</span>产品名称：</label>
                              <input type="hidden"  value="${visaProduct.id}"  name="id" id="visaProductId">
                              <input type="hidden"  value="${visaProduct.deptId}" name="deptId" id="deptId"/>
                              <input type="hidden"  value="${visaProduct.visaPrice}"  name="visaPrice">
                              <input type="hidden"  value="${visaProduct.visaPay}"  name="visaPay">
                              <input type="hidden"  value="${visaProduct.invoiceQZ}" name="invoiceQZ">
                              <input type="hidden"  value="${visaProduct.otherCost}"  name="otherCost">
                              <input type="hidden"  value="${visaProduct.currencyId}"  name="currencyId">
                              <input type="hidden"  value="${visaProduct.original_Project_Type}"  name="original_Project_Type">
                              <input type="hidden"  value="${visaProduct.original_Project_Name}"  name="original_Project_Name">
                              <input type="hidden"  value="${visaProduct.copy_Project_Type}"  name="copy_Project_Type">
                              <input type="hidden"  value="${visaProduct.copy_Project_Name}"  name="copy_Project_Name">
                           
                              <input type="text" maxlength="50" value="${visaProduct.productName}" class="inputTxt" name="productName" id="productName">
<%--                              <span style="color:#b2b2b2" class="acitivityNameSizeSpan">还可输入<span class="productNameSize">50</span>个字</span>--%>
                              <!--  <label for="productName" class="error" id="productNameError" style="display:none;">必填信息</label>--></div>
                          
                          <div class="kongr"></div>
                          <div class="mod_information_d2">
                              <label><span class="xing">*</span>签证国家：</label>
                              <select name="sysCountryId" id="sysCountryId">
                           		  <option value=" ">请选择</option>
                                  <c:forEach items="${countryInfoList}" var="visaCountry">
                                      <option value="${visaCountry[0]}" <c:if test="${visaCountry[0] eq visaProduct.sysCountryId}">selected="selected"</c:if>  ><c:out value="${visaCountry[1]}" /></option>
                                  </c:forEach>
                              </select>
                          </div> 
                          <div class="mod_information_d2">
                              <label><span class="xing">*</span>签证领区：</label>
                              <select name="collarZoning" id="collarZoning">
                              <option value=" ">请选择</option>
                              <c:forEach items="${collarZoningList}" var="zoning">
                              		<option value="${zoning[0]}" <c:if test="${zoning[0] eq visaProduct.collarZoning }">selected="selected"</c:if> >${zoning[1]}</option>
                              </c:forEach>
                              </select>
                          </div>
                          <div class="mod_information_d2">
                              <label><span class="xing">*</span>签证类型：</label>
                              <select name="visaType" id="visaType">
                              		<option value=" ">请选择</option>
                                  <c:forEach items="${visaTypeList}" var="visaType">
                                      <option value="${visaType.key}" <c:if test="${visaType.key eq visaProduct.visaType}">selected="selected"</c:if> ><c:out value="${visaType.value}" /></option>
                                  </c:forEach>
                              </select>
                          </div>
                           <label>领区联系人：</label>
                          <input type="text" value="${visaProduct.contactPerson }" name=contactPerson id="contactPerson" maxlength="20">
                          
                          <div class="kongr"></div>
                          <div class="mod_information_d2 add-paytype">
                              <label><span class="xing">*</span>预定方式：</label>
                              <form:checkbox path="reserveMethod" value="1" cssClass="ckb_mod valid"/>
                              <font>预定</font>
<!--                               <label id="label_advance" class="txt2" for="spinner"> -->
<!--                                   <span style="display: none;" id="advance_xing" class="xing">*</span>保留天数：</label> -->
<!--                                     <span id="payMode_advance_span"> -->

<%--                                     <input class="spinner ui-spinner-input" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" autocomplete="off" value="${visaProduct.stayTime}" maxlength="3" name="stayTime"  id="stayTime" > --%>
<!--                                          <span class="ui-spinner ui-widget ui-widget-content ui-corner-all"><input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" value="" maxlength="3" name="remainDays_advance" class="spinner ui-spinner-input" id="remainDays_advance" autocomplete="off" role="spinbutton" disabled="disabled"><a class="ui-spinner-button ui-spinner-up ui-corner-tr ui-button ui-widget ui-state-default ui-button-text-only" tabindex="-1" role="button" aria-disabled="false"><span class="ui-button-text"><span class="ui-icon ui-icon-triangle-1-n">▲</span></span></a><a class="ui-spinner-button ui-spinner-down ui-corner-br ui-button ui-widget ui-state-default ui-button-text-only" tabindex="-1" role="button" aria-disabled="false"><span class="ui-button-text"><span class="ui-icon ui-icon-triangle-1-s">▼</span></span></a></span> -->
<!--                                         <span style="padding-left:5px;">天</span> -->
<!--                                     </span> -->
                              <br>
                              <label> </label>
                              <form:checkbox path="reserveMethod" value="2" cssClass="ckb_mod valid"/>
                              <font>付全款</font>
                          </div>
                          <!-- C460 -->
                          <c:if test="${fns:getUser().company.groupCodeRuleQZ eq 0}"><!-- 0:手动输入 --> 
                          	<div class="kongr"></div>
                          	<div class="mod_information_d2">
                            	<label><span class="xing">*</span>团号：</label>
                              	<input type="text" class="inputTxt inputTxtlong" id="groupCode" name="groupCode"
									onafterpaste="replaceStr(this)" onkeyup="validateLong(this)"
									maxlength="50" value="${visaProduct.groupCode }">
                          	</div>
                          </c:if>
                          <!-- C460V3 所有批发商展示团号-->
                          <c:if test="${fns:getUser().company.groupCodeRuleQZ eq 1}"><!-- 1:自动生成（系统默认） --> 
                          	<div class="kongr"></div>
                          	<div class="mod_information_d2 ">
                          	  <label>团号：</label><span>${visaProduct.groupCode }</span>
                          	</div>
                          	<input type="hidden"  id="groupCode" name="groupCode" value="${visaProduct.groupCode }"/>
                          </c:if>
                          
                          <div class="kongr"></div>
                          <div class="mod_information_d2 ">
                          	  <label>部门：</label><span>${deptName }</span>
                          </div>
                          <div class="kongr"></div>
                          <!--备注开始-->
                          <div class="mod_information_d2 pro-marks">
                              <label>备注：</label>

                              <textarea  name="remark" maxlength="2000">${visaProduct.remark}</textarea>

                          </div>
                          <!--备注结束-->
                      </div>
                  </div>
                  
              </form:form>
              <div class="ydBtn" id="oneStepBtn">
                      <input type="button" value="下一步" onclick="findMoreProduct('${ctx}')"  class="btn btn-primary valid">
                  </div>
          </div>
</body>
<script type="text/javascript" chartset="UTF-8">
$(function(){
	$("#productName").bind("mouseover keyup",function(){
		$(".productNameSize").html(50-$("#productName").val().length);
		if($("#productName").val().length==0){
			$("#productNameError").show();
		}else{
			$("#productNameError").hide();
		}	
	});
});
</script>
</html>