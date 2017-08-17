<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
<title>产品-机票产品及发布-详情页-单程</title>
<script type="text/javascript">
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
                  <page:param name="desc">机票产品详情</page:param>
               </page:applyDecorator>
    
            	<!--右侧内容部分开始-->
                <div class="produceDiv">
                <form id="modForm" action="/trekiz_wholesaler/a/activity/manager/modsave?proId=" method="post" enctype="multipart/form-data">
                    <div class="mod_information">
                         <div class="mod_information_d"><div class="ydbz_tit">产品基本信息</div></div>
                     </div>
                    <div class="mod_information_dzhan">
                        <div class="mod_information_dzhan_d mod_details2_d">
                        <c:forEach items="${airticket.flightInfos}" var="flightInfo" varStatus="listIndex">
                        <p class="ydbz_mc">
                        
                       	 ${fns:getDictLabel(airticket.departureCity, 'from_area', '')}
                        — 
                      	  <c:forEach items="${arrivedCitys}" var="arrivedCitys">
	                       		<c:if test="${arrivedCitys.id == airticket.arrivedCity}">
	                       		${arrivedCitys.name}
	                       		</c:if>
                     	   </c:forEach>
                        	：单程
							<c:if test="${flightInfo.ticket_area_type eq '1'}">（内陆）</c:if>
							<c:if test="${flightInfo.ticket_area_type eq '2'}">（国际）</c:if>
							<c:if test="${flightInfo.ticket_area_type eq '3'}">（内陆+国际）</c:if>
							<c:if test="${flightInfo.ticket_area_type eq '4'}">（国内）</c:if>
							</p>
                             
                            <table width="90%" border="0">
                            <tbody>
                            <tr>
                                <td class="mod_details2_d1">出发机场：</td>
                                <td class="mod_details2_d2">
	                           		${flightInfo.paraMap.leaveAirport}
                                </td>
                                <td class="mod_details2_d1">到达机场：</td>
                                <td class="mod_details2_d2">
                                     ${flightInfo.paraMap.destinationAirpost}
                                </td>
                                <td class="mod_details2_d1"></td>
                                <td class="mod_details2_d2"></td>
                             </tr>
                            
                             <tr>
                                <td class="mod_details2_d1">出发时刻：</td>
                                <td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td class="mod_details2_d1">到达时刻：</td>
                                <td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td class="mod_details2_d1">航班号：</td>
                                <td class="mod_details2_d2" >${flightInfo.flightNumber}</td>
                             </tr>
                             <tr>
                                <td class="mod_details2_d1">航空公司：</td>
                                <td class="mod_details2_d2">
                                ${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}
                                </td>
                                <td class="mod_details2_d1">舱位等级：</td>
                                <td class="mod_details2_d2">
                                 ${fns:getDictLabel(flightInfo.spaceGrade, "spaceGrade_Type", "")}
                                                 </td>		
                                <td class="mod_details2_d1">舱位：</td>
                                <td class="mod_details2_d2">
                                 ${fns:getDictLabel(flightInfo.airspace, "airspace_Type", "")}</td>
                             </tr>
                            </tbody></table>
                            </c:forEach>
                            
                            <div class="kong"></div>
                            <div class="seach25 seach100">
                                <p class="fbold f14" style="width:auto;">&nbsp;&nbsp;部&nbsp;&nbsp;&nbsp;门&nbsp;&nbsp;：</p>
                                <div>${deptName}</div>
                            </div>
                            <div class="kong"></div>
							<!--分区联运开始-->
                            <div class="mod_information_d2 lianyun">
                                	<div class="lianyun_select">
                                        <label class="fbold f14" style="width:auto">联运类型：</label><!--分区联运 //以前有这个-->
                                        <c:if test="${airticket.intermodalType==0}">无联运 </c:if>
                                        <c:if test="${airticket.intermodalType==2}">分区联运 </c:if>
                                    </div>
                                    <div class="transport_city">
	                                    <c:forEach items="${airticket.intermodalStrategies}" var="list" varStatus="listIndex">
	                                        <p><c:if test="${airticket.intermodalType==2}"><label class="transport_city_label">联运城市分区：</label></c:if>${list.groupPart}<label>联运价格：</label><span class="currency">${list.priceCurrency.currencyMark}${list.price}</span></p>
	                                    </c:forEach> 
                                    </div>
                             </div>          
                             <!--分区联运结束-->  
                             <!--离境口岸开始-->       
                             
                            <c:if test="${airticket.outArea ne '-1' }">
	                            <div class="seach25 seach100">
	                                <p class="fbold f14">离境口岸：</p>
	                                <div>
	                               <c:forEach items="${out_areas}" var="from_areas">
			                       		<c:if test="${from_areas.id == airticket.outArea}">
			                       		${from_areas.label}
			                       		</c:if>
		                     	   </c:forEach>
	                                </div>
	                            </div>
                            </c:if>           
                            
                            <!--离境口岸结束-->
							
							 <!--预收人数开始-->
                            <div class="seach25 seach100">
                                <p class="fbold f14" style="width:auto;">预收人数：</p>
                                <div>${airticket.reservationsNum}</div>
                            </div>
                            <div class="kong"></div>
                            <!--预收人数结束-->
                            <!--出票日期开始-->
                            <div class="seach25 seach100">
                                <p class="fbold f14" style="width:auto;">出票日期：</p>
                                <div><fmt:formatDate value="${airticket.outTicketTime}" pattern="yyyy-MM-dd"/></div>
                            </div>
                            <!--出票日期结束-->
							<div class="kong"></div>
                            
                            <!--占位方式开始-->
                            <div class="seach25 seach100">
                                <p class="fbold f14" style="width:auto;">付款方式：</p>
                                <p class="seach_r add-paytype">
                                    <font id="payModeText">
										<c:if test="${airticket.payMode_deposit==1}">订金占位（保留
											<c:if test="${airticket.remainDays_deposit == '' || airticket.remainDays_deposit == null}">0</c:if>
											<c:if test="${airticket.remainDays_deposit != '' && airticket.remainDays_deposit != null}">${airticket.remainDays_deposit}</c:if>
											天
											<c:if test="${airticket.remainDays_deposit_hour == '' || airticket.remainDays_deposit_hour == null}">0</c:if>
											<c:if test="${airticket.remainDays_deposit_hour != '' && airticket.remainDays_deposit_hour != null}">${airticket.remainDays_deposit_hour}</c:if>
											时
											<c:if test="${airticket.remainDays_deposit_fen == '' || airticket.remainDays_deposit_fen == null}">0</c:if>
											<c:if test="${airticket.remainDays_deposit_fen != '' && airticket.remainDays_deposit_fen != null}">${airticket.remainDays_deposit_fen}</c:if>
											分）
										</c:if>
										&nbsp;&nbsp;
										<c:if test="${airticket.payMode_advance==1}">预占位（保留
											<c:if test="${airticket.remainDays_advance == '' || airticket.remainDays_advance == null}">0</c:if>
											<c:if test="${airticket.remainDays_advance != '' && airticket.remainDays_advance != null}">${airticket.remainDays_advance}</c:if>
											天
											<c:if test="${airticket.remainDays_advance_hour == '' || airticket.remainDays_advance_hour == null}">0</c:if>
											<c:if test="${airticket.remainDays_advance_hour != '' && airticket.remainDays_advance_hour != null}">${airticket.remainDays_advance_hour}</c:if>
											时
											<c:if test="${airticket.remainDays_advance_fen == '' || airticket.remainDays_advance_fen == null}">0</c:if>
											<c:if test="${airticket.remainDays_advance_fen != '' && airticket.remainDays_advance_fen != null}">${airticket.remainDays_advance_fen}</c:if>
											分）</c:if>
										&nbsp;&nbsp;
										<c:if test="${airticket.payMode_full==1}">全款支付</c:if>
										&nbsp;&nbsp;
										<c:if test="${airticket.payMode_op==1}">计调确认占位</c:if>
										&nbsp;&nbsp;
										<c:if test="${airticket.payMode_cw==1}">财务确认占位</c:if>
                                    </font>
                                </p>
                                <div class="kong"></div>
                            </div>
                            <!--占位方式结束-->
                             <!--团号开始-->
                            <div class="seach25 seach100">
                                <p class="fbold f14" style="width:auto;">&nbsp;&nbsp;团&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;：</p>
                                <div>${airticket.groupCode}</div>
                            </div>
                            <div class="kong"></div>
                            <!--团号结束-->
                        </div>
                    </div>
                     <div class="mod_information">
                         <div class="mod_information_d"><div class="ydbz_tit">添加价格</div></div>
                     </div>
                     <div class="mod_information_dzhan">
                         <div class="mod_information_dzhan_d mod_details2_d">
                            <table width="90%" border="0">
                            <tbody><tr>
                                <td class="mod_details2_d1">币种选择：</td>
                                <td class="mod_details2_d2">${currency.currencyName}</td>
                                <td class="mod_details2_d1">税费：</td>
                                <c:if test="${airticket.taxamt == '0.00'}">
	                                <td class="mod_details2_d2">已含</td>	
                                </c:if>
                                
                                 <c:if test="${airticket.taxamt != '0.00'}">
	                                <td class="mod_details2_d2">${currency.currencyMark}${airticket.taxamt}</td>	
                                </c:if>
                                
                                <td class="mod_details2_d1">应付账期：</td>
                                <td class="mod_details2_d2"><span class="disabledshowspan"><fmt:formatDate value="${airticket.payableDate}" pattern="yyyy-MM-dd"/></span></td>
                                
                                <td class="mod_details2_d1">成人同行价：</td>
                                <td class="mod_details2_d2">${currency.currencyMark}${airticket.settlementAdultPrice}</td>
                             </tr>
                             <tr>
                                <td class="mod_details2_d1">儿童同行价：</td>
                                <td class="mod_details2_d2">${currency.currencyMark}${airticket.settlementcChildPrice}</td>
                                <td class="mod_details2_d1">特殊人群同行价：</td>
                                <td class="mod_details2_d2">${currency.currencyMark}${airticket.settlementSpecialPrice}</td>
                                <td class="mod_details2_d1">特殊人群最高人数：</td>
                                <td class="mod_details2_d2">${airticket.maxPeopleCount}</td>
                                <td class="mod_details2_d1">特殊人群备注：</td>
                                <td class="mod_details2_d2"><p style="width:200px; white-space:nowrap; text-overflow:ellipsis; overflow:hidden;" title="${airticket.specialremark}">${airticket.specialremark}</p></td>
                             </tr>
                             <!-- 0258需求,发票税:针对懿洋假期-tgy-s -->
                             <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' }">
                             <tr>
                                <td class="mod_details2_d1">发票税：</td>
                                 <c:if test="${empty airticket.invoiceTax}"><!-- 处理发票税为空的情况 -->
                                 <td class="mod_details2_d2">0.00&nbsp;%</td>
                                </c:if>
                                <c:if test="${not empty airticket.invoiceTax}"><!-- 处理发票税不为空的情况 -->
                                <td class="mod_details2_d2">${airticket.invoiceTax}&nbsp;%</td>
                                </c:if>
                             </tr>
                             </c:if>
                             <!-- 0258需求,发票税:针对懿洋假期-tgy-e -->
                             <tr>
                                <td class="mod_details2_d1">订金：</td>
                                <td class="mod_details2_d2">${currency.currencyMark}${airticket.depositamt}</td>
                                <td class="mod_details2_d1">订金时限：</td>
                               	<td class="mod_details2_d2"><fmt:formatDate value="${airticket.depositTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td class="mod_details2_d1">取消时限：</td>
                                <td class="mod_details2_d2"><fmt:formatDate value="${airticket.cancelTimeLimit}" pattern="yyyy-MM-dd HH:mm"/></td>
                            	<td class="mod_details2_d1">儿童最高人数：</td>
                                <td class="mod_details2_d2">${airticket.maxChildrenCount}</td>
                             </tr>
                            </tbody></table>
                            <div class="seach25 seach100 pro-marks1">
                              <p class="fbold f14" style="width:auto;margin-right:5px;">备注：</p>
                              <p class="seach_r"><span class="disabledshowspan">${airticket.remark}</span></p>
                            </div>
                            <div class="kong"></div>
                         </div> 
                     </div>
                    <!-- 上传文件 -->
                   <div class="team_ins">
                          <div class="mod_information">
                                  <div class="mod_information_d"><div class="ydbz_tit">上传资料</div></div>
                          </div>
                          <div class="mod_information_dzhan">
                            <div class="mod_information_dzhan_d">
	                              <c:forEach items="${airticket.airTicketFiles}" var="afile">  
	                              <label>报名表：</label>
	                              <label><!-- img alt="" src="images/shangchuanbiaoqian_11.gif"--></label>
	                              <label id="introduction_file" name="introduction_file">${afile.fileName }</label>
	                              <a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${afile.docId}')">下载</a>
	                              <div class="kong"></div>
	                              </c:forEach>
                             </div>
                          </div>
                          <c:if test="${is_need_groupCode eq '1' }"><!-- 当批发商具有团号库权限时,才进行修改记录的展示  -->
                          <!--  大洋87   机票产品团号修改记录，对应需求号 c451,c453 -->
                          <c:if test="${fns:getUser().company.uuid == '7a81a03577a811e5bc1e000c29cf2586' ||fns:getUser().company.uuid =='1d4462b514a84ee2893c551a355a82d2' ||fns:getUser().company.uuid =='7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid =='5c05dfc65cd24c239cd1528e03965021' || fns:getUser().company.groupCodeRuleJP==0}">
                            <div class="mod_information">
                                   <div class="mod_information_d">
                                   	<span style=" font-weight:bold; padding-left:20px;float:left">修改记录</span>
                                   </div>
                            </div>
                            
                            <c:forEach items="${groupcodeModifiedRecords}" var="modifiedRecord" varStatus="listIndex">
                               <div class="mod_information_dzhan">
	                                <span class="modifyTime" style="margin-left: 30px"><fmt:formatDate value="${modifiedRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
	                                                                                                 【<span class="modifyType">团号</span> 】
	                                                                                                  由【<span class="exGroupNo">${modifiedRecord.groupcodeOld}</span> 】修改成【<span class="groupNo">${modifiedRecord.groupcodeNew}</span>】
	                                 by【<span class="modifyUser">${modifiedRecord.updateByName}</span>】
                               </div>
		                    </c:forEach>
		                   </c:if> 
		                   </c:if>
                   </div>	
				</form>
                <div class="ydbz_sxb ydbz_button"><a class="ydbz_x" href="javascript:void(0)" onclick="javascript:window.close();">关闭</a></div>
                </div>
				<!--右侧内容部分结束-->
            
</body>
</html>