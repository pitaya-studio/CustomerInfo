<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">

    <head>
        <meta  http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
        <meta  charset='utf-8' />
        <title>支出凭单</title>
        <link href="${ctxStatic}/css/dayinbdzy.css" type="text/css" rel="stylesheet"/>
        <script type="text/javascript" src="${ctxStatic}/js/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
		<script type="text/javascript">
			function downloadJKD(url, ctx, orderId, type){
				updateTime(ctx, orderId, type);	//更新打印时间
				window.opener.$("#searchForm").submit();
				window.location.href=ctx + url;
			}
			
			function printTure(ctx, orderId, type) {
				updateTime(ctx, orderId, type);	//更新打印时间
				window.opener.$("#searchForm").submit();
				printPage(document.getElementById("printDiv"));
			}
	
			function updateTime(ctx, orderId, type){
				$.ajax({
					type: "POST",
					url: ctx + "/finance/serviceCharge/updatePrintDate",
					dataType:"json",
					async:false,
					cache:false,
					data:{
						orderId: orderId,
						type: type
					},
					success: function(data){
						if(data.flag){
							window.location.reload();
							//printPage(document.getElementById("printDiv"));
						}
					}
				});
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
    <!--[if IE 7]>
    <style>
        td {
            border: 1px solid gray;
        }

        .ie7shiying td {
            border-top: none;
        }
    </style>
    <![endif]-->
        <div id="printDiv">
            <div class="dayinzys zhicuhpindan">
                <table width="700" border="0" cellspacing="0" cellpadding="0">
                    <tr class="noborder">
                        <td width="100%" align="right" height="40" class="text_little_title noborder">首次打印日期：
							<c:if test="${map.firstPrintTime != null && map.firstPrintTime != ''}">
								<fmt:formatDate value="${map.firstPrintTime }" type="both" dateStyle="default" pattern="yyyy/MM/dd HH:mm"/>
							</c:if>
						</td>   
                    </tr>
                </table>
                <table width="700" border="0" height="50">
                    <tr class="noborder" height="40">
                        <td align="center" valign="top" class="text_title noborder" height="30" style="line-height:30px;"
                            colspan="2">支出凭单
                        </td>
                    </tr>
                </table>
                <table width="700" border="0" cellspacing="0" cellpadding="0">
                    <tr class="noborder">
                        <td width="80%" height="40" class="text_little_title noborder">填写日期：<span><fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${map.createDate}"/></span></td>
                        <td  width="20%"height="40" align="right"class="text_little_title noborder">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</td>           
                    </tr>
                </table>
                <table width="700" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="81" align="center" class="text_normal bgcolorccc">团号</td>
                        <td colspan="2" align="left" class="text_normal">${map.groupCode}</td>     
                        <td width="81" align="center" class="text_normal bgcolorccc">款项</td>
                        <td align="left" class="text_normal">${map.funds}</td>
                        <td width="81" align="center" class="text_normal bgcolorccc">经办人</td>
                        <td colspan="2" align="left" class="text_normal">${map.operatorName}</td>
                    </tr>
                    <tr>
                        <td width="81" height="10" align="center" class="text_normal bgcolorccc">摘要</td>
                        <td colspan="7"align="left" class="text_normal">${map.remarks}</td>     
                    </tr>
                    <tr>
                        <td width="81" height="10" align="center" class="text_normal bgcolorccc">收款单位</td>
                        <td colspan="7"align="left" class="text_normal">${map.orderCompanyName}</td>
                        
                    </tr>
                    <tr>
                        <td height="10" align="center" class="text_normal bgcolorccc">人民币</td>
                        <td align="right" colspan="5" class="text_normal">${map.money }</td>
                        <td align="center" class="text_normal bgcolorccc">汇率</td>
                        <td align="center" class="text_normal">${map.currencyExchangerate }</td>
                        
                    </tr>
                    <tr>
                        <td height="10" align="center" class="text_normal bgcolorccc">合计人民币</td>
                        <td align="left" colspan="4" class="text_normal">${map.totalRMBMoneyName }</td>
                        <td align="center" class="text_normal bgcolorccc">￥</td>
                        <td align="right" colspan="2" class="text_normal">${map.totalRMBMoney }</td>
                        
                    </tr>
                    <tr>
                        <td height="10" align="center" class="text_normal bgcolorccc">领款人</td>
                        <td align="center" class="text_normal"></td>
                        <td align="center" class="text_normal bgcolorccc">主管审批</td>
                        <td colspan="5" align="center" class="text_normal"></td>
                    </tr>
                    <tr>
                        <td height="10" align="center" class="text_normal bgcolorccc">总经理</td>
                        <td align="center" class="text_normal"></td>
                        <td align="center" class="text_normal bgcolorccc">财务主管</td>
                        <td align="center" class="text_normal"></td>
                        <td align="center" class="text_normal bgcolorccc">出纳</td>
                        <td align="center" class="text_normal"></td>
                        <td align="center" class="text_normal bgcolorccc">审核</td>
                        <td align="center" class="text_normal"></td>
                    </tr>
                    <tr class="noborder" height="30">
						<c:if test="${empty map.confirmPayDate }">
							<td colspan="8" align="right"class="text_little_title noborder" height="30" style="line-height:30px;">确认付款日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
						</c:if>
						<c:if test="${!empty map.confirmPayDate }">
							<td colspan="8" align="right"class="text_little_title noborder" height="30" style="line-height:30px;">确认付款日期：<fmt:formatDate value="${map.confirmPayDate }" pattern="yyyy 年 MM 月 dd 日"></fmt:formatDate></td>
						</c:if>
                    </tr>
                </table> 
            </div>
        </div>
		
		<div class="dbaniu">
			<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure('${ctx}', '${map.orderId}', '${map.type}');">
			<input id="" name="" type="button" class="ydbz_s" value="下载" onclick="downloadJKD('/finance/serviceCharge/serviceChargePaymentDownload?orderId=${map.orderId}&type=${map.type}','${ctx}', '${map.orderId}', '${map.type}');">
		</div>
    </body>
</html>