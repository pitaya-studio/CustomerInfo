<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>基础信息维护-游客类型管理-添加游客类型</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				rules:{
					name:{
						required:true,
						remote: {
							type: "POST",
							url: "${ctx}/travelerType/check?uuid="+$('#uuid').val()
								}
					},
					sysTravelerType:{
						remote: {
							type: "POST",
							url: "${ctx}/travelerType/validateTravelerType?uuid="+$('#uuid').val()
								}
					},
					sort:{
						required:true,
						digits:true
					}
				},
				submitHandler: function(form){
					var url = "";
					if($("#uuid").val()=='') {
						url="${ctx}/travelerType/save";
					} else {
						url="${ctx}/travelerType/update";
					}
					saveApplyProduct();
					$("#btnSubmit").attr("disabled", "disabled");
					$("#shortName").removeAttr("disabled");
					$.post(url,$("#inputForm").serialize(),function(data){
						if(data.message=="1"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("保存成功!");
							setTimeout(function(){window.close();},500);
						}else if(data.message=="2"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("修改成功!");
							setTimeout(function(){window.close();},500);
						}else if(data.message=="3"){
							$.jBox.tip(data.error,'warning');
							$("#btnSubmit").attr("disabled", false);
							$("#shortName").attr("disabled", true);
						}else{
							$.jBox.tip('系统异常，请重新操作!','warning');
							$("#btnSubmit").attr("disabled", false);
							$("#shortName").attr("disabled", true);
						}
					});
				},
				messages:{
					name:{remote:"名称已存在"},
					sysTravelerType:{remote:"系统已保存此游客类型!"}
				}
			});
			
			
			if($("#uuid").val()=='') {
				$("#sort").val("50");
			}
			
			//加载截止年龄
			rangeFromChange();

			//初始化游客类型简称
			$("#shortName").val("${travelerType.shortName}");
			
			//游客类型简称添加联动效果
			$("#sysTravelerType").change(function() {
				//var sysTravelerType=$("#sysTravelerType  option:selected").text();
				var sysTravelerType=$("#sysTravelerType  option:selected").val();
				changeSysTravelerType(sysTravelerType);
			});
		});
		
		function changeSysTravelerType(sysTravelerType) {
			var flag = false;
			$("#shortName").children().each(function(){
				if($(this).attr("typeUuid")==sysTravelerType){
					$(this).attr("selected","selected");
					flag=true;
				}
				if(flag){
					return false;
				}
			})
		}
		
		//加载截止年龄
		function rangeFromChange() {
			var rangeFrom = $("#rangeFrom").val();
			var rangeTo = $("#rangeTo").val();
			
			$("#rangeTo").html('');
			for(var i=rangeFrom; i<= 120; i++) {
				if(rangeTo == i) {
					$("#rangeTo").append("<option value='"+ i +"' selected>"+ i + "</option>");
				} else {
					$("#rangeTo").append("<option value='"+ i +"' >"+ i + "</option>");
				}
			}
		}
		function saveApplyProduct(){
			var applyProduct=[];
			//$("[name=applyProduct1]:checkbox:checked").each(function(){
			$("input[name=applyProduct1]:checked").each(function(){
				applyProduct.push($(this).val());
			});
			$("#applyProduct").val(applyProduct.join(","));
		}
		function selectAll(){
 			var checklist = document.getElementsByName("applyProduct1");
   			if(document.getElementById("controlAll").checked){
   				for(var i=0;i<checklist.length;i++) {
      				checklist[i].checked = 1;
   				}
   			}else{
  				for(var j=0;j<checklist.length;j++){
     				checklist[j].checked = 0;
  				}
 			}
		} 
	</script>
	
	<style type="text/css">  
		#sea{ min-width: 1220px;} 
	</style>
</head>
<body>
	<div>
		<!--右侧内容部分开始-->
        <div class="ydbz_tit pl20">游客类型信息</div>
          <form:form method="post" modelAttribute="travelerType" action="" class="form-horizontal" id="inputForm" novalidate="">
          	<form:hidden path="uuid" />
          	<div class="maintain_add add_visitor"> 
            	<p>
              		<label>游客类型名称：</label>
              		<form:input path="name" htmlEscape="false" maxlength="20" class="required" cssStyle="width:150px;"/>
				</p>
            	<p class="maintain_kong"></p>
				<p style="width:450px;">
				 	<label>年龄范围：</label>
              		<form:select path="rangeFrom" onchange="rangeFromChange();">
						<c:forEach begin="0" end="120" var="item">
							<form:option value="${item }">${item }</form:option>
						</c:forEach>
					</form:select>
					<span>至</span> 
					<form:select path="rangeTo">
						<c:forEach begin="0" end="120" var="item">
							<form:option value="${item }">${item }</form:option>
						</c:forEach>
					</form:select>
				</p>
            	<p class="maintain_kong"></p>
            	<p>
					<label>人员类型：</label>
					<select id="personType" name="personType">
						<option value="">请选择</option>
						<option value="0" <c:if test="${travelerType.personType==0 }">selected="selected" </c:if> >成人</option>
						<option value="1" <c:if test="${travelerType.personType==1 }">selected="selected" </c:if> >婴儿</option>
						<option value="2" <c:if test="${travelerType.personType==2 }">selected="selected" </c:if> >儿童</option>
					</select>
				</p>
            	<p class="maintain_kong"></p>
				<p>
	              <label>对应系统的游客类型：</label>
		             <form:select path="sysTravelerType">
						<form:options items="${sysTravelerTypeLists}" itemValue="uuid"	itemLabel="name" />
					</form:select>
				</p>
	            <p class="maintain_kong"></p>
				<p style="width:800px;">
	              <label>游客类型简称：</label>
	              <select name="shortName" id="shortName" disabled="disabled">
	              	<option typeUuid=""></option>
	              	<c:forEach items="${sysTravelerTypeLists }" var="lists" >
	              		<option value="${lists.shortName}" typeUuid="${lists.uuid}">${lists.shortName}</option>
	              	</c:forEach>
	              </select>
				  <span style="color:#999999;"> 注：A代表Adult；B代表Baby；C代表Children；O代表Old man；S代表Special populations</span>
				</p>
	            <p class="maintain_kong"></p>
	            <p style="width:1000px;">
		            <label>适用产品:</label>
		            <form:hidden path="applyProduct" />
		            <input type="checkbox" name="applyProduct1" value="1" <c:if test="${fns:contains(applyProductArr, '1')}">checked="checked"</c:if>/>单团
		            <input type="checkbox" name="applyProduct1" value="2" <c:if test="${fns:contains(applyProductArr, '2')}">checked="checked"</c:if>/>散拼
		            <input type="checkbox" name="applyProduct1" value="3" <c:if test="${fns:contains(applyProductArr, '3')}">checked="checked"</c:if>/>游学
		            <input type="checkbox" name="applyProduct1" value="4" <c:if test="${fns:contains(applyProductArr, '4')}">checked="checked"</c:if>/>大客户
		            <input type="checkbox" name="applyProduct1" value="5" <c:if test="${fns:contains(applyProductArr, '5')}">checked="checked"</c:if>/>自由行
		            <input type="checkbox" name="applyProduct1" value="6" <c:if test="${fns:contains(applyProductArr, '6')}">checked="checked"</c:if>/>签证
		            <input type="checkbox" name="applyProduct1" value="7" <c:if test="${fns:contains(applyProductArr, '7')}">checked="checked"</c:if>/>机票
		            <input type="checkbox" name="applyProduct1" value="10" <c:if test="${fns:contains(applyProductArr, '10')}">checked="checked"</c:if>/>游轮
		            <input type="checkbox" name="applyProduct1" value="11" <c:if test="${fns:contains(applyProductArr, '11')}">checked="checked"</c:if>/>酒店
		            <input type="checkbox" name="applyProduct1" value="12" <c:if test="${fns:contains(applyProductArr, '12')}">checked="checked"</c:if>/>海岛游
		            <input type="checkbox" name="controlAll" id="controlAll" onclick="selectAll();" />全选
	           	</p>
	            <p class="maintain_kong"></p>
	            <p>
              		<label>排序：</label>
              		<form:input path="sort" htmlEscape="false" maxlength="4" class="required" />
				</p>
            	<p>
              		<label>状态：</label>
              		<input type="radio" id="status_1" name="status" value="1" <c:if test="${travelerType.status != '0' }">checked="checked"</c:if>/> 启用
					&nbsp;&nbsp;&nbsp; 
					<input type="radio" id="status_0" name="status" value="0" <c:if test="${travelerType.status == '0' }">checked="checked"</c:if>/> 停用
				</p>
	            <p class="maintain_kong"></p>
	            <p class="maintain_pfull">
	              <label>描述：</label>
	              <form:textarea class="madintain_text" path="description" />
				</p>
	            <p class="maintain_btn">
	              	<label>&nbsp;</label>
	                <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" /> 
					<input type="submit" value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" />
				</p>     
          </div>
		</form:form>
        <!--右侧内容部分结束-->
	</div>
</body>
</html>
