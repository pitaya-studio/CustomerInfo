<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>实时连通渠道账号列表</title>
    <meta name="decorator" content="wholesaler"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <style>

    </style>
    <script type="text/javascript">
        $(function(){
//          added for UG_V2 展开收起筛选 by tlw at 20170302 start
            launch();
//          added for UG_V2 展开收起筛选 by tlw at 20170302 end
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
            $("#searchForm").attr("action","${ctx}/quauqAgent/manage/list");
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
         * 根据筛选结果，导出渠道商信息
         */
        function exportAgentExcel(){
            $("#searchForm").attr("action","${ctx}/quauqAgent/manage/exportAgent");
            $("#searchForm").submit();
            $("#searchForm").attr("action","${ctx}/quauqAgent/manage/list");
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

        /**
         * 清空所有条件 bug17489
         * added by tlw at 20170309
         */


    </script>

</head>
<body>
<%--<page:applyDecorator name="agent_op_head">--%>
<%--<page:param name="current">agentList</page:param>--%>
<%--</page:applyDecorator>--%>
<%--added for tab标签 by tlw at 20170302 start--%>
<content tag="three_level_menu">
    <li class="active"><a href="javascript:void(0)">渠道账号列表</a></li>
</content>
<%--added for tab标签 by tlw at 20170302 end--%>

<!--右侧内容部分开始-->
<form:form id="searchForm" modelAttribute="agentinfo" action="${ctx}/quauqAgent/manage/list" method="post" class="form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
    <p class="main-right-topbutt pullDown">
        <a class="primary" href="${ctx}/quauqAgent/manage/firstForm">添加渠道</a>
        <span id="exports">导出
                    <span>
                        <a id="exportPwd" onClick="downloadPwd();"/>导出密码</a>
                        <shiro:hasPermission name="export:quauq:price">
                            <a href="${ctx}/pricingStrategy/manager/exportData"/>导出价格策略设置</a>
                        </shiro:hasPermission>
                    </span>

                </span>
    </p>
    <div class="activitylist_bodyer_right_team_co">
        <div class="activitylist_bodyer_right_team_co2 pr">
            <form:input path="agentName" class="searchInput inputTxtlong" placeholder="输入渠道名称"/>
        </div>
        <div class="zksx zksx-on">筛选</div>
        <div class="form_submit">
            <input value="搜索" id="seachbutton" class="btn btn-primary ydbz_x" type="submit">
            <input class="btn ydbz_x" type="button" onclick="resetSearchParams()"value="清空所有条件"><%--bug17489 type=reset换成type=button，添加点击事件resetSearchParams--%>
            <c:if test="${useruuid == '2060'}">
                <input class="btn ydbz_x" id="exportAgent" onClick="exportAgentExcel()" type="button" value="导出Excel">
            </c:if>
        </div>

        <div style="display: block;" class="ydxbd">
            <span></span>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">登录名：</label>
                <form:input path="loginName"/>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">联系人姓名：</label>
                <form:input path="contactName"/>
            </div>

            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">类型：</label>
                <form:select path="agentType" id="agentType">
                    <form:option value="" label="全部"></form:option>
                    <c:forEach items="${customerTypeList }" var="customerType">
                        <form:option value="${customerType.value }" label="${customerType.name }"></form:option>
                    </c:forEach>
                </form:select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">关系：</label>
                <!-- <form:input path="agentParent" /> -->
                <input type="text" name="agentParent" value="<c:choose><c:when test="${agentinfo.agentParent eq '-1' }"></c:when><c:otherwise>${agentinfo.agentParent }</c:otherwise></c:choose>"/>
            </div>
            <div class="activitylist_bodyer_right_team_co1"">
                <label class="activitylist_team_co3_text">账号来源：</label>
                <div class="selectStyle">
                    <form:select path="accountFrom" id="accountFrom">
                        <form:option value="" label="全部"></form:option>
                        <form:option value="0" label="内部"></form:option>
                        <form:option value="1" label="微信"></form:option>
                    </form:select>
                </div>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">关联状态：</label>
                <div class="selectStyle">
                <form:select path="isBound" id="isBound">
                    <form:option value="" label="全部"></form:option>
                    <form:option value="已关联" label="已关联"></form:option>
                    <form:option value="未关联" label="未关联"></form:option>
                </form:select>
                </div>
            </div>
            <div class="kong"></div>
        </div>
    </div>
</form:form>

<!-- 产品线路分区 -->
<%--<div style="height:60px">--%>
<%--<p class="main-right-topbutt">--%>
<%--<a href="${ctx}/quauqAgent/manage/firstForm" class="two">添加渠道</a>--%>
<%--<a id="exportPwd" onClick="downloadPwd();"/>导出密码</a>--%>
<%--</p>--%>
<%--<shiro:hasPermission name="export:quauq:price">--%>
<%--<a href="${ctx}/pricingStrategy/manager/exportData"/>导出价格策略设置</a>--%>
<%--</shiro:hasPermission>--%>
<%--</div>--%>

<table class="activitylist_bodyer_table mainTable" id="contentTable_quauq">
    <thead>
    <tr>
        <th width="4%">状态</th>
        <th width="4%">序号</th>
        <th width="8%">渠道名称</th>
        <th width="8%">登录名</th>
        <th width="8%">类型</th>
        <th width="6%">关系</th>
        <th width="6%">联系人姓名</th>
        <th width="8%">联系人电话</th>
        <th width="4%">账号来源</th>
        <th width="4%">关联状态</th>
        <th width="4%">操作</th>
    </tr>
    </thead>
    <tbody class="orderOrGroup_group_tbody">
    <c:forEach items="${page.list}" var="agent" varStatus="s">
        <c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
        <tr>
            <td class="tc"><input type="checkbox" name="statusFlag" onclick="changeEnableStatus(this)" <c:if test='${agent.enableQuauqAgent eq "1"}'>checked='checked'</c:if> >&nbsp;启用<input type="hidden" name="status" value="${agent.enableQuauqAgent }"></td>
            <td class="tc">${s.count}</td>
            <td class="tc"><a target="_blank" href="${ctx}/person/info/getAgentInfo?agentId=${agent.agentId}">${agent.agentName}</a><input type="hidden" name="agentId" value="${agent.agentId}"></td>
            <td class="tc">${agent.loginName}</td>
            <td class="tc">${agent.agentType}</td>
            <td class="tc">
                <c:choose>
                    <c:when test="${agent.agentParent eq '-1' }">
                        无
                    </c:when>
                    <c:otherwise>
                        ${agent.agentParent }
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="tc">${agent.contactName}</td>
            <td class="tc">${agent.contactMobile}</td>
            <td class="tc">${agent.accountFrom}</td>
            <td class="tc">${agent.isBound}</td>
            <td class="p0">
                <dl class="handle">
                    <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
                    <dd class="">
                        <p>
                            <span></span>
                                <%-- <a href="javascript:void(0)" onClick="detail(${agent.agentId})">查看详情</a> --%>
                            <a href="javascript:void(0)" onClick="mod(${agent.agentId})">修改</a>
                                <%-- <a href="javascript:void(0)" onClick="return del(${agent.agentId})">删除</a></td> --%>
                        </p>
                    </dd>
                </dl>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<!--quauq渠道列表-->
<!--右侧内容部分结束-->

<div class="pagination">${page}</div>
<form id="exportForm" action="${ctx}/quauqAgent/manage/downloadPwd" method="post">
</form>
</body>
</html>