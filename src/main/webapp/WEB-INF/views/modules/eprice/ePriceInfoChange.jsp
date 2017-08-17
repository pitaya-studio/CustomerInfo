<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>询价确认</title>
	<meta name="decorator" content="wholesaler"/>
	<%@ include file="commonVar.jsp"%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/travelRequirementsData.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/ePriceInfo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/epriceCommon.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	
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
	<page:applyDecorator name="eprice_recordinfo_Change" >
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
        	<c:if test="${record.type==1}">
	            <div class="seach25 seach100">
				  <p>接待社计调：</p>
				  <p class="seach_r">
				  	<c:forEach items="${aoperators}" var="u">
				  		<span class="seach_check">${u.userName }</span>
				  	</c:forEach>
				  </p>
				</div>
			</c:if>
			<!-- 机票计调，暂时去掉
            <div class="seach25 seach100">
              <p>机票计调：</p>
              <p class="seach_r">
              	<c:forEach items="${toperators}" var="u">
			  		<span class="seach_check">${u.userName }</span>
			  	</c:forEach>
              </p> 
            </div> -->
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
				  	<c:when test="${record.baseInfo.teamType==2}">大客户</c:when>
				  	<c:when test="${record.baseInfo.teamType==3}">游学</c:when>
				  	<c:when test="${record.baseInfo.teamType==4}">自由行</c:when>
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
            <div class="seach25">
              <p>联系人：</p> <p class="seach_r">${record.baseInfo.contactPerson }</p>
            </div>
            <div class="seach25">
              <p>电话：</p> <p class="seach_r">${record.baseInfo.contactMobile }</p>
            </div>
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
	          <form>
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
						
						
	                    <div class="title_con">选择报价</div>
	                    <div id="admit-eprice-list-id">
		                    <c:forEach items="${alist}" var="a" varStatus="loop" >
			                    <div class="inquiry_choseprice">
			            			<label for="radio-reply-id-${a.id }">
			               				<input type="radio"  id="radio-reply-id-${a.id }" name="chosePriceA" value="${a.id}">${a.operatorUserName }
			               			</label>
			               			<div class="inquiry_chosepri_r" name="reply-info-div" vstat="${a.status}">
			               				<c:if test="${a.status>=2}">
			               					<p>${a.content}</p>
				               				<p class="inquiry_chosepri_r2">
				               				<span>接待社报价：</span>
				               						<span name="reply-price-detail-name" style="display: none;">${a.priceDetail}</span>
				               						<!-- <span>成人总价：¥ 10,000</span>
													<span>儿童总价：¥ 10,000</span>
													<span>其他总价：¥ 10,000</span> -->
													<br/>
													<span>总计： </span>
													<br/>
													<span>成人</span>
														<c:forEach items="${a.adult}"  var="adult"> 
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
													<!-- <em class="red20"><span class="moneyFormat" >¥${a.operatorTotalPrice}</span></em> -->
											</p>
											
												 <c:forEach items="${repalyFilelist[loop.count-1]}" var="list">
											 			 <div><strong>&nbsp;&nbsp;&nbsp;	·</strong>${list.fileName}</div>
											</c:forEach>											
											<c:if test="${repalyFilelist[loop.count-1].size()>=1}">										
												<a class="ydbz_s" href="${ctx}/sys/docinfo/zipdownload/${reUpFileIdsList[loop.count-1]}/aditFiles">下载</a>
											</c:if>
			               				</c:if>
									</div>
								</div>
		                    </c:forEach>
	           			</div>
	          </form>
	        </div>
        </c:if>
        <!--第三步 机票相关-->
        <c:if test="${record.isAppFlight==1}">
	        <div class="ydbz_tit"  style="display:none"><span class="ydExpand closeOrExpand"></span>机票询价内容</div>
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
		            <c:forEach items="${flist}" var="f"  varStatus="fStatus">
			            <div class="inquiry_choseprice">
			            	<label for="radio-reply-id-${f.id }">
			               		<input type="radio"   id="radio-reply-id-${f.id }" name="chosePriceB" value="${f.id}">${f.operatorUserName}
			               	</label>
			               	<div class="inquiry_chosepri_r" name="reply-info-div" vstat="${f.status}">
			               		<c:if test="${f.status>=2}">
				               		<p>${f.content}</p>
					               		<p class="inquiry_chosepri_r2">
					               		 <span>接待社报价：</span>
					               			<span name="reply-price-detail-name" style="display: none;">${f.priceDetail}</span>
				             				<!-- <span>成人总价：¥ 10,000</span>
											<span>儿童总价：¥ 10,000</span>
											<span>其他总价：¥ 10,000</span> -->
											<br/>
											<span>总计： </span>
											<br/>
											<span>成人</span>
												<c:forEach items="${f.adultTop}"  var="adultTop"> 
													<em class="adultmark">${fns:getCurrencyNameOrFlag(adultTop.currencyId,0)}</em> <em class="red20">${adultTop.sumPrice}</em>
												</c:forEach>
											<br/>
											<span>儿童</span>
												<c:forEach items="${f.childTop }"  var="childTop"> 
													<em class="childmark">${fns:getCurrencyNameOrFlag(childTop.currencyId,0)}</em> <em class="red20">${childTop.sumPrice}</em>
												</c:forEach>
											<br/>
											<span>特殊人群</span>
												<c:forEach items="${f.specialTop}"  var="specialTop"> 
													<em class="specialmark">${fns:getCurrencyNameOrFlag(specialTop.currencyId,0)}</em><em class="red20"> ${specialTop.sumPrice}</em>
												</c:forEach>
												<br/>
											<!-- 
											<em class="red20"><span class="moneyFormat">¥${f.operatorTotalPrice}</span></em>
											-->
											<c:if test="${not empty f.remark }">
											<br/>
											<span>备注：</span>
											<span class="seach_r">${f.remark}</span>
											</c:if>
					               		</p>
			               		</c:if>
			               	</div>
			           </div>
		           </c:forEach>
	            </div>
	          </form>
	        </div>
        </c:if>
        
        <div class="ydbz_tit"><span class="ydExpand closeOrExpand"></span>销售报价</div>
        <div class="messageDiv" flag="messageDiv">
        	<div class="seach25">
              <p>成本价：</p>
              <span id="operator-price-span-id"  class="moneyFormat"></span>
              <span id="operator-price-top-span-id"  class="moneyFormat"></span>
            </div>
            <div class="seach25">
              <p>外报价：</p>
              <input type="text" class="rmbp17" id="out-price-id"  maxlength="10" name="outPrice" value="${record.outPrice}" >
            </div>
            <!-- 
            <div class="seach25">
              <p>结算价：</p>
             	<span name="settlePrice" id="settle-price-span-id" class="moneyFormat">¥ 0</span>
            </div> -->
            <div class="kong"></div>
            </div>
        <div class="dbaniu">
			<a class="ydbz_s gray" onclick="history.back(-1)">返回</a>
			<!-- <a class="ydbz_s">取消询价</a> -->
			<a class="ydbz_s" href= "${ctx}/eprice/manager/project/onceagain/${record.type}/${record.id}"+>再次询价</a>
			<form id="record-price-form-id">
			     <!-- 是否有机票询价 -->
			  <input type="hidden" name="id_isAppFlight"  id="id_isAppFlight"  value="${record.isAppFlight}"/> 
			        <!-- 类型 单团or 机票 -->
			   <input type="hidden" name="id_recordType"  id="id_recordType"  value="${record.type}"/> 
			   	   <!-- 选中的 地接价格 -->
			    <input type="hidden" name="acceptAopPrice"  id="acceptAopPrice"  value=""/> 
			        <!-- 选中的 机票价格 -->
			    <input type="hidden" name="acceptTopPrice"  id="acceptTopPrice"  value=""/> 
			       <!-- 选中的 地接回复Id -->
			    <input type="hidden" name="acceptAopId"  id="acceptAopId"  value=""/> 
			     <!-- 选中的 机票回复Id -->
			    <input type="hidden" name="acceptTopId"  id="acceptTopId"  value=""/> 
				<input type="hidden" name="prid" value="${record.id }"/> 
				    <!-- 回复的  -->
				<input type="hidden" name="operatorPrice" id="operatorPrice"  value=""/>
				<input type="hidden" name="outPrice" value=""/>
				<input type="hidden" name="status" value="3"/>
				<input type="button" class="btn btn-primary" id="submitButton"  style="display:none" value="确认报价">
			</form>
		</div>
      <!--右侧内容部分结束--> 
      
	
</body>
</html>