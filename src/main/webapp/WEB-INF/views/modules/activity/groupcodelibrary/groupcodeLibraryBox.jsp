<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>团号库</title>
    <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="Cache-Control" content="no-store" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<link href="${ctxStatic}/bootstrap/2.3.1/css_${not empty cookie.theme.value ? cookie.theme.value:'default'}/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
	<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css" />
	
	<link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />

	
	<c:if test="${fns:getUser().userType eq '1' }">
	   <link href="${ctxStatic}/css/chose_account.css" type="text/css" rel="stylesheet" />
	</c:if>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/jquery-1.9.1.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
	<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/trekiz.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/vendor.service_mode1.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
	<script src="${ctxStatic }/json/json2.js" type="text/javascript" ></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript" ></script>
	<script src="${ctxStatic }/common/common.js" type="text/javascript" ></script>
	<script src="${ctxStatic }/js/socket/sockjs-0.3.min.js" type="text/javascript" ></script>
	<script src="${ctxStatic }/js/socket/stomp.js" type="text/javascript" ></script>
<script>
    $(function(){
//        $(document).on('mouseover', '.activitylist_bodyer_table tr', function(){
//            var  dt$ = $(this).find('td dl dt');
//            dt$.css("display","block");
//        });
//        $(document).on('mouseout', '.activitylist_bodyer_table tr', function(){
//            var  dt$ = $(this).find('td dl dt');
//            dt$.css("display","none");
//        });
        $(document).on('mouseover', '.activitylist_bodyer_table tr td dl dt', function(){
            $('dd').hide();
            var  dd$ = $(this).next();
            dd$.show();
        });
        
        $(document).on('click','.close-group-log',function(){
            $(this).parents('dd:first').hide();
        });
        
    });
    
    
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	};
</script>

<style type="text/css">
.pagination input {
    height: 24px;
    line-height: 10px;
    margin-bottom: 0;
    vertical-align: middle;
    width: 30px;
}

</style>

</head>
<body>

<!--团号库弹出层开始-->
<div id="groupLib">
    <div class="content-box" style="width:600px;margin:0 auto;padding-bottom: 14px;padding-top: 14px;">
    
        <div class="search" style="margin:0 14px 24px 14px">
            <form id="searchForm" action="${ctx}/activity/groupcodelibrary/toGroupcodeLibraryBox" method="post">
            
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
                <input id="pageSize" name="pageSize"  type="hidden" value="${page.pageSize}" /> 
                <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" /> <!-- ${conditionsMap.orderBy} -->
                
                <!-- 处理查询类型  -->
                <input id="groupcodelibtype" name="groupcodelibtype" type="hidden" value="${groupcodelibtype}" />
                
            
	            <label>团号：</label>
	            <input id="groupNo" class="inpuTxt" name="groupNo" value="${groupNo}" style="width: 100px;height: 24px;"/>
	            <label style="margin-left:20px;">创建日期：</label>
	            <input id="groupCreateDate" class="inputTxt dateinput" name="groupCreateDate" value="${groupCreateDate}"
	                   onfocus="WdatePicker({
	                             onpicking:function(dp){
	                                 var vvv=dp.cal.getNewDateStr();
	                                 }
	                             })">
	            <input class="btn btn-primary" value="搜索" type="submit" style="margin-left: 20px;">
            </form>
        </div>
        
        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
        <table class="table activitylist_bodyer_table">
            <thead style="height: 30px;">
            <th width="10%" style="height: 30px;">序号</th>
            <th width="20%" style="height: 30px;">创建日期</th>
            <th width="40%" style="height: 30px;">团号</th>
            <th width="20%" style="height: 30px;">修改记录</th>
            </thead>
            <tbody>
            <% int n = 1; %>
            <c:forEach items="${page.list}" var="groupCodeLibraryBean">
	            <tr>
	                <td><%=n++%></td>
	                <td><fmt:formatDate value="${groupCodeLibraryBean.createDate}" pattern="yyyy-MM-dd"/></td>
	                <td>${groupCodeLibraryBean.group_code}</td>
	                <td class="p0">
	                    <c:choose>
							     <c:when test="${groupCodeLibraryBean.id == null }">
							     		
							     </c:when>
							     <c:otherwise>
							       <!-- 处理团号生命周期 -->
						           <dl class="modify-log">
				                        <dt>
				                        <!-- 
				                        <img src="${ctxStatic}/logo/lmels-ts.png" />
				                         -->
				                            <img src="${ctxStatic}/images/iconfont-jilu.png">
				                        </dt>
				                        <dd class="block">
				                            <span class="close-group-log" title='关闭'>x</span>
				                            <span class="modify-log-img"></span>
				                            <div class="content-group-log">
				                                <article>
				                                    <!-- 原始团号部分（修改前） -->
				                                    <section>
				                                        <span class="point-time step_yes"></span>
				                                        <time>
				                                            <span>
				                                            <fmt:formatDate value="${groupCodeLibraryBean.groupCodeLifeBycle.oldGroupCodeCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				                                            </span>
				                                        </time>
				                                        <aside>
				                                            <p class="things">
				                                                <span>【${groupCodeLibraryBean.groupCodeLifeBycle.oldGroupCodeCreatebyName}】创建了 【团号】</span><br>
				                                                <span>【${groupCodeLibraryBean.groupCodeLifeBycle.oldGroupCode}】</span>
				                                            </p>
				                                        </aside>
				                                    </section>
				                                    <!-- 修改记录（修改后） -->
				                                    <c:forEach items="${groupCodeLibraryBean.groupCodeLifeBycle.groupcodeModifiedRecords}" var="groupCodeLifeBycleBean">
					                                    <section>
					                                        <span class="point-time step_yes"></span>
					                                        <time>
					                                            <span>
					                                               <fmt:formatDate value="${groupCodeLifeBycleBean.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					                                            </span>
					                                        </time>
					                                        <aside>
					                                            <p class="things">
					                                                <span>【${groupCodeLifeBycleBean.updateByName}】修改了【团号】</span><br>
					                                                <span >【${groupCodeLifeBycleBean.groupcodeOld}】改为【${groupCodeLifeBycleBean.groupcodeNew}】</span>
					                                            </p>
					                                        </aside>
					                                    </section>
				                                    </c:forEach>
				                                   
				                                </article>
				                            </div>
				                        </dd>
				                    </dl>     
							     </c:otherwise>
						</c:choose>

	                </td>
	            </tr>
            </c:forEach>
            </tbody>
        </table>
        <!-- 分页部分 -->
		<div class="pagination clearFix">${page}</div>
		<!--右侧内容部分结束  模拟分页
        <div class="page">
            <div class="pagination">
                <div class="endPage">
                    <ul>
                        <li class="disabled"><a href="javascript:">« 上一页</a></li>
                        <li class="active"><a href="javascript:">1</a></li>
                        <li class="disabled"><a href="javascript:">下一页 »</a></li>
                        <li class="disabled controls"><a href="javascript:">共 5 条</a></li>
                    </ul>
                    <div style="clear:both;"></div>
                </div>
                <div style="clear:both;"></div>
            </div>
        </div>
        -->
    </div>
</div>


</body>
</html>