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

   	 function formSubmit() {
           var deptid= $("#deptid").val();
            var jobid= $("#jobid").val();
           //alert(deptid);           
         
            if (deptid==null || deptid==""){ 
              alert("请选择部门");
              return false;
            } 

              if (jobid==null || jobid==""){ 
              alert("请选择职务");
              return false;
            } 
                 	
             $.ajax({
                        type:"POST",
                        url:"${ctx}/sys/userDept/checkUserDeptJob",
                        data:{
                           userid: ${userid}, deptid:deptid, jobid:jobid
                        },
                        success:function(msg) {                        
                        if(msg>=1) {
                        	alert("此部门下的职务已经分配给用户");
                           return false;
                         }
                        $("#companyDictForm").submit();
                        }
                    });
           
        	
        }
	</script>


  </head>
  
  <body>
    <form:form id="companyDictForm" modelAttribute="sysdefinedict" action="${ctx}/sys/userDept/userDeptSave" target="_parent" method="post">
		    
		     
		     <input  type="hidden" id="userid" name="userid" value="${userid}" />
		    <br>
	<div class="msg_divs">
        <table  width="90%"  style="cellspacing:10px;cellpadding:10px;margin-left:10px;font-family:微软雅黑, Arial,宋体; color: #333;background:#ffffff;font-size: 12px" >
      			 <tr>
                <td width="35%">选择部门：<td>
              <td width="55%">                 
                         <select  name="deptid" id="deptid">
                                <option value="" >请选择...</option>
                                <c:forEach items="${deptList}" var="dept">
                                    <option value="${dept.id}">${dept.name}</option>
                                </c:forEach>
                            </select>
                 
              </td></tr>
               <tr>
                <td width="35%">选择职务：<td>
              <td width="55%">                 
                         <select  name="jobid" id="jobid">
                                <option value="" >请选择...</option>
                                <c:forEach items="${jobList}" var="job">
                                    <option value="${job.id}">${job.jobName}</option>
                                </c:forEach>
                            </select>
                 
              </td></tr>
            
      			</table>

         
           <div class="release_next_add">
           <input class="btn btn-primary gray" type="button" value="取消" onClick="window.parent.window.jBox.close()"/>
           <input  type="button"  class="btn btn-primary" onClick="formSubmit()"  value="确定" />
           </div>
    
    </div>
	</form:form>
  </body>
</html>
