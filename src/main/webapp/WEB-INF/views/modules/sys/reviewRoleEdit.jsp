<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>职务审核配置</title>
	<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript" ></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <link href="${ctxStatic}/css/jh-style.css" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
    
	<script type="text/javascript">
	/*
	$(document).ready(function() {
			//表单验证
			$("#companyDictForm").validate({
			rules:{
				label:{
					required:true,
					remote: {
						type: "POST",
						url: "${ctx}/sys/review/reviewRoleSave?type="+$('#type').val()+"&id="+$('#id').val()+"&checked=label"
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
		
	}); */
   	jQuery.extend(jQuery.validator.messages, {
		  		required: "必填信息",
		  		digits:"输入有误"
   			}); 

   	 function formSubmit() {
            var reviewLevel= $("#reviewLevel").val();
            var roleList= $("#roleList").val();
            if (roleList==null || roleList==""){ 
              alert("请选择职务");
              return false;
            }
        	if (reviewLevel==null ||reviewLevel==""){ 
              alert("请输入职务负责审核的 层级");
              return false;
            }
            if (isNaN(reviewLevel)){
             alert("层级必须是数字");
              return false;
            }
            if(reviewLevel > ${topLevel} ){
              alert("层级不能大于"+ ${topLevel} );
              return false;
            }
           if(reviewLevel <1){
              alert("层级不能小于1");
              return false;
            } 

            var end= $("#end").val();
            if (end==null || end==""){ 
              alert("请选择审核是否结束");
              return false;
            }
           
            $.ajax({
                        type:"POST",
                        url:"${ctx}/sys/review/checkReviewJob",
                        data:{
                           jobid: roleList,reviewcompanyid:${reviewcompanyid},id:${id}
                        },
                        success:function(msg) {                        
                        if(msg>=1) {
                          alert("此职务流程审批配置已经存在");
                           return false;
                         }
                         $("#companyDictForm").submit();
                     
                        }
                   });
            
                  
        	
        }
	</script>


  </head>
  
  <body>
    <form:form id="companyDictForm" modelAttribute="sysdefinedict" action="${ctx}/sys/review/roleReviewSave" target="_parent" method="post">
		     <input  type="hidden"  id="roleLevelId" name="roleLevelId" value="${reviewRoleLevel.id}" />
		     <input  type="hidden"  id="reviewcompanyid" name="reviewcompanyid" value="${reviewcompanyid}" />
		     <input  type="hidden"  id="topLevel" name="topLevel" value="${topLevel}" />
		<div class="msg_div">
		 
	

          <table  width="100%"  style="cellspacing:10px;cellpadding:10px;margin-left:10px;font-family:微软雅黑, Arial,宋体; color: #333;background:#ffffff;font-size: 12px" >
      			<tr>
      			    <td width="35%">职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;务 ：<td>
      				<td width="55%">  
      				 <select name="roleList" id="roleList"  style="width:120px"><option value="" selected>请选择</option><c:forEach items="${roleList}" var="role">
            	   <option value="${role.id }"    <c:if test="${role.id == reviewRoleLevel.jobId}">selected</c:if>>${role.name}</option>
                   </c:forEach>
                  </select> 
      			<td>
      			</tr>
     			<tr>
      			    <td>审核层级 ：<td>
      				<td><input type="text" id="reviewLevel"  style="width:120px" class="inputTxt" name="reviewLevel" value="${reviewRoleLevel.reviewLevel}" /><td>
      			</tr>
          

            <tr>
              <td width="35%">审核是否结束 ：${ reviewRoleLevel.isEnd}<td>
              <td width="55%">  
               <select name="end" id="end"  style="width:120px">              
                 <option value="0" <c:if test="${topLevel !=reviewRoleLevel.reviewLevel && reviewRoleLevel.isEnd==0 }">selected</c:if>>审核没结束</option>
                 <option value="1" <c:if test="${topLevel==reviewRoleLevel.reviewLevel || reviewRoleLevel.isEnd==1 }">selected</c:if>>审核结束</option>
                 </select> 
            <td>
            </tr>
      		</table>

           <div class="release_next_add">
           <input class="btn btn-primary gray" type="button" value="取消" onClick="window.parent.window.jBox.close()"/>
           <input  type="button"  class="btn btn-primary" onClick="formSubmit()"  value="确定" />
           </div>
          </div>
	</form:form>
  </body>
</html>
