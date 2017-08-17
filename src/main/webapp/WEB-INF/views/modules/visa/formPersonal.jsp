<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>字典管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function() {
        	
        	$("input[name='personnelType']").click(function(){
        		var personnelType = $(this).val();
        		$(".service").parent().show();
        		var labelName = "";
        	    if(personnelType==1){
        	    	//在职人员
        	    	labelName = "在职证明";
        	    }else if(personnelType==2){
        	    	//在校学生
        	    	labelName = "学校准假信";
                }else if(personnelType==3){
                	//无业人员
                	$(".service").parent().hide();
                }else if(personnelType==4){
                	//退休老人
                	labelName = "退休证";
                }
        	    $(".service").text(labelName);
        	});
        	
            $("#value").focus();
            $("#inputForm").validate({
                submitHandler: function(form){
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
    </script>
</head>
<body>
<!-- 
    <ul class="nav nav-tabs">
        <li><a href="${ctx}/sys/dict/">字典列表</a></li>
        <li class="active"><a href="${ctx}/sys/dict/form?id=${visabasics.id}">字典${not empty dict.id?'修改':'添加'}查看</a></li>
    </ul><br/>
 -->
    <form:form id="inputForm" modelAttribute="visapersonneltype" action="${ctx}/visa/instruction/saveVisaPersonnal" method="post" class="form-horizontal">
        <form:hidden path="id"/>
        <tags:message content="${message}"/>
        
                                
        <form:hidden path="visabasicsId"/>

		<div class="control-group">
			<label class="control-label">人员类型 :</label>
			<div class="controls">
				<form:radiobuttons path="personnelType" htmlEscape="false"
					maxlength="50" class="required" items="${visa_personnaltype}"
					itemLabel="label" itemValue="value" />
			</div>
		</div>
		<div class="control-group">
            <label class="control-label">护照:</label>
            <div class="controls">
                <form:textarea path="passport" htmlEscape="false" class="required" style="width: 60%;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">身份证:</label>
            <div class="controls">
                <form:textarea path="idCard" htmlEscape="false" class="required" style="width: 60%;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">照片:</label>
            <div class="controls">
                <form:textarea path="photo" htmlEscape="false"  class="required" style="width: 60%;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">签证申请表:</label>
            <div class="controls">
                <form:textarea path="visaApplicationForm" htmlEscape="false" class="required" style="width: 60%;"/>
            </div>
        </div>
        
        
        <div class="control-group">
            <label class="control-label">其他补充材料:</label>
            <div class="controls">
                <form:textarea path="otherSupplementaryMaterials" htmlEscape="false" class="required" style="width: 60%;"/>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">存款证明 :</label>
            <div class="controls">
                <form:textarea path="certificateDeposit" htmlEscape="false" class="required" style="width: 60%;"/>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label service">在职证明:</label>
            <div class="controls">
                <form:textarea path="service" htmlEscape="false" class="required" style="width: 60%;"/>
            </div>
        </div>
        
        <div class="form-actions">
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
            <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
        </div>
        
    </form:form>
</body>
</html>