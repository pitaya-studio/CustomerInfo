<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>产品-海岛游产品详情</title>
    <meta name="decorator" content="wholesaler"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<script type="text/javascript" src="${ctxStatic}/js/little_logo.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <!--产品模块的脚本-->
    <script type="text/javascript" src="js/tmp.products.js"></script>
    <script type="text/javascript">
      $(function(){
      	$("#thirdStepDiv .mod_information_d8_2 select[name='country']").comboboxSingle();
      	//显示联运/分段联运价格
      	islandShowPrice();
      	g_context_url = "${ctx}";
      });
      //删除已上传的文件
      function deleteFile(thisDom,fileID){
      	$(thisDom).parent("li").remove();
      }
      function downloads(docId){
        	window.open(g_context_url+"/sys/docinfo/download/"+docId);
        }
    </script>
  </head>
<body>
	 <!--右侧内容部分开始-->
			<div class="mod_nav">产品 &gt; 海岛游产品 &gt; 海岛游产品详情</div>
            <div class="produceDiv">
              <!--产品信息开始-->
              <div class="mod_information mar_top0" id="ofAnchor1">
                <div class="mod_information mar_top0" id="secondStepDiv">
					<div class="ydbz_tit island_productor_upload001">
						<span class="ydExpand" data-target="#baseInfo"></span>基本信息
					</div>
                  <div style="margin-top:28px;" id="baseInfo">
                    <table width="90%" border="0">
                      <tbody>
                        <tr>
                          <td class="mod_details2_d1">产品名称：</td>
                          <td class="mod_details2_d2">${activityIsland.activityName }</td>
                          <td class="mod_details2_d1">国家：</td>
                          <td class="mod_details2_d2"><trekiz:autoId2Name4Table
							tableName="sys_geography" sourceColumnName="uuid"
							srcColumnName="name_cn" value="${activityIsland.country }" /></td>
                          <td class="mod_details2_d1">币种：</td>
                          <td class="mod_details2_d2">
                          		<trekiz:autoId2Name4Table
								tableName='currency' sourceColumnName='currency_id'
								srcColumnName='currency_name'
								value='${activityIsland.currencyId }' /></td>
                          <td class="mod_details2_d1">
                            <!--离境口岸：-->
                          </td>
                          <td title="" class="mod_details2_d2">
                            <!--重庆-->
                          </td>
                        </tr>
                        <tr>
                          <td class="mod_details2_d1"><p>岛屿：</p></td>
                          <td class="mod_details2_d2"><trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${activityIsland.islandUuid}"/></td>
                          <td class="mod_details2_d1">酒店名称 :</td>
                          <td class="mod_details2_d2"><trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${activityIsland.hotelUuid}"/></td>
                          <td class="mod_details2_d1">酒店星级 :</td>
                          <td class="mod_details2_d2"><span class="y_xing"><c:forEach begin="1" end="${val }" step="1">★</c:forEach></span></td>
                          <td class="mod_details2_d1">&nbsp;</td>
                          <td title="" class="mod_details2_d2">&nbsp;</td>
                        </tr>
                      </tbody>
                    </table>
                    <table id="contentTable" class="table activitylist_bodyer_table_new sea_rua_table">
                      <thead>
                        <tr>
                          <th width="10%">团号/日期</th>
                          <th width="5%">房型 * 晚数</th>
                          <th width="8%">基础餐型</th>
                          <th width="13%">升级餐型&amp;升级价格</th>
                          <th width="4%">上岛方式</th>
                          <th width="12%">航班<br />起飞到达时间</th>
                          <th width="13%">舱位等级&amp;同行价格&amp;余位</th>
                          <th width="5%"> <p>余位</p></th>
                          <th width="5%"><p>预收/预报名</p></th>
                          <th width="9%"> <p>单房差</p></th>
                          <th width="9%">需交订金</th>
                          <th width="4%">状态</th>
                          <th width="15%">备注</th>
                        </tr>
                      </thead>
                      <tbody>
							 <c:forEach var="islandGroup" items="${activityIslandInput.activityIslandGroupLists}" varStatus="v">
								  
								  <tr id="${v.count}" data-tag="${v.count}" style="display: table-row;">
	                                            <td rowspan="${islandGroup.baseMealNum}" class="tc">
	                                            <input type="hidden" id="islandGroupUuid" name="islandGroupUuid" value="${islandGroup.uuid }" />
	                                                <a href="${ctx}/activityIsland/showActivityIslandDetail/${islandGroup.uuid}?type=date" target="_blank">${islandGroup.groupCode}</a> <br />
	                                                <span><fmt:formatDate value='${islandGroup.groupOpenDate}' pattern='yyyy-MM-dd' type='date'/></span>
	                                            </td>
	                                         <c:choose>
	                                          	<c:when test="${fn:length(islandGroup.activityIslandGroupRoomList)==0}"><!-- 如果房间为空，则有三个单元格 -->
	                                          		<td></td><td></td><td></td>
	                                          	</c:when>
	                                          	<c:otherwise>
	                                            	<c:forEach begin="0" end="0" var="room" items="${islandGroup.activityIslandGroupRoomList}">
	                                               	 	<td rowspan="${fn:length(room.activityIslandGroupMealList)==0?1:fn:length(room.activityIslandGroupMealList)}" class="tc">
		                                               	 	<p>
		                                               	 		<span data-value="${room.hotelRoomUuid}">
																	<trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.hotelRoomUuid }"/>
																</span>*<span data-value="${room.nights}"> ${room.nights }</span>晚 <br>
		                                               	 	</p>
	                                               	 	 </td>
	                                               	 	 <c:choose>
	                                               	 	 	<c:when test="${fn:length(room.activityIslandGroupMealList)==0}"><!-- 如果基础餐型为空，则有两个单元格 -->
	                                               	 	 		<td></td><td></td>
	                                               	 	 	</c:when>
	                                               	 	 	<c:otherwise>
	                      										<c:forEach begin="0" end="0" var="mealbase" items="${room.activityIslandGroupMealList}">
					                                                <td class="tc" data-value="${mealbase.hotelMealUuid}">
					                                                	<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
					                                                </td>
					                                                <c:choose>
				                                            			<c:when test="${fn:length(mealbase.activityIslandGroupMealRiseList)==0}">
				                                            				<td></td>
				                                            			</c:when>
				                                            			<c:otherwise>
				                                            				<td class="tc">
									                                                <c:forEach var="mealrise" items="${mealbase.activityIslandGroupMealRiseList}">
											                                                <p>
											                                                	<span data-value="${mealrise.hotelMealUuid}">
											                                                		<!--<trekiz:defineDict className="wtext" name="hotelMeals" type="hotel_meal_type" dataScope="system" defaultValue="${mealrise.hotelMealUuid}" /> -->
											                                                		<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealrise.hotelMealUuid}"/>
											                                                	</span>
											                                                	&nbsp;&nbsp;&nbsp;
											                                                	<span data-value="${mealrise.currencyId}">
																									<c:forEach items="${currencyList}" var="item">
																										<c:if test="${mealrise.currencyId==item.id}">${item.currencyMark}</c:if>
																									</c:forEach>
																								</span>
											                                                	<span><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${mealrise.price}" /></span>&nbsp;&nbsp;&nbsp;/人
										                                              	</p>
									                                                </c:forEach>
								                                                </td>
				                                            			</c:otherwise>
				                                            		</c:choose>
					                                                
				                                               </c:forEach>                         	 	 	
	                                               	 	 	</c:otherwise>
	                                               	 	 </c:choose>	 
	                                               	 
	                                             </c:forEach>
	                                          	</c:otherwise>
	                                          </c:choose>
	                                          
	                                           <td rowspan="${islandGroup.baseMealNum}" class="tc">
	                                              	<c:forEach var="islandway" items="${fn:split(islandGroup.islandWay,';')}">
	                                              		<p data-value="${islandway}">
	                                              			<trekiz:autoId2Name4Table tableName="sys_company_dict_view" sourceColumnName="uuid" srcColumnName="label" value="${fn:trim(islandway)}"/>
	                                              		</p>	
	                                              	</c:forEach>
	                                           </td>
	                                           <td rowspan="${islandGroup.baseMealNum}" class="tc">
	                                              <span data-value="${islandGroup.activityIslandGroupAirlineList[0].airline},${islandGroup.activityIslandGroupAirlineList[0].flightNumber}">${islandGroup.activityIslandGroupAirlineList[0].flightNumber}</span>
	                                              <br />
	                                             		<span data-start="<fmt:formatDate value='${islandGroup.activityIslandGroupAirlineList[0].departureTime}' pattern='HH:mm' type='date'/>" data-end="<fmt:formatDate value='${islandGroup.activityIslandGroupAirlineList[0].arriveTime}' pattern='HH:mm' type='date'/>" data-days="${islandGroup.activityIslandGroupAirlineList[0].dayNum}" class="lieHt30 fbold">
	                                             		<fmt:formatDate value="${islandGroup.activityIslandGroupAirlineList[0].departureTime}" pattern="HH:mm" type="date"/>
												-<fmt:formatDate value="${islandGroup.activityIslandGroupAirlineList[0].arriveTime}" pattern="HH:mm" type="date"/>
												<c:choose>
													<c:when test="${islandGroup.activityIslandGroupAirlineList[0].dayNum==0}">
													
													</c:when>
													<c:when test="${islandGroup.activityIslandGroupAirlineList[0].dayNum==1}">
													<br/><span class="lianyun_name next_day_icon" >次日</span>
													</c:when>
													<c:otherwise>
													 <br/> <span class="lianyun_name next_day_icon" > ${islandGroup.activityIslandGroupAirlineList[0].dayNum}+</span>
													</c:otherwise>
												</c:choose>
												</span>
	                                             </td>
	                                             <td rowspan="${islandGroup.baseMealNum}" class="tc">
	                                               <c:forEach var="airline" items="${islandGroup.activityIslandGroupAirlineList}" >
	                                              		<div class="cw_thjg_yw" data-ctrl="${airline.controlNum}" data-unctrl="${airline.uncontrolNum}">
	                            						<span style="display:block">
	                                              			${airline.spaceLevelStr}<br>
	                                               		(<span class="or_color over_handle_cursor" title="控票：${airline.controlNum} 非控票：${airline.uncontrolNum}">
	                                               			余位：${airline.remNumber}
	                                               		</span>)</span>
	                                               		
		                                                	<c:forEach var="groupPrice" items="${islandGroup.activityIslandGroupPriceList}">
		                                                		<c:choose>
		                                                			<c:when test="${airline.uuid==groupPrice.activityIslandGroupAirlineUuid}">
			                            							<p><span  data-value="${groupPrice.type}">
			                                                			<trekiz:autoId2Name4Table tableName='traveler_type' sourceColumnName='uuid' srcColumnName='name' value='${groupPrice.type}'/>
			                                                			</span>
			                                                			:
			                                                				<span data-value="${groupPrice.currencyId}">
			                                                					<c:forEach items="${currencyList}" var="item">
																					<c:if test="${groupPrice.currencyId==item.id}">${item.currencyMark}</c:if>
																				</c:forEach>
			                                                				</span>
			                                                				<span><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${groupPrice.price}" /></span>
			                                                			</p>
		                                                			</c:when>
		                                                			
		                                                		</c:choose>
		                                                </c:forEach>
	                                              		</div>
	                                              	</c:forEach>
	                                             </td>
	                                             <td rowspan="${islandGroup.baseMealNum}" class="tc">
	                                             		<span class="tdred over_handle_cursor">${islandGroup.totalRemNum}</span>
	                                             </td>
	                                             <td rowspan="${islandGroup.baseMealNum}" class="tc" data-ctrlpriority="${islandGroup.priorityDeduction}">
	                                             		<span class="over_handle_cursor">${islandGroup.advNumber}/${islandGroup.bookingNum==null?0:islandGroup.bookingNum}</span>
	                                             </td>
	                                             <td rowspan="${islandGroup.baseMealNum}" class="tr">
	                                              	<span data-value="${islandGroup.currencyId}">
													<c:forEach items="${currencyList}" var="item">
														<c:if test="${islandGroup.currencyId==item.id}">${item.currencyMark}</c:if>
													</c:forEach>
												</span>
												<span><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${islandGroup.singlePrice}" /></span>
												<span data-value="${islandGroup.singlePriceUnit}">
													<c:choose>
														<c:when test="${islandGroup.singlePriceUnit==1}">
														/人
														</c:when>
														<c:when test="${islandGroup.singlePriceUnit==2}">
														/间
														</c:when>
														<c:when test="${islandGroup.singlePriceUnit==3}">
														/晚
														</c:when>
													</c:choose>
												</span>
	                                             </td>
	                                             <td rowspan="${islandGroup.baseMealNum}" class="tr tdgreen">
	                                              	<span data-value="${islandGroup.frontMoneyCurrencyId}">
													 <c:forEach items="${currencyList}" var="item">
														<c:if test="${islandGroup.frontMoneyCurrencyId==item.id}">${item.currencyMark}</c:if>
													</c:forEach>
												</span>
	                                              	<span class="fbold"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${islandGroup.frontMoney}" /></span>
	                                             </td>
	                                             <td rowspan="${islandGroup.baseMealNum}" class="tc">
	                                              	<span class="tdred">
	                                              		<c:choose>
	                                              			<c:when test="${islandGroup.status==1}">上架
	                                              			</c:when>
	                                              			<c:when test="${islandGroup.status==2}">下架
	                                              			</c:when>
	                                              			<c:when test="${islandGroup.status==3}">草稿
	                                              			</c:when>
	                                              			<c:when test="${islandGroup.status==4}">已删除
	                                              			</c:when>
	                                              			<c:otherwise>
	                                              				其他状态
	                                              			</c:otherwise>
	                                              		</c:choose>
	                                              	</span>
	                                             </td>
	                                             <td rowspan="${islandGroup.baseMealNum}" class="tl">
	                                             		${islandGroup.memo}
	                                             </td>
	                                             
	                                         </tr>
	                                         
	                                         <c:forEach begin="0" var="room" items="${islandGroup.activityIslandGroupRoomList}" varStatus="status">
	                                            <c:choose>
	                                             	 	<c:when test="${status.index==0}">
	                                             	 	    <c:forEach begin="1" var="mealbase" items="${room.activityIslandGroupMealList}">
	                                             	 	    	<tr data-tag="${v.count}">
				                                               <td class="tc" data-value="${mealbase.hotelMealUuid}">
				                                                	<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
				                                                </td>
				                                                <td class="tc">
					                                                <c:forEach var="mealrise" items="${mealbase.activityIslandGroupMealRiseList}">
							                                                <p>
							                                                	<span data-value="${mealrise.hotelMealUuid}">
							                                                		<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealrise.hotelMealUuid}"/>
							                                                	</span>
							                                                	&nbsp;&nbsp;&nbsp;
							                                                	<span data-value="${mealrise.currencyId}">
																					<c:forEach items="${currencyList}" var="item">
																						<c:if test="${mealrise.currencyId==item.id}">${item.currencyMark}</c:if>
																					</c:forEach>
																				</span>
							                                                	<span><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${mealrise.price}" /></span>&nbsp;&nbsp;&nbsp;/人
						                                                	</p>
					                                               </c:forEach>
					                                             </td>
				                                            </tr> 
			                                          </c:forEach>
	                                             	 	</c:when>
	                                         	
	                                             	 	<c:otherwise>
	                                            	 				<c:forEach var="mealbase" items="${room.activityIslandGroupMealList}" varStatus="sss">
								                                <c:choose>
	                                            	 							<c:when test="${sss.index==0 }">
	                                            	 								<tr data-tag="${v.count}">
										                                 	 <td rowspan="${fn:length(room.activityIslandGroupMealList)==0?1:fn:length(room.activityIslandGroupMealList)}" class="tc">
										                                  	 		<p>
										                                  	 			<span data-value="${room.uuid}">
																							<trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.hotelRoomUuid }"/>
																						</span>*<span data-value="${room.nights}"> ${room.nights }</span>晚
												                                  </p>
												                              </td>
											                                   <td class="tc" data-value="${mealbase.hotelMealUuid}">
											                                   		<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
											                                   </td>
											                                   <td class="tc">
												                                    <c:forEach var="mealrise" items="${mealbase.activityIslandGroupMealRiseList}">
												                                      <p>
												                                      	<span data-value="${mealrise.hotelMealUuid}">
												                                      		<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealrise.hotelMealUuid}"/>
												                                      	</span>
												                                      	<span data-value="${mealrise.currencyId}">
																									<c:forEach items="${currencyList}" var="item">
																										<c:if test="${mealrise.currencyId==item.id}">${item.currencyMark}</c:if>
																									</c:forEach>
																						</span>
												                                      	<span>${mealrise.price}</span>
												                                      	&nbsp;&nbsp;&nbsp;/人
												                                     	</p>
												                                    </c:forEach>
										                                   		</td>
										                                   	</tr>	
									                                   </c:when>
									                                   <c:otherwise>
									                                   		<tr data-tag="${v.count}">
									                                               <td class="tc" data-value="${mealbase.hotelMealUuid}">
									                                                	<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
									                                                </td>
									                                                <td class="tc">
										                                                <c:forEach var="mealrise" items="${mealbase.activityIslandGroupMealRiseList}">
												                                                <p>
												                                                	<span data-value="${mealrise.hotelMealUuid}">
												                                                		<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealrise.hotelMealUuid}"/>
												                                                	</span>
												                                                	<span data-value="${mealrise.currencyId}">
																										<c:forEach items="${currencyList}" var="item">
																											<c:if test="${mealrise.currencyId==item.id}">${item.currencyMark}</c:if>
																										</c:forEach>
																									</span>
												                                                	<span>${mealrise.price}</span>&nbsp;&nbsp;&nbsp;/人
											                                                	</p>
										                                                </c:forEach>
									                                               </td>
									                                          </tr>      
		                                               	 				</c:otherwise>
	                                             	 					</c:choose>
							                                 </c:forEach>    
	                                             	 	</c:otherwise>
	                                             	</c:choose>
	                                           </c:forEach>
							  </c:forEach>
	                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
              <!--产品信息结束-->
               <!--上传资料开始-->
              <div id="thirdStepDiv">
                <!-- 上传文件 -->
					<div class="ydbz_tit">
						<span class="ydExpand" data-target="#ziliaoInfo"></span>上传资料
					</div>
					<div class="mod_information_3 upload_file_new" id="ziliaoInfo">
					  <div class="batch">
					    <label class="batch-label company_logo_pos"> 产品行程介绍： </label>
					    <ul>
					     	<c:forEach items="${prodSchList}" var="file" varStatus="s1">
								<li>
								     <li> <i class="sq_bz_icon"></i>
									<span>${file.docName}</span><a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docId})">下载</a>
								<!--	<input type="hidden" name="hotelAnnexDocId" value="${file.docId}"/>
									<input type="hidden" name="docOriName" value="${file.docName}"/>
									<input type="hidden" name="docPath" value="${file.docPath}"/>
								 	<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'hotelAnnexDocId',this)">删除</a> -->
								</li>
							</c:forEach>
				        </ul>
				      </div>
					  <div class="mod_information_d7"></div>
					  <div class="batch" style="margin-top:10px;">
					    <label class="batch-label">自费补充协议：</label>
					    <ul>
					      <c:forEach items="${costProtocolList}" var="file" varStatus="s1">
								<li>
								     <li> <i class="sq_bz_icon"></i>
									<span>${file.docName}</span><a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docId})">下载</a>
								<!--	<input type="hidden" name="hotelAnnexDocId" value="${file.docId}"/>
									<input type="hidden" name="docOriName" value="${file.docName}"/>
									<input type="hidden" name="docPath" value="${file.docPath}"/>
								 	<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'hotelAnnexDocId',this)">删除</a> -->
								</li>
							</c:forEach>
				        </ul>
				      </div>
					  <div class="mod_information_d7"></div>
					  <div class="batch" style="margin-top:10px;">
					    <label class="batch-label">其他补充协议：</label>
					    <ul>
					      <c:forEach items="${otherProtocolList}" var="file" varStatus="s1">
								<li>
								     <li> <i class="sq_bz_icon"></i>
									<span>${file.docName}</span><a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docId})">下载</a>
								</li>
							</c:forEach>
				        </ul>
				      </div>
					  <div class="mod_information_d7"></div>
					  <div class="batch" style="margin-top:10px;">
					    <div style="width:85px; text-align:right; float: left; line-height:28px;">签证资料：</div>
					    <div style=" float:left; min-width:1000px;">
                    	<span class="sea_rua_product_detail">
					    <c:forEach items="${activityIsland.activityIslandVisaFile}" var="visafile">
						    <label class="visa_country_view_other">国家：
						      <trekiz:autoId2Name4Table	tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${visafile.country}" />
						    </label>
						    <label class="visa_country_view_other">签证类型：
						      ${fns:getDictLabelName(visafile.visaTypeId, "new_visa_type")}
						    </label>
						    <ul>
						       <c:forEach items="${visafile.hotelAnnexList}" var="annexfile">
							      <li> <i class="sq_bz_icon"></i> 
							      <%-- <a>${annexfile.docName}</a><i class="download_icon" onClick="downloads(${annexfile.docId})"></i></li> --%>
							      <span>${annexfile.docName}</span><a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${annexfile.docId})">下载</a>
				 	       	   </c:forEach>
					        </ul>
				        </c:forEach>
				        </span>
				      </div>
			    	</div>
			    </div>
			    
			    </div>
			    <!-- 上传文件 -->
               	<!--上传资料结束--> 
				<div class="ydbz_tit">
					<span class="ydExpand" data-target="#otherInfo"></span>其他信息
				</div>
                <div class="other_info" id="otherInfo" style="padding-top:0px;padding-bottom:0px;">
                  ${activityIsland.memo }
                </div>
				<div class="ydbz_tit" >
					<span class="ydExpand" data-target="#productInfo"></span>产品分享
				</div>
                <div class="product_share" id="productInfo" style="margin-top:30px;">
                  <table  class="table  activitylist_bodyer_table_new">
                    <thead>
                      <tr>
                        <th width="15%">所属部门</th>
                        <th width="9%">姓名</th>
                        <th width="10%">账号</th>
                      </tr>
                    </thead>
                    <tbody>
                   	  <c:forEach items="${shareUsers }" var="shareUsers">
	                      <tr>
	                        <td class="tc">${shareUsers.company.name}</td>
	                        <td class="tc">${shareUsers.name } </td>
	                        <td class="tc">${shareUsers.email }</td>
	                      </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>
                <div class="release_next_add">
                  <input type="button" value="关闭" class="btn btn-primary" onClick="window.close();"/>
                </div>
              </div>
              <!--第三步上传资料结束-->
            <!--右侧内容部分结束-->
        </div>
      </div>
    </div>
  </body>

</html>
