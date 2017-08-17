<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>提醒列表</title>
    <meta name="decorator" content="wholesaler"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(function() {
            //搜索条件筛选
            launch();
            //操作浮框
            operateHandler();
        });
        function delRemind(obj) {
            $.jBox.confirm("确定删除该条提醒？", "提示", function (v) {
                if (v == 'ok') {
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/sys/remind/del",
                        data: {
                            id: $(obj).parents("tr").find(":hidden[name=remindId]").val()
                        },
                        success: function (data) {
                            if(data.result == 1) {
                                $.jBox.tip("删除成功!");
                                $(obj).parents("tr").remove();
                            } else {
                                $.jBox.tip("删除失败!");
                            }
                        }
                    });
                }
            });
        }
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
        }

        
//         function createMsgRightNow(){
//         	$.ajax({
//                 type: "POST",
//                 url: "${ctx}/activity/manager/createMsgRightNow",
//                 data: {
                    
//                 },
//                 success: function (data) {
//                     if(data) {
//                         $.jBox.tip("删除成功!");
//                     } else {
//                         $.jBox.tip("删除失败!");
//                     }
//                 }
//             });
//         }

    </script>
</head>
<body>
    <tags:message content="${message}"/>
        <div id="sea">
            <!-- 搜索条件 -->
            <form id="searchForm" action="${ctx}/sys/remind/list" method="post">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
                <div class="activitylist_bodyer_right_team_co2 wpr20">
                    <label class="activitylist_team_co2_text">名称：</label>
                    <input class="txtPro inputTxt inquiry_left_text radius4" id="wholeSalerKey" name="remindName" value="${parameters.remindName}" type="text">
                </div>
                <div class="activitylist_bodyer_right_team_co2">
                    <label class="activitylist_team_co2_text">提醒类型：</label>
                    <div class="selectStyle">
                        <select  name="remindType">
                            <option value="">还款提醒</option>
                            <%--<option value="">不限</option>--%>
                            <%--<option value="1" <c:if test="${parameters.remindType==1}">selected="selected"</c:if>>还款提醒</option>--%>
                            <%--<option value="2" <c:if test="${parameters.remindType==2}">selected="selected"</c:if>>收款提醒</option>--%>
                        </select>
                    </div>
                </div>
                <div class="form_submit">
                    <input type="submit" value="搜索" id="seachbutton" class="btn btn-primary ydbz_x">
                </div>
                <p class="main-right-topbutt"><a class="primary" href="${ctx}/sys/remind/form">新增提醒</a></p>
            </form>

            <div class="activitylist_bodyer_right_team_co_paixu">
                <div class="activitylist_paixu">
                    <div class="activitylist_paixu_left"></div>
                    <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
                </div>
            </div>

            <table id="contentTable" class="activitylist_bodyer_table mainTable">
                    <thead>
                        <tr>
                            <th width="5%">序号</th>
                            <th width="5%">提醒类型</th>
                            <th width="7%">名称</th>
                            <th width="7%">提醒起始时间</th>
                            <th width="7%">过期时间</th>
                            <th width="6%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:if test="${fn:length(page.list) <= 0 }">
                        <tr class="toptr" >
                            <td colspan="10" style="text-align: center;">
                                暂无提醒信息
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${fn:length(page.list) > 0 }">
                        <c:forEach items="${page.list}" var="remind" varStatus="var">
                            <tr>
                                <td class="tc">${var.index + 1} <input type="hidden" name="remindId" value="${remind.id}"></td>
                                <td class="tc">
                                    <c:if test="${remind.remindType == 1}">还款</c:if>
                                    <c:if test="${remind.remindType == 2}">收款</c:if>
                                </td>
                                <td class="tc">
                                    ${remind.remindName}
                                </td>
                                <td class="tc">
                                    还款日期
                                    <c:if test="${remind.startRemindStatus == -1}">前</c:if><c:if test="${remind.startRemindStatus == 1}">后</c:if>
                                        ${remind.startRemindDays}天
                                </td>
                                <td class="tc">
                                    还款日期
                                    <c:if test="${remind.endRemindStatus == -1}">前</c:if><c:if test="${remind.endRemindStatus == 1}">后</c:if>
                                        ${remind.endRemindDays}天
                                </td>
                                <td class="tc">
                                    <dl class="handle">
                                        <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
                                        <dd class="">
                                            <p>
                                                <span></span>
                                                <a href="${ctx}/sys/remind/info?id=${remind.id}">查看 </a>
                                                <a href="${ctx}/sys/remind/form?id=${remind.id}">修改 </a>
                                                <a href="javascript:void(0)" onclick="delRemind(this)">删除 </a>
                                            </p>
                                        </dd>
                                    </dl>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
        </div>
<div class="pagination clearFix">
   ${page}
</div>
</body>
</html>
