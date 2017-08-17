<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

    <content tag="three_level_menu">
		<li id="notSolved"><a href="${ctx}/synchronizeExceptionLog/manage/notSolved">待解决异常团期同步</a></li>
		<shiro:hasPermission name="synchronize:view"><li id="solved"><a href="${ctx}/synchronizeExceptionLog/manage/solved">已解决异常团期同步</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li id="success"><a href="${ctx}/synchronizeSuccessLog/manage/success">成功团期同步</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li id="unPrice"><a href="${ctx}/synchronizeUnPriceLog/manage/unPrice">成人价格不存在异常</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li id="un"><a href="${ctx}/synchronizeUnLog/manage/un">正反向产品ID未存在关联日志</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:config"><li id="config"><a href="${ctx}/mailConfig/manage/config">邮件发送设置</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li id="check"><a href="${ctx}/vedorSynchronizeStatus">正反向产品同步结果管理</a></li></shiro:hasPermission>
	</content>