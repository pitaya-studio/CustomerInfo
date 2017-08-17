<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>后台维护-后台添加</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />

	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
	
<script type="text/javascript">
	var fmoduleId = "";
	$(function(){
		launch();
		inputTips();
		$("#path" ).comboboxInquiry();
	});
	function getModules(obj) {
		var fmoduleId=$(obj).find("option:selected").val();
		if(fmoduleId != '') {
			$.ajax({
				type:"POST",
				url:"${ctx}/modules/sysmoduleconfig/getSonModulesFromJson?fmoduleId="+fmoduleId,
				async : false,
				success:function(data){
					$("#moduleId").empty().append('<option value=" ">请选择</option>');
					if(data != null && data.length != 0) {
						for(var index = 0; index < data.length; index++) {
							$("#moduleId").append('<option value=\'' + data[index][0] + '\'>' + data[index][1] + '</option>');
						}
					}
				}
			});
		}else if(fmoduleId == ''){
			$("#moduleId").empty().append('<option value=" ">请选择</option>');
		}
	}
</script>
</head>
<body>
            	<!--右侧内容部分开始-->
            	<form:form id="searchForm" modelAttribute="sysMOduleConfig" action="${ctx}/modules/sysmoduleconfig/saveSysModuleConfig" method="post" class="form-search">
                <div class="sysdiv sysdiv_coupon">
					<p>
						<label>批发商：</label>
						<span>
							<select id="companyId" name="companyId">
								<option value="">请选择</option>
						      		<c:forEach items="${officelist}" var="office">
			                	    	<c:choose>
									  		<c:when test="${companyId==office.id}"><option value="${office.id}"  selected="selected">${office.name}</option></c:when>
									  		<c:otherwise><option value="${office.id}">${office.name}</option></c:otherwise>	                	
			                	    	</c:choose>
		                      		</c:forEach>
							</select> 		
						</span>
					</p>
					<p>
						<label>模块：</label>
						<span>
							<select id="fmoduleId" name="fmoduleId" onchange="getModules(this)">
									<option value="">请选择</option>
					  				<c:forEach items="${fmodulelist}" var="module">
	                				<c:choose>
									<c:when test="${fmoduleId==module[0]}"><option value="${module[0]}"  selected="selected">${module[1]}</option></c:when>
									<c:otherwise><option value="${module[0]}">${module[1]}</option></c:otherwise>	                	
			                		</c:choose>
			                  		</c:forEach>
				 			</select> 
						</span>
					</p>
					<p>
						<label>子模块：</label>
						<span>
							<select id="moduleId" name="moduleId">
							<option value="">请选择</option>
						      <c:forEach items="${modulelist}" var="smodule">
		                	    <c:choose>
								  <c:when test="${moduleId==smodule[0]}"><option value="${smodule[0]}"  selected="selected">${smodule[1]}</option></c:when>
								  <c:otherwise><option value="${smodule[0]}">${smodule[1]}</option></c:otherwise>	                	
		                	    </c:choose>
		                      </c:forEach>
						  </select> 
					  </span>
					</p>
					<p>
						<label>页面名称：</label>
						<span>
							<input type="text" id = "pageName" name = "pageName" maxlength="50">
						</span>
					</p>
					<p>
						<label>页面路径：</label>
						<span>
							<select class="selectinput" name="path" id="path">
                                <option value=" ">所有</option>
                                <c:forEach items="${modulePathlist}" var="sysmodulepath">
			                        <c:choose>
				                		<c:when test="${path ==sysmodulepath.modulepath}"><option value="${sysmodulepath.modulepath }" selected="selected">${sysmodulepath.modulepath}</option></c:when>
				                		<c:otherwise><option value="${sysmodulepath.modulepath}">${sysmodulepath.modulepath}</option></c:otherwise>
				                	</c:choose>
                                </c:forEach>
                            </select>
						</span>
					</p>
					<p>
						<label>预览路径：</label>
						<span>
							<textarea id = "prepath" name = "prepath" rows="3" rows="3" class="input-xlarge" maxlength="200"></textarea>
						</span>
					</p>
					<p>
						<label>&nbsp;</label>
						<span>
							<input type="submit" id="btn_search" value="提交"  class="btn btn-primary ydbz_x">
						</span>
					</p>
				</div>
                <!--右侧内容部分结束-->
			</div>
        </div>
	</div>
   </form:form>
</body>
</html>
