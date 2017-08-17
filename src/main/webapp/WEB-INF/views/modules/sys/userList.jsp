<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>账号管理</title>
    <meta name="decorator" content="wholesaler"/>
    <style type="text/css">.sort {
        color: #0663A2;
        cursor: pointer;
    }</style>

    <script type="text/javascript">
        $(document).ready(function () {
            //added for UG_V2 展开/收起筛选 by tlw at 20170227
            launch();
            //下拉可选
            $("#role").comboboxInquiry();//角色
            $("#department").comboboxInquiry();//部门
            $("#job").comboboxInquiry();//职务 

            // 表格排序
            var orderBy = $("#orderBy").val().split(" ");
            $("#contentTable th.sort").each(function () {
                if ($(this).hasClass(orderBy[0])) {
                    orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "DESC" ? "down" : "up";
                    $(this).html($(this).html() + " <i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
                }
            });
            $("#contentTable th.sort").click(function () {
                var order = $(this).attr("class").split(" ");
                var sort = $("#orderBy").val().split(" ");
                for (var i = 0; i < order.length; i++) {
                    if (order[i] == "sort") {
                        order = order[i + 1];
                        break;
                    }
                }
                if (order == sort[0]) {
                    sort = (sort[1] && sort[1].toUpperCase() == "DESC" ? "ASC" : "DESC");
                    $("#orderBy").val(order + " DESC" != order + " " + sort ? "" : order + " " + sort);
                } else {
                    $("#orderBy").val(order + " ASC");
                }
                page();
            });
        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").attr("action", "${ctx}/sys/user/");
            $("#searchForm").submit();
            return false;
        }

        function downloadPwd() {
            $("#exportForm").submit();
        }
        function resetPwd() {
            $.ajax({
                type: "POST",
                url: "${ctx}/sys/user/resetPwd",
                data: {},
                success: function (msg) {
                    top.$.jBox.tip("重置成功", 'success');
                }
            });
        }
    </script>
</head>
<body>
<div id="importBox" class="hide">
    <form id="importForm" action="${ctx}/sys/user/import" method="post" enctype="multipart/form-data"
          style="padding-left:20px;text-align:center;"><br/>
        <input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
        <input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
        <a href="${ctx}/sys/user/import/template">下载模板</a>
    </form>
</div>
<content tag="three_level_menu">
    <li><a href="${ctx}/sys/profile/info">个人信息</a></li>
    <li><a href="${ctx}/sys/profile/modifyPwd">修改密码</a></li>
    <shiro:hasPermission name="sys:user:view">
        <li class="active"><a href="${ctx}/sys/user/">账号管理</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="sys:user:edit">
        <li><a href="${ctx}/sys/user/form">账号添加</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="transfer:leave:account">
        <li><a href="${ctx}/sys/user/transferLeaveAccountForm">离职账户转移</a></li>
    </shiro:hasPermission>
</content>
<form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/user/" method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
    <%--added for UG_V2 将登录名作为主要筛选项，其他筛选项放入筛选部分隐藏，点击筛选按钮展开。 by tlw at 20170227 start--%>
    <div class="activitylist_bodyer_right_team_co">
        <div class="activitylist_bodyer_right_team_co2 pr">
            <form:input path="loginName" htmlEscape="false" maxlength="50" placeholder="登录名"
                        class="input-medium inputTxt inputTxtlong searchInput"/>
        </div>
        <div class="zksx">筛选</div>
        <div class="form_submit">
            <input id="btnSubmit" class="btn btn-primary ydbz_x" type="submit" value="搜索" onClick="return page();"/>
            <input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"><%--UG_V2清空按钮无效问题 将type改成button by tlw at20170309--%>
            <shiro:hasPermission name="sys:user:exportPwd">
                <input id="exportPwd" class="btn ydbz_x" type="button" value="导出密码" onClick="downloadPwd();"/>
            </shiro:hasPermission>
        </div>
        <div style="display: none;" class="ydxbd">
            <span></span>
            <div class="activitylist_bodyer_right_team_co3">
                <label class="activitylist_team_co3_text">部门：</label>
                <select id="department" name="department">
                    <option value=" ">请选择</option>
                    <c:forEach items="${deptList}" var="dept">
                        <option value="${dept.id }"
                                <c:if test="${deptId == dept.id}">selected="selected"</c:if>>${dept.name }</option>
                    </c:forEach>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">姓名：</label>
                <form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">角色：</label>
                <select id="role" name="role">
                    <option value=" ">请选择</option>
                    <c:forEach items="${allRoles}" var="role">
                        <option value="${role.id }"
                                <c:if test="${roleId == role.id}">selected="selected"</c:if>>${role.name }</option>
                    </c:forEach>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">职务：</label>
                <select id="job" name="job">
                    <option value=" ">请选择</option>
                    <c:forEach items="${jobList}" var="jobList">
                        <option value="${jobList.id }"
                                <c:if test="${jobId == jobList.id}">selected="selected"</c:if>>${jobList.name }</option>
                    </c:forEach>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">微信号：</label>
                <form:input path="weixin" htmlEscape="false" maxlength="20" class="input-medium"/>
            </div>
            <c:if test="${fns:getUser().id==1}">
                <!-- <li> -->
                <!-- <label>归属公司：</label> -->
                <%--                 <form:select path="company.id"> --%>
                <%--                     <form:option value="" label="请选择"/> --%>
                <%--                       <c:forEach items="${fns:getOfficeList(fasle,'','')}" var="company" varStatus="idxStatus"> --%>
                <%--                       <c:if test="${company.id!=1}"> --%>
                <%--                       <form:option value="${company.id}" label="${company.name}"/> --%>
                <%--                      <%-- <c:if test="${company.id eq companyId}">selected</c:if> --% --%>
                <%--                       </c:if> --%>
                <%--                       </c:forEach> --%>
                <%--                 </form:select> --%>
                <!-- </li> -->
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">归属公司：</label>
                    <form:select path="company.id">
                        <form:option value="" label="请选择"/>
                        <c:forEach items="${fns:getOfficeList(false,'','')}" var="company" varStatus="idxStatus">
                            <c:if test="${company.id!=1}">
                                <form:option value="${company.id}" label="${company.name}"/>
                            </c:if>
                        </c:forEach>
                    </form:select>
                </div>
                <%--<div class="activitylist_bodyer_right_team_co1">--%>
                <%--<label class="activitylist_team_co3_text">登录名：</label>--%>
                <%--<form:input path="loginName" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
                <%--</div>--%>
            </c:if>

        </div>
    </div>
    <%--added for UG_V2 将登录名作为主要筛选项，其他筛选项放入筛选部分隐藏，点击筛选按钮展开。 by tlw at 20170227 end--%>

</form:form>
<tags:message content="${message}"/>
<table id="contentTable" class="activitylist_bodyer_table mainTable">
    <thead style="background:#403738">
    <tr>
        <th width="3%">序号</th>
        <th width="10%">归属公司</th>
        <th class="sort loginName" width="5%">登录名</th>
        <th class="sort name" width="5%">姓名</th>
        <th width="6%">电话</th>
        <th width="6%">手机</th>
        <th width="6%">微信号</th>
        <th width="12%">角色</th>
        <th width="20%" colspan="2">部门-职务</th>
        <shiro:hasPermission name="sys:user:edit">
            <th width="10%">操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="user" varStatus="count">
        <c:set var="len" value="${fn:length(user.deptJobRelation)}"></c:set>
        <tr>
            <td class="tc" rowspan="${len==0?1:len }">${count.count }</td>
            <td class="tc" rowspan="${len==0?1:len }">
                <c:choose>
                    <c:when test="${fns:getUser().id == 1}">
                        <c:if test="${user.agentId!=null}">${fns:getAgentName(user.agentId)}</c:if><c:if
                            test="${user.agentId==null}">${user.company.name}</c:if>${user.company.name}
                    </c:when>
                    <c:otherwise>
                        <c:if test="${user.agentId!=null}">${fns:getAgentName(user.agentId)}</c:if><c:if
                            test="${user.agentId==null}">接待社内部</c:if>
                    </c:otherwise>
                </c:choose>
            </td>
            <td rowspan="${len==0?1:len }"><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
            <td rowspan="${len==0?1:len }">${user.name}</td>
            <td rowspan="${len==0?1:len }">${user.phone}</td>
            <td rowspan="${len==0?1:len }">${user.mobile}</td>
            <td rowspan="${len==0?1:len }">${user.weixin}</td>
            <td rowspan="${len==0?1:len }">${user.roleNames}</td>

            <c:if test="${len>0 }">
                <td width="6%" class="tl">${user.deptJobRelation.get(0).deptName }</td>
                <td width="4%" class="tc">${user.deptJobRelation.get(0).jobName }</td>
            </c:if>
            <c:if test="${len==0 }">
                <td></td>
                <td></td>
            </c:if>

            <shiro:hasPermission name="sys:user:edit">
                <td class="tc" rowspan="${len==0?1:len }">
                    <a href="${ctx}/sys/user/form?id=${user.id}">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${ctx}/sys/userDept/deptList?userid=${user.id}&companyid=${user.company.id}">部门职务管理</a>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${ctx}/sys/user/delete?id=${user.id}"
                       onClick="return confirmx('确认要删除该用户吗？', this.href)">删除</a>
                </td>
            </shiro:hasPermission>
        </tr>
        <c:if test="${len>1 }">
            <c:forEach items="${user.deptJobRelation }" var="map" begin="1">
                <tr>
                    <td width="6%" class="tl">${map.deptName }</td>
                    <td width="4%" class="tc">${map.jobName }</td>
                </tr>
            </c:forEach>
        </c:if>

    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>

<!-- 导出用户密码 -->
<form id="exportForm" action="${ctx}/sys/user/downloadPwd" method="post">

</form>
</body>
</html>