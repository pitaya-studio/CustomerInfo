<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="decorator" content="wholesaler"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>产品-酒店产品详情</title>
    <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <!--[if lte IE 6]>
      <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
      <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link type="text/css" rel="stylesheet" href="jquery-validation/1.11.0/jquery.validate.min.css" />
    <link type="text/css" rel="stylesheet" href="jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
    <link type="text/css" rel="stylesheet" href="jqueryUI/themes/base/jquery.ui.all.css" />
    <script type="text/javascript" src="${ctxStatic}/js/little_logo.js"></script>
    <script type="text/javascript" src="js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
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
        	window.open("${ctx}/sys/docinfo/download/"+docId);
        }
    </script>
  </head>
  <div class="mod_nav">产品 &gt; 酒店产品 &gt; 酒店产品详情</div>
            <div class="produceDiv">
              <!--产品信息开始-->
              <div class="mod_information mar_top0" id="ofAnchor1">
                <div class="mod_information mar_top0" id="secondStepDiv">
					<div class="ydbz_tit">
						<span class="ydExpand" data-target="#baseInfo"></span>基本信息
					</div>
                  <div style="margin-top:28px;" id="baseInfo">
                    <table width="90%" border="0">
                      <tbody>
                        <tr>
                      	<!-- 根据需求隐藏空房单号  zhangchao 2016/01/07-->
                          <!-- <td class="mod_details2_d1" style="display:none">控房单号：</td> -->
                          <%-- <td class="mod_details2_d2" style="display:none">${activityHotel.activitySerNum }</td> --%>
                          <td class="mod_details2_d1">产品名称：</td>
                          <td class="mod_details2_d2">${activityHotel.activityName }</td>
                          <td class="mod_details2_d1">国家：</td>
                          <td class="mod_details2_d2"><trekiz:autoId2Name4Table
							tableName="sys_geography" sourceColumnName="uuid"
							srcColumnName="name_cn" value="${activityHotel.country }" /></td>
                          <td class="mod_details2_d1"></td>
                          <td title="" class="mod_details2_d2"></td>
                        </tr>
                        <tr>
                          <td class="mod_details2_d1">岛屿：</td>
                          <td class="mod_details2_d2"><trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${activityHotel.islandUuid}"/></td>
                          <td class="mod_details2_d1">酒店名称：</td>
                          <td class="mod_details2_d2"><trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${activityHotel.hotelUuid}"/></td>
                          <td class="mod_details2_d1">酒店星级：</td>
                          <td class="mod_details2_d2"><span class="y_xing"><c:forEach begin="1" end="${val }" step="1">★</c:forEach></span></td>
                          <td class="mod_details2_d1"></td>
                          <td title="" class="mod_details2_d2"></td>
                        </tr>
                        <tr>
                          <td class="mod_details2_d1">币种：</td>
                          <td class="mod_details2_d2"><trekiz:autoId2Name4Table
								tableName='currency' sourceColumnName='currency_id'
								srcColumnName='currency_name'
								value='${activityHotel.currencyId }' /></td>
                          <td class="mod_details2_d1"></td>
                          <td class="mod_details2_d2"></td>
                          <td class="mod_details2_d1"></td>
                          <td class="mod_details2_d2"></td>
                          <td class="mod_details2_d1"></td>
                          <td title="" class="mod_details2_d2"></td>
                        </tr>
                      </tbody>
                    </table>
                    
                    <table id="contentTable_new" class="table activitylist_bodyer_table_new sea_rua_table">
                      <thead>
                        <tr>
                          <th width="11%">团号/日期</th>
                          <th width="7%">房型 * 晚数</th>
                          <th width="4%">基础餐型</th>
                          <!-- <th width="12%">升级餐型&升级价格/人</th> -->
                          <th width="6%">上岛方式</th>
                          <th width="5%">参考航班</th>
                          <th width="14%">同行价/人</th>
                          <th width="6%"> <p>余位/间数/预报名</p></th>
                          <th width="8%"> <p>单房差</p></th>
                          <th width="9%">需交订金</th>
                          <th width="4%">状态</th>
                          <th width="14%">备注</th>
                        </tr>
                      </thead>
                      <tbody>
                      <c:forEach var="group" items="${activityHotel.activityHotelGroupList}">
                        <tr id="abc00000001" style="display: table-row;">
                          <td rowspan="${group.mealRiseRowspan }" class="tc">
                          	<a href="${ctx}/activityHotel/showActivityHotelDetail/${group.uuid}?type=group" target="_blank">${group.groupCode }</a> <br/>
                            <span><fmt:formatDate value="${group.groupOpenDate}" pattern="yyyy-MM-dd" /></span>
                          </td>
                         <!-- 房型 * 晚数 -->
						<c:choose>
							<c:when test="${fn:length(group.activityHotelGroupRoomList)==0}">
                                         		<td></td><td></td>
                            </c:when>
							<c:otherwise>
								<c:forEach begin="0" end="0" var="room" items="${group.activityHotelGroupRoomList}">
		                        	<td rowspan="${fn:length(room.activityHotelGroupMealList)}" class="tc">
		                            	<p><span>
								           <trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.hotelRoomUuid }"/>
					                   	   </span>
					                   	   *<span data-value="${room.nights}"> ${room.nights }</span>晚
		                                </p>
		                            </td>
		                            <c:choose>
		                            	<c:when test="${fn:length(room.activityHotelGroupMealList)==0}">
		                            		<td></td>
		                            	</c:when>
		                            	<c:otherwise>
			                            	<c:forEach begin="0" end="0" var="mealbase" items="${room.activityHotelGroupMealList}">
			                            	<td class="tc" data-value="${mealbase.hotelMealUuid}">
			                                    <trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
			                                </td>
			                             </c:forEach>
		                            	</c:otherwise>
		                            </c:choose>
		                        </c:forEach>
							</c:otherwise>
						</c:choose>
                          <td  rowspan="${group.mealRiseRowspan }"  class="tc">
                            <c:forEach var="islandway" items="${fn:split(group.islandWay,';')}">
                           		<p>
                           			<trekiz:autoId2Name4Table tableName="sys_company_dict_view" sourceColumnName="uuid" srcColumnName="label" value="${fn:trim(islandway)}"/>
                           		</p>	
                			</c:forEach>
                		 </td>
                          <td rowspan="${group.mealRiseRowspan }" class="tc"><p><span title="中国南方航空公司">
                          ${group.airline }
                          </span></p></td>
                          <td rowspan="${group.mealRiseRowspan }" class="tl">
                          	<c:forEach items="${group.activityHotelGroupPriceList }" var="price" varStatus="status">
                          				<p>
											<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${price.type}" />
											:
											<span data-value="${group.currencyId }"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${price.currencyId }" /></span>
											<span><fmt:formatNumber type="currency" pattern="#,##0.00" value="${price.price }"/></span>
										</p>
							</c:forEach>
                           </td>
                          <td rowspan="${group.mealRiseRowspan }" class="tc"><span class="tdred over_handle_cursor">${group.remNumber }</span>/<span>${group.remNumber}/${group.orderNum }</span></td>
                          <td rowspan="${group.mealRiseRowspan }" class="tr"><span data-value="￥">
                          		<trekiz:autoId2Name4Table
								tableName="currency" sourceColumnName="currency_id"
								srcColumnName="currency_mark"
								value="${group.currencyId}" /></span><span class="fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singlePrice }"/></span></td>
                          <td rowspan="${group.mealRiseRowspan }" class="tr  tdgreen"><p><span data-value="￥"><trekiz:autoId2Name4Table
							tableName="currency" sourceColumnName="currency_id"
							srcColumnName="currency_mark"
							value="${group.frontMoneyCurrencyId}" /></span><span class="fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.frontMoney }"/></span></p></td>
                          <td rowspan="${group.mealRiseRowspan }" class="tc"> 
                          	<span>
		                          <c:choose>
									<c:when test="${group.status=='1' }">上架</c:when>
									<c:when test="${group.status=='2' }">下架</c:when>
									<c:when test="${group.status=='3' }">草稿</c:when>
									<c:when test="${group.status=='4' }">已删除</c:when>
								  </c:choose>
							  </span></td>
                          <td rowspan="${group.mealRiseRowspan }" class="p0">${group.memo }</td>
                        </tr>
                        <c:choose>
						<c:when test="${fn:length(group.activityHotelGroupRoomList)==0}">
						
						</c:when>
						<c:otherwise>
						<c:forEach begin="0" var="room" items="${group.activityHotelGroupRoomList}" varStatus="status">
                          <c:choose>
                           	 	<c:when test="${status.index==0}">
                           	 	    <c:forEach begin="1" var="mealbase" items="${room.activityHotelGroupMealList}">
                           	 	    	<tr>
                                            <td class="tc" data-value="${mealbase.hotelMealUuid}">
                                 	            <trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
                                            </td>
                                            
                                        </tr> 
                                    </c:forEach>
                           	 	</c:when>
                           <c:otherwise>
                           <c:forEach var="mealbase" items="${room.activityHotelGroupMealList}" varStatus="sss">
                     	       <c:choose>
                     	           <c:when test="${sss.index==0 }">
                          	 	       <tr">
                        	               <td rowspan="${fn:length(room.activityHotelGroupMealList)}" class="tc">
	                         	 		       <p>
	                         	 			   <span data-value="${room.uuid}">
					                           <trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.hotelRoomUuid }"/>
				                               </span>*<span data-value="${room.nights}"> ${room.nights }</span>晚
	                                           </p>
                                           </td>
                                           <td class="tc" data-value="${mealbase.hotelMealUuid}">
                           		               <trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
                                           </td>
                                           
                          	            </tr>	
                                  </c:when>
                                  <c:otherwise>
                         		      <tr>
                                          <td class="tc" data-value="${mealbase.hotelMealUuid}">
                                      	     <trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
                                           </td>
                                           
                                      </tr>      
                              	   </c:otherwise>
                           	</c:choose>
                          </c:forEach>    
                          </c:otherwise>
                       </c:choose>
	                </c:forEach>
						</c:otherwise>
					</c:choose>
						
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
						<label class="batch-label company_logo_pos">
						产品行程介绍：
						</label>
						<ul>
						<c:forEach items="${prodSchList}" var="fileprod">
							<li>
								<i class="sq_bz_icon"></i>
								<a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${fileprod.docId})">${fileprod.docName }</a>
								<a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${fileprod.docId})"><i class="download_icon"></i></a>
								<!--<i class="del_fj_icon"></i>-->
							</li>
						</c:forEach>
						</ul>
					</div>
                  <div class="mod_information_d7"></div>
                  <div class="batch" style="margin-top:10px;">
                    <label class="batch-label">自费补充协议：</label>
					<ul>
						<c:forEach items="${costProtocolList}" var="filecostPro">
							<li>
								<i class="sq_bz_icon"></i>
								<a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${filecostPro.docId})">${filecostPro.docName }</a>
								<a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${filecostPro.docId})"><i class="download_icon"></i></a>
							</li>
						</c:forEach>
					</ul>
                  </div>
                  <div class="mod_information_d7"></div>
                 <div class="batch" style="margin-top:10px;">
                    <label class="batch-label">其他补充协议：</label>
					<ul>
						<c:forEach items="${otherProtocolList}" var="file">
							<li>
								<i class="sq_bz_icon"></i>
								<a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docId})">${file.docName }</a>
								<a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docId})"><i class="download_icon"></i></a>
							</li>
						</c:forEach>
					</ul>
                  </div>
                </div>

				<div class="ydbz_tit">
					<span class="ydExpand" data-target="#otherInfo"></span>其他信息
				</div>
                <div class="other_info" id="otherInfo" style="padding-top:0px;padding-bottom:0px;">
                  ${activityHotel.memo }
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
	                    <c:forEach items="${shareUser }" var="shareUser">
	                      <tr>
	                        <td class="tc">${shareUser.company.name }</td>
	                        <td class="tc">${shareUser.name } </td>
	                        <td class="tc">${shareUser.email }</td>
	                      </tr>
	                    </c:forEach>
                    </tbody>
                  </table>
                </div>
                <div class="release_next_add">
                  <input type="button" value="关闭" class="btn btn-primary" onclick="window.close();"/>
                </div>
              </div>
              </form>
              <!--第三步上传资料结束-->
            </div>
            <!--右侧内容部分结束-->
          </div>
        </div>
      </div>
    </div>
  </body>
  <script type="text/javascript">
   /*  $(document).ready(function() {
      $('div.secondStepTitle span:last-child').toggle(function() {
          $(this).text("展开");
          $($(this).attr('data-target')).hide();
      }, 
      function() {
          $(this).text("收起");
          $($(this).attr('data-target')).show();
      });
			  // 折叠展开
		$("span.ydExpand").on('click', function () {
			var $this = $(this);
			var target = $(this).attr("data-target");
			$this.toggleClass("ydClose");
			$(target).slideToggle();
		});

  }); */
  </script>

</html>
