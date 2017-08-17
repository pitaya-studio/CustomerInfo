<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>签证填写基本信息</title>

	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
    <script src="${ctxStatic}/modules/visa/visa.js" type="text/javascript"></script>
    <style type="text/css">
    	.ydBtn {
    width: 78px;
    margin-left: auto;
    margin-right: auto;
    margin-top: 300px;
    height: 28px;
    padding-bottom: 10px;
	}    	
    </style>
    <script type="text/javascript">    
    var countryId = "";   
  	$(function() {		
  		
  		$("#productName").val("");//处理修改后添加后 产品名称缓存的问题
  		
       	var spinner = $( "#stay_time" ).spinner();
 	    
		$( ".spinner" ).spinner({
			spin: function( event, ui ) {
			if ( ui.value > 365 ) {
				$( this ).spinner( "value", 1 );
				return false;
			} else if ( ui.value <= 0 ) {
				$( this ).spinner( "value", 365 );
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
				deptId:"required",
				groupCode:"required"
			},
			errorPlacement: function(error, element) {
                error.appendTo ( element.parent() );
                //处理保留时间的校验
                if($(element).hasClass("spinner"))
                    error.appendTo (element.parent().parent());
            },
			ignore: ""
		});
		jQuery.extend(jQuery.validator.messages, {
            required: "必填信息"
        });
		
		
		
		$(document).on('click','.departmentButton',function(){
			var $currentClick=$(this);
				var url = "/sys/department/treeData?officeId=" + ${deptId};
				// 正常打开	
				top.$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent(url)+"&module=&checked=&extId=&selectIds="+$currentClick.prev().prev().val(), "选择部门", 300, 420,{
					buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
						if (v=="ok"){
							var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
							var ids = [], names = [], nodes = [];
							if ("" == "true"){
								nodes = tree.getCheckedNodes(true);
							}else{
								nodes = tree.getSelectedNodes();
							}
							for(var i=0; i<nodes.length; i++) {//
								ids.push(nodes[i].id);
								names.push(nodes[i].name);//
								break; // 如果为非复选框选择，则返回第一个选择  
							}
							$currentClick.prev().val(names);
							$currentClick.prev().prev().val(ids);
//		 					$("#departmentId").val(ids);
//		 					$("#departmentName").val(names);
							$("#departmentName").focus();
							$("#departmentName").blur();
						}
					}, loaded:function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					},persistent:true
				});
			});
		
		
    });
  	
    //产品名称输入长度  
    $("#productName").live("keyup",function(){
        getAcitivityNameLength();
    });
    $("#productName").live("blur",function(){
        getAcitivityNameLength();
    });
    function getAcitivityNameLength() {
        var acitivityNameLength = 50-($("#productName").val().length);
        if(acitivityNameLength>=0){
            $(".productNameSize").text(acitivityNameLength);
        }
    }
    
    //处理保留时间的校验
    function paychg(obj) {
    	if($(obj).prop("checked")){
    		$(obj).next().next().next().find("span").css("display","inline");
    		$(obj).next().next().next().next().find("input[name^='stayTime']").rules("remove");
        	$(obj).next().next().next().next().find("input[name^='stayTime']").removeAttr("disabled");
    		$(obj).next().next().next().next().find("input[name^='stayTime']").rules("add",{required:true,digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能为0"}});
    	}else{
    		$(obj).next().next().next().find("span").css("display","none");
    		$(obj).next().next().next().next().find("input[name^='stayTime']").rules("remove");
        	$(obj).next().next().next().next().find("input[name^='stayTime']").val("");
    	}
    }
    
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
          <page:param name="desc">签证基本信息</page:param>
      </page:applyDecorator>
          <!--右侧内容部分开始-->
          <div class="produceDiv">
              <div style="width:100%; height:20px;"></div>
              <div class="visa_num"></div>
              <form:form id="addForm" modelAttribute="visaProducts" action="${ctx}/visa/visaProducts/editVisaPrice" method="post" class="form-search" enctype="multipart/form-data">
              	<input type="hidden" id="selectCurrency" value="${selectCurrency}" />
              	<input type="hidden" id="visaPrice" value="${visaPrice }" />
              	<input type="hidden" id="visaPay" value="${visaPay }" />
              	<input type="hidden" id="otherCost" value="${otherCost }" />
              	<!-- C460 判断当前团号是否为手动输入 -->
              	<input type="hidden" id="groupCodeRuleQZ" value="${fns:getUser().company.groupCodeRuleQZ }" />
                  <div class="mod_information_dzhan" id="oneStepDiv">
                      <div class="kongr"></div>
                      <div class="kongr"></div>
                      <div class="mod_information_d7"></div>
                      <div class="kongr"></div>
                      <div class="kongr"></div>
                      <div class="mod_information_dzhan_d error_add1" id="oneStepContent">
                          <div class="mod_information_d1">
                              <label><span class="xing">*</span>产品名称：</label>
                              <input type="text" maxlength="50" value="${visaProduct.productName}" class="inputTxt required" name="productName" id="productName">
                              <span style="color:#b2b2b2" class="acitivityNameSizeSpan">还可输入<span class="productNameSize">50</span>个字</span>
                              <!--  <label for="productName" class="error" id="productNameError" style="display:none;">必填信息</label>--></div>
                          <div class="kongr"></div>
                          <div class="mod_information_d2">
                              <label><span class="xing">*</span>签证国家：</label>
                              <select name="sysCountryId" id="sysCountryId">
                           		  <option value=" ">请选择</option>
                                  <c:forEach items="${countryInfoList}" var="visaCountry">
                                      <option value="${visaCountry[0]}"><c:out value="${visaCountry[1]}" /></option>
                                  </c:forEach>
                              </select>
                          </div>
                          <div class="mod_information_d2">
                              <label><span class="xing">*</span>签证领区：</label>
                              <select name="collarZoning" id="collarZoning">
                              	  <option value=" ">请选择</option>
                              </select>
                              
                          </div>
                          <div class="mod_information_d2">
                              <label><span class="xing">*</span>签证类型：</label>
                              <select name="visaType" id="visaType">
                              		<option value="">请选择</option>
                                  <c:forEach items="${visaTypeList}" var="visaType">
                                      <option value="${visaType.key}"><c:out value="${visaType.value}" /></option>
                                  </c:forEach>
                              </select>
                          </div>
                          
                          <label>领区联系人：</label>
                          <input type="text" value="${visaProduct.contactPerson }" name=contactPerson id="contactPerson" maxlength="20">
                           
                         <div class="kongr"></div>
                          
<%--                           <c:set var="departmentSet" value="${fns:getDepartmentByJob() }"/> --%>
<%-- 				    		<c:choose> --%>
<%-- 				    			<c:when test="${fn:length(departmentSet) gt 1}"> --%>
<!-- 							       <div class="mod_information_d2"> -->
<!-- 							      		<label><span class="xing">*</span>部门：</label> -->
<%-- 							      		<form:select path="deptId"> --%>
<%-- 							      			<form:option value="">请选择</form:option> --%>
<%-- 							               	<c:forEach items="${departmentSet }" var="department"> --%>
<%-- 							               		<form:option value="${department.id }">${department.name }</form:option> --%>
<%-- 							               	</c:forEach> --%>
<%-- 							    		</form:select> --%>
<!-- 							        </div>	 -->
<!-- 							        <div class="kongr"></div> -->
<%-- 				    			</c:when> --%>
<%-- 				    			<c:otherwise> --%>
<%-- 				    				<c:forEach items="${departmentSet }" var="department"> --%>
<%-- 					    				<form:hidden path="deptId" value="${department.id }"/> --%>
<%-- 					               	</c:forEach> --%>
<%-- 				    			</c:otherwise> --%>
<%-- 				    		</c:choose> --%>
				    		
				    		<div class="mod_information_d2" style="min-width:299px">
							    <label><span class="xing">*</span>部门：</label>
				    			<input id="deptId" name="deptId" type="hidden" value="${!empty deptMap ? deptMap.dept_id : '' }">
								<input id="departmentName" name="departmentName" readonly="readonly" type="text" value="${!empty deptMap ? deptMap.deptName : '' }" style="">
								<a id="departmentButton" href="javascript:" class="btn departmentButton" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
				    		</div>
					<!--S C460 增加团号-->
					<c:if test="${fns:getUser().company.groupCodeRuleQZ eq 0}"><!-- 0:手动输入 -->
						<div class="mod_information_d2">
							<label><span class="xing">*</span>团号：</label> 
							<input type="text" class="inputTxt inputTxtlong" id="groupCode" name="groupCode"
								onafterpaste="replaceStr(this)" onkeyup="validateLong(this)"
								maxlength="50">
						</div>
					</c:if>
					<!--E C460 增加团号-->

					<div class="kongr"></div>
				    		
				    <!-- C460 当手动输入团号时，不显示自动团号规则选择框-->
					<c:if test="${fns:getUser().company.groupCodeRuleQZ eq 1}"><!-- 1:自动生成 -->
					    	<c:if test="${fn:length(groupCodeRule) ne 0}">
								<div class="mod_information_d2">
									<label>团号类别：</label>
									<select id="groupCodeRule">
										<c:forEach items="${groupCodeRule}" var="codeRule">
											<option value="${codeRule[0]}">${codeRule[1]}</option>
										</c:forEach>
									</select>
		                     	</div>
		                     	<form:hidden path="groupCode"/>
	                     		<div class="kongr"></div>	
	                     	</c:if>
	                </c:if>
                          <div class="mod_information_d2 add-paytype">
                              <label><span class="xing">*</span>预定方式：</label>
                              <form:checkbox path="reserveMethod" value="1" cssClass="ckb_mod valid"  id="reserveId"  onclick="paychg(this)"/>
                              <font>预定</font>　　　
<!--                               <label for="spinner" class="txt2" id="label_advance"> -->
<!--                                   <span class="xing" id="advance_xing" style="display: none;">*</span>保留天数：</label> -->
<!--                                   <span id="payMode_advance_span"> -->
<!--                                     <input class="spinner"  onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" autocomplete="off"  value="" maxlength="3" name="stayTime"  id="stayTime"  > -->
<!--                                          <span class="ui-spinner ui-widget ui-widget-content ui-corner-all"><input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" value="" maxlength="3" name="remainDays_advance" class="spinner ui-spinner-input" id="remainDays_advance" autocomplete="off" role="spinbutton" disabled="disabled"><a class="ui-spinner-button ui-spinner-up ui-corner-tr ui-button ui-widget ui-state-default ui-button-text-only" tabindex="-1" role="button" aria-disabled="false"><span class="ui-button-text"><span class="ui-icon ui-icon-triangle-1-n">▲</span></span></a><a class="ui-spinner-button ui-spinner-down ui-corner-br ui-button ui-widget ui-state-default ui-button-text-only" tabindex="-1" role="button" aria-disabled="false"><span class="ui-button-text"><span class="ui-icon ui-icon-triangle-1-s">▼</span></span></a></span> -->
<!--                                         <span style="padding-left:5px;">天</span> -->
<!--                                    </span> -->
                              <br>
                              <label> </label>
                              <form:checkbox path="reserveMethod" value="2" cssClass="ckb_mod valid" id="reserveAllId"/>
                              <font>付全款</font>

                          </div>
                          <div class="kongr"></div>
                          <!--备注开始-->
                          <div class="mod_information_d2 pro-marks">
                              <label>备注：</label>

                              <textarea name="remark" maxlength="2000">${visaProduct.remark}</textarea>

                          </div>
                          <!--备注结束-->
                      </div>
                  </div>
                  <div class="ydBtn" id="oneStepBtn">
                      <input type="button" value="下一步" onclick="findMoreProduct('${ctx}')" class="btn btn-primary valid">
                  </div>
              </form:form>
          </div>
</body>
</html>