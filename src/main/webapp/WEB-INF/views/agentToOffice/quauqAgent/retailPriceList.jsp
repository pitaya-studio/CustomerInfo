<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>供应价查看</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=8,Chrome=1"/>
    <title>供应商查看</title>
 	<link rel="stylesheet" href="${ctxStatic}/css/common.css"/>
    <!--css初始化代码-->
     <link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.css"/>
    <!--字体图标-->
     <%--  <link rel="stylesheet" href="${ctxStatic}/css/nav.css"/>--%>
    <%--<link rel="stylesheet" href="${ctxStatic}/css/table.css"/>--%>
    <%--<link rel="stylesheet" href="${ctxStatic}/css/page-T2.css" />--%>
    <link rel="stylesheet" href="${ctxStatic}/css/supplierLook.css"/> 
	<script type="text/javascript">
		$(function(){
			$('.handle').hover(function() {
			    	if(0 != $(this).find('a').length){
			    		$(this).addClass('handle-on');
						$(this).find('dd').addClass('block');
			    	}
				},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
			
			$("#agentType").comboboxInquiry();
		});
	
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sys/office/officeListForExport");
			$("#searchForm").submit();
	    	return false;
	    }
	    
	    function detail(agentid){
	    	window.open("${ctx}/quauqAgent/manage/agentdetail/"+agentid);
	    }
	    
	    function mod(agentid){
	    	window.location.href = "${ctx}/quauqAgent/manage/modifyBaseForm/"+agentid;
	    }
	    
	    function del(agentid){
	    	var mess="确定要删除数据?"
			top.$.jBox.confirm(mess,'系统提示',function(v){
			if(v=='ok'){
				loading('正在提交，请稍等...');
				window.location.href = "${ctx}/quauqAgent/manage/del/" + agentid + "";
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
		
		/**
		 * 导出密码
		 */
		function downloadPwd() {
			$("#exportForm").submit();
		}
		
		/**
		 * 启用、禁用渠道商
		 * @param obj checkbox
		 */
		function changeEnableStatus(obj){
		
			var agentId = $(obj).parent().parent().find("input[name=agentId]").val();
			var checkStatus = 0;
			var isClosedByExit = true;  // 弹窗是通过右上角退出按钮关闭的 
			if ($(obj).is(':checked')) {
				checkStatus = 1;
			} else {
				checkStatus = 0;
			}
			var statusStr = (checkStatus == 1 ? "启用" : "禁用");
			
			$.jBox.confirm("是否"+statusStr+"？","提示",function(v,h,f){
      			if (v == 'ok') {
      				confirmFlag  = true;
					$.ajax({
						type:"POST",
						url: "${ctx}/quauqAgent/manage/changeEnableStatus",
						data: {
							agentId : agentId,
							checkStatus : checkStatus
						},
						datatype:"json",
						success: function(resultData){
							if(resultData){
								top.$.jBox.info(statusStr +"成功!", "消息");
							} else {
								top.$.jBox.info(statusStr +"失败!" + resultData, "错误");
							}
						}
					});
      			} else {
      				if ($(obj).is(':checked')) {
						$(obj).attr("checked", false);
					} else {
						$(obj).attr("checked", true);
					}					
      			}
      			isClosedByExit = false;
      		}, {
      			showClose:true,
      			closed: function () {
      				if (isClosedByExit) {      				
	      				if ($(obj).is(':checked')) {
							$(obj).attr("checked", false);
						} else {
							$(obj).attr("checked", true);
						}
      				}
      			}
      		});
		}
		function exportoffice(){
			var officeId = $("input[name='officeId']").val();
			var officeName = $("input[name='officeName']").val();
			var groupIds = $("input[name='groupIds']").val();
			alert("officeId"+officeId + " officeName "+officeName+" groupsId "+groupIds);
			
			
		}
		function showFlag(){
		
		
		}
		
	</script>
	
</head>
<body>
    <page:applyDecorator name="agent_op_head">
        <page:param name="current">agentList</page:param>
    </page:applyDecorator>
    <form name="searchForm" id="searchForm" action="${ctx}/sys/office/officeListForExport">
    	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
    </form>
    
    <!--右侧内容部分开始-->
     <div>
        <div class="main-content">
            <div class="look_over">供应商查看</div>
            <div class="hint"><i class="fa fa-exclamation-circle reminder-i" style="color:red" aria-hidden="true"></i> 有此标识的的批发商供应价异常，可导出表格查看详情。
            </div>
        </div>
        <div>
            <table id="contentTable" class="activitylist_bodyer_table mainTable">
                <thead>
                <tr>
                    <th width="17%">序号</th>
                    <th width="40%">批发商名称</th>
                    <th width="17%">最后导出时间</th>
                    <th width="17%">操作</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${page.list != null }">
                	<c:forEach items="${page.list }" var="office" varStatus="status">
	                <tr align = "center">
	                    <td class="tc">${status.index+1 }</td>
	                    <td class="tc">
	                    	<c:if test="${office.flag}">
	                    		<i class="fa fa-exclamation-circle reminder-i color_red" style="color:red" aria-hidden="true"></i>
	                    	</c:if>
	                    	${office.name}</td>
	                    <td class="tc">
	                    	<c:if test="${empty office.export_time}">
	                    		尚未导出
	                    	</c:if>
	                    	<c:if test="${!empty office.export_time}">
	                    		${office.export_time}
	                    	</c:if>
	                    </td>
	                    <td class="tc">
	                    	<input type="hidden" name="officeId" value="${office.id}">
	                    	<input type="hidden" name="officeName" value="${office.name}">
	                    	<input type="hidden" name="groupIds" value="${office.activityGrouopIds}">
	                    	<input type="hidden" name="flag" value="${office.flag}">
	                    	<%-- 
	                    	<c:if test="${offcie.flag}">
	                    		<a href="${pageContext.request.contextPath}/a/sys/office/exportExcelOffice?officeId=${office.id}&officeName=${office.name}&activityGrouopIds=${office.activityGrouopIds}">导出表格</a>
	                   		</c:if> --%>
	                   		<c:choose>
	                   			<c:when test="${office.flag}">
	                   				 <a href="${pageContext.request.contextPath}/a/sys/office/exportExcelOffice?officeId=${office.id}&officeName=${office.name}&activityGrouopIds=${office.activityGrouopIds}">导出表格</a>
	                   			</c:when>
	                   			<c:otherwise>
	                   				无需导出
	                   			</c:otherwise>
	                   		</c:choose>
     
 						</td>
	                </tr>
	                </c:forEach>
                </c:if>
                </tbody>
            </table>
        </div>
    <!--右侧内容部分结束--> 
	<div class="pagination">${page}</div>
	</div>
</body>
</html>