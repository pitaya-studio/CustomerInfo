<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
    <title>借款单</title>
    <!-- 页面左边和上边的装饰 -->

    <meta  http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta  charset='utf-8' />
    <link href="${ctxStatic}/css/dayinbdzy.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

    <script type="text/javascript">
        function closePrintSheet(url){
            if(window.opener){
                window.close();
            }else{
                window.location.href=window.opener;
            }
        }

        function downloadJKD(url){
            if("${printFormBean.printDate}" == '') {
                $.post("${ctx}/printForm/updateReviewPrintInfo", {"reviewId":"${printFormBean.reviewId}", "payId":"${payId}"},
                        function(data){
                            if(data != '') {
                                $("#printDate").text(data);
                            }
                        }
                );
            }
            window.location.href=url;
        }
    </script>
    <style type="text/css">
        .dbaniu { overflow: hidden; margin-top: 50px; margin-right: auto; margin-bottom: 50px; margin-left: auto; text-align: center; }
        .ydbz_s{height:28px; padding:0 15px;text-align:center; display: inline-block;margin:0 3px;cursor:pointer;}
        .ydbz_s{color:#fff;border:medium none;background:#5f7795;box-shadow: none;text-shadow: none;font-size:12px;border-radius:4px;height:28px;line-height:28px;}
        .ydbz_s:hover{ text-decoration:none; background:#28b2e6; color:#fff;}
    </style>
</head>
<body>
<div id="printDiv">
    <c:forEach items="${printList}" var="item">
        <c:choose>
            <c:when test="${item.orderType eq '6'}">
                <table class="dayinzy"><thead></thead>
                    <thead>
                    <tr>
                        <th class="fr f4 paddr" colspan="8">首次打印日期：${item.printDate} </th>
                    </tr>
                    <tr>
                        <th class="f1" colspan="8">签证费借款单</th>
                    </tr>
                    <tr>
                        <th class="fl paddl f3" colspan="4">填写日期： <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${item.revCreateDate}"/></th>
                        <th class="fr paddr f3" colspan="4">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
                    </tr>
                    </thead>
                    </table>
                <table  class="dayinzy  dayInIe">
                    <tbody>
                    <tr>
                        <td class="f2">借款单位</td>
                        <td class="f3" colspan="3">
                            <div class="dayinzy_w300">签证部</div>
                        </td>
                        <td class="f2">经办人</td>
                        <td class="f3" colspan="3">
                            <div class="dayinzy_w320">&nbsp;${item.productCreater}</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="f2">借款理由</td>
                        <td class="f3" colspan="7">
                            <div class="dayinzy_w698">&nbsp;${item.grouptotalborrownode}</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="f2">借款金额</td>
                        <td class="f3" colspan="4">
                            <div class="dayinzy_w373" style="text-align: left;">人民币${item.revBorrowAmountDx}</div>
                        </td>
                        <td class="f2">￥</td>
                        <td class="f3" colspan="2">
                            <div class="dayinzy_w193" style="text-align: right;">&nbsp;${item.revBorrowAmount}</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="f2">领款人</td>
                        <td class="f3" colspan="3">
                            <div class="dayinzy_w300">&nbsp;${item.operatorName}</div>
                        </td>
                        <td class="f2">总经理</td>
                        <td class="f3" colspan="3">
                            <div class="dayinzy_w320">&nbsp;${item.majorCheckPerson}</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="f2" width="70">财务主管</td>
                        <td class="f3" width="110">
                            <div class="dayinzy_w130">
                                <c:choose>
                                    <c:when test="${item.companyid==68}">
                                        &nbsp;
                                    </c:when>
                                    <c:otherwise>
                                        &nbsp;${item.cwmanager}
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                        <td class="f2" width="40">出纳</td>
                        <td class="f3" width="110">
                            <div class="dayinzy_w130">
                                <c:choose>
                                    <c:when test="${item.companyid==68}">
                                        &nbsp;
                                    </c:when>
                                    <c:otherwise>
                                        &nbsp;${item.cashier}
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                        <td class="f2" width="70">财务</td>
                        <td class="f3" width="110">
                            <div class="dayinzy_w130">&nbsp;${item.cw}</div>
                        </td>
                        <td class="f2" width="40">审批</td>
                        <td class="f3">
                            <div class="dayinzy_w152">&nbsp;${item.deptmanager}</div>
                        </td>
                    </tr>
                    </tbody>
                    </table>
                <table  class="dayinzy  TopHeight">
                    <tr>
                        <!-- 为再改回来备用：现在只留年月日汉字，具体日期手动填写  -->
                        <!--
					    <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${item.payDate}"/>
					     -->

                        <c:if test="${item.payStatus == 0}">
                            <td class="fr f3 noborder paddr" colspan="8">确认付款日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
                        </c:if>
                        <c:if test="${item.payStatus == 1}">
                            <td class="fr f3 noborder paddr" colspan="8">确认付款日期：<fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${item.revUpdateDate}"/></td>
                        </c:if>

                    </tr>
                </table>
                <input type="hidden" name="printItem" value="6_${item.revid}_0_${item.printDate}"/>
            </c:when>
            <c:otherwise>
                <table class="dayinzy"><thead></thead>
                    <thead>
                    <tr>
                        <th class="fr f4 paddr" colspan="8">首次打印日期：<span class="printDate"><fmt:formatDate pattern="yyyy/ MM /dd HH:mm" value="${item.printFormBean.printDate}"/></span></th>
                    </tr>
                    <tr>
                        <th class="f1" colspan="8">${item.printFormBean.printFormName }</th>
                    </tr>
                    <tr>
                        <th class="fl paddl f3" colspan="4">填写日期： <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${item.printFormBean.revCreateDate}"/></th>
                        <th class="fr paddr f3" colspan="4">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
                    </tr>
                    </thead>
                    </table>
                <table  class="dayinzy  dayInIe">
                    <tbody>
                    <tr>
                        <td class="f2">借款单位</td>
                        <td class="f3" colspan="3">
                            <div class="dayinzy_w300">&nbsp;${item.printFormBean.borrowDept}</div>
                        </td>
                        <td class="f2">经办人</td>
                        <td class="f3" colspan="3">
                            <div class="dayinzy_w320">&nbsp;${item.printFormBean.operatorName}</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="f2">借款理由</td>
                        <td class="f3" colspan="7">
                            <div class="dayinzy_w698">&nbsp;${item.printFormBean.revBorrowRemark}</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="f2">借款金额</td>
                        <td class="f3" colspan="4">
                            <div class="dayinzy_w373" style="text-align: left;">人民币${item.printFormBean.revBorrowAmountDx}</div>
                        </td>
                        <td class="f2">￥</td>
                        <td class="f3" colspan="2">
                            <div class="dayinzy_w193" style="text-align: right;">&nbsp;${item.printFormBean.revBorrowAmount}</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="f2">领款人</td>
                        <td class="f3" colspan="3">
                            <div class="dayinzy_w300">&nbsp;${item.printFormBean.operatorName}</div>
                        </td>
                        <td class="f2">总经理</td>
                        <td class="f3" colspan="3">
                            <div class="dayinzy_w320">&nbsp;${item.printFormBean.majorCheckPerson}</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="f2" width="70">财务主管</td>
                        <td class="f3" width="110">
                            <div class="dayinzy_w130">
                                <c:choose>
                                    <c:when test="${item.companyid==68}">
                                        &nbsp;
                                    </c:when>
                                    <c:otherwise>
                                        &nbsp; ${item.printFormBean.cwmanager}
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                        <td class="f2" width="40">出纳</td>
                        <td class="f3" width="110">
                            <div class="dayinzy_w130">
                                <c:choose>
                                    <c:when test="${item.companyid==68}">
                                        &nbsp;
                                    </c:when>
                                    <c:otherwise>
                                        &nbsp;${item.printFormBean.cashier}
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </td>
                        <td class="f2" width="70">财务</td>
                        <td class="f3" width="110">
                            <!-- 						   by sy 2015.8.6 -->
                            <!-- 							<div class="dayinzy_w130">${item.printFormBean.cw}</div> -->
                            <div style="text-align: center;">&nbsp;${item.printFormBean.cw}</div>
                        </td>
                        <td class="f2" width="40">审批</td>
                        <td class="f3">
                            <!-- 							<div class="dayinzy_w152">${item.printFormBean.deptmanager}</div> -->
                            <div style="text-align: center;">&nbsp;${item.printFormBean.deptmanager}</div>
                        </td>
                    </tr>
                    </tbody>
                    </table>
                <table  class="dayinzy  TopHeight">
                    <tr>
                        <c:if test="${item.printFormBean.payStatus eq '0' }">
                            <td class="fr f3 noborder paddr" colspan="8">确认付款日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
                        </c:if>
                        <c:if test="${item.printFormBean.payStatus eq '1' }">
                            <td class="fr f3 noborder paddr" colspan="8">确认付款日期：<fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${item.printFormBean.payDate}"/></td>
                        </c:if>
                    </tr>
                </table>
                <%--非签证的不需要后面的日期，这里是为了解析所以随便添加了字符2007--%>
                <input type="hidden" name="printItem" value="${item.orderType}_${item.reviewId}_${item.reviewFlag}_2007"/>
            </c:otherwise>
        </c:choose>
        <div style="page-break-after: always;"></div>

    </c:forEach>
</div>
<!--S打印&下载按钮-->
<div class="dbaniu">
    <shiro:hasPermission name="review:print:down">
        <input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure();">
    </shiro:hasPermission>
</div>
<%--<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/common.js"></script>--%>
<script type="text/javascript">

    function printTure() {
        var updateArr = [];
        var printInfo = $("input[name='printItem']");
        if (printInfo[0]){
            printInfo.each(function(){
                var itemVal = $(this).val();
                updateArr.push(itemVal);
            });
            $.ajax({
                type:"POST",
                url:"${ctx}/printForm/borrowMoneyBatchUpdatePrintInfo",
                dataType:"json",
                data:{updateInfo:JSON.stringify(updateArr)},
                async : false,
                success:function(data){
                    window.opener.$("#searchForm").submit();
                    if(data.success == 1){
                        $(".printDate").each(function(){
                            if ($(this).text() == ''){
                                $(this).text(data.printDate);
                            }
                        });
                        printPage(document.getElementById("printDiv"));
                    }else{
                        $.jBox.tip("打印失败！");
                    }
                }
            });
        } else {
//            window.opener.$("#searchForm").submit();
            printPage(document.getElementById("printDiv"));
        }
    }
</script>
</body>


</html>