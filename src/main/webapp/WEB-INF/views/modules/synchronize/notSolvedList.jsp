<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>同步异常信息管理</title>
	<meta name="decorator" content="wholesaler"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	    // 初始化全选操作
	    $(window).load(function(){
			$('#notSolved').attr('class', 'active');
	    	//全选
     		$("[name='checkedAll']").click(function(){
				//所有checkbox跟着全选的checkbox走。
				$('[name=items]:checkbox,[name=checkedAll]').attr("checked", this.checked );
		 	});
		 	//如果放在开始位置，则click事件因为元素还没有加载完成，所有不能绑定，所以放到最后位置
            $('[name=items]:checkbox').click(function(){
   				//定义一个临时变量，避免重复使用同一个选择器选择页面中的元素，提升程序效率。
   				var $tmp=$('[name=items]:checkbox');
   				//用filter方法筛选出选中的复选框。并直接给CheckedAll赋值。
   				$("[name='checkedAll']").attr('checked',$tmp.length==$tmp.filter(':checked').length);
   	 		});
	    })
	    // 批量处理
	    function confirmBatchHandle(mess) {
	    	var ids = new Array();
	    	$('input[type=checkbox][name=items]:checked').each(function(){
				ids.push($(this).val());//把值都封装到数组中
			})
			if(ids.length == 0) {
				$.jBox.info('未选择要处理的数据','系统提示');
				return false;
			} else {
				var strIds = ids.join(",");
			}
			top.$.jBox.confirm(mess,'系统提示',function(v){
				if(v=='ok'){
					loading('正在提交，请稍等...');
					$("#searchForm").attr("action","${ctx}/synchronizeExceptionLog/manage/batchHandle/" + strIds);
					$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
	    // 显示sql
	    function showSql(sqlStr) {
			$.jBox.info(sqlStr,'sql语句');
		}
	</script>
</head>
<body>
	<!-- <ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/synchronizeExceptionLog/manage/notSolved">待解决异常团期同步</a></li>
		<shiro:hasPermission name="synchronize:view"><li><a href="${ctx}/synchronizeExceptionLog/manage/solved">已解决异常团期同步</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li><a href="${ctx}/synchronizeSuccessLog/manage/success">成功团期同步</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li><a href="${ctx}/synchronizeUnPriceLog/manage/unPrice">成人价格不存在异常</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li><a href="${ctx}/synchronizeUnLog/manage/un">正反向产品ID未存在关联日志</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:config"><li><a href="${ctx}/mailConfig/manage/config">邮件发送设置</a></li></shiro:hasPermission>
	</ul> -->
	<%@ include file="header.jsp" %>
	<form:form id="searchForm" modelAttribute="exceptionSynchronizeLog" action="${ctx}/synchronizeExceptionLog/manage/notSolved" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>类型：</label>
		<form:select id="type" path="type" class="input-medium">
			<form:option value="" label=""/>
			<form:option value="1" label="添加"/>
			<form:option value="2" label="修改"/>
			<form:option value="3" label="删除"/>
		</form:select>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th class="table_borderLeftN" width="3%">全选<input name="checkedAll" type="checkbox"/></th><th>类型</th><th>产品ID</th><th>团期ID</th><th>产生时间</th><shiro:hasPermission name="synchronize:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="synchronize">
			<tr>
				<td><input type="checkbox" name="items" value="${synchronize.id}"/></td>
				<td>
					<c:if test="${synchronize.type==1}">
						添加
					</c:if>
					<c:if test="${synchronize.type==2}">
						修改
					</c:if>
					<c:if test="${synchronize.type==3}">
						删除
					</c:if>
				</td>
				<td>${synchronize.idTrekizwholesaler}</td>
				<td>${synchronize.groupIdTrekizwholesaler}</td>
				<td>
					<fmt:formatDate value="${synchronize.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="synchronize:edit"><td>
    				<a onClick="showSql('${synchronize.sqlStr}')">查看sql</a>&nbsp;&nbsp;&nbsp;
    				<a href="${ctx}/synchronizeExceptionLog/manage/batchHandle/${synchronize.id}">处理</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="page">
		<div class="pagination">
			<dl>
				<dt><input name="checkedAll" type="checkbox"/>全选</dt>
				<dd>
					<a onClick="confirmBatchHandle('确定批量处理吗','off')">批量处理</a>
				</dd>
			</dl>
			<div class="endPage">${page}</div>
			<div style="clear:both;"></div>
		</div>	
	</div>
</body>
</html>