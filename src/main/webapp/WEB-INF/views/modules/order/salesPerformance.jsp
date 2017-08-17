<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>财务-销售人员业绩</title>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <style type="text/css">
        #contentTable th{
            height:15px;
        }
        #contentTable td{
            height:20px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            //展开收起搜索条件
            launch();
            closeOrExpand();
        });
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
        function resetParams(){
            $("#wholeSalerKey").val("");
            $("#departmentId").val("");
            $("#salerId").val("")
            $("#productName").val("")
            $("#beginDate").val("");
        }
        function exportToExcel(){
            $("#searchForm").attr("action","${ctx}/finance/manage/downloadSalesPerformance");
            $("#searchForm").submit();
            $("#searchForm").attr("action","${ctx}/finance/manage/showSalesPerformance");
        }
        function closeOrExpand(){
            var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='groupCode']");
            var searchFormselect = $("#searchForm").find("select");
            var inputRequest = false;
            var selectRequest = false;
            for(var i=0;i<searchFormInput.length;i++) {
                var inputValue = $(searchFormInput[i]).val();
                if(inputValue != "" && inputValue != null) {
                    inputRequest = true;
                    break;
                }
            }
            for(var i=0;i<searchFormselect.length;i++) {
                var selectValue = $(searchFormselect[i]).children("option:selected").val();
                if(selectValue != "" && selectValue != null && selectValue != 0) {
                    selectRequest = true;
                    break;
                }
            }
            if(inputRequest||selectRequest) {
                $('.zksx').click();
            }
        }
    </script>
</head>
<body>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
    <form method="post" action="${ctx}/finance/manage/showSalesPerformance" id="searchForm">
        <input type="hidden" value="${page.pageNo}" name="pageNo" id="pageNo">
        <input type="hidden" value="${page.pageSize}" name="pageSize" id="pageSize">
        <div class="activitylist_bodyer_right_team_co">
            <div class="activitylist_bodyer_right_team_co2">
                <input class="txtPro inputTxt searchInput" id="wholeSalerKey" name="groupCode" value="${groupCode }"placeholder="请输入团号">
            </div>
            <div class="zksx">筛选</div>
            <div class="form_submit">
                <input type="submit" value="搜索" id="seachbutton" class="btn btn-primary ydbz_x">
                <input class="btn ydbz_x" type="button" onclick="resetParams()"  value="清空所有条件">
                <input class="btn ydbz_x " type="button" onclick="exportToExcel()" value="导出Excel">
            </div>

            <div style="display:none;" class="ydxbd">
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">部门：</div>
                    <select name="departmentId" id="departmentId">
                        <option value="">全部</option>
                        <c:forEach items="${departments}" var="d">
                            <option value="${d.id }" <c:if test="${d.id==departmentId }">selected</c:if> >${d.text}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">销售：</div>
                    <select name="salerId" id="salerId">
                        <option value="">全部</option>
                        <c:forEach items="${salers}" var="saler">
                            <option value="${saler.key }" <c:if test="${saler.key==salerId }">selected</c:if>>${saler.value }</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">产品名称：</label>
                    <input id="productName" name="productName" class="inputTxt inputTxtlong" value="${productName }">
                </div>
                <div class="activitylist_bodyer_right_team_co2">
                    <label class="activitylist_team_co3_text">汇总月份：</label>
                    <input readonly="" id="beginDate" name="beginDate" class="inputTxt dateinput" value="${beginDate}"
                       onclick="WdatePicker({dateFmt:'yyyy年MM月'})" >
                </div>
                <div class="kong"></div>
            </div>
        </div>
    </form>
</div>
<div class="activitylist_paixu_right"> 查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
<table class="table activitylist_bodyer_table mainTable" id="contentTable">
    <thead>
    <tr>
        <th width="2%" rowspan="2">序号</th>
        <th width="8%" rowspan="2">部门</th>
        <th width="6%" rowspan="2">销售</th>
        <th width="8%" rowspan="2">产品名称</th>
        <th width="8%" rowspan="2">团号</th>
        <th width="10%" colspan="2">客户来源</th>
        <th width="6%" rowspan="2">收款金额</th>
        <th width="4%" rowspan="2">人数</th>
        <th width="6%" rowspan="2">平均每人成本</th>
        <th width="6%" rowspan="2">毛利</th>
    </tr>
    <tr>
        <th>非签约渠道</th>
        <th>签约渠道</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="item" varStatus="status">
        <tr>
            <td>${status.count}</td>
            <td class="tc" title="${item.deptNames}">${item.deptName}</td>
            <td class="tc">${item.userName}</td>
            <td class="tc">${item.productName}</td>
            <td class="tc">${item.groupCode}</td>
            <td class="tc" title="${item.unAgentNames}">${item.unAgentName}</td>
            <td class="tc" title="${item.agentNames}">${item.agentName}</td>
            <td class="tr">${item.accountedMoney}</td>
            <td class="tc">${item.personNum}</td>
            <td class="tr">${item.costPerPerson}</td>
            <td class="tr">${item.profit}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination clearFix">${page}</div>
</body>
</html>