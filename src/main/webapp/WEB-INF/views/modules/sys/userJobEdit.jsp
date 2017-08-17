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
          
            var jobid= $("#jobid").val();
            if (jobid==null || jobid==""){ 
              alert("请选择职务");
              return false;
            }
        
            $.ajax({
                        type:"POST",
                        url:"${ctx}/sys/review/checkUserJob",
                        data:{
                           jobid: jobid,userdeptid:${userdeptid}
                        },
                        success:function(msg) {                        
//                        if(msg>=1) {
//                          alert("此流程审批配置已经存在");
//                           return false;
//                         }
                        $("#companyDictForm").submit();
                        }
                    });
              
          

        
        	
        }
	</script>


  </head>
  
  <body>
    <form:form id="companyDictForm" modelAttribute="sysdefinedict" action="${ctx}/sys/userDept/userJobSave" target="_parent" method="post">
		     <input  type="hidden"  id="userdeptid" name="userdeptid" value="${userdeptid}" />
		    
		<div class="msg_div">
		 
	

          <table  width="100%"  style="cellspacing:10px;cellpadding:10px;margin-left:10px;font-family:微软雅黑, Arial,宋体; color: #333;background:#ffffff;font-size: 12px" >
      			<tr>
      			    <td width="35%">职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;务 ：<td>
      				<td width="55%">  
      				 <select name="jobid" id="jobid"><option value="" selected>请选择</option>
                <c:forEach items="${jobList}" var="jobList">
            	   <option value="${jobList.id }">${jobList.jobName}</option>
                   </c:forEach>
                  </select> 
      			<td>
      			</tr>
     	   
      		</table>
          <input  type="hidden" id="userid" name="userid" value="${userid}" />
           <div class="release_next_add">
           <input class="btn btn-primary gray" type="button" value="取消" onClick="window.parent.window.jBox.close()"/>
           <input  type="button"  class="btn btn-primary" onClick="formSubmit()"  value="确定" />
           </div>
          </div>
	</form:form>
  </body>
</html>
