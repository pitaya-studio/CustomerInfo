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
				label:{
					required:true,
					remote: {
						type: "POST",
						url: "${ctx}/sys/CompanyDict/check?type="+$('#type').val()+"&id="+$('#id').val()+"&checked=label"
							}
					},
				sort:{
					required:true,
					digits:true
				}
			},
			messages:{
				label:{remote:"名称已存在"},
			}
		});
		if(!"${sysdefinedict.id}") {
			var typeValue = "";
			if("${param.type}"=="travel_type")
					typeValue = "旅游类型" ;
				else if("${param.type}"=="product_level")
					typeValue = "产品系列" ;
				else if("${param.type}"=="product_type")
					typeValue = "产品类型" ;
				else if("${param.type}"=="traffic_mode")
					typeValue = "交通方式" ;
			
				$("#description").val(typeValue);
			}
		
	});
   	jQuery.extend(jQuery.validator.messages, {
		  		required: "必填信息",
		  		digits:"输入有误"
   			}); 
	</script>


  </head>
  
  <body>
    <form:form id="companyDictForm" modelAttribute="sysdefinedict" action="${ctx}/sys/CompanyDict/saveCompanyDict" target="_parent" method="post">
		<div class="msg_div">
		    <input type="hidden" id="value" name="value" value="${sysdefinedict.value}" />
		    <c:choose>
           		<c:when test="${param.type eq 'flight_info'}">
           			<c:set value="flightInfo" var="true"></c:set>
           		</c:when>
           		<c:otherwise>
           			<c:set value="flightInfo" var="false"></c:set>
           		</c:otherwise>
           	</c:choose>
            <p><span><font>*</font>
            	<c:choose>
            		<c:when test="${param.type eq 'flight_info'}">字码：</c:when>
            		<c:otherwise>名称 ：</c:otherwise>
            	</c:choose>
           		</span>
           		<input type="text" id="label" class="inputTxt" name="label" value="${sysdefinedict.label}" maxlength="50" />
      		</p>
           	<p><span><font>*</font>顺序 ：</span><input type="text" id="sort" class="inputTxt" name="sort" value="${sysdefinedict.sort}" maxlength="3"/></p>
  			<p><span>
  				<c:choose>
            		<c:when test="${param.type eq 'flight_info'}">机场名：</c:when>
            		<c:otherwise>描述 ：</c:otherwise>
            	</c:choose>
  				</span>
  				<input type="text" id="description" class="inputTxt" name="description" value="${sysdefinedict.description}" maxlength="99" />
  			</p>
            <c:if test="${param.type eq 'traffic_mode'}">
            	<p class="msg_div_check"><input type="checkbox" id="defaultFlag" name="defaultFlag" value="1" <c:if test="${sysdefinedict.defaultFlag eq '1'}">checked</c:if> /><span style="width: auto" title="请与航空类的交通方式关联">&nbsp;&nbsp;关联航空公司二字码 </span></p>
          	</c:if>
           <input type="hidden" name="type" id="type" value="${param.type}"/>
           <input type="hidden" name="id" id="id" value="${sysdefinedict.id}"/>
           <div class="release_next_add">
           <input class="btn btn-primary gray" type="button" value="取消" onClick="window.parent.window.jBox.close()"/>
           <input type="submit" class="btn btn-primary" value="确定" />
           </div>
          </div>
	</form:form>
  </body>
</html>
