<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>询价-酒店-报价-报价详情</title>
<!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript"	src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript"	src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery.zclip/jquery.zclip.js"></script>
</head>
<body>
	<!--右侧内容开始-->
		<div class="mod_nav">询价 > 酒店 > 报价 > 报价详情</div>
		<div class="ydbzbox fs">
			<div class="ydbz_tit island_productor_upload001">基本信息</div>
			<input type="hidden" value='${jsondate}' id="jsondate">
			<input type="hidden" value='${resultCopy}' id="resultCopy">
			<div class="quotation_main_list">
				<div id="signChannelList">
					<div class="activitylist_bodyer_right_team_co background_color_none" id="baseInfo">
						<div style="margin-top:8px;" id="secondStepEnd">
							<div class="activitylist_bodyer_right_team_co1">
								<label>销售姓名：</label> ${user.name }
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label>销售电话：</label> ${user.mobile }
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label>销售邮箱：</label> ${user.email }
							</div>
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co1">
								<label>客户类型：</label>
								<c:choose>
									<c:when test="${hotelQuote.quoteType==1 }">直客</c:when>
									<c:when test="${hotelQuote.quoteType==2 }">同行</c:when>
									<c:when test="${hotelQuote.quoteType==3 }">其他</c:when>
								</c:choose>
							</div>
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co1">
								<label>询价客户：</label>
								<c:choose>
									<c:when test="${hotelQuote.quoteType==2 }">
										<trekiz:autoId2Name4Table tableName="agentinfo"
											sourceColumnName="id" srcColumnName="agentName"
											value="${hotelQuote.quoteObject}" />
									</c:when>
									<c:otherwise>${hotelQuote.quoteObject}</c:otherwise>
								</c:choose>
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label>联系人名：</label> ${hotelQuote.linkName }
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label>联系电话：</label> ${hotelQuote.linkPhone }
							</div>
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co2 wpr20 pr long_text_input01">
								<div class="activitylist_team_co3_text">备注：</div>
								<span class="line_height_28">${hotelQuote.memo }</span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 报价详情 -->
			<div class="quotation_main_list_hotel_echo">
			  <c:forEach items="${conditionList }" var="condition">
			  <div class="quotation_main_list_hotel_name">
			 	 <trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${condition.hotelUuid}" />
			  </div>
			  <table class="quotation_main_list_hotel_stock">
				  <tr>
					<td width="58%" class="font_18">
						<trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${condition.islandUuid}" />
					</td>
					<td class="tr"><span class=" mr25">${condition.supplierInfo.supplierName}</span> 
					    <span class=" mr25">
							<c:choose>
								<c:when test="${condition.purchaseType==0 }">内采</c:when>
								<c:when test="${condition.purchaseType==1 }">外采</c:when>
							</c:choose>
					    </span>
						<span class=" mr25">
						   <a target="_blank" href="${ctx}/hotelPl/show/${condition.hotelPlUuid}">查看酒店价单</a>
					    </span> 
					</td>
				  </tr>
				</table>
				<div class="check_in_date_room_style">
					<ul>
						<li><span>去程交通：</span><em><trekiz:defineDict name="island_way" type="islands_way" defaultValue="${condition.departureIslandWay }" readonly="true" /></em></li>
						<li><span>返程交通：</span><em><trekiz:defineDict name="island_way" type="islands_way" defaultValue="${condition.arrivalIslandWay }" readonly="true" /></em></li>
						<c:forEach items="${condition.conditionDetailPersonNum }" var="personNum">
							<li><span><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${personNum.travelerType}" />数：</span><em>${personNum.personNum}</em></li>
						</c:forEach>
						<li><span>间数：</span><em>${condition.roomNum}间</em></li>
						<li><span>混住次数：</span><em>${condition.mixliveNum}次</em></li>
					</ul>
				</div>
				<table id="background_color_white"	class="activitylist_bodyer_table_new mar_topnone10">
					<thead>
						<tr>
						   <th class="tc" width="10%">入住日期</th>
                           <th class="tc" width="15%">房型（容住率）</th>
                           <th class="tc" width="5%"> <p>餐型</p></th>
                           <c:forEach items="${condition.relMap}" var="map" end="0">
                              <c:set var="count"  value="${fn:length(map.value)}"/>
                           </c:forEach>
							<c:forEach items="${condition.hotelQuoteResult}" var="hotelQuoteResult">
								<c:if test="${hotelQuoteResult.typeUuid!=null && hotelQuoteResult.typeUuid!=''&& hotelQuoteResult.priceType=='1' }">
									<th class="tc" width="${65/count}%">
										<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${hotelQuoteResult.typeUuid}" />/人
									</th>
								</c:if>
								<c:if test="${hotelQuoteResult.typeUuid!=null && hotelQuoteResult.typeUuid!=''&& hotelQuoteResult.priceType=='2' }">
									<th class="tc" width="${65/count}%">
										<trekiz:autoId2Name4Table tableName="hotel_guest_type" sourceColumnName="uuid" srcColumnName="name" value="${hotelQuoteResult.typeUuid}" />/人
									</th>
								</c:if>
							</c:forEach>
							<th class="tc" width="5%">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="dMap" items="${condition.relMap}">
							<tr name="quotedPriceDetail">
								<td class="tc"><p>${fn:substring(fn:split(dMap.key,";")[0],0,10) }</p></td>
								<td class="tc" title="<trekiz:autoId2Name4Class
										classzName="HotelRoomOccuRate" sourceProName="hotelRoomUuid"
										srcProName="remark" value="${fn:split(dMap.key,';')[1] }" regex="、"/>">
								<trekiz:autoId2Name4Class
										classzName="HotelRoom" sourceProName="uuid"
										srcProName="roomName" value="${fn:split(dMap.key,';')[1] }" />
										(<trekiz:autoId2Name4Class
										classzName="HotelRoom" sourceProName="uuid"
										srcProName="occupancyRate" value="${fn:split(dMap.key,';')[1] }" />)
								</td>
								<td class="tc"><p>
										<trekiz:autoId2Name4Class classzName="HotelMeal"
											sourceProName="uuid" srcProName="mealName"
											value="${fn:split(dMap.key,';')[2] }" />
									</p></td>
									<c:forEach items="${dMap.value}" var="detailValue">
										<c:if test="${detailValue.priceType==1||detailValue.priceType==2}">
											<td class="tr"><p>￥${detailValue.price }</p></td>
										</c:if>
									</c:forEach>
								<td class="tc">
									<div class="pr" style="height:100%;width:100%;line-height:28px;">
										<a name="copy" href="#">复制</a>
									</div>
								</td>
							</tr>
						</c:forEach>
						  <tr name="nonePreferential">
								<td colspan="3"><span class="tb_th_title_1 fbold">优惠前合计</span>
									<span class="fr"> <span>混住费用：</span>
									<c:forEach items="${condition.hotelQuoteResult}" var="hotelResult">
									<c:if test="${hotelResult.priceType==3}">
										<span class="padr10 fbold">￥${hotelResult.price }</span>
									</c:if>
									</c:forEach>
									</span>
								</td>
								<c:forEach items="${condition.hotelQuoteResult}" var="hotelResult">
									<c:if test="${hotelResult.priceType==1||hotelResult.priceType==2}">
										<td class="tr quotation_main_list_hotel_name_f14"><p>￥${hotelResult.price}</p></td>
									</c:if>
								</c:forEach>
						    <td class="tc">
								<div class="pr" style="height:100%;width:100%;line-height:28px;">
									<a name="copy" href="#">复制</a>
								</div>
							</td>
						   </tr>
						<c:if test="${condition.sort==-1}">
							<tr name="preferential">
									<td colspan="3"><span class="tb_th_title_1 fbold">优惠后合计</span>
										<span> 
											<span class="tdgreen"> 已使用优惠： </span> 
											<span><a id="usedPreferential" preferential-uuid="${condition.uuid}" href="javascript:;">优惠详情</a></span>
										</span> 
										<span class="fr">
											<span>混住费用：</span> 
											<c:forEach items="${condition.hotelQuoteResult}" var="hotelResult">
											<c:if test="${hotelResult.priceType==3}">
												<span class="padr10 fbold">￥${hotelResult.preferentialPrice}</span>
											</c:if> 
											</c:forEach>
									   </span>
									</td>
									<!-- 判断是否使用打包优惠 -->
									<c:set var="packageFlag"  value="false"/>
									 <c:forEach items="${condition.hotelQuoteResult}" var="result">
										<c:if test="${result.priceType==4}">
											<c:set var="packageFlag"  value="true"/>
										</c:if>
								    </c:forEach>
								    <c:choose>
										<c:when test="${packageFlag}">
										   <c:forEach items="${condition.hotelQuoteResult}" var="hotelResult">
												<c:if test="${hotelResult.priceType==4}">
													<td class="tr quotation_main_list_hotel_name_f14" colspan="${count}"><p>￥${hotelResult.preferentialPrice}</p></td>
												</c:if>
										   </c:forEach>
                                        </c:when>
										<c:otherwise>
										   <c:forEach items="${condition.hotelQuoteResult}" var="hotelResult">
												<c:if test="${hotelResult.priceType==1||hotelResult.priceType==2}">
													<td class="tr quotation_main_list_hotel_name_f14"><p>￥${hotelResult.preferentialPrice}</p></td>
												</c:if>
										   </c:forEach>
                                        </c:otherwise>
									</c:choose>
								<td class="tc">
									<div class="pr" style="height:100%;width:100%;line-height:28px;">
										<a name="copy" href="#">复制</a>
									</div>
								</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			  </c:forEach>
			</div>
			<div class="hotel_price_ask_out01" style="display:true;">
				<!--报价 结束-->
				<div class="release_next_add">
					<input  type="button" value="关闭"	class="btn btn-primary" onclick="window.close();"/>
				</div>
			</div>
		</div>
		<!--右侧内容结束-->
		<div class="information-discount-pop" id="information-discount-pop2">
	            <div class="information-discount-main">
	                <div class="title">
	                    <span>优惠前原价:</span>
	                    <span id="nonepreferentialPrice"></span>
	                </div>
	                <div class="information-discount-projectgroup" style="height: auto">
	                </div>
	            </div>
	     </div>
    <script type="text/javascript">
        $(document).ready(function(){
        	currentPreferential =$.parseJSON($("#jsondate").val()) ;
            $(document).on('click','#usedPreferential',function(){
            	var preferentialUuid= $(this).attr('preferential-uuid');
                showUsedPreferential(currentPreferential[preferentialUuid]);
            });
            $('.quotation_main_list_hotel_echo').find('[name="copy"]').each(function(){
                var $a = $(this);
                $a.zclip( {
                    copy:function(){return copy($a);} ,
                    path:"${ctxStatic}/jquery.zclip/ZeroClipboard.swf"
                });
            })
        });
        function copy($el){
            var copyType= $el.parents('tr:first').attr('name');
            var text =""
            switch (copyType){
                case'quotedPriceDetail':
                    text= getQuotedPriceDtlCopyText($el);
                    break;
                case'nonePreferential':
                    text= getQuotedPriceCopyText();
                    break;
                case'preferential':
                    text= getPreferentialCopyText();
                    break;
            }
            return text;
        }
        function getQuotedPriceCopyText(){
            var quotedPriceJson = $.parseJSON($("#resultCopy").val());
            var checkinDate = quotedPriceJson.beginDate;
            var endDate = quotedPriceJson.endDate;
            var islandText = quotedPriceJson.islandText;

            var roomDayText = "";
            for (var i in quotedPriceJson.roomNights) {
                roomDayText += quotedPriceJson.roomNights[i].roomText + "*" +quotedPriceJson.roomNights[i].night + "晚,";
            }
            roomDayText = roomDayText.substr(0, roomDayText.length - 1);

            var mealText =quotedPriceJson.meals;

            var islandWayText=quotedPriceJson.islandWays;

            var guestPriceListText="";
            for(var i in quotedPriceJson.totalPrice.guestPrices){
                var guestPrice = quotedPriceJson.totalPrice.guestPrices[i];
                var guestPriceText = guestPrice.travelerTypeText+'/人合计：'+guestPrice.currencyText+guestPrice.amount;
                guestPriceListText+=guestPriceText+'\t    '
            }
            //guestPriceListText+='第三人成人/人合计：'+quotedPriceJson.totalPrice.thirdPriceCurrencyText+quotedPriceJson.totalPrice.thirdPrice+'\t';
            guestPriceListText+='混住费合计：'+quotedPriceJson.totalPrice.mixlivePriceCurrencyText+quotedPriceJson.totalPrice.mixlivePrice;
            return checkinDate+'~'+endDate+'\t    '+islandText+'\t    '+roomDayText+'\t    '+mealText+'\t    '+islandWayText+'\t    '+guestPriceListText;
        }
        function getQuotedPriceDtlCopyText($el){
            var $tr=$el.parents('tr:first');
            var quotedPriceJson = $.parseJSON($("#resultCopy").val());
            var quotedPriceJsonDtl;
            var index =$tr.parent().find('[name="quotedPriceDetail"]').index($tr);
            quotedPriceJsonDtl=quotedPriceJson.conditionDetails[index];
            var inDate =quotedPriceJsonDtl.inDate;
            var islandText = quotedPriceJson.islandText;
            var roomDayText=quotedPriceJsonDtl.roomType;
            var mealText =quotedPriceJsonDtl.mealType;

            var islandWayText=quotedPriceJsonDtl.islandWay;

            var guestPriceListText="";
            for(var i in quotedPriceJsonDtl.guestPrices){
                var guestPrice = quotedPriceJsonDtl.guestPrices[i];
                var guestPriceText = guestPrice.travelerTypeText+'/人：'+guestPrice.currencyText+guestPrice.amount;
                guestPriceListText+=guestPriceText+'\t    '
            }
            //guestPriceListText+='第三人成人/人：'+quotedPriceJsonDtl.thirdPriceCurrencyText+quotedPriceJsonDtl.thirdPrice;
            return inDate+'\t    '+islandText+'\t    '+roomDayText+'\t    '+mealText+'\t    '+islandWayText+'\t    '+guestPriceListText;
        }
        function getPreferentialCopyText(){
            var quotedPriceJson = $.parseJSON($("#resultCopy").val());
            var preferential = quotedPriceJson.preferentialTotalPrice;
            var checkinDate = quotedPriceJson.beginDate;
            var endDate = quotedPriceJson.endDate;
            var islandText = quotedPriceJson.islandText;

            var roomDayText = "";
            for (var i in quotedPriceJson.roomNights) {
                roomDayText += quotedPriceJson.roomNights[i].roomText + "*" +quotedPriceJson.roomNights[i].night + "晚,";
            }
            roomDayText = roomDayText.substr(0, roomDayText.length - 1);

            var mealText =quotedPriceJson.meals;

            var islandWayText=quotedPriceJson.islandWays;

            var priceListText = "";
            if(isNaN(preferential.totalPrice)) {
                for (var i in preferential.guestPrices) {
                    var guestPrice = preferential.guestPrices[i];
                    var guestPriceText = guestPrice.travelerTypeText + '/人合计：' + guestPrice.currencyText + guestPrice.amount;
                    priceListText += guestPriceText + '\t    '
                }
                //priceListText += '第三人成人/人合计：' + preferential.thirdPriceCurrencyText + preferential.thirdPrice + '\t    ';
                priceListText += '混住费合计：' + preferential.mixlivePriceCurrencyText + preferential.mixlivePrice;
            }
            else{
                priceListText='打包优惠合计：'+preferential.totalPriceCurrencyText+preferential.totalPrice+'/间';
            }
            return  checkinDate+'~'+endDate+'\t    '+islandText+'\t    '+roomDayText+'\t    '+mealText+'\t    '+islandWayText+'\t    '+priceListText;
        }
        function showUsedPreferential(preferentialGroup) {
               var quotedPriceJson =  $.parseJSON($("#resultCopy").val());
               var $popTemplate = $('#information-discount-pop2').clone().removeClass('information-discount-pop');
               //$popTemplate.find('.information-discount-btnouter').remove();
               var $nonepreferentialPrice = $popTemplate.find('#nonepreferentialPrice');

               for(var i in quotedPriceJson.totalPrice.guestPrices ){
                   var guestPrice = quotedPriceJson.totalPrice.guestPrices[i];
                   $nonepreferentialPrice.append(' <span>'+guestPrice.travelerTypeText+'/人 '+guestPrice.currencyText+guestPrice.amount+'</span>');
               }
               //$nonepreferentialPrice.append(' <span>第三人成人/人 '+quotedPriceJson.totalPrice.thirdPriceCurrencyText+quotedPriceJson.totalPrice.thirdPrice+'</span>');
               $nonepreferentialPrice.append(' <span>混住费用/人 '+quotedPriceJson.totalPrice.mixlivePriceCurrencyText+quotedPriceJson.totalPrice.mixlivePrice+'</span>');

               var $preferentialTable=$('<table width="890" align="center" cellspacing="3" class="information-discount-otherprojecttable"></table>');
               $preferentialTable.data('preferential',preferentialGroup);
               $popTemplate.find('.information-discount-projectgroup').append($preferentialTable);
               var preferentialCount=preferentialGroup.preferentialList.length;
               for(var j in preferentialGroup.preferentialList){
                   var preferential = preferentialGroup.preferentialList[j];
                   var $tr = $('<tr></tr>');
                   $tr.append('' +
                           '<td class="column-left tl" width="180">' +
                           preferential.preferentialName+'<br/> 下单代码：'+
                           preferential.bookingCode +
                           '</td>'
                   );
                   $tr.append('' +
                           '<td class="column  tl" width="320">' +
                           (preferential.description===undefined?'':preferential.description)+
                           '</td>'
                   );
                   if(j==0){
                       var preferentialAmountHtml="";
                       if(isNaN(preferentialGroup.totalPrice)) {
                           for (var k in preferentialGroup.guestPriceList) {
                               var guestPrice = preferentialGroup.guestPriceList[k];
                               preferentialAmountHtml += '<p>' + guestPrice.travelerTypeText + '/人优惠' + guestPrice.currencyText +'-'+guestPrice.preferAmount + '<br/>' +
                               '优惠后价格：<span class="information-discount-price">' + guestPrice.currencyText + guestPrice.amount + '</span></p>';
                           }
                          // preferentialAmountHtml += '<p>第三人成人/人优惠' + preferentialGroup.thirdPersonPriceCurrencyText + preferentialGroup.thirdPersonPreferAmount + '<br/>' +
                          // '优惠后价格：<span class="information-discount-price">' + preferentialGroup.thirdPersonPriceCurrencyText + preferentialGroup.thirdPersonPrice + '</span></p>';
                           preferentialAmountHtml += '<p>混住费用优惠后价格：<span class="information-discount-price">' + preferentialGroup.mixlivePriceCurrencyText + preferentialGroup.mixlivePrice + '</span></p>';
                       }else{
                           preferentialAmountHtml='<p>优惠后价格：<span class="information-discount-price">' + preferentialGroup.totalPriceCurrencyText + preferentialGroup.totalPrice + '/间</span></p>';
                       }
                       $tr.append('<td class="column  tl" width="200" rowspan="'+preferentialCount+'">'+preferentialAmountHtml+'</td>>');
                   }
                   $preferentialTable.append($tr);
               }
               $popTemplate.find('.information-discount-projectgroup').css({'height':'500px'});
               $popTemplate.find('.information-discount-main').css({'box-shadow':'none'});
               var $pop = $.jBox("<div name='PopUsePreferential'></div>",{
                   title:"优惠信息",
                   buttons:{  },
                   width:950
               });
               $pop.find('[name="PopUsePreferential"]').append($popTemplate);
           }
    </script>
</body>
</html>
