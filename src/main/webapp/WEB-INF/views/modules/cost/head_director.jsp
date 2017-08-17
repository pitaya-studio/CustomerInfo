<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="text/javascript">

    $(function() {
        $('#${type}').addClass('active');
    });
</script>
<content tag="three_level_menu">
    <shiro:hasPermission name="cost:manager:view">
        <li id="directorPre"><a href="${ctx}/cost/manager/list/directorPre">待审核</a></li>
        <li id="directorNotApprove"><a href="${ctx}/cost/manager/list/directorNotApprove">未通过</a></li>
        <li id="directorApprove"><a href="${ctx}/cost/manager/list/directorApprove">已通过</a></li>
    </shiro:hasPermission>
</content>