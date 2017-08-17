<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="com.trekiz.admin.common.beanvalidator.BeanValidators"%>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>


<%@ include file="/WEB-INF/views/include/taglib.jsp"%> 
<%response.setStatus(200);%>
<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
	//记录日志
	Logger logger = LoggerFactory.getLogger("500.jsp");
	logger.error(ex.getMessage(), ex);
	// 为数据库连接偶发性被关闭定义的客户友好提醒
	String state = application.getInitParameter("DeploymentState"); 
	//确定展现页面 ，true 为展现友好页面 ，false 为展现异常信息页面
	boolean flag = true;
	// 确定友好页面所提供的按钮类型  ,true  提供返回上一页 ，false提供刷新页面
	boolean buttonFlag = true;
	if(null != ex){
		if(ex.getMessage()!=null){
			if(!ex.getMessage().contains("The last packet successfully received from the server was")){
				/**
				*  1 当报错内容为业务代码问题导致的异常 且state为'true' ，则展现友好页面,提供返回上一页按钮
				*  2 当报错内容为业务代码问题导致的异常 且state 为 false ，则展现异常信息页面 ，提供返回上一页按钮
				**/
				flag = "true".equalsIgnoreCase(state);	 
			}else{
				/**
				*   1 当报错内容为数据库连接偶发性fail则展现友好页面，提供刷新页面。
				**/
				buttonFlag = false;
			}
		}else{
			/**
			*  1 当报错内容为业务代码问题导致的异常 且state为'true' ，则展现友好页面,提供返回上一页按钮
			*  2 当报错内容为业务代码问题导致的异常 且state 为 false ，则展现异常信息页面 ，提供返回上一页按钮
			**/
			flag = "true".equalsIgnoreCase(state);	 
		}
		
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!-- 为数据库连接偶发性被关闭定义的客户友好提醒 --> 
<c:choose>  
	<c:when test="<%=flag%>">
		<title>系统提醒</title>
	</c:when>
	<c:otherwise>
        <title>500 - 系统内部错误</title>  
    </c:otherwise>
</c:choose>

    <meta name="decorator" content="wholesaler"/>  
</head>
<body>   
	<div class="container-fluid">
		
		 <!-- 为数据库连接偶发性被关闭定义的客户友好提醒 --> 
		<c:choose>
			<c:when  test="<%=flag%>">
				<!-- <h3>系统提醒：</h3>-->
			</c:when>
			<c:otherwise>
				<div class="page-header">
		         <h1>系统发生内部错误.</h1>
				 <p>错误信息：</p>
				 </div>
		    </c:otherwise>
		</c:choose> 
		
		<!-- 为数据库连接偶发性被关闭定义的客户友好提醒 --> 
		<c:choose>
			<c:when  test="<%=flag%>">
				<div class="site_error_tips">
					<form action="">
						<div><a class="site_error_butn" href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a></div>
					</form>
				</div>
			</c:when>
			<c:otherwise>
		         <p>
					<%   	 
						if (ex!=null){
							if (ex instanceof javax.validation.ConstraintViolationException){
								for (String s : BeanValidators.extractPropertyAndMessageAsList((javax.validation.ConstraintViolationException)ex, ": ")){
									out.print(s+"<br/>");
								}
							}else{
								out.print(ex.getMessage()+"<br/>"); 
								java.io.ByteArrayOutputStream bo=new java.io.ByteArrayOutputStream();    
								java.io.ObjectOutputStream oo=new java.io.ObjectOutputStream(bo);    
								ex.printStackTrace(new java.io.PrintStream(oo));
								String msg = bo.toString() ;
								msg = msg.replaceAll("at ", "<br/>&nbsp;at ");
								out.print(msg+"<br/>");
							}
						}
					%>
				</p>
				<div><a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a></div>
				<script>try{top.$.jBox.closeTip();}catch(e){}</script>
		    </c:otherwise>
		</c:choose>
	</div>
</body>
</html>