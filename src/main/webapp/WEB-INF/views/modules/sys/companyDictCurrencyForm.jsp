<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>类型字典维护</title>
	<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript" ></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <link href="${ctxStatic}/css/jh-style.css" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
    
	<script type="text/javascript">
	$(document).ready(function() {
			//表单验证
			$("#companyDictForm").validate({
			rules:{
				currencyName:{
					required:true,
					remote: {
						type: "POST",
						url: "${ctx}/sys/currency/check?id="+$('#id').val()
							}
					},
				currencyMark:{
					required:true
				},
				currencyExchangerate:{
					required:true,
					number:true
				}
			},
			messages:{
				currencyName:{remote:"名称已存在"},
			}
		});
	});
   	jQuery.extend(jQuery.validator.messages, {
		  		required: "必填信息",
		  		number:"输入有误"
   			}); 
   	
   	//提交表单
   	function submitForm() {
   		//获得币种标识
   		var currencyMark = $("#currencyStyle").children("option:selected").text();
		$("#currencyMark").val(currencyMark);
   	}
	</script>

	<style>
	.bzxx{width:500px; float:left; overflow:hidden;}
	.bzxx dt { margin-top:10px; float:left; width:100%;}
	.bzxx dt input,dt select{ float:left; width:120px; height:28px; line-height:28px; border:#CCC 1PX SOLID; border-radius:4px;}
	.bzxx dt span{width:120px; text-align:right; float:left; height:30px; line-height:30px;}
	.bzxx dt b{width:120px; text-align:right; float:left; height:30px; line-height:30px;}
	</style>

  </head>
  
  <body>
    <form:form id="companyDictForm" modelAttribute="currency" action="${ctx}/sys/currency/save" target="_parent" method="post">
          <dl class="bzxx">
          	<input type="hidden" id="id" name="id" value="${currency.id}" />
			<dt><span><font>*</font>币种名称 ：</span><input type="text" id="currencyName" class="inputTxt" name="currencyName" value="${currency.currencyName}" /></dt>
			<dt><span><font>*</font>符号：</span>
			<select id="currencyStyle" name="currencyStyle">
				<option value="rmb" <c:if test="${currency.currencyStyle eq 'rmb'}">selected</c:if>>¥</option>
      			<option value="usd" <c:if test="${currency.currencyStyle eq 'usd'}">selected</c:if>>$</option>
      			<option value="ecu" <c:if test="${currency.currencyStyle eq 'ecu'}">selected</c:if>>€</option>
      			<option value="jpy" <c:if test="${currency.currencyStyle eq 'jpy'}">selected</c:if>>J¥</option>
      			<option value="GBP" <c:if test="${currency.currencyStyle eq 'GBP'}">selected</c:if>>£</option>
			</select>
			</dt>
			<dt><span>汇率：</span><input type="text" id="currencyExchangerate" class="inputTxt" name="currencyExchangerate" value="${currency.currencyExchangerate}" /></dt>
			<dt><b>换汇汇率：</b></dt>
			<dt><span>现金收款：</span><input type="text" id="convertCash" class="inputTxt" name="convertCash" value="${currency.convertCash}" />
				<span>对公收款：</span><input type="text" id="convertForeign" class="inputTxt" name="convertForeign" value="${currency.convertForeign}" />
			</dt>
			<dt><span>中行折算价：</span><input type="text" id="convertAbc" class="inputTxt" name="convertAbc" value="${currency.convertAbc}" /></dt>
			<dt><span>最低汇率标准：</span><input type="text" id="convertLowest" class="inputTxt" name="convertLowest" value="${currency.convertLowest}" /></dt>
			<input type="hidden" id="currencyMark" name="currencyMark" value="${currency.currencyMark}"/>
           <input type="hidden" name="type" id="type" value="${param.type}"/>
           <div class="release_next_add">
           <input class="btn btn-primary gray" type="button" value="取消" onClick="window.parent.window.jBox.close()"/>
           <input type="submit" class="btn btn-primary" value="确定" onClick="submitForm()"/>
		</dl>
	</form:form>
  </body>
</html>
