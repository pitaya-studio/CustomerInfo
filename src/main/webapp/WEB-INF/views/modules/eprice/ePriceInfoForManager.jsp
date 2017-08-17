<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>询价详情</title>
	<meta name="decorator" content="wholesaler"/>
	<%@ include file="commonVar.jsp"%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/travelRequirementsData.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/ePriceInfo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/epriceCommon.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		var record = {};
		record.type='${record.type}';
		record.isAppFlight='${record.isAppFlight}';
		record.operatorPrice='${record.operatorPrice}';
		// 地接计调
		var aoperTotalPrice = '${record.acceptAoperatorReply}';
		if(aoperTotalPrice!=null){
			record.aoperatorTotalPrice='${record.acceptAoperatorReply.operatorTotalPrice}';
		}
		// 机票计调
		var aoperTotalPrice = '${record.acceptToperatorReply}';
		if(aoperTotalPrice!=null){
			record.toperatorTotalPrice='${record.acceptToperatorReply.operatorTotalPrice}';
		}
		
		record.outPrice='${record.outPrice}';
	</script>
<style type="text/css">
</style>
</head>
<body>
	<page:applyDecorator name="eprice_details" >
    </page:applyDecorator>
	<!--右侧内容部分开始-->
      
	    
        <!--第一步-->
		<div class="tr">
		<!-- 
			<a class="dyzx-add" href="">下载</a>
			<a class="dyzx-add" href="">打印</a>
		 -->
		</div>
       <div class="ydbz_tit">
					<span class="ydExpand closeOrExpand"></span>基本信息
         </div>
        <div class="messageDiv" flag="messageDiv">
			<!-- 接待社计调主管 -->
			<c:if test="${not empty AopManager }">
				<div class="seach25 seach100">
				  <p>接待社计调主管：</p>
				  <p class="seach_r">
				  	<c:forEach items="${AopManager}" var="u">
				  		<span class="seach_check">${u.name }</span>
				  	</c:forEach>
				  </p>
				</div>
			</c:if>
			<!-- 机票计调主管 -->
			<c:if test="${not empty TopManager }">
				<div class="seach25 seach100">
				  <p>机票计调主管：</p>
				  <p class="seach_r">
				  	<c:forEach items="${TopManager}" var="u">
				  		<span class="seach_check">${u.name }</span>
				  	</c:forEach>
				  </p>
				</div>
			</c:if>
			
            <div class="seach25">
              <p>销售姓名：</p> <p class="seach_r">${record.baseInfo.salerName }</p>
            </div>
            <div class="seach25">
              <p>销售电话：</p> <p class="seach_r">${record.baseInfo.salerPhone }</p>
            </div>
            <div class="seach25">
            	<p>销售邮箱：</p>
            	<p class="seach_r">${record.baseInfo.salerEmail}</p>
            </div>
            <div class="seach25">
              <p>团队类型：</p> 
              <p class="seach_r">
              	<c:choose>
					<c:when test="${record.baseInfo.teamType==1}">单团</c:when>
				  	<c:when test="${record.baseInfo.teamType==4}">大客户</c:when>
				  	<c:when test="${record.baseInfo.teamType==3}">游学</c:when>
				  	<c:when test="${record.baseInfo.teamType==5}">自由行</c:when>
				  		<c:when test="${record.baseInfo.teamType==7}">机票</c:when>
				  	<c:otherwise>其他</c:otherwise>
			  	</c:choose>
			  </p>
            </div>
            <div class="kong"></div>
            <div class="seach25">
              <p>询价客户类型：</p>
              <p class="seach_r">
              	<c:choose>
		   			<c:when test="${record.baseInfo.customerType==1}">直客</c:when>
		   			<c:when test="${record.baseInfo.customerType==2}">同行</c:when>
		   			<c:otherwise>其他</c:otherwise>
				</c:choose>
              </p>
            </div>
            <div class="seach25">
              <p>询价客户：</p><p class="seach_r">${record.baseInfo.customerName }</p>
            </div>
            <shiro:hasPermission name="enquiry:agentinfo:visibility">
	            <div class="seach25">
	              <p>联系人：</p> <p class="seach_r">${record.baseInfo.contactPerson }</p>
	            </div>
	            <div class="seach25">
	              <p>电话：</p> <p class="seach_r">${record.baseInfo.contactMobile }</p>
	            </div>
            </shiro:hasPermission>
            <div class="kong"></div>
			<div class="seach25">
            	<p>申请总人数：</p>
            	<p class="seach_r">${record.baseInfo.allPersonSum}</p>
            </div>
            <div class="seach25">
            	<p>成人人数：</p>
            	<p class="seach_r">${record.baseInfo.adultSum}</p>
            </div>
            <div class="seach25 pr">
              <p>儿童人数：</p>
               <p class="seach_r">${record.baseInfo.childSum}</p>
            </div>
            <div class="seach25">
              <p>特殊人群数：</p>
               <p class="seach_r">${record.baseInfo.specialPersonSum}</p>
            </div>
            <div class="kong"></div>
        </div>
        <c:if test="${record.type==1||record.type==3||record.type==4||record.type==5}">
	        <!--第二步-->
	        <div class="ydbz_tit"><span class="ydExpand closeOrExpand"></span>接待社询价内容</div>
	        <div flag="messageDiv" class="messageDiv">
	         
	            <div class="seach25">
	              <p>预计出团日期：</p>
	              <p class="seach_r"><fmt:formatDate value="${record.admitRequirements.dgroupOutDate}" pattern="yyyy-MM-dd"/></p>
	            </div>
	            <div class="seach25">
	              <p>出境口岸：</p>
	              <p class="seach_r">${record.admitRequirements.outAreaName}</p>
	            </div>
	            <div class="seach25">
	              <p>线路国家：</p>
	              <p class="seach_r">${record.admitRequirements.travelCountry}</p>
	            </div>
	            <div class="seach25">
	              <p>境外停留：</p>
	              <p class="seach_r">${record.admitRequirements.outsideDaySum}天，${record.admitRequirements.outsideNightSum}晚 </p>
	            </div>
	            <div class="kong"></div>
	            <div id="travel-requirements-div-id"> 
					<span  id="travel-requirements-span-id" style="display: none;">${record.admitRequirements.travelRequirements}</span>
	            </div>
	            
	            <div class="seach25 seach100"  fid="${record.admitRequirements.salerTripFile.id}">
							<p>行程附件：</p>
								 	    <c:if test="${upFileList.size()>=1}">
								 	    	  <br/>
								 	    	  <c:forEach items="${upFileList}" var="file">
										 	  <div><strong>&nbsp;&nbsp;&nbsp;	·</strong>${file.fileName}</div>
										 	   </c:forEach>
								 	   	     <a class="ydbz_s" href="${ctx}/sys/docinfo/zipdownload/${upFileDocIds}/aditFiles">下载</a>
								 	   </c:if>
								 	  <c:if test="${upFileList.size()==0}">
								 	   (无)
								 	   </c:if>
							</div>
						</div>
						
	                    <div class="title_con">选择报价</div>
	                    <div id="admit-eprice-list-id">
		                    <c:forEach items="${alist}" var="a" varStatus="loop">
			                    <div class="inquiry_choseprice">
			            			<label for="radio-reply-id-${a.id }">
			            				<c:if test="${a.id==record.acceptAoperatorReply.id}">
			               					<input type="radio"  disabled="disabled"  checked="checked"  id="radio-reply-id-${a.id }" name="chosePriceA" value="${a.id}">${a.operatorUserName }
			               				</c:if>
			               				<c:if test="${a.id!=record.acceptAoperatorReply.id}">
			               					<input type="radio"  disabled="disabled"  id="radio-reply-id-${a.id }" name="chosePriceA" value="${a.id}">${a.operatorUserName }
			               				</c:if>
			               			</label>
			               			<div class="inquiry_chosepri_r" name="reply-info-div" vstat="${a.status}">
			               				<c:if test="${a.status>=2}">
			               					<p>${a.content}</p>
				               				<p class="inquiry_chosepri_r2">
				               						<span>接待社报价：</span>
				               						<span name="reply-price-detail-name" style="display: none;">${a.priceDetail}</span>
				               						<!-- <span>成人总价：¥ 10,000</span>
													<span>儿童总价：¥ 10,000</span>
													<span>其他总价：¥ 10,000</span> 
													<span>总计：</span> <em class="red20"><span class="moneyFormat">¥${a.operatorTotalPrice}</span></em>-->
													<br/>
													<span>总计： </span>
													<br/>
													<span>成人</span>
														<c:forEach items="${a.adult }"  var="adult"> 
															<em class="adultmark">${fns:getCurrencyNameOrFlag(adult.currencyId,0)}</em> <em class="red20">${adult.sumPrice}</em>
														</c:forEach>
													<br/>
													<span>儿童</span>
														<c:forEach items="${a.child }"  var="child"> 
															<em class="childmark">${fns:getCurrencyNameOrFlag(child.currencyId,0)}</em> <em class="red20">${child.sumPrice}</em>
														</c:forEach>
													<br/>
													<span>特殊人群</span>
														<c:forEach items="${a.special }"  var="special"> 
															<em class="specialmark">${fns:getCurrencyNameOrFlag(special.currencyId,0)}</em><em class="red20"> ${special.sumPrice}</em>
														</c:forEach>
														<br/>
											</p>
											
											 <c:forEach items="${repalyFilelist[loop.count-1]}" var="list">
											 			 <div><strong>&nbsp;&nbsp;&nbsp;	·</strong>${list.fileName}</div>
											</c:forEach>	
											<c:if test="${repalyFilelist[loop.count-1].size()>=1}">										
												<a class="ydbz_s" href="${ctx}/sys/docinfo/zipdownload/${reUpFileIdsList[loop.count-1]}/aditFiles">下载</a>
											</c:if>
											    
											<c:if test="${a.tripFile!=null}">
												<p>
													<span class="fl">${a.tripFile.fileName}</span>
													<span class="seach25-xc">
												<!-- 	<a class="ydbz_s">预览</a>-->	
														<a class="ydbz_s" href="${ctx}/eprice/manager/ajax/project/file/download/${a.tripFile.id}">下载</a>
													</span>
												</p>
											</c:if>
											
			               				</c:if>
			               				
									</div>
								</div>
		                    </c:forEach>
	           			</div>
        </c:if>
        
        
        <!--第三步 机票相关-->
        <c:if test="${record.isAppFlight==1}">
	        <div class="ydbz_tit" ><span class="ydExpand closeOrExpand"></span>机票询价内容</div>
	        <div flag="messageDiv" class="messageDiv">
	          <form class="">
	            <div class="title_con">
	            	临时机票申请（
	            	<c:choose>
			   			<c:when test="${record.trafficRequirements.trafficLineType==1}">往返</c:when>
			   			<c:when test="${record.trafficRequirements.trafficLineType==2}">单程</c:when>
			   			<c:when test="${record.trafficRequirements.trafficLineType==3}">多段</c:when>
			   			<c:otherwise>其他</c:otherwise>
					</c:choose>
	            	）
	            </div>
	            
	            <div id="epirce-traffic-line-list-id">
		            <c:forEach items="${lineList}" var="line" varStatus="loop">
		            <c:if test="${record.trafficRequirements.trafficLineType!=2}"> 
		            <div class="title_samil"><strong>第${loop.count }段：</strong></div></c:if>
		            
		             <div class="kong"></div>
		            	<div name="line">
			            	<div class="seach25">
				              <p>出发城市：</p>
				              <p class="seach_r">${line.startCityName}</p>
				            </div>
				            <div class="seach25">
				              <p>到达城市：</p>
				              <p class="seach_r">${line.endCityName}</p>
				            </div>
				            <div class="kong"></div>
				            <div class="seach25">
				              <p>出发日期：</p>
				             <p class="seach_r"><fmt:formatDate value="${line.startDate}" pattern="yyyy-MM-dd"/></p>
				            </div>
				            <div class="seach25">
				              <p>出发时刻：</p>
				              <p class="seach_r" name="startTimeType" vtype="${line.startTimeType}">
								<c:choose>    
							   			<c:when test="${line.startTimeType==1}">早</c:when>
							   			<c:when test="${line.startTimeType==2}">中</c:when>
							   			<c:when test="${line.startTimeType==3}">晚</c:when>
							   			<c:otherwise>其他</c:otherwise>
									</c:choose></p>
				            </div>
				               <div class="seach25">
				              <p>时间区间：</p>
				              <p class="seach_r" name="startTimeType" vtype="${line.startTimeType}">
							   			 ${line.startTime1}:00 - ${line.startTime2}:00
				            </div>
				            <div class="kong"></div>
				            <div class="seach25">
				              <p>舱位等级：</p>
				              <p class="seach_r" name="aircraftSpaceLevel" vtype="${line.aircraftSpaceLevel}"> 
								  <c:choose>    
								        <c:when test="${line.aircraftSpaceLevel==0}">不限</c:when>
							   			<c:when test="${line.aircraftSpaceLevel==1}">头等舱</c:when>
							   			<c:when test="${line.aircraftSpaceLevel==2}">公务舱</c:when>
							   			<c:when test="${line.aircraftSpaceLevel==3}"> 经济舱</c:when>
							   			<c:otherwise>其他</c:otherwise>
									</c:choose>
				              
				          	 </p>
				            </div>
				            <div class="seach25">
				              <p>舱位：</p>
				              <p class="seach_r" name="aircraftSpace" vtype="${line.aircraftSpace}">
				              	 <c:choose>    
								        <c:when test="${line.aircraftSpace=='0'}">不限</c:when>
							   			<c:otherwise>  ${line.aircraftSpace}舱</c:otherwise>
									</c:choose>
				            </div>
				            <div class="kong"></div>
			            </div>
		            </c:forEach>
	            </div>
	            
	            
	            <div class="title_con">申请基本信息</div>
	            <div class="seach25">
	              <p>申请总人数：</p>${record.trafficRequirements.allPersonSum}
	            </div>
	            <div class="seach25">
	              <p>成人人数：</p>${record.trafficRequirements.adultSum}
	            </div>
	           	<div class="seach25 pr">
	              <p>儿童人数：</p>${record.trafficRequirements.childSum}
	            </div>
	            <div class="seach25">
	              <p>特殊人群人数：</p>${record.trafficRequirements.specialPersonSum}
	            </div>
	            <div class="kong"></div>
	            <div class="seach25 seach100">
	              <p>特殊要求：</p>
	              <p class="seach_r">${record.trafficRequirements.specialDescn}</p>
	            </div>
	            <div class="title_con">选择报价</div>
		            <div id="traffic-eprice-list-id">
			            <c:forEach items="${flist}" var="f">
				            <div class="inquiry_choseprice">
				            	<label for="radio-reply-id-${f.id }">
				            			<c:if test="${f.id==record.acceptToperatorReply.id}">
				            				<input type="radio"   id="radio-reply-id-${f.id }" disabled="disabled" checked="checked" name="chosePriceB" value="${f.id}">${f.operatorUserName}
			               				</c:if>
			               				<c:if test="${f.id!=record.acceptToperatorReply.id}">
			               					<input type="radio"   id="radio-reply-id-${f.id }" disabled="disabled" name="chosePriceB" value="${f.id}">${f.operatorUserName}
			               				</c:if>
				               		
				               	</label>
				               	<div class="inquiry_chosepri_r" name="reply-info-div" vstat="${f.status}">
				               		<c:if test="${f.status>=2}">
				               		<p>${f.content}</p>
					               	<p class="inquiry_chosepri_r2">
						               	<span>接待社报价：</span>
						               		<c:if test="${not empty f.operatorTotalPrice}">
						               			<span name="reply-price-detail-name" style="display: none;">${f.priceDetail}</span>
												<span>总计：</span>
												<em class="red20"><span class="moneyFormat">¥${f.operatorTotalPrice}</span></em>
												
						               		</c:if>
						               		<c:if test="${empty f.operatorTotalPrice }">
						               			<br/>
												<span>总计： </span>
												<br/>
												<span>成人</span>
													<c:forEach items="${f.adultTop }"  var="adultTop"> 
														<em class="adultmark">${fns:getCurrencyNameOrFlag(adultTop.currencyId,0)}</em> <em class="red20">${adultTop.sumPrice}</em>
													</c:forEach>
												<br/>
												<span>儿童</span>
													<c:forEach items="${f.childTop }"  var="childTop"> 
														<em class="childmark">${fns:getCurrencyNameOrFlag(childTop.currencyId,0)}</em> <em class="red20">${childTop.sumPrice}</em>
													</c:forEach>
												<br/>
												<span>特殊人群</span>
													<c:forEach items="${f.specialTop }"  var="specialTop"> 
														<em class="specialmark">${fns:getCurrencyNameOrFlag(specialTop.currencyId,0)}</em><em class="red20"> ${specialTop.sumPrice}</em>
													</c:forEach>
												<br/>
						               		</c:if>
											<c:if test="${not empty f.remark }">
												<br/>
												<span>备注：</span>
												<span class="seach_r">${f.remark}</span>
											</c:if>
					               		</p>
				               		</c:if>
				               	</div>
				               	<!-- 机票备注 -->
								
				           </div>
			           </c:forEach>
		           </div>
	          </form>
	        </div>
        </c:if>
        <!-- 计调报价 -->
        <div class="ydbz_tit"><span class="ydExpand closeOrExpand"></span>计调报价</div>
        <div class="messageDiv" flag="messageDiv">
        	<div class="seach25">
	             <p>成本价：</p>
	             <c:if test="${not empty record.operatorPrice}">
	             	<span>¥</span>
	             	 <span class="moneyFormat">${record.operatorPrice}</span>
	             </c:if>
	             <c:if test="${empty record.operatorPrice}">
	             	<span id="operator-price-span-id"  class="moneyFormat"></span>
              		<span id="operator-price-top-span-id"  class="moneyFormat"></span>
	             </c:if>
            </div>
            <div class="seach25">
              <p>外报价：</p><span>¥</span>
              <span class="moneyFormat">${record.outPrice}</span>
            </div>
            <!-- 
            <div class="seach25">
              <p>结算价：</p>
              <span class="moneyFormat">¥ ${setter}</span>
            </div> -->
            <div class="kong"></div>
            </div>
        <div class="dbaniu">
			<a class="ydbz_s gray" name="history-back" onclick="history.go(-1)">返回</a>
		</div>
      <!--右侧内容部分结束--> 
      
	
</body>
</html>