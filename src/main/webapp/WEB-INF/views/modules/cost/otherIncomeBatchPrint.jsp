<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta  http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta  charset='utf-8' />
    <title>财务-收支管理-收款单</title>
    <link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon">


    <link href="${ctxStatic}/css/dayinbdzy.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <style type="text/css">
        .dbaniu { overflow: hidden; margin-top: 50px; margin-right: auto; margin-bottom: 50px; margin-left: auto; text-align: center; }
        .ydbz_s{height:28px; padding:0 15px;text-align:center; display: inline-block;margin:0 3px;cursor:pointer;}
        .ydbz_s{color:#fff;border:medium none;background:#5f7795;box-shadow: none;text-shadow: none;font-size:12px;border-radius:4px;height:28px;line-height:28px;}
        .ydbz_s:hover{ text-decoration:none; background:#28b2e6; color:#fff;}
    </style>
    <script type="text/javascript">
        // 打印
        function printTure() {
            debugger;
            var updateArr = [];
            var printInfo = $("input[name='printItem']");
            printInfo.each(function(){
                var itemVal = $(this).val();
                updateArr.push(itemVal);
            });
            $.ajax({
                type:"POST",
                url:"${ctx}/cost/manager/batchUpdatePrint",
                dataType:"text",
                data:{updateInfo:JSON.stringify(updateArr)},
                async : false,
                success:function(msg){
                    if (msg == 'success'){
                        debugger;
                        window.opener.$("#searchForm").submit();
                        printPage(document.getElementById("printDiv"));
                        window.location.reload();
                    }else {
                        top.$.jBox.tip(msg);
                    }
                }
            });
        }
        // 下载
        function download(payId, prdType, printTime) {

            if(printTime != '') {
                window.location.href = "${ctx}/cost/manager/recevieDownload/" + payId + "/" + prdType;
            }else {
                $.ajax({
                    type: "POST",
                    url: "${ctx}/cost/manager/updatePrint",
                    data: {
                        payId : payId,
                        prdType : prdType
                    },
                    success: function(msg){
                        if(msg == 'success'){
                            window.opener.$("#searchForm").submit();
                            window.location.reload();
                            // reload() 方法必须在下载链接的上面 update by shijun.liu 2016.01.13
                            window.location.href = "${ctx}/cost/manager/recevieDownload/" + payId + "/" + prdType;
                        } else {

                        }
                    }
                });
            }
        }
    </script>
</head>

<body>
<div id="printDiv"  style="text-align: center">
    <c:forEach items="${printList}" var="data">
        <table class="dayinzy"><thead></thead>
            <thead>
            <tr>
                <th class="fr f4 paddr" colspan="6">首次打印日期：${data.printTime}</th>
            </tr>
            <tr>
                <th class="f1" colspan="6">收款单</th>
            </tr>
            <tr>
                <th class="fl paddl f3" colspan="3">填写日期：${data.payDate}</th>
                <th class="fr paddr f3" colspan="3">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
            </tr>
            </thead>
            </table>
        <table class="dayinzy  dayInIe">
            <tbody>
            <tr>
                <td class="fc f2" width="110">团号</td>
                <td class="fc f3" style="white-space: normal;word-break: break-all">
                    &nbsp;${data.groupCode}
                </td>
                <td class="fc f2" width="110">出发/签证日期</td>
                <td class="fc f3" width="70">
                    &nbsp;${data.startDate}
                </td>
                <td class="fc f2" width="60">经办人</td>
                <td class="fc f3" width="70">
                    &nbsp;${data.applyPerson}
                </td>
            </tr>
            <tr>
                <td class="fc f2">线路/产品</td>
                <td class="fl f3 " colspan="5">
                    &nbsp;${data.productName}
                </td>
            </tr>
            <tr>
                <td class="fc f2">来款单位信息</td>
                <td class="fl f3" colspan="3">
                    <div class="duohangnormal">&nbsp;${data.payerName}</div>
                </td>
                <td class="fc f2">款项</td>
                <td class="fc f3">
                    &nbsp;${data.airRefund}
                </td>
            </tr>
            <tr>
                <td class="fc f2">备注</td>
                <td class="fl f3 " colspan="5">
                    <div class="duohangnormal">&nbsp; ${data.remarks}</div>
                </td>
            </tr>
            <tr>
                <td class="fc f2">收款金额</td>
                <td class="fl f3">
                    &nbsp;${data.payPrice}
                </td>
                <td class="fc f2">大写</td>
                <td class="fl f3" colspan="3">
                    &nbsp;${data.payPriceUpCase}
                </td>
            </tr>
            <tr>
                <td class="fc f2">收款账号</td>
                <td class="fl f3" style="overflow:hidden;white-space:normal !important; table-layout:fixed;max-height:120px;">
                    &nbsp;${data.toBankAccount}${data.toAlipayAccount }
                </td>
                <td class="fc f2">交款人</td>
                <td class="fc f3">
                    &nbsp; ${data.payPerson}
                </td>
                <td class="fc f2">收款人</td>
                <td class="fc f3">
                    &nbsp;  ${data.shouKuanRen}
                </td>
            </tr>
            </tbody>
            </table>
        <table  class="dayinzy  TopHeight">
            <tr>
                <td class="fl f3 noborder paddl" colspan="3">银行到账日期：
                    <c:if test="${empty data.bankGetDate }">&nbsp;　　&nbsp;年&nbsp;　&nbsp;月&nbsp;　&nbsp;日</c:if>
                    <c:if test="${not empty data.bankGetDate }">${data.bankGetDate}</c:if>
                </td>
                <td class="fr f3 noborder paddr" colspan="3">确认收款日期：
                    ${data.conDate}
                </td>
            </tr>
        </table>
        <input type="hidden" name="printItem" value="${data.payId}_${data.prdType}"/>
        <div style="page-break-after: always;"></div>
    </c:forEach>
</div>
<!--S打印按钮-->
<div class="dbaniu">
    <input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure()">
</div>
</body>

</html>