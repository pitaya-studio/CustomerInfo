<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色审核配置</title>
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
     
            var topLevel= $("#topLevel").val();
            var redo= $("#redo").val();
            var reviewcompanyid= $("#reviewcompanyid").val();
            var deptid;
            var reviewFlowId;
            /*
            var roleList= $("#roleList").val();
            if (roleList==null || roleList==""){ 
              alert("请选择角色");
              return false;
            } */
            if(reviewcompanyid==""){            	
            	reviewFlowId= $("#roleList").val();
            	if (reviewFlowId==null || reviewFlowId==""){ 
                 alert("请选择审核流程");
                return false;
               }   
              deptid= $("#deptid").val();          
              if (deptid==null || deptid==""){ 
                 alert("请选择所属部门");
                return false;
               }   
            }
              if(redo==""){
              	 alert("请选择是否允许多次提交审核");
                return false;
              }
        	if (topLevel==null ||topLevel==""){ 
              alert("请输入审核层级");
              return false;
            }
            if (isNaN(topLevel)){
             alert("审核层级必须是数字");
              return false;
            }         
             if(topLevel <1){
              alert("审核层级不能小于1");
              return false;
            }
              if(topLevel >10){
              alert("审核层级不能大于10");
              return false;
            }

          if(reviewcompanyid==""){          	
             $.ajax({
                        type:"POST",
                        url:"${ctx}/sys/review/checkReviewCompany",
                        data:{
                            reviewFlowId: reviewFlowId,deptid:deptid
                        },
                        success:function(msg) {                        
                        if(msg>=1) {
                        	alert("此流程审批配置已经存在");
                           return false;
                         }
                        $("#companyDictForm").submit();
                        }
                    });
            }   
        
          if(reviewcompanyid!=""){
              $("#companyDictForm").submit(); 
          }
        	
        }
	</script>


  </head>
  
  <body>
    <form:form id="companyDictForm" modelAttribute="sysdefinedict" action="${ctx}/sys/review/reviewCompanySave" target="_parent" method="post">
		    
		     <input  type="hidden" id="reviewcompanyid" name="reviewcompanyid" value="${reviewcompanyid}" />
		     <input  type="hidden" id="companyId" name="companyId" value="${companyId}" />
		    <br>
	<div class="msg_divs">
        <table  width="100%"  style="cellspacing:10px;cellpadding:10px;margin-left:10px;font-family:微软雅黑, Arial,宋体; color: #333;background:#ffffff;font-size: 12px" >
      			 <tr>
                <td width="35%">所属部门：<td>
              <td width="55%"><c:if test="${! empty reviewcompanyid }" > ${deptName} </c:if> 
                  <c:if test="${ empty reviewcompanyid }" >
                         <select style="width:110px"  name="deptid" id="deptid">
                                <option value="" >请选择...</option>
                                <c:forEach items="${deptList}" var="dept">
                                    <option value="${dept.id}">${dept.name}</option>
                                </c:forEach>
                            </select>
                  </c:if>
              </td></tr>
            <tr>
      			    <td>审核流程 ：<td>
      				<td>   <c:if test="${! empty reviewcompanyid }" > ${productName}${flowName} </c:if>
                        <c:if test="${ empty reviewcompanyid }" >
                        <select style="width:110px"  name="roleList" id="roleList"><option value="" selected>请选择</option><c:forEach items="${reviewFlow}" var="dict"><option value="${dict.id }">${dict.productName } ${dict.flowName }</option></c:forEach></select>
                        </c:if>
      					<td>
      			</tr>
     			<tr>
      			    <td>审核层级 ：<td>
      				<td><input type="text" id="topLevel" style="width:110px"  class="inputTxt" name="topLevel" value="${reviewCompany.topLevel}" /><td>
      			</tr>
      			<tr>
      			    <td>是否支持多个审批 ：<td>
      				<td>  
      				 <select name="redo" id="redo">
                 	<option value="" selected>请选择</option>
                 	<option value="1"   <c:if test="${reviewCompany.redo== 1}">selected</c:if>>一个订单可以提交多个审批</option>
                 	<option value="0"   <c:if test="${reviewCompany.redo== 0}">selected</c:if>>一个订单不可以提交多个审批</option>
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
