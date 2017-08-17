<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证费还款单</title>
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
		var revid = ${revid};
		var printDate="${printDate}";
	    dataparam={ 
	    		'revid':revid,
	    		'printDate':printDate
        };		
        $.ajax({ 
	          type:"POST",
	          url:g_context_url+"/visa/workflow/returnreceipt/visaReturnMoneyFeePrintAjax",
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
	    
	    <!-- 打印按钮的样式要修改并把位置移到 还款单的 下面-->
	    <!-- 
		<input id="dayinDiv" name="dayinDiv" type="button" class="tab" value="打印" onclick="printTure();">
		<input id="dayinDiv" name="dayinDiv" type="button" class="tab" value="下载" onclick="downloadJKD('${ctx}/visa/workflow/returnreceipt/downloadVisaReturnMoneySheet?revid=${revid}');">
		 -->
		<!-- 
		<a id="printdownload" href="${ctx}/visa/workflow/returnreceipt/downloadVisaReturnMoneySheet?revid=${revid}"  style="margin-left: 20px;">下载</a>
		-->
		<div id="printDiv">
			<table class="dayinzy">
				<thead>
					<tr>
						<th class="fr f4 paddr" colspan="8">首次打印日期：${printDate} </th>
					</tr>
					<tr>
						<th class="f1" colspan="8">签证费还款单</th>
					</tr>
					<tr>
						<th class="fl paddl f3" colspan="4">填写日期： <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${revCreateDate}"/></th>
						<th class="fr paddr f3" colspan="4">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="f2">还款单位</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w300">签证部</div>
						</td>
						<td class="f2">经办人</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w320">${operatorName}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">还款说明</td>
						<td class="f3" colspan="7">
							<div class="dayinzy_w698">${returnReceiptRemark}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">还款金额</td>
						<td class="f3" colspan="4">
							<div class="dayinzy_w373" style="text-align: left;">人民币${revReturnAmountDx}</div>
						</td>
						<td class="f2">￥</td>
						<td class="f3" colspan="2">
							<div class="dayinzy_w193" style="text-align: right;">${receiptAmount}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">还款人</td>
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
							<div class="dayinzy_w130">${cwmanager}</div>
						</td>
						<td class="f2" width="40">出纳</td>
						<td class="f3" width="110">
							<div class="dayinzy_w130">${cashier}</div>
						</td>
						<td class="f2" width="70">财务</td>
						<td class="f3" width="110">
							<div class="dayinzy_w130">${cw}</div>
						</td>
						<td class="f2" width="40">审批</td>
						<td class="f3">
							<div class="dayinzy_w152">${deptmanager}</div>
						</td>
					</tr>
					<tr>
					    <!-- 日期暂时先不显示 -->
					    <!-- 
					    <fmt:formatDate pattern="yyyy 年 MM 月 dd 日" value="${payDate}"/>
					    -->
						<td class="fr f3 noborder paddr" colspan="8">确认付款日期：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 下载打印按钮 位置 修改 -->
		<div class="dbaniu">
		    <input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure();">
		    <input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="下载" onclick="downloadJKD('${ctx}/visa/workflow/returnreceipt/downloadVisaReturnMoneySheet?revid=${revid}');"> 
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
				var revid = ${revid};
				var printDate="${printDate}";
			    dataparam={ 
			    		'revid':revid,
			    		'printDate':printDate
		        };	
		        $.ajax({ 
			          type:"POST",
			          url:g_context_url+"/visa/workflow/returnreceipt/visaReturnMoneyFeePrintAjax",
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
		</script>
	</body>

		
</html>