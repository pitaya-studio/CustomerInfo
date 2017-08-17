<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证返佣支出凭单</title>
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
	
	function downloadJKD(url){
		var revid = "${revid}";
		var printDate="${printDate}";
	    dataparam={ 
	    		'revid':revid,
	    		'printDate':printDate,
        };		
        $.ajax({ 
	          type:"POST",
	          url:g_context_url+"/newRebatesReview/visaRebatesReviewPrintAjax_new",
	          dataType:"json",
	          data:dataparam,
	          success:function(data){
	        	  if(data.result==1){
	        		  window.opener.$("#searchForm").submit();
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
		<div id="printDiv">
		   	<table class="dayinzy">
				<thead>
					<tr>
						<th class="fr f4 paddr" colspan="8">首次打印日期：${printDate}</th>
					</tr>
					<tr>
						<th class="f1" colspan="8">支出凭单</th>
					</tr>
					<tr>
						<th class="fl paddl f3" colspan="4">填写日期： <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${revCreateDate}"/></th>
						<th class="fr paddr f3" colspan="4">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="f2">${groupCodeName}</td>
						<td class="fc f3" colspan="2" style="white-space: normal;word-break: break-word">
							${ordergroupcode}
						</td>
						<td class="fc f2">款项</td>
						<td class="f3" >${costname}</td>
						<td class="fc f2">经办人</td>
						<td class="f3" colspan="2">
							${productCreater}
						</td>
					</tr>
					<tr>
						<td class="f2">摘要</td>
						<td class="f3" colspan="7">
							<div class="dayinzy_w698">${remark}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">收款单位</td>
						<td class="fl f3" colspan="7" >&nbsp;
						<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
							<c:choose>
							<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && orderCompanyName=='非签约渠道'}"> 
			       				直客
			   				</c:when>
							<c:otherwise> 
			       				${orderCompanyName}
			   				</c:otherwise>
			   			</c:choose>	
						</td>
					</tr>
					<tr>
						<td class="f2">${currencyName}</td>
						<td class="f3" colspan="5" style="text-align:right;">${rebatesAmount}</td>
						<td class="f2" >汇率</td>
						<td class="f3">${currencyExchangerate}</td>
					</tr>
					<tr>
						<td class="fc f2">合计人民币</td>
						<td class="fl f3" colspan="4">
							${rebatesAmountDx}
						</td>
						<td class="fc f3">￥</td>
						<td class="fl f3" colspan="2" style="text-align:right;">${rebatesAmount}</td>
					</tr>
					<tr>
						<td class="fc f2">领款人</td>
						<td class="fc f3">
							${operatorName}
						</td>
						<td class="fc f2">主管审批</td>
						<td class="fc f3" colspan="5">
							${jdmanager}
						</td>
					</tr>
					<tr>
						<td class="fc f2">总经理</td>
						<td class="fc f3">${majorCheckPerson}</td>
						<td class="fc f2">财务主管</td>
						<td class="fc f2">&nbsp;${cw }</td>
						<td class="fc f2">出纳</td>
						<td class="fc f3">&nbsp;${cashier }</td>
						<td class="fc f2">审核</td>
						<%-- <td class="fc f3">${deptmanager}</td> --%> <!-- 因bug11216而做出的更改. -->
						<td class="fc f3">${reviewer}</td>
					</tr>
					
					<tr>
						<!-- 为再改回来备用：现在只留年月日汉字，具体日期手动填写  -->
						<!-- 
					    <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${payDate}"/>
					     -->
					    <!-- wangxinwei added 20151008,需求C221：付款确认后显示确认付款日期 -->
					    <c:if test="${payStatus == 0}">
					       <td class="fr f3 noborder paddr" colspan="8">确认付款日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
					    </c:if>
					    <c:if test="${payStatus == 1}">
					        <td class="fr f3 noborder paddr" colspan="8">确认付款日期：<fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${revCreateDate}"/></td>
					    </c:if>						
					</tr>
				</tbody>
			</table>
		</div>
		<!--S打印&下载按钮-->
		<div class="dbaniu">
			<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure();">
			<input id="" name="" type="button" class="ydbz_s" value="下载" onclick="downloadJKD('${ctx}/newRebatesReview/visaRebatesDownloadforReview?reviewId=${revid} &groupCode=${ordergroupcode}&rebatesId=${rebatesId}');">
		</div>
		<script type="text/javascript">			
			function printTure() {				
				var revid = "${revid}";
				var printDate="${printDate}";
			    dataparam={ 
			    		'revid':revid,
			    		'printDate':printDate
		        };	
		        $.ajax({ 
			          type:"POST",
			          url:g_context_url+"/newRebatesReview/visaRebatesReviewPrintAjax_new",
			          dataType:"json",
			          data:dataparam,
			          async : false,
			          success:function(data){
			        	  if(data.result==1){
			        		  window.opener.$("#searchForm").submit();
			        		  printPage(document.getElementById("printDiv"));  
			        	  }else{
			        		  $.jBox.tip("打印失败！"); 
			        	  }
			          }
		        });
			}
		</script>
	</body>		
</html>