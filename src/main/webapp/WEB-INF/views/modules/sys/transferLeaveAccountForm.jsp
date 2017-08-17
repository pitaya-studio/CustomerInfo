<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>离职账户转移</title>
	<meta name="decorator" content="wholesaler"/>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/sys/transferLeaveAccount.js" type="text/javascript"></script>
   <style type="text/css">
   </style>
</head>
<body>
    <content tag="three_level_menu">
		<li><a href="${ctx}/sys/profile/info">个人信息</a></li>
		<li><a href="${ctx}/sys/profile/modifyPwd">修改密码</a></li>
        <shiro:hasPermission name="sys:user:view"><li><a href="${ctx}/sys/user/">账号管理</a></li></shiro:hasPermission>
        <shiro:hasPermission name="sys:user:edit"><li><a href="${ctx}/sys/user/form">账号添加</a></li></shiro:hasPermission>
        <shiro:hasPermission name="transfer:leave:account">
        	<li class="active"><a href="${ctx}/sys/user/transferLeaveAccountForm">离职账户转移</a></li>
        </shiro:hasPermission>
	</content>
	<div class="sysdiv">
		<input id="getUserUrl" value="${ctx}/sys/user/getUserInfoById" type="hidden">
		<form method="post" action="${ctx}/sys/user/transferLeaveAccount" id="searchForm">
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co1 wpr20">
					<label>业务：</label>
					<select id="businessType" class="txtPro">
						<option value="1">单团</option>
						<option value="2">散拼</option>
						<option value="3">游学</option>
						<option value="4">大客户</option>
						<option value="5">自由行</option>
						<option value="6">签证</option>
						<option value="7">机票</option>
						<option value="10">游轮</option>
					</select>
				</div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co1 wpr20">
					<label>模块：</label>
					<select id="modulesType" class="txtPro ">
						<option value="1">产品</option>
						<option value="2">订单</option>
					</select>
				</div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co1 wpr20">
					<label>离职账号：</label>
					<select name="leaveUserId" id="leaveUserId" class="selectinput">
			            <option value="">请选择</option>
			            <c:forEach items="${users }" var="user">
			                <option value="${user.id}">${user.name}</option>
			            </c:forEach>
			        </select>
					<span class="notice_msg_bg">
						<span class="msg_error" id="li_users_w" style="display:none;"></span>
						<span class="msg_right" id="leave_users_msg"></span>
						<span class="msg_right" id="leave_users_msg2"></span>
					</span>
				</div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co1 wpr20">
					<label>转入账号：</label>
					<select name="transferUserId" id="transferUserId" class="selectinput">
			            <option value="">请选择</option>
			            <c:forEach items="${users }" var="user">
			                <option value="${user.id}">${user.name}</option>
			            </c:forEach>
			        </select>
					<span class="notice_msg_bg">
						<span class="msg_error" id="li_users_w" style="display:none;"></span>
						<span class="msg_right" id="transfer_users_msg"></span>
						<span class="msg_right" id="transfer_users_msg2"></span>
					</span>
				</div>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co1 wpr20">
					<label>&nbsp;</label>
					<label>
						<%--<a class="ydbz_s" onclick="transferLeaveAccount();">提交</a>--%>
						<input class="btn btn-primary ydbz_x" onclick="transferLeaveAccount();" value="提交" type="submit">
					</label>
				</div>
			</div>
		</form>
	</div>
</body>
</html>