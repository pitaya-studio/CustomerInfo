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
	         // url:g_context_url+"/review/visaRebates/visaRebatesReviewPrintAjax",
	          url:g_context_url+"/newRebatesReview/visaRebatesReviewPrintAjax_new",
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
			   				&nbsp;&nbsp;${accountName}	
						</td>
					</tr>
					<tr>
						<td class="f2">${currencyName}</td>
						<td class="f3" colspan="5" style="text-align:right;">${payRebatesAmount}</td>
						<td class="f2" >汇率</td>
						<td class="f3">${currencyExchangerate }</td>
					</tr>
					<tr>
						<td class="fc f2">合计人民币</td>
						<td class="fl f3" colspan="4">
							${rebatesAmountDx}
						</td>
						<td class="fc f3">￥</td>
						<td class="fl f3" colspan="2" style="text-align:right;">${rebatesAmount}</td>
					</tr>
					<!-- 将领款人改为实际领款人-0419-djw -->
					<c:choose>
						<c:when test="${payType == 1 or payType==3 and companyUUID eq '7a81b21a77a811e5bc1e000c29cf2586'}">
							<tr>
								<td class="fc f2">实际领款人</td>
								<td class="fc f3">
									${orderCompanyName}
								</td>
								<td class="fc f2">主管审批</td>
								<td class="fc f3" colspan="5">
									${jdmanager}
								</td>
							</tr>
						</c:when>
						<c:otherwise>
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
						</c:otherwise>
					</c:choose>
					<%-- <tr>
						<td class="fc f2">领款人</td>
						<td class="fc f3">
							${operatorName}
						</td>
						<td class="fc f2">主管审批</td>
						<td class="fc f3" colspan="5">
							${jdmanager}
						</td>
					</tr> --%>
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
			<input id="" name="" type="button" class="ydbz_s" value="下载" onclick="downloadJKD('${ctx}/newRebatesReview/downloadRebatesPrintSheet_new?reviewId=${revid} &groupCode=${ordergroupcode}&payId=${payId}');">
		</div>
		<script type="text/javascript">
		
		    /** *传入的IDS为需要屏蔽的元素id数组 */ 

/* 			function print(IDS) { //将需要屏蔽的元素隐藏 
				for (var i = 0; i < IDS.length; i++) {
					var target = document.getElementById(IDS[i]);
					target.style.display = "none";
				}
				var imagesarray = window.document.images
				for (var i = 0; i < imagesarray.length; i++) {
					imagesarray[i].style.display = "none";
				}
				//打印
				window.print();
				//显示屏蔽的元素 
				for (var i = 0; i < IDS.length; i++) {
					var target = document.getElementById(IDS[i]);
					target.style.display = "block";
				}
				for (var i = 0; i < imagesarray.length; i++) {
					imagesarray[i].style.display = "block";
				}
			} */
			
			function printTure() {
				/* 	 var dayinDiv = document.getElementById("dayinDiv");
				dayinDiv.style.display = "none";
				var printdownload = document.getElementById("printdownload");
				printdownload.style.display = "none";  */
				
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
			        		  //window.opener.location.reload();
			        		  if(window.opener){
			        			  window.opener.$("#searchForm").submit();
			        		  }
			        		  //location.reload();
			        		  printPage(document.getElementById("printDiv"));  
			        	  }else{
			        		  $.jBox.tip("打印失败！"); 
			        	  }
			          }
		        });
		
				//window.print();
				
				/*  var dayinDivpostprint = document.getElementById("dayinDiv");
				dayinDivpostprint.style.display = "block";
				
				var printdownloadpostprint = document.getElementById("printdownload");
				printdownloadpostprint.style.display = "block";  */
			}
		</script>
	</body>

		
</html>