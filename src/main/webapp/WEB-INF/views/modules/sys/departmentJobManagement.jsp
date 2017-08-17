<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>职务管理</title>
		<meta name="decorator" content="wholesaler"/>
		<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
		<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
		<link rel="stylesheet" href="css/jbox.css" />
		<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
		<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
        <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
		<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
		<script type="text/javascript">
		$(function(){
			g_context_url = "${ctx}";
			//搜索条件筛选
			launch();
			//操作浮框
			operateHandler();
		 //取消一个checkbox 就要联动取消 全选的选中状态
		            $("input[name='ids']").click(function(){
		                if($(this).attr("checked")){
		
		                }else{
		                    $("input[name='allChk']").removeAttr("checked");
		                }
		            });
		
		  var resetSearchParams = function(){
		            $(':input','#searchForm')
		                    .not(':button, :submit, :reset, :hidden')
		                    .val('')
		                    .removeAttr('checked')
		                    .removeAttr('selected');
		            $('#country').val("");
		        }
		    $('#contentTable').on('change','input[type="checkbox"]',function(){
		        if( $('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length ){
		            $('[name="allChk" ]').attr('checked',true);
		        }else{
		            $('[name="allChk" ]').removeAttr('checked');
		        }
		    });
		});
		
		//展开收起
		function expand(child, obj) {
			if($(child).is(":hidden")){
				$(child).show();
				$(obj).parents("td").addClass("td-extend");
				$(obj).parents("tr").addClass("tr-hover");
		
			}else{
				$(child).hide();
				$(obj).parents("td").removeClass("td-extend");
				$(obj).parents("tr").removeClass("tr-hover");
		
			}
		}
		function checkall(obj){
		    if($(obj).attr("checked")){
		        $('#contentTable input[type="checkbox"]').attr("checked",'true');
		        $("input[name='allChk']").attr("checked",'true');
		    }else{
		        $('#contentTable input[type="checkbox"]').removeAttr("checked");
		        $("input[name='allChk']").removeAttr("checked");
		    }
		}
		
		function checkreverse(obj){
		    var $contentTable = $('#contentTable');
		    $contentTable.find('input[type="checkbox"]').each(function(){
		        var $checkbox = $(this);
		        if($checkbox.is(':checked')){
		            $checkbox.removeAttr('checked');
		        }else{
		            $checkbox.attr('checked',true);
		        }
		    });
		}
			
			function page(n,s){
				$("#pageNo").val(n);
				$("#pageSize").val(s);
				$("#searchForm").attr("action","${ctx}/sys/department/jobManagement");
				$("#searchForm").submit();
		    }
			
		</script>
	</head>
	<body>
	<page:applyDecorator name="department_op_head" >
		<page:param name="current">departmentJob</page:param>
	</page:applyDecorator>
		<form method="post" action="${ctx}/sys/department/jobManagement" id="searchForm">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		</form>
	
         <%--<div class="supplierLine tr">--%>
             <%--<a href="javascript:void(0)"  onclick="jbox__pop_add_duty_fab();" id="all" class="select" style=" float:right !important">新增部门职务</a>--%>
         <%--</div>--%>
		<p class="main-right-topbutt">
			<a href="javascript:void(0)" onclick="jbox__pop_add_duty_fab();"id="all" class="select" style="margin-bottom:10px">新增部门职务</a>
		</p>
						<!--右侧内容部分开始-->
						<table id="contentTable" class="activitylist_bodyer_table mainTable">
							<thead>
								<tr>
									<th width="10%">序号</th>
									<th width="30%">职务名称</th>
									<th width="40%">职务类别</th>
									<th width="20%">操作</th>
								</tr>
							</thead>
							<tbody>
							<c:if test="${fn:length(resultList) <= 0 }">
								<tr class="toptr" >
									<td colspan="4" style="text-align: center;color:green">没有符合条件的记录</td>
								</tr>
							</c:if>
							<c:forEach items="${resultList}" var="list" varStatus="count">
								<tr>
									<td class="tc">${count.count }</td>
									<td class="tc">${list.name }</td>
									<c:if test="${list.type==0 }"><td class="tr">计调类</td></c:if>
									<c:if test="${list.type==1 }"><td class="tr">销售类</td></c:if>
									<c:if test="${list.type==2 }"><td class="tr">财务类</td></c:if>
									<c:if test="${list.type==3 }"><td class="tr">管理类</td></c:if>
									<!-- <c:if test="${list.type==4 }"><td class="tr">签证类</td></c:if> -->
									<c:if test="${list.type==5 }"><td class="tr">行政类</td></c:if>
									<td><a onclick="jbox__pop_mod_duty_fab('${list.type}','${list.name }','${list.uuid }');">修改</a> <a onclick="jbox__pop_del_duty_fab('${list.uuid}');">删除</a></td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<!--右侧内容部分结束-->
		<div class="pagination clearFix">${pageStr}</div>

		<!--S新增弹出层-->

		<div id="pop_add_duty">
			<div class="pop_add_duty">
				<p>
					<label for="">职务类别：</label>
					<select name="orderShowType" id="orderShowType">
						<option value="0">计调类</option>
						<option value="1">销售类</option>
						<option value="2">财务类</option>
						<option value="3">管理类</option>
						<!-- <option value="4">签证类</option> -->
						<option value="5">行政类</option>
					</select>
				</p>
                <p>
					<label for="">职务名称：</label>
					<input value="" class="txtPro inputTxt" id="jobName" />
				</p>
			</div>
		</div>
		
		<!--E新增弹出层-->
		<!--S删除弹出层-->

		<div id="pop_del_duty">
			<div class="pop_del_duty">
				<p>
					确认删除该职务
				</p>
			</div>
		</div>

		<!--E删除弹出层-->
	</body>
</html>