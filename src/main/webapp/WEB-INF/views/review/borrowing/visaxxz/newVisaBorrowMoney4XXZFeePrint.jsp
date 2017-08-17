<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
<title>签证费借款单</title>
<!-- 页面左边和上边的装饰 -->

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${ctxStatic}/css/dayinbdzy.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

<script type="text/javascript">

	$(function(){
		g_context_url = "${ctx}";
 	});
	
	function closePrintSheet(url){
		if(window.opener){
			window.close();
		}else{
			window.location.href=window.opener;
		}
	}
	
	function printTure(){
		var revid = "${revid}";
		var printDate="${printDate}";
	    dataparam={ 
	    		'revid':revid,
	    		'printDate':printDate
        };	
        $.ajax({ 
	          type:"POST",
	          url:g_context_url+"/visa/xxz/borrowmoney/visaBorrowMoneyFeePrintAjaxNew",
	          dataType:"json",
	          data:dataparam,
	          async : false,
	          success:function(data){
	        	  if(data.result==1){
	        		  //window.opener.location.reload();
	        		  window.opener.$("#searchForm").submit();
	        		  //location.reload();
	        		  printPage(document.getElementById("printDiv"));  
	        	  }else{
	        		  $.jBox.tip("打印失败！"); 
	        	  }
	          }
        });

	}
	
	function downloadJKD(url){
		var revid = "${revid}";
		var printDate="${printDate}";
	    dataparam={ 
	    		'revid':revid,
	    		'printDate':printDate
        };		
        $.ajax({ 
	          type:"POST",
	          url:g_context_url+"/visa/xxz/borrowmoney/visaBorrowMoneyFeePrintAjaxNew",
	          dataType:"json",
	          data:dataparam,
	          success:function(data){
	        	  if(data.result==1){
	        		  //window.opener.location.reload();
	        		  window.opener.$("#searchForm").submit();
	        		  //location.reload();
	        		  window.location.href=url; 
	        	  }else{
	        		  $.jBox.tip("打印失败！"); 
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
		<!--
		<a id="printdownload" href="${ctx}/visa/workflow/borrowmoney/downloadVisaBorrowMoneySheet?revid=${revid}"  style="margin-left: 20px;">下载</a>
		-->
		<div id="printDiv">
			<table class="dayinzy">
				<thead>
					<tr>
						<th class="fr f4 paddr" colspan="8">首次打印日期：${printDate} </th>
					</tr>
					<tr>
						<th class="f1" colspan="8">签证费借款单</th>
					</tr>
					<tr>
						<th class="fl paddl f3" colspan="4">填写日期： <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${revCreateDate}"/></th>
						<th class="fr paddr f3" colspan="4">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="f2">借款单位</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w300">签证部</div>
						</td>
						<td class="f2">经办人</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w320">${productCreater}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">借款理由</td>
						<td class="f3" colspan="7">
							<div class="dayinzy_w698">${grouptotalborrownode}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">借款金额</td>
						<td class="f3" colspan="4">
							<div class="dayinzy_w373" style="text-align: left;">人民币${revBorrowAmountDx}</div>
						</td>
						<td class="f2">￥</td>
						<td class="f3" colspan="2">
							<div class="dayinzy_w193" style="text-align: right;">${revBorrowAmount}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">领款人</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w300">${operatorName}</div>
						</td>
						<td class="f2">总经理</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w320">${majorCheckPerson}</div>
						</td>
					</tr>
					<tr>
						<td class="f2" width="70">财务主管</td>
						<td class="f3" width="110">
							<div class="dayinzy_w130">
							   <c:choose>
							   		<c:when test="${companyid==68}">
							   		 
							   		</c:when>
							   		<c:otherwise>
							   		    ${cwmanager}
							   		</c:otherwise>
							   </c:choose>
							</div>
						</td>
						<td class="f2" width="40">出纳</td>
						<td class="f3" width="110">
							<div class="dayinzy_w130">
							  <c:choose>
							   		<c:when test="${companyid==68}">
							   		 
							   		</c:when>
							   		<c:otherwise>
							   		    	${cashier}
							   		</c:otherwise>
							   </c:choose>
							</div>
						</td>
						<td class="f2" width="70">财务</td>
						<td class="f3" width="110">
							<div class="dayinzy_w130">${cw}</div>
						</td>
						<td class="f2" width="40">审批</td>
						<td class="f3">
							<div class="dayinzy_w130">${deptmanager}</div>
						</td>
					</tr>
					<tr>
						<!-- 为再改回来备用：现在只留年月日汉字，具体日期手动填写  -->
						<!-- 
					    <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${payDate}"/>
					     -->
					     
					     <c:if test="${payStatus == 0}">
					         <td class="fr f3 noborder paddr" colspan="8">确认付款日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
					     </c:if>
					     <c:if test="${payStatus == 1}">
					         <td class="fr f3 noborder paddr" colspan="8">确认付款日期：<fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${revUpdateDate}"/></td>
					     </c:if>
						
					</tr>
				</tbody>
			</table>
		</div>
		<!--S打印&下载按钮-->
		<div class="dbaniu">
			<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure()" />
			<input id="" name="" type="button" class="ydbz_s" value="下载" onclick="downloadJKD('${ctx}/visa/xxz/borrowmoney/downloadVisaBorrowMoney4XXZSheetNew?revid=${revid}');">
		</div>
	</body>
</html>