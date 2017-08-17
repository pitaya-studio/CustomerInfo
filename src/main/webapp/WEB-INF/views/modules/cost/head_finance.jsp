<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="text/javascript">

    $(function() {
        $('#${type}').addClass('active');
    });
</script>
<content tag="three_level_menu">
    <shiro:hasPermission name="cost:manager:view">
        <li id="financePre"><a href="${ctx}/cost/manager/list/financePre">待录入</a></li>
        <li id="financeNotApprove"><a href="${ctx}/cost/manager/list/financeNotApprove">未通过</a></li>
        <li id="financeApprove"><a href="${ctx}/cost/manager/list/financeApprove">已通过</a></li>
    </shiro:hasPermission>
</content>