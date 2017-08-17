<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<link href="${ctxStatic}/css/dayinbdzy.css" rel="stylesheet" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<style type="text/css">
	.dbaniu { overflow: hidden; margin-top: 50px; margin-right: auto; margin-bottom: 50px; margin-left: auto; text-align: center; }
	.ydbz_s{height:28px; padding:0 15px;text-align:center; display: inline-block;margin:0 3px;cursor:pointer;}
    .ydbz_s{color:#fff;border:medium none;background:#5f7795;box-shadow: none;text-shadow: none;font-size:12px;border-radius:4px;height:28px;line-height:28px;}
    .ydbz_s:hover{ text-decoration:none; background:#28b2e6; color:#fff;}
</style>
<title>客人报名收款单(签证收款)</title>
	</head>

	<body>
		<!--
		<a href="${ctx}/visaOrderPayLog/manage/dst?orderPaySerialNum=${orderPaySerialNum}&vpid=${vpid}&vorderid=${vorderid}" onclick="printTure2('${ctx}','${orderPaySerialNum}');">下载</a>
		 -->
		<div id="printDiv">
			<table class="dayinzy">
				<thead>
					<tr>
					 <span>
						<th class="fr f4 paddr" colspan="12">首次打印日期：${printTime}</th>
					</span>
					</tr>
					<tr>
						<th class="f1" colspan="12">客人报名收款单(签证收款)</th>
					</tr>
					<tr>
						<th class="fl paddl f3" colspan="6">填写日期：<fmt:formatDate value="${startDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日 " /></th>
						<th class="fr paddr f3" colspan="6">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="fc f2" width="110" colspan="2">团号</td>
						<td class="fc f3" colspan="2" style="white-space: normal;word-break: break-word">
							${group_code}
						</td>
						<td class="fc f2" width="110" colspan="2">出发/签证日期</td>
						<td class="fc f3" width="70" colspan="2">
							<fmt:formatDate value="${startDate}" type="both" dateStyle="long" pattern="MM 月  dd 日 " />
						</td>
						<td class="fc f2" width="60" colspan="2">经办人</td>
						<td class="fc f3" width="70" colspan="2">
							${operator}
						</td>
					</tr>
					<tr>
						<td class="fc f2" colspan="2">线路/产品</td>
						<td class="fl f3 paddl" colspan="10">
							${product}
						</td>
					</tr>
					<tr>
						<td class="fc f2" colspan="2">来款单位信息</td>
						<td class="fc f2" colspan="6">
						    <!-- 
							<div class="danhangnormal">${payerName} ${fromPayerName}<br /> ${fromBankAccount}   ${fromPayerName} ${fromBankAccount}</div> 
							 -->
							 <div>${payerName}</div>
						</td>
						<td class="fc f2" colspan="2">款项</td>
						<td class="fc f3" colspan="2">
							${countryName_cn} ${money}
						</td>
					</tr>
					<tr>
						<td class="fc f2" colspan="2">客人名单</td>
						<td class="fl f3 paddl" colspan="10">
							<div class="danhangnormal">${travllerNames}</div>
						</td>
					</tr>
					<tr>
						<td class="fc f2" colspan="2">备注</td>
						<td class="fl f3 paddl" colspan="10">
							${remarks}
						</td>
					</tr>
					
					<!-- 对应需求号   0002  -->
					<c:if test="${companyUUID eq '7a816f5077a811e5bc1e000c29cf2586'}">
					
					    <tr>
							<td class="fc f2" colspan="2">开票状态</td>
							<td class="fl f3 paddl" colspan="4">
								${invoiceStatus}
							</td>
							<td class="fc f2" colspan="2">开票金额</td>
							<td class="fl f3 paddl" colspan="4">
							     ${invoiceCount}
							</td>
						</tr>
					
					</c:if>

					
					
					<tr>
						<td class="fc f2" colspan="2">收款金额</td>
						<td class="fl f3 paddl" colspan="4">
							￥${toatalMoney}
						</td>
						<td class="fc f2" colspan="2">大写</td>
						<td class="fc f3" colspan="4">
							<c:if test="${not empty  toatalMoneyBig}">人民币 ${toatalMoneyBig}</c:if>
			 				<c:if test="${empty toatalMoneyBig}"></c:if>
						</td>
					</tr>
					<tr>
						<td class="fc f2" colspan="2">收款账号</td>
						<td class="fc f3" colspan="4">
							<div class="danhangnormal">
								<c:if test="${toAlipayAccount == null}">
									${toBankNname}
									<br />
									${toBankAccount}
								</c:if>
								<c:if test="${toAlipayAccount != null}">
									${toAlipayName }
									<br>
									${toAlipayAccount }
								</c:if>
							</div>
						</td>
						
						<td class="fc f2" >
							交款人
						</td>
						<td class="fc f3" colspan="2" >
							${operator}
						</td>
						<td class="fc f2" >
							收款人
						</td>
						<td class="fc f3"  colspan="2" >
							${payeee}
						</td>
						
					</tr>
					<tr>
						<td class="fl f3 noborder paddl" colspan="6">
						    <!-- 对应需求号0041 -->
						    <c:if test="${isAsAccount == 1}">
						                     行到账日期：<fmt:formatDate value="${accountDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日" />
						    </c:if>
						    
						     <c:if test="${isAsAccount != 1}">
						                    银行到账日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日
						    </c:if>
						        
						</td>
						<td class="fr f3 noborder paddr" colspan="6">确认收款日期：
						<!-- 需求C221， 如果客人报名收款单 已确认收款要显示确认收款日期 -->
						<c:if test="${isAsAccount!=1}">
						    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日
						</c:if>
						
						
						<c:if test="${isAsAccount==1}">
						<c:if test="${empty payConfirmDate}">
						 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日
						 </c:if>
						 <c:if test="${not empty payConfirmDate}">
						 	 <fmt:formatDate value="${payConfirmDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日" />
						 </c:if>
						   
						</c:if>
						
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!--S打印&下载按钮-->
		<div class="dbaniu">
			<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure('${ctx}','${orderPaySerialNum}');">
			<input id="" name="" type="button" class="ydbz_s" value="下载" onclick="printTure2('${ctx}','${orderPaySerialNum}','${ctx}/visaOrderPayLog/manage/dst?orderPaySerialNum=${orderPaySerialNum}&vpid=${vpid}&vorderid=${vorderid}');">
		</div>
	</body>
<script type="text/javascript">



function printTure(ctx,orderPaySerialNum) {
	    dataparam={ 
	    		'orderPaySerialNum':orderPaySerialNum
        };	
        $.ajax({ 
	          type:"POST",
	          url:ctx+"/visaOrderPayLog/manage/updatePrintTime",
	          dataType:"json",
	          data:dataparam,
	          success:function(data){
	        	  if(data.result==1){
	        		  window.opener.$("#searchForm").submit();
	        		  printPage(document.getElementById("printDiv"));  
	        	  }else if(data.result==0){
	        		  printPage(document.getElementById("printDiv"));
	        	  }else{
	        		  $.jBox.tip("打印失败！"); 
	        	  }
	          }
        });
}

function printTure2(ctx,orderPaySerialNum,url,groupCode,productName,vodate) {
    dataparam={ 
    		'orderPaySerialNum':orderPaySerialNum,
    		'groupCode':groupCode,
    		'productName':productName,
    		'vodate':vodate
    };	
    $.ajax({ 
          type:"POST",
          url:ctx+"/visaOrderPayLog/manage/updatePrintTime",
          dataType:"json",
          data:dataparam,
          async : false,
          success:function(data){
        	  if(data.result==1){
        		  window.opener.$("#searchForm").submit();
        		  window.location.href=url; 
        	  }else if(data.result==0){
        		  window.location.href=url;
        		 // $.jBox.tip("打印失败！"); 
        	  }else{
        		  $.jBox.tip("打印失败！"); 
        	  }
          }
    });
}

</script>
</html>