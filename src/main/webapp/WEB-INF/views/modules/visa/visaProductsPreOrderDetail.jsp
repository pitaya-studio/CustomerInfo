<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>预定-签证产品预定-产品详情</title>
    
    
	<script type="text/javascript">
		//文件下载
		function downloads(docid,activitySerNum,acitivityName,iszip){
	    	if(iszip){
	    		var zipname = activitySerNum;
	    		window.open("${ctx}/sys/docinfo/zipdownload/"+docid+"/"+zipname);
	    	}
	    		
	    	else
	    		window.open("${ctx}/sys/docinfo/download/"+docid);
	    }
	
	</script>
</head>


<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">签证产品详情</page:param>
</page:applyDecorator>
    <!--右侧内容部分开始-->
    <div class="produceDiv">
        <form id="modForm" action="${ctx}/activity/manager/modsave?proId=" method="post" enctype="multipart/form-data">
            <div class="mod_information">
                <div class="mod_information_d"><div class="ydbz_tit">产品基本信息</div></div>
            </div>
            <div class="mod_information_dzhan">
                <div class="mod_information_dzhan_d mod_details2_d">
                    <span style="color:#009535; font-size:16px; font-weight:bold;">${visaProduct.productName}</span>
                    <div class="mod_information_d7"></div>
                    <table width="90%" border="0">
                        <tbody>
                        	<tr>
		                        <td class="mod_details2_d1">签证国家：</td>
		                        <td class="mod_details2_d2">${sysCountry}</td>
		                        <td class="mod_details2_d1">签证领区：</td>
		                        <td class="mod_details2_d2">
			                            <c:if test="${not empty visaProduct.collarZoning }">
				                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
			                        	</c:if>
		                        </td>
		                        <td class="mod_details2_d1">签证类型：</td>
		                        <td class="mod_details2_d2">${visaType}</td>
	                    	</tr>
	                    	<!-- 去掉保留天数：（保留${visaProduct.stayTime}天） -->
		                    <tr>
		                        <td class="mod_details2_d1">预定方式：</td>
		                        <td class="mod_details2_d2">
			                        <c:choose>
			                            <c:when test="${visaProduct.reserveMethod eq '1'}">
			                          		预定
			                            </c:when>
			                            <c:when test="${visaProduct.reserveMethod eq '2'}">
			                          		付全款
			                            </c:when>
			                            <c:otherwise>
			                            	预定, 付全款
			                            </c:otherwise>
			                        </c:choose>
		                        </td>
		                        <!-- 对应需求编号  C460V3 添加团号 -->
		                        <td class="mod_details2_d1">团号：</td>
		                        <td class="mod_details2_d2">${visaProduct.groupCode}</td>
		                    </tr>
		                    <tr>
		                        <td class="mod_details2_d1">备注：</td>
		                        <td class="mod_details2_d2">${visaProduct.remark }</td>
		                    </tr>
                        </tbody>
                   	 </table>
                </div>
            </div>
            <div class="mod_information">
                <div class="mod_information_d"><div class="ydbz_tit">签证价格</div></div>
            </div>
            <div class="mod_information_dzhan">
                <div class="mod_information_dzhan_d mod_details2_d">
                    <table width="90%" border="0">
                        <tbody>
                        <tr>
                        	<c:if test="${visaCostPrice eq '11'}">
                        		<td class="mod_details2_d1">成本价格：</td>
                            	<td class="mod_details2_d2">${currencyMark}&nbsp;${visaProduct.visaPrice}&nbsp;/人</td>
                        	</c:if>
                            <td class="mod_details2_d1">应收价格：</td>
                            <td class="mod_details2_d2">${currencyMark}&nbsp;${visaProduct.visaPay}&nbsp;/人</td>
                            
                            <td colspan="2"></td>
                        </tr>
                        <!--0258-qyl-begin  -->
                         <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
                        <tr>
                            <td class="mod_details2_d1">发票税：</td>
                            <td class="mod_details2_d2">${visaProduct.invoiceQZ}&nbsp;%</td>
                        </tr>
                         </c:if>
                         <!-- 0258-qyl-end -->
                        </tbody></table>

                </div>
            </div>
            <!-- 上传文件 -->
            <div class="team_ins">
                <div class="mod_information">
                    <div class="mod_information_d"><div class="ydbz_tit">签证资料</div></div>
                </div>
                <div class="mod_information_dzhan">
                   	<ol class="batch-ol" style="margin-left:40px;">
						<c:forEach items="${docInfoList}" var="docInfo">
							<li>
								<span>${docInfo.docName}</span>
								<a onclick="downloads(${docInfo.id},'','',false)">下载</a>
							</li>
						</c:forEach>
					</ol>
               	</div>
               	
               	<div class="mod_information">
               		<div class="mod_information_d"><div class="ydbz_tit">需提供办签资料</div></div>
               	</div>
           		<div class="mod_information_dzhan">
					<div class="mod_information_dzhan_d">
						<label>需提供原件项目：</label>
						<c:forEach items="${fn:split(visaProduct.original_Project_Type,',')}" var="project">
							<span class="seach_check">
								<c:choose>
									<c:when test="${project eq 0}">护照</c:when>
									<c:when test="${project eq 1}">身份证</c:when>
									<c:when test="${project eq 3}">电子照片</c:when>
									<c:when test="${project eq 4}">申请表格</c:when>
									<c:when test="${project eq 5}">户口本</c:when>
									<c:when test="${project eq 6}">房产证</c:when>
									<c:when test="${project eq 2}">其他</c:when>
								</c:choose>
							</span>
						</c:forEach>
						<span class="seach_check">${visaProduct.original_Project_Name}</span>
					</div>
					<div class="mod_information_dzhan_d">
						<label>需提供复印件项目：</label>
						<c:forEach items="${fn:split(visaProduct.copy_Project_Type,',')}" var="project">
							<span class="seach_check">
								<c:choose>
								    <c:when test="${project eq 3}">护照</c:when>
								    <c:when test="${project eq 4}">身份证</c:when>
								    <c:when test="${project eq 5}">电子照片</c:when>
								    <c:when test="${project eq 6}">申请表格</c:when>
									<c:when test="${project eq 0}">户口本</c:when>
									<c:when test="${project eq 1}">房产证</c:when>
									<c:when test="${project eq 2}">其他</c:when>
								</c:choose>
							</span>
						</c:forEach>
						<span class="seach_check">${visaProduct.copy_Project_Name}</span>
					</div>
				</div>
            </div>
        </form>
        <div class="ydbz_sxb ydbz_button"><a class="ydbz_x" href="javascript:void(0)" onclick="javascript:window.close();">关闭</a></div>
    </div>
    <!--右侧内容部分结束-->
</body>
</html>