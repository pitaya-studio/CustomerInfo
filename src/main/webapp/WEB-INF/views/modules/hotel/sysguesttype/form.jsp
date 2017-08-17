<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>基本住客类型信息</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
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
		function formSubmit(){
			 //提交前验证
			var flag = true;
    	    var name = $("#name").val();
    	    if(name==null|| $.trim(name)==""){
    			top.$.jBox.tip('请填写住客类型名称');
    			return false;
    		}
    	   
    	    var personType = $("#personType").val();
    	    if(personType==null|| $.trim(personType)==""){
    			top.$.jBox.tip('请选择人员类型');
    			return false;
    		}
    	    if($("#uuid").val()==''){
    	    	$.ajax({
    				type:"post",
    				url:"${ctx}/sysGuestType/check",
    				data:{
    					"name":$("#name").val()
    				},
    				success:function(data){
    					if(data && data.message=="true"){
    						$.jBox.tip("住客类型名称: " + data.result+" 已存在，请重新填写！");
    						flag=false;
    					};
    				}
    			});
    	    }
    	    
    	    if(!flag){
    	    	return false;
    	    }
    	    var url = "";
			if($("#uuid").val()=='') {
				url="${ctx}/sysGuestType/save";
			} else {
				url="${ctx}/sysGuestType/update";
			}
			
			$("#btnSubmit").attr("disabled", "disabled");
			$.post(url,$("#inputForm").serialize(),function(data){
				if(data=="1"){
					$("#searchForm",window.opener.document).submit(); 
					$.jBox.tip("保存成功!");
					setTimeout(function(){window.close();},500);
				}else if(data=="2"){
					$("#searchForm",window.opener.document).submit(); 
					$.jBox.tip("修改成功!");
					setTimeout(function(){window.close();},500);
				}else{
					$.jBox.tip('操作异常！','warning');
					$("#btnSubmit").attr("disabled", false);
				}
			});
		}
		
			/** $("#inputForm").validate({
				rules:{
					label:{
						required:true,
						remote: {
							type: "POST",
							url: "${ctx}/sysGuestType/check?type="+$('#type').val()+"&uuid="+$('#uuid').val()
								}
						},
					sort:{
						required:true,
						digits:true
					},
					dictUuid:{
						required:true
					} 
				},
				submitHandler: function(form){
					 20150812 add by WangXK
					var sysTravelerType="";
		            $("input[name='sysTravelerType']:checkbox").each(function(){ 
		                if($(this).attr("checked")){
		                	sysTravelerType += $(this).val()+",";
		                }
		            });
		            $("#sysTravelerType").val(sysTravelerType);
		            $("#sysTravelerTypeUuids").val(sysTravelerType);
		            
					var url = "";
					if($("#uuid").val()=='') {
						url="${ctx}/sysGuestType/save";
					} else {
						url="${ctx}/sysGuestType/update";
					}
					
					$("#btnSubmit").attr("disabled", "disabled");
					$.post(url,$("#inputForm").serialize(),function(data){
						if(data=="1"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("保存成功!");
							setTimeout(function(){window.close();},500);
						}else if(data=="2"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("修改成功!");
							setTimeout(function(){window.close();},500);
						}else{
							$.jBox.tip('操作异常！','warning');
							$("#btnSubmit").attr("disabled", false);
						}
					});
				},
				messages:{
					label:{remote:"名称已存在"},
				}
			});
			if($("#uuid").val()=='') {
				$("#sort").val("50");
			}
		*/
		function checkSysTravelerType(){
			
				    var value = $("#value").val();
		    	    if(value==null|| $.trim(value)==""){
		    			top.$.jBox.tip('请填写游客人数');
		    			return false;
		    		}
		    	    var reg = new RegExp("^[0-9]*$");
					if(!reg.test(value)) {
						$.jBox.tip("游客人数只能输入整数。", 'error', { focusId: "value" }); 
						return false;
					} 
		    	    
		    	    var type = $("#type").val();
		    	    if(type==null|| $.trim(type)==""){
		    			top.$.jBox.tip('请选择取值类型');
		    			return false;
		    		}
		        	var validate = true;
		        	var sysTravelerType="";
		            $("input[name='sysTravelerType']:checkbox").each(function(){ 
		                if($(this).attr("checked")){
		                	sysTravelerType += $(this).val()+",";
		                }
		            });
		            if(sysTravelerType==null || sysTravelerType==""){
						$.jBox.tip("游客类型不能为空，需至少选择一个游客类型！");
						validate = false;
						return false;
					}
		            $("#sysTravelerType").val(sysTravelerType);
		            $("#sysTravelerTypeUuids").val(sysTravelerType.substr(0, sysTravelerType.length - 1));
		    		$.ajax({
						type:"post",
						url:"${ctx}/sysGuestType/checkRepetition",
						data:{
							"sysTravelerTypeUuids":$("#sysTravelerTypeUuids").val(),
							"sysGuestTypeUuid":$("#uuid").val(),
							"value":$("#value").val(),
							"type":$("#type").val()
						},
						async: false,
						success:function(data){
							if(data.result=="1"){
								$.jBox.tip("在游客人数为："+$("#value").val()+",取值类型为："+ $("#type").find("option:selected").text()+"时，以下游客类型重复："+data.mess);
								validate = false;
							};
						}
					});
					if(!validate){
						return false;
					}
	    			if(validate) {
	    				formSubmit();
	    			};
		    		
		        }
	</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">基础住客类型信息</div>
	<form:form method="post" modelAttribute="sysGuestTypeInput" action="" class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
		<input type="hidden" id="sysTravelerTypeUuids" name="sysTravelerTypeUuids" value=""/>
			
			<p>
				<label><em class="xing">*</em>住客类型名称：</label>
				<input type="text" name="name" id="name" htmlEscape="false" maxlength="11" class="required" value="${sysGuestTypeInput.name }"/>
			</p>
			<p>
				<label><em class="xing">*</em>游客类型名称：</label>
				<input id="sysTravelerType" name="sysTravelerType" value="" type="hidden">
                <c:forEach items="${sysTravelerTypeList}" var="item">
                	<input name="sysTravelerType" type="checkbox" value="${item.uuid}" <c:if test="${sysTravelerTypeString.contains(item.uuid) }">checked="checked"</c:if> data-value="${item.name}"> ${item.name}</input>
                </c:forEach>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label><em class="xing">*</em>游客人数：</label>
				<input type="text" id="value" name="value" htmlEscape="false" maxlength="11" class="required" value="${sysGuestTypeInput.value }"/>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label><em class="xing">*</em>人员类型：</label>
				<select id="personType" name="personType">
					<option value="">请选择</option>
					<option value="0" <c:if test="${sysGuestTypeInput.personType=='0' }">selected="selected" </c:if> >成人</option>
					<option value="1" <c:if test="${sysGuestTypeInput.personType=='1' }">selected="selected" </c:if> >婴儿</option>
					<option value="2" <c:if test="${sysGuestTypeInput.personType=='2' }">selected="selected" </c:if> >儿童</option>
				</select>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label><em class="xing">*</em>取值类型：</label>
				<select id="type" name="type">
					<option value="">请选择</option>
					<option value="0" <c:if test="${sysGuestTypeInput.type=='0' }">selected="selected"</c:if> >共</option>
					<option value="1" <c:if test="${sysGuestTypeInput.type=='1' }">selected="selected"</c:if> >增</option>
				</select>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label><em class="xing"></em>描述：</label>
				<form:textarea class="madintain_text"  maxlength="99" path="description"  />
			</p>
			<p class="maintain_kong"></p>
			<p class="maintain_btn">
				<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
				<input type="button" value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" onclick="checkSysTravelerType();" />
			</p>

	</form:form>
	<!--右侧内容部分结束-->
</body>
</html>
