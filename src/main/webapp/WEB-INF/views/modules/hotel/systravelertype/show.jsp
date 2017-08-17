<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>系统游客类型详情</title>
	<meta name="decorator" content="wholesaler"/>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">系统游客类型信息</div>
	<div class="maintain_add">
		<%-- <p>
			<label>uuid：</label> 
			<span>
				${sysTravelerType.uuid}
			</span>
		</p> --%>
		<p>
			<label>游客类型名称：</label> 
			<span>
				${sysTravelerType.name}
			</span>
		</p>
		<p>
			<label>游客类型简写：</label> 
			<span>
				${sysTravelerType.shortName}
			</span>
		</p>
		<p>
			<label>创建人：</label> 
			<span>
				${fns:getUserById(sysTravelerType.createBy).name}
			</span>
		</p>
		<p>
			<label>创建时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${sysTravelerType.createDate}"/>
			</span>
		</p>
		<p>
			<label>修改人：</label> 
			<span>
				${fns:getUserById(sysTravelerType.updateBy).name}
			</span>
		</p>
		<p>
			<label>修改时间：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${sysTravelerType.updateDate}"/>
			</span>
		</p>
		<p>
			<label>排序：</label> 
			<span>
				${sysTravelerType.sort}
			</span>
		</p>
		<p>
			<label>人员类型：</label> 
			<span>
                <c:choose> 
				  <c:when test="${sysTravelerType.personType == 0}">   
					   成人  
				  </c:when> 
				  <c:when test="${sysTravelerType.personType == 1}">   
				   	 婴儿  
				  </c:when> 
				   <c:when test="${sysTravelerType.personType == 2}">   
				   	儿童    
				  </c:when>
				  <c:otherwise>   
				            
				  </c:otherwise> 
				</c:choose> 
			</span>
		</p>
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
